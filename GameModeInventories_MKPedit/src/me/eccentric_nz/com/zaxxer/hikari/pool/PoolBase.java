package me.eccentric_nz.com.zaxxer.hikari.pool;

import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import me.eccentric_nz.com.zaxxer.hikari.HikariConfig;
import me.eccentric_nz.com.zaxxer.hikari.metrics.IMetricsTracker;
import me.eccentric_nz.com.zaxxer.hikari.util.ClockSource;
import me.eccentric_nz.com.zaxxer.hikari.util.DriverDataSource;
import me.eccentric_nz.com.zaxxer.hikari.util.PropertyElf;
import me.eccentric_nz.com.zaxxer.hikari.util.UtilityElf;
import me.eccentric_nz.org.slf4j.Logger;
import me.eccentric_nz.org.slf4j.LoggerFactory;

abstract class PoolBase {
   private final Logger LOGGER = LoggerFactory.getLogger(PoolBase.class);
   protected final HikariConfig config;
   protected final String poolName;
   long connectionTimeout;
   long validationTimeout;
   PoolBase.IMetricsTrackerDelegate metricsTracker;
   private static final String[] RESET_STATES = new String[]{"readOnly", "autoCommit", "isolation", "catalog", "netTimeout"};
   private static final int UNINITIALIZED = -1;
   private static final int TRUE = 1;
   private static final int FALSE = 0;
   private int networkTimeout;
   private int isNetworkTimeoutSupported;
   private int isQueryTimeoutSupported;
   private int defaultTransactionIsolation;
   private int transactionIsolation;
   private Executor netTimeoutExecutor;
   private DataSource dataSource;
   private final String catalog;
   private final boolean isReadOnly;
   private final boolean isAutoCommit;
   private final boolean isUseJdbc4Validation;
   private final boolean isIsolateInternalQueries;
   private final AtomicReference<Throwable> lastConnectionFailure;
   private volatile boolean isValidChecked;

   PoolBase(HikariConfig config) {
      this.config = config;
      this.networkTimeout = -1;
      this.catalog = config.getCatalog();
      this.isReadOnly = config.isReadOnly();
      this.isAutoCommit = config.isAutoCommit();
      this.transactionIsolation = UtilityElf.getTransactionIsolation(config.getTransactionIsolation());
      this.isQueryTimeoutSupported = -1;
      this.isNetworkTimeoutSupported = -1;
      this.isUseJdbc4Validation = config.getConnectionTestQuery() == null;
      this.isIsolateInternalQueries = config.isIsolateInternalQueries();
      this.poolName = config.getPoolName();
      this.connectionTimeout = config.getConnectionTimeout();
      this.validationTimeout = config.getValidationTimeout();
      this.lastConnectionFailure = new AtomicReference();
      this.initializeDataSource();
   }

   public String toString() {
      return this.poolName;
   }

   abstract void recycle(PoolEntry var1);

   void quietlyCloseConnection(Connection connection, String closureReason) {
      if (connection != null) {
         try {
            this.LOGGER.debug("{} - Closing connection {}: {}", this.poolName, connection, closureReason);

            try {
               this.setNetworkTimeout(connection, TimeUnit.SECONDS.toMillis(15L));
            } finally {
               connection.close();
            }
         } catch (Throwable var7) {
            this.LOGGER.debug("{} - Closing connection {} failed", this.poolName, connection, var7);
         }
      }

   }

   boolean isConnectionAlive(Connection connection) {
      try {
         boolean var4;
         try {
            this.setNetworkTimeout(connection, this.validationTimeout);
            long validationSeconds = (long)((int)Math.max(1000L, this.validationTimeout) / 1000);
            if (!this.isUseJdbc4Validation) {
               Statement statement = connection.createStatement();
               Throwable var5 = null;

               try {
                  if (this.isNetworkTimeoutSupported != 1) {
                     this.setQueryTimeout(statement, (int)validationSeconds);
                  }

                  statement.execute(this.config.getConnectionTestQuery());
                  return true;
               } catch (Throwable var23) {
                  var5 = var23;
                  throw var23;
               } finally {
                  if (statement != null) {
                     if (var5 != null) {
                        try {
                           statement.close();
                        } catch (Throwable var22) {
                           var5.addSuppressed(var22);
                        }
                     } else {
                        statement.close();
                     }
                  }

               }
            }

            var4 = connection.isValid((int)validationSeconds);
         } finally {
            this.setNetworkTimeout(connection, (long)this.networkTimeout);
            if (this.isIsolateInternalQueries && !this.isAutoCommit) {
               connection.rollback();
            }

         }

         return var4;
      } catch (Exception var26) {
         this.lastConnectionFailure.set(var26);
         this.LOGGER.warn("{} - Failed to validate connection {} ({})", this.poolName, connection, var26.getMessage());
         return false;
      }
   }

   Throwable getLastConnectionFailure() {
      return (Throwable)this.lastConnectionFailure.get();
   }

   public DataSource getUnwrappedDataSource() {
      return this.dataSource;
   }

   PoolEntry newPoolEntry() throws Exception {
      return new PoolEntry(this.newConnection(), this, this.isReadOnly, this.isAutoCommit);
   }

   void resetConnectionState(Connection connection, ProxyConnection proxyConnection, int dirtyBits) throws SQLException {
      int resetBits = 0;
      if ((dirtyBits & 1) != 0 && proxyConnection.getReadOnlyState() != this.isReadOnly) {
         connection.setReadOnly(this.isReadOnly);
         resetBits |= 1;
      }

      if ((dirtyBits & 2) != 0 && proxyConnection.getAutoCommitState() != this.isAutoCommit) {
         connection.setAutoCommit(this.isAutoCommit);
         resetBits |= 2;
      }

      if ((dirtyBits & 4) != 0 && proxyConnection.getTransactionIsolationState() != this.transactionIsolation) {
         connection.setTransactionIsolation(this.transactionIsolation);
         resetBits |= 4;
      }

      if ((dirtyBits & 8) != 0 && this.catalog != null && !this.catalog.equals(proxyConnection.getCatalogState())) {
         connection.setCatalog(this.catalog);
         resetBits |= 8;
      }

      if ((dirtyBits & 16) != 0 && proxyConnection.getNetworkTimeoutState() != this.networkTimeout) {
         this.setNetworkTimeout(connection, (long)this.networkTimeout);
         resetBits |= 16;
      }

      if (resetBits != 0 && this.LOGGER.isDebugEnabled()) {
         this.LOGGER.debug("{} - Reset ({}) on connection {}", this.poolName, this.stringFromResetBits(resetBits), connection);
      }

   }

   void shutdownNetworkTimeoutExecutor() {
      if (this.netTimeoutExecutor instanceof ThreadPoolExecutor) {
         ((ThreadPoolExecutor)this.netTimeoutExecutor).shutdownNow();
      }

   }

   void registerMBeans(HikariPool hikariPool) {
      if (this.config.isRegisterMbeans()) {
         try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName beanConfigName = new ObjectName("me.eccentric_nz.com.zaxxer.hikari:type=PoolConfig (" + this.poolName + ")");
            ObjectName beanPoolName = new ObjectName("me.eccentric_nz.com.zaxxer.hikari:type=Pool (" + this.poolName + ")");
            if (!mBeanServer.isRegistered(beanConfigName)) {
               mBeanServer.registerMBean(this.config, beanConfigName);
               mBeanServer.registerMBean(hikariPool, beanPoolName);
            } else {
               this.LOGGER.error((String)"{} - JMX name ({}) is already registered.", (Object)this.poolName, (Object)this.poolName);
            }
         } catch (Exception var5) {
            this.LOGGER.warn((String)"{} - Failed to register management beans.", (Object)this.poolName, (Object)var5);
         }

      }
   }

   void unregisterMBeans() {
      if (this.config.isRegisterMbeans()) {
         try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName beanConfigName = new ObjectName("me.eccentric_nz.com.zaxxer.hikari:type=PoolConfig (" + this.poolName + ")");
            ObjectName beanPoolName = new ObjectName("me.eccentric_nz.com.zaxxer.hikari:type=Pool (" + this.poolName + ")");
            if (mBeanServer.isRegistered(beanConfigName)) {
               mBeanServer.unregisterMBean(beanConfigName);
               mBeanServer.unregisterMBean(beanPoolName);
            }
         } catch (Exception var4) {
            this.LOGGER.warn((String)"{} - Failed to unregister management beans.", (Object)this.poolName, (Object)var4);
         }

      }
   }

   private void initializeDataSource() {
      String jdbcUrl = this.config.getJdbcUrl();
      String username = this.config.getUsername();
      String password = this.config.getPassword();
      String dsClassName = this.config.getDataSourceClassName();
      String driverClassName = this.config.getDriverClassName();
      String dataSourceJNDI = this.config.getDataSourceJNDI();
      Properties dataSourceProperties = this.config.getDataSourceProperties();
      DataSource dataSource = this.config.getDataSource();
      if (dsClassName != null && dataSource == null) {
         dataSource = (DataSource)UtilityElf.createInstance(dsClassName, DataSource.class);
         PropertyElf.setTargetFromProperties(dataSource, dataSourceProperties);
      } else if (jdbcUrl != null && dataSource == null) {
         dataSource = new DriverDataSource(jdbcUrl, driverClassName, dataSourceProperties, username, password);
      } else if (dataSourceJNDI != null && dataSource == null) {
         try {
            InitialContext ic = new InitialContext();
            dataSource = (DataSource)ic.lookup(dataSourceJNDI);
         } catch (NamingException var10) {
            throw new HikariPool.PoolInitializationException(var10);
         }
      }

      if (dataSource != null) {
         this.setLoginTimeout((DataSource)dataSource);
         this.createNetworkTimeoutExecutor((DataSource)dataSource, dsClassName, jdbcUrl);
      }

      this.dataSource = (DataSource)dataSource;
   }

   private Connection newConnection() throws Exception {
      long start = ClockSource.currentTime();
      Connection connection = null;

      Connection var6;
      try {
         String username = this.config.getUsername();
         String password = this.config.getPassword();
         connection = username == null ? this.dataSource.getConnection() : this.dataSource.getConnection(username, password);
         if (connection == null) {
            throw new SQLTransientConnectionException("DataSource returned null unexpectedly");
         }

         this.setupConnection(connection);
         this.lastConnectionFailure.set((Object)null);
         var6 = connection;
      } catch (Exception var10) {
         if (connection != null) {
            this.quietlyCloseConnection(connection, "(Failed to create/setup connection)");
         } else if (this.getLastConnectionFailure() == null) {
            this.LOGGER.debug((String)"{} - Failed to create/setup connection: {}", (Object)this.poolName, (Object)var10.getMessage());
         }

         this.lastConnectionFailure.set(var10);
         throw var10;
      } finally {
         if (this.metricsTracker != null) {
            this.metricsTracker.recordConnectionCreated(ClockSource.elapsedMillis(start));
         }

      }

      return var6;
   }

   private void setupConnection(Connection connection) throws PoolBase.ConnectionSetupException {
      try {
         if (this.networkTimeout == -1) {
            this.networkTimeout = this.getAndSetNetworkTimeout(connection, this.validationTimeout);
         } else {
            this.setNetworkTimeout(connection, this.validationTimeout);
         }

         connection.setReadOnly(this.isReadOnly);
         connection.setAutoCommit(this.isAutoCommit);
         this.checkDriverSupport(connection);
         if (this.transactionIsolation != this.defaultTransactionIsolation) {
            connection.setTransactionIsolation(this.transactionIsolation);
         }

         if (this.catalog != null) {
            connection.setCatalog(this.catalog);
         }

         this.executeSql(connection, this.config.getConnectionInitSql(), true);
         this.setNetworkTimeout(connection, (long)this.networkTimeout);
      } catch (SQLException var3) {
         throw new PoolBase.ConnectionSetupException(var3);
      }
   }

   private void checkDriverSupport(Connection connection) throws SQLException {
      if (!this.isValidChecked) {
         try {
            if (this.isUseJdbc4Validation) {
               connection.isValid(1);
            } else {
               this.executeSql(connection, this.config.getConnectionTestQuery(), false);
            }
         } catch (Throwable var3) {
            this.LOGGER.error((String)("{} - Failed to execute" + (this.isUseJdbc4Validation ? " isValid() for connection, configure" : "") + " connection test query ({})."), (Object)this.poolName, (Object)var3.getMessage());
            throw var3;
         }

         try {
            this.defaultTransactionIsolation = connection.getTransactionIsolation();
            if (this.transactionIsolation == -1) {
               this.transactionIsolation = this.defaultTransactionIsolation;
            }
         } catch (SQLException var4) {
            this.LOGGER.warn((String)"{} - Default transaction isolation level detection failed ({}).", (Object)this.poolName, (Object)var4.getMessage());
            if (var4.getSQLState() != null && !var4.getSQLState().startsWith("08")) {
               throw var4;
            }
         }

         this.isValidChecked = true;
      }

   }

   private void setQueryTimeout(Statement statement, int timeoutSec) {
      if (this.isQueryTimeoutSupported != 0) {
         try {
            statement.setQueryTimeout(timeoutSec);
            this.isQueryTimeoutSupported = 1;
         } catch (Throwable var4) {
            if (this.isQueryTimeoutSupported == -1) {
               this.isQueryTimeoutSupported = 0;
               this.LOGGER.info((String)"{} - Failed to set query timeout for statement. ({})", (Object)this.poolName, (Object)var4.getMessage());
            }
         }
      }

   }

   private int getAndSetNetworkTimeout(Connection connection, long timeoutMs) {
      if (this.isNetworkTimeoutSupported != 0) {
         try {
            int originalTimeout = connection.getNetworkTimeout();
            connection.setNetworkTimeout(this.netTimeoutExecutor, (int)timeoutMs);
            this.isNetworkTimeoutSupported = 1;
            return originalTimeout;
         } catch (Throwable var5) {
            if (this.isNetworkTimeoutSupported == -1) {
               this.isNetworkTimeoutSupported = 0;
               this.LOGGER.info((String)"{} - Driver does not support get/set network timeout for connections. ({})", (Object)this.poolName, (Object)var5.getMessage());
               if (this.validationTimeout < TimeUnit.SECONDS.toMillis(1L)) {
                  this.LOGGER.warn((String)"{} - A validationTimeout of less than 1 second cannot be honored on drivers without setNetworkTimeout() support.", (Object)this.poolName);
               } else if (this.validationTimeout % TimeUnit.SECONDS.toMillis(1L) != 0L) {
                  this.LOGGER.warn((String)"{} - A validationTimeout with fractional second granularity cannot be honored on drivers without setNetworkTimeout() support.", (Object)this.poolName);
               }
            }
         }
      }

      return 0;
   }

   private void setNetworkTimeout(Connection connection, long timeoutMs) throws SQLException {
      if (this.isNetworkTimeoutSupported == 1) {
         connection.setNetworkTimeout(this.netTimeoutExecutor, (int)timeoutMs);
      }

   }

   private void executeSql(Connection connection, String sql, boolean isCommit) throws SQLException {
      if (sql != null) {
         Statement statement = connection.createStatement();
         Throwable var5 = null;

         try {
            statement.execute(sql);
         } catch (Throwable var14) {
            var5 = var14;
            throw var14;
         } finally {
            if (statement != null) {
               if (var5 != null) {
                  try {
                     statement.close();
                  } catch (Throwable var13) {
                     var5.addSuppressed(var13);
                  }
               } else {
                  statement.close();
               }
            }

         }

         if (this.isIsolateInternalQueries && !this.isAutoCommit) {
            if (isCommit) {
               connection.commit();
            } else {
               connection.rollback();
            }
         }
      }

   }

   private void createNetworkTimeoutExecutor(DataSource dataSource, String dsClassName, String jdbcUrl) {
      if (dsClassName != null && dsClassName.contains("Mysql") || jdbcUrl != null && jdbcUrl.contains("mysql") || dataSource != null && dataSource.getClass().getName().contains("Mysql")) {
         this.netTimeoutExecutor = new PoolBase.SynchronousExecutor();
      } else {
         ThreadFactory threadFactory = this.config.getThreadFactory();
         ThreadFactory threadFactory = threadFactory != null ? threadFactory : new UtilityElf.DefaultThreadFactory(this.poolName + " network timeout executor", true);
         ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool((ThreadFactory)threadFactory);
         executor.setKeepAliveTime(15L, TimeUnit.SECONDS);
         executor.allowCoreThreadTimeOut(true);
         this.netTimeoutExecutor = executor;
      }

   }

   private void setLoginTimeout(DataSource dataSource) {
      if (this.connectionTimeout != 2147483647L) {
         try {
            dataSource.setLoginTimeout(Math.max(1, (int)TimeUnit.MILLISECONDS.toSeconds(500L + this.connectionTimeout)));
         } catch (Throwable var3) {
            this.LOGGER.info((String)"{} - Failed to set login timeout for data source. ({})", (Object)this.poolName, (Object)var3.getMessage());
         }
      }

   }

   private String stringFromResetBits(int bits) {
      StringBuilder sb = new StringBuilder();

      for(int ndx = 0; ndx < RESET_STATES.length; ++ndx) {
         if ((bits & 1 << ndx) != 0) {
            sb.append(RESET_STATES[ndx]).append(", ");
         }
      }

      sb.setLength(sb.length() - 2);
      return sb.toString();
   }

   static final class NopMetricsTrackerDelegate implements PoolBase.IMetricsTrackerDelegate {
   }

   static class MetricsTrackerDelegate implements PoolBase.IMetricsTrackerDelegate {
      final IMetricsTracker tracker;

      MetricsTrackerDelegate(IMetricsTracker tracker) {
         this.tracker = tracker;
      }

      public void recordConnectionUsage(PoolEntry poolEntry) {
         this.tracker.recordConnectionUsageMillis(poolEntry.getMillisSinceBorrowed());
      }

      public void recordConnectionCreated(long connectionCreatedMillis) {
         this.tracker.recordConnectionCreatedMillis(connectionCreatedMillis);
      }

      public void recordBorrowTimeoutStats(long startTime) {
         this.tracker.recordConnectionAcquiredNanos(ClockSource.elapsedNanos(startTime));
      }

      public void recordBorrowStats(PoolEntry poolEntry, long startTime) {
         long now = ClockSource.currentTime();
         poolEntry.lastBorrowed = now;
         this.tracker.recordConnectionAcquiredNanos(ClockSource.elapsedNanos(startTime, now));
      }

      public void recordConnectionTimeout() {
         this.tracker.recordConnectionTimeout();
      }

      public void close() {
         this.tracker.close();
      }
   }

   interface IMetricsTrackerDelegate extends AutoCloseable {
      default void recordConnectionUsage(PoolEntry poolEntry) {
      }

      default void recordConnectionCreated(long connectionCreatedMillis) {
      }

      default void recordBorrowTimeoutStats(long startTime) {
      }

      default void recordBorrowStats(PoolEntry poolEntry, long startTime) {
      }

      default void recordConnectionTimeout() {
      }

      default void close() {
      }
   }

   private static class SynchronousExecutor implements Executor {
      private SynchronousExecutor() {
      }

      public void execute(Runnable command) {
         try {
            command.run();
         } catch (Throwable var3) {
            LoggerFactory.getLogger(PoolBase.class).debug((String)"Failed to execute: {}", (Object)command, (Object)var3);
         }

      }

      // $FF: synthetic method
      SynchronousExecutor(Object x0) {
         this();
      }
   }

   static class ConnectionSetupException extends Exception {
      private static final long serialVersionUID = 929872118275916521L;

      ConnectionSetupException(Throwable t) {
         super(t);
      }
   }
}

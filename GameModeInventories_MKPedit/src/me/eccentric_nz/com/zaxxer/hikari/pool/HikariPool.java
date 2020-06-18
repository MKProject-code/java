package me.eccentric_nz.com.zaxxer.hikari.pool;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import me.eccentric_nz.com.zaxxer.hikari.HikariConfig;
import me.eccentric_nz.com.zaxxer.hikari.HikariPoolMXBean;
import me.eccentric_nz.com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import me.eccentric_nz.com.zaxxer.hikari.metrics.PoolStats;
import me.eccentric_nz.com.zaxxer.hikari.metrics.dropwizard.CodahaleHealthChecker;
import me.eccentric_nz.com.zaxxer.hikari.metrics.dropwizard.CodahaleMetricsTrackerFactory;
import me.eccentric_nz.com.zaxxer.hikari.util.ClockSource;
import me.eccentric_nz.com.zaxxer.hikari.util.ConcurrentBag;
import me.eccentric_nz.com.zaxxer.hikari.util.SuspendResumeLock;
import me.eccentric_nz.com.zaxxer.hikari.util.UtilityElf;
import me.eccentric_nz.org.slf4j.Logger;
import me.eccentric_nz.org.slf4j.LoggerFactory;

public final class HikariPool extends PoolBase implements HikariPoolMXBean, ConcurrentBag.IBagStateListener {
   private final Logger LOGGER = LoggerFactory.getLogger(HikariPool.class);
   private static final int POOL_NORMAL = 0;
   private static final int POOL_SUSPENDED = 1;
   private static final int POOL_SHUTDOWN = 2;
   private volatile int poolState;
   private final long ALIVE_BYPASS_WINDOW_MS;
   private final long HOUSEKEEPING_PERIOD_MS;
   private final HikariPool.PoolEntryCreator POOL_ENTRY_CREATOR;
   private final HikariPool.PoolEntryCreator POST_FILL_POOL_ENTRY_CREATOR;
   private final Collection<Runnable> addConnectionQueue;
   private final ThreadPoolExecutor addConnectionExecutor;
   private final ThreadPoolExecutor closeConnectionExecutor;
   private final ConcurrentBag<PoolEntry> connectionBag;
   private final ProxyLeakTask leakTask;
   private final SuspendResumeLock suspendResumeLock;
   private ScheduledExecutorService houseKeepingExecutorService;
   private ScheduledFuture<?> houseKeeperTask;

   public HikariPool(HikariConfig config) {
      super(config);
      this.ALIVE_BYPASS_WINDOW_MS = Long.getLong("me.eccentric_nz.com.zaxxer.hikari.aliveBypassWindowMs", TimeUnit.MILLISECONDS.toMillis(500L));
      this.HOUSEKEEPING_PERIOD_MS = Long.getLong("me.eccentric_nz.com.zaxxer.hikari.housekeeping.periodMs", TimeUnit.SECONDS.toMillis(30L));
      this.POOL_ENTRY_CREATOR = new HikariPool.PoolEntryCreator((String)null);
      this.POST_FILL_POOL_ENTRY_CREATOR = new HikariPool.PoolEntryCreator("After adding ");
      this.connectionBag = new ConcurrentBag(this);
      this.suspendResumeLock = config.isAllowPoolSuspension() ? new SuspendResumeLock() : SuspendResumeLock.FAUX_LOCK;
      this.houseKeepingExecutorService = this.initializeHouseKeepingExecutorService();
      this.checkFailFast();
      if (config.getMetricsTrackerFactory() != null) {
         this.setMetricsTrackerFactory(config.getMetricsTrackerFactory());
      } else {
         this.setMetricRegistry(config.getMetricRegistry());
      }

      this.setHealthCheckRegistry(config.getHealthCheckRegistry());
      this.registerMBeans(this);
      ThreadFactory threadFactory = config.getThreadFactory();
      LinkedBlockingQueue<Runnable> addConnectionQueue = new LinkedBlockingQueue(config.getMaximumPoolSize());
      this.addConnectionQueue = Collections.unmodifiableCollection(addConnectionQueue);
      this.addConnectionExecutor = UtilityElf.createThreadPoolExecutor(addConnectionQueue, this.poolName + " connection adder", threadFactory, new DiscardPolicy());
      this.closeConnectionExecutor = UtilityElf.createThreadPoolExecutor(config.getMaximumPoolSize(), this.poolName + " connection closer", threadFactory, new CallerRunsPolicy());
      this.leakTask = new ProxyLeakTask(config.getLeakDetectionThreshold(), this.houseKeepingExecutorService);
      this.houseKeeperTask = this.houseKeepingExecutorService.scheduleWithFixedDelay(new HikariPool.HouseKeeper(), 100L, this.HOUSEKEEPING_PERIOD_MS, TimeUnit.MILLISECONDS);
   }

   public Connection getConnection() throws SQLException {
      return this.getConnection(this.connectionTimeout);
   }

   public Connection getConnection(long hardTimeout) throws SQLException {
      this.suspendResumeLock.acquire();
      long startTime = ClockSource.currentTime();

      try {
         long timeout = hardTimeout;
         PoolEntry poolEntry = null;

         try {
            while(true) {
               poolEntry = (PoolEntry)this.connectionBag.borrow(timeout, TimeUnit.MILLISECONDS);
               if (poolEntry != null) {
                  long now = ClockSource.currentTime();
                  if (!poolEntry.isMarkedEvicted() && (ClockSource.elapsedMillis(poolEntry.lastAccessed, now) <= this.ALIVE_BYPASS_WINDOW_MS || this.isConnectionAlive(poolEntry.connection))) {
                     this.metricsTracker.recordBorrowStats(poolEntry, startTime);
                     Connection var10 = poolEntry.createProxyConnection(this.leakTask.schedule(poolEntry), now);
                     return var10;
                  }

                  this.closeConnection(poolEntry, "(connection is evicted or dead)");
                  timeout = hardTimeout - ClockSource.elapsedMillis(startTime);
                  if (timeout > 0L) {
                     continue;
                  }
               }

               this.metricsTracker.recordBorrowTimeoutStats(startTime);
               throw this.createTimeoutException(startTime);
            }
         } catch (InterruptedException var14) {
            if (poolEntry != null) {
               poolEntry.recycle(startTime);
            }

            Thread.currentThread().interrupt();
            throw new SQLException(this.poolName + " - Interrupted during connection acquisition", var14);
         }
      } finally {
         this.suspendResumeLock.release();
      }
   }

   public synchronized void shutdown() throws InterruptedException {
      try {
         this.poolState = 2;
         if (this.addConnectionExecutor == null) {
            return;
         }

         this.logPoolState("Before shutdown ");
         if (this.houseKeeperTask != null) {
            this.houseKeeperTask.cancel(false);
            this.houseKeeperTask = null;
         }

         this.softEvictConnections();
         this.addConnectionExecutor.shutdown();
         this.addConnectionExecutor.awaitTermination(5L, TimeUnit.SECONDS);
         this.destroyHouseKeepingExecutorService();
         this.connectionBag.close();
         ThreadPoolExecutor assassinExecutor = UtilityElf.createThreadPoolExecutor(this.config.getMaximumPoolSize(), this.poolName + " connection assassinator", this.config.getThreadFactory(), new CallerRunsPolicy());

         try {
            long start = ClockSource.currentTime();

            do {
               this.abortActiveConnections(assassinExecutor);
               this.softEvictConnections();
            } while(this.getTotalConnections() > 0 && ClockSource.elapsedMillis(start) < TimeUnit.SECONDS.toMillis(5L));
         } finally {
            assassinExecutor.shutdown();
            assassinExecutor.awaitTermination(5L, TimeUnit.SECONDS);
         }

         this.shutdownNetworkTimeoutExecutor();
         this.closeConnectionExecutor.shutdown();
         this.closeConnectionExecutor.awaitTermination(5L, TimeUnit.SECONDS);
      } finally {
         this.logPoolState("After shutdown ");
         this.unregisterMBeans();
         this.metricsTracker.close();
      }

   }

   public void evictConnection(Connection connection) {
      ProxyConnection proxyConnection = (ProxyConnection)connection;
      proxyConnection.cancelLeakTask();

      try {
         this.softEvictConnection(proxyConnection.getPoolEntry(), "(connection evicted by user)", !connection.isClosed());
      } catch (SQLException var4) {
      }

   }

   public void setMetricRegistry(Object metricRegistry) {
      if (metricRegistry != null) {
         this.setMetricsTrackerFactory(new CodahaleMetricsTrackerFactory((MetricRegistry)metricRegistry));
      } else {
         this.setMetricsTrackerFactory((MetricsTrackerFactory)null);
      }

   }

   public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
      if (metricsTrackerFactory != null) {
         this.metricsTracker = new PoolBase.MetricsTrackerDelegate(metricsTrackerFactory.create(this.config.getPoolName(), this.getPoolStats()));
      } else {
         this.metricsTracker = new PoolBase.NopMetricsTrackerDelegate();
      }

   }

   public void setHealthCheckRegistry(Object healthCheckRegistry) {
      if (healthCheckRegistry != null) {
         CodahaleHealthChecker.registerHealthChecks(this, this.config, (HealthCheckRegistry)healthCheckRegistry);
      }

   }

   public void addBagItem(int waiting) {
      boolean shouldAdd = waiting - this.addConnectionQueue.size() >= 0;
      if (shouldAdd) {
         this.addConnectionExecutor.submit(this.POOL_ENTRY_CREATOR);
      }

      CompletableFuture.completedFuture(Boolean.TRUE);
   }

   public int getActiveConnections() {
      return this.connectionBag.getCount(1);
   }

   public int getIdleConnections() {
      return this.connectionBag.getCount(0);
   }

   public int getTotalConnections() {
      return this.connectionBag.size();
   }

   public int getThreadsAwaitingConnection() {
      return this.connectionBag.getWaitingThreadCount();
   }

   public void softEvictConnections() {
      this.connectionBag.values().forEach((poolEntry) -> {
         this.softEvictConnection(poolEntry, "(connection evicted)", false);
      });
   }

   public synchronized void suspendPool() {
      if (this.suspendResumeLock == SuspendResumeLock.FAUX_LOCK) {
         throw new IllegalStateException(this.poolName + " - is not suspendable");
      } else {
         if (this.poolState != 1) {
            this.suspendResumeLock.suspend();
            this.poolState = 1;
         }

      }
   }

   public synchronized void resumePool() {
      if (this.poolState == 1) {
         this.poolState = 0;
         this.fillPool();
         this.suspendResumeLock.resume();
      }

   }

   void logPoolState(String... prefix) {
      if (this.LOGGER.isDebugEnabled()) {
         this.LOGGER.debug("{} - {}stats (total={}, active={}, idle={}, waiting={})", this.poolName, prefix.length > 0 ? prefix[0] : "", this.getTotalConnections(), this.getActiveConnections(), this.getIdleConnections(), this.getThreadsAwaitingConnection());
      }

   }

   void recycle(PoolEntry poolEntry) {
      this.metricsTracker.recordConnectionUsage(poolEntry);
      this.connectionBag.requite(poolEntry);
   }

   void closeConnection(PoolEntry poolEntry, String closureReason) {
      if (this.connectionBag.remove(poolEntry)) {
         Connection connection = poolEntry.close();
         this.closeConnectionExecutor.execute(() -> {
            this.quietlyCloseConnection(connection, closureReason);
            if (this.poolState == 0) {
               this.fillPool();
            }

         });
      }

   }

   int[] getPoolStateCounts() {
      return this.connectionBag.getStateCounts();
   }

   private PoolEntry createPoolEntry() {
      try {
         PoolEntry poolEntry = this.newPoolEntry();
         long maxLifetime = this.config.getMaxLifetime();
         if (maxLifetime > 0L) {
            long variance = maxLifetime > 10000L ? ThreadLocalRandom.current().nextLong(maxLifetime / 40L) : 0L;
            long lifetime = maxLifetime - variance;
            poolEntry.setFutureEol(this.houseKeepingExecutorService.schedule(() -> {
               if (this.softEvictConnection(poolEntry, "(connection has passed maxLifetime)", false)) {
                  this.addBagItem(this.connectionBag.getWaitingThreadCount());
               }

            }, lifetime, TimeUnit.MILLISECONDS));
         }

         return poolEntry;
      } catch (Exception var8) {
         if (this.poolState == 0) {
            this.LOGGER.debug((String)"{} - Cannot acquire connection from data source", (Object)this.poolName, (Object)(var8 instanceof PoolBase.ConnectionSetupException ? var8.getCause() : var8));
         }

         return null;
      }
   }

   private synchronized void fillPool() {
      int connectionsToAdd = Math.min(this.config.getMaximumPoolSize() - this.getTotalConnections(), this.config.getMinimumIdle() - this.getIdleConnections()) - this.addConnectionQueue.size();

      for(int i = 0; i < connectionsToAdd; ++i) {
         this.addConnectionExecutor.submit(i < connectionsToAdd - 1 ? this.POOL_ENTRY_CREATOR : this.POST_FILL_POOL_ENTRY_CREATOR);
      }

   }

   private void abortActiveConnections(ExecutorService assassinExecutor) {
      Iterator var2 = this.connectionBag.values(1).iterator();

      while(var2.hasNext()) {
         PoolEntry poolEntry = (PoolEntry)var2.next();
         Connection connection = poolEntry.close();

         try {
            connection.abort(assassinExecutor);
         } catch (Throwable var9) {
            this.quietlyCloseConnection(connection, "(connection aborted during shutdown)");
         } finally {
            this.connectionBag.remove(poolEntry);
         }
      }

   }

   private void checkFailFast() {
      long initializationTimeout = this.config.getInitializationFailTimeout();
      if (initializationTimeout >= 0L) {
         long startTime = ClockSource.currentTime();

         do {
            PoolEntry poolEntry = this.createPoolEntry();
            if (poolEntry != null) {
               if (this.config.getMinimumIdle() > 0) {
                  this.connectionBag.add(poolEntry);
                  this.LOGGER.debug((String)"{} - Added connection {}", (Object)this.poolName, (Object)poolEntry.connection);
               } else {
                  this.quietlyCloseConnection(poolEntry.close(), "(initialization check complete and minimumIdle is zero)");
               }

               return;
            }

            if (this.getLastConnectionFailure() instanceof PoolBase.ConnectionSetupException) {
               this.throwPoolInitializationException(this.getLastConnectionFailure().getCause());
            }

            UtilityElf.quietlySleep(1000L);
         } while(ClockSource.elapsedMillis(startTime) < initializationTimeout);

         if (initializationTimeout > 0L) {
            this.throwPoolInitializationException(this.getLastConnectionFailure());
         }

      }
   }

   private void throwPoolInitializationException(Throwable t) {
      this.LOGGER.error((String)"{} - Exception during pool initialization.", (Object)this.poolName, (Object)t);
      this.destroyHouseKeepingExecutorService();
      throw new HikariPool.PoolInitializationException(t);
   }

   private boolean softEvictConnection(PoolEntry poolEntry, String reason, boolean owner) {
      poolEntry.markEvicted();
      if (!owner && !this.connectionBag.reserve(poolEntry)) {
         return false;
      } else {
         this.closeConnection(poolEntry, reason);
         return true;
      }
   }

   private ScheduledExecutorService initializeHouseKeepingExecutorService() {
      if (this.config.getScheduledExecutor() == null) {
         ThreadFactory threadFactory = (ThreadFactory)Optional.ofNullable(this.config.getThreadFactory()).orElse(new UtilityElf.DefaultThreadFactory(this.poolName + " housekeeper", true));
         ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadFactory, new DiscardPolicy());
         executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
         executor.setRemoveOnCancelPolicy(true);
         return executor;
      } else {
         return this.config.getScheduledExecutor();
      }
   }

   private void destroyHouseKeepingExecutorService() {
      if (this.config.getScheduledExecutor() == null) {
         this.houseKeepingExecutorService.shutdownNow();
      }

   }

   private PoolStats getPoolStats() {
      return new PoolStats(TimeUnit.SECONDS.toMillis(1L)) {
         protected void update() {
            this.pendingThreads = HikariPool.this.getThreadsAwaitingConnection();
            this.idleConnections = HikariPool.this.getIdleConnections();
            this.totalConnections = HikariPool.this.getTotalConnections();
            this.activeConnections = HikariPool.this.getActiveConnections();
         }
      };
   }

   private SQLException createTimeoutException(long startTime) {
      this.logPoolState("Timeout failure ");
      this.metricsTracker.recordConnectionTimeout();
      String sqlState = null;
      Throwable originalException = this.getLastConnectionFailure();
      if (originalException instanceof SQLException) {
         sqlState = ((SQLException)originalException).getSQLState();
      }

      SQLException connectionException = new SQLTransientConnectionException(this.poolName + " - Connection is not available, request timed out after " + ClockSource.elapsedMillis(startTime) + "ms.", sqlState, originalException);
      if (originalException instanceof SQLException) {
         connectionException.setNextException((SQLException)originalException);
      }

      return connectionException;
   }

   public static class PoolInitializationException extends RuntimeException {
      private static final long serialVersionUID = 929872118275916520L;

      public PoolInitializationException(Throwable t) {
         super("Failed to initialize pool: " + t.getMessage(), t);
      }
   }

   private final class HouseKeeper implements Runnable {
      private volatile long previous;

      private HouseKeeper() {
         this.previous = ClockSource.plusMillis(ClockSource.currentTime(), -HikariPool.this.HOUSEKEEPING_PERIOD_MS);
      }

      public void run() {
         try {
            HikariPool.this.connectionTimeout = HikariPool.this.config.getConnectionTimeout();
            HikariPool.this.validationTimeout = HikariPool.this.config.getValidationTimeout();
            HikariPool.this.leakTask.updateLeakDetectionThreshold(HikariPool.this.config.getLeakDetectionThreshold());
            long idleTimeout = HikariPool.this.config.getIdleTimeout();
            long now = ClockSource.currentTime();
            if (ClockSource.plusMillis(now, 128L) < ClockSource.plusMillis(this.previous, HikariPool.this.HOUSEKEEPING_PERIOD_MS)) {
               HikariPool.this.LOGGER.warn((String)"{} - Retrograde clock change detected (housekeeper delta={}), soft-evicting connections from pool.", (Object)HikariPool.this.poolName, (Object)ClockSource.elapsedDisplayString(this.previous, now));
               this.previous = now;
               HikariPool.this.softEvictConnections();
               HikariPool.this.fillPool();
               return;
            }

            if (now > ClockSource.plusMillis(this.previous, 3L * HikariPool.this.HOUSEKEEPING_PERIOD_MS / 2L)) {
               HikariPool.this.LOGGER.warn((String)"{} - Thread starvation or clock leap detected (housekeeper delta={}).", (Object)HikariPool.this.poolName, (Object)ClockSource.elapsedDisplayString(this.previous, now));
            }

            this.previous = now;
            String afterPrefix = "Pool ";
            if (idleTimeout > 0L && HikariPool.this.config.getMinimumIdle() < HikariPool.this.config.getMaximumPoolSize()) {
               HikariPool.this.logPoolState("Before cleanup ");
               afterPrefix = "After cleanup  ";
               List<PoolEntry> notInUse = HikariPool.this.connectionBag.values(0);
               int removed = 0;
               Iterator var8 = notInUse.iterator();

               while(var8.hasNext()) {
                  PoolEntry entry = (PoolEntry)var8.next();
                  if (ClockSource.elapsedMillis(entry.lastAccessed, now) > idleTimeout && HikariPool.this.connectionBag.reserve(entry)) {
                     HikariPool.this.closeConnection(entry, "(connection has passed idleTimeout)");
                     ++removed;
                     if (removed > HikariPool.this.config.getMinimumIdle()) {
                        break;
                     }
                  }
               }
            }

            HikariPool.this.logPoolState(afterPrefix);
            HikariPool.this.fillPool();
         } catch (Exception var10) {
            HikariPool.this.LOGGER.error((String)"Unexpected exception in housekeeping task", (Throwable)var10);
         }

      }

      // $FF: synthetic method
      HouseKeeper(Object x1) {
         this();
      }
   }

   private final class PoolEntryCreator implements Callable<Boolean> {
      private final String loggingPrefix;

      PoolEntryCreator(String loggingPrefix) {
         this.loggingPrefix = loggingPrefix;
      }

      public Boolean call() throws Exception {
         for(long sleepBackoff = 250L; HikariPool.this.poolState == 0 && this.shouldCreateAnotherConnection(); sleepBackoff = Math.min(TimeUnit.SECONDS.toMillis(10L), Math.min(HikariPool.this.connectionTimeout, (long)((double)sleepBackoff * 1.5D)))) {
            PoolEntry poolEntry = HikariPool.this.createPoolEntry();
            if (poolEntry != null) {
               HikariPool.this.connectionBag.add(poolEntry);
               HikariPool.this.LOGGER.debug((String)"{} - Added connection {}", (Object)HikariPool.this.poolName, (Object)poolEntry.connection);
               if (this.loggingPrefix != null) {
                  HikariPool.this.logPoolState(this.loggingPrefix);
               }

               return Boolean.TRUE;
            }

            UtilityElf.quietlySleep(sleepBackoff);
         }

         return Boolean.FALSE;
      }

      private boolean shouldCreateAnotherConnection() {
         return HikariPool.this.getTotalConnections() < HikariPool.this.config.getMaximumPoolSize() && (HikariPool.this.connectionBag.getWaitingThreadCount() > 0 || HikariPool.this.getIdleConnections() < HikariPool.this.config.getMinimumIdle());
      }
   }
}

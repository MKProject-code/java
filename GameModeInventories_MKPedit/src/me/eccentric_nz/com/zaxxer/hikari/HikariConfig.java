package me.eccentric_nz.com.zaxxer.hikari;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import me.eccentric_nz.com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import me.eccentric_nz.com.zaxxer.hikari.util.PropertyElf;
import me.eccentric_nz.com.zaxxer.hikari.util.UtilityElf;
import me.eccentric_nz.org.slf4j.Logger;
import me.eccentric_nz.org.slf4j.LoggerFactory;

public class HikariConfig implements HikariConfigMXBean {
   private static final Logger LOGGER = LoggerFactory.getLogger(HikariConfig.class);
   private static final long CONNECTION_TIMEOUT;
   private static final long VALIDATION_TIMEOUT;
   private static final long IDLE_TIMEOUT;
   private static final long MAX_LIFETIME;
   private static final int DEFAULT_POOL_SIZE = 10;
   private static boolean unitTest;
   private volatile long connectionTimeout;
   private volatile long validationTimeout;
   private volatile long idleTimeout;
   private volatile long leakDetectionThreshold;
   private volatile long maxLifetime;
   private volatile int maxPoolSize;
   private volatile int minIdle;
   private long initializationFailTimeout;
   private String catalog;
   private String connectionInitSql;
   private String connectionTestQuery;
   private String dataSourceClassName;
   private String dataSourceJndiName;
   private String driverClassName;
   private String jdbcUrl;
   private String password;
   private String poolName;
   private String transactionIsolationName;
   private String username;
   private boolean isAutoCommit;
   private boolean isReadOnly;
   private boolean isIsolateInternalQueries;
   private boolean isRegisterMbeans;
   private boolean isAllowPoolSuspension;
   private DataSource dataSource;
   private Properties dataSourceProperties;
   private ThreadFactory threadFactory;
   private ScheduledExecutorService scheduledExecutor;
   private MetricsTrackerFactory metricsTrackerFactory;
   private Object metricRegistry;
   private Object healthCheckRegistry;
   private Properties healthCheckProperties;

   public HikariConfig() {
      this.dataSourceProperties = new Properties();
      this.healthCheckProperties = new Properties();
      this.minIdle = -1;
      this.maxPoolSize = -1;
      this.maxLifetime = MAX_LIFETIME;
      this.connectionTimeout = CONNECTION_TIMEOUT;
      this.validationTimeout = VALIDATION_TIMEOUT;
      this.idleTimeout = IDLE_TIMEOUT;
      this.initializationFailTimeout = 1L;
      this.isAutoCommit = true;
      String systemProp = System.getProperty("hikaricp.configurationFile");
      if (systemProp != null) {
         this.loadProperties(systemProp);
      }

   }

   public HikariConfig(Properties properties) {
      this();
      PropertyElf.setTargetFromProperties(this, properties);
   }

   public HikariConfig(String propertyFileName) {
      this();
      this.loadProperties(propertyFileName);
   }

   public String getCatalog() {
      return this.catalog;
   }

   public void setCatalog(String catalog) {
      this.catalog = catalog;
   }

   public String getConnectionTestQuery() {
      return this.connectionTestQuery;
   }

   public void setConnectionTestQuery(String connectionTestQuery) {
      this.connectionTestQuery = connectionTestQuery;
   }

   public String getConnectionInitSql() {
      return this.connectionInitSql;
   }

   public void setConnectionInitSql(String connectionInitSql) {
      this.connectionInitSql = connectionInitSql;
   }

   public long getConnectionTimeout() {
      return this.connectionTimeout;
   }

   public void setConnectionTimeout(long connectionTimeoutMs) {
      if (connectionTimeoutMs == 0L) {
         this.connectionTimeout = 2147483647L;
      } else {
         if (connectionTimeoutMs < 250L) {
            throw new IllegalArgumentException("connectionTimeout cannot be less than 250ms");
         }

         this.connectionTimeout = connectionTimeoutMs;
      }

   }

   public long getValidationTimeout() {
      return this.validationTimeout;
   }

   public void setValidationTimeout(long validationTimeoutMs) {
      if (validationTimeoutMs < 250L) {
         throw new IllegalArgumentException("validationTimeout cannot be less than 250ms");
      } else {
         this.validationTimeout = validationTimeoutMs;
      }
   }

   public DataSource getDataSource() {
      return this.dataSource;
   }

   public void setDataSource(DataSource dataSource) {
      this.dataSource = dataSource;
   }

   public String getDataSourceClassName() {
      return this.dataSourceClassName;
   }

   public void setDataSourceClassName(String className) {
      this.dataSourceClassName = className;
   }

   public void addDataSourceProperty(String propertyName, Object value) {
      this.dataSourceProperties.put(propertyName, value);
   }

   public String getDataSourceJNDI() {
      return this.dataSourceJndiName;
   }

   public void setDataSourceJNDI(String jndiDataSource) {
      this.dataSourceJndiName = jndiDataSource;
   }

   public Properties getDataSourceProperties() {
      return this.dataSourceProperties;
   }

   public void setDataSourceProperties(Properties dsProperties) {
      this.dataSourceProperties.putAll(dsProperties);
   }

   public String getDriverClassName() {
      return this.driverClassName;
   }

   public void setDriverClassName(String driverClassName) {
      try {
         Class<?> driverClass = this.getClass().getClassLoader().loadClass(driverClassName);
         driverClass.newInstance();
         this.driverClassName = driverClassName;
      } catch (Exception var3) {
         throw new RuntimeException("Failed to load class of driverClassName " + driverClassName, var3);
      }
   }

   public long getIdleTimeout() {
      return this.idleTimeout;
   }

   public void setIdleTimeout(long idleTimeoutMs) {
      if (idleTimeoutMs < 0L) {
         throw new IllegalArgumentException("idleTimeout cannot be negative");
      } else {
         this.idleTimeout = idleTimeoutMs;
      }
   }

   public String getJdbcUrl() {
      return this.jdbcUrl;
   }

   public void setJdbcUrl(String jdbcUrl) {
      this.jdbcUrl = jdbcUrl;
   }

   public boolean isAutoCommit() {
      return this.isAutoCommit;
   }

   public void setAutoCommit(boolean isAutoCommit) {
      this.isAutoCommit = isAutoCommit;
   }

   public boolean isAllowPoolSuspension() {
      return this.isAllowPoolSuspension;
   }

   public void setAllowPoolSuspension(boolean isAllowPoolSuspension) {
      this.isAllowPoolSuspension = isAllowPoolSuspension;
   }

   public long getInitializationFailTimeout() {
      return this.initializationFailTimeout;
   }

   public void setInitializationFailTimeout(long initializationFailTimeout) {
      this.initializationFailTimeout = initializationFailTimeout;
   }

   /** @deprecated */
   @Deprecated
   public boolean isInitializationFailFast() {
      return this.initializationFailTimeout > 0L;
   }

   /** @deprecated */
   @Deprecated
   public void setInitializationFailFast(boolean failFast) {
      LOGGER.warn("The initializationFailFast propery is deprecated, see initializationFailTimeout");
      this.initializationFailTimeout = (long)(failFast ? 1 : -1);
   }

   public boolean isIsolateInternalQueries() {
      return this.isIsolateInternalQueries;
   }

   public void setIsolateInternalQueries(boolean isolate) {
      this.isIsolateInternalQueries = isolate;
   }

   /** @deprecated */
   @Deprecated
   public boolean isJdbc4ConnectionTest() {
      return false;
   }

   /** @deprecated */
   @Deprecated
   public void setJdbc4ConnectionTest(boolean useIsValid) {
      LOGGER.warn("The jdbcConnectionTest property is now deprecated, see the documentation for connectionTestQuery");
   }

   public MetricsTrackerFactory getMetricsTrackerFactory() {
      return this.metricsTrackerFactory;
   }

   public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
      if (this.metricRegistry != null) {
         throw new IllegalStateException("cannot use setMetricsTrackerFactory() and setMetricRegistry() together");
      } else {
         this.metricsTrackerFactory = metricsTrackerFactory;
      }
   }

   public Object getMetricRegistry() {
      return this.metricRegistry;
   }

   public void setMetricRegistry(Object metricRegistry) {
      if (this.metricsTrackerFactory != null) {
         throw new IllegalStateException("cannot use setMetricRegistry() and setMetricsTrackerFactory() together");
      } else {
         if (metricRegistry != null) {
            metricRegistry = this.getObjectOrPerformJndiLookup(metricRegistry);
            if (!(metricRegistry instanceof MetricRegistry)) {
               throw new IllegalArgumentException("Class must be an instance of com.codahale.metrics.MetricRegistry");
            }
         }

         this.metricRegistry = metricRegistry;
      }
   }

   private Object getObjectOrPerformJndiLookup(Object object) {
      if (object instanceof String) {
         try {
            InitialContext initCtx = new InitialContext();
            return initCtx.lookup((String)object);
         } catch (NamingException var3) {
            throw new IllegalArgumentException(var3);
         }
      } else {
         return object;
      }
   }

   public Object getHealthCheckRegistry() {
      return this.healthCheckRegistry;
   }

   public void setHealthCheckRegistry(Object healthCheckRegistry) {
      if (healthCheckRegistry != null) {
         healthCheckRegistry = this.getObjectOrPerformJndiLookup(healthCheckRegistry);
         if (!(healthCheckRegistry instanceof HealthCheckRegistry)) {
            throw new IllegalArgumentException("Class must be an instance of com.codahale.metrics.health.HealthCheckRegistry");
         }
      }

      this.healthCheckRegistry = healthCheckRegistry;
   }

   public Properties getHealthCheckProperties() {
      return this.healthCheckProperties;
   }

   public void setHealthCheckProperties(Properties healthCheckProperties) {
      this.healthCheckProperties.putAll(healthCheckProperties);
   }

   public void addHealthCheckProperty(String key, String value) {
      this.healthCheckProperties.setProperty(key, value);
   }

   public boolean isReadOnly() {
      return this.isReadOnly;
   }

   public void setReadOnly(boolean readOnly) {
      this.isReadOnly = readOnly;
   }

   public boolean isRegisterMbeans() {
      return this.isRegisterMbeans;
   }

   public void setRegisterMbeans(boolean register) {
      this.isRegisterMbeans = register;
   }

   public long getLeakDetectionThreshold() {
      return this.leakDetectionThreshold;
   }

   public void setLeakDetectionThreshold(long leakDetectionThresholdMs) {
      this.leakDetectionThreshold = leakDetectionThresholdMs;
   }

   public long getMaxLifetime() {
      return this.maxLifetime;
   }

   public void setMaxLifetime(long maxLifetimeMs) {
      this.maxLifetime = maxLifetimeMs;
   }

   public int getMaximumPoolSize() {
      return this.maxPoolSize;
   }

   public void setMaximumPoolSize(int maxPoolSize) {
      if (maxPoolSize < 1) {
         throw new IllegalArgumentException("maxPoolSize cannot be less than 1");
      } else {
         this.maxPoolSize = maxPoolSize;
      }
   }

   public int getMinimumIdle() {
      return this.minIdle;
   }

   public void setMinimumIdle(int minIdle) {
      if (minIdle < 0) {
         throw new IllegalArgumentException("minimumIdle cannot be negative");
      } else {
         this.minIdle = minIdle;
      }
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getPoolName() {
      return this.poolName;
   }

   public void setPoolName(String poolName) {
      this.poolName = poolName;
   }

   /** @deprecated */
   @Deprecated
   public ScheduledThreadPoolExecutor getScheduledExecutorService() {
      return (ScheduledThreadPoolExecutor)this.scheduledExecutor;
   }

   /** @deprecated */
   @Deprecated
   public void setScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
      this.scheduledExecutor = executor;
   }

   public ScheduledExecutorService getScheduledExecutor() {
      return this.scheduledExecutor;
   }

   public void setScheduledExecutor(ScheduledExecutorService executor) {
      this.scheduledExecutor = executor;
   }

   public String getTransactionIsolation() {
      return this.transactionIsolationName;
   }

   public void setTransactionIsolation(String isolationLevel) {
      this.transactionIsolationName = isolationLevel;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public ThreadFactory getThreadFactory() {
      return this.threadFactory;
   }

   public void setThreadFactory(ThreadFactory threadFactory) {
      this.threadFactory = threadFactory;
   }

   public void validate() {
      if (this.poolName == null) {
         this.poolName = "HikariPool-" + this.generatePoolNumber();
      } else if (this.isRegisterMbeans && this.poolName.contains(":")) {
         throw new IllegalArgumentException("poolName cannot contain ':' when used with JMX");
      }

      this.catalog = UtilityElf.getNullIfEmpty(this.catalog);
      this.connectionInitSql = UtilityElf.getNullIfEmpty(this.connectionInitSql);
      this.connectionTestQuery = UtilityElf.getNullIfEmpty(this.connectionTestQuery);
      this.transactionIsolationName = UtilityElf.getNullIfEmpty(this.transactionIsolationName);
      this.dataSourceClassName = UtilityElf.getNullIfEmpty(this.dataSourceClassName);
      this.dataSourceJndiName = UtilityElf.getNullIfEmpty(this.dataSourceJndiName);
      this.driverClassName = UtilityElf.getNullIfEmpty(this.driverClassName);
      this.jdbcUrl = UtilityElf.getNullIfEmpty(this.jdbcUrl);
      if (this.dataSource != null) {
         if (this.dataSourceClassName != null) {
            LOGGER.warn((String)"{} - using dataSource and ignoring dataSourceClassName.", (Object)this.poolName);
         }
      } else if (this.dataSourceClassName != null) {
         if (this.driverClassName != null) {
            LOGGER.error((String)"{} - cannot use driverClassName and dataSourceClassName together.", (Object)this.poolName);
            throw new IllegalStateException("cannot use driverClassName and dataSourceClassName together.");
         }

         if (this.jdbcUrl != null) {
            LOGGER.warn((String)"{} - using dataSourceClassName and ignoring jdbcUrl.", (Object)this.poolName);
         }
      } else if (this.jdbcUrl == null && this.dataSourceJndiName == null) {
         if (this.driverClassName != null) {
            LOGGER.error((String)"{} - jdbcUrl is required with driverClassName.", (Object)this.poolName);
            throw new IllegalArgumentException("jdbcUrl is required with driverClassName.");
         }

         LOGGER.error((String)"{} - dataSource or dataSourceClassName or jdbcUrl is required.", (Object)this.poolName);
         throw new IllegalArgumentException("dataSource or dataSourceClassName or jdbcUrl is required.");
      }

      this.validateNumerics();
      if (LOGGER.isDebugEnabled() || unitTest) {
         this.logConfiguration();
      }

   }

   private void validateNumerics() {
      if (this.maxLifetime != 0L && this.maxLifetime < TimeUnit.SECONDS.toMillis(30L)) {
         LOGGER.warn((String)"{} - maxLifetime is less than 30000ms, setting to default {}ms.", (Object)this.poolName, (Object)MAX_LIFETIME);
         this.maxLifetime = MAX_LIFETIME;
      }

      if (this.idleTimeout + TimeUnit.SECONDS.toMillis(1L) > this.maxLifetime && this.maxLifetime > 0L) {
         LOGGER.warn((String)"{} - idleTimeout is close to or more than maxLifetime, disabling it.", (Object)this.poolName);
         this.idleTimeout = 0L;
      }

      if (this.idleTimeout != 0L && this.idleTimeout < TimeUnit.SECONDS.toMillis(10L)) {
         LOGGER.warn((String)"{} - idleTimeout is less than 10000ms, setting to default {}ms.", (Object)this.poolName, (Object)IDLE_TIMEOUT);
         this.idleTimeout = IDLE_TIMEOUT;
      }

      if (this.leakDetectionThreshold > 0L && !unitTest && (this.leakDetectionThreshold < TimeUnit.SECONDS.toMillis(2L) || this.leakDetectionThreshold > this.maxLifetime && this.maxLifetime > 0L)) {
         LOGGER.warn((String)"{} - leakDetectionThreshold is less than 2000ms or more than maxLifetime, disabling it.", (Object)this.poolName);
         this.leakDetectionThreshold = 0L;
      }

      if (this.connectionTimeout < 250L) {
         LOGGER.warn((String)"{} - connectionTimeout is less than 250ms, setting to {}ms.", (Object)this.poolName, (Object)CONNECTION_TIMEOUT);
         this.connectionTimeout = CONNECTION_TIMEOUT;
      }

      if (this.validationTimeout < 250L) {
         LOGGER.warn((String)"{} - validationTimeout is less than 250ms, setting to {}ms.", (Object)this.poolName, (Object)VALIDATION_TIMEOUT);
         this.validationTimeout = VALIDATION_TIMEOUT;
      }

      if (this.maxPoolSize < 1) {
         this.maxPoolSize = this.minIdle <= 0 ? 10 : this.minIdle;
      }

      if (this.minIdle < 0 || this.minIdle > this.maxPoolSize) {
         this.minIdle = this.maxPoolSize;
      }

   }

   private void logConfiguration() {
      LOGGER.debug((String)"{} - configuration:", (Object)this.poolName);
      Set<String> propertyNames = new TreeSet(PropertyElf.getPropertyNames(HikariConfig.class));
      Iterator var2 = propertyNames.iterator();

      while(var2.hasNext()) {
         String prop = (String)var2.next();

         try {
            Object value = PropertyElf.getProperty(prop, this);
            if ("dataSourceProperties".equals(prop)) {
               Properties dsProps = PropertyElf.copyProperties(this.dataSourceProperties);
               dsProps.setProperty("password", "<masked>");
               value = dsProps;
            }

            if ("initializationFailTimeout".equals(prop) && this.initializationFailTimeout == Long.MAX_VALUE) {
               value = "infinite";
            } else if ("transactionIsolation".equals(prop) && this.transactionIsolationName == null) {
               value = "default";
            } else if (prop.matches("scheduledExecutorService|threadFactory") && value == null) {
               value = "internal";
            } else if (prop.contains("password")) {
               value = "<masked>";
            } else if (value instanceof String) {
               value = "\"" + value + "\"";
            } else if (value == null) {
               value = "none";
            }

            LOGGER.debug((prop + "................................................").substring(0, 32) + value);
         } catch (Exception var6) {
         }
      }

   }

   private void loadProperties(String propertyFileName) {
      File propFile = new File(propertyFileName);

      try {
         InputStream is = propFile.isFile() ? new FileInputStream(propFile) : this.getClass().getResourceAsStream(propertyFileName);
         Throwable var4 = null;

         try {
            if (is == null) {
               throw new IllegalArgumentException("Cannot find property file: " + propertyFileName);
            }

            Properties props = new Properties();
            props.load((InputStream)is);
            PropertyElf.setTargetFromProperties(this, props);
         } catch (Throwable var14) {
            var4 = var14;
            throw var14;
         } finally {
            if (is != null) {
               if (var4 != null) {
                  try {
                     ((InputStream)is).close();
                  } catch (Throwable var13) {
                     var4.addSuppressed(var13);
                  }
               } else {
                  ((InputStream)is).close();
               }
            }

         }

      } catch (IOException var16) {
         throw new RuntimeException("Failed to read property file", var16);
      }
   }

   private int generatePoolNumber() {
      synchronized(System.getProperties()) {
         int next = Integer.getInteger("me.eccentric_nz.com.zaxxer.hikari.pool_number", 0) + 1;
         System.setProperty("me.eccentric_nz.com.zaxxer.hikari.pool_number", String.valueOf(next));
         return next;
      }
   }

   public void copyState(HikariConfig other) {
      Field[] var2 = HikariConfig.class.getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];
         if (!Modifier.isFinal(field.getModifiers())) {
            field.setAccessible(true);

            try {
               field.set(other, field.get(this));
            } catch (Exception var7) {
               throw new RuntimeException("Failed to copy HikariConfig state: " + var7.getMessage(), var7);
            }
         }
      }

   }

   static {
      CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(30L);
      VALIDATION_TIMEOUT = TimeUnit.SECONDS.toMillis(5L);
      IDLE_TIMEOUT = TimeUnit.MINUTES.toMillis(10L);
      MAX_LIFETIME = TimeUnit.MINUTES.toMillis(30L);
      unitTest = false;
   }
}

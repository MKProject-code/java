package me.eccentric_nz.com.zaxxer.hikari.metrics.dropwizard;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.HealthCheck.Result;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import me.eccentric_nz.com.zaxxer.hikari.HikariConfig;
import me.eccentric_nz.com.zaxxer.hikari.pool.HikariPool;

public final class CodahaleHealthChecker {
   public static void registerHealthChecks(HikariPool pool, final HikariConfig hikariConfig, HealthCheckRegistry registry) {
      Properties healthCheckProperties = hikariConfig.getHealthCheckProperties();
      MetricRegistry metricRegistry = (MetricRegistry)hikariConfig.getMetricRegistry();
      long checkTimeoutMs = Long.parseLong(healthCheckProperties.getProperty("connectivityCheckTimeoutMs", String.valueOf(hikariConfig.getConnectionTimeout())));
      registry.register(MetricRegistry.name(hikariConfig.getPoolName(), new String[]{"pool", "ConnectivityCheck"}), new CodahaleHealthChecker.ConnectivityHealthCheck(pool, checkTimeoutMs));
      long expected99thPercentile = Long.parseLong(healthCheckProperties.getProperty("expected99thPercentileMs", "0"));
      if (metricRegistry != null && expected99thPercentile > 0L) {
         SortedMap<String, Timer> timers = metricRegistry.getTimers(new MetricFilter() {
            public boolean matches(String name, Metric metric) {
               return name.equals(MetricRegistry.name(hikariConfig.getPoolName(), new String[]{"pool", "Wait"}));
            }
         });
         if (!timers.isEmpty()) {
            Timer timer = (Timer)((Entry)timers.entrySet().iterator().next()).getValue();
            registry.register(MetricRegistry.name(hikariConfig.getPoolName(), new String[]{"pool", "Connection99Percent"}), new CodahaleHealthChecker.Connection99Percent(timer, expected99thPercentile));
         }
      }

   }

   private CodahaleHealthChecker() {
   }

   private static class Connection99Percent extends HealthCheck {
      private final Timer waitTimer;
      private final long expected99thPercentile;

      Connection99Percent(Timer waitTimer, long expected99thPercentile) {
         this.waitTimer = waitTimer;
         this.expected99thPercentile = expected99thPercentile;
      }

      protected Result check() throws Exception {
         long the99thPercentile = TimeUnit.NANOSECONDS.toMillis(Math.round(this.waitTimer.getSnapshot().get99thPercentile()));
         return the99thPercentile <= this.expected99thPercentile ? Result.healthy() : Result.unhealthy("99th percentile connection wait time of %dms exceeds the threshold %dms", new Object[]{the99thPercentile, this.expected99thPercentile});
      }
   }

   private static class ConnectivityHealthCheck extends HealthCheck {
      private final HikariPool pool;
      private final long checkTimeoutMs;

      ConnectivityHealthCheck(HikariPool pool, long checkTimeoutMs) {
         this.pool = pool;
         this.checkTimeoutMs = checkTimeoutMs > 0L && checkTimeoutMs != 2147483647L ? checkTimeoutMs : TimeUnit.SECONDS.toMillis(10L);
      }

      protected Result check() throws Exception {
         try {
            Connection connection = this.pool.getConnection(this.checkTimeoutMs);
            Throwable var2 = null;

            Result var3;
            try {
               var3 = Result.healthy();
            } catch (Throwable var13) {
               var2 = var13;
               throw var13;
            } finally {
               if (connection != null) {
                  if (var2 != null) {
                     try {
                        connection.close();
                     } catch (Throwable var12) {
                        var2.addSuppressed(var12);
                     }
                  } else {
                     connection.close();
                  }
               }

            }

            return var3;
         } catch (SQLException var15) {
            return Result.unhealthy(var15);
         }
      }
   }
}

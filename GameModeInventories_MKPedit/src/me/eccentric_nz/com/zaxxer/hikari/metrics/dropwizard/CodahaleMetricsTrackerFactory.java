package me.eccentric_nz.com.zaxxer.hikari.metrics.dropwizard;

import com.codahale.metrics.MetricRegistry;
import me.eccentric_nz.com.zaxxer.hikari.metrics.IMetricsTracker;
import me.eccentric_nz.com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import me.eccentric_nz.com.zaxxer.hikari.metrics.PoolStats;

public final class CodahaleMetricsTrackerFactory implements MetricsTrackerFactory {
   private final MetricRegistry registry;

   public CodahaleMetricsTrackerFactory(MetricRegistry registry) {
      this.registry = registry;
   }

   public MetricRegistry getRegistry() {
      return this.registry;
   }

   public IMetricsTracker create(String poolName, PoolStats poolStats) {
      return new CodaHaleMetricsTracker(poolName, poolStats, this.registry);
   }
}

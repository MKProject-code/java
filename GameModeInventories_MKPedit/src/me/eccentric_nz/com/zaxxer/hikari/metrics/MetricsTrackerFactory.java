package me.eccentric_nz.com.zaxxer.hikari.metrics;

public interface MetricsTrackerFactory {
   IMetricsTracker create(String var1, PoolStats var2);
}

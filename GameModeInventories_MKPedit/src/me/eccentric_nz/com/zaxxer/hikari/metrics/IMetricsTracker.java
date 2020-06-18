package me.eccentric_nz.com.zaxxer.hikari.metrics;

public interface IMetricsTracker extends AutoCloseable {
   default void recordConnectionCreatedMillis(long connectionCreatedMillis) {
   }

   default void recordConnectionAcquiredNanos(long elapsedAcquiredNanos) {
   }

   default void recordConnectionUsageMillis(long elapsedBorrowedMillis) {
   }

   default void recordConnectionTimeout() {
   }

   default void close() {
   }
}

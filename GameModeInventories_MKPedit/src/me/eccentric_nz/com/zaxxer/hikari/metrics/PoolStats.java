package me.eccentric_nz.com.zaxxer.hikari.metrics;

import java.util.concurrent.atomic.AtomicLong;
import me.eccentric_nz.com.zaxxer.hikari.util.ClockSource;

public abstract class PoolStats {
   private final AtomicLong reloadAt;
   private final long timeoutMs;
   protected volatile int totalConnections;
   protected volatile int idleConnections;
   protected volatile int activeConnections;
   protected volatile int pendingThreads;

   public PoolStats(long timeoutMs) {
      this.timeoutMs = timeoutMs;
      this.reloadAt = new AtomicLong();
   }

   public int getTotalConnections() {
      if (this.shouldLoad()) {
         this.update();
      }

      return this.totalConnections;
   }

   public int getIdleConnections() {
      if (this.shouldLoad()) {
         this.update();
      }

      return this.idleConnections;
   }

   public int getActiveConnections() {
      if (this.shouldLoad()) {
         this.update();
      }

      return this.activeConnections;
   }

   public int getPendingThreads() {
      if (this.shouldLoad()) {
         this.update();
      }

      return this.pendingThreads;
   }

   protected abstract void update();

   private boolean shouldLoad() {
      long now;
      long reloadTime;
      do {
         now = ClockSource.currentTime();
         reloadTime = this.reloadAt.get();
         if (reloadTime > now) {
            return false;
         }
      } while(!this.reloadAt.compareAndSet(reloadTime, ClockSource.plusMillis(now, this.timeoutMs)));

      return true;
   }
}

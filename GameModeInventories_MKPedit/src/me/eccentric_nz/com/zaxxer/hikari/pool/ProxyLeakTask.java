package me.eccentric_nz.com.zaxxer.hikari.pool;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import me.eccentric_nz.org.slf4j.Logger;
import me.eccentric_nz.org.slf4j.LoggerFactory;

class ProxyLeakTask implements Runnable {
   private static final Logger LOGGER = LoggerFactory.getLogger(ProxyLeakTask.class);
   private static final ProxyLeakTask NO_LEAK = new ProxyLeakTask() {
      public void cancel() {
      }
   };
   private ScheduledExecutorService executorService;
   private long leakDetectionThreshold;
   private ScheduledFuture<?> scheduledFuture;
   private String connectionName;
   private Exception exception;
   private boolean isLeaked;

   ProxyLeakTask(long leakDetectionThreshold, ScheduledExecutorService executorService) {
      this.executorService = executorService;
      this.leakDetectionThreshold = leakDetectionThreshold;
   }

   private ProxyLeakTask(ProxyLeakTask parent, PoolEntry poolEntry) {
      this.exception = new Exception("Apparent connection leak detected");
      this.connectionName = poolEntry.connection.toString();
      this.scheduledFuture = parent.executorService.schedule(this, parent.leakDetectionThreshold, TimeUnit.MILLISECONDS);
   }

   private ProxyLeakTask() {
   }

   ProxyLeakTask schedule(PoolEntry bagEntry) {
      return this.leakDetectionThreshold == 0L ? NO_LEAK : new ProxyLeakTask(this, bagEntry);
   }

   void updateLeakDetectionThreshold(long leakDetectionThreshold) {
      this.leakDetectionThreshold = leakDetectionThreshold;
   }

   public void run() {
      this.isLeaked = true;
      StackTraceElement[] stackTrace = this.exception.getStackTrace();
      StackTraceElement[] trace = new StackTraceElement[stackTrace.length - 5];
      System.arraycopy(stackTrace, 5, trace, 0, trace.length);
      this.exception.setStackTrace(trace);
      LOGGER.warn((String)"Connection leak detection triggered for {}, stack trace follows", (Object)this.connectionName, (Object)this.exception);
   }

   void cancel() {
      this.scheduledFuture.cancel(false);
      if (this.isLeaked) {
         LOGGER.info((String)"Previously reported leaked connection {} was returned to the pool (unleaked)", (Object)this.connectionName);
      }

   }

   // $FF: synthetic method
   ProxyLeakTask(Object x0) {
      this();
   }
}

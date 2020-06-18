package me.eccentric_nz.org.slf4j.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import me.eccentric_nz.org.slf4j.ILoggerFactory;
import me.eccentric_nz.org.slf4j.Logger;

public class SimpleLoggerFactory implements ILoggerFactory {
   ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap();

   public SimpleLoggerFactory() {
      SimpleLogger.lazyInit();
   }

   public Logger getLogger(String name) {
      Logger simpleLogger = (Logger)this.loggerMap.get(name);
      if (simpleLogger != null) {
         return simpleLogger;
      } else {
         Logger newInstance = new SimpleLogger(name);
         Logger oldInstance = (Logger)this.loggerMap.putIfAbsent(name, newInstance);
         return (Logger)(oldInstance == null ? newInstance : oldInstance);
      }
   }

   void reset() {
      this.loggerMap.clear();
   }
}

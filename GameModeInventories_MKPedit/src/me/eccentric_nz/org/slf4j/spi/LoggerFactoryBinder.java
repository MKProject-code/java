package me.eccentric_nz.org.slf4j.spi;

import me.eccentric_nz.org.slf4j.ILoggerFactory;

public interface LoggerFactoryBinder {
   ILoggerFactory getLoggerFactory();

   String getLoggerFactoryClassStr();
}

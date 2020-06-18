package me.eccentric_nz.org.slf4j.helpers;

import me.eccentric_nz.org.slf4j.ILoggerFactory;
import me.eccentric_nz.org.slf4j.Logger;

public class NOPLoggerFactory implements ILoggerFactory {
   public Logger getLogger(String name) {
      return NOPLogger.NOP_LOGGER;
   }
}

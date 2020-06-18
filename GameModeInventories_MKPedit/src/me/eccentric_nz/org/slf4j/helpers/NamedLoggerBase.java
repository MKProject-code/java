package me.eccentric_nz.org.slf4j.helpers;

import java.io.ObjectStreamException;
import java.io.Serializable;
import me.eccentric_nz.org.slf4j.Logger;
import me.eccentric_nz.org.slf4j.LoggerFactory;

abstract class NamedLoggerBase implements Logger, Serializable {
   private static final long serialVersionUID = 7535258609338176893L;
   protected String name;

   public String getName() {
      return this.name;
   }

   protected Object readResolve() throws ObjectStreamException {
      return LoggerFactory.getLogger(this.getName());
   }
}

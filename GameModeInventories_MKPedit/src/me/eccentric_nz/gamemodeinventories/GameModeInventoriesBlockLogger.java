package me.eccentric_nz.gamemodeinventories;

import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;
import me.botsko.prism.Prism;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.plugin.PluginManager;

public class GameModeInventoriesBlockLogger {
   private final GameModeInventories plugin;
   private CoreProtectAPI coreProtectAPI = null;
   private Consumer logBlockConsumer = null;
   private Prism prism = null;
   private GameModeInventoriesBlockLogger.GMIBlockLogger whichLogger;
   private boolean logging = false;

   public GameModeInventoriesBlockLogger(GameModeInventories plugin) {
      this.plugin = plugin;
   }

   public CoreProtectAPI getCoreProtectAPI() {
      return this.coreProtectAPI;
   }

   public Consumer getLogBlockConsumer() {
      return this.logBlockConsumer;
   }

   public Prism getPrism() {
      return this.prism;
   }

   public GameModeInventoriesBlockLogger.GMIBlockLogger getWhichLogger() {
      return this.whichLogger;
   }

   public boolean isLogging() {
      return this.logging;
   }

   public void enableLogger() {
      PluginManager pm = this.plugin.getServer().getPluginManager();
      if (pm.isPluginEnabled("CoreProtect")) {
         CoreProtect cp = (CoreProtect)pm.getPlugin("CoreProtect");
         if (cp == null || !(cp instanceof CoreProtect)) {
            return;
         }

         CoreProtectAPI CoreProtect = cp.getAPI();
         if (!CoreProtect.isEnabled()) {
            return;
         }

         if (CoreProtect.APIVersion() < 2) {
            return;
         }

         this.plugin.getServer().getConsoleSender().sendMessage(this.plugin.MY_PLUGIN_NAME + "Connecting to CoreProtect");
         this.coreProtectAPI = CoreProtect;
         this.whichLogger = GameModeInventoriesBlockLogger.GMIBlockLogger.CORE_PROTECT;
         this.logging = true;
      }

      if (pm.isPluginEnabled("LogBlock")) {
         LogBlock lb = (LogBlock)pm.getPlugin("LogBlock");
         if (lb != null) {
            this.plugin.getServer().getConsoleSender().sendMessage(this.plugin.MY_PLUGIN_NAME + "Connecting to LogBlock");
            this.logBlockConsumer = lb.getConsumer();
            this.whichLogger = GameModeInventoriesBlockLogger.GMIBlockLogger.LOG_BLOCK;
            this.logging = true;
         }
      }

      if (pm.isPluginEnabled("Prism")) {
         Prism tmp_prism = (Prism)pm.getPlugin("Prism");
         if (tmp_prism != null) {
            this.plugin.getServer().getConsoleSender().sendMessage(this.plugin.MY_PLUGIN_NAME + "Connecting to Prism");
            this.prism = tmp_prism;
            this.whichLogger = GameModeInventoriesBlockLogger.GMIBlockLogger.PRISM;
            this.logging = true;
         }
      }

   }

   public static enum GMIBlockLogger {
      CORE_PROTECT,
      LOG_BLOCK,
      PRISM;
   }
}

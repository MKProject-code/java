package me.eccentric_nz.gamemodeinventories;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class GameModeInventoriesDeath implements Listener {
   private final GameModeInventories plugin;
   private final boolean force;
   private final GameMode mode;

   public GameModeInventoriesDeath(GameModeInventories plugin) {
      this.plugin = plugin;
      GameModeInventoriesDeath.ServerValues sv = this.getServerForceGamemode();
      this.force = sv.getForce();
      this.mode = sv.getMode();
   }

   @EventHandler
   public void onPlayerDeath(PlayerDeathEvent event) {
      assert event.getEntity() instanceof Player;

      Player p = event.getEntity();
      if (p.hasPermission("gamemodeinventories.death") && this.plugin.getConfig().getBoolean("save_on_death")) {
         this.plugin.getInventoryHandler().saveOnDeath(p);
         event.getDrops().clear();
      }

      if (p.getGameMode().equals(GameMode.CREATIVE) && this.plugin.getConfig().getBoolean("no_drops")) {
         event.getDrops().clear();
      }

   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      Player p = event.getPlayer();
      if (p.hasPermission("gamemodeinventories.use") && this.force) {
         p.setGameMode(this.mode);
      }

   }

   @EventHandler
   public void onRespawn(PlayerRespawnEvent event) {
      Player p = event.getPlayer();
      if (p.hasPermission("gamemodeinventories.death") && this.plugin.getConfig().getBoolean("save_on_death")) {
         this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
            this.plugin.getInventoryHandler().restoreOnSpawn(p);
         });
      }

   }

   private GameModeInventoriesDeath.ServerValues getServerForceGamemode() {
      FileInputStream in = null;
      GameModeInventoriesDeath.ServerValues sv = new GameModeInventoriesDeath.ServerValues();

      try {
         Properties properties = new Properties();
         String path = "server.properties";
         in = new FileInputStream(path);
         properties.load(in);
         sv.setForce(properties.getProperty("force-gamemode").equalsIgnoreCase("true"));
         GameMode gmode = GameMode.valueOf(properties.getProperty("gamemode").toUpperCase(Locale.ENGLISH));
         sv.setMode(gmode);
      } catch (FileNotFoundException var16) {
         this.plugin.debug("Could not find server.properties!");
      } catch (IOException var17) {
         this.plugin.debug("Could not read server.properties!");
      } finally {
         try {
            if (in != null) {
               in.close();
            }
         } catch (IOException var15) {
            this.plugin.debug("Could not close server.properties!");
         }

      }

      return sv;
   }

   public class ServerValues {
      private boolean force;
      private GameMode mode;

      public boolean getForce() {
         return this.force;
      }

      public void setForce(boolean force) {
         this.force = force;
      }

      public GameMode getMode() {
         return this.mode;
      }

      public void setMode(GameMode mode) {
         this.mode = mode;
      }
   }
}

package me.eccentric_nz.gamemodeinventories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.eccentric_nz.gamemodeinventories.database.GameModeInventoriesConnectionPool;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class GameModeInventoriesWorldListener implements Listener {
   private final GameModeInventories plugin;

   public GameModeInventoriesWorldListener(GameModeInventories plugin) {
      this.plugin = plugin;
   }

   @EventHandler
   public void onWorldChange(PlayerChangedWorldEvent event) {
      if (this.plugin.getConfig().getBoolean("survival_on_world_change")) {
         Player p = event.getPlayer();
         if (!GameModeInventoriesBypass.canBypass(p, "survival", this.plugin)) {
            World from = event.getFrom();
            World to = p.getWorld();
            if (from != to) {
               p.setGameMode(GameMode.SURVIVAL);
            }

         }
      }
   }

   @EventHandler
   public void onPlayerTeleport(PlayerTeleportEvent event) {
      if (this.plugin.getConfig().getBoolean("creative_world.switch_to")) {
         Location from = event.getFrom();
         Location to = event.getTo();
         if (!from.getWorld().equals(to.getWorld())) {
            String uuid = event.getPlayer().getUniqueId().toString();
            Connection connection = null;
            PreparedStatement statement = null;
            PreparedStatement update = null;
            PreparedStatement insert = null;
            ResultSet rs = null;

            try {
               connection = GameModeInventoriesConnectionPool.dbc();
               if (connection != null && !connection.isClosed()) {
                  statement = connection.prepareStatement("SELECT world FROM worlds WHERE uuid = ? AND world = ?");
                  statement.setString(1, uuid);
                  statement.setString(2, from.getWorld().getName());
                  rs = statement.executeQuery();
                  if (rs.isBeforeFirst()) {
                     rs.next();
                     update = connection.prepareStatement("UPDATE worlds set x = ?, y = ?, z = ?, pitch = ?, yaw = ? WHERE uuid = ? AND world = ?");
                     update.setDouble(1, from.getX());
                     update.setDouble(2, from.getY());
                     update.setDouble(3, from.getZ());
                     update.setDouble(4, (double)from.getPitch());
                     update.setDouble(5, (double)from.getYaw());
                     update.setString(6, uuid);
                     update.setString(7, from.getWorld().getName());
                     update.executeUpdate();
                  } else {
                     insert = connection.prepareStatement("INSERT INTO worlds (uuid, world, x, y, z, pitch, yaw) VALUES (?, ?, ?, ?, ?, ?, ?)");
                     insert.setString(1, uuid);
                     insert.setString(2, from.getWorld().getName());
                     insert.setDouble(3, from.getX());
                     insert.setDouble(4, from.getY());
                     insert.setDouble(5, from.getZ());
                     insert.setDouble(6, (double)from.getPitch());
                     insert.setDouble(7, (double)from.getYaw());
                     insert.executeUpdate();
                  }
               }
            } catch (SQLException var19) {
               GameModeInventories.plugin.debug("Could not save world on teleport, " + var19);
            } finally {
               try {
                  if (rs != null) {
                     rs.close();
                  }

                  if (statement != null) {
                     statement.close();
                  }

                  if (update != null) {
                     update.close();
                  }

                  if (insert != null) {
                     insert.close();
                  }

                  if (connection != null && GameModeInventoriesConnectionPool.isIsMySQL()) {
                     connection.close();
                  }
               } catch (SQLException var18) {
                  System.err.println("Could not close resultsets, statements or connection, " + var18);
               }

            }
         }
      }

   }
}

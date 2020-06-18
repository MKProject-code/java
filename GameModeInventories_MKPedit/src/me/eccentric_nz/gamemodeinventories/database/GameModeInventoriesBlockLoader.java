package me.eccentric_nz.gamemodeinventories.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.gamemodeinventories.GMIDebug;
import me.eccentric_nz.gamemodeinventories.GameModeInventories;
import org.bukkit.scheduler.BukkitRunnable;

public class GameModeInventoriesBlockLoader extends BukkitRunnable {
   private final GameModeInventories plugin;
   private final String gmiwc;

   public GameModeInventoriesBlockLoader(GameModeInventories plugin, String gmiwc) {
      this.plugin = plugin;
      this.gmiwc = gmiwc;
   }

   public void run() {
      Connection connection = null;
      PreparedStatement psb = null;
      ResultSet rb = null;

      try {
         connection = GameModeInventoriesConnectionPool.dbc();
         String blocksQuery = "SELECT location FROM blocks WHERE worldchunk = ?";
         psb = connection.prepareStatement(blocksQuery);
         psb.setString(1, this.gmiwc);
         rb = psb.executeQuery();
         List<String> l = new ArrayList();
         if (rb.isBeforeFirst()) {
            while(rb.next()) {
               l.add(rb.getString("location"));
            }
         }

         this.plugin.getCreativeBlocks().put(this.gmiwc, l);
         this.plugin.debug("Protecting blocks for chunk: " + this.gmiwc, GMIDebug.ALL);
      } catch (SQLException var14) {
         System.err.println("Could not load blocks, " + var14);
      } finally {
         try {
            if (rb != null) {
               rb.close();
            }

            if (psb != null) {
               psb.close();
            }

            if (connection != null && GameModeInventoriesConnectionPool.isIsMySQL()) {
               connection.close();
            }
         } catch (SQLException var13) {
            System.err.println("Could not load blocks, " + var13);
         }

      }

   }
}

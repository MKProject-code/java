package me.eccentric_nz.gamemodeinventories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.gamemodeinventories.database.GameModeInventoriesConnectionPool;
import me.eccentric_nz.gamemodeinventories.database.GameModeInventoriesQueueData;
import me.eccentric_nz.gamemodeinventories.database.GameModeInventoriesRecordingQueue;

public class GameModeInventoriesBlock {
   private final GameModeInventories plugin;

   public GameModeInventoriesBlock(GameModeInventories plugin) {
      this.plugin = plugin;
   }

   public void addBlock(String gmiwc, String l) {
      GameModeInventoriesQueueData data = new GameModeInventoriesQueueData(gmiwc, l);
      if (GameModeInventoriesConnectionPool.isIsMySQL()) {
         GameModeInventoriesRecordingQueue.addToQueue(data);
      } else {
         this.saveBlockNow(data);
      }

      if (this.plugin.getCreativeBlocks().containsKey(gmiwc)) {
         ((List)this.plugin.getCreativeBlocks().get(gmiwc)).add(l);
      } else {
         this.plugin.getCreativeBlocks().put(gmiwc, new ArrayList(Arrays.asList(l)));
      }

   }

   public void removeBlock(String gmiwc, String l) {
      Connection connection = null;
      PreparedStatement ps = null;

      try {
         connection = GameModeInventoriesConnectionPool.dbc();
         String deleteQuery = "DELETE FROM blocks WHERE worldchunk = ? AND location = ?";
         ps = connection.prepareStatement(deleteQuery);
         ps.setString(1, gmiwc);
         ps.setString(2, l);
         ps.executeUpdate();
         if (GameModeInventoriesConnectionPool.isIsMySQL()) {
            connection.close();
         }
      } catch (SQLException var14) {
         System.err.println("Could not remove block, " + var14);
      } finally {
         try {
            if (ps != null) {
               ps.close();
            }

            if (connection != null && GameModeInventoriesConnectionPool.isIsMySQL()) {
               connection.close();
            }
         } catch (SQLException var13) {
            System.err.println("Could not remove block, " + var13);
         }

      }

      if (this.plugin.getCreativeBlocks().containsKey(gmiwc)) {
         ((List)this.plugin.getCreativeBlocks().get(gmiwc)).remove(l);
      }

   }

   private void saveBlockNow(GameModeInventoriesQueueData data) {
      Connection connection = null;
      PreparedStatement ps = null;

      try {
         connection = GameModeInventoriesConnectionPool.dbc();
         String insertQuery = "INSERT INTO blocks (worldchunk, location) VALUES (?,?)";
         ps = connection.prepareStatement(insertQuery);
         ps.setString(1, data.getWorldChunk());
         ps.setString(2, data.getLocation());
         ps.executeUpdate();
      } catch (SQLException var13) {
         System.err.println("Could not save block, " + var13);
      } finally {
         try {
            if (ps != null) {
               ps.close();
            }

            if (connection != null && GameModeInventoriesConnectionPool.isIsMySQL()) {
               connection.close();
            }
         } catch (SQLException var12) {
            System.err.println("Could not remove block, " + var12);
         }

      }

   }
}

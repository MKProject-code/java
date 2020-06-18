package me.eccentric_nz.gamemodeinventories.database;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.eccentric_nz.gamemodeinventories.GMIDebug;
import me.eccentric_nz.gamemodeinventories.GameModeInventories;

public class GameModeInventoriesBlocksConverter {
   private final GameModeInventories plugin;

   public GameModeInventoriesBlocksConverter(GameModeInventories plugin) {
      this.plugin = plugin;
   }

   public static void processUpdateCounts(int[] updateCounts) {
      for(int i = 0; i < updateCounts.length; ++i) {
         if (updateCounts[i] < 0 && updateCounts[i] != -2 && updateCounts[i] == -3) {
         }
      }

   }

   public void convertBlocksTable() {
      Connection connection = null;
      PreparedStatement statement = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {
         try {
            connection = GameModeInventoriesConnectionPool.dbc();
            statement = connection.prepareStatement("SELECT id, location FROM blocks");
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
               ps = connection.prepareStatement("UPDATE blocks SET worldchunk = ? WHERE id = ?");
               connection.setAutoCommit(false);
               long count = 0L;

               while(rs.next()) {
                  String l = rs.getString("location");
                  String[] first = l.split(",");
                  String[] wStr = first[0].split("=");
                  String[] xStr = first[1].split("=");
                  String[] zStr = first[3].split("=");
                  String w = wStr[2].substring(0, wStr[2].length() - 1);
                  int x = Integer.parseInt(xStr[1].substring(0, xStr[1].length() - 2)) >> 4;
                  int z = Integer.parseInt(zStr[1].substring(0, zStr[1].length() - 2)) >> 4;
                  ps.setString(1, w + "," + x + "," + z);
                  ps.setInt(2, rs.getInt("id"));
                  ps.addBatch();
                  ++count;
                  if (count == 1000L) {
                     int[] updateCounts = ps.executeBatch();
                     processUpdateCounts(updateCounts);
                     connection.commit();
                     count = 0L;
                  }
               }
            }
         } catch (BatchUpdateException var25) {
            int[] updateCounts = var25.getUpdateCounts();
            processUpdateCounts(updateCounts);
            if (connection != null) {
               connection.rollback();
            }
         }
      } catch (SQLException var26) {
         this.plugin.debug("Blocks updater error: " + var26.getMessage(), GMIDebug.ERROR);
      } finally {
         try {
            if (ps != null) {
               ps.close();
            }

            if (rs != null) {
               rs.close();
            }

            if (statement != null) {
               statement.close();
            }

            if (connection != null && GameModeInventoriesConnectionPool.isIsMySQL()) {
               connection.setAutoCommit(true);
               connection.close();
            }
         } catch (SQLException var24) {
            this.plugin.debug("Blocks closing error: " + var24.getMessage(), GMIDebug.ERROR);
         }

      }

   }
}

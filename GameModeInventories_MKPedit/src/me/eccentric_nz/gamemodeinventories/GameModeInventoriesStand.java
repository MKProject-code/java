package me.eccentric_nz.gamemodeinventories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.UUID;
import me.eccentric_nz.gamemodeinventories.database.GameModeInventoriesConnectionPool;

public class GameModeInventoriesStand {
   private final GameModeInventories plugin;
   private Connection connection = null;

   public GameModeInventoriesStand(GameModeInventories plugin) {
      this.plugin = plugin;
   }

   public void loadStands() {
      PreparedStatement statement = null;
      ResultSet rs = null;
      if (this.plugin.getConfig().getBoolean("track_creative_place.enabled")) {
         try {
            this.connection = GameModeInventoriesConnectionPool.dbc();
            statement = this.connection.prepareStatement("SELECT uuid FROM stands");
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
               while(rs.next()) {
                  this.plugin.getStands().add(UUID.fromString(rs.getString("uuid")));
               }
            }

            statement = this.connection.prepareStatement("DELETE FROM stands");
            statement.executeUpdate();
         } catch (SQLException var12) {
            System.err.println("Could not load stands, " + var12);
         } finally {
            try {
               if (statement != null) {
                  statement.close();
               }

               if (rs != null) {
                  rs.close();
               }

               if (this.connection != null && GameModeInventoriesConnectionPool.isIsMySQL()) {
                  this.connection.close();
               }
            } catch (SQLException var11) {
               this.plugin.debug("Error closing stands statement or resultset: " + var11.getMessage());
            }

         }
      }

   }

   public void saveStands() {
      PreparedStatement ps = null;

      try {
         this.connection = GameModeInventoriesConnectionPool.dbc();
         ps = this.connection.prepareStatement("INSERT INTO stands (uuid) VALUES (?)");
         Iterator var2 = this.plugin.getStands().iterator();

         while(var2.hasNext()) {
            UUID uuid = (UUID)var2.next();
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
         }
      } catch (SQLException var12) {
         System.err.println("Could not save stands, " + var12);
      } finally {
         try {
            if (ps != null) {
               ps.close();
            }

            if (this.connection != null && GameModeInventoriesConnectionPool.isIsMySQL()) {
               this.connection.close();
            }
         } catch (SQLException var11) {
            this.plugin.debug("Error closing stands statement: " + var11.getMessage());
         }

      }

   }
}

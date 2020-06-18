package me.eccentric_nz.gamemodeinventories.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.eccentric_nz.gamemodeinventories.GameModeInventories;

public class GameModeInventoriesMySQL {
   private final GameModeInventories plugin;

   public GameModeInventoriesMySQL(GameModeInventories plugin) {
      this.plugin = plugin;
   }

   public void createTables() {
      Connection connection = null;
      Statement statement = null;
      ResultSet rsAttr = null;
      ResultSet rsWorld = null;

      try {
         connection = GameModeInventoriesConnectionPool.dbc();
         statement = connection.createStatement();
         String queryInventories = "CREATE TABLE IF NOT EXISTS inventories (id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', player varchar(24) DEFAULT '', gamemode varchar(24) DEFAULT '', inventory text, xp double, armour text, enderchest text, attributes text, armour_attributes text, PRIMARY KEY (id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
         statement.executeUpdate(queryInventories);
         String queryAttr = "SHOW COLUMNS FROM inventories LIKE 'attributes'";
         rsAttr = statement.executeQuery(queryAttr);
         String queryBlocks;
         String queryWorld;
         if (!rsAttr.next()) {
            queryBlocks = "ALTER TABLE inventories ADD attributes text";
            statement.executeUpdate(queryBlocks);
            queryWorld = "ALTER TABLE inventories ADD armour_attributes text";
            statement.executeUpdate(queryWorld);
            System.out.println("[GameModeInventories] Adding attributes to database!");
         }

         queryBlocks = "CREATE TABLE IF NOT EXISTS blocks (id int(11) NOT NULL AUTO_INCREMENT, worldchunk varchar(128), location text, PRIMARY KEY (id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
         statement.executeUpdate(queryBlocks);
         queryWorld = "SHOW COLUMNS FROM blocks LIKE 'worldchunk'";
         rsWorld = statement.executeQuery(queryWorld);
         String queryStands;
         if (!rsWorld.next()) {
            queryStands = "ALTER TABLE blocks ADD worldchunk varchar(128)";
            statement.executeUpdate(queryStands);
            System.out.println("[GameModeInventories] Adding new fields to database!");
         }

         queryStands = "CREATE TABLE IF NOT EXISTS stands (uuid varchar(48) NOT NULL, PRIMARY KEY (uuid)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
         statement.executeUpdate(queryStands);
         String queryWorlds = "CREATE TABLE IF NOT EXISTS worlds (id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', world varchar(24) DEFAULT '', x double, y double, z double, yaw float, pitch float, PRIMARY KEY (id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
         statement.executeUpdate(queryWorlds);
      } catch (SQLException var19) {
         this.plugin.getServer().getConsoleSender().sendMessage(this.plugin.MY_PLUGIN_NAME + "MySQL create table error: " + var19);
      } finally {
         try {
            if (rsWorld != null) {
               rsWorld.close();
            }

            if (rsAttr != null) {
               rsAttr.close();
            }

            if (statement != null) {
               statement.close();
            }

            if (connection != null) {
               connection.close();
            }
         } catch (SQLException var18) {
            this.plugin.getServer().getConsoleSender().sendMessage(this.plugin.MY_PLUGIN_NAME + "MySQL close statement error: " + var18);
         }

      }

   }
}

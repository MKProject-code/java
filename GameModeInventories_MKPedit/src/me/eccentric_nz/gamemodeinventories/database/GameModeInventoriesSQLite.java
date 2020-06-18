package me.eccentric_nz.gamemodeinventories.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.eccentric_nz.gamemodeinventories.GameModeInventories;

public class GameModeInventoriesSQLite {
   private final GameModeInventories plugin;

   public GameModeInventoriesSQLite(GameModeInventories plugin) {
      this.plugin = plugin;
   }

   public void createTables() {
      Connection connection = null;
      Statement statement = null;
      ResultSet rsUUID = null;
      ResultSet rsXP = null;
      ResultSet rsArmour = null;
      ResultSet rsEnder = null;
      ResultSet rsAttr = null;
      ResultSet rsWorld = null;

      try {
         connection = GameModeInventoriesConnectionPool.dbc();
         statement = connection.createStatement();
         String queryInventories = "CREATE TABLE IF NOT EXISTS inventories (id INTEGER PRIMARY KEY NOT NULL, uuid TEXT, player TEXT, gamemode TEXT, inventory TEXT, xp REAL, armour TEXT, enderchest TEXT, attributes TEXT, armour_attributes TEXT)";
         statement.executeUpdate(queryInventories);
         String queryUUID = "SELECT sql FROM sqlite_master WHERE tbl_name = 'inventories' AND sql LIKE '%uuid TEXT%'";
         rsUUID = statement.executeQuery(queryUUID);
         String queryXP;
         if (!rsUUID.next()) {
            queryXP = "ALTER TABLE inventories ADD uuid TEXT";
            statement.executeUpdate(queryXP);
            System.out.println("[GameModeInventories] Adding UUID to database!");
         }

         queryXP = "SELECT sql FROM sqlite_master WHERE tbl_name = 'inventories' AND sql LIKE '%xp REAL%'";
         rsXP = statement.executeQuery(queryXP);
         String queryArmour;
         if (!rsXP.next()) {
            queryArmour = "ALTER TABLE inventories ADD xp REAL";
            statement.executeUpdate(queryArmour);
            System.out.println("[GameModeInventories] Adding xp to database!");
         }

         queryArmour = "SELECT sql FROM sqlite_master WHERE tbl_name = 'inventories' AND sql LIKE '%armour TEXT%'";
         rsArmour = statement.executeQuery(queryArmour);
         String queryEnder;
         if (!rsArmour.next()) {
            queryEnder = "ALTER TABLE inventories ADD armour TEXT";
            statement.executeUpdate(queryEnder);
            System.out.println("[GameModeInventories] Adding armour to database!");
         }

         queryEnder = "SELECT sql FROM sqlite_master WHERE tbl_name = 'inventories' AND sql LIKE '%enderchest TEXT%'";
         rsEnder = statement.executeQuery(queryEnder);
         String queryAttr;
         if (!rsEnder.next()) {
            queryAttr = "ALTER TABLE inventories ADD enderchest TEXT";
            statement.executeUpdate(queryAttr);
            System.out.println("[GameModeInventories] Adding enderchest to database!");
         }

         queryAttr = "SELECT sql FROM sqlite_master WHERE tbl_name = 'inventories' AND sql LIKE '%attributes TEXT%'";
         rsAttr = statement.executeQuery(queryAttr);
         String queryBlocks;
         String queryWorld;
         if (!rsAttr.next()) {
            queryBlocks = "ALTER TABLE inventories ADD attributes TEXT";
            statement.executeUpdate(queryBlocks);
            queryWorld = "ALTER TABLE inventories ADD armour_attributes TEXT";
            statement.executeUpdate(queryWorld);
            System.out.println("[GameModeInventories] Adding attributes to database!");
         }

         queryBlocks = "CREATE TABLE IF NOT EXISTS blocks (id INTEGER PRIMARY KEY NOT NULL, worldchunk TEXT, location TEXT)";
         statement.executeUpdate(queryBlocks);
         queryWorld = "SELECT sql FROM sqlite_master WHERE tbl_name = 'blocks' AND sql LIKE '%worldchunk TEXT%'";
         rsWorld = statement.executeQuery(queryWorld);
         String queryStands;
         if (!rsWorld.next()) {
            queryStands = "ALTER TABLE blocks ADD worldchunk TEXT";
            statement.executeUpdate(queryStands);
            System.out.println("[GameModeInventories] Adding new fields to database!");
         }

         queryStands = "CREATE TABLE IF NOT EXISTS stands (uuid TEXT PRIMARY KEY NOT NULL)";
         statement.executeUpdate(queryStands);
         String queryWorlds = "CREATE TABLE IF NOT EXISTS worlds (id INTEGER PRIMARY KEY NOT NULL, uuid TEXT, world TEXT, x REAL, y REAL, z REAL, yaw REAL, pitch REAL)";
         statement.executeUpdate(queryWorlds);
         statement.close();
      } catch (SQLException var27) {
         this.plugin.getServer().getConsoleSender().sendMessage(this.plugin.MY_PLUGIN_NAME + "SQLite create table error: " + var27);
      } finally {
         try {
            if (rsUUID != null) {
               rsUUID.close();
            }

            if (rsXP != null) {
               rsXP.close();
            }

            if (rsArmour != null) {
               rsArmour.close();
            }

            if (rsEnder != null) {
               rsEnder.close();
            }

            if (rsAttr != null) {
               rsAttr.close();
            }

            if (rsWorld != null) {
               rsWorld.close();
            }

            if (statement != null) {
               statement.close();
            }

            if (connection != null && GameModeInventoriesConnectionPool.isIsMySQL()) {
               connection.close();
            }
         } catch (SQLException var26) {
            this.plugin.getServer().getConsoleSender().sendMessage(this.plugin.MY_PLUGIN_NAME + "SQLite close statement error: " + var26);
         }

      }

   }
}

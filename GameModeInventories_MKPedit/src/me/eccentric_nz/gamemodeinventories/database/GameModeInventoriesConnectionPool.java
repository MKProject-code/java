package me.eccentric_nz.gamemodeinventories.database;

import java.sql.Connection;
import java.sql.SQLException;
import me.eccentric_nz.com.zaxxer.hikari.HikariConfig;
import me.eccentric_nz.com.zaxxer.hikari.HikariDataSource;
import me.eccentric_nz.gamemodeinventories.GameModeInventories;

public class GameModeInventoriesConnectionPool {
   private static HikariDataSource hikari;
   private static boolean isMySQL = false;
   private static GameModeInventoriesSQLiteConnection service;

   public GameModeInventoriesConnectionPool(String path) {
      try {
         service = GameModeInventoriesSQLiteConnection.getINSTANCE();
         service.setConnection(path);
      } catch (Exception var3) {
         GameModeInventories.plugin.debug("Database connection failed. " + var3.getMessage());
      }

   }

   public GameModeInventoriesConnectionPool() throws ClassNotFoundException {
      isMySQL = true;
      Class.forName("com.mysql.jdbc.Driver");
      String host = GameModeInventories.plugin.getConfig().getString("storage.mysql.server");
      String port = GameModeInventories.plugin.getConfig().getString("storage.mysql.port");
      String databaseName = GameModeInventories.plugin.getConfig().getString("storage.mysql.database");
      String user = GameModeInventories.plugin.getConfig().getString("storage.mysql.user");
      String password = GameModeInventories.plugin.getConfig().getString("storage.mysql.password");
      int pool_size = GameModeInventories.plugin.getConfig().getInt("storage.mysql.pool_size");
      String url = String.format("jdbc:mysql://%s:%s/%s", host, port, databaseName);
      HikariConfig config = new HikariConfig();
      config.setMinimumIdle(1);
      config.setMaximumPoolSize(pool_size);
      config.setJdbcUrl(url);
      config.setUsername(user);
      config.setPassword(password);
      config.setDriverClassName("com.mysql.jdbc.Driver");
      config.addDataSourceProperty("databaseName", databaseName);
      config.addDataSourceProperty("cachePrepStmts", "true");
      config.addDataSourceProperty("prepStmtCacheSize", "250");
      config.addDataSourceProperty("prepStmtCacheSqlLimit", "1024");
      if (GameModeInventories.plugin.getConfig().getBoolean("storage.mysql.test_connection")) {
         config.setConnectionTestQuery("SELECT 1");
      }

      hikari = new HikariDataSource(config);
   }

   public static Connection dbc() {
      Connection con = null;
      if (isMySQL) {
         try {
            con = hikari.getConnection();
         } catch (SQLException var2) {
            GameModeInventories.plugin.debug("Could not get database connection: " + var2.getMessage());
         }
      } else {
         service = GameModeInventoriesSQLiteConnection.getINSTANCE();
         con = service.getConnection();
      }

      return con;
   }

   public static boolean isIsMySQL() {
      return isMySQL;
   }
}

package me.eccentric_nz.gamemodeinventories.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class GameModeInventoriesSQLiteConnection {
   private static final GameModeInventoriesSQLiteConnection INSTANCE = new GameModeInventoriesSQLiteConnection();
   public Connection connection = null;

   public static synchronized GameModeInventoriesSQLiteConnection getINSTANCE() {
      return INSTANCE;
   }

   public Connection getConnection() {
      return this.connection;
   }

   public void setConnection(String path) throws Exception {
      Class.forName("org.sqlite.JDBC");
      this.connection = DriverManager.getConnection("jdbc:sqlite:" + path);
   }

   protected Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException("Clone is not allowed.");
   }
}

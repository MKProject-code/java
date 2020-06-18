package mkproject.maskat.SpawnManager;

import mkproject.maskat.Papi.Papi;

public class Database {
	
	public static void initialize(Plugin plugin) {
		Users.TABLE = plugin.getConfig().getString("MySQL.Table.Users")+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		
		if(Papi.DEVELOPER_DATABASE && Papi.DEVELOPER_DATABASE_AUTODELETE)
			Papi.MySQL.deleteTable(Users.TABLE);
		
		Papi.MySQL.createTable(Users.TABLE,
					Papi.SQL.createColumnParse(Database.Users.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Database.Users.USERNAME, Papi.SQL.ValType.VARCHAR, 32, true, false, true)
				+","+Papi.SQL.createColumnParse(Database.Users.PLAYER_SURVIVAL_SPAWN_LOCATION, Papi.SQL.ValType.VARCHAR, 256, false)
				+","+Papi.SQL.createColumnParse(Database.Users.PLAYER_SURVIVAL_SPAWN_GENERATED, Papi.SQL.ValType.BOOLEAN, 1, true, false, false, "0")
				+","+Papi.SQL.createColumnParse(Database.Users.PLAYER_SURVIVAL_LAST_LOCATION, Papi.SQL.ValType.VARCHAR, 256, false)
		);
	}
	
	public static class Users {
		public static String TABLE;
		public static final String ID = "userid";
		public static final String USERNAME = "username";
		public static final String PLAYER_SURVIVAL_SPAWN_LOCATION = "player_survival_spawn_location";
		public static final String PLAYER_SURVIVAL_SPAWN_GENERATED = "player_survival_spawn_generated";
		public static final String PLAYER_SURVIVAL_LAST_LOCATION = "player_survival_last_location";
	}
}

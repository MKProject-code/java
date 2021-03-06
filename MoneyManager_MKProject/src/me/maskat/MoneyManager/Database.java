package me.maskat.MoneyManager;

import mkproject.maskat.Papi.Papi;

public class Database {
	
	protected static void initialize() {
		Users.TABLE = Plugin.getPlugin().getConfig().getString("MySQL.Table.Users")+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		
		if(Papi.DEVELOPER_DATABASE && Papi.DEVELOPER_DATABASE_AUTODELETE)
			Papi.MySQL.deleteTable(Users.TABLE);
		
		Papi.MySQL.createTable(Users.TABLE,
					Papi.SQL.createColumnParse(Database.Users.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Database.Users.NAME, Papi.SQL.ValType.VARCHAR, 32, true, false, true)
				+","+Papi.SQL.createColumnParse(Database.Users.POINTS, Papi.SQL.ValType.DOUBLE, -1, false)
				+","+Papi.SQL.createColumnPrimary(Database.Users.ID)
		);
	}
	
	protected static class Users {
		public static String TABLE;
		public static final String ID = "userid";
		public static final String NAME = "username";
		public static final String POINTS = "points";
	}
}

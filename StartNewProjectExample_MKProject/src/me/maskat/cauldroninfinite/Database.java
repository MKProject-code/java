package me.maskat.cauldroninfinite;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;

public class Database {
	
	public static void Initialize(Plugin plugin) {
		Portals.TABLE = plugin.getConfig().getString("MySQL.Table.Portals")+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		
		if(Papi.DEVELOPER_DATABASE && Papi.DEVELOPER_DATABASE_AUTODELETE)
			Papi.MySQL.deleteTable(Portals.TABLE);
		
		Papi.MySQL.createTable(Portals.TABLE,
					Papi.SQL.createColumnParse(Database.Portals.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Database.Portals.NAME, Papi.SQL.ValType.VARCHAR, 32, true, false, true)
				+","+Papi.SQL.createColumnParse(Database.Portals.PORTAL_LOCATION_FIRST, Papi.SQL.ValType.VARCHAR, 32, true)
				+","+Papi.SQL.createColumnParse(Database.Portals.PORTAL_LOCATION_SECOUND, Papi.SQL.ValType.VARCHAR, 32, true)
				+","+Papi.SQL.createColumnParse(Database.Portals.DESTINATION_POSITION, Papi.SQL.ValType.VARCHAR, 32, true)
				+","+Papi.SQL.createColumnPrimary(Database.Portals.ID)
		);
	}
	
	public static class Portals {
		public static String TABLE;
		public static final String ID = "userid";
		public static final String NAME = "username";
		public static final String PORTAL_LOCATION_FIRST = "portallocationfirst";
		public static final String PORTAL_LOCATION_SECOUND = "portallocationsecound";
		public static final String DESTINATION_POSITION = "destinationposition";
	}
}

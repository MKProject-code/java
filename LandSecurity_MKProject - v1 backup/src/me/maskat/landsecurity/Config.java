package me.maskat.landsecurity;

public class Config {
	public static String databaseTableRegions = "landsecurity_develop_regions";
	public static String databaseTableUsers = "landsecurity_develop_users";
	public static String databaseTableWolves = "landsecurity_develop_wolves";
	
	public static String allowWorld = "world";
	public static int wolfProtectedRadiusStart = 5;
	
	public static boolean wolfAlwaysSilent = true;
	
	public static boolean debug = false;
	public static boolean developDB = false;
	public static boolean deleteDB = false;
	
	public static void init() {
		if(!developDB) {
			databaseTableRegions = LandSecurity.plugin.config.getString("database.table.regions");
			databaseTableUsers = LandSecurity.plugin.config.getString("database.table.users");
			databaseTableWolves = LandSecurity.plugin.config.getString("database.table.wolves");
		}
		//wolfguard_world = LandSecurity.plugin.config.getString("wolfguard.world");
	}
}

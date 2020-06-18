package me.maskat.wolfsecurity;

public class Config {
	public static String databaseTableRegions = "wolfsecurity_develop_regions";
	public static String databaseTableUsers = "wolfsecurity_develop_users";
	public static String databaseTableWolves = "wolfsecurity_develop_wolves";
	
	public static String allowWorld = "world";
	public static int wolfProtectedRadiusStart = 4;
	public static boolean wolfAlwaysSilent = true;
	
	public static boolean debug = false;
	public static boolean debug2 = false;
	public static boolean debugWolfFixInfo = false;
	public static boolean developDB = false;
	public static boolean deleteDB = false;
	
	public static void init() {
		if(!developDB) {
			databaseTableRegions = Plugin.plugin.config.getString("database.table.regions");
			databaseTableUsers = Plugin.plugin.config.getString("database.table.users");
			databaseTableWolves = Plugin.plugin.config.getString("database.table.wolves");
		}
		//wolfguard_world = LandSecurity.plugin.config.getString("wolfguard.world");
	}
}

package me.maskat.wolfsecurity;

public class Config {
	public static String databaseTableRegions = "wolfsecurity_develop_regions";
	public static String databaseTableUsers = "wolfsecurity_develop_users";
	public static String databaseTableWolves = "wolfsecurity_develop_wolves";
	
	public static final String allowedWorld = "world";
	public static final int wolfProtectedRadiusStart = 4;
	public static final boolean wolfAlwaysSilent = true;
	
	public static final boolean debug = false;
	public static final boolean debug2 = false;
	public static final boolean debugWolfFixInfo = false;
	public static final boolean developDB = false;
	public static final boolean deleteDB = false;
	
	public static final String permissionRegionProtect = "mkp.wolfsecurity.region.protect";
	
	public static void init() {
		if(!developDB) {
			databaseTableRegions = Plugin.plugin.config.getString("database.table.regions");
			databaseTableUsers = Plugin.plugin.config.getString("database.table.users");
			databaseTableWolves = Plugin.plugin.config.getString("database.table.wolves");
		}
		//wolfguard_world = LandSecurity.plugin.config.getString("wolfguard.world");
	}
}

package mkproject.maskat.AdminUtils;

import java.util.HashMap;
import java.util.Map;

public class Config {
	private static Map<String, Object> configuration = new HashMap<String, Object>();
	
	public static class ConfigKey {
		public static final String MySQLTableMutes = "MySQL.Table.Mutes";
		public static final String MySQLTableKicks = "MySQL.Table.Kicks";
		public static final String MySQLTableBans = "MySQL.Table.Bans";
		public static final String PermissionPrefix = "Permission.Prefix";
	}
	
	public static void initialize() {
		configuration.put(ConfigKey.MySQLTableMutes, "MKP_AdminUtils_Mutes");
		configuration.put(ConfigKey.MySQLTableKicks, "MKP_AdminUtils_Kicks");
		configuration.put(ConfigKey.MySQLTableBans, "MKP_AdminUtils_Bans");
		configuration.put(ConfigKey.PermissionPrefix, "mkp.adminutils");
	}
	
	public static String getString(String string) {
		return String.valueOf(configuration.get(string));
	}
	
}

package mkproject.maskat.BungeeManager;

import java.util.HashMap;
import java.util.Map;

public class Config {
	private static Map<String, Object> configuration = new HashMap<String, Object>();
	
	public static class ConfigKey {
		public static final String PermissionPrefix = "Permission.Prefix";
	}
	
	public static void initialize() {
		configuration.put(ConfigKey.PermissionPrefix, "mkp.bungeemanager");
	}
	
	public static String getString(String string) {
		return String.valueOf(configuration.get(string));
	}
	
}

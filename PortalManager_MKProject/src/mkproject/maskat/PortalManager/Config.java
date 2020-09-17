package mkproject.maskat.PortalManager;

import java.util.HashMap;
import java.util.Map;

public class Config {
	private static Map<String, Object> configuration = new HashMap<String, Object>();
	
	public static class ConfigKey {
//		public static final String CustomPortalsWorldName = "CustomPortalsWorldName";
		public static final String ServerSpawnWorldName = "ServerSpawnWorldName";
		public static final String RandomTpWorldName = "RandomTpWorldName";
	}
	
	public static void Initialize() {
		configuration.put("MySQL.Table.Portals", "MKP_PortalManager_Portals");
//		configuration.put(ConfigKey.CustomPortalsWorldName, "world_spawncenter");
		configuration.put(ConfigKey.ServerSpawnWorldName, "world");
		configuration.put(ConfigKey.RandomTpWorldName, "world");
	}
	
	public static String getString(String string) {
		return String.valueOf(configuration.get(string));
	}
	
}

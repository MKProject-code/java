package me.maskat.InventoryManager;

import java.util.HashMap;
import java.util.Map;

public class Config {
	private static Map<String, Object> configuration = new HashMap<String, Object>();
	
	protected static class ConfigKey {
//		public static final String CustomPortalsWorldName = "CustomPortalsWorldName";
//		public static final String ServerSpawnWorldName = "ServerSpawnWorldName";
//		public static final String RandomTpWorldName = "RandomTpWorldName";
	}
	
	protected static void initialize() {
		configuration.put("MySQL.Table.Inventories", "MKP_InventoryManager_Inventories");
//		configuration.put(ConfigKey.CustomPortalsWorldName, "world_spawncenter");
//		configuration.put(ConfigKey.ServerSpawnWorldName, "world");
//		configuration.put(ConfigKey.RandomTpWorldName, "world");
	}
	
	protected static String getString(String string) {
		return String.valueOf(configuration.get(string));
	}
	
}

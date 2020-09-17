package mkproject.maskat.DropManager;

import org.bukkit.configuration.file.FileConfiguration;

public class Lang {
	public static FileConfiguration getConfig() {
		return Plugin.getPlugin().getConfig();
	}
	public static String getGetChest(String item) {
		return getConfig().getString("Language.GetChest").replace("%item%", item);
	}
}
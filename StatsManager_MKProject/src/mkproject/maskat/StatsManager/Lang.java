package mkproject.maskat.StatsManager;

import org.bukkit.configuration.file.FileConfiguration;

public class Lang {
	public static FileConfiguration getConfig() {
		return Plugin.getPlugin().getConfig();
	}
}
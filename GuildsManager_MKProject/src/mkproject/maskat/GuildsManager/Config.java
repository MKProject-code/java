package mkproject.maskat.GuildsManager;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	public static FileConfiguration getConfig() {
		return Plugin.getPlugin().getConfig();
	}

}

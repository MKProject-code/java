package mkproject.maskat.SpecialTools;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	public static FileConfiguration getConfig() {
		return Plugin.getPlugin().getConfig();
	}
	public static class DropSpecialBone {
		public static List<String> getMaterialsToDrop() {
			return getConfig().getStringList("Drops.SpecialBone.MaterialsToDrop");
		}
		public static int getProcentageChance() {
			return getConfig().getInt("Drops.SpecialBone.DropProcentageChance");
		}
	}
}

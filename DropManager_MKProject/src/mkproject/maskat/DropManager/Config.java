package mkproject.maskat.DropManager;

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
		public static int getChance() {
			return getConfig().getInt("Drops.SpecialBone.DropChance");
		}
	}
	public static class VipPotion {
		public static List<String> getMaterialsToDrop() {
			return getConfig().getStringList("Drops.VipPotion.MaterialsToDrop");
		}
		public static int getChance() {
			return getConfig().getInt("Drops.VipPotion.DropChance");
		}
	}
	public static class VipBook {
		public static List<String> getMaterialsToDrop() {
			return getConfig().getStringList("Drops.VipBook.MaterialsToDrop");
		}
		public static int getChance() {
			return getConfig().getInt("Drops.VipBook.DropChance");
		}
	}
}

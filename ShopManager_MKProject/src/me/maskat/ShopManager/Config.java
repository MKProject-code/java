package me.maskat.ShopManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.entity.Player;

abstract public class Config {
	public static int getResetTheEndPrice() {
		return Plugin.getPlugin().getConfig().getInt("ResetTheEnd.Price");
	}
	public static void setResetTheEndLastResetInfo(Player player) {
		Plugin.getPlugin().getConfig().set("ResetTheEnd.LastReset.DateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
		Plugin.getPlugin().getConfig().set("ResetTheEnd.LastReset.PlayerName", player.getName());
		Plugin.getPlugin().saveConfig();
	}
	public static String getResetTheEndLastResetDateTime() {
		return Plugin.getPlugin().getConfig().getString("ResetTheEnd.LastReset.DateTime");
	}
	public static String getResetTheEndLastResetPlayerName() {
		return Plugin.getPlugin().getConfig().getString("ResetTheEnd.LastReset.PlayerName");
	}
}

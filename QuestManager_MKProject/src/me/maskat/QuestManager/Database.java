package me.maskat.QuestManager;

import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public abstract class Database {
	
	public static void setScore(Player player, int score) {
		if(!Plugin.getPlugin().getConfig().contains("currentData."+player.getName().toLowerCase()+".realname"))
			Plugin.getPlugin().getConfig().set("currentData."+player.getName().toLowerCase()+".realname", player.getName());
		
		Plugin.getPlugin().getConfig().set("currentData."+player.getName().toLowerCase()+".score", score);
		Plugin.getPlugin().saveConfig();
	}

	public static int getScore(Player player) {
		return Plugin.getPlugin().getConfig().getInt("currentData."+player.getName().toLowerCase()+".score");
	}
	
	public static String getRealName(Player player) {
		return Plugin.getPlugin().getConfig().getString("currentData."+player.getName().toLowerCase()+".realname");
	}
	
	public static int getScore(String playerNameLowerCase) {
		return Plugin.getPlugin().getConfig().getInt("currentData."+playerNameLowerCase+".score");
	}
	
	public static String getRealName(String playerNameLowerCase) {
		return Plugin.getPlugin().getConfig().getString("currentData."+playerNameLowerCase+".realname");
	}
	
	public static Set<String> getPlayers() {
		ConfigurationSection configSection = Plugin.getPlugin().getConfig().getConfigurationSection("currentData");
		if(configSection == null)
			return null;
		
		return configSection.getKeys(false);
	}
	
}

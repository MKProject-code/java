package me.maskat.compasspoint;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.maskat.compasspoint.compass.Compass;
import me.maskat.compasspoint.models.ModelPlayer;

public class Plugin extends JavaPlugin {
	private static Plugin plugin;
	
	private static Map<Player, ModelPlayer> mapPlayers = new HashMap<>();
	
	@Override
	public void onEnable() {
		plugin = this;
		
		getServer().getPluginManager().registerEvents(new Event(), this);
		
		Compass.start();
		getLogger().info("[CompassPoint] Has been enabled!");
	}
	
	public static Plugin getPlugin()
	{
		return plugin;
	}
	
	public static Map<Player, ModelPlayer> getPlayersMap() {
		return mapPlayers;
	}
}

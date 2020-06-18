package me.maskat.ArenaPVP;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import me.maskat.ArenaManager.ArenaPlugin.ArenaPluginManager;
import me.maskat.ArenaPVP.enums.Team;

public class Plugin extends JavaPlugin {
	public static Plugin plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		Event eventHandler = new Event();
		
		getServer().getPluginManager().registerEvents(eventHandler, this);
		
		ArenaPluginManager.registerArenaPlugin(new PvP_ArenaPlugin(), "PvP", List.of(Team.PLAYER_ONE.name(), Team.PLAYER_TWO.name()), null);
		
		getLogger().info("Has been enabled!");
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
}

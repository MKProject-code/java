package me.maskat.MoneyManager;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	public static Plugin plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		Event eventHandler = new Event();
		
		Database.initialize();
		
		getServer().getPluginManager().registerEvents(eventHandler, this);
		
		Task.runUpdatePlayersPointsTask(10*60); // 10 minut
		
		getLogger().info("Has been enabled!");
	}
	
	@Override
	public void onDisable() {
		for(Player player : getServer().getOnlinePlayers()) {
			MapiModel.getPlayer(player).saveData();
		}
	}
	
	protected static Plugin getPlugin() {
		return plugin;
	}
}

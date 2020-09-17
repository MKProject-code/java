package me.maskat.InventoryManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	public static Plugin plugin;
	public static Task task;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		//ExecuteCommand executeCommand = new ExecuteCommand();
		Event eventHandler = new Event();
		
		getServer().getPluginManager().registerEvents(eventHandler, this);
		
		//getCommand("mycommand").setExecutor(executeCommand);
		
		Config.initialize();
		Database.initialize(this);
		
		task = new Task();
		Bukkit.getScheduler().runTaskTimer(this, task, 6000, 6000);
		
		getLogger().info("Has been enabled!");
	}
	
	@Override
	public void onDisable() {
		task.run();
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
}

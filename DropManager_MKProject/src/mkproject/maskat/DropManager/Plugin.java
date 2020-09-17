package mkproject.maskat.DropManager;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	private static Plugin plugin;
	
	public void onEnable() {
		plugin = this;
		
		ExecuteCommand executeCommand = new ExecuteCommand();
		Event eventHandler = new Event();
		
		getServer().getPluginManager().registerEvents(eventHandler, this);
		
		Database.initialize();
		
		getCommand("dropmanager").setExecutor(executeCommand);
		
		this.saveDefaultConfig();
		
		getLogger().info("Has been enabled!");
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}

	public static void reloadAllConfigs() {
		plugin.reloadConfig();
	}
}

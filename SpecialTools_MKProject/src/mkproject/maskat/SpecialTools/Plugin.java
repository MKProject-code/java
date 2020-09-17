package mkproject.maskat.SpecialTools;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	public static Plugin plugin;
	
	public void onEnable() {
		plugin = this;
		
//		ExecuteCommand executeCommand = new ExecuteCommand();
		Event eventHandler = new Event();
		
		getServer().getPluginManager().registerEvents(eventHandler, this);
		
		this.saveDefaultConfig();
		
		getLogger().info("Has been enabled!");
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
}

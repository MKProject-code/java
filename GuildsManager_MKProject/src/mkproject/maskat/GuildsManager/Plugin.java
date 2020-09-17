package mkproject.maskat.GuildsManager;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	public static Plugin plugin;
	public static String PERMISSION_PREFIX = "mkp.guildsmanager";
	
	public void onEnable() {
		plugin = this;
		
		Database.initialize(this);
		
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

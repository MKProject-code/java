package mkproject.maskat.PortalManager;

import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class Plugin extends JavaPlugin {
	public static Plugin plugin;
	public static WorldEditPlugin worldEditPlugin;
	
	public void onEnable() {
		plugin = this;
		
		Config.Initialize();
		Database.Initialize();
		
		ExecuteCommand executeCommand = new ExecuteCommand();
		Event eventHandler = new Event();
		
		getServer().getPluginManager().registerEvents(eventHandler, this);
		
		getCommand("portalmanager").setExecutor(executeCommand);
		
		worldEditPlugin = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
		
		getLogger().info("Has been enabled!");
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
}

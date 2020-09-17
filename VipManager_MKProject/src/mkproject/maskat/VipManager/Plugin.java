package mkproject.maskat.VipManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import mkproject.maskat.VipManager.Tasks.CheckValidVIP;
import net.luckperms.api.LuckPerms;

public class Plugin extends JavaPlugin {
	private static Plugin plugin;
	private static LuckPerms luckPerms;
	
	public void onEnable() {
		plugin = this;
		
//		ExecuteCommand executeCommand = new ExecuteCommand();
		Event eventHandler = new Event();
		
		getServer().getPluginManager().registerEvents(eventHandler, this);
		
		this.saveDefaultConfig();
		
		Database.initialize();
		
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider != null) {
		    luckPerms = provider.getProvider();
		}
		else
		{
			this.getLogger().warning("******************** LuckPerms not found ********************");
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		
		this.getServer().getScheduler().runTaskTimerAsynchronously(this, new CheckValidVIP(), 20L*60*9, 20L*60*9);
		
		getLogger().info("Has been enabled!");
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
	public static LuckPerms getLuckPerms() {
		return luckPerms;
	}
}

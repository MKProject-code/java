package me.maskat.PromoManager;

import org.bukkit.plugin.java.JavaPlugin;

import me.maskat.PromoManager.Cmds.CmdPromo;
import mkproject.maskat.Papi.Utils.CommandManager;

public class Plugin extends JavaPlugin {
	public static Plugin plugin;
	
	public void onEnable() {
		plugin = this;
		
		getServer().getPluginManager().registerEvents(new Event(), this);
		
		CommandManager.initCommand(this, "promo", new CmdPromo(), true);
		
		Database.initialize(this);
		
		getLogger().info("Has been enabled!");
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
}

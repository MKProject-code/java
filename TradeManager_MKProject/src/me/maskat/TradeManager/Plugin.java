package me.maskat.TradeManager;

import java.io.IOException;

import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import me.maskat.TradeManager.Cmds.CmdMenu;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.CommandManager;

public class Plugin extends JavaPlugin {
	private static Plugin plugin;
	public static String PERMISSION_PREFIX = "mkp.questmanager";
	
	@Override
	public void onEnable() {
		plugin = this;
		
		getServer().getPluginManager().registerEvents(new Event(), this);
		
		CommandManager.initCommand(this, "trade", new CmdMenu(), true);
		
		if(Papi.DEVELOPER_DIRECTORY_AUTODELETE) {
			try {
				FileUtils.deleteDirectory(getDataFolder());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.saveDefaultConfig();
		
		getLogger().info("Has been enabled!");
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
}

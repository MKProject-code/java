package me.maskat.ShopManager;

import java.io.IOException;

import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import me.maskat.ShopManager.Cmds.CmdShop;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.CommandManager;

public class Plugin extends JavaPlugin {
	private static Plugin plugin;
	public static String PERMISSION_PREFIX = "mkp.shopmanager";
	public static String CATEGORY_SALES_GENERATED = "SalesGenerated";
	
	@Override
	public void onEnable() {
		plugin = this;
		
		CommandManager.initCommand(this, "shop", new CmdShop(), false);
		
		if(Papi.DEVELOPER_DIRECTORY_AUTODELETE) {
			try {
				FileUtils.deleteDirectory(getDataFolder());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.saveDefaultConfig();
		
		Papi.Scheduler.registerTimerTask(new SchedulerTask(), null, 5, 0, 0);
		
		getLogger().info("Has been enabled!");
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
}

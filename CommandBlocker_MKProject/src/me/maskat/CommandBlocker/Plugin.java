package me.maskat.CommandBlocker;

import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import mkproject.maskat.Papi.Papi;

public class Plugin extends JavaPlugin {
	private static Plugin plugin;
	public static String PERMISSION_PREFIX = "mkp.shopmanager";
	public static String CATEGORY_SALES_GENERATED = "SalesGenerated";
	
	private static YamlConfiguration database;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		getServer().getPluginManager().registerEvents(new Event(), this);
		
		if(Papi.DEVELOPER_DIRECTORY_AUTODELETE) {
			try {
				FileUtils.deleteDirectory(getDataFolder());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		getCommand("commandblocker").setExecutor(new ExecuteCommand());
		
		this.saveDefaultConfig();
		
		database = Papi.Yaml.registerYaml(this, "database.yml");
		
		getLogger().info("Has been enabled!");
	}
	
	@Override
	public void onDisable() {
		Papi.Yaml.saveYaml(this, "database.yml", database);
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
	
	public static YamlConfiguration getDatabase() {
		return database;
	}

	public static void reloadAllConfigs() {
		plugin.reloadConfig();
	}
}

package me.maskat.QuestManager;

import java.io.IOException;

import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import me.maskat.QuestManager.Quests.QuestManager;
import mkproject.maskat.Papi.Papi;

public class Plugin extends JavaPlugin {
	private static Plugin plugin;
	private static QuestManager questManager;
	//public static String PERMISSION_PREFIX = "mkp.questmanager";
	
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
		
		this.saveDefaultConfig();
		
		getLogger().info("Has been enabled!");
		
		questManager = new QuestManager();
		
		Papi.Scheduler.registerTimerTask(new Scheduler(), null, 4, 0, 0);
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
	
	public static QuestManager getQuestManager() {
		return questManager;
	}
}

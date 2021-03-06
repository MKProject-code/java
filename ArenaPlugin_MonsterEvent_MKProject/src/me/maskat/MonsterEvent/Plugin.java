package me.maskat.MonsterEvent;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import me.maskat.ArenaManager.ArenaAPI.ApiArena;
import me.maskat.ArenaManager.ArenaPlugin.ArenaPluginManager;
import me.maskat.MonsterEvent.enums.Team;
import mkproject.maskat.Papi.Papi;

public class Plugin extends JavaPlugin {
	public static Plugin plugin;
	public static ApiArena apiArena;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		Event eventHandler = new Event();
		
		getServer().getPluginManager().registerEvents(eventHandler, this);
		ArenaPluginManager.registerArenaPlugin(null, "MonsterEvent", List.of(Team.MONSTERS.name()), null);
		
		getCommand("arenapluginmonsterevent").setExecutor(new ExecuteCommand());
		
		//SchedulerTask.allowMusicMapInitialize();
		Papi.Scheduler.registerTimerTask(new SchedulerTask(), null, 20, 0, 0);
		
		getLogger().info("Has been enabled!");
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}

	public static void reloadAllConfigs() {
		
	}
}

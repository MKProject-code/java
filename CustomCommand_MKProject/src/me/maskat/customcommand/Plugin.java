package me.maskat.customcommand;

import org.bukkit.plugin.java.JavaPlugin;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.CommandManager;

public class Plugin extends JavaPlugin {
	
    private static Plugin plugin;

	@Override
    public void onEnable() {
		plugin = this;
		
		this.saveDefaultConfig();
        Config.init(this.getConfig());
        
        CommandManager.initCommand(this, "konkurs", new ExecuteCommand(), true);
        CommandManager.initCommand(this, "konkursreload", new ExecuteCommandReload(), false);
        
        if(Papi.Scheduler.registerTimerTask(new SchedulerTask(), Config.EndYear, Config.EndMonth, Config.EndDay, 23, 59, 59))
        	this.getLogger().info("Scheduled task: "+String.format("%04d.%02d.%02d", Config.EndYear, Config.EndMonth, Config.EndDay)+"23:59:59");
        else
        	this.getLogger().info("********** WARNING!!! Task not sheduled at "+String.format("%04d.%02d.%02d", Config.EndYear, Config.EndMonth, Config.EndDay)+" 23:59:59");
    }
    
    public static Plugin getPlugin() {
    	return plugin;
    }
}
package me.maskat.ArenaManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import me.maskat.ArenaManager.Cmds.CmdArena;
import me.maskat.ArenaManager.Cmds.CmdArenaManager;
import me.maskat.ArenaManager.Models.ArenesModel;
import me.maskat.ArenaManager.Models.ModelArena;
import mkproject.maskat.Papi.Utils.CommandManager;

public class Plugin extends JavaPlugin {
	public static Plugin plugin;
	
	public void onEnable() {
		plugin = this;
		
		getServer().getPluginManager().registerEvents(new Event(), this);
		
		CommandManager.initCommand(this, "arena", new CmdArena(), true);
		CommandManager.initCommand(this, "arenamanager", new CmdArenaManager(), true);
		
		Database.initialize(this);
		
	    getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	    {
	        @Override
	        public void run()
	        {
	        	for(ModelArena modelArena : ArenesModel.getArenesMap().values())
	        	{
					if(!ArenesModel.existArenaType(modelArena.getType()))
					{
						Plugin.getPlugin().getLogger().warning("Arena type '"+modelArena.getType()+"' no registered! Arena ID:"+modelArena.getId()+" '"+modelArena.getName()+"' is disabled.");
						modelArena.setEnabled(false);
					}
					else
					{
						if(modelArena.isEnabled())
							Plugin.getPlugin().getLogger().info("Arena type '"+modelArena.getType()+"' loaded. Arena ID:"+modelArena.getId()+" '"+modelArena.getName()+"' is enabled.");
						else
							Plugin.getPlugin().getLogger().info("Arena type '"+modelArena.getType()+"' loaded. Arena ID:"+modelArena.getId()+" '"+modelArena.getName()+"' is disabled.");
					}
	        	}
	    		for(Team sbTeam : Bukkit.getScoreboardManager().getMainScoreboard().getTeams())
	    		{
	    			sbTeam.unregister();
	    			Plugin.getPlugin().getLogger().info("Unregistered scoreboard team: " + sbTeam.getName());
	    		}
	        }
	    });
		
		getLogger().info("Has been enabled!");
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
}

package me.maskat.ArenaManager.ArenaPlugin;

import java.util.List;

import me.maskat.ArenaManager.Plugin;
import me.maskat.ArenaManager.ArenaManager.Manager;
import me.maskat.ArenaManager.Models.ArenesModel;
import me.maskat.ArenaManager.Models.ModelArena;

public class ArenaPluginManager {
	
	//private static Map<String, ArenaPlugin> arenaPluginsMap = new HashMap<>(); //String = ArenaTypeName
	
	public static void registerArenaPlugin(ArenaPlugin plugin, String arenaType, List<String> teamTypes, List<String> objGroupTypes) {
		if(plugin != null)
		{
			Plugin.getPlugin().getLogger().info("Registering arena from '"+plugin.getClass().getName()+"' as type '"+arenaType+"'...");
			
			if(arenaType == null)
			{
				Plugin.getPlugin().getLogger().warning("Invalid parameter arenaType!");
				Plugin.getPlugin().getLogger().warning("Arena form '"+plugin.getClass().getName()+"' disabled!");
				return;
			}
			
			//if(arenaPluginsMap.containsKey(arenaType))
			if(ArenesModel.existArenaType(arenaType))
			{
				Plugin.getPlugin().getLogger().warning("This type of arena already exists!");
				Plugin.getPlugin().getLogger().warning("Arena form '"+plugin.getClass().getName()+"' disabled!");
				return;
			}
		}
		
		if(teamTypes != null && teamTypes.size() <= 0) teamTypes = null;
		if(objGroupTypes != null && objGroupTypes.size() <= 0) objGroupTypes = null;
		
		//arenaPluginsMap.put(arenaType, plugin);
		ArenesModel.addArenaType(arenaType, plugin, teamTypes, objGroupTypes);
		
		Plugin.getPlugin().getLogger().info("Arena type '"+arenaType+"' successfully registered!");
	}
	
	public static boolean doStartGame(ArenaInstance arenaInstance, int startSecoundDelay) {
		for(ModelArena modelArena : ArenesModel.getArenesMap().values()) {
			if(modelArena.getArenaPluginInstance() == arenaInstance) {
				Manager.doStartGame(modelArena, startSecoundDelay);
				return true;
			}
		}
		return false;
	}
	
	public static boolean doEndGame(ArenaInstance arenaInstance) {
		for(ModelArena modelArena : ArenesModel.getArenesMap().values()) {
			if(modelArena.getArenaPluginInstance() == arenaInstance) {
				Manager.doEndGame(modelArena, null, null);
				return true;
			}
		}
		return false;
	}
	
	public static void doLeavePlayerFromArena(ArenaInstance arenaInstance, ArpPlayer arpPlayer) {
		arpPlayer.leaveArena();
	}
}

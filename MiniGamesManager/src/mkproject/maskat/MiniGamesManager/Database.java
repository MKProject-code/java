package mkproject.maskat.MiniGamesManager;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import mkproject.maskat.MiniGamesManager.Game.MiniGame;
import mkproject.maskat.Papi.Papi;

public class Database {
//
//	public static World getRandomWorld(MiniGame minigame) {
//		ConfigurationSection configSection = Plugin.getPlugin().getConfig().getConfigurationSection("Games."+minigame.name()+".Worlds");
//		
//		Set<String> worldsList = configSection.getKeys(false);
//		
//		String worldName = worldsList.toArray(new String[worldsList.size()])[Papi.Function.randomInteger(0, worldsList.size()-1)];
//		
//		return Bukkit.getWorld(worldName);
//	}
	
	public static int getPropertyInt(MiniGame miniGame, World world, String property) {
		return Plugin.getPlugin().getConfig().getInt("Games."+miniGame.name()+".Worlds."+world.getName()+"."+property);
	}
	public static String getPropertyString(MiniGame miniGame, World world, String property) {
		return Plugin.getPlugin().getConfig().getString("Games."+miniGame.name()+".Worlds."+world.getName()+"."+property);
	}

	public static int getPlayersMax(MiniGame miniGame, World world) {
		return Plugin.getPlugin().getConfig().getInt("Games."+miniGame.name()+".Worlds."+world.getName()+".PlayersMax");
	}
	
	public static int getPlayersNeed(MiniGame miniGame, World world) {
		return Plugin.getPlugin().getConfig().getInt("Games."+miniGame.name()+".Worlds."+world.getName()+".PlayersNeed");
	}

	public static Set<String> getMiniGames() {
		ConfigurationSection configSection = Plugin.getPlugin().getConfig().getConfigurationSection("Games");
		if(configSection == null)
			return new HashSet<String>();
		return configSection.getKeys(false);
	}
	
	public static Set<String> getWorlds(String miniGame) {
		ConfigurationSection configSection = Plugin.getPlugin().getConfig().getConfigurationSection("Games."+miniGame+".Worlds");
		if(configSection == null)
			return new HashSet<String>();
		return configSection.getKeys(false);
	}
	
	public static Location getSpawnLocation() {
		return Papi.Server.getServerSpawnLocation();
	}
}

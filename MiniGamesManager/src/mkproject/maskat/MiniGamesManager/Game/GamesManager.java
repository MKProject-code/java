package mkproject.maskat.MiniGamesManager.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import mkproject.maskat.MiniGamesManager.Database;
import mkproject.maskat.MiniGamesManager.Plugin;

public abstract class GamesManager {

	private static Map<MiniGame, GameModel> lobbyGamesMap = new HashMap<>();
	private static Map<World, GameModel> worldGamesMap = new HashMap<>();
	private static Map<MiniGame, List<World>> worldAvailableMap = new HashMap<>();
	
	public static void initialize() {
		for(String miniGameName : Database.getMiniGames()) {
			MiniGame miniGame = MiniGame.valueOf(miniGameName);
			if(miniGame != null)
			{
				List<World> worldsList = new ArrayList<>();
				for(String worldName : Database.getWorlds(miniGameName)) {
					World world = Bukkit.getWorld(worldName);
					if(world != null)
						worldsList.add(world);
				}
				if(worldsList.size() > 0)
					worldAvailableMap.put(miniGame, worldsList);
			}
		}
	}
	
	public static void restartAllAndReinitialize() {
		for(GameModel gameModel : lobbyGamesMap.values()) {
			for(Player player : gameModel.getWorld().getPlayers())
			{
				if(player.getGameMode() == GameMode.SPECTATOR) //TODO !!!
					player.setGameMode(GameMode.ADVENTURE);    //TODO !!!
				player.teleport(Database.getSpawnLocation());
			}
			gameModel.setGameClosed(true);
		}
		lobbyGamesMap.clear();
		
		if(worldGamesMap.size() > 0) {
			for(World world : worldGamesMap.keySet()) {
				for(Player player : world.getPlayers())
				{
					if(player.getGameMode() == GameMode.SPECTATOR) //TODO !!!
						player.setGameMode(GameMode.ADVENTURE);    //TODO !!!
					player.teleport(Database.getSpawnLocation());
				}
				
				GameModel gameModel = worldGamesMap.remove(world);
				gameModel.setGameClosed(true);
			}
		}
		worldGamesMap.clear();
		worldAvailableMap.clear();
		
		initialize();
	}
	
	public static GameModel getLobbyGame(MiniGame miniGame) {
		GameModel gameModel = lobbyGamesMap.get(miniGame);
		if(gameModel == null)
		{
			List<World> worlds = worldAvailableMap.get(miniGame);
			if(worlds == null || worlds.size() == 0)
				return null;
			
			Collections.shuffle(worlds);
			World world = worlds.remove(0);
			if(world == null)
				return null;
			
			gameModel = GameModel.createInstance(miniGame, world);
			if(gameModel == null)
				return null;
			
			lobbyGamesMap.put(miniGame, gameModel);
			worldGamesMap.put(world, gameModel);
		}
		return gameModel;
	}
	
	public static GameModel getGameModel(World world) {
		return worldGamesMap.get(world);
	}

	public static void deleteLobby(MiniGame miniGame) {
		lobbyGamesMap.remove(miniGame);
	}

	public static Collection<Player> getPlayers(World world) {
		Collection<Player> players = new ArrayList<>();
		for(Player p : world.getPlayers())
		{
			if(p.getGameMode() != GameMode.SPECTATOR)
				players.add(p);
		}
		return players;
	}

	public static String getMessagePrefix(MiniGame miniGame) {
		return "&8&l[&6&l"+miniGame.name()+"&8&l] ";
	}

	public static void closeGame(MiniGame miniGame, World world) {
		GameModel gameModel = worldGamesMap.remove(world);
		
		for(Player player : world.getPlayers())
		{
			if(player.getGameMode() == GameMode.SPECTATOR) //TODO !!!
				player.setGameMode(GameMode.ADVENTURE);    //TODO !!!
			
			GameModel gameModelNew = getLobbyGame(miniGame);
			
			if(gameModelNew == null || !gameModelNew.joinPlayer(player))
			{
				GamesManager.teleportSafe(player, Database.getSpawnLocation());
			}
		}
		
		Plugin.getPlayerMenu().refreshPapiMenuItem(miniGame);

		gameModel.setGameClosed(true);
		
		worldAvailableMap.get(miniGame).add(world);
	}

	public static boolean teleportSafe(Player player, Location location) {
    	player.setWalkSpeed(0.0f);
    	player.setFlySpeed(0.0f);
    	player.setVelocity(new Vector().zero());
		boolean result = player.teleport(location);
		player.setWalkSpeed(0.2f);
		player.setFlySpeed(0.1f);
		return result;
	}

	public static void initPlayer(Player player, GameMode gameMode) {
		if(player.getGameMode() != gameMode)
			player.setGameMode(gameMode);
		
		player.getInventory().clear();
		player.setLevel(0);
		player.setExp(0F);
		
		player.setHealth(20D);
		player.setFoodLevel(20);
		player.setSaturation(20F);
		player.setExhaustion(0F);
		player.setAbsorptionAmount(0D);
	}

}

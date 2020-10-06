package mkproject.maskat.Papi;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import mkproject.maskat.Papi.Utils.Message;

public class PapiServer {
	private static World serverLobbyWorld;
	private static World serverSpawnWorld;
	private static Location serverSpawnLocation = null;
	private static World survivalWorld;
	private static World netherWorld;
	private static World theEndWorld;
	
	protected static void initServerLobbyWorld(World world) {
		serverLobbyWorld = world;
	}
	protected static void initServerSpawnWorld(World world) {
		serverSpawnWorld = world;
	}
	protected static void initServerSpawnLocation(Location location) {
		serverSpawnWorld = location.getWorld();
		serverSpawnLocation = location;
	}
	
	protected static World getServerSpawnWorld() {
		return serverSpawnWorld;
	}
	protected static Location getServerSpawnLocation() {
		if(serverSpawnLocation != null)
			return serverSpawnLocation;
		
		return serverSpawnWorld.getSpawnLocation();
	}
	
	protected static void initSurvivalWorld(World world) {
		survivalWorld = world;
	}
	protected static World getSurvivalWorld() {
		return survivalWorld;
	}
	
	protected static void initNetherWorld(World world) {
		netherWorld = world;
	}
	protected static World getNetherWorld() {
		return netherWorld;
	}
	
	protected static void initTheEndWorld(World world) {
		theEndWorld = world;
	}
	protected static World getTheEndWorld() {
		return theEndWorld;
	}
	
	protected static Location getServerLobbyLocation() {
		return serverLobbyWorld.getSpawnLocation();
	}
	protected static World getServerLobbyWorld() {
		return serverLobbyWorld;
	}
	public static void dispatchCommand(JavaPlugin pluginExecutor, String command) {
		Message.sendConsoleInfo(pluginExecutor, "Trying execute command: " + command);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
	}
	public static void dispatchCommandSilent(String command) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
	}
	public static boolean isSurvivalWorld(World world, boolean allowSpawn, boolean allowLobby) {
		if(world == survivalWorld || world == netherWorld || world == theEndWorld) {
			if((allowSpawn && world == serverSpawnWorld) || (allowLobby && world == serverLobbyWorld)) {
				return true;
			}
		}
		return false;
	}

}

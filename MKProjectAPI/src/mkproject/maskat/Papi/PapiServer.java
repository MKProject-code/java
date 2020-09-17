package mkproject.maskat.Papi;

import org.bukkit.Location;
import org.bukkit.World;

public class PapiServer {
	private static World serverLobbyWorld;
	private static World serverSpawnWorld;
	private static Location serverSpawnLocation = null;
	private static World survivalWorld;
	
	protected static void initServerLobbyWorld(World world) {
		serverLobbyWorld = world;
	}
	protected static void initServerSpawnWorld(World world) {
		serverSpawnWorld = world;
	}
	public static void initServerSpawnLocation(Location location) {
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
	
	protected static Location getServerLobbyLocation() {
		return serverLobbyWorld.getSpawnLocation();
	}
	protected static World getServerLobbyWorld() {
		return serverLobbyWorld;
	}

}

package mkproject.maskat.WorldManager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.World;

public class Model {
	private static Map<World, ModelWorld> worldsMap = new HashMap<>();
	
	public static Map<World, ModelWorld> getWorldsMap() {
		return worldsMap;
	}
	public static ModelWorld getWorld(World world) {
		return worldsMap.get(world);
	}
	public static boolean existWorld(World world) {
		return worldsMap.containsKey(world);
	}
	public static ModelWorld addWorld(World world, GameMode gamemode, int borderRadius, boolean borderSquared) {//, boolean allowWeather) {
		return worldsMap.put(world, new ModelWorld(gamemode, borderRadius, borderSquared));//, allowWeather));
	}
	public static void removeWorld(World world) {
		worldsMap.remove(world);
	}
}

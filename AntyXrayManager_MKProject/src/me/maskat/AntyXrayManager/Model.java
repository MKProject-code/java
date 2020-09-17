package me.maskat.AntyXrayManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Model {
	private static Map<Player,ModelPlayer> playersMap = new HashMap<>();
	public static List<Material> hiddenMaterials_world = new ArrayList<>();
	public static List<Material> materialsToShow_world = new ArrayList<>();
	public static List<Material> hiddenMaterials_nether = new ArrayList<>();
	public static List<Material> materialsToShow_nether = new ArrayList<>();
	
	protected static boolean existPlayer(Player player) {
		return playersMap.containsKey(player);
	}
	protected static Collection<ModelPlayer> getPlayers() {
		return playersMap.values();
	}
	protected static void addPlayer(Player player) {
		playersMap.put(player, new ModelPlayer(player));
	}
	protected static void removePlayer(Player player) {
		playersMap.remove(player);
	}
	protected static ModelPlayer getPlayer(Player player) {
		return playersMap.get(player);
	}
	
	protected static List<Material> getHiddenMaterialsList(World world) {
		if(world == Plugin.world)
			return hiddenMaterials_world;
		else if(world == Plugin.world_nether)
			return hiddenMaterials_nether;
		return null;
	}
	protected static List<Material> getMaterialsToShowList(World world) {
		if(world == Plugin.world)
			return materialsToShow_world;
		else if(world == Plugin.world_nether)
			return materialsToShow_nether;
		return null;
	}
}

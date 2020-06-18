package me.maskat.InventoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public class Model {
	public static Map<Player, ModelPlayer> mapPlayers = new HashMap<>();
	
	public static ModelPlayer getPlayer(Player player) {
		return mapPlayers.get(player);
	}
	
	public static List<Player> getPlayers() {
		return new ArrayList<>(mapPlayers.keySet());
	}
	
	public static ModelPlayer addPlayer(Player player) {
		return mapPlayers.put(player, new ModelPlayer(player));
	}
	public static ModelPlayer removePlayer(Player player) {
		return mapPlayers.remove(player);
	}

	public static boolean existPlayer(Player player) {
		return mapPlayers.containsKey(player);
	}
}

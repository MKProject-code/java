package me.maskat.AntyXrayManager.copy;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class Model {
	private static Map<Player,ModelPlayer> playersMap = new HashMap<>();
	
	protected static boolean existPlayer(Player player) {
		return playersMap.containsKey(player);
	}
	protected static void addPlayer(Player player) {
		playersMap.put(player, new ModelPlayer(player));
	}
	protected static void removePlayer(Player player) {
		playersMap.remove(player);
	}
	public static ModelPlayer getPlayer(Player player) {
		return playersMap.get(player);
	}
}

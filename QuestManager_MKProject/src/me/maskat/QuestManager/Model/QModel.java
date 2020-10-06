package me.maskat.QuestManager.Model;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public abstract class QModel {
	
	private static Map<Player, QPlayer> playersMap = new HashMap<>();
	
	public static void addPlayer(Player player) {
		playersMap.put(player, new QPlayer(player));
	}

	public static QPlayer removePlayer(Player player) {
		return playersMap.remove(player);
	}

	public static QPlayer getPlayer(Player player) {
		return playersMap.get(player);
	}
}

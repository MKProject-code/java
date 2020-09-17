package mkproject.maskat.CmdBlockAdvancedManager.Models;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class CbamModel {
	public static Map<Player, CbamPlayer> playersMap = new HashMap<>();
	
	public static void addPlayer(Player player) {
		playersMap.putIfAbsent(player, new CbamPlayer(player));
	}
	
	public static Map<Player, CbamPlayer> getPlayersMap() {
		return playersMap;
	}
	
	public static CbamPlayer getPlayer(Player player) {
		return playersMap.get(player);
	}

	public static boolean existPlayer(Player player) {
		return playersMap.containsKey(player);
	}

	public static void removePlayer(Player player) {
		playersMap.remove(player);
	}
}

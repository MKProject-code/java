package mkproject.maskat.StatsManager.Model;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class Model {
	private static Map<Player, StatsPlayer> statsPlayersMap = new HashMap<>();
	
	public static StatsPlayer getStatsPlayer(Player player) {
		return statsPlayersMap.get(player);
	}
	
	public static void addStatsPlayer(Player player) {
		statsPlayersMap.put(player, new StatsPlayer(player));
	}
	
	public static void removeStatsPlayer(Player player) {
		statsPlayersMap.remove(player);
	}
}

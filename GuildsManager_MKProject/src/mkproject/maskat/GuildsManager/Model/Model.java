package mkproject.maskat.GuildsManager.Model;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class Model {
	private static Map<Player, GuildPlayer> statsPlayersMap = new HashMap<>();
	
	public static GuildPlayer getStatsPlayer(Player player) {
		return statsPlayersMap.get(player);
	}
	
	public static void addStatsPlayer(Player player) {
		statsPlayersMap.put(player, new GuildPlayer(player));
	}
	
	public static void removeStatsPlayer(Player player) {
		statsPlayersMap.remove(player);
	}
}

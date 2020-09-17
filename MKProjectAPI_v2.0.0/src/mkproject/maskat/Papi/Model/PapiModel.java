package mkproject.maskat.Papi.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public class PapiModel {
	public static Map<Player, PapiPlayer> mapPlayers = new HashMap<>();
	
	public static PapiPlayer getPlayer(Player player) {
		return mapPlayers.get(player);
	}
	
	public static List<Player> getPlayers() {
		return new ArrayList<>(mapPlayers.keySet());
	}
	
	public static PapiPlayer addPlayer(Player player) {
		return mapPlayers.put(player, new PapiPlayer(player));
	}
	public static PapiPlayer removePlayer(Player player) {
		return mapPlayers.remove(player);
	}

	public static boolean existPlayer(Player player) {
		return mapPlayers.containsKey(player);
	}
}

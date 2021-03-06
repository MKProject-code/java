package me.maskat.MoneyManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class MapiModel {
	
	private static Map<Player, MapiPlayer> mapiPlayers = new HashMap<>();
	
	protected static MapiPlayer getPlayer(Player player) {
		return mapiPlayers.get(player);
	}
	
	protected static boolean existPlayer(Player player) {
		return mapiPlayers.containsKey(player);
	}

	protected static void addPlayer(Player player) {
		mapiPlayers.put(player, new MapiPlayer(player));
	}

	protected static void removePlayer(Player player) {
		mapiPlayers.remove(player);
	}
	
	private static Collection<MapiUpdater> mapiUpdaters = new ArrayList<>();
	
	protected static void registerMapiUpdater(MapiUpdater mapiUpdater) {
		mapiUpdaters.add(mapiUpdater);
	}
	
	protected static void doMapiUpdate(MapiPlayer mapiPlayer) {
		for(MapiUpdater mapiUpdater : mapiUpdaters) {
			mapiUpdater.onMoneyUpdate(mapiPlayer);
		}
	}
}

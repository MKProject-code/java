package me.maskat.WorldEditTools.models;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class Model {
	private static Map<Player, ModelPlayer> mapPlayers = new HashMap<>();
	
    public static Map<Player, ModelPlayer> getPlayersMap() { return mapPlayers; }
    
	public static ModelPlayer getPlayer(Player player) { return getPlayersMap().get(player); }
    
    public static boolean existPlayer(Player player) { return getPlayersMap().containsKey(player); }
    
    public static void addPlayer(Player player) { getPlayersMap().putIfAbsent(player, new ModelPlayer(player)); }
    
    public static void removePlayer(Player player) { getPlayersMap().remove(player); }
}

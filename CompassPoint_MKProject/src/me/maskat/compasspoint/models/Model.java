package me.maskat.compasspoint.models;

import java.util.Map;

import org.bukkit.entity.Player;

import me.maskat.compasspoint.Plugin;

public class Model {
	public static ModelPlayer Player(Player player) {
		return Players().get(player);
	}
	
    public static Map<Player, ModelPlayer> Players() { return Plugin.getPlayersMap(); }
    
    public static class Players {
    	
    	public static boolean isExist(Player player) { return Players().containsKey(player); }
    	private static ModelPlayer addModel(Player player) { return Players().putIfAbsent(player, new ModelPlayer(player)); }
    	private static ModelPlayer removeModel(Player player) { return Players().remove(player); }
    	
    	public static void addPlayer(Player player) {
    		addModel(player);
    	}
    	
    	public static void removePlayer(Player player) {
    		if(Model.Players.isExist(player))
    			removeModel(player);
    	}
    }
}

package me.maskat.TradeManager.PlayerMenu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class Models {
	public static Map<Player, TradePlayer> tradePlayersMap = new HashMap<>();
	
	public static boolean existTradePlayer(Player player) {
		return tradePlayersMap.containsKey(player);
	}
	public static TradePlayer getTradePlayer(Player player) {
		return tradePlayersMap.get(player);
	}
	public static void addTradePlayer(Player player, Player destPlayer) {
		tradePlayersMap.put(player, new TradePlayer(player, destPlayer));
	}
	public static void removeTradePlayer(Player player) {
		TradePlayer tradePlayer = tradePlayersMap.get(player);
		tradePlayersMap.remove(player);
		if(tradePlayer.isOpenedMenu() && !tradePlayer.isClosingMenu())
			player.closeInventory();
	}
}

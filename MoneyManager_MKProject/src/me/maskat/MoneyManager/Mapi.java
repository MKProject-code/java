package me.maskat.MoneyManager;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Mapi {
	public static MapiPlayer getPlayer(Player player) {
		return MapiModel.getPlayer(player);
	}
	public static MapiOfflinePlayer getPlayer(OfflinePlayer offlinePlayer) {
		if(offlinePlayer.isOnline() || MapiModel.existPlayer(offlinePlayer.getPlayer()))
			return null;
		return new MapiOfflinePlayer(offlinePlayer);
	}
	
	public static void registerMapiUpdater(MapiUpdater mapiUpdater) {
		MapiModel.registerMapiUpdater(mapiUpdater);
	}
}

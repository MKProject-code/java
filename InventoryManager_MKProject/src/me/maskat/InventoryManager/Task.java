package me.maskat.InventoryManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Task implements Runnable {
	@Override
	public void run() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if (Manager.checkSave(player, player.getWorld())) {
				Model.getPlayer(player).saveInventory(player.getGameMode());
			}
		}
	}
}

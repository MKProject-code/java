package me.maskat.InventoryManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Event implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		Model.addPlayer(e.getPlayer());
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent e) {
		Model.getPlayer(e.getPlayer()).saveInventory(e.getPlayer().getGameMode());
		Model.getPlayer(e.getPlayer()).loadInventory(e.getNewGameMode());
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinEvent(PlayerQuitEvent e) {
		Model.removePlayer(e.getPlayer());
	}
}

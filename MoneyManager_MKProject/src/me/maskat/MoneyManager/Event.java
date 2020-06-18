package me.maskat.MoneyManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Event implements Listener {
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		MapiModel.addPlayer(e.getPlayer());
	}
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		MapiModel.getPlayer(e.getPlayer()).saveData();
		MapiModel.removePlayer(e.getPlayer());
	}
}

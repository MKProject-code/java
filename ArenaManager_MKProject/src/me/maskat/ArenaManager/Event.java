package me.maskat.ArenaManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.maskat.ArenaManager.ArenaManager.Manager;
import me.maskat.ArenaManager.Models.ArenesModel;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.MenuInventory.PapiMenuInventoryClickEvent;
import mkproject.maskat.Papi.Model.PapiListenChatEvent;

public class Event implements Listener {
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		ArenesModel.addPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		Manager.onPlayerQuitEvent(e);
		
		ArenesModel.removePlayer(e.getPlayer());
		ArenesModel.removeAdmin(e.getPlayer());
	}
	
	@EventHandler
	public void onPapiMenuInventoryClickEvent(PapiMenuInventoryClickEvent e) {
		if(e.getPluginExecutor() != Plugin.getPlugin())
			return;
		
		if(ArenesModel.existAdmin(e.getPlayer()))
			ArenesModel.getAdmin(e.getPlayer()).onPapiMenuInventoryClick(e);
	}
	
	@EventHandler
	public void onPapiListenChatEvent(PapiListenChatEvent e) {
		if(e.getPluginExecutor() != Plugin.getPlugin())
			return;
		
		if(ArenesModel.existAdmin(e.getPlayer()))
			ArenesModel.getAdmin(e.getPlayer()).onPapiListenChat(e);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeathEvent(PlayerDeathEvent e) {
		Manager.onPlayerDeathEvent(e);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawnEvent(PlayerRespawnEvent e) {
		Manager.onPlayerRespawnEvent(e);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		if(ArenesModel.existPlayer(e.getPlayer()) && ArenesModel.getPlayer(e.getPlayer()).isFreeze() && Papi.Function.isMovedBlock(e.getFrom(), e.getTo(), true, true))
			e.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDamageEvent(EntityDamageEvent e) {
		if(!(e.getEntity() instanceof Player))
			return;
		Manager.onPlayerDamageEvent(e, (Player)e.getEntity());
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerTeleportEvent(PlayerTeleportEvent e) {
		if(ArenesModel.existPlayer(e.getPlayer()) && !ArenesModel.getPlayer(e.getPlayer()).isAllowTeleport())
			e.setCancelled(true);
		
//		if(!ArenesModel.existPlayer(e.getPlayer()) || ArenesModel.getPlayer(e.getPlayer()).getPlayedInsideArena() == null)
//			return;
//			
//		if(e.getTo().getWorld().getName().equalsIgnoreCase(ArenesModel.getPlayer(e.getPlayer()).getPlayedInsideArena().getWorld().getName()))
//			return;
//		Bukkit.broadcastMessage("canceled Teleport");
//		e.setCancelled(true);
	}
}

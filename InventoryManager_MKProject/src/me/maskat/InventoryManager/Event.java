package me.maskat.InventoryManager;

import java.sql.ResultSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Model.PapiPlayerLoginEvent;

public class Event implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		//Model.addPlayer(e.getPlayer());
	}

//	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
//	public void onPlayerGameModeChangeLow(PlayerGameModeChangeEvent e) {
//		if(e.getNewGameMode() != GameMode.SURVIVAL && !e.getPlayer().hasPermission(Papi.Function.getPermission(Plugin.getPlugin(), "bypass.creativeonsurvival")))
//		{
//			World playerWorld = e.getPlayer().getLocation().getWorld();
//			if((playerWorld == Papi.Server.getServerLobbyWorld() && e.getNewGameMode() != GameMode.SPECTATOR) || 
//					playerWorld == Papi.Server.getServerSpawnWorld()
//					|| playerWorld == Papi.Server.getSurvivalWorld()
//					|| playerWorld == Papi.Server.getNetherWorld()
//					|| playerWorld == Papi.Server.getTheEndWorld()
//				)
//			{
//				e.setCancelled(true);
//			}
//		}
//	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
		
		Player player = e.getPlayer();

		if(!Papi.Model.getPlayer(player).isLogged())
			return;
		
		if (Manager.checkSave(player, player.getWorld())) {
			Model.getPlayer(player).saveInventory(player.getGameMode());
		}

		if (Manager.checkLoad(player, player.getWorld(), e.getNewGameMode())) {
			Model.getPlayer(player).loadInventory(e.getNewGameMode());
		} else {
			Model.getPlayer(player).clearInventory();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeathEvent(PlayerDeathEvent e) {
		
		Player player = e.getEntity();
		
		if(!Papi.Model.getPlayer(player).isLogged())
			return;
		
		GameMode gameMode = player.getGameMode();
		
		if(Manager.checkDeathUpdate(player, player.getWorld(), gameMode))
		{
			if(e.getKeepLevel())
				Model.getPlayer(player).updateInventory(player.getGameMode(), player.getInventory(), e.getKeepInventory(), player.getLevel(), player.getExp());
			else
				Model.getPlayer(player).updateInventory(player.getGameMode(), player.getInventory(), e.getKeepInventory(), e.getNewLevel(), e.getNewExp());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerRespawnEvent(PlayerRespawnEvent e) {
		
		Player player = e.getPlayer();
		
		if(!Papi.Model.getPlayer(player).isLogged())
			return;
		
		World playerWorld = player.getWorld();
		World respawnWorld = e.getRespawnLocation().getWorld();
		
		if(playerWorld == respawnWorld)
			return;
		
		GameMode gameMode = player.getGameMode();

		if (Manager.checkLoad(player, respawnWorld, gameMode)) {
			Model.getPlayer(player).loadInventory(gameMode);
		}
	}
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleportEvent(PlayerTeleportEvent e) {

		World fromWorld = e.getFrom().getWorld();
		World toWorld = e.getTo().getWorld();
		
		if(fromWorld == toWorld)
			return;
		
		Player player = e.getPlayer();
		
		if(!Papi.Model.getPlayer(player).isLogged())
			return;
		
		GameMode gameMode = player.getGameMode();
		
		if (Manager.checkSave(player, fromWorld)) {
			Model.getPlayer(player).saveInventory(gameMode);
		}
		
		if (Manager.checkLoad(player, toWorld, gameMode)) {
			Model.getPlayer(player).loadInventory(gameMode);
		} else {
			Model.getPlayer(player).clearInventory();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPapiPlayerLoginEvent(PapiPlayerLoginEvent e) {
		Player player = e.getPlayer();
		GameMode playerGameMode = player.getGameMode();

		if (Manager.checkLoad(player, player.getWorld(), playerGameMode)) {
			Model.getPlayer(player).loadInventory(playerGameMode);
		} else {
			Model.getPlayer(player).clearInventory();
		}
	}
	
	
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void onPlayerTeleportEvent(PlayerTeleportEvent e) {
//		
//		if(e.getFrom().getWorld() == Papi.Server.getServerSpawnWorld()
//				|| e.getFrom().getWorld() == Papi.Server.getSurvivalWorld()
//				|| e.getFrom().getWorld().getName().equals("world_nether")
//				|| e.getFrom().getWorld().getName().equals("world_the_end")
//				|| e.getFrom().getWorld() == Papi.Server.getServerLobbyWorld())
//		{
//			if(e.getTo().getWorld() != Papi.Server.getServerSpawnWorld()
//					&& e.getTo().getWorld() != Papi.Server.getSurvivalWorld()
//					&& !e.getTo().getWorld().getName().equals("world_nether")
//					&& !e.getTo().getWorld().getName().equals("world_the_end")
//					&& e.getTo().getWorld() != Papi.Server.getServerLobbyWorld())
//			{
////				if(e.getPlayer().getGameMode() == GameMode.SURVIVAL)
////				{
////				if(e.getPlayer().getGameMode() == GameMode.SURVIVAL)
//					Model.getPlayer(e.getPlayer()).saveInventory(e.getPlayer().getGameMode());
////					e.getPlayer().getInventory().clear();
////				}
////				else
//				e.getPlayer().getInventory().clear();
//			}
//		}
//		else if(e.getFrom().getWorld() != Papi.Server.getServerSpawnWorld()
//				&& e.getFrom().getWorld() != Papi.Server.getSurvivalWorld()
//				&& !e.getFrom().getWorld().getName().equals("world_nether")
//				&& !e.getFrom().getWorld().getName().equals("world_the_end")
//				&& e.getFrom().getWorld() != Papi.Server.getServerLobbyWorld())
//		{
//			if(e.getTo().getWorld() == Papi.Server.getServerSpawnWorld()
//					|| e.getTo().getWorld() == Papi.Server.getSurvivalWorld()
//					|| e.getTo().getWorld().getName().equals("world_nether")
//					|| e.getTo().getWorld().getName().equals("world_the_end")
//					|| e.getTo().getWorld() == Papi.Server.getServerLobbyWorld())
//			{
////				if(e.getPlayer().getGameMode() == GameMode.SURVIVAL)
////				{
//					Model.getPlayer(e.getPlayer()).loadInventory(e.getPlayer().getGameMode());
////				}
//			}
//			else if(e.getFrom().getWorld() != e.getTo().getWorld())
//				e.getPlayer().getInventory().clear();
//		}
//	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuitLow(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if(player.isDead())
			return;
		
		if (Manager.checkSave(player, player.getWorld())) {
			Model.getPlayer(player).saveInventory(player.getGameMode());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent e) {
		Model.removePlayer(e.getPlayer());
	}
}

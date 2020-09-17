package me.maskat.AntyXrayManager.copy2;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import mkproject.maskat.Papi.Papi;

public class Event implements Listener {
//	
//	@EventHandler
//	public void onChunkLoad(ChunkLoadEvent e) {
//		e.getWorld().getLoadedChunks()
//	}
	
//	@EventHandler
//	public void onBlockBreak(BlockBreakEvent e) {
//		Player player = e.getPlayer();
//		
//		if(e.getBlock().getWorld() != Plugin.world)
//			return;
//		
//		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable() {
//			@Override
//			public void run() {
//				Function.blockBreakCheckAround(player, e.getBlock());
//			}
//		});
//	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Model.addPlayer(e.getPlayer());
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Model.removePlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		
		if(e.getAction() != Action.LEFT_CLICK_BLOCK)
			return;
		
		if(e.getClickedBlock().getWorld() != Plugin.world && e.getClickedBlock().getWorld() != Plugin.world_nether)
			return;
		
		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Function.blockBreakCheckAround(player, e.getClickedBlock());
			}
		});
	}
	
	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent e) {

		if(e.getSourceBlock().getWorld() != Plugin.world && e.getSourceBlock().getWorld() != Plugin.world_nether)
			return;
		
		for(Player player : e.getSourceBlock().getWorld().getPlayers()) {
			Location playerLoc = player.getLocation();
			
			if(playerLoc.distance(e.getSourceBlock().getLocation()) > 300)
				return;
			
			Bukkit.getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					Function.blockBreakCheckAround(player, e.getSourceBlock());
				}
			});
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		
		if(e.getTo().getWorld() != Plugin.world && e.getTo().getWorld() != Plugin.world_nether)
			return;
		
		if(Papi.Function.isMovedBlock(e.getFrom(), e.getTo(), true) == false)
			return;
		
		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Function.doHider(player, e.getTo().getWorld(), e.getTo().getBlockX(), e.getTo().getBlockY(), e.getTo().getBlockZ(), player.getTargetBlockExact(100), player.getTargetBlockExact(10));
			}
		});
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		Player player = e.getPlayer();
		
		if(e.getTo().getWorld() != Plugin.world && e.getTo().getWorld() != Plugin.world_nether)
			return;
		
		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Function.doHider(player, e.getTo().getWorld(), e.getTo().getBlockX(), e.getTo().getBlockY(), e.getTo().getBlockZ(), player.getTargetBlockExact(100), player.getTargetBlockExact(10));
			}
		});
	}
}

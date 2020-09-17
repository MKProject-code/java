package me.maskat.AntyXrayManager.copy;

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
		
		if(e.getAction() != Action.LEFT_CLICK_BLOCK || e.getClickedBlock().getWorld() != Plugin.world)
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

		if(e.getSourceBlock().getWorld() != Plugin.world)
			return;
		
		for(Player player : Plugin.world.getPlayers()) {
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
		
		if(e.getFrom().getWorld() != Plugin.world || e.getTo().getWorld() != Plugin.world)
			return;
		
		if(Papi.Function.isMovedBlock(e.getFrom(), e.getTo(), true) == false)
			return;
		
		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Function.doHider(player, e.getTo().getWorld(), e.getTo().getBlockX(), e.getTo().getBlockY(), e.getTo().getBlockZ(), player.getTargetBlockExact(100), player.getTargetBlockExact(10));
				
				// -- v2
//				int xStart = e.getTo().getBlockX();
//				int yStart = e.getTo().getBlockY()+1;
//				int zStart = e.getTo().getBlockZ();
//			
//				int radius = 5;
//				
//				for(int yy=yStart; yy < yStart+radius; yy++) {
//					for(int xx=xStart; xx < xStart+radius; xx++) {
//						for(int zz=zStart; zz < zStart+radius; zz++) {
//							Function.blockCheck(player,xx,yy,zz);
//						}
//						for(int zz=zStart; zz > zStart-radius; zz--) {
//							Function.blockCheck(player,xx,yy,zz);
//						}
//					}
//					for(int xx=xStart; xx > xStart-radius; xx--) {
//						for(int zz=zStart; zz < zStart+radius; zz++) {
//							Function.blockCheck(player,xx,yy,zz);
//						}
//						for(int zz=zStart; zz > zStart-radius; zz--) {
//							Function.blockCheck(player,xx,yy,zz);
//						}
//					}
//				}
//				for(int yy=yStart; yy > yStart-radius; yy--) {
//					for(int xx=xStart; xx < xStart+radius; xx++) {
//						for(int zz=zStart; zz < zStart+radius; zz++) {
//							Function.blockCheck(player,xx,yy,zz);
//						}
//						for(int zz=zStart; zz > zStart-radius; zz--) {
//							Function.blockCheck(player,xx,yy,zz);
//						}
//					}
//					for(int xx=xStart; xx > xStart-radius; xx--) {
//						for(int zz=zStart; zz < zStart+radius; zz++) {
//							Function.blockCheck(player,xx,yy,zz);
//						}
//						for(int zz=zStart; zz > zStart-radius; zz--) {
//							Function.blockCheck(player,xx,yy,zz);
//						}
//					}
//				}

				
				// -- v1
//				for(int xx=xStart; xx < xStart+radius; xx++) {
//					for(int zz=zStart; zz < zStart+radius; zz++) {
//						for(int yy=yStart; yy < yStart+radius; yy++) {
//							Function.blockCheck(player,xx,yy,zz);
//						}
//					}
//				}
//				for(int xx=xStart; xx > xStart-radius; xx--) {
//					for(int zz=zStart; zz > zStart-radius; zz--) {
//						for(int yy=yStart; yy > yStart-radius; yy--) {
//							Function.blockCheck(player,xx,yy,zz);
//						}
//					}
//				}
			}
		});
	}
}

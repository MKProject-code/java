package me.maskat.AntyXrayManager.copy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import mkproject.maskat.Papi.Papi;

public class Plugin extends JavaPlugin {
	private static Plugin plugin;
	public static String PERMISSION_PREFIX = "mkp.antyxraymanager";
	
	//public static Map<Player,Location> playersLastLocationMap = new HashMap<>();
	public static List<Material> hiddenMaterials = new ArrayList<>();
	public static List<Material> materialsToShow = new ArrayList<>();
	public static World world;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		getServer().getPluginManager().registerEvents(new Event(), this);
		
		if(Papi.DEVELOPER_DIRECTORY_AUTODELETE) {
			try {
				FileUtils.deleteDirectory(getDataFolder());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.saveDefaultConfig();
		
		hiddenMaterials.add(Material.STONE);
		hiddenMaterials.add(Material.GRANITE);
		hiddenMaterials.add(Material.DIORITE);
		hiddenMaterials.add(Material.ANDESITE);
		hiddenMaterials.add(Material.GRAVEL);
		hiddenMaterials.add(Material.DIRT);
		
		materialsToShow.add(Material.DIAMOND_ORE);
		materialsToShow.add(Material.GOLD_ORE);
		materialsToShow.add(Material.IRON_ORE);
		materialsToShow.add(Material.EMERALD_ORE);
		materialsToShow.add(Material.COAL_ORE);
		materialsToShow.add(Material.LAPIS_ORE);
		materialsToShow.add(Material.REDSTONE_ORE);
		
		world = Bukkit.getWorld("world");
		//task();
		
		getLogger().info("Has been enabled!");
	}
	
//	public static void task() {
//		Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
//			@Override
//			public void run() {
//				for(Player player : Bukkit.getServer().getOnlinePlayers()) {
//					
//					if(player.getWorld() != world)
//						continue;
//					
//					Location lastPlayerLocation = playersLastLocationMap.get(player);
//					Location playerLocation = player.getLocation();
//					
//					if(lastPlayerLocation != null && Papi.Function.isMovedBlock(lastPlayerLocation, playerLocation, true) == false)
//						continue;
//					
//					int xStart = playerLocation.getBlockX();
//					int yStart = playerLocation.getBlockY();
//					int zStart = playerLocation.getBlockZ();
//				
//					int radius = 10;
//					
//					for(int xx=xStart; xx < xStart+radius; xx++) {
//						for(int zz=zStart; zz < zStart+radius; zz++) {
//							for(int yy=yStart; yy < yStart+radius; yy++) {
//								blockCheck(player,xx,yy,zz);
//							}
//						}
//					}
//					for(int xx=xStart; xx > xStart-radius; xx--) {
//						for(int zz=zStart; zz > zStart-radius; zz--) {
//							for(int yy=yStart; yy > yStart-radius; yy--) {
//								blockCheck(player,xx,yy,zz);
//							}
//						}
//					}
//					
//					playersLastLocationMap.put(player, playerLocation);
//				}
//				task();
//			}
//		}, 60L);
//	}
	

	
//	public static void task() {
//		Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
//			@Override
//			public void run() {
//				for(Player player : Bukkit.getServer().getOnlinePlayers()) {
//					if(player.getWorld() != world)
//						continue;
//					
//					Chunk chunk = playersCurrentChunkMap.get(player);
//					
//					boolean doHide = false;
//					if(chunk == null) {
//						chunk = player.getLocation().getChunk();
//						doHide = true;
//					}
//					
//					if(chunk != player.getLocation().getChunk()) {
//						chunk = player.getLocation().getChunk();
//						doHide = true;
//					}
//					
////					if(doHide) {
////						for(int xx = 0; xx < 16; xx++) {
////						    for(int zz = 0; zz < 16; zz++) {
////						        for(int yy = 0; yy < 100; yy++) {
////						            Block block = chunk.getBlock(xx, yy, zz);
////						            if(hiddenMaterials.contains(block.getType())) {
////						            	player.sendBlockChange(block.getLocation(), Material.createBlockData());
////						            }
////						        }
////						    }
////						}
////					}
//					if(doHide) {
//						for(int xx = 0; xx < 16; xx++) {
//							for(int zz = 0; zz < 16; zz++) {
//								for(int yy = 0; yy < 100; yy++) {
//									Block block = chunk.getBlock(xx, yy, zz);
//									if(hiddenMaterials.contains(block.getType())) {
//										player.sendBlockChange(block.getLocation(), getRandomMaterialToShow().createBlockData());
//									}
//								}
//							}
//						}
//					}
//					
//					playersCurrentChunkMap.put(player, chunk);
//				}
//				task();
//			}
//		}, 40L);
//	}
	
	public static Plugin getPlugin() {
		return plugin;
	}

}

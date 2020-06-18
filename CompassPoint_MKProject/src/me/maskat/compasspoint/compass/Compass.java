package me.maskat.compasspoint.compass;

import java.util.Collection;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import me.maskat.compasspoint.Plugin;
import me.maskat.compasspoint.enums.NavLocation;
import me.maskat.compasspoint.models.Model;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class Compass {
//	public void onPlayerJoin(final Player player) {
//		checkLocation(player);
//	}
	
//	public void checkLocationOLD(final Player player) {
//    	Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), new Runnable() {
//            @Override
//            public void run() {
//            	if (player.isOnline()) {
//            		Location bedlocation = player.getBedSpawnLocation();
//	        		if (bedlocation == null || bedlocation.getWorld() != player.getWorld()) {
//	        			Random random = new Random();
//	        			Location location = player.getLocation();
//	        			player.setCompassTarget(new Location(location.getWorld(), location.getX()+(random.nextInt(51)-25), location.getY(), location.getZ()+(random.nextInt(51)-25)));
//	        		}
//	        		else
//	        		{
//	        			player.setCompassTarget(bedlocation);
//	        		}
//	            	checkLocation(player);
//            	}
//            }
//        }, 100L);
//	}
	
	private static boolean setBedLocation(final Player player) {
		if(!Model.Player(player).existBedSpawn()) {
			setRandomLocation(player);
			return false;
		}
		
		player.setCompassTarget(Model.Player(player).getBedSpawnLocation());
		return true;
	}
	
	private static boolean setWolfLocation(final Player player) {
		if(!Model.Player(player).existWolfAPI())
		{
			setRandomLocation(player);
			return false;
		}
		Wolf wolfentity = Model.Player(player).getWolfEntityAPI();
		if(wolfentity == null)
		{
			Message.sendActionBar(player, "&cCoś poszło nie tak... Nie umiemy zlokalizować twojego wilka :(");
			setRandomLocation(player);
			return false;
		}
		player.setCompassTarget(wolfentity.getLocation());
		return true;
	}
	
	private static boolean setPlayerLocation(Player player, Player targetPlayer)
	{
		if(targetPlayer == null || !targetPlayer.isOnline() || !targetPlayer.getWorld().getName().equals("world"))
		{
			setRandomLocation(player);
			return false;
		}
		
		player.setCompassTarget(targetPlayer.getLocation());
		return true;
	}
	
	private static void setRandomLocation(Player player) {
		Random random = new Random();
		Location location = player.getLocation();
		player.setCompassTarget(new Location(location.getWorld(), location.getX()+(random.nextInt(51)-25), location.getY(), location.getZ()+(random.nextInt(51)-25)));
	}
	
	public static void updateCompassLocation(Player player) {
        if (!player.isOnline() || Papi.Model.getPlayer(player).isAfk() || !player.getWorld().getName().equals("world"))
    		return;
        
        if(Model.Player(player).getNavLocation() == NavLocation.BED)
        	Model.Player(player).setComapssWorking(setBedLocation(player));
        else if(Model.Player(player).getNavLocation() == NavLocation.WOLF)
        	Model.Player(player).setComapssWorking(setWolfLocation(player));
        else if(Model.Player(player).getNavLocation() == NavLocation.PLAYER)
        	Model.Player(player).setComapssWorking(setPlayerLocation(player, Model.Player(player).getNavTargetPlayer()));
	}
	
	private static void updateCompassLocationTask() {
		Collection<? extends Player> playersOnline = Bukkit.getOnlinePlayers();
    	Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
            @Override
            public void run() {
            	for(Player player : playersOnline)
            	{
            		updateCompassLocation(player);
            	}
            	updateCompassLocationTask();
            }
        }, 40L);
	}
	
//	private static void updateWolfLocationTask() {
//    	Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
//            @Override
//            public void run() {
//            	for(Player player : Bukkit.getOnlinePlayers())
//            	{
//            		if(!Model.Player(player).existWolfEntity() && Model.Player(player).existWolfAPI())
//            			Model.Player(player).updateWolfEntityAPI();
//            	}
//            	updateWolfLocationTask();
//            }
//        }, 200L);
//	}
	
	public static void start() {
		updateCompassLocationTask();
//		updateWolfLocationTask();
	}
}

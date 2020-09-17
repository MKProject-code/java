package me.maskat.ShopManager.PlayerMenu;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import com.google.common.io.Files;

import me.maskat.MoneyManager.Mapi;
import me.maskat.ShopManager.Config;
import me.maskat.ShopManager.Function;
import me.maskat.ShopManager.Plugin;
import me.maskat.ShopManager.PlayerMenu.SelectedItemMenu.SelectedItemMenuSlot;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

abstract public class Manager {
	public static boolean buyItem(Player player, SelectedItemMenuSlot itemsMenuSlot) {
		if(player.getInventory().firstEmpty() < 0)
		{
			Message.sendMessage(player, "&4Twój ekwipunek jest pełny!");
			return false;
		}
		
		int priceItems = -1;
		if(itemsMenuSlot.getPrice() <= 0)
		{
			if(itemsMenuSlot.getPriceVip() > 0 && player.hasPermission(Plugin.PERMISSION_PREFIX+".vipzone"))
				priceItems = itemsMenuSlot.getPriceVip()*itemsMenuSlot.getAmount();
		}
		else if(itemsMenuSlot.getPriceVipSale() > 0 && player.hasPermission(Plugin.PERMISSION_PREFIX+".vip"))
			priceItems = itemsMenuSlot.getPriceVipSale()*itemsMenuSlot.getAmount();
		else if(itemsMenuSlot.getPriceVip() > 0 && player.hasPermission(Plugin.PERMISSION_PREFIX+".svip"))
			priceItems = itemsMenuSlot.getPriceVip()*itemsMenuSlot.getAmount();
		else if(itemsMenuSlot.getPriceSale() > 0)
			priceItems = itemsMenuSlot.getPriceSale()*itemsMenuSlot.getAmount();
		else
			priceItems = itemsMenuSlot.getPrice()*itemsMenuSlot.getAmount();
		
		if(priceItems > 0 && Mapi.getPlayer(player).getPoints() >= priceItems) {
			player.getInventory().addItem(itemsMenuSlot.getItemStackWithAmount());
			player.updateInventory();
			Mapi.getPlayer(player).delPoints(priceItems);
			Message.sendMessage(player, "&2Zapłaciłeś &a"+priceItems+"&2 "+Function.Menu.getCurrency(priceItems)+" za zakupy w sklepie.");
			return true;
		}
		else
		{
			Message.sendMessage(player, "&4Masz tylko &c"+(int)Mapi.getPlayer(player).getPoints()+" "+Function.Menu.getCurrency((int)Mapi.getPlayer(player).getPoints()));
			return false;
		}
	}
	
	public static boolean buyResetTheEnd(Player player, int price) {
		
		if(price <= 0)
		{
			Message.sendConsoleWarrning(Plugin.getPlugin(), "&c-----> ERROR !!! Price for 'Reset The End' is invalid <-----");
			return false;
		}
		
		if(Mapi.getPlayer(player).getPoints() >= price) {
			
			Message.sendMessage(player, "&a&oTrwa resetowanie kresu...");
			
			File file = new File(Plugin.getPlugin().getServer().getWorldContainer().getAbsolutePath()+File.separator+Papi.Server.getTheEndWorld().getName());
			
			if(file.isDirectory())
			{
				File fileDest = new File(Plugin.getPlugin().getServer().getWorldContainer().getAbsolutePath()+File.separator+Papi.Server.getTheEndWorld().getName()+"_OLD");
				if(fileDest.isDirectory())
				{
					if(!deleteDirectory(fileDest) || fileDest.isDirectory())
					{
						Message.sendConsoleWarrning(Plugin.getPlugin(), "&c-----> ERROR !!! 'Reset The End' error! Old direcotory exist and remove failure <-----");
						return false;
					}
				}
				
				for(Player playerInTheEnd : Papi.Server.getTheEndWorld().getPlayers())
				{
					if(!playerInTheEnd.teleport(Papi.Model.getPlayer(playerInTheEnd).getRespawnLocation()))
					{
						Message.sendConsoleWarrning(Plugin.getPlugin(), "&c-----> ERROR !!! 'Reset The End' error! Player '"+playerInTheEnd.getName()+"' teleport failed <-----");
						return false;
					}
				}
				
				String worldTheEndName = Papi.Server.getTheEndWorld().getName();
				
				Bukkit.unloadWorld(Papi.Server.getTheEndWorld(), true);
				
				try {
					Files.move(file, fileDest);
				} catch (IOException e) {
					Message.sendConsoleWarrning(Plugin.getPlugin(), "&c-----> ERROR !!! 'Reset The End' generated exception <-----");
					e.printStackTrace();
					return false;
				}
				
				if(file.isDirectory())
				{
					Message.sendConsoleWarrning(Plugin.getPlugin(), "&c-----> ERROR !!! 'Reset The End' error! Old directory still exist after renamed <-----");
					return false;
				}
				
				WorldCreator worldCreator = new WorldCreator(worldTheEndName);
				worldCreator.environment(Environment.THE_END);
				
				World newWorldTheEnd = Bukkit.createWorld(worldCreator);
				
				Papi.Server.initTheEndWorld(newWorldTheEnd);
				
				Mapi.getPlayer(player).delPoints(price);
				
				Message.sendBroadcast("&a&k?&b&k?&c&k?&d&k?&e&k?&a&k?&b&k?&c&k?&d&k?&e&k?&a&k?&b&k?&c&k?&d&k?&e&k?&a&k?&b&k?&c&k?&d&k?&e&k?&a&k?&b&k?&c&k?&d&k?&e&k?&a&k?&b&k?&c&k?&d&k?&e&k?&e&k?&d&k?&c&k?&b&k?&a&k?&e&k?&d&k?&c&k?&b&k?&a&k?&e&k?&d&k?&c&k?&b&k?&a&k?");
				Message.sendBroadcast("");
				Message.sendBroadcast("&a&k?&b&k?&c&k?&d&k?&e&k? &a&lŚwiętujmy! "+Papi.Model.getPlayer(player).getNameWithPrefix()+"&a zresetował END &e&k?&d&k?&c&k?&b&k?&a&k?");
				Message.sendBroadcast("");
				Message.sendBroadcast("&a&k?&b&k?&c&k?&d&k?&e&k?&a&k?&b&k?&c&k?&d&k?&e&k?&a&k?&b&k?&c&k?&d&k?&e&k?&a&k?&b&k?&c&k?&d&k?&e&k?&a&k?&b&k?&c&k?&d&k?&e&k?&a&k?&b&k?&c&k?&d&k?&e&k?&e&k?&d&k?&c&k?&b&k?&a&k?&e&k?&d&k?&c&k?&b&k?&a&k?&e&k?&d&k?&c&k?&b&k?&a&k?");
				
				Message.sendMessage(player, "&2Zapłaciłeś &a"+price+"&2 "+Function.Menu.getCurrency(price)+" za zresetowanie kresu.");
				
				Config.setResetTheEndLastResetInfo(player);
				
				return true;
			}
			else
			{
				Message.sendConsoleWarrning(Plugin.getPlugin(), "&c-----> ERROR !!! 'Reset The End' error! Direcotory not exist <-----");
				return false;
			}
		}
		else
			Message.sendMessage(player, "&4Masz tylko &c"+(int)Mapi.getPlayer(player).getPoints()+" "+Function.Menu.getCurrency((int)Mapi.getPlayer(player).getPoints()));
		return false;
	}
	
	private static boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
}

package me.maskat.TradeManager.PlayerMenu;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.inventory.ItemStack;

import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Utils.Message;

public class TradeManager {
protected static void endTrade(TradePlayer tradePlayer) {
		
		TradePlayer tradePlayerDestination = Models.getTradePlayer(tradePlayer.getDestinationPlayer());
		if(tradePlayerDestination == null)
			return;
		
		Collection<ItemStack> tradePlayerItemsStack = new ArrayList<>();
		
		for(int i1=0;i1<5;i1++) {
			for(int i2=0;i2<4;i2++) {
				ItemStack item = tradePlayer.getTradePlayerMenu().getPapiMenuPage().getItem(InventorySlot.valueOf(i1*9+i2));
				if(item != null) tradePlayerItemsStack.add(item);
			}
		}
		
		Collection<ItemStack> tradePlayerDestinationItemsStack = new ArrayList<>();
		
		for(int i1=0;i1<5;i1++) {
			for(int i2=0;i2<4;i2++) {
				ItemStack item = Models.getTradePlayer(tradePlayer.getDestinationPlayer()).getTradePlayerMenu().getPapiMenuPage().getItem(InventorySlot.valueOf(i1*9+i2));
				if(item != null) tradePlayerDestinationItemsStack.add(item);
			}
		}
		
		if(tradePlayerDestination != null && tradePlayer.isAccepted() && tradePlayerDestination.isAccepted())
		{
			for(ItemStack itemStack : tradePlayerItemsStack) {
				if(tradePlayerDestination.getPlayer().getInventory().firstEmpty() < 0)
					tradePlayerDestination.getPlayer().getLocation().getWorld().dropItem(tradePlayerDestination.getPlayer().getLocation(), itemStack);
				else
					tradePlayerDestination.getPlayer().getInventory().addItem(itemStack);
			}
			for(ItemStack itemStack : tradePlayerDestinationItemsStack) {
				if(tradePlayer.getPlayer().getInventory().firstEmpty() < 0)
					tradePlayer.getPlayer().getLocation().getWorld().dropItem(tradePlayer.getPlayer().getLocation(), itemStack);
				else
					tradePlayer.getPlayer().getInventory().addItem(itemStack);
			}
			
			Message.sendMessage(tradePlayer.getPlayer(), "&a&oTransakcja z graczem &e&o"+tradePlayerDestination.getPlayer().getName()+" &a&oudana!");
			Message.sendMessage(tradePlayerDestination.getPlayer(), "&a&oTransakcja z graczem &e&o"+tradePlayer.getPlayer().getName()+" &a&oudana!");
		}
		else
		{
			for(ItemStack itemStack : tradePlayerItemsStack) {
				if(tradePlayer.getPlayer().getInventory().firstEmpty() < 0)
					tradePlayer.getPlayer().getLocation().getWorld().dropItem(tradePlayer.getPlayer().getLocation(), itemStack);
				else
					tradePlayer.getPlayer().getInventory().addItem(itemStack);
			}
			for(ItemStack itemStack : tradePlayerDestinationItemsStack) {
				if(tradePlayerDestination.getPlayer().getInventory().firstEmpty() < 0)
					tradePlayerDestination.getPlayer().getLocation().getWorld().dropItem(tradePlayerDestination.getPlayer().getLocation(), itemStack);
				else
					tradePlayerDestination.getPlayer().getInventory().addItem(itemStack);
			}
			
			Message.sendMessage(tradePlayer.getPlayer(), "&c&oAnulowano transakcje z graczem &e&o"+tradePlayerDestination.getPlayer().getName());
			Message.sendMessage(tradePlayerDestination.getPlayer(), "&c&oAnulowano transakcje z graczem &e&o"+tradePlayer.getPlayer().getName());
		}
		
		Models.removeTradePlayer(tradePlayer.getPlayer());
		Models.removeTradePlayer(tradePlayerDestination.getPlayer());
	}
}

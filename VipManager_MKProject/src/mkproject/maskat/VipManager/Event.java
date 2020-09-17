package mkproject.maskat.VipManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class Event implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		Manager.checkValidVIP(e.getPlayer(), false);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerItemDamageEvent(PlayerItemDamageEvent e) {
		if(e.getPlayer().hasPermission("mkp.vipmanager.noarmordamage"))
		{
			Material material = e.getItem().getType();
			
			if(		   material == Material.CHAINMAIL_HELMET
					|| material == Material.DIAMOND_HELMET
					|| material == Material.GOLDEN_HELMET
					|| material == Material.IRON_HELMET
					|| material == Material.LEATHER_HELMET
					|| material == Material.NETHERITE_HELMET
					|| material == Material.TURTLE_HELMET
					
					|| material == Material.CHAINMAIL_CHESTPLATE
					|| material == Material.DIAMOND_CHESTPLATE
					|| material == Material.GOLDEN_CHESTPLATE
					|| material == Material.IRON_CHESTPLATE
					|| material == Material.LEATHER_CHESTPLATE
					|| material == Material.NETHERITE_CHESTPLATE
					
					|| material == Material.CHAINMAIL_LEGGINGS
					|| material == Material.DIAMOND_LEGGINGS
					|| material == Material.GOLDEN_LEGGINGS
					|| material == Material.IRON_LEGGINGS
					|| material == Material.LEATHER_LEGGINGS
					|| material == Material.NETHERITE_LEGGINGS
					
					|| material == Material.CHAINMAIL_BOOTS
					|| material == Material.DIAMOND_BOOTS
					|| material == Material.GOLDEN_BOOTS
					|| material == Material.IRON_BOOTS
					|| material == Material.LEATHER_BOOTS
					|| material == Material.NETHERITE_BOOTS
				)
			{
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e) {
		e.setDeathMessage(Message.getColorMessage("&8&o"+e.getDeathMessage()));
		
		if(e.getEntity().hasPermission("mkp.vipmanager.keepexp"))
		{
			e.setKeepLevel(true);
			e.setDroppedExp(0);
		}
		
		if(e.getEntity().hasPermission("mkp.vipmanager.keepinv100procent"))
		{
			e.setKeepInventory(true);
			e.getDrops().clear();
		}
		else if(e.getEntity().hasPermission("mkp.vipmanager.keepinv50procent") && Papi.Function.randomInteger(0, 1) == 1)
		{
			e.setKeepInventory(true);
			e.getDrops().clear();
		}
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player)
		{
			Player player = (Player) e.getWhoClicked();
			if(player.getWorld() == Papi.Server.getServerSpawnWorld() && e.getInventory().getType() == InventoryType.MERCHANT && !player.hasPermission("mkp.vipmanager.tradevillagerinspawn"))
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onTradeSelectEvent(TradeSelectEvent e) {
		if(e.getWhoClicked() instanceof Player)
		{
			Player player = (Player) e.getWhoClicked();
			if(player.getWorld() == Papi.Server.getServerSpawnWorld() && !player.hasPermission("mkp.vipmanager.tradevillagerinspawn"))
			{
				player.closeInventory();
				Message.sendMessage(player, "&c&lHandlować tutaj może tylko gracz VIP+");
			}
		}
		
	}
}

package me.maskat.MonsterEvent;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class Event implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntitySpawnEvent(EntitySpawnEvent e) {
		if(SchedulerTask.eventStarted && e.getLocation().getWorld() == Papi.Server.getServerSpawnWorld())
			e.setCancelled(false);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityCombustEvent(EntityCombustEvent e) {
		if(SchedulerTask.entitiesArenaAlive.contains(e.getEntity()))
			e.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeathEvent(EntityDeathEvent  e) {
		if(SchedulerTask.entitiesArenaAlive.contains(e.getEntity()))
		{
			SchedulerTask.entitiesArenaAlive.remove(e.getEntity());
			e.getDrops().clear();
			
			Player player = e.getEntity().getKiller();
			if(player == null)
				return;
			
			if(player.getInventory().firstEmpty() < 0)
			{
				Message.sendMessage(player, "&4Nie otrzymasz nagrody z eventu dopuki w twoim ekwipunku nie będzie wolnego miejsca!");
				return;
			}
			
			if(Papi.Function.randomInteger(0, 40)==0)
			{
				if(Papi.Function.randomInteger(0, 10)==0)
				{
					player.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1));
					Plugin.getPlugin().getLogger().info("----> Player '"+player.getName()+"' revard: ENCHANTED_GOLDEN_APPLE x1");
				}
				else if(Papi.Function.randomInteger(0, 2)==0)
				{
					player.getInventory().addItem(Papi.Function.randomStorageEnchantment(new ItemStack(Material.ENCHANTED_BOOK, 1)));
					Plugin.getPlugin().getLogger().info("----> Player '"+player.getName()+"' revard: ENCHANTED_BOOK x1");
				}
				else
				{
					player.getInventory().addItem(new ItemStack(Material.EXPERIENCE_BOTTLE, 1));
					Plugin.getPlugin().getLogger().info("----> Player '"+player.getName()+"' revard: EXPERIENCE_BOTTLE x1");
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageEvent(EntityDamageEvent e) {
		if(SchedulerTask.eventStarted && e.getEntity().getLocation().getWorld() == Papi.Server.getServerSpawnWorld())
			e.setCancelled(false);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(SchedulerTask.eventStarted && e.getEntity().getLocation().getWorld() == Papi.Server.getServerSpawnWorld())
		{
			if(e.getDamager() instanceof Player && e.getEntity() instanceof Player)
				e.setCancelled(true);
			else
				e.setCancelled(false);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeathEvent(PlayerDeathEvent e) {
		if(SchedulerTask.eventStarted && e.getEntity().getLocation().getWorld() == Papi.Server.getServerSpawnWorld())
		{
			e.setKeepInventory(true);
			e.setKeepLevel(true);
			e.getDrops().clear();
			e.setDroppedExp(0);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerItemDamageEvent(PlayerItemDamageEvent e) {
		if(SchedulerTask.eventStarted && e.getPlayer().getLocation().getWorld() == Papi.Server.getServerSpawnWorld())
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
					
					|| material == Material.DIAMOND_SWORD
					|| material == Material.GOLDEN_SWORD
					|| material == Material.IRON_SWORD
					|| material == Material.NETHERITE_SWORD
					|| material == Material.STONE_SWORD
					|| material == Material.WOODEN_SWORD
					
					|| material == Material.DIAMOND_AXE
					|| material == Material.GOLDEN_AXE
					|| material == Material.IRON_AXE
					|| material == Material.NETHERITE_AXE
					|| material == Material.STONE_AXE
					|| material == Material.WOODEN_AXE
					
					|| material == Material.DIAMOND_PICKAXE
					|| material == Material.GOLDEN_PICKAXE
					|| material == Material.IRON_PICKAXE
					|| material == Material.NETHERITE_PICKAXE
					|| material == Material.STONE_PICKAXE
					|| material == Material.WOODEN_PICKAXE
					
					|| material == Material.DIAMOND_HOE
					|| material == Material.GOLDEN_HOE
					|| material == Material.IRON_HOE
					|| material == Material.NETHERITE_HOE
					|| material == Material.STONE_HOE
					|| material == Material.WOODEN_HOE
					
					|| material == Material.DIAMOND_SHOVEL
					|| material == Material.GOLDEN_SHOVEL
					|| material == Material.IRON_SHOVEL
					|| material == Material.NETHERITE_SHOVEL
					|| material == Material.STONE_SHOVEL
					|| material == Material.WOODEN_SHOVEL
					
					|| material == Material.BOW
					|| material == Material.CROSSBOW
				)
			{
				e.setCancelled(true);
			}
		}
	}
}

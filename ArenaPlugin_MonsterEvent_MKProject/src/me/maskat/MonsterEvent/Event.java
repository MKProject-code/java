package me.maskat.MonsterEvent;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
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
			
			if(player.getInventory().firstEmpty() < 0)
			{
				Message.sendMessage(player, "&4Nie otrzymasz nagrody z eventu dopuki w twoim ekwipunku nie będzie miejsca!");
				return;
			}
			
			if(Papi.Function.randomInteger(0, 90)==0)
			{
				if(Papi.Function.randomInteger(0, 10)==0)
					player.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1));
				else if(Papi.Function.randomInteger(0, 1)==0)
					player.getInventory().addItem(Papi.Function.randomEnchantment(new ItemStack(Material.BOOK, 1), true, Papi.Function.randomInteger(1, 3)));
				else
					player.getInventory().addItem(new ItemStack(Material.EXPERIENCE_BOTTLE, 1));
			}
			
		}
	}
}

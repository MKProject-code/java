package mkproject.maskat.StatsManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import mkproject.maskat.StatsManager.Model.Model;

public class Event implements Listener {
	
//	@EventHandler
//	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
//		if(!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player))
//			return;
//		
//		Player damager = (Player)e.getDamager();
//		
//		Model.getStatsPlayer(damager).addKill();
//		
//		Model.getStatsPlayer((Player)e.getEntity()).setLastKiller(damager);
//	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeathEvent(PlayerDeathEvent e) {
		Model.getStatsPlayer(e.getEntity()).addDeath();
		
		Player killer = e.getEntity().getKiller();
		if(killer == null)
			return;
		
		Model.getStatsPlayer(killer).addKill();
		Model.getStatsPlayer((Player)e.getEntity()).setLastKiller(killer);
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		Model.addStatsPlayer(e.getPlayer());
	}
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		Model.removeStatsPlayer(e.getPlayer());
	}
}

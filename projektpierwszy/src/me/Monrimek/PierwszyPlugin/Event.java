package me.Monrimek.PierwszyPlugin;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Event implements Listener {
	
	private Map<Player, ModelPlayer> modelPlayersMap = new HashMap<>();
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		LivingEntity entity = e.getEntity();
		Player killer = entity.getKiller();
		
		if(entity.getType() == EntityType.ZOMBIE)
		{
			ModelPlayer modelPlayer = this.modelPlayersMap.get(killer);
			
			killer.sendMessage("Zdobyłeś 1 punkt");
			
			modelPlayer.addScore();
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		
		e.getPlayer().setHealth(2);
		
		Player player = e.getPlayer();
		
		ModelPlayer modelPlayer = new ModelPlayer(player);
		
		this.modelPlayersMap.put(player, modelPlayer);
		
	}
	
}

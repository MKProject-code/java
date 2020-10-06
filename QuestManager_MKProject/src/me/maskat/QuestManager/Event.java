package me.maskat.QuestManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.maskat.QuestManager.Model.QModel;
import mkproject.maskat.Papi.Model.PapiPlayerLoginEvent;

public class Event implements Listener {

	@EventHandler
	public void onPlayerLogin(PapiPlayerLoginEvent e) {
		QModel.addPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		QModel.removePlayer(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent e) {
		
		Object killer = e.getEntity().getKiller();
		
        if (killer instanceof Player && Plugin.getQuestManager().isValidEvent((Player) killer)) {
        	Plugin.getQuestManager().getCurrentQuest().onKillEntity((Player) killer, e.getEntity());
        }
    }
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		if(Plugin.getQuestManager().isValidSurvival(e.getTo().getWorld()))
			Plugin.getQuestManager().assignScoreBoard(e.getPlayer());
	}
}

package mkproject.maskat.AdminUtils;

import java.time.LocalDateTime;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import mkproject.maskat.Papi.Papi;

public class Event implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		LocalDateTime datetimeEnd = Database.Mutes.checkMuted(e.getPlayer());
		if(datetimeEnd == null)
			return;
		
		if(datetimeEnd.isAfter(LocalDateTime.now()))
			Papi.Model.getPlayer(e.getPlayer()).setMuted(datetimeEnd);
	}
}

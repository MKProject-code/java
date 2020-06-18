package me.maskat.cauldroninfinite;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent.ChangeReason;

public class Event implements Listener {
	
	@EventHandler
	public void onCauldronLevelChangeEvent(CauldronLevelChangeEvent e) {
		if(e.getReason() == ChangeReason.BOTTLE_FILL && e.getOldLevel() == 3 && e.getNewLevel() == 2)
			e.setNewLevel(3);
	}
}

package me.maskat.MenuHelpManager;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.maskat.MenuHelpManager.PlayerMenu.MainMenu;

public class Event implements Listener {
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		if(e.getMaterial() == Material.BOOK)
		{
			new MainMenu().initOpenMenu(e.getPlayer());
		}
	}
}

package mkproject.maskat.PortalManager;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.PortalManager.Config.ConfigKey;
import mkproject.maskat.PortalManager.Teleport.Portal;

public class Event implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerPortalEvent(PlayerPortalEvent e) {
		//if(e.getCause() != TeleportCause.NETHER_PORTAL)
		//	return;
		
//		if(e.getFrom().getWorld().getName().equalsIgnoreCase(Config.getString(ConfigKey.CustomPortalsWorldName))
//				|| e.getPlayer().getWorld().getName().equalsIgnoreCase(Config.getString(ConfigKey.CustomPortalsWorldName))
//				|| Model.existPlayer(e.getPlayer()))
//		{
//			e.setCancelled(true);
//			return;
//		}
		
		if(Model.existPlayer(e.getPlayer()))
		{
			Plugin.getPlugin().getLogger().info("Player '"+e.getPlayer().getName()+"' cancelled use "+e.getCause().toString());
			e.setCancelled(true);
			return;
		}
		
		if(e.getFrom().getWorld().getName().equalsIgnoreCase("world") || e.getFrom().getWorld().getName().equalsIgnoreCase("world_nether") || e.getFrom().getWorld().getName().equalsIgnoreCase("world_the_end"))
		{
			Plugin.getPlugin().getLogger().info("Player '"+e.getPlayer().getName()+"' use "+e.getCause().toString());
			return;
		}
		
		Plugin.getPlugin().getLogger().info("Player '"+e.getPlayer().getName()+"' cancelled use "+e.getCause().toString());
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		if(!e.getTo().getWorld().getName().equalsIgnoreCase(Config.getString(ConfigKey.CustomPortalsWorldName)))
			return;
		
		if(Model.existPlayer(e.getPlayer()) && Model.getPlayer(e.getPlayer()).isTeleporting())
			return;
		
		if(!Papi.Function.isMovedBlock(e.getFrom(), e.getTo(), true))
			return;

		for(Map.Entry<Integer,ModelPortal> entry : Model.getPortalsMap().entrySet())
		{
			if(Papi.Function.isLocationInRegion(e.getTo(), entry.getValue().getPortalLocationFirst(), entry.getValue().getPortalLocationSecound(), true))
			{
				Model.addPlayer(e.getPlayer());
				Model.getPlayer(e.getPlayer()).setTeleporting(true);
				Portal.teleportPlayer(e.getPlayer(), entry);
				Model.getPlayer(e.getPlayer()).setTeleporting(false);
				Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
				      @Override
				      public void run() {
				    	  Model.removePlayer(e.getPlayer());
				      }}, 5L);
			}
		}
	}
	
//	@EventHandler
//	public void onPapiPlayerLoginEvent(PapiPlayerLoginEvent e) {
//		Bukkit.broadcastMessage("onPapiPlayerLoginEvent");
//	}
//	@EventHandler
//	public void onPapiPlayerFirstLoginEvent(PapiPlayerFirstLoginEvent e) {
//		Bukkit.broadcastMessage("onPapiPlayerFirstLoginEvent");
//		Message.sendTitle(e.getPlayer(), "&aW&bi&ct&da&em&fy&0!", "", 10, 70, 20);
//		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable() {
//		      @Override
//		      public void run() {
//		    	  Location loc = RandomTp.getRandomLocation(e.getPlayer(), Bukkit.getWorld("world"));
//		    	  
//		      }});
//	}
}

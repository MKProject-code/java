package mkproject.maskat.WorldManager;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import mkproject.maskat.Papi.Papi;

public class Event implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		
//		for(Entry<World, ModelWorld> entry : Model.getWorldsMap().entrySet())
//		{
//			if(e.getTo().getWorld() == entry.getKey())
//			{
//				if(entry.getValue().getBorderRadius() >= 0 && e.getTo().distance(entry.getKey().getSpawnLocation()) > entry.getValue().getBorderRadius())
//					e.setCancelled(true);
//				
//				if(entry.getValue().getBorderRadiusSquared() >= 0 && e.getTo().distanceSquared(entry.getKey().getSpawnLocation()) > entry.getValue().getBorderRadius())
//					e.setCancelled(true);
//			}
//		}
		
		if(Model.getWorld(e.getTo().getWorld()).getBorderRadius() >= 0 && e.getTo().distance(e.getTo().getWorld().getSpawnLocation()) > Model.getWorld(e.getTo().getWorld()).getBorderRadius())
			e.setCancelled(true);
				
		if(Model.getWorld(e.getTo().getWorld()).getBorderRadiusSquared() >= 0 && e.getTo().distanceSquared(e.getTo().getWorld().getSpawnLocation()) > Model.getWorld(e.getTo().getWorld()).getBorderRadiusSquared())
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		if(e.getPlayer().hasPermission("mkp.worldmanager.bypass.gamemode"))
			return;
		
		e.getPlayer().setGameMode(GameMode.valueOf(Plugin.getPlugin().getConfig().getString("configWorlds."+e.getPlayer().getLocation().getWorld().getName()+".GameMode").toUpperCase()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerTeleportEventHighest(PlayerTeleportEvent e) {
		Plugin.getPlugin().getLogger().info("Player '"+e.getPlayer().getName()+"' teleport from "+e.getFrom().getWorld().getName()+","+e.getFrom().getBlockX()+","+e.getFrom().getBlockY()+","+e.getFrom().getBlockZ()
				+ " to "+e.getTo().getWorld().getName()+","+e.getFrom().getBlockX()+","+e.getFrom().getBlockY()+","+e.getFrom().getBlockZ());
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerTeleportEvent(PlayerTeleportEvent e) {
		if(Model.getWorld(e.getTo().getWorld()).getBorderRadius() >= 0 && e.getTo().distance(e.getTo().getWorld().getSpawnLocation()) > Model.getWorld(e.getTo().getWorld()).getBorderRadius())
		{
			e.setCancelled(true);
			return;
		}
		
		if(Model.getWorld(e.getTo().getWorld()).getBorderRadiusSquared() >= 0 && e.getTo().distanceSquared(e.getTo().getWorld().getSpawnLocation()) > Model.getWorld(e.getTo().getWorld()).getBorderRadiusSquared())
		{
			e.setCancelled(true);
			return;
		}
		
		if(e.getFrom().getWorld() == e.getTo().getWorld())
			return;
		
		Papi.Model.getPlayer(e.getPlayer()).setSpeedDefault();
		
		if(e.getPlayer().hasPermission("mkp.worldmanager.bypass.gamemode"))
			return;
		
		//GameMode gamemode = GameMode.valueOf(Plugin.getPlugin().getConfig().getString("configWorlds."+e.getTo().getWorld().getName()+".GameMode").toUpperCase());
		GameMode gamemode = Model.getWorld(e.getTo().getWorld()).getGameMode();
		e.getPlayer().setGameMode(gamemode);
		if(gamemode == GameMode.CREATIVE)
			e.getPlayer().setFlying(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerRespawnEvent(PlayerRespawnEvent e) {
		if(Model.getWorld(e.getRespawnLocation().getWorld()).getBorderRadius() >= 0 && e.getRespawnLocation().distance(e.getRespawnLocation().getWorld().getSpawnLocation()) > Model.getWorld(e.getRespawnLocation().getWorld()).getBorderRadius())
		{
			e.setRespawnLocation(e.getRespawnLocation().getWorld().getSpawnLocation());
		}
		
		if(Model.getWorld(e.getRespawnLocation().getWorld()).getBorderRadiusSquared() >= 0 && e.getRespawnLocation().distanceSquared(e.getRespawnLocation().getWorld().getSpawnLocation()) > Model.getWorld(e.getRespawnLocation().getWorld()).getBorderRadiusSquared())
		{
			e.setRespawnLocation(e.getRespawnLocation().getWorld().getSpawnLocation());
		}
		
		if(e.getPlayer().hasPermission("mkp.worldmanager.bypass.gamemode"))
			return;
		
		//e.getPlayer().setGameMode(GameMode.valueOf(Plugin.getPlugin().getConfig().getString("configWorlds."+e.getRespawnLocation().getWorld().getName()+".GameMode").toUpperCase()));
		e.getPlayer().setGameMode(Model.getWorld(e.getRespawnLocation().getWorld()).getGameMode());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDeathEvent(PlayerDeathEvent e) {
		Plugin.getPlugin().getLogger().info("Player '"+e.getEntity().getName()+"' death "+e.getEntity().getLocation().getWorld().getName()+", "+e.getEntity().getLocation().getBlockX()+", "+e.getEntity().getLocation().getBlockY()+", "+e.getEntity().getLocation().getBlockZ());
	}
	
//	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
//    public void onWeatherChangeEvent(WeatherChangeEvent e) {
//        boolean rain = e.toWeatherState();
//        if(!rain)
//        	return;
//		
//    	if(Model.existWorld(e.getWorld()) && !Model.getWorld(e.getWorld()).isAllowWeather())
//    		e.setCancelled(true);
//    }
// 
//    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
//    public void onThunderChangeEvent(ThunderChangeEvent e) {
//        boolean storm = e.toThunderState();
//        if(!storm)
//        	return;
//		
//    	if(Model.existWorld(e.getWorld()) && !Model.getWorld(e.getWorld()).isAllowWeather())
//    		e.setCancelled(true);
//    }
}

package mkproject.maskat.WorldManager;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;

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
		if(!Model.existWorld(e.getTo().getWorld()))
			return;
		
		if(Model.getWorld(e.getTo().getWorld()).getBorderRadius() >= 0 && e.getTo().distance(e.getTo().getWorld().getSpawnLocation()) > Model.getWorld(e.getTo().getWorld()).getBorderRadius())
			e.setCancelled(true);
				
		if(Model.getWorld(e.getTo().getWorld()).getBorderRadiusSquared() >= 0 && e.getTo().distanceSquared(e.getTo().getWorld().getSpawnLocation()) > Model.getWorld(e.getTo().getWorld()).getBorderRadiusSquared())
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		if(e.getPlayer().hasPermission("mkp.worldmanager.bypass.gamemode"))
			return;
		
		try {
			e.getPlayer().setGameMode(GameMode.valueOf(Plugin.getPlugin().getConfig().getString("configWorlds."+e.getPlayer().getLocation().getWorld().getName()+".GameMode").toUpperCase()));
		}
		catch(Exception ex) {
			
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerTeleportEventHighest(PlayerTeleportEvent e) {
		Plugin.getPlugin().getLogger().info("Player '"+e.getPlayer().getName()+"' teleport from "+e.getFrom().getWorld().getName()+","+e.getFrom().getBlockX()+","+e.getFrom().getBlockY()+","+e.getFrom().getBlockZ()
				+ " to "+e.getTo().getWorld().getName()+","+e.getFrom().getBlockX()+","+e.getFrom().getBlockY()+","+e.getFrom().getBlockZ());
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerTeleportEvent(PlayerTeleportEvent e) {
		if(e.getPlayer().getGameMode() == GameMode.SPECTATOR && !e.getPlayer().hasPermission("mkp.worldmanager.bypass.spectatorteleport"))
		{
			if(e.getFrom().getWorld() == e.getTo().getWorld() && e.getTo().getWorld().getName().indexOf("minigame_") == 0)
				return;
			
			e.setCancelled(true);
			return;
		}
		
		if(Model.existWorld(e.getTo().getWorld())) {
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
		}
		
		if(e.getFrom().getWorld() == e.getTo().getWorld())
			return;
		
		Papi.Model.getPlayer(e.getPlayer()).setSpeedDefault();
		
		if(e.getPlayer().hasPermission("mkp.worldmanager.bypass.gamemode"))
			return;
		
		//GameMode gamemode = GameMode.valueOf(Plugin.getPlugin().getConfig().getString("configWorlds."+e.getTo().getWorld().getName()+".GameMode").toUpperCase());
		if(!Model.existWorld(e.getTo().getWorld()))
			return;
		
		GameMode gamemode = Model.getWorld(e.getTo().getWorld()).getGameMode();
		e.getPlayer().setGameMode(gamemode);
		if(gamemode == GameMode.CREATIVE)
			e.getPlayer().setFlying(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerRespawnEvent(PlayerRespawnEvent e) {
		if(Model.existWorld(e.getRespawnLocation().getWorld())) {
			if(Model.getWorld(e.getRespawnLocation().getWorld()).getBorderRadius() >= 0 && e.getRespawnLocation().distance(e.getRespawnLocation().getWorld().getSpawnLocation()) > Model.getWorld(e.getRespawnLocation().getWorld()).getBorderRadius())
			{
				e.setRespawnLocation(e.getRespawnLocation().getWorld().getSpawnLocation());
			}
			
			if(Model.getWorld(e.getRespawnLocation().getWorld()).getBorderRadiusSquared() >= 0 && e.getRespawnLocation().distanceSquared(e.getRespawnLocation().getWorld().getSpawnLocation()) > Model.getWorld(e.getRespawnLocation().getWorld()).getBorderRadiusSquared())
			{
				e.setRespawnLocation(e.getRespawnLocation().getWorld().getSpawnLocation());
			}
		}
		
		if(e.getPlayer().hasPermission("mkp.worldmanager.bypass.gamemode"))
			return;
		
		if(!Model.existWorld(e.getRespawnLocation().getWorld()))
			return;
		
		e.getPlayer().setGameMode(Model.getWorld(e.getRespawnLocation().getWorld()).getGameMode());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDeathEvent(PlayerDeathEvent e) {
		Plugin.getPlugin().getLogger().info("Player '"+e.getEntity().getName()+"' death "+e.getEntity().getLocation().getWorld().getName()+", "+e.getEntity().getLocation().getBlockX()+", "+e.getEntity().getLocation().getBlockY()+", "+e.getEntity().getLocation().getBlockZ());
	}
	
	//Parties Friendly Fire
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player))
			return;
		
		World entityWorld = e.getEntity().getWorld();
		if(entityWorld == Papi.Server.getSurvivalWorld() || entityWorld.getName().equals("world_nether") || entityWorld.getName().equals("world_the_end"))
		{
			PartiesAPI api = Parties.getApi();
			PartyPlayer playerOne = api.getPartyPlayer(e.getEntity().getUniqueId()); // Get the player
			if(!playerOne.getPartyName().isEmpty()) {
				Party partyOne = api.getParty(playerOne.getPartyName()); // Get the party by its name
				if(partyOne != null) {
					if(partyOne.getMembers().contains(e.getDamager().getUniqueId()))
						e.setCancelled(true);
				}
			}
		}
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

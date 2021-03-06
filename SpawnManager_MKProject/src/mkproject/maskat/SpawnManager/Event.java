package mkproject.maskat.SpawnManager;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Model.PapiPlayerLoginEvent;
import mkproject.maskat.Papi.Utils.Message;

public class Event implements Listener {
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		Object userid = Papi.MySQL.get(Database.Users.ID, Database.Users.USERNAME, "=", e.getPlayer().getName().toLowerCase(), Database.Users.TABLE);
		if(userid == null) {
			userid = Papi.MySQL.put(Map.of(Database.Users.USERNAME,  e.getPlayer().getName().toLowerCase()), Database.Users.TABLE);
		} else {
			Object locationResult = Papi.MySQL.get(Database.Users.PLAYER_SURVIVAL_SPAWN_LOCATION, Database.Users.ID, "=", userid.toString(), Database.Users.TABLE);
			if(locationResult != null) {
				Object generatedResult = Papi.MySQL.get(Database.Users.PLAYER_SURVIVAL_SPAWN_GENERATED, Database.Users.ID, "=", userid.toString(), Database.Users.TABLE);
				if(generatedResult.toString().equalsIgnoreCase("true"))
					Papi.Model.getPlayer(e.getPlayer()).initPlayerSpawnGenerated(true);
				
				Location survivalspawnlocation = Papi.Function.getLocationFromString(locationResult.toString());
				Papi.Model.getPlayer(e.getPlayer()).initPlayerSpawnLocation(survivalspawnlocation);
				return;
			}
		}
		RandomTp.updatePlayerSpawnLocationAsync(e.getPlayer(), Papi.Server.getSurvivalWorld());
	}
	
	@EventHandler
	public void onPlayerTeleportEvent(PlayerTeleportEvent e) {
		if((e.getFrom().getWorld() == Papi.Server.getSurvivalWorld() || e.getFrom().getWorld().getName().equals("world_nether") || e.getFrom().getWorld().getName().equals("world_the_end")) && 
				(!e.getTo().getWorld().getName().equals("world_nether") && e.getTo().getWorld() != Papi.Server.getSurvivalWorld() && !e.getTo().getWorld().getName().equals("world_the_end")))
		{
			Function.setPlayerSurvivalLastLocation(e.getPlayer(), e.getFrom());
		}
		else if(e.getTo().getWorld().getName().equals("world_nether") || e.getTo().getWorld().getName().equals("world_the_end") || e.getTo().getWorld() == Papi.Server.getSurvivalWorld())
		{
			Function.setPlayerSurvivalLastLocation(e.getPlayer(), null);
		}
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent e) {
		Plugin.getPlugin().getLogger().warning("Player '"+e.getPlayer().getName()+"' respawning...");
		
		if(e.getPlayer().getLocation().getWorld() == Papi.Server.getServerSpawnWorld())
		{
			e.setRespawnLocation(Papi.Server.getServerSpawnLocation());
			return;
		}
		
		if(Papi.Model.getPlayer(e.getPlayer()).getSurvivalLastLocation() != null && 
				e.getPlayer().getLocation().getWorld() != Papi.Server.getSurvivalWorld() && 
				!e.getPlayer().getLocation().getWorld().getName().equals("world_nether") && 
				!e.getPlayer().getLocation().getWorld().getName().equals("world_the_end") && 
				!e.getPlayer().getLocation().getWorld().getName().equals("world_lobby"))
		{
			e.setRespawnLocation(Papi.Model.getPlayer(e.getPlayer()).getSurvivalLastLocation());
			return;
		}
		
		if(e.getPlayer().getBedSpawnLocation() == null)
		{
			if(Papi.Model.getPlayer(e.getPlayer()).getPlayerSpawnLocation() != null)
			{
				if(!Papi.Model.getPlayer(e.getPlayer()).isPlayerSpawnGenerated())
				{
					if(SpawnManagerAPI.checkValidLocation(Papi.Model.getPlayer(e.getPlayer()).getPlayerSpawnLocation()))
					{
						if(SpawnManagerAPI.generatePlayerSpawnSchemat(e.getPlayer()))
							Papi.Model.getPlayer(e.getPlayer()).initPlayerSpawnGenerated(true);
					}
					else
					{
						SpawnManagerAPI.updatePlayerSpawnLocationAsync(e.getPlayer());
					}
				}
			}
		}
//		Message.sendBroadcast("spawn loc:" + Papi.Model.getPlayer(e.getPlayer()).getRespawnLocation());
		e.setRespawnLocation(Papi.Model.getPlayer(e.getPlayer()).getRespawnLocation());
	}
	
	@EventHandler
	public void onPapiPlayerLoginEvent(PapiPlayerLoginEvent e) {
		Papi.Model.getPlayer(e.getPlayer()).initSurvivalLastLocation(Function.getPlayerSurvivalLastLocation(e.getPlayer()));
		
		if(e.getPlayer().getWorld() == Papi.Server.getServerSpawnWorld() && e.getPapiPlayer().getPlayerSpawnLocation() == null)
		{
			onPlayerJoinTaskAsync(e.getPlayer());
		}
	}
	
	private void onPlayerJoinTaskAsync(Player player) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(Plugin.getPlugin(), new Runnable() {
		      @Override
		      public void run() {
		    	  Plugin.getPlugin().getLogger().warning("Checking generate safe location for player: "+player.getName());
		    	  if(player.getWorld() == Papi.Server.getServerSpawnWorld())
		    	  {
		    		  if(Papi.Model.getPlayer(player).getPlayerSpawnLocation() == null)
		    		  {
		    			  Message.sendActionBar(player, "&eSzukamy dla ciebie bezpiecznego miejsca do odrodzenia na mapie survival...");
		    			  onPlayerJoinTaskAsync(player);
		    		  }
		    		  else
		    		  {
		    			  Plugin.getPlugin().getLogger().warning("Succes finded safe location for player: "+player.getName());
		    			  Message.sendActionBar(player, "&aZnaleźliśmy dla ciebie bezpieczne miejsca do odrodzenia na mapie survival!");
		    		  }
		    	  }
		      }}, 40L);
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e) {
		e.setDeathMessage(Message.getColorMessage("&8&o"+e.getDeathMessage()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeathEventHigh(PlayerDeathEvent e) {
		if(e.getEntity().getLocation().getWorld() == Papi.Server.getServerSpawnWorld())
		{
			e.setKeepInventory(true);
			e.setKeepLevel(true);
			e.getDrops().clear();
			e.setDroppedExp(0);
		}
	}
}

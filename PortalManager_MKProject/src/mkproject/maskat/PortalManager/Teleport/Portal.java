package mkproject.maskat.PortalManager.Teleport;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;
import mkproject.maskat.PortalManager.ModelPortal;
import mkproject.maskat.PortalManager.Plugin;
import mkproject.maskat.SpawnManager.SpawnManagerAPI;

public class Portal {
	public static void teleportPlayer(Player player, Entry<Integer, ModelPortal> entry) {
		//Model.addPlayer(player);
		//Model.getPlayer(player).setTeleporting(true);
		
		if(entry.getValue().getPortalDestination() != null)
		{
			Papi.Model.getPlayer(player).setSpeedFreeze();
			player.teleport(entry.getValue().getPortalDestination());
			//Model.removePlayer(player);
			Papi.Model.getPlayer(player).setSpeedDefault();
		}
		else if(entry.getValue().getPortalDestinationCustom().equalsIgnoreCase("toserverspawn"))
		{
			Papi.Model.getPlayer(player).setSpeedFreeze();
			player.teleport(Papi.Server.getServerSpawnLocation());
			//Model.removePlayer(player);
			Papi.Model.getPlayer(player).setSpeedDefault();
		}
		else if(entry.getValue().getPortalDestinationCustom().equalsIgnoreCase("toplayersurvivalspawn"))
		{
			//Message.sendTitle(player, "Szukam bezpiecznego miejsca...", "To może chwilę potrwać :)", 10, 70, 20);
			if(Papi.Model.getPlayer(player).getSurvivalLastLocation() != null)
			{
				player.teleport(Papi.Model.getPlayer(player).getSurvivalLastLocation());
				return;
			}
			
			
			Location playerSpawnLocation = Papi.Model.getPlayer(player).getPlayerSpawnLocation();
			if(playerSpawnLocation == null)
			{
				Message.sendMessage(player, "&cTrwa szukanie bezpiecznego miejsca dla ciebie...\n&6To może chwilę potrwać, prosimy o cierpliwość :)\n&bWróć tu za kilka chwil i spróbuj ponownie!");
				return;
			}
			else
			{
				if(Papi.Model.getPlayer(player).isPlayerSpawnGenerated())
				{
					Papi.Model.getPlayer(player).setSpeedFreeze();
					player.teleport(playerSpawnLocation);
					Papi.Model.getPlayer(player).setSpeedDefault();
					return;
				}
				else if(SpawnManagerAPI.checkValidLocation(playerSpawnLocation))
				{
					Papi.Model.getPlayer(player).setSpeedFreeze();
					Message.sendMessage(player, "&a&oTrwa teleportacja...");
					if(!Papi.Model.getPlayer(player).isPlayerSpawnGenerated())
					{
						if(!SpawnManagerAPI.generatePlayerSpawnSchemat(player))
						{
							Plugin.getPlugin().getLogger().warning(" ************** ERROR ************** ");
							Plugin.getPlugin().getLogger().warning(" Generate player spawn form schematic problem ! -> Player: "+player.getName());
							Plugin.getPlugin().getLogger().warning(" *********************************** ");
							Message.sendMessage(player, "&cCoś poszło nie tak... :( Prosimy skontaktować się z Administratorem!");
							Papi.Model.getPlayer(player).setSpeedDefault();
							return;
						}
					}
					player.teleport(playerSpawnLocation);
					
					firstTeleportToGeneratedPlayerSpawn(player);
					
					Papi.Model.getPlayer(player).setSpeedDefault();
					return;
				}
				else
				{
					Message.sendMessage(player, "&cTrwa szukanie bezpiecznego miejsca dla ciebie...\n&6To może chwilę potrwać, prosimy o cierpliwość :)\n&bWróć tu za kilka chwil i spróbuj ponownie!");
					SpawnManagerAPI.updatePlayerSpawnLocationAsync(player);
					return;
				}
			}
			

//			Bukkit.getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable() {
//			      @Override
//			      public void run() {
//			    	  Location loc = RandomTp.getRandomLocation(player, Bukkit.getWorld("world"));
//			    	  Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
//					      @Override
//					      public void run() {
//					    	  player.teleport(loc);
//					    	  Model.removePlayer(player);
//					    	  Papi.Model.getPlayer(player).setSpeedDefault();
//					      }});
//			      }});
		}
	}
	
	public static void firstTeleportToGeneratedPlayerSpawn(Player player) {
		Message.sendTitle(player, "&aPrawdziwy Survival", "&6Przetrwanie to twoje zadanie!", 10, 70, 20);
		player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
		player.spawnParticle(Particle.PORTAL, player.getLocation(), 50);
	}
}

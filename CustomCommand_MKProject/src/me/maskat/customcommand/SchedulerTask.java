package me.maskat.customcommand;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Scheduler.PapiScheduler;
import mkproject.maskat.Papi.Utils.Message;

public class SchedulerTask implements PapiScheduler {

	@Override
	public void runTaskThread() {
		for(World world : Bukkit.getWorlds()) {
			if(world.getName().indexOf(Config.WorldPrefix) == 0)
			{
				for(Player player : world.getPlayers()) {
					player.teleport(Papi.Server.getServerSpawnLocation());
					Message.sendMessage(player, "&c&lKonkurs został zakończony! Niedługo pojawią się wyniki");
				}
			}
		}
			
	}

}

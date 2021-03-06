package me.maskat.MoneyManager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.PapiPlugin;

public class Task {
	protected static void runUpdatePlayersPointsTask(int repeatTime) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(PapiPlugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				for(Player player : PapiPlugin.getPlugin().getServer().getOnlinePlayers())
				{
					if(Papi.Model.getPlayer(player).isLogged() && !Papi.Model.getPlayer(player).isAfk() && player.getGameMode() != GameMode.SPECTATOR)
						Mapi.getPlayer(player).addPoints(player.hasPermission("mkp.moneymanager.doublepointsreward") ? 3 : 1);
				}
				runUpdatePlayersPointsTask(repeatTime);
			}
		}, repeatTime*20L);
	}
}

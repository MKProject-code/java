package mkproject.maskat.Papi;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Model.PapiModel;

public class PapiTask {

	protected static void runCheckAfkPlayersTask(int repeatTime) {
		Collection<? extends Player> onlinePlayers = PapiPlugin.getPlugin().getServer().getOnlinePlayers();
		for(Player player : onlinePlayers)
		{
			if(PapiModel.existPlayer(player))
				PapiModel.getPlayer(player).updateAfkStatus();
		}
		Bukkit.getScheduler().runTaskLater(PapiPlugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				runCheckAfkPlayersTask(repeatTime);
			}
		}, repeatTime*20L);
	}
	
//	protected static void runSchedulerTask() {
////		Timer timer = new Timer();
////		timer.schedule(new TimerTask() {
////			@Override
////			public void run() {
////				DayOfWeek dayOfWeek = Papi.Function.getCurrentLocalDateTime().getDayOfWeek();
////				int hourOfDay = Papi.Function.getCurrentLocalDateTime().getHour();
////				int minute = Papi.Function.getCurrentLocalDateTime().getMinute();
////				int second = Papi.Function.getCurrentLocalDateTime().getSecond();
////				
////				
////			}
////		}, 0, 1000);
//		
//		Bukkit.getScheduler().runTaskTimerAsynchronously(Plugin.getPlugin(), new Runnable() {
//			@Override
//			public void run() {
//				PapiSchedulerManager.doCheck();
//			}
//		}, 20L, 20L);
//	}
}

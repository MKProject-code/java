package mkproject.maskat.ChatManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;

public class TaskAsyncTimer implements Runnable {
	private double LAST_TPS = 0D;
	@Override
	public void run() {
		double NOW_TPS = Papi.Function.getTPS();
		if(LAST_TPS == NOW_TPS)
			return;
		
		LAST_TPS = NOW_TPS;
		
		for(Player player : Bukkit.getServer().getOnlinePlayers())
		{
			TabFormatter.updateHeader(player);
		}
	}
}

package mkproject.maskat.ChatManager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;

public class TaskAsyncTimerPing implements Runnable {
	private Map<Player, Integer> lastPlayersPing = new HashMap<>();
	@Override
	public void run() {
		Map<Player, Integer> lastPlayersPingTemp = new HashMap<>();
		for(Player player : Bukkit.getOnlinePlayers()) {
			
			int ping = Papi.Model.getPlayer(player).getPing();
			lastPlayersPingTemp.put(player, ping);
			
			if(lastPlayersPing.containsKey(player) && lastPlayersPing.get(player) == ping)
				continue;
			
			TabFormatter.updatePing(player, ping);
		}
		lastPlayersPing = lastPlayersPingTemp;
	}
}

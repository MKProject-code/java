package mkproject.maskat.LoginManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.PapiPlugin;
import mkproject.maskat.Papi.Utils.Message;

public class Task {

	protected static void playerUnloggedTask(Player player, double repeatTime, String message) {
		Message.sendMessage(player, message);
		Bukkit.getScheduler().runTaskLater(PapiPlugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if(player.isValid()
						&&
						player.isOnline()
						&&
						!Model.getPlayer(player).isLogged() && !Model.getPlayer(player).isExecutedFirstCmd())
					playerUnloggedTask(player, repeatTime, message);
			}
		}, (long)(repeatTime*20.0));
	}
}

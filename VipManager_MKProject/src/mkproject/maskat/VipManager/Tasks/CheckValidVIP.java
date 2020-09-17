package mkproject.maskat.VipManager.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mkproject.maskat.VipManager.Manager;

public class CheckValidVIP implements Runnable {
	@Override
	public void run() {
		for(Player playerOnline : Bukkit.getOnlinePlayers()) {
			if(playerOnline.hasPermission("group.vip") || playerOnline.hasPermission("group.svip"))
				Manager.checkValidVIP(playerOnline, true);
		}
	}
}

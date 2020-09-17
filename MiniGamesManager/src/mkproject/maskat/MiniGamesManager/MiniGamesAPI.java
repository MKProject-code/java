package mkproject.maskat.MiniGamesManager;

import org.bukkit.entity.Player;

import mkproject.maskat.MiniGamesManager.Menu.PlayerMenu;
import mkproject.maskat.Papi.Menu.PapiMenu;

public abstract class MiniGamesAPI {
	public static void openMenu(Player player, PapiMenu backMenu) {
		new PlayerMenu(backMenu).openPapiMenuPage(player);
	}
}

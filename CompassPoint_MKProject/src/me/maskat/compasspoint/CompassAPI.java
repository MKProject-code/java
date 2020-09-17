package me.maskat.compasspoint;

import org.bukkit.entity.Player;

import me.maskat.compasspoint.inventories.MainMenuV2;
import mkproject.maskat.Papi.Menu.PapiMenu;

public class CompassAPI {
	public static void openCompassMenu(Player player, PapiMenu backMenu) {
		new MainMenuV2().initOpenMenu(player, backMenu);
	}
}

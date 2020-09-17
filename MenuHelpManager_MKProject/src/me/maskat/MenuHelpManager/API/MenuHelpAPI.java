package me.maskat.MenuHelpManager.API;

import org.bukkit.entity.Player;

import me.maskat.MenuHelpManager.BungeeMenu.ServersMenu;
import me.maskat.MenuHelpManager.PlayerMenu.MainMenu;

public class MenuHelpAPI {
	public static void openMenuHelp(Player player) {
		new MainMenu().initOpenMenu(player);
	}
	public static class BungeeMenu {
		public static void openServersMenu(Player player) {
			new ServersMenu().initOpenMenu(player);
		}
	}
}

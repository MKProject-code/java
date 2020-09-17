package me.maskat.ShopManager;

import org.bukkit.entity.Player;

import me.maskat.ShopManager.PlayerMenu.CategoriesMenu;
import mkproject.maskat.Papi.Menu.PapiMenu;

public class ShopAPI {
	public static void openShopMenu(Player player, PapiMenu backPapiMenu) {
		PapiMenu papiMenu = new CategoriesMenu();
		papiMenu.initPapiMenu(player, 1, backPapiMenu);
		papiMenu.openPapiMenuPage(player);
	}
}

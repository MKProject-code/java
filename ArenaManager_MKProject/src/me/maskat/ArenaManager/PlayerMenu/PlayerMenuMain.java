package me.maskat.ArenaManager.PlayerMenu;

import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Menu.PapiMenu;

public class PlayerMenuMain {
	
	public static enum Page {
		LIST,
		ARENA,
	}
	public static enum SlotOption {
		//List
		STATS_PLAYER,
		STATS_SERVER,
		ITEM_ARENA,
		
		//Arena
		REGISTER_IN_ARENA,
		UNREGISTER_IN_ARENA,
		ITEM_TEAM,
		BACK,
	}
	public static enum Action {
		NONE,
		SUCCESS,
		ERROR,
		EXIST
	}
	
	public static void openMenu(Player player, PapiMenu backMenu) {
		new PlayerMenu_List().openMenuList(player, backMenu);
	}
	public static void openMenu(Player player) {
		new PlayerMenu_List().openMenuList(player);
	}
}

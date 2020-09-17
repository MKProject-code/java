package me.maskat.ArenaManager.PlayerMenu_old;

import org.bukkit.entity.Player;

import mkproject.maskat.Papi.MenuInventory.PapiMenuInventoryClickEvent;

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
	
	public static void openMenu(Player player) {
		PlayerMenu_List.openMenuList(player);
	}
	
	public static void onPapiMenuInventoryClick(PapiMenuInventoryClickEvent e) {
		if(!e.existSlotStoreObject() || !(e.getSlotStoreObject() instanceof PlayerMenuItem))
			return;
		
		if(e.getUniquePageId() == Page.LIST)
			PlayerMenu_List.onMenuListClick(e, (PlayerMenuItem)e.getSlotStoreObject());
		if(e.getUniquePageId() == Page.ARENA)
			PlayerMenu_Arena.onMenuArenaClick(e, (PlayerMenuItem)e.getSlotStoreObject());
	}
}

package me.maskat.ArenaManager.PlayerMenu;

import org.bukkit.entity.Player;

import me.maskat.ArenaManager.Models.ModelArena;
import me.maskat.ArenaManager.PlayerMenu.PlayerMenuMain.SlotOption;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuPage;

public class PlayerMenu_Header {
	protected static PapiMenuPage getMenuHeader(PapiMenu papiMenu, Player player, PlayerMenuItem menuItem) {
		PapiMenuPage menuPage = new PapiMenuPage(papiMenu, 6, " * * * * * Areny Skyidea * * * * *");
		

		
		//menuPage.setItem(InventorySlot.ROW1_COLUMN8, Material.BOOK, "&aPlugin autorstwa &bSkyidea.pl &a- &owersja beta!\n&7Wszelkie błędy prosimy zgłaszać administracji.");
		
//		menuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.WOODEN_SWORD, " ");
//		menuPage.setItem(InventorySlot.ROW2_COLUMN1, Material.STONE_SWORD, " ");
//		menuPage.setItem(InventorySlot.ROW3_COLUMN1, Material.IRON_SWORD, " ");
//		menuPage.setItem(InventorySlot.ROW4_COLUMN1, Material.GOLDEN_SWORD, " ");
//		menuPage.setItem(InventorySlot.ROW5_COLUMN1, Material.DIAMOND_SWORD, " ");
//		menuPage.setItem(InventorySlot.ROW6_COLUMN1, Material.SHIELD, " ");
//		
//		menuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.WOODEN_SWORD, " ");
//		menuPage.setItem(InventorySlot.ROW2_COLUMN9, Material.STONE_SWORD, " ");
//		menuPage.setItem(InventorySlot.ROW3_COLUMN9, Material.IRON_SWORD, " ");
//		menuPage.setItem(InventorySlot.ROW4_COLUMN9, Material.GOLDEN_SWORD, " ");
//		menuPage.setItem(InventorySlot.ROW5_COLUMN9, Material.DIAMOND_SWORD, " ");
//		menuPage.setItem(InventorySlot.ROW6_COLUMN9, Material.SHIELD, " ");
		
		return menuPage;
	}

	public static void getSlotArenaInfo(PapiMenuPage menuPage, InventorySlot invSlot, ModelArena modelArena) {
		int playersRegisteredSize = modelArena.getPlayersRegistered().size();
		menuPage.setItem(invSlot, modelArena.getIcon(),
				 "&6Arena: &e"+modelArena.getName()
				+"\n&fZapisanych: "+(playersRegisteredSize == 0 ? "&7" : playersRegisteredSize >= modelArena.getMaxPlayers() ? "&c" : "&a") + playersRegisteredSize +" / " + modelArena.getMaxPlayers()
				+"\n&fTyp: &7"+modelArena.getType()
				+"\n&fOpis: &7"+modelArena.getDescription()
				, new PlayerMenuItem(menuPage, SlotOption.ITEM_ARENA, modelArena));
	}
}

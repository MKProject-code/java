package me.maskat.ArenaManager.PlayerMenu_old;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.maskat.ArenaManager.Plugin;
import me.maskat.ArenaManager.Models.ArenesModel;
import me.maskat.ArenaManager.Models.ModelArena;
import me.maskat.ArenaManager.PlayerMenu.PlayerMenuMain.Page;
import me.maskat.ArenaManager.PlayerMenu.PlayerMenuMain.SlotOption;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.MenuInventory.InventorySlot;
import mkproject.maskat.Papi.MenuInventory.MenuPage;

public class PlayerMenu_Header {
	protected static MenuPage getMenuHeader(Player player, PlayerMenuItem menuItem, Page page) {
		MenuPage menuPage = Papi.Model.getPlayer(player).createMenu(Plugin.getPlugin(), 6, "* * * * * Areny Skyidea.pl * * * * *", page);
		
		if(!ArenesModel.getPlayer(player).isRegisteredArena())
			menuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.REDSTONE_TORCH, "&cAktualnie nie jesteś zapisany do żadnej areny\n&7Wybierz arene aby się zapisać");
		else
			menuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.REDSTONE_TORCH,
					"&aJesteś zapisany:"
					+"\n&6Arena: &e"+ArenesModel.getPlayer(player).getRegisteredArena().getName()
					+"\n&6Drużyna: &e"+ArenesModel.getPlayer(player).getRegisteredTeam().getName()
					);
		menuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.PAINTING, "&6Twoje statystyki\n&7Jeszcze niedostępne...", new PlayerMenuItem(menuPage, SlotOption.STATS_PLAYER, menuItem));
		menuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.PAINTING, "&6Statystyki serwera\n&7Jeszcze niedostępne...", new PlayerMenuItem(menuPage, SlotOption.STATS_SERVER, menuItem));
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN8, Material.BOOK, "&aPlugin autorstwa &bSkyidea.pl &a- &owersja beta!\n&7Wszelkie błędy prosimy zgłaszać administracji.");
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.WOODEN_SWORD, " ");
		menuPage.setItem(InventorySlot.ROW2_COLUMN1, Material.STONE_SWORD, " ");
		menuPage.setItem(InventorySlot.ROW3_COLUMN1, Material.IRON_SWORD, " ");
		menuPage.setItem(InventorySlot.ROW4_COLUMN1, Material.GOLDEN_SWORD, " ");
		menuPage.setItem(InventorySlot.ROW5_COLUMN1, Material.DIAMOND_SWORD, " ");
		menuPage.setItem(InventorySlot.ROW6_COLUMN1, Material.SHIELD, " ");
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.WOODEN_SWORD, " ");
		menuPage.setItem(InventorySlot.ROW2_COLUMN9, Material.STONE_SWORD, " ");
		menuPage.setItem(InventorySlot.ROW3_COLUMN9, Material.IRON_SWORD, " ");
		menuPage.setItem(InventorySlot.ROW4_COLUMN9, Material.GOLDEN_SWORD, " ");
		menuPage.setItem(InventorySlot.ROW5_COLUMN9, Material.DIAMOND_SWORD, " ");
		menuPage.setItem(InventorySlot.ROW6_COLUMN9, Material.SHIELD, " ");
		
		return menuPage;
	}

	public static void getSlotArenaInfo(MenuPage menuPage, InventorySlot invSlot, ModelArena modelArena) {
		int playersRegisteredSize = modelArena.getPlayersRegistered().size();
		menuPage.setItem(invSlot, modelArena.getIcon(),
				 "&6Arena: &e"+modelArena.getName()
				+"\n&fZapisanych: "+(playersRegisteredSize == 0 ? "&7" : playersRegisteredSize >= modelArena.getMaxPlayers() ? "&c" : "&a") + playersRegisteredSize +" / " + modelArena.getMaxPlayers()
				+"\n&fTyp: &7"+modelArena.getType()
				+"\n&fOpis: &7"+modelArena.getDescription()
				, new PlayerMenuItem(menuPage, SlotOption.ITEM_ARENA, modelArena));
	}
}

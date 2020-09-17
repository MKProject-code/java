package me.maskat.ArenaManager.PlayerMenu_old;

import java.util.Map;

import org.bukkit.entity.Player;

import me.maskat.ArenaManager.Models.ArenesModel;
import me.maskat.ArenaManager.Models.ModelArena;
import me.maskat.ArenaManager.PlayerMenu.PlayerMenuMain.Page;
import me.maskat.ArenaManager.PlayerMenu.PlayerMenuMain.SlotOption;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.MenuInventory.InventorySlot;
import mkproject.maskat.Papi.MenuInventory.MenuPage;
import mkproject.maskat.Papi.MenuInventory.PapiMenuInventoryClickEvent;

public class PlayerMenu_List {
	
	public static void openMenuList(Player player) {//, InventorySlot selectedInvSlot, Action selectedAction

		MenuPage menuPage = PlayerMenu_Header.getMenuHeader(player, null, Page.LIST);
		
	    int iV=2;
	    int iH=3;
		for (Map.Entry<Integer, ModelArena> entry : ArenesModel.getArenesMap().entrySet()) {
			if(entry.getValue().isEnabled()) {
				InventorySlot invSlot = InventorySlot.valueOf((iV*9)+iH-1);
//				if((invSlot == selectedInvSlot && selectedAction == Action.EXIST) || ArenesModel.getPlayer(player).getInsideArenaId() == entry.getKey())
//					menuPage.setItem(invSlot, entry.getValue().getIcon(), "&c"+entry.getValue().getName()+"\n&eOpis: "+entry.getValue().getDescription()+"\n&eTyp: "+entry.getValue().getType()+"\n&eZapisanych: "+entry.getValue().getPlayersRegistered()+" / " + entry.getValue().getMaxPlayers()+"\n&a&oJesteś zapisany", new PlayerMenuItem(menuPage, SlotOption.ITEM_ARENA, entry.getValue()));
//				else if(invSlot == selectedInvSlot)
//				{
//					if(selectedAction == Action.SUCCESS)
//						menuPage.setItem(invSlot, entry.getValue().getIcon(), "&c"+entry.getValue().getName()+"\n&eOpis: "+entry.getValue().getDescription()+"\n&eTyp: "+entry.getValue().getType()+"\n&eZapisanych: "+entry.getValue().getPlayersRegistered()+" / " + entry.getValue().getMaxPlayers()+"\n&a&oZapisałeś się!", new PlayerMenuItem(menuPage, SlotOption.ITEM_ARENA, entry.getValue()));
//					else
//						menuPage.setItem(invSlot, entry.getValue().getIcon(), "&c"+entry.getValue().getName()+"\n&eOpis: "+entry.getValue().getDescription()+"\n&eTyp: "+entry.getValue().getType()+"\n&eZapisanych: "+entry.getValue().getPlayersRegistered()+" / " + entry.getValue().getMaxPlayers()+"\n&c&oBrak miejsc", new PlayerMenuItem(menuPage, SlotOption.ITEM_ARENA, entry.getValue()));
//				}
//				else if(entry.getValue().getPlayersRegistered() < entry.getValue().getMaxPlayers())
//					menuPage.setItem(invSlot, entry.getValue().getIcon(), "&c"+entry.getValue().getName()+"\n&eOpis: "+entry.getValue().getDescription()+"\n&eTyp: "+entry.getValue().getType()+"\n&eZapisanych: "+entry.getValue().getPlayersRegistered()+" / " + entry.getValue().getMaxPlayers()+"\n&7&oKliknij, aby się zapisać", new PlayerMenuItem(menuPage, SlotOption.ITEM_ARENA, entry.getValue()));
//				else
//					menuPage.setItem(invSlot, entry.getValue().getIcon(), "&c"+entry.getValue().getName()+"\n&eOpis: "+entry.getValue().getDescription()+"\n&eTyp: "+entry.getValue().getType()+"\n&eZapisanych: "+entry.getValue().getPlayersRegistered()+" / " + entry.getValue().getMaxPlayers()+"\n&c&oBrak miejsc", new PlayerMenuItem(menuPage, SlotOption.ITEM_ARENA, entry.getValue()));
				
				PlayerMenu_Header.getSlotArenaInfo(menuPage, invSlot, entry.getValue());
				
				if(iH >= 7) {
					iH=3;
					iV++;
				}
				else
					iH++;
			}
		}
		
		Papi.Model.getPlayer(player).openMenu(menuPage);
	}

	public static void onMenuListClick(PapiMenuInventoryClickEvent e, PlayerMenuItem menuItem) {
		if(menuItem.getSlotOption() == SlotOption.ITEM_ARENA) {
			//TODO: jesli wiecej niz 1 team ma maxPlayers>0 to otwierac menu z wyborem druzyny
//			if(ArenesModel.getPlayer(e.getPlayer()).getInsideArenaId() == item.getArenaModel().getId())
//				openMenuList(e.getPlayer(), e.getSlot(), Action.EXIST);
//			else if(item.getArenaModel().registerPlayer(e.getPlayer()))
//				openMenuList(e.getPlayer(), e.getSlot(), Action.SUCCESS);
//			else
//				openMenuList(e.getPlayer(), e.getSlot(), Action.ERROR);
			PlayerMenu_Arena.openMenuArena(e.getPlayer(), menuItem);
		}
	}
	
}

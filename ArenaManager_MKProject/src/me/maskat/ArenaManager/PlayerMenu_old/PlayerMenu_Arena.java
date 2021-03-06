package me.maskat.ArenaManager.PlayerMenu_old;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.maskat.ArenaManager.Models.ArenesModel;
import me.maskat.ArenaManager.Models.ModelArenaPlayer;
import me.maskat.ArenaManager.Models.ModelArenaTeam;
import me.maskat.ArenaManager.PlayerMenu.PlayerMenuMain.Page;
import me.maskat.ArenaManager.PlayerMenu.PlayerMenuMain.SlotOption;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.MenuInventory.InventorySlot;
import mkproject.maskat.Papi.MenuInventory.MenuPage;
import mkproject.maskat.Papi.MenuInventory.PapiMenuInventoryClickEvent;

public class PlayerMenu_Arena {
	public static void openMenuArena(Player player, PlayerMenuItem menuItem) {
		
		MenuPage menuPage = PlayerMenu_Header.getMenuHeader(player, menuItem, Page.ARENA);

		menuPage.setItem(InventorySlot.ROW3_COLUMN3, Material.ENDER_PEARL, "&7Powrót", new PlayerMenuItem(menuPage, SlotOption.BACK, menuItem));
		
		PlayerMenu_Header.getSlotArenaInfo(menuPage, InventorySlot.ROW3_COLUMN4, menuItem.getArenaModel());
		
//		String playerslist = "";
//		List<ModelArenaPlayer> modelPlayersList = menuItem.getArenaModel().getPlayersRegistered();
//		for(ModelArenaPlayer modelPlayer : modelPlayersList) {
//			playerslist += "\n"+modelPlayer.getPlayer().getName();
//		}
		
		//menuPage.setItem(InventorySlot.ROW3_COLUMN4, Material.PLAYER_HEAD, "Zapisani gracze: "+playerslist);
		if(ArenesModel.getPlayer(player).getRegisteredArenaId() == menuItem.getArenaModel().getId())
		{
			ModelArenaPlayer modelArenaPlayer = ArenesModel.getPlayer(player);
			ModelArenaTeam modelArenaTeam = modelArenaPlayer.getRegisteredTeam();
			String teamName = modelArenaTeam.getName();
			menuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.LIME_DYE, "&aJesteś zapisany\n&6Twoja drużyna: &e"+teamName+"\n&7Kliknij, aby się wypisać", new PlayerMenuItem(menuPage, SlotOption.UNREGISTER_IN_ARENA, menuItem));
		}
		else if(menuItem.getArenaModel().isAlreadyGamed() || menuItem.getArenaModel().getPlayersRegistered().size() >= menuItem.getArenaModel().getMaxPlayers())
			menuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.PINK_DYE, "&cNie możesz się teraz zapisać do tej areny");
		else
			menuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.NETHER_STAR, "&bZapisz się do areny\n&7Kliknij tutaj, aby wybrać losową drużynę\n&7&oUWAGA! Spowoduje to wypisanie z innych aren!", new PlayerMenuItem(menuPage, SlotOption.REGISTER_IN_ARENA, menuItem));
		//\n&7lub wybierz ją z poniższej listy
	    int iV=3;
	    int iH=3;
		for (Entry<Integer, ModelArenaTeam> entry : menuItem.getArenaModel().getTeamsMap().entrySet()) {
			if(entry.getValue().isAllowPlayers()) {
				InventorySlot invSlot = InventorySlot.valueOf((iV*9)+iH-1);
				List<String> playersRegisteredNames = entry.getValue().getPlayersRegisteredNames();
				menuPage.setItem(invSlot, entry.getValue().getIcon(),
						 "&6Drużyna: &e"+entry.getValue().getName()
						+(ArenesModel.getPlayer(player).getRegisteredTeamId() == entry.getValue().getId() ? "\n&aJesteś zapisany do tej drużyny" : menuItem.getArenaModel().isAlreadyGamed() ? "" : playersRegisteredNames.size() >= entry.getValue().getMaxPlayers() ? "" : "\n&7Kliknij tutaj, aby zapisać się to tej drużyny")
						+"\n&fZapisanych: "+(playersRegisteredNames.size() == 0 ? "&7" : playersRegisteredNames.size() >= entry.getValue().getMaxPlayers() ? "&c" : "&a") + playersRegisteredNames.size() + " / " + entry.getValue().getMaxPlayers()
						+(playersRegisteredNames.size() > 0 ? "\n&7- &o"+String.join("\n&7- &o", playersRegisteredNames) : "")
						, new PlayerMenuItem(menuPage, SlotOption.ITEM_TEAM, menuItem.getArenaModel(), entry.getValue()));
				
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

	public static void onMenuArenaClick(PapiMenuInventoryClickEvent e, PlayerMenuItem menuItem) {
		if(menuItem.getSlotOption() == SlotOption.BACK)
		{
			PlayerMenu_List.openMenuList(e.getPlayer());
			return;
		}
		else if(menuItem.getSlotOption() == SlotOption.REGISTER_IN_ARENA && !menuItem.getArenaModel().isAlreadyGamed() && menuItem.getArenaModel().getPlayersRegistered().size() < menuItem.getArenaModel().getMaxPlayers())
		{
			ArenesModel.getPlayer(e.getPlayer()).setRegisteredTeamRandom(menuItem.getArenaModel());
			
			if(menuItem.getArenaModel().getPlayersRegistered().size() >= menuItem.getArenaModel().getMaxPlayers())
			{
				menuItem.getArenaModel().doPrepareArena();
				e.getPlayer().closeInventory();
				return;
			}
			openMenuArena(e.getPlayer(), menuItem);
		}
		else if(menuItem.getSlotOption() == SlotOption.ITEM_TEAM && !menuItem.getArenaModel().isAlreadyGamed() && menuItem.getArenaModel().getPlayersRegistered().size() < menuItem.getArenaModel().getMaxPlayers())
		{
			if(ArenesModel.getPlayer(e.getPlayer()).getRegisteredTeamId() == menuItem.getArenaTeamModel().getId())
			{
				//register del
				ArenesModel.getPlayer(e.getPlayer()).unsetRegisteredArena();
				openMenuArena(e.getPlayer(), menuItem);
			}
			else if(menuItem.getArenaTeamModel().getPlayersRegistered().size() < menuItem.getArenaTeamModel().getMaxPlayers())
			{
				//register add
				ArenesModel.getPlayer(e.getPlayer()).setRegisteredTeamId(menuItem.getArenaModel().getId(), menuItem.getArenaTeamModel().getId());
				
				if(menuItem.getArenaModel().getPlayersRegistered().size() >= menuItem.getArenaModel().getMaxPlayers())
				{
					menuItem.getArenaModel().doPrepareArena();
					e.getPlayer().closeInventory();
					return;
				}
				openMenuArena(e.getPlayer(), menuItem);
			}
		}
		else if(menuItem.getSlotOption() == SlotOption.UNREGISTER_IN_ARENA && !menuItem.getArenaModel().isAlreadyGamed())
		{
			ArenesModel.getPlayer(e.getPlayer()).unsetRegisteredArena();
			openMenuArena(e.getPlayer(), menuItem);
		}
	}
	
}

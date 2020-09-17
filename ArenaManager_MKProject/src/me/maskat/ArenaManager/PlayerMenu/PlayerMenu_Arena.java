package me.maskat.ArenaManager.PlayerMenu;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.maskat.ArenaManager.Models.ArenesModel;
import me.maskat.ArenaManager.Models.ModelArenaPlayer;
import me.maskat.ArenaManager.Models.ModelArenaTeam;
import me.maskat.ArenaManager.PlayerMenu.PlayerMenuMain.SlotOption;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuPage;

public class PlayerMenu_Arena implements PapiMenu {
	private PapiMenuPage papiMenuPage;
	private PlayerMenuItem menuItem;
	
	@Override
	public PapiMenuPage getPapiMenuPage() {
		return this.papiMenuPage;
	}
	
	private PapiMenu backMenu;
	
	public void openMenuArena(PapiMenu backMenu, Player player, PlayerMenuItem menuItem) {
		this.menuItem = menuItem;
		initPapiMenu(player, 1, backMenu);
	}
	
	@Override
	public void initPapiMenu(Player player, int page, PapiMenu backMenu) {
		if(backMenu != null)
			this.backMenu = backMenu;
		
		this.papiMenuPage = PlayerMenu_Header.getMenuHeader(this, player, menuItem);

		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		//this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN3, Material.ENDER_PEARL, "&7Powrót", this.backPage);
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.BOOK, "&7Wróć do listy aren", this.backMenu);
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		
		PlayerMenu_Header.getSlotArenaInfo(papiMenuPage, InventorySlot.ROW2_COLUMN5, menuItem.getArenaModel());
		
		if(ArenesModel.getPlayer(player).getRegisteredArenaId() == menuItem.getArenaModel().getId())
		{
			ModelArenaPlayer modelArenaPlayer = ArenesModel.getPlayer(player);
			ModelArenaTeam modelArenaTeam = modelArenaPlayer.getRegisteredTeam();
			String teamName = modelArenaTeam.getName();
			this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.LIME_DYE, "&aJesteś zapisany\n&6Twoja drużyna: &e"+teamName+"\n&7Kliknij, aby się wypisać", new PlayerMenuItem(papiMenuPage, SlotOption.UNREGISTER_IN_ARENA, menuItem));
		}
		else if(menuItem.getArenaModel().isAlreadyGamed() || menuItem.getArenaModel().getPlayersRegistered().size() >= menuItem.getArenaModel().getMaxPlayers())
			this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.PINK_DYE, "&cNie możesz się teraz zapisać do tej areny");
		else
			this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.NETHER_STAR, "&bZapisz się do areny\n&7Kliknij tutaj, aby wybrać losową drużynę\n&7&oUWAGA! Spowoduje to wypisanie z innych aren!", new PlayerMenuItem(papiMenuPage, SlotOption.REGISTER_IN_ARENA, menuItem));
		
	    int iV=3;
	    int iH=3;
		for (Entry<Integer, ModelArenaTeam> entry : menuItem.getArenaModel().getTeamsMap().entrySet()) {
			if(entry.getValue().isAllowPlayers()) {
				InventorySlot invSlot = InventorySlot.valueOf((iV*9)+iH-1);
				List<String> playersRegisteredNames = entry.getValue().getPlayersRegisteredNames();
				this.papiMenuPage.setItem(invSlot, entry.getValue().getIcon(),
						 "&6Drużyna: &e"+entry.getValue().getName()
						+(ArenesModel.getPlayer(player).getRegisteredTeamId() == entry.getValue().getId() ? "\n&aJesteś zapisany do tej drużyny" : menuItem.getArenaModel().isAlreadyGamed() ? "" : playersRegisteredNames.size() >= entry.getValue().getMaxPlayers() ? "" : "\n&7Kliknij tutaj, aby zapisać się do tej drużyny")
						+"\n&fZapisanych: "+(playersRegisteredNames.size() == 0 ? "&7" : playersRegisteredNames.size() >= entry.getValue().getMaxPlayers() ? "&c" : "&a") + playersRegisteredNames.size() + " / " + entry.getValue().getMaxPlayers()
						+(playersRegisteredNames.size() > 0 ? "\n&7- &o"+String.join("\n&7- &o", playersRegisteredNames) : "")
						, new PlayerMenuItem(this.papiMenuPage, SlotOption.ITEM_TEAM, menuItem.getArenaModel(), entry.getValue()));
				
				if(iH >= 7) {
					iH=3;
					iV++;
				}
				else
					iH++;
			}
		}
		
		
		Papi.Model.getPlayer(player).openMenu(this.papiMenuPage);
	}

	@Override
	public void onMenuClick(PapiMenuClickEvent e) {
		if(e.getSlotStoreObject() == null)
			return;
		
		if(e.getSlotStoreObject() instanceof PlayerMenu_List)
		{
			((PlayerMenu_List)e.getSlotStoreObject()).openMenuList(e.getPlayer());
			return;
		}
		PlayerMenuItem menuItem = (PlayerMenuItem)e.getSlotStoreObject();
		
		if(menuItem.getSlotOption() == SlotOption.BACK)
		{
			new PlayerMenu_List().openMenuList(e.getPlayer());
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
			this.openMenuArena(null, e.getPlayer(), menuItem);
		}
		else if(menuItem.getSlotOption() == SlotOption.ITEM_TEAM && !menuItem.getArenaModel().isAlreadyGamed() && menuItem.getArenaModel().getPlayersRegistered().size() < menuItem.getArenaModel().getMaxPlayers())
		{
			if(ArenesModel.getPlayer(e.getPlayer()).getRegisteredTeamId() == menuItem.getArenaTeamModel().getId())
			{
				//register del
				ArenesModel.getPlayer(e.getPlayer()).unsetRegisteredArena();
				this.openMenuArena(null, e.getPlayer(), menuItem);
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
				this.openMenuArena(null, e.getPlayer(), menuItem);
			}
		}
		else if(menuItem.getSlotOption() == SlotOption.UNREGISTER_IN_ARENA && !menuItem.getArenaModel().isAlreadyGamed())
		{
			ArenesModel.getPlayer(e.getPlayer()).unsetRegisteredArena();
			this.openMenuArena(null, e.getPlayer(), menuItem);
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent event) {
		// TODO Auto-generated method stub
		
	}

	
}

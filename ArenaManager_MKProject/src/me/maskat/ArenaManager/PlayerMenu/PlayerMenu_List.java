package me.maskat.ArenaManager.PlayerMenu;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.maskat.ArenaManager.Models.ArenesModel;
import me.maskat.ArenaManager.Models.ModelArena;
import me.maskat.ArenaManager.PlayerMenu.PlayerMenuMain.SlotOption;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.PapiHeadBase64;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuPage;

public class PlayerMenu_List implements PapiMenu {
	
	private PapiMenuPage papiMenuPage;
	private PapiMenu backMenu;
	
	public PapiMenuPage getPapiMenuPage() {
		return this.papiMenuPage;
	}
	
	protected void openMenuList(Player player) {
		this.initPapiMenu(player, 1, null);
	}
	protected void openMenuList(Player player, PapiMenu backMenu) {
		this.initPapiMenu(player, 1, backMenu);
	}

	@Override
	public void initPapiMenu(Player player, int page, PapiMenu backMenu) {//, InventorySlot selectedInvSlot, Action selectedAction
		
		this.papiMenuPage = PlayerMenu_Header.getMenuHeader(this, player, null);
	
		if(backMenu != null)
			this.backMenu = backMenu;
		
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		if(this.backMenu != null)
			this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.BOOK, "&7Wróć do strony głównej", this.backMenu);
		else
			this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		
		if(!ArenesModel.getPlayer(player).isRegisteredArena())
			this.papiMenuPage.setItemHeadAsync(InventorySlot.ROW2_COLUMN3, Material.REDSTONE_TORCH, PapiHeadBase64.GREEN_Information, "&cAktualnie nie jesteś zapisany do żadnej areny\n&7Wybierz arene aby się zapisać");
		else
			this.papiMenuPage.setItemHeadAsync(InventorySlot.ROW2_COLUMN3, Material.REDSTONE_TORCH, PapiHeadBase64.GREEN_Information,
					"&aJesteś zapisany:"
					+"\n&6Arena: &e"+ArenesModel.getPlayer(player).getRegisteredArena().getName()
					+"\n&6Drużyna: &e"+ArenesModel.getPlayer(player).getRegisteredTeam().getName()
					);
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN5, Material.PAINTING, "&6Twoje statystyki\n&7Jeszcze niedostępne...", new PlayerMenuItem(this.papiMenuPage, SlotOption.STATS_PLAYER));
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN6, Material.PAINTING, "&6Statystyki serwera\n&7Jeszcze niedostępne...", new PlayerMenuItem(this.papiMenuPage, SlotOption.STATS_SERVER));
		
	    int iV=3;
	    int iH=3;
		for (Map.Entry<Integer, ModelArena> entry : ArenesModel.getArenesMap().entrySet()) {
			if(entry.getValue().isEnabled()) {
				InventorySlot invSlot = InventorySlot.valueOf((iV*9)+iH-1);
				
				PlayerMenu_Header.getSlotArenaInfo(this.papiMenuPage, invSlot, entry.getValue());
				
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
		
		if(e.getSlotStoreObject() instanceof PapiMenu)
		{
			((PapiMenu) e.getSlotStoreObject()).openPapiMenuPage(e.getPlayer());
			return;
		}
		
		PlayerMenuItem menuItem = (PlayerMenuItem)e.getSlotStoreObject();
		if(menuItem.getSlotOption() == SlotOption.ITEM_ARENA) {
			new PlayerMenu_Arena().openMenuArena(this, e.getPlayer(), menuItem);
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
		
	}


	
}

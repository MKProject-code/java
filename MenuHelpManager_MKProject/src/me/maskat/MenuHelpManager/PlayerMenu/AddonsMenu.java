package me.maskat.MenuHelpManager.PlayerMenu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuPage;

public class AddonsMenu implements PapiMenu {
	
	private PapiMenuPage papiMenuPage;

	@Override
	public PapiMenuPage getPapiMenuPage() {
		return papiMenuPage;
	}
	
	public void initOpenMenu(Player player, PapiMenu backMenu) {
		this.initPapiMenu(player, 1, backMenu);
		this.openPapiMenuPage(player);
	}
	
	@Override
	public void initPapiMenu(Player player, int page, PapiMenu backMenu) {
		this.papiMenuPage = new PapiMenuPage(this, 5, " * * * * * Dodatki Skyidea * * * * *");
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN5, Material.BOOK, "&7Wróć do strony głównej", backMenu);
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
//		this.papiMenuPage.setItemsList(InventorySlot.ROW2_COLUMN1, InventorySlot.ROW3_COLUMN2, 1,
//				new PapiMenuItem(Material.ACACIA_BOAT, "title", SlotOption.BACK)
//				);
		
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN1, Material.PAPER, "&f&lTwój własny spawn na mapie Survival\n&7(tzw. punkt startowy)");//\n&7Rozpoczynasz grę w chacie,\n&7która jest twoim osobistym spawnem
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN2, Material.PAPER, "&f&lLeczenie w łóżku\n&7Leżąc w łóżku regeneruje ci się zdrowie");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN3, Material.PAPER, "&f&lWizualizacja przedmiotów\n&7Zobacz przedmioty na craftingu itp");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN4, Material.PAPER, "&f&lNieskończony kocioł\n&7Kocioł ma nieskończoną ilość wody");
	}

	@Override
	public void onMenuClick(PapiMenuClickEvent e) {
		if(e.getSlotStoreObject() == null)
			return;
		
		if(e.getSlotStoreObject() instanceof PapiMenu)
		{
			PapiMenu papiMenu = (PapiMenu)e.getSlotStoreObject();
			papiMenu.openPapiMenuPage(e.getPlayer());
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
		
	}
}

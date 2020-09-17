package mkproject.maskat.CmdBlockAdvancedManager.Menu;

import org.bukkit.Material;

import mkproject.maskat.CmdBlockAdvancedManager.Plugin;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.MenuManager.SlotOption;
import mkproject.maskat.CmdBlockAdvancedManager.Models.CbamPlayer;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuItem;
import mkproject.maskat.Papi.Menu.PapiMenuPage;
import mkproject.maskat.Papi.Menu.PapiMenuV2;

public class Menu_Plugin implements PapiMenuV2 {
	
	private PapiMenuPage papiMenuPage;
	private CbamPlayer cbamPlayer;
	
	public Menu_Plugin(CbamPlayer cbamPlayer) {
		this.cbamPlayer = cbamPlayer;
	}
	
	@Override
	public PapiMenuPage getPapiMenuPage() {
		return this.papiMenuPage;
	}

	@Override
	public boolean refreshPapiMenuPage() {
		if(this.papiMenuPage == null)
			this.papiMenuPage = new PapiMenuPage(this, 6, "CommandBlock Advanced Manager by Skyidea.pl");
		else
			this.papiMenuPage.clearItems();
		this.loadMenuItems();
		return true;
	}
	
	private void loadMenuItems() {
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
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.BARRIER, "&7Exit", new PapiMenuItem(SlotOption.EXIT));
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN9, Material.SHULKER_SHELL, "&6Plugin configuration reload", new PapiMenuItem(SlotOption.RELOAD_CONFIGURATION));
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.LECTERN, "&6Open projects menu\nONLY CURRENT MAP", new PapiMenuItem(SlotOption.OPEN_PROJECTS_MENU));
	}

	@Override
	public void onMenuClick(PapiMenuClickEvent e) {
		if(e.getSlotStoreObject() == null)
			return;
		
		PapiMenuItem menuItem = (PapiMenuItem)e.getSlotStoreObject();
		
		if(menuItem.getSlotOption() == SlotOption.EXIT)
		{
			MenuManager.exitMenu(this.cbamPlayer);
		}
		else if(menuItem.getSlotOption() == SlotOption.RELOAD_CONFIGURATION)
		{
			Plugin.getPlugin().reloadConfig();
			e.getPapiMenuPage().setItemLoreLine(e.getSlot(), 1, "&aConfig reloaded.");
		}
		else if(menuItem.getSlotOption() == SlotOption.OPEN_PROJECTS_MENU)
		{
			MenuManager.openProjectsMenu(this, this.cbamPlayer, this.cbamPlayer.getPlayer().getWorld().getName(), 1);
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
	}


	
}

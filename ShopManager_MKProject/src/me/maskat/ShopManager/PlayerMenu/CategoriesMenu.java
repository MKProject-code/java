package me.maskat.ShopManager.PlayerMenu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.maskat.ShopManager.Plugin;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuPage;

public class CategoriesMenu implements PapiMenu {
	public void openMenu(Player player) {
		PapiMenuPage papiMenuPage = new PapiMenuPage(this, 6, "* * * * * * Sklep Skyidea * * * * * *");
		
		papiMenuPage.setItem(InventorySlot.ROW2_COLUMN5, Material.DIAMOND, "&bPROMOCJE DNIA", Plugin.CATEGORY_SALES_GENERATED);
		
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
	    int iV=2;
	    int iH=3;
		for(String key : Plugin.getPlugin().getConfig().getConfigurationSection("categories").getKeys(false))
		{
			if(key.equals(Plugin.CATEGORY_SALES_GENERATED))
				continue;
			
			try {
				InventorySlot invSlot = InventorySlot.valueOf((iV*9)+iH-1);
				Material icon = Material.valueOf(Plugin.getPlugin().getConfig().getString("categories."+key+".icon"));
				papiMenuPage.setItem(invSlot, icon, "&7Kategoria: &f&l"+Plugin.getPlugin().getConfig().getString("categories."+key+".name"), key);
				
				if(iH >= 7) {
					iH=3;
					iV++;
				}
				else
					iH++;
			} catch(Exception ex) {
				Plugin.getPlugin().getLogger().warning(">>>>>>>>>> Invalid configuration: categories."+key);
			}
		}
		
		Papi.Model.getPlayer(player).openMenu(papiMenuPage);
	}

	@Override
	public void onMenuClick(PapiMenuClickEvent event) {
		if(event.getSlotStoreObject() == null)
			return;

		new ItemsMenu().openMenu(this, event.getPlayer(), (String)event.getSlotStoreObject());
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent event) {
	}
}

package me.maskat.ShopManager.PlayerMenu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.maskat.ShopManager.Config;
import me.maskat.ShopManager.Function;
import me.maskat.ShopManager.Plugin;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuPage;

public class CategoriesMenu implements PapiMenu {
	private PapiMenuPage papiMenuPage;
	public PapiMenuPage getPapiMenuPage() {
		return papiMenuPage;
	}
	
	public void initOpenMenu(Player player) {
		this.initPapiMenu(player, 1, null);
		this.openPapiMenuPage(player);
	}
	
	@Override
	public void initPapiMenu(Player player, int page, PapiMenu backMenu) {
		this.papiMenuPage = new PapiMenuPage(this, 6, "* * * * * * Sklep Skyidea * * * * * *");
		
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN3, Material.EMERALD, "&a&lPromocje dnia", Plugin.CATEGORY_SALES_GENERATED);
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN5, Material.DIAMOND, "&b&lStrefa VIP+", Plugin.CATEGORY_ZONE_VIP_PLUS);
		
		String lastResetTheEndInfo = "";
		
		String lastResetTheEndDateTime = Config.getResetTheEndLastResetDateTime();
		String lastResetTheEndPlayerName = Config.getResetTheEndLastResetPlayerName();
		
		if(lastResetTheEndDateTime != null && lastResetTheEndDateTime.length() > 0 && lastResetTheEndPlayerName != null && lastResetTheEndPlayerName.length() > 0)
		{
			lastResetTheEndInfo = "\n\n&e&oOstatni reset: "+lastResetTheEndDateTime+"\n&e&oZresetował: "+lastResetTheEndPlayerName;
		}
		
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN7, Material.END_PORTAL_FRAME, "&d&lReset ENDu\n\n&2Zresetuj END za&a "+Config.getResetTheEndPrice()+" &2"+Function.Menu.getCurrency(Config.getResetTheEndPrice())+lastResetTheEndInfo, Plugin.CATEGORY_RESET_THE_END);

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
		
		if(backMenu == null)
			this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		else
			this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.BOOK, "&7Wróć do strony głównej", backMenu);
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		InventorySlot invSlot;
	    int iV=3;
	    int iH=2;
		for(String key : Plugin.getPlugin().getConfig().getConfigurationSection("categories").getKeys(false))
		{
			if(key.equals(Plugin.CATEGORY_SALES_GENERATED) || key.equals(Plugin.CATEGORY_RESET_THE_END) || key.equals(Plugin.CATEGORY_ZONE_VIP_PLUS))
				continue;
			
			try {
				invSlot = InventorySlot.valueOf((iV*9)+iH-1);
				Material icon = Material.valueOf(Plugin.getPlugin().getConfig().getString("categories."+key+".icon"));
				this.papiMenuPage.setItem(invSlot, icon, "&7Kategoria: &f&l"+Plugin.getPlugin().getConfig().getString("categories."+key+".name"), key);
				
				if(iH >= 8) {
					iH=2;
					iV++;
				}
				else
					iH++;
			} catch(Exception ex) {
				Plugin.getPlugin().getLogger().warning(">>>>>>>>>> Invalid configuration: categories."+key);
			}
		}
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
		
		String slotStoreObject = (String)e.getSlotStoreObject();
		
		if(slotStoreObject.equals(Plugin.CATEGORY_RESET_THE_END))
		{
			e.getPlayer().closeInventory();
			Manager.buyResetTheEnd(e.getPlayer(), Config.getResetTheEndPrice());
			return;
		}
		
		new ItemsMenu().initOpenMenu(this, e.getPlayer(), slotStoreObject);
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
	}
}

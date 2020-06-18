package me.maskat.ShopManager.PlayerMenu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.maskat.MoneyManager.Mapi;
import me.maskat.ShopManager.Function;
import me.maskat.ShopManager.Plugin;
import me.maskat.ShopManager.PlayerMenu.ItemsMenu.ItemsMenuSlot;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuPage;
import mkproject.maskat.Papi.Utils.Message;

public class SelectedItemMenu implements PapiMenu {
	class SelectedItemMenuSlot {
		private ItemsMenuSlot itemsMenuSlot;
		private int amount;
		
		public SelectedItemMenuSlot(ItemsMenuSlot itemsMenuSlot, int amount) {
			this.itemsMenuSlot = itemsMenuSlot;
			this.amount = amount;
		}
		protected ItemsMenuSlot getItemsMenuSlot() {
			return this.itemsMenuSlot;
		}
		protected int getAmount() {
			return this.amount;
		}
		protected int getPrice() {
			return this.getItemsMenuSlot().getPrice()*this.amount;
		}
		protected int getPriceSale() {
			return this.getItemsMenuSlot().getPriceSale()*this.amount;
		}
		protected int getPriceVipSale() {
			return this.getItemsMenuSlot().getPriceVipSale()*this.amount;
		}
		protected ItemStack getItemStack() {
			return this.itemsMenuSlot.getItemStack();
		}
		protected ItemStack getItemStackWithAmount() {
			ItemStack itemStack = this.itemsMenuSlot.getItemStack().clone();
			itemStack.setAmount(this.amount);
			return itemStack;
		}
	}
	
	private ItemsMenu backMenu;
	
	public void openMenu(ItemsMenu backMenu, Player player, ItemsMenuSlot itemsMenuSlot) {
		if(backMenu != null)
			this.backMenu = backMenu;
		
		PapiMenuPage papiMenuPage = new PapiMenuPage(this, 6, "* * * * * * Sklep Skyidea * * * * * *");
		
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.BOOK, "&7Powrót do listy przedmiotów", "BACK");
		
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

		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.addItemToMenu(papiMenuPage, InventorySlot.ROW3_COLUMN3, itemsMenuSlot, 1);
		this.addItemToMenu(papiMenuPage, InventorySlot.ROW3_COLUMN4, itemsMenuSlot, 8);
		this.addItemToMenu(papiMenuPage, InventorySlot.ROW3_COLUMN5, itemsMenuSlot, 16);
		this.addItemToMenu(papiMenuPage, InventorySlot.ROW3_COLUMN6, itemsMenuSlot, 32);
		this.addItemToMenu(papiMenuPage, InventorySlot.ROW3_COLUMN7, itemsMenuSlot, 64);
		
		Papi.Model.getPlayer(player).openMenu(papiMenuPage);
	}

	private void addItemToMenu(PapiMenuPage papiMenuPage, InventorySlot inventorySlot, ItemsMenuSlot itemsMenuSlot, int amount) {
		SelectedItemMenuSlot selectedItemMenuSlot = new SelectedItemMenuSlot(itemsMenuSlot, amount);
		
		
		List<String> loresList = new ArrayList<>();
		loresList.add("");
		if(selectedItemMenuSlot.getPriceSale() > 0)
		{
			loresList.add("&2&m&oKup &a&m&o"+selectedItemMenuSlot.getAmount()+"&2&m&o za &a&m&o"+selectedItemMenuSlot.getPrice()+"&2&m&o "+Function.Menu.getCurrency(selectedItemMenuSlot.getPrice()));
			loresList.add("&2Kup w promocji za: &a&l"+selectedItemMenuSlot.getPriceSale()+"&2 "+Function.Menu.getCurrency(selectedItemMenuSlot.getPriceVipSale()));
		}
		else
			loresList.add("&2Kup &a"+selectedItemMenuSlot.getAmount()+"&2 za &a"+selectedItemMenuSlot.getPrice()+"&2 "+Function.Menu.getCurrency(selectedItemMenuSlot.getPrice()));
		
		if(selectedItemMenuSlot.getPriceVipSale() > 0)
			loresList.add("&3&oCena dla VIP: &b&o"+selectedItemMenuSlot.getPriceVipSale()+"&3&o "+Function.Menu.getCurrency(selectedItemMenuSlot.getPriceVipSale()));
		
		ItemStack itemStack = Papi.ItemUtils.addLore(selectedItemMenuSlot.getItemStackWithAmount().clone(), loresList);

		
		papiMenuPage.setItem(inventorySlot, itemStack, selectedItemMenuSlot);
	}
	
	@Override
	public void onMenuClick(PapiMenuClickEvent e) {
		if(!e.existSlotStoreObject())
			return;
		
		if(e.getSlotStoreObject() instanceof String)
		{
			String key = (String)e.getSlotStoreObject();
			
			if(key.equals("BACK")) {
				backMenu.openMenu(null, e.getPlayer(), null);
				return;
			}
		}
		else if(e.getSlotStoreObject() instanceof SelectedItemMenuSlot)
		{
			if(e.getPlayer().getInventory().firstEmpty() < 0)
			{
				Message.sendMessage(e.getPlayer(), "&4Twój ekwipunek jest pełny!");
				return;
			}
			
			SelectedItemMenuSlot itemsMenuSlot = (SelectedItemMenuSlot)e.getSlotStoreObject();
			int priceItems = -1;
			if(itemsMenuSlot.getPriceVipSale() > 0 && e.getPlayer().hasPermission(Plugin.PERMISSION_PREFIX+".vip"))
				priceItems = itemsMenuSlot.getPriceVipSale()*itemsMenuSlot.getAmount();
			else if(itemsMenuSlot.getPriceSale() > 0)
				priceItems = itemsMenuSlot.getPriceSale()*itemsMenuSlot.getAmount();
			else
				priceItems = itemsMenuSlot.getPrice()*itemsMenuSlot.getAmount();
			
			if(priceItems > 0 && Mapi.getPlayer(e.getPlayer()).getPoints() >= priceItems) {
				e.getPlayer().getInventory().addItem(itemsMenuSlot.getItemStackWithAmount());
				e.getPlayer().updateInventory();
				Mapi.getPlayer(e.getPlayer()).delPoints(priceItems);
				Message.sendMessage(e.getPlayer(), "&2Zapłaciłeś &a"+priceItems+"&2 "+Function.Menu.getCurrency(priceItems)+" za zakupy w sklepie.");
				return;
			}
			else
			{
				Message.sendMessage(e.getPlayer(), "&4Masz tylko &c"+(int)Mapi.getPlayer(e.getPlayer()).getPoints()+" "+Function.Menu.getCurrency((int)Mapi.getPlayer(e.getPlayer()).getPoints()));
				return;
			}
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent event) {
	}
}
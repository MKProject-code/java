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
import me.maskat.ShopManager.PlayerMenu.SelectedItemMenu.SelectedItemMenuSlot;
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
		protected int getPriceVip() {
			return this.getItemsMenuSlot().getPriceVip()*this.amount;
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

	private PapiMenuPage papiMenuPage;
	private ItemsMenuSlot itemsMenuSlot;
	@Override
	public PapiMenuPage getPapiMenuPage() {
		return papiMenuPage;
	}
	
	public void initOpenMenu(Player player, PapiMenu backMenu, ItemsMenuSlot itemsMenuSlot) {
		this.itemsMenuSlot = itemsMenuSlot;
		this.initPapiMenu(player, 1, backMenu);
		this.openPapiMenuPage(player);
	}
	
	@Override
	public void initPapiMenu(Player player, int page, PapiMenu backMenu) {
		papiMenuPage = new PapiMenuPage(this, 6, "* * * * * * Sklep Skyidea * * * * * *");
		
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.BOOK, "&7Powrót do listy przedmiotów", backMenu);
		
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
		
		this.addItemToMenu(papiMenuPage, InventorySlot.ROW3_COLUMN3, 1);
		this.addItemToMenu(papiMenuPage, InventorySlot.ROW3_COLUMN4, 8);
		this.addItemToMenu(papiMenuPage, InventorySlot.ROW3_COLUMN5, 16);
		this.addItemToMenu(papiMenuPage, InventorySlot.ROW3_COLUMN6, 32);
		this.addItemToMenu(papiMenuPage, InventorySlot.ROW3_COLUMN7, 64);
	}

	private void addItemToMenu(PapiMenuPage papiMenuPage, InventorySlot inventorySlot, int amount) {
		SelectedItemMenuSlot selectedItemMenuSlot = new SelectedItemMenuSlot(this.itemsMenuSlot, amount);
		
		List<String> loresList = new ArrayList<>();
		loresList.add("");
		if(selectedItemMenuSlot.getPrice() <= 0)
		{
			if(selectedItemMenuSlot.getPriceVip() > 0)
			{
				loresList.add("&2Kup &a"+selectedItemMenuSlot.getAmount()+"&2 za &a"+selectedItemMenuSlot.getPriceVip()+"&2 "+Function.Menu.getCurrency(selectedItemMenuSlot.getPriceVip()));
				loresList.add("&3&oDostępne tylko dla VIP+");
			}
		}
		else if(selectedItemMenuSlot.getPriceSale() > 0)
		{
			loresList.add("&2&m&oKup &a&m&o"+selectedItemMenuSlot.getAmount()+"&2&m&o za &a&m&o"+selectedItemMenuSlot.getPrice()+"&2&m&o "+Function.Menu.getCurrency(selectedItemMenuSlot.getPrice()));
			loresList.add("&2Kup w promocji za: &a&l"+selectedItemMenuSlot.getPriceSale()+"&2 "+Function.Menu.getCurrency(selectedItemMenuSlot.getPriceVipSale()));
			if(selectedItemMenuSlot.getPriceVipSale() > 0)
				loresList.add("&3&oCena dla VIP: &b&o"+selectedItemMenuSlot.getPriceVipSale()+"&3&o "+Function.Menu.getCurrency(selectedItemMenuSlot.getPriceVipSale()));
		}
		else
		{
			loresList.add("&2Kup &a"+selectedItemMenuSlot.getAmount()+"&2 za &a"+selectedItemMenuSlot.getPrice()+"&2 "+Function.Menu.getCurrency(selectedItemMenuSlot.getPrice()));
			if(selectedItemMenuSlot.getPriceVip() > 0)
				loresList.add("&3&oCena dla VIP+: &b&o"+selectedItemMenuSlot.getPriceVip()+"&3&o "+Function.Menu.getCurrency(selectedItemMenuSlot.getPriceVip()));
		}
		
		ItemStack itemStack = Papi.ItemUtils.addLore(selectedItemMenuSlot.getItemStackWithAmount().clone(), loresList);

		papiMenuPage.setItem(inventorySlot, itemStack, selectedItemMenuSlot);
	}
	
	@Override
	public void onMenuClick(PapiMenuClickEvent e) {
		if(!e.existSlotStoreObject())
			return;
		
		if(e.getSlotStoreObject() instanceof PapiMenu)
		{
			((PapiMenu) e.getSlotStoreObject()).openPapiMenuPage(e.getPlayer());
			return;
		}
		else if(e.getSlotStoreObject() instanceof SelectedItemMenuSlot)
		{
			Manager.buyItem(e.getPlayer(), (SelectedItemMenuSlot)e.getSlotStoreObject());
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
	}
}

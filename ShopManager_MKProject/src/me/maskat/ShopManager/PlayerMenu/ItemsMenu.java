package me.maskat.ShopManager.PlayerMenu;

import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.maskat.ShopManager.Function;
import me.maskat.ShopManager.Plugin;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuPage;
import mkproject.maskat.Papi.Utils.Message;

public class ItemsMenu implements PapiMenu {
	class ItemsMenuSlot {
		private int price;
		private int priceSale;
		private int priceVipSale;
		private ItemStack itemStack;
		
		protected ItemsMenuSlot(ItemStack itemStack, int price, int priceSale, int priceVipSale) {
			this.price = price;
			this.priceSale = priceSale;
			this.priceVipSale = priceVipSale;
			this.itemStack = itemStack;
		}
		protected int getPrice() {
			return this.price;
		}
		protected int getPriceSale() {
			return this.priceSale;
		}
		protected int getPriceVipSale() {
			return this.priceVipSale;
		}
		protected ItemStack getItemStack() {
			return this.itemStack;
		}
	}
	
	private CategoriesMenu backMenu;
	private String category;
	
	public void openMenu(CategoriesMenu backMenu, Player player, String category) {
		if(backMenu != null)
			this.backMenu = backMenu;
		if(category != null)
			this.category = category;
		
		openMenu(player, 1);
	}
	
	private void openMenu(Player player, int page) {
		PapiMenuPage papiMenuPage = new PapiMenuPage(this, 6, "* * * * * * Sklep Skyidea * * * * * *");

		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.BOOK, "&7Powrót do listy kategorii", "BACK");

//		List<Enchant> enchantments = new ArrayList<Enchant>();
//		if(this.category.equals("Enchantment")) {
//	        for (Enchantment ench : Enchantment.values()) {
//	        	for(int i=1;i<=ench.getMaxLevel();i++)
//	        	{
//	        		int price=200-((ench.getMaxLevel()-i)*30);
//	        		enchantments.add(new Enchant(ench, i, price));
//	        		Bukkit.broadcastMessage(ench.toString()+":"+i+"-"+price);
//	        	}
//	        }
//		}
		
		ConfigurationSection itemsSection = Plugin.getPlugin().getConfig().getConfigurationSection("categories."+this.category+".items");
		if(itemsSection == null)
		{
			Message.sendMessage(player, "&cDziś nie ma żadnych promocji :(");
			return;
		}
		Set<String> itemsSet = itemsSection.getKeys(false);
		if(itemsSet.size() <= 0)
		{
			Message.sendMessage(player, "&cDziś nie ma żadnych promocji :(");
			return;
		}
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
		
		int itemsPageLimit = 28;
		
		int iStart=0;
		int iEnd=itemsPageLimit-1;
		if(itemsSet.size() > itemsPageLimit)
		{
			iStart=(page-1)*itemsPageLimit;
			iEnd=(page*itemsPageLimit)-1;
			if(page>1)
				papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.PAPER, "&7Poprzednia strona", "PREV_PAGE:"+(page-1));
			if(itemsSet.size()>iEnd)
				papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.PAPER, "&7Następna strona", "NEXT_PAGE:"+(page+1));
		}
		
	    int i=0;
	    int iV=1;
	    int iH=2;
		for(String key : itemsSet)
		{
			try
			{
				if(i>=iStart)
				{
					InventorySlot invSlot = InventorySlot.valueOf((iV*9)+iH-1);
					Material material = Material.valueOf(Plugin.getPlugin().getConfig().getString("categories."+this.category+".items."+key+".item"));
					
					int price = Plugin.getPlugin().getConfig().getInt("categories."+this.category+".items."+key+".buy");
					
					ItemStack itemStack = new ItemStack(material, 1);
					
					ConfigurationSection enchants = Plugin.getPlugin().getConfig().getConfigurationSection("categories."+this.category+".items."+key+".enchants");
					if(enchants != null)
					{
						for(String enchantName : enchants.getKeys(false))
						{
							Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(enchantName));
							int enchantLevel = Plugin.getPlugin().getConfig().getInt("categories."+this.category+".items."+key+".enchants."+enchantName);
							itemStack.addUnsafeEnchantment(enchant, enchantLevel);
						}
					}
					
					if(this.category != Plugin.CATEGORY_SALES_GENERATED)
					{
						ItemStack icon = Papi.ItemUtils.addLore(itemStack.clone(), List.of("","&2Kup za &a"+price+"&2 "+Function.Menu.getCurrency(price)));
						papiMenuPage.setItem(invSlot, icon, new ItemsMenuSlot(itemStack, price, -1, -1));
					}
					else
					{
						int priceSale = Plugin.getPlugin().getConfig().getInt("categories."+this.category+".items."+key+".sale");
						int priceSaleVip = Plugin.getPlugin().getConfig().getInt("categories."+this.category+".items."+key+".salevip");
						
						ItemStack icon = Papi.ItemUtils.addLore(itemStack.clone(), List.of(
								"",
								"&2&m&oKup za &a&m&o"+price+"&2&m&o "+Function.Menu.getCurrency(price),
								"&2Kup w promocji za: &a&l"+priceSale+"&2 "+Function.Menu.getCurrency(priceSale),
								"&3&oCena dla VIP: &b&o"+priceSaleVip+"&3&o "+Function.Menu.getCurrency(priceSaleVip)
								));
						papiMenuPage.setItem(invSlot, icon, new ItemsMenuSlot(itemStack, price, priceSale, priceSaleVip));
					}
					
					if(iH >= 8) {
						iH=2;
						iV++;
					}
					else
						iH++;
				}
				if(i>=iEnd)
					break;
				i++;
			}
			catch(Exception ex)
			{
				Plugin.getPlugin().getLogger().warning(">>>>>>>>>> Invalid configuration: categories."+this.category+".items."+key);
			}
		}
		
		Papi.Model.getPlayer(player).openMenu(papiMenuPage);
	}

	@Override
	public void onMenuClick(PapiMenuClickEvent event) {
		if(!event.existSlotStoreObject())
			return;
		
		if(event.getSlotStoreObject() instanceof String)
		{
			String key = (String)event.getSlotStoreObject();
			
			if(key.equals("BACK")) {
				this.backMenu.openMenu(event.getPlayer());
				return;
			}
			if(key.indexOf("PREV_PAGE") == 0) {
				this.openMenu(event.getPlayer(), Integer.parseInt(key.split(":")[1]));
				return;
			}
			if(key.indexOf("NEXT_PAGE") == 0) {
				this.openMenu(event.getPlayer(), Integer.parseInt(key.split(":")[1]));
				return;
			}
		}
		else if(event.getSlotStoreObject() instanceof ItemsMenuSlot)
		{
			ItemsMenuSlot itemsMenuSlot = (ItemsMenuSlot)event.getSlotStoreObject();
			new SelectedItemMenu().openMenu(this, event.getPlayer(), itemsMenuSlot);
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent event) {
	}
}
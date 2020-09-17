package me.maskat.ShopManager.PlayerMenu;

import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
		private String category;
		private int price;
		private int priceVip;
		private int priceSale;
		private int priceVipSale;
		private ItemStack itemStack;
		
		protected ItemsMenuSlot(String category, ItemStack itemStack, int price, int priceVip, int priceSale, int priceVipSale) {
			this.category = category;
			this.price = price;
			this.priceVip = priceVip;
			this.priceSale = priceSale;
			this.priceVipSale = priceVipSale;
			this.itemStack = itemStack;
		}
		protected String getCategory() {
			return this.category;
		}
		protected int getPrice() {
			return this.price;
		}
		protected int getPriceVip() {
			return this.priceVip;
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
	
//	private CategoriesMenu backMenu;
	private String category;
	private PapiMenuPage papiMenuPage;
	
	public PapiMenuPage getPapiMenuPage() {
		return this.papiMenuPage;
	}
	
	public void initOpenMenu(PapiMenu backMenu, Player player, String category) {
//		if(backMenu != null)
//			this.backMenu = backMenu;
		if(category != null)
			this.category = category;
		
		this.initPapiMenu(player, 1, backMenu);
		this.openPapiMenuPage(player);
	}
	
	@Override
	public void initPapiMenu(Player player, int page, PapiMenu backMenu) {
		this.papiMenuPage = new PapiMenuPage(this, 6, "* * * * * * Sklep Skyidea * * * * * *");

		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.BOOK, "&7Powrót do listy kategorii", backMenu);

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

		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.initPage(player, page);
	}
	
	private void initPage(Player player, int page) {
		ConfigurationSection itemsSection = Plugin.getPlugin().getConfig().getConfigurationSection("categories."+this.category+".items");
		if(itemsSection == null)
		{
			if(this.category.equals(Plugin.CATEGORY_SALES_GENERATED))
				Message.sendMessage(player, "&cDziś nie ma żadnych promocji :(");
			else
				Message.sendMessage(player, "&cBrak ofert w tej kategorii");
			return;
		}
		Set<String> itemsSet = itemsSection.getKeys(false);
		if(itemsSet.size() <= 0)
		{
			if(this.category.equals(Plugin.CATEGORY_SALES_GENERATED))
				Message.sendMessage(player, "&cDziś nie ma żadnych promocji :(");
			else
				Message.sendMessage(player, "&cBrak ofert w tej kategorii");
			return;
		}
		
		int itemsPageLimit = 28;
		
		int iStart=0;
		int iEnd=itemsPageLimit-1;
		
		if(itemsSet.size() > itemsPageLimit)
		{
			iStart=(page-1)*itemsPageLimit;
			iEnd=(page*itemsPageLimit)-1;
			if(page>1)
				this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.MAP, "&7Poprzednia strona", "PREV_PAGE:"+(page-1));
			else
				this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
			
			if((itemsSet.size()-1)>iEnd)
				this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.MAP, "&7Następna strona", "NEXT_PAGE:"+(page+1));
			else
				this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		}
		else
			page = 1;
		
		for(int i=InventorySlot.ROW2_COLUMN1.getValue(); i<=InventorySlot.ROW5_COLUMN9.getValue(); i++)
			this.papiMenuPage.removeItem(InventorySlot.valueOf(i));
		
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
							if(material == Material.ENCHANTED_BOOK) {
				        	    EnchantmentStorageMeta esm = (EnchantmentStorageMeta)itemStack.getItemMeta();
				        	    esm.addStoredEnchant(enchant, enchantLevel, true);
				        	    itemStack.setItemMeta(esm);
							}
							else
								itemStack.addUnsafeEnchantment(enchant, enchantLevel);
						}
					}
					
					ConfigurationSection effects = Plugin.getPlugin().getConfig().getConfigurationSection("categories."+this.category+".items."+key+".effects");
					if(effects != null)
					{
						for(String effectTypeName : effects.getKeys(false))
						{
							PotionEffectType effectType = PotionEffectType.getByName(effectTypeName);
							int effectDuration = Plugin.getPlugin().getConfig().getInt("categories."+this.category+".items."+key+".effects."+effectTypeName+".time")*20;
							int effectAmplifier = Plugin.getPlugin().getConfig().getInt("categories."+this.category+".items."+key+".effects."+effectTypeName+".level")-1;
							if(effectDuration > 0 && effectAmplifier > 0) {
								PotionMeta pm = (PotionMeta)itemStack.getItemMeta();
				        	    pm.addCustomEffect(new PotionEffect(effectType, effectDuration, effectAmplifier), true);
				        	    itemStack.setItemMeta(pm);
							}
						}
					}
					
					if(this.category.equals(Plugin.CATEGORY_SALES_GENERATED))
					{
						int priceSale = Plugin.getPlugin().getConfig().getInt("categories."+this.category+".items."+key+".sale");
						int priceSaleVip = Plugin.getPlugin().getConfig().getInt("categories."+this.category+".items."+key+".salevip");
						
						ItemStack icon = Papi.ItemUtils.addLore(itemStack.clone(), List.of(
								"",
								"&2&m&oKup za &a&m&o"+price+"&2&m&o "+Function.Menu.getCurrency(price),
								"&2Kup w promocji za: &a&l"+priceSale+"&2 "+Function.Menu.getCurrency(priceSale),
								"&3&oCena dla VIP: &b&o"+priceSaleVip+"&3&o "+Function.Menu.getCurrency(priceSaleVip)
								));
						this.papiMenuPage.setItem(invSlot, icon, new ItemsMenuSlot(this.category, itemStack, price, -1, priceSale, priceSaleVip));
					}
					else if(this.category.equals(Plugin.CATEGORY_ZONE_VIP_PLUS))
					{
						ItemStack icon = Papi.ItemUtils.addLore(itemStack.clone(), List.of(
								"",
								"&2Kup za &a"+price+"&2 "+Function.Menu.getCurrency(price),
								"&3&oDostępne tylko dla VIP+"
								));
						this.papiMenuPage.setItem(invSlot, icon, new ItemsMenuSlot(this.category, itemStack, -1, price, -1, -1));
					}
					else
					{
						int priceVip = (int) (price*0.75);
						ItemStack icon = Papi.ItemUtils.addLore(itemStack.clone(),
								(priceVip<price) ? List.of(
										"",
										"&2Kup za &a"+price+"&2 "+Function.Menu.getCurrency(price),
										"&3&oCena dla VIP+: &b&o"+priceVip+"&3&o "+Function.Menu.getCurrency(priceVip)
								) : List.of(
										"",
										"&2Kup za &a"+price+"&2 "+Function.Menu.getCurrency(price)
										)
								
								);
						this.papiMenuPage.setItem(invSlot, icon, new ItemsMenuSlot(this.category, itemStack, price, (priceVip<price) ? priceVip : -1, -1, -1));
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
		else if(e.getSlotStoreObject() instanceof String)
		{
			String key = (String)e.getSlotStoreObject();
			
			if(key.indexOf("PREV_PAGE") == 0) {
				this.initPage(e.getPlayer(), Integer.parseInt(key.split(":")[1]));
				return;
			}
			if(key.indexOf("NEXT_PAGE") == 0) {
				this.initPage(e.getPlayer(), Integer.parseInt(key.split(":")[1]));
				return;
			}
		}
		else if(e.getSlotStoreObject() instanceof ItemsMenuSlot)
		{
			if(this.category.equals(Plugin.CATEGORY_ZONE_VIP_PLUS))
			{
				if(e.getPlayer().hasPermission(Plugin.PERMISSION_PREFIX+".vipzone"))
				{
					ItemsMenuSlot itemsMenuSlot = (ItemsMenuSlot)e.getSlotStoreObject();
					new SelectedItemMenu().initOpenMenu(e.getPlayer(), this, itemsMenuSlot);
					return;
				}
				else
				{
					Message.sendMessage(e.getPlayer(), "&cMusisz mieć VIP+ aby móc kupować te przedmioty");
					return;
				}
			}
			else
			{
				ItemsMenuSlot itemsMenuSlot = (ItemsMenuSlot)e.getSlotStoreObject();
				new SelectedItemMenu().initOpenMenu(e.getPlayer(), this, itemsMenuSlot);
				return;
			}
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent event) {
	}

}

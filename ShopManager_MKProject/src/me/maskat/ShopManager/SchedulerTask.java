package me.maskat.ShopManager;

import org.bukkit.configuration.ConfigurationSection;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Scheduler.PapiScheduler;

public class SchedulerTask implements PapiScheduler {

	@Override
	public void runTaskThread() {
		Plugin.getPlugin().getConfig().set("categories."+Plugin.CATEGORY_SALES_GENERATED+".items", null);
		
		for(String category : Plugin.getPlugin().getConfig().getConfigurationSection("categories").getKeys(false))
		{
			//if(Papi.Function.randomInteger(0, 1) == 0)
			//{
				for(String item : Plugin.getPlugin().getConfig().getConfigurationSection("categories."+category+".items").getKeys(false))
				{
					if(Papi.Function.randomInteger(0, 8) == 0)
					{
						String itemName = Plugin.getPlugin().getConfig().getString("categories."+category+".items."+item+".item");
						int price = Plugin.getPlugin().getConfig().getInt("categories."+category+".items."+item+".buy");
						int priceSale = (int)(price*(Papi.Function.randomInteger(80, 95)*0.01D));
						int priceSaleVip = (int)(priceSale*(Papi.Function.randomInteger(60, 70)*0.01D));
						Plugin.getPlugin().getConfig().set("categories."+Plugin.CATEGORY_SALES_GENERATED+".items."+item+".item", itemName);
						Plugin.getPlugin().getConfig().set("categories."+Plugin.CATEGORY_SALES_GENERATED+".items."+item+".buy", price);
						Plugin.getPlugin().getConfig().set("categories."+Plugin.CATEGORY_SALES_GENERATED+".items."+item+".sale", priceSale);
						Plugin.getPlugin().getConfig().set("categories."+Plugin.CATEGORY_SALES_GENERATED+".items."+item+".salevip", priceSaleVip);
						
						ConfigurationSection enchants = Plugin.getPlugin().getConfig().getConfigurationSection("categories."+category+".items."+item+".enchants");
						if(enchants != null)
						{
							for(String enchantName : enchants.getKeys(false))
							{
								Plugin.getPlugin().getConfig().set("categories."+Plugin.CATEGORY_SALES_GENERATED+".items."+item+".enchants."+enchantName, Plugin.getPlugin().getConfig().getInt("categories."+category+".items."+item+".enchants."+enchantName));
							}
						}
					}
				}
			//}
		}
		
		Plugin.getPlugin().saveConfig();
	}
}
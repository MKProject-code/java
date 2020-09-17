package me.maskat.ShopManager;

import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import me.maskat.ShopManager.Cmds.CmdShop;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.CommandManager;

public class Plugin extends JavaPlugin {
	private static Plugin plugin;
	public static String PERMISSION_PREFIX = "mkp.shopmanager";
	public static String CATEGORY_SALES_GENERATED = "SalesGenerated";
	public static String CATEGORY_RESET_THE_END = "ResetTheEnd";
	public static Object CATEGORY_ZONE_VIP_PLUS = "ZoneVipPlus";
	
	@Override
	public void onEnable() {
		plugin = this;
		
		this.saveDefaultConfig();
		this.loadConfig();
		
		this.getCommand("shopmanager").setExecutor(new ExecuteCommand());
		
		CommandManager.initCommand(this, "shop", new CmdShop(), false);
		
		if(Papi.DEVELOPER_DIRECTORY_AUTODELETE) {
			try {
				FileUtils.deleteDirectory(getDataFolder());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Papi.Scheduler.registerTimerTask(new SchedulerTask(), null, 5, 0, 0);
		
		getLogger().info("Has been enabled!");
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}

	public static void reloadAllConfigs() {
		plugin.reloadConfig();
		plugin.loadConfig();
	}
	
	private void loadConfig() {
		if(this.getConfig().getBoolean("categories."+CATEGORY_ZONE_VIP_PLUS+".autoGenerate.enchanted_books.enable"))
			this.autoGenerateVipEnchants();
		if(this.getConfig().getBoolean("categories."+CATEGORY_ZONE_VIP_PLUS+".autoGenerate.potions.enable"))
			this.autoGenerateVipPotions();
		if(this.getConfig().getBoolean("categories."+CATEGORY_ZONE_VIP_PLUS+".autoGenerate.splash_potions.enable"))
			this.autoGenerateVipSplashPotions();
	}
	
	private void autoGenerateVipPotions() {
		for(PotionEffectType potionEffectType : PotionEffectType.values())
		{
			int time = 600;
			for(int level=2;level<=6;level++)
			{
				this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+Material.POTION.name()+potionEffectType.getName()+level+"_"+time+".item", Material.POTION.name());
				this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+Material.POTION.name()+potionEffectType.getName()+level+"_"+time+".buy", 40+(level*20));
				this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+Material.POTION.name()+potionEffectType.getName()+level+"_"+time+".effects."+potionEffectType.getName()+".time", time);
				this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+Material.POTION.name()+potionEffectType.getName()+level+"_"+time+".effects."+potionEffectType.getName()+".level", level);
			}
			time = 1200;
			for(int level=2;level<=6;level++)
			{
				this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+Material.POTION.name()+potionEffectType.getName()+level+"_"+time+".item", Material.POTION.name());
				this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+Material.POTION.name()+potionEffectType.getName()+level+"_"+time+".buy", 70+(level*20));
				this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+Material.POTION.name()+potionEffectType.getName()+level+"_"+time+".effects."+potionEffectType.getName()+".time", time);
				this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+Material.POTION.name()+potionEffectType.getName()+level+"_"+time+".effects."+potionEffectType.getName()+".level", level);
			}
		}
	}
	
	private void autoGenerateVipSplashPotions() {
		for(PotionEffectType potionEffectType : PotionEffectType.values())
		{
			int time = 600;
			for(int level=2;level<=6;level++)
			{
				this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+Material.SPLASH_POTION.name()+potionEffectType.getName()+level+"_"+time+".item", Material.SPLASH_POTION.name());
				this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+Material.SPLASH_POTION.name()+potionEffectType.getName()+level+"_"+time+".buy", 60+(level*20));
				this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+Material.SPLASH_POTION.name()+potionEffectType.getName()+level+"_"+time+".effects."+potionEffectType.getName()+".time", time);
				this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+Material.SPLASH_POTION.name()+potionEffectType.getName()+level+"_"+time+".effects."+potionEffectType.getName()+".level", level);
			}
			time = 1200;
			for(int level=2;level<=6;level++)
			{
				this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+Material.SPLASH_POTION.name()+potionEffectType.getName()+level+"_"+time+".item", Material.SPLASH_POTION.name());
				this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+Material.SPLASH_POTION.name()+potionEffectType.getName()+level+"_"+time+".buy", 90+(level*20));
				this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+Material.SPLASH_POTION.name()+potionEffectType.getName()+level+"_"+time+".effects."+potionEffectType.getName()+".time", time);
				this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+Material.SPLASH_POTION.name()+potionEffectType.getName()+level+"_"+time+".effects."+potionEffectType.getName()+".level", level);
			}
		}
	}
	
	private void autoGenerateVipEnchants() {
		for(Enchantment enchant : Enchantment.values())
		{
			for(int level=enchant.getMaxLevel()+1;level<=10;level++)
			{
				if(enchant.getKey().getNamespace().equals("minecraft")) {
					this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+enchant.getKey().getNamespace()+enchant.getKey().getKey()+level+".item", Material.ENCHANTED_BOOK.name());
					this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+enchant.getKey().getNamespace()+enchant.getKey().getKey()+level+".buy", 150+((level-enchant.getMaxLevel())*30));
					this.getConfig().set("categories."+CATEGORY_ZONE_VIP_PLUS+".items."+enchant.getKey().getNamespace()+enchant.getKey().getKey()+level+".enchants."+enchant.getKey().getKey(), level);
				}
			}
		}
	}
}

package mkproject.maskat.SpecialTools.Rewards;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;
import mkproject.maskat.SpecialTools.Lang;

public class Chests {
	public static void giveReward(Player player, Chest chest) {
		
		String itemInfo = null;
		if(chest == Chest.FIRST) {
			int random = Papi.Function.randomInteger(1, 8);
			if(random==1)
			{
				addItem(player, new ItemStack(Material.GOLDEN_APPLE, 2));
				itemInfo = "2x Złote jabłko";
			}
			else if(random==2)
			{
				addItem(player, new ItemStack(Material.LEATHER, 16));
				itemInfo = "16x Skóra";
			}
			else if(random==3)
			{
				addItem(player, new ItemStack(Material.ENDER_PEARL, 1));
				itemInfo = "1x Perła";
			}
			else if(random==4)
			{
				addItem(player, new ItemStack(Material.GOLD_INGOT, 10));
				itemInfo = "10x Złoto";
			}
			else if(random==5)
			{
				//TODO : give VIP for 3 day
				itemInfo = "VIP na 3 dni";
			}
			else if(random==6)
			{
				//TODO : give VIP for 1 day
				itemInfo = "VIP na 1 dzień";
			}
			else if(random==7)
			{
				addItem(player, new ItemStack(Material.TOTEM_OF_UNDYING, 1));
				itemInfo = "1x Totem nieśmiertelności";
			}
			else if(random==8)
			{
				//TODO: item for create guild
				ItemStack itemStack = new ItemStack(Material.GLOWSTONE_DUST, 1);
				ItemMeta itemMeta = itemStack.getItemMeta();
				itemMeta.setDisplayName(Message.getColorMessage("&6Bursztyn"));
				itemMeta.setLore(List.of("Przedmiot do tworzenia gildi"));
				itemStack.setItemMeta(itemMeta);
				itemStack = Papi.ItemUtils.addGlow(itemStack);
				
				addItem(player, itemStack);
				
				itemInfo = "1x Przedmiot do tworzenia gildi";
			}
		}
		else if(chest == Chest.SECOUND) {
			int random = Papi.Function.randomInteger(1, 8);
			if(random==1)
			{
				//TODO: Przedmiot na powiększenie gildi
				ItemStack itemStack = new ItemStack(Material.GUNPOWDER, 1);
				ItemMeta itemMeta = itemStack.getItemMeta();
				itemMeta.setDisplayName(Message.getColorMessage("&6Zgniły banan"));
				itemMeta.setLore(List.of("Przedmiot do powiększania gildi"));
				itemStack.setItemMeta(itemMeta);
				itemStack = Papi.ItemUtils.addGlow(itemStack);
				
				addItem(player, itemStack);
				
				itemInfo = "1x Przedmiot do powiększenia gildi";
			}
			else if(random==2)
			{
				addItem(player, new ItemStack(Material.IRON_INGOT, 16));
				itemInfo = "16x Żelazo";
				
			}
			else if(random==3)
			{
				// TODO : 2 refy ????
				itemInfo = "2x REF???";
				
			}
			else if(random==4)
			{
				//TODO : give VIP for 1 day
				itemInfo = "VIP na 1 dzień";
				
			}
			else if(random==5)
			{
				//TODO : give VIP for 3 day
				itemInfo = "VIP na 3 dni";
				
			}
			else if(random==6)
			{
				addItem(player, new ItemStack(Material.ENDER_PEARL, 1));
				itemInfo = "1x Perła";
				
			}
			else if(random==7)
			{
				addItem(player, new ItemStack(Material.LEATHER, 16));
				itemInfo = "16x Skóra";
				
			}
			else if(random==8)
			{
				addItem(player, new ItemStack(Material.COAL, 32));
				itemInfo = "32x Węgiel";
			}
		}
		
		if(itemInfo != null)
			Message.sendMessage(player, Lang.getGetChest(itemInfo));
	}
	
	private static void addItem(Player player, ItemStack itemStack) {
		PlayerInventory playerInv = player.getInventory();
		if(playerInv.firstEmpty() < 0)
			player.getWorld().dropItem(player.getLocation(), itemStack);
		else
			playerInv.addItem(itemStack);
	}
}

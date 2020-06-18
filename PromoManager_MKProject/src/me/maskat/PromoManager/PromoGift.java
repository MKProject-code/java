package me.maskat.PromoManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mkproject.maskat.Papi.Utils.Message;

public class PromoGift {
	public static void doGivePromoGift(Player player) {
		List<Material> itemsListTool = new ArrayList<Material>();
		itemsListTool.add(Material.DIAMOND_SWORD);
		itemsListTool.add(Material.DIAMOND_AXE);
		itemsListTool.add(Material.DIAMOND_PICKAXE);
		itemsListTool.add(Material.DIAMOND_SHOVEL);
		
		List<Material> itemsListArmor = new ArrayList<Material>();
		itemsListArmor.add(Material.DIAMOND_LEGGINGS);
		itemsListArmor.add(Material.DIAMOND_BOOTS);
		itemsListArmor.add(Material.DIAMOND_CHESTPLATE);
		itemsListArmor.add(Material.DIAMOND_HELMET);
		
		//Tool
		int randomNum = ThreadLocalRandom.current().nextInt(0, itemsListTool.size());
		ItemStack itemStack = generateItem(itemsListTool.get(randomNum), false);
		player.getInventory().addItem(itemStack);
		
		
		//Armor
		randomNum = ThreadLocalRandom.current().nextInt(0, itemsListArmor.size());
		itemStack = generateItem(itemsListArmor.get(randomNum), false);
		player.getInventory().addItem(itemStack);
		
		
		//Book
	    itemStack = generateItem(Material.ENCHANTED_BOOK, true);
	    ItemMeta itemMetaBook = itemStack.getItemMeta();
	    itemMetaBook.setDisplayName(Message.getColorMessage("&a&oKsięga Bogów &b&oSkyidea"));
	    itemStack.setItemMeta(itemMetaBook);
		player.getInventory().addItem(itemStack);
		
		//Golden Apple
		player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 5));
		
		player.updateInventory();
	}
	
	private static ItemStack generateItem(Material material, boolean allowAllEnchant) {
		ItemStack itemStack = new ItemStack(material, 1);
		
		itemStack = randomEnchantment(itemStack, allowAllEnchant);
		itemStack = randomEnchantment(itemStack, allowAllEnchant);
		itemStack = randomEnchantment(itemStack, allowAllEnchant);
		itemStack = randomEnchantment(itemStack, allowAllEnchant);
		itemStack = randomEnchantment(itemStack, allowAllEnchant);
		
		return itemStack;
	}
	
    private static ItemStack randomEnchantment(ItemStack item, boolean allowAllEnchant) {
        // Store all possible enchantments for the item
        List<Enchantment> possible = new ArrayList<Enchantment>();
     
        // Loop through all enchantemnts
        for (Enchantment ench : Enchantment.values()) {
            // Check if the enchantment can be applied to the item, save it if it can
            if (allowAllEnchant || ench.canEnchantItem(item)) {
                possible.add(ench);
            }
        }
     
        // If we have at least one possible enchantment
        if (possible.size() >= 1) {
            // Randomize the enchantments
            Collections.shuffle(possible);
            // Get the first enchantment in the shuffled list
            Enchantment chosen = possible.get(0);
            // Apply the enchantment with a random level between 1 and the max level
            if(!allowAllEnchant)
            	item.addEnchantment(chosen, 1 + (int) (Math.random() * ((chosen.getMaxLevel() - 1) + 1)));
            else
            	item.addUnsafeEnchantment(chosen, 1 + (int) (Math.random() * ((chosen.getMaxLevel() - 1) + 1)));
        }
     
        // Return the item even if it doesn't have any enchantments
        return item;
    }
}

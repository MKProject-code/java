package mkproject.maskat.Papi;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mkproject.maskat.Papi.Utils.ItemGlow;
import mkproject.maskat.Papi.Utils.Message;

public class PapiItemUtils {
	protected static ItemStack addLore(ItemStack itemStack, List<String> colorLoresList) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		List<String> itemLores = itemMeta.getLore();
		if(itemLores == null)
			itemLores = new ArrayList<>();
		
		for(String lore : colorLoresList)
			itemLores.add(Message.getColorMessage(lore));
		itemMeta.setLore(itemLores);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	protected static ItemStack addGlow(ItemStack itemStack) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		NamespacedKey key = new NamespacedKey(PapiPlugin.getPlugin(), PapiPlugin.getPlugin().getDescription().getName());
		ItemGlow glow = new ItemGlow(key);
		itemMeta.addEnchant(glow, 1, true);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	protected static ItemStack addEnchant(ItemStack itemStack, Enchantment enchant, int level) {
		itemStack.addUnsafeEnchantment(enchant, level);
//		ItemMeta itemMeta = itemStack.getItemMeta();
//		itemMeta.addEnchant(enchant, level, true);
//		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
}

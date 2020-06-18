package mkproject.maskat.Papi;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
}

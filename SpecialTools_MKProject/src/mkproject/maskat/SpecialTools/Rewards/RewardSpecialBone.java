package mkproject.maskat.SpecialTools.Rewards;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class RewardSpecialBone {

	public static void dropReward(Block block) {
		ItemStack itemStack = new ItemStack(Material.BONE, 1);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(Message.getColorMessage("&fStara kość"));
		itemMeta.setLore(List.of("&7Daj tę kość swojemu wilkowi, aby zwiększyć jego poziom"));
		itemStack.setItemMeta(itemMeta);
		itemStack = Papi.ItemUtils.addGlow(itemStack);
		
		block.getWorld().dropItemNaturally(block.getLocation(), itemStack);
	}

}

package mkproject.maskat.Papi.Menu;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class MenuUtils {
//	private static Map<Player, MenuManager> menuList = new HashMap<>();
//	
//	protected static MenuManager createMenuInventory(final Player player) {
//		return menuList.put(player, new MenuManager());
//	}
//	protected static MenuManager getMenuInventory(final Player player) {
//		return menuList.get(player);
//	}
//	
//	
//	
	protected static ItemStack createGuiItem(final Material material) {
		return Extend.createGuiItem(material, null);
	}
	
	protected static ItemStack createGuiItem(final Material material, final String colorMessage) {
		return createGuiItem(material, false, colorMessage);
    }
	protected static ItemStack createGuiItem(final Material material, final boolean withGlow, final String colorMessage) {
		if(material == Material.AIR)
			return new ItemStack(Material.AIR);
		
		ItemStack itemStack;
		
		String[] msgArray = Message.getColorMessage(colorMessage).split("\n");
		if(msgArray.length == 1)
			itemStack = Extend.createGuiItem(material, msgArray[0]);
		else
			itemStack = Extend.createGuiItem(material, msgArray[0], Arrays.copyOfRange(msgArray, 1, msgArray.length));
		
		if(withGlow)
			return Papi.ItemUtils.addGlow(itemStack);
		else
			return itemStack;
	}
	
	protected static ItemStack createGuiItemHead(final Player player) {
		return Extend.createGuiItemHead(player, null);
	}
	protected static ItemStack createGuiItemHead(final Player player, final String colorMessage) {
    	String[] msgArray = Message.getColorMessage(colorMessage).split("\n");
    	if(msgArray.length == 1)
    		return Extend.createGuiItemHead(player, msgArray[0]);
    	else
    		return Extend.createGuiItemHead(player, msgArray[0], Arrays.copyOfRange(msgArray, 1, msgArray.length));
    }
	protected static ItemStack createGuiItemHead(final String headBase64, final String colorMessage) {
		return createGuiItemHead(headBase64, false, colorMessage);
	}
	protected static ItemStack createGuiItemHead(final String headBase64, final boolean withGlow, final String colorMessage) {
		ItemStack itemStack;
		
		String[] msgArray = Message.getColorMessage(colorMessage).split("\n");
		if(msgArray.length == 1)
			itemStack = Extend.createGuiItemHead(headBase64, msgArray[0]);
		else
			itemStack = Extend.createGuiItemHead(headBase64, msgArray[0], Arrays.copyOfRange(msgArray, 1, msgArray.length));
		
		if(withGlow)
			return Papi.ItemUtils.addGlow(itemStack);
		else
			return itemStack;
	}
	
    /////////
	
	private static class Extend {
		private static ItemStack createGuiItem(final Material material, final String displayName) {
			final ItemStack item = new ItemStack(material, 1);
			final ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(displayName);
			meta.setLore(null);
			item.setItemMeta(meta);
			return item;
		}
		
	    private static ItemStack createGuiItem(final Material material, final String displayName, final String[] lore) {
	    	final ItemStack item = new ItemStack(material, 1);
	    	final ItemMeta meta = item.getItemMeta();
	    	meta.setDisplayName(displayName);
	    	meta.setLore(Arrays.asList(lore));
	    	item.setItemMeta(meta);
	    	return item;
	    }
	    
	    private static ItemStack createGuiItemHead(final Player player, final String displayName) {
	    	final ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
	    	final SkullMeta meta = (SkullMeta)skull.getItemMeta();
	    	meta.setOwningPlayer(player);
	    	meta.setDisplayName(displayName);
	    	meta.setLore(null);
	    	skull.setItemMeta(meta);
	    	return skull;
	    }
	    private static ItemStack createGuiItemHead(final String headbase64, final String displayName) {
	    	final ItemStack skull = Papi.Function.getCustomSkull(headbase64);
	    	final SkullMeta meta = (SkullMeta)skull.getItemMeta();
	    	meta.setDisplayName(displayName);
	    	meta.setLore(null);
	    	skull.setItemMeta(meta);
	    	return skull;
	    }
	    private static ItemStack createGuiItemHead(final Player player, final String displayName, final String[] lore) {
	    	final ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
	    	final SkullMeta meta = (SkullMeta)skull.getItemMeta();
	    	meta.setOwningPlayer(player);
	    	meta.setDisplayName(displayName);
	    	meta.setLore(Arrays.asList(lore));
	    	skull.setItemMeta(meta);
	    	return skull;
	    }
	    private static ItemStack createGuiItemHead(final String headbase64, final String displayName, final String[] lore) {
	    	final ItemStack skull = Papi.Function.getCustomSkull(headbase64);
	    	final SkullMeta meta = (SkullMeta)skull.getItemMeta();
	    	meta.setDisplayName(displayName);
	    	meta.setLore(Arrays.asList(lore));
	    	skull.setItemMeta(meta);
	    	return skull;
	    }
	}
//	
//	///////////
//	
////	private static ItemStack getHead(String value) {
////		final ItemStack skull = getSkull();
////		final UUID hashAsId = new UUID(value.hashCode(), value.hashCode());
////        return Bukkit.getUnsafe().modifyItemStack(skull,
////                "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + value + "\"}]}}}"
////        );
////    }
}

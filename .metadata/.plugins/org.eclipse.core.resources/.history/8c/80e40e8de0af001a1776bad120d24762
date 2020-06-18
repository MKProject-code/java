package mkproject.maskat.Papi.Menu;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

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
    	String[] msgArray = Message.getColorMessage(colorMessage).split("\n");
    	if(msgArray.length == 1)
    		return Extend.createGuiItem(material, msgArray[0]);
    	else
    		return Extend.createGuiItem(material, msgArray[0], Arrays.copyOfRange(msgArray, 1, msgArray.length));
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
	    private static ItemStack createGuiItemHead(final Player player, final String displayName, final String[] lore) {
	    	final ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
	    	final SkullMeta meta = (SkullMeta)skull.getItemMeta();
	    	meta.setOwningPlayer(player);
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

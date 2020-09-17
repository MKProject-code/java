package me.maskat.compasspoint.inventories;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class InventoryMenu_old {
    private MenuMain_old menuMain = new MenuMain_old();

    public void initAllGui(Player player) {
    	menuMain.createGui(player);
    }
    
	public void openMainMenu(Player player) { menuMain.openInventory(player); }
	
	//public void openSubmenu(Player player) { menuSubmenu.openInventory(player); }
	
	public void onInventoryClick(final InventoryClickEvent e, final Player player)
	{
		if (e.getInventory().getHolder() == menuMain) menuMain.onInventoryClick(e, player);
	}
	
	//////////////////////////////////////////
	
	public static ItemStack createGuiItem(final Material material)
	{
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(null);

        // Set the lore of the item
        meta.setLore(null);

        item.setItemMeta(meta);

        return item;
	}
	
//    private static ItemStack createGuiItemTitleList(final Material material, final String name, final String... lore)
//    {
//        return createGuiItemTitleArray(material, name, lore);
//    }
    private static ItemStack createGuiItemTitleArray(final Material material, final String name, final String[] lore)
    {
    	final ItemStack item = new ItemStack(material, 1);
    	final ItemMeta meta = item.getItemMeta();
    	// Set the name of the item
    	meta.setDisplayName(name);
    	
    	// Set the lore of the item
    	meta.setLore(Arrays.asList(lore));
    	
    	item.setItemMeta(meta);
    	
    	return item;
    }
    
    public static ItemStack createGuiItemParseMsg(final Material material, final String msg)
    {
    	String[] msgArray = msg.split("\n");
    	return createGuiItemTitleArray(material, msgArray[0], Arrays.copyOfRange(msgArray, 1, msgArray.length));
    }
    
    public static ItemStack createGuiItemHeadParseMsg(final Player player, final String msg)
    {
    	String[] msgArray = msg.split("\n");
    	return createGuiItemHead(player, msgArray[0], Arrays.copyOfRange(msgArray, 1, msgArray.length));
    }
    
    private static ItemStack createGuiItemHead(final Player player, final String name, final String[] lore)
    {
    	ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta)skull.getItemMeta();
        meta.setOwningPlayer(player);
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        skull.setItemMeta(meta);
        return skull;
    }
}

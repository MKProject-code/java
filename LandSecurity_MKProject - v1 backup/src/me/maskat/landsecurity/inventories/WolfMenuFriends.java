package me.maskat.landsecurity.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.maskat.landsecurity.Message;
import me.maskat.landsecurity.models.Model;

public class WolfMenuFriends implements InventoryHolder
{
    private Inventory inv;
    private static class InvSlot {
    	private static int back() { return 49; }
    	private static int showUsers() { return 30; }
    	private static int addUsers() { return 32; }
    }

	@Override
    public Inventory getInventory() { return inv; }
    
    public void createGui(Player player)
    {
        inv = Bukkit.createInventory(this, 54, Message.getColorMessage(Model.Player(player).getWolf().getName()));
        initializeItems();
    }

    private void initializeItems()
    {
        inv.setItem(0,WolfMenu.createGuiItem(Material.BONE));
        inv.setItem(8,WolfMenu.createGuiItem(Material.BONE));
        inv.setItem(9,WolfMenu.createGuiItem(Material.BONE));
        inv.setItem(17,WolfMenu.createGuiItem(Material.BONE));
        inv.setItem(18,WolfMenu.createGuiItem(Material.BONE));
        inv.setItem(26,WolfMenu.createGuiItem(Material.BONE));
        inv.setItem(27,WolfMenu.createGuiItem(Material.BONE));
        inv.setItem(35,WolfMenu.createGuiItem(Material.BONE));
        inv.setItem(36,WolfMenu.createGuiItem(Material.BONE));
        inv.setItem(44,WolfMenu.createGuiItem(Material.BONE));
        inv.setItem(45,WolfMenu.createGuiItem(Material.BONE));
        inv.setItem(53,WolfMenu.createGuiItem(Material.BONE));
        
        inv.setItem(InvSlot.back(),WolfMenu.createGuiItemParseLang(Material.ENDER_PEARL, Message.getColorMessageLang("inventory.wolfmenu.wolf.managefriends.submenu.back.slot_title")));
        inv.setItem(InvSlot.showUsers(),WolfMenu.createGuiItemParseLang(Material.PLAYER_HEAD, Message.getColorMessageLang("inventory.wolfmenu.wolf.managefriends.submenu.showusers.slot_title")));
        inv.setItem(InvSlot.addUsers(),WolfMenu.createGuiItemParseLang(Material.NETHER_STAR, Message.getColorMessageLang("inventory.wolfmenu.wolf.managefriends.submenu.addusers.slot_title")));
    }

    public void openInventory(final HumanEntity ent) { ent.openInventory(inv); }
    
	public void onInventoryClick(final InventoryClickEvent e, final Player player)
	{
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        int clickedSlot = e.getRawSlot();
        if(clickedSlot == InvSlot.back()) {
        	Model.Player(player).getWolfMenu().openMainInventory();
        	return;
        }
        if(clickedSlot == InvSlot.showUsers()) {
        	Model.Player(player).getWolfMenu().openFriendsShowUsersInventory();
        	return;
        }
        if(clickedSlot == InvSlot.addUsers()) {
        	Model.Player(player).getWolfMenu().openFriendsAddUsersInventory();
        	return;
        }
	}
}

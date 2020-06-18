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

public class WolfMenuRegion implements InventoryHolder
{
    private Inventory inv;
    public int invSlotChoose1 = 10;
    public int invSlotChoose2 = 12;
    public int invSlotChoose3 = 14;
    public int invSlotChoose4 = 15;
    public int invSlotChoose5 = 16;

    private String listenChatFor = "";
    
    public void createGui(Player player)
    {
        // Create a new inventory, with "this" owner for comparison with other inventories, a size of nine, called example
        inv = Bukkit.createInventory(this, 27, Message.getColorMessage(Model.Player(player).getWolf().getName()));

        // Put the items into the inventory
        initializeItems();
    }

    //@Override
    public Inventory getInventory()
    {
        return inv;
    }

    // You can call this whenever you want to put the items in
    private void initializeItems()
    {
        //inv.setItem(invSlotChoose1,WolfMenu.createGuiItem(Material.NAME_TAG, Message.getColorMessageLang("inventory.wolfmenu.changename"), "§7Nazwa ta wyœwietla siê", "§7zawsze nad jego cia³em"));
        inv.setItem(invSlotChoose1,WolfMenu.createGuiItemParseLang(Material.NAME_TAG, Message.getColorMessageLang("inventory.wolfmenu.changename.slot_title")));
        //inv.setItem(invSlotChoose2,WolfMenu.applySkullTexture());
        inv.setItem(invSlotChoose3,WolfMenu.createGuiItemParseLang(Material.DIAMOND, Message.getColorMessageLang("inventory.wolfmenu.managerfamily.slot_title")));
        inv.setItem(invSlotChoose4,WolfMenu.createGuiItemParseLang(Material.EMERALD, Message.getColorMessageLang("inventory.wolfmenu.managerfriends.slot_title")));
        inv.setItem(invSlotChoose5,WolfMenu.createGuiItemParseLang(Material.REDSTONE, Message.getColorMessageLang("inventory.wolfmenu.managerenemies.slot_title")));
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent)
    {
        ent.openInventory(inv);
    }
    
	public void onInventoryClick(final InventoryClickEvent e, final Player player)
	{
        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        // Using slots click is a best option for your inventory click's
        int clickedSlot = e.getRawSlot();
        if(clickedSlot == invSlotChoose1) {
        	player.closeInventory();
        	Message.sendMessage(player, "inventory.wolfmenu.changename.input");
        	Model.Player(player).setListenChat(this);
        	listenChatFor = "ChangeWolfName";
        }
	}
	
//	public boolean onAsyncPlayerChat(AsyncPlayerChatEvent e)
//	{
//		Player player = e.getPlayer();
//		
//		if(listenChatFor == "ChangeWolfName") {
//			String message = e.getMessage();
//			if(message.equalsIgnoreCase("cancel"))
//			{
//				Message.sendMessage(player, "inventory.wolfmenu.changename.done.canceled");
//				listenChatFor = "";
//				return true;
//			}
//			if(message.length() < 3 || message.length() > 16)
//			{
//				Message.sendMessage(player, "inventory.wolfmenu.changename.error.length");
//				return false;
//			}
//			if(!message.matches("^[a-zA-Z0-9 ]*$"))
//			{
//				Message.sendMessage(player, "inventory.wolfmenu.changename.error.alfanumeric");
//				return false;
//			}
//			
//			Model.Player(player).getWolf().changeName("&e"+message);
//			Message.sendMessage(player, "inventory.wolfmenu.changename.done.changed");
//		}
//		
//		listenChatFor = "";
//		return true;
//	}
}

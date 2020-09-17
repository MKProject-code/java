package me.maskat.wolfsecurity.inventories;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableMap;

import me.maskat.wolfsecurity.Message;
import me.maskat.wolfsecurity.Plugin;
import me.maskat.wolfsecurity.models.Model;

public class WolfMenuFriendsShowUsers implements InventoryHolder
{
    private Inventory inv;
    private static class InvSlot {
    	private static Map<Integer, Integer> users = new HashMap<>();
    	private static int back() { return 49; }
    	private static int getUserId(int invslot) { return users.get(invslot); }
    	private static boolean existUser(int invslot) { return users.containsKey(invslot); }
    	private static void putUserId(int invslot, int userid) { users.put(invslot, userid); }
    	private static void removeUser(int invslot) { users.remove(invslot); }
    }
    
	@Override
    public Inventory getInventory() { return inv; }
    
    public void createGui(Player player)
    {
        inv = Bukkit.createInventory(this, 54, Message.getColorMessage(Model.Player(player).getWolf().getName()));
        initializeItems(player);
    }

    private void initializeItems(Player player)
    {
    	int i=0;
    	for (int userid: Model.Region(Model.Player(player).getOwnRegionId()).getFriendsUsersIdList()) {
			InvSlot.putUserId(i, userid);
			inv.setItem(i,WolfMenu.createGuiItemHeadParseLang(userid, Message.getColorMessageLang("inventory.wolfmenu.wolf.managefriends.submenu.showusers.submenu.head.slot_title", ImmutableMap.of(
					"player_name", Model.User(userid).getName()
					))));
    		i++;
    	}
    	inv.setItem(InvSlot.back(),WolfMenu.createGuiItemParseLang(Material.ENDER_PEARL, Message.getColorMessageLang("inventory.wolfmenu.wolf.managefriends.submenu.showusers.submenu.back.slot_title")));
    	updateHeadItemsAsync();
    }
    
	private void updateHeadItemsAsync() {
		Bukkit.getScheduler().runTaskAsynchronously(Plugin.plugin, new Runnable() {
			@Override
			public void run() {
				for (Map.Entry<Integer, Integer> entry : InvSlot.users.entrySet()) {
	    			inv.setItem(entry.getKey(),WolfMenu.createGuiItemHeadParseLang(entry.getValue(), Message.getColorMessageLang("inventory.wolfmenu.wolf.managefriends.submenu.showusers.submenu.head.slot_title", ImmutableMap.of(
	    					"player_name", Model.User(entry.getValue()).getName()
	    					))));
				}
			}
		});
	}

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
        if(clickedSlot == InvSlot.back()) {
        	Model.Player(player).getWolfMenu().openFriendsInventory();
        	return;
        }
        if(InvSlot.existUser(clickedSlot))
        {
        	int clickedUserid = InvSlot.getUserId(clickedSlot);
        	Model.Player(player).getOwnRegion().removeFriend(clickedUserid);
        	InvSlot.removeUser(clickedSlot);
        	Model.Player(player).getWolfMenu().initializeNewUsersInventoryWithout(this);
        	inv.setItem(clickedSlot, WolfMenu.createGuiItemHeadParseLang(clickedUserid, Message.getColorMessageLang("inventory.wolfmenu.wolf.managefriends.submenu.showusers.submenu.head.done.slot_title", ImmutableMap.of(
					"player_name", Model.User(clickedUserid).getName()
					))));
        	return;
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

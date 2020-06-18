package me.maskat.landsecurity.inventories;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.maskat.landsecurity.Message;
import me.maskat.landsecurity.models.Model;

public class WolfMenuCollarColor implements InventoryHolder
{
    private Inventory inv;
    private static class InvSlot {
    	private static int back() { return 49; }
    	private static int WHITE() { return 12-9; }
    	private static int ORANGE() { return 13-9; }
    	private static int MAGENTA() { return 14-9; }
    	private static int LIGHT_BLUE() { return 20-9; }
    	private static int YELLOW() { return 21-9; }
    	private static int LIME() { return 22-9; }
    	private static int PINK() { return 23-9; }
    	private static int GRAY() { return 24-9; }
    	private static int LIGHT_GRAY() { return 29-9; }
    	private static int CYAN() { return 30-9; }
    	private static int PURPLE() { return 31-9; }
    	private static int BLUE() { return 32-9; }
    	private static int BROWN() { return 33-9; }
    	private static int GREEN() { return 39-9; }
    	private static int RED() { return 40-9; }
    	private static int BLACK() { return 41-9; }
    }

	@Override
    public Inventory getInventory() { return inv; }
    
    public void createGui(Player player)
    {
        inv = Bukkit.createInventory(this, 54, Message.getColorMessage(Model.Player(player).getWolf().getName()));
        initializeItems(-1);
    }

    private void initializeItems(int slotClicked)
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
        
        inv.setItem(InvSlot.back(),WolfMenu.createGuiItemParseLang(Material.ENDER_PEARL, Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.back.slot_title")));
        inv.setItem(InvSlot.WHITE(),WolfMenu.createGuiItemParseLang(Material.WHITE_DYE, (slotClicked != InvSlot.WHITE()) ?
        		Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.white.slot_title") : Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.white.done.changed")));
        inv.setItem(InvSlot.ORANGE(),WolfMenu.createGuiItemParseLang(Material.ORANGE_DYE, (slotClicked != InvSlot.ORANGE()) ?
        		Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.orange.slot_title") : Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.orange.done.changed")));
        inv.setItem(InvSlot.MAGENTA(),WolfMenu.createGuiItemParseLang(Material.MAGENTA_DYE, (slotClicked != InvSlot.MAGENTA()) ?
        		Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.magenta.slot_title") : Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.magenta.done.changed")));
        inv.setItem(InvSlot.LIGHT_BLUE(),WolfMenu.createGuiItemParseLang(Material.LIGHT_BLUE_DYE, (slotClicked != InvSlot.LIGHT_BLUE()) ?
        		Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.light_blue.slot_title") : Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.light_blue.done.changed")));
        inv.setItem(InvSlot.YELLOW(),WolfMenu.createGuiItemParseLang(Material.YELLOW_DYE, (slotClicked != InvSlot.YELLOW()) ?
        		Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.yellow.slot_title") : Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.yellow.done.changed")));
        inv.setItem(InvSlot.LIME(),WolfMenu.createGuiItemParseLang(Material.LIME_DYE, (slotClicked != InvSlot.LIME()) ?
        		Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.lime.slot_title") : Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.lime.done.changed")));
        inv.setItem(InvSlot.PINK(),WolfMenu.createGuiItemParseLang(Material.PINK_DYE, (slotClicked != InvSlot.PINK()) ?
        		Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.pink.slot_title") : Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.pink.done.changed")));
        inv.setItem(InvSlot.GRAY(),WolfMenu.createGuiItemParseLang(Material.GRAY_DYE, (slotClicked != InvSlot.GRAY()) ?
        		Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.gray.slot_title") : Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.gray.done.changed")));
        inv.setItem(InvSlot.LIGHT_GRAY(),WolfMenu.createGuiItemParseLang(Material.LIGHT_GRAY_DYE, (slotClicked != InvSlot.LIGHT_GRAY()) ?
        		Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.light_gray.slot_title") : Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.light_gray.done.changed")));
        inv.setItem(InvSlot.CYAN(),WolfMenu.createGuiItemParseLang(Material.CYAN_DYE, (slotClicked != InvSlot.CYAN()) ?
        		Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.cyan.slot_title") : Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.cyan.done.changed")));
        inv.setItem(InvSlot.PURPLE(),WolfMenu.createGuiItemParseLang(Material.PURPLE_DYE, (slotClicked != InvSlot.PURPLE()) ?
        		Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.purple.slot_title") : Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.purple.done.changed")));
        inv.setItem(InvSlot.BLUE(),WolfMenu.createGuiItemParseLang(Material.BLUE_DYE, (slotClicked != InvSlot.BLUE()) ?
        		Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.blue.slot_title") : Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.blue.done.changed")));
        inv.setItem(InvSlot.BROWN(),WolfMenu.createGuiItemParseLang(Material.BROWN_DYE, (slotClicked != InvSlot.BROWN()) ?
        		Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.brown.slot_title") : Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.brown.done.changed")));
        inv.setItem(InvSlot.GREEN(),WolfMenu.createGuiItemParseLang(Material.GREEN_DYE, (slotClicked != InvSlot.GREEN()) ?
        		Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.green.slot_title") : Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.green.done.changed")));
        inv.setItem(InvSlot.RED(),WolfMenu.createGuiItemParseLang(Material.RED_DYE, (slotClicked != InvSlot.RED()) ?
        		Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.red.slot_title") : Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.red.done.changed")));
        inv.setItem(InvSlot.BLACK(),WolfMenu.createGuiItemParseLang(Material.BLACK_DYE, (slotClicked != InvSlot.BLACK()) ?
        		Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.black.slot_title") : Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.submenu.black.done.changed")));
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
        	Model.Player(player).getWolfMenu().openMainInventory();
        	return;
        }
        if(clickedSlot == InvSlot.WHITE()) {
        	if(Model.Player(player).getWolf().changeCollarColor(DyeColor.WHITE))
        		initializeItems(clickedSlot);
        	return;
        }
        if(clickedSlot == InvSlot.ORANGE()) {
        	if(Model.Player(player).getWolf().changeCollarColor(DyeColor.ORANGE))
        		initializeItems(clickedSlot);
        	return;
        }
        if(clickedSlot == InvSlot.MAGENTA()) {
        	if(Model.Player(player).getWolf().changeCollarColor(DyeColor.MAGENTA))
        		initializeItems(clickedSlot);
        	return;
        }
        if(clickedSlot == InvSlot.LIGHT_BLUE()) {
        	if(Model.Player(player).getWolf().changeCollarColor(DyeColor.LIGHT_BLUE))
        		initializeItems(clickedSlot);
        	return;
        }
        if(clickedSlot == InvSlot.YELLOW()) {
        	if(Model.Player(player).getWolf().changeCollarColor(DyeColor.YELLOW))
        		initializeItems(clickedSlot);
        	return;
        }
        if(clickedSlot == InvSlot.LIME()) {
        	if(Model.Player(player).getWolf().changeCollarColor(DyeColor.LIME))
        		initializeItems(clickedSlot);
        	return;
        }
        if(clickedSlot == InvSlot.PINK()) {
        	if(Model.Player(player).getWolf().changeCollarColor(DyeColor.PINK))
        		initializeItems(clickedSlot);
        	return;
        }
        if(clickedSlot == InvSlot.GRAY()) {
        	if(Model.Player(player).getWolf().changeCollarColor(DyeColor.GRAY))
        		initializeItems(clickedSlot);
        	return;
        }
        if(clickedSlot == InvSlot.LIGHT_GRAY()) {
        	if(Model.Player(player).getWolf().changeCollarColor(DyeColor.LIGHT_GRAY))
        		initializeItems(clickedSlot);
        	return;
        }
        if(clickedSlot == InvSlot.CYAN()) {
        	if(Model.Player(player).getWolf().changeCollarColor(DyeColor.CYAN))
        		initializeItems(clickedSlot);
        	return;
        }
        if(clickedSlot == InvSlot.PURPLE()) {
        	if(Model.Player(player).getWolf().changeCollarColor(DyeColor.PURPLE))
        		initializeItems(clickedSlot);
        	return;
        }
        if(clickedSlot == InvSlot.BLUE()) {
        	if(Model.Player(player).getWolf().changeCollarColor(DyeColor.BLUE))
        		initializeItems(clickedSlot);
        	return;
        }
        if(clickedSlot == InvSlot.BROWN()) {
        	if(Model.Player(player).getWolf().changeCollarColor(DyeColor.BROWN))
        		initializeItems(clickedSlot);
        	return;
        }
        if(clickedSlot == InvSlot.GREEN()) {
        	if(Model.Player(player).getWolf().changeCollarColor(DyeColor.GREEN))
        		initializeItems(clickedSlot);
        	return;
        }
        if(clickedSlot == InvSlot.RED()) {
        	if(Model.Player(player).getWolf().changeCollarColor(DyeColor.RED))
        		initializeItems(clickedSlot);
        	return;
        }
        if(clickedSlot == InvSlot.BLACK()) {
        	if(Model.Player(player).getWolf().changeCollarColor(DyeColor.BLACK))
        		initializeItems(clickedSlot);
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

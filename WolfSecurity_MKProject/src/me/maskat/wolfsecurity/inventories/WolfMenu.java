package me.maskat.wolfsecurity.inventories;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.maskat.wolfsecurity.Message;
import me.maskat.wolfsecurity.models.Model;

//public class WolfMenu {
//    public static Inventory myInventory = Bukkit.createInventory(null, 18, "My custom Inventory!");
//    // The first parameter, is the inventory owner. I make it null to let everyone use it.
//    //The second parameter, is the slots in a inventory. Must be a multiple of 9. Can be up to 54.
//    //The third parameter, is the inventory name. This will accept chat colors.
//    
//    static {
//	    myInventory.setItem(0, new ItemStack(Material.DIRT, 1));
//	    myInventory.setItem(8, new ItemStack(Material.GOLD_BLOCK, 1));
//	    //The first parameter, is the slot that is assigned to. Starts counting at 0
//    }
//    
//    @EventHandler
//    public void onInventoryClick(InventoryClickEvent event) {
//	    Player player = (Player) event.getWhoClicked(); // The player that clicked the item
//	    ItemStack clicked = event.getCurrentItem(); // The item that was clicked
//	    Inventory inventory = event.getInventory(); // The inventory that was clicked in
//	    if (inventory.getName().equals(myInventory.getName())) { // The inventory is our custom Inventory
//		    if (clicked.getType() == Material.DIRT) { // The item that the player clicked it dirt
//			    event.setCancelled(true); // Make it so the dirt is back in its original spot
//			    player.closeInventory(); // Closes there inventory
//			    player.getInventory().addItem(new ItemStack(Material.DIRT, 1)); // Adds dirt
//		    }
//	    }
//    }
    
public class WolfMenu
{
	private Player player = null;
	public WolfMenu(Player p) {
		player = p;
	}

    private WolfMenuMain wolfMenuMain = new WolfMenuMain();
    
    private WolfMenuCollarColor wolfMenuCollarColor = new WolfMenuCollarColor();
    
    private WolfMenuFamily wolfMenuFamily = new WolfMenuFamily();
    private WolfMenuFamilyShowUsers wolfMenuFamilyShowUsersInventory = new WolfMenuFamilyShowUsers();
    private WolfMenuFamilyAddUsers wolfMenuFamilyAddUsersInventory = new WolfMenuFamilyAddUsers();
    
    private WolfMenuFriends wolfMenuFriends = new WolfMenuFriends();
    private WolfMenuFriendsShowUsers wolfMenuFriendsShowUsersInventory = new WolfMenuFriendsShowUsers();
    private WolfMenuFriendsAddUsers wolfMenuFriendsAddUsersInventory = new WolfMenuFriendsAddUsers();
    
    private WolfMenuEnemies wolfMenuEnemies = new WolfMenuEnemies();
    private WolfMenuEnemiesShowUsers wolfMenuEnemiesShowUsersInventory = new WolfMenuEnemiesShowUsers();
    private WolfMenuEnemiesAddUsers wolfMenuEnemiesAddUsersInventory = new WolfMenuEnemiesAddUsers();

	public void onInventoryClick(final InventoryClickEvent e, final Player player)
	{
		if (e.getInventory().getHolder() == wolfMenuMain) wolfMenuMain.onInventoryClick(e, player);
		
		if (e.getInventory().getHolder() == wolfMenuCollarColor) wolfMenuCollarColor.onInventoryClick(e, player);
		
		if (e.getInventory().getHolder() == wolfMenuFamily) wolfMenuFamily.onInventoryClick(e, player);
		if (e.getInventory().getHolder() == wolfMenuFamilyShowUsersInventory) wolfMenuFamilyShowUsersInventory.onInventoryClick(e, player);
		if (e.getInventory().getHolder() == wolfMenuFamilyAddUsersInventory) wolfMenuFamilyAddUsersInventory.onInventoryClick(e, player);
		
		if (e.getInventory().getHolder() == wolfMenuFriends) wolfMenuFriends.onInventoryClick(e, player);
		if (e.getInventory().getHolder() == wolfMenuFriendsShowUsersInventory) wolfMenuFriendsShowUsersInventory.onInventoryClick(e, player);
		if (e.getInventory().getHolder() == wolfMenuFriendsAddUsersInventory) wolfMenuFriendsAddUsersInventory.onInventoryClick(e, player);
		
		if (e.getInventory().getHolder() == wolfMenuEnemies) wolfMenuEnemies.onInventoryClick(e, player);
		if (e.getInventory().getHolder() == wolfMenuEnemiesShowUsersInventory) wolfMenuEnemiesShowUsersInventory.onInventoryClick(e, player);
		if (e.getInventory().getHolder() == wolfMenuEnemiesAddUsersInventory) wolfMenuEnemiesAddUsersInventory.onInventoryClick(e, player);
	}
	
	//////////////////////////////////////////
    
    public void initAllGui() {
    	Model.Player(player).unsetListens(player);
    	
    	if(Model.Player(player).getOwnRegion().hideBorders())
			Message.sendMessage(player, "inventory.wolfmenu.region.showborders.done.canceled");
    	
    	//TODO
    	/////unset listenlogblock
    	
    	wolfMenuMain.createGui(player);
    	
    	wolfMenuCollarColor.createGui(player);
    	
    	wolfMenuFamily.createGui(player);
    	wolfMenuFamilyShowUsersInventory.createGui(player);
    	wolfMenuFamilyAddUsersInventory.createGui(player);
    	
    	wolfMenuFriends.createGui(player);
    	wolfMenuFriendsShowUsersInventory.createGui(player);
    	wolfMenuFriendsAddUsersInventory.createGui(player);
    	
    	wolfMenuEnemies.createGui(player);
    	wolfMenuEnemiesShowUsersInventory.createGui(player);
    	wolfMenuEnemiesAddUsersInventory.createGui(player);
    }
    
	public void openMainInventory() { wolfMenuMain.openInventory(player); }
	public void onInventoryClose(InventoryCloseEvent e, Player player) {
		if (e.getInventory().getHolder() == wolfMenuMain ||
				e.getInventory().getHolder() == wolfMenuCollarColor || 
				e.getInventory().getHolder() == wolfMenuFamily || 
				e.getInventory().getHolder() == wolfMenuFamilyShowUsersInventory || 
				e.getInventory().getHolder() == wolfMenuFamilyAddUsersInventory || 
				e.getInventory().getHolder() == wolfMenuFriends || 
				e.getInventory().getHolder() == wolfMenuFriendsShowUsersInventory || 
				e.getInventory().getHolder() == wolfMenuFriendsAddUsersInventory || 
				e.getInventory().getHolder() == wolfMenuEnemies || 
				e.getInventory().getHolder() == wolfMenuEnemiesShowUsersInventory || 
				e.getInventory().getHolder() == wolfMenuEnemiesAddUsersInventory) Model.Player(player).getWolf().fixSitting();
	}
	
	public void openCollarColorInventory() { wolfMenuCollarColor.openInventory(player); }
	
	public void openFamilyInventory() { wolfMenuFamily.openInventory(player); }
	public void openFamilyShowUsersInventory() { wolfMenuFamilyShowUsersInventory.openInventory(player); }
	public void openFamilyAddUsersInventory() { wolfMenuFamilyAddUsersInventory.openInventory(player); }
	
	public void openFriendsInventory() { wolfMenuFriends.openInventory(player); }
	public void openFriendsShowUsersInventory() { wolfMenuFriendsShowUsersInventory.openInventory(player); }
	public void openFriendsAddUsersInventory() { wolfMenuFriendsAddUsersInventory.openInventory(player); }
	
	public void openEnemiesInventory() { wolfMenuEnemies.openInventory(player); }
	public void openEnemiesShowUsersInventory() { wolfMenuEnemiesShowUsersInventory.openInventory(player); }
	public void openEnemiesAddUsersInventory() { wolfMenuEnemiesAddUsersInventory.openInventory(player); }
	
	public void initializeNewUsersInventoryWithout(Object menu) {
		if(!(menu instanceof WolfMenuFamilyShowUsers)) initializeNewFamilyShowUsersInventory();
		if(!(menu instanceof WolfMenuFamilyAddUsers)) initializeNewFamilyAddUsersInventory();
		if(!(menu instanceof WolfMenuFriendsShowUsers)) initializeNewFriendsShowUsersInventory();
		if(!(menu instanceof WolfMenuFriendsAddUsers)) initializeNewFriendsAddUsersInventory();
		if(!(menu instanceof WolfMenuEnemiesShowUsers)) initializeNewEnemiesShowUsersInventory();
		if(!(menu instanceof WolfMenuEnemiesAddUsers)) initializeNewEnemiesAddUsersInventory();
	}
	
	private void initializeNewFamilyShowUsersInventory() {
		wolfMenuFamilyShowUsersInventory = new WolfMenuFamilyShowUsers();
		wolfMenuFamilyShowUsersInventory.createGui(player);
	}
	private void initializeNewFamilyAddUsersInventory() {
		wolfMenuFamilyAddUsersInventory = new WolfMenuFamilyAddUsers();
		wolfMenuFamilyAddUsersInventory.createGui(player);
	}
	private void initializeNewFriendsShowUsersInventory() {
		wolfMenuFriendsShowUsersInventory = new WolfMenuFriendsShowUsers();
		wolfMenuFriendsShowUsersInventory.createGui(player);
	}
	private void initializeNewFriendsAddUsersInventory() {
		wolfMenuFriendsAddUsersInventory = new WolfMenuFriendsAddUsers();
		wolfMenuFriendsAddUsersInventory.createGui(player);
	}
	private void initializeNewEnemiesShowUsersInventory() {
		wolfMenuEnemiesShowUsersInventory = new WolfMenuEnemiesShowUsers();
		wolfMenuEnemiesShowUsersInventory.createGui(player);
	}
	private void initializeNewEnemiesAddUsersInventory() {
		wolfMenuEnemiesAddUsersInventory = new WolfMenuEnemiesAddUsers();
		wolfMenuEnemiesAddUsersInventory.createGui(player);
	}
	
	//////////////////////////////////////////
	
	
	
	//////////////////////////////////////////
	
	public static ItemStack createGuiItem(final Material material)
	{
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName("");

        // Set the lore of the item
        meta.setLore(null);

        item.setItemMeta(meta);

        return item;
	}
	
    // Nice little method to create a gui item with a custom name, and description
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
    
    public static ItemStack createGuiItemParseLang(final Material material, final String msglang)
    {
    	String[] msgArray = msglang.split("\n");
    	return createGuiItemTitleArray(material, msgArray[0], Arrays.copyOfRange(msgArray, 1, msgArray.length));
    }
    
    public static ItemStack createGuiItemHeadParseLang(final int userid, final String msglang)
    {
    	String[] msgArray = msglang.split("\n");
    	return createGuiItemHead(userid, msgArray[0], Arrays.copyOfRange(msgArray, 1, msgArray.length));
    }
    
//    public static ItemStack applySkullTexture() {
//    	net.minecraft.server.v1_15_R1.ItemStack item = CraftItemStack.asNMSCopy(new ItemStack(Material.PLAYER_HEAD, 1));
//        NBTTagCompound tag;
//        if (item.hasTag()) {
//            tag = item.getTag();
//        } else {
//            tag = new NBTTagCompound();
//        }
//        NBTTagCompound skullOwner = new NBTTagCompound();
//        NBTTagCompound properties = new NBTTagCompound();
//        NBTTagList textures = new NBTTagList();
//        NBTTagCompound texture = new NBTTagCompound();
//
//        texture.setString("Value", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTU5ZmI5NTdiNzM2MzBiMTczYzIyNTJkNTZkODI5NzVmZGYwZThkZjVjMGMzZjNhNTE0MjU4NWExYjM3MDA5MyJ9fX0=");
//        textures.add(texture);
//        properties.set("textures", textures);
//        skullOwner.set("Properties", properties);
//        tag.set("SkullOwner", skullOwner);
//
//        item.setTag(tag);
//        return CraftItemStack.asBukkitCopy(item);
//    }
    
    private static ItemStack createGuiItemHead(final int userid, final String name, final String[] lore)
    {
    	ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta)skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getServer().getOfflinePlayer(Model.User(userid).getUUID()));
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        skull.setItemMeta(meta);
        return skull;
    }
}

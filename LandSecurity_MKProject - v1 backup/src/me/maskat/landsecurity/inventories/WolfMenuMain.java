package me.maskat.landsecurity.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableMap;

import me.maskat.landsecurity.Config;
import me.maskat.landsecurity.Message;
import me.maskat.landsecurity.models.Model;

public class WolfMenuMain implements InventoryHolder
{
    private Inventory inv;
    private static class InvSlot {
    	private static int wolfInfo() { return 1; }
    	private static int pluginInfo() { return 7; }
    	private static int wolfChangeName() { return 12; }
    	private static int wolfChangeCollarColor() { return 13; }
    	private static int wolfLevelUp() { return 14; }
    	private static int wolfManageFamily() { return 21; }
    	private static int wolfManageFriends() { return 22; }
    	private static int wolfManageEnemies() { return 23; }
    	private static int regionClaim() { return 39; }
    	private static int regionShowBorders() { return 40; }
    	private static int regionChangeMessage() { return 41; }
    }

    private String listenChatFor = "";
    
	@Override
    public Inventory getInventory() { return inv; }
    
    public void createGui(Player player)
    {
        // Create a new inventory, with "this" owner for comparison with other inventories, a size of nine, called example
        inv = Bukkit.createInventory(this, 54, Message.getColorMessage(Model.Player(player).getWolf().getName()));
        // Put the items into the inventory
        initializeItems(player);
    }


    // You can call this whenever you want to put the items in
    private void initializeItems(Player player)
    {
        //inv.setItem(invSlotChoose1,WolfMenu.createGuiItem(Material.NAME_TAG, Message.getColorMessageLang("inventory.wolfmenu.changename"), "§7Nazwa ta wyœwietla siê", "§7zawsze nad jego cia³em"));
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
        
        inv.setItem(InvSlot.wolfInfo(),WolfMenu.createGuiItemParseLang(Material.ENCHANTED_BOOK, Message.getColorMessageLang("inventory.wolfmenu.wolf.info.slot_title", ImmutableMap.of(
    		    "level", String.valueOf(Model.Player(player).getWolf().getLevel()),
    		    "protected_radius", Model.Player(player).getWolf().getProtectedArea()
    		))));
        inv.setItem(InvSlot.pluginInfo(),WolfMenu.createGuiItemParseLang(Material.BOOK, Message.getColorMessageLang("inventory.wolfmenu.plugin.info.slot_title")));
        
        inv.setItem(InvSlot.wolfChangeName(),WolfMenu.createGuiItemParseLang(Material.NAME_TAG, Message.getColorMessageLang("inventory.wolfmenu.wolf.changename.slot_title")));
        inv.setItem(InvSlot.wolfChangeCollarColor(),WolfMenu.createGuiItemParseLang(Material.LEAD, Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.slot_title")));
        inv.setItem(InvSlot.wolfLevelUp(),WolfMenu.createGuiItemParseLang(Material.EXPERIENCE_BOTTLE, Message.getColorMessageLang("inventory.wolfmenu.wolf.levelup.slot_title")));
        
        inv.setItem(InvSlot.wolfManageFamily(),WolfMenu.createGuiItemParseLang(Material.DIAMOND, Message.getColorMessageLang("inventory.wolfmenu.wolf.managefamily.slot_title")));
        inv.setItem(InvSlot.wolfManageFriends(),WolfMenu.createGuiItemParseLang(Material.EMERALD, Message.getColorMessageLang("inventory.wolfmenu.wolf.managefriends.slot_title")));
        inv.setItem(InvSlot.wolfManageEnemies(),WolfMenu.createGuiItemParseLang(Material.REDSTONE, Message.getColorMessageLang("inventory.wolfmenu.wolf.manageenemies.slot_title")));
        
        if(Model.Player(player).isClaimedRegion())
        	inv.setItem(InvSlot.regionClaim(),WolfMenu.createGuiItemParseLang(Material.WHITE_BANNER, Message.getColorMessageLang("inventory.wolfmenu.region.unclaim.slot_title")));
        else
        	inv.setItem(InvSlot.regionClaim(),WolfMenu.createGuiItemParseLang(Material.GREEN_BANNER, Message.getColorMessageLang("inventory.wolfmenu.region.claim.slot_title")));
        if(Model.Player(player).isClaimedRegion())
        	inv.setItem(InvSlot.regionShowBorders(),WolfMenu.createGuiItemParseLang(Material.OAK_FENCE, Message.getColorMessageLang("inventory.wolfmenu.region.showborders.slot_title")));
        
        inv.setItem(InvSlot.regionChangeMessage(),WolfMenu.createGuiItemParseLang(Material.OAK_SIGN, Message.getColorMessageLang("inventory.wolfmenu.region.changemessage.slot_title")));
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
        if(clickedSlot == InvSlot.wolfChangeName()) {
        	player.closeInventory();
        	Message.sendMessage(player, "inventory.wolfmenu.wolf.changename.input");
        	Model.Player(player).setListenChat(this);
        	listenChatFor = "ChangeWolfName";
        	return;
        }
        if(clickedSlot == InvSlot.wolfChangeCollarColor()) {
        	Model.Player(player).getWolfMenu().openCollarColorInventory();
        	return;
        }
        if(clickedSlot == InvSlot.wolfLevelUp()) {
        	//TODO !!!!!!
//        	if(Model.Player(player).wolfLevelUp())
//        	{
//        		inv.setItem(InvSlot.wolfLevelUp(),WolfMenu.createGuiItemParseLang(Material.EXPERIENCE_BOTTLE, Message.getColorMessageLang("inventory.wolfmenu.wolf.levelup.done.raised", ImmutableMap.of(
//	        		    "level", String.valueOf(Model.Player(player).getWolf().getLevel())
//	        		))));
//        		int protectedRadius = Model.Player(player).getWolf().getProtectedRadius()*2;
//	            inv.setItem(InvSlot.wolfInfo(),WolfMenu.createGuiItemParseLang(Material.ENCHANTED_BOOK, Message.getColorMessageLang("inventory.wolfmenu.wolf.info.slot_title", ImmutableMap.of(
//	        		    "level", String.valueOf(Model.Player(player).getWolf().getLevel()),
//	        		    "protected_radius", Model.Player(player).getWolf().getProtectedArea()
//	        		))));
//        	}
//        	else
//        		inv.setItem(InvSlot.wolfLevelUp(),WolfMenu.createGuiItemParseLang(Material.EXPERIENCE_BOTTLE, Message.getColorMessageLang("inventory.wolfmenu.wolf.levelup.error.nomoney")));
        	return;
        }
        if(clickedSlot == InvSlot.wolfManageFamily()) {
        	//TODO: future...
        	//Model.Player(player).getWolfMenu().openFamilyInventory();
        	return;
        }
        if(clickedSlot == InvSlot.wolfManageFriends()) {
        	Model.Player(player).getWolfMenu().openFriendsInventory();
        	return;
        }
        if(clickedSlot == InvSlot.wolfManageEnemies()) {
        	//TODO: future...
        	//Model.Player(player).getWolfMenu().openEnemiesInventory();
        	return;
        }
        if(clickedSlot == InvSlot.regionClaim()) {
        	if(player.getWorld() != Bukkit.getServer().getWorld(Config.allowWorld))
        		return;
        	
        	if(!Model.Player(player).isClaimedRegion())
        	{
        		int regionid = Model.Player(player).claimRegion(Model.Player(player).getWolfEntity().getLocation(), Model.Player(player).getWolf().getProtectedRadius());
        		if(regionid < 0)
        		{
        			Message.sendMessage(player, "inventory.wolfmenu.region.claim.error.nearotherregion");
        		}
        		else
        		{
	        		Message.sendMessage(player, "inventory.wolfmenu.region.claim.done.claimed");
	        		inv.setItem(InvSlot.regionClaim(),WolfMenu.createGuiItemParseLang(Material.WHITE_BANNER, Message.getColorMessageLang("inventory.wolfmenu.region.unclaim.slot_title")));
	        		inv.setItem(InvSlot.regionShowBorders(),WolfMenu.createGuiItemParseLang(Material.OAK_FENCE, Message.getColorMessageLang("inventory.wolfmenu.region.showborders.slot_title")));
        		}
        	}
        	else
        	{
        		int regionid = Model.Player(player).unclaimRegion();
        		Message.sendMessage(player, "inventory.wolfmenu.region.unclaim.done.unclaimed");
        		inv.setItem(InvSlot.regionClaim(),WolfMenu.createGuiItemParseLang(Material.GREEN_BANNER, Message.getColorMessageLang("inventory.wolfmenu.region.claim.slot_title")));
        		inv.setItem(InvSlot.regionShowBorders(), null);
        	}
        	return;
        }
        if(clickedSlot == InvSlot.regionShowBorders())
        {
        	if(Model.Player(player).isClaimedRegion())
        	{
        		Model.Player(player).spawnBordersOwnRegion();
        		player.closeInventory();
        		Message.sendMessage(player, "inventory.wolfmenu.region.showborders.done.showing");
        	}
        	else
        	{
        		inv.setItem(InvSlot.regionShowBorders(), null);
        	}
        	
//        	EntityHider entityHider = new EntityHider(LandSecurity.plugin, Policy.WHITELIST);
//            final Sheep sheep = player.getWorld().spawn(player.getLocation(), Sheep.class);
// 
//            // Show a particular entity
//            entityHider.toggleEntity(player, sheep);
 
//            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(LandSecurity.plugin, new Runnable() {
//                @Override
//                public void run() {
//                    entityHider.toggleEntity(player, sheep);
//                }
//            }, 100);
            
//            PacketContainer fakeblock = new PacketContainer(PacketType.Play.Server.BLOCK_CHANGE);
//            fakeblock.getBlockPositionModifier().write(0, new BlockPosition(
//            		player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()));
//            fakeblock.getBlockData().write(0, WrappedBlockData.createData(Material.IRON_BLOCK));
//            try {
//                ProtocolLibrary.getProtocolManager().sendServerPacket(player, fakeblock);
//            } catch (InvocationTargetException e1) {
//                throw new RuntimeException("Cannot send packet " + fakeblock, e1);
//            }
        	
        	//spawnBarrier(player);
        }
	}
	
	//private void spawnBarrier(Player player) {
		//player.spawnParticle(Particle.DRIP_LAVA, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 1);
//		PacketContainer packet = new PacketContainer(PacketType.Play.Server.WORLD_PARTICLES);
//		// particle id
//		packet.getParticles().write(0, Particle.DRIP_LAVA); //Particle itself
//		// long distance
//		packet.getBooleans().write(0, false);        
//		// position
//		packet.getFloat().write(0, (float)player.getLocation().getX()); //x
//		packet.getFloat().write(1, (float)player.getLocation().getY()); //y
//		packet.getFloat().write(2, (float)player.getLocation().getZ()); //z
//		// offset
//		packet.getFloat().write(0, 0F);                                              
//		packet.getFloat().write(1, 0F);                                              
//		packet.getFloat().write(2, 0F);       
//		// extra
//		packet.getFloat().write(0, 1F);                                              
//		// amount
//		packet.getIntegers().write(0, 1); //Number of particles                                    
//		// particle specific (dust)
////		packet.getFloat().write(0, isInside ? 0F : 1F); // red
////		packet.getFloat().write(1, isInside ? 1F : 0F); // green
////		packet.getFloat().write(2, 0F); // blue
//		packet.getFloat().write(3, 2F); // size
//		packet.getWorldTypeModifier().write(0, arg1)
//		
//		
//		try {
//			ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
//		} catch (InvocationTargetException e1) {
//			throw new RuntimeException("Cannot send packet " + packet, e1);
//		}
	//}
	
	public boolean onAsyncPlayerChat(AsyncPlayerChatEvent e)
	{
		Player player = e.getPlayer();
		
		if(listenChatFor == "ChangeWolfName") {
			String message = e.getMessage();
			if(message.equalsIgnoreCase("cancel"))
			{
				Message.sendMessage(player, "inventory.wolfmenu.wolf.changename.done.canceled");
				listenChatFor = "";
				return true;
			}
			if(message.length() < 3 || message.length() > 16)
			{
				Message.sendMessage(player, "inventory.wolfmenu.wolf.changename.error.canceled_length");
				return true;
			}
			if(!message.matches("^[a-zA-Z0-9 ]*$"))
			{
				Message.sendMessage(player, "inventory.wolfmenu.wolf.changename.error.canceled_alfanumeric");
				return true;
			}
			
			Model.Player(player).getWolf().changeName("&e"+message);
			Message.sendMessage(player, "inventory.wolfmenu.wolf.changename.done.changed");
		}
		
		listenChatFor = "";
		return true;
	}
}

package me.maskat.wolfsecurity.inventories;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableMap;

import me.maskat.wolfsecurity.Message;
import me.maskat.wolfsecurity.Plugin;
import me.maskat.wolfsecurity.models.Model;
import net.coreprotect.CoreProtectAPI.ParseResult;

public class WolfMenuMain implements InventoryHolder
{
	private boolean tempBlockInteract = false;
	private Location lastBlockInteract = null;
	
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
    	//private static int regionChangeMessage() { return 41; }
    	private static int regionCoreProtectInfo() { return 41; }
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
        //inv.setItem(invSlotChoose1,WolfMenu.createGuiItem(Material.NAME_TAG, Message.getColorMessageLang("inventory.wolfmenu.changename"), "�7Nazwa ta wy�wietla si�", "�7zawsze nad jego cia�em"));
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
        
        setItemWolfInfo(player);
        inv.setItem(InvSlot.pluginInfo(),WolfMenu.createGuiItemParseLang(Material.BOOK, Message.getColorMessageLang("inventory.wolfmenu.plugin.info.slot_title")));
        
        inv.setItem(InvSlot.wolfChangeName(),WolfMenu.createGuiItemParseLang(Material.NAME_TAG, Message.getColorMessageLang("inventory.wolfmenu.wolf.changename.slot_title")));
        inv.setItem(InvSlot.wolfChangeCollarColor(),WolfMenu.createGuiItemParseLang(Material.LEAD, Message.getColorMessageLang("inventory.wolfmenu.wolf.changecollarcolor.slot_title")));
		if(Model.Player(player).getWolf().isMaxLevel())
		{
    		inv.setItem(InvSlot.wolfLevelUp(),WolfMenu.createGuiItemParseLang(Material.EXPERIENCE_BOTTLE, Message.getColorMessageLang("inventory.wolfmenu.wolf.levelup.slot_title_max", ImmutableMap.of(
        		    "exp_cost", String.valueOf(Model.Player(player).getWolf().getLevelUpCost()),
        		    "player_exp", String.valueOf(Model.Player(player).getEconomy())
        		))));
		}
		else
		{
			inv.setItem(InvSlot.wolfLevelUp(),WolfMenu.createGuiItemParseLang(Material.EXPERIENCE_BOTTLE, Message.getColorMessageLang("inventory.wolfmenu.wolf.levelup.slot_title", ImmutableMap.of(
	    		    "exp_cost", String.valueOf(Model.Player(player).getWolf().getLevelUpCost()),
	    		    "player_exp", String.valueOf(Model.Player(player).getEconomy())
	    		))));
		}
        inv.setItem(InvSlot.wolfManageFamily(),WolfMenu.createGuiItemParseLang(Material.DIAMOND, Message.getColorMessageLang("inventory.wolfmenu.wolf.managefamily.slot_title")));
        inv.setItem(InvSlot.wolfManageFriends(),WolfMenu.createGuiItemParseLang(Material.EMERALD, Message.getColorMessageLang("inventory.wolfmenu.wolf.managefriends.slot_title")));
        inv.setItem(InvSlot.wolfManageEnemies(),WolfMenu.createGuiItemParseLang(Material.REDSTONE, Message.getColorMessageLang("inventory.wolfmenu.wolf.manageenemies.slot_title")));
        
        if(Model.Player(player).isClaimedRegion())
        {
        	inv.setItem(InvSlot.regionClaim(),WolfMenu.createGuiItemParseLang(Material.WHITE_BANNER, Message.getColorMessageLang("inventory.wolfmenu.region.unclaim.slot_title")));
        	inv.setItem(InvSlot.regionShowBorders(),WolfMenu.createGuiItemParseLang(Material.OAK_FENCE, Message.getColorMessageLang("inventory.wolfmenu.region.showborders.slot_title_on")));
        	inv.setItem(InvSlot.regionCoreProtectInfo(),WolfMenu.createGuiItemParseLang(Material.REDSTONE_TORCH, Message.getColorMessageLang("inventory.wolfmenu.region.coreprotectinfo.slot_title_on")));
        }
        else
        {
        	inv.setItem(InvSlot.regionClaim(),WolfMenu.createGuiItemParseLang(Material.GREEN_BANNER, Message.getColorMessageLang("inventory.wolfmenu.region.claim.slot_title")));
        	inv.setItem(InvSlot.regionShowBorders(),WolfMenu.createGuiItemParseLang(Material.OAK_FENCE, Message.getColorMessageLang("inventory.wolfmenu.region.showborders.slot_title_off")));
        	inv.setItem(InvSlot.regionCoreProtectInfo(),WolfMenu.createGuiItemParseLang(Material.REDSTONE_TORCH, Message.getColorMessageLang("inventory.wolfmenu.region.coreprotectinfo.slot_title_off")));
        }
        
        //inv.setItem(InvSlot.regionChangeMessage(),WolfMenu.createGuiItemParseLang(Material.OAK_SIGN, Message.getColorMessageLang("inventory.wolfmenu.region.changemessage.slot_title")));
        //inv.setItem(InvSlot.regionCoreProtectInfo(),WolfMenu.createGuiItemParseLang(Material.OAK_SIGN, Message.getColorMessageLang("inventory.wolfmenu.region.coreprotectinfo.slot_title")));
    }
    
    private void setItemWolfInfo(Player player) {
    	String claimed_datetime = Model.Player(player).getOwnRegion().getClaimedDateTimeString();
    	inv.setItem(InvSlot.wolfInfo(),WolfMenu.createGuiItemParseLang(Material.ENCHANTED_BOOK, Message.getColorMessageLang("inventory.wolfmenu.wolf.info.slot_title", ImmutableMap.of(
    		    "level", String.valueOf(Model.Player(player).getWolf().getLevel()),
    		    "protected_radius", Model.Player(player).getWolf().getProtectedArea(),
    		    "claimed_datetime", (Model.Player(player).isClaimedRegion() ? (claimed_datetime == null ? "Brak danych" : claimed_datetime) : "-")
    		))));
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent)
    {
    	tempBlockInteract = false;
    	lastBlockInteract = null;
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
    		Model.Player(player).getWolf().setSitting(Model.Player(player).getWolfEntity(), true);
    		
    		Model.Player(player).setListenChat(this);
        	listenChatFor = "ChangeWolfName";
        	
    		player.closeInventory();
        	Message.sendMessage(player, "inventory.wolfmenu.wolf.changename.input");
        	
        	return;
        }
        if(clickedSlot == InvSlot.wolfChangeCollarColor()) {
        	Model.Player(player).getWolfMenu().openCollarColorInventory();
        	return;
        }
        if(clickedSlot == InvSlot.wolfLevelUp()) {
        	//TODO !!!!!!
    		if(Model.Player(player).getWolf().isMaxLevel())
    		{
        		inv.setItem(InvSlot.wolfLevelUp(),WolfMenu.createGuiItemParseLang(Material.EXPERIENCE_BOTTLE, Message.getColorMessageLang("inventory.wolfmenu.wolf.levelup.slot_title_max", ImmutableMap.of(
	        		    "exp_cost", String.valueOf(Model.Player(player).getWolf().getLevelUpCost()),
	        		    "player_exp", String.valueOf(Model.Player(player).getEconomy())
	        		))));
        		return;
    		}
    		
        	if(Model.Player(player).wolfLevelUp())
        	{
        		if(Model.Player(player).getWolf().isMaxLevel())
        		{
	        		inv.setItem(InvSlot.wolfLevelUp(),WolfMenu.createGuiItemParseLang(Material.EXPERIENCE_BOTTLE, Message.getColorMessageLang("inventory.wolfmenu.wolf.levelup.done.raised_max", ImmutableMap.of(
		        		    "exp_cost", String.valueOf(Model.Player(player).getWolf().getLevelUpCost()),
		        		    "player_exp", String.valueOf(Model.Player(player).getEconomy()),
		        		    "level", String.valueOf(Model.Player(player).getWolf().getLevel())
		        		))));
        		}
        		else
        		{
	        		inv.setItem(InvSlot.wolfLevelUp(),WolfMenu.createGuiItemParseLang(Material.EXPERIENCE_BOTTLE, Message.getColorMessageLang("inventory.wolfmenu.wolf.levelup.done.raised", ImmutableMap.of(
		        		    "exp_cost", String.valueOf(Model.Player(player).getWolf().getLevelUpCost()),
		        		    "player_exp", String.valueOf(Model.Player(player).getEconomy()),
		        		    "level", String.valueOf(Model.Player(player).getWolf().getLevel())
		        		))));
        		}
        		//int protectedRadius = Model.Player(player).getWolf().getProtectedRadius()*2;
        		setItemWolfInfo(player);
        	}
        	else
        	{
	    		inv.setItem(InvSlot.wolfLevelUp(),WolfMenu.createGuiItemParseLang(Material.EXPERIENCE_BOTTLE, Message.getColorMessageLang("inventory.wolfmenu.wolf.levelup.error.nomoney", ImmutableMap.of(
	        		    "exp_cost", String.valueOf(Model.Player(player).getWolf().getLevelUpCost()),
	        		    "player_exp", String.valueOf(Model.Player(player).getEconomy())
	        		))));
        	}
        	return;
        	
        }
        if(clickedSlot == InvSlot.wolfManageFamily()) {
        	//TODO: future...
        	Model.Player(player).getWolfMenu().openFamilyInventory();
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
        	if(player.getWorld() != Plugin.getAllowedWorld())
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
	        		inv.setItem(InvSlot.regionShowBorders(),WolfMenu.createGuiItemParseLang(Material.OAK_FENCE, Message.getColorMessageLang("inventory.wolfmenu.region.showborders.slot_title_on")));
	        		inv.setItem(InvSlot.regionCoreProtectInfo(),WolfMenu.createGuiItemParseLang(Material.REDSTONE_TORCH, Message.getColorMessageLang("inventory.wolfmenu.region.coreprotectinfo.slot_title_on")));
	        		setItemWolfInfo(player);
        		}
        	}
        	else
        	{
        		//int regionid = Model.Player(player).unclaimRegion();
        		Model.Player(player).unclaimRegion();
        		Message.sendMessage(player, "inventory.wolfmenu.region.unclaim.done.unclaimed");
        		inv.setItem(InvSlot.regionClaim(),WolfMenu.createGuiItemParseLang(Material.GREEN_BANNER, Message.getColorMessageLang("inventory.wolfmenu.region.claim.slot_title")));
        		inv.setItem(InvSlot.regionShowBorders(),WolfMenu.createGuiItemParseLang(Material.OAK_FENCE, Message.getColorMessageLang("inventory.wolfmenu.region.showborders.slot_title_off")));
        		inv.setItem(InvSlot.regionCoreProtectInfo(),WolfMenu.createGuiItemParseLang(Material.REDSTONE_TORCH, Message.getColorMessageLang("inventory.wolfmenu.region.coreprotectinfo.slot_title_off")));
        		setItemWolfInfo(player);
        	}
        	return;
        }
        
        if(clickedSlot == InvSlot.regionShowBorders())
        {
        	if(Model.Player(player).isClaimedRegion())
        	{
        		Model.Player(player).getWolf().setSitting(Model.Player(player).getWolfEntity(), true);
        		
        		Model.Player(player).spawnBordersOwnRegion();
        		
        		player.closeInventory();
        		Message.sendMessage(player, "inventory.wolfmenu.region.showborders.done.showing");
        	}
        	else
        	{
        		inv.setItem(InvSlot.regionShowBorders(),WolfMenu.createGuiItemParseLang(Material.OAK_FENCE, Message.getColorMessageLang("inventory.wolfmenu.region.showborders.slot_title_off")));
        	}
        	return;
        }
        
        if(clickedSlot == InvSlot.regionCoreProtectInfo())
        {
        	if(Model.Player(player).isClaimedRegion())
        	{
        		Model.Player(player).getWolf().setSitting(Model.Player(player).getWolfEntity(), true);
        		
        		Model.Player(player).setListenLogBlock(this);
	        	//listenChatFor = "RegionCheckLogBlock";
	        	
        		player.closeInventory();
	        	Message.sendMessage(player, "inventory.wolfmenu.region.coreprotectinfo.done.activation");
	        	//Model.Player(player).setListenChat(this);
	        	
	        	
        	}
        	else
        	{
        		inv.setItem(InvSlot.regionCoreProtectInfo(),WolfMenu.createGuiItemParseLang(Material.REDSTONE_TORCH, Message.getColorMessageLang("inventory.wolfmenu.region.coreprotectinfo.slot_title_off")));
        	}
        	return;
        	//coreProtectAPI.blockLookup(block, time)
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
	        	Model.Player(player).setListenChat(null);
				listenChatFor = null;
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
//		else if(listenChatFor == "RegionCheckLogBlock") {
//			String message = e.getMessage();
//			//f(message.equalsIgnoreCase("cancel"))
//			//{
//				Message.sendMessage(player, "inventory.wolfmenu.region.coreprotectinfo.done.canceled");
//	        	//Model.Player(player).setListenChat(null);
//	        	Model.Player(player).setListenBlockBreak(null);
//	        	Model.Player(player).setListenBlockPlace(null);
//	        	Model.Player(player).setListenPlayerInteract(null);
//				//listenChatFor = null;
//				return true;
//			//}
//			
//			
//			//TODO
			
			
		//}
		
    	Model.Player(player).setListenChat(null);
		listenChatFor = null;
		return true;
	}

//	public boolean onBlockPlace(BlockPlaceEvent e) {
//		blockLog(e.getPlayer(), e.getBlock());
//		
//		return false;
//	}
//	
//	public boolean onBlockBreak(BlockBreakEvent e) {
//		blockLog(e.getPlayer(), e.getBlock());
//		
//		return false;
//	}

//	public boolean onPlayerInteract(PlayerInteractEvent e) {
//		if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR)
//		{
//			Message.sendMessage(e.getPlayer(), "inventory.wolfmenu.region.coreprotectinfo.done.canceled");
//        	Model.Player(e.getPlayer()).setListenBlockBreak(null);
//        	Model.Player(e.getPlayer()).setListenBlockPlace(null);
//        	Model.Player(e.getPlayer()).setListenPlayerInteract(null);
//        	return true;
//		}
//		
//		blockLog(e.getPlayer(), e.getClickedBlock());
//		
//		return false;
//	}
	
	public boolean onLogBlock(PlayerInteractEvent e) {
		if(tempBlockInteract == true)
			return false;
		
		tempBlockInteract = true;
		
		Plugin.plugin.getServer().getScheduler().runTaskLaterAsynchronously(Plugin.plugin, new Runnable() {
            @Override
            public void run() {
            	tempBlockInteract = false;
            }
        }, 1L);
		
		if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR)
		{
			Message.sendMessage(e.getPlayer(), "inventory.wolfmenu.region.coreprotectinfo.done.canceled");
			tempBlockInteract = false;
			Model.Player(e.getPlayer()).setListenLogBlock(null);
        	return false;
		}
		
		if(lastBlockInteract != null && lastBlockInteract.getBlockX() == e.getClickedBlock().getLocation().getBlockX()  && lastBlockInteract.getBlockY() == e.getClickedBlock().getLocation().getBlockY() && lastBlockInteract.getBlockZ() == e.getClickedBlock().getLocation().getBlockZ())
		{
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK && !e.isBlockInHand())
				return true;
			else
				return false;
		}
		
		if(e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_AIR)
			lastBlockInteract = e.getClickedBlock().getLocation();
		
		if(!e.getHand().equals(EquipmentSlot.HAND))
			return false;
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && !e.isBlockInHand())
		{
			onLogBlock(e.getPlayer(), e.getClickedBlock());
			return true;
		}
		
		onLogBlock(e.getPlayer(), e.getClickedBlock());
		
		return false;
	}
	
	public boolean onLogBlock(Player player, Block block) {
		if(!Model.Player(player).isClaimedRegion())
		{
			Message.sendActionBar(player, "inventory.wolfmenu.region.coreprotectinfo.logblock.noclaimedregion");
			Message.sendMessage(player, "inventory.wolfmenu.region.coreprotectinfo.done.canceled");
        	//Model.Player(player).setListenChat(null);
        	Model.Player(player).setListenLogBlock(null);
        	tempBlockInteract = false;
        	lastBlockInteract = null;
			//listenChatFor = null;
			return false;
		}
		
		if(!Model.Player(player).getOwnRegion().isInRegion(block.getLocation()))
		{
			Message.sendActionBar(player, "inventory.wolfmenu.region.coreprotectinfo.logblock.outregion");
			return false;
		}
		
		//if(Config.debug2) Bukkit.broadcastMessage("blockLog history last secounds: "+(int)Model.Player(player).getOwnRegion().getSecoundsFromClaimedDateTime());
		
		int secounds = (int)Model.Player(player).getOwnRegion().getSecoundsFromClaimedDateTime();
		List<String[]> lookup = null;
		if(secounds > 0)
			lookup = Plugin.getCoreProtectAPI().blockLookup(block, secounds);
		
		boolean isSomething = false;
		
		if (lookup!=null) {
			Collections.reverse(lookup);
			for (String[] value : lookup) {
				ParseResult result = Plugin.getCoreProtectAPI().parseResult(value);
				String dateAsText = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
	                    .format(new Date(result.getTime() * 1000L));
				//if(!player.getName().equalsIgnoreCase(result.getPlayer()))
				//{
				
				String blockname = result.getBlockData().getAsString().replace("minecraft:", "").replace("_", " ");
				if(blockname.indexOf("[") > 0)
					blockname = blockname.substring(0, blockname.indexOf("["));
				
				if(!isSomething)
					player.sendMessage(Message.getColorMessage("&e---- &e&lLog Block&6: &aX&e=&b"+block.getLocation().getBlockX()+"&b&6, &aY&e=&b"+block.getLocation().getBlockY()+"&b&6, &aZ&e=&b"+block.getLocation().getBlockZ()+" &e----"));
					isSomething = true;
					player.sendMessage(Message.getColorMessage("&6"+dateAsText+"&f: &e"+result.getPlayer()+" &6"+result.getActionString().
							replace("Removal", Message.getColorMessageLang("inventory.wolfmenu.region.coreprotectinfo.logblock.wordbreak")).
							replace("Placement", Message.getColorMessageLang("inventory.wolfmenu.region.coreprotectinfo.logblock.wordplace")).
							replace("Interaction", Message.getColorMessageLang("inventory.wolfmenu.region.coreprotectinfo.logblock.wordinteract"))
							+"&f "+blockname));
				//}
			}
		}

		if(isSomething) {
			//Message.sendActionBar(player, "inventory.wolfmenu.region.coreprotectinfo.logblock.foundhistory");
		}
		else
		{
			player.sendMessage(Message.getColorMessage("&e---- &e&lLog Block&6: &aX&e=&b"+block.getLocation().getBlockX()+"&b&6, &aY&e=&b"+block.getLocation().getBlockY()+"&b&6, &aZ&e=&b"+block.getLocation().getBlockZ()+" &e----"));
			player.sendMessage(Message.getColorMessageLang("inventory.wolfmenu.region.coreprotectinfo.logblock.nohistory"));
			//Message.sendActionBar(player, "inventory.wolfmenu.region.coreprotectinfo.logblock.nohistory");
		}
		
		return false;
	}


}

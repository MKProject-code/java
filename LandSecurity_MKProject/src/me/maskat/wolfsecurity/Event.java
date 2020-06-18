package me.maskat.wolfsecurity;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import me.maskat.wolfsecurity.inventories.WolfMenu;
import me.maskat.wolfsecurity.models.Model;
import me.maskat.wolfsecurity.models.Model.Players;
import me.maskat.wolfsecurity.models.Model.Wolves;
import mkproject.maskat.Papi.Model.PapiPlayerLoginEvent;

public class Event implements Listener {
	
	//	e.setCancelled(true);
	
	//AuthMeApi authmeApi = AuthMeApi.getInstance();
	
	@EventHandler
    public void playerJoinEvent(final PlayerJoinEvent e) throws SQLException {
		if(Config.debug) Bukkit.broadcastMessage("player join!");
		//if(Config.debug) Bukkit.broadcastMessage("event join");
		//Player player = event.getPlayer();
		//if(Config.debug) Bukkit.broadcastMessage("addPlayer");
		//int userid = Model.Players.addPlayer(player);
    }
	
	@EventHandler
    public void playerQuitEvent(final PlayerQuitEvent e) throws SQLException {
		if(Config.debug) Bukkit.broadcastMessage("player quit!");
		
		Player player = e.getPlayer();
		
		Model.Players.removePlayer(player);
	}
	
	@EventHandler
	public void onPapiPlayerLoginEvent(final PapiPlayerLoginEvent e) throws SQLException {
		if(Config.debug) Bukkit.broadcastMessage("authme: logged!");
		
		Player player = e.getPlayer();
		
		//int userid = Model.Players.addPlayer(player);
		Model.Players.addPlayer(player);
		
        Function.checkPlayerInsideRegions(player, e.getPlayer().getLocation(), false);
	}
	
	@EventHandler
	public void onPlayerMoveEvent(final PlayerMoveEvent e) {
        if (e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockZ() == e.getFrom().getBlockZ()) return; //The player hasn't moved
        
        Player player = e.getPlayer();
        if(!Model.Players.isExist(player))
        	return;
        
        Function.checkPlayerInsideRegions(player, e.getTo(), false);
    }
	
	@EventHandler
	public void onPlayerTeleportEvent(final PlayerTeleportEvent e) {
        if (e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockZ() == e.getFrom().getBlockZ()) return; //The player hasn't moved
        
        Player player = e.getPlayer();
        if(!Model.Players.isExist(player))
        	return;
        
        Function.checkPlayerInsideRegions(player, e.getTo(), false);
	}
	
	@EventHandler
	public void onChunkUnloadEvent(final ChunkUnloadEvent e) {
		for(Entity entity : e.getChunk().getEntities()) {
			int wolfid = Wolves.getWolfId(entity.getUniqueId());
			if(wolfid < 0)
				continue;
			
			Model.Wolf(wolfid).setLastLocation(entity.getLocation());
		}
	}
	
	@EventHandler
	public void onEntityTargetEvent(final EntityTargetEvent e) {
		if(Config.debug) Bukkit.broadcastMessage("E: " + e.getEntity());
		if(Config.debug) Bukkit.broadcastMessage("T: " + e.getTarget());
		if(e.getTarget() != null && Model.Wolves.getWolfId(e.getTarget().getUniqueId()) > 0)
		{
			e.setCancelled(true);
		}
	}
    
    @EventHandler
    public void onEntityDamageByEntityEvent(final EntityDamageByEntityEvent e) {
    	if(Config.debug) Bukkit.broadcastMessage("EntityDamageByEntityEvent");
    	
    	if(Model.Wolves.existWolf(e.getEntity().getUniqueId()))
    	{
    		e.setCancelled(true);
    		
    		if(e.getDamager() instanceof Player)
    		{
    			Player player = (Player)e.getDamager();
    			
    			if(Model.Player(player).existWolf() && Model.Player(player).getWolf().getEnityUUID().equals(e.getEntity().getUniqueId()))
    				Message.sendActionBar(player, "actionbar.interact.wolf.owner.damage");
    			else
    				Message.sendActionBar(player, "actionbar.interact.wolf.other.damage");
    		}
    	}
    }
    
    @EventHandler
    public void onEntityPortalEvent(final EntityPortalEvent e) {
    	if(Config.debug) Bukkit.broadcastMessage("EntityPortalEvent");
    	
		if(Model.Wolves.existWolf(e.getEntity().getUniqueId()) && e.getTo().getWorld() != Bukkit.getServer().getWorld(Config.allowWorld))
		{
			if(Config.debug) Bukkit.broadcastMessage("WOLF EXIST -> canceled portal TP");
			e.setCancelled(true);
		}
    }
    
    @EventHandler
    public void onEntityTeleportEvent(final EntityTeleportEvent e) {
    	//if(Config.debug) Bukkit.broadcastMessage("onEntityTeleportEvent");
    	
    	int wolfid = Model.Wolves.getWolfId(e.getEntity().getUniqueId());
		if(wolfid > 0)
		{
			//if(Config.debug) Bukkit.broadcastMessage("WOLF EXIST");
			//if(Config.debug) Bukkit.broadcastMessage(e.getTo().getWorld().toString());
			//if(Config.debug) Bukkit.broadcastMessage(Bukkit.getServer().getWorld(Config.allowWorld).toString());
			if(e.getTo().getWorld() != Bukkit.getServer().getWorld(Config.allowWorld))
			{
				if(Config.debug) Bukkit.broadcastMessage("WOLF EXIST");
    			e.setCancelled(true);
			}
			else 
			{
				Player ownerplayer = Model.Wolf(wolfid).getOwner();
				if(ownerplayer == null || !Players.isExist(ownerplayer) || (Model.Player(ownerplayer).isClaimedRegion() && !Model.Player(ownerplayer).isInsideOwnRegion()))
					e.setCancelled(true);
			}
			
		}
    	
    }
    
    @EventHandler
    public void onEntityDamageByBlockEvent(final EntityDamageByBlockEvent e) {
    	if(Config.debug) Bukkit.broadcastMessage("onEntityDamageByBlockEvent");
//    	Entity entity = e.getEntity();
//    	if(!(entity instanceof Wolf))
//			return;
//			
//    	int wolfid = Model.Wolves.getWolfId(entity.getUniqueId());
//    	if(wolfid > 0)
//    	{
//    		Location entitylocation = entity.getLocation();
//    		
//    		entitylocation.setY(entity.getWorld().getHighestBlockYAt(entitylocation)+1);
//    		
//    		Model.Wolf(wolfid).setSitting((Wolf)entity, false);
//    		entity.teleport(entitylocation);
//    		
//    		e.setCancelled(true);
//    	}
    	
//    	Entity entity = e.getEntity();
//    	if(!(entity instanceof Wolf))
//			return;
//    	
//    	int wolfid = Model.Wolves.getWolfId(entity.getUniqueId());
//    	if(wolfid > 0)
//    	{
//    		if(Config.debug) Bukkit.broadcastMessage("Damager: " + e.getDamager().getType());
//    		if(e.getDamager().getType() != Material.AIR)
//    		{
//    			if(e.getDamager().getType() == Material.LAVA)
//    			{
////    				entitylocation.setY(entity.getWorld().getHighestBlockYAt(entitylocation));
////    				if(entitylocation.getBlock() == Material.LAVA)
////    				{
////    					
////    				}
//    				Model.Wolf(wolfid).setSitting((Wolf)entity, false);
//    			}
//    			else
//    			{
//    				Location entitylocation = entity.getLocation();
//    				entitylocation.setX(entitylocation.getBlockX()+0.5);
//    				entitylocation.setY(entity.getWorld().getHighestBlockYAt(entitylocation)+1);
//    				entitylocation.setZ(entitylocation.getBlockZ()+0.5);
//    				
//		    		Model.Wolf(wolfid).setSitting((Wolf)entity, false);
//		    		entity.teleport(entitylocation);
//    			}
//    		}
//    		//e.setCancelled(true);
//    	}
    }
    
    @EventHandler
    public void onEntityChangeBlockEvent(final EntityChangeBlockEvent e)
    {
    	if(Config.debug) Bukkit.broadcastMessage("onEntityChangeBlockEvent");
    }
    
    @EventHandler
    public void onEntityDamageEvent(final EntityDamageEvent e) {
    	if(Config.debug) Bukkit.broadcastMessage("onEntityDamageEvent");
    	if(Config.debug) Bukkit.broadcastMessage("e.getCause():" + e.getCause());
    	
    	Entity entity = e.getEntity();
    	if(!(entity instanceof Wolf))
			return;
    	
    	int wolfid = Model.Wolves.getWolfId(entity.getUniqueId());
    	if(wolfid > 0)
    	{
	    	if(e.getCause().equals(DamageCause.SUFFOCATION))
	    	{
				Location entitylocation = entity.getLocation();
				entitylocation.setX(entitylocation.getBlockX()+0.5);
				entitylocation.setY(entity.getWorld().getHighestBlockYAt(entitylocation)+1);
				entitylocation.setZ(entitylocation.getBlockZ()+0.5);
				
	    		Model.Wolf(wolfid).setSitting((Wolf)entity, false);
	    		entity.teleport(entitylocation);
	    	}
	    	
	    	e.setCancelled(true);
    	}
    	
//    	if(Model.Wolves.existWolf(entity.getUniqueId()))
//    		e.setCancelled(true);
//    	
//    	int wolfid = Model.Wolves.getWolfId(entity.getUniqueId());
//    	if(wolfid > 0)
//    	{
//    		Location entitylocation = entity.getLocation();
//    		if(entitylocation.getBlock().getType() != Material.AIR)
//    		{
//    			if(entitylocation.getBlock().getType() == Material.LAVA)
//    			{
////    				entitylocation.setY(entity.getWorld().getHighestBlockYAt(entitylocation));
////    				if(entitylocation.getBlock() == Material.LAVA)
////    				{
////    					
////    				}
//    				Model.Wolf(wolfid).setSitting((Wolf)entity, false);
//    			}
//    			else
//    			{
//    				entitylocation.setX(entitylocation.getBlockX()+0.5);
//    				entitylocation.setY(entity.getWorld().getHighestBlockYAt(entitylocation)+1);
//    				entitylocation.setZ(entitylocation.getBlockZ()+0.5);
//    				
//		    		Model.Wolf(wolfid).setSitting((Wolf)entity, false);
//		    		entity.teleport(entitylocation);
//    			}
//    		}
//    		else
//    		{
//				entitylocation.setX(entitylocation.getBlockX()+0.5);
//				entitylocation.setZ(entitylocation.getBlockZ()+0.5);
//				entity.teleport(entitylocation);
//    		}
//    		e.setCancelled(true);
//    	}
    	
//    	if(Model.Wolves.existWolf(e.getEntity().getUniqueId()))
//    		e.setCancelled(true);
    }
    
    @EventHandler
    public void onFoodLevelChangeEvent(final FoodLevelChangeEvent e) {
    	//if(Config.debug) Bukkit.broadcastMessage("FoodLevelChangeEvent");
    }
    
    @EventHandler
    public void onBlockGrowEvent(final BlockGrowEvent e) {
//    	Bukkit.broadcastMessage("BlockGrowEvent");
    }
    
    @EventHandler
    public void onBlockSpreadEvent(final BlockSpreadEvent e) {
//    	Bukkit.broadcastMessage("BlockSpreadEvent");
    }
    
    @EventHandler
    public void onLeavesDecayEvent(final LeavesDecayEvent e) {
    	//if(Config.debug) Bukkit.broadcastMessage("LeavesDecayEvent");
    }
    
    @EventHandler
    public void onBlockExplodeEvent(final BlockExplodeEvent e) {
    	//if(Config.debug) Bukkit.broadcastMessage("BlockExplodeEvent");
    }
    
    @EventHandler
    public void onEntityExplodeEvent(final EntityExplodeEvent e) {
    	//if(Config.debug) Bukkit.broadcastMessage("EntityExplodeEvent");
    }
    
    @EventHandler
    public void onBlockPlaceEvent(final BlockPlaceEvent e) {
        //if(e.getEnity() instanceof Player)
    	
//    	//TODO
//        int regionid = Model.Regions.getRegionId(e.getBlock().getLocation());
//        if(!Model.Regions().containsKey(regionid))
//        	return;
        ////////////////////////////
        
//        String regionName = Model.Region(regionid).getName();
//        Player player = e.getPlayer();
//        
//        if(realRegionName != null && !region.isOwner(player) && !region.isMemeber(player)) {
//        	Message.sendActionBar(player, "actionbar.deny.place", ImmutableMap.of(
//        		    "region_name", regionName
//        		));
//        	e.setCancelled(true);
//        }
    	
    	Player player = e.getPlayer();
        if(!Model.Players.isExist(player))
        	return;
        
    	if(!Model.Player(player).onBlockPlace(e))
    		e.setCancelled(true);
    }
    
    @EventHandler
    public void onBlockBreakEvent(final BlockBreakEvent e) {
        //if(e.getEnity() instanceof Player)
    	
//    	//TODO
//        int regionid = Model.Regions.getRegionId(e.getBlock().getLocation());
//        if(!Model.Regions().containsKey(regionid))
//        	return;
        ////////////////////////////
        
//        String realRegionName = region.getName();
//        Player player = e.getPlayer();
//        
//        if(realRegionName != null && !region.isOwner(player) && !region.isMemeber(player)) {
//        	Message.sendActionBar(player, "actionbar.deny.break", ImmutableMap.of(
//        		    "region_name", realRegionName
//        		));
//        	e.setCancelled(true);
//        }
    	
    	Player player = e.getPlayer();
        if(!Model.Players.isExist(player))
        	return;
        
    	if(!Model.Player(player).onBlockBreak(e))
    		e.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityPickupItemEvent(final EntityPickupItemEvent e) {
    	//if(Config.debug) Bukkit.broadcastMessage("EntityPickupItemEvent");
    }
    
    @EventHandler
    public void onPlayerDropItemEvent(final PlayerDropItemEvent e) {
    	//if(Config.debug) Bukkit.broadcastMessage("PlayerDropItemEvent");
    }
    
    @EventHandler
    public void onPlayerInteractEvent(final PlayerInteractEvent e) {
    	//if(Config.debug) Bukkit.broadcastMessage("PlayerInteractEvent");
    	
    	Player player = e.getPlayer();
        if(!Model.Players.isExist(player))
        	return;
        
    	if((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR) && Model.Player(player).existOwnRegion())
    	{
    		if(Model.Player(player).getOwnRegion().hideBorders())
    			Message.sendMessage(player, "inventory.wolfmenu.region.showborders.done.canceled");
    	}
    	
    	if(!Model.Player(player).onPlayerInteract(e))
    		e.setCancelled(true);
    	
    	//WolfGuard.summonWolf(e.getPlayer());
    }
    
    @EventHandler
    public void onPlayerInteractEntityEvent(final PlayerInteractEntityEvent e) {
    	//if(Config.debug) Bukkit.broadcastMessage("PlayerInteractEntityEvent");
    	
    	Player player = e.getPlayer();
        if(!Model.Players.isExist(player))
        	return;
        
//    	if(Model.Player(player).existOwnRegion())
//    	{
//    		if(Model.Player(player).getOwnRegion().hideBorders())
//    			Message.sendMessage(player, "inventory.wolfmenu.region.showborders.done.canceled");
//    		
//    		//Model.Player(e.getPlayer()).unsetListen(player);
//        	//Model.Player(e.getPlayer()).setListenChat(null);
//        	//Model.Player(e.getPlayer()).setListenBlockBreak(null);
//        	//Model.Player(e.getPlayer()).setListenBlockPlace(null);
//        	//Model.Player(e.getPlayer()).setListenPlayerInteract(null);
//    	}
    	
    	if(e.getRightClicked().getWorld() != Bukkit.getServer().getWorld(Config.allowWorld))
    		return;
    	
    	if(!(e.getRightClicked() instanceof Wolf))
    		return;
    	
    	if(Model.Player(player).existWolf()) {
    		if(e.getRightClicked().getUniqueId().equals(Model.Player(player).getWolf().getEnityUUID()))
    		{
    			Wolf wolfentity = Model.Player(player).getWolfEntity();
		    	if(player.getInventory().getItemInMainHand().getType() == Material.BONE) {
		        	Message.sendActionBar(player, "actionbar.interact.wolf.owner.item_bone");
		    		
		        	//Model.Player(player).getWolf().setSitting(wolfentity, true);
		    		
		        	wolfentity.setSitting(true);
		        	
		    		WolfMenu wolfmenu = Model.Player(player).getWolfMenu();
		    		wolfmenu.initAllGui();
		    		wolfmenu.openMainInventory();
		    		e.setCancelled(true);
		    		return;
		    	}
		    	else
		    	{
		    		if(Config.debug2) Bukkit.broadcastMessage("onPlayerInteractEntityEvent: isClaimedRegion()="+Model.Player(player).isClaimedRegion());
		    		if(Config.debug2) Bukkit.broadcastMessage("onPlayerInteractEntityEvent: isInsideOwnRegion="+Model.Player(player).isInsideOwnRegion());
		    		
		    		if(!Model.Player(player).isClaimedRegion())
		    		{
			        	Message.sendActionBar(player, "actionbar.interact.wolf.owner.item_other");
		    		}
		    		else if(!Model.Player(player).isInsideOwnRegion())
		    		{
			        	Message.sendActionBar(player, "actionbar.interact.wolf.owner.outregion");
		    			//Model.Player(player).getWolf().setSitting(wolfentity, true);
		    			//Model.Player(player).getWolf().initSitting(true);
		    			wolfentity.setSitting(true);
		    			e.setCancelled(true);
		    			return;
		    		}
		    	}
		    	
		    	Model.Player(player).getWolf().setSitting(wolfentity, !wolfentity.isSitting());
		    	e.setCancelled(true);
		    	//wolfentity.setSitting(!wolfentity.isSitting());
		    	//Model.Player(player).getWolf().setInfoSitting(wolfentity.isSitting());
		    	//e.setCancelled(true);
		    	return;
    		}
//    		else
//    			e.setCancelled(true);
    	}
    	
    	if(Model.Wolves.existWolf(e.getRightClicked().getUniqueId()))
    	{
    		Message.sendActionBar(player, "actionbar.interact.wolf.other.use");
    		e.setCancelled(true);
    	}
    }
    
    @EventHandler
    public void onAsyncPlayerChatEvent(final AsyncPlayerChatEvent e) {
    	//if(Config.debug) Bukkit.broadcastMessage("onAsyncPlayerChatEvent");
    	
    	Player player = e.getPlayer();
        if(!Model.Players.isExist(player))
        	return;
        
    	if(!Model.Player(player).onAsyncPlayerChat(e))
    		e.setCancelled(true);
    }
    
    @EventHandler
    public void onInventoryClickEvent(final InventoryClickEvent e) {
    	//if(Config.debug) Bukkit.broadcastMessage("InventoryClickEvent");
    	
    	HumanEntity humanentity = e.getWhoClicked();
    	if(!(humanentity instanceof Player))
    		return;
    	
    	Player player = (Player)humanentity;
        if(!Model.Players.isExist(player))
        	return;
        
    	Model.Player(player).getWolfMenu().onInventoryClick(e, player);
    }
    
    @EventHandler
    public void onInventoryCloseEvent(final InventoryCloseEvent e) {
    	//if(Config.debug) Bukkit.broadcastMessage("InventoryCloseEvent");
    	
    	HumanEntity humanentity = e.getPlayer();
    	if(!(humanentity instanceof Player))
    		return;
    	
    	Player player = (Player)humanentity;
    	
        if(!Model.Players.isExist(player))
        	return;
        
    	if(!Model.Player(player).isClaimedRegion() || Model.Player(player).isInsideOwnRegion())
    		Model.Player(player).getWolfMenu().onInventoryClose(e, player);
    }
    
    @EventHandler
    public void onHangingBreakByEntityEvent(final HangingBreakByEntityEvent e) {
    	//if(Config.debug) Bukkit.broadcastMessage("HangingBreakByEntityEvent");
    	
    }
}

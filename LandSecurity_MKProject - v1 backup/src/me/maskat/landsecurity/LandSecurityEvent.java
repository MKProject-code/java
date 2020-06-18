package me.maskat.landsecurity;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
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

import com.google.common.collect.ImmutableMap;

import fr.xephi.authme.events.LoginEvent;
import me.maskat.landsecurity.inventories.WolfMenu;
import me.maskat.landsecurity.models.Model;
import me.maskat.landsecurity.models.Model.Players;

public class LandSecurityEvent implements Listener {
	
	//	e.setCancelled(true);
	
	//AuthMeApi authmeApi = AuthMeApi.getInstance();
	
	@EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) throws SQLException {
		if(Config.debug) Bukkit.broadcastMessage("player join!");
		//if(Config.debug) Bukkit.broadcastMessage("event join");
		//Player player = event.getPlayer();
		//if(Config.debug) Bukkit.broadcastMessage("addPlayer");
		//int userid = Model.Players.addPlayer(player);
    }
	
	@EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) throws SQLException {
		if(Config.debug) Bukkit.broadcastMessage("player quit!");
		
		Player player = event.getPlayer();
		
		Model.Players.removePlayer(player);
	}
	
	@EventHandler
	public void playerLoginEvent(LoginEvent event) throws SQLException {
		if(Config.debug) Bukkit.broadcastMessage("authme: logged!");
		
		Player player = event.getPlayer();
		
		int userid = Model.Players.addPlayer(player);
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
        if (e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockZ() == e.getFrom().getBlockZ()) return; //The player hasn't moved
        
        Player player = e.getPlayer();
        if(!Model.Players.isExist(player))
        	return;
        
        int regionidInside = Model.Regions.getRegionId(e.getTo());
        
        if(Model.Regions().containsKey(regionidInside)) {
        	if(!Model.Player(player).isInsideAnyRegion())
        	{
	        	int userid = Model.Player(player).getUserId();
	        	if(Model.Player(player).getOwnRegionId() == regionidInside)
	        	{
	        		Model.Player(player).setInsideOwnRegion();
	        		
	        		if(Model.Player(player).existWolf())
	        			Model.Player(player).fixWolfSitting();
	        			//Model.Player(player).getWolfEntity().setSitting(false);
	        		
		        	Message.sendActionBar(player, "actionbar.move.enter.owner", ImmutableMap.of(
		        		    "region_name", Model.Region(regionidInside).getName()
		        		));
	        	}
	        	else
	        	{
//		        	Message.sendActionBar(player, "actionbar.move.enter.other", ImmutableMap.of(
//		        		    "region_name", Model.Region(regionidInside).getName()
//		        		));
		        	
		        	//TODO: .isEnemy(player) itp...
		        	if(!Model.Region(regionidInside).isFriend(player))
		        	{
		        		// atakuje jestli nie jest przyjacielem
		        		Model.Region(regionidInside).getWolf().setTarget(player, regionidInside);
		        	}
	        	}
	        	Model.Player(player).setInsideRegionId(regionidInside);
        	}
        }
        else
        {
        	if(Model.Player(player).isInsideAnyRegion())
        	{
        		int regionidLeave = Model.Player(player).getInsideRegionId();
        		if(Model.Player(player).getOwnRegionId() == regionidLeave)
        		{
		        	Message.sendActionBar(player, "actionbar.move.leave.owner", ImmutableMap.of(
		        		    "region_name", Model.Region(regionidLeave).getName()
		        		));
        		}
        		else
        		{
//		        	Message.sendActionBar(player, "actionbar.move.leave.other", ImmutableMap.of(
//		        		    "region_name", Model.Region(regionidLeave).getName()
//		        		));
        		}
        		Model.Player(player).unsetInsideOwnRegion();
            	Model.Player(player).unsetInsideRegionId();
        		if(Model.Player(player).getOwnRegionId() == regionidLeave && Model.Player(player).existWolf())
        			Model.Player(player).getWolfEntity().setSitting(true);
        	}
        }
    }
	
	@EventHandler
	public void onEntityTargetEvent(final EntityTargetEvent e) {
		if(Config.debug) Bukkit.broadcastMessage("E: " + e.getEntity());
		if(Config.debug) Bukkit.broadcastMessage("T: " + e.getTarget());
	}
    
    @EventHandler
    public void onEntityDamageByEntityEvent(final EntityDamageByEntityEvent e) {
    	if(Config.debug) Bukkit.broadcastMessage("EntityDamageByEntityEvent");
    	
    	if(Model.Wolves.existWolf(e.getEntity().getUniqueId()))
    		e.setCancelled(true);
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
    	if(Config.debug) Bukkit.broadcastMessage("onEntityTeleportEvent");
    	
    	int wolfid = Model.Wolves.getWolfId(e.getEntity().getUniqueId());
		if(wolfid > 0)
		{
			if(Config.debug) Bukkit.broadcastMessage("WOLF EXIST");
			if(Config.debug) Bukkit.broadcastMessage(e.getTo().getWorld().toString());
			if(Config.debug) Bukkit.broadcastMessage(Bukkit.getServer().getWorld(Config.allowWorld).toString());
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
    public void onEntityDamageEvent(final EntityDamageEvent e) {
    	if(Config.debug) Bukkit.broadcastMessage("EntityDamageEvent");
    	
    	if(Model.Wolves.existWolf(e.getEntity().getUniqueId()))
    		e.setCancelled(true);
    }
    
    @EventHandler
    public void onFoodLevelChangeEvent(final FoodLevelChangeEvent e) {
    	if(Config.debug) Bukkit.broadcastMessage("FoodLevelChangeEvent");
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
    	if(Config.debug) Bukkit.broadcastMessage("LeavesDecayEvent");
    }
    
    @EventHandler
    public void onBlockExplodeEvent(final BlockExplodeEvent e) {
    	if(Config.debug) Bukkit.broadcastMessage("BlockExplodeEvent");
    }
    
    @EventHandler
    public void onEntityExplodeEvent(final EntityExplodeEvent e) {
    	if(Config.debug) Bukkit.broadcastMessage("EntityExplodeEvent");
    }
    
    @EventHandler
    public void onBlockPlaceEvent(final BlockPlaceEvent e) {
        //if(e.getEnity() instanceof Player)
        int regionid = Model.Regions.getRegionId(e.getBlock().getLocation());
        if(!Model.Regions().containsKey(regionid))
        	return;
        
//        String regionName = Model.Region(regionid).getName();
//        Player player = e.getPlayer();
//        
//        if(realRegionName != null && !region.isOwner(player) && !region.isMemeber(player)) {
//        	Message.sendActionBar(player, "actionbar.deny.place", ImmutableMap.of(
//        		    "region_name", regionName
//        		));
//        	e.setCancelled(true);
//        }
    }
    
    @EventHandler
    public void onBlockBreakEvent(final BlockBreakEvent e) {
        //if(e.getEnity() instanceof Player)
        int regionid = Model.Regions.getRegionId(e.getBlock().getLocation());
        if(!Model.Regions().containsKey(regionid))
        	return;
        
//        String realRegionName = region.getName();
//        Player player = e.getPlayer();
//        
//        if(realRegionName != null && !region.isOwner(player) && !region.isMemeber(player)) {
//        	Message.sendActionBar(player, "actionbar.deny.break", ImmutableMap.of(
//        		    "region_name", realRegionName
//        		));
//        	e.setCancelled(true);
//        }
    }
    
    @EventHandler
    public void onEntityPickupItemEvent(final EntityPickupItemEvent e) {
    	if(Config.debug) Bukkit.broadcastMessage("EntityPickupItemEvent");
    }
    
    @EventHandler
    public void onPlayerDropItemEvent(final PlayerDropItemEvent e) {
    	if(Config.debug) Bukkit.broadcastMessage("PlayerDropItemEvent");
    }
    
    @EventHandler
    public void onPlayerInteractEvent(final PlayerInteractEvent e) {
    	if(Config.debug) Bukkit.broadcastMessage("PlayerInteractEvent");
    	
    	Player player = e.getPlayer();
        if(!Model.Players.isExist(player))
        	return;
        
    	if((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR) && Model.Player(player).existOwnRegion())
    		Model.Player(player).getOwnRegion().hideBorders();
    	//WolfGuard.summonWolf(e.getPlayer());
    }
    
    @EventHandler
    public void onPlayerInteractEntityEvent(final PlayerInteractEntityEvent e) {
    	if(Config.debug) Bukkit.broadcastMessage("PlayerInteractEntityEvent");
    	
    	Player player = e.getPlayer();
        if(!Model.Players.isExist(player))
        	return;
        
    	if(Model.Player(player).existOwnRegion())
    		Model.Player(player).getOwnRegion().hideBorders();
    	
    	if(e.getRightClicked().getWorld() != Bukkit.getServer().getWorld(Config.allowWorld))
    		return;
    	
    	if(!(e.getRightClicked() instanceof Wolf))
    		return;
    	
    	if(Model.Player(player).existWolf()) {
    		if(e.getRightClicked().getUniqueId().equals(Model.Player(player).getWolf().getEnityUUID()))
    		{
    			Wolf wolfentity = Model.Player(player).getWolfEntity();
		    	if(player.getInventory().getItemInMainHand().getType() == Material.BONE) {
		        	Message.sendActionBar(player, "actionbar.interact.wolf.owner.item_bone", ImmutableMap.of(
		        		    "wolf_name", Model.Player(player).getWolf().getName()
		        		));
		    		
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
		    		if(!Model.Player(player).isClaimedRegion())
		    		{
			        	Message.sendActionBar(player, "actionbar.interact.wolf.owner.item_other", ImmutableMap.of(
			        		    "wolf_name", Model.Player(player).getWolf().getName()
			        		));
		    		}
		    		else if(!Model.Player(player).isInsideOwnRegion())
		    		{
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
    		}
    		else
    			e.setCancelled(true);
    	}
    	else if(Model.Wolves.existWolf(e.getRightClicked().getUniqueId()))
    	{
    		e.setCancelled(true);
    	}
    }
    
    @EventHandler
    public void onAsyncPlayerChatEvent(final AsyncPlayerChatEvent e) {
    	if(Config.debug) Bukkit.broadcastMessage("onAsyncPlayerChatEvent");
    	
    	Player player = e.getPlayer();
        if(!Model.Players.isExist(player))
        	return;
        
    	if(!Model.Player(player).onAsyncPlayerChat(e))
    		e.setCancelled(true);
    }
    
    @EventHandler
    public void onInventoryClickEvent(final InventoryClickEvent e) {
    	if(Config.debug) Bukkit.broadcastMessage("InventoryClickEvent");
    	
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
    	if(Config.debug) Bukkit.broadcastMessage("InventoryCloseEvent");
    	
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
    	if(Config.debug) Bukkit.broadcastMessage("HangingBreakByEntityEvent");
    	
    }
}

package me.maskat.wolfsecurity.models;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.google.common.collect.ImmutableMap;

import me.maskat.wolfsecurity.Function;
import me.maskat.wolfsecurity.Message;
import me.maskat.wolfsecurity.Plugin;
import me.maskat.wolfsecurity.inventories.WolfMenu;
import me.maskat.wolfsecurity.inventories.WolfMenuMain;
import net.milkbowl.vault.economy.EconomyResponse;

public class ModelPlayer {
	private Player player = null;
	private int userid = -1;
	private int ownregionid = -1;
	private int ownwolfid = -1;
    //private boolean insideOwnRegion = false;
    private List<Integer> insideRegionsId = new ArrayList<>();
    private WolfMenu wolfMenu = null;
    private Object listenChat = null;
    //private Object listenBlockBreak = null;
    //private Object listenBlockPlace = null;
    //private Object listenPlayerInteract = null;
    private Object listenLogBlock = null;
    private boolean playerInitialized = false;

	public void setPlayer(Player p) { player = p; wolfMenu = new WolfMenu(player); }
	public void setUserId(int i) { userid = i; }
	public void setOwnRegionId(int i) { ownregionid = i; }
	public void setOwnWolfId(int i) { ownwolfid = i; }
	//public void setInsideOwnRegion() { insideOwnRegion = true; }
	//public void unsetInsideOwnRegion() { insideOwnRegion = false; }
	public void setInsideRegionId(int i) { insideRegionsId.add(i); }
	public void unsetInsideRegionId(int i) { insideRegionsId.remove((Object)i); }
	public void unsetAllInsideRegions() { insideRegionsId = new ArrayList<>(); }
	//public void setWolfMenu(WolfMenu wm) { wolfMenu = wm; }
	
	public Player getPlayer() { return player; }
	public int getUserId() { return userid; }
	public int getOwnRegionId() { return ownregionid; }
	public int getOwnWolfId() { return ownwolfid; }
	public boolean isInsideOwnRegion() {
		//return insideOwnRegion;
		return insideRegionsId.contains((Object)ownregionid);
		}
	public List<Integer> getInsideRegionsId() { return insideRegionsId; }
	public WolfMenu getWolfMenu() { return wolfMenu; }
	
	public void LeaveAllRegions() {
		for(int i=0;i<insideRegionsId.size();i++)
			LeaveRegionId(insideRegionsId.get(i), false);
	}
	
	public void LeaveRegionId(int regionid, boolean checkIsAfterUnclaim) {
		
		//if(Config.debug) Bukkit.broadcastMessage("[LeaveRegionId] regionid:"+regionid+" == ownregionid:"+ownregionid);
		if(regionid == ownregionid)
		{
			if(!checkIsAfterUnclaim && existWolf())
				getWolfEntity().setSitting(true);
			
        	Message.sendActionBar(player, "actionbar.move.leave.owner", ImmutableMap.of(
        		    "region_name", Model.Region(regionid).getName(),
        		    "wolf_name", Model.Region(regionid).getWolf().getName()
        		));
        	//insideOwnRegion = false;
		}
		else
		{
//        	Message.sendActionBar(player, "actionbar.move.leave.other", ImmutableMap.of(
//        		    "region_name", Model.Region(last_regionidInside).getName(),
//					"wolf_name", Model.Region(regionid).getWolf().getName()
//        		));
		}
		if(insideRegionsId.contains((Object)regionid))
			insideRegionsId.remove((Object)regionid);
	}
	
	public void EntryRegionId(int regionid, boolean checkIsAfterClaim) {
		//if(Config.debug) Bukkit.broadcastMessage("[EntryRegionId] regionid:"+regionid+" == ownregionid:"+ownregionid);
		
		if(insideRegionsId.contains((Object)regionid))
			return;
		
		insideRegionsId.add(regionid);

		if(regionid == ownregionid)
		{
    		if(!checkIsAfterClaim && existWolf())
    			fixWolfSitting();
    		
        	Message.sendActionBar(player, "actionbar.move.enter.owner", ImmutableMap.of(
        		    "region_name", Model.Region(regionid).getName(),
        		    "wolf_name", Model.Region(regionid).getWolf().getName()
        		));
		}
		else
		{
        	if(player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR && !Model.Region(regionid).isFriend(player) && !Model.Region(regionid).isFamily(player))
        	{
        		// atakuje jestli nie jest przyjacielem ani rodzina
        		Model.Region(regionid).getWolf().setTarget(player, regionid);
        	}
        	
//		        	Message.sendActionBar(player, "actionbar.move.enter.other", ImmutableMap.of(
//        		    		"region_name", Model.Region(regionidInside).getName(),
//		    				"wolf_name", Model.Region(regionid).getWolf().getName()
//        				));
		}
	}
	
	public boolean isInsideAnyRegion() {
		for(int regionid : insideRegionsId)
		{
			if(Model.Regions().containsKey(regionid))
				return true;
			else
				insideRegionsId.remove((Object)regionid);
		}
		return false;
	}
	
	public int addWolf() {
		ownwolfid = Model.Wolves.addWolf(player);
		Model.User(userid).setAssignedWolfId(ownwolfid);
		return ownwolfid;
	}
	
	public int addRegion() {
		ownregionid = Model.Regions.addRegion(player);
		return ownregionid;
	}
	
	public boolean spawnBordersOwnRegion() {
		return Model.Region(ownregionid).showBorders(player);
	}
	
	public ModelRegion getOwnRegion() {
		return Model.Region(ownregionid);
	}
	
	public int claimRegion(Location loc, int radius) {
		if(Model.Region(ownregionid).claimRegion(player, loc, radius))
		{
			Function.checkPlayerInsideRegions(player, player.getLocation(), true);
			// TODO: FIX !!! this.! jak zajmuje region, sprawdz czy w nim jest gracz -> DONE! chyba ok? :)
			//insideOwnRegion = true;
			//insideRegionId = ownregionid;
			
//			int regionidInside = Model.Regions.getRegionId(player.getLocation());
//			if(Model.Regions().containsKey(regionidInside)) {
//	        	if(!isInsideAnyRegion())
//	        	{
//		        	if(getOwnRegionId() == regionidInside)
//		        	{
//		        		setInsideOwnRegion();
//		        	}
//		        	setInsideRegionId(regionidInside);
//	        	}
//	        }
//	        else
//	        {
//	        	if(isInsideAnyRegion())
//	        	{
//	        		int regionidLeave = Model.Player(player).getInsideRegionId();
//	        		unsetInsideOwnRegion();
//	            	unsetInsideRegionId();
//	        		if(getOwnRegionId() == regionidLeave && existWolf())
//	        			getWolfEntity().setSitting(true);
//	        	}
//	        }
			return ownregionid;
		}
		else
		{
			return -1;
		}
	}
	
	public int unclaimRegion() {
		
		// TODO: FIX !!! this.! jak zajmuje region, sprawdz czy w nim jest gracz -> DONE! chyba ok? :)
		//insideOwnRegion = false;
		//insideRegionId = -1;
		Model.Region(ownregionid).unclaimRegion();
		
		Function.checkPlayerInsideRegions(player, player.getLocation(), true);
		
//		int regionidInside = Model.Regions.getRegionId(player.getLocation());
//		if(Model.Regions().containsKey(regionidInside)) {
//        	if(!isInsideAnyRegion())
//        	{
////	        	if(getOwnRegionId() == regionidInside)
////	        	{
////	        		setInsideOwnRegion();
////	        	}
//	        	setInsideRegionId(regionidInside);
//        	}
//        }
//        else
//        {
//        	if(isInsideAnyRegion())
//        	{
//        		int regionidLeave = getInsideRegionId();
//        		unsetInsideOwnRegion();
//            	unsetInsideRegionId();
//        		if(getOwnRegionId() == regionidLeave && existWolf())
//        			getWolfEntity().setSitting(true);
//        	}
//        }
		return ownregionid;
	}
	
	public boolean isClaimedRegion() {
		
		return (existOwnRegion() && Model.Region(ownregionid).isClaimed());
	}
	
	public boolean existOwnRegion() {
		return Model.Regions().containsKey(ownregionid);
	}
	
	public boolean existWolf()
	{
		//if(Config.debug) Bukkit.broadcastMessage("[LeaveRegionId] ownwolfid:"+ownwolfid);
		return Model.Wolves().containsKey(ownwolfid) ? true : false;
	}
	
	public ModelWolf getWolf() {
		return Model.Wolf(ownwolfid);
	}
	
	public Wolf getWolfEntity() {
		return (Wolf)Model.Wolf(ownwolfid).getEntityWolf();
	}

	public void setListenChat(Object lc) { listenChat = lc; }
//	public void setListenBlockBreak(Object lbb) { listenBlockBreak = lbb; }
//	public void setListenBlockPlace(Object lbp) { listenBlockPlace = lbp; }
//	public void setListenPlayerInteract(Object lpi) { listenPlayerInteract = lpi; }
	public void setListenLogBlock(Object llb) { listenLogBlock = llb; }
	
	public void unsetListens(Player player)
	{
		if(listenChat != null)
		{
			//if(Config.debug) Bukkit.broadcastMessage("BROADCAST listen,,, cancel");
			AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(false, player, null, null);
			event.setMessage("cancel");
			onAsyncPlayerChat(event);
			listenChat = null;
		}
		
		if(listenLogBlock != null)
		{
			Message.sendMessage(player, "inventory.wolfmenu.region.coreprotectinfo.done.canceled");
			listenLogBlock = null;
		}
	}
	
	//public void unsetListenChat() { listenChat = null; }
	public boolean onAsyncPlayerChat(AsyncPlayerChatEvent e) {
		if(listenChat == null) return true;
		
		if(listenChat instanceof WolfMenuMain)
		{
			if(!((WolfMenuMain) listenChat).onAsyncPlayerChat(e))
				return false;
		}
		
		listenChat = null;
		return false;
	}
	
	public boolean onBlockPlace(BlockPlaceEvent e) {
		if(listenLogBlock == null) return true;
		
		if(listenLogBlock instanceof WolfMenuMain)
		{
			if(!((WolfMenuMain) listenLogBlock).onLogBlock(e.getPlayer(), e.getBlock()))
				return false;
		}
		
		listenLogBlock = null;
		return false;
	}
	
	public boolean onBlockBreak(BlockBreakEvent e) {
		if(listenLogBlock == null) return true;
		
		if(listenLogBlock instanceof WolfMenuMain)
		{
			if(!((WolfMenuMain) listenLogBlock).onLogBlock(e.getPlayer(), e.getBlock()))
				return false;
		}
		
		listenLogBlock = null;
		return false;
	}
	
	public boolean onPlayerInteract(PlayerInteractEvent e) {
		if(listenLogBlock == null) return true;
		
		if(listenLogBlock instanceof WolfMenuMain)
		{
			return ((WolfMenuMain) listenLogBlock).onLogBlock(e);
			//if(!((WolfMenuMain) listenLogBlock).onLogBlock(e.getPlayer(), e.getClickedBlock(), e.getAction()))
				//return false;
		}
		
		listenLogBlock = null;
		return false;
	}

	
//	public boolean onBlockPlace(BlockPlaceEvent e) {
//		if(listenBlockPlace == null) return true;
//		
//		if(listenBlockPlace instanceof WolfMenuMain)
//		{
//			if(!((WolfMenuMain) listenBlockPlace).onBlockPlace(e))
//				return false;
//		}
//		
//		listenBlockPlace = null;
//		return false;
//	}
//	
//	public boolean onBlockBreak(BlockBreakEvent e) {
//		if(listenBlockBreak == null) return true;
//		
//		if(listenBlockBreak instanceof WolfMenuMain)
//		{
//			if(!((WolfMenuMain) listenBlockBreak).onBlockBreak(e))
//				return false;
//		}
//		
//		listenBlockBreak = null;
//		return false;
//	}
//
//	public boolean onPlayerInteract(PlayerInteractEvent e) {
//		if(listenPlayerInteract == null) return true;
//		
//		if(listenPlayerInteract instanceof WolfMenuMain)
//		{
//			if(!((WolfMenuMain) listenPlayerInteract).onPlayerInteract(e))
//				return false;
//		}
//		
//		listenPlayerInteract = null;
//		return false;
//	}
	
	
	public void fixWolfSitting() {
		if(Model.Region(ownregionid).isInRegion(getWolfEntity().getLocation()))
			getWolf().fixSitting();
		else
			getWolf().setSitting(getWolfEntity(), false);
	}
	
	public void fixWolfEnity() {
		if(existWolf())
			getWolf().fixEnity(player);
	}
	
	public boolean isInitialized() { 
		return playerInitialized;
	}
	public void setInitialized(boolean b) {
		playerInitialized = b;
	}
	
	public int getEconomy() {
		return (int)Plugin.plugin.economy.getBalance(player);
	}
	public EconomyResponse withdrawEconomy(double value) {
		return Plugin.plugin.economy.withdrawPlayer(player, value);
	}
	public boolean wolfLevelUp() {
		if(getWolf().levelUp(player))
		{
			if(this.isClaimedRegion())
			{
				if(claimRegion(this.getOwnRegion().getCenterLocation(), this.getWolf().getProtectedRadius()) > 0)
					Message.sendMessage(player, "inventory.wolfmenu.wolf.levelup.done.region_resize");
				else
					Message.sendMessage(player, "inventory.wolfmenu.wolf.levelup.error.region_resize_nearotherregion");
			}
			return true;
		}
		else
		{
			return false;
		}
	}






}
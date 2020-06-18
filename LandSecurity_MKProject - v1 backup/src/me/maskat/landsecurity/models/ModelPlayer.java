package me.maskat.landsecurity.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.google.common.collect.ImmutableMap;

import me.maskat.landsecurity.Config;
import me.maskat.landsecurity.LandSecurity;
import me.maskat.landsecurity.Message;
import me.maskat.landsecurity.inventories.WolfMenu;
import me.maskat.landsecurity.inventories.WolfMenuMain;
import net.milkbowl.vault.economy.EconomyResponse;

public class ModelPlayer {
	private Player player = null;
	private int userid = -1;
	private int ownregionid = -1;
	private int ownwolfid = -1;
    private boolean insideOwnRegion = false;
    private int insideRegionId = -1;
    private WolfMenu wolfMenu = null;
    private Object listenChat = null;

	public void setPlayer(Player p) { player = p; wolfMenu = new WolfMenu(player); }
	public void setUserId(int i) { userid = i; }
	public void setOwnRegionId(int i) { ownregionid = i; }
	public void setOwnWolfId(int i) { ownwolfid = i; }
	public void setInsideOwnRegion() { insideOwnRegion = true; }
	public void unsetInsideOwnRegion() { insideOwnRegion = false; }
	public void setInsideRegionId(int i) { insideRegionId = i; }
	public void unsetInsideRegionId() { insideRegionId = -1; }
	//public void setWolfMenu(WolfMenu wm) { wolfMenu = wm; }
	
	public Player getPlayer() { return player; }
	public int getUserId() { return userid; }
	public int getOwnRegionId() { return ownregionid; }
	public int getOwnWolfId() { return ownwolfid; }
	public boolean isInsideOwnRegion() { return insideOwnRegion; }
	public int getInsideRegionId() { return insideRegionId; }
	public WolfMenu getWolfMenu() { return wolfMenu; }
	
	public boolean isInsideAnyRegion() { return (Model.Regions().containsKey(insideRegionId)) ? true : false; }
	
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
		if(Model.Region(ownregionid).claimRegion(loc, radius))
		{
			// TODO: FIX !!! this.! jak zajmuje region, sprawdz czy w nim jest gracz -> DONE! chyba ok? :)
			//insideOwnRegion = true;
			//insideRegionId = ownregionid;
			int regionidInside = Model.Regions.getRegionId(player.getLocation());
			if(Model.Regions().containsKey(regionidInside)) {
	        	if(!isInsideAnyRegion())
	        	{
		        	if(getOwnRegionId() == regionidInside)
		        	{
		        		setInsideOwnRegion();
		        	}
		        	setInsideRegionId(regionidInside);
	        	}
	        }
	        else
	        {
	        	if(isInsideAnyRegion())
	        	{
	        		int regionidLeave = Model.Player(player).getInsideRegionId();
	        		unsetInsideOwnRegion();
	            	unsetInsideRegionId();
	        		if(getOwnRegionId() == regionidLeave && existWolf())
	        			getWolfEntity().setSitting(true);
	        	}
	        }
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
		
		int regionidInside = Model.Regions.getRegionId(player.getLocation());
		if(Model.Regions().containsKey(regionidInside)) {
        	if(!isInsideAnyRegion())
        	{
//	        	if(getOwnRegionId() == regionidInside)
//	        	{
//	        		setInsideOwnRegion();
//	        	}
	        	setInsideRegionId(regionidInside);
        	}
        }
        else
        {
        	if(isInsideAnyRegion())
        	{
        		int regionidLeave = getInsideRegionId();
        		unsetInsideOwnRegion();
            	unsetInsideRegionId();
        		if(getOwnRegionId() == regionidLeave && existWolf())
        			getWolfEntity().setSitting(true);
        	}
        }
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
		return Model.Wolves().containsKey(ownwolfid) ? true : false;
	}
	
	public ModelWolf getWolf() {
		return Model.Wolf(ownwolfid);
	}
	
	public Wolf getWolfEntity() {
		return (Wolf)Model.Wolf(ownwolfid).getEntityWolf();
	}

	public void setListenChat(Object lc) { listenChat = lc; }
	public void unsetListenChat(Player player)
	{
		if(listenChat != null)
		{
			if(Config.debug) Bukkit.broadcastMessage("BROADCAST listenChat,,,");
			AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(false, player, null, null);
			event.setMessage("cancel");
			onAsyncPlayerChat(event);
			listenChat = null;
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
	public double getEconomy() {
		return LandSecurity.plugin.economy.getBalance(player);
	}
	public EconomyResponse withdrawEconomy(double value) {
		return LandSecurity.plugin.economy.withdrawPlayer(player, value);
	}
	public boolean wolfLevelUp() {
		return getWolf().levelUp(player);
	}

}
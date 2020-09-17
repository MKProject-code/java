package me.maskat.compasspoint.models;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.maskat.compasspoint.enums.NavLocation;
import me.maskat.compasspoint.inventories.InventoryMenu_old;
import me.maskat.wolfsecurity.api.WolfSecurityApi;
import mkproject.maskat.Papi.Papi;

public class ModelPlayer {
	private Player player = null;
	private InventoryMenu_old inventoryMenu = null;
	private NavLocation navLocation = NavLocation.BED;
	private Player navTargetPlayer = null;
	private boolean navCompassWorking = false;
//	private Wolf wolfEntity = null;
//	private Location lastTargetLocation = null;
	Wolf wolfEntity = null;
	
	
	public ModelPlayer(Player p) {
		player = p;
	}
	
//	public boolean existWolfEntity() {
//		return (wolfEntity == null) ? false:true;
//	}
	
//	public void updateWolfEntityAPI() {
//		//wolfLocation = WolfSecurityApi.getWolfLocation(player);
//		wolfEntity = WolfSecurityApi.getWolfEntity(player);
//	}
	
	public Wolf getWolfEntity() {
		return wolfEntity;
	}
	public void setWolfEntity(Wolf wolfEntity) {
		this.wolfEntity = wolfEntity;
	}
	public Wolf getWolfEntityAPI() {
		return WolfSecurityApi.getWolfEntity(player);
	}

	public void setInventoryMenu() {
		inventoryMenu = new InventoryMenu_old();
	}
	
	public void removeInventoryMenu() {
    	inventoryMenu = null;
	}
	
	private boolean existInventoryMenu() {
		if(inventoryMenu != null)
			return true;
		else
			return false;
	}
	
	public InventoryMenu_old getInventoryMenu() {
		return inventoryMenu;
	}
	
	public void initInventoryMenu() {
		inventoryMenu.initAllGui(player);
	}
	public void openMainMenu() {
		inventoryMenu.openMainMenu(player);
	}

	public void onInventoryClick(InventoryClickEvent e) {
    	if(Model.Player(player).existInventoryMenu())
    		inventoryMenu.onInventoryClick(e, player);
	}
	
	public NavLocation getNavLocation() {
		return navLocation;
	}
	
	public Player getNavTargetPlayer() {
		return navTargetPlayer;
	}
	
	public void setNavLocation(NavLocation navlocation) {
		navLocation = navlocation;
	}
	
	public void setNavLocation(NavLocation navlocation, Player navtargetplayer) {
		navLocation = navlocation;
		navTargetPlayer = navtargetplayer;
	}
	
	public boolean existBedSpawn() {
		Location bedlocation = player.getBedSpawnLocation();
		if (bedlocation == null || bedlocation.getWorld() != player.getWorld())
			return  false;
		return true;
	}
	
	public Location getBedSpawnLocation() {
		return player.getBedSpawnLocation();
	}

	public void setComapssWorking(boolean b) {
		navCompassWorking = b;
	}
	
	public boolean getCompassWorking() {
		return navCompassWorking;
	}
	
	public String getNavLocationInfo() {
		if(navCompassWorking)
		{
			if(navLocation == NavLocation.PLAYER && navTargetPlayer != null && navTargetPlayer.isOnline())
			{
				return "&bNawiguję do gracza \n&e" + navTargetPlayer.getName();
			}
			else if(navLocation == NavLocation.BED)
			{
				return "&bNawiguję do twojego łóżka";
			}
			else if(navLocation == NavLocation.WOLF)
			{
				return "&bNawiguję do twojego wilka";
			}
			else if(navLocation == NavLocation.PLAYERSPAWN)
			{
				return "&bNawiguję do twojego punktu startowego";
			}
		}
		return "&5Kompas rozregulowany";
	}

	public boolean existWolfAPI() {
		return WolfSecurityApi.existWolf(player);
	}

	public boolean existPlayerSpawnPoint() {
		return Papi.Model.getPlayer(player).isPlayerSpawnGenerated();
	}
//
//	public void setLastTargetLocation(Location currentTargetLocation) {
//		this.lastTargetLocation = currentTargetLocation;
//	}
//	
//	public Location getLastTargetLocation() {
//		return this.lastTargetLocation;
//	}
}
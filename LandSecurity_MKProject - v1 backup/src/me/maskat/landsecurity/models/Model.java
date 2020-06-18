package me.maskat.landsecurity.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.maskat.landsecurity.Config;
import me.maskat.landsecurity.LandSecurity;
import me.maskat.landsecurity.WolfGuard;
import me.maskat.mysql.SQL;

public class Model {
//	public static Map<String, ModelRegion> mapRegions() {
//		return LandSecurity.plugin.mapRegions;
//	}
//	
//	public static Map<Integer, ModelUser> mapUsers() {
//		return LandSecurity.plugin.mapUsers;
//	}
//	
//	public static Map<Integer, ModelWolf> mapWolves() {
//		return LandSecurity.plugin.mapWolves;
//	}
    
	/////////////////////////////////////////////////////////
	
	public static ModelRegion Region(int regionid) {
		return Regions().get(regionid);
	}
	
	public static ModelUser User(int userid) {
		return Users().get(userid);
	}
	
	public static ModelPlayer Player(Player player) {
		return Players().get(player);
	}

	public static ModelWolf Wolf(int wolfid) {
		return Wolves().get(wolfid);
	}
	
	/////////////////////////////////////////////////////////
	
	public static Map<Integer, ModelRegion> Regions() { return LandSecurity.plugin.mapRegions; }
	
    public static class Regions {
    	
    	//public static Map<Integer, ModelRegion> getMap() { return LandSecurity.plugin.mapRegions; }
    	
    	//private static ModelRegion addModel(int regionid, String regionname, int ownerid) { return LandSecurity.plugin.mapRegions.putIfAbsent(regionid, new ModelRegion(regionid, regionname, new int[] { ownerid })); }
    	private static ModelRegion addModel(int regionid, String regionname, int ownerid) { return Regions().putIfAbsent(regionid, new ModelRegion(regionid, regionname, new ArrayList<Integer>(Arrays.asList(ownerid)))); }
    	
//    	public static ModelRegion getRegion(Location loc) {
//    		for (Map.Entry<Integer, ModelRegion> region : LandSecurity.plugin.mapRegions.entrySet()) {
//    			if(region.getValue().isInRegion(loc))
//    				return region.getValue();
//    		}
//    		return null;
//    	}
//    	
//    	public static ModelRegion getRegion(Map.Entry<Integer, ModelUser> user) {
//    		for (Map.Entry<Integer, ModelRegion> region : Model.Regions.getMap().entrySet()) {
//    			if(region.getValue().isOwner(user.getKey()))
//    				return region.getValue();
//    		}
//    		return null;
//    	}
    	
    	public static int getRegionId(Location loc) {
    		for (Map.Entry<Integer, ModelRegion> region : Regions().entrySet()) {
    			if(region.getValue().isInRegion(loc))
    				return region.getKey();
    		}
    		return -1;
    	}
    	
		public static int addRegion(Player player) {
			String regionname = "Terytorium gracza "+player.getName();
			int ownerid = Model.Player(player).getUserId();
			
			int regionid = SQL.insertData_getId("regionname, owners", "'"+regionname+"', '["+ownerid+"]'", Config.databaseTableRegions);
			addModel(regionid, regionname, ownerid);
			
			return regionid;
		}
    }
	/////////////////////////////////////////////////////////
    
    public static Map<Integer, ModelUser> Users() { return LandSecurity.plugin.mapUsers; }
    
    public static class Users {
    	
//   	public static Map<Integer, ModelUser> getMap() { return LandSecurity.plugin.mapUsers; }
//    	public static ModelUser getModel(int userid) { return LandSecurity.plugin.mapUsers.get(userid); }
    	public static ModelUser addModel(int userid) { return Users().putIfAbsent(userid, new ModelUser()); }
    	
		public static int getUserId(String playerName) {
			for (Map.Entry<Integer, ModelUser> user : Users().entrySet()) {
				if(user.getValue().getName().equalsIgnoreCase(playerName))
					return user.getKey();
			}
			return -1;
		}
		
		public static int getUserId(Player player) throws SQLException {
			if(Config.debug) Bukkit.broadcastMessage("getUserID");
			String playerName = player.getName();
			for (Map.Entry<Integer, ModelUser> user : Users().entrySet()) {
				{
					if(Config.debug) Bukkit.broadcastMessage("getUserID now finding: " + user.getValue().getName() + " == " +playerName);
					if(user.getValue().getName().equalsIgnoreCase(playerName))
						return user.getKey();
				}
			}
			if(Config.debug) Bukkit.broadcastMessage("not exist, add user now");
			return addUser(player);
		}
		
	    public static int getOwnRegionId(int userid) {
	    	for (Map.Entry<Integer, ModelRegion> region : Model.Regions().entrySet()) {
	    		if(region.getValue().isOwner(userid))
	    			return region.getKey();
	    	}
	    	return -1;
	    }
	    
	    public static int getOwnWolfId(int userid) {
	    	return User(userid).getAssignedWolfId();
	    }
	
		public static int addUser(Player player) throws SQLException {
			if(Config.debug) Bukkit.broadcastMessage("addnewUser");
			String playerName = player.getName();
			UUID playerUUID = player.getUniqueId();
			int userid = SQL.insertData_getId("username, useruuid", "'"+playerName+"', '"+playerUUID+"'", Config.databaseTableUsers);
			if(Config.debug) Bukkit.broadcastMessage("after SQL");
			addModel(userid);
			User(userid).initId(userid);
			User(userid).initName(playerName);
			User(userid).initUUID(playerUUID);
			User(userid).initAssignedWolfId(-1);
			
			return userid;
		}
		
//		public static void addAssignedWolfId(int userid, int wolfid) {
//			SQL.set("assigned_wolfid", wolfid, "userid", "=", String.valueOf(userid), Config.databaseTableUsers);
//			
//			User(userid).setAssignedWolfId(wolfid);
//		}
		
		public static int getAssignedWolfId(int userid) {
			return User(userid).getAssignedWolfId();
	}
		public static int getAssignedWolfId(String playerName) {
			for (Map.Entry<Integer, ModelUser> user : Model.Users().entrySet()) {
				if(user.getValue().getName().equalsIgnoreCase(playerName))
				{
					int wolfid = user.getValue().getAssignedWolfId();
					if(Model.Wolves().containsKey(wolfid))
						return wolfid;
				}
			}
			return -1;
		}
    }
    
	/////////////////////////////////////////////////////////
    
	public static Map<Integer, ModelWolf> Wolves() { return LandSecurity.plugin.mapWolves; }
    
    public static class Wolves {
    	
//    	private static Map<Integer, ModelWolf> getMap() { return LandSecurity.plugin.mapWolves; }
    	private static ModelWolf addModel(int wolfid) { return Wolves().putIfAbsent(wolfid, new ModelWolf()); }
    	
		public static int addWolf(Player player) {
			String playerName = player.getName();
			String wolfName = "&eWilk gracza " + playerName;
			int wolfCollarColorId = WolfGuard.getRandomColorId();
			
			UUID wolfEntityUUID = WolfGuard.spawnWolf(player, wolfName, wolfCollarColorId);
			
			//int userid = Model.Users.getUserId(playerName);
			
			//String playerNameLower = playerName.toLowerCase();
			
			int wolfid = SQL.insertData_getId("wolfenityUUID, wolfname, wolfcollarcolorid", "'"+wolfEntityUUID+"', '"+wolfName+"', '"+wolfCollarColorId+"'", Config.databaseTableWolves);
			
			//Model.Users.addAssignedWolfId(userid, wolfid);
			
			addModel(wolfid);
			Wolf(wolfid).initId(wolfid);
			Wolf(wolfid).initEnityUUID(wolfEntityUUID);
			Wolf(wolfid).initName(wolfName);
			Wolf(wolfid).initCollarColorId(wolfCollarColorId);
			
			return wolfid;
		}

		public static boolean existWolf(UUID wolfentityUUID) {
			for (Map.Entry<Integer, ModelWolf> wolf : Wolves().entrySet()) {
				if(wolf.getValue().getEnityUUID().equals(wolfentityUUID))
					return true;
			}
			return false;
		}
		
		public static int getWolfId(UUID wolfentityUUID) {
			for (Map.Entry<Integer, ModelWolf> wolf : Wolves().entrySet()) {
				if(wolf.getValue().getEnityUUID().equals(wolfentityUUID))
					return wolf.getKey();
			}
			return -1;
		}
		
//		public static Player getPlayer(int wolfid)
//		{
//			
//		}
		
//		public static Location getWolfLocation(int wolfid) {
//			UUID wolfentityUUID = Wolf(wolfid).getEnityUUID();
//			return Bukkit.getEntity(wolfentityUUID).getLocation();
//		}
//		
//		public static Location getWolfLocation(UUID wolfentityUUID) {
//			return Bukkit.getEntity(wolfentityUUID).getLocation();
//		}
//		
//		public static Wolf getWolfEntity(UUID wolfentityUUID) {
//			Entity entity = Bukkit.getEntity(wolfentityUUID);
//			if (entity instanceof Wolf) {
//				return (Wolf)entity;
//			}
//			return null;
//		}
//		
//		public static Wolf getWolfEntity(int wolfid) {
//			Entity entity = Bukkit.getEntity(Wolf(wolfid).getEnityUUID());
//			if (entity instanceof Wolf) {
//				return (Wolf)entity;
//			}
//			return null;
//		}
    }
    
	/////////////////////////////////////////////////////////
    
    public static Map<Player, ModelPlayer> Players() { return LandSecurity.plugin.mapPlayers; }
    
    public static class Players {
    	
//    	private static Map<Player, ModelPlayer> getMap() { return LandSecurity.plugin.mapPlayers; }
//    	public static ModelPlayer getModel(Player player) { return LandSecurity.plugin.mapPlayers.get(player); }
    	public static boolean isExist(Player player) { return Players().containsKey(player); }
    	private static ModelPlayer addModel(Player player) { return Players().putIfAbsent(player, new ModelPlayer()); }
    	private static ModelPlayer removeModel(Player player) { return Players().remove(player); }
    	
    	public static int addPlayer(Player player) throws SQLException {
    		if(Config.debug) Bukkit.broadcastMessage("addPlayer next create  model");
    		int userid = Model.Users.getUserId(player);
    		if(Config.debug) Bukkit.broadcastMessage("i have user id:" + userid);
    		addModel(player);
			Player(player).setPlayer(player);
			Player(player).setUserId(userid);
			Player(player).setOwnRegionId(Model.Users.getOwnRegionId(userid));
			Player(player).setOwnWolfId(Model.Users.getOwnWolfId(userid));
			Player(player).fixWolfEnity();
			return userid;
    	}
    	
    	public static int removePlayer(Player player) throws SQLException {
    		if(!Model.Players.isExist(player))
				return -1;
    		
    		int userid = Player(player).getUserId();
    		removeModel(player);
			return userid;
    	}
    	
//    	public static boolean isInsideOwnRegion(Player player) {
//    		return Model.Players.getMap().get(player).isInsideOwnRegion();
//    	}
//    	
//    	public static void setInsideOwnRegion(Player player) {
//    		Model.Players.getMap().get(player).setInsideOwnRegion(true);
//    	}
//    	
//    	public static void setOutsideOwnRegion(Player player) {
//    		Model.Players.getMap().get(player).setInsideOwnRegion(false);
//    	}
//    	
//    	public static int getInsideRegionId(Player player) {
//    		return Model.Players.getMap().get(player).getInsideRegionId();
//    	}
//    	
//    	public static void setInsideRegionId(Player player, int regionid) {
//    		Model.Players.getMap().get(player).setInsideRegionId(regionid);
//    	}
//    	
//    	public static void unsetInsideRegionId(Player player) {
//    		Model.Players.getMap().get(player).setInsideRegionId(-1);
//    	}
    }
}

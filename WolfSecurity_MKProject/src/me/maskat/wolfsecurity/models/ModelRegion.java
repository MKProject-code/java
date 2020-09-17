package me.maskat.wolfsecurity.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.maskat.mysql.SQL;
import me.maskat.wolfsecurity.Config;
import me.maskat.wolfsecurity.Plugin;
import mkproject.maskat.VipManager.VipAPI;

public class ModelRegion {
    private int regionid = -1;
    private String regionname = null;
    //private String real_regionname;
    private int pos1x;
    private int pos1z;
    private int pos2x;
    private int pos2z;
    private World world = null;
    private boolean claimed = false;
    private boolean protection = false;
    private LocalDateTime claimed_datetime = null;
    private List<Integer> owners = new ArrayList<Integer>();
    private List<Integer> family = new ArrayList<Integer>();
    private List<Integer> friends = new ArrayList<Integer>();
    private List<Integer> enemies = new ArrayList<Integer>();
    
    public ModelRegion(int regionid, String regionname, List<Integer> owners, boolean protection) {
    	this.regionid = regionid;
    	this.regionname = regionname;
    	this.owners = owners;
    	this.protection = protection;
    }
    
//    public void setName(String str) { regionname = str; }
    public void setId(int i) { regionid = i; }
    public void setName(String str) { regionname = str; }
    //public void setRealName(String str) { real_regionname = str; }
    public void setPositionFirstX(int i) { pos1x = i; }
    public void setPositionFirstZ(int i) { pos1z = i; }
    public void setPositionSecoundX(int i) { pos2x = i; }
    public void setPositionSecoundZ(int i) { pos2z = i; }
	public void setWorld(String str) { if(str != null) world = Bukkit.getServer().getWorld(str); }
    public void setOwners(List<Integer> iArrayList) { owners = iArrayList; }
    public void setFamily(List<Integer> iArrayList) { family = iArrayList; }
    public void setFriends(List<Integer> iArrayList) { friends = iArrayList; }
    public void setEnemies(List<Integer> iArrayList) { enemies = iArrayList; }
    
    public int getId() { return regionid; }
    public String getName() { return regionname; }
    
    public void setClaimed(boolean b) { claimed = b; }
    public void setClaimedDateTime(String claimed_datetime) {
    	if(claimed_datetime == null)
    	{
    		this.claimed_datetime = null;
    		return;
    	}
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    	this.claimed_datetime = LocalDateTime.parse(claimed_datetime, formatter);
    }
	public String getClaimedDateTimeString() {
		if(claimed_datetime == null)
		{
			if(claimed == true)
			{
				claimed_datetime = LocalDateTime.now();
				SQL.set("claimed_datetime", claimed_datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "regionid", "=", regionid, Config.databaseTableRegions);
			}
			return null;
		}
		
		return claimed_datetime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
	}
    public long getSecoundsFromClaimedDateTime() {
    	if(claimed_datetime == null)
    		return -1;
    	return claimed_datetime.until(LocalDateTime.now(), ChronoUnit.SECONDS);
    }
    
    //public String getRealName() { return real_regionname; }
//    public int[] getOwners() { return owners; }
//    public int[] getMembers() { return members; }
//    public int[] getFriends() { return friends; }
//    public int[] getEnemies() { return enemies; }

    public boolean isInRegion(Location location) {
    	if(!claimed || location.getWorld() != world)
    		return false;
    	
    	int locX = location.getBlockX();
    	int locZ = location.getBlockZ();
    	
    	int minX = Math.min(pos1x, pos2x);
    	int minZ = Math.min(pos1z, pos2z);
    	
    	int maxX = Math.max(pos1x, pos2x);
    	int maxZ = Math.max(pos1z, pos2z);
    	
//    	If the player X is less than maxX and greater than minX.
    	if(locX <= maxX && locX >= minX && locZ <= maxZ && locZ >= minZ)
    		return true;
    	return false;
    }
    
    public Location getCenterLocation() {
    	int minX = Math.min(pos1x, pos2x);
    	int minZ = Math.min(pos1z, pos2z);
    	int maxX = Math.max(pos1x, pos2x);
    	int maxZ = Math.max(pos1z, pos2z);
    	
    	int locX = maxX-((maxX-minX)/2);
    	int locZ = maxZ-((maxZ-minZ)/2);
    	
    	World world = Plugin.getAllowedWorld();
    	Location preloc = new Location(world, locX, 64, locZ);
    	return new Location(world, preloc.getBlockX(), world.getHighestBlockYAt(preloc.getBlockX(), preloc.getBlockZ()), preloc.getBlockZ());
    }
    
	public boolean isOwner(Player player)
	{
		return owners.contains(Model.Player(player).getUserId());
//		for(int i = 0; i < owners.length; i++) {
//			System.out.println("owners[i]:" + owners[i]);
//			if(LandSecurity.plugin.mapUsers.containsKey(owners[i]) && LandSecurity.plugin.mapUsers.get(owners[i]).getName() == player.getName().toLowerCase())
//				return true;
//		}
//		return false;
	}
	
	public ModelWolf getWolf()
	{
		return Model.Wolf(Model.User(owners.get(0)).getAssignedWolfId());
	}
	
	public ModelUser getOwner()
	{
		return Model.User(owners.get(0));
	}
	
	public boolean isOwner(int userid)
	{
		return owners.contains(userid);
		//return Arrays.stream(owners).anyMatch(i -> i == userid);
	}
	
	public boolean isFamily(Player player)
	{
		return family.contains(Model.Player(player).getUserId());
	}
	
	public boolean isFriend(Player player)
	{
		return friends.contains(Model.Player(player).getUserId());
	}
	
	public boolean isEnemy(Player player)
	{
		return enemies.contains(Model.Player(player).getUserId());
	}

//	public boolean isFamily(int userid)
//	{
//		return owners.contains(userid);
//		//return Arrays.stream(owners).anyMatch(i -> i == userid);
//	}
	
	public List<Integer> getFamilyUsersIdList()
	{
		return family;
	}
	
	public List<Integer> getFriendsUsersIdList()
	{
		return friends;
	}
	
	public List<Integer> getEnemiesUsersIdList()
	{
		return enemies;
	}
	
	public void addFamily(int userid)
	{
		if(friends.contains((Object)userid))
			removeFriend(userid);
		if(enemies.contains((Object)userid))
			removeEnemy(userid);
		
		family.add(userid);
		
		SQL.set("family", family, "regionid", "=", regionid, Config.databaseTableRegions);
	}
	
	public void removeFamily(int userid)
	{
		family.remove((Object)userid);
		
		SQL.set("family", family, "regionid", "=", regionid, Config.databaseTableRegions);
	}
	
	public void addFriend(int userid)
	{
		if(family.contains((Object)userid))
			removeFamily(userid);
		if(enemies.contains((Object)userid))
			removeEnemy(userid);
		
		friends.add(userid);
		
		SQL.set("friends", friends, "regionid", "=", regionid, Config.databaseTableRegions);
	}
	
	public void removeFriend(int userid)
	{
		friends.remove((Object)userid);
		
		SQL.set("friends", friends, "regionid", "=", regionid, Config.databaseTableRegions);
	}
	
	public void addEnemy(int userid)
	{
		if(family.contains((Object)userid))
			removeFamily(userid);
		if(friends.contains((Object)userid))
			removeFriend(userid);
		
		enemies.add(userid);
		
		SQL.set("enemies", enemies, "regionid", "=", regionid, Config.databaseTableRegions);
	}
	
	public void removeEnemy(int userid)
	{
		enemies.remove((Object)userid);
		
		SQL.set("enemies", enemies, "regionid", "=", regionid, Config.databaseTableRegions);
	}
	
	private boolean isNearOtherEnemyRegion(Player player, int pos1x, int pos1z, int pos2x, int pos2z, World world)
	{
		int minX = Math.min(pos1x, pos2x);
		int minZ = Math.min(pos1z, pos2z);
		
		int maxX = Math.max(pos1x, pos2x);
		int maxZ = Math.max(pos1z, pos2z);
		
		//int testi = 0;
		for(int x = minX; x <= maxX; x++)
		{
			if(x != minX && x != maxX && x % 10 != 0)
				continue;
			
			for(int z = minZ; z <= maxZ; z++)
			{
				if(z != minZ && z != maxZ && z % 10 != 0)
					continue;
				
				Location loc = new Location(world, x, 64, z);
				List<Integer> regionsidInside = Model.Regions.getRegionsId(loc);
		        //if(Model.Regions().containsKey(regionidInside)) {
				if(regionsidInside.size() > 0) {
					for(int regionidInside : regionsidInside)
					{
						if(regionidInside != regionid && !Model.Region(regionidInside).isFamily(player))
							return true;
					}
		        }
			}
		}
		//Bukkit.broadcastMessage("Count: " + testi);
		return false;
	}
	
	public boolean claimRegion(Player player, Location loc, int radius) {
		int locX = loc.getBlockX();
		int locZ = loc.getBlockZ();
		
		pos1x = locX+radius;
		pos1z = locZ+radius;
		pos2x = locX-radius;
		pos2z = locZ-radius;
		world = loc.getWorld();
		
		if(isNearOtherEnemyRegion(player, pos1x,pos1z,pos2x,pos2z,world))
			return false;
		
		claimed = true;
		claimed_datetime = LocalDateTime.now();
		
		if(player.hasPermission(Config.permissionRegionProtect))
			this.protection = true;
		
    	SQL.set("pos1x", pos1x, "regionid", "=", regionid, Config.databaseTableRegions);
    	SQL.set("pos1z", pos1z, "regionid", "=", regionid, Config.databaseTableRegions);
    	SQL.set("pos2x", pos2x, "regionid", "=", regionid, Config.databaseTableRegions);
    	SQL.set("pos2z", pos2z, "regionid", "=", regionid, Config.databaseTableRegions);
    	SQL.set("world", world.getName(), "regionid", "=", regionid, Config.databaseTableRegions);
    	SQL.set("claimed", claimed, "regionid", "=", regionid, Config.databaseTableRegions);
    	SQL.set("claimed_datetime", claimed_datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "regionid", "=", regionid, Config.databaseTableRegions);
    	SQL.set("protection", this.protection, "regionid", "=", regionid, Config.databaseTableRegions);
    	return true;
	}
	
	public void unclaimRegion() {
		claimed = false;
		this.protection = false;
		SQL.set("claimed", claimed, "regionid", "=", regionid, Config.databaseTableRegions);
		SQL.set("protection", this.protection, "regionid", "=", regionid, Config.databaseTableRegions);
	}
	
	public boolean isClaimed() {
		return claimed;
	}
	
    private int showBordersTaskId = 0;
	
	public boolean hideBorders()
	{
		if(showBordersTaskId > 0)
		{
			showBordersTaskId *= -1;
			return true;
		}
		return false;
	}
	
	public boolean showBorders(Player player)
	{
		if(!claimed)
			return false;
		
		if(showBordersTaskId < 0)
			showBordersTaskId *= -1;
		if(showBordersTaskId > 1000)
			showBordersTaskId = 1;
		showBordersTaskId++;
		
    	int minX = Math.min(pos1x, pos2x);
    	int minZ = Math.min(pos1z, pos2z);
    	
    	int maxX = Math.max(pos1x, pos2x)+1;
    	int maxZ = Math.max(pos1z, pos2z)+1;
    	
    	spawnBordersTask(showBordersTaskId, player, minX, minZ, maxX, maxZ, player.getWorld(), 60); // 60 x 2sec = 120sec duration time
    	return true;
	}
	
	private void spawnBordersTask(int taskId, Player player, int minX, int minZ, int maxX, int maxZ, World world, int repeat) {
		spawnBordersTaskRun(player, minX, minZ, maxX, maxZ, world);
		if(repeat > 0)
			Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.plugin, new Runnable() {
			    @Override
			    public void run() {
			    	if(showBordersTaskId == taskId && player.isOnline() && player.getWorld() == world)
			    		spawnBordersTask(taskId, player, minX, minZ, maxX, maxZ, world, repeat-1);
			    }
			}, 2*20L); //20 Tick (1 Second) delay before run() is called
	}
	
	private void spawnBordersTaskRun(Player player, int minX, int minZ, int maxX, int maxZ, World world)
	{
		double highestY = player.getLocation().getY()-1;
    	for(int i = minX; i <= maxX; i++) {
			if(i % 2 == 0 && i != minX && i != maxX)
				continue;
			
    		//int highestY = world.getHighestBlockYAt(i, minZ);
    		for(int ih = 2; ih <= 5; ih++)
    			player.spawnParticle(Particle.DRIP_LAVA, i, highestY+ih, minZ, 1, 0, 0, 0);
		}
		for(int i = minZ; i <= maxZ; i++) {
			if(i % 2 == 0 && i != minZ && i != maxZ)
				continue;
			
    		//int highestY = world.getHighestBlockYAt(minX, i);
    		for(int ih = 2; ih <= 5; ih++)
    			player.spawnParticle(Particle.DRIP_LAVA, minX, highestY+ih, i, 1, 0, 0, 0);
		}
		for(int i = minX; i <= maxX; i++) {
			if(i % 2 == 0 && i != minX && i != maxX)
				continue;
			
    		//int highestY = world.getHighestBlockYAt(i, maxZ);
    		for(int ih = 2; ih <= 5; ih++)
    			player.spawnParticle(Particle.DRIP_LAVA, i, highestY+ih, maxZ, 1, 0, 0, 0);
		}
		for(int i = minZ; i <= maxZ; i++) {
			if(i % 2 == 0 && i != minZ && i != maxZ)
				continue;
			
    		//int highestY = world.getHighestBlockYAt(maxX, i);
    		for(int ih = 2; ih <= 5; ih++)
    			player.spawnParticle(Particle.DRIP_LAVA, maxX, highestY+ih, i, 1, 0, 0, 0);
		}
	}
	
	public void setProtection(boolean protection) {
		this.protection = protection;
		SQL.set("protection", this.protection, "regionid", "=", regionid, Config.databaseTableRegions);
	}
	
	public boolean isProtection() {
		if(this.protection == true)
		{
			UUID ownerUUID = this.getOwner().getUUID();
			if(!VipAPI.isOfflineVIP(ownerUUID)) {
				this.protection = false;
			}
		}
		return this.protection;
	}
}
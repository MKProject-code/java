package me.maskat.wolfsecurity.models;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.maskat.mysql.SQL;
import me.maskat.wolfsecurity.Config;
import me.maskat.wolfsecurity.Function;
import me.maskat.wolfsecurity.Message;
import me.maskat.wolfsecurity.Plugin;
import me.maskat.wolfsecurity.WolfGuard;

public class ModelWolf {
    private int wolfid;
    private UUID wolfentityUUID;
    private String wolfname;
    private int wolfcollarcolorid;
    private int wolflevel = 1;
    private boolean wolfsitting = false;
    private LivingEntity wolftarget = null;
    private Location wolflastlocation = null;
    private Wolf wolfentity = null;
    
    public void initId(int i) { wolfid = i; }
    public void initEnityUUID(UUID uuid) { wolfentityUUID = uuid; }
	public void initName(String str) { wolfname = str; }
	public void initCollarColorId(int i) { wolfcollarcolorid = i; }
	public void initWolfLevel(int i) { wolflevel = i; }
	//public void initSitting(boolean bool) { wolfsitting = bool; }
	public void initLastLocation(String str) {
		if(str != null)
		{
			String[] lastLocationArray = str.split(",");
			if(lastLocationArray.length == 4)
			{
				wolflastlocation = new Location(Bukkit.getServer().getWorld(lastLocationArray[0]), Double.parseDouble(lastLocationArray[1]), Double.parseDouble(lastLocationArray[2]), Double.parseDouble(lastLocationArray[3]));
			}
			//else
			//	if(Config.debug) Bukkit.broadcastMessage("ModelWolf WARNING: initLastLocation(String str) --> lastLocationArray.length != 4");
		}
		//else if(Config.debug) Bukkit.broadcastMessage("ModelWolf WARNING: initLastLocation(String str) --> == null!");
	}
	
	public int getId() { return wolfid; }
	public UUID getEnityUUID() { return wolfentityUUID; }
    public String getName() { return wolfname; }
    public int getCollarColorId() { return wolfcollarcolorid; }
    public boolean getSitting() { return wolfsitting; }
    
    public void setNewEnity(UUID uuid, Location location) {
    	wolfentityUUID = uuid;
    	setLastLocation(location);
    	wolfentity = null;
    	
    	initEntityWolf();
    	
    }
    
    public void initEntityWolf() {
    	Wolf wolf = getEntityWolf();
    	if(wolf == null) 
    	{
    		Plugin.plugin.getServer().getLogger().warning("******* Entity not found on server world map!");
    		Plugin.plugin.getServer().getLogger().warning("******* Entity ID: " + wolfid);
    		return;
    	}
    	wolfsitting = wolf.isSitting();
    }
    
    public void fixSitting() {
    	getEntityWolf().setSitting(wolfsitting);
    }
	
    public void setSitting(Wolf wolfentitiy, boolean sitting) {
    	//if(Config.debug2) Bukkit.broadcastMessage("ModelWolf.setSitting: "+sitting);
    	wolfentitiy.setSitting(sitting);
    	wolfsitting = sitting;
    }
    
    public Wolf getEntityWolf() {
//    	Wolf wolf = (Wolf)Bukkit.getEntity(wolfentityUUID);
//    	if(!wolf.isValid())
//    	{
//    		//TODO !!!
//    		//Model.Wolves.getPlayer(int wolfid)
//    	}
//    	Entity firstentity = Bukkit.getServer().getEntity(wolfentityUUID);
//    	if(firstentity != null && firstentity.isValid())
//    		return (Wolf)firstentity;
//    	
//    	for(Entity entity : Bukkit.getServer().getWorld(Config.allowWorld).getEntities())
//    	{
//    		if(wolfentityUUID.equals(entity.getUniqueId()))
//    			return (Wolf)entity;
//    	}
    	
    	if(wolfentity != null && wolfentity.isValid()) //isValid-przy kazdym wywolaniu laduje chunk!!!
    		return wolfentity;
    	
    	Location lastlocation = this.getLastLocation();
    	//if(Config.debug) Bukkit.broadcastMessage("LastLocation: "+lastlocation);
    	if(lastlocation != null) {
    		Plugin.plugin.getLogger().info("Chunk loading for '"+this.wolfname+"' - entity last location: "+lastlocation);
    		lastlocation.getChunk().load();
    	}
    	
        for(Entity e: Plugin.getAllowedWorld().getEntities()) {
        	if(e.getUniqueId().equals(wolfentityUUID))
        	{
        		e.setSilent(true);
        		wolfentity = (Wolf)e;
        		return (Wolf)e;
        	}
        }
    	
//    	Location lastlocation = this.getLastLocation();
//    	if(lastlocation != null)
//    	{
//    		l.getWorld().loadChunk(l.getBlock.getChunk())
//    		 player.getLocation().getChunk().unload(true, false);
//    		 player.getLocation().getChunk().load(); 
//    	}
    	return null;
    	//return .getEntity(wolfentityUUID);
    }
    
    public boolean changeName(String wolfnameNew) {
    	wolfname = wolfnameNew;
    	SQL.set("wolfname", wolfnameNew, "wolfid", "=", wolfid, Config.databaseTableWolves);
    	
    	Bukkit.getScheduler().runTask(Plugin.plugin, new Runnable() {
            @Override
            public void run() {
            	getEntityWolf().setCustomName(Message.getColorMessage(wolfnameNew));
            }
        });
    	
    	
    	return true;
    }
    
    private Location wolfLastLocation = null;
    private int setTargetTaskId = 0;
    private int unsetTargetTaskId = 0;
    
    public void setTarget(LivingEntity player, int regionid) {
		if(wolftarget != null && wolftarget.isValid())
			wolftarget.remove();
		
    	Wolf wolfentity = getEntityWolf();
    	if(wolfentity.getTarget() != null && wolfentity.getTarget().equals(player))
    	{
    		if(Config.debug) Bukkit.broadcastMessage("wolf target jest taki sam! abort.");
    		return;
    	}
    	if(wolfLastLocation == null)
    		wolfLastLocation = wolfentity.getLocation();
    	boolean wolfLastIsSitting = wolfentity.isSitting();
    	wolfentity.setSitting(false);
    	wolfentity.setTarget(player);
		if(!Config.wolfAlwaysSilent)
			getEntityWolf().setSilent(false);
    	setTargetTaskId++;
    	setTargetTask(setTargetTaskId, (Player)player, regionid, wolfLastIsSitting);
    }
    
//    private void unsetTarget() {
//    	
//    }
	
	private void setTargetTask(int taskid, Player player, int regionid, boolean wolfLastIsSitting) {
		//setTargetTaskRun(player);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.plugin, new Runnable() {
			@Override
			public void run() {
				//if(Config.debug) Bukkit.broadcastMessage("Set: "+taskid+" != "+unsetTargetTaskId);
				if(taskid != setTargetTaskId)
					return;
				
				if(player.isOnline() && !player.isDead() && Model.Player(player).getInsideRegionsId().contains((Object)regionid) && !Model.Region(regionid).isFamily(player) && !Model.Region(regionid).isFriend(player))
				{
					//if(Config.debug) Bukkit.broadcastMessage("wrog w regionie! ponawiam task...");
					setTargetTask(taskid, player, regionid, wolfLastIsSitting);
				}
				else
				{
					//if(Config.debug) Bukkit.broadcastMessage("wrog oposcil region - wracam wilka do regionu");
					unsetTarget(regionid, wolfLastIsSitting);
				}
			}
		}, 2*20L); //20 Tick (1 Second) delay before run() is called
	}
	
//	private void setTargetTaskRun(Player player)
//	{
//		if(Config.debug) Bukkit.broadcastMessage("setTargetTaskRun(Player player)ID:"+setTargetTaskId);
//		if(!Config.wolfAlwaysSilent)
//			getEntityWolf().setSilent(false);
//		getEntityWolf().setTarget(player);
//	}
	
	private void unsetTarget(int regionid, boolean wolfLastIsSitting)
	{
		//if(Config.debug) Bukkit.broadcastMessage("initialize unsetTarget");
		if(wolftarget != null && wolftarget.isValid())
			wolftarget.remove();
		//{
		wolfLastLocation.setY(wolfLastLocation.getBlockY()-1);
		wolftarget = (LivingEntity) wolfLastLocation.getWorld().spawnEntity(wolfLastLocation, EntityType.CHICKEN);
		wolfLastLocation.setY(wolfLastLocation.getBlockY()+1);
		wolftarget.teleport(wolfLastLocation);
		
		wolftarget.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
		wolftarget.setAI(false);
		wolftarget.setSilent(true);
		wolftarget.setCollidable(false);
		wolftarget.setGravity(false);
		wolftarget.setInvulnerable(true);
		wolftarget.setRemoveWhenFarAway(true);
		wolftarget.setVelocity(new Vector(0,0,0));
		wolftarget.setHealth(1);
		getEntityWolf().setTarget(wolftarget);
		unsetTargetTaskId++;
		unsetTargetTask(unsetTargetTaskId, 10);
		//}
		//if(getEntityWolf().getTarget() != wolftarget)
		//{
		//	getEntityWolf().setTarget(wolftarget);
		//	unsetTargetTask(wolfLastLocation, 10);
		//}
	}
	
	private void unsetTargetTask(int taskid, int repeat)
	{
		//if(Config.debug) Bukkit.broadcastMessage("initialize unsetTargetTask");
		Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.plugin, new Runnable() {
		@Override
		public void run() {
			//if(Config.debug) Bukkit.broadcastMessage("Unset: "+taskid+" != "+unsetTargetTaskId);
			if(taskid != unsetTargetTaskId)
				return;
			
			LivingEntity target = getEntityWolf().getTarget();
			if(target == null || !target.equals(wolftarget))
			{
				if(!(target instanceof Player))
				{
					getEntityWolf().setTarget(wolftarget);
					unsetTargetTask(taskid, repeat-1);
				}
				else if(wolftarget != null && wolftarget.isValid())
					wolftarget.remove();
			}
			else if(repeat > 0)
			{
				if(Function.isNearLocation(getEntityWolf().getLocation(), wolftarget.getLocation(), 3))
				{
					getEntityWolf().setTarget(null);
					if(wolftarget != null && wolftarget.isValid())
						wolftarget.remove();
					fixSitting();
					wolfLastLocation = null;
					getEntityWolf().setSilent(true);
				}
				else if(getEntityWolf().isSitting())
				{
					wolfsitting = true;
					if(wolftarget != null && wolftarget.isValid())
						wolftarget.remove();
					getEntityWolf().setTarget(null);
					wolfLastLocation = null;
					getEntityWolf().setSilent(true);
				}
				else
					unsetTargetTask(taskid, repeat-1);
			}
			else
			{
				if(!getEntityWolf().isSitting())
					wolfsitting = false;
				if(wolftarget != null && wolftarget.isValid())
					wolftarget.remove();
				getEntityWolf().setTarget(null);
				wolfLastLocation = null;
				getEntityWolf().setSilent(true);
			}
		}
	}, 2*20L); //20 Tick (1 Second) delay before run() is called
	}
	
	public void fixEnity(Player player) {
//		Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.plugin, new Runnable() {
//			@Override
//			public void run() {
//				if(!player.isOnline())
//					return;
				//if(Config.debugWolfFixInfo) Bukkit.broadcastMessage("Model.Wolf(wolfid).fixEnity(player)");
				if(getEntityWolf() == null || !getEntityWolf().isValid())
				{
					if(Model.Player(player).isClaimedRegion())
					{
						//if(Config.debugWolfFixInfo) Bukkit.broadcastMessage("actionbar.fix.wolf.teleport.region");
						setNewEnity(WolfGuard.spawnWolf(player, wolfname, wolfcollarcolorid, Model.Player(player).getOwnRegion().getCenterLocation()),Model.Player(player).getOwnRegion().getCenterLocation());
						Message.sendActionBar(player, "actionbar.fix.wolf.teleport.region");
						SQL.set("wolfenityUUID", wolfentityUUID.toString(), "wolfid", "=", wolfid, Config.databaseTableWolves);
					}
					else
					{
						if(player.getWorld() != Plugin.getAllowedWorld())
						{
							//if(Config.debugWolfFixInfo) Bukkit.broadcastMessage("actionbar.fix.wolf.teleport.error");
							Message.sendActionBar(player, "actionbar.fix.wolf.teleport.error");
						}
						else
						{
							//if(Config.debugWolfFixInfo) Bukkit.broadcastMessage("actionbar.fix.wolf.teleport.owner");
							setNewEnity(WolfGuard.spawnWolf(player, wolfname, wolfcollarcolorid), player.getLocation());
				        	Message.sendActionBar(player, "actionbar.fix.wolf.teleport.owner");
				        	SQL.set("wolfenityUUID", wolfentityUUID.toString(), "wolfid", "=", wolfid, Config.databaseTableWolves);
						}
					}
				}
				//if(Config.debugWolfFixInfo) Bukkit.broadcastMessage("* Z wilkiem wszystko OK :)");
//			}
//		}, 10*20L); //20 Tick (1 Second) delay before run() is called
	}
	public Player getOwner() {
		for (Map.Entry<Player, ModelPlayer> player : Model.Players().entrySet()) {
			if(player.getValue().existWolf() && player.getValue().getWolf().getId() == wolfid)
				return player.getKey();
		}
		return null;
	}
	public boolean changeCollarColor(DyeColor colorname) {
		for(int i = 0; i < DyeColor.values().length; i++)
		{
			if(DyeColor.values()[i] == colorname)
			{
				wolfcollarcolorid = i;
		    	SQL.set("wolfcollarcolorid", wolfcollarcolorid, "wolfid", "=", wolfid, Config.databaseTableWolves);
		    	this.getEntityWolf().setCollarColor(colorname);
		    	return true;
			}
		}
		return false;
//		int x = DyeColor.BLACK;
//    	wolfname = wolfnameNew;
//    	SQL.set("wolfname", wolfnameNew, "wolfid", "=", wolfid, Config.databaseTableWolves);
//    	this.getEntityWolf().setCustomName(Message.getColorMessage(wolfnameNew));
//		DyeColor.WHITE
	}
	
	public int getLevelUpCost() {
		//return 50+(5*(wolflevel));
		//return (int)(100*(wolflevel*wolflevel));
		//return (int)((2.5 * (wolflevel*wolflevel)) - (40.5 * wolflevel) + 220);
		return (int)(wolflevel*50);
	}
	
	public boolean levelUp(Player player) {
		boolean success = Model.Player(player).withdrawEconomy(getLevelUpCost()).transactionSuccess();
		if(success)
		{
			wolflevel++;
			SQL.set("wolflevel", wolflevel, "wolfid", "=", wolfid, Config.databaseTableWolves);
		}
		return success;
	}
	public int getLevel() {
		return wolflevel;
	}
	public int getMaxLevel() {
		//TODO: if VIP return 30 max lvl
		return 30;
	}
//	public int getMaxLevelVIP() {
//		return 30;
//	}
	public int getProtectedRadius() {
		//return (int)(Config.wolfProtectedRadiusStart+((wolflevel-1)-((wolflevel/3)*wolflevel)));
		//return (int)(Config.wolfProtectedRadiusStart+(((wolflevel-1)*wolflevel)/2));
//		return (int)
//				(
//					Config.wolfProtectedRadiusStart+
//					(
//						(
//							Math.sqrt(2*(wolflevel-1))*1.5
//						)
//						+
//						(
//							wolflevel==getMaxLevel()?1:0
//						)
//					)
//				);
//		
		return (int)(Config.wolfProtectedRadiusStart+wolflevel);
	}
	public String getProtectedArea() {
		int protectedsize = (getProtectedRadius()*2)+1;
		return protectedsize+"x"+protectedsize;
	}
	
	public void setLastLocation(Location location) {
		wolflastlocation = location;
    	SQL.set("wolflastlocation", location.getWorld().getName()+","+location.getX()+","+location.getY()+","+location.getZ(), "wolfid", "=", wolfid, Config.databaseTableWolves);
	}
	
	public Location getLastLocation() {
		return wolflastlocation;
//		String lastLocationData = (String)SQL.get("wolflastlocation", "wolfid", "=", String.valueOf(wolfid), Config.databaseTableWolves);
//		if(lastLocationData == null)
//			return null;
//		
//		String[] lastLocationArray = lastLocationData.split(",");
//		if(lastLocationArray.length < 0)
//			return null;
//		
//		return new Location(Bukkit.getServer().getWorld(lastLocationArray[0]), Double.parseDouble(lastLocationArray[1]), Double.parseDouble(lastLocationArray[2]), Double.parseDouble(lastLocationArray[3]));
	}
	public boolean isMaxLevel() {
		return wolflevel >= this.getMaxLevel();
	}
	
//	private void unsetTargetTask(Location wolfLastLocation, int repeat)
//	{
//		Bukkit.getScheduler().scheduleSyncDelayedTask(LandSecurity.plugin, new Runnable() {
//			@Override
//			public void run() {
//				if(repeat > 0 && getEntityWolf().getTarget() == wolftarget && wolftarget.isValid())
//				{
//					wolftarget.teleport(wolfLastLocation);
//					wolftarget.setVelocity(new Vector(0,0,0));
//					unsetTargetTask(wolfLastLocation, repeat-1);
//				}
//				else if(wolftarget.isValid())
//					wolftarget.remove();
//			}
//		}, 2*20L); //20 Tick (1 Second) delay before run() is called
//	}
}
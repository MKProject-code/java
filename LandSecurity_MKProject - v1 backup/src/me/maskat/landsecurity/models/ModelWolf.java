package me.maskat.landsecurity.models;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.maskat.landsecurity.Config;
import me.maskat.landsecurity.Function;
import me.maskat.landsecurity.LandSecurity;
import me.maskat.landsecurity.Message;
import me.maskat.landsecurity.WolfGuard;
import me.maskat.mysql.SQL;
import net.milkbowl.vault.economy.EconomyResponse;

public class ModelWolf {
    private int wolfid;
    private UUID wolfentityUUID;
    private String wolfname;
    private int wolfcollarcolorid;
    private int wolflevel = 1;
    private boolean wolfsitting = false;
    private LivingEntity wolftarget = null;
    
    public void initId(int i) { wolfid = i; }
    public void initEnityUUID(UUID uuid) { wolfentityUUID = uuid; }
	public void initName(String str) { wolfname = str; }
	public void initCollarColorId(int i) { wolfcollarcolorid = i; }
	public void initWolfLevel(int i) { wolflevel = i; }
	public void initSitting(boolean bool) { wolfsitting = bool; }
	
	public int getId() { return wolfid; }
	public UUID getEnityUUID() { return wolfentityUUID; }
    public String getName() { return wolfname; }
    public int getCollarColorId() { return wolfcollarcolorid; }
    public boolean getSitting() { return wolfsitting; }
    
    public void fixSitting() {
    	getEntityWolf().setSitting(wolfsitting);
    }
	
    public void setSitting(Wolf wolfentitiy, boolean sitting) {
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
    	Entity firstentity = Bukkit.getServer().getEntity(wolfentityUUID);
    	if(firstentity != null && firstentity.isValid())
    		return (Wolf)firstentity;
    	
    	for(Entity entity : Bukkit.getServer().getWorld(Config.allowWorld).getEntities())
    	{
    		if(wolfentityUUID.equals(entity.getUniqueId()))
    			return (Wolf)entity;
    	}
    	
    	for(World w: Bukkit.getServer().getWorlds()) {
            for(Entity e: w.getEntities()) {
            	if(wolfentityUUID.equals(e.getUniqueId()))
            		return (Wolf)e;
                }
            }
    	return null;
    	//return .getEntity(wolfentityUUID);
    }
    
    public boolean changeName(String wolfnameNew) {
    	wolfname = wolfnameNew;
    	SQL.set("wolfname", wolfnameNew, "wolfid", "=", wolfid, Config.databaseTableWolves);
    	this.getEntityWolf().setCustomName(Message.getColorMessage(wolfnameNew));
    	
    	return true;
    }
    
    public void setTarget(LivingEntity player, int regionid) {
    	Wolf wolfentity = getEntityWolf();
    	Location wolfLastLocation = wolfentity.getLocation();
    	boolean wolfLastIsSitting = wolfentity.isSitting();
    	wolfentity.setSitting(false);
    	setTargetTask((Player)player, regionid, wolfLastLocation, wolfLastIsSitting);
    }
    
//    private void unsetTarget() {
//    	
//    }
	
	private void setTargetTask(Player player, int regionid, Location wolfLastLocation, boolean wolfLastIsSitting) {
		setTargetTaskRun(player);
		Bukkit.getScheduler().scheduleSyncDelayedTask(LandSecurity.plugin, new Runnable() {
			@Override
			public void run() {
				if(Model.Player(player).getInsideRegionId() == regionid && !Model.Region(regionid).isFamily(player) && !Model.Region(regionid).isFriend(player))
					setTargetTask(player, regionid, wolfLastLocation, wolfLastIsSitting);
				else
					unsetTarget(regionid, wolfLastLocation, wolfLastIsSitting);
			}
		}, 2*20L); //20 Tick (1 Second) delay before run() is called
	}
	
	private void setTargetTaskRun(Player player)
	{
		if(!Config.wolfAlwaysSilent)
			getEntityWolf().setSilent(false);
		getEntityWolf().setTarget(player);
	}
	
	private void unsetTarget(int regionid, Location wolfLastLocation, boolean wolfLastIsSitting)
	{
		if(wolftarget != null && wolftarget.isValid())
			wolftarget.remove();
		//{
			wolfLastLocation.setY(wolfLastLocation.getBlockY()-1);
			wolftarget = (LivingEntity) wolfLastLocation.getWorld().spawnEntity(wolfLastLocation, EntityType.CHICKEN);
			wolfLastLocation.setY(wolfLastLocation.getBlockY()+1);
			
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
			unsetTargetTask(wolfLastLocation, 10);
		//}
		//if(getEntityWolf().getTarget() != wolftarget)
		//{
		//	getEntityWolf().setTarget(wolftarget);
		//	unsetTargetTask(wolfLastLocation, 10);
		//}
	}
	
	private void unsetTargetTask(Location wolfLastLocation, int repeat)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(LandSecurity.plugin, new Runnable() {
		@Override
		public void run() {
			if(repeat > 0 && getEntityWolf().getTarget() == wolftarget && wolftarget.isValid())
			{
				if(Function.isNearLocation(getEntityWolf().getLocation(), wolfLastLocation, 3))
				{
					fixSitting();
					wolftarget.remove();
				}
				else
				{
					if(getEntityWolf().isSitting())
					{
						getEntityWolf().setSilent(true);
						getEntityWolf().setTarget(null);
						wolftarget.remove();
					}
					else
						unsetTargetTask(wolfLastLocation, repeat-1);
				}
			}
			else if(wolftarget.isValid())
			{
				getEntityWolf().setSilent(true);
				getEntityWolf().setTarget(null);
				wolftarget.remove();
			}
		}
	}, 2*20L); //20 Tick (1 Second) delay before run() is called
	}
	
	public void fixEnity(Player player) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(LandSecurity.plugin, new Runnable() {
			@Override
			public void run() {
				if(!player.isOnline())
					return;
				
				if(getEntityWolf() == null || !getEntityWolf().isValid())
				{
		
					if(Model.Player(player).isClaimedRegion())
					{
						wolfentityUUID = WolfGuard.spawnWolf(player, wolfname, wolfcollarcolorid, Model.Player(player).getOwnRegion().getCenterLocation());
						Message.sendActionBar(player, "actionbar.fix.wolf.teleport.region");
					}
					else
					{
						if(player.getWorld() != Bukkit.getServer().getWorld(Config.allowWorld))
						{
							Message.sendActionBar(player, "actionbar.fix.wolf.teleport.error");
						}
						else
						{
							wolfentityUUID = WolfGuard.spawnWolf(player, wolfname, wolfcollarcolorid);
				        	Message.sendActionBar(player, "actionbar.fix.wolf.teleport.owner");
						}
					}
		        	SQL.set("wolfenityUUID", wolfentityUUID.toString(), "wolfid", "=", wolfid, Config.databaseTableWolves);
		
				}
				if(Config.debug) Bukkit.broadcastMessage("* Z wilkiem wszystko OK :)");
			}
		}, 10*20L); //20 Tick (1 Second) delay before run() is called
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
	public boolean levelUp(Player player) {
		boolean success = Model.Player(player).withdrawEconomy(55).transactionSuccess();
		if(success)
		{
			// TODO: region reclaim! and resize!
			wolflevel++;
		}
		return success;
	}
	public int getLevel() {
		return wolflevel;
	}
	public int getProtectedRadius() {
		//return (int)(Config.wolfProtectedRadiusStart+((wolflevel-1)-((wolflevel/3)*wolflevel)));
		//return (int)(Config.wolfProtectedRadiusStart+(((wolflevel-1)*wolflevel)/2));
		return (int)
				(
					Config.wolfProtectedRadiusStart+
					(
						(
							Math.sqrt(2*(wolflevel-1))*2
						)
						+
						(
							wolflevel==100?1:0
						)
					)
				);
	}
	public String getProtectedArea() {
		int protectedsize = (getProtectedRadius()*2)+1;
		return protectedsize+"x"+protectedsize;
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
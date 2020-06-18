package me.maskat.landsecurity;

public class LandSecurityTask {
    public static void taskCheckAllPlayers() {
//    	World world = Bukkit.getServer().getWorld("world");
//    	RegionManager regions = container.get((com.sk89q.worldedit.world.World)world);
//    	Bukkit.broadcastMessage("task work");
//    	ProtectedRegion region = regions.setRegions(regions);
//    	regions.addRegion(region);
//    	System.out.println("[LandSecurity] *** Task work ***");
    }
    
    public static void TaskCheckPlayerWolfGuard() {
//        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
//        scheduler.scheduleSyncRepeatingTask(LandSecurity.plugin, new Runnable() {
//            @Override
//            public void run() {
//    			for (Map.Entry<Integer, ModelUser> user : Model.Users.getMap().entrySet()) {
//    				int user_wolfid = user.getValue().getAssignedWolfId();
//    				if(user_wolfid > 0)
//    				{
//    					ModelRegion region = Model.Regions.getRegion(user);
//    					if(region == null)
//    						return;
//    					
//    					
//    					UUID wolfentityUUID = Model.Wolves.getMap().get(user_wolfid).getEnityUUID();
//    					Wolf wolfentity = Model.Wolves.getWolfEntity(wolfentityUUID);
//    					
//    					if(wolfentity.isSitting() == true)
//    						return;
//    					
//    					if(region.isInRegion(wolfentity.getLocation()))
//	    					return;
//    					
//    					wolfentity.setSitting(true);
//    					
//    					
////    					wolfentity.
//    					
////    					  World world = getWorld();
////    					  List<Entity> ents = world.getEntities();
////    					  List<com.sk89q.worldedit.entity.Entity> entities = new ArrayList<>();
////    					  for (Entity ent : ents) {
////    					    if (region.contains(BukkitAdapter.asBlockVector(ent.getLocation()))) {
////    					      entities.add(BukkitAdapter.adapt(ent));
////    					    }
////    					  }
//    				}
//    			}
//            }
//        }, 0L, 100L);
    }
    
//    private static void runTaskWolfMove(Location regionCenterVector, Wolf wolfentity)
//    {
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//            	Vector vector = regionCenterVector.toVector().subtract(wolfentity.getLocation().toVector()).normalize();
//				// dir is now a unit vector pointing from wolfentity.getLocation() to region.getCenterLocation()
//				// set "speed" to how fast you want the entity at A to move towards B
//				wolfentity.setVelocity(vector.multiply(0.6));
//				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&bWolf idzie do regionu! X:"+regionCenterVector.getBlockX()+" Y:"+regionCenterVector.getBlockY()+" Z:"+regionCenterVector.getBlockZ()));
//            	if(!Function.isInLocation(regionCenterVector, wolfentity.getLocation()))
//            	{
//            		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cWolf nie dotarl jeszcze do regionu!"));
//            		runTaskWolfMove(regionCenterVector, wolfentity);
//            		return;
//            	}
//            	wolfentity.setAI(true);
//            }
//        }.runTaskLater(LandSecurity.plugin, 10L);
//    }
    
//    public void moveTo(LivingEntity entity, Location moveTo, float speed)
//    {
//        EntityLiving nmsEntity = ((CraftLivingEntity) entity).getHandle();
//        // Create a path to the location
//        PathEntity path = nmsEntity.getNavigation().a(moveTo.getX(), moveTo.getY(), moveTo.getZ());
//        // Move to that path at 'speed' speed.
//        nmsEntity.getNavigation().a(path, speed);
//    }
}

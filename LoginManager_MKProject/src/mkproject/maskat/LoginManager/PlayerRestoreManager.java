package mkproject.maskat.LoginManager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class PlayerRestoreManager {

	private static boolean ENABLED_VEHICLE_TELEPORT = false;
	
	public static void onPlayerJoinLow(Player player) {
    	if(!Model.existPlayer(player))
    		Model.addPlayer(player);
		
		if(player.getGameMode() == null)
		{
			player.setGameMode(GameMode.SURVIVAL);
			Model.getPlayer(player).setGameMode(GameMode.SURVIVAL); // Update FIX 3.0
		}
		else
			Model.getPlayer(player).setGameMode(player.getGameMode()); // Update FIX 3.0
		
		if(Model.getPlayer(player).isDead())
		{
			Plugin.getPlugin().getLogger().info("Player '"+player.getName()+"' is dead - respawning...");
			
			Model.getPlayer(player).setDead(true);
			
			player.spigot().respawn();
			
			//Model.getPlayer(player).setJoinSpawnLocation(Papi.Model.getPlayer(player).getRespawnLocation());
			
			Model.getPlayer(player).setVelocity(null);
		}
//		else
//		{
//			Bukkit.broadcastMessage("player last velocity: "+player.getVelocity());
//			Model.getPlayer(player).setVelocity(player.getVelocity());
//		}
//    	e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 250));
//    	e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 250));
//    	e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 250));
    	if(player.isInsideVehicle())
    	{
    		//Plugin.getPlugin().getLogger().info("Player '"+player.getName()+"' is inside vehicle - leaving and teleporting vehicle...");
    		Entity vehicle = player.getVehicle();
    		//vehicle.getLocation().getWorld().loadChunk(vehicle.getLocation().getWorld().getChunkAt(vehicle.getLocation()));
    		//Plugin.getPlugin().getLogger().warning("vehicle.getLocation():"+vehicle.getLocation());
    		Model.getPlayer(player).setVehicle(vehicle);
    		Model.getPlayer(player).setVehicleLocation(vehicle.getLocation());
    		Model.getPlayer(player).getVehicleLocation().setWorld(Model.getPlayer(player).getJoinSpawnLocation().getWorld());
//    		vehicle.getLocation().getWorld().loadChunk(vehicle.getLocation().getWorld().getChunkAt(vehicle.getLocation()));
//    		Bukkit.broadcastMessage("load chunk");
//    		if(vehicle.isValid())
//    			Bukkit.broadcastMessage("valid");
//    		else
//    				Bukkit.broadcastMessage("invalid");
    		player.leaveVehicle();
//    		if(vehicle.isValid())
//    			Bukkit.broadcastMessage("valid");
//    		else
//    				Bukkit.broadcastMessage("invalid");
    		//vehicle.teleport(Papi.Server.getServerSpawnLocation());
    		if(ENABLED_VEHICLE_TELEPORT)
    			vehicle.teleport(Plugin.getEntitySpawnLocation());
    		else
    		{
        		Location vehicleLocation = vehicle.getLocation().clone();
        		vehicleLocation.getWorld().loadChunk(vehicleLocation.getWorld().getChunkAt(vehicleLocation));
        		vehicleLocation.setWorld(Model.getPlayer(player).getJoinSpawnLocation().getWorld());
        		vehicleLocation.getWorld().loadChunk(vehicleLocation.getWorld().getChunkAt(vehicleLocation));
        		vehicle.teleport(vehicleLocation);
    		}
//    		if(vehicle.isValid())
//    			Bukkit.broadcastMessage("valid");
//    		else
//    				Bukkit.broadcastMessage("invalid");
    		
        	//Plugin.getPlugin().getLogger().info("Player '"+player.getName()+"' teleporting to server spawn.");
        	//player.teleport(Papi.Server.getServerSpawnLocation());
        	
        	
    	}
    	
    	player.teleport(Papi.Server.getServerSpawnLocation());
    	
    	player.setWalkSpeed(0);
    	player.setFlySpeed(0);
    	
    	clearPlayer(player);
    	
//    	if(Model.getPlayer(player).getGameMode() == null) // Update FIX 2.0
//    		Model.getPlayer(player).setGameMode(player.getGameMode()); // Update FIX 2.0
    	
    	Model.getPlayer(player).setActivePotionEffects(player.getActivePotionEffects());
    	
//		for(PotionEffect potion : Model.getPlayer(player).getActivePotionEffects()) { // Update FIX 2.0
//			player.removePotionEffect(potion.getType()); // Update FIX 2.0
//		} // Update FIX 2.0
		
		//player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1000, true, false));
    	//player.hidePlayer(Plugin.getPlugin(), player);
		
		player.setGameMode(GameMode.SPECTATOR);
		
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(!p.equals(player))
			{
				if(!Papi.Model.getPlayer(p).isLogged() || p.getGameMode() == GameMode.SPECTATOR)
				{
					player.hidePlayer(Plugin.getPlugin(), p);
					p.hidePlayer(Plugin.getPlugin(), player);
				}
				
				//player.hidePlayer(Plugin.getPlugin(), p);
			}
		}
	}
	
	public static void onPlayerJoinHigh(Player player) {
		String ipAddr = player.getAddress().getAddress().getHostAddress().toString();
		Plugin.getPlugin().getLogger().info("Player '"+player.getName()+"' IP: "+ipAddr);
		if(ipAddr.equals("192.168.1.15"))
			Login.doRegisterSuccessLoginPlayer(player);
		
		//player.setGameMode(GameMode.SPECTATOR);
	}
	
	
	public static void onPlayerSpawnLocation(PlayerSpawnLocationEvent e) {
    	if(!Model.existPlayer(e.getPlayer()))
    		Model.addPlayer(e.getPlayer());
    	
    	Model.getPlayer(e.getPlayer()).setDead(e.getPlayer().isDead());
    	
//    	if(Model.getPlayer(e.getPlayer()).isDead())
//    	{
//    		Model.getPlayer(e.getPlayer()).setJoinSpawnLocation(null);
//    	}
//    	else
    	//Bukkit.broadcastMessage("last spawn: "+e.getSpawnLocation());
    	Model.getPlayer(e.getPlayer()).setJoinSpawnLocation(e.getSpawnLocation());
    	
    	Plugin.getPlugin().getLogger().warning("Player '"+e.getPlayer().getName()+"' join location: " + e.getSpawnLocation());
    	
//    	e.getSpawnLocation().getWorld().loadChunk(e.getSpawnLocation().getWorld().getChunkAt(e.getSpawnLocation()));
//    	Bukkit.broadcastMessage("load chunk pre");
		e.setSpawnLocation(Papi.Server.getServerSpawnLocation());
	}
	
	public static void onPlayerLogin(Player player) {
		
		//player.setGameMode(Model.getPlayer(player).getGameMode());
		
		player.setWalkSpeed(0.2f);
		player.setFlySpeed(0.1f);
//		player.removePotionEffect(PotionEffectType.JUMP);
//		player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
//		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		
		//player.showPlayer(Plugin.getPlugin(), player);
		
		restorePlayer(player);
		
		if(!Model.getPlayer(player).isVehicleScheduler())
			Model.removePlayer(player);
	}

	public static void onPlayerQuit(Player player) {
		player.leaveVehicle();
		
		if(!Model.existPlayer(player))
			return;
		
		if(Model.getPlayer(player).isVehicleScheduler())
		{
			if(ENABLED_VEHICLE_TELEPORT)
			{
				Model.getPlayer(player).getVehicle().teleport(Model.getPlayer(player).getVehicleLocation());
				Model.getPlayer(player).getVehicle().setRotation(Model.getPlayer(player).getVehicleLocation().getYaw(),Model.getPlayer(player).getVehicleLocation().getPitch());
			}
			Model.getPlayer(player).getVehicle().addPassenger(player);
			Model.removePlayer(player);
			return;
		}
		
		if(Papi.Model.getPlayer(player).isLogged())
			return;
		
		Model.getPlayer(player).setQuitBeforeLogin(true);
		restorePlayer(player);
		
		Model.removePlayer(player);
	}
	
	private static void clearPlayer(Player player) {
		//Model.getPlayer(player).setExp(player.getExp()); // Update FIX 2.0
		//player.setExp(0); // Update FIX 2.0
		//Model.getPlayer(player).setInventoryContents(player.getInventory().getContents()); // Update FIX 2.0
		//Model.getPlayer(player).setInventoryArmorContents(player.getInventory().getArmorContents()); // Update FIX 2.0
		//player.getInventory().clear(); // Update FIX 2.0

		//player.updateInventory(); // Update FIX 2.0
	}
	
	private static void restorePlayer(Player player) {
		//Bukkit.broadcastMessage("restorePlayer gamemode to: "+Model.getPlayer(player).getGameMode());
		if(!player.hasPermission("mkp.loginmanager.bypass.gamemode")) // Update FIX 2.0
			player.setGameMode(GameMode.SURVIVAL); // Update FIX 2.0
		else
			player.setGameMode(Model.getPlayer(player).getGameMode());
		
		if(Model.getPlayer(player).isDead())
		{
			Plugin.getPlugin().getLogger().info("Player '"+player.getName()+"' teleporting to respawn location: " + Papi.Model.getPlayer(player).getRespawnLocation());
			player.teleport(Papi.Model.getPlayer(player).getRespawnLocation());
		}
		else if(Model.getPlayer(player).getVehicle() == null)
		{
			if(Model.getPlayer(player).getJoinSpawnLocation() != null && Bukkit.getServer().getOfflinePlayer(player.getUniqueId()).hasPlayedBefore())
			{
				Plugin.getPlugin().getLogger().info("Player '"+player.getName()+"' teleporting to last location: " + Model.getPlayer(player).getJoinSpawnLocation());
				player.teleport(Model.getPlayer(player).getJoinSpawnLocation());
			}
		}
		else if(!Model.getPlayer(player).getVehicle().isValid())
		{
			Plugin.getPlugin().getLogger().info("Player '"+player.getName()+"' teleporting to vehicle last location: " + Model.getPlayer(player).getVehicleLocation());
			player.teleport(Model.getPlayer(player).getVehicleLocation());
		}
		else
		{
			//Model.getPlayer(player).getVehicleLocation().getWorld().loadChunk(Model.getPlayer(player).getVehicleLocation().getWorld().getChunkAt(Model.getPlayer(player).getVehicleLocation()));
			////////////////////////////////////////////////////////////////
			
				
				//Model.getPlayer(player).getVehicle().getLocation().getWorld().loadChunk(Model.getPlayer(player).getVehicle().getLocation().getWorld().getChunkAt(Model.getPlayer(player).getVehicle().getLocation()));
				Plugin.getPlugin().getLogger().info("Player '"+player.getName()+"' teleport to: " + Model.getPlayer(player).getVehicleLocation());
				player.teleport(Model.getPlayer(player).getVehicleLocation());
				
				if(ENABLED_VEHICLE_TELEPORT)
				{
					player.setWalkSpeed(0f);
	            	player.setFlySpeed(0f);
				}
            	
            	if(Model.getPlayer(player).isQuitBeforeLogin())
            	{
            		if(ENABLED_VEHICLE_TELEPORT) {
						Model.getPlayer(player).getVehicle().teleport(Model.getPlayer(player).getVehicleLocation());
						Model.getPlayer(player).getVehicle().setRotation(Model.getPlayer(player).getVehicleLocation().getYaw(),Model.getPlayer(player).getVehicleLocation().getPitch());
            		}
					Model.getPlayer(player).getVehicle().addPassenger(player);
            	}
            	else if(ENABLED_VEHICLE_TELEPORT)
            	{
            		Message.sendActionBar(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.RestoreLocationInfo"));
            		Model.getPlayer(player).setVehicleScheduler(true);
            	}
            		
				//Model.getPlayer(player).getVehicle().setRotation(Model.getPlayer(player).getVehicleLocation().getYaw(),Model.getPlayer(player).getVehicleLocation().getPitch());
				
				
//				Entity vehicle = Model.getPlayer(player).getVehicle();
//				Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), new Runnable() {
//		                public void run() {
//		                	vehicle.addPassenger(player);
//		                }
//					}, 100L);
			//player.teleport(Model.getPlayer(player).getVehicle().getLocation());
			//Model.getPlayer(player).getVehicle().addPassenger(player);
//			}
		}

			
		//player.setExp(Model.getPlayer(player).getExp()); // Update FIX 2.0
		//player.getInventory().setContents(Model.getPlayer(player).getInventoryContents()); // Update FIX 2.0
		//player.getInventory().setArmorContents(Model.getPlayer(player).getInventoryArmorContents()); // Update FIX 2.0
		
		//player.updateInventory(); // Update FIX 2.0
		
//		for(Player p : Bukkit.getOnlinePlayers())
//		{
//			if(!p.equals(player))
//				player.showPlayer(Plugin.getPlugin(), p);
//		}
		
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(!p.equals(player))
			{
				if((Model.existPlayer(p) && Model.getPlayer(p).isAferLogin()) || Papi.Model.getPlayer(p).isLogged())
				{
					player.showPlayer(Plugin.getPlugin(), p);
					//p.showPlayer(Plugin.getPlugin(), player);
				}
				
				if(player.getGameMode() != GameMode.SPECTATOR)
					p.showPlayer(Plugin.getPlugin(), player);
				else if((Model.existPlayer(p) && Model.getPlayer(p).isAferLogin()) || Papi.Model.getPlayer(p).isLogged())
					p.showPlayer(Plugin.getPlugin(), player);
			}
		}
		
		for(PotionEffect potionActive : player.getActivePotionEffects()) { // Update FIX 2.0
			player.removePotionEffect(potionActive.getType()); // Update FIX 2.0
		} // Update FIX 2.0
		for(PotionEffect potion : Model.getPlayer(player).getActivePotionEffects()) {
			player.addPotionEffect(potion);
		}
		
		if(Model.getPlayer(player).isVehicleScheduler())
		{
			Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), new Runnable() {
				@Override
                public void run() {
                	if(Model.existPlayer(player) && !Model.getPlayer(player).isQuitBeforeLogin())
					{
//                		boolean playerStay = player.getLocation().getBlockX() == Model.getPlayer(player).getVehicleLocation().getBlockX() &&
//                				player.getLocation().getBlockY() == Model.getPlayer(player).getVehicleLocation().getBlockY() &&
//                				player.getLocation().getBlockZ() == Model.getPlayer(player).getVehicleLocation().getBlockZ();
//                		
                		if(!Model.getPlayer(player).isTeleportedWhenIsLogged())
                		{
	                		player.setWalkSpeed(0.2f);
	                		player.setFlySpeed(0.1f);
                		}
                		
						Model.getPlayer(player).getVehicle().teleport(Model.getPlayer(player).getVehicleLocation());
                		Model.getPlayer(player).getVehicle().setRotation(Model.getPlayer(player).getVehicleLocation().getYaw(),Model.getPlayer(player).getVehicleLocation().getPitch());
						
                		if(!Model.getPlayer(player).isTeleportedWhenIsLogged())
							Model.getPlayer(player).getVehicle().addPassenger(player);
                		
						Model.removePlayer(player);
						//Papi.Model.getPlayer(player).registerLogged();
					}
                }
			}, 40L);
		}
	}
	
	
	
}

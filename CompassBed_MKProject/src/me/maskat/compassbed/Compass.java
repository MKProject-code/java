package me.maskat.compassbed;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Compass extends JavaPlugin implements Listener {

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	    System.out.println("[CommpassBed] Hello! My developllo! My developer is MasKAT from skyidea.pl ---> Plugin enabled!");
	}

	@EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
		checkLocation(event.getPlayer());
//    	getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
//            @Override
//            public void run() {
//            	Player player = event.getPlayer();
//            	if(player.isOnline())
//            	{
//	            	setCompassNavigate(event.getPlayer());
//	            	checkLocation(event.getPlayer());
//            	}
//            }
//        }, 50L);
    }
	
	public void checkLocation(Player player) {
    	getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
            	if (player.isOnline()) {
            		Location bedlocation = player.getBedSpawnLocation();
	        		if (bedlocation == null || bedlocation.getWorld() != player.getWorld()) {
	        			Random random = new Random();
	        			Location location = player.getLocation();
	        			player.setCompassTarget(new Location(location.getWorld(), location.getX()+(random.nextInt(51)-25), location.getY(), location.getZ()+(random.nextInt(51)-25)));
	        		}
	        		else
	        		{
	        			player.setCompassTarget(bedlocation);
	        		}
	            	checkLocation(player);
            	}
            }
        }, 100L);
	}
	
	/*@EventHandler
	public void inventoryClickEvent(InventoryClickEvent event) {
		HumanEntity player = event.getWhoClicked();
		ItemStack itemStack = event.getCurrentItem();
		if(player instanceof Player && itemStack != null)
			if(itemStack.getType().equals(Material.COMPASS))
				setCompassNavigate((Player)player);
	}
	
	@EventHandler
	public void pickupItemEvent(EntityPickupItemEvent event) {
		LivingEntity player = event.getEntity();
		ItemStack itemStack = event.getItem().getItemStack();
		if(player instanceof Player && itemStack != null)
		{
			if(itemStack.getType().equals(Material.COMPASS))
				setCompassNavigate((Player)player);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType().toString().toLowerCase().contains("_bed")) {
			
			getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	            @Override
	            public void run() {
	            	Player player = event.getPlayer();
	            	if(player.isOnline())
	            	{
		            	//Location location = event.getClickedBlock().getLocation();
		            	//player.setBedSpawnLocation(location);
		            	
		            	setCompassNavigate(player);
		            	//player.sendMessage(ChatColor.GREEN+"Od teraz twój kompas bêdzie wskazywaæ to miejsce!");
	            	}
	            }
	        }, 50L);
		}
	}
	
	public void setCompassNavigate(Player player) {
        if (player.getBedSpawnLocation() != null)
        	player.setCompassTarget(player.getBedSpawnLocation());
	}*/
}

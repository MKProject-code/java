package other;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CompassOld extends JavaPlugin implements Listener {

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	    System.out.println("[CommpassBed] Hello! My developllo! My developer is MasKAT from skyidea.pl ---> Plugin enabled!");
	}

	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent event) {
		setCompassNavigate((Player)event.getWhoClicked(), event.getCurrentItem().getType());
	}
	
	@EventHandler
	public void inventoryDragEvent(InventoryDragEvent event) {

	}
	
	@EventHandler
	public void inventoryMoveItemEvent(InventoryMoveItemEvent event) {
		System.out.println("[CommpassBed] inventoryMoveItemEvent");
		Bukkit.broadcastMessage("[CommpassBed] inventoryMoveItemEvent");
	}
	
	@EventHandler
	public void inventoryOpenEvent(InventoryOpenEvent event) {
		System.out.println("[CommpassBed] inventoryOpenEvent");
		Bukkit.broadcastMessage("[CommpassBed] inventoryOpenEvent");
	}
	
	@EventHandler
	public void inventoryCloseEvent(InventoryCloseEvent event) {
		System.out.println("[CommpassBed] inventoryCloseEvent");
		Bukkit.broadcastMessage("[CommpassBed] inventoryCloseEvent");

        
        Player player = (Player) event.getPlayer();
        Bukkit.broadcastMessage("[CommpassBed] player: " + player);
        Inventory inv = player.getInventory();

        for(int i = 0; i < inv.getSize(); i++) {
            ItemStack is = inv.getItem(i);
              
            if(is != null) {
                // if an itemstack is null it is empty
              
                if(is.getType() == Material.COMPASS) {
                	Bukkit.broadcastMessage("[CommpassBed] compass exist! 1");
//                    if(is.getAmount() >= 20) {
//                        // player does have 20 wood
//                     }
                 }
            }
        }
        
        ItemStack itemstack = new ItemStack(Material.COMPASS);

        if(!(player.getInventory().contains(itemstack)))
        	Bukkit.broadcastMessage("[CommpassBed] compass exist! 2");
	}
	
	@EventHandler
	public void pickupItemEvent(EntityPickupItemEvent event) {
		setCompassNavigate((Player)event.getEntity(), event.getItem().getItemStack().getType());
	}
	
	public void setCompassNavigate(Player player, Material material) {
		if(player instanceof Player && material.equals(Material.COMPASS)){
            if (player.getBedSpawnLocation() != null) {
            	player.setCompassTarget(player.getBedSpawnLocation());
            	player.sendMessage(ChatColor.YELLOW+"You have picked up "+ material +"!");
            }
            else
            {
            	player.sendMessage(ChatColor.RED + "You don't have a bed");
            }
		}
		
//		if(event.getItem().getItemStack().getType().equals(Material.EMERALD)){
//			if(event.getItem().getItemStack().getItemMeta().hasDisplayName()){
//				if(event.getItem().getItemStack().getItemMeta().getDisplayName().contains("$")){
//					if(event.getEntity() instanceof Player){
//						String name = event.getItem().getItemStack().getItemMeta().getDisplayName();
//						name = name.replace("$", "");
//						((Player)event.getEntity()).sendMessage(ChatColor.YELLOW+"You have picked up $"+ Integer.valueOf(name)+" Dollars!");
//					}
//		            event.setCancelled(true);
//		            event.getItem().remove();
//				}
//			}
//		}
		
		
	}
}

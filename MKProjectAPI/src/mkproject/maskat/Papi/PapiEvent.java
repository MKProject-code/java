package mkproject.maskat.Papi;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import mkproject.maskat.Papi.Model.PapiModel;
import mkproject.maskat.Papi.Utils.Message;

public class PapiEvent implements Listener {

	@EventHandler
	public void onInventoryClickEvent(final InventoryClickEvent e) {
    	HumanEntity humanentity = e.getWhoClicked();
    	if(!(humanentity instanceof Player))
    		return;

    	Player player = (Player)humanentity;
    	
    	if(!Papi.Model.existPlayer(player))
    	{
    		e.setCancelled(true);
    		return;
    	}
    	
    	//MenuManager.onInventoryClick(e, player);
    	Papi.Model.getPlayer(player).onInventoryClick(e);
	}
	@EventHandler
	public void onInventoryDragEvent(final InventoryDragEvent e) {
		HumanEntity humanentity = e.getWhoClicked();
		if(!(humanentity instanceof Player))
			return;
		
		Player player = (Player)humanentity;
		
    	if(!Papi.Model.existPlayer(player))
    	{
    		e.setCancelled(true);
    		return;
    	}
		
		Papi.Model.getPlayer(player).onInventoryDrag(e);
	}
	@EventHandler
	public void onInventoryCloseEvent(final InventoryCloseEvent e) {
    	HumanEntity humanentity = e.getPlayer();
    	if(!(humanentity instanceof Player))
    		return;
    	
    	Player player = (Player)humanentity;
    	
    	if(!Papi.Model.existPlayer(player))
    		return;
    	
    	Papi.Model.getPlayer(player).onInventoryClose(e);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		PapiModel.addPlayer(e.getPlayer());
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		PapiModel.removePlayer(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
        if (e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockZ() == e.getFrom().getBlockZ()) return; //The player hasn't moved
        
        PapiModel.getPlayer(e.getPlayer()).registerAction();
        PapiModel.getPlayer(e.getPlayer()).onPlayerMoveEvent();
    }
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		PapiModel.getPlayer(e.getPlayer()).registerAction();
		PapiModel.getPlayer(e.getPlayer()).onPlayerInteractEvent();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
		Bukkit.getScheduler().runTask(PapiPlugin.getPlugin(), new Runnable() {
	        @Override
	        public void run() {
	        	PapiModel.getPlayer(e.getPlayer()).registerAction();
	        }
	    });
		PapiModel.getPlayer(e.getPlayer()).onAsyncPlayerChat(e);
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onAsyncPlayerChatEventHigh(AsyncPlayerChatEvent e) {
		if(Papi.Model.getPlayer(e.getPlayer()).isMuted())
		{
			String duration = Papi.Model.getPlayer(e.getPlayer()).getMutedRemainingTime();
			if(duration.equals("-"))
				return;
			
			Message.sendMessage(e.getPlayer(), "&c&oZostałeś wyciszony. Nie możesz pisać jeszcze przez &6&o" + duration);
			e.setCancelled(true);
		}
	}
    @EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
    	PapiModel.getPlayer(e.getPlayer()).registerAction();
	}
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleportEvent(PlayerTeleportEvent e) {
    	if(PapiModel.existPlayer(e.getPlayer()) && PapiModel.getPlayer(e.getPlayer()).isLogged())
    		PapiModel.getPlayer(e.getPlayer()).setGlobalLastLocation(e.getFrom());
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeathEvent(PlayerDeathEvent e) {
    	if(PapiModel.existPlayer(e.getEntity()) && PapiModel.getPlayer(e.getEntity()).isLogged())
    		PapiModel.getPlayer(e.getEntity()).setGlobalLastLocation(e.getEntity().getLocation());
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageEvent(EntityDamageEvent e) {
    	if(e.getEntity() instanceof Player)
    	{
    		Player player = (Player)e.getEntity();
    		if(PapiModel.existPlayer(player))
    			PapiModel.getPlayer(player).registerDamage();
    	}
    }
}

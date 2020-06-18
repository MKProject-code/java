package mkproject.maskat.Papi.Model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;

import me.maskat.InventoryManager.InventoryManagerAPI;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.PapiPlugin;
import mkproject.maskat.Papi.PapiWorldEdit;
import mkproject.maskat.Papi.Menu.PapiMenuPage;
import mkproject.maskat.Papi.MenuInventory.MenuPage;

public class PapiPlayer {
	private Player player;
	private LocalDateTime lastAction;
	private boolean afk;
	private boolean logged;
	private LocalDateTime mutedExpires;
	
	private Location playerspawnlocation;
	private boolean playerspawngenerated;
	
	private Location survivalLastLocation;
	private Location globalLastLocation;
	
	private BukkitPlayer worldeditBukkitPlayer;
	
	private JavaPlugin listenChatPluginExecutor;
	private Object listenChatEventUniqueId;
	private Object listenChatStoreObject;
	private boolean listenChatMovmentCancel;
	
	public PapiPlayer(Player p)
	{
		player = p;
		lastAction = LocalDateTime.now();
		afk = false;
		logged = false;
		mutedExpires = null;
		playerspawnlocation = null;
		playerspawngenerated = false;
		survivalLastLocation = null;
		globalLastLocation = null;
		worldeditBukkitPlayer = Papi.WorldEdit.getPlugin().wrapPlayer(player);
		listenChatPluginExecutor = null;
		listenChatEventUniqueId = null;
		listenChatStoreObject = null;
		listenChatMovmentCancel = true;
	}
	
	public String getNameWithPrefix() {
		return Papi.Vault.getChat().getPlayerPrefix(player)+player.getName();
	}
	
	public boolean updateAfkStatus() {
		boolean lastAfk = afk;
		afk = lastAction.plusSeconds(PapiPlugin.getPlugin().getConfig().getInt("Player.GoAfkAfterTime")).compareTo(LocalDateTime.now()) < 0 ? true : false;
		
		if(!lastAfk && afk)
		{
			PapiPlayerChangeAfkEvent event = new PapiPlayerChangeAfkEvent(this, player);
			
			Bukkit.getServer().getPluginManager().callEvent(event);
			
			if (!event.isCancelled()) {
				
			}
		}
		return afk;
	}
	
	public boolean isAfk() {
		return afk;
	}
	
	public boolean isMuted() {
		if(mutedExpires == null)
			return false;
		
		if(mutedExpires.isAfter(LocalDateTime.now()))
			return true;
		
		mutedExpires = null;
		return false;
	}
	public void setMuted(LocalDateTime mutedExpires) {
		this.mutedExpires = mutedExpires;
	}
	
	public void registerAction() {
		if(afk)
		{
			lastAction = LocalDateTime.now();
			afk = false;
			
			PapiPlayerChangeAfkEvent event = new PapiPlayerChangeAfkEvent(this, player);
			
			Bukkit.getServer().getPluginManager().callEvent(event);
			
			if (!event.isCancelled()) {
				
			}
		}
		else
			lastAction = LocalDateTime.now();
	}
	
	//////////////////////
	public boolean isLogged() {
		return logged;
	}
	
	public void registerLogged() {
		if(!logged)
		{
			logged = true;
			
			PapiPlayerLoginEvent event = new PapiPlayerLoginEvent(this, player);
			
			Bukkit.getServer().getPluginManager().callEvent(event);
			
			if (!event.isCancelled()) {
				
			}
		}
//		else
//		{
//			logged = false;
//			PapiPlayerLogoutEvent event = new PapiPlayerLogoutEvent(this, player);
//			
//			Bukkit.getServer().getPluginManager().callEvent(event);
//			
//			if (!event.isCancelled()) {
//				
//			}
//		}
	}
	public void registerFirstLogged() {
		if(!logged)
		{
			logged = true;
			
			PapiPlayerLoginEvent event = new PapiPlayerLoginEvent(this, player);
			
			Bukkit.getServer().getPluginManager().callEvent(event);
			
			if (!event.isCancelled()) {
				
			}
			////////////////////////////
			PapiPlayerFirstLoginEvent eventfirst = new PapiPlayerFirstLoginEvent(this, player);
			
			Bukkit.getServer().getPluginManager().callEvent(eventfirst);
			
			if (!eventfirst.isCancelled()) {
				
			}
		}
	}
	
	public void setSpeedFreeze() {
    	player.setWalkSpeed(0.0f);
    	player.setFlySpeed(0.0f);
    	player.setVelocity(new Vector().zero());
	}
	public void setSpeedDefault() {
		player.setWalkSpeed(0.2f);
		player.setFlySpeed(0.1f);
	}
	
	public void initPlayerSpawnLocation(Location location) {
		playerspawnlocation = location;
	}
	public Location getPlayerSpawnLocation() {
		return playerspawnlocation;
	}
	public void initPlayerSpawnGenerated(boolean b) {
		playerspawngenerated = b;
	}
	public boolean isPlayerSpawnGenerated() {
		return playerspawngenerated;
	}
	
	public Location getRespawnLocation() {
		if(player.getBedSpawnLocation() != null)
			return player.getBedSpawnLocation();
		else if(this.getPlayerSpawnLocation() != null && this.isPlayerSpawnGenerated())
			return this.getPlayerSpawnLocation();
		else
			return Papi.Server.getServerSpawnLocation();
	}
	
	public Location getSurvivalLastLocation() {
		return survivalLastLocation;
	}
	public void initSurvivalLastLocation(Location location) {
		survivalLastLocation = location;
	}
	
	public Location getGlobalLastLocation() {
		return globalLastLocation;
	}
	public void setGlobalLastLocation(Location location) {
		globalLastLocation = location;
	}
	
	public String getAddressIP() {
		return player.getAddress().getAddress().getHostAddress();
	}
	
	public void saveInventory() {
		InventoryManagerAPI.savePlayerInventory(player);
	}
	
	// MenuInventory
	private List<MenuPage> menuPagesList = new ArrayList<MenuPage>();
	
	public MenuPage createMenu(JavaPlugin pluginExecutor, final int numberOfRows, final String colorTitle) {
		return createMenu(pluginExecutor, numberOfRows, colorTitle, null);
	}
	
	public MenuPage createMenu(JavaPlugin pluginExecutor, final int numberOfRows, final String colorTitle, final Object pageUniqueId) {
		MenuPage menuInvPage = new MenuPage(pluginExecutor, numberOfRows, colorTitle, pageUniqueId);
		menuPagesList.add(menuInvPage);
		return menuInvPage;
	}
	
//	public void setMenuItem(final MenuPage menuPage, final InventorySlot menuInvSlot, final Material material, final String colorNameAndLore) {
//		menuPage.setItem(menuInvSlot, material, colorNameAndLore);
//	}
//	
//	public void setMenuItemHeadAsync(final MenuPage menuPage, final InventorySlot menuInvSlot, final Player player, final String colorNameAndLore) {
//		menuPage.setItemHeadAsync(menuInvSlot, player, colorNameAndLore);
//	}
	
	public void openMenu(final MenuPage menuPage) {
		player.openInventory(menuPage.getInventory());
	}
	
	public void openMenu(final PapiMenuPage papiMenuPage) {
		player.openInventory(papiMenuPage.getInventory());
	}
	
	public void onInventoryClick(final InventoryClickEvent e)
	{
		if(e.getInventory().getHolder() instanceof PapiMenuPage)
		{
			if(e.getClickedInventory() != ((PapiMenuPage)e.getInventory().getHolder()).getInventory())
				return;
			
			e.setCancelled(true);
			if(e.getCurrentItem() != null)
				((PapiMenuPage)e.getInventory().getHolder()).onInventoryClick(e, player);
		}
		
		if(e.getInventory().getHolder() instanceof MenuPage)
		{
			e.setCancelled(true);
			if(menuPagesList.contains((MenuPage)e.getInventory().getHolder()))
				((MenuPage)e.getInventory().getHolder()).onInventoryClick(e, player);
		}
	}
	
	public void onInventoryClose(final InventoryCloseEvent e) {
		if(e.getInventory().getHolder() instanceof PapiMenuPage)
		{
			((PapiMenuPage)e.getInventory().getHolder()).onInventoryClose(e, player);
		}
		
		if(!(e.getInventory().getHolder() instanceof MenuPage))
			return;
		
		if(listenChatPluginExecutor != null && listenChatEventUniqueId != null)
			return;
		menuPagesList.remove((MenuPage)e.getInventory().getHolder());
	}
	
	public boolean pasteSchematic(String fileName, Location loc, boolean pasteAir) {
		if(worldeditBukkitPlayer == null)
				return false;
		
		EditSession worldeditEditSession = PapiWorldEdit.pasteSchematic(fileName, loc, pasteAir, true);
		
		if(worldeditEditSession != null) {
			worldeditBukkitPlayer.getSession().remember(worldeditEditSession, true, worldeditBukkitPlayer.getLimit().MAX_HISTORY);
		}
		
		return worldeditEditSession == null ? false : true;
	}
	
	public boolean undoWorldEdit() {
		EditSession worldeditEditSession = worldeditBukkitPlayer.getSession().undo(worldeditBukkitPlayer.getSession().getBlockBag(BukkitAdapter.adapt(player)), BukkitAdapter.adapt(player));
		return worldeditEditSession == null ? false : true;
	}
	
	public boolean redoWorldEdit() {
		EditSession worldeditEditSession = worldeditBukkitPlayer.getSession().redo(worldeditBukkitPlayer.getSession().getBlockBag(BukkitAdapter.adapt(player)), BukkitAdapter.adapt(player));
		return worldeditEditSession == null ? false : true;
	}

	public void listenChat(JavaPlugin pluginExecutor, Object eventUniqueId) {
		 listenChat(pluginExecutor, eventUniqueId, null, true);
	}
	public void listenChat(JavaPlugin pluginExecutor, Object eventUniqueId, boolean movmentCancel) {
		listenChat(pluginExecutor, eventUniqueId, null, movmentCancel);
	}
	public void listenChat(JavaPlugin pluginExecutor, Object eventUniqueId, Object storeObject) {
		listenChat(pluginExecutor, eventUniqueId, storeObject, true);
	}
	public void listenChat(JavaPlugin pluginExecutor, Object eventUniqueId, Object storeObject, boolean movmentCancel) {
		this.listenChatPluginExecutor = pluginExecutor;
		this.listenChatEventUniqueId = eventUniqueId;
		this.listenChatStoreObject = storeObject;
		this.listenChatMovmentCancel = movmentCancel;
	}
	
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
		if(sendEventListenChat(e.getMessage()))
			e.setCancelled(true);
	}
	
	public void onPlayerMoveEvent() {
		if(listenChatMovmentCancel)
			sendEventListenChat(null);
	}

	public void onPlayerInteractEvent() {
		sendEventListenChat(null);
	}
	
	public boolean sendEventListenChat(String message) {
		if(listenChatPluginExecutor == null || listenChatEventUniqueId == null)
			return false;
		
		Bukkit.getScheduler().runTask(PapiPlugin.getPlugin(), new Runnable() {
	        @Override
	        public void run() {
	    		PapiListenChatEvent event = new PapiListenChatEvent(listenChatPluginExecutor, player, message, listenChatEventUniqueId, listenChatStoreObject);
	    		
	    		Bukkit.getServer().getPluginManager().callEvent(event);
	    		
	    		if (!event.isCancelled()) {
	    			listenChatPluginExecutor = null;
	    			listenChatEventUniqueId = null;
	    			listenChatStoreObject = null;
	    			listenChatMovmentCancel = true;
	    		}
	        }
	    });
		return true;
	}

	public String getMutedRemainingTime() {
		if(this.mutedExpires == null)
			return null;
		
		Duration duration = Duration.between(LocalDateTime.now(), this.mutedExpires);
		
	    long seconds = duration.getSeconds();
	    
	    if(seconds <= 0) {
	    	this.mutedExpires = null;
	    	return "-";
	    }
	    
	    if(duration.toDays() < 2) {
	    	long absSeconds = Math.abs(seconds);
	    	long iHour = absSeconds / 3600;
	    	long iMin = (absSeconds % 3600) / 60;
	    	long iSec = absSeconds % 60;
	    	
	    	String positive = "";
	    	if(iHour > 0)
	    		positive += iHour+"h ";
	    	if(iMin > 0)
	    		positive += iMin+"m ";
	    	if(iSec > 0)
	    		positive += iSec+"s";
//		    
//		    String positive = String.format(
//		        "%dh %dm %ds",
//		        absSeconds / 3600,
//		        (absSeconds % 3600) / 60,
//		        absSeconds % 60);
		    return positive.trim();
	    }
	    else
	    	return duration.toDays() + " dni";
	}
}

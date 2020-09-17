package mkproject.maskat.Papi.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;

import me.maskat.InventoryManager.InventoryManagerAPI;
import me.maskat.MoneyManager.Mapi;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.PapiPlugin;
import mkproject.maskat.Papi.PapiWorldEdit;
import mkproject.maskat.Papi.Menu.PapiMenuPage;
import mkproject.maskat.Papi.MenuInventory.MenuPage;
import mkproject.maskat.Papi.Utils.Message;
import mkproject.maskat.StatsManager.API.StatsAPI;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_16_R1.EntityPlayer;

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
	
	private Object worldeditBukkitPlayer;//BukkitPlayer
	
	private JavaPlugin listenChatPluginExecutor;
	private Object listenChatEventUniqueId;
	private Object listenChatStoreObject;
	private boolean listenChatMovmentCancel;
	
	private BukkitTask teleportTask = null;
	
	private Map<Integer, GameProfile> tabListGameProfilesMap = new HashMap<>();
	
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
		PapiPlugin.getPlugin().getLogger().warning("PapiPlugin.getWorldEditPlugin()="+PapiPlugin.getWorldEditPlugin());
		if(PapiPlugin.getWorldEditPlugin() != null)
			worldeditBukkitPlayer = Papi.WorldEdit.getPlugin().wrapPlayer(player);
		else
			worldeditBukkitPlayer = null;
		listenChatPluginExecutor = null;
		listenChatEventUniqueId = null;
		listenChatStoreObject = null;
		listenChatMovmentCancel = true;
	}
	
	public Map<Integer, GameProfile> getTabListGameProfilesMap() {
		return tabListGameProfilesMap;
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
		
		if(teleportTask != null) {
			teleportTask.cancel();
			teleportTask = null;
			Message.sendMessage(player, "&c&oTeleportacja anulowana");
		}
	}
	
	public void registerDamage() {
		if(teleportTask != null) {
			teleportTask.cancel();
			teleportTask = null;
			Message.sendMessage(player, "&c&oTeleportacja anulowana");
		}
	}
	
	//////////////////////
	public boolean isLogged() {
		return logged;
	}
	
	public void registerLogged() {
		if(!logged)
		{
			logged = true;
			
			PapiPlayerLoginEvent event = new PapiPlayerLoginEvent(this, player, false);
			
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
			
			PapiPlayerLoginEvent event = new PapiPlayerLoginEvent(this, player, true);
			
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
	
	public boolean openMenu(final PapiMenuPage papiMenuPage) {
		//this.player.openInventory(papiMenuPage.getInventory());
		return papiMenuPage.openMenu(player);
	}
	public void closeMenu(final PapiMenuPage papiMenuPage) {
		papiMenuPage.closeMenu(player);
	}
	
	public void onInventoryClick(final InventoryClickEvent e)
	{
		if(e.getInventory().getHolder() instanceof PapiMenuPage)
		{
			if(e.getAction() == InventoryAction.COLLECT_TO_CURSOR || e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
				e.setCancelled(true);
			
//			if(e.getClickedInventory() != ((PapiMenuPage)e.getInventory().getHolder()).getInventory())
//				return;
			
			if(e.getClickedInventory() != e.getInventory())
				return;
			
			e.setCancelled(true);
			((PapiMenuPage)e.getInventory().getHolder()).onInventoryClick(e, player);
			
			if(e.getAction() == InventoryAction.COLLECT_TO_CURSOR || e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
				e.setCancelled(true);
		}
		
		if(e.getInventory().getHolder() instanceof MenuPage)
		{
			e.setCancelled(true);
			if(menuPagesList.contains((MenuPage)e.getInventory().getHolder()))
				((MenuPage)e.getInventory().getHolder()).onInventoryClick(e, player);
		}
	}
	

	public void onInventoryDrag(InventoryDragEvent e) {
		if(e.getInventory().getHolder() instanceof PapiMenuPage)
		{
			for(int rawSlot : e.getRawSlots()) {
				if(rawSlot < e.getInventory().getSize()) {
					e.setCancelled(true);
					return;
				}
			}
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
		
		com.sk89q.worldedit.EditSession worldeditEditSession = PapiWorldEdit.pasteSchematic(fileName, loc, pasteAir, true);
		
		if(worldeditEditSession != null) {
			((com.sk89q.worldedit.bukkit.BukkitPlayer)worldeditBukkitPlayer).getSession().remember(worldeditEditSession, true, ((com.sk89q.worldedit.bukkit.BukkitPlayer)worldeditBukkitPlayer).getLimit().MAX_HISTORY);
		}
		
		return worldeditEditSession == null ? false : true;
	}
	
//	public boolean undoWorldEdit() {
//		com.sk89q.worldedit.EditSession worldeditEditSession = ((com.sk89q.worldedit.bukkit.BukkitPlayer)worldeditBukkitPlayer).getSession().undo(((com.sk89q.worldedit.bukkit.BukkitPlayer)worldeditBukkitPlayer).getSession().getBlockBag(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(player)), com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(player));
//		return worldeditEditSession == null ? false : true;
//	}
//	
//	public boolean redoWorldEdit() {
//		com.sk89q.worldedit.EditSession worldeditEditSession = ((com.sk89q.worldedit.bukkit.BukkitPlayer)worldeditBukkitPlayer).getSession().redo(((com.sk89q.worldedit.bukkit.BukkitPlayer)worldeditBukkitPlayer).getSession().getBlockBag(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(player)), com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(player));
//		return worldeditEditSession == null ? false : true;
//	}

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
		
		String remainingTimeStr = Papi.Function.getRemainingTimeString(this.mutedExpires);
		
		if(remainingTimeStr==null)
			return "-";
		
		return remainingTimeStr;
	}
	
	public Economy getEconomy() {
		return Papi.Vault.getEconomy();
	}
	
	public void teleportTimer(Location location) {
		teleportTimerExpLvl(location, 0);
	}
	public void teleportTimerExpLvl(Location location, int paidExpLevel) {
    	if(paidExpLevel > 0 && player.getLevel() < paidExpLevel)
    	{
    		Message.sendMessage(player, "&c&oMusisz mieć conajmniej "+paidExpLevel+" EXP Level aby użyć teleportacji");
    		return;
    	}
    	
		Message.sendMessage(this.player, "&7&oTeleportacja za 5 sekund... Nie ruszaj się");
		if(teleportTask!=null)
			teleportTask.cancel();
		
		teleportTask = Bukkit.getScheduler().runTaskLater(PapiPlugin.getPlugin(), new Runnable() {
		            @Override
		            public void run() {
		            	teleportTask = null;
		            	if(!player.isOnline() || player.isDead())
		            		return;
		            	
		            	if(paidExpLevel > 0 && player.getLevel() < paidExpLevel)
		            	{
		            		Message.sendMessage(player, "&c&oMusisz mieć conajmniej "+paidExpLevel+" EXP Level aby użyć teleportacji");
		            	}
		            	else
		            	{
		            		location.setX(location.getBlockX());
		            		location.setZ(location.getBlockZ());
			            	if(player.teleport(location))
			            	{
			            		if(paidExpLevel>0)
			            			player.setLevel(player.getLevel()-paidExpLevel);
			            		Message.sendMessage(player, "&a&oZostałeś przeteleportowany");
			            	}
			            	else
			            		Message.sendMessage(player, "&c&oTeleportacja nieudana");
		            	}
		           }
				}, 20L*5);
	}
	
	public double getPoints() {
		return Mapi.getPlayer(player).getPoints();
	}
	
	public double addPoints(double points) {
		return Mapi.getPlayer(player).addPoints(points);
	}
	
	public boolean delPoints(double points) {
		return Mapi.getPlayer(player).delPoints(points);
	}
	
	public int getPing() {
	    EntityPlayer entityPlayer = ((CraftPlayer) this.player).getHandle();
	    return entityPlayer.ping;
	}
	
	public StatsPlayer getStats() {
		return new StatsPlayer();
	}

	public class StatsPlayer {
		private mkproject.maskat.StatsManager.Model.StatsPlayer statsPlayer;
		public StatsPlayer() {
			this.statsPlayer = StatsAPI.getStatsPlayer(player);
		}
		public int getKills() {
			return this.statsPlayer.getKills();
		}
		public int getDeaths() {
			return this.statsPlayer.getDeaths();
		}
		public String getLastKiller() {
			return this.statsPlayer.getLastKiller();
		}
	}
}

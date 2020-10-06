package mkproject.maskat.LoginManager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Model.PapiPlayerChangeAfkEvent;
import mkproject.maskat.Papi.Utils.Message;

public class Event implements Listener {
//	@EventHandler
//	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
//		Login.onPlayerCommandPreprocess(e);
//	}
//    @EventHandler(priority = EventPriority.LOWEST)
//	public void onPlayerCommandPreprocessEventLow(PlayerCommandPreprocessEvent e) {
//		Login.onPlayerCommandPreprocess(e);
//	}
	
    @EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocessEventLow(PlayerCommandPreprocessEvent e) {
		Login.onPlayerCommandPreprocessLow(e);
	}
    
//    @EventHandler
//    public void onChunkUnloadEvent(ChunkUnloadEvent e) { 
//    	Bukkit.broadcastMessage("onChunkUnloadEvent");
//    }
    
//    @EventHandler
//    public void onPlayerLoginEvent(PlayerLoginEvent e) { 
//    	Bukkit.broadcastMessage("PlayerLoginEvent");
//    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommandPreprocessEventHigh(PlayerCommandPreprocessEvent e) {
		Login.onPlayerCommandPreprocessHigh(e);
	}
    
//	@EventHandler
//	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
//    	Login.onAsyncPlayerChat(e);
//	}
//    @EventHandler(priority = EventPriority.LOWEST)
//    public void onAsyncPlayerChatEventLow(AsyncPlayerChatEvent e) {
//    	Login.onAsyncPlayerChat(e);
//    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEventLow(AsyncPlayerChatEvent e) {
    	Login.onAsyncPlayerChatLow(e);
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChatEventHigh(AsyncPlayerChatEvent e) {
    	Login.onAsyncPlayerChatHigh(e);
    }
    
    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent e) {
    	if(e.getName() == null || e.getName().length() < 3 || e.getName().length() > 16 || !e.getName().matches("^[a-zA-Z0-9_]*$"))
    	{
    		e.disallow(Result.KICK_OTHER, Message.getColorMessage("&cZły nick! Zasady nicku: od &b3&c do &b16&c znaków, &bA-Z&c, &ba-z&c, &b0-9&c i &b_"));
    		return;
    	}
    	
    	for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
    		if(onlinePlayer.getName().equalsIgnoreCase(e.getName()))
    		{
    			e.disallow(Result.KICK_OTHER, Message.getColorMessage("&cTen nick jest już połączony z serwerem."));
    			return;
    		}
    	}
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEventLow(PlayerJoinEvent e) {
    	PlayerRestoreManager.onPlayerJoinLow(e.getPlayer());
    	
    	if(Model.getPlayer(e.getPlayer()).isRegistered())
    		Task.playerUnloggedTask(e.getPlayer(), Plugin.getPlugin().getConfig().getDouble("Player.UnloggedSendMessageTime"), Plugin.getLanguageYaml().getString("Player.LoginInfoRepeatMessage"));
    	else
    		Task.playerUnloggedTask(e.getPlayer(), Plugin.getPlugin().getConfig().getDouble("Player.UnloggedSendMessageTime"), Plugin.getLanguageYaml().getString("Player.RegisterInfoRepeatMessage"));
    	
    	TitleManager.onPlayerJoin(e);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoinEventHigh(PlayerJoinEvent e) {
    	PlayerRestoreManager.onPlayerJoinHigh(e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSpawnLocationEventLow(PlayerSpawnLocationEvent e) {
    	PlayerRestoreManager.onPlayerSpawnLocation(e);
    }
    
    @EventHandler
    public void onPlayerTeleportEvent(PlayerTeleportEvent e) {
    	if(Model.existPlayer(e.getPlayer()) && Model.getPlayer(e.getPlayer()).isVehicleScheduler() && !Model.getPlayer(e.getPlayer()).isTeleportedWhenIsLogged()) {
    		Model.getPlayer(e.getPlayer()).setTeleportedWhenIsLogged(true);
    		e.getPlayer().setWalkSpeed(0.2f);
    		e.getPlayer().setFlySpeed(0.1f);
    	}
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleportEventHigh(PlayerTeleportEvent e) {
    	if(Model.existPlayer(e.getPlayer()) && !Model.getPlayer(e.getPlayer()).isAferLogin() && !Model.getPlayer(e.getPlayer()).isQuitBeforeLogin())
    		e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
    	
    	PlayerRestoreManager.onPlayerQuit(e.getPlayer());
    	
    	TitleManager.onPlayerQuit(e);
    	
    	if(Papi.Model.getPlayer(e.getPlayer()).isLogged())
    		Database.onPlayerQuit(e.getPlayer());
    }
    
//    @EventHandler
//    public void onPlayerKickEvent(PlayerKickEvent e) {
//    	
//    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerGameModeChangeEventLowest(PlayerGameModeChangeEvent e) {
    	if(Model.existPlayer(e.getPlayer()) && Model.getPlayer(e.getPlayer()).isQuitBeforeLogin())
    		return;
    	
    	if(Papi.Model.getPlayers().contains(e.getPlayer()) && Papi.Model.getPlayer(e.getPlayer()).isLogged() && e.getNewGameMode() == GameMode.SPECTATOR)
    	{
    		for(Player p : Bukkit.getOnlinePlayers())
    		{
    			if(!p.equals(e.getPlayer()) && Model.existPlayer(p) && !Model.getPlayer(p).isAferLogin())
    			{
    				p.hidePlayer(Plugin.getPlugin(), e.getPlayer());
    			}
    		}
    	}
    	
    	if(Model.existPlayer(e.getPlayer()) && Model.getPlayer(e.getPlayer()).isAferLogin())
    		return;
    	
    	if(!Papi.Model.getPlayers().contains(e.getPlayer()))
    	{
    		if(e.getNewGameMode() != GameMode.SPECTATOR)
    		{
    			e.setCancelled(true);
    			//e.getPlayer().setGameMode(GameMode.SPECTATOR);
    		}
    		return;
    	}
    	
    	if(!Papi.Model.getPlayer(e.getPlayer()).isLogged())
    	{
    		if(Model.getPlayer(e.getPlayer()).getGameMode() == null)
    			Model.getPlayer(e.getPlayer()).setGameMode(e.getPlayer().getGameMode());
    		if(e.getNewGameMode() != GameMode.SPECTATOR)
    		{
    			e.setCancelled(true);
    			//e.getPlayer().setGameMode(GameMode.SPECTATOR);
    		}
    	}
    }
    
    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent e) {
    	if(!(e.getPlayer() instanceof Player))
    		return;
    	if(!Papi.Model.getPlayers().contains((Player)e.getPlayer()) || !Papi.Model.getPlayer((Player)e.getPlayer()).isLogged())
    		e.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPapiPlayerChangeAfkEvent(PapiPlayerChangeAfkEvent e) {
    	if(!e.getPapiPlayer().isLogged() && e.getPapiPlayer().isAfk())
    	{
    		e.getPlayer().kickPlayer(Message.getColorMessage(Plugin.getLanguageYaml().getString("Player.KickMessage.Timeout")));
    	}
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
    	if(!Papi.Model.existPlayer(e.getPlayer()))
			e.setCancelled(true);
    	
    	if(!Papi.Model.getPlayer(e.getPlayer()).isLogged())
    	{
    		if(e.getTo().getX() == e.getFrom().getX() && e.getTo().getY() == e.getFrom().getY() && e.getTo().getZ() == e.getFrom().getZ())
    			return; //The player hasn't moved)
    		e.setCancelled(true);
    	}
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
    	if(!Papi.Model.getPlayers().contains(e.getPlayer()) || !Papi.Model.getPlayer(e.getPlayer()).isLogged())
			e.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItemEvent(EntityPickupItemEvent e) {
    	if(!(e.getEntity() instanceof Player))
    		return;
    	
    	if(!Papi.Model.getPlayers().contains((Player)e.getEntity()) || !Papi.Model.getPlayer((Player)e.getEntity()).isLogged())
    		e.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
    	if(!Papi.Model.getPlayers().contains(e.getPlayer()))
			e.setCancelled(true);
    	
    	if(!Papi.Model.getPlayer(e.getPlayer()).isLogged())
    		e.setCancelled(true);
    }
    
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onPlayerAnimationEvent(PlayerAnimationEvent e) {
//    	if(!Papi.Model.getPlayer(e.getPlayer()).isLogged())
//    		e.setCancelled(true);
//    }
    
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onPlayerVelocityEvent(PlayerVelocityEvent e) {
//    	if(!Papi.Model.getPlayer(e.getPlayer()).isLogged())
//    		e.setCancelled(true);
//    }
    
    
}

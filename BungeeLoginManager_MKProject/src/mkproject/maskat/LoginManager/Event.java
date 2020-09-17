package mkproject.maskat.LoginManager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
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

    @EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocessEventLow(PlayerCommandPreprocessEvent e) {
    	Player player = e.getPlayer();
    	
    	if(Papi.Model.existPlayer(player) && Model.getPlayer(player).isLogged())
    		return;
    	
    	e.setCancelled(true);
    	
    	if(player.isOnline()) {
    		Model.getPlayer(player).registerExecuteCmd();
    		Login.onChatSend(player, e.getMessage());
    	}
	}
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerCommandPreprocessEventHigh(PlayerCommandPreprocessEvent e) {
		if(!Papi.Model.existPlayer(e.getPlayer()) || !Model.getPlayer(e.getPlayer()).isLogged())
			e.setCancelled(true);
	}
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
    	e.setCancelled(true);
    	Model.getPlayer(e.getPlayer()).registerExecuteCmd();
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent e) {
    	if(!e.getName().equals("MasKAT")
    			&& !e.getName().equals("Monrimek")
    			&& !e.getName().equals("AKIT96")
    		)
    	{
    		e.disallow(Result.KICK_WHITELIST, "SERWER NIEDOSTĘPNY");
    		return;
    	}
    	
    	
    	if(e.getName() == null || e.getName().length() < 3 || e.getName().length() > 16 || !e.getName().matches("^[a-zA-Z0-9_]*$") || e.getName().charAt(0) == '_' || e.getName().charAt(e.getName().length()-1) == '_' || e.getName().contains("__"))
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
//    	for(Player p : Bukkit.getOnlinePlayers()) {
//    		e.getPlayer().hidePlayer(Plugin.getPlugin(), p);
//    		p.hidePlayer(Plugin.getPlugin(), e.getPlayer());
//    	}
//    	e.getPlayer().setPlayerListHeader("&a*** &bSerwer Skyidea.pl&a ***");
    	
    	Player player = e.getPlayer();
    	
    	if(!Model.existPlayer(player))
    		Model.addPlayer(player);
		
		player.spigot().respawn();
    	
    	player.setWalkSpeed(0);
    	player.setFlySpeed(0);

		player.setGameMode(GameMode.SPECTATOR);
		
    	if(Model.getPlayer(player).isRegistered())
    		Task.playerUnloggedTask(player, Plugin.getPlugin().getConfig().getDouble("Player.UnloggedSendMessageTime"), Plugin.getLanguageYaml().getString("Player.LoginInfoRepeatMessage"));
    	else
    		Task.playerUnloggedTask(player, Plugin.getPlugin().getConfig().getDouble("Player.UnloggedSendMessageTime"), Plugin.getLanguageYaml().getString("Player.RegisterInfoRepeatMessage"));
    	
    	TitleManager.onPlayerJoin(e);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoinEventHigh(PlayerJoinEvent e) {
    	
//		if(ipAddr.equals("192.168.1.15"))
//		{
//			Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
//				@Override
//				public void run() {
//					Login.doRegisterSuccessLoginPlayer(player, null);
//				}
//			}, 5L);
//		}
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSpawnLocationEvent(PlayerSpawnLocationEvent e) {
    	Player player = e.getPlayer();
    	
    	if(!Model.existPlayer(player))
    		Model.addPlayer(player);
    	
		e.setSpawnLocation(Papi.Server.getServerLobbyLocation());
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleportEvent(PlayerTeleportEvent e) {
    		e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
    	Player player = e.getPlayer();
    	
    	player.leaveVehicle();
		
    	TitleManager.onPlayerQuit(e);
    	
    	Model.removePlayer(player);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent e) {
    	if(e.getNewGameMode() != GameMode.SPECTATOR)
			e.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPapiPlayerChangeAfkEvent(PapiPlayerChangeAfkEvent e) {
    	if(!e.getPapiPlayer().isAfk())
    		return;
    	
    	if(!Model.getPlayer(e.getPlayer()).isLogged())
    		e.getPlayer().kickPlayer(Message.getColorMessage(Plugin.getLanguageYaml().getString("Player.KickMessage.Timeout")));
    	else
    		e.getPlayer().kickPlayer(Message.getColorMessage(Plugin.getLanguageYaml().getString("Player.KickMessage.AFK")));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
    	if(e.getTo().getX() == e.getFrom().getX() && e.getTo().getY() == e.getFrom().getY() && e.getTo().getZ() == e.getFrom().getZ())
    		return; //The player hasn't moved
    	e.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
    	e.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPickupItemEvent(EntityPickupItemEvent e) {
    	e.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
		e.setCancelled(true);
    }
}

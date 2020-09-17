package mkproject.maskat.ChatManager;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;

import mkproject.maskat.ChatManager.Models.Model;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Model.PapiPlayerChangeAfkEvent;
import mkproject.maskat.Papi.Utils.Message;
import net.milkbowl.vault.chat.Chat;

public class Event implements Listener {
	
	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
		if(e.getMessage().charAt(0) == '@' && e.getPlayer().hasPermission("mkp.chatmanager.adminchat.send")) {
//			Set<Player> receipes = e.getRecipients();
//	        for(Player p : receipes) {
//	        	if(!p.hasPermission("mkp.adminchat.access"))
//	        		e.getRecipients().remove(p);
//	        }
	        
	        for (Player p : new ArrayList<>(e.getRecipients())) {
	        	if(!p.hasPermission("mkp.chatmanager.adminchat.receive"))
	        		e.getRecipients().remove(p);
	        }
	        
	        e.setMessage(e.getMessage().substring(1).trim());
	        e.setFormat(e.getFormat().replace("{group}", "&4AdminChat").replace("{prefixmessage}", Plugin.getPlugin().getConfig().getString("ChatFormatter.PrefixMessage.AdminChat")));
		}
	}
	
    @EventHandler
    public void onServiceRegisterEvent(ServiceRegisterEvent e) {
        if (e.getProvider().getService() == Chat.class) {
            Plugin.refreshVault();
        }
    }

    @EventHandler
    public void onServiceUnregisterEvent(ServiceUnregisterEvent e) {
        if (e.getProvider().getService() == Chat.class) {
            Plugin.refreshVault();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAsyncPlayerChatEventLow(AsyncPlayerChatEvent e) {
    	if(!e.getPlayer().hasPermission("mkp.chatmanager.disallowedwords.bypass") && !Papi.Model.getPlayer(e.getPlayer()).isMuted())
    	{
    		if(Plugin.getMessageFilter().isDisallowedWords(e.getMessage())) {
	    		Papi.Model.getPlayer(e.getPlayer()).setMuted(LocalDateTime.now().plusSeconds(30L));
	    		Message.sendMessage(e.getPlayer(), Plugin.getLanguageYaml().getString("Player.MuteDisallowedWord"));
	    		e.setCancelled(true);
    		}
    		else if(e.getMessage().equalsIgnoreCase(Model.getPlayer(e.getPlayer()).getLastMessage())) {
	    		Papi.Model.getPlayer(e.getPlayer()).setMuted(LocalDateTime.now().plusSeconds(30L));
	    		Message.sendMessage(e.getPlayer(), Plugin.getLanguageYaml().getString("Player.MuteSpam"));
	    		e.setCancelled(true);
    		}
    	}
    	
    	ChatFormatter.onChatLow(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAsyncPlayerChatEventHigh(AsyncPlayerChatEvent e) {
    	Model.getPlayer(e.getPlayer()).setLastMessage(e.getMessage());
    	ChatFormatter.onChatHigh(e);
    }
    
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
    	Model.addPlayer(e.getPlayer());
    	TabFormatter.onPlayerJoin(e);
    }
    
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
    	TabFormatter.onPlayerQuit(e);
    	Model.removePlayer(e.getPlayer());
    }
//    
//    @EventHandler
//	public static void onPlayerLogin(PapiPlayerLoginEvent e) {
//    	TabFormatter_old.onPlayerLogin(e);
//	}
    
    @EventHandler
    public void onPapiPlayerChangeAfkEvent(PapiPlayerChangeAfkEvent e) {
    	TabFormatter.onPapiPlayerChangeAfk(e);
    }
    
    
    @EventHandler(priority = EventPriority.HIGHEST)
	public static void onPlayerDeathEvent(PlayerDeathEvent e) {
    	TabFormatter.updateKillsDeaths(e.getEntity(), Papi.Model.getPlayer(e.getEntity()).getStats());
    	Player killer = e.getEntity().getKiller();
    	if(killer != null)
    	{
    		TabFormatter.updateKillsDeaths(killer, Papi.Model.getPlayer(killer).getStats());
    		
    		TabFormatter.updateTopPlayersKillsList();
    	}
	}
//    
//    @EventHandler
//    public void onPlayerAdvancementDoneEvent(PlayerAdvancementDoneEvent e) {
//    	Message.sendBroadcast("&8&o"+e.getPlayer().getName()+" zdobył osiągnięcie: "+e.getAdvancement().getKey().getKey()+" ["+String.join(", ", e.getAdvancement().getCriteria())+"]");
//    }
}

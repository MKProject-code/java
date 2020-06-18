package mkproject.maskat.ChatManager;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Model.PapiPlayerChangeAfkEvent;
import mkproject.maskat.Papi.Model.PapiPlayerLoginEvent;
import mkproject.maskat.Papi.Utils.Message;

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
    	ChatFormatter.onServiceChange(e);
    }

    @EventHandler
    public void onServiceUnregisterEvent(ServiceUnregisterEvent e) {
    	ChatFormatter.onServiceChange(e);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAsyncPlayerChatEventLow(AsyncPlayerChatEvent e) {
    	if(!e.getPlayer().hasPermission("mkp.chatmanager.disallowedwords.bypass") && !Papi.Model.getPlayer(e.getPlayer()).isMuted() && Function.isDisallowedWords(e.getMessage()))
    	{
    		Papi.Model.getPlayer(e.getPlayer()).setMuted(LocalDateTime.now().plusSeconds(30L));
    		Message.sendMessage(e.getPlayer(), Plugin.getLanguageYaml().getString("Player.MuteDisallowedWord"));
    		e.setCancelled(true);
    	}
    	
    	ChatFormatter.onChatLow(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAsyncPlayerChatEventHigh(AsyncPlayerChatEvent e) {
    	ChatFormatter.onChatHigh(e);
    }
    
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
    	TabFormatter.onPlayerJoin(e);
    }
    
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
    	TabFormatter.onPlayerQuit(e);
    }
    
    @EventHandler
	public static void onPlayerLogin(PapiPlayerLoginEvent e) {
    	TabFormatter.onPlayerLogin(e);
	}
    
    @EventHandler
    public void onPapiPlayerChangeAfkEvent(PapiPlayerChangeAfkEvent e) {
    	TabFormatter.onPapiPlayerChangeAfk(e);
    }
}

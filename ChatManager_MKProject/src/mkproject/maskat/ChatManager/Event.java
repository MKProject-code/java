package mkproject.maskat.ChatManager;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;

import mkproject.maskat.ChatManager.Models.Model;
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
    	if(!e.getPlayer().hasPermission("mkp.chatmanager.disallowedwords.bypass") && !Papi.Model.getPlayer(e.getPlayer()).isMuted())
    	{
    		String defaultMessage = e.getMessage();
    		
    		List<Character> msgCharArray = defaultMessage.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
    		
    		List<Character> lastChar = new ArrayList<>();
    		
    		lastChar.add(' ');
    		lastChar.add(' ');
    		lastChar.add(' ');
    		
    		int size = msgCharArray.size();
    		
    		for(int i=0; i < size; i++)
    		{
    			lastChar.set(0, lastChar.get(1));
    			lastChar.set(1, lastChar.get(2));
    			lastChar.set(2, msgCharArray.get(i));
    			
    			if(lastChar.get(0) == lastChar.get(1) && lastChar.get(1) == lastChar.get(2) && lastChar.get(2) == msgCharArray.get(i) && !Character.isDigit(msgCharArray.get(i)))
    			{
	    			msgCharArray.remove(i);
	    			i--;
	    			size--;
    			}
    		}
    		
            StringBuilder sb = new StringBuilder();
            
            for (Character ch : msgCharArray) {
                sb.append(ch);
            }
            
            String newMsg = sb.toString();
    		
            if(!newMsg.equals(defaultMessage))
            	Plugin.getPlugin().getLogger().info("Flood detected -> '"+e.getPlayer().getName()+"' trying to send: "+defaultMessage);
            
    		e.setMessage(Normalizer.normalize(newMsg, Normalizer.Form.NFKC));
    		
    		//0 - string is ok
    		//1 - found disallowed world
    		//2 - found URL
    		//3 - found IP
    		int result = Plugin.getMessageFilter().isDisallowedWords(e.getMessage());
    		
    		if(result == 1) {
    			Plugin.getPlugin().getLogger().info("Disallowed words detected -> '"+e.getPlayer().getName()+"' trying to send: "+defaultMessage);
	    		Papi.Model.getPlayer(e.getPlayer()).setMuted(LocalDateTime.now().plusSeconds(30L));
	    		Message.sendMessage(e.getPlayer(), Plugin.getLanguageYaml().getString("Player.MuteDisallowedWord"));
	    		e.setCancelled(true);
    			return;
    		}
    		else if(result == 2) {
    			Plugin.getPlugin().getLogger().info("Address URL detected -> '"+e.getPlayer().getName()+"' trying to send: "+defaultMessage);
    			Papi.Model.getPlayer(e.getPlayer()).setMuted(LocalDateTime.now().plusSeconds(120L));
    			Message.sendMessage(e.getPlayer(), Plugin.getLanguageYaml().getString("Player.MuteDisallowedURL"));
    			e.setCancelled(true);
    			return;
    		}
    		else if(result == 3) {
    			Plugin.getPlugin().getLogger().info("Address IP detected -> '"+e.getPlayer().getName()+"' trying to send: "+defaultMessage);
    			Papi.Model.getPlayer(e.getPlayer()).setMuted(LocalDateTime.now().plusSeconds(120L));
    			Message.sendMessage(e.getPlayer(), Plugin.getLanguageYaml().getString("Player.MuteDisallowedIP"));
    			e.setCancelled(true);
    			return;
    		}
    		else if(e.getMessage().equalsIgnoreCase(Model.getPlayer(e.getPlayer()).getLastMessage())) {
    			Plugin.getPlugin().getLogger().info("Spam detected -> '"+e.getPlayer().getName()+"' trying to send: "+defaultMessage);
	    		Papi.Model.getPlayer(e.getPlayer()).setMuted(LocalDateTime.now().plusSeconds(30L));
	    		Message.sendMessage(e.getPlayer(), Plugin.getLanguageYaml().getString("Player.MuteSpam"));
	    		e.setCancelled(true);
    			return;
    		}
    		
    		String messageValid = Plugin.getDictionaryFilter().getValidMessage(e.getMessage());
    		
    		if(messageValid != null)
    		{
    			Plugin.getPlugin().getLogger().info("Invalid words detected -> '"+e.getPlayer().getName()+"' trying to send: "+defaultMessage);
    			Message.sendMessage(e.getPlayer(), Plugin.getLanguageYaml().getString("Player.InvalidMessage").replace("{validmessage}", messageValid));
    			e.setCancelled(true);
    			return;
    		}
    		
    		if(e.getPlayer().getName().equals("EzzyBoii"))
    			e.setMessage(e.getMessage().replace("vv", "w"));
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
    
    @EventHandler
	public static void onPlayerLogin(PapiPlayerLoginEvent e) {
    	TabFormatter.onPlayerLogin(e);
	}
    
    @EventHandler
    public void onPapiPlayerChangeAfkEvent(PapiPlayerChangeAfkEvent e) {
    	TabFormatter.onPapiPlayerChangeAfk(e);
    }
}

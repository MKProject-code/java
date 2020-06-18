package mkproject.maskat.LoginManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.collect.ImmutableMap;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class TitleManager {

	public static void onPlayerJoin(PlayerJoinEvent e) {
		Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), () -> {
			if(!e.getPlayer().isOnline() || !Papi.Model.getPlayers().contains(e.getPlayer()) || !Model.existPlayer(e.getPlayer()))
				return;
			
            if (Model.getPlayer(e.getPlayer()).isRegistered()) {
            	Plugin.getUnloggedAnimation().play(e.getPlayer(), p -> Papi.Model.getPlayer(e.getPlayer()).isLogged());
            } else {
            	Plugin.getUnregisteredAnimation().play(e.getPlayer(), p -> Papi.Model.getPlayer(e.getPlayer()).isLogged());
            }
        }, 20L);
		
		e.setJoinMessage(null);
	}

    public static void onPlayerLogin(Player player, boolean isFirstLogin) {
		String message = Message.getMessageLang(Plugin.getLanguageYaml(), "Message.PlayerLogged", ImmutableMap.of(
    			"playername", player.getName()
    			));
		
    	Message.sendConsole(message);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!p.equals(player) && Papi.Model.getPlayer(p).isLogged())
			{
		    	Message.sendMessage(p, message);
			}
		}
    	
    	//player.removePotionEffect(PotionEffectType.INVISIBILITY);
    	
    	if(isFirstLogin) {
    		Message.sendBroadcast(Message.getMessageLang(Plugin.getLanguageYaml(), "Message.PlayerFirstLogged", ImmutableMap.of(
    			"playername", player.getName()
    			)));
    		player.giveExpLevels(5);
    	}
    	
        Plugin.getAuthorizedAnimation().play(player.getPlayer(), p -> false);
    }

	public static void onPlayerQuit(PlayerQuitEvent e) {
		if(Papi.Model.getPlayer(e.getPlayer()).isLogged())
		{
	    	e.setQuitMessage(Message.getMessageLang(Plugin.getLanguageYaml(), "Message.PlayerQuit", ImmutableMap.of(
	    			"playername", e.getPlayer().getName()
	    			)));
		}
		else
			e.setQuitMessage(null);
	}
	
}

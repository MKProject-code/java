package mkproject.maskat.ChatManager;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.collect.ImmutableMap;

import me.maskat.MoneyManager.Mapi;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Papi.Vault;
import mkproject.maskat.Papi.Model.PapiPlayerChangeAfkEvent;
import mkproject.maskat.Papi.Model.PapiPlayerLoginEvent;
import mkproject.maskat.Papi.Utils.Message;

public class TabFormatter {

	protected static int onlinePlayersSize = 0;
	
	protected static void onPlayerJoin(PlayerJoinEvent e) {
		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		
		int onlinePlayersSize_update = 0;
		for(Player onlineP : onlinePlayers) {
			if(Papi.Model.getPlayer(onlineP).isLogged())
				onlinePlayersSize_update++;
		}
		onlinePlayersSize = onlinePlayersSize_update;

		updatePlayerListName(e.getPlayer(), false);
		
    	e.getPlayer().setPlayerListFooter(Message.getMessageConfig(Plugin.getPlugin().getConfig(), "TabFormatter.FooterBeforeLogin"));
    	
    	updateHeader(e.getPlayer());
	}
	
	protected static void updateHeader(Player player) {
		player.setPlayerListHeader(Message.getMessageConfig(Plugin.getPlugin().getConfig(), "TabFormatter.Header", ImmutableMap.of(
				"tps", String.format("%.1f", Papi.Function.getTPS()),
				"online_players", String.valueOf(onlinePlayersSize),
				"max_players", String.valueOf(Plugin.getPlugin().getServer().getMaxPlayers())
				)));
	}
	
	protected static void onPlayerLogin(PapiPlayerLoginEvent e) {
		
		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		
		int onlinePlayersSize_update = 0;
		for(Player onlineP : onlinePlayers) {
			if(Papi.Model.getPlayer(onlineP).isLogged())
				onlinePlayersSize_update++;
		}
		onlinePlayersSize = onlinePlayersSize_update;
		
		for(Player onlinePlayer : onlinePlayers) {
			if(Papi.Model.getPlayer(onlinePlayer).isLogged())
				updateHeader(onlinePlayer);
//				onlinePlayer.setPlayerListHeader(Message.getMessageConfig(Plugin.getPlugin().getConfig(), "TabFormatter.Header", ImmutableMap.of(
//						"online_players", String.valueOf(onlinePlayersSize),
//						"max_players", String.valueOf(Plugin.getPlugin().getServer().getMaxPlayers())
//						)));
		}
		updatePlayerListName(e.getPlayer(), true);
    	
    	setPlayerListFooter(e.getPlayer(), Mapi.getPlayer(e.getPlayer()).getPoints());
	}
	
	protected static void updatePlayerListName(Player player, boolean isLogged) {
		if(isLogged)
		{
			player.setPlayerListName(Message.getMessageConfig(Plugin.getPlugin().getConfig(), "TabFormatter.Player", ImmutableMap.of(
					"group", Vault.getChat().getPrimaryGroup(player),
					"prefix", Vault.getChat().getPlayerPrefix(player),
					"name", player.getName(),
		    		"afk", Papi.Model.getPlayer(player).isAfk() ? "AFK" : ""
				)));
		}
		else
		{
			player.setPlayerListName(Message.getMessageConfig(Plugin.getPlugin().getConfig(), "TabFormatter.PlayerBeforeLogin", ImmutableMap.of(
					"name", player.getName()
    			)));
		}
	}

	protected static void onPapiPlayerChangeAfk(PapiPlayerChangeAfkEvent e) {
    	e.getPlayer().setPlayerListName(Message.getMessageConfig(Plugin.getPlugin().getConfig(), "TabFormatter.Player", ImmutableMap.of(
				"group", Vault.getChat().getPrimaryGroup(e.getPlayer()),
				"prefix", Vault.getChat().getPlayerPrefix(e.getPlayer()),
				"name", e.getPlayer().getName(),
    			"afk", e.getPapiPlayer().isAfk() ? "AFK" : ""
				)));
	}

	protected static void onPlayerQuit(PlayerQuitEvent e) {
		
		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		
		int onlinePlayersSize_update = 0;
		for(Player onlineP : onlinePlayers) {
			if(!e.getPlayer().getName().equals(onlineP.getName()) && Papi.Model.getPlayer(onlineP).isLogged())
				onlinePlayersSize_update++;
		}
		onlinePlayersSize = onlinePlayersSize_update;
		
		for(Player onlinePlayer : onlinePlayers) {
			if(!onlinePlayer.getName().equals(e.getPlayer().getName()))
				updateHeader(onlinePlayer);
//			onlinePlayer.setPlayerListHeader(Message.getMessageConfig(Plugin.getPlugin().getConfig(), "TabFormatter.Header", ImmutableMap.of(
//					"online_players", String.valueOf(onlinePlayersSize),
//					"max_players", String.valueOf(Plugin.getPlugin().getServer().getMaxPlayers())
//					)));
		}
	}
	
	protected static void setPlayerListFooter(Player player, double mapiPoints) {
		player.setPlayerListFooter(Message.getMessageConfig(Plugin.getPlugin().getConfig(), "TabFormatter.Footer", ImmutableMap.of(
				"player_mapi_points", String.valueOf((int)mapiPoints),
				"daily_quest", "brak"
				)));
	}
	
}

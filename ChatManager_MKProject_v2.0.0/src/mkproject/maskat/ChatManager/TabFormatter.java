package mkproject.maskat.ChatManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.collect.ImmutableMap;

import me.maskat.MoneyManager.Mapi;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Model.PapiPlayer;
import mkproject.maskat.Papi.Model.PapiPlayer.StatsPlayer;
import mkproject.maskat.Papi.Model.PapiPlayerChangeAfkEvent;
import mkproject.maskat.Papi.TabList.PapiTabList;
import mkproject.maskat.Papi.TabList.TabListFourSlot;
import mkproject.maskat.Papi.Utils.Message;
import mkproject.maskat.StatsManager.API.StatsAPI;
import mkproject.maskat.StatsManager.Model.StatsPlayerOffline;

public class TabFormatter {

//	protected static int onlinePlayersSize = 0;
	private static List<StatsPlayerOffline> topPlayersKillsList = new ArrayList<>();
	
	private static String prefixTitle = "&b&l→ &a&l";
	private static String prefixKey = "&c";
	private static String prefixValue = "&6";
	private static String prefixTopNum = "&c";
	private static String prefixTopNick = "&6";
	private static String prefixTopKills = "&c&o";
	private static String prefixTopValue = "&6&o";
	private static String prefixGroup = "&7&l";
	
	private static Map<Player, Integer> playersTabIds = new HashMap<>();
	
	protected static void initialize() {
		topPlayersKillsList = StatsAPI.getTopPlayersKills();
	}
	
//	protected static void onPlayerJoin(PlayerJoinEvent e) {
//		Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
//			@Override
//			public void run() {
//				Player player = e.getPlayer();
//				
//				if(!player.isOnline())
//					return;
//				
//				PapiTabList.clearTabPlayers(player);
//				PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW1, prefixKey+"Ping: "+prefixValue+Papi.Model.getPlayer(player).getPing()+"ms");
//			}
//		}, 1L);
//	}
	
	protected static void onPlayerQuit(PlayerQuitEvent e) {
		updateOnlinePlayers(e.getPlayer());
	}
	
//	protected static void onPlayerLogin(PapiPlayerLoginEvent e) {
	protected static void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		PapiTabList.Four.addTabsClear(player, true);
		
		player.setPlayerListHeader(Message.getMessageConfig(Plugin.getPlugin().getConfig(), "TabFormatter.Header"));
		player.setPlayerListFooter(Message.getMessageConfig(Plugin.getPlugin().getConfig(), "TabFormatter.Footer"));
		
		PapiPlayer papiPlayer = Papi.Model.getPlayer(player);

		String firstplayed = Papi.Convert.TimestampToDateStringFormat(String.valueOf(player.getFirstPlayed()), "dd.MM.yyyy HH:mm:ss");
		
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW1, prefixTitle+"SERWER");
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW2, prefixKey+"TPS: "+prefixValue+String.format("%.1f", Papi.Function.getTPS()));
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW3, prefixKey+"Ping: "+prefixValue+papiPlayer.getPing()+"ms");
		
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW6, prefixTitle+"STATYSTYKI");
		updateKillsDeaths(player, papiPlayer.getStats());
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW12, prefixKey+"Pierwsza gra:");
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW13, prefixValue+firstplayed);
		
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW15, prefixTitle+"BANK");
		updatePlayerPoints(player, (int)Mapi.getPlayer(player).getPoints());
		
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW19, prefixKey+"Data: "+prefixValue+LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW20, prefixKey+"Godzina: "+prefixValue+LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		
//		String cfgTop = Plugin.getPlugin().getConfig().getString("TabFormatter.TopPlayers");
//		if(cfgTop.equalsIgnoreCase("KILLS")) {
			PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN2_ROW1, prefixTitle+"TOP ZABÓJCY");
			updateTopPlayersKillsList(player, null);
//		}
//		else if(cfgTop.equalsIgnoreCase("ONLINE_TIME")) {
//			PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN2_ROW1, prefixTitle+"TOP ONLINE");
//			updateTopPlayersOnlineList(player, null);
//		}
		
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN3_ROW1, prefixTitle+"GRACZE ONLINE");
		updateOnlinePlayers(null);
	}

	public static void updatePlayerPoints(Player player, int points) {
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW16, prefixKey+"SkyPunkty: "+prefixValue+points);
	}

	protected static void updateTime() {
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		
		for(Player player : Bukkit.getServer().getOnlinePlayers())
		{
			PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW19, prefixKey+"Data: "+prefixValue+date);
			PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW20, prefixKey+"Godzina: "+prefixValue+time);
		}
	}
	
	protected static void updateTPS() {
		String tps = String.format("%.1f", Papi.Function.getTPS());
		
		for(Player player : Bukkit.getServer().getOnlinePlayers())
			PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW2, prefixKey+"TPS: "+prefixValue+tps);
	}
	public static void updatePing(Player player, int ping) {
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW3, prefixKey+"Ping: "+prefixValue+ping+"ms");
		PapiTabList.Four.updateTabColumn(3, playersTabIds.get(player), Message.getMessageConfig(Plugin.getPlugin().getConfig(), "TabFormatter.PlayerName", ImmutableMap.of(
				"prefix", Plugin.getVaultChat().getPlayerPrefix(player),
				"name", player.getName(),
	    		"afk", Papi.Model.getPlayer(player).isAfk() ? "AFK" : ""
			)), ping);
	}

	public static void onPapiPlayerChangeAfk(PapiPlayerChangeAfkEvent e) {
		PapiTabList.Four.updateTabColumn(3, playersTabIds.get(e.getPlayer()), Message.getMessageConfig(Plugin.getPlugin().getConfig(), "TabFormatter.PlayerName", ImmutableMap.of(
				"prefix", Plugin.getVaultChat().getPlayerPrefix(e.getPlayer()),
				"name", e.getPlayer().getName(),
	    		"afk", Papi.Model.getPlayer(e.getPlayer()).isAfk() ? "AFK" : ""
			)), Papi.Model.getPlayer(e.getPlayer()).getPing());
	}
	
	protected static void updateOnlinePlayers(Player playerQuiting) {
		
		int playerOnlineSize = 0;
		
		Map<String, List<Player>> onlinePlayersList = new HashMap<>();
		for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if((playerQuiting == null || !onlinePlayer.getName().equalsIgnoreCase(playerQuiting.getName())) && Papi.Model.getPlayer(onlinePlayer).isLogged())
			{
				String playerGroup = Plugin.getVaultChat().getPrimaryGroup(onlinePlayer);
				if(onlinePlayersList.containsKey(playerGroup))
					onlinePlayersList.get(playerGroup).add(onlinePlayer);
				else
				{
					List<Player> pList = new ArrayList<>();
					pList.add(onlinePlayer);
					onlinePlayersList.put(playerGroup, pList);
				}
				
				playerOnlineSize++;
			}
		}
		
		for(Player playerOnline : Bukkit.getOnlinePlayers()) {
			PapiTabList.Four.updateTab(playerOnline, TabListFourSlot.COLUMN1_ROW4, prefixKey+"Online: "+prefixValue+getOnlinePlayersName(playerOnlineSize));
		}
		
		List<String> groupSortList = Plugin.getPlugin().getConfig().getStringList("TabFormatter.GroupSort");
		
		int i=2;
		
		String groupSelect = null;
		while(groupSelect == null && groupSortList.size() > 0)
		{
			if(onlinePlayersList.containsKey(groupSortList.get(0)))
			{
				groupSelect = groupSortList.get(0);
				
				PapiTabList.Four.updateTabColumn(3, i, prefixGroup+groupSelect);
				i++;
				
				if(i >= 40)
					return;
				
				for(Player playerOnline : onlinePlayersList.get(groupSelect)) {
					PapiTabList.Four.updateTabColumn(3, i, Message.getMessageConfig(Plugin.getPlugin().getConfig(), "TabFormatter.PlayerName", ImmutableMap.of(
							"prefix", Plugin.getVaultChat().getPlayerPrefix(playerOnline),
							"name", playerOnline.getName(),
				    		"afk", Papi.Model.getPlayer(playerOnline).isAfk() ? "AFK" : ""
						)), Papi.Model.getPlayer(playerOnline).getPing());
					
					playersTabIds.put(playerOnline, i);
					
					i++;
					
					if(i >= 40)
						return;
				}
				onlinePlayersList.remove(groupSelect);
			}
			
			groupSortList.remove(0);
		}
		
		
		for(Entry<String, List<Player>> entry : onlinePlayersList.entrySet())
		{
			PapiTabList.Four.updateTabColumn(3, i, prefixGroup+entry.getKey());
			i++;
			
			if(i >= 40)
				return;
			
			for(Player playerOnline : entry.getValue()) {
				
				PapiTabList.Four.updateTabColumn(3, i, Message.getMessageConfig(Plugin.getPlugin().getConfig(), "TabFormatter.PlayerName", ImmutableMap.of(
						"prefix", Plugin.getVaultChat().getPlayerPrefix(playerOnline),
						"name", playerOnline.getName(),
			    		"afk", Papi.Model.getPlayer(playerOnline).isAfk() ? "AFK" : ""
					)), Papi.Model.getPlayer(playerOnline).getPing());
				
				playersTabIds.put(playerOnline, i);
				
				i++;
				
				if(i >= 40)
					return;
			}
		}
		
		while(i<40) {
			PapiTabList.Four.clearTabColumn(3, i);
			i++;
		}
		
		//old
//		List<Player> onlinePlayersLogged = new ArrayList<>();
//		for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
//			if((playerQuiting == null || !onlinePlayer.getName().equalsIgnoreCase(playerQuiting.getName())) && Papi.Model.getPlayer(onlinePlayer).isLogged())
//				onlinePlayersLogged.add(onlinePlayer);
//		}
//		
//		int i=1;
//		for(Player playerOnlineLogged : onlinePlayersLogged) {
//			PapiTabList.Four.updateTab(playerOnlineLogged, TabListFourSlot.COLUMN1_ROW4, prefixKey+"Online: "+prefixValue+getOnlinePlayersName(onlinePlayersLogged.size()));
//			PapiTabList.Four.updateTabColumn(3, i+1, Message.getMessageConfig(Plugin.getPlugin().getConfig(), "TabFormatter.PlayerName", ImmutableMap.of(
//					"group", Plugin.getVaultChat().getPrimaryGroup(playerOnlineLogged),
//					"prefix", Plugin.getVaultChat().getPlayerPrefix(playerOnlineLogged),
//					"name", playerOnlineLogged.getName(),
//		    		"afk", Papi.Model.getPlayer(playerOnlineLogged).isAfk() ? "AFK" : ""
//				)), Papi.Model.getPlayer(playerOnlineLogged).getPing());
//			
//			i++;
//		}
	}

	private static String getOnlinePlayersName(int onlinePlayersSize) { 
		String str = "graczy";
		if(onlinePlayersSize == 1)
			str = "gracz";
		else if(onlinePlayersSize >= 2 && onlinePlayersSize <= 4)
			str = "gracze";
		return onlinePlayersSize + " " +str;
	}

	public static void updateKillsDeaths(Player player, StatsPlayer statsPlayer) {
		int kills = statsPlayer.getKills();
		int deaths = statsPlayer.getDeaths();
		String killer = statsPlayer.getLastKiller() != null ? statsPlayer.getLastKiller() : "-";
		double kdr = (deaths<=0) ? kills : (double)kills/(double)deaths;
		
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW7, prefixKey+"Zabicia: "+prefixValue+kills);
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW8, prefixKey+"Zgony: "+prefixValue + deaths);
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW9, prefixKey+"KDR: "+prefixValue+String.format("%.1f", kdr));
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW10, prefixKey+"Ostatni zabójca:");
		PapiTabList.Four.updateTab(player, TabListFourSlot.COLUMN1_ROW11, prefixValue+killer);
	}

	public static void updateTopPlayersKillsList() {
		List<StatsPlayerOffline> topPlayersKillsListTemp = StatsAPI.getTopPlayersKills();
		
		for(Player playerOnline : Bukkit.getOnlinePlayers())
		{
			if(Papi.Model.getPlayer(playerOnline).isLogged())
				updateTopPlayersKillsList(playerOnline, topPlayersKillsListTemp);
		}
		topPlayersKillsList = topPlayersKillsListTemp;
	}
	
	public static void updateTopPlayersKillsList(Player player, List<StatsPlayerOffline> topPlayersKillsListTemp)
	{
		int iNum=1;
		int iRow=2;
		for(StatsPlayerOffline topPlayerKills : topPlayersKillsList) {
			if(topPlayersKillsListTemp == null)
			{
				PapiTabList.Four.updateTabColumn(player, 2, iRow, prefixTopNum+iNum+". "+prefixTopNick+topPlayerKills.getPlayerName());
				iRow++;
				PapiTabList.Four.updateTabColumn(player, 2, iRow, prefixTopKills+"Zabić: "+prefixTopValue+topPlayerKills.getKills());
			}
			else
			{
				StatsPlayerOffline topPlayerKillsTemp = topPlayersKillsListTemp.get(iNum-1);
				if(!topPlayerKills.getPlayerName().contains(topPlayerKillsTemp.getPlayerName()) || topPlayerKills.getKills() != topPlayerKillsTemp.getKills())
				{
					PapiTabList.Four.updateTabColumn(player, 2, iRow, prefixTopNum+iNum+". "+prefixTopNick+topPlayerKills.getPlayerName());
					iRow++;
					PapiTabList.Four.updateTabColumn(player, 2, iRow, prefixTopKills+"Zabić: "+prefixTopValue+topPlayerKills.getKills());
				}
				else
					iRow++;
			}
			iRow++;
			iNum++;
		}
	}


	
	
//	private static void updateTopPlayersOnlineList(Player player, List<StatsPlayerOffline> topPlayersKillsListTemp) {
//		int iNum=1;
//		int iRow=2;
//		for(StatsPlayerOffline topPlayerKills : topPlayersKillsList) {
//			if(topPlayersKillsListTemp == null)
//			{
//				PapiTabList.Four.updateTabColumn(player, 2, iRow, prefixTopNum+iNum+". "+prefixTopNick+topPlayerKills.getPlayerName());
//				iRow++;
//				PapiTabList.Four.updateTabColumn(player, 2, iRow, prefixTopKills+"Czas online: "+prefixTopValue+topPlayerKills.getKills());
//			}
//			else
//			{
//				StatsPlayerOffline topPlayerKillsTemp = topPlayersKillsListTemp.get(iNum-1);
//				if(!topPlayerKills.getPlayerName().contains(topPlayerKillsTemp.getPlayerName()) || topPlayerKills.getKills() != topPlayerKillsTemp.getKills())
//				{
//					PapiTabList.Four.updateTabColumn(player, 2, iRow, prefixTopNum+iNum+". "+prefixTopNick+topPlayerKills.getPlayerName());
//					iRow++;
//					PapiTabList.Four.updateTabColumn(player, 2, iRow, prefixTopKills+"Czas online: "+prefixTopValue+topPlayerKills.getKills());
//				}
//				else
//					iRow++;
//			}
//			iRow++;
//			iNum++;
//		}
//	}
}

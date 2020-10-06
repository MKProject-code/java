package me.maskat.QuestManager.Quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;

import me.maskat.QuestManager.Database;
import me.maskat.QuestManager.Plugin;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.BoardManager.PapiBoard;
import mkproject.maskat.Papi.BoardManager.PapiObjective;

public class QuestManager {

	private PapiObjective papiObjective;
	private Quest currentQuest;
	
	private Map<Integer, Integer> scoreBoardPos = new HashMap<>(); //TODO propably remove this!
	
	private enum QuestClasses {
		KillEntity
	}
	
	public QuestManager() {
		this.doNewQuest();
		
		this.papiObjective = (new PapiBoard()).registerNewObjective​("questTop", "&8» &6Wyzwanie dnia&8 «", RenderType.INTEGER, DisplaySlot.SIDEBAR);
		
		scoreBoardPos.put(1, 0);
		scoreBoardPos.put(2, 0);
		scoreBoardPos.put(3, 0);
		scoreBoardPos.put(4, 0);
		scoreBoardPos.put(5, 0);
		scoreBoardPos.put(6, 0);
		scoreBoardPos.put(7, 0);
		scoreBoardPos.put(8, 0);
		scoreBoardPos.put(9, 0);
		scoreBoardPos.put(10, 0);
		
		for(Entry<Integer, Integer> entry : scoreBoardPos.entrySet())
			this.papiObjective.addScore(entry.getKey().toString(), entry.getKey().toString()+". &r-", 1);
		
//		this.papiObjective..addScore(entry.getKey().toString(), "&a1. &r-", 0);
	}
	
	public void doNewQuest() {
		
		switch((QuestClasses) Papi.Function.getRandom(QuestClasses.values()))
		{
			case KillEntity:
				currentQuest = new KillEntity();
				break;
			default:
				currentQuest = null;
				break;
		}
	}
	
	public Quest getCurrentQuest() {
		return currentQuest;
	}

	public void assignScoreBoard(Player player) {
		this.papiObjective.getBoard().assignToPlayer(player);
	}

	public boolean isValidSurvival(World world) {
		if(Papi.Server.isSurvivalWorld(world, false, false))
			return true;
		
		return false;
	}
	
	public boolean isValidEvent(Player killer) {
		World world = killer.getWorld();
		
		if(killer.getGameMode() == GameMode.SURVIVAL && this.isValidSurvival(world))
			return true;
		
		return false;
	}

	public void doUpdateScoreboard() {
		Set<String> playersSet = Database.getPlayers();
		if(playersSet != null) {
			
			Map<String, Integer> playersTop = new HashMap<>();
			
			for(String playerNameLowerCase : playersSet) {
				String p = Database.getRealName(playerNameLowerCase);
				int s = Database.getScore(playerNameLowerCase);
				
				playersTop.put(p, s);
			}
			
	        List<Entry<String, Integer>> playersTopList = new ArrayList<>(playersTop.entrySet());
	        playersTopList.sort(Entry.comparingByValue());
	        
//	        Plugin.getQuestManager().updateScoreboard(playersTopList);
			int i=1;
			for(Entry<String, Integer> entry : playersTopList)
			{
				this.papiObjective.setScore(String.valueOf(i), "&l"+i+". &r"+entry.getKey(), entry.getValue());
				i++;
			}
		}
	}

}

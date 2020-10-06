package me.maskat.QuestManager.Model;

import org.bukkit.entity.Player;

import me.maskat.QuestManager.Database;
import me.maskat.QuestManager.Plugin;

public class QPlayer {

	private Player player;
	private int score;
	
	public QPlayer(Player player) {
		this.player = player;
		this.score = Database.getScore(this.player);
		Plugin.getQuestManager().assignScoreBoard(this.player);
	}

	public void addScore() {
		this.score++;
		
		Database.setScore(this.player, this.score);
		
		Plugin.getQuestManager().doUpdateScoreboard();
	}
	
	public int getScore() {
		return this.score;
	}
}

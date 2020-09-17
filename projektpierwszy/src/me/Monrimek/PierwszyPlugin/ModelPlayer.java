package me.Monrimek.PierwszyPlugin;

import org.bukkit.entity.Player;

public class ModelPlayer {

	private Player player;
	private int score;
	
	public ModelPlayer(Player player) {
		this.player = player;
		this.score = 0;
	}
	
	public void addScore() {
		this.score++;
	}
	
}

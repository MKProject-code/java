package me.maskat.ArenaManager.ArenaPlugin;

import me.maskat.ArenaManager.Models.ModelArena;

public class EndArenaGameEvent {
	private ModelArena modelArena;
	private ArpTeam winnerTeam;
	private ArpPlayer winnerPlayer;
	private long kickPlayersTime;
	
	public EndArenaGameEvent(ModelArena modelArena, ArpTeam winnerTeam, ArpPlayer winnerPlayer) {
		this.modelArena = modelArena;
		this.winnerTeam = winnerTeam;
		this.winnerPlayer = winnerPlayer;
		this.kickPlayersTime = 3L;
	}

	public ArpArena getArena() {
		return this.modelArena.getArenaPluginInstanceArpArena();
	}

	public void setKickPlayersTime(long kickPlayersTime) {
		this.kickPlayersTime = kickPlayersTime;
	}
	
	public long getKickPlayersTime() {
		return this.kickPlayersTime;
	}
	
	public ArpTeam getWinnerTeam() {
		return this.winnerTeam;
	}
	public ArpPlayer getWinnerPlayer() {
		return this.winnerPlayer;
	}
}

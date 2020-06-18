package me.maskat.ArenaManager.ArenaPlugin;

import java.util.Collection;

import me.maskat.ArenaManager.Models.ModelArena;

public class StartArenaGameEvent {
	private ModelArena modelArena;
	
	public StartArenaGameEvent(ModelArena modelArena) {
		this.modelArena = modelArena;
	}

	public ArpArena getArena() {
		return this.modelArena.getArenaPluginInstanceArpArena();
	}
	
	public int getPlayedRound() {
		return this.modelArena.getPlayedRound();
	}

	public ArpTeam getTeam(String teamType) {
		return this.modelArena.getArenaPluginInstanceArpTeam(teamType);
	}
	public Collection<ArpTeam> getTeams() {
		return this.modelArena.getArenaPluginInstanceArpTeams();
	}
}

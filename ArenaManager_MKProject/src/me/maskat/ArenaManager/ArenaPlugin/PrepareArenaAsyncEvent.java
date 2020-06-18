package me.maskat.ArenaManager.ArenaPlugin;

import java.util.Collection;

import me.maskat.ArenaManager.Models.ModelArena;

public class PrepareArenaAsyncEvent {
	private ModelArena modelArena;

	public PrepareArenaAsyncEvent(ModelArena modelArena) {
		this.modelArena = modelArena;
	}

	public ArpArena getArena() {
		return modelArena.getArenaPluginInstanceArpArena();
	}
	
	public ArpTeam getTeam(String teamType) {
		return modelArena.getArenaPluginInstanceArpTeam(teamType);
	}
	public Collection<ArpTeam> getTeams() {
		return modelArena.getArenaPluginInstanceArpTeams();
	}
}

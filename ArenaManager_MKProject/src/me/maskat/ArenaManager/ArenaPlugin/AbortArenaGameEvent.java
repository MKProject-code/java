package me.maskat.ArenaManager.ArenaPlugin;

import java.util.Collection;

import org.bukkit.entity.Player;

import me.maskat.ArenaManager.Models.ModelArena;

public class AbortArenaGameEvent {
	private ModelArena modelArena;
	
	public AbortArenaGameEvent(ModelArena modelArena) {
		this.modelArena = modelArena;
	}
	
	public ArpArena getArena() {
		return this.modelArena.getArenaPluginInstanceArpArena();
	}
	public ArpTeam getTeam(String teamType) {
		return this.modelArena.getArenaPluginInstanceArpTeam(teamType);
	}
	public Collection<ArpTeam> getTeams() {
		return this.modelArena.getArenaPluginInstanceArpTeams();
	}
	public ArpPlayer getPlayer(Player player) {
		return this.modelArena.getArenaPluginInstanceArpPlayer(player);
	}
	public Collection<ArpPlayer> getPlayers() {
		return this.modelArena.getArenaPluginInstanceArpPlayers();
	}
	public Collection<ArpPlayer> getPlayersOnline() {
		return this.modelArena.getArenaPluginInstanceArpPlayersOnline();
	}
}

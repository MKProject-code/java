package me.maskat.ArenaManager.ArenaPlugin;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.maskat.ArenaManager.Models.ModelArena;

public class PlayerRespawnInArenaEvent {
	
	private Player player;
	private ModelArena modelArena;
	
	private Location respawnPlayerLocation;

	public PlayerRespawnInArenaEvent(Player player, ModelArena modelArena) {
		this.player = player;
		this.modelArena = modelArena;
		
		this.respawnPlayerLocation = this.getPlayer().getTeam().getOneRandomLocation();
	}

	public ArpArena getArena() {
		return this.modelArena.getArenaPluginInstanceArpArena();
	}
	
	public ArpPlayer getPlayer() {
		return this.modelArena.getArenaPluginInstanceArpPlayer(player);
	}

	public Location getRespawnPlayerLocation() {
		return this.respawnPlayerLocation;
	}
	public void setRespawnPlayerLocation(Location respawnPlayerLocation) {
		this.respawnPlayerLocation = respawnPlayerLocation;
	}

	public ArpTeam getTeam(String teamType) {
		return this.modelArena.getArenaPluginInstanceArpTeam(teamType);
	}
	public Collection<ArpTeam> getTeams() {
		return this.modelArena.getArenaPluginInstanceArpTeams();
	}
}

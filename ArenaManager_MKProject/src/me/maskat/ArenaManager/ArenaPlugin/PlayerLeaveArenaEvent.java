package me.maskat.ArenaManager.ArenaPlugin;

import java.util.Collection;

import org.bukkit.entity.Player;

import me.maskat.ArenaManager.Models.ModelArena;

public class PlayerLeaveArenaEvent {

	private ModelArena modelArena;
	private Player player;
//	private Location teleportLocation;
	//private GameMode newGamemode;

	public PlayerLeaveArenaEvent(Player player, ModelArena modelArena) {
		this.player = player;
		this.modelArena = modelArena;
//		if(Papi.Model.getPlayer(player).getSurvivalLastLocation() != null)
//			this.teleportLocation = Papi.Model.getPlayer(player).getSurvivalLastLocation();
//		else
//			this.teleportLocation = Papi.Server.getServerSpawnLocation();
		//this.newGamemode = GameMode.SURVIVAL;
	}

	public ArpArena getArena() {
		return this.modelArena.getArenaPluginInstanceArpArena();
	}
	
	public ArpPlayer getPlayer() {
		return this.modelArena.getArenaPluginInstanceArpPlayer(player);
	}
	
	public ArpTeam getTeam(String teamType) {
		return this.modelArena.getArenaPluginInstanceArpTeam(teamType);
	}
	public Collection<ArpTeam> getTeams() {
		return this.modelArena.getArenaPluginInstanceArpTeams();
	}
	
//	public Location getTeleportLocation() {
//		return this.teleportLocation;
//	}
//	public void setTeleportLocation(Location teleportLocation) {
//		this.teleportLocation = teleportLocation;
//	}
	
//	public GameMode getNewGameMode() {
//		return this.newGamemode;
//	}
//	public void setNewGameMode(GameMode newGamemode) {
//		this.newGamemode = newGamemode;
//	}

}

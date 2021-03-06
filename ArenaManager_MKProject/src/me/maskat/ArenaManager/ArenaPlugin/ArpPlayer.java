package me.maskat.ArenaManager.ArenaPlugin;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.maskat.ArenaManager.ArenaManager.Manager;
import me.maskat.ArenaManager.Models.ModelArena;
import me.maskat.ArenaManager.Models.ModelArenaPlayer;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class ArpPlayer {
	private Player player;
	private ModelArenaPlayer modelArenaPlayer;
	private ModelArena modelArena;
	private ArpTeam arpTeam;
	private boolean insideArena;
	private boolean quitedServer;
//	private boolean allowTeleport;
	
	public ArpPlayer(ModelArenaPlayer modelArenaPlayer, ArpTeam arpTeam, ModelArena modelArena) {
		this.player = modelArenaPlayer.getPlayer();
		this.modelArenaPlayer = modelArenaPlayer;
		this.modelArena = modelArena;
		this.arpTeam = arpTeam;
		this.insideArena = true;
		this.quitedServer = false;
//		this.allowTeleport = true;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public boolean isQuitedServer() {
		return this.quitedServer;
	}
	public boolean setQuitedServer(boolean quitedServer) {
		return this.quitedServer;
	}
	
	public ArpArena getArena() {
		return this.modelArena.getArenaPluginInstanceArpArena();
	}
	
	public ArpTeam getTeam() {
		return this.arpTeam;
	}
	
	public boolean isOnline() {
		return this.player.isOnline();
	}
	
	public boolean isInsideArena() {
		return this.insideArena;
	}
	
	public boolean isFreeze() {
		return this.modelArenaPlayer.isFreeze();
	}
	public void setFreeze(boolean freeze) {
		this.modelArenaPlayer.setFreeze(freeze);
	}

	public void leaveArena() {
		if(insideArena == false)
			return;
		
		this.modelArenaPlayer.unsetRegisteredArena();
		this.insideArena = false;
		
		PlayerLeaveArenaEvent event = new PlayerLeaveArenaEvent(this.player, this.modelArena);
		
		this.modelArena.getArenaPluginInstance().onPlayerLeaveArena(event);
		
		Location teleportLocation;
		if(Papi.Model.getPlayer(this.player).getSurvivalLastLocation() != null)
			teleportLocation = Papi.Model.getPlayer(this.player).getSurvivalLastLocation();
		else
			teleportLocation = Papi.Server.getServerSpawnLocation();
		
		if(this.player.isDead())
			this.modelArenaPlayer.doLeaveRespawn(teleportLocation);
		else
			this.player.teleport(teleportLocation);
		
		//player.setGameMode(event.getNewGameMode());
		this.player.setFireTicks(0);
	}

	public boolean isGodmode() {
		return this.modelArenaPlayer.isGodmode();
	}
	public void setGodmode(boolean godmode) {
		this.modelArenaPlayer.setGodmode(godmode);
	}
	
	public void doWinnerGame() {
		Message.sendTitle(this.player, null, "&a--- ZWYCIĘSTWO ---");
		Manager.doEndGame(this.modelArena, this.arpTeam, this);
	}

	public void setAllowTeleport(boolean allowTeleport) {
		this.modelArenaPlayer.setAllowTeleport(allowTeleport);
	}
	public boolean isAllowTeleport() {
		return this.modelArenaPlayer.isAllowTeleport();
	}

	public void playSound(Sound sound) {
		this.player.playSound(this.player.getLocation(), sound, 1, 0);
	}
	
	public void sendTitle(String title, String subtitle) {
		Message.sendTitle(this.player, title, subtitle);
	}
}

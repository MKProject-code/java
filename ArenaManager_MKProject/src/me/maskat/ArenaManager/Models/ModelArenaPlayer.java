package me.maskat.ArenaManager.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.maskat.ArenaManager.PlayerMenu.PlayerMenuMain;
import mkproject.maskat.Papi.Papi;

public class ModelArenaPlayer {
	private Player player;
	private int registeredArenaId = 0;
	private int registeredTeamId = 0;
//	private int playedInsideArenaId = 0;
//	private int playedInsideTeamId = 0;
	//ArenaPlayed arenaPlayed = null;
	private boolean freeze = false;
	private boolean godmode = false;
	private boolean blockArenaMenu = false;
	private boolean allowTeleport = true;
	
	private ModelArena leavePlayedInsideArena = null;
	
	public ModelArenaPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
//	public void setPlayedInsideArenaId(int arenaId) {
//		this.playedInsideArenaId = arenaId;
//	}
//	public void unsetPlayedInsideArena() {
//		this.playedInsideArenaId = 0;
//		this.playedInsideTeamId = 0;
//		this.blockArenaMenu = false;
//	}
//	public int getPlayedInsideArenaId() {
//		return this.playedInsideArenaId;
//	}
//	public void setRegisteredArenaId(int arenaId) {
//		this.registeredArenaId = arenaId;
//	}
	public void unsetRegisteredArena() {
		this.registeredArenaId = 0;
		this.registeredTeamId = 0;
		this.blockArenaMenu = false;
		this.freeze = false;
		this.godmode = false;
		this.allowTeleport = true;
	}
	public int getRegisteredArenaId() {
		return this.registeredArenaId;
	}
//	public ModelArena getPlayedInsideArena() {
//		return ArenesModel.getArena(this.playedInsideArenaId);
//	}
	public ModelArena getRegisteredArena() {
		return ArenesModel.getArena(this.registeredArenaId);
	}
//	public boolean isPlayedInsideArena() {
//		return (this.playedInsideArenaId > 0) ? true : false;
//	}
	public boolean isRegisteredArena() {
		return (this.registeredArenaId > 0 && this.registeredTeamId > 0) ? true : false;
	}
//	public void setPlayedInsideTeamId(int arenaId, int teamId) {
//		this.playedInsideArenaId = arenaId;
//		this.playedInsideTeamId = teamId;
//	}
//	public void unsetPlayedInsideTeam() {
//		this.playedInsideTeamId = 0;
//	}
//	public int getPlayedInsideTeamId() {
//		return this.playedInsideTeamId;
//	}
	public void setRegisteredTeamId(int arenaId, int teamId) {
		this.registeredArenaId = arenaId;
		this.registeredTeamId = teamId;
	}
//	public void unsetRegisteredTeam() {
//		this.registeredTeamId = 0;
//	}
	public boolean setRegisteredTeamRandom(ModelArena arenaModel) {
		List<ModelArenaTeam> arenaTeams = new ArrayList<>();
		for(Entry<Integer, ModelArenaTeam> entry : arenaModel.getTeamsMap().entrySet()) {
			if(entry.getValue().getMaxPlayers() > 0 && entry.getValue().getPlayersRegistered().size() < entry.getValue().getMaxPlayers())
				arenaTeams.add(entry.getValue());
		}
		
	    Random rand = new Random();
	    int randomIndex = rand.nextInt(arenaTeams.size());
	    
	    this.setRegisteredTeamId(arenaModel.getId(), arenaTeams.get(randomIndex).getId());
	    
	    return true;
	}
	public int getRegisteredTeamId() {
		return this.registeredTeamId;
	}
//	public ModelArenaTeam getPlayedInsideTeam() {
//		return ArenesModel.getArena(this.playedInsideArenaId).getTeam(this.playedInsideTeamId);
//	}
	public ModelArenaTeam getRegisteredTeam() {
		return ArenesModel.getArena(this.registeredArenaId).getTeam(this.registeredTeamId);
	}
//	public boolean isPlayedInsideTeam() {
//		return (this.playedInsideTeamId > 0) ? true : false;
//	}
//	public boolean isRegisteredTeam() {
//		return (this.registeredTeamId > 0) ? true : false;
//	}
	public void openArenesMenu() {
		PlayerMenuMain.openMenu(player);
	}

//	public void changeStatusToPlayedInsideArena() {
//		if(registeredArenaId == 0 || registeredTeamId == 0)
//			return;
//		this.setPlayedInsideTeamId(registeredArenaId, registeredTeamId);
//		this.unsetRegisteredArena();
//	}
//
//	public void setPlayedInsideArenaPlayed(ArenaPlayed arenaPlayed) {
//		this.arenaPlayed = arenaPlayed;
//	}
//	
//	public ArenaPlayed getPlayedInsideArenaPlayed() {
//		return this.arenaPlayed;
//	}

	public void setFreeze(boolean freeze) {
		this.freeze  = freeze;
		if(freeze == true)
		{
			Papi.Model.getPlayer(this.getPlayer()).setSpeedFreeze();
//			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128));
		}
		else
		{
			Papi.Model.getPlayer(this.getPlayer()).setSpeedDefault();
//			player.removePotionEffect(PotionEffectType.JUMP);
		}
	}

	public boolean isFreeze() {
		return this.freeze;
	}

	public void setGodmode(boolean godmode) {
		this.godmode  = godmode;
	}

	public boolean isGodmode() {
		return this.godmode;
	}

	public ModelArena getLeavePlayedInsideArena() {
		return leavePlayedInsideArena;
	}

	public void setLeavePlayedInsideArena(ModelArena modelArena) {
		this.leavePlayedInsideArena = modelArena;
	}

	public void setBlockArenaMenu(boolean blockArenaMenu) {
		 this.blockArenaMenu =  blockArenaMenu;
	}
	
	public boolean isBlockArenaMenu() {
		return this.blockArenaMenu;
	}

	public void setAllowTeleport(boolean allowTeleport) {
		this.allowTeleport = allowTeleport;
	}
	
	public boolean isAllowTeleport() {
		return this.allowTeleport;
	}

	private Location leaveRespawnTeleportLocation = null;
	public void doLeaveRespawn(Location leaveRespawnTeleportLocation) {
		if(!player.isDead())
			return;
		
		this.leaveRespawnTeleportLocation = leaveRespawnTeleportLocation;
		player.spigot().respawn();
	}
	
	public Location getLeaveRespawnTeleportLocation() {
		if(leaveRespawnTeleportLocation == null)
			return null;
		Location tempLoc = leaveRespawnTeleportLocation.clone();
		this.leaveRespawnTeleportLocation = null;
		return tempLoc;
	}
}

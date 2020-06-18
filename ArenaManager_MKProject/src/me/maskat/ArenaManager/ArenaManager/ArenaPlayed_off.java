//package me.maskat.ArenaManager.ArenaManager;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//
//import me.maskat.ArenaManager.ArenaPlugin.ArpArena;
//import me.maskat.ArenaManager.ArenaPlugin.ArpTeam;
//import me.maskat.ArenaManager.Models.ModelArena;
//import me.maskat.ArenaManager.Models.ModelArenaPlayer;
//import mkproject.maskat.Papi.Papi;
//
//public class ArenaPlayed_off {
//
//	private ModelArena modelArena;
//	private ArpArena arena;
//	private Map<String, ArpTeam> teamsMap;
//	
//	public ArenaPlayed_off(ModelArena modelArena, ArpArena arena, Map<String, ArpTeam> teamsMap) {
//		this.modelArena = modelArena;
//		this.arena = arena;
//		this.teamsMap = teamsMap;
//	}
//
//	public ArpArena getArena() {
//		return this.arena;
//	}
//
//	public ArpTeam getTeam(String teamType) {
//		return this.teamsMap.get(teamType);
//	}
//	public Collection<ArpTeam> getTeams() {
//		return this.teamsMap.values();
//	}
//	
//	public void unfreezePlayers() {
//		List<ModelArenaPlayer> modelArenaPlayers = modelArena.getPlayersPlayedInside();
//		for(ModelArenaPlayer modelArenaPlayer : modelArenaPlayers) {
//			Papi.Model.getPlayer(modelArenaPlayer.getPlayer()).setSpeedDefault();
//		}
//	}
//
//}

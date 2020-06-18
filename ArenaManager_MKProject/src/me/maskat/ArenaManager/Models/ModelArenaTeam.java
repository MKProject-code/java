package me.maskat.ArenaManager.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;

import me.maskat.ArenaManager.Database;
import mkproject.maskat.Papi.Papi;

public class ModelArenaTeam {

	private int teamId;
	private String teamName;
	private int teamMaxPlayers;
	private String teamType;
	private Material teamIcon;
	
	//teamId => ModelArenaTeam
	private Map<Integer, ModelArenaSpawn> arenaSpawnsMap = new HashMap<>();
	
	public ModelArenaTeam(int teamId, String teamName, int teamMaxPlayers, String teamType, Material teamIcon) {
		this.teamId = teamId;
		this.teamName = teamName;
		this.teamMaxPlayers = teamMaxPlayers;
		this.teamType = teamType;
		this.teamIcon = teamIcon;
	}
	
	public int getId() {
		return this.teamId;
	}
	
	public String getName() {
		return this.teamName;
	}
	
	public int getMaxPlayers() {
		return this.teamMaxPlayers;
	}
		
	public String getType() {
		return this.teamType;
	}
	
	public Material getIcon() {
		return this.teamIcon;
	}
	
	public boolean isAllowPlayers() {
		return this.teamMaxPlayers <= 0 ? false : true;
	}
	
	public boolean setName(String teamNewName) {
		if(!Papi.MySQL.set(Map.of(Database.Teams.NAME, teamNewName), Database.Teams.ID, "=", teamId, Database.Teams.TABLE))
			return false;
		
		this.teamName = teamNewName;
		return true;
	}
	
	public boolean setType(String teamNewType, boolean existTeamType) {
		if(!existTeamType)
			return false;
		
		if(!Papi.MySQL.set(Map.of(Database.Teams.TYPE, teamNewType), Database.Teams.ID, "=", teamId, Database.Teams.TABLE))
			return false;
		
		this.teamType = teamNewType;
		return true;
	}
	
	public boolean setMaxPlayers(int teamNewMaxPlayers) {
		if(!Papi.MySQL.set(Map.of(Database.Teams.MAX_PLAYERS, teamNewMaxPlayers), Database.Teams.ID, "=", teamId, Database.Teams.TABLE))
			return false;
		
		this.teamMaxPlayers = teamNewMaxPlayers;
		return true;
	}
	
	public boolean setIcon(String teamNewIconName) {
		Material material = Material.getMaterial(teamNewIconName.toUpperCase());
		if(material == null)
			return false;
		
		if(!Papi.MySQL.set(Map.of(Database.Teams.ICON, material.name()), Database.Teams.ID, "=", teamId, Database.Teams.TABLE))
			return false;
		
		this.teamIcon = material;
		return true;
	}

	
	public boolean existSpawn(int spawnId) {
		return arenaSpawnsMap.containsKey(spawnId);
	}
	
	public void initSpawn(int spawnId, String spawnName, Location location) {
		arenaSpawnsMap.put(spawnId, new ModelArenaSpawn(spawnId, spawnName, location));
	}
	
	public boolean addSpawn(String spawnName, Location spawnLocation) {
		Object arenaId = Papi.MySQL.get(Database.Teams.ARENAID, Database.Teams.ID, "=", String.valueOf(teamId), Database.Teams.TABLE);
		if(arenaId == null)
			return false;
		
		if(ArenesModel.getArena((int)arenaId).getWorld() == null)
			return false;
		
		if(!ArenesModel.getArena((int)arenaId).getWorld().getName().equalsIgnoreCase(spawnLocation.getWorld().getName()))
			return false;
		
		int spawnId = Papi.MySQL.put(Map.of(Database.Spawns.TEAMID, String.valueOf(teamId),
				Database.Spawns.NAME, spawnName,
				Database.Spawns.LOCATION, Papi.Function.getLocationToString(spawnLocation, true, true)), Database.Spawns.TABLE);
		
		if(spawnId < 0)
			return false;
		
		arenaSpawnsMap.put(spawnId, new ModelArenaSpawn(spawnId, spawnName, spawnLocation));
		return true;
	}
	
	public ModelArenaSpawn getSpawn(int spawnId) {
		return arenaSpawnsMap.get(spawnId);
	}
	
	public boolean removeSpawn(int spawnId) {
		if(!existSpawn(spawnId) || !Papi.MySQL.deleteData(Database.Spawns.ID, "=", String.valueOf(spawnId), Database.Spawns.TABLE))
			return false;
		
		arenaSpawnsMap.remove(teamId);
		return true;
	}
	
	public List<Integer> getSpawnsIdList() {
		return new ArrayList<>(arenaSpawnsMap.keySet());
	}
	public List<String> getSpawnsIdListString() {
		return Lists.transform(new ArrayList<>(arenaSpawnsMap.keySet()), Functions.toStringFunction());
	}
	
	public Map<Integer, ModelArenaSpawn> getSpawnsMap() {
		return arenaSpawnsMap;
	}

//	public int getPlayersRegistered() {
//		int teamPlayersRegistered=0;
//		for(ModelPlayer modelPlayer : ArenesModel.getPlayersMap().values()) {
//			if(modelPlayer.getRegisteredTeamId() == this.teamId)
//				teamPlayersRegistered++;
//		}
//		return teamPlayersRegistered;
//	}
	
	public List<ModelArenaPlayer> getPlayersRegistered() {
		List<ModelArenaPlayer> modelPlayersList = new ArrayList<>();
		for(ModelArenaPlayer modelPlayer : ArenesModel.getPlayersMap().values()) {
			if(modelPlayer.getRegisteredTeamId() == this.teamId)
				modelPlayersList.add(modelPlayer);
		}
		return modelPlayersList;
	}

	public List<String> getPlayersRegisteredNames() {
		List<String> modelPlayersList = new ArrayList<>();
		for(ModelArenaPlayer modelPlayer : ArenesModel.getPlayersMap().values()) {
			if(modelPlayer.getRegisteredTeamId() == this.teamId)
				modelPlayersList.add(modelPlayer.getPlayer().getName());
		}
		return modelPlayersList;
	}	
//	public List<ModelArenaPlayer> getPlayersInsideArena() {
//		List<ModelArenaPlayer> modelPlayersList = new ArrayList<>();
//		for(ModelArenaPlayer modelPlayer : ArenesModel.getPlayersMap().values()) {
//			if(modelPlayer.getPlayedInsideTeamId() == this.teamId)
//				modelPlayersList.add(modelPlayer);
//		}
//		return modelPlayersList;
//	}
//
//	public List<String> getPlayersInsideArenaNames() {
//		List<String> modelPlayersList = new ArrayList<>();
//		for(ModelArenaPlayer modelPlayer : ArenesModel.getPlayersMap().values()) {
//			if(modelPlayer.getPlayedInsideTeamId() == this.teamId)
//				modelPlayersList.add(modelPlayer.getPlayer().getName());
//		}
//		return modelPlayersList;
//	}
}

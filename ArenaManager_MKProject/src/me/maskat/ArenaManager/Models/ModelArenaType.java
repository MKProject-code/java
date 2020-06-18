package me.maskat.ArenaManager.Models;

import java.util.List;

import me.maskat.ArenaManager.ArenaPlugin.ArenaPlugin;

public class ModelArenaType {
	private String arenaType;
	private ArenaPlugin arenaPlugin;
	private List<String> teamTypes;
	private List<String> objGroupTypes;
	
	public ModelArenaType(String arenaType, ArenaPlugin arenaPlugin, List<String> teamTypes, List<String> objGroupTypes) {
		this.arenaType = arenaType;
		this.arenaPlugin = arenaPlugin;
		this.teamTypes = teamTypes;
		this.objGroupTypes = objGroupTypes;
	}
	
	public String getArenaType() {
		return arenaType;
	}
	
	public ArenaPlugin getArenaPlugin() {
		return arenaPlugin;
	}
	
	public List<String> getTeamTypes() {
		return teamTypes;
	}
	
	public List<String> getObjectsGroupTypes() {
		return objGroupTypes;
	}

	public boolean existTeamType(String teamType) {
		if(teamTypes==null)
			return false;
		return teamTypes.contains(teamType);
	}

	public boolean existObjectsGroupType(String objGroupType) {
		if(objGroupTypes==null)
			return false;
		return objGroupTypes.contains(objGroupType);
	}
}

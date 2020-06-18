package me.maskat.ArenaManager.ArenaAPI;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.World;

import me.maskat.ArenaManager.Models.ModelArena;
import me.maskat.ArenaManager.Models.ModelArenaTeam;

public class ApiArena {

	private Map<String, ApiTeam> apiTeams = new HashMap<>();
	private World world;
	
	public ApiArena(ModelArena modelArena) {
		this.world = modelArena.getWorld();
		for(ModelArenaTeam modelArenaTeam : modelArena.getTeamsMap().values())
		{
			if(modelArenaTeam.getType() != null)
				apiTeams.put(modelArenaTeam.getType(), new ApiTeam(modelArenaTeam));
		}
	}
	
	public World getWorld() {
		return world;
	}
	
	public ApiTeam getTeam(String teamType) {
		return apiTeams.get(teamType);
	}
	
}

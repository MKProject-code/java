package me.maskat.ArenaManager.ArenaAPI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import me.maskat.ArenaManager.Models.ModelArenaSpawn;
import me.maskat.ArenaManager.Models.ModelArenaTeam;

public class ApiTeam {

	private List<Location> apiSpawnsLocation = new ArrayList<>();
	
	public ApiTeam(ModelArenaTeam modelTeam) {
		for(ModelArenaSpawn modelArenaSpawn : modelTeam.getSpawnsMap().values())
		{
			this.apiSpawnsLocation.add(modelArenaSpawn.getLocation());
		}
	}
	
	public List<Location> getSpawnsLocation() {
		return this.apiSpawnsLocation;
	}
	
}

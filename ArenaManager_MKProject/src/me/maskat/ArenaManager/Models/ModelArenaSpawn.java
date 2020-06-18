package me.maskat.ArenaManager.Models;

import org.bukkit.Location;

public class ModelArenaSpawn {
	private int spawnId;
	private String spawnName;
	private Location spawnLocation;
	
	public ModelArenaSpawn(int spawnId, String spawnName, Location spawnLocation) {
		this.spawnId = spawnId;
		this.spawnName = spawnName;
		this.spawnLocation = spawnLocation;
	}
	
	public int getId() {
		return this.spawnId;
	}
	
	public String getName() {
		return this.spawnName;
	}
	
	public Location getLocation() {
		return this.spawnLocation;
	}
}

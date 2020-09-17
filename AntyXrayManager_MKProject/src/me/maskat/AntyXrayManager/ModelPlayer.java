package me.maskat.AntyXrayManager;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ModelPlayer {
	Player player;
	Collection<Location> hiddenLocations = new ArrayList<>();
	Chunk lastChunk = null;
	
	public ModelPlayer(Player player) {
		this.player = player;
	}
	
	public boolean existHiddenBlock(Location location) {
		return hiddenLocations.contains(location);
	}
	
	public boolean addHiddenBlock(Location location) {
		if(hiddenLocations.size() > 10000) {
			hiddenLocations = new ArrayList<>();
		}
		return hiddenLocations.add(location);
	}
	
	public boolean removeHiddenBlock(Location location) {
		return hiddenLocations.remove(location);
	}
	
	public void setLastChunk(Chunk chunk) {
		this.lastChunk = chunk;
	}
	
	public Chunk getLastChunk() {
		return this.lastChunk;
	}

}

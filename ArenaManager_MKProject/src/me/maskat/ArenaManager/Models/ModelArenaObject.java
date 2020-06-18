package me.maskat.ArenaManager.Models;

import org.bukkit.Location;

public class ModelArenaObject {
	private int objectId;
	private String objectName;
	private Location objectLocation;
	
	public ModelArenaObject(int objectId, String objectName, Location objectLocation) {
		this.objectId = objectId;
		this.objectName = objectName;
		this.objectLocation = objectLocation;
	}
	
	public int getId() {
		return this.objectId;
	}
	
	public String getName() {
		return this.objectName;
	}
	
	public Location getLocation() {
		return this.objectLocation;
	}
}

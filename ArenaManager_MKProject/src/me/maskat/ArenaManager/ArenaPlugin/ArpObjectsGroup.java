package me.maskat.ArenaManager.ArenaPlugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import me.maskat.ArenaManager.Models.ModelArenaObject;
import me.maskat.ArenaManager.Models.ModelArenaObjectsGroup;

public class ArpObjectsGroup {
	
	List<Location> objectsLocation = new ArrayList<>();

	public ArpObjectsGroup(ModelArenaObjectsGroup modelArenaObiectsGroup) {
		for(ModelArenaObject modelArenaObject : modelArenaObiectsGroup.getObjectsMap().values()) {
			objectsLocation.add(modelArenaObject.getLocation());
		}
	}
	
	public Location getLocation(int index) {
		return objectsLocation.get(index);
	}
	
	public List<Location> getLocations() {
		return objectsLocation;
	}

}

package me.maskat.ArenaManager.ArenaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;

public class ArpRandomLocation {

	private List<Location> spawnsLocationTemp = new ArrayList<>();
	
	public ArpRandomLocation(List<Location> spawnsLocation) {
		spawnsLocationTemp = new ArrayList<Location>(spawnsLocation);
	}
	
	public Location next() {
		if(spawnsLocationTemp.size() <= 0)
			return null;
	    Random rand = new Random();
	    Location randomLocation = spawnsLocationTemp.get(rand.nextInt(spawnsLocationTemp.size()));
	    spawnsLocationTemp.remove(randomLocation);
	    return randomLocation;
	}

}

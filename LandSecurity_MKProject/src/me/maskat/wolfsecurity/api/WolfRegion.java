package me.maskat.wolfsecurity.api;

import org.bukkit.Location;

import me.maskat.wolfsecurity.models.ModelRegion;

public class WolfRegion {
	ModelRegion modelRegion;
	Location centerLocation;
	
	public WolfRegion(ModelRegion modelRegion) {
		this.centerLocation = modelRegion.getCenterLocation();
	}
	
	public Location getCenterLocation() {
		return this.centerLocation;
	}
	
	
}

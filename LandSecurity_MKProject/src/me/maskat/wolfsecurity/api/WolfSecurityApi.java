package me.maskat.wolfsecurity.api;

import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import me.maskat.wolfsecurity.models.Model;
import me.maskat.wolfsecurity.models.ModelPlayer;

public class WolfSecurityApi {
	
	// Api for ComapssPoint
	public static boolean existWolf(Player player) {
		ModelPlayer modelPlayer = Model.Player(player);
		return modelPlayer.existWolf();
	}
	
//	public static Location getWolfLocation(Player player) {
//		Wolf wolfentity = Model.Player(player).getWolfEntity();
//		if(wolfentity == null)
//			return null;
//		
//		return wolfentity.getLocation();
//	}
	
	public static Wolf getWolfEntity(Player player) {
		return Model.Player(player).getWolfEntity();
	}
	///////////////////////
}

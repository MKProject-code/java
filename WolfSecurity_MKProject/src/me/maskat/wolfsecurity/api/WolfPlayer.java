package me.maskat.wolfsecurity.api;

import org.bukkit.entity.Player;

import me.maskat.wolfsecurity.Config;
import me.maskat.wolfsecurity.models.Model;
import me.maskat.wolfsecurity.models.ModelPlayer;
import me.maskat.wolfsecurity.models.ModelRegion;

public class WolfPlayer {

	private Player player;
	public WolfPlayer(Player player) {
		this.player = player;
	}
	
	public void updateOwnRegionProtection() {
		ModelPlayer modelPlayer = Model.Player(player);
		if(modelPlayer == null)
			return;
		ModelRegion modelRegion = modelPlayer.getOwnRegion();
		if(modelRegion == null)
			return;
		modelRegion.setProtection(player.hasPermission(Config.permissionRegionProtect));
	}
}

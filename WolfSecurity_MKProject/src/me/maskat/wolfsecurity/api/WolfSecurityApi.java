package me.maskat.wolfsecurity.api;

import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import me.maskat.wolfsecurity.Plugin;
import me.maskat.wolfsecurity.menu.WolfMenu;
import me.maskat.wolfsecurity.models.Model;
import me.maskat.wolfsecurity.models.ModelPlayer;
import me.maskat.wolfsecurity.models.ModelRegion;
import mkproject.maskat.Papi.Menu.PapiMenu;

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
	
	public static boolean isClaimedRegion(Player player) {
		return Model.Player(player).isClaimedRegion();
	}
	public static WolfRegion getClaimedRegion(Player player) {
		ModelRegion ownRegion = Model.Player(player).getOwnRegion();
		if(ownRegion.isClaimed())
			return new WolfRegion(ownRegion);
		else
			return null;
	}
	
	public static int giveWolf(Player player) {
		int wolfid = Model.Player(player).getOwnWolfId();
    	
    	if(Model.Wolves().containsKey(wolfid))
    	{
    		//player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMasz już swojego wilka!"));
    		return -1;
    	}
    	
    	if(player.getWorld() != Plugin.getAllowedWorld())
    	{
    		//player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNie możesz tego zrobić w tym świecie!"));
    		return 0;
    	}
    	
    	Model.Player(player).addWolf();
    	Model.Player(player).addRegion();
    	
    	//player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aDostałeś swojego własnego wilka!"));
    	
    	return 1;
	}
	
	public static void openWolfMenu(Player player, PapiMenu backMenu) {
		new WolfMenu().initOpenMenu(player, backMenu);
	}
	
	public static WolfPlayer getWolfPlayer(Player player) {
		return new WolfPlayer(player);
	}
}

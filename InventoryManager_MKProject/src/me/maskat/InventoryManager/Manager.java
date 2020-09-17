package me.maskat.InventoryManager;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;

public abstract class Manager {
	public static boolean checkSave(Player player, World world) {
		GameMode playerGameMode = player.getGameMode();
		
		boolean saveInv = false;
		
//		if(player.hasPermission(Papi.Function.getPermission(Plugin.getPlugin(), "bypass.inventory.save")))
//		{
//			saveInv = true;
//		}
//		else
		if(playerGameMode == GameMode.SURVIVAL)
		{
			if(//world == Papi.Server.getServerLobbyWorld() || 
					world == Papi.Server.getServerSpawnWorld()
					|| world == Papi.Server.getSurvivalWorld()
					|| world == Papi.Server.getNetherWorld()
					|| world == Papi.Server.getTheEndWorld())
			{
				saveInv = true;
			}
		}
		else if(playerGameMode == GameMode.CREATIVE)
		{
			saveInv = true;
		}
		
		return saveInv;
	}
	
	public static boolean checkLoad(Player player, World world, GameMode newGameMode) {

		boolean loadInv = false;
		
		if(newGameMode == GameMode.SURVIVAL)
		{
			if(//world == Papi.Server.getServerLobbyWorld() || 
					world == Papi.Server.getServerSpawnWorld()
					|| world == Papi.Server.getSurvivalWorld()
					|| world == Papi.Server.getNetherWorld()
					|| world == Papi.Server.getTheEndWorld())
			{
				loadInv = true;
			}
		}
		else if(newGameMode == GameMode.CREATIVE)
		{
			loadInv = true;
		}
		
		return loadInv;
	}
	
	public static boolean checkDeathUpdate(Player player, World world, GameMode gameMode) {

		boolean deathUpdateInv = false;
		
		if(gameMode == GameMode.SURVIVAL)
		{
			if(//world == Papi.Server.getServerLobbyWorld() || 
					world == Papi.Server.getServerSpawnWorld()
					|| world == Papi.Server.getSurvivalWorld()
					|| world == Papi.Server.getNetherWorld()
					|| world == Papi.Server.getTheEndWorld())
			{
				deathUpdateInv = true;
			}
		}
		else if(gameMode == GameMode.CREATIVE)
		{
			deathUpdateInv = true;
		}
		
		return deathUpdateInv;
	}
}

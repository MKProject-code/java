package mkproject.maskat.SpawnManager;

import java.io.File;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;

public class SpawnManagerAPI {
	public static void updatePlayerSpawnLocationAsync(Player player) {
		Papi.Model.getPlayer(player).initPlayerSpawnLocation(null);
		RandomTp.updatePlayerSpawnLocationAsync(player, Papi.Server.getSurvivalWorld());
	}
	public static boolean checkValidLocation(Location location) {
		Location locationTemp = location.clone();
		locationTemp.setY(locationTemp.getY()-1);
		return RandomTp.checkValidLocation(locationTemp, true);
	}
	public static void setPlayerSpawnGenerated(Player player, boolean bool) {
		Papi.MySQL.set(Map.of(Database.Users.PLAYER_SURVIVAL_SPAWN_GENERATED, (bool ? "1" : "0")), Database.Users.USERNAME, "=", player.getName().toLowerCase(), Database.Users.TABLE);
		Papi.Model.getPlayer(player).initPlayerSpawnGenerated(bool);
	}
	public static boolean generatePlayerSpawnSchemat(Player player) {
		String fileName = Plugin.getPlugin().getDataFolder() + File.separator + Plugin.getPlugin().getConfig().getString("GeneratedPlayerSpawnSchematicName");
		Location pasteLocation = Papi.Model.getPlayer(player).getPlayerSpawnLocation().clone();
		pasteLocation.setY(pasteLocation.getY()-1);
		
		if(!Papi.WorldEdit.pasteSchematic(fileName, pasteLocation, false))
		{
			RandomTp.preparePlayerSpawnArea(player, false);
			RandomTp.preparePlayerSpawnAreaWithoutPasteSchematic(player);
			//return false;
		}
		else
			RandomTp.preparePlayerSpawnArea(player, true);
		
		Papi.MySQL.set(Map.of(Database.Users.PLAYER_SURVIVAL_SPAWN_GENERATED, "1"), Database.Users.USERNAME, "=", player.getName().toLowerCase(), Database.Users.TABLE);
		Papi.Model.getPlayer(player).initPlayerSpawnGenerated(true);
		return true;
	}
	public static Location getRandomLocation(World world) {
		return RandomTp.getRandomLocationTask(null, world, false);
	}
}

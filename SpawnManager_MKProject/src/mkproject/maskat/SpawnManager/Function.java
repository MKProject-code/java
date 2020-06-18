package mkproject.maskat.SpawnManager;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;

public class Function {
	protected static void setPlayerSurvivalLastLocation(Player player, Location location) {
		if(location == null)
			Papi.MySQL.set(Map.of(Database.Users.PLAYER_SURVIVAL_LAST_LOCATION, ""), Database.Users.USERNAME, "=", player.getName().toLowerCase(), Database.Users.TABLE);
		else
			Papi.MySQL.set(Map.of(Database.Users.PLAYER_SURVIVAL_LAST_LOCATION, Papi.Function.getLocationToString(location, true, true)), Database.Users.USERNAME, "=", player.getName().toLowerCase(), Database.Users.TABLE);
		
		Papi.Model.getPlayer(player).initSurvivalLastLocation(location);
	}
	protected static Location getPlayerSurvivalLastLocation(Player player) {
		Object locStr = Papi.MySQL.get(Database.Users.PLAYER_SURVIVAL_LAST_LOCATION, Database.Users.USERNAME, "=", player.getName().toLowerCase(), Database.Users.TABLE);
		if(locStr == null)
			return null;
		
		return Papi.Function.getLocationFromString(locStr.toString());
	}
}

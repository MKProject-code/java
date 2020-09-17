package mkproject.maskat.StatsManager.Model;

import java.util.Map;

import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.StatsManager.Database;

public class StatsPlayer {

	private Player player;
	private int kills;
	private int deaths;
	private String lastKiller;
	
	public StatsPlayer(Player player) {
		this.player = player;
		
		String playerUUID = player.getUniqueId().toString();
		Database.playerInit(player);
		this.kills = (int) Papi.MySQL.get(Database.Users.KILLS, Database.Users.UUID, "=", playerUUID, Database.Users.TABLE);
		this.deaths = (int) Papi.MySQL.get(Database.Users.DEATHS, Database.Users.UUID, "=", playerUUID, Database.Users.TABLE);
		this.lastKiller = (String) Papi.MySQL.get(Database.Users.LAST_KILLER, Database.Users.UUID, "=", playerUUID, Database.Users.TABLE);
	}

	public void addKill() {
		this.kills++;
		Papi.MySQL.set(Map.of(Database.Users.KILLS, this.kills), Database.Users.UUID, "=", player.getUniqueId().toString(), Database.Users.TABLE);
	}
	
	public void addDeath() {
		this.deaths++;
		Papi.MySQL.set(Map.of(Database.Users.DEATHS, this.deaths), Database.Users.UUID, "=", player.getUniqueId().toString(), Database.Users.TABLE);
	}

	public void setLastKiller(Player killer) {
		this.lastKiller = killer.getName();
		Papi.MySQL.set(Map.of(Database.Users.LAST_KILLER, this.lastKiller), Database.Users.UUID, "=", player.getUniqueId().toString(), Database.Users.TABLE);
	}	
	
	public int getKills() {
		return this.kills;
	}
	
	public int getDeaths() {
		return this.deaths;
	}

	public String getLastKiller() {
		return this.lastKiller;
	}
	
	

}

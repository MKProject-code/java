package mkproject.maskat.GuildsManager;

import java.util.Map;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;

public class Database {
	public static void initialize(Plugin plugin) {
		Users.TABLE = plugin.getConfig().getString("MySQL.Table.Users")+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		
		if(Papi.DEVELOPER_DATABASE && Papi.DEVELOPER_DATABASE_AUTODELETE) {
			Papi.MySQL.deleteTable(Users.TABLE);
		}
		
		Papi.MySQL.createTable(Users.TABLE,
					Papi.SQL.createColumnParse(Database.Users.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Database.Users.REAL_NAME, Papi.SQL.ValType.VARCHAR, 32, true, false, true)
				+","+Papi.SQL.createColumnParse(Database.Users.UUID, Papi.SQL.ValType.VARCHAR, 128, true)
				+","+Papi.SQL.createColumnParse(Database.Users.KILLS, Papi.SQL.ValType.INT, 16, true, false, false, "0")
				+","+Papi.SQL.createColumnParse(Database.Users.DEATHS, Papi.SQL.ValType.INT, 16, true, false, false, "0")
				+","+Papi.SQL.createColumnParse(Database.Users.LAST_KILLER, Papi.SQL.ValType.VARCHAR, 32, false)
				+","+Papi.SQL.createColumnPrimary(Database.Users.ID)
		);
	}
	
	public static class Users {
		public static String TABLE;
		public static final String ID = "id";
		public static final String REAL_NAME = "user_realname";
		public static final String UUID = "user_uuid";
		public static final String KILLS = "kills";
		public static final String DEATHS = "deaths";
		public static final String LAST_KILLER = "last_killer";
	}

	public static void playerInit(Player player) {
		String playerUUID = player.getUniqueId().toString();
		Object userid = Papi.MySQL.get(Users.ID, Users.UUID, "=", playerUUID, Users.TABLE);
		if(userid == null)
		{
			Papi.MySQL.put(Map.of(
				Users.REAL_NAME, player.getName(),
				Users.UUID, player.getUniqueId().toString(),
				Users.KILLS, player.getStatistic(Statistic.PLAYER_KILLS),
				Users.DEATHS, player.getStatistic(Statistic.DEATHS)
				), Users.TABLE);
		}
		else
			Papi.MySQL.set(Map.of(Database.Users.REAL_NAME, player.getName()), Database.Users.UUID, "=", playerUUID, Database.Users.TABLE);
	}
	
	public static void playerSave(Player player, int kills, int deaths, String lastKiller) {
		Papi.MySQL.set(Map.of(
				Users.KILLS, kills,
				Users.DEATHS, deaths,
				Users.LAST_KILLER, lastKiller
				), Users.UUID, "=", player.getUniqueId().toString(), Users.TABLE);
	}
}

package mkproject.maskat.VipManager;

import java.time.LocalDateTime;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;

public class Database {

	public static void initialize() {
		Users.TABLE = Config.getConfig().getString("MySQL.Table.Users")+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		
		if(Papi.DEVELOPER_DATABASE && Papi.DEVELOPER_DATABASE_AUTODELETE) {
			Papi.MySQL.deleteTable(Users.TABLE);
		}
		
		Papi.MySQL.createTable(Users.TABLE,
					Papi.SQL.createColumnParse(Database.Users.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Database.Users.USERNAME, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Database.Users.DATETIME_START, Papi.SQL.ValType.DATETIME, -1, true, false, false, Papi.SQL.ValDefault.CURRENT_TIMESTAMP)
				+","+Papi.SQL.createColumnParse(Database.Users.DATETIME_END, Papi.SQL.ValType.DATETIME, -1, true)
				+","+Papi.SQL.createColumnParse(Database.Users.DESCRIPTION, Papi.SQL.ValType.VARCHAR, 512, false)
				+","+Papi.SQL.createColumnParse(Database.Users.DONATION, Papi.SQL.ValType.BOOLEAN, -1, true, false, false, "0")
				+","+Papi.SQL.createColumnPrimary(Database.Users.ID)
		);
	}
	public static class Users {
		public static String TABLE;
		public static final String ID = "id";
		public static final String USERNAME = "username";
		public static final String DATETIME_START = "datetime_start";
		public static final String DATETIME_END = "datetime_end";
		public static final String DESCRIPTION = "description";
		public static final String DONATION = "donation";
		
		public static int addVip(Player player, LocalDateTime endDateTime, String description) {
			return Papi.MySQL.put(Map.of(
					USERNAME, player.getName().toLowerCase(),
					DATETIME_END, Papi.Function.getLocalDateTimeToString(endDateTime),
					DESCRIPTION, description
					), TABLE);
		}
		
		public static LocalDateTime getVipEnd(Player player) {
			Object endDateTime = Papi.MySQL.getOrderBy(DATETIME_END, 
					Papi.SQL.getWhereAnd(
							Papi.SQL.getWhereObject(USERNAME, "=", player.getName().toLowerCase()),
							Papi.SQL.getWhereObject(DATETIME_END, ">", Papi.Function.getCurrentLocalDateTimeToString())
						), DATETIME_END, Papi.SQL.OrderType.DESC, TABLE);
			if(endDateTime == null)
				return null;
			
			return Papi.Function.getLocalDateTimeFromString(endDateTime.toString());
		}

		public static Object getVipOfflineEnd(OfflinePlayer playerOffline) {
			Object endDateTime = Papi.MySQL.getOrderBy(DATETIME_END, 
					Papi.SQL.getWhereAnd(
							Papi.SQL.getWhereObject(USERNAME, "=", playerOffline.getName().toLowerCase()),
							Papi.SQL.getWhereObject(DATETIME_END, ">", Papi.Function.getCurrentLocalDateTimeToString())
						), DATETIME_END, Papi.SQL.OrderType.DESC, TABLE);
			if(endDateTime == null)
				return null;
			
			return Papi.Function.getLocalDateTimeFromString(endDateTime.toString());
		}

		public static boolean isDonationVIP(Player player) {
			Object id = Papi.MySQL.getOrderBy(ID, 
					Papi.SQL.getWhereAnd(
							Papi.SQL.getWhereObject(USERNAME, "=", player.getName().toLowerCase()),
							Papi.SQL.getWhereObject(DATETIME_END, ">", Papi.Function.getCurrentLocalDateTimeToString()),
							Papi.SQL.getWhereObject(DONATION, "=", "1")
						), DATETIME_END, Papi.SQL.OrderType.DESC, TABLE);
			
			if(id == null)
				return false;
			
			return true;
		}
	}
}

package mkproject.maskat.AdminUtils;

import java.time.LocalDateTime;
import java.util.Map;

import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;

public class Database {
	public static void initialize() {
		Mutes.TABLE = Config.getString(Config.ConfigKey.MySQLTableMutes)+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		Bans.TABLE = Config.getString(Config.ConfigKey.MySQLTableBans)+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		
		if(Papi.DEVELOPER_DATABASE && Papi.DEVELOPER_DATABASE_AUTODELETE) {
			Papi.MySQL.deleteTable(Mutes.TABLE);
			Papi.MySQL.deleteTable(Bans.TABLE);
		}
		
		Papi.MySQL.createTable(Mutes.TABLE,
					Papi.SQL.createColumnParse(Database.Mutes.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Database.Mutes.USERNAME, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Database.Mutes.USERIP, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Database.Mutes.DATETIME, Papi.SQL.ValType.DATETIME, -1, true)
				+","+Papi.SQL.createColumnParse(Database.Mutes.DATETIME_END, Papi.SQL.ValType.DATETIME, -1, true)
				+","+Papi.SQL.createColumnParse(Database.Mutes.ADMIN, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Database.Mutes.ACTIVE, Papi.SQL.ValType.BOOLEAN, -1, true, false, false, "True")
				+","+Papi.SQL.createColumnParse(Database.Mutes.REASON, Papi.SQL.ValType.VARCHAR, 512, false)
				+","+Papi.SQL.createColumnParse(Database.Mutes.ADMIN_DEACTIVE, Papi.SQL.ValType.VARCHAR, 64, false)
				+","+Papi.SQL.createColumnParse(Database.Mutes.REASON_DEACTIVE, Papi.SQL.ValType.VARCHAR, 512, false)
				+","+Papi.SQL.createColumnPrimary(Database.Mutes.ID)
		);
		
		Papi.MySQL.createTable(Bans.TABLE,
				Papi.SQL.createColumnParse(Database.Bans.ID, Papi.SQL.ValType.INT, 16, true, true, true)
			+","+Papi.SQL.createColumnParse(Database.Bans.USERNAME, Papi.SQL.ValType.VARCHAR, 64, true)
			+","+Papi.SQL.createColumnParse(Database.Bans.USERIP, Papi.SQL.ValType.VARCHAR, 64, true)
			+","+Papi.SQL.createColumnParse(Database.Bans.DATETIME, Papi.SQL.ValType.DATETIME, -1, true)
			+","+Papi.SQL.createColumnParse(Database.Bans.DATETIME_END, Papi.SQL.ValType.DATETIME, -1, true)
			+","+Papi.SQL.createColumnParse(Database.Bans.ADMIN, Papi.SQL.ValType.VARCHAR, 64, true)
			+","+Papi.SQL.createColumnParse(Database.Mutes.ACTIVE, Papi.SQL.ValType.BOOLEAN, -1, true, false, false, "True")
			+","+Papi.SQL.createColumnParse(Database.Bans.REASON, Papi.SQL.ValType.VARCHAR, 512, false)
			+","+Papi.SQL.createColumnParse(Database.Bans.ADMIN_DEACTIVE, Papi.SQL.ValType.VARCHAR, 64, false)
			+","+Papi.SQL.createColumnParse(Database.Bans.REASON_DEACTIVE, Papi.SQL.ValType.VARCHAR, 512, false)
			+","+Papi.SQL.createColumnPrimary(Database.Bans.ID)
		);
	}
	
	public static class Mutes {
		public static String TABLE;
		public static final String ID = "muteid";
		public static final String USERNAME = "username";
		public static final String USERIP = "userip";
		public static final String DATETIME = "datetime";
		public static final String DATETIME_END = "datetime_end";
		public static final String ADMIN = "admin";
		public static final String ACTIVE = "active";
		public static final String REASON = "reason";
		public static final String ADMIN_DEACTIVE = "admin_deactive";
		public static final String REASON_DEACTIVE = "reason_deactive";
		
		public static int addMute(Player player, LocalDateTime endDatetime, Player admin, String reason) {
			return Papi.MySQL.put(Map.of(
					USERNAME, player.getName().toLowerCase(),
					USERIP, Papi.Model.getPlayer(player).getAddressIP(),
					DATETIME, Papi.Function.getCurrentLocalDateTimeToString(),
					DATETIME_END, Papi.Function.getLocalDateTimeToString(endDatetime),
					ADMIN, admin.getName(),
					REASON, reason
					), TABLE);
		}
		public static int removeMute(Player player, Player admin, String reason) {
			Object result = Papi.MySQL.getOrderBy(ID,Papi.SQL.getWhereAnd(
					Papi.SQL.getWhereObject(USERNAME, "=", player.getName().toLowerCase()),
					Papi.SQL.getWhereObject(DATETIME_END, ">", Papi.Function.getCurrentLocalDateTimeToString()),
					Papi.SQL.getWhereObject(ACTIVE, "=", true)
							), DATETIME_END, Papi.SQL.OrderType.DESC, TABLE);
			
			if(result == null)
				return -1;
			
			return Papi.MySQL.set(Map.of(
					ACTIVE, false,
					ADMIN_DEACTIVE, admin.getName(),
					REASON_DEACTIVE, reason
					), ID, "=", String.valueOf(result), TABLE) ? 1 : 0;
		}
		public static LocalDateTime checkMuted(Player player) {
			Object result = Papi.MySQL.getOrderBy(DATETIME_END,Papi.SQL.getWhereAnd(
					Papi.SQL.getWhereObject(USERNAME, "=", player.getName().toLowerCase()),
					Papi.SQL.getWhereObject(DATETIME_END, ">", Papi.Function.getCurrentLocalDateTimeToString()),
					Papi.SQL.getWhereObject(ACTIVE, "=", true)
							), DATETIME_END, Papi.SQL.OrderType.DESC, TABLE);
			
			if(result == null)
				return null;
			
			return Papi.Function.getLocalDateTimeFromString(String.valueOf(result));
		}
	}
	public static class Bans {
		public static String TABLE;
		public static final String ID = "banid";
		public static final String USERNAME = "username";
		public static final String USERIP = "userip";
		public static final String DATETIME = "datetime";
		public static final String DATETIME_END = "datetime_end";
		public static final String ADMIN = "admin";
		public static final String ACTIVE = "active";
		public static final String REASON = "reason";
		public static final String ADMIN_DEACTIVE = "admin_deactive";
		public static final String REASON_DEACTIVE = "reason_deactive";
	}
}

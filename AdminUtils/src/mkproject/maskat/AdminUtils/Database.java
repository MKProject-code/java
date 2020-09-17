package mkproject.maskat.AdminUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import mkproject.maskat.AdminUtils.Model.HistoryData;
import mkproject.maskat.LoginManager.UsersAPI;
import mkproject.maskat.Papi.Papi;

abstract public class Database {
	public static void initialize() {
		Mutes.TABLE = Config.getString(Config.ConfigKey.MySQLTableMutes)+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		Kicks.TABLE = Config.getString(Config.ConfigKey.MySQLTableKicks)+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
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
		
		Papi.MySQL.createTable(Kicks.TABLE,
				Papi.SQL.createColumnParse(Database.Kicks.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Database.Kicks.USERNAME, Papi.SQL.ValType.VARCHAR, 1024, true)
				+","+Papi.SQL.createColumnParse(Database.Kicks.USERIP, Papi.SQL.ValType.VARCHAR, 1024, true)
				+","+Papi.SQL.createColumnParse(Database.Kicks.DATETIME, Papi.SQL.ValType.DATETIME, -1, true)
				+","+Papi.SQL.createColumnParse(Database.Kicks.ADMIN, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Database.Kicks.REASON, Papi.SQL.ValType.VARCHAR, 512, false)
				+","+Papi.SQL.createColumnPrimary(Database.Kicks.ID)
				);
		
		Papi.MySQL.createTable(Bans.TABLE,
				Papi.SQL.createColumnParse(Database.Bans.ID, Papi.SQL.ValType.INT, 16, true, true, true)
			+","+Papi.SQL.createColumnParse(Database.Bans.USERNAME, Papi.SQL.ValType.VARCHAR, 64, true)
			+","+Papi.SQL.createColumnParse(Database.Bans.USERIP, Papi.SQL.ValType.VARCHAR, 64, true)
			+","+Papi.SQL.createColumnParse(Database.Bans.DATETIME, Papi.SQL.ValType.DATETIME, -1, true)
			+","+Papi.SQL.createColumnParse(Database.Bans.DATETIME_END, Papi.SQL.ValType.DATETIME, -1, false)
//			+","+Papi.SQL.createColumnParse(Database.Bans.PERMANENT, Papi.SQL.ValType.BOOLEAN, -1, true, false, false, "0")
			+","+Papi.SQL.createColumnParse(Database.Bans.ADMIN, Papi.SQL.ValType.VARCHAR, 64, true)
			+","+Papi.SQL.createColumnParse(Database.Bans.ACTIVE, Papi.SQL.ValType.BOOLEAN, -1, true, false, false, "True")
			+","+Papi.SQL.createColumnParse(Database.Bans.REASON, Papi.SQL.ValType.VARCHAR, 512, false)
			+","+Papi.SQL.createColumnParse(Database.Bans.ADMIN_DEACTIVE, Papi.SQL.ValType.VARCHAR, 64, false)
			+","+Papi.SQL.createColumnParse(Database.Bans.REASON_DEACTIVE, Papi.SQL.ValType.VARCHAR, 512, false)
			+","+Papi.SQL.createColumnParse(Database.Bans.IGNORE_CHECK_BAN, Papi.SQL.ValType.BOOLEAN, -1, true, false, false , "0")
			+","+Papi.SQL.createColumnPrimary(Database.Bans.ID)
		);
	}
	
	public static class History {
		private static final String TYPE = "type";
//		private static final String USERNAME = "username";
		private static final String DATETIME = "datetime";
		private static final String DATETIME_END = "datetime_end";
		private static final String ADMIN = "admin";
		private static final String ACTIVE = "active";
		private static final String REASON = "reason";
		private static final String ADMIN_DEACTIVE = "admin_deactive";
		private static final String REASON_DEACTIVE = "reason_deactive";
		
		public static List<HistoryData> getPlayerAll(String playerName, int page, int limit) {
			
			playerName = playerName.toLowerCase();
			
			int offset = 0;
			if(page <= 0)
				page = 1;
			
			if(page > 1)
				offset = (limit*(page-1))-1;
			
        	final ResultSet rs = Papi.MySQL.query(
        			"SELECT * FROM " + 
	        			"(SELECT 'mute' as type, username, datetime, datetime_end, admin, active, reason, admin_deactive, reason_deactive FROM `MKP_AdminUtils_Mutes` " + 
	        			"UNION " + 
	        			"SELECT 'kick' as type, username, datetime, NULL as datetime_end, admin, 1 as active, reason, NULL as admin_deactive, NULL as reason_deactive FROM `MKP_AdminUtils_Kicks` " + 
	        			"UNION " + 
	        			"SELECT 'ban' as type, username, datetime, datetime_end, admin, active, reason, admin_deactive, reason_deactive FROM `MKP_AdminUtils_Bans` WHERE ignore_check_ban = FALSE) AS u " + 
        			"WHERE u.username = '" + playerName + "' " + 
        			"ORDER BY u.datetime DESC LIMIT "+offset+","+limit
        			);
        	
        	if(rs == null)
        		return null;
        	
        	List<HistoryData> historyDataList = new ArrayList<>();
        	
			try {
				while(rs.next()) {
					historyDataList.add(new HistoryData(
							playerName,
							rs.getString(TYPE),
							rs.getString(DATETIME),
							rs.getString(DATETIME_END),
							rs.getString(ADMIN),
							rs.getBoolean(ACTIVE),
							rs.getString(REASON),
							rs.getString(ADMIN_DEACTIVE),
							rs.getString(REASON_DEACTIVE)
							));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            if(historyDataList.size() <= 0)
            	return null;
            
			return historyDataList;
		}
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
		public static List<String> getMutedPlayersNamesList() {
			ResultSet rs = Papi.MySQL.getResultSetAll(-1, USERNAME, Papi.SQL.getWhereObject(DATETIME_END, ">", Papi.Function.getCurrentLocalDateTimeToString()), TABLE);
			
			List<String> playersList = new ArrayList<>();
			if(rs != null) {
				try {
					while(rs.next()) {
						String username = rs.getString(USERNAME);
						if(username != null)
						{
							OfflinePlayer offlinePlayer = UsersAPI.getOfflinePlayer(username);
							if(offlinePlayer != null && offlinePlayer.getName() != null)
								playersList.add(offlinePlayer.getName());
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//	old
//			if(rs != null) {
//				try {
//					while(rs.next()) {
//						String username = rs.getString(USERNAME);
//						if(username != null)
//						playersList.add(username);
//					}
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			return playersList;
		}
	}
	public static class Kicks {
		public static String TABLE;
		public static final String ID = "kickid";
		public static final String USERNAME = "username";
		public static final String USERIP = "userip";
		public static final String DATETIME = "datetime";
		public static final String ADMIN = "admin";
		public static final String REASON = "reason";
		
		public static int addKick(Player player, Player admin, String reason) {
			return Papi.MySQL.put(Map.of(
					USERNAME, player.getName(),
					USERIP, Papi.Model.getPlayer(player).getAddressIP(),
					DATETIME, Papi.Function.getCurrentLocalDateTimeToString(),
					ADMIN, admin == null ? "*SERVER*" : admin.getName(),
					REASON, reason
					), TABLE);
		}

		public static int addKickAll(Map<String, String> playersOnlineNamesIPs, Player admin, String reason) {
			return Papi.MySQL.put(Map.of(
					USERNAME, playersOnlineNamesIPs.keySet(),
					USERIP, playersOnlineNamesIPs.values(),
					DATETIME, Papi.Function.getCurrentLocalDateTimeToString(),
					ADMIN, admin == null ? "*SERVER*" : admin.getName(),
					REASON, reason
					), TABLE);
		}
	}
	public static class Bans {
		public static String TABLE;
		public static final String ID = "banid";
		public static final String USERNAME = "username";
		public static final String USERIP = "userip";
		public static final String DATETIME = "datetime";
		public static final String DATETIME_END = "datetime_end";
//		public static final String PERMANENT = "permanent";
		public static final String ADMIN = "admin";
		public static final String ACTIVE = "active";
		public static final String REASON = "reason";
		public static final String ADMIN_DEACTIVE = "admin_deactive";
		public static final String REASON_DEACTIVE = "reason_deactive";
		public static final String IGNORE_CHECK_BAN = "ignore_check_ban";
		
		public static int addBan(Player player, LocalDateTime endDatetime, Player admin, String reason) {
			Map<String, Object> columnsValuesMap = new HashMap<>();
			columnsValuesMap.put(USERNAME, player.getName().toLowerCase());
			columnsValuesMap.put(USERIP, Papi.Model.getPlayer(player).getAddressIP());
			columnsValuesMap.put(DATETIME, Papi.Function.getCurrentLocalDateTimeToString());
			columnsValuesMap.put(DATETIME_END, endDatetime == null ? null : Papi.Function.getLocalDateTimeToString(endDatetime));
			columnsValuesMap.put(ADMIN, admin == null ? "*SERVER*" : admin.getName());
			columnsValuesMap.put(REASON, reason);
			return Papi.MySQL.put(columnsValuesMap, TABLE);
//			return Papi.MySQL.put(Map.of(
//					USERNAME, player.getName().toLowerCase(),
//					USERIP, Papi.Model.getPlayer(player).getAddressIP(),
//					DATETIME, Papi.Function.getCurrentLocalDateTimeToString(),
////					endDatetime == null ? PERMANENT : DATETIME_END, endDatetime == null ? "1" : Papi.Function.getLocalDateTimeToString(endDatetime),
//					DATETIME_END, endDatetime == null ? null : Papi.Function.getLocalDateTimeToString(endDatetime),
//					ADMIN, admin == null ? "*SERVER*" : admin.getName(),
//					REASON, reason
//					), TABLE);
		}
		public static int addBanOffline(OfflinePlayer player, LocalDateTime endDatetime, Player admin, String reason) {
			Map<String, Object> columnsValuesMap = new HashMap<>();
			columnsValuesMap.put(USERNAME, player.getName().toLowerCase());
			columnsValuesMap.put(USERIP, UsersAPI.getPlayerLastLoginIP(player.getUniqueId()));
			columnsValuesMap.put(DATETIME, Papi.Function.getCurrentLocalDateTimeToString());
			columnsValuesMap.put(DATETIME_END, endDatetime == null ? null : Papi.Function.getLocalDateTimeToString(endDatetime));
			columnsValuesMap.put(ADMIN, admin == null ? "*SERVER*" : admin.getName());
			columnsValuesMap.put(REASON, reason);
			return Papi.MySQL.put(columnsValuesMap, TABLE);
		}
		public static int removeBan(OfflinePlayer offlinePlayer, Player admin, String reason) {
			Object result = Papi.MySQL.get(ID, Papi.SQL.getWhereAnd(
					Papi.SQL.getWhereObject(USERNAME, "=", offlinePlayer.getName().toLowerCase()),
					Papi.SQL.getWhereOr(
							Papi.SQL.getWhereObject(DATETIME_END, ">", Papi.Function.getCurrentLocalDateTimeToString()),
							Papi.SQL.getWhereObject(DATETIME_END, "=", null)
//							Papi.SQL.getWhereObject(PERMANENT, "=", "1")
							),
					Papi.SQL.getWhereObject(ACTIVE, "=", "1")
					), TABLE);
			
			if(result == null)
				return -1;
			
			return Papi.MySQL.set(Map.of(
					ACTIVE, "0",
					ADMIN_DEACTIVE, admin.getName(),
					REASON_DEACTIVE, reason
					), ID, "=", String.valueOf(result), TABLE) ? 1 : 0;
		}
		public static class BanInfo {
			
			private String username;
			private LocalDateTime datetime_end;
			private boolean permanent;
			private String reason;

			public BanInfo(String username, String datetime_end, String reason) {//boolean permanent, 
				this.username = username;
				if(datetime_end != null) {
					this.datetime_end = Papi.Function.getLocalDateTimeFromString(datetime_end);
					this.permanent = false;
				}
				else {
					this.datetime_end = null;
					this.permanent = true;
				}
				this.reason = reason;
			}
			public String getUsername() { return this.username; }
			public LocalDateTime getDatetimeEnd() { return this.datetime_end; }
			public boolean isPermament() { return this.permanent; }
			public String getReason() { return this.reason; }
		}
		public static BanInfo checkBan(String username, String hostAddress) {
			ResultSet rs;
			if(hostAddress != null) {
				rs = Papi.MySQL.getResultSetAllOrderBy(1, List.of(USERNAME, DATETIME_END, REASON),//, PERMANENT
						Papi.SQL.getWhereOr(
							Papi.SQL.getWhereAnd(
									Papi.SQL.getWhereObject(USERNAME, "=", username.toLowerCase()),
									Papi.SQL.getWhereOr(
											Papi.SQL.getWhereObject(DATETIME_END, ">", Papi.Function.getCurrentLocalDateTimeToString()),
											Papi.SQL.getWhereObject(DATETIME_END, "=", null)
	//										Papi.SQL.getWhereObject(PERMANENT, "=", "1")
											),
									Papi.SQL.getWhereObject(ACTIVE, "=", "1")
									),
							Papi.SQL.getWhereAnd(
									Papi.SQL.getWhereObject(USERIP, "=", hostAddress),
									Papi.SQL.getWhereOr(
											Papi.SQL.getWhereObject(DATETIME_END, ">", Papi.Function.getCurrentLocalDateTimeToString()),
											Papi.SQL.getWhereObject(DATETIME_END, "=", null)
	//										Papi.SQL.getWhereObject(PERMANENT, "=", "1")
											),
									Papi.SQL.getWhereObject(ACTIVE, "=", "1")
									)
							), List.of(DATETIME_END), List.of(Papi.SQL.OrderType.ASC, Papi.SQL.OrderType.DESC), TABLE);//List.of(PERMANENT,...
			}
			else
			{
				rs = Papi.MySQL.getResultSetAllOrderBy(1, List.of(USERNAME, DATETIME_END, REASON),//, PERMANENT
						Papi.SQL.getWhereOr(
								Papi.SQL.getWhereAnd(
										Papi.SQL.getWhereObject(USERNAME, "=", username.toLowerCase()),
										Papi.SQL.getWhereOr(
												Papi.SQL.getWhereObject(DATETIME_END, ">", Papi.Function.getCurrentLocalDateTimeToString()),
												Papi.SQL.getWhereObject(DATETIME_END, "=", null)
	//										Papi.SQL.getWhereObject(PERMANENT, "=", "1")
												),
										Papi.SQL.getWhereObject(ACTIVE, "=", "1")
										)
								), List.of(DATETIME_END), List.of(Papi.SQL.OrderType.ASC, Papi.SQL.OrderType.DESC), TABLE);//List.of(PERMANENT,...
			}
			
			if(rs == null)
				return null;
			
			try {
				if(rs.next())
				{
					return new BanInfo(
							rs.getString(USERNAME),
							rs.getString(DATETIME_END),
//							rs.getBoolean(PERMANENT),
							rs.getString(REASON)
							);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		public static List<String> getBannedPlayersNamesList() {
			
			ResultSet rs = Papi.MySQL.getResultSetAll(-1, USERNAME, 
					Papi.SQL.getWhereAnd(
							Papi.SQL.getWhereOr(
									Papi.SQL.getWhereObject(DATETIME_END, ">", Papi.Function.getCurrentLocalDateTimeToString()),
									Papi.SQL.getWhereObject(DATETIME_END, "=", null)
//									Papi.SQL.getWhereObject(PERMANENT, "=", "1")
									),
							Papi.SQL.getWhereObject(ACTIVE, "=", "1")
							)
					, TABLE);
			
			List<String> playersList = new ArrayList<>();
			if(rs != null) {
				try {
					while(rs.next()) {
						String username = rs.getString(USERNAME);
						if(username != null)
						{
							OfflinePlayer offlinePlayer = UsersAPI.getOfflinePlayer(username);
							if(offlinePlayer != null && offlinePlayer.getName() != null)
								playersList.add(offlinePlayer.getName());
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return playersList;
		}
		
		public static boolean ignoreCheckBan(String name) {
			Object ignorebanid = Papi.MySQL.get(ID, Papi.SQL.getWhereAnd(
					Papi.SQL.getWhereObject(USERNAME, "=", name.toLowerCase()),
					Papi.SQL.getWhereObject(IGNORE_CHECK_BAN, "=", "1")
					), Database.Bans.TABLE);
			
			if(ignorebanid == null)
				return false;
			
			if(ignorebanid instanceof Integer && (int)ignorebanid > 0)
				return true;
			
			return false;
		}
	}
}

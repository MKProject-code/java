package me.maskat.PromoManager;

import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import mkproject.maskat.LoginManager.UsersAPI;
import mkproject.maskat.Papi.Papi;

public class Database {
	
	public static void initialize(Plugin plugin) {
		Users.TABLE = plugin.getConfig().getString("MySQL.Table.Users")+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		
		if(Papi.DEVELOPER_DATABASE && Papi.DEVELOPER_DATABASE_AUTODELETE)
			Papi.MySQL.deleteTable(Users.TABLE);
		
		Papi.MySQL.createTable(Users.TABLE,
					Papi.SQL.createColumnParse(Database.Users.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Database.Users.NAME, Papi.SQL.ValType.VARCHAR, 32, true, false, true)
				+","+Papi.SQL.createColumnParse(Database.Users.PROMO_USED_IP, Papi.SQL.ValType.VARCHAR, 64, false)
				+","+Papi.SQL.createColumnParse(Database.Users.PROMO_USED_CODE, Papi.SQL.ValType.VARCHAR, 32, false)
				+","+Papi.SQL.createColumnParse(Database.Users.PROMO_USED_PAID, Papi.SQL.ValType.BOOLEAN, -1, true, false, false, "false")
				+","+Papi.SQL.createColumnParse(Database.Users.PROMO_ASSIGNED_CODE, Papi.SQL.ValType.VARCHAR, 32, false, false, true)
				+","+Papi.SQL.createColumnPrimary(Database.Users.ID)
		);
		
		Plugin.getPlugin().getLogger().info("Loading data from MySQL...");
	}
	
	public static class Users {
		public static String TABLE;
		public static final String ID = "userid";
		public static final String NAME = "username";
		public static final String PROMO_USED_IP = "promo_used_ip";
		public static final String PROMO_USED_CODE = "promo_used_code";
		public static final String PROMO_USED_PAID = "promo_used_paid";
		public static final String PROMO_ASSIGNED_CODE = "promo_assigned_code";
		
		public static void initUser(Player player) {
			if(!Papi.MySQL.exists(NAME, "=", player.getName().toLowerCase(), TABLE))
				Papi.MySQL.put(Map.of(NAME, player.getName().toLowerCase()), TABLE);
		}
		
		public static String getPromoCodeUsed(Player player) {
			Object promoUsedCode = Papi.MySQL.get(PROMO_USED_CODE, NAME, "=", player.getName().toLowerCase(), TABLE);
			if(promoUsedCode == null)
				return null;
			else
				return String.valueOf(promoUsedCode);
		}
		
		public static boolean setPromoCodeUsed(Player player, String promoCode) {
			return Papi.MySQL.set(Map.of(
					PROMO_USED_IP, Papi.Model.getPlayer(player).getAddressIP(),
					PROMO_USED_CODE, promoCode.toLowerCase()
					), NAME, "=", player.getName().toLowerCase(), TABLE);
		}
		
		public static String getPromoCodeAssigned(Player player) {
			Object promoUsedAssigned = Papi.MySQL.get(PROMO_ASSIGNED_CODE, NAME, "=", player.getName().toLowerCase(), TABLE);
			
			if(promoUsedAssigned == null) {
				while(true) {
					String generatedPromoCode = RandomStringUtils.randomAlphanumeric(5).toLowerCase();
					
					Object checkExist = Papi.MySQL.get(ID, PROMO_ASSIGNED_CODE, "=", generatedPromoCode, TABLE);
					if(checkExist == null) {
						if(Papi.MySQL.set(Map.of(PROMO_ASSIGNED_CODE, generatedPromoCode), NAME, "=", player.getName().toLowerCase(), TABLE))
							return generatedPromoCode;
						else
							return "Wystąpił problem podczas pobierania kodu :(";
					}
				}
			}
			else
				return String.valueOf(promoUsedAssigned);
		}
		
		public static boolean existPromoUsedIP(Player player) {
			Object userid = Papi.MySQL.get(ID, PROMO_USED_IP, "=", Papi.Model.getPlayer(player).getAddressIP(), TABLE);
			if(userid == null)
				return false;
			
			return true;
		}
		
		public static int getPromoCodeOther(Player player, String promoCode) {
			Object userid = Papi.MySQL.get(ID, Papi.SQL.getWhereAnd(
					Papi.SQL.getWhereObject(NAME, "!=", player.getName().toLowerCase()),
					Papi.SQL.getWhereObject(PROMO_ASSIGNED_CODE, "=", promoCode.toLowerCase())
					), TABLE);
			if(userid == null)
				return -1;
			
			try {
				return Integer.parseInt(userid.toString());
			} catch(Exception ex) {
				return -1;
			}
		}

		public static OfflinePlayer getPlayerAssignedPromoCode(int userid) {
			Object userName = Papi.MySQL.get(NAME, ID, "=", String.valueOf(userid), TABLE);
			if(userName == null)
				return null;
			return UsersAPI.getOfflinePlayer(userName.toString());
		}

		public static boolean setPromoCodeUsedPayed(Player player) {
			return Papi.MySQL.set(Map.of(
					PROMO_USED_PAID, true
					), NAME, "=", player.getName().toLowerCase(), TABLE);
		}
	}
}

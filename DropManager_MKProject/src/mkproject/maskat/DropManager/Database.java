package mkproject.maskat.DropManager;

import java.time.LocalDateTime;
import java.util.Map;

import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;

public class Database {

	public static void initialize() {
		DropsVipPotion.TABLE = Config.getConfig().getString("MySQL.Table.DropsVipPotion")+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		DropsVipBook.TABLE = Config.getConfig().getString("MySQL.Table.DropsVipBook")+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		
		if(Papi.DEVELOPER_DATABASE && Papi.DEVELOPER_DATABASE_AUTODELETE) {
			Papi.MySQL.deleteTable(DropsVipPotion.TABLE);
			Papi.MySQL.deleteTable(DropsVipBook.TABLE);
		}
		
		Papi.MySQL.createTable(DropsVipPotion.TABLE,
					Papi.SQL.createColumnParse(Database.DropsVipPotion.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Database.DropsVipPotion.USERNAME, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Database.DropsVipPotion.BLOCK_DROP_EXPIRED, Papi.SQL.ValType.DATETIME, -1, true)
				+","+Papi.SQL.createColumnPrimary(Database.DropsVipPotion.ID)
		);
		
		Papi.MySQL.createTable(DropsVipBook.TABLE,
				Papi.SQL.createColumnParse(Database.DropsVipBook.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Database.DropsVipBook.USERNAME, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Database.DropsVipBook.CODE, Papi.SQL.ValType.VARCHAR, 16, true)
				+","+Papi.SQL.createColumnParse(Database.DropsVipBook.SALE_PROCENT, Papi.SQL.ValType.VARCHAR, 4, true)
				+","+Papi.SQL.createColumnParse(Database.DropsVipBook.SALE_EXPIRED, Papi.SQL.ValType.DATETIME, -1, true)
				+","+Papi.SQL.createColumnParse(Database.DropsVipBook.USED, Papi.SQL.ValType.BOOLEAN, -1, false, false, false, "0")
				+","+Papi.SQL.createColumnPrimary(Database.DropsVipBook.ID)
				);
	}
	public static class DropsVipPotion {
		public static String TABLE;
		public static final String ID = "id";
		public static final String USERNAME = "username";
		public static final String BLOCK_DROP_EXPIRED = "block_drop_expired";
		
		public static int addVipPotion(Player player, LocalDateTime blockDropExpired) {
			return Papi.MySQL.put(Map.of(
					USERNAME, player.getName().toLowerCase(),
					BLOCK_DROP_EXPIRED, Papi.Function.getLocalDateTimeToString(blockDropExpired)
					), TABLE);
		}
		
		public static boolean isVipPotionBlockDropValidExist(Player player) {
			Object id = Papi.MySQL.getOrderBy(ID, 
					Papi.SQL.getWhereAnd(
							Papi.SQL.getWhereObject(USERNAME, "=", player.getName().toLowerCase()),
							Papi.SQL.getWhereObject(BLOCK_DROP_EXPIRED, ">", Papi.Function.getCurrentLocalDateTimeToString())
						), BLOCK_DROP_EXPIRED, Papi.SQL.OrderType.DESC, TABLE);
			if(id == null)
				return false;
			else
				return true;
		}
	}
	public static class DropsVipBook {
		public static String TABLE;
		public static final String ID = "id";
		public static final String USERNAME = "username";
		public static final String CODE = "code";
		public static final String SALE_PROCENT = "sale_procent";
		public static final String SALE_EXPIRED = "sale_expired";
		public static final String USED = "used";
		
		public static int addVipBook(Player player, String code, String saleProcent, LocalDateTime saleExpired) {
			return Papi.MySQL.put(Map.of(
					USERNAME, player.getName().toLowerCase(),
					CODE, code,
					SALE_PROCENT, saleProcent,
					SALE_EXPIRED, Papi.Function.getLocalDateTimeToString(saleExpired)
					), TABLE);
		}
		
		public static boolean isVipBookSaleValidExist(Player player) {
			Object id = Papi.MySQL.getOrderBy(ID, 
					Papi.SQL.getWhereAnd(
							Papi.SQL.getWhereObject(USERNAME, "=", player.getName().toLowerCase()),
							Papi.SQL.getWhereObject(SALE_EXPIRED, ">", Papi.Function.getCurrentLocalDateTimeToString())
							), SALE_EXPIRED, Papi.SQL.OrderType.DESC, TABLE);
			if(id == null)
				return false;
			else
				return true;
		}
	}
}

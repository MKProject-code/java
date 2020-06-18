package mkproject.maskat.LoginManager;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;

public class Database {
//	public enum User {
//		ID("userid"),
//		NAME("username"),
//		PASSWORD("password"),
//		REGISTER_DATETIME("registerdatetime"),
//		LASTLOGIN_DATETIME("lastlogindatetime");
//		
//		private String value;
//		
//	    private User(final String value) {
//	        this.value = value;
//	    }
//	    
//	    public String getValue() {
//	        return value;
//	    }
//	}
	
	public static void Initialize(Plugin plugin) {
		Users.TABLE = plugin.getConfig().getString("MySQL.Table.Users")+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		plugin.getLogger().warning("Papi.DEVELOPER_DATABASE="+Papi.DEVELOPER_DATABASE);
		plugin.getLogger().warning("Papi.DEVELOPER_DATABASE_AUTODELETE="+Papi.DEVELOPER_DATABASE_AUTODELETE);
		if(Papi.DEVELOPER_DATABASE && Papi.DEVELOPER_DATABASE_AUTODELETE)
			Papi.MySQL.deleteTable(Users.TABLE);
		
		Papi.MySQL.createTable(Users.TABLE,
					Papi.SQL.createColumnParse(Database.Users.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Database.Users.NAME, Papi.SQL.ValType.VARCHAR, 32, true, false, true)
				+","+Papi.SQL.createColumnParse(Database.Users.REALNAME, Papi.SQL.ValType.VARCHAR, 32, true, false, true)
				+","+Papi.SQL.createColumnParse(Database.Users.PASSWORD, Papi.SQL.ValType.VARCHAR, 32, true)
				+","+Papi.SQL.createColumnParse(Database.Users.REGISTER_DATETIME, Papi.SQL.ValType.DATETIME, 32, true, false, false, Papi.SQL.ValDefault.CURRENT_TIMESTAMP)
				+","+Papi.SQL.createColumnParse(Database.Users.REGISTER_IP, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Database.Users.LASTLOGIN_IP, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Database.Users.LASTLOGIN_DATETIME, Papi.SQL.ValType.DATETIME, 32, true)
				+","+Papi.SQL.createColumnParse(Database.Users.LOGOUT_POSITION, Papi.SQL.ValType.VARCHAR, 128, false)
				+","+Papi.SQL.createColumnParse(Database.Users.UUID, Papi.SQL.ValType.VARCHAR, 128, true)
				+","+Papi.SQL.createColumnPrimary(Database.Users.ID)
		);
	}
	
	public static class Users {
		public static String TABLE;
		public static final String ID = "userid";
		public static final String NAME = "username";
		public static final String REALNAME = "realname";
		public static final String PASSWORD = "password";
		public static final String REGISTER_DATETIME = "registerdatetime";
		public static final String REGISTER_IP = "registerip";
		public static final String LASTLOGIN_IP = "lastloginip";
		public static final String LASTLOGIN_DATETIME = "lastlogindatetime";
		public static final String LOGOUT_POSITION = "logoutposition";
		public static final String UUID = "uuid";
	}

	public static void onPlayerQuit(Player player) {
		Location location = player.getLocation();
		String locationData = location.getWorld().getName()+","+location.getX()+","+location.getY()+","+location.getZ();
		
		Papi.MySQL.set(Map.of(Users.LOGOUT_POSITION, locationData), Users.NAME, "=", player.getName(), Users.TABLE);
	}
}

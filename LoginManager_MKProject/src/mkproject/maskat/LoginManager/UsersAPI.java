package mkproject.maskat.LoginManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class UsersAPI {
	public static List<OfflinePlayer> getPlayersWithSameIP(Player player) {
		ResultSet rs = Papi.MySQL.getResultSetAll(-1, Database.Users.UUID, Papi.SQL.getWhereOr(
				Papi.SQL.getWhereObject(Database.Users.REGISTER_IP, "=", Papi.Model.getPlayer(player).getAddressIP()),
				Papi.SQL.getWhereObject(Database.Users.LASTLOGIN_IP, "=", Papi.Model.getPlayer(player).getAddressIP())
				), Database.Users.TABLE);
		
		List<OfflinePlayer> players = new ArrayList<>();
		try {
			while(rs.next()) {
				String uuidStr = rs.getString(Database.Users.UUID);
				
				if(uuidStr == null)
					continue;
				UUID uuid = UUID.fromString(uuidStr);
				if(uuid == null || player.getUniqueId().equals(uuid))
					continue;
				
				OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
				if(p != null)
					players.add(p);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return players;
	}
	
	public static List<String> getLastRegisterPlayersRealNames(int limit) {
		ResultSet rs = Papi.MySQL.getResultSetAllOrderBy(limit, Database.Users.REALNAME, Database.Users.ID, Papi.SQL.OrderType.DESC, Database.Users.TABLE);
		
		List<String> players = new ArrayList<>();
		
		if(rs == null)
			return players;
		
		try {
			while(rs.next()) {
				players.add(rs.getString(Database.Users.REALNAME));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return players;
	}
	
	public static OfflinePlayer getOfflinePlayer(String userName) {
		Object playerUUID = Papi.MySQL.get(Database.Users.UUID, Database.Users.NAME, "=", userName.toLowerCase(), Database.Users.TABLE);
		if(playerUUID == null)
			return null;
		return Bukkit.getOfflinePlayer(UUID.fromString(playerUUID.toString()));
	}
	
	public static boolean changePassword(Player player, String oldPassword, String newPassword) {
		Object resultOldPassword = Papi.MySQL.get(Database.Users.PASSWORD, Database.Users.NAME, "=", player.getName().toLowerCase(), Database.Users.TABLE);
		if(resultOldPassword == null || !resultOldPassword.equals(oldPassword))
		{
			Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.ChangePassword.OldPasswordWrong"));
			return false;
		}
		
		if(!Login.checkPasswordValidate(player, newPassword))
			return false;
		
		if(Papi.MySQL.set(Map.of(Database.Users.PASSWORD, newPassword), Database.Users.NAME, "=", player.getName().toLowerCase(), Database.Users.TABLE))
		{
			Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.ChangePassword.SuccessChanged"));
			return true;
		}
		else
		{
			Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.ChangePassword.SqlSetError"));
			return false;
		}
	}
	
	public static String getPlayerLastLoginIP(UUID uuid) {
		Object lastLoginIP = Papi.MySQL.get(Database.Users.LASTLOGIN_IP, Database.Users.UUID, "=", uuid.toString(), Database.Users.TABLE);
		if(lastLoginIP == null)
			return null;
		return String.valueOf(lastLoginIP);
	}
	
	public static LocalDateTime getPlayerLastLoginDateTime(UUID uuid) {
		Object lastLoginDateTime = Papi.MySQL.get(Database.Users.LASTLOGIN_DATETIME, Database.Users.UUID, "=", uuid.toString(), Database.Users.TABLE);
		if(lastLoginDateTime == null)
			return null;
		return Papi.Function.getLocalDateTimeFromString(String.valueOf(lastLoginDateTime));
	}
}

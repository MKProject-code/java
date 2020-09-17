package mkproject.maskat.LoginManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import mkproject.maskat.LoginManager.Database.Users;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class Login {
	public static void onPlayerCommandPreprocessLow(PlayerCommandPreprocessEvent e) {
		if (!Papi.Model.getPlayer(e.getPlayer()).isLogged() && e.getPlayer().isOnline()) {
			e.setCancelled(true);
			Model.getPlayer(e.getPlayer()).registerExecuteCmd();
			onChatSend(e.getPlayer(), e.getMessage());
		}
	}

	public static void onPlayerCommandPreprocessHigh(PlayerCommandPreprocessEvent e) {
		if (!Papi.Model.existPlayer(e.getPlayer())) {
			e.setCancelled(true);
			return;
		}

		if (!Papi.Model.getPlayer(e.getPlayer()).isLogged() && e.getPlayer().isOnline()) {
			e.setCancelled(true);
		}
	}

	public static void onAsyncPlayerChatLow(AsyncPlayerChatEvent e) {
		if (!Papi.Model.getPlayer(e.getPlayer()).isLogged()) {
			e.setCancelled(true);
			Model.getPlayer(e.getPlayer()).registerExecuteCmd();
			onChatSend(e.getPlayer(), e.getMessage());
		} else {
			for (Player onlinePlayer : Papi.Model.getPlayers()) {
				if (!Papi.Model.getPlayer(onlinePlayer).isLogged())
					e.getRecipients().remove(onlinePlayer);
			}
		}
	}

	public static void onAsyncPlayerChatHigh(AsyncPlayerChatEvent e) {
		if (!Papi.Model.getPlayer(e.getPlayer()).isLogged()) {
			e.setCancelled(true);
		}
	}

	public static void onChatSend(Player player, String message) {
		if (message.charAt(0) != '/')
			return;

		String[] args = message.substring(1).split(" ");
		if (Model.getPlayer(player).isRegistered() && args.length == 2 && Model.getPlayer(player).isRegistered()
				&& (args[0].equalsIgnoreCase("login") || args[0].equalsIgnoreCase("l"))) {
			Login.onExecuteCommandLogin(player, args[1]);
			return;
		} else if (!Model.getPlayer(player).isRegistered() && args.length == 3
				&& (args[0].equalsIgnoreCase("register") || args[0].equalsIgnoreCase("reg"))) {
			if (!Login.checkPasswordValidate(player, args[1]))
				return;

			Login.onExecuteCommandRegister(player, args[1], args[2]);
			return;
		}

		if (Model.getPlayer(player).isRegistered()) {
			Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.CommandLoginWrong"));
			return;
		} else {
			Message.sendMessage(player,
					Message.getMessageLang(Plugin.getLanguageYaml(), "Player.CommandRegisterWrong"));
			return;
		}
	}

	public static boolean checkPasswordValidate(Player player, String password) {
		if (password.length() < 5) {
			Message.sendMessage(player,
					Message.getMessageLang(Plugin.getLanguageYaml(), "Player.RegisterPasswordTooShort"));
			return false;
		}
		if (password.length() > 30) {
			Message.sendMessage(player,
					Message.getMessageLang(Plugin.getLanguageYaml(), "Player.RegisterPasswordTooLong"));
			return false;
		}
		if (password.toLowerCase().indexOf(player.getName().toLowerCase()) >= 0) {
			Message.sendMessage(player,
					Message.getMessageLang(Plugin.getLanguageYaml(), "Player.RegisterPasswordEqualsUserName"));
			return false;
		}
		if ((password.toLowerCase().indexOf("haslo") == 0 && password.length() <= 7)
				|| (password.toLowerCase().indexOf("pass") == 0 && password.length() <= 6)
				|| (password.toLowerCase().indexOf("password") == 0 && password.length() <= 10)) {
			Message.sendMessage(player,
					Message.getMessageLang(Plugin.getLanguageYaml(), "Player.RegisterPasswordDangerous"));
			return false;
		}
		return true;
	}

	public static boolean onExecuteCommandLogin(Player player, String password) {
		// AuthMe VERIFIER
//		if(Model.getPlayer(player).isRegisteredByAuthMe())
//		{
//			Object hashedPass = Papi.MySQL.get("password",
//							Papi.SQL.getWhereObject("username","=",player.getName().toLowerCase()),
//					"authme");
//			
//			Sha256 sha = new Sha256();
//			if(sha.comparePassword(password, new HashedPassword((String)hashedPass), player.getName().toLowerCase()))
//			{
//				Papi.MySQL.put(Map.of(
//						Database.Users.NAME, player.getName().toLowerCase(),
//						Database.Users.REALNAME, player.getName(),
//						Database.Users.PASSWORD, password,
//						Database.Users.REGISTER_IP, player.getAddress().getAddress().getHostAddress().toString(),
//						Database.Users.LASTLOGIN_IP, player.getAddress().getAddress().getHostAddress().toString(),
//						Database.Users.LASTLOGIN_DATETIME, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
//						Database.Users.UUID, player.getUniqueId().toString()
//						), Database.Users.TABLE);
//				
//				doRegisterSuccessLoginPlayer(player);
//				return true;
//			}
//			else
//			{
//				player.kickPlayer(Message.getMessageLang(Plugin.getLanguageYaml(), "Player.KickMessage.WrongLoginPassword"));
//				return false;
//			}
//		}

		Object userid = Papi.MySQL.get(Database.Users.ID,
				Papi.SQL.getWhereObject(Database.Users.NAME, "=", player.getName().toLowerCase()),
				Database.Users.TABLE);

		if (userid == null) {
			// Message.sendMessage(player,
			// Plugin.getLanguageYaml().getString("Player.WrongLoginPassword"));
			player.kickPlayer(
					Message.getMessageLang(Plugin.getLanguageYaml(), "Player.KickMessage.WrongLoginPassword"));
			return false;
		}

		Object userpass = Papi.MySQL.get(Database.Users.PASSWORD,
				Papi.SQL.getWhereObject(Database.Users.ID, "=", userid.toString()), Database.Users.TABLE);

		if (userpass == null || !userpass.equals(password)) {
			// Message.sendMessage(player,
			// Plugin.getLanguageYaml().getString("Player.WrongLoginPassword"));
			player.kickPlayer(
					Message.getMessageLang(Plugin.getLanguageYaml(), "Player.KickMessage.WrongLoginPassword"));
			return false;
		}

		doSuccessLoginPlayer(player, userid);

		Papi.MySQL.set(
				Map.of(Database.Users.LASTLOGIN_DATETIME,
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), Users.UUID,
						player.getUniqueId().toString(), Users.LASTLOGIN_IP,
						player.getAddress().getAddress().getHostAddress().toString()),
				Database.Users.ID, "=", userid, Database.Users.TABLE);

//		Papi.MySQL.set(Database.Users.LASTLOGIN_DATETIME, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), Database.Users.ID, "=", userid, Database.Users.TABLE);
//		Papi.MySQL.set(Users.UUID, player.getUniqueId(), Database.Users.ID, "=", userid, Users.TABLE);
//		Papi.MySQL.set(Users.LASTLOGIN_IP, player.getAddress().getAddress().getHostAddress().toString(), Database.Users.ID, "=", userid, Users.TABLE);

		return true;
	}

	public static boolean onExecuteCommandRegister(Player player, String password1, String password2) {
		if (!password1.equals(password2)) {
			Message.sendMessage(player,
					Message.getMessageLang(Plugin.getLanguageYaml(), "Player.CommandRegisterWrongPasswordNotEquals"));
			return false;
		}
		Integer userid = Papi.MySQL
				.put(Map.of(Database.Users.NAME, player.getName().toLowerCase(), Database.Users.REALNAME,
						player.getName(), Database.Users.PASSWORD, password1, Database.Users.REGISTER_IP,
						player.getAddress().getAddress().getHostAddress().toString(), Database.Users.LASTLOGIN_IP,
						player.getAddress().getAddress().getHostAddress().toString(), Database.Users.LASTLOGIN_DATETIME,
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
						Database.Users.UUID, player.getUniqueId().toString()), Database.Users.TABLE);

		if (userid > 0)
			doSuccessRegisterPlayer(player, userid);
		else {
			Plugin.getPlugin().getLogger().warning("************ REGISTER ERROR ************");
			Plugin.getPlugin().getLogger().warning("************ REGISTER ERROR ************");
			Plugin.getPlugin().getLogger().warning("************ REGISTER ERROR ************");
			Plugin.getPlugin().getLogger().warning("************ REGISTER ERROR ************");
			Plugin.getPlugin().getLogger().warning("************ REGISTER ERROR ************");
			player.kickPlayer("Wystąpił nieznany błąd. Spróbuj ponownie");
		}
		return true;
	}

	public static void doSuccessLoginPlayer(Player player, Object userid) {
		Model.getPlayer(player).setAferLogin(true);
		
		Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.SuccessLogged"));
		
		TitleManager.onPlayerLogin(player, false);
		
		PlayerRestoreManager.onPlayerLogin(player, false);
		
		Papi.Model.getPlayer(player).registerLogged();
		
		doSaveLoginLog(player, userid);
	}

	public static void doSuccessRegisterPlayer(Player player, Object userid) {
		Model.getPlayer(player).setAferLogin(true);

		Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.SuccessRegistered"));

		TitleManager.onPlayerLogin(player, true);

		PlayerRestoreManager.onPlayerLogin(player, true);

		Papi.Model.getPlayer(player).registerFirstLogged();

		doSaveLoginLog(player, userid);
	}
	
	private static void doSaveLoginLog(Player player, Object userid) {
		if(userid != null) {
			Papi.MySQL.put(Map.of(
					Database.Logs.USER_ID, userid.toString(),
					Database.Logs.LOGIN_IP, player.getAddress().getAddress().getHostAddress().toString()
					), Database.Logs.TABLE);
			
			final ResultSet rs = Papi.MySQL.query(
					"SELECT d.`"+Database.Users.REALNAME+"`, d.`"+Database.Users.UUID+"`, d.`"+Database.Logs.LOGIN_DATETIME+"`, d.`"+Database.Logs.LOGOUT_DATETIME+"` FROM "+
					"("+
						"SELECT `"+Database.Users.TABLE+"`.`"+Database.Users.REALNAME+"`, `"+Database.Users.TABLE+"`.`"+Database.Users.UUID+"`, `"+Database.Logs.TABLE+"`.`"+Database.Logs.LOGIN_DATETIME+"`, `"+Database.Logs.TABLE+"`.`"+Database.Logs.LOGOUT_DATETIME+"`, ROW_NUMBER() OVER(PARTITION BY `"+Database.Users.TABLE+"`.`"+Database.Users.NAME+"` ORDER BY ID DESC) rn FROM `"+Database.Logs.TABLE+"` "+
						"INNER JOIN `"+Database.Users.TABLE+"` ON`"+Database.Logs.TABLE+"`.`"+Database.Logs.USER_ID+"`=`"+Database.Users.TABLE+"`.`"+Database.Users.ID+"` "+
						"WHERE `"+Database.Logs.TABLE+"`.`"+Database.Logs.LOGIN_IP+"`='"+Papi.Model.getPlayer(player).getAddressIP()+"' AND `"+Database.Users.TABLE+"`.`"+Database.Users.ID+"`!='"+userid+"' "+
						"ORDER BY `"+Database.Logs.TABLE+"`.`"+Database.Logs.ID+"` DESC"+
					") AS d " +
					"WHERE d.rn=1");

			if(rs != null)
			{
				List<String> usersWithSameIPraw = new ArrayList<>();
				try {
					while(rs.next()) {
						String loginDatetime = rs.getString(Database.Logs.LOGIN_DATETIME);
						String logoutDatetime = rs.getString(Database.Logs.LOGOUT_DATETIME);
						String realName = rs.getString(Database.Users.REALNAME);
						String uuid = rs.getString(Database.Users.UUID);
						
						OfflinePlayer offlinePlayer = null;
						if(uuid != null)
							offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid));

						boolean isOnline = (offlinePlayer == null ? false : offlinePlayer.isOnline());
						
						usersWithSameIPraw.add("{\"text\":\""+Message.getColorMessage((isOnline?"&a":"")+realName)+"\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\""+Message.getColorMessage("&7Last login: "+(loginDatetime==null?"-":loginDatetime.substring(0, loginDatetime.lastIndexOf(".")))+"\\n"+(isOnline?"&7Last logout: &a&oplayer is online":"&7Last logout: "+(logoutDatetime==null?"-":logoutDatetime.substring(0, logoutDatetime.lastIndexOf(".")))))+"\"}}}");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(usersWithSameIPraw.size() > 0)
				{
					for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
						if(onlinePlayer.hasPermission("mkp.loginmanager.info.multiaccount")) {
							Message.sendRawMessage(onlinePlayer,
									"[" + 
										"{\"text\":\""+Message.getColorMessage("&8[&3MultiAccount&8] ")+"\"}," + 
										"{\"text\":\""+Message.getColorMessage(Papi.Model.getPlayer(player).getNameWithPrefix())+"\"}," + 
										"{\"text\":\""+Message.getColorMessage(" &3→ ")+"\"}," + 
										String.join(",{\"text\":\", \"}," ,usersWithSameIPraw) + 
									"]"
									);
//							Message.sendMessage(onlinePlayer, "&8[&3MultiAccount&8] "++" &3-> &a"+String.join(", ", usersWithSameIP));
						}
					}
				}
			}
		}
	}
}

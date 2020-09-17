package mkproject.maskat.LoginManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.bukkit.entity.Player;

import me.maskat.MenuHelpManager.API.MenuHelpAPI;
import mkproject.maskat.LoginManager.Database.Users;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class Login {

	public static void onChatSend(Player player, String message) {
		if(message.charAt(0) != '/')
			return;
		
		String[] args = message.substring(1).split(" ");
		if(Model.getPlayer(player).isRegistered() && args.length == 2 && Model.getPlayer(player).isRegistered() && (args[0].equalsIgnoreCase("login") || args[0].equalsIgnoreCase("l"))) {
			Login.onExecuteCommandLogin(player, args[1]);
			return;
		}
		else if(!Model.getPlayer(player).isRegistered() && args.length == 3 && (args[0].equalsIgnoreCase("register") || args[0].equalsIgnoreCase("reg"))) {
			if(!Login.checkPasswordValidate(player, args[1]))
				return;
			
			Login.onExecuteCommandRegister(player, args[1], args[2]);
			return;
		}
		
		if(Model.getPlayer(player).isRegistered())
		{
			Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.CommandLoginWrong"));
			return;
		}
		else
		{
			Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.CommandRegisterWrong"));
			return;
		}
	}

	private static boolean checkPasswordValidate(Player player, String password) {
		if(password.length() < 5) {
			Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.RegisterPasswordTooShort"));
			return false;
		}
		if(password.length() > 30) {
			Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.RegisterPasswordTooLong"));
			return false;
		}
		if(player.getName().equalsIgnoreCase(password)) {
			Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.RegisterPasswordEqualsUserName"));
			return false;
		}
		return true;
	}
	public static boolean onExecuteCommandLogin(Player player, String password) {
		//AuthMe VERIFIER
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
				Papi.SQL.getWhereAnd(
						Papi.SQL.getWhereObject(Database.Users.NAME, "=", player.getName().toLowerCase()),
						Papi.SQL.getWhereObject(Database.Users.PASSWORD, "=", password) // SECURITY PROBLEM: this ignoring case !!!
					),
				Database.Users.TABLE);
		
		if(userid == null)
		{
			//Message.sendMessage(player, Plugin.getLanguageYaml().getString("Player.WrongLoginPassword"));
			player.kickPlayer(Message.getMessageLang(Plugin.getLanguageYaml(), "Player.KickMessage.WrongLoginPassword"));
			return false;
		}
		
		Object userpass = Papi.MySQL.get(Database.Users.PASSWORD,
				Papi.SQL.getWhereObject(Database.Users.ID, "=", userid.toString()),
				Database.Users.TABLE);
		
		if(userpass == null || !userpass.equals(password))
		{
			//Message.sendMessage(player, Plugin.getLanguageYaml().getString("Player.WrongLoginPassword"));
			player.kickPlayer(Message.getMessageLang(Plugin.getLanguageYaml(), "Player.KickMessage.WrongLoginPassword"));
			return false;
		}
		
		doRegisterSuccessLoginPlayer(player, userid);
		
		Papi.MySQL.set(Map.of(
				Database.Users.LASTLOGIN_DATETIME, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				Users.UUID, player.getUniqueId().toString(),
				Users.LASTLOGIN_IP, player.getAddress().getAddress().getHostAddress().toString()
				), Database.Users.ID, "=", userid, Database.Users.TABLE);
		
		return true;
	}

	public static boolean onExecuteCommandRegister(Player player, String password1,  String password2) {
		if(!password1.equals(password2))
		{
			Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.CommandRegisterWrongPasswordNotEquals"));
			return false;
		}
		int userid = Papi.MySQL.put(Map.of(
				Database.Users.NAME, player.getName().toLowerCase(),
				Database.Users.REALNAME, player.getName(),
				Database.Users.PASSWORD, password1,
				Database.Users.REGISTER_IP, player.getAddress().getAddress().getHostAddress().toString(),
				Database.Users.LASTLOGIN_IP, player.getAddress().getAddress().getHostAddress().toString(),
				Database.Users.LASTLOGIN_DATETIME, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				Database.Users.UUID, player.getUniqueId().toString()
				), Database.Users.TABLE);
		
		if(userid <= 0)
		{
			Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.CommandRegisterErrorDatabase"));
			return false;
		}
		
		Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.SuccessRegistered"));
		
		TitleManager.onPlayerLogin(player, true);
		
		//Papi.Model.getPlayer(player).registerFirstLogged();
		Model.getPlayer(player).setLogged(true);
		
		MenuHelpAPI.BungeeMenu.openServersMenu(player);
		
		Papi.MySQL.put(Map.of(
				Database.Logs.USER_ID, String.valueOf(userid),
				Database.Logs.LOGIN_IP, player.getAddress().getAddress().getHostAddress().toString()
				), Database.Logs.TABLE);
		return true;
	}
	
	public static void doRegisterSuccessLoginPlayer(Player player, Object userid) {
		
		Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.SuccessLogged"));
		
		TitleManager.onPlayerLogin(player, false);
		
		//Papi.Model.getPlayer(player).registerLogged();
		Model.getPlayer(player).setLogged(true);
		
		MenuHelpAPI.BungeeMenu.openServersMenu(player);
		
		if(userid != null) {
			Papi.MySQL.put(Map.of(
					Database.Logs.USER_ID, userid.toString(),
					Database.Logs.LOGIN_IP, player.getAddress().getAddress().getHostAddress().toString()
					), Database.Logs.TABLE);
		}
	}
}

package mkproject.maskat.LoginManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import mkproject.maskat.LoginManager.Database.Users;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;
import mkproject.maskat.PasswordHash.HashedPassword;
import mkproject.maskat.PasswordHash.Sha256;

public class Login {
	public static void onPlayerCommandPreprocessLow(PlayerCommandPreprocessEvent e) {
    	if(e.getPlayer().isOnline() && !Papi.Model.getPlayer(e.getPlayer()).isLogged())
    	{
    		e.setCancelled(true);
    		Model.getPlayer(e.getPlayer()).registerExecuteCmd();
    		onChatSend(e.getPlayer(), e.getMessage());
    	}
	}
	public static void onPlayerCommandPreprocessHigh(PlayerCommandPreprocessEvent e) {
		if(e.getPlayer().isOnline() && !Papi.Model.getPlayer(e.getPlayer()).isLogged())
		{
			e.setCancelled(true);
		}
	}
	public static void onAsyncPlayerChatLow(AsyncPlayerChatEvent e) {
		if(!Papi.Model.getPlayer(e.getPlayer()).isLogged())
		{
			e.setCancelled(true);
			Model.getPlayer(e.getPlayer()).registerExecuteCmd();
			onChatSend(e.getPlayer(), e.getMessage());
		}
		else
		{
			for(Player onlinePlayer : Papi.Model.getPlayers()) {
				if(!Papi.Model.getPlayer(onlinePlayer).isLogged())
					e.getRecipients().remove(onlinePlayer);
			}
		}
	}
	public static void onAsyncPlayerChatHigh(AsyncPlayerChatEvent e) {
    	if(!Papi.Model.getPlayer(e.getPlayer()).isLogged())
    	{
    		e.setCancelled(true);
    	}
	}
	
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
		if(Model.getPlayer(player).isRegisteredByAuthMe())
		{
			Object hashedPass = Papi.MySQL.get("password",
							Papi.SQL.getWhereObject("username","=",player.getName().toLowerCase()),
					"authme");
			
			Sha256 sha = new Sha256();
			if(sha.comparePassword(password, new HashedPassword((String)hashedPass), player.getName().toLowerCase()))
			{
//				Papi.MySQL.put(Map.of(
//						Database.Users.NAME, player.getName().toLowerCase(),
//						Database.Users.REALNAME, player.getName(),
//						Database.Users.PASSWORD, password,
//						Database.Users.LASTLOGIN_DATETIME, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//						), Database.Users.TABLE);
				
				Papi.MySQL.put(Map.of(
						Database.Users.NAME, player.getName().toLowerCase(),
						Database.Users.REALNAME, player.getName(),
						Database.Users.PASSWORD, password,
						Database.Users.REGISTER_IP, player.getAddress().getAddress().getHostAddress().toString(),
						Database.Users.LASTLOGIN_IP, player.getAddress().getAddress().getHostAddress().toString(),
						Database.Users.LASTLOGIN_DATETIME, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
						Database.Users.UUID, player.getUniqueId().toString()
						), Database.Users.TABLE);
				
				doRegisterSuccessLoginPlayer(player);
				return true;
			}
			else
			{
				player.kickPlayer(Message.getMessageLang(Plugin.getLanguageYaml(), "Player.KickMessage.WrongLoginPassword"));
				//Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.WrongLoginPassword"));
				return false;
			}
		}
		
//		Object userid = Papi.MySQL.get(Database.Users.ID,
//				Papi.SQL.getWhereAnd(
//						Papi.SQL.getWhereObject(Database.Users.NAME,"=",player.getName().toLowerCase()),
//						Papi.SQL.getWhereObject(Database.Users.PASSWORD,"=",password)),
//				Database.Users.TABLE);
		
		Object userid = Papi.MySQL.get(Database.Users.ID,
				Papi.SQL.getWhereObject(Database.Users.NAME, "=", player.getName().toLowerCase()),
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
		
		doRegisterSuccessLoginPlayer(player);
		
		Papi.MySQL.set(Map.of(
				Database.Users.LASTLOGIN_DATETIME, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				Users.UUID, player.getUniqueId().toString(),
				Users.LASTLOGIN_IP, player.getAddress().getAddress().getHostAddress().toString()
				), Database.Users.ID, "=", userid, Database.Users.TABLE);
		
//		Papi.MySQL.set(Database.Users.LASTLOGIN_DATETIME, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), Database.Users.ID, "=", userid, Database.Users.TABLE);
//		Papi.MySQL.set(Users.UUID, player.getUniqueId(), Database.Users.ID, "=", userid, Users.TABLE);
//		Papi.MySQL.set(Users.LASTLOGIN_IP, player.getAddress().getAddress().getHostAddress().toString(), Database.Users.ID, "=", userid, Users.TABLE);
		
		return true;
	}

	public static boolean onExecuteCommandRegister(Player player, String password1,  String password2) {
		if(!password1.equals(password2))
		{
			Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.CommandRegisterWrongPasswordNotEquals"));
			return false;
		}
		Papi.MySQL.put(Map.of(
				Database.Users.NAME, player.getName().toLowerCase(),
				Database.Users.REALNAME, player.getName(),
				Database.Users.PASSWORD, password1,
				Database.Users.REGISTER_IP, player.getAddress().getAddress().getHostAddress().toString(),
				Database.Users.LASTLOGIN_IP, player.getAddress().getAddress().getHostAddress().toString(),
				Database.Users.LASTLOGIN_DATETIME, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				Database.Users.UUID, player.getUniqueId().toString()
				), Database.Users.TABLE);
		
		Model.getPlayer(player).setAferLogin(true);
		
		Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.SuccessRegistered"));
		
		TitleManager.onPlayerLogin(player, true);
		
		PlayerRestoreManager.onPlayerLogin(player);
		
		Papi.Model.getPlayer(player).registerFirstLogged();
		
		return true;
	}
	
	public static void doRegisterSuccessLoginPlayer(Player player) {
		Model.getPlayer(player).setAferLogin(true);
		
		Message.sendMessage(player, Message.getMessageLang(Plugin.getLanguageYaml(), "Player.SuccessLogged"));
		
		TitleManager.onPlayerLogin(player, false);
		
		PlayerRestoreManager.onPlayerLogin(player);
		
		Papi.Model.getPlayer(player).registerLogged();
	}
}

package mkproject.maskat.LoginManager.Cmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import mkproject.maskat.LoginManager.Plugin;
import mkproject.maskat.LoginManager.UsersAPI;
import mkproject.maskat.Papi.Utils.CommandManager;
import mkproject.maskat.Papi.Utils.Message;

public class CmdChangePassword implements CommandExecutor, TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return new ArrayList<>();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager("mkp.loginmanager", sender, command, label, args, List.of("<stare_hasło>", "<nowe_hasło>", "<powtórz_nowe_hasło>"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(manager.hasArgs(3))
				this.changePassword(manager, manager.getArg(1), manager.getArg(2), manager.getArg(3));
		
		return manager.doReturn();
	}
	
	public void changePassword(CommandManager manager, String oldPassword, String newPassword1, String newPassword2) {
		if(!newPassword1.equals(newPassword2)) {
			manager.setReturnMessage(Message.getMessageLang(Plugin.getLanguageYaml(), "Player.ChangePassword.NewPasswordNotEquals"));
			return;
		}
		
		if(oldPassword.equals(newPassword1)) {
			manager.setReturnMessage(Message.getMessageLang(Plugin.getLanguageYaml(), "Player.ChangePassword.NewPasswordEqualsOldPassword"));
			return;
		}
		
		UsersAPI.changePassword(manager.getPlayer(), oldPassword, newPassword1);
		manager.setReturnMessage(null);
	}
}

package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import mkproject.maskat.AdminUtils.Config;
import mkproject.maskat.AdminUtils.Config.ConfigKey;
import mkproject.maskat.AdminUtils.Database;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.CommandManager;
import mkproject.maskat.Papi.Utils.Message;

public class CmdUnmute implements CommandExecutor, TabCompleter {

	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args);

		manager.registerArgTabComplete(0, manager.getOnlinePlayersNameList());
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args, List.of("<player>", "[reason]"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		if(manager.hasArgs(1,2))
		{
			this.removeMute(manager, manager.getChosenPlayerFromArg(1, false), manager.getStringArgStart(2));
		}
		return manager.doReturn();
	}
	
	// --------- /unmute <player> [reason]
	public void removeMute(CommandManager manager, Player destPlayer, String reason) {
		if(destPlayer == null)
			return;
		
		if(!Papi.Model.getPlayer(destPlayer).isMuted())
		{
			manager.setReturnMessage("&c&oGracz &e&o"+destPlayer.getName()+"&c&o nie ma aktywnego wyciszenia");
			return;
		}
		
		if(reason == null)
			reason = "";
		
		int result = Database.Mutes.removeMute(destPlayer, manager.getPlayer(), reason);
		
		if(result == 1)
		{
			Papi.Model.getPlayer(destPlayer).setMuted(null);
			Message.sendBroadcast(Papi.Model.getPlayer(destPlayer).getNameWithPrefix()+" &7może już pisać. Wyciszenie zostało anulowane przez "+Papi.Model.getPlayer(manager.getPlayer()).getNameWithPrefix()+"&7."+((reason!="") ? ("\nPowód: &6"+reason) : ""));
//			manager.setReturnMessage("&a&oWyciszyłeś gracza &e&o"+destPlayer.getName());
			manager.setReturnMessage(null);
		}
		else if(result == -1)
			manager.setReturnMessage("&c&oZapis o wyciszeniu gracza &e&o"+destPlayer.getName()+"&c&o nie został odnaleziony w bazie");
		else if(result == 0)
			manager.setReturnMessage("&c&oWystąpił błąd podczas próby anulowania wyciszenia gracza &e&o"+destPlayer.getName());
			
	}
}

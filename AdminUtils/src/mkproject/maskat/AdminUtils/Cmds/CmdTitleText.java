package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mkproject.maskat.AdminUtils.Config;
import mkproject.maskat.AdminUtils.Config.ConfigKey;
import mkproject.maskat.Papi.Utils.CommandManager;
import mkproject.maskat.Papi.Utils.Message;


public class CmdTitleText implements CommandExecutor {
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args, List.of("<player|*> <<title>%nl%<subtitle>>"));
		
		if(manager.isPlayer())
			if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
				return manager.doReturn();
		
		if(manager.hasArgStart(2)) // /title <player|*> <<Title>%nl%<Subtitle>>
		{
			if(manager.hasArg(1, "*"))
				this.sendTitleAll(manager, manager.getStringArgStart(2));
			else
				this.sendTitle(manager, manager.getChosenPlayerFromArg(1, true), manager.getStringArgStart(2));
		}
		
		return manager.doReturn();
	}
	
	// --------- /title * <<Title>%nl%<Subtitle>>
	private void sendTitleAll(CommandManager manager, String msg) {
		String[] msgArr = msg.split("%nl%");
		String title = msgArr[0];
		String subtitle = null;
		
		if(msgArr.length >= 2)
			subtitle = msgArr[1];

		for(Player player : Bukkit.getOnlinePlayers())
			Message.sendTitle(player, title, subtitle);
		
		if(subtitle == null)
			subtitle = "";
		
		manager.setReturnMessage("&a&oWysłałeś wszystkim napis o treści:\n&r&f"+title+"\n&r&f"+subtitle);
	}
	
	// --------- /title <player> <<Title>%nl%<Subtitle>>
	public void sendTitle(CommandManager manager, Player destPlayer, String msg) {
		if(destPlayer == null)
			return;
		
		String[] msgArr = msg.split("%nl%");
		String title = msgArr[0];
		String subtitle = null;
		
		if(msgArr.length >= 2)
			subtitle = msgArr[1];
		
		Message.sendTitle(destPlayer, title, subtitle);
		
		if(subtitle == null)
			subtitle = "";
		
		manager.setReturnMessage(destPlayer,
				"&a&oWysłałeś sobie napis o treści:\n&r&f"+title+"\n&r&f"+subtitle,
				"&a&oWysłałeś graczowi &o&e"+destPlayer.getName()+"&o&a napis o treści:\n&r&f"+title+"\n&r&f"+subtitle
				);
	}
}

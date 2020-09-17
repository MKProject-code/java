package mkproject.maskat.ChatManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import mkproject.maskat.Papi.Utils.Message;

public class ExecuteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length != 0 && label.equalsIgnoreCase("broadcast"))
		{
			Message.sendBroadcast(Message.getCommandMessage(args, 0));
			return true;
		}
		else if (args.length != 0 && args[0].equalsIgnoreCase("reload")) {
	      Plugin.reloadAllConfigs();
	      ChatFormatter.reloadConfigValues();
	
	      sender.sendMessage("Reloaded successfully.");
	      return true;
	}
	
	  return false;
	}

}

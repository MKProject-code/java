package mkproject.maskat.DropManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ExecuteCommand implements CommandExecutor {

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length != 0) {
			if(label.equalsIgnoreCase("dropmanager")) {
				if(args[0].equalsIgnoreCase("reload")) {
				    Plugin.reloadAllConfigs();
				    
				    sender.sendMessage("Reloaded successfully.");
				    return true;
				}
			}
		}
		return false;
	}

}

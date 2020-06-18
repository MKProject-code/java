package mkproject.maskat.WorldManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ExecuteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length != 0) {
			if(label.equalsIgnoreCase("worldmanager")) {
				if(args[0].equalsIgnoreCase("reload")) {
					
					Plugin.getPlugin().getLogger().info("Reloading worlds configurations...");
					
				    Plugin.reloadAllConfigs();
				    
				    sender.sendMessage("Reloaded successfully.");
				    return true;
				}
			}
		}
		return false;
	}
}
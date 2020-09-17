package mkproject.maskat.MiniGamesManager.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import mkproject.maskat.MiniGamesManager.Plugin;
import mkproject.maskat.MiniGamesManager.Game.GamesManager;

public class ManagerCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length < 1)
			return false;
		
		if(args[0].equalsIgnoreCase("reload"))
		{
			Plugin.getPlugin().reloadConfig();
			sender.sendMessage("Config reloaded.");
			return true;
		}
		else if(args[0].equalsIgnoreCase("restart"))
		{
			GamesManager.restartAllAndReinitialize();
			sender.sendMessage("All minigames restarted.");
			return true;
		}
		
		return false;
	}

}

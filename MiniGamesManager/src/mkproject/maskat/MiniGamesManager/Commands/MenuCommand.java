package mkproject.maskat.MiniGamesManager.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mkproject.maskat.MiniGamesManager.Plugin;

public class MenuCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Plugin.getPlayerMenu().openPapiMenuPage((Player) sender);
		
		return true;
	}
}

package me.maskat.customcommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Utils.Message;

public class ExecuteCommandReload implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Plugin.getPlugin().reloadConfig();
		Config.init(Plugin.getPlugin().getConfig());
		
		if(sender instanceof Player) 
		{
			Message.sendMessage((Player)sender, "&aConfiguration reloaded.");
		}
		else
		{
			Message.sendConsoleInfo(Plugin.getPlugin(), "&aConfiguration reloaded.");
		}
		
		return false;
	}

}

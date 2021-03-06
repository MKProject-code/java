package me.maskat.MonsterEvent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ExecuteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length != 0) {
			if(sender.hasPermission("mkp.arenaplugin.monsterevent.reload") && label.equalsIgnoreCase("arenapluginmonsterevent")) {
				if(args[0].equalsIgnoreCase("reload")) {
				    Plugin.reloadAllConfigs();
				    
				    sender.sendMessage("Reloaded successfully.");
				    return true;
				}
				if(args[0].equalsIgnoreCase("start")) {
					new SchedulerTask().runTaskThread();
					
					sender.sendMessage("MonsterEvent started.");
					return true;
				}
			}
		}
	
	  return false;
	}
}
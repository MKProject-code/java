package me.maskat.WorldEditTools;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.maskat.WorldEditTools.models.Model;
import mkproject.maskat.Papi.Utils.CommandManager;

public class CmdSignLine implements CommandExecutor, TabCompleter {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager("mkp.worldedittools",sender, command, label, args);
		
		manager.registerArgTabComplete(0, List.of("1","2","3","4"));
		manager.registerArgTabComplete(1, List.of("1","2","3","4"), List.of(Model.getPlayer(manager.getPlayer()).getSignLine(manager.getIntArg(1))));
		
		return manager.getTabComplete();
	}
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager("mkp.worldedittools",sender, command, label, args, List.of("<line>","<text>"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		if(manager.hasArgStart(2) && manager.isNumericArg(1))
		{
			if(Model.getPlayer(manager.getPlayer()).editSignLines(manager.getIntArg(1), manager.getStringArgStart(2)))
				manager.setReturnMessage("&a&oSignEdit success");
			else
				manager.setReturnMessage("&c&oSignEdit error");
		}
		
		return manager.doReturn();
	}
}

package me.maskat.ArenaManager.Cmds;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.maskat.ArenaManager.Models.ArenesModel;
import mkproject.maskat.Papi.Utils.CommandManager;

public class CmdArena implements CommandExecutor, TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager("mkp.arenamanager",sender, command, label, args);

		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager("mkp.arenamanager",sender, command, label, args);

		if(!manager.isPlayer())
			return false;
		
		this.openArenesMenu(manager);

		return manager.doReturn();
	}
	
	private void openArenesMenu(CommandManager manager) {

		if(!ArenesModel.getPlayer(manager.getPlayer()).isBlockArenaMenu())
		{
			ArenesModel.getPlayer(manager.getPlayer()).openArenesMenu();
			manager.setReturnMessage(null);
		}
		else
			manager.setReturnMessage("&6&oNie możesz teraz tego zrobić! Twoja arena wystartowała");
	}
}

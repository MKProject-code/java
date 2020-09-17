package io.github.cactusburrito.customcommands.commands;

import io.github.cactusburrito.customcommands.CustomCommandsMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Command executor for this plugin.
 */
public class PluginCommandExecutor implements CommandExecutor
{
	/**
	 * {@inheritDoc}
	 */
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
	{
		if(args!= null && args.length > 0)
		{
			String commandArg_0 = args[0].toLowerCase();

			if(commandArg_0.equals("reload"))
			{
				if(commandSender.hasPermission("obsidianraider.reload"))
				{
					CustomCommandsMain.GetInstance().Reload();

					commandSender.sendMessage(ChatColor.DARK_GREEN + "[" + CustomCommandsMain.GetInstance().PLUGIN_PREFIX + "] Plugin reloaded!");
				} else
				{
					commandSender.sendMessage(ChatColor.DARK_RED + "[" + CustomCommandsMain.GetInstance().PLUGIN_PREFIX + "] No permission to reload plugin.");
				}
			}
		}

		return false;
	}
}

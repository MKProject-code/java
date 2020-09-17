package me.maskat.MenuHelpManager.Cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.maskat.MenuHelpManager.Plugin;
import me.maskat.MenuHelpManager.PlayerMenu.VipMenu;
import mkproject.maskat.Papi.Utils.CommandManager;

public class CmdVip implements CommandExecutor {
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.PERMISSION_PREFIX, sender, command, label, args);
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		new VipMenu().initOpenMenu(manager.getPlayer(), null);
		return false;
	}
}

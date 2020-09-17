package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mkproject.maskat.AdminUtils.Plugin;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.CommandManager;


public class CmdEnderChest implements CommandExecutor {
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args);
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
			return manager.doReturn();
		
		if(manager.hasArgs(0,1)) // /ec [player]
			this.openEnderChest(manager, manager.getChosenPlayerFromArg(1, true));
		
		return manager.doReturn();
	}
	
	// --------- /back [player]
	public void openEnderChest(CommandManager manager, Player destPlayer) {
		if(destPlayer == null)
			return;
		
		manager.getPlayer().openInventory(destPlayer.getEnderChest());
		manager.setReturnMessage(null);
	}
}

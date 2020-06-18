package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CmdFly implements CommandExecutor {
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager_local manager = new CommandManager_local(sender, command, label, args, List.of("0|1","[player]"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
			return manager.doReturn();
		
		if(manager.hasArgs(1,2))
		{
			if(manager.hasArgAndPermission(1, "0") && manager.hasArgs(1,2)) // /fly 0 [player]
			{
				this.setFly(manager, manager.getChosenPlayerFromArg(2, true), false);
				return manager.doReturn();
			}
			else if(manager.hasArgAndPermission(1, "1") && manager.hasArgs(1,2)) // /fly 1 [player]
			{
				this.setFly(manager, manager.getChosenPlayerFromArg(2, true), true);
				return manager.doReturn();
			}
		}
		return manager.doReturn();
	}
	
	// --------- /fly 0|1 [player]
	public void setFly(CommandManager_local manager, Player destPlayer, boolean enable) {
		if(destPlayer == null)
			return;
		
		destPlayer.setAllowFlight(enable);
		
		manager.setReturnMessage(destPlayer,
				"&a&oZmieniłeś możliwość latania na &b&o"+(enable?"dozwolone":"zabronione"),
				"&a&oZmieniłeś możliwość latania graczowi &e&o"+destPlayer.getName() + "&a&o na &b&o"+(enable?"dozwolone":"zabronione"));
	}
}

package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CmdFeed implements CommandExecutor {
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager_local manager = new CommandManager_local(sender, command, label, args, List.of("[player]"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
			return manager.doReturn();
		
		if(manager.hasArgs(0,1))// /feed [player]
			this.doRepair(manager, manager.getChosenPlayerFromArg(1, true));
		
		return manager.doReturn();
	}
	
	// --------- /feed [player]
	public void doRepair(CommandManager_local manager, Player destPlayer) {
		if(destPlayer == null)
			return;
		
		destPlayer.setFoodLevel(20);

		manager.setReturnMessage(destPlayer,
				"&a&oNakarmiłeś się",
				"&a&oNakarmiłeś gracza &e&o"+destPlayer.getName());
	}
}

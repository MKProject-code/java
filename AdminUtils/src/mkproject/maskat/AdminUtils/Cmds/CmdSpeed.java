package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CmdSpeed implements CommandExecutor {
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager_local manager = new CommandManager_local(sender, command, label, args, List.of("0-5","[player]"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
			return manager.doReturn();
		
		if(manager.hasArgs(1,2))
		{
			if(!manager.isNumericArg(1))
				return manager.doReturn();
			
			this.setSpeed(manager, manager.getChosenPlayerFromArg(2, true), Float.parseFloat(manager.getArg(1)));
		}
		
		return manager.doReturn();
	}
	
	// --------- /speed <speed> [player]
	public void setSpeed(CommandManager_local manager, Player destPlayer, float speed) {
		if(destPlayer == null)
			return;
		
		if(speed < 0)
			speed = 0;
		else if(speed > 5)
			speed = 5;
		
		destPlayer.setWalkSpeed(0.2f*speed);
		destPlayer.setFlySpeed(0.1f*speed);
		
		manager.setReturnMessage(destPlayer,
				"&a&oZmieniłeś prętkośc poruszania się na &b&ox"+speed,
				"&a&oZmieniłeś prętkośc poruszania się graczowi &e&o"+destPlayer.getName() + "&a&o na &b&ox"+speed);
	}
}

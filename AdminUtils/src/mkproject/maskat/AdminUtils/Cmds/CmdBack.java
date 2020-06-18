package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;


public class CmdBack implements CommandExecutor {
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager_local manager = new CommandManager_local(sender, command, label, args, List.of("[player]"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
			return manager.doReturn();
		
		if(manager.hasArgs(0,1)) // /back [player]
			this.teleportToLastLocation(manager, manager.getChosenPlayerFromArg(1, true));
		
		return manager.doReturn();
	}
	
	// --------- /back [player]
	public void teleportToLastLocation(CommandManager_local manager, Player destPlayer) {
		if(destPlayer == null)
			return;
		
		if(Papi.Model.getPlayer(destPlayer).getGlobalLastLocation() == null)
		{
			manager.setReturnMessage(destPlayer,
					"&c&oBrak danych o ostatniej lokalizacji",
					"&c&oBrak danych o ostatniej lokalizacji gracza &e&o"+destPlayer.getName());
			return;
		}
		
		destPlayer.teleport(Papi.Model.getPlayer(destPlayer).getGlobalLastLocation());
		
		manager.setReturnMessage(destPlayer,
				"&a&oTeleportowałeś się do swojej ostatniej lokalizacji",
				"&a&oTeleportowałeś gracza &e&o"+destPlayer.getName()+"&a&o do jego ostatniej lokalizacji");
	}
}

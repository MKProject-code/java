package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdTphere implements CommandExecutor {

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager_local manager = new CommandManager_local(sender, command, label, args, List.of("<player>"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		if(manager.hasArgs(1))
			this.teleportPlayerHere(manager, manager.getChosenPlayerFromArg(1, false));
		
		return manager.doReturn();
	}
	
	private void teleportPlayerHere(CommandManager_local manager, Player destPlayer) {
		if(destPlayer == null)
			return;
		
		if(manager.playerTeleport(destPlayer, manager.getPlayer().getLocation()))
			manager.setReturnMessage("&a&oTeleportowałeś gracza &e&o"+destPlayer.getName()+"&a&o do siebie");
	}
}

package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import mkproject.maskat.AdminUtils.Config;
import mkproject.maskat.AdminUtils.Config.ConfigKey;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.CommandManager;

public class CmdSpawn implements CommandExecutor, TabCompleter {

	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args);

		manager.registerArgTabComplete(0, manager.getOnlinePlayersCanChooseNameList(true, true));
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args);
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		if(manager.hasArgs(0,1)) // /spawn [player]
			this.teleportToSpawn(manager, manager.getChosenPlayerFromArg(1, true));
		
		return manager.doReturn();
	}
	
	// --------- /spawn
	private void teleportToSpawn(CommandManager manager, Player destPlayer) {
		if(destPlayer == null)
			return;
		
		if(destPlayer == manager.getPlayer())
			Papi.Model.getPlayer(manager.getPlayer()).teleportTimer(Papi.Server.getServerSpawnLocation());
		else
			destPlayer.teleport(Papi.Server.getServerSpawnLocation());
		manager.setReturnMessage(null);
	}
}

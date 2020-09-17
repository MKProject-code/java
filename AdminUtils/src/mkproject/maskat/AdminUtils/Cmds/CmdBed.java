package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import mkproject.maskat.AdminUtils.Config;
import mkproject.maskat.AdminUtils.Config.ConfigKey;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.CommandManager;

public class CmdBed implements CommandExecutor, TabCompleter {

	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args);
		
		manager.registerArgTabComplete(0, manager.getOnlinePlayersCanChooseNameList(true, false));
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args, List.of("[player]"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		Player destPlayer = manager.getChosenPlayerFromArg(1, true);
		if(destPlayer == null)
			this.teleportToBed(manager, manager.getPlayer());
		else
			this.teleportToBed(manager, destPlayer);
		
		return manager.doReturn();
	}
	
	// --------- /back [player]
	private void teleportToBed(CommandManager manager, Player destPlayer) {
//		if(destPlayer == null)
//			return;
		Location bedSpawnLoc = manager.getPlayer().getBedSpawnLocation();
		if(bedSpawnLoc == null) {
			manager.setReturnMessage("&c&oNie ustawiłeś łóżka lub jest ono niedostępne");
			return;
		}
		
		Papi.Model.getPlayer(manager.getPlayer()).teleportTimerExpLvl(bedSpawnLoc, manager.getUsePrice(5));
		manager.setReturnMessage(null);
	}
}

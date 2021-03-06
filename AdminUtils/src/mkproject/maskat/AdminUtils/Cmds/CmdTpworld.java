package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import mkproject.maskat.AdminUtils.Plugin;
import mkproject.maskat.Papi.Utils.CommandManager;

public class CmdTpworld implements CommandExecutor, TabCompleter {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args);
		
		List<String> worlds = manager.getWorldsNameListWithAccess(true);
		
		manager.registerArgTabComplete(0, worlds);
		manager.registerArgTabComplete(1, worlds, manager.getOnlinePlayersCanChooseNameList(true, true));
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args, List.of("<world>","[player]"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		if(manager.hasArgs(1,2))
			this.teleportWorldSpawn(manager, manager.getChosenWorldFromArg(1, true), manager.getChosenPlayerFromArg(2, true));
		
		return manager.doReturn();
	}
	
	private void teleportWorldSpawn(CommandManager manager, World world, Player destPlayer) {
		if(destPlayer == null || world == null)
			return;
		
		if(manager.playerTeleport(destPlayer, world.getSpawnLocation()))
			manager.setReturnMessage(destPlayer,
					"&a&oTeleportowałeś się do świata &b&o"+world.getName(),
					"&a&oTeleportowałeś gracza &e&o"+destPlayer.getName() + "&a&o do swiata &b&o"+world.getName());
	}
}

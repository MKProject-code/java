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

public class CmdSurvival implements CommandExecutor, TabCompleter {

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
		
		if(manager.hasArgs(0,1)) // /survival [player]
			this.teleportToSpawn(manager, manager.getChosenPlayerFromArg(1, true));
		
		return manager.doReturn();
	}
	
	// --------- /survival
	private void teleportToSpawn(CommandManager manager, Player destPlayer) {
		if(destPlayer == null)
			return;
		
		if(destPlayer.getWorld() == Papi.Server.getServerSpawnWorld()) {
			manager.setReturnMessage(destPlayer,
					"&c&oBrak danych o twojej ostatniej lokalizacji na mapie Survival.\n&6&oSpróbuj wejść w jeden z portali :)",
					"&c&oBrak informacji o ostatniej lokalizacji na mapie Survival od gracza &e&o"+destPlayer.getName()+"&c&o. &6&oGracz znajduje się aktualnie na SPAWN");
			return;
		}
		else if(destPlayer.getWorld() == Papi.Server.getSurvivalWorld() || destPlayer.getWorld().getName().equals("world_nether") || destPlayer.getWorld().getName().equals("world_the_end"))
		{
			manager.setReturnMessage(destPlayer,
					"&c&oJesteś aktualnie na mapie Survival",
					"&c&oGracz &e&o"+destPlayer.getName()+"&c&o jest aktualnie na mapie Survival");
			return;
		}
		
		if(Papi.Model.getPlayer(destPlayer).getSurvivalLastLocation() == null)
		{
			manager.setReturnMessage(destPlayer,
					"&c&oBrak danych o twojej ostatniej lokalizacji na mapie Survival.\n&c&oSpróbuj użyć &6&o/spawn&c&o i wejść w portal :)",
					"&c&oBrak informacji o ostatniej lokalizacji na mapie Survival od gracza &e&o"+destPlayer.getName()+"&c&o. Spróbuj użyć &6/spawn [player]");
		}
		else
		{
			if(destPlayer == manager.getPlayer())
				Papi.Model.getPlayer(manager.getPlayer()).teleportTimer(Papi.Model.getPlayer(destPlayer).getSurvivalLastLocation());
			else
				destPlayer.teleport(Papi.Model.getPlayer(destPlayer).getSurvivalLastLocation());
			manager.setReturnMessage(null);
		}
	}
}

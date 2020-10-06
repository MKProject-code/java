package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import mkproject.maskat.AdminUtils.Plugin;
import mkproject.maskat.Papi.Papi;


public class CmdGamemode implements CommandExecutor, TabCompleter  {
	
	private void registerArgAliases(CommandManager_local manager) {
		manager.registerArgAlias(1, "survival", "0");
		manager.registerArgAlias(1, "survival", "su");
		manager.registerArgAlias(1, "survival", "sur");
		manager.registerArgAlias(1, "survival", "surv");
		manager.registerArgAlias(1, "survival", "survi");
		manager.registerArgAlias(1, "survival", "surviv");
		manager.registerArgAlias(1, "survival", "surviva");
		
		manager.registerArgAlias(1, "creative", "1");
		manager.registerArgAlias(1, "creative", "c");
		manager.registerArgAlias(1, "creative", "cr");
		manager.registerArgAlias(1, "creative", "cre");
		manager.registerArgAlias(1, "creative", "crea");
		manager.registerArgAlias(1, "creative", "creat");
		manager.registerArgAlias(1, "creative", "creati");
		manager.registerArgAlias(1, "creative", "creativ");
		
		manager.registerArgAlias(1, "adventure", "2");
		manager.registerArgAlias(1, "adventure", "a");
		manager.registerArgAlias(1, "adventure", "ad");
		manager.registerArgAlias(1, "adventure", "adv");
		manager.registerArgAlias(1, "adventure", "adve");
		manager.registerArgAlias(1, "adventure", "adven");
		manager.registerArgAlias(1, "adventure", "advent");
		manager.registerArgAlias(1, "adventure", "adventu");
		manager.registerArgAlias(1, "adventure", "adventur");
		
		manager.registerArgAlias(1, "spectator", "3");
		manager.registerArgAlias(1, "spectator", "sp");
		manager.registerArgAlias(1, "spectator", "spe");
		manager.registerArgAlias(1, "spectator", "spec");
		manager.registerArgAlias(1, "spectator", "spect");
		manager.registerArgAlias(1, "spectator", "specta");
		manager.registerArgAlias(1, "spectator", "spectat");
		manager.registerArgAlias(1, "spectator", "spectato");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager_local manager = new CommandManager_local(sender, command, label, args);
		
		this.registerArgAliases(manager);
		
		manager.registerArgTabComplete(0, List.of("survival","creative","adventure","spectator"));
		manager.registerArgTabComplete(1, List.of("survival","creative","adventure","spectator"), manager.getOnlinePlayersNameList());
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager_local manager = new CommandManager_local(sender, command, label, args, List.of("0|1|2|3","[player]"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
			return manager.doReturn();
		
		this.registerArgAliases(manager);
		
		//manager.registerArgUsage(0, "survival|creative|adventure|spectator","[player]");
		manager.registerArgUsage(1, "survival","[player]");
		manager.registerArgUsage(1, "creative","[player]");
		manager.registerArgUsage(1, "adventure","[player]");
		manager.registerArgUsage(1, "spectator","[player]");
		
		// --------- /gm 0|1|2|3 [player]
		if(manager.hasArgs(1,2))
		{
			if(manager.hasArgAndPermission(1, "survival") && manager.hasArgs(1,2)) // /gamemode survival [player]
			{
				this.setGameMode(manager, manager.getChosenPlayerFromArg(2, true), GameMode.SURVIVAL);
				return manager.doReturn();
			}
			else if(manager.hasArgAndPermission(1, "creative") && manager.hasArgs(1,2)) // /gamemode creative [player]
			{
				this.setGameMode(manager, manager.getChosenPlayerFromArg(2, true), GameMode.CREATIVE);
				return manager.doReturn();
			}
			else if(manager.hasArgAndPermission(1, "adventure") && manager.hasArgs(1,2)) // /gamemode adventure [player]
			{
				this.setGameMode(manager, manager.getChosenPlayerFromArg(2, true), GameMode.ADVENTURE);
				return manager.doReturn();
			}
			else if(manager.hasArgAndPermission(1, "spectator") && manager.hasArgs(1,2)) // /gamemode spectator [player]
			{
				this.setGameMode(manager, manager.getChosenPlayerFromArg(2, true), GameMode.SPECTATOR);
				return manager.doReturn();
			}
		}
		return manager.doReturn();
	}
	
	// --------- /gm 0|1|2|3 [player]
	public void setGameMode(CommandManager_local manager, Player destPlayer, GameMode gamemode) {
		if(destPlayer == null)
			return;
		
		World destPlayerWorld = destPlayer.getWorld();
		
		boolean protectedWorld = false;
		
		if(gamemode != GameMode.SURVIVAL) {
			if(destPlayerWorld == Papi.Server.getServerLobbyWorld()
					|| destPlayerWorld == Papi.Server.getServerSpawnWorld()
					|| destPlayerWorld == Papi.Server.getSurvivalWorld()
					|| destPlayerWorld == Papi.Server.getNetherWorld()
					|| destPlayerWorld == Papi.Server.getTheEndWorld()
					)
				protectedWorld = true;
		}
		
		if(!protectedWorld || destPlayer == manager.getPlayer())
			destPlayer.setGameMode(gamemode);
		
		manager.setReturnMessage(destPlayer,
				"&a&oZmieniłeś sobie tryb gry na &b&o"+gamemode.name() + (!protectedWorld ? "" : "\n&c&oUWAGA! Jesteś na mapie Survival!"),
				"&a&oZmieniłeś tryb gry graczowi &e&o"+destPlayer.getName() + "&a&o na &b&o"+gamemode.name() + (!protectedWorld ? "" : "\n&c&oUWAGA! Tryb gry nie został zmieniony! Gracz jest na mapie Survival!"));
	}
}

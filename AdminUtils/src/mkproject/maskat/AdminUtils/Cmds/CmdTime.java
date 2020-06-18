package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;


public class CmdTime implements CommandExecutor, TabCompleter {
	
	private void registerArgAliases(CommandManager_local manager) {
		manager.registerArgAlias(1, "day", "d");
		manager.registerArgAlias(1, "day", "da");
		
		manager.registerArgAlias(1, "noon", "no");
		manager.registerArgAlias(1, "noon", "noo");
		
		manager.registerArgAlias(1, "night", "ni");
		manager.registerArgAlias(1, "night", "nig");
		manager.registerArgAlias(1, "night", "nigh");
		
		manager.registerArgAlias(1, "midnight", "m");
		manager.registerArgAlias(1, "midnight", "mi");
		manager.registerArgAlias(1, "midnight", "mid");
		manager.registerArgAlias(1, "midnight", "midn");
		manager.registerArgAlias(1, "midnight", "midni");
		manager.registerArgAlias(1, "midnight", "midnig");
		manager.registerArgAlias(1, "midnight", "midnigh");
		manager.registerArgAlias(1, "midnight", "midnight");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager_local manager = new CommandManager_local(sender, command, label, args);
		
		this.registerArgAliases(manager);
		
		manager.registerArgTabComplete(0, List.of("day","noon","night","midnight","0-24000"));
		manager.registerArgTabComplete(1, null, manager.getWorldsNameList());
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager_local manager = new CommandManager_local(sender, command, label, args, List.of("day|noon|night|midnight|0-24000","[player]"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
			return manager.doReturn();
		
		manager.registerArgUsage(1, null,"[world]");
		
		if(manager.hasArgs(1,2))
		{
			if(manager.hasArg(1, "day")) // /time day [player]
			{
				this.setTime(manager, manager.getChosenWorldFromArg(2, true), 1000L);
				return manager.doReturn();
			}
			else if(manager.hasArg(1, "noon")) // /time noon [world]
			{
				this.setTime(manager, manager.getChosenWorldFromArg(2, true), 6000L);
				return manager.doReturn();
			}
			else if(manager.hasArg(1, "night")) // /time noon [world]
			{
				this.setTime(manager, manager.getChosenWorldFromArg(2, true), 13000L);
				return manager.doReturn();
			}
			else if(manager.hasArg(1, "midnight")) // /time noon [world]
			{
				this.setTime(manager, manager.getChosenWorldFromArg(2, true), 18000L);
				return manager.doReturn();
			}
			else if(manager.isNumericArg(1)) // /time 0-24000 [world]
			{
				this.setTime(manager, manager.getChosenWorldFromArg(2, true), Long.parseLong(manager.getArg(1)));
				return manager.doReturn();
			}
		}
		return manager.doReturn();
	}
	
	// --------- /time day [world] - 1000
	// --------- /time noon [world] - 6000
	// --------- /time night [world] - 13000
	// --------- /time midnight [world] - 18000
	// --------- /time 0-24000 [world]
	public void setTime(CommandManager_local manager, World destWorld, long timeTick) {
		if(destWorld == null)
			return;
		
		destWorld.setTime(timeTick);
		
		manager.setReturnMessage(destWorld,
				"&a&oZmieniłeś czas w obecnym świecie na &b&o" + timeTick,
				"&a&oZmieniłeś czas w świecie &e&o" + destWorld.getName() + "&a&o na &b&o" + timeTick);
	}
}

package mkproject.maskat.CmdBlockAdvancedManager.Cmds;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import mkproject.maskat.CmdBlockAdvancedManager.Plugin;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.MenuManager;
import mkproject.maskat.CmdBlockAdvancedManager.Models.CbamModel;
import mkproject.maskat.CmdBlockAdvancedManager.Models.CbamPlayer;
import mkproject.maskat.CmdBlockAdvancedManager.ProjectManager.ProjectManager;
import mkproject.maskat.CmdBlockAdvancedManager.ProjectManager.ProjectManager.ProjectStartReturn;
import mkproject.maskat.Papi.Utils.CommandManager;

public class CmdBlockAdvancedManager implements CommandExecutor, TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args);
		
		if(!manager.setArgTabComplete(1, List.of("menu", "start", "end", "break", "preview", "debug")))
		{
			if(manager.hasArgTabComplete(1, "start")) {
				if(manager.hasArgOrSetTabComplete(2, ProjectManager.getAvailableWorldsList()))
					if(manager.hasArgOrSetTabComplete(3, ProjectManager.getAvailableProjectsList(manager.getArg(2))))
						if(manager.hasArgOrSetTabComplete(4, Stream.of(ProjectManager.getAvailableActionsGroupsList(manager.getArg(2), manager.getArg(3)), List.of("all")).flatMap(Collection::stream).collect(Collectors.toList())))
							manager.setArgTabComplete(5, manager.getWorldsNameList());
			}
			else if(manager.hasArgTabComplete(1, "break")) {
				if(manager.hasArgOrSetTabComplete(2, ProjectManager.getPlayedWorldsList()))
					if(manager.hasArgOrSetTabComplete(3, ProjectManager.getPlayedProjectsList(manager.getArg(2))))
						manager.setArgTabComplete(4, Stream.of(ProjectManager.getPlayedActionsGroupsList(manager.getArg(2), manager.getArg(3)), List.of("all")).flatMap(Collection::stream).collect(Collectors.toList()));
			}
			else if(manager.hasArgTabComplete(1, "preview")) {
				if(manager.hasArgOrSetTabComplete(2, List.of("start", "break")))
					if(manager.hasArgTabComplete(2, "start"))
					{
						if(manager.hasArgOrSetTabComplete(3, ProjectManager.getAvailableWorldsList()))
							if(manager.hasArgOrSetTabComplete(4, ProjectManager.getAvailableProjectsList(manager.getArg(3))))
								if(manager.hasArgOrSetTabComplete(5, Stream.of(ProjectManager.getAvailableActionsGroupsList(manager.getArg(3), manager.getArg(4)), List.of("all")).flatMap(Collection::stream).collect(Collectors.toList())))
									if(manager.hasArgOrSetTabComplete(6, manager.getValuesRangeList(0, 20*60*60)))
										if(manager.hasArgOrSetTabComplete(7, manager.getValuesRangeList(0, 20*60*60)))
											manager.setArgTabComplete(8, manager.getWorldsNameList());
					}
					else if(manager.hasArgTabComplete(2, "break"))
						manager.setArgTabComplete(3, Stream.of(ProjectManager.getDebuggingActionsGroupsList(manager.getPlayer()), List.of("all")).flatMap(Collection::stream).collect(Collectors.toList()));
			}
			else if(manager.hasArgTabComplete(1, "debug")) {
				if(manager.hasArgOrSetTabComplete(2, List.of("start", "break")))
					if(manager.hasArgTabComplete(2, "start"))
					{
						if(manager.hasArgOrSetTabComplete(3, ProjectManager.getAvailableWorldsList()))
							if(manager.hasArgOrSetTabComplete(4, ProjectManager.getAvailableProjectsList(manager.getArg(3))))
								if(manager.hasArgOrSetTabComplete(5, Stream.of(ProjectManager.getAvailableActionsGroupsList(manager.getArg(3), manager.getArg(4)), List.of("all")).flatMap(Collection::stream).collect(Collectors.toList())))
									if(manager.hasArgOrSetTabComplete(6, manager.getValuesRangeList(0, 20*60*60)))
										if(manager.hasArgOrSetTabComplete(7, manager.getValuesRangeList(0, 20*60*60)))
											manager.setArgTabComplete(8, manager.getWorldsNameList());
					}
					else if(manager.hasArgTabComplete(2, "break"))
						manager.setArgTabComplete(3, Stream.of(ProjectManager.getDebuggingActionsGroupsList(manager.getPlayer()), List.of("all")).flatMap(Collection::stream).collect(Collectors.toList()));
			}
//			else if(manager.hasArgTabComplete(2, "break")) {
//				if(manager.hasArgOrSetTabComplete(3, ProjectManager.getPlayedWorldsList()))
//					if(manager.hasArgOrSetTabComplete(4, ProjectManager.getPlayedProjectsList(manager.getArg(2))))
//						manager.setArgTabComplete(5, Stream.of(ProjectManager.getPlayedActionsGroupsList(manager.getArg(3), manager.getArg(4)), List.of("all")).flatMap(Collection::stream).collect(Collectors.toList()));
//			}
		}
//			else if(manager.hasArgTabComplete(1, "debug")) {
//				if(!manager.setArgTabComplete(2, List.of("start", "break", "end")))
//					if(manager.hasArgTabComplete(2, "start", "end"))
//						if(manager.hasArgOrSetTabComplete(3, ProjectManager.getAvailableWorldsList()))
//							if(manager.hasArgOrSetTabComplete(4, ProjectManager.getAvailableProjectsList(manager.getArg(3))))
//								if(manager.hasArgOrSetTabComplete(5, manager.getValuesRangeList(0, 216000)))
//									if(manager.hasArgOrSetTabComplete(5, Stream.concat(manager.getValuesRangeList(-1, 216000).stream(), manager.getWorldsNameList().stream()).collect(Collectors.toList())))
//										manager.setArgTabComplete(6, manager.getWorldsNameList());
//			}
		
		return manager.getTabComplete();
	}
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args, List.of("menu|start|break|preview|debug ?"));
		
		if(manager.isPlayer())
			if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
				return manager.doReturn();
		
		manager.registerArgUsage(1, "menu");
		manager.registerArgUsage(1, "start", "<world>", "<project>", "<actions_group|all>", "[world_to_play]");
		manager.registerArgUsage(1, "break", "<world_played>", "<project>", "[actions_group|all]");
		
		manager.registerArgUsage(1, "preview", "start|break ?");
		manager.registerArgUsage(2, "preview", "start", "<world>", "<project>", "<actions_group|all>", "[start_tick]", "[duration_ticks|all]", "[world_to_play]");
		manager.registerArgUsage(2, "preview", "break", "[actions_group|all]");
		
		manager.registerArgUsage(1, "debug", "start|break ?");
		manager.registerArgUsage(2, "debug", "start", "<world>", "<project>", "<actions_group|all>", "[start_tick]", "[duration_ticks|all]", "[world_to_play]");
		manager.registerArgUsage(2, "debug", "break", "[actions_group|all]");
		
		if(manager.isPlayer() && manager.hasArgs(0))
			this.openLastMenu(manager);
		else if(manager.isPlayer() && manager.hasArgStart(1) && manager.hasArg(1, "menu"))
		{
			if(manager.hasArgs(1)) // /cb menu
				this.openLastMenu(manager);
			else if(manager.hasArgs(7) && manager.isNumericArg(6)) // /cb menu <worldName> <projectName> <actionsGroup> <actionsSubGroup> <actionDelay> <actionName>
				this.openProjectsMenu(manager, manager.getArg(2), manager.getArg(3), manager.getArg(4), manager.getArg(5), manager.getIntArg(6), manager.getArg(7));
		}
		else if(manager.hasArgs(2))
		{
			if(manager.hasArgAndPermission(1, "preview")) // /cb preview break ...
			{
				if(manager.hasArgAndPermission(2, "break")) // /cb preview break [actions_group|all]
					this.doPreviewAndDebugBreak(manager, "all");
			}
			else if(manager.hasArgAndPermission(1, "debug")) // /cb debug break ...
			{
				if(manager.hasArgAndPermission(2, "break")) // /cb debug break [actions_group|all]
					this.doPreviewAndDebugBreak(manager, "all");
			}
		}
		else if(manager.hasArgs(3))
		{
			if(manager.hasArgAndPermission(1, "break")) // /cb break <world_played> <project>
				this.doBreakProject(manager, manager.getArg(2), manager.getArg(3), "all");
			else if(manager.hasArgAndPermission(1, "preview")) // /cb preview break ...
			{
				if(manager.hasArgAndPermission(2, "break")) // /cb preview break [actions_group|all]
					this.doPreviewAndDebugBreak(manager, manager.getArg(3));
			}
			else if(manager.hasArgAndPermission(1, "debug")) // /cb debug break ...
			{
				if(manager.hasArgAndPermission(2, "break")) // /cb debug break [actions_group|all]
					this.doPreviewAndDebugBreak(manager, manager.getArg(3));
			}
		}
		else if(manager.hasArgs(4))
		{
			if(manager.hasArgAndPermission(1, "start")) // /cb start <world> <project> <actions_group>
				this.doStartProject(manager, manager.getArg(2), manager.getArg(3), manager.getArg(4), manager.getArg(2));
			else if(manager.hasArgAndPermission(1, "break")) // /cb break <world_played> <project> [actions_group|all]
				this.doBreakProject(manager, manager.getArg(2), manager.getArg(3), manager.getArg(4));
		}
		else if(manager.hasArgs(5))
		{
			if(manager.hasArgAndPermission(1, "start")) // /cb start <world> <project> <actions_group> [world_to_play]
				this.doStartProject(manager, manager.getArg(2), manager.getArg(3), manager.getArg(4), manager.getArg(5));
			else if(manager.hasArgAndPermission(1, "preview")) // /cb preview start ...
			{
				if(manager.hasArgAndPermission(2, "start")) // /cb preview start <world> <project> <actions_group|all>
					this.doPreviewStartProject(manager, manager.getArg(3), manager.getArg(4), manager.getArg(5), 0, "all", manager.getChosenWorldFromArg(3, true));
			}
			else if(manager.hasArgAndPermission(1, "debug")) // /cb debug start ...
			{
				if(manager.hasArgAndPermission(2, "start")) // /cb debug start <world> <project> <actions_group|all>
					this.doDebugStartProject(manager, manager.getArg(3), manager.getArg(4), manager.getArg(5), 0, "all", manager.getChosenWorldFromArg(3, true));
			}
		}
		else if(manager.hasArgs(6) && manager.isNumericArg(6))
		{
			if(manager.hasArgAndPermission(1, "preview")) // /cb preview start ...
			{
				if(manager.hasArgAndPermission(2, "start")) // /cb preview start <world> <project> <actions_group|all> [start_tick]
					this.doPreviewStartProject(manager, manager.getArg(3), manager.getArg(4), manager.getArg(5), manager.getIntArg(6), "all", manager.getChosenWorldFromArg(3, true));
			}
			else if(manager.hasArgAndPermission(1, "debug")) // /cb debug start ...
			{
				if(manager.hasArgAndPermission(2, "start")) // /cb debug start <world> <project> <actions_group|all> [start_tick]
					this.doDebugStartProject(manager, manager.getArg(3), manager.getArg(4), manager.getArg(5), manager.getIntArg(6), "all", manager.getChosenWorldFromArg(3, true));
			}
		}
		else if(manager.hasArgs(7) && manager.isNumericArg(6))
		{
			if(manager.hasArgAndPermission(1, "preview")) // /cb preview start ...
			{
				if(manager.hasArgAndPermission(2, "start")) // /cb preview start <world> <project> <actions_group|all> [start_tick] [duration_ticks|all]
					this.doPreviewStartProject(manager, manager.getArg(3), manager.getArg(4), manager.getArg(5), manager.getIntArg(6), manager.getArg(7), manager.getChosenWorldFromArg(3, true));
			}
			else if(manager.hasArgAndPermission(1, "debug")) // /cb debug start ...
			{
				if(manager.hasArgAndPermission(2, "start")) // /cb debug start <world> <project> <actions_group|all> [start_tick] [duration_ticks|all]
					this.doDebugStartProject(manager, manager.getArg(3), manager.getArg(4), manager.getArg(5), manager.getIntArg(6), manager.getArg(7), manager.getChosenWorldFromArg(3, true));
			}
		}
		else if(manager.hasArgs(8) && manager.isNumericArg(6))
		{
			if(manager.hasArgAndPermission(1, "preview")) // /cb preview start ...
			{
				if(manager.hasArgAndPermission(2, "start")) // /cb preview start <world> <project> <actions_group|all> [start_tick] [duration_ticks|all] [world_to_play]
					this.doPreviewStartProject(manager, manager.getArg(3), manager.getArg(4), manager.getArg(5), manager.getIntArg(6), manager.getArg(7), manager.getChosenWorldFromArg(8, true));
			}
			else if(manager.hasArgAndPermission(1, "debug")) // /cb debug start ...
			{
				if(manager.hasArgAndPermission(2, "start")) // /cb debug start <world> <project> <actions_group|all> [start_tick] [duration_ticks|all] [world_to_play]
					this.doDebugStartProject(manager, manager.getArg(3), manager.getArg(4), manager.getArg(5), manager.getIntArg(6), manager.getArg(7), manager.getChosenWorldFromArg(8, true));
			}
		}
		
		return manager.doReturn();
	}

	private CbamPlayer getCbamPlayer(Player player)
	{
		if(!CbamModel.existPlayer(player))
		{
			CbamModel.addPlayer(player);
			return CbamModel.getPlayer(player);
		}
		else
		{
			CbamPlayer cbamPlayer = CbamModel.getPlayer(player);
			
		    if(cbamPlayer.isCommandBlockExist() && cbamPlayer.getCommandBlock().getType() == Material.COMMAND_BLOCK) {
		    	cbamPlayer.getCommandBlock().setType(Material.AIR);
		    	cbamPlayer.unsetCommandBlock();
		    }
		    
		    return cbamPlayer;
		}
	}
	
	private void openLastMenu(CommandManager manager) {
		MenuManager.openLastMenu(getCbamPlayer(manager.getPlayer()));
		manager.setReturnMessage(null);
	}
	private void openProjectsMenu(CommandManager manager, String worldName, String projectName, String actionsGroup, String actionsSubGroup, int actionsStartInDelay, String actionName) {
		MenuManager.openTheProjectTheActionMenu(null, getCbamPlayer(manager.getPlayer()), worldName, projectName, actionsGroup, actionsSubGroup, (long)actionsStartInDelay, actionName);
		manager.setReturnMessage(null);
	}
	
	private String getReturnStartMessage(ProjectStartReturn result, String worldToPlay) {
		//TODO update return message -> ProjectStartReturn changed
		if(result == ProjectStartReturn.PROJECT_NOT_FOUND)
			return "&cProject not found";
		else if(result == ProjectStartReturn.SUCCESSFULLY_STARTED || result == ProjectStartReturn.SUCCESSFULLY_SOMEBODY_STARTED)
			return "&aProject successfully started in world '"+worldToPlay+"'";
		else if(result == ProjectStartReturn.ACTIONS_START_NOT_FOUND || result == ProjectStartReturn.NOBODY_ACTIONS_STARTED)
			return "&cProject exist, but there are no actions to start";
		else if(result == ProjectStartReturn.CFG_ERROR_ACTION_DELAY_INVALID)
			return "&cProject and actions to start exist, but some value in database is invalid. See console for more information";
		else if(result == ProjectStartReturn.PROJECT_ALREADY_RUNNING)
			return "&cProject already running!";
		else if(result == ProjectStartReturn.NO_ONE_TASK_TO_RUN)
			return "&cNo one valid task to run in this project";
		else if(result == ProjectStartReturn.ACTIONS_GROUP_NOT_FOUND)
			return "&cNo one actions group with tasks in this project";
		else
			return "&cUnknown error, please contact the author ("+result+")";
	}
	
	private void doStartProject(CommandManager manager, String worldName, String projectName, String actionsGroup, String worldToPlay) {
		ProjectStartReturn result = ProjectManager.startProject(worldName, projectName, actionsGroup, worldToPlay);
		manager.setReturnMessage(this.getReturnStartMessage(result, worldToPlay));
	}
	private void doPreviewStartProject(CommandManager manager, String worldName, String projectName, String actionsGroup, int startTick, String durationTicks, World worldToPlay) {
		if(worldToPlay == null)
			return;
		
		ProjectStartReturn result = ProjectManager.startPreviewProject(worldName, projectName, actionsGroup, startTick, durationTicks, manager.getPlayer(), worldToPlay.getName());
		manager.setReturnMessage(this.getReturnStartMessage(result, worldName));
	}
	private void doDebugStartProject(CommandManager manager, String worldName, String projectName, String actionsGroup, int startTick, String durationTicks, World worldToPlay) {
		if(worldToPlay == null)
			return;
		
		ProjectStartReturn result = ProjectManager.startDebugProject(worldName, projectName, actionsGroup, startTick, durationTicks, manager.getPlayer(), worldToPlay.getName());
		manager.setReturnMessage(this.getReturnStartMessage(result, worldName));
	}

	private void doBreakProject(CommandManager manager, String worldName, String projectName, String actionsGroup) {
		if(ProjectManager.breakProject(worldName, projectName, actionsGroup))
			manager.setReturnMessage("&aSuccessful stopped actions group: "+actionsGroup);
		else
			manager.setReturnMessage("&cNothing changed. This project not running");
		
		//Plugin.getPlugin().getLogger().info(Message.getColorMessage("&8[&7&lCBAM&8] &dBreaked project '"+projectName+"' ("+this.worldName+") on playing map '"+this.worldToPlay+"'"));
	}
	private void doPreviewAndDebugBreak(CommandManager manager, String actionsGroup) {
		if(ProjectManager.previewBreakProject(manager.getPlayer(), actionsGroup))
			manager.setReturnMessage("&aSuccessful stopped actions group: "+actionsGroup);
		else
			manager.setReturnMessage("&cNothing changed. This project not running");
	}
	

//	private void doPreviewStartProject(CommandManager manager, String worldName, String projectName, int startTick, String worldToPlay, Integer durationTicks) {
//		if(durationTicks != null && durationTicks < 0)
//			durationTicks = null;
//		
//		ProjectStartReturn result = ProjectManager.previewStartProject(manager.getPlayer(), worldName, projectName, startTick, worldToPlay, durationTicks);
//
//		if(result == ProjectStartReturn.PROJECT_NOT_FOUND)
//			manager.setReturnMessage("&cProject not found");
//		else if(result == ProjectStartReturn.SUCCESSFULLY_STARTED)
//			manager.setReturnMessage("&aPreview for project successfully started");
//		else if(result == ProjectStartReturn.ACTIONS_START_NOT_FOUND)
//			manager.setReturnMessage("&cProject exist, but there are no actions to start");
//		else if(result == ProjectStartReturn.CFG_ERROR_ACTION_DELAY_INVALID)
//			manager.setReturnMessage("&cProject and actions to start exist, but some value in database is invalid. See console for more information");
//		else if(result == ProjectStartReturn.PROJECT_ALREADY_RUNNING)
//			manager.setReturnMessage("&cPreview or debug already running! Use: /cb preview break");
//		else if(result == ProjectStartReturn.NO_ONE_TASK_TO_RUN)
//			manager.setReturnMessage("&cNo one task to run in this project");
//		else
//			manager.setReturnMessage("&cUnknown error, please contact the author");
//	}
//	
//	private void doDebugStartProject(CommandManager manager, String worldName, String projectName, int startTick, String worldToPlay, Integer durationTicks) {
//		if(durationTicks != null && durationTicks < 0)
//			durationTicks = null;
//		
//		ProjectStartReturn result = ProjectManager.debugStartProject(manager.getPlayer(), worldName, projectName, startTick, worldToPlay, durationTicks);
//		
//		if(result == ProjectStartReturn.PROJECT_NOT_FOUND)
//			manager.setReturnMessage("&cProject not found");
//		else if(result == ProjectStartReturn.SUCCESSFULLY_STARTED)
//			manager.setReturnMessage("&aDebug for project successfully started");
//		else if(result == ProjectStartReturn.ACTIONS_START_NOT_FOUND)
//			manager.setReturnMessage("&cProject exist, but there are no actions to start");
//		else if(result == ProjectStartReturn.CFG_ERROR_ACTION_DELAY_INVALID)
//			manager.setReturnMessage("&cProject and actions to start exist, but some value in database is invalid. See console for more information");
//		else if(result == ProjectStartReturn.PROJECT_ALREADY_RUNNING)
//			manager.setReturnMessage("&cPreview or debug already running! Use: /cb debug break");
//		else if(result == ProjectStartReturn.NO_ONE_TASK_TO_RUN)
//			manager.setReturnMessage("&cNo one task to run in this project");
//		else
//			manager.setReturnMessage("&cUnknown error, please contact the author");
//	}
	
//	private void doPreviewBreakProject(CommandManager manager) {
//		if(ProjectManager.previewBreakProject(manager.getPlayer()))
//			manager.setReturnMessage("&aPreview or debug successful stopped");
//		else
//			manager.setReturnMessage("&cPreview or debug not running.");
//	}
//	
//	private void doDebugBreakProject(CommandManager manager) {
//		if(ProjectManager.debugBreakProject(manager.getPlayer()))
//			manager.setReturnMessage("&aPreview or debug successful stopped");
//		else
//			manager.setReturnMessage("&cPreview or debug not running.");
//	}
}
	
//	@Override
//	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//		if(sender instanceof Player) {
//			Player player = (Player)sender;
//			if(!CbamModel.existPlayer(player))
//				CbamModel.addPlayer(player);
//			CbamPlayer cbamPlayer = CbamModel.getPlayer(player);
//			
//	        if(cbamPlayer.isCommandBlockExist() && cbamPlayer.getCommandBlock().getType() == Material.COMMAND_BLOCK) {
//	        	cbamPlayer.getCommandBlock().setType(Material.AIR);
//	        	cbamPlayer.unsetCommandBlock();
//	        }
//			
//			ManagerMenuMain.openLastMenu(cbamPlayer);
//		}
//		return false;
//	}

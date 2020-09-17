package mkproject.maskat.CmdBlockAdvancedManager.ProjectManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import mkproject.maskat.CmdBlockAdvancedManager.Database;
import mkproject.maskat.CmdBlockAdvancedManager.Plugin;
import mkproject.maskat.CmdBlockAdvancedManager.ProjectManager.ProjectManager.ProjectStartReturn;
import mkproject.maskat.Papi.Utils.Message;

public class ProjectModel {

	private final String worldName;
	private final String projectName;
	private final String worldToPlay;
	
	//String = actionsGroup
	private final Map<String, List<BukkitTask>> delayedTasksListMap;
	private final Map<String, List<BukkitTask>> repeatingTasksListMap;
	
	protected ProjectModel(String worldName, String projectName, String worldToPlay) {
		this.worldName = worldName;
		this.projectName = projectName;
		this.worldToPlay = worldToPlay;
		this.delayedTasksListMap = new HashMap<>();
		this.repeatingTasksListMap = new HashMap<>();
	}
	
	protected boolean isEquals(String worldName, String projectName) {
		return (this.worldName.equals(worldName) && this.projectName.equals(projectName));
	}

	private enum RunType {
		PREVIEW, DEBUG, NORMAL
	}
	
	protected List<String> getPlayedActionsGroupsList() {
		return new ArrayList<>(delayedTasksListMap.keySet());
	}
	
//	protected ProjectStartReturn doPreviewStart(Player playerDebugger, int startTick, Integer durationTicks) {
//		return this.doRun(RunType.PREVIEW, startTick, durationTicks, playerDebugger);
//	}
//	protected ProjectStartReturn doDebugStart(Player playerDebugger, int startTick, Integer durationTicks) {
//		return this.doRun(RunType.DEBUG, startTick, durationTicks, playerDebugger);
//	}
	protected ProjectStartReturn doStart(String actionsGroup) {
		if(!actionsGroup.equalsIgnoreCase("all"))
			return this.prepareScheduleTasks(RunType.NORMAL, actionsGroup, 0, null, null);
		else
		{
			List<String> actionsGroups = Database.getActionsGroups(this.worldName, this.projectName);
			if(actionsGroups == null)
				return ProjectStartReturn.ACTIONS_GROUP_NOT_FOUND;
			
			boolean resultSuccess = false;
			for(String actionsGroupItem : actionsGroups)
			{
				if(this.prepareScheduleTasks(RunType.NORMAL, actionsGroupItem, 0, null, null) == ProjectStartReturn.SUCCESSFULLY_STARTED)
					resultSuccess = true;
			}
			
			if(resultSuccess)
				return ProjectStartReturn.SUCCESSFULLY_SOMEBODY_STARTED;
			else
				return ProjectStartReturn.NOBODY_ACTIONS_STARTED;
		}
	}
	public ProjectStartReturn doPreviewStart(String actionsGroup, int startTick, String durationTicks, Player playerDebugger) {
		Integer durationTicksInt = null;
		if(!durationTicks.equalsIgnoreCase("all"))
		{
			try {
				durationTicksInt = Integer.parseInt(durationTicks);
				if(durationTicksInt < 0)
					durationTicksInt = 0;
			} catch(Exception ex) {}
		}
		
		if(!actionsGroup.equalsIgnoreCase("all"))
			return this.prepareScheduleTasks(RunType.PREVIEW, actionsGroup, startTick, durationTicksInt, playerDebugger);
		else
		{
			List<String> actionsGroups = Database.getActionsGroups(this.worldName, this.projectName);
			if(actionsGroups == null)
				return ProjectStartReturn.ACTIONS_GROUP_NOT_FOUND;
			
			boolean resultSuccess = false;
			for(String actionsGroupItem : actionsGroups)
			{
				if(this.prepareScheduleTasks(RunType.PREVIEW, actionsGroupItem, startTick, durationTicksInt, playerDebugger) == ProjectStartReturn.SUCCESSFULLY_STARTED)
					resultSuccess = true;
			}
			
			if(resultSuccess)
				return ProjectStartReturn.SUCCESSFULLY_SOMEBODY_STARTED;
			else
				return ProjectStartReturn.NOBODY_ACTIONS_STARTED;
		}
	}
	public ProjectStartReturn doDebugStart(String actionsGroup, int startTick, String durationTicks, Player playerDebugger) {
		Integer durationTicksInt = null;
		if(!durationTicks.equalsIgnoreCase("all"))
		{
			try {
				durationTicksInt = Integer.parseInt(durationTicks);
				if(durationTicksInt < 0)
					durationTicksInt = 0;
			} catch(Exception ex) {}
		}
		
		if(!actionsGroup.equalsIgnoreCase("all"))
			return this.prepareScheduleTasks(RunType.DEBUG, actionsGroup, startTick, durationTicksInt, playerDebugger);
		else
		{
			List<String> actionsGroups = Database.getActionsGroups(this.worldName, this.projectName);
			if(actionsGroups == null)
				return ProjectStartReturn.ACTIONS_GROUP_NOT_FOUND;
			
			boolean resultSuccess = false;
			for(String actionsGroupItem : actionsGroups)
			{
				if(this.prepareScheduleTasks(RunType.DEBUG, actionsGroupItem, startTick, durationTicksInt, playerDebugger) == ProjectStartReturn.SUCCESSFULLY_STARTED)
					resultSuccess = true;
			}
			
			if(resultSuccess)
				return ProjectStartReturn.SUCCESSFULLY_SOMEBODY_STARTED;
			else
				return ProjectStartReturn.NOBODY_ACTIONS_STARTED;
		}
	}
	
	private ProjectStartReturn prepareScheduleTasks(RunType runType, String actionsGroup, int startTick, Integer durationTicks, Player playerDebugger) {
		
		List<BukkitTask> repeatingTasksList = this.repeatingTasksListMap.get(actionsGroup);
		if(repeatingTasksList != null)
		{
			for(BukkitTask repeatingTask : repeatingTasksList)
				repeatingTask.cancel();
			
			repeatingTasksList.clear();
		}
		else
		{
			repeatingTasksList = new ArrayList<>();
		}
		
		List<BukkitTask> delayedTasksList = this.delayedTasksListMap.get(actionsGroup);
		if(delayedTasksList != null)
		{
			for(BukkitTask delayedTask : delayedTasksList)
				delayedTask.cancel();
			
			delayedTasksList.clear();
		}
		else
		{
			delayedTasksList = new ArrayList<>();
		}
		
		long actionsDelayLongMax = -1;
		
		actionsDelayLongMax = scheduleTasks(runType, actionsGroup, startTick, durationTicks, playerDebugger, repeatingTasksList, delayedTasksList, actionsDelayLongMax);
	
		if(actionsDelayLongMax == -1)
			return ProjectStartReturn.NO_ONE_TASK_TO_RUN;
		
		final long actionsDelayLongMaxFinal = actionsDelayLongMax;
		
		final int repeatingTasksListSizeFinal = repeatingTasksList.size();
		
		if(runType == RunType.PREVIEW || runType == RunType.DEBUG)
		{
			BukkitTask endTask = Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					if(playerDebugger.isOnline())
					{
						Message.sendMessage(playerDebugger, "&8[&7&l"+runType.name()+"&8] &5&lAll no repeating task ended. &3Duration: &b"+actionsDelayLongMaxFinal+"T ("+(actionsDelayLongMaxFinal/20.0)+"s)");
						if(repeatingTasksListSizeFinal>0)
							Message.sendMessage(playerDebugger, "&8[&7&l"+runType.name()+"&8] &5&lStill running repeating tasks: "+repeatingTasksListSizeFinal);
					}
				}
			}, actionsDelayLongMax+1);
			delayedTasksList.add(endTask);
		}
		
		this.repeatingTasksListMap.put(actionsGroup, repeatingTasksList);
		this.delayedTasksListMap.put(actionsGroup, delayedTasksList);
		
		if(runType == RunType.NORMAL)
			Plugin.getPlugin().getLogger().info(Message.getColorMessage("&8[&7&lCBAM&8] &dStarted project '"+this.projectName+"' ("+this.worldName+") on playing map '"+this.worldToPlay+"'. Duration actions: "+actionsDelayLongMax+"T ("+(actionsDelayLongMax/20.0)+"s)"));
		
		return ProjectStartReturn.SUCCESSFULLY_STARTED;
	}
	
	private long scheduleTasks(RunType runType, String actionsGroup, int startTick, Integer durationTicks, Player playerDebugger, List<BukkitTask> repeatingTasksList, List<BukkitTask> delayedTasksList, long actionsDelayLongMax) {
		List<String> actionsSubGroupsList = Database.getActionsSubGroups(this.worldName, this.projectName, actionsGroup);
		
		int actionsGroupParameterRandomnessAmount = Database.getActionsGroupParameterRandomnessAmount(this.worldName, this.projectName, actionsGroup);
		
		if(actionsGroupParameterRandomnessAmount > 0 && actionsGroupParameterRandomnessAmount < actionsSubGroupsList.size())
		{
			Random rand = new Random();
			List<String> actionsSubGroupsListNew = new ArrayList<>();
			for(int i=0;i<actionsGroupParameterRandomnessAmount;i++) {
				int randomIndex = rand.nextInt(actionsSubGroupsList.size());
				actionsSubGroupsListNew.add(actionsSubGroupsList.get(randomIndex));
			}
			actionsSubGroupsList = actionsSubGroupsListNew;
		}
//		List<Long> actionsDelaysList = Database.getActionsDelays(this.worldName, this.projectName, actionsGroup);
//		
//		if(actionsDelaysList == null)
//			return ProjectStartReturn.ACTIONS_GROUP_NOT_FOUND;
//		
		
		if(startTick < 0)
			startTick = 0;
		
		for(String actionsSubGroup : actionsSubGroupsList)
		{
			List<Long> actionsDelaysList = Database.getActionsDelays(this.worldName, this.projectName, actionsGroup, actionsSubGroup);
			
			for(long actionsDelayLong : actionsDelaysList)
			{
				if(actionsDelayLong < 0 || actionsDelayLong < startTick)
					continue;
				if(durationTicks != null && actionsDelayLong > startTick+durationTicks)
					break;
				
				List<String> actionsList = Database.getActions(this.worldName, this.projectName, actionsGroup, actionsSubGroup, actionsDelayLong);
				
				if(actionsList == null)
					continue;
				
				for(String actionName : actionsList)
				{
					String commandExecuteCfg = Database.getActionCommandExecute(this.worldName, this.projectName, actionsGroup, actionsSubGroup, actionsDelayLong, actionName);
//					String actionsGroupExecuteCfg = Database.getActionsGroupExecute(this.worldName, this.projectName, actionsGroup, actionsSubGroup, actionsDelayLong, actionName);
//					//TODO;...
					if(commandExecuteCfg != null)
					{
						final String actionPositioned = Database.getActionPositioned(this.worldName, this.projectName, actionsGroup, actionsSubGroup, actionsDelayLong, actionName);
						final String commandExecuteFinal = (commandExecuteCfg == null) ? null : ProjectManager.prepareCommand(commandExecuteCfg, this.worldToPlay, actionPositioned);
						
						if(actionPositioned != null && commandExecuteFinal != null)
						{
							final long repeatTimeLong = Database.getActionRepeatTime(this.worldName, this.projectName, actionsGroup, actionsSubGroup, actionsDelayLong, actionName);
							
							BukkitTask task;
							if(repeatTimeLong > 0)
							{
								task = Bukkit.getScheduler().runTaskTimer(Plugin.getPlugin(), new Runnable() {
									private int count = 0;
									@Override
									public void run() {
										if(runType == RunType.PREVIEW || runType == RunType.DEBUG)
										{
											if(playerDebugger.isOnline())
											{
												if(count == 0)
												{
													String rawMsg = "[\"\",{\"text\":\"[\",\"color\":\"dark_gray\"},{\"text\":\""+runType.name()+"\",\"bold\":true,\"color\":\"gray\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/cb "+runType.name().toLowerCase()+" break\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"Click to break this "+runType.name().toLowerCase()+"\",\"italic\":true,\"color\":\"gray\"}}},{\"text\":\"]\",\"color\":\"dark_gray\"},"
															+ "{\"text\":\" "+actionsGroup+" \",\"color\":\"dark_purple\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/cb "+runType.name().toLowerCase()+" start "+worldName+" "+projectName+" "+actionsGroup+"\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Click to run "+runType.name().toLowerCase()+" from this actions group\",\"italic\":true,\"color\":\"gray\"}]}},"
															+ "{\"text\":\" D"+actionsDelayLong+" R"+(actionsDelayLong+(repeatTimeLong*count))+" \",\"color\":\"light_purple\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/cb "+runType.name().toLowerCase()+" start "+worldName+" "+projectName+" "+actionsGroup+" "+actionsDelayLong+" \"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Click to run "+runType.name().toLowerCase()+" from this delay (you can add duration)\",\"italic\":true,\"color\":\"gray\"}]}},"
															+ "{\"text\":\""+actionName+"\",\"bold\":true,\"color\":\"dark_aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/cb menu "+worldName+" "+projectName+" "+actionsGroup+" "+actionsSubGroup+" "+actionsDelayLong+" "+actionName+"\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"Click to manage this action\",\"italic\":true,\"color\":\"gray\"}}},{\"text\":\": \",\"color\":\"gray\"},"
															+(commandExecuteFinal == null ? ("{\"text\":\"NULL\",\"color\":\"dark_purple\",\"bold\":\"true\"}") : ("{\"text\":\""+commandExecuteFinal.replaceAll("\"", "\\\\\"")+"\",\"color\":\"red\",\"clickEvent\":{\"action\":\"copy_to_clipboard\",\"value\":\""+commandExecuteFinal.replaceAll("\"", "\\\\\"")+"\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"Click to copy this command to clipboard\",\"italic\":true,\"color\":\"gray\"}}"))
															+"}]";
													Message.sendRawMessage(playerDebugger, rawMsg);
												}
												
												if(runType == RunType.DEBUG) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandExecuteFinal);
											}
										}
										else
										{
											Plugin.getPlugin().getLogger().info(Message.getColorMessage("&8[&7&lCBAM&8] &dD"+(actionsDelayLong+(repeatTimeLong*count))+" R"+repeatTimeLong+" '&3"+actionName+"&d' &b-> &aTrying to execute command: &b"+commandExecuteFinal));
											Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandExecuteFinal);
										}
										count++;
									}
								}, actionsDelayLong-startTick, repeatTimeLong);
								
								//List<BukkitTask> repeatingTasksList = this.repeatingTasksListMap.get(actionsGroup);
								repeatingTasksList.add(task);
							}
							else
							{
								task = Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
									@Override
									public void run() {
										if(runType == RunType.PREVIEW || runType == RunType.DEBUG)
										{
											if(playerDebugger.isOnline())
											{
												String rawMsg = "[\"\",{\"text\":\"[\",\"color\":\"dark_gray\"},{\"text\":\""+runType.name()+"\",\"bold\":true,\"color\":\"gray\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/cb "+runType.name().toLowerCase()+" break\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"Click to break this "+runType.name().toLowerCase()+"\",\"italic\":true,\"color\":\"gray\"}}},{\"text\":\"]\",\"color\":\"dark_gray\"},"
														+ "{\"text\":\" "+actionsGroup+" \",\"color\":\"dark_purple\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/cb "+runType.name().toLowerCase()+" start "+worldName+" "+projectName+" "+actionsGroup+"\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Click to run "+runType.name().toLowerCase()+" from this actions group\",\"italic\":true,\"color\":\"gray\"}]}},"
														+ "{\"text\":\" D"+actionsDelayLong+" \",\"color\":\"light_purple\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/cb "+runType.name().toLowerCase()+" start "+worldName+" "+projectName+" "+actionsGroup+" "+actionsDelayLong+" \"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Click to run "+runType.name().toLowerCase()+" from this delay (you can add duration)\",\"italic\":true,\"color\":\"gray\"}]}},"
														+ "{\"text\":\""+actionName+"\",\"bold\":true,\"color\":\"dark_aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/cb menu "+worldName+" "+projectName+" "+actionsGroup+" "+actionsSubGroup+" "+actionsDelayLong+" "+actionName+"\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"Click to manage this action\",\"italic\":true,\"color\":\"gray\"}}},{\"text\":\": \",\"color\":\"gray\"},"
														+(commandExecuteFinal == null ? ("{\"text\":\"NULL\",\"color\":\"dark_purple\",\"bold\":\"true\"}") : ("{\"text\":\""+commandExecuteFinal.replaceAll("\"", "\\\\\"")+"\",\"color\":\"red\",\"clickEvent\":{\"action\":\"copy_to_clipboard\",\"value\":\""+commandExecuteFinal.replaceAll("\"", "\\\\\"")+"\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"Click to copy this command to clipboard\",\"italic\":true,\"color\":\"gray\"}}"))
														+"}]";
												Message.sendRawMessage(playerDebugger, rawMsg);
												
												if(runType == RunType.DEBUG) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandExecuteFinal);
											}
										}
										else
										{
											Plugin.getPlugin().getLogger().info(Message.getColorMessage("&8[&7&lCBAM&8] &dD"+actionsDelayLong+" '&6"+actionName+"&d' &b-> &aTrying to execute command: &b"+commandExecuteFinal));
											Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandExecuteFinal);
										}
										//delayedTasksListMap.get(actionsGroup).remove(0);//TODO: test it!!!
									}
								}, actionsDelayLong-startTick);
								
								//List<BukkitTask> delayedTasksList = this.delayedTasksListMap.get(actionsGroup);
								delayedTasksList.add(task);
							}
							
							if(actionsDelayLong > actionsDelayLongMax)
								actionsDelayLongMax = actionsDelayLong;
						}
						else
						{
							//TODO add preview and debug info -> actionPositioned == null || commandExecute = null
						}
					}
					else
					{
						//TODO add preview and debug info -> commandExecute = null
					}
				}
			}
		}
		return actionsDelayLongMax;
	}

	private void cancelAllTasks() {
		for(List<BukkitTask> repeatingTasksList : this.repeatingTasksListMap.values())
		{
			for(BukkitTask task : repeatingTasksList)
				task.cancel();
			
			repeatingTasksList.clear();
		}
		
		this.repeatingTasksListMap.clear();
		
		for(List<BukkitTask> delayedTasksList : this.delayedTasksListMap.values())
		{
			for(BukkitTask task : delayedTasksList)
				task.cancel();
			
			delayedTasksList.clear();
		}
		
		this.delayedTasksListMap.clear();
	}
	private void cancelAllTasks(String actionsGroup) {
		List<BukkitTask> repeatingTasksList = this.repeatingTasksListMap.remove(actionsGroup);
		if(repeatingTasksList != null) {
			for(BukkitTask task : repeatingTasksList)
				task.cancel();
			
			repeatingTasksList.clear();
		}
		
		List<BukkitTask> delayedTasksList = this.delayedTasksListMap.remove(actionsGroup);
		if(delayedTasksList != null) {
			for(BukkitTask task : delayedTasksList)
				task.cancel();
			
			delayedTasksList.clear();
		}
	}
	
	// BREAK
	protected void doBreak(String actionsGroup) {
		if(actionsGroup.equalsIgnoreCase("all"))
			this.cancelAllTasks();
		else
			this.cancelAllTasks(actionsGroup);
		
		if(this.delayedTasksListMap.size() == 0 && this.repeatingTasksListMap.size() == 0)
			ProjectManager.projectsRunningRemove(this.worldName, this.projectName);
	}
	public void doPreviewAndDebugBreak(Player playerDebugger, String actionsGroup) {
		if(actionsGroup.equalsIgnoreCase("all"))
			this.cancelAllTasks();
		else
			this.cancelAllTasks(actionsGroup);
		
		if(this.delayedTasksListMap.size() == 0 && this.repeatingTasksListMap.size() == 0)
			ProjectManager.playersDebuggingRemove(playerDebugger);
	}
	
//	protected void doPreviewBreak(Player playerDebugger) {
//		for(BukkitTask task : this.allTasksList) {
//			task.cancel();
//		}
//		this.allTasksList.clear();
//	}
//	protected void doDebugBreak(Player playerDebugger) {
//		for(BukkitTask task : this.allTasksList) {
//			task.cancel();
//		}
//		this.allTasksList.clear();
//	}
	
	// END
//	protected ProjectEndReturn doEnd() {
//		for(BukkitTask task : this.allTasksList) {
//			task.cancel();
//		}
//		this.allTasksList.clear();
//		
//	}
//	private ProjectStartReturn runTasks(RunType runType, int startTick, Integer durationTicks, Player playerDebugger, ActionType actionType) {
//		long actionsStartDelayLongMax = this.prepareTasks(runType, startTick, durationTicks, playerDebugger, actionType);
//		
//		if(actionsStartDelayLongMax == -2)
//			return ProjectStartReturn.ACTIONS_START_NOT_FOUND;
//		
//		if(actionsStartDelayLongMax == -1)
//			return ProjectStartReturn.NO_ONE_TASK_TO_RUN;
//		
//		BukkitTask endTask = Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
//			@Override
//			public void run() {
//				for(BukkitTask repeatingTask : repeatingTasksList) {
//					repeatingTask.cancel();
//				}
//				
//				repeatingTasksList.clear();
//				allTasksList.clear();
//				
//				long actionsEndDelayLongMax = -1;
//				if(durationTicks == null)
//					actionsEndDelayLongMax = prepareTasks(runType, startTick, null, playerDebugger, ActionType.ActionsEnd);
//				else if((startTick+durationTicks) >= actionsStartDelayLongMax)
//					actionsEndDelayLongMax = prepareTasks(runType, startTick, (int)((startTick+durationTicks)-actionsStartDelayLongMax), playerDebugger, ActionType.ActionsEnd);
//				
//				if(actionsEndDelayLongMax >= 0)
//				{
//					long actionsEndDelayLongMaxFinal = actionsEndDelayLongMax;
//					
//					BukkitTask finishTask = Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
//						@Override
//						public void run() {
//							for(BukkitTask repeatingTask : repeatingTasksList) {
//								repeatingTask.cancel();
//							}
//							
//							if(runType == RunType.PREVIEW || runType == RunType.DEBUG)
//							{
//								if(playerDebugger.isOnline())
//									Message.sendMessage(playerDebugger, "&8[&7&l"+runType.name()+"&8] &5&lProject ended. &3Project '&b"+projectName+"&3' duration: &b"+(actionsStartDelayLongMax+actionsEndDelayLongMaxFinal)+"T ("+((actionsStartDelayLongMax+actionsEndDelayLongMaxFinal)/20.0)+"s)");
//								
//								ProjectManager.getPlayersDebuggingMap().remove(playerDebugger);
//							}
//							else
//							{
//								Plugin.getPlugin().getLogger().info(Message.getColorMessage("&8[&7&lCBAM&8] &dProject ended. Ended project '"+projectName+"' ("+worldName+") on playing map '"+worldToPlay+"' in "+(actionsStartDelayLongMax+actionsEndDelayLongMaxFinal)+"T ("+((actionsStartDelayLongMax+actionsEndDelayLongMaxFinal)/20.0)+"s)"));
//								ProjectManager.getProjectsRunningMap().remove(worldName+"."+projectName);
//							}
//						}
//					}, actionsEndDelayLongMax+1);
//					
//					allTasksList.add(finishTask);
//				}
//				else
//				{
//					if(runType == RunType.PREVIEW || runType == RunType.DEBUG)
//					{
//						if(playerDebugger.isOnline())
//							Message.sendMessage(playerDebugger, "&8[&7&l"+runType.name()+"&8] &5&lProject ended. (No one action end/stop executed) &3Project '&b"+projectName+"&3' duration: &b"+actionsStartDelayLongMax+"T ("+(actionsStartDelayLongMax/20.0)+"s)");
//						
//						ProjectManager.getPlayersDebuggingMap().remove(playerDebugger);
//					}
//					else
//					{
//						Plugin.getPlugin().getLogger().info(Message.getColorMessage("&8[&7&lCBAM&8] &dProject ended. (No one action end/stop executed) Ended project '"+projectName+"' ("+worldName+") on playing map '"+worldToPlay+"' in "+actionsStartDelayLongMax+"T ("+(actionsStartDelayLongMax/20.0)+"s)"));
//						ProjectManager.getProjectsRunningMap().remove(worldName+"."+projectName);
//					}
//				}
//			}
//		}, actionsStartDelayLongMax+1);
//		
//		this.allTasksList.add(endTask);
//		
//		if(runType == RunType.NORMAL)
//			Plugin.getPlugin().getLogger().info(Message.getColorMessage("&8[&7&lCBAM&8] &dStarted project '"+this.projectName+"' ("+this.worldName+") on playing map '"+this.worldToPlay+"'. Duration actions start: "+actionsStartDelayLongMax+"T ("+(actionsStartDelayLongMax/20.0)+"s)"));
//		
//		return ProjectStartReturn.SUCCESSFULLY_STARTED;
//	}
	
}

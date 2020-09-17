package mkproject.maskat.CmdBlockAdvancedManager.ProjectManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;

import mkproject.maskat.CmdBlockAdvancedManager.Database;
import mkproject.maskat.CmdBlockAdvancedManager.Plugin;
import mkproject.maskat.Papi.Papi;

public class ProjectManager {
	
	public enum ProjectStartReturn {
		PROJECT_NOT_FOUND,
		SUCCESSFULLY_STARTED,
		ACTIONS_START_NOT_FOUND,
		CFG_ERROR_ACTION_DELAY_INVALID,
		PROJECT_ALREADY_RUNNING,
		NO_ONE_TASK_TO_RUN,
		ACTIONS_GROUP_NOT_FOUND, SUCCESSFULLY_SOMEBODY_STARTED, NOBODY_ACTIONS_STARTED, OTHER_PROJECT_DEBUGGING,
	}
	public enum ProjectEndReturn {
		PROJECT_NOT_RUNNING,
	}

	private static Map<String, ProjectModel> projectsRunningMap = new HashMap<>();
	private static Map<Player, ProjectModel> playersDebuggingMap = new HashMap<>();
	
	protected static void projectsRunningRemove(String worldName, String projectName) {
		projectsRunningMap.remove(worldName+"."+projectName);
	}
	protected static void playersDebuggingRemove(Player playerDebugger) {
		playersDebuggingMap.remove(playerDebugger);
	}
	
	// START
	public static ProjectStartReturn startProject(String worldName, String projectName, String actionsGroup, String worldToPlay) {
		if(!Database.isProjectExist(worldName, projectName))
			return ProjectStartReturn.PROJECT_NOT_FOUND;
		if(!actionsGroup.equalsIgnoreCase("all") && !Database.isActionsGroupExist(worldName, projectName, actionsGroup))
			return ProjectStartReturn.ACTIONS_GROUP_NOT_FOUND;
		
		ProjectModel projectModel = projectsRunningMap.get(worldToPlay+"."+projectName);
		
		if(projectModel != null)
			projectModel.doBreak(actionsGroup);
		
		projectModel = new ProjectModel(worldName, projectName, worldToPlay);
		
		ProjectStartReturn result = projectModel.doStart(actionsGroup);
		
		if(result == ProjectStartReturn.SUCCESSFULLY_STARTED || result == ProjectStartReturn.SUCCESSFULLY_SOMEBODY_STARTED)
			projectsRunningMap.put(worldToPlay+"."+projectName, projectModel);
		
		return result;
	}
	public static ProjectStartReturn startPreviewProject(String worldName, String projectName, String actionsGroup, int startTick, String durationTicks, Player playerDebugger, String worldToPlay) {
		if(!Database.isProjectExist(worldName, projectName))
			return ProjectStartReturn.PROJECT_NOT_FOUND;
		if(!actionsGroup.equalsIgnoreCase("all") && !Database.isActionsGroupExist(worldName, projectName, actionsGroup))
			return ProjectStartReturn.ACTIONS_GROUP_NOT_FOUND;
		
		ProjectModel projectModel = playersDebuggingMap.get(playerDebugger);
		
		if(projectModel == null)
			projectModel = new ProjectModel(worldName, projectName, worldToPlay);
		else if(!projectModel.isEquals(worldName, projectName))
			return ProjectStartReturn.OTHER_PROJECT_DEBUGGING;
		
		ProjectStartReturn result = projectModel.doPreviewStart(actionsGroup, startTick, durationTicks, playerDebugger);
		
		if(result == ProjectStartReturn.SUCCESSFULLY_STARTED || result == ProjectStartReturn.SUCCESSFULLY_SOMEBODY_STARTED)
			playersDebuggingMap.put(playerDebugger, projectModel);
		
		return result;
	}
	public static ProjectStartReturn startDebugProject(String worldName, String projectName, String actionsGroup, int startTick, String durationTicks, Player playerDebugger, String worldToPlay) {
		if(!Database.isProjectExist(worldName, projectName))
			return ProjectStartReturn.PROJECT_NOT_FOUND;
		if(!actionsGroup.equalsIgnoreCase("all") && !Database.isActionsGroupExist(worldName, projectName, actionsGroup))
			return ProjectStartReturn.ACTIONS_GROUP_NOT_FOUND;
		
		ProjectModel projectModel = playersDebuggingMap.get(playerDebugger);
		
		if(projectModel == null)
			projectModel = new ProjectModel(worldName, projectName, worldToPlay);
		else if(!projectModel.isEquals(worldName, projectName))
			return ProjectStartReturn.OTHER_PROJECT_DEBUGGING;
		
		ProjectStartReturn result = projectModel.doDebugStart(actionsGroup, startTick, durationTicks, playerDebugger);
		
		if(result == ProjectStartReturn.SUCCESSFULLY_STARTED || result == ProjectStartReturn.SUCCESSFULLY_SOMEBODY_STARTED)
			playersDebuggingMap.put(playerDebugger, projectModel);
		
		return result;
	}
//	public static ProjectStartReturn debugStartProject(Player playerDebugger, String worldName, String projectName, int startTick, String worldToPlay, Integer durationTicks) {
//		if(playersDebuggingMap.containsKey(playerDebugger))
//			return ProjectStartReturn.PROJECT_ALREADY_RUNNING;
//		
//		if(Plugin.getPlugin().getConfig().get("database."+worldName+"."+projectName) == null)
//			return ProjectStartReturn.PROJECT_NOT_FOUND;
//		
//		ProjectModel projectModel = new ProjectModel(worldName, projectName, worldToPlay);
//		
//		ProjectStartReturn result = projectModel.doDebugStart(playerDebugger, startTick, durationTicks);
//		
//		if(result == ProjectStartReturn.SUCCESSFULLY_STARTED)
//			playersDebuggingMap.put(playerDebugger, projectModel);
//		
//		return result;
//	}
	
	// BREAK
	public static boolean breakProject(String worldPlayed, String projectName, String actionsGroup) {
		ProjectModel projectModel = projectsRunningMap.get(worldPlayed+"."+projectName);
		if(projectModel == null)
			return false;
		
		projectModel.doBreak(actionsGroup);
		return true;
	}
	public static boolean previewBreakProject(Player playerDebugger, String actionsGroup) {
		ProjectModel projectModel = playersDebuggingMap.get(playerDebugger);
		if(projectModel == null)
			return false;
		
		projectModel.doPreviewAndDebugBreak(playerDebugger, actionsGroup);
		return true;
	}
//	public static boolean previewBreakProject(Player playerDebugger) {
//		ProjectModel projectModel = playersDebuggingMap.get(playerDebugger);
//		if(projectModel == null)
//			return false;
//		
//		projectModel.doPreviewBreak(playerDebugger);
//		playersDebuggingMap.remove(playerDebugger);
//		return true;
//	}
//	public static boolean debugBreakProject(Player playerDebugger) {
//		ProjectModel projectModel = playersDebuggingMap.get(playerDebugger);
//		if(projectModel == null)
//			return false;
//		
//		projectModel.doDebugBreak(playerDebugger);
//		playersDebuggingMap.remove(playerDebugger);
//		return true;
//	}
	
	// UTILS
	public static List<String> getAvailableWorldsList() {
		List<String> worldsList = Database.getWorlds();
		return worldsList == null ? new ArrayList<>() : worldsList;
	}
	public static List<String> getAvailableProjectsList(String worldName) {
		List<String> projectsList = Database.getProjects(worldName);
		return projectsList == null ? new ArrayList<>() : projectsList;
	}
	public static List<String> getAvailableActionsGroupsList(String worldName, String projectName) {
		List<String> actionsGroupsList = Database.getActionsGroups(worldName, projectName);
		return actionsGroupsList == null ? new ArrayList<>() : actionsGroupsList;
	}
	
	public static List<String> getPlayedWorldsList() {
		List<String> playedWorldsList = new ArrayList<>();
		
		for(String projectRunning : projectsRunningMap.keySet()) {
			playedWorldsList.add(projectRunning.split("\\.")[0]);
		}
		
		return playedWorldsList;
	}
	public static List<String> getPlayedProjectsList(String worldPlayed) {
		List<String> playedProjectsList = new ArrayList<>();
		
		for(String projectRunning : projectsRunningMap.keySet()) {
			String[] projectRunningArr = projectRunning.split("\\.");
			if(projectRunningArr[0].equals(worldPlayed))
				playedProjectsList.add(projectRunningArr[1]);
		}
		
		return playedProjectsList;
	}
	public static List<String> getPlayedActionsGroupsList(String worldPlayed, String projectPlayed) {
		ProjectModel projectsRunning = projectsRunningMap.get(worldPlayed+"."+projectPlayed);
		return projectsRunning == null ? new ArrayList<>() : projectsRunning.getPlayedActionsGroupsList();
	}
	public static List<String> getDebuggingActionsGroupsList(Player playerDebugger) {
		ProjectModel projectsRunning = playersDebuggingMap.get(playerDebugger);
		return projectsRunning == null ? new ArrayList<>() : projectsRunning.getPlayedActionsGroupsList();
	}

	public static String prepareCommand(String commandExecute, String worldToPlay, String actionPositioned) {
		if(commandExecute.indexOf("/") != 0)
			commandExecute = "/"+commandExecute;
		
		if(commandExecute.indexOf("/execute ") == 0)
		{
			int indexRun = commandExecute.indexOf(" run ");
			if(indexRun > 0)
			{
				String executeWithParameters = commandExecute.substring(0, indexRun+1);
				
				String addMinecraftWorldToPlay = null;
				if(worldToPlay != null && !executeWithParameters.contains(" in "))
				{
					if(worldToPlay.equals("world"))
						addMinecraftWorldToPlay = "overworld";
					else if(worldToPlay.equals("world_nether"))
						addMinecraftWorldToPlay = "the_nether";
					else if(worldToPlay.equals("world_the_end"))
						addMinecraftWorldToPlay = "the_end";
					else
						addMinecraftWorldToPlay = worldToPlay;
					
					addMinecraftWorldToPlay = "in minecraft:"+addMinecraftWorldToPlay+" ";
				}
				
				String addPositioned = null;
				if(actionPositioned != null)
				{
					if(!executeWithParameters.contains(" positioned "))
					{
						String[] actionPositionedArr = actionPositioned.split(" ");
					
						if(actionPositionedArr.length == 3 && Papi.Function.isNumeric(actionPositionedArr[0]) && Papi.Function.isNumeric(actionPositionedArr[1]) && Papi.Function.isNumeric(actionPositionedArr[2]))
						{
							double actionPositionedX = Double.parseDouble(actionPositionedArr[0]);
							double actionPositionedY = Double.parseDouble(actionPositionedArr[1]);
							double actionPositionedZ = Double.parseDouble(actionPositionedArr[2]);
							
							addPositioned = "positioned "+actionPositionedX+" "+actionPositionedY+" "+actionPositionedZ+" ";
						}
					}
					else
					{
						String[] actionPositionedArr = actionPositioned.split(" ");
						
						if(actionPositionedArr.length == 3 && Papi.Function.isNumeric(actionPositionedArr[0]) && Papi.Function.isNumeric(actionPositionedArr[1]) && Papi.Function.isNumeric(actionPositionedArr[2]))
						{
							double actionPositionedX = Double.parseDouble(actionPositionedArr[0]);
							double actionPositionedY = Double.parseDouble(actionPositionedArr[1]);
							double actionPositionedZ = Double.parseDouble(actionPositionedArr[2]);
							
							//^\/execute\s(.*?positioned\s(\~?\-?\d*\.?\d*)\s(\~?\-?\d*\.?\d*)\s(\~?\-?\d*\.?\d*)\s)*?run
							//^\/execute\s.*?positioned\s((?:\~)|(?:\~?\-?\d+\.?\d*))\s((?:\~)|(?:\~?\-?\d+\.?\d*))\s((?:\~)|(?:\~?\-?\d+\.?\d*))\s
							String regex = "^\\/execute\\s.*?positioned\\s((?:\\~)|(?:\\~?\\-?\\d+\\.?\\d*))\\s((?:\\~)|(?:\\~?\\-?\\d+\\.?\\d*))\\s((?:\\~)|(?:\\~?\\-?\\d+\\.?\\d*))\\s";
							Pattern pattern = Pattern.compile(regex);
							Matcher matcher = pattern.matcher(executeWithParameters);
							StringBuilder strBuilder = new StringBuilder(commandExecute);
							
							if(matcher.find()) {
								String cmdPosXStr = matcher.group(1);
								int gr1Len = cmdPosXStr.length();
								if(cmdPosXStr.indexOf("~") == 0)
								{
									if(cmdPosXStr.length() > 1)
										cmdPosXStr = String.valueOf(actionPositionedX+Double.parseDouble(cmdPosXStr.substring(1)));
									else
										cmdPosXStr = String.valueOf(actionPositionedX);
								}
								
								String cmdPosYStr = matcher.group(2);
								int gr2Len = cmdPosYStr.length();
								if(cmdPosYStr.indexOf("~") == 0)
								{
									if(cmdPosYStr.length() > 1)
										cmdPosYStr = String.valueOf(actionPositionedY+Double.parseDouble(cmdPosYStr.substring(1)));
									else
										cmdPosYStr = String.valueOf(actionPositionedY);
								}
								
								String cmdPosZStr = matcher.group(3);
								int gr3Len = cmdPosYStr.length();
								if(cmdPosZStr.indexOf("~") == 0)
								{
									if(cmdPosZStr.length() > 1)
										cmdPosZStr = String.valueOf(actionPositionedZ+Double.parseDouble(cmdPosZStr.substring(1)));
									else
										cmdPosZStr = String.valueOf(actionPositionedZ);
								}
								
								if(cmdPosXStr.contains("."))
									cmdPosXStr = cmdPosXStr.replaceAll("0*$", "").replaceAll("\\.$", "");
								if(cmdPosYStr.contains("."))
									cmdPosYStr = cmdPosYStr.replaceAll("0*$", "").replaceAll("\\.$", "");
								if(cmdPosZStr.contains("."))
									cmdPosZStr = cmdPosZStr.replaceAll("0*$", "").replaceAll("\\.$", "");

								int gr1lenDiff = cmdPosXStr.length()-gr1Len;
								strBuilder.replace(matcher.start(1), matcher.end(1), cmdPosXStr);

								int gr2lenDiff = cmdPosYStr.length()-gr2Len;
								strBuilder.replace(matcher.start(2)+gr1lenDiff, matcher.end(2)+gr1lenDiff, cmdPosYStr);

								int gr3lenDiff = cmdPosZStr.length()-gr3Len;
								strBuilder.replace(matcher.start(3)+gr1lenDiff+gr2lenDiff, matcher.end(3)+gr1lenDiff+gr2lenDiff, cmdPosZStr);
								
//								matcher.appendReplacement(sb, matcher.group()
//										.replaceFirst(Pattern.quote(matcher.group(1)),cmdPosXStr)
//										.replaceFirst(Pattern.quote(matcher.group(2)),cmdPosYStr)
//										.replaceFirst(Pattern.quote(matcher.group(3)),cmdPosZStr)
//										);
								
								//INFO: matcher.group(0) == matcher.group()
								//matcher.appendReplacement(sb, " " + cmdPosXStr + " " + cmdPosYStr + " " + cmdPosZStr);
								
								//OTHER TIP
				//				matcher.appendReplacement(sb, matcher.group().replaceFirst(Pattern.quote(matcher.group(2)), String.valueOf(actionPositionedY+cmdPosY)));
				//				matcher.appendReplacement(sb, matcher.group().replaceFirst(Pattern.quote(matcher.group(3)), String.valueOf(actionPositionedZ+cmdPosZ)));
							}
							
							commandExecute = strBuilder.toString();
						}
					}
				}
				
				if(addMinecraftWorldToPlay != null) {
					if(addPositioned != null)
						commandExecute = commandExecute.replaceFirst(Pattern.quote("/execute "), "/execute "+addMinecraftWorldToPlay+addPositioned);
					else
						commandExecute = commandExecute.replaceFirst(Pattern.quote("/execute "), "/execute "+addMinecraftWorldToPlay);
				}
				else if(addPositioned != null)
					commandExecute = commandExecute.replaceFirst(Pattern.quote("/execute "), "/execute "+addPositioned);
			}
		}
		return commandExecute.substring(1);
	}
//	
//	@Deprecated
//	public static String prepareCommandOld(String commandExecute, String worldToPlay, String actionPositioned) {
//		if(commandExecute.indexOf("/") != 0)
//			commandExecute = "/"+commandExecute;
//		
//		if(commandExecute.indexOf("/execute in projectworld") == 0)
//		{
//			String minecraftWorldToPlay = null;
//			if(worldToPlay.equals("world"))
//				minecraftWorldToPlay = "overworld";
//			else if(worldToPlay.equals("world_nether"))
//				minecraftWorldToPlay = "the_nether";
//			else if(worldToPlay.equals("world_the_end"))
//				minecraftWorldToPlay = "the_end";
//			else
//				minecraftWorldToPlay = worldToPlay;
//			
//			commandExecute = commandExecute.replaceFirst("/execute in projectworld", "/execute in minecraft:"+minecraftWorldToPlay);
//		}
//		
//		if(actionPositioned != null)
//		{
//			String[] actionPositionedArr = actionPositioned.split(" ");
//			
//			if(actionPositionedArr.length == 3 && Papi.Function.isNumeric(actionPositionedArr[0]) && Papi.Function.isNumeric(actionPositionedArr[1]) && Papi.Function.isNumeric(actionPositionedArr[2]))
//			{
//				double actionPositionedX = Double.parseDouble(actionPositionedArr[0]);
//				double actionPositionedY = Double.parseDouble(actionPositionedArr[1]);
//				double actionPositionedZ = Double.parseDouble(actionPositionedArr[2]);
//				
//				String regex = "\\s(\\~?\\-?\\d*\\.?\\d*)\\s(\\~?\\-?\\d*\\.?\\d*)\\s(\\~?\\-?\\d*\\.?\\d*)";
//				Pattern pattern = Pattern.compile(regex);
//				Matcher matcher = pattern.matcher(commandExecute);
//				StringBuffer sb = new StringBuffer();
////				while(matcher.find()) { // for replace all
//				if(matcher.find()) { // for replace first
//					String cmdPosXStr = matcher.group(1);
//					if(cmdPosXStr.indexOf("~") == 0)
//					{
//						if(cmdPosXStr.length() > 1)
//							cmdPosXStr = String.valueOf(actionPositionedX+Double.parseDouble(cmdPosXStr.substring(1)));
//						else
//							cmdPosXStr = String.valueOf(actionPositionedX);
//					}
//					
//					String cmdPosYStr = matcher.group(2);
//					if(cmdPosYStr.indexOf("~") == 0)
//					{
//						if(cmdPosYStr.length() > 1)
//							cmdPosYStr = String.valueOf(actionPositionedY+Double.parseDouble(cmdPosYStr.substring(1)));
//						else
//							cmdPosYStr = String.valueOf(actionPositionedY);
//					}
//					
//					String cmdPosZStr = matcher.group(3);
//					if(cmdPosZStr.indexOf("~") == 0)
//					{
//						if(cmdPosZStr.length() > 1)
//							cmdPosZStr = String.valueOf(actionPositionedZ+Double.parseDouble(cmdPosZStr.substring(1)));
//						else
//							cmdPosZStr = String.valueOf(actionPositionedZ);
//					}
//					
//					if(cmdPosXStr.contains("."))
//						cmdPosXStr = cmdPosXStr.replaceAll("0*$", "").replaceAll("\\.$", "");
//					if(cmdPosYStr.contains("."))
//						cmdPosYStr = cmdPosYStr.replaceAll("0*$", "").replaceAll("\\.$", "");
//					if(cmdPosZStr.contains("."))
//						cmdPosZStr = cmdPosZStr.replaceAll("0*$", "").replaceAll("\\.$", "");
//					
//					//INFO: matcher.group(0) == matcher.group()
//					matcher.appendReplacement(sb, " " + cmdPosXStr + " " + cmdPosYStr + " " + cmdPosZStr);
//					
//					//OTHER TIP
//					//				matcher.appendReplacement(sb, matcher.group().replaceFirst(Pattern.quote(matcher.group(2)), String.valueOf(actionPositionedY+cmdPosY)));
//					//				matcher.appendReplacement(sb, matcher.group().replaceFirst(Pattern.quote(matcher.group(3)), String.valueOf(actionPositionedZ+cmdPosZ)));
//				}
//				matcher.appendTail(sb);
//				commandExecute = sb.toString();
//			}
//		}
//		return commandExecute.substring(1);
//	}




}

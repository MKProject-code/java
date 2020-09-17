package mkproject.maskat.CmdBlockAdvancedManager.Menu;

import mkproject.maskat.CmdBlockAdvancedManager.Plugin;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.Actions.Menu_TheProjectActions;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.Actions.Menu_TheProjectActionsDelays;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.Actions.Menu_TheProjectActionsGroups;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.Actions.Menu_TheProjectActionsSubGroups;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.Actions.Menu_TheProjectTheAction;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.Events.ManagerMenu_TheProjectEventsTypes;
import mkproject.maskat.CmdBlockAdvancedManager.Models.CbamPlayer;
import mkproject.maskat.Papi.Menu.PapiMenuV2;
import mkproject.maskat.Papi.Utils.Message;

public class MenuManager {
	
//	public static enum Page {
//		LIST,
//		ARENA,
//	}
	public static enum MenuType {
		ACTION_START, ACTION_END
	}
	public static enum SlotOption {
		RELOAD_CONFIGURATION, OPEN_PROJECTS_MENU,
		
		ADD_NEW_PROJECT, OPEN_PROJECT, BACK, EXIT, DELETE_PROJECT, MODIFY_EXECUTED_COMMAND, OPEN_ACTION_IN_DELAY, ADD_NEW_ACTION_DELAY, ADD_NEW_ACTION, OPEN_ACTION, DELETE_ACTION, CHANGE_DELAY_TIME, ENABLE_REPEATING, DISABLE_REPEATING, CHANGE_REPEATING_TIME, DEBUG_EXECUTE_COMMAND_WITHOUT_CLOSE, DEBUG_EXECUTE_COMMAND_WITH_CLOSE, RENAME_ACTION, SELECT_POSITION, TELEPORT_TO_ACTION_POSITION, DEBUG_PROJECT, PREVIOUS_PAGE, NEXT_PAGE, PREVIEW_PROJECT, PREVIEW_EXECUTE_COMMAND_WITHOUT_CLOSE, SHOW_TO_ACTION_POSITION_WITHOUT_CLOSE, SHOW_TO_ACTION_POSITION_WITH_CLOSE, ACTIONS, EVENTS, OPEN_ACTION_GROUP, ADD_NEW_ACTION_GROUP, PREVIEW_ACTIONS_GROUP, DEBUG_ACTIONS_GROUP, PREVIEW_ACTIONS_DELAYED, DEBUG_ACTIONS_DELAYED, PARAMETER_RANDOMNESS_AMOUNT, OPEN_ACTION_SUBGROUP, ADD_NEW_ACTION_SUBGROUP, RENAME_GROUP, RENAME_SUBGROUP, DELETE_GROUP, DELETE_SUBGROUP, ADVANCED_COMMANDS,
	}
	public static enum ListenerChat {
		NEW_PROJECT_NAME, NEW_ACTION_DELAY, DELETE_ACTION, ADD_NEW_ACTION, DELETE_PROJECT, CHANGE_ACTION_DELAY, CHANGE_ACTION_REPEATING_TIME, RENAME_ACTION, NEW_ACTIONS_GROUP_NAME, CHANGE_PARAMETER_RANDOMNESS_AMOUNT, NEW_ACTIONS_SUBGROUP_NAME, RENAME_GROUP, RENAME_SUBGROUP, DELETE_GROUP, DELETE_SUBGROUP
	}
	public static enum ListenerInteract {
		SELECT_EXECUTE_COMMAND_POSITION
	}
//	public static enum Action {
//		NONE,
//		SUCCESS,
//		ERROR,
//		EXIST
//	}
	public static void exitMenu(CbamPlayer cbamPlayer) {
		cbamPlayer.getPlayer().closeInventory();
	}
//	public static void openMenu(CbamPlayer cbamPlayer, PapiMenu backMenu) {
//		cbamPlayer.openPapiMenuPage(backMenu);
//	}
	public static void openLastMenu(CbamPlayer cbamPlayer) {
		cbamPlayer.openLastPapiMenuPage();
	}
//	public static boolean refreshMenu(PapiMenu papiMenu) {
//		if(papiMenu instanceof ManagerMenu_Plugin) {
//			return ((ManagerMenu_Plugin)papiMenu).refreshPapiMenuPage();
//		}
//		else if(papiMenu instanceof ManagerMenu_Projects) {
//			return ((ManagerMenu_Projects)papiMenu).refreshPapiMenuPage();
//		}
//		else if(papiMenu instanceof ManagerMenu_TheProject) {
//			return ((ManagerMenu_TheProject)papiMenu).refreshPapiMenuPage();
//		}
//		else if(papiMenu instanceof ManagerMenu_TheProjectActionsGroups) {
//			return ((ManagerMenu_TheProjectActionsGroups)papiMenu).refreshPapiMenuPage();
//		}
//		else if(papiMenu instanceof ManagerMenu_TheProjectActionsDelays) {
//			return ((ManagerMenu_TheProjectActionsDelays)papiMenu).refreshPapiMenuPage();
//		}
//		else if(papiMenu instanceof ManagerMenu_TheProjectActions) {
//			return ((ManagerMenu_TheProjectActions)papiMenu).refreshPapiMenuPage();
//		}
//		else if(papiMenu instanceof ManagerMenu_TheProjectTheAction) {
//			return ((ManagerMenu_TheProjectTheAction)papiMenu).refreshPapiMenuPage();
//		}
//		Plugin.getPlugin().getLogger().warning("********** ERROR ********** Can't refresh menu! [`ManagerMenuMain`]");
//		return false;
//	}
	
	private static boolean openMenu(CbamPlayer cbamPlayer, PapiMenuV2 papiMenu) {
		if(papiMenu.refreshPapiMenuPage())
		{
			cbamPlayer.openPapiMenuPage(papiMenu);
			return true;
		}
		else
		{
			Plugin.getPlugin().getLogger().warning("***** ERROR ***** MenuManager generated an exception! Can't load '"+papiMenu.toString()+"' for player '"+cbamPlayer.getPlayer().getName()+"'");
			Message.sendMessage(cbamPlayer.getPlayer(), "&4&lERROR! &c&lPlugin generated an exception! See console to more infomation");
			return false;
		}
	}
	public static void openPluginMenu(CbamPlayer cbamPlayer) {
		if(!openMenu(cbamPlayer, new Menu_Plugin(cbamPlayer)))
			Plugin.getPlugin().getLogger().warning("*** Parameters: none");
	}
	public static void openProjectsMenu(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, int page) {
		if(!openMenu(cbamPlayer, new Menu_Projects(backMenu, cbamPlayer, worldName, page)))
			Plugin.getPlugin().getLogger().warning("*** Parameters:\nworldName="+worldName+"\npage="+page);
	}
	public static void openTheProjectMenu(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, String projectName) {
		if(!openMenu(cbamPlayer, new Menu_TheProject(backMenu, cbamPlayer, worldName, projectName)))
		Plugin.getPlugin().getLogger().warning("*** Parameters:\nworldName="+worldName+"\nprojectName="+projectName);
	}
	public static void openTheProjectActionsGroupsMenu(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, String projectName, int page) {
		if(!openMenu(cbamPlayer, new Menu_TheProjectActionsGroups(backMenu, cbamPlayer, worldName, projectName, page)))
		Plugin.getPlugin().getLogger().warning("*** Parameters:\nworldName="+worldName+"\nprojectName="+projectName+"\npage="+page);
	}
	public static void openTheProjectActionsSubGroupsMenu(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, String projectName, String actionsGroup, int page) {
		if(!openMenu(cbamPlayer, new Menu_TheProjectActionsSubGroups(backMenu, cbamPlayer, worldName, projectName, actionsGroup, page)))
			Plugin.getPlugin().getLogger().warning("*** Parameters:\nworldName="+worldName+"\nprojectName="+projectName+"\nactionsGroup"+actionsGroup+"\npage="+page);
	}
	public static void openTheProjectActionsDelaysMenu(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, String projectName, String actionsGroup, String actionsSubGroup, int page) {
		if(!openMenu(cbamPlayer, new Menu_TheProjectActionsDelays(backMenu, cbamPlayer, worldName, projectName, actionsGroup, actionsSubGroup, page)))
		Plugin.getPlugin().getLogger().warning("*** Parameters:\nworldName="+worldName+"\nprojectName="+projectName+"\nactionsGroup="+actionsGroup+"\nactionsSubGroup="+actionsSubGroup+"\npage="+page);
	}
	public static void openTheProjectActionsMenu(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, int page) {
		if(!openMenu(cbamPlayer, new Menu_TheProjectActions(backMenu, cbamPlayer, worldName, projectName, actionsGroup, actionsSubGroup, actionsDelayLong, page)))
		Plugin.getPlugin().getLogger().warning("*** Parameters:\nworldName="+worldName+"\nprojectName="+projectName+"\nactionsGroup="+actionsGroup+"\nactionsSubGroup="+actionsSubGroup+"\nactionsDelayLong="+actionsDelayLong+"\npage="+page);
	}
	public static void openTheProjectTheActionMenu(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, String actionName) {
		if(!openMenu(cbamPlayer, new Menu_TheProjectTheAction(backMenu, cbamPlayer, worldName, projectName, actionsGroup, actionsSubGroup, actionsDelayLong, actionName)))
		Plugin.getPlugin().getLogger().warning("*** Parameters:\nworldName="+worldName+"\nprojectName="+projectName+"\nactionsGroup="+actionsGroup+"\nactionsSubGroup="+actionsSubGroup+"\nactionsDelayLong="+actionsDelayLong+"\nactionName="+actionName);
	}
	public static void openTheProjectEventsTypesMenu(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, String projectName) {
		if(!openMenu(cbamPlayer, new ManagerMenu_TheProjectEventsTypes(backMenu, cbamPlayer, worldName, projectName)))
			Plugin.getPlugin().getLogger().warning("*** Parameters:\nworldName="+worldName+"\nprojectName="+projectName);
	}
	public static void backMenu(CbamPlayer cbamPlayer, PapiMenuV2 backMenu) {
		if(backMenu.refreshPapiMenuPage())
			cbamPlayer.openPapiMenuPage(backMenu);
	}
//	public static void openTheProjectTheActionAdvancedMenu(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, String actionName) {
//		if(!openMenu(cbamPlayer, new Menu_TheProjectTheActionAdvanced(backMenu, cbamPlayer, worldName, projectName, actionsGroup, actionsSubGroup, actionsDelayLong, actionName)))
//			Plugin.getPlugin().getLogger().warning("*** Parameters:\nworldName="+worldName+"\nprojectName="+projectName+"\nactionsGroup="+actionsGroup+"\nactionsSubGroup="+actionsSubGroup+"\nactionsDelayLong="+actionsDelayLong+"\nactionName="+actionName);
//	}
}

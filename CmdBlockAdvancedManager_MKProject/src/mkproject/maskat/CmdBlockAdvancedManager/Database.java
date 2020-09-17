package mkproject.maskat.CmdBlockAdvancedManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class Database {
	static class Config {
		private static ConfigurationSection getConfigurationSection(String path) {
			return Plugin.getPlugin().getConfig().getConfigurationSection(path);
		}
		
		private static List<String> getConfigurationSectionStringList(String path, boolean sort) {
			ConfigurationSection databaseSection = Plugin.getPlugin().getConfig().getConfigurationSection(path);
			if(databaseSection == null)
				return null;
			
			List<String> databaseStringList = new ArrayList<>(databaseSection.getKeys(false));
			
			if(sort) Collections.sort(databaseStringList);
			
			return databaseStringList;
		}
		
		private static List<Long> getConfigurationSectionLongList(String path, boolean sort) {
			ConfigurationSection databaseSection = Plugin.getPlugin().getConfig().getConfigurationSection(path);
			if(databaseSection == null)
				return null;
			
			List<String> databaseStringList = new ArrayList<>(databaseSection.getKeys(false));
			
			List<Long> databaseLongList = new ArrayList<>();
			
			for(String valueStr : databaseStringList) {
				try
				{
					long valueLong = Long.parseLong(valueStr);
					databaseLongList.add(valueLong);
				}
				catch(Exception ex)
				{
					Plugin.getPlugin().getLogger().warning("***** WARNING ***** Invalid configuration: " + path);
				}
			}
			
			if(sort) Collections.sort(databaseLongList);
			
			return databaseLongList;
		}
		
		private static void addEmpty(String path) {
			Plugin.getPlugin().getConfig().set(path, "");
		}
		
		public static void set(String path, Object object) {
			Plugin.getPlugin().getConfig().set(path, object);
		}
		
		private static void delete(String path) {
			Plugin.getPlugin().getConfig().set(path, null);
		}
		
		public static void clearIfEmpty(String path) {
			ConfigurationSection databaseSection = Plugin.getPlugin().getConfig().getConfigurationSection(path);
			if(databaseSection != null && databaseSection.getKeys(false).size() <= 0)
				Config.delete(path);
		}

		public static Object get(String path) {
			return Plugin.getPlugin().getConfig().get(path);
		}
		public static String getString(String path) {
			return Plugin.getPlugin().getConfig().getString(path);
		}
		public static long getLong(String path) {
			return Plugin.getPlugin().getConfig().getLong(path);
		}
		public static int getInteger(String path) {
			return Plugin.getPlugin().getConfig().getInt(path);
		}
		public static void save() {
			Plugin.getPlugin().saveConfig();
		}
	}
	
	public static boolean isProjectExist(String worldName, String projectName) {
		return (Plugin.getPlugin().getConfig().get("database."+worldName+"."+projectName) != null);
	}
	public static boolean isActionExist(String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, String actionName) {
		return (Plugin.getPlugin().getConfig().get("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong+"."+actionName) != null);
	}
	public static boolean isActionsGroupExist(String worldName, String projectName, String actionsGroup) {
		return (Plugin.getPlugin().getConfig().get("database."+worldName+"."+projectName+".Actions."+actionsGroup) != null);
	}
	public static boolean isActionsSubGroupExist(String worldName, String projectName, String actionsGroup, String actionsSubGroup) {
		return (Plugin.getPlugin().getConfig().get("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup) != null);
	}
	
	public static void addActionsGroup(String worldName, String projectName, String newActionsGroupName) {
		Config.addEmpty("database."+worldName+"."+projectName+".Actions."+newActionsGroupName);
		Config.save();
	}
	public static void addActionsSubGroup(String worldName, String projectName, String actionsGroup, String newActionsSubGroupName) {
		Config.addEmpty("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+newActionsSubGroupName);
		Config.save();
	}
	public static void addAction(String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, String newActionName) {
		Config.addEmpty("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong+"."+newActionName);
		Config.save();
	}
	public static void deleteGroup(String worldName, String projectName, String actionsGroup) {
		Config.delete("database."+worldName+"."+projectName+".Actions."+actionsGroup);
		Config.save();
	}
	public static void deleteSubGroup(String worldName, String projectName, String actionsGroup, String actionsSubGroup) {
		Config.delete("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup);
		Config.save();
	}
	public static void deleteAction(String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, String actionName) {
		Config.delete("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong+"."+actionName);
		Config.clearIfEmpty("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong);
		Config.save();
	}
	public static void renameActionsGroup(String worldName, String projectName, String actionsGroup, String newActionsGroupName) {
		ConfigurationSection databaseSection = Config.getConfigurationSection("database."+worldName+"."+projectName+".Actions."+actionsGroup);
		Config.set("database."+worldName+"."+projectName+".Actions."+newActionsGroupName, (databaseSection == null ? "" : databaseSection));//.getKeys(true)
		Config.delete("database."+worldName+"."+projectName+".Actions."+actionsGroup);
		Config.save();
	}
	public static void renameActionsSubGroup(String worldName, String projectName, String actionsGroup, String actionsSubGroup, String newActionsSubGroupName) {
		ConfigurationSection databaseSection = Config.getConfigurationSection("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup);
		Config.set("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+newActionsSubGroupName, (databaseSection == null ? "" : databaseSection));//.getKeys(true)
		Config.delete("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup);
		Config.save();
	}
	public static void renameAction(String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, String actionName, String newActionName) {
		ConfigurationSection databaseSection = Config.getConfigurationSection("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong+"."+actionName);
		Config.set("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong+"."+newActionName, (databaseSection == null ? "" : databaseSection));//.getKeys(true)
		Config.delete("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong+"."+actionName);
		Config.save();
	}
	public static void setActionsGroupParameterRandomnessAmount(String worldName, String projectName, String actionsGroup, int newActionsGroupRandomnessAmount) {
		Config.set("database."+worldName+"."+projectName+".Actions."+actionsGroup+".Parameters.RandomnessAmount", newActionsGroupRandomnessAmount);
		Config.save();
	}
	public static void setActionCommandExecute(String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, String actionName, String newExecutedCommand) {
		Config.set("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong+"."+actionName+".CommandExecute", newExecutedCommand);
		Config.save();
	}
	public static void setActionDelayTime(String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, String actionName, long newActionDelayTimeLong) {
		ConfigurationSection databaseSection = Config.getConfigurationSection("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong+"."+actionName);
		Config.set("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+newActionDelayTimeLong+"."+actionName, (databaseSection == null ? "" : databaseSection));//.getKeys(true)
		Config.delete("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong+"."+actionName);
		Config.clearIfEmpty("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong);
		Config.save();
	}
	public static void setActionRepeatTime(String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, String actionName, long newActionRepeatTimeLong) {
		Config.set("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong+"."+actionName+".RepeatTime", newActionRepeatTimeLong == 0 ? null : newActionRepeatTimeLong);
		Config.save();
	}
	public static void setActionPositioned(String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, String actionName, String newActionPositioned) {
		Config.set("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong+"."+actionName+".Positioned", newActionPositioned);
		Config.save();
	}
	
	public static List<String> getWorlds() {
		return Config.getConfigurationSectionStringList("database", true);
	}
	public static List<String> getProjects(String worldName) {
		return Config.getConfigurationSectionStringList("database."+worldName, true);
	}
	public static List<String> getActionsGroups(String worldName, String projectName) {
		return Config.getConfigurationSectionStringList("database."+worldName+"."+projectName+".Actions", true);
	}
	public static List<String> getActionsSubGroups(String worldName, String projectName, String actionsGroup) {
		return Config.getConfigurationSectionStringList("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List", true);
	}
	public static List<Long> getActionsDelays(String worldName, String projectName, String actionsGroup, String actionsSubGroup) {
		return Config.getConfigurationSectionLongList("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup, true);
	}
	public static List<String> getActions(String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong) {
		return Config.getConfigurationSectionStringList("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong, true);
	}

	public static int getActionsGroupParameterRandomnessAmount(String worldName, String projectName, String actionsGroup) {
		return Config.getInteger("database."+worldName+"."+projectName+".Actions."+actionsGroup+".Parameters.RandomnessAmount");
	}
	
	public static long getActionRepeatTime(String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, String actionName) {
		return Config.getLong("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong+"."+actionName+".RepeatTime");
	}
	public static String getActionCommandExecute(String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, String actionName) {
		return Config.getString("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong+"."+actionName+".CommandExecute");
	}
	public static String getActionPositioned(String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, String actionName) {
		return Config.getString("database."+worldName+"."+projectName+".Actions."+actionsGroup+".List."+actionsSubGroup+"."+actionsDelayLong+"."+actionName+".Positioned");
	}






}

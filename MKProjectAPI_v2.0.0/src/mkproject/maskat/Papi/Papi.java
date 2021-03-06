package mkproject.maskat.Papi;

import java.io.IOException;
import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import me.maskat.InventoryManager.InventoryManagerAPI;
import mkproject.maskat.Papi.Bungee.BungeeChannel;
import mkproject.maskat.Papi.Bungee.BungeeServer;
import mkproject.maskat.Papi.Bungee.PapiBungee;
import mkproject.maskat.Papi.Model.PapiModel;
import mkproject.maskat.Papi.Model.PapiPlayer;
import mkproject.maskat.Papi.MySQL.PapiSQL;
import mkproject.maskat.Papi.Scheduler.PapiScheduler;
import mkproject.maskat.Papi.Scheduler.PapiSchedulerManager;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

abstract public class Papi {
	public static boolean DEVELOPER_DEBUG = false;
	public static boolean DEVELOPER_DATABASE = false;
	public static boolean DEVELOPER_DATABASE_AUTODELETE = false;
	public static boolean DEVELOPER_DIRECTORY_AUTODELETE = false;
	
	public static class Debug {
		public static void setDebug(boolean enabled) {
			DEVELOPER_DEBUG = enabled;
		}
	}
	
	public static class Yaml {
		public static YamlConfiguration registerYaml(final JavaPlugin plugin, final String fileName) {
			return PapiConfigManager.registerYaml(plugin, fileName);
		}
		public static void saveYaml(final JavaPlugin plugin, final String fileName, final YamlConfiguration yamlConfiguration) {
			PapiConfigManager.saveYaml(plugin, fileName, yamlConfiguration);
		}
	}
	public static class Vault {
		public static Economy getEconomy() {
			return PapiVault.getEconomy();
		}
		public static Permission getPermissions() {
			return PapiVault.getPermissions();
		}
	    public static Chat getChat() {
	        return PapiVault.getChat();
	    }
	}
	public static class Region {
		public static Collection<Block> copyArea(Location fromPoint, Location fromPointEnd, Location toPoint) {
			return PapiRegion.copyArea(fromPoint, fromPointEnd, toPoint);
		}
		public static Collection<Block> getBlocks(Location pointStart, Location pointEnd) {
			return PapiRegion.getBlocks(pointStart, pointEnd);
		}
		public static Collection<Block> setArea(Location pointStart, Location pointEnd, Material material) {
			return PapiRegion.setArea(pointStart, pointEnd, material);
		}
		public static Collection<Block> setAreaCyl(Location pointCenter, int radius, int height, Material material) {
			return PapiRegion.setAreaCyl(pointCenter, radius, height, material);
		}
		public static Collection<Block> setAreaCyl(Location pointCenter, int radius, int height, List<Material> materials, boolean applyPhysics) {
			return PapiRegion.setAreaCyl(pointCenter, radius, height, materials, applyPhysics);
		}
	}
//	public static class MenuInventory {
//		public static MenuPage createPage(final int numberOfRows, final String colorTitle) {
//			return PapiMenuInventory.createPage(numberOfRows, colorTitle);
//		}
//		public static boolean removePage(final MenuPage menuPage) {
//			return PapiMenuInventory.removePage(menuPage);
//		}
//		public static void setItem(final MenuPage menuPage, final InventorySlot menuInvSlot, final Material material, final String colorNameAndLore) {
//			PapiMenuInventory.setItem(menuPage, menuInvSlot, material, colorNameAndLore);
//		}
//		public static void setItemHeadAsync(final MenuPage menuPage, final InventorySlot menuInvSlot, final Player player, final String colorNameAndLore) {
//			PapiMenuInventory.setItemHeadAsync(menuPage, menuInvSlot, player, colorNameAndLore);
//		}
//		public static void openPage(final MenuPage menuPage, final Player player) {
//			PapiMenuInventory.openPage(menuPage, player);
//		}
//	}
	public static class Model {
		public static PapiPlayer getPlayer(Player player) {
			return PapiModel.getPlayer(player);
		}
		public static List<Player> getPlayers() {
			return PapiModel.getPlayers();
		}
		public static boolean existPlayer(Player player) {
			return getPlayers().contains(player);
		}
	}
	public static class EventHelp {
		public static void PapiMenuInventoryClickEvent() {};
		public static void PapiPlayerChangeAfkEvent() {};
		public static void PapiPlayerLoginEvent() {};
		public static void PapiPlayerLogoutEvent() {};
	}
	public static class Function {
		public static boolean isNumeric(String strNum) {
			return PapiFunction.isNumeric(strNum, false);
		}
		public static boolean isNumeric(String strNum, boolean onlyPositiveNumeric) {
			return PapiFunction.isNumeric(strNum, onlyPositiveNumeric);
		}
		public static double getNumeric(String strNum, double defaultNum) {
			return PapiFunction.getNumeric(strNum, defaultNum);
		}
		public static int getNumeric(String strNum, int defaultNum) {
			return PapiFunction.getNumeric(strNum, defaultNum);
		}
		public static float getNumeric(String strNum, float defaultNum) {
			return PapiFunction.getNumeric(strNum, defaultNum);
		}
		public static int getDifference(int nr1, int nr2) {
			return PapiFunction.getDifference(nr1, nr2);
		}
		public static double getDifference(double nr1, double nr2) {
			return PapiFunction.getDifference(nr1, nr2);
		}
		public static String getPermission(JavaPlugin plugin, String permission) {
			return PapiFunction.getPermission(plugin, permission);
		}
		public static String getLocationToString(Location location, boolean toPreciseLocation, boolean withYawPitch) {
			return PapiFunction.getLocationToString(location, toPreciseLocation, withYawPitch);
		}
		public static Location getLocationFromString(String stringLocation) {
			return PapiFunction.getLocationFromString(stringLocation);
		}
		public static boolean isMovedBlock(Location locationFrom, Location locationTo, boolean checkVertical) {
			return PapiFunction.isMovedBlock(locationFrom, locationTo, checkVertical, false);
		}
		public static boolean isMovedBlock(Location locationFrom, Location locationTo, boolean checkVertical, boolean checkPrecision) {
			return PapiFunction.isMovedBlock(locationFrom, locationTo, checkVertical, checkPrecision);
		}
		public static boolean isLocationInRegion(Location location, Location regionPosFirst, Location regionPosSecound, boolean checkVertical) {
			return PapiFunction.isLocationInRegion(location, regionPosFirst, regionPosSecound, checkVertical);
		}
		public static Player getNearestPlayer(Location location) {
			return PapiFunction.getNearestPlayer(location);
		}
		public static List<Player> getPlayersNearSquared(Player player, int distance) {
			return PapiFunction.getPlayersNearSquared(player, distance);
		}
		public static List<Player> getPlayersNearSquared(Location location, int distance) {
			return PapiFunction.getPlayersNearSquared(location, distance);
		}
		public static boolean isSomePlayerNearSquared(Player player, int distance) {
			return PapiFunction.isSomePlayerNearSquared(player, distance);
		}
		public static boolean isSomePlayerNearSquared(Location location, int distance) {
			return PapiFunction.isSomePlayerNearSquared(location, distance);
		}
		public static List<Player> getPlayersNear(Player player, int distance) {
			return PapiFunction.getPlayersNear(player, distance);
		}
		public static List<Player> getPlayersNear(Location location, int distance) {
			return PapiFunction.getPlayersNear(location, distance);
		}
		public static boolean isSomePlayerNear(Player player, int distance) {
			return PapiFunction.isSomePlayerNear(player, distance);
		}
		public static boolean isSomePlayersNear(Location location, int distance) {
			return PapiFunction.isSomePlayersNear(location, distance);
		}
	    public static String inventoryToBase64(PlayerInventory playerInventory) {
	    	return InventoryManagerAPI.inventoryToBase64(playerInventory);
	    }
	    public static String inventoryToBase64(Inventory inventory) {
	    	return InventoryManagerAPI.inventoryToBase64(inventory);
	    }
	    public static ItemStack[] inventoryFromBase64(String data) throws IOException {
	    	return InventoryManagerAPI.inventoryFromBase64(data);
	    }
	    public static LocalDateTime getCurrentLocalDateTime() {
	    	return PapiFunction.getCurrentLocalDateTime();
		}
	    public static String getCurrentLocalDateTimeToString() {
	    	return PapiFunction.getCurrentLocalDateTimeToString();
		}
	    public static LocalDateTime getLocalDateTimeFromString(String datetime) {
	    	return PapiFunction.getLocalDateTimeFromString(datetime, null);
		}
	    public static LocalDateTime getLocalDateTimeFromString(String datetime, String pattern) {
	    	return PapiFunction.getLocalDateTimeFromString(datetime, pattern);
	    }
	    public static String getLocalDateTimeToString(LocalDateTime datetime) {
	    	return PapiFunction.getLocalDateTimeToString(datetime);
	    }
	    public static ItemStack randomEnchantment(ItemStack item) {
	    	return PapiFunction.randomEnchantment(item, false, 1);
	    }
	    public static ItemStack randomEnchantment(ItemStack item, boolean allowAllEnchant) {
	    	return PapiFunction.randomEnchantment(item, allowAllEnchant, 1);
	    }
	    public static ItemStack randomEnchantment(ItemStack item, boolean allowAllEnchant, int numberOfEnchants) {
	    	return PapiFunction.randomEnchantment(item, allowAllEnchant, numberOfEnchants);
	    }
	    public static ItemStack randomStorageEnchantment(ItemStack item) {
	    	return PapiFunction.randomStorageEnchantment(item, 1);
	    }
	    public static ItemStack randomStorageEnchantment(ItemStack item, int numberOfEnchants) {
	    	return PapiFunction.randomStorageEnchantment(item, numberOfEnchants);
	    }
	    public static int randomInteger(int min, int max) {
	    	return ThreadLocalRandom.current().nextInt(min, max + 1);
	    }
	    public static Object getRandom(Object type1, Object type2) {
	    	return (ThreadLocalRandom.current().nextInt(0, 2) == 0 ? type1 : type2);
	    }
	    public static Object getRandom(Object... types) {
	    	return types[ThreadLocalRandom.current().nextInt(0, types.length)];
	    }
	    public static double getTPS() {
	    	return TPS.getTPS();
	    }
	    public static ItemStack getCustomSkull(String headBase64) {
	    	return PapiFunction.getCustomSkull(headBase64);
	    }
	    public static ItemStack getCustomSkull(PapiHeadBase64 papiHeadBase64) {
	    	return PapiFunction.getCustomSkull(papiHeadBase64.getValue());
	    }
		public static String getRemainingTimeString(LocalDateTime datetimeExpires) {
			return PapiFunction.getRemainingTimeString(null, datetimeExpires);
		}
		public static String getRemainingTimeString(LocalDateTime datetimeStart, LocalDateTime datetimeExpires) {
			return PapiFunction.getRemainingTimeString(datetimeStart, datetimeExpires);
		}
		public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(Map<K, V> map) {
			return PapiFunction.sortMapByValue(map);
		}
	}
	public static class ItemUtils {
		public static ItemStack addLore(ItemStack itemStack, List<String> loresList) {
			return PapiItemUtils.addLore(itemStack, loresList);
		}
		public static ItemStack addGlow(ItemStack itemStack) {
			return PapiItemUtils.addGlow(itemStack);
		}
		public static ItemStack addEnchant(ItemStack itemStack, Enchantment enchant, int level) {
			return PapiItemUtils.addEnchant(itemStack, enchant, level);
		}
	}
	public static class Convert {
		public static String TimestampToDateStringFormat(String timestamp, String format) {
			return PapiConvert.getTimestampToDateStringFormat(timestamp, format);
		}
		public static Date TimestampToDate(String timestamp) {
			return PapiConvert.getTimestampToDate(timestamp);
		}
		public static Duration TimestampToDuration(String timestamp) {
			return PapiConvert.getTimestampToDuration(timestamp);
		}
//		public static String TimestampToDurationString(String timestamp) {
//			return PapiConvert.getTimestampToDurationString(timestamp);
//		}
		public static String TimestempTicksToDurationString(long timestempTicks) {
			return PapiConvert.getTimestempTicksToDurationString(timestempTicks);
		}
		public static List<String> ListIntegerToListString(List<Integer> listInteger) {
			return Lists.transform(listInteger, Functions.toStringFunction());
		}
		public static List<Integer> SetIntegerToListInteger(Set<Integer> setInteger) {
			return new ArrayList<>(setInteger);
		}
		public static List<String> SetIntegerToListString(Set<Integer> setInteger) {
			return Lists.transform(SetIntegerToListInteger(setInteger), Functions.toStringFunction());
		}
	}
	public static class Scheduler {
		public static boolean registerTimerTask(PapiScheduler papiScheduler, DayOfWeek dayOfWeek, int hourOfDay, int minute, int second) {
			return PapiSchedulerManager.registerTask(papiScheduler, dayOfWeek, hourOfDay, minute, second);
		}
		public static boolean registerTimerTask(PapiScheduler papiScheduler, int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
			return PapiSchedulerManager.registerTask(papiScheduler, year, month, dayOfMonth, hourOfDay, minute, second);
		}
	}
	public static class MySQL {
		public static ResultSet query(String sql) {
			return PapiSQL.query(sql);
		}
		public static boolean exists(final String where_arguments, final String table) {
			//TODO: potential dangerous with use char ' in variable where_arguments
			return PapiSQL.exists(where_arguments, table);
		}
		public static boolean exists(final String where_column, final String logic_gate, final Object where_data, final String table) {
			return PapiSQL.exists(where_column, logic_gate, where_data, table);
		}
//	    public static int put(final String columns, final String values, final String table) {
//	    	return PapiSQL.insertData(columns, values, table);
//	    }
	    public static int put(Map<String, Object> columnsValuesMap, final String table) {
	    	String columns = "";
	    	String values = "";

	    	for (Map.Entry<String,Object> entry : columnsValuesMap.entrySet()) {  
	    		columns = columns+"`"+entry.getKey().replace("`", "\\`")+"`,";
	    		if(entry.getValue() == null)
	    			values += "NULL,";
	    		else
	    			values += "'"+String.valueOf(entry.getValue()).replace("'", "\\'")+"',";
	    	}
	    	
	    	if(columns.length() > 0 && values.length() > 0)
	    		return PapiSQL.insertData(columns.substring(0, columns.length()-1), values.substring(0, values.length()-1), table);
	    	else
	    		return -1;
	    }
	    public static boolean set(Map<String, Object> columnsValuesMap, final String where_column, final String logic_gate, Object where_data, final String table) {
	    	String changeSet = "";

	    	for (Map.Entry<String,Object> entry : columnsValuesMap.entrySet()) {
	    		if(entry.getValue() == null || String.valueOf(entry.getValue()).length() <= 0)
	    			changeSet = changeSet+"`"+entry.getKey().replace("`", "\\`")+"`=NULL,";
	    		else if(entry.getValue() instanceof Boolean)
		    		changeSet = changeSet+"`"+entry.getKey().replace("`", "\\`")+"`="+String.valueOf(entry.getValue())+",";
		    	else
		    		changeSet = changeSet+"`"+entry.getKey().replace("`", "\\`")+"`='"+String.valueOf(entry.getValue()).replace("'", "\\'")+"',";
	    	}
	    	
	    	if(changeSet.length() > 0)
	    		return PapiSQL.setMulti(changeSet.substring(0, changeSet.length()-1), where_column, logic_gate, where_data, table);
	    	else
	    		return false;
	    }
	    public static boolean setOrderBy(int limit, Map<String, Object> columnsValuesMap, final String where_column, final String logic_gate, Object where_data, final String orderByColumn, final String OrderType, final String table) {
	    	String changeSet = "";
	    	
	    	for (Map.Entry<String,Object> entry : columnsValuesMap.entrySet()) {
	    		if(entry.getValue() == null || String.valueOf(entry.getValue()).length() <= 0)
	    			changeSet = changeSet+"`"+entry.getKey().replace("`", "\\`")+"`=NULL,";
	    		else if(entry.getValue() instanceof Boolean)
	    			changeSet = changeSet+"`"+entry.getKey().replace("`", "\\`")+"`="+String.valueOf(entry.getValue())+",";
	    		else
	    			changeSet = changeSet+"`"+entry.getKey().replace("`", "\\`")+"`='"+String.valueOf(entry.getValue()).replace("'", "\\'")+"',";
	    	}
	    	
	    	if(changeSet.length() > 0)
	    		return PapiSQL.setMultiOrderBy(limit, changeSet.substring(0, changeSet.length()-1), where_column, logic_gate, where_data, orderByColumn, OrderType, table);
	    	else
	    		return false;
	    }
//	    public static boolean set(final String key, Object value, final String where_column, final String logic_gate, Object where_data, final String table) {
//	    	return PapiSQL.set(key, value, where_column, logic_gate, where_data, table);
//	    }
	    public static Object get(final String key, final String where_column, final String logic_gate, String where_data, final String table) {
	        return PapiSQL.get(key, where_column, logic_gate, where_data, table); 
	    }
		public static Object get(final String key, final String getWhereObject, final String table) {
			return PapiSQL.get(key, getWhereObject, table);
		}
		public static Object getOrderBy(final String key, final String getWhereObject, final String orderByColumn, final String OrderType, final String table) {
			return PapiSQL.getOrderBy(key, getWhereObject, orderByColumn, OrderType, table);
		}
		public static boolean deleteTable(final String table) {
			return PapiSQL.deleteTable(table);
		}
		public static boolean createTable(final String table, final String columns) {
			return PapiSQL.createTable(table, columns);
		}
	    public static ResultSet getResultSetAll(final int limit, final String select, final String table) {
	    	return PapiSQL.getResultSetAll(limit, select, table);
	    }
	    public static ResultSet getResultSetAll(final int limit, final String select, final String getWhereObject, final String table) {
	    	return PapiSQL.getResultSetAll(limit, select, getWhereObject, table);
	    }
	    public static ResultSet getResultSetAllOrderBy(final int limit, final List<String> selects, final String getWhereObject, final String orderByColumn, final String OrderType, final String table) {
	    	return PapiSQL.getResultSetAllOrderBy(limit, selects, getWhereObject, orderByColumn, OrderType, table);
	    }
	    public static ResultSet getResultSetAllOrderBy(final int limit, final String select, final String orderByColumn, final String OrderType, final String table) {
	    	return PapiSQL.getResultSetAllOrderBy(limit, List.of(select), orderByColumn, OrderType, table);
	    }
	    public static ResultSet getResultSetAllOrderBy(final int limit, final List<String> selects, final String orderByColumn, final String OrderType, final String table) {
	    	return PapiSQL.getResultSetAllOrderBy(limit, selects, orderByColumn, OrderType, table);
	    }
	    public static ResultSet getResultSetAllOrderBy(final int limit, final List<String> selects, final String getWhereObject, final List<String> orderByColumn, List<String> OrderType, final String table) {
	    	return PapiSQL.getResultSetAllOrderBy(limit, selects, getWhereObject, orderByColumn, OrderType, table);
	    }
	    public static boolean deleteData(final String column, final String logic_gate, String data, final String table) {
	    	return PapiSQL.deleteData(column, logic_gate, data, table);
	    }
	}
	public static class SQL {
		public static class OrderType {
			public static String ASC = "ASC";
			public static String DESC = "DESC";
		}
		
		public static String getWhereObject(String key, String logic_gate, Object value) {
			if(value == null)
			{
				if(logic_gate.equals("="))
					logic_gate = "IS";
				else if(logic_gate.equals("!="))
					logic_gate = "IS NOT";
				return "`"+key+"`"+logic_gate+" NULL";
			}
			else if(value instanceof Boolean)
				return "`"+key+"`"+logic_gate+" "+value;
			else
				return "`"+key+"`"+logic_gate+" '"+String.valueOf(value).replace("'", "\\'")+"'";
		}
		public static String getWhereAnd(String... getWhereObjects) {
			String args = "";
			if(getWhereObjects.length > 1)
			{
				for(String getWhereObject : getWhereObjects) {
					args = args+"("+getWhereObject+") AND ";
				}
				args = args.substring(0, args.length() - 5);
			}
			else
				return getWhereObjects[0];
			return args;
		}
		public static String getWhereOr(String... getWhereObjects) {
			String args = "";
			if(getWhereObjects.length > 1)
			{
				for(String getWhereObject : getWhereObjects) {
					args = args+"("+getWhereObject+") OR ";
				}
				args = args.substring(0, args.length() - 4);
			}
			else
				return getWhereObjects[0];
			return args;
		}
		
		public static class ValType {
			public static String INT = "INT";
			public static String VARCHAR = "VARCHAR";
			public static String DATETIME = "DATETIME";
			public static String BOOLEAN = "BOOLEAN";
			public static String TEXT = "TEXT";
			public static String DOUBLE = "DOUBLE";
		}
		public static class ValDefault {
			public static String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";
		}
		
		public static String createColumnParse(String column, String ValType, int length, boolean require) {
			return createColumnParse(column, ValType, length, require, false, false, null);
		}
		
		public static String createColumnParse(String column, String ValType, int length, boolean require, boolean autoIncrement, boolean unique) {
			return createColumnParse(column, ValType, length, require, autoIncrement, unique, null);
		}
		
		public static String createColumnParse(String column, String valType, int length, boolean require, boolean autoIncrement, boolean unique, String ValDefault) {
			String arg;
			if(valType.equals(ValType.BOOLEAN) || valType.equals(ValType.TEXT) || valType.equals(ValType.DATETIME) || valType.equals(ValType.DOUBLE))
				arg = "`"+column+"` "+valType;
			else
				arg = "`"+column+"` "+valType+" ("+length+")";
			
			if(require)
				arg = arg + " NOT NULL";
			if(autoIncrement)
				arg = arg + " AUTO_INCREMENT";
			if(unique)
				arg = arg + " UNIQUE";
			if(ValDefault != null && ValDefault.length() > 0) {
				arg = arg + " DEFAULT "+ValDefault;
			}
			return arg;
		}
		public static String createColumnPrimary(String... columns) {
			return "PRIMARY KEY (`"+String.join("`,`", columns)+"`)";
		}
	}
	
	public static class Server {
		public static World getServerSpawnWorld() {
			return PapiServer.getServerSpawnWorld();
		}
		public static World getServerLobbyWorld() {
			return PapiServer.getServerLobbyWorld();
		}
		public static Location getServerLobbyLocation() {
			return PapiServer.getServerLobbyLocation();
		}
		public static Location getServerSpawnLocation() {
			return PapiServer.getServerSpawnLocation();
		}
		public static World getSurvivalWorld() {
			return PapiServer.getSurvivalWorld();
		}
		public static World getNetherWorld() {
			return PapiServer.getNetherWorld();
		}
		public static World getTheEndWorld() {
			return PapiServer.getTheEndWorld();
		}
		public static boolean isSurvivalWorld(World world, boolean allowSpawn, boolean allowLobby) {
			return PapiServer.isSurvivalWorld(world, allowSpawn, allowLobby);
		}
		public static void initServerSpawnLocation(Location location) {
			PapiServer.initServerSpawnLocation(location);
		}
		public static void initTheEndWorld(World world) {
			PapiServer.initTheEndWorld(world);
		}
		public static void dispatchCommand(JavaPlugin pluginExecutor, String command) {
			PapiServer.dispatchCommand(pluginExecutor, command);
		}
		public static void dispatchCommandSilent(String command) {
			PapiServer.dispatchCommandSilent(command);
		}
	}
	public static class WorldEdit {
		public static boolean pasteSchematic(String fileName, Location location, boolean pasteAir) {
			return PapiWorldEdit.pasteSchematic(fileName, location, pasteAir, false) == null ? false : true;
		}
		public static WorldEditPlugin getPlugin() {
			return PapiWorldEdit.getPlugin();
		}
	}
	public static class Bungee {
		public static boolean connectPlayer(Player player, BungeeServer BungeeServer) {
			return PapiBungee.connectPlayer(player, BungeeServer);
		}
		public static boolean forward(BungeeServer BungeeServer, BungeeChannel BungeeChannel) {
			return PapiBungee.forward(BungeeServer, BungeeChannel);
		}
		public static boolean forwardPlayer(Player player, BungeeServer BungeeServer, BungeeChannel BungeeChannel) {
			return PapiBungee.forwardPlayer(player, BungeeServer, BungeeChannel);
		}
	}
}

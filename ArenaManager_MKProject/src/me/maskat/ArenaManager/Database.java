package me.maskat.ArenaManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.maskat.ArenaManager.Models.ArenesModel;
import mkproject.maskat.Papi.Papi;

public class Database {
	
	public static void initialize(Plugin plugin) {
		Arenes.TABLE = plugin.getConfig().getString("MySQL.Table.Arenes")+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		Teams.TABLE = plugin.getConfig().getString("MySQL.Table.Teams")+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		Spawns.TABLE = plugin.getConfig().getString("MySQL.Table.Spawns")+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		Objects.TABLE = plugin.getConfig().getString("MySQL.Table.Objects")+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		ObjectsGroups.TABLE = plugin.getConfig().getString("MySQL.Table.ObjectsGroups")+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		
		if(Papi.DEVELOPER_DATABASE && Papi.DEVELOPER_DATABASE_AUTODELETE)
			Papi.MySQL.deleteTable(Arenes.TABLE);
		
		Papi.MySQL.createTable(Arenes.TABLE,
					Papi.SQL.createColumnParse(Database.Arenes.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Database.Arenes.NAME, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Database.Arenes.DESCRIPTION, Papi.SQL.ValType.VARCHAR, 128, false)
				+","+Papi.SQL.createColumnParse(Database.Arenes.TYPE, Papi.SQL.ValType.VARCHAR, 32, false)
				+","+Papi.SQL.createColumnParse(Database.Arenes.ICON, Papi.SQL.ValType.VARCHAR, 32, false)
				+","+Papi.SQL.createColumnParse(Database.Arenes.WORLD, Papi.SQL.ValType.VARCHAR, 32, false)
				+","+Papi.SQL.createColumnParse(Database.Arenes.ENABLED, Papi.SQL.ValType.BOOLEAN, -1, true, false, false, "true")
				+","+Papi.SQL.createColumnPrimary(Database.Arenes.ID)
		);
		
		Papi.MySQL.createTable(Teams.TABLE,
				Papi.SQL.createColumnParse(Database.Teams.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Database.Teams.NAME, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Database.Teams.MAX_PLAYERS, Papi.SQL.ValType.INT, 4, true)
				+","+Papi.SQL.createColumnParse(Database.Teams.TYPE, Papi.SQL.ValType.VARCHAR, 32, false)
				+","+Papi.SQL.createColumnParse(Database.Teams.ICON, Papi.SQL.ValType.VARCHAR, 32, true)
				+","+Papi.SQL.createColumnParse(Database.Teams.ARENAID, Papi.SQL.ValType.INT, 16, true)
				+","+Papi.SQL.createColumnPrimary(Database.Teams.ID)
				);
		
		Papi.MySQL.createTable(Spawns.TABLE,
				Papi.SQL.createColumnParse(Database.Spawns.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Database.Spawns.NAME, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Database.Spawns.TEAMID, Papi.SQL.ValType.INT, 16, true)
				+","+Papi.SQL.createColumnParse(Database.Spawns.LOCATION, Papi.SQL.ValType.VARCHAR, 512, true)
				+","+Papi.SQL.createColumnPrimary(Database.Spawns.ID)
				);
		
		Papi.MySQL.createTable(ObjectsGroups.TABLE,
				Papi.SQL.createColumnParse(Database.ObjectsGroups.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Database.ObjectsGroups.NAME, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Database.ObjectsGroups.TYPE, Papi.SQL.ValType.VARCHAR, 32, false)
				+","+Papi.SQL.createColumnParse(Database.ObjectsGroups.ARENAID, Papi.SQL.ValType.INT, 16, true)
				+","+Papi.SQL.createColumnPrimary(Database.ObjectsGroups.ID)
				);
		
		Papi.MySQL.createTable(Objects.TABLE,
				Papi.SQL.createColumnParse(Database.Objects.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Database.Objects.NAME, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Database.Objects.OBJ_GROUPID, Papi.SQL.ValType.INT, 16, true)
				+","+Papi.SQL.createColumnParse(Database.Objects.LOCATION, Papi.SQL.ValType.VARCHAR, 512, true)
				+","+Papi.SQL.createColumnPrimary(Database.Objects.ID)
				);
		
		Plugin.getPlugin().getLogger().info("Loading data from MySQL...");
		
		ResultSet rsArenes = Papi.MySQL.getResultSetAll(-1, "*", Arenes.TABLE);
		if(rsArenes != null) {
			try {
				while (rsArenes.next()) {
					int arenaId = rsArenes.getInt(Database.Arenes.ID);
					String arenaName = rsArenes.getString(Database.Arenes.NAME);
					String arenaDescription = rsArenes.getString(Database.Arenes.DESCRIPTION);
					String arenaType = rsArenes.getString(Database.Arenes.TYPE);
					String arenaIconName = rsArenes.getString(Database.Arenes.ICON);
					String arenaWorldName = rsArenes.getString(Database.Arenes.WORLD);
					boolean arenaEnabled = rsArenes.getBoolean(Database.Arenes.ENABLED);

					ArenesModel.initArena(arenaId, arenaName, arenaDescription, arenaType, arenaIconName, arenaWorldName, arenaEnabled);
					Plugin.getPlugin().getLogger().info("Initialized arena: [" + arenaId + "] " + arenaName);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		List<Integer> teamsIdToDelete = new ArrayList<Integer>();
		ResultSet rsTeams = Papi.MySQL.getResultSetAll(-1, "*", Teams.TABLE);
		if(rsTeams != null) {
			try {
				while (rsTeams.next()) {
					int teamId = rsTeams.getInt(Database.Teams.ID);
					String teamName = rsTeams.getString(Database.Teams.NAME);
					int teamMaxPlayers = rsTeams.getInt(Database.Teams.MAX_PLAYERS);
					String teamType = rsTeams.getString(Database.Teams.TYPE);
					String teamIconName = rsTeams.getString(Database.Teams.ICON);
					int arenaId = rsTeams.getInt(Database.Teams.ARENAID);
					if(ArenesModel.existArena(arenaId)) {
						ArenesModel.getArena(arenaId).initTeam(teamId, teamName, teamMaxPlayers, teamType, teamIconName);
						Plugin.getPlugin().getLogger().info("Initialized team: [" + teamId + "] " + teamName);
					}
					else
						teamsIdToDelete.add(teamId);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(int teamId : teamsIdToDelete)
			Papi.MySQL.deleteData(Database.Teams.ID, "=", String.valueOf(teamId), Database.Teams.TABLE);
		
		List<Integer> spawnsIdToDelete = new ArrayList<Integer>();
		ResultSet rsSpawns = Papi.MySQL.getResultSetAll(-1, "*", Spawns.TABLE);
		if(rsSpawns != null) {
			try {
				while (rsSpawns.next()) {
					int spawnId = rsSpawns.getInt(Database.Spawns.ID);
					String spawnName = rsSpawns.getString(Database.Spawns.NAME);
					int teamId = rsSpawns.getInt(Database.Spawns.TEAMID);
					String spawnLocation = rsSpawns.getString(Database.Spawns.LOCATION);
					
					Object arenaIdObj = Papi.MySQL.get(Database.Teams.ARENAID, Database.Teams.ID, "=", String.valueOf(teamId), Database.Teams.TABLE);
					int arenaId = -1;
					if(arenaIdObj != null)
						arenaId = Integer.parseInt(String.valueOf(arenaIdObj));
					
					if(ArenesModel.existArena(arenaId) && ArenesModel.getArena(arenaId).existTeam(teamId)) {
						ArenesModel.getArena(arenaId).getTeam(teamId).initSpawn(spawnId, spawnName, Papi.Function.getLocationFromString(spawnLocation));
						Plugin.getPlugin().getLogger().info("Initialized spawn: [" + spawnId + "] " + spawnName);
					}
					else
						spawnsIdToDelete.add(spawnId);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
		for(int spawnId : spawnsIdToDelete)
			Papi.MySQL.deleteData(Database.Spawns.ID, "=", String.valueOf(spawnId), Database.Spawns.TABLE);
		
		List<Integer> objGroupsIdToDelete = new ArrayList<Integer>();
		ResultSet rsObjectsGroups = Papi.MySQL.getResultSetAll(-1, "*", ObjectsGroups.TABLE);
		if(rsObjectsGroups != null) {
			try {
				while (rsObjectsGroups.next()) {
					int objGroupId = rsObjectsGroups.getInt(Database.ObjectsGroups.ID);
					String objGroupName = rsObjectsGroups.getString(Database.ObjectsGroups.NAME);
					String objGroupType = rsObjectsGroups.getString(Database.ObjectsGroups.TYPE);
					int arenaId = rsObjectsGroups.getInt(Database.ObjectsGroups.ARENAID);
					
					if(ArenesModel.existArena(arenaId)) {
						ArenesModel.getArena(arenaId).initObjectsGroup(objGroupId, objGroupName, objGroupType);
						Plugin.getPlugin().getLogger().info("Initialized object group: [" + objGroupId + "] " + objGroupName);
					}
					else
						objGroupsIdToDelete.add(objGroupId);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(int objGroupId : objGroupsIdToDelete)
			Papi.MySQL.deleteData(Database.ObjectsGroups.ID, "=", String.valueOf(objGroupId), Database.ObjectsGroups.TABLE);
		
		List<Integer> objectsIdToDelete = new ArrayList<Integer>();
		ResultSet rsObjects = Papi.MySQL.getResultSetAll(-1, "*", Objects.TABLE);
		if(rsObjects != null) {
			try {
				while (rsObjects.next()) {
					int objectId = rsObjects.getInt(Database.Objects.ID);
					String objectName = rsObjects.getString(Database.Objects.NAME);
					int objGroupId = rsObjects.getInt(Database.Objects.OBJ_GROUPID);
					String objectLocation = rsObjects.getString(Database.Objects.LOCATION);
					
					Object arenaIdObj = Papi.MySQL.get(Database.ObjectsGroups.ARENAID, Database.ObjectsGroups.ID, "=", String.valueOf(objGroupId), Database.ObjectsGroups.TABLE);
					int arenaId = -1;
					if(arenaIdObj != null)
						arenaId = Integer.parseInt(String.valueOf(arenaIdObj));
					
					if(ArenesModel.existArena(arenaId) && ArenesModel.getArena(arenaId).existObjectsGroup(objGroupId)) {
						ArenesModel.getArena(arenaId).getObjectsGroup(objGroupId).initObject(objectId, objectName, Papi.Function.getLocationFromString(objectLocation));
						Plugin.getPlugin().getLogger().info("Initialized object: [" + objectId + "] " + objectName);
					}
					else
						objectsIdToDelete.add(objectId);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(int objectId : objectsIdToDelete)
			Papi.MySQL.deleteData(Database.Objects.ID, "=", String.valueOf(objectId), Database.Objects.TABLE);
	}
	
	public static class Arenes {
		public static String TABLE;
		public static final String ID = "arenaid";
		public static final String NAME = "arenaname";
		public static final String DESCRIPTION = "arenadescription";
		public static final String TYPE = "arenatype";
		public static final String ICON = "arenaicon";
		public static final String WORLD = "arenaworld";
		public static final String ENABLED = "arenaenabled";
	}
	public static class Teams {
		public static String TABLE;
		public static final String ID = "teamid";
		public static final String NAME = "teamname";
		public static final String MAX_PLAYERS = "teammaxplayers";
		public static final String TYPE = "teamtype";
		public static final String ICON = "teamicon";
		public static final String ARENAID = "arenaid";
	}
	public static class Spawns {
		public static String TABLE;
		public static final String ID = "spawnid";
		public static final String NAME = "spawnname";
		public static final String TEAMID = "teamid";
		public static final String LOCATION = "spawnlocation";
	}
	public static class Objects {
		public static String TABLE;
		public static final String ID = "objectid";
		public static final String NAME = "objectname";
		public static final String OBJ_GROUPID = "objgroupid";
		public static final String LOCATION = "objectlocation";
	}
	public static class ObjectsGroups {
		public static String TABLE;
		public static final String ID = "objgroupid";
		public static final String NAME = "objgroupname";
		public static final String TYPE = "objgrouptype";
		public static final String ARENAID = "arenaid";
	}
}

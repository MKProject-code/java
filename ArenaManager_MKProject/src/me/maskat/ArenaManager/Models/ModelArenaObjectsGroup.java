package me.maskat.ArenaManager.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;

import me.maskat.ArenaManager.Database;
import mkproject.maskat.Papi.Papi;

public class ModelArenaObjectsGroup {
	private int objGroupId;
	private String objGroupName;
	private String objGroupType;
	
	private Map<Integer, ModelArenaObject> arenaObjectsMap = new HashMap<>();
	
	public ModelArenaObjectsGroup(int objGroupId, String objGroupName, String objGroupType) {
		this.objGroupId = objGroupId;
		this.objGroupName = objGroupName;
		this.objGroupType = objGroupType;
	}
	
	public int getId() {
		return this.objGroupId;
	}
	
	public String getName() {
		return this.objGroupName;
	}
	
	public String getType() {
		return this.objGroupType;
	}
	
	public boolean setName(String objGroupNewName) {
		if(!Papi.MySQL.set(Map.of(Database.ObjectsGroups.NAME, objGroupNewName), Database.ObjectsGroups.ID, "=", objGroupId, Database.ObjectsGroups.TABLE))
			return false;
		
		this.objGroupName = objGroupNewName;
		return true;
	}
	
	public boolean setType(String objGroupNewType, boolean existTeamType) {
		if(!existTeamType)
			return false;
		
		if(!Papi.MySQL.set(Map.of(Database.ObjectsGroups.TYPE, objGroupNewType), Database.ObjectsGroups.ID, "=", objGroupId, Database.ObjectsGroups.TABLE))
			return false;
		
		this.objGroupType = objGroupNewType;
		return true;
	}
	
	public ModelArenaObject getObject(int objectId) {
		return arenaObjectsMap.get(objectId);
	}
	
	public boolean existObject(int objectId) {
		return arenaObjectsMap.containsKey(objectId);
	}
	
	public boolean removeObject(int objectId) {
		if(!existObject(objectId) || !Papi.MySQL.deleteData(Database.Objects.ID, "=", String.valueOf(objectId), Database.Objects.TABLE))
			return false;
		
		arenaObjectsMap.remove(objectId);
		return true;
	}
	
	public List<Integer> getObjectsIdList() {
		return new ArrayList<>(arenaObjectsMap.keySet());
	}
	public List<String> getObjectsIdListString() {
		return Lists.transform(new ArrayList<>(arenaObjectsMap.keySet()), Functions.toStringFunction());
	}

	public Map<Integer, ModelArenaObject> getObjectsMap() {
		return arenaObjectsMap;
	}
	
	public void initObject(int objectId, String objectName, Location locationFromString) {
		arenaObjectsMap.put(objectId, new ModelArenaObject(objectId, objectName, locationFromString));
	}

	public boolean addObject(String objectName, Location objectLocation) {
		Object arenaId = Papi.MySQL.get(Database.ObjectsGroups.ARENAID, Database.ObjectsGroups.ID, "=", String.valueOf(this.objGroupId), Database.ObjectsGroups.TABLE);
		if(arenaId == null)
			return false;
		
		if(ArenesModel.getArena((int)arenaId).getWorld() == null)
			return false;
		
		if(!ArenesModel.getArena((int)arenaId).getWorld().getName().equalsIgnoreCase(objectLocation.getWorld().getName()))
			return false;
		
		int objectId = Papi.MySQL.put(Map.of(Database.Objects.NAME, objectName,
				Database.Objects.OBJ_GROUPID, String.valueOf(objGroupId),
				Database.Objects.LOCATION, Papi.Function.getLocationToString(objectLocation, true, true)
				), Database.Objects.TABLE);
		
		if(objectId < 0)
			return false;
		
		arenaObjectsMap.put(objectId, new ModelArenaObject(objectId, objectName, objectLocation));
		return true;
	}
}

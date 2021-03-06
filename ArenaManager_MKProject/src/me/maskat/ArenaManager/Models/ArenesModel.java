package me.maskat.ArenaManager.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;

import me.maskat.ArenaManager.Database;
import me.maskat.ArenaManager.AdminMenu.ModelAdminMenu;
import me.maskat.ArenaManager.ArenaPlugin.ArenaPlugin;
import mkproject.maskat.Papi.Papi;

public class ArenesModel {
	private static Map<Player, ModelArenaPlayer> mapPlayers = new HashMap<>();
	
	public static ModelArenaPlayer getPlayer(Player player) {
		return mapPlayers.get(player);
	}
	
	public static ModelArena getModelArenaPlayed(Player player) {
		for(ModelArena modelArena : mapArenes.values())
		{
			if(modelArena.existArenaPluginInstanceArpPlayer(player))
				return modelArena;
		}
		return null;
	}
	
	public static Map<Player, ModelArenaPlayer> getPlayersMap() {
		//return new ArrayList<>(mapPlayers.keySet());
		return mapPlayers;
	}
	
	public static ModelArenaPlayer addPlayer(Player player) {
		return mapPlayers.put(player, new ModelArenaPlayer(player));
	}
	public static ModelArenaPlayer removePlayer(Player player) {
		return mapPlayers.remove(player);
	}

	public static boolean existPlayer(Player player) {
		return mapPlayers.containsKey(player);
	}
	
	private static Map<Player, ModelAdminMenu> mapAdmins = new HashMap<>();
	
	public static ModelAdminMenu getAdmin(Player player) {
		return mapAdmins.get(player);
	}
	
	public static Map<Player, ModelAdminMenu> getAdminsMap() {
		//return new ArrayList<>(mapAdmins.keySet());
		return mapAdmins;
	}
	
	public static ModelAdminMenu addAdmin(Player player) {
		return mapAdmins.put(player, new ModelAdminMenu(player));
	}
	public static ModelAdminMenu removeAdmin(Player player) {
		return mapAdmins.remove(player);
	}

	public static boolean existAdmin(Player player) {
		return mapAdmins.containsKey(player);
	}
	
	
	private static Map<Integer, ModelArena> mapArenes = new HashMap<>();
	
	public static ModelArena getArena(int arenaId) {
		return mapArenes.get(arenaId);
	}
	
	public static Map<Integer, ModelArena> getArenesMap() {
		return mapArenes;
	}
	
	public static void initArena(int arenaId, String arenaName, String arenaDescription, String arenaType, String arenaIconName, String arenaWorldName, boolean arenaEnabled) {
		if(arenaIconName == null)
			mapArenes.put(arenaId, new ModelArena(arenaId, arenaName, arenaDescription, arenaType, Material.OAK_PLANKS, (arenaWorldName==null ? null : Bukkit.getWorld(arenaWorldName)), arenaEnabled));
		else
			mapArenes.put(arenaId, new ModelArena(arenaId, arenaName, arenaDescription, arenaType, Material.getMaterial(arenaIconName.toUpperCase()), (arenaWorldName==null ? null : Bukkit.getWorld(arenaWorldName)), arenaEnabled));
	}
	
	public static boolean addArena(String arenaName) {
		int arenaId = Papi.MySQL.put(Map.of(Database.Arenes.NAME, arenaName), Database.Arenes.TABLE);
		if(arenaId < 0)
			return false;
		
		mapArenes.put(arenaId, new ModelArena(arenaId, arenaName, null, null, Material.OAK_PLANKS, null, false));
		return true;
	}
	public static boolean removeArena(int arenaId) {
		if(!existArena(arenaId) || !Papi.MySQL.deleteData(Database.Arenes.ID, "=", String.valueOf(arenaId), Database.Arenes.TABLE))
			return false;
		
		mapArenes.remove(arenaId);
		return true;
	}

	public static boolean existArena(int arenaId) {
		return mapArenes.containsKey(arenaId);
	}
	
	
	private static Map<String, ModelArenaType> mapArenesTypes = new HashMap<>();
	
	public static List<String> getArenesTypes() {
		return new ArrayList<>(mapArenesTypes.keySet());
	}
	public static ModelArenaType getArenaType(String arenaType) {
		return mapArenesTypes.get(arenaType);
	}
	public static ModelArenaType addArenaType(String arenaType, ArenaPlugin arenaPlugin, List<String> teamTypes, List<String> objGroupTypes) {
		return mapArenesTypes.put(arenaType, new ModelArenaType(arenaType, arenaPlugin, teamTypes, objGroupTypes));
	}
	public static ModelArenaType removeArenaType(String arenaType) {
		return mapArenesTypes.remove(arenaType);
	}
	public static boolean existArenaType(String arenaType) {
		return mapArenesTypes.containsKey(arenaType);
	}
	

	public static List<Integer> getArenesIdList() {
		return new ArrayList<>(mapArenes.keySet());
	}
	public static List<String> getArenesIdListString() {
		return Lists.transform(new ArrayList<>(mapArenes.keySet()), Functions.toStringFunction());
	}

	public static List<String> getTeamsIdListString(Integer arenaId) {
		if(arenaId == null || !existArena(arenaId))
			return new ArrayList<>();
		
		return getArena(arenaId).getTeamsIdListString();
	}

	public static List<String> getSpawnsIdListString(Integer arenaId, Integer teamId) {
		if(arenaId == null || teamId == null || !existArena(arenaId) || !getArena(arenaId).existTeam(teamId))
			return new ArrayList<>();
		
		return getArena(arenaId).getTeam(teamId).getSpawnsIdListString();
	}
}

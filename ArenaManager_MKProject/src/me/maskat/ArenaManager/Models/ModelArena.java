package me.maskat.ArenaManager.Models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;

import me.maskat.ArenaManager.Database;
import me.maskat.ArenaManager.Plugin;
import me.maskat.ArenaManager.ArenaManager.Manager;
import me.maskat.ArenaManager.ArenaPlugin.ArenaInstance;
import me.maskat.ArenaManager.ArenaPlugin.ArpArena;
import me.maskat.ArenaManager.ArenaPlugin.ArpPlayer;
import me.maskat.ArenaManager.ArenaPlugin.ArpTeam;
import mkproject.maskat.Papi.Papi;

public class ModelArena {

	private int arenaId;
	private String arenaName;
	private String arenaDescription;
	private String arenaType;
	private Material arenaIcon;
	private World arenaWorld;
	private boolean arenaEnabled;
	private boolean arenaAlreadyGamed;
	private boolean arenaGameStarted;
	private boolean arenaGameEnded;
	private int playedRound;
	private ArenaInstance arenaInstance;
	
	//teamId => ModelArenaTeam
	private Map<Integer, ModelArenaTeam> arenaTeamsMap = new HashMap<>();
	private Map<Integer, ModelArenaObjectsGroup> arenaObjectsGroupsMap = new HashMap<>();
	
	
	public ModelArena(int arenaId, String arenaName, String arenaDescription, String arenaType, Material arenaIcon, World arenaWorld, boolean arenaEnabled) {
		this.arenaId = arenaId;
		this.arenaName = arenaName;
		this.arenaDescription = arenaDescription;
		this.arenaType = arenaType;
		this.arenaIcon = arenaIcon;
		this.arenaWorld = arenaWorld;
		this.arenaEnabled = arenaEnabled;
		this.arenaAlreadyGamed = false;
		this.arenaGameStarted = false;
		this.arenaGameEnded = false;
		this.playedRound = 0;
	}

	public int getId() {
		return this.arenaId;
	}
	
	public String getName() {
		return this.arenaName;
	}
	
	public String getDescription() {
		return this.arenaDescription;
	}
	
	public String getType() {
		return this.arenaType;
	}
	
	public Material getIcon() {
		return this.arenaIcon;
	}
	
	public World getWorld() {
		return this.arenaWorld;
	}

	public boolean isEnabled() {
		return this.arenaEnabled;
	}
	
	public boolean isAlreadyGamed() {
		return this.arenaAlreadyGamed;
	}
	
	public boolean isGameStarted() {
		return this.arenaGameStarted;
	}
	public void setGameStarted(boolean gameStarted) {
		this.arenaGameStarted = gameStarted;
	}
	
	public boolean isGameEnded() {
		return this.arenaGameEnded;
	}
	public void setGameEnded(boolean arenaGameEnded) {
		this.arenaGameEnded = arenaGameEnded;
	}
	
	public boolean setName(String arenaNewName) {
		if(!Papi.MySQL.set(Map.of(Database.Arenes.NAME, arenaNewName), Database.Arenes.ID, "=", arenaId, Database.Arenes.TABLE))
			return false;
		
		this.arenaName = arenaNewName;
		return true;
	}	
	
	public boolean setDescription(String arenaNewDescription) {
		if(!Papi.MySQL.set(Map.of(Database.Arenes.DESCRIPTION, arenaNewDescription), Database.Arenes.ID, "=", arenaId, Database.Arenes.TABLE))
			return false;
		
		this.arenaDescription = arenaNewDescription;
		return true;
	}
	
	public boolean setType(String arenaNewType) {
		if(!ArenesModel.existArenaType(arenaNewType))
			return false;
		
		if(!Papi.MySQL.set(Map.of(Database.Arenes.TYPE, arenaNewType), Database.Arenes.ID, "=", arenaId, Database.Arenes.TABLE))
			return false;
		
		this.arenaType = arenaNewType;
		return true;
	}

	
	public boolean setIcon(String arenaNewIconName) {
		Material material = Material.getMaterial(arenaNewIconName.toUpperCase());
		if(material == null)
			return false;
		
		if(!Papi.MySQL.set(Map.of(Database.Arenes.ICON, material.name()), Database.Arenes.ID, "=", arenaId, Database.Arenes.TABLE))
			return false;
		
		this.arenaIcon = material;
		return true;
	}
	
	public boolean setWorld(String arenaNewWorldName) {
		World newWorld = Bukkit.getWorld(arenaNewWorldName);
		if(newWorld == null)
			return false;
		
		if(!Papi.MySQL.set(Map.of(Database.Arenes.WORLD, newWorld.getName()), Database.Arenes.ID, "=", arenaId, Database.Arenes.TABLE))
			return false;
		
		this.arenaWorld = newWorld;
		return true;
	}
	
	public boolean setEnabled(boolean arenaNewEnabled) {
		if(!Papi.MySQL.set(Map.of(Database.Arenes.ENABLED, arenaNewEnabled), Database.Arenes.ID, "=", arenaId, Database.Arenes.TABLE))
			return false;
		
		this.arenaEnabled = arenaNewEnabled;
		return true;
	}

	public boolean Team() {
		return arenaType == null ? false : true;
	}

	public boolean existTeam(int teamId) {
		return arenaTeamsMap.containsKey(teamId);
	}
	
	public void initTeam(int teamId, String teamName, int teamMaxPlayers, String teamType, String teamIconName) {
		if(teamIconName == null)
			arenaTeamsMap.put(teamId, new ModelArenaTeam(teamId, teamName, teamMaxPlayers, teamType, Material.CARVED_PUMPKIN));
		else
			arenaTeamsMap.put(teamId, new ModelArenaTeam(teamId, teamName, teamMaxPlayers, teamType, Material.getMaterial(teamIconName.toUpperCase())));
	}
	
	public boolean addTeam(String teamName) {
		int teamId = Papi.MySQL.put(Map.of(
				Database.Teams.NAME, teamName,
				Database.Teams.ARENAID, String.valueOf(arenaId),
				Database.Teams.MAX_PLAYERS, "0",
				Database.Teams.ICON, Material.CARVED_PUMPKIN.name()
				), Database.Teams.TABLE);
		
		if(teamId < 0)
			return false;
		
		arenaTeamsMap.put(teamId, new ModelArenaTeam(teamId, teamName, 0, null, Material.CARVED_PUMPKIN));
		return true;
	}
	
	public ModelArenaTeam getTeam(int teamId) {
		return arenaTeamsMap.get(teamId);
	}
	
	public boolean removeTeam(int teamId) {
		if(!existTeam(teamId) || !Papi.MySQL.deleteData(Database.Teams.ID, "=", String.valueOf(teamId), Database.Teams.TABLE))
			return false;
		
		arenaTeamsMap.remove(teamId);
		return true;
	}
	
	public List<Integer> getTeamsIdList() {
		return new ArrayList<>(arenaTeamsMap.keySet());
	}
	public List<String> getTeamsIdListString() {
		return Lists.transform(new ArrayList<>(arenaTeamsMap.keySet()), Functions.toStringFunction());
	}

	public Map<Integer, ModelArenaTeam> getTeamsMap() {
		return arenaTeamsMap;
	}
	
	public Map<Integer, ModelArenaTeam> getTeams(int pageInt, int pageLimit) {
		Map<Integer, ModelArenaTeam> arenaTeamsMapTemp = new HashMap<>();
		int i=0;
		for(Map.Entry<Integer, ModelArenaTeam> entry : arenaTeamsMap.entrySet()) {
			if(i >= ((pageInt*pageLimit)-pageLimit))
				arenaTeamsMapTemp.put(entry.getKey(), entry.getValue());
			if(i >= (pageInt*pageLimit))
				return arenaTeamsMapTemp;
			i++;
		}
		return arenaTeamsMapTemp;
	}

	public boolean existObjectsGroup(int objGroupId) {
		return arenaObjectsGroupsMap.containsKey(objGroupId);
	}
	
	public void initObjectsGroup(int objGroupId, String objGroupName, String objGroupType) {
		arenaObjectsGroupsMap.put(objGroupId, new ModelArenaObjectsGroup(objGroupId, objGroupName, objGroupType));
	}
	
	public boolean addObjectsGroup(String objGroupName) {
		int objGroupId = Papi.MySQL.put(Map.of(Database.ObjectsGroups.NAME, objGroupName,
				Database.ObjectsGroups.ARENAID, String.valueOf(arenaId)
				), Database.ObjectsGroups.TABLE);
		
		if(objGroupId < 0)
			return false;
		
		arenaObjectsGroupsMap.put(objGroupId, new ModelArenaObjectsGroup(objGroupId, objGroupName, null));
		return true;
	}
	
	public ModelArenaObjectsGroup getObjectsGroup(int objGroupId) {
		return arenaObjectsGroupsMap.get(objGroupId);
	}
	
	public boolean removeObjectsGroup(int objGroupId) {
		if(!existObjectsGroup(objGroupId) || !Papi.MySQL.deleteData(Database.ObjectsGroups.ID, "=", String.valueOf(objGroupId), Database.ObjectsGroups.TABLE))
			return false;
		
		arenaObjectsGroupsMap.remove(objGroupId);
		return true;
	}
	
	public List<Integer> getObjectsGroupsIdList() {
		return new ArrayList<>(arenaObjectsGroupsMap.keySet());
	}
	public List<String> getObjectsGroupsIdListString() {
		return Lists.transform(new ArrayList<>(arenaObjectsGroupsMap.keySet()), Functions.toStringFunction());
	}

	public Map<Integer, ModelArenaObjectsGroup> getObjectsGroupsMap() {
		return arenaObjectsGroupsMap;
	}
	
	public Map<Integer, ModelArenaObjectsGroup> getObjectsGroups(int pageInt, int pageLimit) {
		Map<Integer, ModelArenaObjectsGroup> arenaObjectsGroupMapTemp = new HashMap<>();
		int i=0;
		for(Map.Entry<Integer, ModelArenaObjectsGroup> entry : arenaObjectsGroupsMap.entrySet()) {
			if(i >= ((pageInt*pageLimit)-pageLimit))
				arenaObjectsGroupMapTemp.put(entry.getKey(), entry.getValue());
			if(i >= (pageInt*pageLimit))
				return arenaObjectsGroupMapTemp;
			i++;
		}
		return arenaObjectsGroupMapTemp;
	}

	public int getMaxPlayers() {
		int arenaMaxPlayers = 0;
		for(ModelArenaTeam modelArenaTeam : this.getTeamsMap().values()) {
			arenaMaxPlayers += modelArenaTeam.getMaxPlayers();
		}
		return arenaMaxPlayers;
	}

	public List<ModelArenaPlayer> getPlayersRegistered() {
		List<ModelArenaPlayer> modelPlayersList = new ArrayList<>();
		for(ModelArenaPlayer modelPlayer : ArenesModel.getPlayersMap().values()) {
			if(modelPlayer.getRegisteredArenaId() == this.arenaId)
				modelPlayersList.add(modelPlayer);
		}
		return modelPlayersList;
	}

	public boolean registerPlayer(Player player) {
		if(this.getPlayersRegistered().size() >= this.getMaxPlayers())
			return false;
		
		if(ArenesModel.getPlayer(player).getRegisteredArenaId() == this.getId())
			return false;
		
		int teamId = 0;
		for(ModelArenaTeam modelTeam : this.getTeamsMap().values()) {
			if(modelTeam.getPlayersRegistered().size() < modelTeam.getMaxPlayers())
				teamId = modelTeam.getId();
		}
		if(teamId == 0)
			return false;
		ArenesModel.getPlayer(player).setRegisteredTeamId(arenaId, teamId);
		return true;
	}
	
	public boolean existType() {
		return arenaType == null ? false : true;
	}
	
	public boolean existTeamType(String teamType) {
		if(this.arenaType == null)
			return false;
		
		if(!ArenesModel.existArenaType(arenaType))
			return false;
		
		if(ArenesModel.getArenaType(arenaType).existTeamType(teamType))
			return true;
		
		return false;
	}
	
	public boolean existObjectsGroupType(String objGroupType) {
		if(this.arenaType == null)
			return false;
		
		if(!ArenesModel.existArenaType(arenaType))
			return false;
		
		if(ArenesModel.getArenaType(arenaType).existObjectsGroupType(objGroupType))
			return true;
		
		return false;
	}

	public void doPrepareArena() {
		this.arenaAlreadyGamed = true;
		this.arenaGameEnded = false;
		Manager.prepareArena(this);
	}

	public void doPreparePlayers() {
		Manager.preparePlayersTask(this, 10);
	}
	
	private boolean doPreparePlayersFromAsync = false;

	public void doPreparePlayersFromAsync() {
		this.doPreparePlayersFromAsync = true;
	}

	public void runTaskPreparePlayersFromAsync() {
		this.doPreparePlayersFromAsync = false;
		taskPreparePlayersFromAsync();
	}
	
	private void taskPreparePlayersFromAsync() {
        Plugin.getPlugin().getServer().getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
            @Override
            public void run() {
            	if(doPreparePlayersFromAsync)
            		doPreparePlayers();
            	else
            		taskPreparePlayersFromAsync();
            }
        }, 20L);
	}

//	public void doEndArenaClear() {
//		for(ModelArenaPlayer modelArenaPlayer : this.get) {
//    		modelArenaPlayer.setFreeze(false);
//    		modelArenaPlayer.setGodMode(false);
//		}
//		
////		this.unsetPlayersPlayedInside();
//		this.arenaAlreadyGamed = false;
//		this.arenaGameEnded = false;
//		this.playedRound = 0;
//	}

	public void nextPlayRound() {
		this.playedRound++;
	}
	public int getPlayedRound() {
		return this.playedRound;
	}
	
	ArpArena arpArena = null;
	Map<String, ArpTeam> arpTeams = null;
	Map<Player, ArpPlayer> arpPlayers = null;
	
	public void setArenaPluginNewInstance() {
		this.arenaInstance = ArenesModel.getArenaType(this.getType()).getArenaPlugin().getNewInstance();
		this.arpArena = new ArpArena(this);
		this.arpTeams = new HashMap<>();
		this.arpPlayers = new HashMap<>();
		for(Entry<Integer, ModelArenaTeam> arenaTeam : this.arenaTeamsMap.entrySet()) {
			if(arenaTeam.getValue().getType() != null && arenaTeam.getValue().getType().length() > 0)
			{
				ArpTeam arpTeam = new ArpTeam(arenaTeam.getValue(), this);
				this.arpTeams.put(arenaTeam.getValue().getType(),arpTeam);
				for(ArpPlayer arpPlayer : arpTeam.getPlayers())
					this.arpPlayers.put(arpPlayer.getPlayer(), arpPlayer);
			}
		}
	}
	
	public ArenaInstance getArenaPluginInstance() {
		return this.arenaInstance;
	}
	
	
	public ArpArena getArenaPluginInstanceArpArena() {
		return this.arpArena;
	}
	
	public Collection<ArpTeam> getArenaPluginInstanceArpTeams() {
		return this.arpTeams.values();
	}
	
	public Collection<ArpPlayer> getArenaPluginInstanceArpPlayers() {
		return this.arpPlayers.values();
	}
	
	public ArpTeam getArenaPluginInstanceArpTeam(String teamType) {
		return this.arpTeams.get(teamType);
	}

	public Collection<ArpPlayer> getArenaPluginInstanceArpPlayersOnline() {
		Collection<ArpPlayer> arpPlayersOnline = new ArrayList<>();
		for(ArpPlayer arpPlayer : this.arpPlayers.values()) {
			if(arpPlayer.getPlayer().isOnline() && !arpPlayer.isQuitedServer())
				arpPlayersOnline.add(arpPlayer);
		}
		return arpPlayersOnline;
	}

	public boolean existArenaPluginInstanceArpPlayer(Player player) {
		if(arpPlayers == null)
			return false;
		return this.arpPlayers.containsKey(player);
	}
	
	public ArpPlayer getArenaPluginInstanceArpPlayer(Player player) {
		return this.arpPlayers.get(player);
	}

	public void doClearArenaGame() {
		this.arenaInstance = null;
		this.arpArena = null;
		this.arpTeams = null;
		this.arpPlayers = null;
		this.arenaAlreadyGamed = false;
		this.arenaGameStarted = false;
	}
}

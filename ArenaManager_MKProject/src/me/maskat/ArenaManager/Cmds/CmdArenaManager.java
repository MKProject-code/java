package me.maskat.ArenaManager.Cmds;

import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.maskat.ArenaManager.Models.ArenesModel;
import me.maskat.ArenaManager.Models.ModelArena;
import mkproject.maskat.Papi.Utils.CommandManager;

public class CmdArenaManager implements CommandExecutor, TabCompleter {

	private void registerArgAliases(CommandManager manager) {
		manager.registerArgAliasAuto(1, "list", 1);
		manager.registerArgAliasAuto(1, "info", 1);
		manager.registerArgAliasAuto(1, "create", 1);
		manager.registerArgAliasAuto(1, "rename", 3);
		manager.registerArgAliasAuto(1, "remove", 3);
		manager.registerArgAliasAuto(1, "team", 1);
		manager.registerArgAliasAuto(1, "spawn", 1);
		manager.registerArgAliasAuto(1, "menu", 1);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager("mkp.arenamanager",sender, command, label, args);
		
		this.registerArgAliases(manager);
		
		manager.registerArgTabComplete(0, List.of("list","info","create","rename","remove","team","spawn","menu"));
		
		manager.registerArgTabComplete(1, List.of("info","rename","remove"), ArenesModel.getArenesIdListString());
		manager.registerArgTabComplete(1, List.of("team"), List.of("add","del","rename"));
		manager.registerArgTabComplete(1, List.of("spawn"), List.of("add","del"));
		
		manager.registerArgTabComplete(2, List.of("team"), List.of("add","del","rename"), ArenesModel.getArenesIdListString());
		manager.registerArgTabComplete(2, List.of("spawn"), List.of("add","del"), ArenesModel.getArenesIdListString());
		
		manager.registerArgTabComplete(3, List.of("team"), List.of("del","rename"), ArenesModel.getArenesIdListString(), ArenesModel.getTeamsIdListString(manager.getIntArg(3)));
		manager.registerArgTabComplete(3, List.of("spawn"), List.of("add","del"), ArenesModel.getArenesIdListString(), ArenesModel.getTeamsIdListString(manager.getIntArg(3)));
		
		manager.registerArgTabComplete(4, List.of("spawn"), List.of("del"), ArenesModel.getArenesIdListString(), ArenesModel.getTeamsIdListString(manager.getIntArg(3)), ArenesModel.getSpawnsIdListString(manager.getIntArg(3), manager.getIntArg(4)));
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager("mkp.arenamanager",sender, command, label, args, List.of("list | info | create | rename | remove | team | spawn | menu"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		this.registerArgAliases(manager);
		
		if(manager.registerArgUsage(1, "menu") && manager.hasArgs(1))
			arenesMenu(manager);
		else if(manager.registerArgUsage(1, "list") && manager.hasArgs(1))
			arenesList(manager);
		else if(manager.registerArgUsage(1, "info", "<arenaId>") && manager.hasArgs(2))// && manager.isNumericArg(2))
			arenaInfo(manager, manager.getIntArg(2));
		
		else if(manager.registerArgUsage(1, "create", "<arenaName>") && manager.hasArgs(2))
			arenaCreate(manager, manager.getArg(2));
		
		else if(manager.registerArgUsage(1, "remove", "<arenaId>") && manager.hasArgs(2))// && manager.isNumericArg(2))
			arenaRemove(manager, manager.getIntArg(2));
		
		else if(manager.registerArgUsage(1, "rename", "<arenaId>", "<arenaNewName>") && manager.hasArgs(3))// && manager.isNumericArg(2))
			arenaRename(manager, manager.getIntArg(2), manager.getArg(3));
		
		else if(manager.registerArgUsage(1, "team", "add|del|rename", "<arenaId>", "<teamName>"))
		{
			if(manager.registerArgUsage(2, "team", "add", "<arenaId>", "<teamName>") && manager.hasArgs(4))// && manager.isNumericArg(3))
				arenaAddTeam(manager, manager.getIntArg(3), manager.getArg(4));
			
			else if(manager.registerArgUsage(2, "team", "del", "<arenaId>", "<teamId>") && manager.hasArgs(4))// && manager.isNumericArg(3,4))
				arenaDelTeam(manager, manager.getIntArg(3), manager.getIntArg(4));
			
			else if(manager.registerArgUsage(2, "team", "rename", "<arenaId>", "<teamId>", "<teamNewName>") && manager.hasArgs(5))// && manager.isNumericArg(3,4))
				arenaRenameTeam(manager, manager.getIntArg(3), manager.getIntArg(4), manager.getArg(5));
		}
		else if(manager.registerArgUsage(1, "spawn", "add|del", "<arenaId>", "<teamId>", "<spawnId>"))
		{
			if(manager.registerArgUsage(2, "spawn", "add", "<arenaId>", "<teamId>", "<spawnName>") && manager.hasArgs(5))// && manager.isNumericArg(3,4,5))
				arenaAddSpawn(manager, manager.getIntArg(3), manager.getIntArg(4), manager.getArg(5));
			
			else if(manager.registerArgUsage(2, "spawn", "del", "<arenaId>", "<teamId>", "<spawnId>") && manager.hasArgs(5))// && manager.isNumericArg(3,4,5))
				arenaDelSpawn(manager, manager.getIntArg(3), manager.getIntArg(4), manager.getIntArg(5));
		}
		
		return manager.doReturn();
	}
	
	private void arenesMenu(CommandManager manager) {
		ArenesModel.addAdmin(manager.getPlayer());
		ArenesModel.getAdmin(manager.getPlayer()).openMenuMain();
		manager.setReturnMessage(null);
	}
	
	
	private void arenaCreate(CommandManager manager, String arenaName) {
		if(arenaName == null) return;
		
		if(!ArenesModel.addArena(arenaName))
			manager.setReturnMessage("&c&oNie udało się utworzyć takiej areny");
		else
			manager.setReturnMessage("&a&oUtworzyłeś arene &e&o"+arenaName);
	}
	
	private void arenaRemove(CommandManager manager, Integer arenaId) {
		if(arenaId == null) return;
		
		if(!ArenesModel.existArena(arenaId))
			manager.setReturnMessage("&c&oTaka arena nie istnieje!");
		else if(!ArenesModel.removeArena(arenaId))
			manager.setReturnMessage("&c&oNie udało się usunąć tej areny");
		else
			manager.setReturnMessage("&a&oUsunąłeś arene &e&o"+arenaId);
	}

	private void arenesList(CommandManager manager) {
		String arenesList = "";
		for(Map.Entry<Integer, ModelArena> entry : ArenesModel.getArenesMap().entrySet()) {
			arenesList += "\n&d"+entry.getKey()+". &b"+entry.getValue().getName()+" - Teams: "+entry.getValue().getTeamsIdList();
		}
		if(arenesList=="")
			manager.setReturnMessage("&c&oBrak aren");
		else
			manager.setReturnMessage("&a&oLista aren:"+arenesList);
	}
	
	private void arenaInfo(CommandManager manager, Integer arenaId) {
		if(arenaId == null) return;
		
		if(!ArenesModel.existArena(arenaId))
			manager.setReturnMessage("&c&oTaka arena nie istnieje!");
		else
		{
			String arenaInfo = "\n&bName: &e"+ArenesModel.getArena(arenaId).getName();
			arenaInfo += "\n&bTeams: &d"+ArenesModel.getArena(arenaId).getTeamsIdList();
			for(int teamId : ArenesModel.getArena(arenaId).getTeamsIdList()) {
				arenaInfo += "\n&bTeam &d"+teamId+"&b (&e"+ArenesModel.getArena(arenaId).getTeam(teamId).getName()+"&b) - Spawns: &c"+ArenesModel.getSpawnsIdListString(arenaId, teamId);
			}
			manager.setReturnMessage("&a&oInformacje o arenie &e&o"+arenaId+"&a&o:"+arenaInfo);
		}
	}
	
	private void arenaRename(CommandManager manager, Integer arenaId, String arenaNewName) {
		if(arenaId == null || arenaNewName == null) return;
		
		if(!ArenesModel.existArena(arenaId))
			manager.setReturnMessage("&c&oTaka arena nie istnieje!");
		else if(!ArenesModel.getArena(arenaId).setName(arenaNewName))
			manager.setReturnMessage("&c&oNie udało się zmienić nazwy tej areny");
		else
			manager.setReturnMessage("&a&oZmieniłeś nazwę areny &e&o"+ arenaId +"&a&o na &e&o"+ArenesModel.getArena(arenaId).getName());
	}
	
	private void arenaAddTeam(CommandManager manager, Integer arenaId, String teamName) {
		if(arenaId == null || teamName == null) return;
		
		if(!ArenesModel.existArena(arenaId))
			manager.setReturnMessage("&c&oTaka arena nie istnieje!");
		else if(!ArenesModel.getArena(arenaId).addTeam(teamName))
			manager.setReturnMessage("&c&oNie udało się utworzyć takiego teamu");
		else
			manager.setReturnMessage("&a&oDodałeś team dla areny &e&o"+ arenaId +"&a&o - &e&o"+ArenesModel.getArena(arenaId).getName());
	}	
	
	private void arenaDelTeam(CommandManager manager, Integer arenaId, Integer teamId) {
		if(arenaId == null || teamId == null) return;
		
		if(!ArenesModel.existArena(arenaId))
			manager.setReturnMessage("&c&oTaka arena nie istnieje!");
		else if(!ArenesModel.getArena(arenaId).existTeam(teamId))
			manager.setReturnMessage("&c&oTaki team nie istnieje dla areny &e&o"+ arenaId +"&c&o!");
		else if(!ArenesModel.getArena(arenaId).removeTeam(teamId))
			manager.setReturnMessage("&c&oNie udało się usunąć tego teamu");
		else
			manager.setReturnMessage("&a&oUsunąłeś team &e&o"+teamId+"&a&o w arenie &e&o"+ arenaId +"&a&o - &e&o"+ArenesModel.getArena(arenaId).getName());
	}
	private void arenaRenameTeam(CommandManager manager, Integer arenaId, Integer teamId, String teamNewName) {
		if(arenaId == null || teamId == null || teamNewName == null) return;
		
		if(!ArenesModel.existArena(arenaId))
			manager.setReturnMessage("&c&oTaka arena nie istnieje!");
		else if(!ArenesModel.getArena(arenaId).existTeam(teamId))
			manager.setReturnMessage("&c&oTaki team nie istnieje dla areny &e&o"+ arenaId +"&c&o!");
		else if(!ArenesModel.getArena(arenaId).getTeam(teamId).setName(teamNewName))
			manager.setReturnMessage("&c&oNie udało się zmienić nazwy tego teamu");
		else
			manager.setReturnMessage("&a&oZmieniłeś nazwę teamu &e&o"+teamId+"&a&o na &e&o"+ArenesModel.getArena(arenaId).getTeam(teamId).getName()+"&a&o w arenie &e&o"+ arenaId +"&a&o - &e&o"+ArenesModel.getArena(arenaId).getName());
	}
	
	private void arenaAddSpawn(CommandManager manager, Integer arenaId, Integer teamId, String spawnName) {
		if(arenaId == null || teamId == null) return;
		
		if(!ArenesModel.existArena(arenaId))
			manager.setReturnMessage("&c&oTaka arena nie istnieje!");
		else if(!ArenesModel.getArena(arenaId).existTeam(teamId))
			manager.setReturnMessage("&c&oTaki team nie istnieje dla areny &e&o"+ arenaId +"&c&o!");
		else if(!ArenesModel.getArena(arenaId).getTeam(teamId).addSpawn(spawnName, manager.getPlayer().getLocation()))
			manager.setReturnMessage("&c&oNie udało się dodać takiego spawnu");
		else
			manager.setReturnMessage("&a&oDodałeś spawn dla teamu &e&o"+ teamId +"&a&o w arenie &e&o"+ arenaId +"&a&o - &e&o"+ArenesModel.getArena(arenaId).getName());
	}	
	
	private void arenaDelSpawn(CommandManager manager, Integer arenaId, Integer teamId, Integer spawnId) {
		if(arenaId == null || teamId == null || spawnId == null) return;
		
		if(!ArenesModel.existArena(arenaId))
			manager.setReturnMessage("&c&oTaka arena nie istnieje!");
		else if(!ArenesModel.getArena(arenaId).existTeam(teamId))
			manager.setReturnMessage("&c&oTaki team nie istnieje dla areny &e&o"+ arenaId +"&c&o!");
		else if(!ArenesModel.getArena(arenaId).getTeam(teamId).existSpawn(spawnId))
			manager.setReturnMessage("&c&oTaki spawn nie istnieje dla teamu &e&o"+teamId+"&a&o w arenie &e&o"+ arenaId +"&c&o!");
		else if(!ArenesModel.getArena(arenaId).getTeam(teamId).removeSpawn(spawnId))
			manager.setReturnMessage("&c&oNie udało się usunąć tego spawnu");
		else
			manager.setReturnMessage("&a&oUsunąłeś spawn &e&o"+spawnId+"&a&o dla teamu &e&o"+ teamId +"&a&o w arenie &e&o"+ arenaId +"&a&o - &e&o"+ArenesModel.getArena(arenaId).getName());
	}
}

package mkproject.maskat.AdminUtils.Cmds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import mkproject.maskat.AdminUtils.Config;
import mkproject.maskat.AdminUtils.Config.ConfigKey;
import mkproject.maskat.Papi.Utils.CommandManager;


public class CmdNear implements CommandExecutor {

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args, List.of("[player]", "[range]"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
			return manager.doReturn();
		
		if(manager.hasArgs(0)) // /near [player] [range]
			this.doShowNear(manager, manager.getPlayer(), 500);
		else if(manager.hasArgs(1)) // /near [player] [range]
		{
			if(manager.isNumericArg(1))
				this.doShowNear(manager, manager.getPlayer(), manager.getIntArg(1));
			else
				this.doShowNear(manager, manager.getChosenPlayerFromArg(1, true), 500);
		}
		else if(manager.hasArgs(2))// /near [player] [range]
			this.doShowNear(manager, manager.getChosenPlayerFromArg(1, true), manager.getIntArg(2));
		
		return manager.doReturn();
	}
	
	// --------- /near [player]
	public void doShowNear(CommandManager manager, Player destPlayer, int range) {
		if(destPlayer == null)
			return;
//		Map<EntityType,Integer> entitiesMap = new HashMap<>();
//		
//		for(Entity entity : destPlayer.getNearbyEntities(range, range, range)) {
//			
//			Integer value = 1;
//			
//			if(entitiesMap.containsKey(entity.getType()))
//				value += entitiesMap.get(entity.getType());
//			
//			entitiesMap.put(entity.getType(), value);
//		}
//		String str = "";
//		for(Entry<EntityType, Integer> entityType : entitiesMap.entrySet()) {
//			str += "\n&6"+entityType.getKey()+"&7: &e"+entityType.getValue();
//		}
		
		Map<EntityType,List<Entity>> entitiesMap = new HashMap<>();
		
		for(Entity entity : destPlayer.getNearbyEntities(range, range, range)) {
			if(entitiesMap.containsKey(entity.getType()))
				entitiesMap.get(entity.getType()).add(entity);
			else {
				entitiesMap.put(entity.getType(), new ArrayList<>());
				entitiesMap.get(entity.getType()).add(entity);
			}
		}
		String str = "";
		for(Entry<EntityType, List<Entity>> entityType : entitiesMap.entrySet()) {
			String names = "";
			for(Entity entity : entityType.getValue()) {
				if(!entity.getType().name().equalsIgnoreCase(entity.getName())) {
					if(entity.getCustomName() == null || entity.getName().equals(entity.getCustomName()))
						names += "&e"+entity.getName()+"&7, ";
					else
						names += "&e"+entity.getName()+" &7("+entity.getCustomName()+"), ";
				}
			}
			str += "\n&d"+entityType.getKey()+"&7: &6"+entityType.getValue().size() + (names.length() >= 2 ? "&7 - "+names.substring(0, names.length()-2) : "");
		}
		
		manager.setReturnMessage(destPlayer,
				"&a&oWyświetlam liste bytów znajdujących się w zasięgu &e&o"+range+"&a&o kratek..."+str,
				"&a&oWyświetlam liste bytów znajdujących się w zasięgu &e&o"+range+"&a&o kratek od gracza &e&o"+destPlayer.getName()+"&a&o..."+str);
	}
}

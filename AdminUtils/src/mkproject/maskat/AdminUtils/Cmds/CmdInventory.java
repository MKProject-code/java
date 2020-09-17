package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CmdInventory implements CommandExecutor, TabCompleter {

	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager_local manager = new CommandManager_local(sender, command, label, args);
		
		manager.registerArgTabComplete(0, List.of("see","get","set","clear"));
		manager.registerArgTabComplete(1, List.of("see","get","set","clear"), manager.getOnlinePlayersNameList());
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager_local manager = new CommandManager_local(sender, command, label, args, List.of("see|get|set|clear", "<[player]>"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		manager.registerArgUsage(1, "see","<player>");
		manager.registerArgUsage(1, "get","<player>");
		manager.registerArgUsage(1, "set","<player>");
		manager.registerArgUsage(1, "clear","[player]");
		
		if(manager.hasArgs(1,2))
		{
			if(manager.hasArgAndPermission(1, "see") && manager.hasArgs(2))
			{
				this.inventorySee(manager, manager.getChosenPlayerFromArg(2, false));
				return manager.doReturn();
			}
			else if(manager.hasArgAndPermission(1, "get") && manager.hasArgs(2))
			{
				this.inventoryGet(manager, manager.getChosenPlayerFromArg(2, false));
				return manager.doReturn();
			}
			else if(manager.hasArgAndPermission(1, "set") && manager.hasArgs(2))
			{
				this.inventorySet(manager, manager.getChosenPlayerFromArg(2, false));
				return manager.doReturn();
			}
			else if(manager.hasArgAndPermission(1, "clear") && manager.hasArgs(1,2))
			{
				this.inventoryClear(manager, manager.getChosenPlayerFromArg(2, true));
				return manager.doReturn();
			}
//			else if(manager.hasArgAndPermission(1, "clear") && manager.hasArgs("clear <player>", 2))
//			{
//				this.inventoryClear(manager.getChosenPlayerFromArg(2, true));
//				return manager.doReturn();
//			}
		}
		return manager.doReturn();
	}
	
	// --------- /inventory see <player>
	public void inventorySee(CommandManager_local manager, Player destPlayer) {
		if(destPlayer == null)
			return;
		
		manager.getPlayer().openInventory(destPlayer.getInventory());
		
		manager.setReturnMessage(null);
	}
	
	// --------- /inventory get <player>
	public void inventoryGet(CommandManager_local manager, Player destPlayer) {
		if(destPlayer == null)
			return;
		
		manager.getPlayer().getInventory().setContents(destPlayer.getInventory().getContents());
		manager.getPlayer().getInventory().setArmorContents(destPlayer.getInventory().getArmorContents());
		
		manager.getPlayer().updateInventory();
//	    
		manager.setReturnMessage("&a&oZaładowałeś sobie ekwipunek gracza &e&o"+destPlayer.getName());
	}
	
	// --------- /inventory set <player>
	public void inventorySet(CommandManager_local manager, Player destPlayer) {
		if(destPlayer == null)
			return;
		
		destPlayer.getInventory().setContents(manager.getPlayer().getInventory().getContents());
		destPlayer.getInventory().setArmorContents(manager.getPlayer().getInventory().getArmorContents());
		
		destPlayer.updateInventory();
	    
		manager.setReturnMessage("&a&oZaładowałeś graczowi &e&o"+destPlayer.getName()+"&a&o swój ekwipunek");
	}
	
	// --------- /inventory clear [player]
	public void inventoryClear(CommandManager_local manager, Player destPlayer) {
		if(destPlayer == null)
			return;
		
		destPlayer.getInventory().clear();
		
		destPlayer.updateInventory();
	    
		manager.setReturnMessage(destPlayer,
				"&a&oWyczyściłeś swój ekwipunek",
				"&a&oWyczyściłeś ekwipunek graczowi &e&o"+destPlayer.getName());
	}
}

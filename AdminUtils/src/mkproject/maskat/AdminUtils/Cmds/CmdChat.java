package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import mkproject.maskat.AdminUtils.Plugin;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.CommandManager;
import mkproject.maskat.Papi.Utils.Message;


public class CmdChat implements CommandExecutor, TabCompleter {
	private void registerArgAliases(CommandManager manager) {
		manager.registerArgAliasAuto(1, "clear", 1);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args);
		
		this.registerArgAliases(manager);
		
		manager.registerArgTabComplete(0, List.of("clear"));
		
		return manager.getTabComplete();
	}
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args, List.of("clear"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
			return manager.doReturn();
		
		this.registerArgAliases(manager);
		
		if(manager.hasArgs(1))
		{
			if(manager.hasArgAndPermission(1, "clear"))// /chat clear
				this.doChatClear(manager);
		}
		
		return manager.doReturn();
	}
	
	// --------- /chat clear
	public void doChatClear(CommandManager manager) {
		for(Player player: Bukkit.getOnlinePlayers()) {
			for(int i=0;i<100;i++)
				player.sendMessage(" ");
			Message.sendMessage(player, "&7Chat został wyczyszczony przez "+Papi.Model.getPlayer(manager.getPlayer()).getNameWithPrefix()+"&7.");
		}
		
		//manager.setReturnMessage("&a&oWyczyściłeś chat wszystkim graczą");
		manager.setReturnMessage(null);
	}
}

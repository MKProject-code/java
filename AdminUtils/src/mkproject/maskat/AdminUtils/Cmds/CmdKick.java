package mkproject.maskat.AdminUtils.Cmds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import mkproject.maskat.AdminUtils.Config;
import mkproject.maskat.AdminUtils.Config.ConfigKey;
import mkproject.maskat.AdminUtils.Database;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.CommandManager;
import mkproject.maskat.Papi.Utils.Message;

public class CmdKick implements CommandExecutor, TabCompleter {

//	private void registerArgAliases(CommandManager manager) {
//		manager.registerArgAliasAuto(3, "seconds", 1);
//		manager.registerArgAliasAuto(3, "minutes", 1);
//		manager.registerArgAliasAuto(3, "hours", 1);
//		manager.registerArgAliasAuto(3, "days", 1);
//	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args);
		
//		this.registerArgAliases(manager);
		
		List<String> players = manager.getOnlinePlayersCanChooseNameList(false, true);
		players.add("*");
		
		manager.registerArgTabComplete(0, players);
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args, List.of("<player>", "[reason]"));
		
		manager.registerArgUsage(1, null,"[reason]");
		
		if(manager.isPlayer())
			if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
				return manager.doReturn();
		
//		this.registerArgAliases(manager);
		
		if(manager.hasArgStart(2)) {
			if(manager.hasArgAndPermission(1, "*", "all"))
				this.kickall(manager, manager.getStringArgStart(2));
			else
				this.kick(manager, manager.getChosenPlayerFromArg(1, false, true), manager.getStringArgStart(2));
		}
		return manager.doReturn();
	}
	
	// --------- /kick <player> [reason]
	public void kick(CommandManager manager, Player destPlayer, String reason) {
		if(destPlayer == null)
			return;
		
		if(reason == null)
			reason = "";
		if(Database.Kicks.addKick(destPlayer, manager.getPlayer(), reason) > 0)
		{
			String admin = manager.isPlayer() ? Papi.Model.getPlayer(manager.getPlayer()).getNameWithPrefix() : "serwer";
			Message.sendBroadcast(Papi.Model.getPlayer(destPlayer).getNameWithPrefix()+" &7został wyrzucony przez "+admin+"&7."+((reason!="") ? ("\nPowód: &6"+reason) : ""));
			destPlayer.kickPlayer(Message.getColorMessage("&cZostałeś wyrzucony!"+((reason!="") ? ("\n&6Powód: &e"+reason) : "")));
			manager.setReturnMessage(null);
		}
		else
			manager.setReturnMessage("&c&oWystąpił błąd podczas próby wyrzucenia gracza &e&o"+destPlayer.getName());
			
	}
	
	// --------- /kick * [reason]
	public void kickall(CommandManager manager, String reason) {
		if(reason == null)
			reason = "";
		
		Collection<Player> playersOnlines = new ArrayList<>();
		
		Map<String, String> playersOnlineNamesIPs = new HashMap<>();
		
		for(Player playerOnline : Bukkit.getOnlinePlayers()) {
			
			if(playerOnline != manager.getPlayer())
			{
				playersOnlines.add(playerOnline);
				playersOnlineNamesIPs.put(playerOnline.getName(), Papi.Model.getPlayer(playerOnline).getAddressIP());
			}
		}
		
		if(Database.Kicks.addKickAll(playersOnlineNamesIPs, manager.getPlayer(), reason) > 0)
		{
			String admin = manager.isPlayer() ? Papi.Model.getPlayer(manager.getPlayer()).getNameWithPrefix() : "serwer";
			Message.sendBroadcast("&7Wszyscy gracze zostali wyrzuceni przez "+admin+"&7."+((reason!="") ? ("\nPowód: &6"+reason) : ""));
			for(Player playerOnline : playersOnlines) {
				playerOnline.kickPlayer(Message.getColorMessage((reason!="") ? ("&c"+reason) : "&cZostałeś wyrzucony!"));
			}
			manager.setReturnMessage(null);
		}
		else
			manager.setReturnMessage("&c&oWystąpił błąd podczas próby wyrzucenia wszystkich graczy");
		
	}
}

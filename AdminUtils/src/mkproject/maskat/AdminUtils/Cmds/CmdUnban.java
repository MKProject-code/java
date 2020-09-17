package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import mkproject.maskat.AdminUtils.Config;
import mkproject.maskat.AdminUtils.Config.ConfigKey;
import mkproject.maskat.AdminUtils.Database;
import mkproject.maskat.AdminUtils.Plugin;
import mkproject.maskat.LoginManager.UsersAPI;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.CommandManager;
import mkproject.maskat.Papi.Utils.Message;

public class CmdUnban implements CommandExecutor, TabCompleter {

	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args);

		
		List<String> test = Database.Bans.getBannedPlayersNamesList();
		Plugin.getPlugin().getLogger().warning("UNBAN TEST: "+String.join(", ", test));
		manager.registerArgTabComplete(0, test);
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args, List.of("<player>", "[reason]"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		if(manager.hasArgStart(2))
			this.removeUnban(manager, manager.getArg(1), manager.getStringArgStart(2));
		
		return manager.doReturn();
	}
	
	// --------- /unban <player> [reason]
	public void removeUnban(CommandManager manager, String destPlayerName, String reason) {
		if(destPlayerName == null)
			return;
		
//		if(!Papi.Model.getPlayer(destPlayer).isMuted())
//		{
//			manager.setReturnMessage("&c&oGracz &e&o"+destPlayer.getName()+"&c&o nie ma aktywnego wyciszenia");
//			return;
//		}
		
		OfflinePlayer offlinePlayer = UsersAPI.getOfflinePlayer(destPlayerName);
		
		if(offlinePlayer == null) {
			manager.setReturnMessage("&c&oGracz &e&o"+destPlayerName+"&c&o nie został znaleziony");
			return;
		}
		
		if(reason == null)
			reason = "";
		
		int result = Database.Bans.removeBan(offlinePlayer, manager.getPlayer(), reason);
		
		if(result == 1)
		{
			String reasonStr = (reason!="") ? "\nPowód: &6"+reason : "";
			
			Bukkit.getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					Message.sendBroadcast(Papi.Vault.getChat().getPlayerPrefix(Papi.Server.getServerSpawnWorld().getName(), offlinePlayer)+offlinePlayer.getName()+" &7został odbanowany przez "+Papi.Model.getPlayer(manager.getPlayer()).getNameWithPrefix()+"&7."+reasonStr);
				}
			});
			manager.setReturnMessage(null);
		}
		else if(result == -1)
			manager.setReturnMessage("&c&oZapis o zbanowaniu gracza &e&o"+offlinePlayer.getName()+"&c&o nie został odnaleziony w bazie");
		else if(result == 0)
			manager.setReturnMessage("&c&oWystąpił błąd podczas próby odbanowania gracza &e&o"+offlinePlayer.getName());
			
	}
}

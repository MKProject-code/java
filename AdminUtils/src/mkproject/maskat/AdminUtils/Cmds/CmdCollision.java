package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import mkproject.maskat.AdminUtils.Config;
import mkproject.maskat.AdminUtils.Config.ConfigKey;
import mkproject.maskat.Papi.Utils.CommandManager;


public class CmdCollision implements CommandExecutor, TabCompleter {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args);
		
		List<String> playersList = manager.getOnlinePlayersNameList();
		playersList.add("@a[gamemode=adventure,distance=100]");
		
		manager.registerArgTabComplete(0, List.of("0","1"));
		manager.registerArgTabComplete(1, null, playersList);
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args, List.of("0|1","[player|@a[gamemode=adventure,distance=100]]"));
		
		if(manager.isPlayer())
			if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
				return manager.doReturn();
		
		if(manager.hasArgs(1))
		{
			if(manager.hasArg(1, "0")) // /collision 0
				this.setCollision(manager, manager.getPlayer(), false);
			else if(manager.hasArg(1, "1")) // /collision 1
				this.setCollision(manager, manager.getPlayer(), true);
		}
		else if(manager.hasArgs(2))
		{
			if(manager.hasArg(1, "0")) // /collision 0 [player|*adventure]
			{
				if(manager.hasArgAndPermission(2, "@a[gamemode=adventure,distance=100]"))
					this.setCollisionAllAdventureNear(sender, manager, false);
				else if(manager.hasArgAndPermission(2, "*"))
					this.setCollisionAll(manager, false);
				else
					this.setCollision(manager, manager.getChosenPlayerFromArg(2, true), false);
				return manager.doReturn();
			}
			else if(manager.hasArg(1, "1")) // /collision 1 [player|*adventure]
			{
				if(manager.hasArgAndPermission(2, "@a[gamemode=adventure,distance=100]"))
					this.setCollisionAllAdventureNear(sender, manager, true);
				else if(manager.hasArgAndPermission(2, "*"))
					this.setCollisionAll(manager, true);
				else
					this.setCollision(manager, manager.getChosenPlayerFromArg(2, true), true);
				return manager.doReturn();
			}
		}
		return manager.doReturn();
	}

	private void setCollisionAll(CommandManager manager, boolean enable) {
		for(Player destPlayer : Bukkit.getOnlinePlayers())
			destPlayer.setCollidable(enable);
	}

	private void setCollisionAllAdventureNear(CommandSender sender, CommandManager manager, boolean enable) {
		
        if(sender instanceof CommandBlock) {
            CommandBlock cmd = (CommandBlock) sender;
            
            for(Player destPlayer : Bukkit.getOnlinePlayers()) {
            	if(destPlayer.getGameMode() == GameMode.ADVENTURE && destPlayer.getLocation().distance(cmd.getLocation()) <= 100)
            		destPlayer.setCollidable(enable);
            }
            
            manager.setReturnMessage(null);
        }
        else if(sender instanceof Player) {
        	Player player = (Player) sender;
            
            for(Player destPlayer : Bukkit.getOnlinePlayers()) {
            	if(destPlayer.getGameMode() == GameMode.ADVENTURE && destPlayer.getLocation().distance(player.getLocation()) <= 100)
            		destPlayer.setCollidable(enable);
            }
            
    		manager.setReturnMessage("&a&oZmieniłeś kolizyjność wszyskim graczom ADVENTURE w promieniu 100 kratek na &b&o"+(enable?"włączone":"wyłączone"));
        }
	}

	// --------- /collision 0|1 [player]
	public void setCollision(CommandManager manager, Player destPlayer, boolean enable) {
		if(destPlayer == null)
			return;
		
		destPlayer.setCollidable(enable);
		
		manager.setReturnMessage(destPlayer,
				"&a&oZmieniłeś sobie kolizyjność na &b&o"+(enable?"włączone":"wyłączone"),
				"&a&oZmieniłeś kolizyjność graczowi &e&o"+destPlayer.getName() + "&a&o na &b&o"+(enable?"włączone":"wyłączone"));
	}
}

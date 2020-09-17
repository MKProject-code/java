package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mkproject.maskat.AdminUtils.Config;
import mkproject.maskat.AdminUtils.Config.ConfigKey;
import mkproject.maskat.AdminUtils.Plugin;
import mkproject.maskat.Papi.Utils.CommandManager;
import mkproject.maskat.Papi.Utils.Message;
import mkproject.maskat.SpawnManager.SpawnManagerAPI;

public class CmdRandomTp implements CommandExecutor {

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args, List.of("<X>","[Y]","<Z>","[world]"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		this.randomTeleport(manager);
		
		return manager.doReturn();
	}
	
	private void randomTeleport(CommandManager manager) {
		Player player = manager.getPlayer();
		Message.sendMessage(player, "&7&oSzukamy bezpiecznego miejsca dla Ciebie...");
		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Location randomLocation = SpawnManagerAPI.getRandomLocation(player.getWorld());
				randomLocation.setY(randomLocation.getY()+1);
				
				Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
					@Override
					public void run() {
						if(!player.isOnline())
							return;
						
						if(player.teleport(randomLocation))
							Message.sendMessage(player, "&a&oZostałeś przeteleportowany");
						else
							Message.sendMessage(player, "&c&oTeleportacja nieudana");
					}
				});
			}
		});
		manager.setReturnMessage(null);
	}
}

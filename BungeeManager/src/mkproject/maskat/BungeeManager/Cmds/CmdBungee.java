package mkproject.maskat.BungeeManager.Cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import mkproject.maskat.BungeeManager.BungeeAPI;
import mkproject.maskat.BungeeManager.Config;
import mkproject.maskat.BungeeManager.Config.ConfigKey;
import mkproject.maskat.BungeeManager.Plugin;
import mkproject.maskat.Papi.Utils.CommandManager;

public class CmdBungee implements CommandExecutor {
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args);
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		this.teleport(manager, manager.getArg(1), manager.getArg(2));
		
		return manager.doReturn();
	}
	
	// --------- /bungee
	private void teleport(CommandManager manager, String cmd, String server) {
		if(cmd.equals("1")) {
			Bukkit.getServer().getLogger().warning("manager.getPlayer()="+manager.getPlayer().getName());
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF(server);
			manager.getPlayer().sendPluginMessage(Plugin.getPlugin(), "BungeeCord", out.toByteArray());
			manager.setReturnMessage("Teleportacja udana?1");
		}
		else if(cmd.equals("2")) {
			BungeeAPI.connectPlayer(manager.getPlayer(), server);
			manager.setReturnMessage("Teleportacja connectPlayer?2");
		}
		else if(cmd.equals("3")) {
			BungeeAPI.forward(server, "SkyChannel");
			manager.setReturnMessage("Teleportacja forward?3");
		}
		else if(cmd.equals("4")) {
			BungeeAPI.forwardPlayer(manager.getPlayer(), server, "SkyChannel");
			manager.setReturnMessage("Teleportacja forwardPlayer?4X");
		}
		else if(cmd.equals("5")) {
			  ByteArrayDataOutput out = ByteStreams.newDataOutput();
			  out.writeUTF("SkyChannel");
			  out.writeUTF("Argument");
			  
			  manager.getPlayer().sendPluginMessage(Plugin.getPlugin(), "SkyChannel", out.toByteArray());
		}
	}
}

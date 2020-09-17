package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import mkproject.maskat.AdminUtils.Config;
import mkproject.maskat.AdminUtils.Config.ConfigKey;
import mkproject.maskat.Papi.Utils.CommandManager;

public class CmdTpposition implements CommandExecutor {

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args, List.of("<X>","[Y]","<Z>","[world]"));
		
//		if(!manager.isPlayer())
//			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		if(manager.hasArgs(2))
		{
			if(!manager.isNumericArg(1,2))
				return manager.doReturn();
			
			int blockX = (int)Double.parseDouble(manager.getArg(1));
			int blockZ = (int)Double.parseDouble(manager.getArg(2));
			this.teleportToPosition(manager, blockX, manager.getPlayer().getWorld().getHighestBlockYAt(blockX, blockZ)+1, blockZ, manager.getPlayer().getWorld());
		}
		else if(manager.hasArgs(3))
		{
			if(!manager.isNumericArg(1,2))
				return manager.doReturn();
			
			if(!manager.isNumericArg(3))
			{
				int blockX = (int)Double.parseDouble(manager.getArg(1));
				int blockZ = (int)Double.parseDouble(manager.getArg(2));
				this.teleportToPosition(manager, blockX, manager.getPlayer().getWorld().getHighestBlockYAt(blockX, blockZ)+1, blockZ, manager.getChosenWorldFromArg(3, true));
			}
			else
			{
				int blockX = (int)Double.parseDouble(manager.getArg(1));
				int blockY = (int)Double.parseDouble(manager.getArg(2));
				int blockZ = (int)Double.parseDouble(manager.getArg(3));
				this.teleportToPosition(manager, blockX, blockY, blockZ, manager.getPlayer().getWorld());
			}
		}
		else if(manager.hasArgs(4))
		{
			if(!manager.isNumericArg(1,2,3))
				return manager.doReturn();
			
			int blockX = (int)Double.parseDouble(manager.getArg(1));
			int blockY = (int)Double.parseDouble(manager.getArg(2));
			int blockZ = (int)Double.parseDouble(manager.getArg(3));
			this.teleportToPosition(manager, blockX, blockY, blockZ, manager.getChosenWorldFromArg(4, true));
		}
		
		return manager.doReturn();
	}
	
	private void teleportToPosition(CommandManager manager, int blockX, int blockY, int blockZ, World world) {
		if(manager.playerTeleport(manager.getPlayer(), new Location(world, blockX+0.5, blockY, blockZ+0.5)))
			manager.setReturnMessage("&a&oTeleportowałeś się do: &b&o"+world.getName()+"&a&o, &b&oX="+blockX+".5&a&o, &b&oY="+blockY+"&a&o, &b&oZ="+blockZ+".5");
	}
}

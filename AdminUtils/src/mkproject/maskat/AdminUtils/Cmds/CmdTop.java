package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CmdTop implements CommandExecutor {

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager_local manager = new CommandManager_local(sender, command, label, args, List.of("<amount>"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		if(!manager.isNumericArgs())
			return manager.doReturn();
		
		if(manager.hasArgs(1))
		{
			int blockUp = (int)Double.parseDouble(manager.getArg(1));
			this.teleportToPosition(manager, manager.getPlayer().getLocation().getBlockX(), manager.getPlayer().getLocation().getBlockY()+blockUp, manager.getPlayer().getLocation().getBlockZ());
		}
		
		return manager.doReturn();
	}
	
	private void teleportToPosition(CommandManager_local manager, int blockX, int blockY, int blockZ) {
		
		Block block = manager.getPlayer().getWorld().getBlockAt(blockX, blockY-1, blockZ);
		if(block.getType().isAir())
			block.setType(Material.GLASS);
		manager.getPlayer().teleport(new Location(manager.getPlayer().getWorld(), blockX+0.5, blockY, blockZ+0.5, manager.getPlayer().getLocation().getYaw(), manager.getPlayer().getLocation().getPitch()));
		
		manager.setReturnMessage("&a&oStoisz na bloku o lokalizacji: &b&o"+manager.getPlayer().getWorld().getName()+"&a&o, &b&oX="+blockX+".5&a&o, &b&oY="+(blockY-1)+"&a&o, &b&oZ="+blockZ+".5\n"
				+ "&a&oTeleportowałeś się do: &b&o"+manager.getPlayer().getWorld().getName()+"&a&o, &b&oX="+blockX+".5&a&o, &b&oY="+blockY+"&a&o, &b&oZ="+blockZ+".5");
	}
}

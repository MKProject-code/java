package mkproject.maskat.SpawnManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class ExecuteCommand implements CommandExecutor {

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
//		if (!command.getName().equalsIgnoreCase("spawn"))
//            return false;
		
		Player player = (Player)sender;
		
		if(player.isDead())
		{
			Message.sendMessage(player, "&c&oHej! Jesteś martwy!");
			return false;
		}
		
		if(player.getWorld().getName().equals("world_nether") || player.getWorld().getName().equals("world_the_end"))
		{
			Message.sendMessage(player, "&c&oNie możesz użyć tej komendy z zaświatu!");
			return false;
		}
		
		if(command.equals("setspawn") && player.hasPermission("mkp.spawnmanager.setspawn"))
		{
			Papi.Server.initServerSpawnLocation(player.getLocation());
			Plugin.getPlugin().getConfig().set("ServerSpawnLocation.World", player.getLocation().getWorld());
			Plugin.getPlugin().getConfig().set("ServerSpawnLocation.X", player.getLocation().getX());
			Plugin.getPlugin().getConfig().set("ServerSpawnLocation.Y", player.getLocation().getY());
			Plugin.getPlugin().getConfig().set("ServerSpawnLocation.Z", player.getLocation().getZ());
			Plugin.getPlugin().getConfig().set("ServerSpawnLocation.Yaw", player.getLocation().getYaw());
			Plugin.getPlugin().getConfig().set("ServerSpawnLocation.Pitch", player.getLocation().getPitch());
			Message.sendMessage(player, "&a&oUstawiono spawn");
			return false;
		}
		
//		if(player.getWorld() == Papi.Server.getSurvivalWorld())
//			SpawnManagerAPI.setPlayerSurvivalLastLocation(player, player.getLocation());
		if(command.equals("spawn") && player.hasPermission("mkp.spawnmanager.spawn"))
		{
			player.teleport(Papi.Server.getServerSpawnLocation());
			Message.sendMessage(player, "&a&oZostałeś przeteleportowany na spawn");
		}
        
        return false;
	}

}

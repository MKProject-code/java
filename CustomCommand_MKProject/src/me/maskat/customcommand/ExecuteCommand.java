package me.maskat.customcommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExecuteCommand implements CommandExecutor {
    Plugin plugin;
    
    private static String bedWorldName = "world";
    
    private static class Commands {
    	private static String tpSurvival() { return "survival"; }
    	private static String tpOldBuild() { return "oldbuild"; }
    	private static String tpNewBuild() { return "build"; }
    	private static String tpBedWars() { return "bedwars"; }
//    	private static String tpSpawnCenter() { return "spawncenter"; }
    }

    public ExecuteCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("c"))
            return false;

        if(!(sender instanceof Player))
        	return false;
        
        final Player player = Bukkit.getPlayer(sender.getName());
        
	    if (args.length > 0)
	    {
	        if (args[0].equalsIgnoreCase(Commands.tpSurvival()) && player.hasPermission("mkp.customcommand."+args[0])) {
	        	tpToWorld(player, "world");
	        	return false;
	        }
	        else if (args[0].equalsIgnoreCase(Commands.tpOldBuild()) && player.hasPermission("mkp.customcommand."+args[0])) {
	        	tpToWorld(player, "world_spawn");
	        	return false;
	        }
	        else if (args[0].equalsIgnoreCase(Commands.tpNewBuild()) && player.hasPermission("mkp.customcommand."+args[0])) {
	        	tpToWorld(player, "world_build");
	        	return false;
	        }
	        else if (args[0].equalsIgnoreCase(Commands.tpBedWars()) && player.hasPermission("mkp.customcommand."+args[0])) {
	        	tpToWorld(player, "world_bedwars");
	        	return false;
	        }
//	        else if (args[0].equalsIgnoreCase(Commands.tpSpawnCenter()) && player.hasPermission("mkp.customcommand."+args[0])) {
//	        	tpToWorld(player, "world_spawncenter");
//	        	return false;
//	        }
	    }
	    
	    boolean hasAnyPermission = false;
	    String cmd = Commands.tpSurvival();
        if(player.hasPermission("mkp.customcommand."+cmd))
        {
        	if(!hasAnyPermission)
        		printHelpHeader(player);
        	hasAnyPermission = true;
        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/c "+cmd+"&7 - teleportacja na survival"));
        }
        
        cmd = Commands.tpOldBuild();
        if(player.hasPermission("mkp.customcommand."+cmd))
        {
        	hasAnyPermission = true;
        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/c "+cmd+"&7 - teleportacja na stary plac budowy"));
        }
        cmd = Commands.tpNewBuild();
        if(player.hasPermission("mkp.customcommand."+cmd))
        {
        	hasAnyPermission = true;
        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/c "+cmd+"&7 - teleportacja na plac budowy"));
        }
        cmd = Commands.tpBedWars();
        if(player.hasPermission("mkp.customcommand."+cmd))
        {
        	hasAnyPermission = true;
        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/c "+cmd+"&7 - teleportacja na plac budowy"));
        }
        
//        cmd = Commands.tpSpawnCenter();
//        if(player.hasPermission("mkp.customcommand."+cmd))
//        {
//        	hasAnyPermission = true;
//        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/c "+cmd+"&7 - teleportacja na nowy spawn (spawn center)"));
//        }
        
        if(!hasAnyPermission)
        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNieznana komenda."));
        
        return false;
	}
    
    private void printHelpHeader(Player player)
    {
    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6======= &lCommand Center Help&6 ======="));
    }
    
    private void tpToWorld(Player player, String tpToWorldName) {
		Location playerCurrentLocation = player.getLocation();
		
		if(tpToWorldName == "world") {
			if(playerCurrentLocation.getWorld().getName().equalsIgnoreCase("world_nether") ||
					playerCurrentLocation.getWorld().getName().equalsIgnoreCase("world_the_end") ||
					playerCurrentLocation.getWorld().getName().equalsIgnoreCase("world_spawncenter"))
			{
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cJesteś juz w tym świecie!"));
				return;
			}
		}
		
		if(playerCurrentLocation.getWorld().getName().equalsIgnoreCase(tpToWorldName))
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cJesteś juz w tym świecie!"));
			return;
		}
		plugin.putDatabasePlayerLastLocation(player, player.getLocation());
		Location lastlocation = plugin.getDatabasePlayerLastLocation(player, tpToWorldName);
		if(lastlocation != null)
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eTeleportacja do poprzedniej lokalizacji..."));
			player.teleport(lastlocation);
		}
		else
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cBrak informacji o poprzedniej lokalizacji na danej mapie."));
			if(tpToWorldName == bedWorldName)
			{
		    	Location bedlocation = player.getBedSpawnLocation();
		    	if(bedlocation != null && bedlocation.getWorld().getName().equalsIgnoreCase(bedWorldName))
		    	{
		    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eTeleportacja do łóżka..."));
		    		player.teleport(bedlocation);
		    	}
		    	else
		    	{
		    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cOdradzanie przy łóżku nie zostało ustawione."));
		    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eTeleportacja na spawn danej mapy..."));
		    		player.teleport(Bukkit.getWorld(tpToWorldName).getSpawnLocation());
		    	}
			}
			else
			{
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eTeleportacja na spawn danej mapy..."));
				player.teleport(Bukkit.getWorld(tpToWorldName).getSpawnLocation());
			}
		}
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aZostałeś przeteleportowany."));
    }
}
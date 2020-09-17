package me.maskat.wolfsecurity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import me.maskat.wolfsecurity.models.Model;

public class ExecuteCmd implements CommandExecutor {
	
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    	
    	//Player playerx = (Player)sender;
    	//playerx.sendMessage("SHOWN borders!");
    	
    	//player.spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 1);
    	
    	//playerx.spawnParticle(Particle.FIREWORKS_SPARK, playerx.getLocation().getX(), playerx.getLocation().getY(), playerx.getLocation().getZ(), 5, 0, 5, 0);

    	
        if (command.getName().equalsIgnoreCase("wolf")) {
            if (!(sender instanceof Player))
            	return false;
            
            if (args.length == 0) {
            	sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUżyj /wolf [info|get]"));
            	return false;
            }
            
            final Player player = Bukkit.getPlayer(sender.getName());
            //final String playerName = Bukkit.getPlayer(sender.getName()).getName();
            
            if(args.length == 1) {
            	if(!Model.Player(player).isInitialized())
            	{
	            	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cAktualnie sprawdzamy status twojego wilka! Spróbuj ponownie za kilka sekund."));
	            	return false;
            	}
            	
            	final String subcommand = args[0];
	            if (subcommand.equalsIgnoreCase("info")) {
		            int wolfid = Model.Player(player).getOwnWolfId();
		            
		            if(!Model.Wolves().containsKey(wolfid)) {
		            	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNie masz żadnego wilka! Użyj &e/wolf get&c aby dostać!"));
		            	return false;
		            }
		            else {
	            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMasz swojego wilka " + Model.Wolf(wolfid).getName()));
	            		if(Config.debug) player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUUID: " + Model.Wolf(wolfid).getEntityWolf().getUniqueId()));
	            		if(Config.debug) player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cXYZ: " + Model.Wolf(wolfid).getEntityWolf().getLocation()));
	            		return false;
	            	}
	            }
	            else if (subcommand.equalsIgnoreCase("get")) {
	            	int wolfid = Model.Player(player).getOwnWolfId();
	            	
	            	if(Model.Wolves().containsKey(wolfid))
	            	{
	            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMasz już swojego wilka!"));
	            		return false;
	            	}
	            	
	            	if(player.getWorld() != Plugin.getAllowedWorld())
	            	{
	            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNie możesz tego zrobić w tym świecie!"));
	            		return false;
	            	}
	            	//daje wilka
	            	//Model.Wolves.addWolf(player);
	            	
	            	Model.Player(player).addWolf();
	            	Model.Player(player).addRegion();
	            	
	            	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aDostałeś swojego własnego wilka!"));
	            	return false;
	            }
            }
	        if(args.length == 2) {
	        	final String subcommand = args[0];
	        	if (subcommand.equalsIgnoreCase("tp") && player.hasPermission("mkp.wolfsecurity.command.remove")) {
	            	if(args.length < 2)
	            	{
	            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUżyj /wolf tp <id>"));
	            		return false;
	            	}
	            	int wolfid = Integer.valueOf(args[1]);
	            	if(Model.Wolves().containsKey(wolfid))
	            	{
	            		Wolf wolfentity = Model.Wolf(wolfid).getEntityWolf();
	            		if(wolfentity == null)
	            		{
	            			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cByt wilka nie został znaleziony - WolfID: " + args[1]));
	            			return false;
	            		}
	            		
	            		player.teleport(wolfentity);
	            		
	            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTeleportowałeś się do wilka - WolfID: " + args[1]));
	            		return false;
	            	}
	            	
	            	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTaki wilk nie istnieje - WolfID: " + args[1]));
	            	return false;
	            }
            }
            
        }
        return false;
    }
    
//    private String getMessage(final String[] args, final int index) {
//        final StringBuilder sb = new StringBuilder();
//        for (int i = index; i < args.length; ++i) {
//            sb.append(args[i]).append(" ");
//        }
//        return sb.toString();
//    }
    
}

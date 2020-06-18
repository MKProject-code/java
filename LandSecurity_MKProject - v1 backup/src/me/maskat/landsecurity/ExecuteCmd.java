package me.maskat.landsecurity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.maskat.landsecurity.models.Model;

public class ExecuteCmd implements CommandExecutor {
	
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args) {
    	
    	//Player playerx = (Player)sender;
    	//playerx.sendMessage("SHOWN borders!");
    	
    	//player.spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 1);
    	
    	//playerx.spawnParticle(Particle.FIREWORKS_SPARK, playerx.getLocation().getX(), playerx.getLocation().getY(), playerx.getLocation().getZ(), 5, 0, 5, 0);

    	
        if (command.getName().equalsIgnoreCase("wolf")) {
            if (!(sender instanceof Player))
            	return false;
            
            if (args.length == 0) {
            	sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eU¿yj /wolf [info|get]"));
            	return false;
            }
            
            final Player player = Bukkit.getPlayer(sender.getName());
            //final String playerName = Bukkit.getPlayer(sender.getName()).getName();
            
            if(args.length == 1) {
            	final String subcommand = args[0];
	            if (subcommand.equalsIgnoreCase("info")) {
		            int wolfid = Model.Player(player).getOwnWolfId();
		            
		            if(!Model.Wolves().containsKey(wolfid)) {
		            	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNie masz ¿adnego wilka! U¿yj &e/wolf get&c aby dostaæ!"));
		            	return false;
		            }
		            else {
	            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMasz swojego wilka " + Model.Wolf(wolfid).getName()));
	            		if(Config.debug) player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUUID: " + Model.Wolf(wolfid).getEntityWolf().getUniqueId()));
	            		if(Config.debug) player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cXYZ: " + Model.Wolf(wolfid).getEntityWolf().getLocation()));
	            		return false;
	            	}
	            }
	            
	            if (subcommand.equalsIgnoreCase("get")) {
	            	int wolfid = Model.Player(player).getOwnWolfId();
	            	
	            	if(Model.Wolves().containsKey(wolfid))
	            	{
	            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMasz ju¿ swojego wilka!"));
	            		return false;
	            	}
	            	
	            	if(player.getWorld() != Bukkit.getServer().getWorld(Config.allowWorld))
	            	{
	            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNie mo¿esz tego zrobiæ w tym œwiecie!"));
	            		return false;
	            	}
	            	//daje wilka
	            	//Model.Wolves.addWolf(player);
	            	
	            	Model.Player(player).addWolf();
	            	Model.Player(player).addRegion();
	            	
	            	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aDosta³eœ swojego w³asnego wilka!"));
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

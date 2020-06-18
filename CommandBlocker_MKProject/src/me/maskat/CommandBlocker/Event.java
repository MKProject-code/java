package me.maskat.CommandBlocker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

import mkproject.maskat.Papi.Utils.Message;

public class Event implements Listener {

    @EventHandler
	public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent e) {
    	try
    	{
	    	final Player player = e.getPlayer();
	    	
	    	if(player.hasPermission("mkp.commandblocker.bypassall"))
	    		return;
	    	
	        final Set<String> commandsWhitelist = Plugin.getPlugin().getConfig().getConfigurationSection("allowCommands").getKeys(false);
	        
	    	boolean isAllow = false;
	    	
	    	String command = e.getMessage().split(" ")[0];
	    	
	        if(commandsWhitelist.contains(command))
	        {
        		String inherit = Plugin.getPlugin().getConfig().getString("allowCommands."+command+".inherit");
        		String command_temp = command;
        		
        		if(inherit != null)
        			command_temp = inherit;
        		
	        	final Set<String> groupsWhitelist = Plugin.getPlugin().getConfig().getConfigurationSection("allowCommands."+command_temp).getKeys(false);
	        	for(String groupWhite : groupsWhitelist) {
            		if(groupWhite.equals("all"))
            		{
            			isAllow = true;
            			break;
            		}
            		
	            	if(player.hasPermission("mkp.commandblocker."+groupWhite))
	            	{
	            		
						List<String> worldsWhite = Plugin.getPlugin().getConfig().getStringList("allowCommands."+command_temp+"."+groupWhite);
						for(String worldWhiteName : worldsWhite) {
							if(worldWhiteName.equals("all") || worldWhiteName.equals(player.getWorld().getName()))
		            		{
		            			isAllow = true;
		            			break;
		            		}
						}
	            	}
	        	}
	        }
	        
	        if(!isAllow)
	        {
	        	Message.sendMessage(player, "&c&oBrak uprawnień");
	        	e.setCancelled(true);
	        }
    	}
    	catch (Exception ex)
    	{
    		e.setCancelled(true);
    		Message.sendMessage(e.getPlayer(), "&c&oWystąpił błąd... Prosimy o zgłoszenie tego Administracji.");
    		Plugin.getPlugin().getLogger().warning("*********** ERROR when player '"+e.getPlayer()+"' executed command: "+e.getMessage());
    	}
    	
    	if(e.isCancelled())
    		Plugin.getPlugin().getLogger().info("Player '"+e.getPlayer().getName()+"' no access to use command: "+e.getMessage());
	}
    
    @EventHandler
    public void onCommandSuggestionSend(final PlayerCommandSendEvent e) {
		final Player player = e.getPlayer();
        
        if(player.hasPermission("mkp.commandblocker.bypassall"))
            return;
            
        final ArrayList<String> suggestions = new ArrayList<String>(e.getCommands());
        final Set<String> commandsWhitelist = Plugin.getPlugin().getConfig().getConfigurationSection("allowCommands").getKeys(false);
       
        for (String suggestion : suggestions)
        {
        	boolean isAllow = false;
        	
        	try
        	{
	        	if(commandsWhitelist.contains("/"+suggestion)) {
	            	
	        		String inherit = Plugin.getPlugin().getConfig().getString("allowCommands./"+suggestion+".inherit");
	        		String suggestion_temp = "/"+suggestion;
	        		if(inherit != null)
	        			suggestion_temp = inherit;
	        		
	        		final Set<String> groupsWhitelist = Plugin.getPlugin().getConfig().getConfigurationSection("allowCommands."+suggestion_temp).getKeys(false);
	            	
	            	for(String groupWhite : groupsWhitelist) {
	            		if(groupWhite.equals("all"))
	            		{
	            			isAllow = true;
	            			break;
	            		}
	            		
		            	if(player.hasPermission("mkp.commandblocker."+groupWhite)) 
		            	{
		            		isAllow = true;
		            		break;
		            	}
	            	}
	            }
        	}
        	catch (Exception ex)
        	{
        		Plugin.getPlugin().getLogger().warning("*********** ERROR Send suggestion command '"+suggestion+"' to player '"+e.getPlayer()+"'");
        	}
        	
            if(!isAllow)
            	e.getCommands().remove(suggestion);
        }

    }
	
}

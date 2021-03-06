package me.maskat.CommandBlocker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class Event implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent e) {
    	try
    	{
	    	final Player player = e.getPlayer();
	    	
	    	if(player.hasPermission("mkp.commandblocker.bypassall"))
	    		return;
	    	
	        final Set<String> commandsWhitelist = Plugin.getPlugin().getConfig().getConfigurationSection("allowCommands").getKeys(false);
	        
	    	boolean isAllow = false;
	    	
	    	String command = e.getMessage().split(" ")[0].toLowerCase();
	    	
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
	            		final List<String> worldsWhite = Plugin.getPlugin().getConfig().getStringList("allowCommands."+command_temp+"."+groupWhite+".allowWorlds");
						for(String worldWhiteName : worldsWhite) {
							if(worldWhiteName.equals("all") || worldWhiteName.equals(player.getWorld().getName()))
		            		{
		            			isAllow = true;
		            			break;
		            		}
						}
	            	}
	            	
	            	if(isAllow) {
	            		final ConfigurationSection maxUsageTypes = Plugin.getPlugin().getConfig().getConfigurationSection("allowCommands."+command_temp+"."+groupWhite+".maxUsage");
						if(maxUsageTypes != null) {
							final Set<String> maxUsageTypesSet = maxUsageTypes.getKeys(false);
							for(String maxUsageType : maxUsageTypesSet) {
								if(maxUsageType.equals("inOneDay"))
								{
									String lastUsage = Plugin.getDatabase().getString(player.getName().toLowerCase()+"."+command+".inOneDay.last_usage");
									LocalDateTime lastUsageDateTime = null;
									LocalDateTime nowDateTime = null;
									
									if(lastUsage != null) {
										lastUsageDateTime = Papi.Function.getLocalDateTimeFromString(lastUsage);
										nowDateTime = LocalDateTime.now();
									}
									
									if(lastUsage != null && lastUsageDateTime.getDayOfYear() == nowDateTime.getDayOfYear() && lastUsageDateTime.getYear() == nowDateTime.getYear())
									{
										int inOneDay = Plugin.getPlugin().getConfig().getInt("allowCommands."+command_temp+"."+groupWhite+".maxUsage."+maxUsageType);
										int inOneDayPlayer = Plugin.getDatabase().getInt(player.getName().toLowerCase()+"."+command+".inOneDay.usages");
										
										if(inOneDayPlayer>=inOneDay)
										{
								        	Message.sendMessage(player, "&c&oMożesz użyć tej komendy maksymalnie "+inOneDay+" "+(inOneDay==1 ? "raz" : "razy")+" w ciągu doby.");
								        	e.setCancelled(true);
								        	return;
										}
										else
										{
											Plugin.getDatabase().set(player.getName().toLowerCase()+"."+command+".inOneDay.usages", inOneDayPlayer+1);
											Plugin.getDatabase().set(player.getName().toLowerCase()+"."+command+".inOneDay.last_usage", Papi.Function.getCurrentLocalDateTimeToString());
										}
									}
									else
									{
										Plugin.getDatabase().set(player.getName().toLowerCase()+"."+command+".inOneDay.usages", 1);
										Plugin.getDatabase().set(player.getName().toLowerCase()+"."+command+".inOneDay.last_usage", Papi.Function.getCurrentLocalDateTimeToString());
									}
								}
								//else if(...) // you can add new usage times here... :)
							}
						}
		            }
	        	}
	        }
	        
	        if(!isAllow)
	        {
	        	Message.sendMessage(player, "&c&oNie możesz użyć tej komendy.");
	        	e.setCancelled(true);
	        }
    	}
    	catch (Exception ex)
    	{
    		e.setCancelled(true);
    		Message.sendMessage(e.getPlayer(), "&c&oWystąpił błąd... Prosimy o zgłoszenie tego do Administracji.");
    		Plugin.getPlugin().getLogger().warning("*********** ERROR when player '"+e.getPlayer()+"' executed command: "+e.getMessage());
    		//ex.printStackTrace(new java.io.PrintStream(System.out));
    		ex.printStackTrace(System.out);
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
        		String suggestion_temp = "/"+suggestion;
	        	if(commandsWhitelist.contains(suggestion_temp)) {
	        		String inherit = Plugin.getPlugin().getConfig().getString("allowCommands."+suggestion_temp+".inherit");
	        		
	        		if(inherit != null)
	        			suggestion_temp = inherit;
	        		
	        		final Set<String> groupsWhitelist = Plugin.getPlugin().getConfig().getConfigurationSection("allowCommands."+suggestion_temp).getKeys(false);
	            	
	            	for(String groupWhite : groupsWhitelist) {
	            		if(groupWhite.equals("all") || player.hasPermission("mkp.commandblocker."+groupWhite))
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

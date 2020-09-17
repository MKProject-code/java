package mkproject.maskat.AdminUtils.Commands;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import me.maskat.MoneyManager.Mapi;

abstract public class CommandSkyPoints {
	
	@SuppressWarnings({ })
	public static void register(JavaPlugin plugin, String permissionPrefix) {
		command(plugin, permissionPrefix);
	}
	
	@SuppressWarnings({ "unchecked" })
	public static void command(JavaPlugin plugin, String permissionPrefix) {
		
	    LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	    arguments.put("operation", new LiteralArgument("add"));
	    arguments.put("target", new EntitySelectorArgument(EntitySelector.MANY_PLAYERS));
	    arguments.put("value", new IntegerArgument(0));
	    
	    new CommandAPICommand("skypoints")
        .withArguments(arguments)
        .withPermission(CommandPermission.fromString(permissionPrefix+"skypoints.use"))
        .executes((sender, args) -> {
        	Collection<Player> targets = (Collection<Player>) args[0];
        	int val = (int) args[1];
        	for(Player targetPlayer : targets)
        		Mapi.getPlayer(targetPlayer).addPoints(val);
        })
        .register();
	    
	    arguments.put("operation", new LiteralArgument("remove"));
	    
	    new CommandAPICommand("skypoints")
	    .withArguments(arguments)
	    .executes((sender, args) -> {
	    	Collection<Player> targets = (Collection<Player>) args[0];
	    	int val = (int) args[1];
	    	for(Player targetPlayer : targets)
	    	{
	    		if((Mapi.getPlayer(targetPlayer).getPoints()-Double.valueOf(val)) > 0)
	    			Mapi.getPlayer(targetPlayer).delPoints(val);
	    		else
	    			Mapi.getPlayer(targetPlayer).setPoints(0);
	    	}
	    })
	    .register();
	    
	    arguments.put("operation", new LiteralArgument("set"));
	    
	    new CommandAPICommand("skypoints")
	    .withArguments(arguments)
	    .executes((sender, args) -> {
	    	Collection<Player> targets = (Collection<Player>) args[0];
	    	int val = (int) args[1];
	    	for(Player targetPlayer : targets)
	    		Mapi.getPlayer(targetPlayer).setPoints(val);
	    })
	    .register();
	}

}

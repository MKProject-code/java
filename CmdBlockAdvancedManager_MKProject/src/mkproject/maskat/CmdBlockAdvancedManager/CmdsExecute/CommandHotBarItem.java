package mkproject.maskat.CmdBlockAdvancedManager.CmdsExecute;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.ItemStackArgument;

public abstract class CommandHotBarItem {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static void register(JavaPlugin plugin) {
		command(plugin);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void command(JavaPlugin plugin) {

	    //Create our arguments as usual, using the LiteralArgument for the name of the gamemode
	    LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	    arguments.put("target", new EntitySelectorArgument(EntitySelector.MANY_PLAYERS));
	    arguments.put("itemstack", new ItemStackArgument());
	    
	    //Register the command as usual
	    new CommandAPICommand("hotbar")
	        .withArguments(arguments)
	        .withPermission(CommandPermission.OP)
	        .executes((sender, args) -> {
		    	Collection<Entity> targets = (Collection<Entity>) args[0];
		    	ItemStack itemStack = (ItemStack) args[1];
		    	
		    	for(Entity target : targets) {
		    		if(target instanceof Player)
		    		{
		    			Player targetPlayer = (Player)target;
		    			
		    			for(int i=0;i<9;i++)
		    				targetPlayer.getInventory().setItem(i, itemStack);
		    		}
		    	}
	        })
	        .register();
	}
}

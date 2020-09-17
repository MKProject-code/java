package mkproject.maskat.AdminUtils;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException;
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;

public class Plugin extends JavaPlugin {
	private static Plugin plugin;
	
	public void onEnable() {
		plugin = this;
		
	    LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	    arguments.put("target", new EntitySelectorArgument(EntitySelector.ONE_PLAYER));
	    arguments.put("color", CustomArg.color());
	    arguments.put("style", CustomArg.style());
	    
	    new CommandAPICommand("test")
	        .withArguments(arguments)
	        .executes((sender, args) -> {

	        })
	        .register();
	    
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
	
	public abstract static class CustomArg {
		protected static Argument color() {
			Set<String> colors = new HashSet<>();
			for(BarColor barColor : BarColor.values())
				colors.add(barColor.name());

		    return new CustomArgument<BarColor>((input) -> {
		        if(!colors.contains(input)) {
		            throw new CustomArgumentException(new MessageBuilder("Unknown Boss Bar Color: ").appendArgInput());
		        } else {
		            return BarColor.valueOf(input);
		        }
		    }).overrideSuggestions(colors.toArray(new String[colors.size()]));
		}

		protected static Argument style() {
			Set<String> styles = new HashSet<>();
			for(BarStyle barColor : BarStyle.values())
				styles.add(barColor.name());

		    return new CustomArgument<BarStyle>((input) -> {
		        if(!styles.contains(input)) {
		            throw new CustomArgumentException(new MessageBuilder("Unknown Boss Bar Style: ").appendArgInput());
		        } else {
		            return BarStyle.valueOf(input);
		        }
		    }).overrideSuggestions(styles.toArray(new String[styles.size()]));
		}
	}
}


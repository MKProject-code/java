package mkproject.maskat.CmdBlockAdvancedManager.CmdsExecute;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException;
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder;

public abstract class CustomArgumentManager {
	//Function that returns our custom argument
	protected static Argument BossBarColor() {
		Set<String> operators = new HashSet<>();
		
		for(BarColor barColor : BarColor.values())
			operators.add(barColor.name());

	    //Construct our CustomArgument that takes in a String input and returns a World object
	    return new CustomArgument<BarColor>((input) -> {
	        //Parse the world from our input

	        if(!operators.contains(input)) {
	            throw new CustomArgumentException(new MessageBuilder("Unknown Boss Bar Color: ").appendArgInput());
	        } else {
	            return BarColor.valueOf(input);
	        }
	    }).overrideSuggestions(operators.toArray(new String[operators.size()]));
	}

	protected static Argument BossBarStyle() {
		Set<String> operators = new HashSet<>();
		
		for(BarStyle barColor : BarStyle.values())
			operators.add(barColor.name());

	    //Construct our CustomArgument that takes in a String input and returns a World object
	    return new CustomArgument<BarStyle>((input) -> {
	        //Parse the world from our input

	        if(!operators.contains(input)) {
	            throw new CustomArgumentException(new MessageBuilder("Unknown Boss Bar Style: ").appendArgInput());
	        } else {
	            return BarStyle.valueOf(input);
	        }
	    }).overrideSuggestions(operators.toArray(new String[operators.size()]));
	}
}

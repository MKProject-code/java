package mkproject.maskat.CmdBlockAdvancedManager.CmdsExecute;

import java.util.LinkedHashMap;

import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import dev.jorel.commandapi.CommandAPIHandler.Brigadier;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.IntegerArgument;

public abstract class ChildExecuteIfRandomChance {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static void register(JavaPlugin plugin) {
		childIf(plugin);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void childIf(JavaPlugin plugin) {
		
		//Register literal "randomchance"
		LiteralCommandNode randomChance = Brigadier.registerNewLiteral("randomchance");
		
		//Declare arguments like normal
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		arguments.put("numerator", new IntegerArgument(0));
		arguments.put("denominator", new IntegerArgument(1));
		
		//Get brigadier argument objects
		ArgumentBuilder numerator = Brigadier.argBuildOf(arguments, "numerator");
		ArgumentBuilder denominator = Brigadier.argBuildOf(arguments, "denominator")
				//Fork redirecting to "execute" and state our predicate
				.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) -> {
					//Parse arguments like normal
					int num = (int) args[0];
					int denom = (int) args[1];
					
					//Return boolean with a num/denom chance
					return Math.ceil(Math.random() * (double) denom) <= (double) num;
				}, arguments));
		
		//Add <numerator> <denominator> as a child of randomchance
		randomChance.addChild(numerator.then(denominator).build());
		
		//Add (randomchance <numerator> <denominator>) as a child of (execute -> if)
		Brigadier.getRootNode().getChild("execute").getChild("if").addChild(randomChance);
	}
}

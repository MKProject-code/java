package mkproject.maskat.CmdBlockAdvancedManager.CmdsExecute;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import dev.jorel.commandapi.CommandAPIHandler.Brigadier;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.IntegerRangeArgument;
import dev.jorel.commandapi.wrappers.IntegerRange;

public abstract class ChildExecuteIfCount {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static void register(JavaPlugin plugin) {
		childIf(plugin);
		childUnless(plugin);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void childIf(JavaPlugin plugin) {
		
		//Register literal "countext"
		LiteralCommandNode count = Brigadier.registerNewLiteral("count");
		
		//Declare arguments like normal
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		arguments.put("target", new EntitySelectorArgument(EntitySelector.MANY_ENTITIES));
		arguments.put("amount", new IntegerRangeArgument());
		
		//Get brigadier argument objects
		ArgumentBuilder target = Brigadier.argBuildOf(arguments, "target");
		ArgumentBuilder amount = Brigadier.argBuildOf(arguments, "amount")
				//Fork redirecting to "execute" and state our predicate
				.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) -> {
					//Parse arguments like normal
					Collection<Entity> targets = (Collection<Entity>) args[0];
					IntegerRange amountRange = (IntegerRange) args[1];
					
					int targetsSize = targets.size();
					
					//Return boolean
					return (targetsSize >= amountRange.getLowerBound() && targetsSize <= amountRange.getUpperBound());
				}, arguments));
		
		//Add <target> <amount> as a child of count
		count.addChild(target.then(amount).build());
		
		//Add (count <target> <amount>) as a child of (execute -> if)
		Brigadier.getRootNode().getChild("execute").getChild("if").addChild(count);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void childUnless(JavaPlugin plugin) {
		
		//Register literal "count"
		LiteralCommandNode count = Brigadier.registerNewLiteral("count");
		
		//Declare arguments like normal
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		arguments.put("target", new EntitySelectorArgument(EntitySelector.MANY_ENTITIES));
		arguments.put("amount", new IntegerRangeArgument());
		
		//Get brigadier argument objects
		ArgumentBuilder target = Brigadier.argBuildOf(arguments, "target");
		ArgumentBuilder amount = Brigadier.argBuildOf(arguments, "amount")
				//Fork redirecting to "execute" and state our predicate
				.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) -> {
					//Parse arguments like normal
					Collection<Entity> targets = (Collection<Entity>) args[0];
					IntegerRange amountRange = (IntegerRange) args[1];
					
					int targetsSize = targets.size();
					
					//Return boolean
					return !(targetsSize >= amountRange.getLowerBound() && targetsSize <= amountRange.getUpperBound());
				}, arguments));
		
		//Add <target> <amount> as a child of count
		count.addChild(target.then(amount).build());
		
		//Add (count <target> <amount>) as a child of (execute -> if)
		Brigadier.getRootNode().getChild("execute").getChild("unless").addChild(count);
	}
}

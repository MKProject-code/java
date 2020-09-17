package mkproject.maskat.AdminUtils.Commands;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import dev.jorel.commandapi.CommandAPIHandler.Brigadier;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.IntegerRangeArgument;
import dev.jorel.commandapi.wrappers.IntegerRange;
import me.maskat.MoneyManager.Mapi;

public class ChildIfSkyPoints {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void register(JavaPlugin plugin) {
		childIf(plugin);
		childUnless(plugin);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void childIf(JavaPlugin plugin) {
		
		LiteralCommandNode skypoints = Brigadier.registerNewLiteral("skypoints");

		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		arguments.put("target", new EntitySelectorArgument(EntitySelector.ONE_PLAYER));
		arguments.put("amount", new IntegerRangeArgument());
		
		//Get brigadier argument objects
		ArgumentBuilder target = Brigadier.argBuildOf(arguments, "target");
		ArgumentBuilder amount = Brigadier.argBuildOf(arguments, "amount")
				//Fork redirecting to "execute" and state our predicate
				.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) -> {
					Player player = (Player) args[0];
					IntegerRange amountRange = (IntegerRange) args[1];
					
					double points = Mapi.getPlayer(player).getPoints();
					
					return (points >= amountRange.getLowerBound() && points <= amountRange.getUpperBound());
				}, arguments));
		
		skypoints.addChild(target.then(amount).build());
		
		Brigadier.getRootNode().getChild("execute").getChild("if").addChild(skypoints);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void childUnless(JavaPlugin plugin) {
		
		LiteralCommandNode skypoints = Brigadier.registerNewLiteral("skypoints");

		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		arguments.put("target", new EntitySelectorArgument(EntitySelector.ONE_PLAYER));
		arguments.put("amount", new IntegerRangeArgument());
		
		//Get brigadier argument objects
		ArgumentBuilder target = Brigadier.argBuildOf(arguments, "target");
		ArgumentBuilder amount = Brigadier.argBuildOf(arguments, "amount")
				//Fork redirecting to "execute" and state our predicate
				.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) -> {
					Player player = (Player) args[0];
					IntegerRange amountRange = (IntegerRange) args[1];
					
					double points = Mapi.getPlayer(player).getPoints();
					
					return !(points >= amountRange.getLowerBound() && points <= amountRange.getUpperBound());
				}, arguments));
		
		skypoints.addChild(target.then(amount).build());
		
		Brigadier.getRootNode().getChild("execute").getChild("unless").addChild(skypoints);
	}
}

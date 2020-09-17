package mkproject.maskat.CmdBlockAdvancedManager.CmdsExecute;

import java.util.LinkedHashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import dev.jorel.commandapi.CommandAPIHandler.Brigadier;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.StringArgument;

public abstract class ChildExecuteIfPermission {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static void register(JavaPlugin plugin) {
		childIf(plugin);
		childUnless(plugin);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void childIf(JavaPlugin plugin) {
		LiteralCommandNode permissionChild = Brigadier.registerNewLiteral("permission");
		
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		arguments.put("target", new EntitySelectorArgument(EntitySelector.ONE_PLAYER));
		arguments.put("permission", new StringArgument());
		
		ArgumentBuilder targetArg = Brigadier.argBuildOf(arguments, "target");
		ArgumentBuilder permissionArg = Brigadier.argBuildOf(arguments, "permission")
				.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) -> {
					Player player = (Player) args[0];
					String perm = (String) args[1];
					
					return player.hasPermission(perm);
				}, arguments));

		permissionChild.addChild(targetArg.then(permissionArg).build());

		Brigadier.getRootNode().getChild("execute").getChild("if").addChild(permissionChild);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void childUnless(JavaPlugin plugin) {
		LiteralCommandNode permissionChild = Brigadier.registerNewLiteral("permission");
		
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		arguments.put("target", new EntitySelectorArgument(EntitySelector.ONE_PLAYER));
		arguments.put("permission", new StringArgument());
		
		ArgumentBuilder targetArg = Brigadier.argBuildOf(arguments, "target");
		ArgumentBuilder permissionArg = Brigadier.argBuildOf(arguments, "permission")
				.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) -> {
					Player player = (Player) args[0];
					String perm = (String) args[1];
					
					return !player.hasPermission(perm);
				}, arguments));

		permissionChild.addChild(targetArg.then(permissionArg).build());

		Brigadier.getRootNode().getChild("execute").getChild("if").addChild(permissionChild);
	}
}

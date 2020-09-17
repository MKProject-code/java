package mkproject.maskat.WorldManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.google.common.io.Files;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.CommandManager;

public class CmdWorldManager implements CommandExecutor, TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args);
		
		if(!manager.setArgTabComplete(1, List.of("info", "create", "modify", "load", "unload", "remove", "reload")))
		{
			if(manager.hasArgTabComplete(1, "create")) {
				if(manager.hasArgOrSetTabComplete(3, List.of("AMPLIFIED", "FLAT", "NORMAL", "EMPTY", "LARGE_BIOMES")))
					if(manager.hasArgOrSetTabComplete(4, List.of("SURVIVAL", "CREATIVE", "ADVENTURE", "SPECTATOR")))
						if(manager.hasArgOrSetTabComplete(5, List.of("PEACEFUL", "EASY", "NORMAL", "HARD")))
							manager.setArgTabComplete(6, List.of("TRUE", "FALSE"));
			}
			else if(manager.hasArgTabComplete(1, "modify")) {
				if(manager.hasArgOrSetTabComplete(2, manager.getWorldsNameListWithThis()))
					if(manager.hasArgOrSetTabComplete(3, List.of("GameMode", "Difficulty", "KeepSpawnInMemory", "BorderRadius", "BorderSquared", "GameRules")))
					{
						if(manager.hasArgTabComplete(3, "GameMode"))
						{
							manager.setArgTabComplete(4, List.of("SURVIVAL", "CREATIVE", "ADVENTURE", "SPECTATOR"));
						}
						else if(manager.hasArgTabComplete(3, "Difficulty"))
						{
							manager.setArgTabComplete(4, List.of("PEACEFUL", "EASY", "NORMAL", "HARD"));
						}
						else if(manager.hasArgTabComplete(3, "KeepSpawnInMemory"))
						{
							manager.setArgTabComplete(4, List.of("TRUE", "FALSE"));
						}
						else if(manager.hasArgTabComplete(3, "BorderRadius"))
						{
							manager.setArgTabComplete(4, manager.getValuesRangeList(0, 10000));
						}
						else if(manager.hasArgTabComplete(3, "BorderSquared"))
						{
							manager.setArgTabComplete(4, List.of("TRUE", "FALSE"));
						}
						else if(manager.hasArgTabComplete(3, "GameRules"))
						{
							List<String> gameRulesList = new ArrayList<>();
							for(GameRule<?> gameRule : GameRule.values())
								gameRulesList.add(gameRule.getName());
							if(manager.hasArgOrSetTabComplete(4, gameRulesList))
							{
								if(manager.hasArg(4, List.of("maxCommandChainLength", "maxEntityCramming", "randomTickSpeed", "spawnRadius")))
									manager.setArgTabComplete(5, manager.getValuesRangeList(0, 100000));
								else
									manager.setArgTabComplete(5, List.of("TRUE", "FALSE"));
							}
						}
					}
			}
			else if(manager.hasArgTabComplete(1, "unload")) {
				manager.setArgTabComplete(2, manager.getWorldsNameListWithThis());
			}
			else if(manager.hasArgTabComplete(1, "remove")) {
				manager.setArgTabComplete(2, manager.getWorldsNameListWithThis());
			}
			else if(manager.hasArgTabComplete(1, "info")) {
				manager.setArgTabComplete(2, manager.getWorldsNameListWithThis());
			}
		}
		
		return manager.getTabComplete();
	}
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args, List.of("info|create|modify|load|unload|remove|reload ?"));
		
		if(manager.isPlayer())
			if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
				return manager.doReturn();
		
		manager.registerArgUsage(1, "info", "<world>");
		manager.registerArgUsage(1, "create", "<world>", "<WorldType>", "<GameMode>", "<Difficulty>", "<GenerateStructures>");
		manager.registerArgUsage(1, "modify", "<world>", "<property>", "<value>");
		manager.registerArgUsage(1, "load", "<world>");
		manager.registerArgUsage(1, "unload", "<world>");
		manager.registerArgUsage(1, "remove", "<world>");
		manager.registerArgUsage(1, "reload");
		
		if(manager.hasArgs(1) && manager.hasArgAndPermission(1, "reload"))
			this.doWorldManagerReload(manager);
		else if(manager.hasArgs(6) && manager.hasArgAndPermission(1, "create"))
			this.doWorldCreate(manager, manager.getArg(2), manager.getArg(3), manager.getArg(4), manager.getArg(5), manager.getArg(6));
		else if(manager.hasArgs(4) && manager.hasArgAndPermission(1, "modify"))
			this.doWorldModifyProperty(manager, manager.getChosenWorldFromArgWithThis(2, true), manager.getArg(3), manager.getArg(4));
		else if(manager.hasArgs(5) && manager.hasArgAndPermission(1, "modify") && manager.hasArgAndPermission(3, "GameRules"))
			this.doWorldModifyGameRule(manager, manager.getChosenWorldFromArgWithThis(2, true), manager.getArg(4), manager.getArg(5));
		else if(manager.hasArgs(2) && manager.hasArgAndPermission(1, "load"))
			this.doWorldLoad(manager, manager.getArg(2));
		else if(manager.hasArgs(2) && manager.hasArgAndPermission(1, "unload"))
			this.doWorldUnload(manager, manager.getChosenWorldFromArgWithThis(2, true));
		else if(manager.hasArgs(2) && manager.hasArgAndPermission(1, "remove"))
			this.doWorldRemove(manager, manager.getChosenWorldFromArgWithThis(2, true));
		else if(manager.hasArgs(2) && manager.hasArgAndPermission(1, "info"))
			this.doWorldInfo(manager, manager.getChosenWorldFromArgWithThis(2, true));
		
		return manager.doReturn();
	}
	
	private void doWorldInfo(CommandManager manager, World world) {
		if(world == null)
			return;
		
		manager.sendMessage("&4&l======= &c&l"+world.getName()+" &4&l=======");
		
		String worldType = Plugin.getPlugin().getConfig().getString("configWorlds."+world.getName()+".WorldType");
		if(worldType == null)
			manager.sendMessage("&6&lWorldType: &e-");
		else if(worldType.equalsIgnoreCase("EMPTY"))
			manager.sendMessage("&6&lWorldType: &eEMPTY");
		else
			manager.sendMessage("&6&lWorldType: &e"+WorldType.valueOf(worldType));

		String worldEnvironment = Plugin.getPlugin().getConfig().getString("configWorlds."+world.getName()+".WorldEnvironment");
		if(worldEnvironment == null)
			manager.sendMessage("&6&lWorldEnvironment: &e-");
		else
			manager.sendMessage("&6&lWorldEnvironment: &e"+Environment.valueOf(worldEnvironment));
		
		Boolean worldGenerateStructures = Plugin.getPlugin().getConfig().getBoolean("configWorlds."+world.getName()+".GenerateStructures");
		manager.sendMessage("&6&lWorldGenerateStructures: &e"+(worldGenerateStructures == null ? "-" : worldGenerateStructures.toString()));
		
		String worldGeneratorSettings = Plugin.getPlugin().getConfig().getString("configWorlds."+world.getName()+".WorldGeneratorSettings");
		if(worldGeneratorSettings == null)
			manager.sendMessage("&6&lWorldGeneratorSettings: &e-");
		else if(Function.isJSONValid(worldGeneratorSettings))
			manager.sendMessage("&6&lWorldGeneratorSettings: &e"+worldGeneratorSettings);
		else
			manager.sendMessage("&6&lWorldGeneratorSettings: &cINVALID");
		
		Boolean keepSpawnInMemory = Plugin.getPlugin().getConfig().getBoolean("configWorlds."+world.getName()+".KeepSpawnInMemory");
		manager.sendMessage("&6&lKeepSpawnInMemory: &e"+(keepSpawnInMemory == null ? "-" : keepSpawnInMemory.toString()));
		
		manager.sendMessage("&6&lGameMode: &e"+Model.getWorld(world).getGameMode().name());
		manager.sendMessage("&6&lDifficulty: &e"+world.getDifficulty().name());
		manager.sendMessage("&6&lBorderRadius: &e"+Model.getWorld(world).getBorderRadius());
		manager.sendMessage("&6&lBorderSquared: &e"+Model.getWorld(world).isBorderSquared());
		
		String gameRuleInfoList = "";
		for(GameRule<?> gameRule : GameRule.values())
		{
			gameRuleInfoList += "\n&6- " + gameRule.getName() + ": &e" + world.getGameRuleValue(gameRule);
		}
		manager.setReturnMessage("&6&lGameRules:"+(gameRuleInfoList==""?" &e-":gameRuleInfoList));
	}
	private void doWorldLoad(CommandManager manager, String worldNameStr) {
		
//		worldNameStr = worldNameStr.toLowerCase();
		
		if(worldNameStr.equalsIgnoreCase("this")) {
			manager.setReturnMessage("&cInvalid world name! You can't use: this");
			return;
		}
		
		Pattern p = Pattern.compile("[^a-zA-Z0-9_]");
		if(p.matcher(worldNameStr).find())
		{
			manager.setReturnMessage("&cInvalid world name! You can use only: a-zA-Z0-9_");
			return;
		}
		
		if(Bukkit.getWorld(worldNameStr) != null)
		{
			manager.setReturnMessage("&cWorld '"+worldNameStr+"' already loaded.");
			return;
		}
		
		File file = new File(Plugin.getPlugin().getServer().getWorldContainer().getAbsolutePath()+File.separator+worldNameStr);
		
		ConfigurationSection configWorld = Plugin.getPlugin().getConfig().getConfigurationSection("configWorlds."+worldNameStr);
		if(configWorld == null)
		{
			manager.setReturnMessage("&cConfig for world '"+worldNameStr+"' not exist.");
			return;
		}
		
		if(!file.isDirectory())
		{
			manager.setReturnMessage("&cDirectory for world '"+worldNameStr+"' not exist.");
			return;
		}

		Plugin.getPlugin().modifyWorldConfigProperty(worldNameStr, "WorldLoad", true);
		
		World createdWorld = Plugin.getPlugin().initWorldConfig(worldNameStr);
		Plugin.getPlugin().loadWorldConfig(createdWorld);
		
		manager.setReturnMessage("&aWorld '"+worldNameStr+"' loaded successfully.");
	}
	private void doWorldRemove(CommandManager manager, World world) {
		if(world == null)
			return;
		
		if(!doWorldUnload(manager, world))
			return;
		
		File file = new File(Plugin.getPlugin().getServer().getWorldContainer().getAbsolutePath()+File.separator+world.getName());
		File fileDest = new File(Plugin.getPlugin().getServer().getWorldContainer().getAbsolutePath()+File.separator+"removed_"+world.getName());
		
		if(fileDest.isDirectory()) {
			manager.setReturnMessage("&cDirectory for world '"+world.getName()+"' not removed! Directory '"+fileDest.getName()+"' already exist.");
			return;
		}
		
		try {
			Files.move(file, fileDest);
		} catch (IOException e) {
			manager.sendMessage("&cDirectory for world '"+world.getName()+"' not removed! (Exception) Error rename directory to '"+fileDest.getName()+"'.");
			manager.setReturnMessage(null);
			e.printStackTrace();
			return;
		}
		
		if(!fileDest.isDirectory()) {
			manager.setReturnMessage("&cDirectory for world '"+world.getName()+"' not removed! (Other) Error rename directory to '"+fileDest.getName()+"'.");
			return;
		}
		
		ConfigurationSection configurationSection = Plugin.getPlugin().getConfig().getConfigurationSection("configWorlds."+world.getName());
		Plugin.getPlugin().getConfig().set("configWorlds."+world.getName(), null);
		Plugin.getPlugin().getConfig().set("configWorlds.removed_"+world.getName(), configurationSection);
		Plugin.getPlugin().saveConfig();
		
		manager.setReturnMessage("&aWorld '"+world.getName()+"' removed successfully.");
	}
	private boolean doWorldUnload(CommandManager manager, World world) {
		if(world == null)
			return false;
		
		if(world == Papi.Server.getServerLobbyWorld()
				|| world == Papi.Server.getServerSpawnWorld()
				|| world == Papi.Server.getSurvivalWorld()
				|| world == Papi.Server.getNetherWorld()
				|| world == Papi.Server.getTheEndWorld())
		{
			manager.setReturnMessage("&c&oThis world is protected!");
			return false;
		}
		
		Plugin.getPlugin().getLogger().info("-> Unloading world '"+world.getName()+"'...");
		
		List<Player> worldPlayers = world.getPlayers();
		
		for(Player worldPlayer : worldPlayers) {
			if(worldPlayer.teleport(Papi.Server.getServerSpawnLocation()))
				Plugin.getPlugin().getLogger().info("Player '"+worldPlayer.getName()+"' teleported to spawn sucessfully!");
			else
			{
				Plugin.getPlugin().getLogger().info("Player '"+worldPlayer.getName()+"' teleported to spawn failed!!!");
				manager.setReturnMessage("&cFailed teleport player &e"+worldPlayer.getName()+"&c to spawn!");
				Plugin.getPlugin().getLogger().info("World '"+world.getName()+"' unload aborted!!!");
				return false;
			}
		}

		Bukkit.unloadWorld(world, true);
		Model.removeWorld(world);
		
		Plugin.getPlugin().modifyWorldConfigProperty(world.getName(), "WorldLoad", false);
		
		Plugin.getPlugin().getLogger().info("World '"+world.getName()+"' unloaded sucessfully!");
		
		manager.sendMessage("&aPlayers teleported to spawn: "+worldPlayers.size());
		manager.sendMessage("&aWorld '"+world.getName()+"' unloaded successfully.");
		manager.setReturnMessage(null);
		return true;
	}
	private void doWorldManagerReload(CommandManager manager) {
		Plugin.getPlugin().getLogger().info("Reloading worlds configurations...");
		
	    Plugin.reloadAllConfigs();
	    
	    manager.setReturnMessage("&aReloaded successfully.");
	}
	private void doWorldCreate(CommandManager manager, String worldNameStr, String worldTypeStr, String gameModeStr, String difficultyStr, String generateStructuresStr) {
		
//		worldNameStr = worldNameStr.toLowerCase();
		
		if(worldNameStr.equalsIgnoreCase("this")) {
			manager.setReturnMessage("&cInvalid world name! You can't use: this");
			return;
		}
		
		Pattern p = Pattern.compile("[^a-zA-Z0-9_]");
		if(p.matcher(worldNameStr).find())
		{
			manager.setReturnMessage("&cInvalid world name! You can use only: a-zA-Z0-9_");
			return;
		}
		
		if(Bukkit.getWorld(worldNameStr) != null)
		{
			manager.setReturnMessage("&cWorld '"+worldNameStr+"' already loaded.");
			return;
		}
		
		File file = new File(Plugin.getPlugin().getServer().getWorldContainer().getAbsolutePath()+File.separator+worldNameStr);
		
		if(file.isDirectory())
		{
			manager.setReturnMessage("&cDirectory for world '"+worldNameStr+"' already exist.");
			return;
		}
		
		ConfigurationSection configWorld = Plugin.getPlugin().getConfig().getConfigurationSection("configWorlds."+worldNameStr);
		if(configWorld != null)
		{
			manager.setReturnMessage("&cConfig for world '"+worldNameStr+"' already exist.");
			return;
		}
		
		WorldType worldType;
		GameMode gameMode;
		Difficulty difficulty;
		boolean generateStructures;
		
		try {
			if(!worldTypeStr.equalsIgnoreCase("EMPTY")) { worldType = WorldType.getByName(worldTypeStr); }
		} catch(Exception ex) { manager.setReturnMessage("&cWorldType invalid"); return; }
		try {
			 gameMode = GameMode.valueOf(gameModeStr);
		} catch(Exception ex) { manager.setReturnMessage("&cGameMode invalid"); return; }
		try {
			difficulty = Difficulty.valueOf(difficultyStr);
		} catch(Exception ex) { manager.setReturnMessage("&cDifficulty invalid"); return; }
		try {
			generateStructures = Boolean.getBoolean(generateStructuresStr);
		} catch(Exception ex) { manager.setReturnMessage("&cGenerateStructures invalid"); return; }
		
		Plugin.getPlugin().createWorldConfig(worldNameStr, worldTypeStr, gameMode, difficulty, generateStructures);
		manager.setReturnMessage("&aSuccess. World '"+worldNameStr+"' created.");
	}
	
	private void doWorldModifyProperty(CommandManager manager, World world, String property, String value) {
		if(world == null)
			return;

		boolean error = false;
		if(property.equalsIgnoreCase("GameMode"))
		{
			if(value.equalsIgnoreCase("SURVIVAL")) {
				Plugin.getPlugin().modifyWorldConfigProperty(world.getName(), "GameMode", GameMode.SURVIVAL.name());
				Model.getWorld(world).setGameMode(GameMode.SURVIVAL);
				for(Player player : world.getPlayers()) {
					if(!player.hasPermission("mkp.worldmanager.bypass.gamemode"))
						player.setGameMode(GameMode.SURVIVAL);
				}
				manager.sendMessage("&aWorld '"+world.getName()+"' - GameMode changed to: "+GameMode.SURVIVAL.name());
			}
			else if(value.equalsIgnoreCase("CREATIVE")) {
				Plugin.getPlugin().modifyWorldConfigProperty(world.getName(), "GameMode", GameMode.CREATIVE.name());
				Model.getWorld(world).setGameMode(GameMode.CREATIVE);
				for(Player player : world.getPlayers()) {
					if(!player.hasPermission("mkp.worldmanager.bypass.gamemode"))
						player.setGameMode(GameMode.CREATIVE);
				}
				manager.sendMessage("&aWorld '"+world.getName()+"' - GameMode changed to: "+GameMode.CREATIVE.name());
			}
			else if(value.equalsIgnoreCase("ADVENTURE")) {
				Plugin.getPlugin().modifyWorldConfigProperty(world.getName(), "GameMode", GameMode.ADVENTURE.name());
				Model.getWorld(world).setGameMode(GameMode.ADVENTURE);
				for(Player player : world.getPlayers()) {
					if(!player.hasPermission("mkp.worldmanager.bypass.gamemode"))
						player.setGameMode(GameMode.ADVENTURE);
				}
				manager.sendMessage("&aWorld '"+world.getName()+"' - GameMode changed to: "+GameMode.ADVENTURE.name());
			}
			else if(value.equalsIgnoreCase("SPECTATOR")) {
				Plugin.getPlugin().modifyWorldConfigProperty(world.getName(), "GameMode", GameMode.SPECTATOR.name());
				Model.getWorld(world).setGameMode(GameMode.SPECTATOR);
				for(Player player : world.getPlayers()) {
					if(!player.hasPermission("mkp.worldmanager.bypass.gamemode"))
						player.setGameMode(GameMode.SPECTATOR);
				}
				manager.sendMessage("&aWorld '"+world.getName()+"' - GameMode changed to: "+GameMode.SPECTATOR.name());
			}
			else
				error = true;
		}
		else if(property.equalsIgnoreCase("Difficulty"))
		{
			if(value.equalsIgnoreCase("PEACEFUL")) {
				Plugin.getPlugin().modifyWorldConfigProperty(world.getName(), "Difficulty", Difficulty.PEACEFUL.name());
				world.setDifficulty(Difficulty.PEACEFUL);
				manager.sendMessage("&aWorld '"+world.getName()+"' - Difficulty changed to: "+Difficulty.PEACEFUL.name());
			}
			else if(value.equalsIgnoreCase("EASY")) {
				Plugin.getPlugin().modifyWorldConfigProperty(world.getName(), "Difficulty", Difficulty.EASY.name());
				world.setDifficulty(Difficulty.EASY);
				manager.sendMessage("&aWorld '"+world.getName()+"' - Difficulty changed to: "+Difficulty.EASY.name());
			}
			else if(value.equalsIgnoreCase("NORMAL")) {
				Plugin.getPlugin().modifyWorldConfigProperty(world.getName(), "Difficulty", Difficulty.NORMAL.name());
				world.setDifficulty(Difficulty.NORMAL);
				manager.sendMessage("&aWorld '"+world.getName()+"' - Difficulty changed to: "+Difficulty.NORMAL.name());
			}
			else if(value.equalsIgnoreCase("HARD")) {
				Plugin.getPlugin().modifyWorldConfigProperty(world.getName(), "Difficulty", Difficulty.HARD.name());
				world.setDifficulty(Difficulty.HARD);
				manager.sendMessage("&aWorld '"+world.getName()+"' - Difficulty changed to: "+Difficulty.HARD.name());
			}
			else
				error = true;
		}
		else if(property.equalsIgnoreCase("KeepSpawnInMemory"))
		{
			if(value.equalsIgnoreCase("TRUE")) {
				Plugin.getPlugin().modifyWorldConfigProperty(world.getName(), "KeepSpawnInMemory", true);
				world.setKeepSpawnInMemory(true);
				manager.sendMessage("&aWorld '"+world.getName()+"' - KeepSpawnInMemory changed to: "+true);
			}
			else if(value.equalsIgnoreCase("FALSE")) {
				Plugin.getPlugin().modifyWorldConfigProperty(world.getName(), "KeepSpawnInMemory", false);
				world.setKeepSpawnInMemory(false);
				manager.sendMessage("&aWorld '"+world.getName()+"' - KeepSpawnInMemory changed to: "+false);
			}
			else
				error = true;
		}
		else if(property.equalsIgnoreCase("BorderRadius"))
		{
			if(Papi.Function.isNumeric(value, true)) {
				int borderRadius = (int)Double.parseDouble(value);
				Plugin.getPlugin().modifyWorldConfigProperty(world.getName(), "Border.Radius", borderRadius);
				Model.getWorld(world).setBorderRadius(borderRadius);
				manager.sendMessage("&aWorld '"+world.getName()+"' - BorderRadius changed to: "+borderRadius);
			}
			else
				error = true;
		}
		else if(property.equalsIgnoreCase("BorderSquared"))
		{
			if(value.equalsIgnoreCase("TRUE")) {
				Plugin.getPlugin().modifyWorldConfigProperty(world.getName(), "Border.Squared", true);
				Model.getWorld(world).setBorderRadiusSquared(true);
				manager.sendMessage("&aWorld '"+world.getName()+"' - BorderSquared changed to: "+true);
			}
			else if(value.equalsIgnoreCase("FALSE")) {
				Plugin.getPlugin().modifyWorldConfigProperty(world.getName(), "Border.Squared", false);
				Model.getWorld(world).setBorderRadiusSquared(false);
				manager.sendMessage("&aWorld '"+world.getName()+"' - BorderSquared changed to: "+false);
			}
			else
				error = true;
		}
		else
			error = true;
		
		if(error)
			manager.setReturnMessage("&cSomething wrong...");
		else
			manager.setReturnMessage("&aSuccessfully changed");
	}
	
	private void doWorldModifyGameRule(CommandManager manager, World world, String gameRuleStr, String value) {
		if(world == null)
			return;
		
		GameRule gameRule = GameRule.getByName(gameRuleStr);
		
		if(gameRule == null)
		{
			manager.setReturnMessage("&cThis GameRule not found");
			return;
		}
		
		boolean error = false;
		if(gameRule == GameRule.MAX_COMMAND_CHAIN_LENGTH || gameRule == GameRule.MAX_ENTITY_CRAMMING || gameRule == GameRule.RANDOM_TICK_SPEED || gameRule == GameRule.SPAWN_RADIUS)
		{
			if(Papi.Function.isNumeric(value, true)) {
				int valueInt = (int)Double.parseDouble(value);
				Plugin.getPlugin().modifyWorldConfigGameRule(world.getName(), gameRule, valueInt);
				world.setGameRule(gameRule, valueInt);
				manager.sendMessage("&aWorld '"+world.getName()+"' - GameRule '"+gameRule.getName()+"' changed to: "+valueInt);
			}
			else
				error = true;
		}
		else
		{
			if(value.equalsIgnoreCase("TRUE")) {
				Plugin.getPlugin().modifyWorldConfigGameRule(world.getName(), gameRule, true);
				world.setGameRule(gameRule, true);
				manager.sendMessage("&aWorld '"+world.getName()+"' - GameRule '"+gameRule.getName()+"' changed to: "+true);
			}
			else if(value.equalsIgnoreCase("FALSE")) {
				Plugin.getPlugin().modifyWorldConfigGameRule(world.getName(), gameRule, false);
				world.setGameRule(gameRule, false);
				manager.sendMessage("&aWorld '"+world.getName()+"' - GameRule '"+gameRule.getName()+"' changed to: "+false);
			}
			else
				error = true;
		}
		
		if(error)
			manager.setReturnMessage("&cSomething wrong...");
		else
			manager.setReturnMessage("&aSuccessfully changed");
	}


}

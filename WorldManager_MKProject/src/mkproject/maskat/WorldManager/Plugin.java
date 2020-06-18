package mkproject.maskat.WorldManager;

import java.io.IOException;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import mkproject.maskat.Papi.Papi;

public class Plugin extends JavaPlugin {
	public static Plugin plugin;
	
	public void onEnable() {
		plugin = this;
		
		Event eventHandler = new Event();
		
		getServer().getPluginManager().registerEvents(eventHandler, this);
		
//		getCommand("spawn").setExecutor(executeCommand);
//		getCommand("setspawn").setExecutor(executeCommand);
		
		if(Papi.DEVELOPER_DIRECTORY_AUTODELETE) {
			try {
				FileUtils.deleteDirectory(getDataFolder());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.saveDefaultConfig();
		
		this.loadConfigsAndWorlds();
		
		getCommand("worldmanager").setExecutor(new ExecuteCommand());
		
		getLogger().info("Has been enabled!");
	}
	
	public static void reloadAllConfigs() {
		plugin.reloadConfig();
		plugin.loadConfigsAndWorlds();
	}
	
	private void loadConfigsAndWorlds() {
		getLogger().info("Loading server worlds...");
		
//		getServer().createWorld(new WorldCreator("world"));
//		getServer().createWorld(new WorldCreator("world_nether"));
//		getServer().createWorld(new WorldCreator("world_the_end"));
		
		ConfigurationSection configWorld = getConfig().getConfigurationSection("configWorlds");
		
		if(configWorld != null) {
			for(String worldName : configWorld.getKeys(false)) {
				getLogger().info("-> Loading world '"+worldName+"'...");
				if(worldName.equalsIgnoreCase("world") || worldName.equalsIgnoreCase("world_nether") || worldName.equalsIgnoreCase("world_the_end"))
				{
					getLogger().info("Pass... Default world managing from server.");
				}
				else
				{
					WorldCreator worldCreator = new WorldCreator(worldName);
					
					WorldType worldType = WorldType.valueOf(((String) loadWorldConfig(ValueType.STRING, "WorldType", worldName, "NORMAL")));
					worldCreator.type(worldType);
					
					Environment worldEnvironment = Environment.valueOf(((String) loadWorldConfig(ValueType.STRING, "WorldEnvironment", worldName, "NORMAL")));
					worldCreator.environment(worldEnvironment);
					
					boolean worldGenerateStructures = (boolean) loadWorldConfig(ValueType.BOOLEAN, "GenerateStructures", worldName, true);
					worldCreator.generateStructures(worldGenerateStructures);
					
					String worldGeneratorSettings = (String) loadWorldConfig(ValueType.STRING, "WorldGeneratorSettings", worldName, "{\"layers\": [{\"block\": \"stone\", \"height\": 1}, {\"block\": \"grass\", \"height\": 1}], \"biome\":\"plains\"}");
					
					if(Function.isJSONValid(worldGeneratorSettings))
						worldCreator.generatorSettings(worldGeneratorSettings);
					else
						getLogger().warning("Value for 'WorldGeneratorSettings' is invalid !!!");
					
					getLogger().info("(info) Generator: "+worldCreator.generator());
					getLogger().info("(info) GeneratorSettings: "+worldCreator.generatorSettings());
					
					getServer().createWorld(worldCreator);
					getLogger().info("World '"+worldName+"' loaded.");
				}
			}
		}
		
		for(World world : getServer().getWorlds())
		{
			getLogger().info("-> Preparing world '"+world.getName()+"'...");
			
			GameMode gameMode = GameMode.valueOf(((String) loadWorldConfig(ValueType.STRING, "GameMode", world, GameMode.SURVIVAL.name())));
			
			Difficulty difficulty = Difficulty.valueOf(((String) loadWorldConfig(ValueType.STRING, "Difficulty", world, Difficulty.PEACEFUL.name())));
			world.setDifficulty(difficulty);
			
			boolean keepspawninmemory = (boolean) loadWorldConfig(ValueType.BOOLEAN, "KeepSpawnInMemory", world, false);
			world.setKeepSpawnInMemory(keepspawninmemory);
			
			int borderRadius = (int) loadWorldConfig(ValueType.INTEGER, "Border.Radius", world, -1);
			
			boolean borderSquared = (boolean) loadWorldConfig(ValueType.BOOLEAN, "Border.Squared", world, false);
			
//			boolean allowWeather = getConfig().getBoolean("configWorlds."+world.getName()+".AllowWeather");
//			getLogger().info("AllowWeather: "+allowWeather);
//			if(!allowWeather) {
//				world.setStorm(false);
//				world.setThundering(false);
//			}
			
			for(GameRule<?> gameRule : GameRule.values())
				loadGameRule(world, gameRule);
			
			Model.addWorld(world, gameMode, borderRadius, borderSquared);//, allowWeather);
			
			getLogger().info("Loaded world '"+world.getName()+"' sucessfully!");
		}
		
		getLogger().info("Done. All worlds is loaded :)");
		this.saveConfig();
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
	
	private enum ValueType { 
		BOOLEAN,
		INTEGER,
		STRING,
	}
	
	private Object loadWorldConfig(ValueType valueType, String path, World world, Object defaultValue) {
		return loadWorldConfig(valueType, path, world.getName(), defaultValue);
	}
	private Object loadWorldConfig(ValueType valueType, String path, String worldName, Object defaultValue) {
		String fullpath = "configWorlds."+worldName+"."+path;
		
//		Object value = null;
//		if(valueType == ValueType.BOOLEAN)
//			value = getConfig().getBoolean(fullpath);
//		else if(valueType == ValueType.INTEGER)
//			value = getConfig().getInt(fullpath);
//		else if(valueType == ValueType.STRING)
//			value = getConfig().getString(fullpath);
		Object value = null;
		if(valueType == ValueType.BOOLEAN)
			value = getConfig().getObject(fullpath, Boolean.class);
		else if(valueType == ValueType.INTEGER)
			value = getConfig().getObject(fullpath, Integer.class);
		else if(valueType == ValueType.STRING)
			value = getConfig().getObject(fullpath, String.class);
		
		if(value == null)
		{
			value = defaultValue;
			getConfig().set(fullpath, defaultValue);
		}
		
		if(value.equals(defaultValue))
			getLogger().info(path.replace(".", " ") + ": " + value + " (default)");
		else
			getLogger().info(path.replace(".", " ") + ": " + value);
		return value;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadGameRule(World world, GameRule gameRule) {
		Object value = getConfig().getObject("configWorlds."+world.getName()+".GameRules."+gameRule.getName(), Object.class);
		if(value == null)
		{
			value = world.getGameRuleValue(gameRule) + " (new config)";
			getConfig().set("configWorlds."+world.getName()+".GameRules."+gameRule.getName(), world.getGameRuleValue(gameRule));
		}
		else
			world.setGameRule(gameRule, value);
		getLogger().info("GameRule "+gameRule.getName() + ": " + value);
	}
}

package mkproject.maskat.WorldManager;

import java.io.File;
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
import mkproject.maskat.Papi.Utils.CommandManager;
import mkproject.maskat.WorldManager.WorldGenerators.CustomWorldGenerator;

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
		
		CommandManager.initCommand(this, "worldmanager", new CmdWorldManager(), true);
//		getCommand("worldmanager").setExecutor(new ExecuteCommand());
		
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
//				worldName = worldName.toLowerCase();
				this.initWorldConfig(worldName);
			}
		}
		
		for(World world : getServer().getWorlds())
		{
			this.loadWorldConfig(world);
		}
		
		getLogger().info("Done. All worlds is loaded :)");
		this.saveConfig();
	}
	
	protected World initWorldConfig(String worldName) {
		getLogger().info("-> Loading world '"+worldName+"'...");
		if(worldName.equalsIgnoreCase("world") || worldName.equalsIgnoreCase("world_nether") || worldName.equalsIgnoreCase("world_the_end"))
		{
			getLogger().info("Pass... Default world managing from server.");
		}
		else if(worldName.equalsIgnoreCase("this"))
		{
			getLogger().info("Pass... Invalid world name!!!");
		}
		else
		{
			boolean worldLoaded = (boolean) loadWorldConfig(ValueType.BOOLEAN, "WorldLoad", worldName, true);
			if(worldLoaded)
			{
				WorldCreator worldCreator = new WorldCreator(worldName);
				
				String worldTypeStr = (String) loadWorldConfig(ValueType.STRING, "WorldType", worldName, WorldType.NORMAL.name());
				
				if(worldTypeStr.equalsIgnoreCase("EMPTY")) {
					CustomWorldGenerator.setEmpty(worldCreator);
					getLogger().info("Succesful set custom world type: EMPTY");
				}
				else
				{
					WorldType worldType = WorldType.valueOf(worldTypeStr);
					worldCreator.type(worldType);
				}
				
				Environment worldEnvironment = Environment.valueOf(((String) loadWorldConfig(ValueType.STRING, "WorldEnvironment", worldName, Environment.NORMAL.name())));
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
				
				World createdWorld = getServer().createWorld(worldCreator);
				if(createdWorld != null)
				{
					getLogger().info("World '"+worldName+"' loaded.");
					return createdWorld;
				}
				else
					getLogger().info("World '"+worldName+"' loading error!");
			}
			else
			{
				File file = new File(Plugin.getPlugin().getServer().getWorldContainer().getAbsolutePath()+File.separator+worldName);
				
				if(!file.isDirectory())
				{
					Plugin.getPlugin().getConfig().set("configWorlds."+worldName, null);
					getLogger().info("World '"+worldName+"' not loaded. Direcotry not exist. World config removed.");
				}
				else
					getLogger().info("World '"+worldName+"' not loaded.");
			}
		}
		return null;
	}

	public static Plugin getPlugin() {
		return plugin;
	}
	
	private enum ValueType { 
		BOOLEAN,
		INTEGER,
		STRING,
	}
	
	private void prepareWorld(World world, GameMode gameMode, Difficulty difficulty, boolean keepspawninmemory, int borderRadius, boolean borderSquared) {
		world.setDifficulty(difficulty);
		world.setKeepSpawnInMemory(keepspawninmemory);
		
//		boolean allowWeather = getConfig().getBoolean("configWorlds."+world.getName()+".AllowWeather");
//		getLogger().info("AllowWeather: "+allowWeather);
//		if(!allowWeather) {
//			world.setStorm(false);
//			world.setThundering(false);
//		}
		
		for(GameRule<?> gameRule : GameRule.values())
			loadGameRule(world, gameRule);
		
		Model.addWorld(world, gameMode, borderRadius, borderSquared);//, allowWeather);
	}
	
	protected boolean modifyWorldConfigProperty(String worldName, String property, Object value) 
	{
		if(worldName.equalsIgnoreCase("this"))
			return false;
		
		if(getConfig().getConfigurationSection("configWorlds."+worldName) == null)
			return false;
		
		getConfig().set("configWorlds."+worldName+"."+property, value);
		this.saveConfig();
		return true;
	}
	
	protected boolean modifyWorldConfigGameRule(String worldName, GameRule gameRule, Object value) 
	{
		if(worldName.equalsIgnoreCase("this"))
			return false;
		
		if(getConfig().getConfigurationSection("configWorlds."+worldName) == null)
			return false;
		
		getConfig().set("configWorlds."+worldName+".GameRules."+gameRule.getName(), value);
		this.saveConfig();
		return true;
	}
	
	protected void loadWorldConfig(World world) {
		getLogger().info("-> Preparing world '"+world.getName()+"'...");
		
		GameMode gameMode = GameMode.valueOf(((String) loadWorldConfig(ValueType.STRING, "GameMode", world, GameMode.SURVIVAL.name())));
		
		Difficulty difficulty = Difficulty.valueOf(((String) loadWorldConfig(ValueType.STRING, "Difficulty", world, Difficulty.PEACEFUL.name())));
		world.setDifficulty(difficulty);
		
		boolean keepspawninmemory = (boolean) loadWorldConfig(ValueType.BOOLEAN, "KeepSpawnInMemory", world, false);
		world.setKeepSpawnInMemory(keepspawninmemory);
		
		int borderRadius = (int) loadWorldConfig(ValueType.INTEGER, "Border.Radius", world, -1);
		
		boolean borderSquared = (boolean) loadWorldConfig(ValueType.BOOLEAN, "Border.Squared", world, false);
		
		prepareWorld(world, gameMode, difficulty, keepspawninmemory, borderRadius, borderSquared);
		
//		boolean allowWeather = getConfig().getBoolean("configWorlds."+world.getName()+".AllowWeather");
//		getLogger().info("AllowWeather: "+allowWeather);
//		if(!allowWeather) {
//			world.setStorm(false);
//			world.setThundering(false);
//		}
		
		for(GameRule<?> gameRule : GameRule.values())
			loadGameRule(world, gameRule);
		
//		Model.addWorld(world, gameMode, borderRadius, borderSquared);//, allowWeather);
		
		getLogger().info("Loaded world '"+world.getName()+"' sucessfully!");
	}
	
	protected void createWorldConfig(String worldName, String worldTypeStr, GameMode gameMode, Difficulty difficulty, boolean generateStructures) {
		
		getLogger().info("-> Creating world '"+worldName+"'...");
		
		if(worldName.equalsIgnoreCase("this")) {
			getLogger().info("-> Canceled! Invalid world name!!!");
			return;
		}
		
		WorldCreator worldCreator = new WorldCreator(worldName);
		
		WorldType worldType = null;
		if(worldTypeStr.equalsIgnoreCase("EMPTY"))
			CustomWorldGenerator.setEmpty(worldCreator);
		else
		{
			worldType = WorldType.valueOf(worldTypeStr);
			worldCreator.type(worldType);
		}
		
		Environment environment = Environment.NORMAL;
		worldCreator.environment(environment);
		
		worldCreator.generateStructures(generateStructures);
		
		String worldGeneratorSettings = "{\"layers\": [{\"block\": \"stone\", \"height\": 1}, {\"block\": \"grass\", \"height\": 1}], \"biome\":\"plains\"}";
		worldCreator.generatorSettings();
		
		World world = getServer().createWorld(worldCreator);
		
		getConfig().set("configWorlds."+world.getName()+"."+"WorldType", worldTypeStr.equalsIgnoreCase("EMPTY") ? "EMPTY" : worldType.name());
		getConfig().set("configWorlds."+world.getName()+"."+"WorldEnvironment", environment.name());
		getConfig().set("configWorlds."+world.getName()+"."+"GenerateStructures", generateStructures);
		getConfig().set("configWorlds."+world.getName()+"."+"WorldGeneratorSettings", worldGeneratorSettings);
		
		getLogger().info("-> Preparing world '"+world.getName()+"'...");
		
		int borderRadius = 200;
		boolean borderSquared = false;
		
		prepareWorld(world, gameMode, difficulty, false, borderRadius, borderSquared);
		
		getConfig().set("configWorlds."+world.getName()+"."+"GameMode", gameMode.name());
		getConfig().set("configWorlds."+world.getName()+"."+"Difficulty", difficulty.name());
		getConfig().set("configWorlds."+world.getName()+"."+"KeepSpawnInMemory", false);
		getConfig().set("configWorlds."+world.getName()+"."+"Border.Radius", borderRadius);
		getConfig().set("configWorlds."+world.getName()+"."+"Border.Squared", borderSquared);
		
		for(GameRule<?> gameRule : GameRule.values())
			loadGameRule(world, gameRule);
		
		Model.addWorld(world, gameMode, borderRadius, borderSquared);//, allowWeather);
		
		getLogger().info("Loaded world '"+world.getName()+"' sucessfully!");
		
		this.saveConfig();
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

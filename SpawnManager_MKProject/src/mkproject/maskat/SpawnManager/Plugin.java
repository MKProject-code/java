package mkproject.maskat.SpawnManager;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import mkproject.maskat.Papi.Papi;

public class Plugin extends JavaPlugin {
	public static Plugin plugin;
	public static String PERMISSION_PREFIX = "mkp.spawnmanager";
	
	public void onEnable() {
		plugin = this;
		
		//ExecuteCommand executeCommand = new ExecuteCommand();
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
		
		Database.initialize(this);
		
		try {
			World world = Bukkit.getWorld(getConfig().getString("ServerSpawnLocation.World"));
			double posX = getConfig().getDouble("ServerSpawnLocation.X");
			double posY = getConfig().getDouble("ServerSpawnLocation.Y");
			double posZ = getConfig().getDouble("ServerSpawnLocation.Z");
			double yaw = getConfig().getDouble("ServerSpawnLocation.Yaw");
			double pitch = getConfig().getDouble("ServerSpawnLocation.Pitch");
			getLogger().info("Server spawnpoint for player location: "+world.getName()+", X="+posX+", Y="+posY+", Z="+posZ+", Yaw="+yaw+", Pitch="+pitch);
			Location spawnLoc = new Location(world, posX, posY, posZ, (float)yaw, (float)pitch);
			world.setSpawnLocation(spawnLoc);
			Papi.Server.initServerSpawnLocation(spawnLoc);
			getLogger().info("Server spawnpoint is initialized :)");
		} catch(Exception ex) {
			getLogger().warning("******* Server spawnpoint not initialized *******");
			getLogger().warning("******* Server spawnpoint not initialized *******");
			getLogger().warning("******* Server spawnpoint not initialized *******");
		}
		
		getLogger().info("Has been enabled!");
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
}

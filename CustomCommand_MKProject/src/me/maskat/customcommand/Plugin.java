package me.maskat.customcommand;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	
    private File db = null;
    public YamlConfiguration database = new YamlConfiguration();
    
    
    @Override
    public void onEnable() {
        getCommand("c").setExecutor(new ExecuteCommand(this));
        
        this.db = new File(this.getDataFolder(), "database.yml");
        this.mkdir();
        this.loadYamls();
    }
    
    public void onDisable() {
    	this.saveDatabase();
    }
    
    private void mkdir() {
        if (!this.db.exists()) {
        	this.saveResource("database.yml", false);
        }
    }
    
    private void loadYamls() {
        try {
        	this.database.load(this.db);
        }
        catch (IOException e1) {
        	e1.printStackTrace();
        }
        catch (InvalidConfigurationException e2) {
        	e2.printStackTrace();
        }
    }
    
    public YamlConfiguration getDatabase() {
        return this.database;
    }
    
    public void saveDatabase() {
    	try {
    		this.database.save(this.db);
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public boolean putDatabasePlayerLastLocation(Player player, Location location) {
    	String saveWorldName = location.getWorld().getName();
    	if(location.getWorld().getName().equalsIgnoreCase("world_nether") || location.getWorld().getName().equalsIgnoreCase("world_the_end") || location.getWorld().getName().equalsIgnoreCase("world_spawncenter"))
    		saveWorldName = "world";
    	
    	String playerName = player.getName().toLowerCase();
    	String locationCoords = location.getX()+","+location.getY()+","+location.getZ()+","+location.getWorld().getName()+","+location.getYaw()+","+location.getPitch();
    	database.set(playerName+".lastlocation."+saveWorldName, locationCoords);
    	
    	return true;
    }
    
    public Location getDatabasePlayerLastLocation(Player player, String worldName) {
    	String data = database.getString(player.getName().toLowerCase()+".lastlocation."+worldName);
    	if(data == null)
    		return null;
    	String[] lastlocation = data.split(",");
    	
    	String world = worldName;
    	if(lastlocation.length >= 4)
    		world = lastlocation[3];
    	
    	if(lastlocation.length == 6)
    		return new Location(Bukkit.getServer().getWorld(world), Double.parseDouble(lastlocation[0]), Double.parseDouble(lastlocation[1]), Double.parseDouble(lastlocation[2]), Float.parseFloat(lastlocation[4]), Float.parseFloat(lastlocation[5]));
    	
    	return new Location(Bukkit.getServer().getWorld(world), Double.parseDouble(lastlocation[0]), Double.parseDouble(lastlocation[1]), Double.parseDouble(lastlocation[2]));
    }
}
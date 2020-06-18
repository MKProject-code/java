package mkproject.maskat.Papi;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import mkproject.maskat.Papi.MySQL.MySQL_Config;
import mkproject.maskat.Papi.MySQL.PapiMySQL;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class PapiPlugin extends JavaPlugin {
	private static PapiPlugin plugin;
	private static WorldEditPlugin worldEditPlugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		worldEditPlugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		
		getLogger().info("Initializing API for MKProject by MasKAT...");
		
		getServer().getPluginManager().registerEvents(new PapiEvent(), this);
		
		if(Papi.DEVELOPER_DIRECTORY_AUTODELETE) {
			try {
				FileUtils.deleteDirectory(getDataFolder());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.saveDefaultConfig();
		
        if (!setupEconomy() ) {
        	getLogger().warning("*********************************************************************");
        	getLogger().warning("********* ERROR: Disabled due to no Vault dependency found! *********");
        	getLogger().warning("*********************************************************************");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!setupPermissions() ) {
        	getLogger().warning("*********************************************************************");
        	getLogger().warning("********* ERROR: Disabled due to no Vault dependency found! *********");
        	getLogger().warning("*********************************************************************");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!setupChat() ) {
        	getLogger().warning("*********************************************************************");
        	getLogger().warning("********* ERROR: Disabled due to no Vault dependency found! *********");
        	getLogger().warning("*********************************************************************");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        MySQL_Config.init(getConfig().getString("MySQL.Hostname"),
        		getConfig().getString("MySQL.Port"),
        		getConfig().getString("MySQL.Username"),
        		getConfig().getString("MySQL.Password"),
        		getConfig().getString("MySQL.Database"),
        		getConfig().getBoolean("MySQL.SSL"),
        		getConfig().getBoolean("MySQL.DebugConsole"));
        
        PapiMySQL.connect();
        
		if(PapiMySQL.isConnected())
			getLogger().info("MySQL is connected :)");
		else
		{
			getLogger().warning("***************** ERROR: MySQL is not connected *****************");
			getLogger().warning("***************** ERROR: MySQL is not connected *****************");
			getLogger().warning("***************** ERROR: MySQL is not connected *****************");
			getServer().getPluginManager().disablePlugin(this);
		}
		
        PapiTask.runCheckAfkPlayersTask(getConfig().getInt("Player.CheckAfkStatusTime"));
        
        PapiServer.initServerSpawnWorld(Bukkit.getServer().getWorld(getConfigValueString("Server.SpawnWorldName")));
        PapiServer.initSurvivalWorld(Bukkit.getServer().getWorld(getConfigValueString("Server.SurvivalWorldName")));
        
        getServer().getScheduler().runTaskTimer(this, new TPS(), 100L, 1L);
        
        new PapiThread().start();
        
		getLogger().info("Has been enabled!");
	}
	
	protected String getConfigValueString(String configKey) {
		String value = getConfig().getString(configKey);
        if(value == null)
        {
        	getLogger().warning("***************** ERROR: Config \""+configKey+"\" is wrong! *****************");
        	getLogger().warning("***************** ERROR: Config \""+configKey+"\" is wrong! *****************");
        	getLogger().warning("***************** ERROR: Config \""+configKey+"\" is wrong! *****************");
        }
        return value;
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Has been disabled!");
	}
	
	public static PapiPlugin getPlugin() {
		return plugin;
	}
	
	public static WorldEditPlugin getWorldEditPlugin() {
		return worldEditPlugin;
	}
	
    private boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        PapiVault.econ = rsp.getProvider();
        return PapiVault.econ != null;
    }
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        PapiVault.perms = rsp.getProvider();
        return PapiVault.perms != null;
    }
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            return false;
        }
        PapiVault.chat = rsp.getProvider();
        return PapiVault.chat != null;
    }
}
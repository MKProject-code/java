package me.maskat.wolfsecurity;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.maskat.mysql.MySQL;
import me.maskat.mysql.MySQL_Config;
import me.maskat.mysql.SQL;
import me.maskat.wolfsecurity.Cmds.CmdWolf;
import me.maskat.wolfsecurity.models.Model;
import me.maskat.wolfsecurity.models.Model.Wolves;
import me.maskat.wolfsecurity.models.ModelPlayer;
import me.maskat.wolfsecurity.models.ModelRegion;
import me.maskat.wolfsecurity.models.ModelUser;
import me.maskat.wolfsecurity.models.ModelWolf;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.milkbowl.vault.economy.Economy;  
public class Plugin extends JavaPlugin {
	
	public static String PERMISSION_PREFIX = "mkp.wolfsecurity";
	
	public static Plugin plugin = null;
	public static CoreProtectAPI coreProtectAPI = null;
	private File c = null;
	private File l = null;
	public YamlConfiguration config = new YamlConfiguration();
	public YamlConfiguration lang = new YamlConfiguration();
	
	//private LandSecurityTask task = new LandSecurityTask();
	
	public Map<Integer, ModelRegion> mapRegions = new HashMap<>();
	public Map<Integer, ModelUser> mapUsers = new HashMap<>();
	public Map<Integer, ModelWolf> mapWolves = new HashMap<>();
	public Map<Player, ModelPlayer> mapPlayers = new HashMap<>();
	
	public Economy economy = null;
	private static World allowedWorld = null;
	
	@Override
	public void onEnable() {
		
		getServer().getLogger().info("Hello! My developllo! My developer is MasKAT from skyidea.pl ---> Plugin enabling...");
		
		plugin = this;
		
		getServer().getLogger().info("Initialize CoreProtectAPI...");
		
		coreProtectAPI = getCoreProtect();
		
		if (coreProtectAPI != null){ //Ensure we have access to the API
			coreProtectAPI.testAPI(); //Will print out "[CoreProtect] API Test Successful." in the console.
		}
		else
			getServer().getLogger().warning("******************************* CoreProtectAPI not found !!! *******************************");
		
		Event event = new Event();
		c = new File(this.getDataFolder(), "config.yml");
		l = new File(this.getDataFolder(), "lang.yml");
		getServer().getPluginManager().registerEvents(event, this);
		
		//getCommand("wolf").setExecutor((CommandExecutor)new ExecuteCmd());
		getCommand("wolf").setExecutor((CommandExecutor)new CmdWolf());
		
        this.mkdir();
        this.loadYamls();
        this.saveDefaultConfig();
        
        allowedWorld = this.getServer().getWorld(Config.allowedWorld);
        
		Config.init();
		
		try {
			initDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		setupEconomy();
		
		//Task.TaskCheckPlayerWolfGuard();
		
		
		
//        BukkitScheduler scheduler = getServer().getScheduler();
//        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
//            @Override
//            public void run() {
//            	//LandSecurityTask.TaskCheckAllPlayers();
//            	LandSecurityTask.TaskCheckAllPlayers();
//            }
//        }, 0L, 20L);
		getServer().getLogger().info("Hello! My developllo! My developer is MasKAT from skyidea.pl ---> Plugin enabled! :)");
	}
	
	public static CoreProtectAPI getCoreProtectAPI() {
		return coreProtectAPI;
	}
	
	private CoreProtectAPI getCoreProtect() {
		org.bukkit.plugin.Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");
     
        // Check that CoreProtect is loaded
        if (plugin == null || !(plugin instanceof CoreProtect)) {
            return null;
        }

        // Check that the API is enabled
        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
        if (CoreProtect.isEnabled() == false) {
            return null;
        }

        // Check that a compatible version of the API is loaded
        if (CoreProtect.APIVersion() < 6) {
            return null;
        }

        return CoreProtect;
}

	private void mkdir() {
        if (!c.exists()) {
            saveResource("config.yml", false);
        }
        if (!l.exists()) {
        	saveResource("lang.yml", false);
        }
    }
    
    private void loadYamls() {
        try {
            config.load(c);
            lang.load(l);
	    }
        catch (IOException | InvalidConfigurationException e) {
	        e.printStackTrace();
	    }
    }
//    
//    public YamlConfiguration getConfig() {
//        return config;
//    }
//    
//    public YamlConfiguration getLang() {
//    	return lang;
//    }

    public void onDisable() {
    	System.out.println("[WolfSecurity] Saving data...");
    	for(World w: Bukkit.getServer().getWorlds()) {
            for(Entity e: w.getEntities()) {
            	int wolfid = Wolves.getWolfId(e.getUniqueId());
            	if(wolfid > 0)
            		Model.Wolf(wolfid).setLastLocation(e.getLocation());
            }
        }
    	System.out.println("[WolfSecurity] Wolves location saved :)");
    	MySQL.disconnect();
    	System.out.println("[WolfSecurity] MySQL is disconnected :)");
    }
    
    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
    
    
	private void initDatabase() throws SQLException
	{
        MySQL_Config.init(config.getString("database.host"),
        		config.getString("database.port"),
        		config.getString("database.user"),
        		config.getString("database.password"),
        		config.getString("database.database"),
        		config.getBoolean("database.ssl")
        		);
        MySQL.connect();
        
		//String databaseTableRegions = config.getString("database.table.regions");
		//String databaseTableUsers = config.getString("database.table.users");
		//String databaseTableWolves = config.getString("database.table.wolves");
		
		if(MySQL.isConnected())
			System.out.println("[WolfSecurity] MySQL is connected :)");
		else
			System.out.println("\n"
					+ "     [WolfSecurity] ***************** ERROR: MySQL is not connected *****************\n"
					+ "     [WolfSecurity] ***************** ERROR: MySQL is not connected *****************\n"
					+ "     [WolfSecurity] ***************** ERROR: MySQL is not connected *****************\n"
					+ "     [WolfSecurity] ***************** ERROR: MySQL is not connected *****************\n"
					+ "     [WolfSecurity] ***************** ERROR: MySQL is not connected *****************\n"
					+ "     [WolfSecurity] ***************** ERROR: MySQL is not connected *****************\n"
					+ "     [WolfSecurity] ***************** ERROR: MySQL is not connected *****************\n"
					+ "     [WolfSecurity] ***************** ERROR: MySQL is not connected *****************");
		
		if(Config.developDB && Config.deleteDB)
		{
			SQL.deleteTable(Config.databaseTableRegions);
			SQL.deleteTable(Config.databaseTableUsers);
			SQL.deleteTable(Config.databaseTableWolves);
		}
		
		boolean result = SQL.createTable(Config.databaseTableRegions, "regionid INT (16) NOT NULL AUTO_INCREMENT UNIQUE, regionname VARCHAR(64) NOT NULL, pos1x INT(16), pos1z INT(16), pos2x INT(16), pos2z INT(16), world VARCHAR(32), claimed BOOLEAN NOT NULL DEFAULT FALSE, claimed_datetime VARCHAR(32) NULL DEFAULT NULL, protection BOOLEAN NOT NULL DEFAULT FALSE, owners VARCHAR(256) NOT NULL, family VARCHAR(256), friends VARCHAR(256), enemies VARCHAR(256), PRIMARY KEY (regionid, regionname)");
		this.getLogger().info("MySQL: Created regions table result: " + result);
		result = SQL.createTable(Config.databaseTableUsers, "userid INT (16) NOT NULL AUTO_INCREMENT UNIQUE, username VARCHAR(32) NOT NULL UNIQUE, useruuid VARCHAR(64) NOT NULL UNIQUE, assigned_wolfid INT(16) UNIQUE, PRIMARY KEY (userid, username)");
		this.getLogger().info("MySQL: Created members table result: " + result);
		result = SQL.createTable(Config.databaseTableWolves, "wolfid INT (16) NOT NULL AUTO_INCREMENT UNIQUE, wolfenityUUID VARCHAR(64) NOT NULL UNIQUE, wolfname VARCHAR(32) NOT NULL, wolfcollarcolorid INT (16) NOT NULL, wolflevel INT(6) NOT NULL DEFAULT '1', wolflastlocation VARCHAR(256) NULL DEFAULT NULL, PRIMARY KEY (wolfid)");
		this.getLogger().info("MySQL: Created wolves table result: " + result);
		
		//SQL.insertData("regionname,pos1x,pos1z,pos2x,pos2z,world,owners,members", "'TesT', '118', '337', '108', '332','world','"+Arrays.toString(new int[] {1})+"','"+Arrays.toString(new int[] {2,3,4})+"'", databaseTableRegions);
//		SQL.insertData("username,real_username", "'maskat', 'MasKAT'", databaseTableMembers);
		
		try {
			getRegionsData(Config.databaseTableRegions);
			getUsersData(Config.databaseTableUsers);
			getWolvesData(Config.databaseTableWolves);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getRegionsData(String databaseTableRegions) throws SQLException {
		ResultSet rs = MySQL.query("SELECT * FROM `"+databaseTableRegions+"`");

		while (rs.next()) {
			int regionid = rs.getInt("regionid");

			mapRegions.putIfAbsent(regionid, new ModelRegion(rs.getInt("regionid"),rs.getString("regionname"),Function.StringToIntegerArrayList(rs.getString("owners")), rs.getBoolean("protection")));
//			mapRegions.get(regionid).setId();
//			mapRegions.get(regionid).setName();
			//mapRegions.get(regionid).setRealName(rs.getString("real_regionname"));
			mapRegions.get(regionid).setPositionFirstX(rs.getInt("pos1x"));
			mapRegions.get(regionid).setPositionFirstZ(rs.getInt("pos1z"));
			mapRegions.get(regionid).setPositionSecoundX(rs.getInt("pos2x"));
			mapRegions.get(regionid).setPositionSecoundZ(rs.getInt("pos2z"));
			mapRegions.get(regionid).setWorld(rs.getString("world"));
			mapRegions.get(regionid).setClaimed(rs.getInt("claimed") == 1 ? true : false);
			mapRegions.get(regionid).setClaimedDateTime(rs.getString("claimed_datetime"));
//		    mapRegions.get(regionid).setOwners();
		    mapRegions.get(regionid).setFamily(Function.StringToIntegerArrayList(rs.getString("family")));
		    mapRegions.get(regionid).setFriends(Function.StringToIntegerArrayList(rs.getString("friends")));
		    mapRegions.get(regionid).setEnemies(Function.StringToIntegerArrayList(rs.getString("enemies")));
		}
		this.getLogger().info("MySQL: Success load regions");
	}
	
	private void getUsersData(String databaseTableUsers) throws SQLException {
		ResultSet rs = MySQL.query("SELECT * FROM `"+databaseTableUsers+"`");
		
		while (rs.next()) {
			int id = rs.getInt("userid");
			
			mapUsers.putIfAbsent(id, new ModelUser());
			mapUsers.get(id).initId(rs.getInt("userid"));
			mapUsers.get(id).initName(rs.getString("username"));
			mapUsers.get(id).initUUID(UUID.fromString(rs.getString("useruuid")));
			//mapUsers.get(id).setRealName(rs.getString("real_username"));
			mapUsers.get(id).initAssignedWolfId(rs.getInt("assigned_wolfid"));
		}
		this.getLogger().info("MySQL: Success load users");
	}
	
    private void getWolvesData(String databaseTableWolves) throws SQLException {
		ResultSet rs = MySQL.query("SELECT * FROM `"+databaseTableWolves+"`");
		
		while (rs.next()) {
			int id = rs.getInt("wolfid");
			
			mapWolves.putIfAbsent(id, new ModelWolf());
			mapWolves.get(id).initId(rs.getInt("wolfid"));
			mapWolves.get(id).initEnityUUID(UUID.fromString(rs.getString("wolfenityUUID")));
			mapWolves.get(id).initName(rs.getString("wolfname"));
			mapWolves.get(id).initCollarColorId(rs.getInt("wolfcollarcolorid"));
			mapWolves.get(id).initWolfLevel(rs.getInt("wolflevel"));
			mapWolves.get(id).initLastLocation(rs.getString("wolflastlocation"));
			mapWolves.get(id).initEntityWolf();

			
			//TODO : Not working !!!
//			Wolf wolfentity = (Wolf)Bukkit.getServer().getEntity(mapWolves.get(id).getEnityUUID());
//			if(wolfentity != null && wolfentity.isValid())
//			{
//				mapWolves.get(id).initSitting((wolfentity).isSitting());
//			}
			
//			mapUsers.get(id).assigned_regionid = rs.getInt("assigned_regionid");
		}
		this.getLogger().info("MySQL: Success load wolves");
	}

	public static World getAllowedWorld() {
		return allowedWorld;
	}
}

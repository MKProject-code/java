package mkproject.maskat.LoginManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import mkproject.maskat.Papi.Papi;
import ru.ilyahiguti.authmetitle.title.TitleAnimation;
import ru.ilyahiguti.authmetitle.title.TitleDescriptionException;
import ru.ilyahiguti.authmetitle.nms.NMS;
import ru.ilyahiguti.authmetitle.nms.BukkitImpl;

public class Plugin extends JavaPlugin {
	private static Plugin plugin;
	private static YamlConfiguration languagYaml;
	
	private static Location entitySpawnLocation = null;
	
	private static NMS nms;
	
    private static TitleAnimation unregisteredAnimation;
    private static TitleAnimation unloggedAnimation;
    private static TitleAnimation authorizedAnimation;
    
	public void onEnable() {
		plugin = this;
		
        if (!setupNMS()) {
            getLogger().severe("Could not find support for this Bukkit version (" + getServer().getClass().getPackage().getName().substring(23) + ")");
            setEnabled(false);
            return;
        }
		
		ExecuteCommand executeCommand = new ExecuteCommand();
		Event eventHandler = new Event();
		
		getServer().getPluginManager().registerEvents(eventHandler, this);
        
		if(Papi.DEVELOPER_DIRECTORY_AUTODELETE) {
			try {
				FileUtils.deleteDirectory(getDataFolder());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
//		config = Papi.Yaml.registerConfig(this, "config.yml");
		this.saveDefaultConfig();
		
		Database.Initialize(this);
		
		languagYaml = Papi.Yaml.registerYaml(this, "lang.yml");
		
		getCommand("loginmanager").setExecutor(executeCommand);
		getCommand("login").setExecutor(executeCommand);
		getCommand("l").setExecutor(executeCommand);
		getCommand("reg").setExecutor(executeCommand);
		getCommand("register").setExecutor(executeCommand);
		
		loadTitleConfigs();
		
		try {
			World world = Bukkit.getWorld(getConfig().getString("EntitySpawnLocation.World"));
			double posX = getConfig().getDouble("EntitySpawnLocation.X");
			double posY = getConfig().getDouble("EntitySpawnLocation.Y");
			double posZ = getConfig().getDouble("EntitySpawnLocation.Z");
			getLogger().info("Server spawnpoint for entity location: "+world.getName()+", X="+posX+", Y="+posY+", Z="+posZ);
			
			entitySpawnLocation = new Location(world, posX, posY, posZ);
			getLogger().info("Entity spawnpoint is initialized :)");
		} catch(Exception ex) {
			getLogger().warning("******* Entity spawnpoint not initialized *******");
			getLogger().warning("******* Entity spawnpoint not initialized *******");
			getLogger().warning("******* Entity spawnpoint not initialized *******");
		}
		
        getLogger().info("Has been enabled!");
	}
	
	public static void reloadAllConfigs() {
		plugin.reloadConfig();
		languagYaml = Papi.Yaml.registerYaml(Plugin.getPlugin(), "lang.yml");
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
	public static YamlConfiguration getLanguageYaml() {
		return languagYaml;
	}
	public static Location getEntitySpawnLocation() {
		return entitySpawnLocation;
	}
	
    private void loadTitleConfigs() {
        //if (!new File(getDataFolder(), "title_config.yml").exists()) saveResource("title_config.yml", false);
        if (!new File(getDataFolder(), "title_unregister.yml").exists()) saveResource("title_unregister.yml", false);
        if (!new File(getDataFolder(), "title_login.yml").exists()) saveResource("title_login.yml", false);
        if (!new File(getDataFolder(), "title_auth.yml").exists()) saveResource("title_auth.yml", false);

        YamlConfiguration title_unregister = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "title_unregister.yml"));
        YamlConfiguration title_login = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "title_login.yml"));
        YamlConfiguration title_auth = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "title_auth.yml"));

        try {
            List<String> titleSList = title_unregister.getStringList("Title");
            boolean repeat = title_unregister.getBoolean("Repeat.Enabled");
            int repeatFrom = title_unregister.getInt("Repeat.Start_From");

            if (repeatFrom > (titleSList.size() - 1)) {
                repeatFrom = 0;
                getLogger().warning("Repeat_From value is greater than the title frames in Unregister Title, will use 0 index.");
            }

            unregisteredAnimation = new TitleAnimation(titleSList, repeat, repeatFrom);
        } catch (TitleDescriptionException e) {
            getLogger().severe("An error occurred while loading the Unregister Title: " + e.getMessage());
        }

        try {
            List<String> titleSList = title_login.getStringList("Title");
            boolean repeat = title_login.getBoolean("Repeat.Enabled");
            int repeatFrom = title_login.getInt("Repeat.Start_From");

            if (repeatFrom > (titleSList.size() - 1)) {
                repeatFrom = 0;
                getLogger().warning("Repeat_From value is greater than the title frames in Login Title, will use 0 index.");
            }

            unloggedAnimation = new TitleAnimation(titleSList, repeat, repeatFrom);
        } catch (TitleDescriptionException e) {
            getLogger().severe("An error occurred while loading the Login Title: " + e.getMessage());
        }

        try {
            List<String> titleSList = title_auth.getStringList("Title");

            authorizedAnimation = new TitleAnimation(titleSList, false, 0);
        } catch (TitleDescriptionException e) {
            getLogger().severe("An error occurred while loading the Auth Title: " + e.getMessage());
        }

    }

//	
//	public static YamlConfiguration getConfig() {
//		return config;
//	}
	
    private boolean setupNMS() {
        try {
            Class<?> nmsClass = Class.forName("ru.ilyahiguti.authmetitle.nms." + getServer().getClass().getPackage().getName().substring(23));

            nms = (NMS) nmsClass.getConstructor().newInstance();
            return true;
        } catch (ClassNotFoundException e) {
            try {
                Player.class.getMethod("sendTitle", String.class, String.class, int.class, int.class, int.class);
                nms = BukkitImpl.class.getConstructor().newInstance();
                return true;
            } catch (Exception ex) {
                return false;
            }
        } catch (Exception ignored) {
            return false;
        }
    }
    
    public static NMS getNMS() {
        return nms;
    }
    
    public static TitleAnimation getUnregisteredAnimation() {
        return unregisteredAnimation;
    }

    public static TitleAnimation getUnloggedAnimation() {
        return unloggedAnimation;
    }

    public static TitleAnimation getAuthorizedAnimation() {
        return authorizedAnimation;
    }
}

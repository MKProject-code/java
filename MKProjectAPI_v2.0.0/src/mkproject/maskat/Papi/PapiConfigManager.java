package mkproject.maskat.Papi;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PapiConfigManager {
	protected static YamlConfiguration registerYaml(final JavaPlugin plugin, final String fileName) {
		File file = new File(plugin.getDataFolder(), fileName);
		
        mkdir(plugin, file, fileName);
        //plugin.saveDefaultConfig();
        return loadYaml(file);
	}
	
	private static void mkdir(JavaPlugin plugin, File file, String fileName) {
        if (!file.exists()) {
        	plugin.saveResource(fileName, false);
        }
    }
	
    private static YamlConfiguration loadYaml(final File file) {
    	YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
	    }
        catch (IOException | InvalidConfigurationException e) {
	        e.printStackTrace();
	    }
        return config;
    }

	public static void saveYaml(JavaPlugin plugin, String fileName, YamlConfiguration yamlConfiguration) {
    	try {
    		File file = new File(plugin.getDataFolder(), fileName);
    		yamlConfiguration.save(file);
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
	}
}

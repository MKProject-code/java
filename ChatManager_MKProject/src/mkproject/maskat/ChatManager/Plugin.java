package mkproject.maskat.ChatManager;

import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import me.maskat.MoneyManager.Mapi;
import mkproject.maskat.Papi.Papi;

public class Plugin extends JavaPlugin {
	private static Plugin plugin;
	private static YamlConfiguration languagYaml;
	private static MessageFilter messageFilter;
	private static DictionaryFilter dictionaryFilter;
	
	public void onEnable() {
		plugin = this;
		
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
		ChatFormatter.reloadConfigValues();
		ChatFormatter.refreshVault();
		
		languagYaml = Papi.Yaml.registerYaml(this, "lang.yml");
		
		getCommand("chatmanager").setExecutor(executeCommand);
		getCommand("broadcast").setExecutor(executeCommand);
		
		Mapi.registerMapiUpdater(new Updater());
		
		getServer().getScheduler().runTaskTimerAsynchronously(this, new TaskAsyncTimer(), 0L, 40L);
		
		messageFilter = new MessageFilter();
		dictionaryFilter = new DictionaryFilter();
		
        getLogger().info("Has been enabled!");
	}
	
	public static MessageFilter getMessageFilter() {
		return messageFilter;
	}
	public static DictionaryFilter getDictionaryFilter() {
		return dictionaryFilter;
	}
	
	public static void reloadAllConfigs() {
		plugin.reloadConfig();
		languagYaml = Papi.Yaml.registerYaml(Plugin.getPlugin(), "lang.yml");
		dictionaryFilter.reloadDatabase();
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
	public static YamlConfiguration getLanguageYaml() {
		return languagYaml;
	}
//	
//	public static YamlConfiguration getConfig() {
//		return config;
//	}
}

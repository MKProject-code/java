package mkproject.maskat.ChatManager;

import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import me.maskat.MoneyManager.Mapi;
import mkproject.maskat.Papi.Papi;
import net.milkbowl.vault.chat.Chat;

public class Plugin extends JavaPlugin {
	private static Plugin plugin;
	private static YamlConfiguration languagYaml;
	private static MessageFilter messageFilter;
	private static Chat vaultChat;
	
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
		refreshVault();
		
		TabFormatter.initialize();
		
		languagYaml = Papi.Yaml.registerYaml(this, "lang.yml");
		
		getCommand("chatmanager").setExecutor(executeCommand);
		getCommand("broadcast").setExecutor(executeCommand);
		
		Mapi.registerMapiUpdater(new Updater());
		
		getServer().getScheduler().runTaskTimerAsynchronously(this, new TaskAsyncTimerTime(), 0L, 20L);
		getServer().getScheduler().runTaskTimerAsynchronously(this, new TaskAsyncTimerTPS(), 0L, 20L);
		getServer().getScheduler().runTaskTimerAsynchronously(this, new TaskAsyncTimerPing(), 0L, 60L);
		
		messageFilter = new MessageFilter();
		
        getLogger().info("Has been enabled!");
	}
	
	protected static void refreshVault() {
		Chat vaultchat = Plugin.getPlugin().getServer().getServicesManager().load(Chat.class);
		if (vaultchat != vaultChat) {
			Plugin.getPlugin().getLogger().info("New Vault Chat implementation registered: " + (vaultchat == null ? "null" : vaultchat.getName()));
		}
		vaultChat = vaultchat;
	}
	
	public static Chat getVaultChat() {
		return vaultChat;
	}
	
	public static MessageFilter getMessageFilter() {
		return messageFilter;
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
//	
//	public static YamlConfiguration getConfig() {
//		return config;
//	}
}

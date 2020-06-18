package config;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import java.io.IOException;
import org.bukkit.command.TabCompleter;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;

public class config extends JavaPlugin
{
    public static config plugin;
    private File c;
    private File db;
    public YamlConfiguration config;
    public YamlConfiguration database;
    public static HashMap<Player, Player> lastMessageSent;
    
    static {
        lastMessageSent = new HashMap<Player, Player>();
    }
    
    public config() {
        this.c = null;
        this.db = null;
        this.config = new YamlConfiguration();
        this.database = new YamlConfiguration();
    }
    
    public void onEnable() {
        plugin = this;
        this.c = new File(this.getDataFolder(), "config.yml");
        this.db = new File(this.getDataFolder(), "database.yml");
        this.getCommand("msg").setExecutor((CommandExecutor)new msg());
        this.getCommand("tell").setExecutor((CommandExecutor)new msg());
        this.getCommand("t").setExecutor((CommandExecutor)new msg());
        this.getCommand("reply").setExecutor((CommandExecutor)new msg());
        this.getCommand("msgblock").setExecutor((CommandExecutor)new msg());
        this.getCommand("msgunblock").setExecutor((CommandExecutor)new msg());
        this.getCommand("msgreload").setExecutor((CommandExecutor)this);
        this.mkdir();
        this.loadYamls();
        this.saveDefaultConfig();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "better-msg has been enabled!");
        Bukkit.getPluginCommand("msg").setTabCompleter((TabCompleter)this);
    }
    
    public void onDisable() {
    	this.saveDatabase();
    }
    
    private void mkdir() {
        if (!this.c.exists()) {
            this.saveResource("config.yml", false);
        }
        if (!this.db.exists()) {
        	this.saveResource("database.yml", false);
        }
    }
    
    private void loadYamls() {
        try {
            this.config.load(this.c);
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        catch (InvalidConfigurationException e2) {
            e2.printStackTrace();
        }
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
    
    public YamlConfiguration getConfig() {
        return this.config;
    }
    
    public YamlConfiguration getDatabase() {
    	return this.database;
    }
    
    // jesli gracz jest juz zablokowany zwraca False, jesli poprawnie zostal zablokowany zwraca True
    public boolean putBlockDatabase(CommandSender sender, Player target) {
    	String senderName = sender.getName().toLowerCase();
    	List<String> list = database.getStringList(senderName);
    	
    	String targetName = target.getName().toLowerCase();
    	if(list.contains(targetName))
    		return false;
    	
    	list.add(targetName);
    	database.set(senderName, list);
    	
    	return true;
    }
    
    // jesli gracz nie byl zablokowany zwraca False, jesli poprawnie zostal odblokowany zwraca True
    public boolean putUnBlockDatabase(CommandSender sender, Player target) {
    	String senderName = sender.getName().toLowerCase();
    	List<String> list = database.getStringList(senderName);
    	
    	String targetName = target.getName().toLowerCase();
    	if(!list.contains(targetName))
    		return false;
    	
    	list.remove(targetName);
    	database.set(senderName, list);
    	
    	return true;
    }
    
    //jesli gracz jest zablokowany zwraca True, jesli moze wysylac wiadomosc zwraca False
    public boolean getPlayerIsBlockedDatabase(CommandSender sender, Player target) {
    	String targetName = target.getName().toLowerCase();
    	List<String> list = database.getStringList(targetName);
    	
    	String senderName = sender.getName().toLowerCase();
    	if(list.contains(senderName))
    		return true;
    	
    	return false;
    }
    
//    public void saveConfig() {
//        try {
//            this.config.save(this.c);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public void saveDatabase() {
    	try {
    		this.database.save(this.db);
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("msgreload")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("msg.reload")) {
                    try {
                        this.config.load(this.c);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (InvalidConfigurationException e2) {
                        e2.printStackTrace();
                    }
                    try {
                        this.config.save(this.c);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.reload")));
                    return true;
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("deny-messages.reload")));
            }
            if (sender instanceof ConsoleCommandSender) {
                try {
                    this.config.load(this.c);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (InvalidConfigurationException e2) {
                    e2.printStackTrace();
                }
                try {
                    this.config.save(this.c);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                sender.sendMessage(ChatColor.RED + "better-msg has been reloaded");
                return true;
            }
        }
        return false;
    }
}

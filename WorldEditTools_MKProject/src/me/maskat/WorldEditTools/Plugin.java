package me.maskat.WorldEditTools;

import java.io.File;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import mkproject.maskat.Papi.Utils.CommandManager;

public class Plugin extends JavaPlugin {
	private static Plugin plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		getServer().getPluginManager().registerEvents(new Event(), this);
		
		CommandManager.initCommand(this, "signedit", new CmdSignLine(), true);
		
		getLogger().info("[CompassPoint] Has been enabled!");
	}
	
	public static Plugin getPlugin() { return plugin; }

	public static String getWorldEditPlayerSchemFolder(Player player) {
		//return plugin.getDataFolder() + File.separator + ".." + File.separator + "FastAsyncWorldEdit" + File.separator + "schematics" + File.separator + player.getUniqueId();
		return "plugins" + File.separator + "FastAsyncWorldEdit" + File.separator + "schematics" + File.separator + player.getUniqueId();
	}
}

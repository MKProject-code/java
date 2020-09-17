package me.maskat.compasspoint;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.maskat.compasspoint.Cmds.CmdCompass;
import me.maskat.compasspoint.compass.Compass;
import me.maskat.compasspoint.models.ModelPlayer;
import mkproject.maskat.Papi.Utils.CommandManager;

public class Plugin extends JavaPlugin {
	public static final String PERMISSION_PREFIX = "mkp.compasspoint";

	private static Plugin plugin;
	
	private static Map<Player, ModelPlayer> mapPlayers = new HashMap<>();
	
	@Override
	public void onEnable() {
		plugin = this;
		
		getServer().getPluginManager().registerEvents(new Event(), this);
		
		CommandManager.initCommand(this, "compass", new CmdCompass(), false);
		
		Compass.start();
		getLogger().info("[CompassPoint] Has been enabled!");
	}
	
	public static Plugin getPlugin()
	{
		return plugin;
	}
	
	public static Map<Player, ModelPlayer> getPlayersMap() {
		return mapPlayers;
	}
}

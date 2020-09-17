package mkproject.maskat.MiniGamesManager;

import org.bukkit.plugin.java.JavaPlugin;

import mkproject.maskat.MiniGamesManager.Commands.JoinCommand;
import mkproject.maskat.MiniGamesManager.Commands.ManagerCommand;
import mkproject.maskat.MiniGamesManager.Commands.MenuCommand;
import mkproject.maskat.MiniGamesManager.Game.GamesManager;
import mkproject.maskat.MiniGamesManager.Game.MiniGame;
import mkproject.maskat.MiniGamesManager.Menu.PlayerMenu;

public class Plugin extends JavaPlugin {
	
	private static Plugin plugin;
	private static PlayerMenu playerMenu;
	
	@Override
	public void onEnable() {
		plugin = this;
		this.saveDefaultConfig();
		
		GamesManager.initialize();
		
		this.getCommand("minigames").setExecutor(new MenuCommand());
		this.getCommand("minigamesmanager").setExecutor(new ManagerCommand());
		this.getServer().getPluginManager().registerEvents(new Event(), this);
		
		for(MiniGame miniGame : MiniGame.values()) {
			this.getCommand(miniGame.name().toLowerCase()).setExecutor(new JoinCommand(miniGame));
		}
		
		playerMenu = new PlayerMenu(null);
	}

	public static Plugin getPlugin() {
		return plugin;
	}
	
	public static PlayerMenu getPlayerMenu() {
		return playerMenu;
	}
	
}

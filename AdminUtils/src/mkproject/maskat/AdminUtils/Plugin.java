package mkproject.maskat.AdminUtils;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import mkproject.maskat.AdminUtils.Cmds.CmdBack;
import mkproject.maskat.AdminUtils.Cmds.CmdFeed;
import mkproject.maskat.AdminUtils.Cmds.CmdFly;
import mkproject.maskat.AdminUtils.Cmds.CmdGamemode;
import mkproject.maskat.AdminUtils.Cmds.CmdHead;
import mkproject.maskat.AdminUtils.Cmds.CmdHeal;
import mkproject.maskat.AdminUtils.Cmds.CmdInventory;
import mkproject.maskat.AdminUtils.Cmds.CmdMute;
import mkproject.maskat.AdminUtils.Cmds.CmdPlayerInfo;
import mkproject.maskat.AdminUtils.Cmds.CmdRepair;
import mkproject.maskat.AdminUtils.Cmds.CmdRespawn;
import mkproject.maskat.AdminUtils.Cmds.CmdSpeed;
import mkproject.maskat.AdminUtils.Cmds.CmdTime;
import mkproject.maskat.AdminUtils.Cmds.CmdTop;
import mkproject.maskat.AdminUtils.Cmds.CmdTphere;
import mkproject.maskat.AdminUtils.Cmds.CmdTpposition;
import mkproject.maskat.AdminUtils.Cmds.CmdTpworld;
import mkproject.maskat.AdminUtils.Cmds.CmdUnmute;

public class Plugin extends JavaPlugin {
	private static Plugin plugin;
	
	public void onEnable() {
		plugin = this;
		
		//CommandListener executeCommand = new CommandListener();
		Event eventHandler = new Event();
		
		getServer().getPluginManager().registerEvents(eventHandler, this);
		
		initCommand("tpposition", new CmdTpposition(), false);
		initCommand("tphere", new CmdTphere(), false);
		initCommand("inventory", new CmdInventory(), true);
		initCommand("gamemode", new CmdGamemode(), true);
		//initCommand("give", new CmdGive(), false);
		initCommand("head", new CmdHead(), false);
		initCommand("fly", new CmdFly(), false);
		initCommand("speed", new CmdSpeed(), false);
		initCommand("repair", new CmdRepair(), false);
		
		initCommand("back", new CmdBack(), false);
		initCommand("feed", new CmdFeed(), false);
		initCommand("heal", new CmdHeal(), false);
		
		initCommand("time", new CmdTime(), true);
		initCommand("playerinfo", new CmdPlayerInfo(), false);
		
		initCommand("mute", new CmdMute(), true);
		initCommand("unmute", new CmdUnmute(), true);
		
		initCommand("tpworld", new CmdTpworld(), true);
		initCommand("top", new CmdTop(), false);
		
		initCommand("respawn", new CmdRespawn(), false);
		
//		getCommand("tppos").setExecutor(executeCommand);
//		getCommand("tphere").setExecutor(executeCommand);
//		getCommand("inv").setExecutor(executeCommand);
//		getCommand("gm").setExecutor(executeCommand);
//		getCommand("give").setExecutor(executeCommand);
//		getCommand("head").setExecutor(executeCommand);
//		
//		getCommand("tppos").setExecutor(executeCommand);
//		getCommand("tphere").setExecutor(executeCommand);
//		getCommand("inv").setExecutor(executeCommand);
//		getCommand("gm").setExecutor(executeCommand);
//		getCommand("give").setExecutor(executeCommand);
//		getCommand("head").setExecutor(executeCommand);
		
		Config.initialize();
		Database.initialize();
		
//		getCommand("command").setTabCompleter(new CommandTabCompleter("command",
//				List.of(
//						"set",
//						"get",
//						"clear"
//						)));
		
		getLogger().info("Has been enabled!");
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
	
	private void initCommand(String command, Object listener, boolean tabCompleter) {
		getCommand(command).setExecutor((CommandExecutor)listener);
		if(tabCompleter)
			getCommand(command).setTabCompleter((TabCompleter)listener);
	}
}

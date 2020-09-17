package mkproject.maskat.AdminUtils;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import mkproject.maskat.AdminUtils.Config.ConfigKey;
import mkproject.maskat.AdminUtils.Cmds.CmdBack;
import mkproject.maskat.AdminUtils.Cmds.CmdBan;
import mkproject.maskat.AdminUtils.Cmds.CmdBanOffline;
import mkproject.maskat.AdminUtils.Cmds.CmdBed;
import mkproject.maskat.AdminUtils.Cmds.CmdChat;
import mkproject.maskat.AdminUtils.Cmds.CmdCollision;
import mkproject.maskat.AdminUtils.Cmds.CmdEnderChest;
import mkproject.maskat.AdminUtils.Cmds.CmdEventSpawnChestItems;
import mkproject.maskat.AdminUtils.Cmds.CmdFeed;
import mkproject.maskat.AdminUtils.Cmds.CmdFly;
import mkproject.maskat.AdminUtils.Cmds.CmdGamemode;
import mkproject.maskat.AdminUtils.Cmds.CmdHead;
import mkproject.maskat.AdminUtils.Cmds.CmdHeal;
import mkproject.maskat.AdminUtils.Cmds.CmdInventory;
import mkproject.maskat.AdminUtils.Cmds.CmdKick;
import mkproject.maskat.AdminUtils.Cmds.CmdMute;
import mkproject.maskat.AdminUtils.Cmds.CmdNear;
import mkproject.maskat.AdminUtils.Cmds.CmdPlayerHistory;
import mkproject.maskat.AdminUtils.Cmds.CmdPlayerInfo;
import mkproject.maskat.AdminUtils.Cmds.CmdPlugin;
import mkproject.maskat.AdminUtils.Cmds.CmdRandomTp;
import mkproject.maskat.AdminUtils.Cmds.CmdRename;
import mkproject.maskat.AdminUtils.Cmds.CmdRepair;
import mkproject.maskat.AdminUtils.Cmds.CmdRespawn;
import mkproject.maskat.AdminUtils.Cmds.CmdSpawn;
import mkproject.maskat.AdminUtils.Cmds.CmdSpeed;
import mkproject.maskat.AdminUtils.Cmds.CmdSurvival;
import mkproject.maskat.AdminUtils.Cmds.CmdTime;
import mkproject.maskat.AdminUtils.Cmds.CmdTitleText;
import mkproject.maskat.AdminUtils.Cmds.CmdTop;
import mkproject.maskat.AdminUtils.Cmds.CmdTphere;
import mkproject.maskat.AdminUtils.Cmds.CmdTpposition;
import mkproject.maskat.AdminUtils.Cmds.CmdTpworld;
import mkproject.maskat.AdminUtils.Cmds.CmdUnban;
import mkproject.maskat.AdminUtils.Cmds.CmdUnmute;
import mkproject.maskat.AdminUtils.Commands.ChildIfSkyPoints;
import mkproject.maskat.AdminUtils.Commands.CommandSkyPoints;

public class Plugin extends JavaPlugin {
	private static Plugin plugin;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onEnable() {
		plugin = this;
		
		//CommandListener executeCommand = new CommandListener();
		Event eventHandler = new Event();
		
		getServer().getPluginManager().registerEvents(eventHandler, this);
		
		initCommand("tpposition", new CmdTpposition(), false);
		initCommand("tphere", new CmdTphere(), false);
		initCommand("inventory", new CmdInventory(), true);
		initCommand("gm", new CmdGamemode(), true);
		//initCommand("give", new CmdGive(), false);
		initCommand("head", new CmdHead(), false);
		initCommand("fly", new CmdFly(), false);
		initCommand("speed", new CmdSpeed(), false);
		initCommand("repair", new CmdRepair(), false);
		initCommand("collision", new CmdCollision(), false);
		
		initCommand("back", new CmdBack(), false);
		initCommand("feed", new CmdFeed(), false);
		initCommand("heal", new CmdHeal(), false);
		
		initCommand("time", new CmdTime(), true);
		initCommand("playerinfo", new CmdPlayerInfo(), false);
		initCommand("playerhistory", new CmdPlayerHistory(), false);
		
		initCommand("mute", new CmdMute(), true);
		initCommand("unmute", new CmdUnmute(), true);
		
		initCommand("kick", new CmdKick(), true);
		
		initCommand("ban", new CmdBan(), true);
		initCommand("banoffline", new CmdBanOffline(), true);
		initCommand("unban", new CmdUnban(), true);
		
		initCommand("tpworld", new CmdTpworld(), true);
		initCommand("top", new CmdTop(), false);
		
		initCommand("respawn", new CmdRespawn(), false);
		
		initCommand("bed", new CmdBed(), true);
		initCommand("spawn", new CmdSpawn(), true);
		
		initCommand("near", new CmdNear(), false);
		
		initCommand("randomtp", new CmdRandomTp(), false);
		
		initCommand("plugin", new CmdPlugin(), true);
		
		initCommand("titletext", new CmdTitleText(), false);
		
		initCommand("eventspawnchestitems", new CmdEventSpawnChestItems(), false);
		
		initCommand("chat", new CmdChat(), true);
		
		initCommand("survival", new CmdSurvival(), true);
		initCommand("rename", new CmdRename(), false);
		
		initCommand("enderchest", new CmdEnderChest(), false);
		
		CommandSkyPoints.register(this, Config.getString(ConfigKey.PermissionPrefix)+".");
		ChildIfSkyPoints.register(this);
//		RandomChanceChild.register(this);
		
//		initCommand("collision", new CmdCollision(), true);
		
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

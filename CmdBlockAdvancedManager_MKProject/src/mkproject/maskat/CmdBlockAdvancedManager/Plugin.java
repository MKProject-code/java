package mkproject.maskat.CmdBlockAdvancedManager;

import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import mkproject.maskat.CmdBlockAdvancedManager.Cmds.CmdBlockAdvancedManager;
import mkproject.maskat.CmdBlockAdvancedManager.CmdsExecute.CmdsExecuteManager;
import mkproject.maskat.CmdBlockAdvancedManager.ProjectManager.ProjectManager;
import mkproject.maskat.Papi.Utils.CommandManager;

public class Plugin extends JavaPlugin {
	
	private static Plugin plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		Event eventListener = new Event();
		
		this.getServer().getPluginManager().registerEvents(eventListener, this);
		
		//this.getCommand("cmdblockadvancedmanager").setExecutor(new CmdBlockAdvancedManager());
		
		CommandManager.initCommand(this, "cmdblockadvancedmanager", new CmdBlockAdvancedManager(), true);
		
//		PacketListenerAPI.addPacketHandler(new PacketListener());
		
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(this, PacketType.Play.Client.SET_COMMAND_BLOCK) {
                    @Override
                    public void onPacketReceiving(PacketEvent e) {
                    	eventListener.onPacketReceivingSetCommandBlock(e);
                    }
                }
            );
		
		this.saveDefaultConfig();
		
		CmdsExecuteManager.registerCommandsExecute(this);
		
//		this.getServer().getScheduler().runTask(this, new Runnable() {
//
//			@Override
//			public void run() {
//				
//
//				getLogger().info("");
//				String commandExecute = "execute in projectworld positioned ~ ~ ~";
//				getLogger().warning("DEF: "+commandExecute);
//				getLogger().warning("REP: "+ProjectManager.prepareCommand(commandExecute, "world", "0 0 0"));
//				getLogger().info("");
//				commandExecute = "/execute in projectworld positioned ~ ~ ~ run particle minecraft:white_ash ~123.123 ~321.312 ~532.4324 ~ ~ ~ 1 1";
//				getLogger().warning("DEF: "+commandExecute);
//				getLogger().warning("REP: "+ProjectManager.prepareCommand(commandExecute, "world_nether", "10 0 0"));
//				getLogger().info("");
//				commandExecute = "/execute in projectworld positioned ~ ~ ~-5 run particle minecraft:white_ash ~ ~50 ~-1 ~ ~-10 ~1 1 1";
//				getLogger().warning("DEF: "+commandExecute);
//				getLogger().warning("REP: "+ProjectManager.prepareCommand(commandExecute, "world_the_end", "0 10 0"));
//				getLogger().info("");
//				commandExecute = "execute in projectworld positioned ~-5 ~ ~1 run particle minecraft:white_ash ~-61.123 ~-62213.0123 ~-123.5124 ~-63.0976 ~ ~ 1 1";
//				getLogger().warning("DEF: "+commandExecute);
//				getLogger().warning("REP: "+ProjectManager.prepareCommand(commandExecute, "world_castle", "0 0 10"));
//				getLogger().info("");
//				commandExecute = "particle minecraft:ambient_entity_effect 500 ~-50 ~ ~ ~-10 ~";
//				getLogger().warning("DEF: "+commandExecute);
//				getLogger().warning("REP: "+ProjectManager.prepareCommand(commandExecute, "world_castle", "0 0 10"));
//				getLogger().info("");
//				
//				commandExecute = "execute positioned ~ ~ ~ in projectworld";
//				getLogger().warning("DEF: "+commandExecute);
//				getLogger().warning("REP: "+ProjectManager.prepareCommand(commandExecute, "world", "0 0 0"));
//				getLogger().info("");
//				commandExecute = "execute positioned ~ ~ ~ in projectworld ";
//				getLogger().warning("DEF: "+commandExecute);
//				getLogger().warning("REP: "+ProjectManager.prepareCommand(commandExecute, "world", "0 0 0"));
//				getLogger().info("");
//				commandExecute = "execute in projectworld positioned ~ ~ ~ ";
//				getLogger().warning("DEF: "+commandExecute);
//				getLogger().warning("REP: "+ProjectManager.prepareCommand(commandExecute, "world", "0 0 0"));
//				getLogger().info("");
//				commandExecute = "/execute positioned ~ ~ ~ in projectworld run particle minecraft:white_ash ~123.123 ~321.312 ~532.4324 ~ ~ ~ 1 1";
//				getLogger().warning("DEF: "+commandExecute);
//				getLogger().warning("REP: "+ProjectManager.prepareCommand(commandExecute, "world_nether", "10 0 0"));
//				getLogger().info("");
//				commandExecute = "/execute positioned ~2 ~-2 ~-3 in projectworld run particle minecraft:white_ash ~ ~50 ~-1 ~ ~-10 ~1 1 1";
//				getLogger().warning("DEF: "+commandExecute);
//				getLogger().warning("REP: "+ProjectManager.prepareCommand(commandExecute, "world_the_end", "0 10 0"));
//				getLogger().info("");
//				commandExecute = "execute positioned ~1 ~ ~2 in projectworld run particle minecraft:white_ash ~-61.123 ~-62213.0123 ~-123.5124 ~-63.0976 ~ ~ 1 1";
//				getLogger().warning("DEF: "+commandExecute);
//				getLogger().warning("REP: "+ProjectManager.prepareCommand(commandExecute, "world_castle", "0 0 10"));
//				getLogger().info("");
//				commandExecute = "particle minecraft:ambient_entity_effect 500 ~-50 ~ ~ ~-10 ~";
//				getLogger().warning("DEF: "+commandExecute);
//				getLogger().warning("REP: "+ProjectManager.prepareCommand(commandExecute, "world_castle", "0 0 10"));
//				getLogger().info("");
//			}
//			
//		});
		
		
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
}

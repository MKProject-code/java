package mkproject.maskat.CmdBlockAdvancedManager.Models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import mkproject.maskat.CmdBlockAdvancedManager.Database;
import mkproject.maskat.CmdBlockAdvancedManager.Plugin;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.MenuManager;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.Actions.Menu_TheProjectTheAction;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuV2;
import mkproject.maskat.Papi.Utils.Message;

public class CbamPlayer {
	
	private Player player;
	private PapiMenuV2 lastOpenedPapiMenu;
	private Block commandBlock;
	private CommandBlockUpdate commandBlockUpdate;
	private BukkitTask positionBlockChangeTask;
	private Location positionBlockChangeTaskBlock1;
	private Location positionBlockChangeTaskBlock2;
	private Location positionBlockChangeTaskBlock3;
	
	public CbamPlayer(Player player) {
		this.player = player;
		this.lastOpenedPapiMenu = null;
		this.commandBlock = null;
		this.commandBlockUpdate = null;
		this.positionBlockChangeTask = null;
	}

	public void openPapiMenuPage(PapiMenuV2 papiMenu) {
		this.lastOpenedPapiMenu = papiMenu;
		papiMenu.openPapiMenuPage(this.player);
	}
	
	public void unsetLastPapiMenuPage() {
		this.lastOpenedPapiMenu = null;
	}

	public void openLastPapiMenuPage() {
		if(this.lastOpenedPapiMenu == null)
			MenuManager.openPluginMenu(this);
		else
			MenuManager.backMenu(this, this.lastOpenedPapiMenu);
	}

	public Player getPlayer() {
		return this.player;
	}
	
	public void setCommandBlock(Block commandBlock, CommandBlockUpdate commandBlockUpdate) {
		this.commandBlock = commandBlock;
		this.commandBlockUpdate = commandBlockUpdate;
	}
	
	public void unsetCommandBlock() {
		this.commandBlock = null;
		this.commandBlockUpdate = null;
	}
	
	public Block getCommandBlock() {
		return this.commandBlock;
	}
	
	public class CommandBlockUpdate {
		private String worldName;
		private String projectName;
		private String actionsGroup;
		private String actionsSubGroup;
		private long actionsDelayLong;
		private String actionName;
		public CommandBlockUpdate(String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, String actionName) {
			this.worldName = worldName;
			this.projectName = projectName;
			this.actionsGroup = actionsGroup;
			this.actionsSubGroup = actionsSubGroup;
			this.actionsDelayLong = actionsDelayLong;
			this.actionName = actionName;
		}
		protected boolean isProjectExist() {
			return Database.isProjectExist(this.worldName, this.projectName);
		}
		public void updateCommand(String newExecutedCommand) {
			Database.setActionCommandExecute(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.actionName, newExecutedCommand);
		}
	}
	
	public boolean isCommandBlockExist() {
		return this.commandBlock == null ? false : true;
	}

	public boolean updateCommandBlock(String newExecutedCommand) {
		if(!this.commandBlockUpdate.isProjectExist())
		{
			Message.sendMessage(this.player, "&c&lSomething wrong... The project no longer exists\n&c&lCanceled. Type /cb for reopen menu");
			return false;
		}
		
		this.commandBlockUpdate.updateCommand(newExecutedCommand);
		
		Message.sendMessage(this.player, "&a&lExecuted command updated.");
		return true;
	}

	public void setPositionBlockChangeTask(Location block1, Location block2, Location block3) {
		if(this.positionBlockChangeTask != null)
		{
			positionBlockChangeTask.cancel();
			player.sendBlockChange(this.positionBlockChangeTaskBlock1, this.positionBlockChangeTaskBlock1.getBlock().getBlockData());
			player.sendBlockChange(this.positionBlockChangeTaskBlock2, this.positionBlockChangeTaskBlock2.getBlock().getBlockData());
			player.sendBlockChange(this.positionBlockChangeTaskBlock3, this.positionBlockChangeTaskBlock3.getBlock().getBlockData());
		}
		
		this.positionBlockChangeTaskBlock1 = block1;
		this.positionBlockChangeTaskBlock2 = block2;
		this.positionBlockChangeTaskBlock3 = block3;
		
		this.positionBlockChangeTask = Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if(player.isOnline()) {
					player.sendBlockChange(block1, block1.getBlock().getBlockData());
					player.sendBlockChange(block2, block2.getBlock().getBlockData());
					player.sendBlockChange(block3, block3.getBlock().getBlockData());
				}
			}
		}, 100L);
	}
}

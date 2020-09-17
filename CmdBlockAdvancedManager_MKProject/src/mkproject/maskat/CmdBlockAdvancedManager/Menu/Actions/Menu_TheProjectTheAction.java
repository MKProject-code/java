package mkproject.maskat.CmdBlockAdvancedManager.Menu.Actions;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CommandBlock;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import mkproject.maskat.CmdBlockAdvancedManager.Database;
import mkproject.maskat.CmdBlockAdvancedManager.Validation;
import mkproject.maskat.CmdBlockAdvancedManager.Plugin;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.MenuManager;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.MenuManager.ListenerChat;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.MenuManager.ListenerInteract;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.MenuManager.MenuType;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.MenuManager.SlotOption;
import mkproject.maskat.CmdBlockAdvancedManager.Models.CbamPlayer;
import mkproject.maskat.CmdBlockAdvancedManager.Models.CbamPlayer.CommandBlockUpdate;
import mkproject.maskat.CmdBlockAdvancedManager.ProjectManager.ProjectManager;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.PapiHeadBase64;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuItem;
import mkproject.maskat.Papi.Menu.PapiMenuPage;
import mkproject.maskat.Papi.Menu.PapiMenuV2;
import mkproject.maskat.Papi.Model.PapiChatListener;
import mkproject.maskat.Papi.Model.PapiInteractListener;
import mkproject.maskat.Papi.Model.PapiListenerChatEvent;
import mkproject.maskat.Papi.Model.PapiListenerInteractEvent;
import mkproject.maskat.Papi.Model.PapiPlayer;
import mkproject.maskat.Papi.Utils.Message;

public class Menu_TheProjectTheAction implements PapiMenuV2, PapiChatListener, PapiInteractListener {
	
	private PapiMenuPage papiMenuPage;
	private PapiMenuV2 backMenu;
	private CbamPlayer cbamPlayer;
	private String worldName;
	private String projectName;
	private String actionsGroup;
	private String actionsSubGroup;
	private long actionsDelayLong;
	private String actionName;
	private String actionCommandExecute;
	private long actionRepeatTimeLong;
	private String actionPositioned;
	
	public Menu_TheProjectTheAction(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, String actionName) {
		this.backMenu = backMenu;
		this.cbamPlayer = cbamPlayer;
		this.worldName = worldName;
		this.projectName = projectName;
		this.actionsGroup = actionsGroup;
		this.actionsSubGroup = actionsSubGroup;
		this.actionsDelayLong = actionsDelayLong;
		this.actionName = actionName;
	}
	
	private PapiMenuV2 getInstance() {
		return this;
	}
	
	@Override
	public PapiMenuPage getPapiMenuPage() {
		return this.papiMenuPage;
	}
	
	private void loadDynamicVariables() {
		this.actionCommandExecute = Database.getActionCommandExecute(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.actionName);
		this.actionRepeatTimeLong = Database.getActionRepeatTime(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.actionName);
		this.actionPositioned = Database.getActionPositioned(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.actionName);
	}

	@Override
	public boolean refreshPapiMenuPage() {
		if(!Database.isProjectExist(this.worldName, this.projectName))
		{
			Message.sendMessage(this.cbamPlayer.getPlayer(), "&c&lLast menu can't be opened... The project no longer exists\n&c&lType /cb for reopen menu");
			this.cbamPlayer.unsetLastPapiMenuPage();
			return false;
		}

		if(!Database.isActionExist(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.actionName))
		{
			Message.sendMessage(this.cbamPlayer.getPlayer(), "&c&lLast menu can't be opened... This action no longer exists\n&c&lType /cb for reopen menu");
			this.cbamPlayer.unsetLastPapiMenuPage();
			return false;
		}
		
		this.loadDynamicVariables();
		
		if(this.papiMenuPage == null)
		{
//			this.papiMenuPage = new PapiMenuPage(this, 6, "&4&l"+this.projectName+" &1&l"+this.actionsDelayLong+"T = "+(this.actionsDelayLong/20.0)+"s");
			this.papiMenuPage = new PapiMenuPage(this, 6, "&d&l"+this.actionsSubGroup+" &2&l"+this.actionName);
		}
		else
			this.papiMenuPage.clearItems();
		this.loadMenuItems();
		return true;
	}
	
	private void loadMenuItems() {
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.GREEN_DYE, "&6&lCategory:\n&6Actions");
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN2, Material.NAME_TAG, "&a&l"+this.actionName+"\n&7&oClick to rename", new PapiMenuItem(SlotOption.RENAME_ACTION));
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.REDSTONE, "&4Delete action: &c"+this.actionName+"\n&7&oClick to delete this action", new PapiMenuItem(SlotOption.DELETE_ACTION));

		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.BOOK, "&7Wróć", new PapiMenuItem(SlotOption.BACK));
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN2, Material.REPEATER, "&6Delay time: &e&l"+this.actionsDelayLong+"T ("+(this.actionsDelayLong/20.0)+"s)\n&7&oClick to change", new PapiMenuItem(SlotOption.CHANGE_DELAY_TIME));
		this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN2, Material.COMPARATOR, "&6Repeating time: "+(this.actionRepeatTimeLong <=0 ? "&c&lDISABLED" : "&e&l"+this.actionRepeatTimeLong+"T ("+(this.actionRepeatTimeLong/20.0)+"s)")+"\n&7&oClick to change", new PapiMenuItem(SlotOption.CHANGE_REPEATING_TIME));
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN3, Material.ARMOR_STAND, "&6Positioned: &e&l"+(this.actionPositioned == null ? "-" : this.actionPositioned)+"\n&7&oClick to change", new PapiMenuItem(SlotOption.SELECT_POSITION));
		if(this.actionPositioned == null)
			this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN3, (this.actionRepeatTimeLong > 0 ? Material.REPEATING_COMMAND_BLOCK : Material.COMMAND_BLOCK), "&6Modify executed command\n&c&oPosition not set");
		else
			this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN3, (this.actionRepeatTimeLong > 0 ? Material.REPEATING_COMMAND_BLOCK : Material.COMMAND_BLOCK), "&6Modify executed command\n&7&oClick to modify", new PapiMenuItem(SlotOption.MODIFY_EXECUTED_COMMAND));
//		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN3, Material.COMMAND_BLOCK, true, "&6Advanced\n&7&oClick to open advanced menu", new PapiMenuItem(SlotOption.ADVANCED_COMMANDS));

		
		
		String actionCommandExecuteFormatted = "";
		if(this.actionCommandExecute != null)
		{
			int i=0;
			for(char actionCommandExecuteChar : this.actionCommandExecute.toCharArray()) {
				if(i%70==0)
					actionCommandExecuteFormatted += "\n&d";
				actionCommandExecuteFormatted += actionCommandExecuteChar;
				i++;
			}
		}
		
		this.papiMenuPage.setItemHeadAsync(InventorySlot.ROW4_COLUMN5, Material.AIR, PapiHeadBase64.GREEN_Information, "&aExecute command:"+(actionCommandExecuteFormatted == "" ? "\n&d-" : actionCommandExecuteFormatted));
		
		if(this.actionPositioned == null) {
			this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN7, Material.ENDER_PEARL, "&6Teleport to positioned\n&c&oPosition not set");
			this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN7, Material.ENDER_EYE, "&6Show positioned\n&c&oPosition not set");
			this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN7, Material.ENDER_EYE, true, "&6Show positioned with close menu\n&c&oPosition not set");
		} else {
			this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN7, Material.ENDER_PEARL, "&6Teleport to positioned\n&7&oClick to teleport to action execute position\n&7&owith closing menu", new PapiMenuItem(SlotOption.TELEPORT_TO_ACTION_POSITION));
			this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN7, Material.ENDER_EYE, "&6Show positioned\n&7&oClick to show action execute position", new PapiMenuItem(SlotOption.SHOW_TO_ACTION_POSITION_WITHOUT_CLOSE));
			this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN7, Material.ENDER_EYE, true, "&6Show positioned with close menu\n&7&oClick to show action execute position with closing menu", new PapiMenuItem(SlotOption.SHOW_TO_ACTION_POSITION_WITH_CLOSE));
		}
		
		if(this.actionCommandExecute == null) {
			this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN8, Material.STRING, "&6Quick preview\n&c&oCommand execute not set");
			this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN8, Material.BLAZE_POWDER, "&6Quick debug\n&c&oCommand execute not set");
			this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN8, Material.BLAZE_POWDER, true, "&6Quick debug with close menu\n&c&oCommand execute not set");
		} else {
			this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN8, Material.STRING, "&6Quick preview\n&7&oClick to preview this command", new PapiMenuItem(SlotOption.PREVIEW_EXECUTE_COMMAND_WITHOUT_CLOSE));
			this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN8, Material.BLAZE_POWDER, "&6Quick debug\n&7&oClick to execute this command", new PapiMenuItem(SlotOption.DEBUG_EXECUTE_COMMAND_WITHOUT_CLOSE));
			this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN8, Material.BLAZE_POWDER, true, "&6Quick debug with close menu\n&7&oClick to execute this command with closing menu", new PapiMenuItem(SlotOption.DEBUG_EXECUTE_COMMAND_WITH_CLOSE));
		}
	}

	@Override
	public void onMenuClick(PapiMenuClickEvent e) {
		if(e.getSlotStoreObject() == null)
			return;
		
		PapiMenuItem menuItem = (PapiMenuItem)e.getSlotStoreObject();
		if(menuItem.getSlotOption() == SlotOption.BACK)
		{
			if(this.backMenu == null)
				MenuManager.openTheProjectActionsMenu(null, this.cbamPlayer, this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, 1);
			else
				MenuManager.backMenu(this.cbamPlayer, this.backMenu);
		}
		else if(menuItem.getSlotOption() == SlotOption.DELETE_ACTION)
		{
			Message.sendMessage(e.getPlayer(), "&6&lType &e&lconfirm&6&l to delete action '"+this.actionName+"' in chat");
			e.getPapiPlayer().listenChat(this, ListenerChat.DELETE_ACTION, true);
			e.closeMenuForThisPlayer();
		}
		else if(menuItem.getSlotOption() == SlotOption.RENAME_ACTION)
		{
			Message.sendMessage(e.getPlayer(), "&6&lType new name for action '"+this.actionName+"' in chat");
			e.getPapiPlayer().listenChat(this, ListenerChat.RENAME_ACTION, true);
			e.closeMenuForThisPlayer();
		}
		else if(menuItem.getSlotOption() == SlotOption.CHANGE_DELAY_TIME)
		{
			e.closeMenuForThisPlayer();
			Message.sendMessage(e.getPlayer(), "&6&lType new delay of ticks in chat (20 ticks ≈ 1 secound). You can use only digits and * for multiplication (type '20*60*10' to result 10 minutes)");
			e.getPapiPlayer().listenChat(this, ListenerChat.CHANGE_ACTION_DELAY, true);
		}
		else if(menuItem.getSlotOption() == SlotOption.CHANGE_REPEATING_TIME)
		{
			e.closeMenuForThisPlayer();
			Message.sendMessage(e.getPlayer(), "&6&lType new repeat time of ticks in chat (20 ticks ≈ 1 secound). You can use only digits and * for multiplication (type '20*60*10' to result 10 minutes)\n&6&lType '0' for disable repeating.");
			e.getPapiPlayer().listenChat(this, ListenerChat.CHANGE_ACTION_REPEATING_TIME, true);
		}
		else if(menuItem.getSlotOption() == SlotOption.SELECT_POSITION)
		{
			e.closeMenuForThisPlayer();
			Message.sendMessage(e.getPlayer(), "&6&lSelect some block with the right mouse button!\n&6&lClick someone else for cancel.");
			e.getPapiPlayer().listenInteract(this, ListenerInteract.SELECT_EXECUTE_COMMAND_POSITION);
		}
//		else if(menuItem.getSlotOption() == SlotOption.ADVANCED_COMMANDS)
//		{
//			MenuManager.openTheProjectTheActionAdvancedMenu(this, this.cbamPlayer, this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.actionName);
//		}
		else if(menuItem.getSlotOption() == SlotOption.MODIFY_EXECUTED_COMMAND)
		{
			if(e.getPlayer().getGameMode() != GameMode.CREATIVE)
				e.getPlayer().performCommand("gamemode creative");
			
			if(e.getPlayer().getGameMode() != GameMode.CREATIVE)
			{
				e.getPapiMenuPage().setItemLore(e.getSlot(), "&cYou must have CREATIVE");
				return;
			}
			
			e.closeMenuForThisPlayer();
			Location blockLoc = e.getPlayer().getEyeLocation().clone();
			BlockFace blockFace = e.getPlayer().getFacing();
			if(blockFace == BlockFace.NORTH || blockFace == BlockFace.NORTH_NORTH_EAST || blockFace == BlockFace.NORTH_NORTH_WEST)
				blockLoc.add(0, 0, -1);
			else if(blockFace == BlockFace.SOUTH || blockFace == BlockFace.SOUTH_SOUTH_EAST || blockFace == BlockFace.SOUTH_SOUTH_WEST)
				blockLoc.add(0, 0, 1);
			else if(blockFace == BlockFace.WEST || blockFace == BlockFace.WEST_NORTH_WEST || blockFace == BlockFace.WEST_SOUTH_WEST)
				blockLoc.add(-1, 0, 0);
			else if(blockFace == BlockFace.EAST || blockFace == BlockFace.EAST_NORTH_EAST || blockFace == BlockFace.EAST_SOUTH_EAST)
				blockLoc.add(1, 0, 0);
			else if(blockFace == BlockFace.DOWN)
				blockLoc.add(0, -1, 0);
			else if(blockFace == BlockFace.UP)
				blockLoc.add(0, 1, 0);
			else if(blockFace == BlockFace.NORTH_EAST)
				blockLoc.add(1, 0, -1);
			else if(blockFace == BlockFace.NORTH_WEST)
				blockLoc.add(-1, 0, -1);
			else if(blockFace == BlockFace.SOUTH_EAST)
				blockLoc.add(1, 0, 1);
			else if(blockFace == BlockFace.SOUTH_WEST)
				blockLoc.add(-1, 0, 1);
			else {
				Message.sendMessage(e.getPlayer(), "&c&lWhere are you? Where you looking? Something wrong...");
				return;
			}

			if(!blockLoc.getBlock().getType().isAir())
			{
				Message.sendMessage(e.getPlayer(), "&c&lYour current location is wrong! We need more space...\n&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			Location playerTpLoc = e.getPlayer().getLocation().clone();
			playerTpLoc.setX(playerTpLoc.getBlockX()+0.5);
			playerTpLoc.setZ(playerTpLoc.getBlockZ()+0.5);
			playerTpLoc.setPitch(0F);

			if(playerTpLoc.getBlockX() < blockLoc.getBlockX())
				playerTpLoc.setYaw(-90F);
			else if(playerTpLoc.getBlockX() > blockLoc.getBlockX())
				playerTpLoc.setYaw(90F);
			else if(playerTpLoc.getBlockZ() > blockLoc.getBlockZ())
				playerTpLoc.setYaw(180F);
			else if(playerTpLoc.getBlockZ() < blockLoc.getBlockZ())
				playerTpLoc.setYaw(0F);
			
			Location playerTpEyeLoc = playerTpLoc.clone();
			playerTpEyeLoc.setY(playerTpEyeLoc.getBlockY()+1);
			
			if(!playerTpEyeLoc.getBlock().getType().isAir())
			{
				Message.sendMessage(e.getPlayer(), "&c&lYour current location is wrong! We need more space...\n&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			else
			{
				e.getPlayer().teleport(playerTpLoc);
				e.getPlayer().setFlying(true);
			}
			
			Block block = blockLoc.getBlock();
			blockLoc.getBlock().setType(Material.COMMAND_BLOCK);
			
			CommandBlock blockState = (CommandBlock)block.getState();
			
			blockState.setCommand(this.actionCommandExecute != null ? this.actionCommandExecute : "execute run ");
			blockState.update();
			
			this.cbamPlayer.getPlayer().spawnParticle(Particle.TOTEM, blockLoc, 50);
			
			this.cbamPlayer.setCommandBlock(block, this.cbamPlayer.new CommandBlockUpdate(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.actionName));
			
			Message.sendMessage(e.getPlayer(), "&a&lClick CommandBlock to open GUI and modify executed command");
		}
		else if(menuItem.getSlotOption() == SlotOption.TELEPORT_TO_ACTION_POSITION || menuItem.getSlotOption() == SlotOption.SHOW_TO_ACTION_POSITION_WITHOUT_CLOSE || menuItem.getSlotOption() == SlotOption.SHOW_TO_ACTION_POSITION_WITH_CLOSE)
		{
			if(this.actionPositioned != null) {
				String[] actionPositionedArr = this.actionPositioned.split(" ");
				if(actionPositionedArr.length == 3 && Papi.Function.isNumeric(actionPositionedArr[0]) && Papi.Function.isNumeric(actionPositionedArr[1]) && Papi.Function.isNumeric(actionPositionedArr[2]))
				{
					e.closeMenuForThisPlayer();
					
					World world = Bukkit.getWorld(this.worldName);
					
					double blockX = Double.parseDouble(actionPositionedArr[0]);
					double blockY = Double.parseDouble(actionPositionedArr[1]);
					double blockZ = Double.parseDouble(actionPositionedArr[2]);
					
					if(menuItem.getSlotOption() == SlotOption.TELEPORT_TO_ACTION_POSITION)
						e.getPlayer().teleport(new Location(world, Double.parseDouble(actionPositionedArr[0])+0.5, Double.parseDouble(actionPositionedArr[1])+1, Double.parseDouble(actionPositionedArr[2])+0.5, 0F, 90F));
					else
					{
						PapiPlayer papiPlayer = Papi.Model.getPlayer(e.getPlayer());
						e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getY(), e.getPlayer().getLocation().getZ(),
							papiPlayer.getDirectionYaw(blockX+0.5, blockY+0.5, blockZ+0.5), papiPlayer.getDirectionPitch(blockX+0.5, blockY+0.5, blockZ+0.5)));
					}
					
					Location block1 = new Location(world, blockX, blockY, blockZ);
					Location block2 = new Location(world, blockX, blockY+1, blockZ);
					Location block3 = new Location(world, blockX, blockY+2, blockZ);
					
					this.cbamPlayer.setPositionBlockChangeTask(block1, block2, block3);
					
					e.getPlayer().sendBlockChange(block1, (this.actionRepeatTimeLong <= 0 ? Material.COMMAND_BLOCK.createBlockData() : Material.REPEATING_COMMAND_BLOCK.createBlockData()));
					e.getPlayer().sendBlockChange(block2, Material.AIR.createBlockData());
					e.getPlayer().sendBlockChange(block3, Material.AIR.createBlockData());
					
					if(menuItem.getSlotOption() == SlotOption.SHOW_TO_ACTION_POSITION_WITHOUT_CLOSE)
						this.openPapiMenuPage(e.getPlayer());
				}
				else
					e.getPapiMenuPage().setItemLore(e.getSlot(), "&cPosition is invalid");
			}
			else
				e.getPapiMenuPage().setItemLore(e.getSlot(), "&cPosition not set");
		}
		else if(menuItem.getSlotOption() == SlotOption.PREVIEW_EXECUTE_COMMAND_WITHOUT_CLOSE || menuItem.getSlotOption() == SlotOption.DEBUG_EXECUTE_COMMAND_WITHOUT_CLOSE || menuItem.getSlotOption() == SlotOption.DEBUG_EXECUTE_COMMAND_WITH_CLOSE)
		{
			if(menuItem.getSlotOption() == SlotOption.DEBUG_EXECUTE_COMMAND_WITH_CLOSE)
				e.closeMenuForThisPlayer();
			
			if(this.actionCommandExecute == null)
			{
				if(menuItem.getSlotOption() == SlotOption.PREVIEW_EXECUTE_COMMAND_WITHOUT_CLOSE)
					Message.sendMessage(e.getPlayer(), "&8[&7&lPREVIEW&8] &cCommand not exist!");
				else
					Message.sendMessage(e.getPlayer(), "&8[&7&lDEBUG&8] &cCommand not exist!");
			}
			else
			{
				final String commandExecuteFinal = ProjectManager.prepareCommand(this.actionCommandExecute, this.worldName, this.actionPositioned);
				if(menuItem.getSlotOption() == SlotOption.PREVIEW_EXECUTE_COMMAND_WITHOUT_CLOSE)
				{
					Message.sendRawMessage(e.getPlayer(), "[{\"text\":\""+Message.getColorMessage("&8[&7&lPREVIEW&8] &aPreview execute command:")+"\\n\"},{\"color\":\"aqua\",\"text\":\""+commandExecuteFinal.replaceAll("\"", "\\\\\"")+"\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+Message.getColorMessage("&7&oClick to suggest command")+"\"},\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\""+(commandExecuteFinal.charAt(0)=='/'?commandExecuteFinal:"/"+commandExecuteFinal).replaceAll("\"", "\\\\\"")+"\"}}]");
				}
				else
				{
					Message.sendRawMessage(e.getPlayer(), "[{\"text\":\""+Message.getColorMessage("&8[&7&lDEBUG&8] &aTrying to execute command:")+"\\n\"},{\"color\":\"aqua\",\"text\":\""+commandExecuteFinal.replaceAll("\"", "\\\\\"")+"\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+Message.getColorMessage("&7&oClick to suggest command")+"\"},\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\""+(commandExecuteFinal.charAt(0)=='/'?commandExecuteFinal:"/"+commandExecuteFinal).replaceAll("\"", "\\\\\"")+"\"}}]");
					//Message.sendMessage(e.getPlayer(), "&8[&7&lDEBUG&8] &aTrying to execute command:\n&b"+commandExecuteFinal);
					e.getPlayer().performCommand(commandExecuteFinal);
				}
			}
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
	}

	@Override
	public void onPapiListenerChatEvent(PapiListenerChatEvent e) {
		if(e.getStoreObject() == ListenerChat.DELETE_ACTION) {
			String confirm = e.getMessage();
			
			if(confirm != null && confirm.equals("confirm"))
			{
				Database.deleteAction(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.actionName);
				
				Message.sendMessage(e.getPlayer(), "&a&lDeleted action: &a"+this.actionName);
			}
			else
			{
				Message.sendMessage(e.getPlayer(), "&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					if(backMenu == null)
						MenuManager.openTheProjectActionsMenu(null, cbamPlayer, worldName, projectName, actionsGroup, actionsSubGroup, actionsDelayLong, 1);
					else
						MenuManager.backMenu(cbamPlayer, backMenu);
				}
			});
		}
		else if(e.getStoreObject() == ListenerChat.RENAME_ACTION) {
			String newActionName = e.getMessage();
			
			if(newActionName == null)
			{
				Message.sendMessage(e.getPlayer(), "&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			if(!newActionName.equals(this.actionName))
			{
				Pattern p = Pattern.compile("[^a-zA-Z0-9_\\s]");
				if(p.matcher(newActionName).find())
				{
					Message.sendMessage(e.getPlayer(), "&c&lInvalid name! You can use only a-zA-Z0-9_ and space\n&c&lType a different name in chat");
					e.setCancelled(true);
					return;
				}
				
				if(!Database.isProjectExist(this.worldName, this.projectName))
				{
					Message.sendMessage(e.getPlayer(), "&c&lSomething wrong... The project no longer exists\n&c&lCanceled. Type /cb for reopen menu");
					return;
				}
	
				if(Database.isActionExist(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, newActionName))
				{
					Message.sendMessage(e.getPlayer(), "&c&lAction delayed "+this.actionsDelayLong+"T with that name already exists!\n&c&lType a different name in chat");
					e.setCancelled(true);
					return;
				}
				
				if(!Database.isActionExist(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.actionName))
				{
					Message.sendMessage(e.getPlayer(), "&c&lSomething wrong... The action no longer exists!\n&c&lCanceled. Type /cb for reopen menu");
					return;
				}
				
				Database.renameAction(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.actionName, newActionName);
				
				Message.sendMessage(e.getPlayer(), "&a&lChanged. New action name: &a"+newActionName);
			}
			else
				Message.sendMessage(e.getPlayer(), "&c&lNothing change.");
			
			Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					MenuManager.openTheProjectTheActionMenu(backMenu, cbamPlayer, worldName, projectName, actionsGroup, actionsSubGroup, actionsDelayLong, newActionName);
				}
			});
		}
		else if(e.getStoreObject() == ListenerChat.CHANGE_ACTION_DELAY) {
			String newActionDelayStr = e.getMessage();
			
			if(newActionDelayStr == null)
			{
				Message.sendMessage(e.getPlayer(), "&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			if(!Database.isProjectExist(this.worldName, this.projectName))
			{
				Message.sendMessage(e.getPlayer(), "&c&lSomething wrong... The project no longer exists\n&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			if(!Database.isActionExist(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.actionName))
			{
				Message.sendMessage(e.getPlayer(), "&c&lSomething wrong... The action no longer exists\n&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			final long newActionDelayLong = Validation.getActionsDelayFromInput(newActionDelayStr);
			
			if(newActionDelayLong < 0) {
				Message.sendMessage(e.getPlayer(), "&c&lInvalid delay! You can use only digits and * for multiplication (type '20*60*10' to result 10 minutes)\n&c&lType a different value in chat");
				e.setCancelled(true);
			}
			
			if(Database.isActionExist(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, newActionDelayLong, this.actionName))
			{
				Message.sendMessage(e.getPlayer(), "&c&lOther action with that same name already exist for delay &c"+newActionDelayLong+"T\n&c&lChange action name first!\n&c&lCanceled. Type /cb for reopen menu");
				return;
			}

			Database.setActionDelayTime(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.actionName, newActionDelayLong);
			
			Message.sendMessage(e.getPlayer(), "&a&lChanged. New delay time: &a"+newActionDelayLong+"T ("+(newActionDelayLong/20.0)+"s)");
			
			Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					MenuManager.openTheProjectTheActionMenu(backMenu, cbamPlayer, worldName, projectName, actionsGroup, actionsSubGroup, newActionDelayLong, actionName);
				}
			});
		}
		else if(e.getStoreObject() == ListenerChat.CHANGE_ACTION_REPEATING_TIME) {
			String newActionRepeatTimeStr = e.getMessage();
			
			if(newActionRepeatTimeStr == null)
			{
				Message.sendMessage(e.getPlayer(), "&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			if(!Database.isProjectExist(this.worldName, this.projectName))
			{
				Message.sendMessage(e.getPlayer(), "&c&lSomething wrong... The project no longer exists\n&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			if(!Database.isActionExist(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.actionName))
			{
				Message.sendMessage(e.getPlayer(), "&c&lSomething wrong... The action no longer exists\n&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			final long newActionRepeatTimeLong = Validation.getActionsDelayFromInput(newActionRepeatTimeStr);
			
			if(newActionRepeatTimeLong < 0) {
				Message.sendMessage(e.getPlayer(), "&c&lInvalid delay! You can use only digits and * for multiplication (type '20*60*10' to result 10 minutes)\n&c&lType a different value in chat");
				e.setCancelled(true);
			}
			
			Database.setActionRepeatTime(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.actionName, newActionRepeatTimeLong);

			Message.sendMessage(e.getPlayer(), "&a&lChanged. New repeat time: "+(newActionRepeatTimeLong == 0 ? "&cDISABLED" : "&a"+newActionRepeatTimeLong+"T ("+(newActionRepeatTimeLong/20.0)+"s)"));
			
			Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					MenuManager.backMenu(cbamPlayer, getInstance());
				}
			});
		}
	}

	@Override
	public void onPapiListenerInteractEvent(PapiListenerInteractEvent e) {
		if(e.getStoreObject() == ListenerInteract.SELECT_EXECUTE_COMMAND_POSITION)
		{
			if(e.getEvent() != null && e.getEvent().getAction() == Action.LEFT_CLICK_BLOCK)
			{
				Block clickedBlock = e.getEvent().getClickedBlock();
				if(clickedBlock == null)
				{
					Message.sendMessage(e.getPlayer(), "&c&lCanceled. Type /cb for reopen menu");
					return;
				}
				
				if(!Database.isProjectExist(this.worldName, this.projectName))
				{
					Message.sendMessage(e.getPlayer(), "&c&lSomething wrong... The project no longer exists\n&c&lCanceled. Type /cb for reopen menu");
					return;
				}
				
				if(!Database.isActionExist(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.actionName))
				{
					Message.sendMessage(e.getPlayer(), "&c&lSomething wrong... The action no longer exists\n&c&lCanceled. Type /cb for reopen menu");
					return;
				}
				
				String newActionPositioned = clickedBlock.getLocation().getBlockX() + " " + clickedBlock.getLocation().getBlockY() + " " + clickedBlock.getLocation().getBlockZ();
				
				Database.setActionPositioned(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.actionName, newActionPositioned);
				
				Message.sendMessage(e.getPlayer(), "&a&lPosition changed to &2&l"+newActionPositioned+"&a&l for action: "+this.actionName);
				
				Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
					@Override
					public void run() {
						MenuManager.backMenu(cbamPlayer, getInstance());
					}
				});
			}
			else
				Message.sendMessage(e.getPlayer(), "&c&lCanceled. Type /cb for reopen menu");
		}
	}
}

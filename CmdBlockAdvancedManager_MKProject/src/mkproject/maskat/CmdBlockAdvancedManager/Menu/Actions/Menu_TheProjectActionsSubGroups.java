package mkproject.maskat.CmdBlockAdvancedManager.Menu.Actions;

import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import mkproject.maskat.CmdBlockAdvancedManager.Database;
import mkproject.maskat.CmdBlockAdvancedManager.Plugin;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.MenuManager;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.MenuManager.ListenerChat;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.MenuManager.SlotOption;
import mkproject.maskat.CmdBlockAdvancedManager.Models.CbamPlayer;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuItem;
import mkproject.maskat.Papi.Menu.PapiMenuPage;
import mkproject.maskat.Papi.Menu.PapiMenuV2;
import mkproject.maskat.Papi.Model.PapiChatListener;
import mkproject.maskat.Papi.Model.PapiListenerChatEvent;
import mkproject.maskat.Papi.Utils.Message;

public class Menu_TheProjectActionsSubGroups implements PapiMenuV2, PapiChatListener {
	
	private PapiMenuPage papiMenuPage;
	private PapiMenuV2 backMenu;
	private CbamPlayer cbamPlayer;
	private String worldName;
	private String projectName;
	private String actionsGroup;
	private int actionsGroupParameterRandomnessAmount;
	private int page;
	
	public Menu_TheProjectActionsSubGroups(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, String projectName, String actionsGroup, int page) {
		this.backMenu = backMenu;
		this.cbamPlayer = cbamPlayer;
		this.worldName = worldName;
		this.projectName = projectName;
		this.actionsGroup = actionsGroup;
		this.actionsGroupParameterRandomnessAmount = Database.getActionsGroupParameterRandomnessAmount(worldName, projectName, actionsGroup);
		this.page = page < 1 ? 1 : page;
	}
	
	private PapiMenuV2 getInstance() {
		return this;
	}
	
	@Override
	public PapiMenuPage getPapiMenuPage() {
		return this.papiMenuPage;
	}

	@Override
	public boolean refreshPapiMenuPage() {
		if(!Database.isProjectExist(this.worldName, this.projectName))
		{
			Message.sendMessage(this.cbamPlayer.getPlayer(), "&c&lLast menu can't be opened... The project no longer exists\n&c&lType /cb for reopen menu");
			this.cbamPlayer.unsetLastPapiMenuPage();
			return false;
		}
		
		if(this.papiMenuPage == null)
			this.papiMenuPage = new PapiMenuPage(this, 6, "&5&l"+this.actionsGroup);
		else
			this.papiMenuPage.clearItems();
		this.loadMenuItems();
		return true;
	}
	
	private void loadMenuItems() {
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.GREEN_DYE, "&6&lCategory:\n&6Actions");
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN2, Material.NAME_TAG, "&5Group: &5&l"+this.actionsGroup+"\n&7&oClick to rename", new PapiMenuItem(SlotOption.RENAME_GROUP));
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.REDSTONE, "&5Group: &5&l"+this.actionsGroup+"\n&7&oClick to delete", new PapiMenuItem(SlotOption.DELETE_GROUP));
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.NETHER_STAR, "&2Add new actions subgroup\n&7&oClick to add new group for actions", new PapiMenuItem(SlotOption.ADD_NEW_ACTION_SUBGROUP));
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN8, Material.STRING, "&6Preview actions group:\n&5"+this.actionsGroup+"\n&7&oClick to run preview this project", new PapiMenuItem(SlotOption.PREVIEW_ACTIONS_GROUP));
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.BLAZE_POWDER, "&6Debug actions group:\n&5"+this.actionsGroup+"\n&7&oClick to run debug this project", new PapiMenuItem(SlotOption.DEBUG_ACTIONS_GROUP));
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN1, Material.FIREWORK_ROCKET, "&9Parameter:\n&9&lRandomness"+(this.actionsGroupParameterRandomnessAmount<=0?": DISABLED (run all tasks)":" amount: "+this.actionsGroupParameterRandomnessAmount)+"\n&7&oClick to change", new PapiMenuItem(SlotOption.PARAMETER_RANDOMNESS_AMOUNT));
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		if(this.page > 1)
			this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.PAPER, "&7Previous page", new PapiMenuItem(SlotOption.PREVIOUS_PAGE));
		else
			this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.BOOK, "&7Wróć", new PapiMenuItem(SlotOption.BACK));
		
		//next page button below
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		
		List<String> databaseList = Database.getActionsSubGroups(this.worldName, this.projectName, this.actionsGroup);
		
		if(databaseList == null)
			this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		else
		{
		    int iV=1;
		    int iH=1;
		    
		    int firstElem = 36*(page-1);
		    int lastElem = page*36;
		    
			for(int i=firstElem; i<lastElem;i++) {
				if(i>=databaseList.size())
					break;
				
				String actionsGroup = databaseList.get(i);
				
				InventorySlot invSlot = InventorySlot.valueOf((iV*9)+iH-1);
				
				this.papiMenuPage.setItem(invSlot, Material.KNOWLEDGE_BOOK, "&dActions subgroup:\n&d&l"+actionsGroup+"\n&7&oClick to open", new PapiMenuItem(SlotOption.OPEN_ACTION_SUBGROUP, actionsGroup));
				
				if(iH >= 9) {
					iH=1;
					iV++;
				}
				else
					iH++;
			}
			
			if(databaseList.size() > lastElem)
				this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.PAPER, "&7Next page", new PapiMenuItem(SlotOption.NEXT_PAGE));
			else
				this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
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
				MenuManager.openTheProjectActionsGroupsMenu(null, this.cbamPlayer, this.worldName, this.projectName, 1);
			else
				MenuManager.backMenu(this.cbamPlayer, this.backMenu);
		}
		else if(menuItem.getSlotOption() == SlotOption.PREVIOUS_PAGE)
		{
			MenuManager.openTheProjectActionsSubGroupsMenu(this.backMenu, this.cbamPlayer, this.worldName, this.projectName, this.actionsGroup, this.page-1);
		}
		else if(menuItem.getSlotOption() == SlotOption.NEXT_PAGE)
		{
			MenuManager.openTheProjectActionsSubGroupsMenu(this.backMenu, this.cbamPlayer, this.worldName, this.projectName, this.actionsGroup, this.page+1);
		}
		else if(menuItem.getSlotOption() == SlotOption.RENAME_GROUP)
		{
			Message.sendMessage(e.getPlayer(), "&6&lType new name for group '"+this.actionsGroup+"' in chat");
			e.getPapiPlayer().listenChat(this, ListenerChat.RENAME_GROUP, true);
			e.closeMenuForThisPlayer();
		}
		else if(menuItem.getSlotOption() == SlotOption.DELETE_GROUP)
		{
			Message.sendMessage(e.getPlayer(), "&6&lType &e&lconfirm&6&l to delete group '"+this.actionsGroup+"' in chat");
			e.getPapiPlayer().listenChat(this, ListenerChat.DELETE_GROUP, true);
			e.closeMenuForThisPlayer();
		}
		else if(menuItem.getSlotOption() == SlotOption.PREVIEW_ACTIONS_GROUP)
		{
			Message.sendMessage(e.getPlayer(), "&f&lTrying to execute: &f/cb preview start "+this.worldName+" "+this.projectName+" "+this.actionsGroup);
			e.closeMenuForThisPlayer();
			Bukkit.dispatchCommand(e.getPlayer(), "cb preview start "+this.worldName+" "+this.projectName+" "+this.actionsGroup);
		}
		else if(menuItem.getSlotOption() == SlotOption.DEBUG_ACTIONS_GROUP)
		{
			Message.sendMessage(e.getPlayer(), "&f&lTrying to execute: &f/cb debug start "+this.worldName+" "+this.projectName+" "+this.actionsGroup);
			e.closeMenuForThisPlayer();
			Bukkit.dispatchCommand(e.getPlayer(), "cb debug start "+this.worldName+" "+this.projectName+" "+this.actionsGroup);
		}
		else if(menuItem.getSlotOption() == SlotOption.ADD_NEW_ACTION_SUBGROUP)
		{
			e.closeMenuForThisPlayer();
			Message.sendMessage(e.getPlayer(), "&6&lType name for new subgroup actions in chat");
			e.getPapiPlayer().listenChat(this, ListenerChat.NEW_ACTIONS_SUBGROUP_NAME, true);
		}
		else if(menuItem.getSlotOption() == SlotOption.PARAMETER_RANDOMNESS_AMOUNT)
		{
			e.closeMenuForThisPlayer();
			Message.sendMessage(e.getPlayer(), "&6&lType parameter randomness amount. You can use only digits (0=DISABLE)");
			e.getPapiPlayer().listenChat(this, ListenerChat.CHANGE_PARAMETER_RANDOMNESS_AMOUNT, true);
		}
		else if(menuItem.getSlotOption() == SlotOption.OPEN_ACTION_SUBGROUP)
		{
			MenuManager.openTheProjectActionsDelaysMenu(this, this.cbamPlayer, this.worldName, this.projectName, this.actionsGroup, menuItem.getSlotData().getString(), 1);
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
	}

	@Override
	public void onPapiListenerChatEvent(PapiListenerChatEvent e) {
		if(e.getStoreObject() == ListenerChat.NEW_ACTIONS_SUBGROUP_NAME) {
			String newActionsSubGroupName = e.getMessage();
			
			if(newActionsSubGroupName == null)
			{
				Message.sendMessage(e.getPlayer(), "&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			if(!Database.isProjectExist(this.worldName, this.projectName))
			{
				Message.sendMessage(e.getPlayer(), "&c&lSomething wrong... The project no longer exists\n&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			if(!Database.isActionsGroupExist(this.worldName, this.projectName, this.actionsGroup))
			{
				Message.sendMessage(e.getPlayer(), "&c&lSomething wrong... The group no longer exists\n&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			if(newActionsSubGroupName.equalsIgnoreCase("all") || newActionsSubGroupName.equalsIgnoreCase("parameters"))
			{
				Message.sendMessage(e.getPlayer(), "&c&lInvalid name! You can't use name '"+newActionsSubGroupName+"'!\n&c&lType a different name in chat");
				e.setCancelled(true);
				return;
			}
			
			Pattern p = Pattern.compile("[^a-zA-Z0-9_]");
			if(p.matcher(newActionsSubGroupName).find())
			{
				Message.sendMessage(e.getPlayer(), "&c&lInvalid name! You can use only: a-zA-Z0-9_\n&c&lType a different name in chat");
				e.setCancelled(true);
				return;
			}
			
			if(Database.isActionsSubGroupExist(this.worldName, this.projectName, this.actionsGroup, newActionsSubGroupName))
			{
				Message.sendMessage(e.getPlayer(), "&c&lActions group with that name already exists!\n&c&lType a different name in chat");
				e.setCancelled(true);
				return;
			}
			
			Database.addActionsSubGroup(this.worldName, this.projectName, this.actionsGroup, newActionsSubGroupName);
			
			Message.sendMessage(e.getPlayer(), "&a&lCreated new actions subgroup: &a"+newActionsSubGroupName);
			
			Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					MenuManager.openTheProjectActionsDelaysMenu(getInstance(), cbamPlayer, worldName, projectName, actionsGroup, newActionsSubGroupName, page);
				}
			});
		}
		else if(e.getStoreObject() == ListenerChat.RENAME_GROUP) {
			String newActionsGroupName = e.getMessage();
			
			if(newActionsGroupName == null)
			{
				Message.sendMessage(e.getPlayer(), "&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			if(!Database.isProjectExist(this.worldName, this.projectName))
			{
				Message.sendMessage(e.getPlayer(), "&c&lSomething wrong... The project no longer exists\n&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			if(!Database.isActionsGroupExist(this.worldName, this.projectName, this.actionsGroup))
			{
				Message.sendMessage(e.getPlayer(), "&c&lSomething wrong... The actions group no longer exists\n&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			if(!newActionsGroupName.equals(this.actionsGroup))
			{
				if(newActionsGroupName.equalsIgnoreCase("all") || newActionsGroupName.equalsIgnoreCase("parameters"))
				{
					Message.sendMessage(e.getPlayer(), "&c&lInvalid name! You can't use name '"+newActionsGroupName+"'!\n&c&lType a different name in chat");
					e.setCancelled(true);
					return;
				}
				
				Pattern p = Pattern.compile("[^a-zA-Z0-9_]");
				if(p.matcher(newActionsGroupName).find())
				{
					Message.sendMessage(e.getPlayer(), "&c&lInvalid name! You can use only: a-zA-Z0-9_\n&c&lType a different name in chat");
					e.setCancelled(true);
					return;
				}
				
				if(Database.isActionsGroupExist(this.worldName, this.projectName, newActionsGroupName))
				{
					Message.sendMessage(e.getPlayer(), "&c&lActions group with that name already exists!\n&c&lType a different name in chat");
					e.setCancelled(true);
					return;
				}
				
				Database.renameActionsGroup(this.worldName, this.projectName, this.actionsGroup, newActionsGroupName);
				
				Message.sendMessage(e.getPlayer(), "&a&lRenamed actions group to: &a"+newActionsGroupName);
			}
			else
				Message.sendMessage(e.getPlayer(), "&c&lNothing change.");
			
			Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					MenuManager.openTheProjectActionsSubGroupsMenu(backMenu, cbamPlayer, worldName, projectName, newActionsGroupName, page);
				}
			});
		}
		else if(e.getStoreObject() == ListenerChat.DELETE_GROUP) {
			String confirm = e.getMessage();
			
			if(confirm != null && confirm.equals("confirm"))
			{
				Database.deleteGroup(this.worldName, this.projectName, this.actionsGroup);
				
				Message.sendMessage(e.getPlayer(), "&a&lDeleted actions group: &a"+this.actionsGroup);
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
						MenuManager.openTheProjectActionsGroupsMenu(null, cbamPlayer, worldName, projectName, 1);
					else
						MenuManager.backMenu(cbamPlayer, backMenu);
				}
			});
		}
		else if(e.getStoreObject() == ListenerChat.CHANGE_PARAMETER_RANDOMNESS_AMOUNT) {
			String newActionsGroupRandomnessAmount = e.getMessage();
			
			if(newActionsGroupRandomnessAmount == null)
			{
				Message.sendMessage(e.getPlayer(), "&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			if(!Database.isProjectExist(this.worldName, this.projectName))
			{
				Message.sendMessage(e.getPlayer(), "&c&lSomething wrong... The project no longer exists\n&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			if(!Papi.Function.isNumeric(newActionsGroupRandomnessAmount))
			{
				Message.sendMessage(e.getPlayer(), "&c&lInvalid delay! You can use only digits (0=DISABLE)\n&c&lType a different value in chat");
				e.setCancelled(true);
				return;
			}
			
			final int newActionsGroupRandomnessAmountFinal = Integer.parseInt(newActionsGroupRandomnessAmount);
			
			if(newActionsGroupRandomnessAmountFinal < 0) {
				Message.sendMessage(e.getPlayer(), "&c&lInvalid delay! You can use only digits (0=DISABLE)\n&c&lType a different value in chat");
				e.setCancelled(true);
				return;
			}
			
			Database.setActionsGroupParameterRandomnessAmount(this.worldName, this.projectName, this.actionsGroup, newActionsGroupRandomnessAmountFinal);
			
			Message.sendMessage(e.getPlayer(), "&a&lParameter 'Randomness' changed to: " + (newActionsGroupRandomnessAmountFinal == 0 ? "DISABLE" : newActionsGroupRandomnessAmountFinal));
			
			Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					MenuManager.openTheProjectActionsSubGroupsMenu(backMenu, cbamPlayer, worldName, projectName, actionsGroup, page);
				}
			});
		}
	}
}

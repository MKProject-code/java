package mkproject.maskat.CmdBlockAdvancedManager.Menu.Actions;

import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import mkproject.maskat.CmdBlockAdvancedManager.Database;
import mkproject.maskat.CmdBlockAdvancedManager.Validation;
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

public class Menu_TheProjectActionsDelays implements PapiMenuV2, PapiChatListener {
	
	private PapiMenuPage papiMenuPage;
	private PapiMenuV2 backMenu;
	private CbamPlayer cbamPlayer;
	private String worldName;
	private String projectName;
	private String actionsGroup;
	private String actionsSubGroup;
	private int page;
	
	public Menu_TheProjectActionsDelays(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, String projectName, String actionsGroup, String actionsSubGroup, int page) {
		this.backMenu = backMenu;
		this.cbamPlayer = cbamPlayer;
		this.worldName = worldName;
		this.projectName = projectName;
		this.actionsGroup = actionsGroup;
		this.actionsSubGroup = actionsSubGroup;
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
			this.papiMenuPage = new PapiMenuPage(this, 6, "&5&l"+this.actionsGroup+" &d&l"+this.actionsSubGroup);
		else
			this.papiMenuPage.clearItems();
		this.loadMenuItems();
		return true;
	}
	
	private void loadMenuItems() {
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.GREEN_DYE, "&6&lCategory:\n&6Actions");
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN2, Material.NAME_TAG, "&dSubgroup: &d&l"+this.actionsSubGroup+"\n&7&oClick to rename", new PapiMenuItem(SlotOption.RENAME_SUBGROUP));
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.REDSTONE, "&dSubgroup: &d&l"+this.actionsSubGroup+"\n&7&oClick to delete", new PapiMenuItem(SlotOption.DELETE_SUBGROUP));
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.NETHER_STAR, "&2Select actions delay\n&7&oClick to select delay for actions", new PapiMenuItem(SlotOption.ADD_NEW_ACTION_DELAY));
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");

//		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN8, Material.STRING, "&6Preview actions group:\n&d"+this.actionsGroup+"\n&7&oClick to run preview this project", new PapiMenuItem(SlotOption.PREVIEW_ACTIONS_GROUP));
//		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.BLAZE_POWDER, "&6Debug actions group:\n&d"+this.actionsGroup+"\n&7&oClick to run debug this project", new PapiMenuItem(SlotOption.DEBUG_ACTIONS_GROUP));
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
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
		
		List<Long> databaseList = Database.getActionsDelays(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup);

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
				
				long actionsDelayLong = databaseList.get(i);
				
				InventorySlot invSlot = InventorySlot.valueOf((iV*9)+iH-1);
				
				List<String> subDatabaseList = Database.getActions(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, actionsDelayLong);
				
				this.papiMenuPage.setItem(invSlot, Material.REPEATER, "&bActions delayed:\n&b&l"+actionsDelayLong+"T ("+(actionsDelayLong/20.0)+"s)\n&7&oClick to open\n&7- &f"+String.join("\n&7- &f", subDatabaseList), new PapiMenuItem(SlotOption.OPEN_ACTION_IN_DELAY, actionsDelayLong));
				
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
			MenuManager.openTheProjectActionsDelaysMenu(this.backMenu, this.cbamPlayer, this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.page-1);
		}
		else if(menuItem.getSlotOption() == SlotOption.NEXT_PAGE)
		{
			MenuManager.openTheProjectActionsDelaysMenu(this.backMenu, this.cbamPlayer, this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.page+1);
		}
		else if(menuItem.getSlotOption() == SlotOption.RENAME_SUBGROUP)
		{
			Message.sendMessage(e.getPlayer(), "&6&lType new name for subgroup '"+this.actionsSubGroup+"' in chat");
			e.getPapiPlayer().listenChat(this, ListenerChat.RENAME_SUBGROUP, true);
			e.closeMenuForThisPlayer();
		}
		else if(menuItem.getSlotOption() == SlotOption.DELETE_SUBGROUP)
		{
			Message.sendMessage(e.getPlayer(), "&6&lType &e&lconfirm&6&l to delete subgroup '"+this.actionsSubGroup+"' in chat");
			e.getPapiPlayer().listenChat(this, ListenerChat.DELETE_SUBGROUP, true);
			e.closeMenuForThisPlayer();
		}
		else if(menuItem.getSlotOption() == SlotOption.ADD_NEW_ACTION_DELAY)
		{
			e.closeMenuForThisPlayer();
			Message.sendMessage(e.getPlayer(), "&6&lType actions delay of ticks in chat (20 ticks ≈ 1 secound). You can use only digits and * for multiplication (type '20*60*10' to result 10 minutes)");
			e.getPapiPlayer().listenChat(this, ListenerChat.NEW_ACTION_DELAY, true);
		}
		else if(menuItem.getSlotOption() == SlotOption.OPEN_ACTION_IN_DELAY)
		{
			MenuManager.openTheProjectActionsMenu(this, this.cbamPlayer, this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, menuItem.getSlotData().getLong(), this.page);
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
	}

	@Override
	public void onPapiListenerChatEvent(PapiListenerChatEvent e) {
		if(e.getStoreObject() == ListenerChat.NEW_ACTION_DELAY) {
			String newActionsDelay = e.getMessage();
			
			if(newActionsDelay == null)
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
			
			if(!Database.isActionsSubGroupExist(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup))
			{
				Message.sendMessage(e.getPlayer(), "&c&lSomething wrong... The subgroup no longer exists\n&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			final long newActionsDelayLongFinal = Validation.getActionsDelayFromInput(newActionsDelay);
			
			if(newActionsDelayLongFinal < 0) {
				Message.sendMessage(e.getPlayer(), "&c&lInvalid delay! You can use only digits and * for multiplication (type '20*60*10' to result 10 minutes)\n&c&lType a different value in chat");
				e.setCancelled(true);
				return;
			}
			
			Message.sendMessage(e.getPlayer(), "&a&lSelected actions delay: &a"+newActionsDelayLongFinal+"T ("+newActionsDelayLongFinal/20.0+"s)");
			
			Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					MenuManager.openTheProjectActionsMenu(getInstance(), cbamPlayer, worldName, projectName, actionsGroup, actionsSubGroup, newActionsDelayLongFinal, page);
				}
			});
		}
		else if(e.getStoreObject() == ListenerChat.RENAME_SUBGROUP) {
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
				Message.sendMessage(e.getPlayer(), "&c&lSomething wrong... The actions group no longer exists\n&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			if(!Database.isActionsSubGroupExist(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup))
			{
				Message.sendMessage(e.getPlayer(), "&c&lSomething wrong... The actions subgroup no longer exists\n&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			if(!newActionsSubGroupName.equals(this.actionsSubGroup))
			{
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
					Message.sendMessage(e.getPlayer(), "&c&lActions subgroup with that name already exists!\n&c&lType a different name in chat");
					e.setCancelled(true);
					return;
				}
				
				Database.renameActionsSubGroup(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, newActionsSubGroupName);
				
				Message.sendMessage(e.getPlayer(), "&a&lRenamed actions subgroup to: &a"+newActionsSubGroupName);
			}
			else
				Message.sendMessage(e.getPlayer(), "&c&lNothing change.");
			
			Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					MenuManager.openTheProjectActionsDelaysMenu(backMenu, cbamPlayer, worldName, projectName, actionsGroup, newActionsSubGroupName, page);
				}
			});
		}
		else if(e.getStoreObject() == ListenerChat.DELETE_SUBGROUP) {
			String confirm = e.getMessage();
			
			if(confirm != null && confirm.equals("confirm"))
			{
				Database.deleteSubGroup(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup);
				
				Message.sendMessage(e.getPlayer(), "&a&lDeleted actions subgroup: &a"+this.actionsSubGroup);
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
						MenuManager.openTheProjectActionsSubGroupsMenu(null, cbamPlayer, worldName, projectName, actionsGroup, 1);
					else
						MenuManager.backMenu(cbamPlayer, backMenu);
				}
			});
		}
	}
}

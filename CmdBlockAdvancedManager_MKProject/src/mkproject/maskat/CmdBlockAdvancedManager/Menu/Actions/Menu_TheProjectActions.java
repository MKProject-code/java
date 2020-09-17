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
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuItem;
import mkproject.maskat.Papi.Menu.PapiMenuPage;
import mkproject.maskat.Papi.Menu.PapiMenuV2;
import mkproject.maskat.Papi.Model.PapiChatListener;
import mkproject.maskat.Papi.Model.PapiListenerChatEvent;
import mkproject.maskat.Papi.Utils.Message;

public class Menu_TheProjectActions implements PapiMenuV2, PapiChatListener {
	
	private PapiMenuPage papiMenuPage;
	private PapiMenuV2 backMenu;
	private CbamPlayer cbamPlayer;
	private String worldName;
	private String projectName;
	private String actionsGroup;
	private String actionsSubGroup;
	private long actionsDelayLong;
	private int page;
	
	public Menu_TheProjectActions(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, String projectName, String actionsGroup, String actionsSubGroup, long actionsDelayLong, int page) {
		this.backMenu = backMenu;
		this.cbamPlayer = cbamPlayer;
		this.worldName = worldName;
		this.projectName = projectName;
		this.actionsGroup = actionsGroup;
		this.actionsSubGroup = actionsSubGroup;
		this.actionsDelayLong = actionsDelayLong;
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
			this.papiMenuPage = new PapiMenuPage(this, 6, "&d&l"+this.actionsSubGroup+" &1&l"+this.actionsDelayLong+"T = "+(this.actionsDelayLong/20.0)+"s");
		else
			this.papiMenuPage.clearItems();
		this.loadMenuItems();
		return true;
	}
	
	private void loadMenuItems() {
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.GREEN_DYE, "&6&lCategory:\n&6Actions");
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.NETHER_STAR, "&2Add new action\n&7&oClick to add new action with delay "+this.actionsDelayLong+"T ("+(this.actionsDelayLong/20.0)+"s)", new PapiMenuItem(SlotOption.ADD_NEW_ACTION));
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN8, Material.STRING, "&6Preview actions delayed:\n&d"+this.actionsGroup+"\n&7&oClick to run preview this project", new PapiMenuItem(SlotOption.PREVIEW_ACTIONS_DELAYED));
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.BLAZE_POWDER, "&6Debug actions delayed:\n&d"+this.actionsGroup+"\n&7&oClick to run debug this project", new PapiMenuItem(SlotOption.DEBUG_ACTIONS_DELAYED));
		
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
		
		List<String> databaseList = Database.getActions(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong);
		
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
				
				String actionName = databaseList.get(i);
				
				InventorySlot invSlot = InventorySlot.valueOf((iV*9)+iH-1);
				
				long actionRepeatTimeLong = Database.getActionRepeatTime(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, actionName);
				
				this.papiMenuPage.setItem(invSlot, (actionRepeatTimeLong > 0 ? Material.REPEATING_COMMAND_BLOCK : Material.COMMAND_BLOCK), "&aAction:\n&a&l"+actionName+"\n&dDelay: &e&l"+this.actionsDelayLong+"T ("+(this.actionsDelayLong/20.0)+"s)\n&dRepeating time: "+(actionRepeatTimeLong <=0 ? "&c&lDISABLED" : "&e&l"+actionRepeatTimeLong+"T ("+(actionRepeatTimeLong/20.0)+"s)")+"\n&7&oClick to open", new PapiMenuItem(SlotOption.OPEN_ACTION, actionName));
				
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
				MenuManager.openTheProjectActionsDelaysMenu(null, this.cbamPlayer, this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, 1);
			else
				MenuManager.backMenu(this.cbamPlayer, this.backMenu);
		}
		else if(menuItem.getSlotOption() == SlotOption.PREVIOUS_PAGE)
		{
			MenuManager.openTheProjectActionsMenu(this.backMenu, this.cbamPlayer, this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.page-1);
		}
		else if(menuItem.getSlotOption() == SlotOption.NEXT_PAGE)
		{
			MenuManager.openTheProjectActionsMenu(this.backMenu, this.cbamPlayer, this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, this.page+1);
		}
		else if(menuItem.getSlotOption() == SlotOption.PREVIEW_ACTIONS_DELAYED)
		{
			Message.sendMessage(e.getPlayer(), "&f&lTrying to execute: &f/cb preview start "+this.worldName+" "+this.projectName+" "+this.actionsGroup+" "+this.actionsDelayLong+" 0");
			e.closeMenuForThisPlayer();
			Bukkit.dispatchCommand(e.getPlayer(), "cb preview start "+this.worldName+" "+this.projectName+" "+this.actionsGroup+" "+this.actionsDelayLong+" 0");
		}
		else if(menuItem.getSlotOption() == SlotOption.DEBUG_ACTIONS_DELAYED)
		{
			Message.sendMessage(e.getPlayer(), "&f&lTrying to execute: &f/cb debug start "+this.worldName+" "+this.projectName+" "+this.actionsGroup+" "+this.actionsDelayLong+" 0");
			e.closeMenuForThisPlayer();
			Bukkit.dispatchCommand(e.getPlayer(), "cb debug start "+this.worldName+" "+this.projectName+" "+this.actionsGroup+" "+this.actionsDelayLong+" 0");
		}
		else if(menuItem.getSlotOption() == SlotOption.ADD_NEW_ACTION)
		{
			e.closeMenuForThisPlayer();
			Message.sendMessage(e.getPlayer(), "&6&lType name for new action in chat");
			e.getPapiPlayer().listenChat(this, ListenerChat.ADD_NEW_ACTION, true);
		}
		else if(menuItem.getSlotOption() == SlotOption.OPEN_ACTION)
		{
			MenuManager.openTheProjectTheActionMenu(this, this.cbamPlayer, this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, menuItem.getSlotData().getString());
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
	}

	@Override
	public void onPapiListenerChatEvent(PapiListenerChatEvent e) {
		if(e.getStoreObject() == ListenerChat.ADD_NEW_ACTION) {
			String newActionName = e.getMessage();
			
			if(newActionName == null)
			{
				Message.sendMessage(e.getPlayer(), "&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			Pattern p = Pattern.compile("[^a-zA-Z0-9_]");
			if(p.matcher(newActionName).find())
			{
				Message.sendMessage(e.getPlayer(), "&c&lInvalid name! You can use only a-zA-Z0-9_\n&c&lType a different name in chat");
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
			
			Database.addAction(this.worldName, this.projectName, this.actionsGroup, this.actionsSubGroup, this.actionsDelayLong, newActionName);
			
			Message.sendMessage(e.getPlayer(), "&a&lCreated new action: &a"+newActionName);
			
			Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					MenuManager.openTheProjectTheActionMenu(getInstance(), cbamPlayer, worldName, projectName, actionsGroup, actionsSubGroup, actionsDelayLong, newActionName);
				}
			});
		}
	}



	
}

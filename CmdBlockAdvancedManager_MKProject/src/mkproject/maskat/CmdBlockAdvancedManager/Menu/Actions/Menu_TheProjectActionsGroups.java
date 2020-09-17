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

public class Menu_TheProjectActionsGroups implements PapiMenuV2, PapiChatListener {
	
	private PapiMenuPage papiMenuPage;
	private PapiMenuV2 backMenu;
	private CbamPlayer cbamPlayer;
	private String worldName;
	private String projectName;
	private int page;
	
	public Menu_TheProjectActionsGroups(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, String projectName, int page) {
		this.backMenu = backMenu;
		this.cbamPlayer = cbamPlayer;
		this.worldName = worldName;
		this.projectName = projectName;
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
			this.papiMenuPage = new PapiMenuPage(this, 6, "&4&l"+this.projectName);
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
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.NETHER_STAR, "&2Add new actions group\n&7&oClick to add new group for actions", new PapiMenuItem(SlotOption.ADD_NEW_ACTION_GROUP));
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
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
		
		
		List<String> databaseList = Database.getActionsGroups(this.worldName, this.projectName);
		
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
				
				this.papiMenuPage.setItem(invSlot, Material.ENCHANTED_BOOK, "&5Actions group:\n&5&l"+actionsGroup+"\n&7&oClick to open", new PapiMenuItem(SlotOption.OPEN_ACTION_GROUP, actionsGroup));
				
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
				MenuManager.openTheProjectMenu(null, this.cbamPlayer, this.worldName, this.projectName);
			else
				MenuManager.backMenu(this.cbamPlayer, this.backMenu);
		}
		else if(menuItem.getSlotOption() == SlotOption.PREVIOUS_PAGE)
		{
			MenuManager.openTheProjectActionsGroupsMenu(this.backMenu, this.cbamPlayer, this.worldName, this.projectName, this.page-1);
		}
		else if(menuItem.getSlotOption() == SlotOption.NEXT_PAGE)
		{
			MenuManager.openTheProjectActionsGroupsMenu(this.backMenu, this.cbamPlayer, this.worldName, this.projectName, this.page+1);
		}
		else if(menuItem.getSlotOption() == SlotOption.ADD_NEW_ACTION_GROUP)
		{
			e.closeMenuForThisPlayer();
			Message.sendMessage(e.getPlayer(), "&6&lType name for new group actions in chat");
			e.getPapiPlayer().listenChat(this, ListenerChat.NEW_ACTIONS_GROUP_NAME, true);
		}
		else if(menuItem.getSlotOption() == SlotOption.OPEN_ACTION_GROUP)
		{
			MenuManager.openTheProjectActionsSubGroupsMenu(this, this.cbamPlayer, this.worldName, this.projectName, menuItem.getSlotData().getString(), 1);
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
	}

	@Override
	public void onPapiListenerChatEvent(PapiListenerChatEvent e) {
		if(e.getStoreObject() == ListenerChat.NEW_ACTIONS_GROUP_NAME) {
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
			
			Database.addActionsGroup(this.worldName, this.projectName, newActionsGroupName);
			
			Message.sendMessage(e.getPlayer(), "&a&lCreated new actions group: &a"+newActionsGroupName);
			
			Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					MenuManager.openTheProjectActionsSubGroupsMenu(getInstance(), cbamPlayer, worldName, projectName, newActionsGroupName, page);
				}
			});
		}
	}
}

package mkproject.maskat.CmdBlockAdvancedManager.Menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import mkproject.maskat.CmdBlockAdvancedManager.Plugin;
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

public class Menu_TheProject implements PapiMenuV2, PapiChatListener {
	
	private PapiMenuPage papiMenuPage;
	private PapiMenuV2 backMenu;
	private CbamPlayer cbamPlayer;
	private String worldName;
	private String projectName;
	
	public Menu_TheProject(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, String projectName) {
		this.backMenu = backMenu;
		this.cbamPlayer = cbamPlayer;
		this.worldName = worldName;
		this.projectName = projectName;
	}
	
//	private PapiMenuV2 getInstance() {
//		return this;
//	}
	
	@Override
	public PapiMenuPage getPapiMenuPage() {
		return this.papiMenuPage;
	}
	
	@Override
	public boolean refreshPapiMenuPage() {
		if(Plugin.getPlugin().getConfig().get("database."+this.worldName+"."+this.projectName) == null)
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
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.REDSTONE, "&4Delete project: &c"+this.projectName+"\n&7&oClick to delete this project", new PapiMenuItem(SlotOption.DELETE_PROJECT));
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN8, Material.STRING, "&6Preview project: &c"+this.projectName+"\n&7&oClick to run preview this project", new PapiMenuItem(SlotOption.PREVIEW_PROJECT));
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.BLAZE_POWDER, "&6Debug project: &c"+this.projectName+"\n&7&oClick to run debug this project", new PapiMenuItem(SlotOption.DEBUG_PROJECT));
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");

		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.BOOK, "&7Wróć", new PapiMenuItem(SlotOption.BACK));
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN4, Material.GREEN_DYE, "&6Actions\n&7&oClick to open", new PapiMenuItem(SlotOption.ACTIONS));
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN6, Material.YELLOW_DYE, "&6Events\n&7&oClick to open", new PapiMenuItem(SlotOption.EVENTS));
//		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN7, Material.RED_DYE, "&6Actions when end/stop\n&7&oClick to open", new PapiMenuItem(SlotOption.ACTIONS_WHEN_END));
	}

	@Override
	public void onMenuClick(PapiMenuClickEvent e) {
		if(e.getSlotStoreObject() == null)
			return;

		PapiMenuItem menuItem = (PapiMenuItem)e.getSlotStoreObject();
		if(menuItem.getSlotOption() == SlotOption.BACK)
		{
			if(this.backMenu == null)
				MenuManager.openProjectsMenu(null, this.cbamPlayer, this.worldName, 1);
			else
				MenuManager.backMenu(this.cbamPlayer, this.backMenu);
		}
		else if(menuItem.getSlotOption() == SlotOption.DELETE_PROJECT)
		{
			Message.sendMessage(e.getPlayer(), "&6&lType &e&lconfirm&6&l to delete project '"+this.projectName+"' in chat");
			e.getPapiPlayer().listenChat(this, ListenerChat.DELETE_PROJECT, true);
			e.closeMenuForThisPlayer();
		}
		else if(menuItem.getSlotOption() == SlotOption.PREVIEW_PROJECT)
		{
			Message.sendMessage(e.getPlayer(), "&f&lTrying to execute: &f/cb preview start "+this.worldName+" "+this.projectName+" all");
			e.closeMenuForThisPlayer();
			Bukkit.dispatchCommand(e.getPlayer(), "cb preview start "+this.worldName+" "+this.projectName+" all");
		}
		else if(menuItem.getSlotOption() == SlotOption.DEBUG_PROJECT)
		{
			Message.sendMessage(e.getPlayer(), "&f&lTrying to execute: &f/cb debug start "+this.worldName+" "+this.projectName+" all");
			e.closeMenuForThisPlayer();
			Bukkit.dispatchCommand(e.getPlayer(), "cb debug start "+this.worldName+" "+this.projectName+" all");
		}
		else if(menuItem.getSlotOption() == SlotOption.ACTIONS)
		{
			MenuManager.openTheProjectActionsGroupsMenu(this, this.cbamPlayer, this.worldName, this.projectName, 1);
		}
		else if(menuItem.getSlotOption() == SlotOption.EVENTS)
		{
			MenuManager.openTheProjectEventsTypesMenu(this, this.cbamPlayer, this.worldName, this.projectName);
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
	}

	@Override
	public void onPapiListenerChatEvent(PapiListenerChatEvent e) {
		if(e.getStoreObject() == ListenerChat.DELETE_PROJECT) {
			String confirm = e.getMessage();
			
			if(confirm != null && confirm.equals("confirm"))
			{
				Plugin.getPlugin().getConfig().set("database."+this.worldName+"."+this.projectName, null);
				Plugin.getPlugin().saveConfig();
				Message.sendMessage(e.getPlayer(), "&a&lDeleted project '"+this.projectName+"'");
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
						MenuManager.openProjectsMenu(null, cbamPlayer, worldName, 1);
					else
						MenuManager.backMenu(cbamPlayer, backMenu);
				}
			});
		}
	}



	
}

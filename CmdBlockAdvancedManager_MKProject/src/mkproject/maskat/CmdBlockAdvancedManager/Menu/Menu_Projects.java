package mkproject.maskat.CmdBlockAdvancedManager.Menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

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

public class Menu_Projects implements PapiMenuV2, PapiChatListener {
	
	private PapiMenuPage papiMenuPage;
	private PapiMenuV2 backMenu;
	private CbamPlayer cbamPlayer;
	private String worldName;
	private int page;
	
	public Menu_Projects(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, int page) {
		this.backMenu = backMenu;
		this.cbamPlayer = cbamPlayer;
		this.worldName = worldName;
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
		if(this.papiMenuPage == null)
			this.papiMenuPage = new PapiMenuPage(this, 6, "&lProjects on current map");
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
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.NETHER_STAR, "&2Add new project in &a"+this.worldName+"\n&7&oClick to add new project", new PapiMenuItem(SlotOption.ADD_NEW_PROJECT));
		
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
		
		ConfigurationSection databaseSection = Plugin.getPlugin().getConfig().getConfigurationSection("database."+this.worldName);
		
		if(databaseSection != null)
		{
			List<String> databaseProjectsInWorld = new ArrayList<>(databaseSection.getKeys(false));
			Collections.sort(databaseProjectsInWorld);
			
		    int iV=1;
		    int iH=1;
		    
		    int firstElem = 36*(page-1);
		    int lastElem = page*36;
		    
			for(int i=firstElem; i<lastElem;i++) {
				if(i>=databaseProjectsInWorld.size())
					break;
				
				String projectName = databaseProjectsInWorld.get(i);
				
				InventorySlot invSlot = InventorySlot.valueOf((iV*9)+iH-1);
				
				this.papiMenuPage.setItem(invSlot, Material.CAMPFIRE, "&cProject:\n&c&l"+projectName+"\n&7&oClick to open", new PapiMenuItem(SlotOption.OPEN_PROJECT, projectName));
				
				if(iH >= 9) {
					iH=1;
					iV++;
				}
				else
					iH++;
			}
			
			if(databaseProjectsInWorld.size() > lastElem)
				this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.PAPER, "&7Next page", new PapiMenuItem(SlotOption.NEXT_PAGE));
			else
				this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		}
		else
		{
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
				MenuManager.openPluginMenu(this.cbamPlayer);
			else
				MenuManager.backMenu(this.cbamPlayer, this.backMenu);
		}
		else if(menuItem.getSlotOption() == SlotOption.PREVIOUS_PAGE)
		{
			MenuManager.openProjectsMenu(this.backMenu, this.cbamPlayer, this.worldName, this.page-1);
		}
		else if(menuItem.getSlotOption() == SlotOption.NEXT_PAGE)
		{
			MenuManager.openProjectsMenu(this.backMenu, this.cbamPlayer, this.worldName, this.page+1);
		}
		else if(menuItem.getSlotOption() == SlotOption.ADD_NEW_PROJECT)
		{
			e.closeMenuForThisPlayer();
			Message.sendMessage(e.getPlayer(), "&6&lType name for new project in chat");
			e.getPapiPlayer().listenChat(this, ListenerChat.NEW_PROJECT_NAME, true);
		}
		else if(menuItem.getSlotOption() == SlotOption.OPEN_PROJECT)
		{
			MenuManager.openTheProjectMenu(this, this.cbamPlayer, this.worldName, menuItem.getSlotData().getString());
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
	}

	@Override
	public void onPapiListenerChatEvent(PapiListenerChatEvent e) {
		if(e.getStoreObject() == ListenerChat.NEW_PROJECT_NAME) {
			String newProjectName = e.getMessage();
			
			if(newProjectName == null)
			{
				Message.sendMessage(e.getPlayer(), "&c&lCanceled. Type /cb for reopen menu");
				return;
			}
			
			Pattern p = Pattern.compile("[^a-zA-Z0-9_]");
			if(p.matcher(newProjectName).find())
			{
				Message.sendMessage(e.getPlayer(), "&c&lInvalid name! You can use only a-zA-Z0-9_\n&c&lType a different name in chat");
				e.setCancelled(true);
				return;
			}
			
			if(Plugin.getPlugin().getConfig().get("database."+this.worldName+"."+newProjectName) != null)
			{
				Message.sendMessage(e.getPlayer(), "&c&lProject with that name already exists!\n&c&lType a different name in chat");
				e.setCancelled(true);
				return;
			}
			
			Plugin.getPlugin().getConfig().set("database."+this.worldName+"."+newProjectName, "");
			
//			Plugin.getPlugin().getConfig().set("database."+this.worldName+"."+newProjectName+".ActionsStart", "");
//			Plugin.getPlugin().getConfig().set("database."+this.worldName+"."+newProjectName+".Events", "");
//			Plugin.getPlugin().getConfig().set("database."+this.worldName+"."+newProjectName+".ActionsEnd", "");
			
			Plugin.getPlugin().saveConfig();
			
			Message.sendMessage(e.getPlayer(), "&a&lCreated new project: "+newProjectName);
			
			Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					MenuManager.openTheProjectMenu(getInstance(), cbamPlayer, worldName, newProjectName);
				}
			});
		}
	}
}

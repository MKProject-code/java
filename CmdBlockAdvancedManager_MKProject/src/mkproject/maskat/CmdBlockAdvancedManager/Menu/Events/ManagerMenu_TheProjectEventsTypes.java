package mkproject.maskat.CmdBlockAdvancedManager.Menu.Events;

import org.bukkit.Material;

import mkproject.maskat.CmdBlockAdvancedManager.Database;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.MenuManager;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.MenuManager.ListenerChat;
import mkproject.maskat.CmdBlockAdvancedManager.Menu.MenuManager.SlotOption;
import mkproject.maskat.CmdBlockAdvancedManager.Models.CbamPlayer;
import mkproject.maskat.Papi.PapiHeadBase64;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuItem;
import mkproject.maskat.Papi.Menu.PapiMenuPage;
import mkproject.maskat.Papi.Menu.PapiMenuV2;
import mkproject.maskat.Papi.Model.PapiChatListener;
import mkproject.maskat.Papi.Model.PapiListenerChatEvent;
import mkproject.maskat.Papi.Utils.Message;

public class ManagerMenu_TheProjectEventsTypes implements PapiMenuV2, PapiChatListener {
	
	private PapiMenuPage papiMenuPage;
	private PapiMenuV2 backMenu;
	private CbamPlayer cbamPlayer;
	private String worldName;
	private String projectName;
	
	public ManagerMenu_TheProjectEventsTypes(PapiMenuV2 backMenu, CbamPlayer cbamPlayer, String worldName, String projectName) {
		this.backMenu = backMenu;
		this.cbamPlayer = cbamPlayer;
		this.worldName = worldName;
		this.projectName = projectName;

		this.refreshPapiMenuPage();
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
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.YELLOW_DYE, "&6&lCategory:\n&6Events");
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
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
		
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN2, Material.JACK_O_LANTERN, "&6Player join\n&7&oClick to open\n&cfuture...");
		this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN2, Material.CARVED_PUMPKIN, "&6Player leave\n&7&oClick to open\n&cfuture...");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN3, Material.REDSTONE_TORCH, "&6Player interact block\n&7&oClick to open\n&cfuture...");
		this.papiMenuPage.setItemHeadAsync(InventorySlot.ROW4_COLUMN3, Material.AIR, PapiHeadBase64.Villager, "&6Player interact entity\n&7&oClick to open\n&cfuture...");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN4, Material.ELYTRA, "&6Player move\n&7&oClick to open\n&cfuture...");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.IRON_SWORD, "&6Player damage\n&7&oClick to open\n&cfuture...");
		this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN5, Material.WITHER_ROSE, "&6Player death\n&7&oClick to open\n&cfuture...");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN6, Material.STONE_BRICKS, "&6Player place block\n&7&oClick to open\n&cfuture...");
		this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN6, Material.CRACKED_STONE_BRICKS, "&6Player break block\n&7&oClick to open\n&cfuture...");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN7, Material.HOPPER, "&6Player drop item\n&7&oClick to open\n&cfuture...");
		this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN7, Material.CHEST, "&6Player pickup item\n&7&oClick to open\n&cfuture...");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN8, Material.RESPAWN_ANCHOR, "&6Player respawn\n&7&oClick to open\n&cfuture...");
	}

	@Override
	public void onMenuClick(PapiMenuClickEvent e) {
		if(e.getSlotStoreObject() == null)
			return;

		PapiMenuItem menuItem = (PapiMenuItem)e.getSlotStoreObject();
		if(menuItem.getSlotOption() == SlotOption.BACK)
		{
			MenuManager.backMenu(this.cbamPlayer, this.backMenu);
		}
		else if(menuItem.getSlotOption() == SlotOption.DELETE_PROJECT)
		{
			Message.sendMessage(e.getPlayer(), "&6&lType &e&lconfirm&6&l to delete project '"+this.projectName+"' in chat");
			e.getPapiPlayer().listenChat(this, ListenerChat.DELETE_PROJECT, true);
			e.closeMenuForThisPlayer();
		}
//		else if(menuItem.getSlotOption() == SlotOption.PREVIEW_PROJECT)
//		{
//			Message.sendMessage(e.getPlayer(), "&f&lTrying to execute: &f/cb preview start "+this.worldName+" "+this.projectName);
//			e.closeMenuForThisPlayer();
//			Bukkit.dispatchCommand(e.getPlayer(), "cb preview start "+this.worldName+" "+this.projectName);
//		}
//		else if(menuItem.getSlotOption() == SlotOption.DEBUG_PROJECT)
//		{
//			Message.sendMessage(e.getPlayer(), "&f&lTrying to execute: &f/cb debug start "+this.worldName+" "+this.projectName);
//			e.closeMenuForThisPlayer();
//			Bukkit.dispatchCommand(e.getPlayer(), "cb debug start "+this.worldName+" "+this.projectName);
//		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
	}

	@Override
	public void onPapiListenerChatEvent(PapiListenerChatEvent e) {
//		if(e.getStoreObject() == ListenerChat.DELETE_PROJECT) {
//			String confirm = e.getMessage();
//			
//			if(confirm != null && confirm.equals("confirm"))
//			{
//				Plugin.getPlugin().getConfig().set("database."+this.worldName+"."+this.projectName, null);
//				Plugin.getPlugin().saveConfig();
//				Message.sendMessage(e.getPlayer(), "&a&lDeleted project '"+this.projectName+"'");
//			}
//			else
//			{
//				Message.sendMessage(e.getPlayer(), "&c&lCanceled. Type /cb for reopen menu");
//				return;
//			}
//			
//			Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
//				@Override
//				public void run() {
//					ManagerMenuMain.openProjectsMenu(cbamPlayer, worldName);
//				}
//			});
//		}
	}



	
}

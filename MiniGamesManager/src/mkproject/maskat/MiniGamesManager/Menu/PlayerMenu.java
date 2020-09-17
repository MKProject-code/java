package mkproject.maskat.MiniGamesManager.Menu;

import org.bukkit.Material;

import mkproject.maskat.MiniGamesManager.Game.GameModel;
import mkproject.maskat.MiniGamesManager.Game.GamesManager;
import mkproject.maskat.MiniGamesManager.Game.MiniGame;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuPage;
import mkproject.maskat.Papi.Menu.PapiMenuV2;

public class PlayerMenu implements PapiMenuV2 {

	private PapiMenuPage papiMenuPage;
//	private enum SlotOption {
//		COLOR_MIX
//	}
	
	@Override
	public PapiMenuPage getPapiMenuPage() {
		// TODO Auto-generated method stub
		return papiMenuPage;
	}
	
	public PlayerMenu(PapiMenu backMenu) {
		this.papiMenuPage = new PapiMenuPage(this, 3, "Minigry");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, "");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, "");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, "");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, "");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, "");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, "");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, "");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, "");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, "");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, "");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, "");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, "");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, "");
		if(backMenu != null)
			this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.BOOK, "&7Wróć do strony głównej", backMenu);
		else
			this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, "");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, "");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, "");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, "");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, "");
		this.refreshPapiMenuPage();
	}

	@Override
	public boolean refreshPapiMenuPage() {

		for(MiniGame miniGame : MiniGame.values()) {
			GameModel gameModel = GamesManager.getLobbyGame(miniGame);
			if(gameModel == null)
				this.papiMenuPage.setItem(miniGame.getMenuSlot(), miniGame.getMenuIcon(), "&a&l"+miniGame.name()+"\n&cWszystkie mapy w trakcie gry :(\n&7&oKliknij aby sprawdzić dostępność", miniGame);
			else
				this.papiMenuPage.setItem(miniGame.getMenuSlot(), miniGame.getMenuIcon(), "&a&l"+miniGame.name()+"\n&6W poczekalni: "+gameModel.getPlayers().size()+"/"+gameModel.getMaxPlayers()+"\n&7&oKliknij aby dołączyć do poczekalni", miniGame);
		}
		return true;
	}
	
	public void refreshPapiMenuItem(MiniGame miniGame) {
		GameModel gameModel = GamesManager.getLobbyGame(miniGame);
		if(gameModel == null)
			this.papiMenuPage.setItemLore(miniGame.getMenuSlot(), "&cWszystkie mapy w trakcie gry :(\n&7&oKliknij aby sprawdzić dostępność");
		else
			this.papiMenuPage.setItemLore(miniGame.getMenuSlot(), "&6W poczekalni: "+gameModel.getPlayers().size()+"/"+gameModel.getMaxPlayers()+"\n&7&oKliknij aby dołączyć do poczekalni");
	}

	@Override
	public void onMenuClick(PapiMenuClickEvent e) {
		if(e.getSlotStoreObject() == null)
			return;
		
		if(e.getSlotStoreObject() instanceof PapiMenu) {
			PapiMenu papiMenu = (PapiMenu)e.getSlotStoreObject();
			papiMenu.openPapiMenuPage(e.getPlayer());
		}
		else if(e.getSlotStoreObject() instanceof MiniGame) {
				GameModel gameModel = GamesManager.getLobbyGame((MiniGame) e.getSlotStoreObject());
				if(gameModel == null) {
					e.getPapiMenuPage().setItemLore(e.getSlot(), "&cWszystkie mapy w trakcie gry :(\n&7&oKliknij aby sprawdzić dostępność");
					return;
				}
				
				if(gameModel.joinPlayer(e.getPlayer()))
				{
					if(gameModel.isGameStarted())
					{
						GameModel gameModelNew = GamesManager.getLobbyGame((MiniGame) e.getSlotStoreObject());
						if(gameModelNew == null)
							e.getPapiMenuPage().setItemLore(e.getSlot(), "&cWszystkie mapy w trakcie gry :(\n&7&oKliknij aby sprawdzić dostępność");
						else
							e.getPapiMenuPage().setItemLore(e.getSlot(), "&6W poczekalni: 0/"+gameModelNew.getMaxPlayers()+"\n&7&oKliknij aby dołączyć do poczekalni");
					}
					else
						e.getPapiMenuPage().setItemLore(e.getSlot(), "&6W poczekalni: "+gameModel.getPlayers().size()+"/"+gameModel.getMaxPlayers()+"\n&7&oKliknij aby dołączyć do poczekalni");
				}
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
		// TODO Auto-generated method stub
		
	}



}

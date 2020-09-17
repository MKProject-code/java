package me.maskat.MenuHelpManager.PlayerMenu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuPage;

public class EventsMenu implements PapiMenu {

	private PapiMenuPage papiMenuPage;

	@Override
	public PapiMenuPage getPapiMenuPage() {
		return papiMenuPage;
	}
	
	public void initOpenMenu(Player player, PapiMenu backMenu) {
		this.initPapiMenu(player, 1, backMenu);
		this.openPapiMenuPage(player);
	}
	
	@Override
	public void initPapiMenu(Player player, int page, PapiMenu backMenu) {
		this.papiMenuPage = new PapiMenuPage(this, 5, " * * * * * Eventy Skyidea * * * * *");
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN5, Material.BOOK, "&7Wróć do strony głównej", backMenu);
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
//		this.papiMenuPage.setItemsList(InventorySlot.ROW2_COLUMN1, InventorySlot.ROW3_COLUMN2, 1,
//				new PapiMenuItem(Material.ACACIA_BOAT, "title", SlotOption.BACK)
//				);
		
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN1, Material.PAPER, "&f&lCzas zagłady - codziennie o 20:00\n&7Walcz z falą wrogich mobów na /spawn\n&7Na wydarzeniu nie stracisz EQ ani EXPa gdy zginiesz\n&7Dodatkowo zbroja oraz miecze się nie niszczą");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN2, Material.PAPER, "&f&lCzas hojności\n&7Szukaj przedmiotów w skrzyniach na /spawn\n&7&oEvent organizowany przez Administrację");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN3, Material.PAPER, "&f&lParkour\n&7Wykaż się zwinnością skacząc po blokach\n&7&oEvent organizowany przez Administrację");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN4, Material.PAPER, "&f&lKruche liście\n&7Utrzymaj się jak najdłużej na znikających liściach\n&7&oEvent organizowany przez Administrację");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN5, Material.PAPER, "&f&lPojedynek snajperski\n&7Wykaż się celnością w trafianiu do celu\n&7&oEvent organizowany przez Administrację");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN6, Material.PAPER, "&f&lLabirynt\n&7Znajdź wyjście z wielkiego labiryntu\n&7&oEvent organizowany przez Administrację");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN7, Material.PAPER, "&f&lQuiz\n&7Odpowiadaj poprawnie na pytania, aby przeżyć\n&7&oEvent organizowany przez Administrację");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN8, Material.PAPER, "&f&lSumo\n&7Zepchnij wszystkich przeciwników z platformy\n&7&oEvent organizowany przez Administrację");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN9, Material.PAPER, "&f&lŚliski wyścig\n&7Ścigaj się łódkami po lodzie\n&7&oEvent organizowany przez Administrację");
	}

	@Override
	public void onMenuClick(PapiMenuClickEvent e) {
		if(e.getSlotStoreObject() == null)
			return;
		
		if(e.getSlotStoreObject() instanceof PapiMenu)
		{
			PapiMenu papiMenu = (PapiMenu)e.getSlotStoreObject();
			papiMenu.openPapiMenuPage(e.getPlayer());
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
		
	}
}

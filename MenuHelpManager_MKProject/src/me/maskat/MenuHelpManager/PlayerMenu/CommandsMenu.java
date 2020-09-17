package me.maskat.MenuHelpManager.PlayerMenu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuPage;

public class CommandsMenu implements PapiMenu {
	
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
		this.papiMenuPage = new PapiMenuPage(this, 5, "* * * * * Komendy Skyidea * * * * *");
		
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
		
//		papiMenuPage.setItemsList(InventorySlot.ROW2_COLUMN1, InventorySlot.ROW3_COLUMN2, 1,
//				new PapiMenuItem(Material.ACACIA_BOAT, "title", SlotOption.BACK)
//				);
		
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN1, Material.PAPER, "&f&l/spawn\n&7Darmowa teleportacja na spawn");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN2, Material.PAPER, "&f&l/survival\n&7Darmowa teleportacja do ostatniej\n&7lokalizacji na mapie Survial");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN3, Material.PAPER, "&f&l/warp\n&7Darmowa teleportacja do miejsc specjalnych\n&7(minigry, eventy itp)");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN4, Material.PAPER, "&f&l/bed\n&7Teleportacja do łóżka\n&7(koszt 5 EXP Level)\n&oDarmowa tylko dla VIP");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN5, Material.PAPER, "&f&l/tpa\n&7Teleportacja do gracza\n&7(koszt 5 EXP Level)\n&oDarmowa tylko dla VIP");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN6, Material.PAPER, "&f&l/trade\n&7Handluj z innymi graczami");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN7, Material.PAPER, "&f&l/skin\n&7Zmień swój skin");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN8, Material.PAPER, "&f&l/party\n&7Stwórz party ze znajomymi");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN9, Material.PAPER, "&f&l/promo\n&7Promuj serwer i otrzymuj benefity");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN1, Material.PAPER, "&f&l/pw\n&7Wysyłaj prywatne wiadomości");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN2, Material.PAPER, "&f&l/exp\n&7Sprawdź ile posiadasz EXPa");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN3, Material.PAPER, "&f&l/rtp\n&7Teleportacja do losowej lokalizacji\n&oDostępne tylko dla VIP");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN4, Material.PAPER, "&f&l/sethome &fi &f&l/home\n&7Ustawianie domu i teleportacja do niego\n&oDostępne tylko dla VIP");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.PAPER, "&f&l/enderchest\n&7Dostęp do twojej skrzyni kresu\n&7w każdym miejscu!\n&oDostępne tylko dla VIP");
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

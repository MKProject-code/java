package me.maskat.MenuHelpManager.PlayerMenu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.maskat.ArenaManager.ArenaAPI.ArenaAPI;
import me.maskat.ShopManager.ShopAPI;
import me.maskat.compasspoint.CompassAPI;
import me.maskat.wolfsecurity.api.WolfSecurityApi;
import mkproject.maskat.MiniGamesManager.MiniGamesAPI;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuPage;

public class MainMenu implements PapiMenu {
	private enum SlotOption {
		EXIT,
		
		COMMANDS,
		ADDONS,
		EVENTS,
		ARENES,
		SHOP,
		COMPASS,
		WOLF,
		VIP,
		MINIGAMES;
	}
	
	private PapiMenuPage papiMenuPage;
	
	@Override
	public PapiMenuPage getPapiMenuPage() {
		return this.papiMenuPage;
	}
	
	public void initOpenMenu(Player player) {
		this.initPapiMenu(player, 1, null);
		this.openPapiMenuPage(player);
	}
	
	@Override
	public void initPapiMenu(Player player, int page, PapiMenu backMenu) {

		this.papiMenuPage = new PapiMenuPage(this, 6, "* * * * * * Menu Skyidea * * * * * *");
		
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
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.BARRIER, "&cWyjdź z menu", SlotOption.EXIT);
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN2, Material.COMPARATOR,
				"&c&lKomendy"
				+"\n&7Sprawdz listę komend dostępnych na serwerze"
				, SlotOption.COMMANDS);
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN3, Material.EMERALD,
				"&a&lDodatki"
				+"\n&7Sprawdz nasze dodatki i urozmaicenia,"
				+ "\n&7które umilą grę!"
				, SlotOption.ADDONS);
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN4, Material.FIREWORK_ROCKET,
				"&d&lEventy"
				+"\n&7Sprawdz jakie przygotowaliśmy"
				+ "\n&7wydarzenia i atrakcje!"
				, SlotOption.EVENTS);
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN6, Material.DIAMOND_SWORD,
				"&3&lAreny &7&l/arena"
				+"\n&7Sprawdź nasze areny,"
				+ "\n&7w których możesz zaprezentować"
				+ "\n&7swoje umiejętności!"
				, SlotOption.ARENES);
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN7, Material.BLAZE_POWDER,
				"&3&lMinigry &7&l/minigry"
				+"\n&7Sprawdź nasze minigry,"
				+ "\n&7w których możesz wygrywać"
				+ "\n&7dodatkowe SkyPunkty!"
				, SlotOption.MINIGAMES);
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN8, Material.CHEST,
				"&6&lSklep &7&l/sklep"
				+"\n&7Odwiedź sklep, w którym możesz"
				+ "\n&7zakupić unikalne przedmioty!"
				, SlotOption.SHOP);
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN3, Material.COMPASS,
				"&9&lKompas &7&l/kompas"
				+"\n&7Zarządzaj swoim kompasem,"
				+ "\n&7aby ustawić nawigację!"
				, SlotOption.COMPASS);
		this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN5, Material.DIAMOND,
				"&b&lVIP+"
				+"\n&7Chcesz się wyróżniać z tłumu?"
				+"\n&7Sprawdź jakie przywileje posiada VIP+!"
				, SlotOption.VIP);
		this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN7, Material.BONE,
				"&f&lWilk &7&l/wilk"
				+"\n&7Zarządzaj swoim wilkiem, który"
				+ "\n&7będzie chronił twojego terytorium!"
				, SlotOption.WOLF);
		this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
	}

	@Override
	public void onMenuClick(PapiMenuClickEvent e) {
		if(e.getSlotStoreObject() == null)
			return;
		
		if(e.getSlotStoreObject() == SlotOption.EXIT) {
			e.getPlayer().closeInventory();
		}
		else if(e.getSlotStoreObject() == SlotOption.COMMANDS) {
			new CommandsMenu().initOpenMenu(e.getPlayer(), this);
		}
		else if(e.getSlotStoreObject() == SlotOption.ADDONS) {
			new AddonsMenu().initOpenMenu(e.getPlayer(), this);
		}
		else if(e.getSlotStoreObject() == SlotOption.EVENTS) {
			new EventsMenu().initOpenMenu(e.getPlayer(), this);
		}
		else if(e.getSlotStoreObject() == SlotOption.SHOP) {
			ShopAPI.openShopMenu(e.getPlayer(), this);
		}
		else if(e.getSlotStoreObject() == SlotOption.ARENES) {
			ArenaAPI.openArenesPlayerMenu(e.getPlayer(), this);
		}
		else if(e.getSlotStoreObject() == SlotOption.MINIGAMES) {
			MiniGamesAPI.openMenu(e.getPlayer(), this);
		}
		else if(e.getSlotStoreObject() == SlotOption.COMPASS) {
			CompassAPI.openCompassMenu(e.getPlayer(), this);
		}
		else if(e.getSlotStoreObject() == SlotOption.WOLF) {
			WolfSecurityApi.openWolfMenu(e.getPlayer(), this);
		}
		else if(e.getSlotStoreObject() == SlotOption.VIP) {
			new VipMenu().initOpenMenu(e.getPlayer(), this);
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
		
	}


}

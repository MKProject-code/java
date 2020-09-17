package me.maskat.MenuHelpManager.PlayerMenu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.PapiHeadBase64;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuPage;

public class VipMenu implements PapiMenu {
	
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
		this.papiMenuPage = new PapiMenuPage(this, 5, "* * * * * * V.I.P. Skyidea * * * * * *");
		
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
		if(backMenu != null)
			this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN5, Material.BOOK, "&7Wróć do strony głównej", backMenu);
		else
			this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
//		papiMenuPage.setItemsList(InventorySlot.ROW2_COLUMN1, InventorySlot.ROW3_COLUMN2, 1,
//				new PapiMenuItem(Material.ACACIA_BOAT, "title", SlotOption.BACK)
//				);
		
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN1, Material.GREEN_DYE, true, "&2Pisanie kolorem na czacie");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN2, Material.JUNGLE_SIGN, true, "&3Pisanie kolorem na znakach");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN3, Material.RED_BED, true, "&c&lDarmowe&c teleportacje &l/bed /tpa");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN4, Material.CAMPFIRE, true, "&eMożliwość ustawienia &l/sethome\n&ez darmową teleportacją &l/home");
//		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN5, Material.DARK_OAK_DOOR, "&6Rezerwacja slota\n&7Będziesz mógł wejść na serwer nawet gdy będzie pełny!");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN5, Material.CHORUS_FRUIT, true, "&dLosowa teleportacja &l/rtp\n&7Możesz teleportować się za darmo\n&7w losowe miejsce trzy razy\n&7w ciągu doby!");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN6, Material.BARRIER, true, "&4Na terytorium wilka tzw. \"&lcuboid&4\"\n&7Inni gracze nie będą mogli nic zniszczyć\n&7na zajętym terytorium!");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN7, Material.DIAMOND, true, "&9Niższe ceny w &l/sklep\n&7&oW przypadku wypicia mikstury na darmowego VIPa\n&7&otylko promocje dnia mają niższe ceny");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN8, Material.NAME_TAG, true, "&bSpecjalna ranga &l&9VIP+&b wyświetlana przy nicku\n&7&oW przypadku wypicia mikstury na darmowego VIPa\n&7&owidnieje ranga VIP");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN9, Material.EXPERIENCE_BOTTLE, true, "&a&lEXP&a zostaje po każdej śmierci\n&7Twój ciężko zdobyty EXP zostanie w całości\n&7zachowany zawsze gdy zginiesz!");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN1, Material.NETHERITE_CHESTPLATE, true, "&5Zbroja nigdy się nie niszczy\n&7Twoja zbroja będzie niezniszczalna!\n&7&oNie obowiązuje w przypadku wypicia mikstury na darmowego VIPa");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN2, Material.CHEST, true, "&6&lEQ&6 zostanie zachowane zawsze gdy zginiesz\n&7Twój ekwipunek w całości zostanie zachowany gdy zginiesz!\n&7&oW przypadku wypicia mikstury na darmowego VIPa\n&7&otylko 50% szans na zachowanie EQ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN3, Material.SNOWBALL, true, "&f&l3x&f więcej SkyPunktów\n&7Będziesz otrzymywał &l3x&7 więcej SkyPunktów za aktywność!\n&7&oNie obowiązuje w przypadku wypicia mikstury na darmowego VIPa");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN4, Material.ENDER_CHEST, true, "&6Dostęp do skrzyni kresu &l/enderchest\n&7Będziesz mógł otworzyć swoją\n&7skrzynie kresu w każdej chwili!");
//		this.papiMenuPage.setItemHeadAsync(InventorySlot.ROW3_COLUMN4, Material.AIR, PapiHeadBase64.Villager, "&6&lHandel&6 z wieśniakami na targu na /spawn\n&7Będziesz mógł handlować z wieśniakami,\n&7którzy są na targu na /spawn!\n&7Oni mają mega niesamowite przedmioty!\n&7&oNie obowiązuje w przypadku wypicia mikstury na darmowego VIPa");
		
		this.papiMenuPage.setItemHeadAsync(InventorySlot.ROW4_COLUMN5, Material.AIR, PapiHeadBase64.Discord, "&5Zainteresowany?\n&9Skontaktuj się z nami przez Discord: &bMasKAT#5316");
		
		//this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMNx, Material.BONE, "&fMniejszy koszt zwiększania poziomu wilka");
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

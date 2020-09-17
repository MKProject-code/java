package me.maskat.compasspoint.inventories;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.alessiodp.parties.api.interfaces.PartyPlayer;

import me.maskat.compasspoint.Message;
import me.maskat.compasspoint.PartiesApi;
import me.maskat.compasspoint.compass.Compass;
import me.maskat.compasspoint.enums.NavLocation;
import me.maskat.compasspoint.models.Model;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.PapiHeadBase64;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuPage;

public class MainMenuV2 implements PapiMenu {

	PapiMenuPage papiMenuPage;
	@Override
	public PapiMenuPage getPapiMenuPage() {
		return papiMenuPage;
	}
	
	private enum SlotOption {
		NAV_BED, NAV_WOLF, NAV_PLAYERSPAWN
	}
	
	private boolean isBackMenu = true;
	
	public void initOpenMenu(Player player, PapiMenu backMenu) {
		this.isBackMenu = (backMenu == null) ? false : true;
		this.initPapiMenu(player, 1, backMenu);
		this.openPapiMenuPage(player);
	}
	
	@Override
	public void initPapiMenu(Player player, int page, PapiMenu backMenu) {
		papiMenuPage = new PapiMenuPage(this, 6, "* * * * * Kompas Skyidea * * * * *");
		
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW1_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		//papiMenuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItemHeadAsync(InventorySlot.ROW1_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, PapiHeadBase64.Orange_Question_Mark,
				"&l&6*** POMOC ***\n"
				+"&e> Trzymając w ręku kompas:\n"
				+"&f- Kliknij lewym przyciskiem myszy&7, aby utworzyć chwilowy \"wskaźnik\",\n"
				+"&7który mogą zobaczyć tylko członkowie twojego party\n"
				+"&f- Kliknij prawym przyciskiem myszy&7, aby otworzyć to menu\n\n"
				+"&7&oKompas działa tylko w świecie Survival"
			);
		
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		if(backMenu != null)
			papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.BOOK, "&7Wróć do strony głównej", backMenu);
		else
			papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		papiMenuPage.setItem(InventorySlot.ROW6_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");

		this.initButtons(player);
	}

	private void initButtons(Player player) {
		papiMenuPage.setItemHeadAsync(InventorySlot.ROW2_COLUMN2, Material.REDSTONE_TORCH, PapiHeadBase64.GREEN_Information, "&2Aktualne ustawienie kompasu:\n"+Model.Player(player).getNavLocationInfo());
		
		if(Model.Player(player).existBedSpawn())
			papiMenuPage.setItem(InventorySlot.ROW2_COLUMN4, Material.RED_BED, "&6Nawiguj do &ełóżka", SlotOption.NAV_BED);
		else
			papiMenuPage.setItem(InventorySlot.ROW2_COLUMN4, Material.RED_BED, "&6Nawiguj do &ełóżka\n&c&oNiedostępne - brak łóżka");
		
		 if(Model.Player(player).existWolfAPI())
				papiMenuPage.setItem(InventorySlot.ROW2_COLUMN5, Material.BONE, "&6Nawiguj do &ewilka", SlotOption.NAV_WOLF);
			else
				papiMenuPage.setItem(InventorySlot.ROW2_COLUMN5, Material.BONE, "&6Nawiguj do &ewilka\n&c&oNiedostępne - brak wilka");
		 
		 if(Model.Player(player).existPlayerSpawnPoint())
			 papiMenuPage.setItem(InventorySlot.ROW2_COLUMN6, Material.CAMPFIRE, "&6Nawiguj do &epunktu startowego", SlotOption.NAV_PLAYERSPAWN);
		 else
			 papiMenuPage.setItem(InventorySlot.ROW2_COLUMN6, Material.CAMPFIRE, "&6Nawiguj do &epunktu startowego\n&c&oNiedostępne - brak punktu startowego");

		String partyName = PartiesApi.getPartyName(player);
		if (partyName.isEmpty()) {
			papiMenuPage.setItem(InventorySlot.ROW2_COLUMN7, Material.PAINTING, "&6Nawiguj do przyjaciela z party\n&7Stwórz party aby móc ustawiać nawigację do przyjaciół");
		    return;
		}
		
		Set<PartyPlayer> partyPlayers = PartiesApi.getPartyOnlineMembers(partyName);
		if(partyPlayers.size() > 1)
		{
			papiMenuPage.setItem(InventorySlot.ROW2_COLUMN7, Material.PAINTING, "&6Nawiguj do przyjaciela z party\n&7Wybierz gracza z poniższej listy");
			int i = InventorySlot.ROW4_COLUMN1.getValue();
			for(PartyPlayer partyPlayer : partyPlayers)
			{
				Player targetPlayer = Bukkit.getServer().getPlayer(partyPlayer.getPlayerUUID());
				if(targetPlayer.equals(player))
					continue;
				
				if(targetPlayer.getWorld() == Papi.Server.getSurvivalWorld())
					papiMenuPage.setItemHeadAsync(InventorySlot.valueOf(i),targetPlayer, "&6Nawiguj do &e" + targetPlayer.getName(), targetPlayer);
				else
					papiMenuPage.setItemHeadAsync(InventorySlot.valueOf(i),targetPlayer, "&6Nawiguj do &e" + targetPlayer.getName() + "\n&7&oGracz jest aktualnie poza światem Survival", targetPlayer);
				i++;
			}
		}
		else
			papiMenuPage.setItem(InventorySlot.ROW2_COLUMN7, Material.PAINTING, "&6Nawiguj do przyjaciela z party\n&c&oBrak graczy online w twoim party");
	}
	
	@Override
	public void onMenuClick(PapiMenuClickEvent e) {
		if(e.getSlotStoreObject() == null)
			return;
		
		Player player = e.getPlayer();
		
		if(e.getSlotStoreObject() instanceof SlotOption)
		{
			SlotOption slotOption = (SlotOption)e.getSlotStoreObject();
			if(slotOption == SlotOption.NAV_BED)
			{
	        	if(!Model.Player(player).existBedSpawn()) {
	        		e.getPapiMenuPage().setItemNameAndLore(e.getSlot(), "&6Nawiguj do &ełóżka\n&c&oNiedostępne - brak łóżka");
	        		return;
	        	}
	        	if(!isBackMenu)
	        		player.closeInventory();
	        	Model.Player(player).setNavLocation(NavLocation.BED);
	        	Message.sendActionBar(player, "&aUstawiono nawigację. &aKompas poprowadzi cię do twojego &ełóżka");
	        	Compass.updateCompassLocation(player);
	        	this.initButtons(player);
	        	e.getPapiMenuPage().addItemLoreLine(e.getSlot(), "&aUstawiono nawigację");
	        	return;
			}
			else if(slotOption == SlotOption.NAV_WOLF)
			{
	        	if(!Model.Player(player).existWolfAPI()) {
	        		e.getPapiMenuPage().setItemNameAndLore(e.getSlot(), "&6Nawiguj do &ewilka\n&c&oNiedostępne - brak wilka");
	        		return;
	        	}
	        	if(!isBackMenu)
	        		player.closeInventory();
	        	Model.Player(player).setNavLocation(NavLocation.WOLF);
	        	Message.sendActionBar(player, "&aUstawiono nawigację. &aKompas poprowadzi cię do twojego &ewilka");
	        	Compass.updateCompassLocation(player);
	        	this.initButtons(player);
	        	e.getPapiMenuPage().addItemLoreLine(e.getSlot(), "&aUstawiono nawigację");
	        	return;
			}
			else if(slotOption == SlotOption.NAV_PLAYERSPAWN)
			{
	        	if(!isBackMenu)
	        		player.closeInventory();
				Model.Player(player).setNavLocation(NavLocation.PLAYERSPAWN);
				Message.sendActionBar(player, "&aUstawiono nawigację. &aKompas poprowadzi cię do twojego &epunktu startowego");
				Compass.updateCompassLocation(player);
				this.initButtons(player);
				e.getPapiMenuPage().addItemLoreLine(e.getSlot(), "&aUstawiono nawigację");
				return;
			}
		}
		else if(e.getSlotStoreObject() instanceof Player)
		{
			Player targetPlayer = (Player)e.getSlotStoreObject();
			if(!targetPlayer.isOnline()) {
				papiMenuPage.setItemNameAndLore(e.getSlot(), "&6Nawiguj do &e" + targetPlayer.getName() + "\n&7&cGracz opuścił gre");
				return;
			}
//			if(targetPlayer.getWorld() != Papi.Server.getSurvivalWorld()) {
//				papiMenuPage.setItemNameAndLore(e.getSlot(), "&6Nawiguj do &e" + targetPlayer.getName() + "\n&7&oGracz jest aktualnie poza światem Survival");
//			}
        	if(!isBackMenu)
        		player.closeInventory();
        	Model.Player(player).setNavLocation(NavLocation.PLAYER, targetPlayer);
        	Message.sendActionBar(player, "&aUstawiono nawigację. &aKompas poprowadzi cię do gracza &e" + targetPlayer.getName());
        	Compass.updateCompassLocation(player);
        	this.initButtons(player);
        	e.getPapiMenuPage().addItemLoreLine(e.getSlot(), "&aUstawiono nawigację");
        	return;
		}
		else if(e.getSlotStoreObject() instanceof PapiMenu)
		{
			PapiMenu papiMenu = (PapiMenu)e.getSlotStoreObject();
			papiMenu.openPapiMenuPage(e.getPlayer());
			return;
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

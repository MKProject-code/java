package me.maskat.MenuHelpManager.PlayerMenu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import me.maskat.wolfsecurity.api.WolfRegion;
import me.maskat.wolfsecurity.api.WolfSecurityApi;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.PapiHeadBase64;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuPage;

public class WolfMenu_old implements PapiMenu {

	private PapiMenuPage papiMenuPage;

	private enum SlotOption {
		GIVE_WOLF,
		ME_TP_TO_WOLF,
		ME_TP_TO_REGION,
		WOLF_TP_TO_ME,
		WOLF_TP_TO_REGION,
	}
	
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
		this.papiMenuPage = new PapiMenuPage(this, 5, " * * * * * * Wilk Skyidea * * * * * *");
		
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
		
		this.papiMenuPage.setItemHeadAsync(InventorySlot.ROW1_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, PapiHeadBase64.Orange_Question_Mark,
				"&6*** POMOC ***\n"
						+"&eWilk chroni twojego terytorium.\n"
						+"&eMożesz zarzącać wilkiem klikając w niego kością.");
		
		if(WolfSecurityApi.existWolf(player))
			this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.BONE, "&aPosiadasz wilka");
		else if(player.getWorld() == Papi.Server.getSurvivalWorld())
			this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.BONE, "&eOtrzymaj wilka za darmo", SlotOption.GIVE_WOLF);
		else
			this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.BONE, "&eOtrzymaj wilka za darmo\n&7&oMożesz to zrobić tylko w świecie Survival");
		
		if(WolfSecurityApi.existWolf(player))
		{
			if(player.getLevel() >= 5)
				this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN2, Material.ENDER_PEARL, "&3Teleportuj się do wilka\n&7Koszt 5 EXP Level", SlotOption.ME_TP_TO_WOLF);
			else
				this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN2, Material.ENDER_PEARL, "&3Teleportuj się do wilka\n&7Koszt 5 EXP Level\n&c&oNiewystarczający EXP Level");
		}
		else
			this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN2, Material.ENDER_PEARL, "&3Teleportuj się do wilka\n&7Koszt 5 EXP Level\n&c&oNiedostępne - brak wilka");
		
		
		if(WolfSecurityApi.isClaimedRegion(player))
		{
			if(player.getLevel() >= 5)
				this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN3, Material.ENDER_PEARL, "&3Teleportuj się na terytorium wilka\n&7Koszt 5 EXP Level", SlotOption.ME_TP_TO_REGION);
			else
				this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN3, Material.ENDER_PEARL, "&3Teleportuj się na terytorium wilka\n&7Koszt 5 EXP Level\n&c&oNiewystarczający EXP Level");
		}
		else
			this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN3, Material.ENDER_PEARL, "&3Teleportuj się na terytorium wilka\n&7Koszt 5 EXP Level\n&c&oNiedostępne - brak terytorium");
		
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN7, Material.ENDER_PEARL, "&3Teleportuj wilka do siebie\n&7Koszt 5 EXP Level", SlotOption.WOLF_TP_TO_ME);
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN8, Material.ENDER_PEARL, "&3Teleportuj wilka na jego terytorium\n&7Koszt 5 EXP Level", SlotOption.WOLF_TP_TO_REGION);
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
		else
		{
			SlotOption slotOption = (SlotOption)e.getSlotStoreObject();
			if(slotOption == SlotOption.GIVE_WOLF) {
				if(!WolfSecurityApi.existWolf(e.getPlayer()))
				{
					int result = WolfSecurityApi.giveWolf(e.getPlayer());
					if(result == -1)
						e.getPapiMenuPage().setItemNameAndLore(e.getSlot(), "&aPosiadasz już wilka");
					if(result == 0)
						e.getPapiMenuPage().setItemNameAndLore(e.getSlot(), "&eOtrzymaj wilka za darmo\n&7&oMożesz to zrobić tylko w świecie Survival");
					if(result == 1)
						e.getPapiMenuPage().setItemNameAndLore(e.getSlot(), "&aOtrzmałeś swojego wilka!\n&7Możesz zarzącać wilkiem klikając w niego kością");
				}
				else
				{
					e.getPapiMenuPage().setItemNameAndLore(e.getSlot(), "&aPosiadasz wilka");
				}
			}
			if(slotOption == SlotOption.ME_TP_TO_WOLF) {
				if(WolfSecurityApi.existWolf(e.getPlayer()))
				{
					Wolf wolf = WolfSecurityApi.getWolfEntity(e.getPlayer());
					if(wolf.isValid())
					{
						if(e.getPlayer().getLevel() >= 5) {
							e.getPlayer().closeInventory();
							Papi.Model.getPlayer(e.getPlayer()).teleportTimerExpLvl(wolf.getLocation(), 5);
						}
						else
							e.getPapiMenuPage().setItemLoreLine(e.getSlot(), 2, "&c&oNiewystarczający EXP Level");
					}
					else
						e.getPapiMenuPage().setItemLoreLine(e.getSlot(), 2, "&cNie możemy odnaleźć twojego wilka :(");
				}
				else
				{
					e.getPapiMenuPage().setItemLoreLine(e.getSlot(), 2, "&c&oNiedostępne - brak wilka");
				}
			}
			if(slotOption == SlotOption.ME_TP_TO_REGION) {
				if(WolfSecurityApi.isClaimedRegion(e.getPlayer()))
				{
					WolfRegion wolfRegion = WolfSecurityApi.getClaimedRegion(e.getPlayer());
					if(wolfRegion != null)
					{
						if(e.getPlayer().getLevel() >= 5) {
							e.getPlayer().closeInventory();
							Papi.Model.getPlayer(e.getPlayer()).teleportTimerExpLvl(wolfRegion.getCenterLocation(), 5);
						}
						else
							e.getPapiMenuPage().setItemLoreLine(e.getSlot(), 2, "&c&oNiewystarczający EXP Level");
					}
					else
						e.getPapiMenuPage().setItemLoreLine(e.getSlot(), 2, "&c&oNiedostępne - brak terytorium");
				}
				else
				{
					e.getPapiMenuPage().setItemLoreLine(e.getSlot(), 2, "&c&oNiedostępne - brak terytorium");
				}
			}
			else if(slotOption == SlotOption.WOLF_TP_TO_ME) {
				//TODO: only execute in player inside region !!!
//				if(WolfSecurityApi.existWolf(e.getPlayer()))
//				{
//					Wolf wolf = WolfSecurityApi.getWolfEntity(e.getPlayer());
//					if(wolf.isValid())
//					{
//						e.getPlayer().closeInventory();
//						wolf.teleport(e.getPlayer().getLocation());
//						Message.sendMessage(e.getPlayer(), "&a&oTwój wilk został przeteleportowany do Ciebie.");
//					}
//					else
//						e.getPapiMenuPage().setItemLoreLine(e.getSlot(), 2, "&cNie możemy odnaleźć twojego wilka :(");
//				}
//				else
//				{
//					e.getPapiMenuPage().setItemLoreLine(e.getSlot(), 2, "&c&oNiedostępne - brak wilka");
//				}
			}
			else if(slotOption == SlotOption.WOLF_TP_TO_REGION) {
				//TODO: only execute in player inside region !!!
			}
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
		
	}
}

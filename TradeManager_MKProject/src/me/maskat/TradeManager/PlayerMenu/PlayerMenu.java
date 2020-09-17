package me.maskat.TradeManager.PlayerMenu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.maskat.TradeManager.Plugin;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuPage;

public class PlayerMenu implements PapiMenu {
	private enum SlotOption {
		EXIT,
		
		ACCEPT,
		DECIDE,
	}
	
	private Player player;
	
	private PapiMenuPage papiMenuPage;
	
	@Override
	public PapiMenuPage getPapiMenuPage() {
		return this.papiMenuPage;
	}
	
	protected PlayerMenu(TradePlayer tradePlayer) {
		this.player = tradePlayer.getPlayer();
		this.initPapiMenu(this.player, 1, null);
		this.papiMenuPage.openMenu(this.player);
		
//		this.repeatUpdaterTask();
	}
	
//	private void repeatUpdaterTask() {
//		Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
//			@Override
//			public void run() {
//				if(Models.existTradePlayer(player))
//				{
//					updateStatus();
//					repeatUpdaterTask();
//				}
//			}
//		}, 20L);
//	}
	
	@Override
	public void initPapiMenu(Player player, int page, PapiMenu backMenu) {

		this.papiMenuPage = new PapiMenuPage(this, 6, "        Ty             Inny gracz", true);
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW4_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW5_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN1, Material.GREEN_WOOL, "&aKliknij tutaj, aby zaakceptować transakcje", SlotOption.ACCEPT);
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN2, Material.RED_WOOL, "&cKliknij tutaj, aby odmówić transakcji", SlotOption.DECIDE);
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN3, Material.BARRIER, "&4Anuluj transakcje", SlotOption.EXIT);
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.GRAY_DYE, "&7Oczekiwanie na status...");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_DYE, "&7Oczekiwanie na status...");
		
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
	}
	

//	protected void updateStatus() {
//		TradePlayer tradePlayer = Models.getTradePlayer(this.player);
//		TradePlayer tradePlayerDestination = Models.getTradePlayer(tradePlayer.getDestinationPlayer());
//		player.updateInventory(); 
//		tradePlayer.getDestinationPlayer().updateInventory(); 
//		
//		if(tradePlayer.isAccepted())
//			this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.LIME_DYE, " ");
//		else if(tradePlayer.isDecided())
//			this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.MAGENTA_DYE, " ");
//		else
//			this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.GRAY_DYE, " ");
//		
//		if(tradePlayerDestination.isAccepted())
//			this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.LIME_DYE, " ");
//		else if(tradePlayerDestination.isDecided())
//			this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.MAGENTA_DYE, " ");
//		else
//			this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_DYE, " ");
//
//	}
	
	protected void setAccepted(boolean thisPlayer, boolean accepted) {
		if(accepted)
		{
			if(thisPlayer)
				this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.LIME_DYE, "&aZaakceptowano!");
			else
				this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.LIME_DYE, "&aZaakceptowano!");
		}
		else
		{
			if(thisPlayer)
				this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.GRAY_DYE, "&7Oczekiwanie na status...");
			else
				this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.GRAY_DYE, "&7Oczekiwanie na status...");
		}
	}
	

	public void setDecided(boolean thisPlayer) {
		if(thisPlayer)
			this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN4, Material.MAGENTA_DYE, "&cOdrzucono!");
		else
			this.papiMenuPage.setItem(InventorySlot.ROW6_COLUMN6, Material.MAGENTA_DYE, "&cOdrzucono!");
	}

	@Override
	public void onMenuClick(PapiMenuClickEvent e) {
//		if(e.getSlotStoreObject() == null)
//			return;
		
		TradePlayer tradePlayer = Models.getTradePlayer(e.getPlayer());
		if(e.getSlotStoreObject() == SlotOption.EXIT) {
			
			tradePlayer.endTradeMenu();
			return;
		}
		
		if(e.getSlotStoreObject() == SlotOption.ACCEPT) {
			
			tradePlayer.setAccepted(true);
			return;
		}
		
		if(e.getSlotStoreObject() == SlotOption.DECIDE) {
			
			tradePlayer.setDecide();
			return;
		}
		
		int clickedSlotId = e.getSlot().getValue();
		
		if((clickedSlotId >= InventorySlot.ROW1_COLUMN1.getValue() && clickedSlotId <= InventorySlot.ROW1_COLUMN4.getValue())
				|| (clickedSlotId >= InventorySlot.ROW2_COLUMN1.getValue() && clickedSlotId <= InventorySlot.ROW2_COLUMN4.getValue())
				|| (clickedSlotId >= InventorySlot.ROW3_COLUMN1.getValue() && clickedSlotId <= InventorySlot.ROW3_COLUMN4.getValue())
				|| (clickedSlotId >= InventorySlot.ROW4_COLUMN1.getValue() && clickedSlotId <= InventorySlot.ROW4_COLUMN4.getValue())
				|| (clickedSlotId >= InventorySlot.ROW5_COLUMN1.getValue() && clickedSlotId <= InventorySlot.ROW5_COLUMN4.getValue()))
		{
			TradePlayer tradePlayerDestination = Models.getTradePlayer(tradePlayer.getDestinationPlayer());
			tradePlayer.setAccepted(false);
			tradePlayerDestination.setAccepted(false);
			
			Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					try {
						Models.getTradePlayer(tradePlayer.getDestinationPlayer()).getTradePlayerMenu().getPapiMenuPage().setItem(InventorySlot.valueOf(clickedSlotId+5), e.getMenuInventory().getItem(clickedSlotId));
					} catch(Exception ex) {}
					player.updateInventory(); 
					tradePlayer.getDestinationPlayer().updateInventory(); 
				}
			}, 1L);
			
			e.setCancelled(false);
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
		if(!Models.existTradePlayer(e.getPlayer()))
			return;
		TradePlayer tradePlayer = Models.getTradePlayer(e.getPlayer());
		tradePlayer.closeTradeMenu();
	}


	
	
}

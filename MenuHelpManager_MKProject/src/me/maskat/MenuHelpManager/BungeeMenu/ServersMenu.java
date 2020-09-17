package me.maskat.MenuHelpManager.BungeeMenu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.maskat.MenuHelpManager.Plugin;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.PapiHeadBase64;
import mkproject.maskat.Papi.Bungee.BungeeServer;
import mkproject.maskat.Papi.Menu.InventorySlot;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Menu.PapiMenuClickEvent;
import mkproject.maskat.Papi.Menu.PapiMenuCloseEvent;
import mkproject.maskat.Papi.Menu.PapiMenuPage;
import mkproject.maskat.Papi.Utils.Message;

public class ServersMenu implements PapiMenu {
	private enum SlotOption {
		SURVIVAL, ARENES, SKYBLOCK, DISCORD
	}
	
	private PapiMenuPage papiMenuPage;
	private boolean allowClose = false;
	
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

		this.papiMenuPage = new PapiMenuPage(this, 3, "* * * * * Serwery Skyidea * * * * *");
		
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN2, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN4, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN6, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN8, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW3_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
		
		
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN1, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN2, Material.CAMPFIRE, true,
				"&a&lPrawdziwy &6&lSURVIVAL &7&l1.16.1"
				+"\n&7&oKliknij aby dołączyć do serwera"
				, SlotOption.SURVIVAL);
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN3, Material.GRAY_STAINED_GLASS_PANE, " ");

		this.papiMenuPage.setItemHeadAsync(InventorySlot.ROW2_COLUMN4, Material.AIR, PapiHeadBase64.Sky,
				"&b&lSkyBlock &7&l1.16.1"
				+"\n&7&oKliknij aby dołączyć do serwera"
				, SlotOption.SKYBLOCK);
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN5, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN6, Material.NETHERITE_SWORD, true,
				"&6&lARENY &7&l1.16.1"
				+"\n&7&oKliknij aby dołączyć do serwera"
				, SlotOption.ARENES);
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN7, Material.GRAY_STAINED_GLASS_PANE, " ");
		this.papiMenuPage.setItemHeadAsync(InventorySlot.ROW2_COLUMN8, Material.AIR, PapiHeadBase64.Discord,
				"&3&lDiscord"
				+"\n&7https://discord.gg/MbCEphx", SlotOption.DISCORD);
		
		this.papiMenuPage.setItem(InventorySlot.ROW2_COLUMN9, Material.GRAY_STAINED_GLASS_PANE, " ");
	}

	@Override
	public void onMenuClick(PapiMenuClickEvent e) {
		if(e.getSlotStoreObject() == null)
			return;
		
		if(e.getSlotStoreObject() == SlotOption.SURVIVAL) {
			allowClose = true;
			e.closeMenuForThisPlayer();
			
			Message.sendMessage(e.getPlayer(), "&7&oŁączenie z serwerem: Survival");
			Papi.Bungee.connectPlayer(e.getPlayer(), BungeeServer.SURVIVAL);
				
			Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					allowClose = false;
					papiMenuPage.openMenu(e.getPlayer());
				}
			}, 60L);
		}
		else if(e.getSlotStoreObject() == SlotOption.SKYBLOCK) {
			allowClose = true;
			e.closeMenuForThisPlayer();
			
			Message.sendMessage(e.getPlayer(), "&7&oŁączenie z serwerem: SkyBlock");
			Papi.Bungee.connectPlayer(e.getPlayer(), BungeeServer.SKYBLOCK);
			
			Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					allowClose = false;
					papiMenuPage.openMenu(e.getPlayer());
				}
			}, 60L);
		}
		else if(e.getSlotStoreObject() == SlotOption.ARENES) {
			allowClose = true;
			e.closeMenuForThisPlayer();
			
			Message.sendMessage(e.getPlayer(), "&7&oŁączenie z serwerem: Areny");
			Papi.Bungee.connectPlayer(e.getPlayer(), BungeeServer.ARENES);
			
			Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					allowClose = false;
					papiMenuPage.openMenu(e.getPlayer());
				}
			}, 60L);
		}
	}

	@Override
	public void onMenuClose(PapiMenuCloseEvent e) {
		if(allowClose)
			return;
		
		Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				papiMenuPage.openMenu(e.getPlayer());
			}
		}, 1L);
	}


}

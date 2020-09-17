package me.maskat.compasspoint.inventories;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.alessiodp.parties.api.interfaces.PartyPlayer;

import me.maskat.compasspoint.Message;
import me.maskat.compasspoint.PartiesApi;
import me.maskat.compasspoint.Plugin;
import me.maskat.compasspoint.compass.Compass;
import me.maskat.compasspoint.enums.NavLocation;
import me.maskat.compasspoint.models.Model;

public class MenuMain_old implements InventoryHolder {

    private Inventory inv;
    private Map<Integer, Player> targetPlayerSlotList = new HashMap<Integer, Player>();
    private Player getTargetPlayerSlot(final int slot) { return targetPlayerSlotList.get(slot); }
    private Player setTargetPlayerSlot(final int slot, final Player player) { return targetPlayerSlotList.put(slot, player); }
    
    private static class InvSlot {
    	private static final int navigateHelp() { return 7; }
    	private static final int navigateInfo() { return 4; }
    	private static final int navigateToBed() { return 12; }
    	private static final int navigateToWolf() { return 13; }
    	private static final int navigateToPlayer() { return 14; }
    	private static final int[] navigateToPlayerSlots() { return new int[] {
    			20,21,22,23,24,
    			29,30,31,32,33,
    			38,39,40,41,42,
    			47,48,49,50,51
    			}; }
    }
    

	@Override
    public Inventory getInventory() { return inv; }
    
	public Inventory createGui(final Player player) {
        inv = Bukkit.createInventory(this, 54, Message.getColorMessage("Menu nawigacji kompasu"));
        initializeItems(player);
        return inv;
	}
	
	private void initializeItems(final Player player)
    {
        //inv.setItem(invSlotChoose1,WolfMenu.createGuiItem(Material.NAME_TAG, Message.getColorMessageLang("inventory.wolfmenu.changename"), "�7Nazwa ta wy�wietla si�", "�7zawsze nad jego cia�em"));
        inv.setItem(0,InventoryMenu_old.createGuiItem(Material.COMPASS));
        inv.setItem(8,InventoryMenu_old.createGuiItem(Material.COMPASS));
        inv.setItem(9,InventoryMenu_old.createGuiItem(Material.COMPASS));
        inv.setItem(17,InventoryMenu_old.createGuiItem(Material.COMPASS));
        inv.setItem(18,InventoryMenu_old.createGuiItem(Material.COMPASS));
        inv.setItem(26,InventoryMenu_old.createGuiItem(Material.COMPASS));
        inv.setItem(27,InventoryMenu_old.createGuiItem(Material.COMPASS));
        inv.setItem(35,InventoryMenu_old.createGuiItem(Material.COMPASS));
        inv.setItem(36,InventoryMenu_old.createGuiItem(Material.COMPASS));
        inv.setItem(44,InventoryMenu_old.createGuiItem(Material.COMPASS));
        inv.setItem(45,InventoryMenu_old.createGuiItem(Material.COMPASS));
        inv.setItem(53,InventoryMenu_old.createGuiItem(Material.COMPASS));
        
        inv.setItem(InvSlot.navigateHelp(),InventoryMenu_old.createGuiItemParseMsg(Material.BOOK, Message.getColorMessage("&l*** POMOC ***\nTrzymając w ręku kompas możesz kliknąć\nlewym przyciskiem myszy w oddalony punkt,\naby utworzyć chwilowy \"wskaźnik\", który\nmogą zobaczyć tylko członkowie twojego party.")));
        
        inv.setItem(InvSlot.navigateInfo(),InventoryMenu_old.createGuiItemParseMsg(Material.REDSTONE_TORCH, Message.getColorMessage("&2Aktualne ustawienie kompasu:\n"+Model.Player(player).getNavLocationInfo())));
        
        if(Model.Player(player).existBedSpawn())
        	inv.setItem(InvSlot.navigateToBed(),InventoryMenu_old.createGuiItemParseMsg(Material.RED_BED, Message.getColorMessage("&6Nawiguj do &ełóżka")));
        else
        	inv.setItem(InvSlot.navigateToBed(),InventoryMenu_old.createGuiItemParseMsg(Material.RED_BED, Message.getColorMessage("&6Nawiguj do &ełóżka\n&c&oNiedostępne - brak łóżka")));
        
        if(Model.Player(player).existWolfAPI())
        	inv.setItem(InvSlot.navigateToWolf(),InventoryMenu_old.createGuiItemParseMsg(Material.BONE, Message.getColorMessage("&6Nawiguj do &ewilka")));
        else
        	inv.setItem(InvSlot.navigateToWolf(),InventoryMenu_old.createGuiItemParseMsg(Material.BONE, Message.getColorMessage("&6Nawiguj do &ewilka\n&c&oNiedostępne - brak wilka")));
    
		String partyName = PartiesApi.getPartyName(player);
		if (partyName.isEmpty()) {
			inv.setItem(InvSlot.navigateToPlayer(),InventoryMenu_old.createGuiItemParseMsg(Material.PAINTING, Message.getColorMessage("&6Nawiguj do przyjaciela z party\n&7Stwórz party aby móc ustawiać nawigację do przyjaciół")));
		    return;
		}
		
//		Set<PartyPlayer> partyPlayers = PartiesApi.getPartyOnlineMembers(partyName);
//		if(partyPlayers.size() > 1)
//		{
//			inv.setItem(InvSlot.navigateToPlayer(),InventoryMenu.createGuiItemParseMsg(Material.PAINTING, Message.getColorMessage("&6Nawiguj do przyjaciela z party\n&7Wybierz gracza z poni�szej listy")));
//			int i = 0;
//			for(PartyPlayer partyPlayer : partyPlayers)
//			{
//				Player targetPlayer = Bukkit.getServer().getPlayer(partyPlayer.getPlayerUUID());
//				if(targetPlayer.equals(player))
//					continue;
//				this.setTargetPlayerSlot(InvSlot.navigateToPlayerSlots()[i], targetPlayer);
//				if(targetPlayer.getWorld().getName().equals("world"))
//					inv.setItem(InvSlot.navigateToPlayerSlots()[i],InventoryMenu.createGuiItemHeadParseMsg(targetPlayer, Message.getColorMessage("&6Nawiguj do &e" + targetPlayer.getName())));
//				else
//					inv.setItem(InvSlot.navigateToPlayerSlots()[i],InventoryMenu.createGuiItemHeadParseMsg(targetPlayer, Message.getColorMessage("&6Nawiguj do &e" + targetPlayer.getName() + "\n&7&oGracz jest aktualnie poza �wiatem Survival")));
//				i++;
//			}
//		}
//		else
//			inv.setItem(InvSlot.navigateToPlayer(),InventoryMenu.createGuiItemParseMsg(Material.PAINTING, Message.getColorMessage("&6Nawiguj do przyjaciela z party\n&c&oBrak graczy online w twoim party")));
//    
    
    
		Set<PartyPlayer> partyPlayers = PartiesApi.getPartyOnlineMembers(partyName);
		if(partyPlayers.size() > 1)
		{
			inv.setItem(InvSlot.navigateToPlayer(),InventoryMenu_old.createGuiItemParseMsg(Material.PAINTING, Message.getColorMessage("&6Nawiguj do przyjaciela z party\n&7Wybierz gracza z poniższej listy")));
			int i = 0;
			for(PartyPlayer partyPlayer : partyPlayers)
			{
				Player targetPlayer = Bukkit.getServer().getPlayer(partyPlayer.getPlayerUUID());
				if(targetPlayer.equals(player))
					continue;
				this.setTargetPlayerSlot(InvSlot.navigateToPlayerSlots()[i], targetPlayer);
				if(targetPlayer.getWorld().getName().equals("world"))
					inv.setItem(InvSlot.navigateToPlayerSlots()[i],InventoryMenu_old.createGuiItemParseMsg(Material.PLAYER_HEAD, Message.getColorMessage("&6Nawiguj do &e" + targetPlayer.getName())));
				else
					inv.setItem(InvSlot.navigateToPlayerSlots()[i],InventoryMenu_old.createGuiItemParseMsg(Material.PLAYER_HEAD, Message.getColorMessage("&6Nawiguj do &e" + targetPlayer.getName() + "\n&7&oGracz jest aktualnie poza światem Survival")));
				i++;
			}
			updateHeadItemsAsync();
		}
		else
			inv.setItem(InvSlot.navigateToPlayer(),InventoryMenu_old.createGuiItemParseMsg(Material.PAINTING, Message.getColorMessage("&6Nawiguj do przyjaciela z party\n&c&oBrak graczy online w twoim party")));
    }
	
	private void updateHeadItemsAsync() {
		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				for (Map.Entry<Integer, Player> entry : targetPlayerSlotList.entrySet()) {
					Player targetPlayer = entry.getValue();
					if(targetPlayer.getWorld().getName().equals("world"))
						inv.setItem(entry.getKey(),InventoryMenu_old.createGuiItemHeadParseMsg(targetPlayer, Message.getColorMessage("&6Nawiguj do &e" + targetPlayer.getName())));
					else
						inv.setItem(entry.getKey(),InventoryMenu_old.createGuiItemHeadParseMsg(targetPlayer, Message.getColorMessage("&6Nawiguj do &e" + targetPlayer.getName() + "\n&7&oGracz jest aktualnie poza światem Survival")));
				}
			}
		});
	}

	public void openInventory(final Player player) {
		player.openInventory(inv);
	}
	
	public void onInventoryClick(final InventoryClickEvent e, final Player player) {
		e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        int clickedSlot = e.getRawSlot();
        if(clickedSlot == InvSlot.navigateToBed()) {
        	if(!Model.Player(player).existBedSpawn())
        		return;
        	player.closeInventory();
        	Model.Player(player).setNavLocation(NavLocation.BED);
        	Message.sendActionBar(player, "&aUstawiono nawigację. &aKompas poprowadzi cię do twojego &ełóżka");
        	Compass.updateCompassLocation(player);
        	return;
        }
        if(clickedSlot == InvSlot.navigateToWolf()) {
        	if(!Model.Player(player).existWolfAPI())
        		return;
        	player.closeInventory();
        	Model.Player(player).setNavLocation(NavLocation.WOLF);
        	//Model.Player(player).updateWolfEntityAPI();
        	Message.sendActionBar(player, "&aUstawiono nawigację. &aKompas poprowadzi cię do twojego &ewilka");
        	Compass.updateCompassLocation(player);
        	return;
        }
        
        if(targetPlayerSlotList.containsKey(clickedSlot))
        {
        	player.closeInventory();
        	Player targetPlayer = this.getTargetPlayerSlot(clickedSlot);
        	Model.Player(player).setNavLocation(NavLocation.PLAYER, targetPlayer);
        	Message.sendActionBar(player, "&aUstawiono nawigację. &aKompas poprowadzi cię do gracza &e" + targetPlayer.getName());
        	Compass.updateCompassLocation(player);
        	return;
        }
	}
}

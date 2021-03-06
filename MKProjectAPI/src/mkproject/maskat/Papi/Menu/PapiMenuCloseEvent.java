package mkproject.maskat.Papi.Menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import mkproject.maskat.Papi.Model.PapiModel;
import mkproject.maskat.Papi.Model.PapiPlayer;

public class PapiMenuCloseEvent {
	private PapiMenu papiMenuExecutor;
	private PapiMenuPage menuInvPage;
    private Player player;
    private Map<InventorySlot, Object> slotStoreObjectMap = new HashMap<>();

    public PapiMenuCloseEvent(PapiMenu papiMenuExecutor, final PapiMenuPage menuInvPage, final Player player, final Map<InventorySlot, Object> slotStoreObjectMap) {
    	this.papiMenuExecutor = papiMenuExecutor;
    	this.menuInvPage = menuInvPage;
        this.player = player;
        this.slotStoreObjectMap = slotStoreObjectMap;
        
        this.menuInvPage.removePlayerOpenedMenu(player);
    }

    public PapiMenuPage getPapiMenuPage() {
        return menuInvPage;
    }
    
    public PapiMenu getPapiMenuExecutor() {
    	return papiMenuExecutor;
    }

    public boolean existSlotStoreObject(InventorySlot inventorySlot) {
    	return slotStoreObjectMap.containsKey(inventorySlot);
    }
    
    public Object getSlotStoreObject(InventorySlot inventorySlot) {
    	return slotStoreObjectMap.get(inventorySlot);
    }
    
    public Inventory getMenuInventory() {
    	return menuInvPage.getInventory();
    }
    
    public Player getPlayer() {
    	return player;
    }
    
    public PapiPlayer getPapiPlayer() {
    	return PapiModel.getPlayer(player);
    }
    
    public void closeInventory() {
    	player.closeInventory();
    }
}

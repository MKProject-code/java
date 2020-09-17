package mkproject.maskat.Papi.Menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import mkproject.maskat.Papi.Model.PapiModel;
import mkproject.maskat.Papi.Model.PapiPlayer;

public class PapiMenuClickEvent {
	private PapiMenu papiMenuExecutor;
	private PapiMenuPage menuInvPage;
    private InventorySlot papiInventorySlot;
    private Player player;
    private Map<InventorySlot, Object> slotStoreObjectMap = new HashMap<>();
    
    private boolean canceled;
	private ItemStack currentItem;
	private ItemStack cursor;

    public PapiMenuClickEvent(PapiMenu papiMenuExecutor, final PapiMenuPage menuInvPage, final InventorySlot papiInventorySlot, final Player player, final Map<InventorySlot, Object> slotStoreObjectMap, InventoryClickEvent e) {
    	this.papiMenuExecutor = papiMenuExecutor;
    	this.menuInvPage = menuInvPage;
    	this.papiInventorySlot = papiInventorySlot;
        this.player = player;
        this.slotStoreObjectMap = slotStoreObjectMap;
        
        this.currentItem = e.getCurrentItem();
        this.cursor = e.getCursor();
        this.canceled = true;
    }
    
    public ItemStack getCurrentItem() {
    	return this.currentItem;
    }
    
    public ItemStack getCursor() {
    	return this.cursor;
    }
    
    public void setCancelled(boolean canceled) {
    	this.canceled = canceled;
    }
    public boolean isCancelled() {
    	return this.canceled;
    }

    public PapiMenuPage getPapiMenuPage() {
        return menuInvPage;
    }
    
    public PapiMenu getPapiMenuExecutor() {
    	return papiMenuExecutor;
    }
    
    public InventorySlot getSlot() {
    	return papiInventorySlot;
    }
    
    public boolean existSlotStoreObject() {
    	return existSlotStoreObject(papiInventorySlot);
    }
    public boolean existSlotStoreObject(InventorySlot inventorySlot) {
    	return slotStoreObjectMap.containsKey(inventorySlot);
    }
    
    public Object getSlotStoreObject() {
    	return getSlotStoreObject(papiInventorySlot);
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
    
    public void closeMenuForThisPlayer() {
    	menuInvPage.closeMenu(player);
    }
//    public void closeInventoryForPlayer() {
//    	player.closeInventory();
//    }
//    
//    public void closeInventoryForAll() {
//    	player.closeInventory();
//    }
}

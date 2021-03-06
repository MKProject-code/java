package mkproject.maskat.Papi.MenuInventory;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import mkproject.maskat.Papi.Model.PapiModel;
import mkproject.maskat.Papi.Model.PapiPlayer;

public final class PapiMenuInventoryClickEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private JavaPlugin pluginExecutor;
	private MenuPage menuInvPage;
    private InventorySlot papiInventorySlot;
    private Player player;
    private boolean cancelled;
    private Map<InventorySlot, Object> slotStoreObjectMap = new HashMap<>();
    private Object pageUniqueId;

    public PapiMenuInventoryClickEvent(JavaPlugin pluginExecutor, final MenuPage menuInvPage, final InventorySlot papiInventorySlot, final Player player, final Map<InventorySlot, Object> slotStoreObjectMap, Object pageUniqueId) {
    	this.pluginExecutor = pluginExecutor;
    	this.menuInvPage = menuInvPage;
    	this.papiInventorySlot = papiInventorySlot;
        this.player = player;
        this.slotStoreObjectMap = slotStoreObjectMap;
        this.pageUniqueId = pageUniqueId;
        this.cancelled = false;
    }

    public MenuPage getMenuPage() {
        return menuInvPage;
    }
    
    public JavaPlugin getPluginExecutor() {
    	return pluginExecutor;
    }
    
    public InventorySlot getSlot() {
    	return papiInventorySlot;
    }
    
    public Object getUniquePageId() {
    	return pageUniqueId;
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
    
    public void closeInventory() {
    	player.closeInventory();
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(final boolean cancel) {
        cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

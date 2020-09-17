package mkproject.maskat.Papi.Model;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class PapiListenChatEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private JavaPlugin pluginExecutor;
    private Object eventUniqueId;
    private Object storeObject;
    private Player player;
    private String message;
    private boolean cancelled;

    public PapiListenChatEvent(JavaPlugin pluginExecutor, Player player, String message, Object eventUniqueId, Object storeObject) {
    	this.pluginExecutor = pluginExecutor;
        this.player = player;
        this.eventUniqueId = eventUniqueId;
        this.storeObject = storeObject;
        this.message = message;
        this.cancelled = false;
    }

    public JavaPlugin getPluginExecutor() {
    	return pluginExecutor;
    }
    
    public Object getStoreObject() {
    	return storeObject;
    }
    public String getMessage() {
    	return message;
    }
    
    public boolean isNoMessage() {
    	return message == null ? true : false;
    }
    
    public Object getEventUniqueId() {
    	return eventUniqueId;
    }
    
    public Player getPlayer() {
    	return player;
    }
    
    public PapiPlayer getPapiPlayer() {
    	return PapiModel.getPlayer(player);
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

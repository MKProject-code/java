package mkproject.maskat.Papi.Model;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public final class PapiListenerChatEvent implements Cancellable {// extends Event
//	private static final HandlerList handlers = new HandlerList();
	private PapiChatListener pluginExecutor;
    private Player player;
	private PapiPlayer papiPlayer;
    private Object storeObject;
    private String message;
    private boolean cancelled;

    public PapiListenerChatEvent(PapiChatListener listenChatPapiChatListener, Player player, String message, Object storeObject) {
    	this.pluginExecutor = listenChatPapiChatListener;
        this.player = player;
        this.papiPlayer = PapiModel.getPlayer(player);
        this.storeObject = storeObject;
        this.message = message;
        this.cancelled = false;
	}

	public PapiChatListener getPluginExecutor() {
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
    
    public Player getPlayer() {
    	return player;
    }
    
    public PapiPlayer getPapiPlayer() {
    	return this.papiPlayer;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(final boolean cancel) {
        cancelled = cancel;
    }
//
//    public HandlerList getHandlers() {
//        return handlers;
//    }
//
//    public static HandlerList getHandlerList() {
//        return handlers;
//    }
}

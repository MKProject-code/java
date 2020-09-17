package mkproject.maskat.Papi.Model;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerInteractEvent;

public class PapiListenerInteractEvent implements Cancellable {
	private PapiInteractListener pluginExecutor;
    private Player player;
	private PapiPlayer papiPlayer;
    private Object storeObject;
	private PlayerInteractEvent event;
    private boolean cancelled;

    public PapiListenerInteractEvent(PapiInteractListener listenInteractPapiInteractListener, Player player, PlayerInteractEvent event, Object storeObject) {
    	this.pluginExecutor = listenInteractPapiInteractListener;
        this.player = player;
        this.papiPlayer = PapiModel.getPlayer(player);
        this.storeObject = storeObject;
        this.event = event;
        this.cancelled = false;
	}

	public PapiInteractListener getPluginExecutor() {
    	return pluginExecutor;
    }
    
    public Object getStoreObject() {
    	return storeObject;
    }
    
    public PlayerInteractEvent getEvent() {
    	return this.event;
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
}

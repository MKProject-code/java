package mkproject.maskat.Papi.Model;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PapiPlayerLoginEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
    private PapiPlayer papiPlayer;
    private boolean firstLogin;
    private Player player;
    private boolean cancelled;

    public PapiPlayerLoginEvent(final PapiPlayer papiplayer, final Player p, final boolean firstLogin) {
    	papiPlayer = papiplayer;
        player = p;
        this.firstLogin = firstLogin;
    }
    public PapiPlayer getPapiPlayer() {
    	return papiPlayer;
    }
    public Player getPlayer() {
        return player;
    }
    public boolean isFirstLogin() {
    	return this.firstLogin;
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

package me.maskat.ArenaManager.ArenaPlugin;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.maskat.ArenaManager.Models.ModelArena;

public class PlayerDamageInArenaEvent {

	private Player player;
	private ModelArena modelArena;
	
	private DamageCause eventCause;
	private double eventDamage;
	private double eventFinalDamage;
	
	private boolean cancelled;
	
	public PlayerDamageInArenaEvent(Player player, ModelArena modelArena, EntityDamageEvent e) {
		this.player = player;
		this.modelArena = modelArena;
		
		this.eventCause = e.getCause();
		this.eventDamage = e.getDamage();
		this.eventFinalDamage = e.getFinalDamage();
		
		this.cancelled = false;
	}

	public ArpArena getArena() {
		return this.modelArena.getArenaPluginInstanceArpArena();
	}
	
	public ArpPlayer getPlayer() {
		return this.modelArena.getArenaPluginInstanceArpPlayer(player);
	}

	public ArpTeam getTeam(String teamType) {
		return this.modelArena.getArenaPluginInstanceArpTeam(teamType);
	}
	public Collection<ArpTeam> getTeams() {
		return this.modelArena.getArenaPluginInstanceArpTeams();
	}
	
	public DamageCause getCause() {
		return this.eventCause;
	}
	public double getDamage() {
		return this.eventDamage;
	}
	public double getFinalDamage() {
		return this.eventFinalDamage;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	public boolean isCancelled() {
		return this.cancelled;
	}
}

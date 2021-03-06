package me.maskat.ArenaManager.ArenaPlugin;

import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.maskat.ArenaManager.Plugin;
import me.maskat.ArenaManager.Models.ModelArena;

public class PlayerDeathInArenaEvent {

	private Player player;
	private ModelArena modelArena;

	private boolean keepInventory = false;
	private boolean keepLevel = false;
	private int droppedExp;
	private List<ItemStack> drops;

	public PlayerDeathInArenaEvent(Player player, ModelArena modelArena, PlayerDeathEvent e) {
		this.player = player;
		this.modelArena = modelArena;
		
		this.droppedExp = e.getDroppedExp();
		this.drops = e.getDrops();
	}
	public ArpArena getArena() {
		return modelArena.getArenaPluginInstanceArpArena();
	}
	
	public ArpTeam getTeam(String teamType) {
		return modelArena.getArenaPluginInstanceArpTeam(teamType);
	}
	public Collection<ArpTeam> getTeams() {
		return modelArena.getArenaPluginInstanceArpTeams();
	}
	
	public ArpPlayer getPlayer() {
		return this.modelArena.getArenaPluginInstanceArpPlayer(player);
	}
	
	public boolean getKeepInventory() {
		return this.keepInventory;
	}

	public boolean getKeepLevel() {
		return this.keepLevel;
	}
	
	public void setKeepInventory(boolean keepInventory) {
		this.keepInventory = keepInventory;
	}
	
	public void setKeepLevel(boolean keepLevel) {
		this.keepLevel = keepLevel;
	}

	public int getDroppedExp() {
		return droppedExp;
	}
	
	public void setDroppedExp(int droppedExp) {
		this.droppedExp = droppedExp;
	}

	public List<ItemStack> getDrops() {
		return drops;
	}
	
	public void playerLeaveArena() {
		Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				//player.spigot().respawn(); // implemented in getPlayer().leaveArena();
				getPlayer().leaveArena();
			}
		}, 1L);
	}
}

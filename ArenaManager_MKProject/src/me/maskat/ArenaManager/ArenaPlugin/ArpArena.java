package me.maskat.ArenaManager.ArenaPlugin;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.maskat.ArenaManager.Models.ModelArena;

public class ArpArena {

	private ModelArena modelArena;
	
	public ArpArena(ModelArena modelArena) {
		this.modelArena = modelArena;
	}
	
	public String getName() {
		return modelArena.getName();
	}
	
	public World getWorld() {
		return modelArena.getWorld();
	}
	
	public boolean isGameEnded() {
		return modelArena.isGameEnded();
	}
	
	public ArpPlayer getPlayer(Player player) {
		return modelArena.getArenaPluginInstanceArpPlayer(player);
	}
	
	public Collection<ArpPlayer> getPlayers() {
		return modelArena.getArenaPluginInstanceArpPlayers();
	}
	
	public Collection<ArpPlayer> getPlayersOnline() {
		return modelArena.getArenaPluginInstanceArpPlayersOnline();
	}
	
	public Collection<ArpTeam> getTeams() {
		return modelArena.getArenaPluginInstanceArpTeams();
	}	
	
	public ArpTeam getTeam(String teamType) {
		return modelArena.getArenaPluginInstanceArpTeam(teamType);
	}
	
	public Collection<ArpTeam> getTeamsInsideArena() {
		Collection<ArpTeam> teamsInsideArena = new ArrayList<>();
		for(ArpTeam arpTeam : modelArena.getArenaPluginInstanceArpTeams()) {
			if(arpTeam.getPlayersInsideArena().size() > 0)
				teamsInsideArena.add(arpTeam);
		}
		return teamsInsideArena;
	}
	
	public void teleportPlayers(Location location) {
		for(ArpTeam arpTeam : this.getTeams())
			arpTeam.teleportPlayers(location);
	}
	
	public void teleportPlayersToRandomLocations() {
		for(ArpTeam arpTeam : this.getTeams())
			arpTeam.teleportPlayersToRandomLocations();
	}
	
	public void setPlayersFreeze(boolean freeze) {
		for(ArpTeam arpTeam : this.getTeams())
			arpTeam.setPlayersFreeze(freeze);
	}
	
	public void setPlayersGamemode(GameMode gamemode) {
		for(ArpTeam arpTeam : this.getTeams())
			arpTeam.setPlayersGamemode(gamemode);
	}
	
	public void setPlayersGodmode(boolean godmode) {
		for(ArpTeam arpTeam : this.getTeams())
			arpTeam.setPlayersGodmode(godmode);
	}
	
	public void setPlayersClearInventory() {
		for(ArpTeam arpTeam : this.getTeams())
			arpTeam.setPlayersClearInventory();
	}
	public void setPlayersInventoryContent(ItemStack... contentsItems) {
		for(ArpTeam arpTeam : this.getTeams())
			arpTeam.setPlayersInventoryContent(contentsItems);
	}
	public void setPlayersInventoryArmor(ItemStack... armorContentsItems) {
		for(ArpTeam arpTeam : this.getTeams())
			arpTeam.setPlayersInventoryArmor(armorContentsItems);
	}
	public void removePlayersPotionEffect() {
		for(ArpTeam arpTeam : this.getTeams())
			arpTeam.removePlayersPotionEffect();
	}
	public void setPlayersParameters(int health, int food, int saturation, int absorption) {
		for(ArpTeam arpTeam : this.getTeams())
			arpTeam.setPlayersParameters(health, food, saturation, absorption);
	}
	public void setPlayersAllowTeleport(boolean allowTeleport) {
		for(ArpTeam arpTeam : this.getTeams())
			arpTeam.setPlayersAllowTeleport(allowTeleport);
	}
	public void playersPlaySound(Sound sound) {
		for(ArpTeam arpTeam : this.getTeams())
			arpTeam.playersPlaySound(sound);
	}
}

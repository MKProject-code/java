package me.maskat.ArenaManager.ArenaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.maskat.ArenaManager.ArenaManager.Manager;
import me.maskat.ArenaManager.Models.ModelArena;
import me.maskat.ArenaManager.Models.ModelArenaPlayer;
import me.maskat.ArenaManager.Models.ModelArenaSpawn;
import me.maskat.ArenaManager.Models.ModelArenaTeam;
import mkproject.maskat.Papi.Utils.Message;

public class ArpTeam {
	
	ModelArena modelArena;
	ModelArenaTeam modelArenaTeam;
	
	List<Location> spawnsLocation = new ArrayList<>();
	Collection<ArpPlayer> arpPlayers = new ArrayList<>();
	
	
	public ArpTeam(ModelArenaTeam modelArenaTeam, ModelArena modelArena) {
		this.modelArena = modelArena;
		this.modelArenaTeam = modelArenaTeam;
		
		for(ModelArenaSpawn modelArenaSpawn : modelArenaTeam.getSpawnsMap().values()) {
			spawnsLocation.add(modelArenaSpawn.getLocation());
		}
			
		for(ModelArenaPlayer modelPlayer : modelArenaTeam.getPlayersRegistered())
		{
			arpPlayers.add(new ArpPlayer(modelPlayer, this, modelArena));
		}
	}

	public Location getLocation(int index) {
		return spawnsLocation.get(index);
	}
	
	public Collection<ArpPlayer> getPlayers() {
		return arpPlayers;
	}
	
	public Collection<ArpPlayer> getPlayersOnline() {
		Collection<ArpPlayer> arpPlayersOnline = new ArrayList<>();
		for(ArpPlayer arpPlayer : this.arpPlayers) {
			if(arpPlayer.getPlayer().isOnline() && !arpPlayer.isQuitedServer())
				arpPlayersOnline.add(arpPlayer);
		}
		return arpPlayersOnline;
	}
	
	public Collection<ArpPlayer> getPlayersInsideArena() {
		Collection<ArpPlayer> arpPlayersOnlineInsideArena = new ArrayList<>();
		for(ArpPlayer arpPlayer : this.arpPlayers) {
			if(arpPlayer.getPlayer().isOnline() && !arpPlayer.isQuitedServer() && arpPlayer.isInsideArena())
				arpPlayersOnlineInsideArena.add(arpPlayer);
		}
		return arpPlayersOnlineInsideArena;
	}
	
	public String getTeamType() {
		return this.modelArenaTeam.getType();
	}
	
	public ArpRandomLocation getRandomLocation() {
		return new ArpRandomLocation(spawnsLocation);
	}
	
	public Location getOneRandomLocation() {
		ArpRandomLocation randLoc = new ArpRandomLocation(spawnsLocation);
		return randLoc.next();
	}
	
	public List<Location> getLocations() {
		return spawnsLocation;
	}
	
	public void setPlayersGamemode(GameMode gamemode) {
		for(ArpPlayer arpPlayer : this.getPlayersOnline())
			arpPlayer.getPlayer().setGameMode(gamemode);
	}
	
	public void teleportPlayers(Location location) {
		for(ArpPlayer arpPlayer : this.getPlayersOnline())
			arpPlayer.getPlayer().teleport(location);
	}
	
	public void teleportPlayersToRandomLocations() {
		ArpRandomLocation randomLocations = getRandomLocation();
		for(ArpPlayer arpPlayer : this.getPlayersOnline())
			arpPlayer.getPlayer().teleport(randomLocations.next());
	}
	
	public void setPlayersFreeze(boolean freeze) {
		for(ArpPlayer arpPlayer : this.getPlayersOnline())
			arpPlayer.setFreeze(freeze);
	}
	
	public void setPlayersGodmode(boolean godmode) {
		for(ArpPlayer arpPlayer : this.getPlayersOnline())
			arpPlayer.setGodmode(godmode);
	}
	
	public void setPlayersClearInventory() {
		for(ArpPlayer arpPlayer : this.getPlayersOnline())
		{
			arpPlayer.getPlayer().getInventory().clear();
			arpPlayer.getPlayer().updateInventory();
		}
	}
	public void setPlayersInventoryContent(ItemStack... contentsItems) {
		for(ArpPlayer arpPlayer : this.getPlayersOnline())
		{
			arpPlayer.getPlayer().getInventory().setContents(contentsItems);
			arpPlayer.getPlayer().updateInventory();
		}
	}
	public void setPlayersInventoryArmor(ItemStack... armorContentsItems) {
		for(ArpPlayer arpPlayer : this.getPlayersOnline())
		{
			arpPlayer.getPlayer().getInventory().setArmorContents(armorContentsItems);
			arpPlayer.getPlayer().updateInventory();
		}
	}
	
	public void removePlayersPotionEffect() {
		for(ArpPlayer arpPlayer : this.getPlayersOnline())
		{
			for(PotionEffect potionEffect : arpPlayer.getPlayer().getActivePotionEffects())
				arpPlayer.getPlayer().removePotionEffect(potionEffect.getType());
		}
	}

	public void setPlayersParameters(int health, int food, int saturation, int absorption) {
		for(ArpPlayer arpPlayer : this.getPlayersOnline())
		{
			arpPlayer.getPlayer().setHealth(health);
			arpPlayer.getPlayer().setFoodLevel(food);
			arpPlayer.getPlayer().setSaturation(saturation);
			arpPlayer.getPlayer().setAbsorptionAmount(absorption);
		}
	}
	
	public void setPlayersAllowTeleport(boolean allowTeleport) {
		for(ArpPlayer arpPlayer : this.getPlayersOnline())
			arpPlayer.setAllowTeleport(allowTeleport);
	}
	
	public void playersPlaySound(Sound sound) {
		for(ArpPlayer arpPlayer : this.getPlayersOnline())
			arpPlayer.playSound(sound);
	}
	
	public void doWinnerGame() {
		for(ArpPlayer arpPlayer : this.getPlayersOnline())
		{
			if(arpPlayer.isInsideArena())
				Message.sendTitle(arpPlayer.getPlayer(), null, "&a--- ZWYCIĘSTWO ---");
			else
				Message.sendMessage(arpPlayer.getPlayer(), "&a&oZWYCIĘSTWO !!! Twoja drużyna zwyciężyła!");
		}
		
		Manager.doEndGame(modelArena, this, null);
	}
}

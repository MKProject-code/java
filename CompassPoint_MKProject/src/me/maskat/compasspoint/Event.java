package me.maskat.compasspoint;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.alessiodp.parties.api.interfaces.PartyPlayer;

import me.maskat.compasspoint.inventories.MainMenuV2;
import me.maskat.compasspoint.models.Model;

public class Event implements Listener {
	
	@EventHandler
    public void onPlayerInteractEvent(final PlayerInteractEvent e) {
		if(e.getItem() == null)
			return;
		
		Player player = e.getPlayer();
		Location playerlocation = player.getLocation();
		
		if(((e.getAction() == Action.LEFT_CLICK_AIR || (player.getGameMode() == GameMode.CREATIVE && e.getAction() == Action.LEFT_CLICK_BLOCK)) && e.getItem().getType() == Material.COMPASS && e.getHand() == EquipmentSlot.HAND)
				|| (e.getAction() == Action.RIGHT_CLICK_AIR && e.getItem().getType() == Material.COMPASS && e.getHand() == EquipmentSlot.OFF_HAND))
		{
			if(!playerlocation.getWorld().getName().equals("world"))
			{
				if(!player.hasPermission("mkp.compasspoint.bypass.otherworld"))
				{
					Message.sendActionBar(player, "&cMożesz oznaczać punkty dla swojego party tylko w świecie Survival");
					return;
				}
			}
			else
			{
				if(playerlocation.getBlockY() <= 60)
				{
					Message.sendActionBar(player, "&cMożesz oznaczać punkty dla swojego party tylko na powierzchni");
					return;
				}
			}
			Block block = player.getTargetBlockExact(300);
			if(block == null)
				return;
			
			Location blocklocation = block.getLocation();
			if(blocklocation == null)
				return;
			
			String partyName = PartiesApi.getPartyName(player);
			if (partyName.isEmpty()) {
				Message.sendActionBar(player, "&eStwórz party ze znajomymi aby oznaczać im punkty kompasem");
				return;
			}
			
			boolean showFirstLowerParticle = true;
			if(playerlocation.distance(blocklocation) < 10)
				showFirstLowerParticle = false;
			
			Set<PartyPlayer> partyPlayers = PartiesApi.getPartyOnlineMembers(partyName);
			if(partyPlayers.size() > 0)
			{
				for(PartyPlayer partyPlayer : partyPlayers)
				{
					Player p = Bukkit.getServer().getPlayer(partyPlayer.getPlayerUUID());
					if(p.isOnline())
					{
						Function.spawnPoint(p, blocklocation, showFirstLowerParticle);
					}
				}
			}
		}
		else if((e.getAction() == Action.RIGHT_CLICK_AIR) && e.getItem().getType() == Material.COMPASS && e.getHand() == EquipmentSlot.HAND)
		{
			if(!playerlocation.getWorld().getName().equals("world"))
			{
				Message.sendActionBar(player, "&cMożesz używać kompasu tylko w świecie Survival");
				return;
			}
//			Model.Player(player).setInventoryMenu();
//			Model.Player(player).initInventoryMenu();
//			Model.Player(player).openMainMenu();
			new MainMenuV2().initOpenMenu(player, null);
		}
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
    	HumanEntity humanentity = e.getWhoClicked();
    	if(!(humanentity instanceof Player))
    		return;
    	
    	Player player = (Player)humanentity;
    	Model.Player(player).onInventoryClick(e);
	}
	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent e) {
    	HumanEntity humanentity = e.getPlayer();
    	if(!(humanentity instanceof Player))
    		return;
    	
    	Player player = (Player)humanentity;
    	if(Model.Players().containsKey(player))
    		Model.Player(player).removeInventoryMenu();
	}
	
	@EventHandler
	public void onBlockBreakEvent(final BlockBreakEvent e) {
		Player player = e.getPlayer();
		if(player.getGameMode() == GameMode.CREATIVE && player.getInventory().getItemInMainHand().getType() == Material.COMPASS)
			e.setCancelled(true);
	}
	
	@EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
		Model.Players.addPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		Model.Players.removePlayer(e.getPlayer());
	}
}

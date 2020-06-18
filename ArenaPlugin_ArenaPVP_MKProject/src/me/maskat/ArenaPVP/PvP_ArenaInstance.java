package me.maskat.ArenaPVP;

import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import me.maskat.ArenaManager.ArenaPlugin.AbortArenaGameEvent;
import me.maskat.ArenaManager.ArenaPlugin.ArenaInstance;
import me.maskat.ArenaManager.ArenaPlugin.ArenaPluginManager;
import me.maskat.ArenaManager.ArenaPlugin.ArpTeam;
import me.maskat.ArenaManager.ArenaPlugin.EndArenaGameEvent;
import me.maskat.ArenaManager.ArenaPlugin.PlayerDamageInArenaEvent;
import me.maskat.ArenaManager.ArenaPlugin.PlayerDeathInArenaEvent;
import me.maskat.ArenaManager.ArenaPlugin.PlayerLeaveArenaEvent;
import me.maskat.ArenaManager.ArenaPlugin.PlayerRespawnInArenaEvent;
import me.maskat.ArenaManager.ArenaPlugin.PrepareArenaAsyncEvent;
import me.maskat.ArenaManager.ArenaPlugin.PreparePlayersToArenaEvent;
import me.maskat.ArenaManager.ArenaPlugin.StartArenaGameEvent;
import mkproject.maskat.Papi.Papi;

public class PvP_ArenaInstance implements ArenaInstance {

	@Override
	public void onPrepareArenaAsync(PrepareArenaAsyncEvent event) {
	}

	@Override
	public void onPreparePlayersToArenaEvent(PreparePlayersToArenaEvent event) {

//        ItemStack item = new ItemStack(Material.SPLASH_POTION);
//        PotionMeta meta = ((PotionMeta) item.getItemMeta());
//        meta.setColor(Color.BLUE);
//        meta.addCustomEffect(new PotionEffect(PotionEffectType.Speed, 400, 0), true);
//        item.setItemMeta(meta);
//        player.getInventory().addItem(item);
		
		event.getArena().setPlayersFreeze(true);
		event.getArena().setPlayersGodmode(true);
		//event.getArena().setPlayersAllowTeleport(false);
		event.getArena().teleportPlayersToRandomLocations();
		event.getArena().setPlayersGamemode(GameMode.ADVENTURE);
		event.getArena().removePlayersPotionEffect();
		event.getArena().setPlayersClearInventory();
		event.getArena().setPlayersParameters(20, 20, 20, 0);
		
		boolean giveBow = Papi.Function.randomInteger(0, 1) == 1 ? true : false;
		
		event.getArena().setPlayersInventoryContent(
				Papi.Function.randomEnchantment(new ItemStack(Material.DIAMOND_SWORD, 1), false, Papi.Function.randomInteger(3, 5)),
				new ItemStack(Material.GOLDEN_APPLE, Papi.Function.randomInteger(3, 20)),
				(giveBow ? (Papi.Function.randomEnchantment(new ItemStack(Material.BOW, 1), false, Papi.Function.randomInteger(1, 4))) : null),
				(giveBow ? new ItemStack(Material.SPECTRAL_ARROW, Papi.Function.randomInteger(1, 30)) : null)
				);
		
		event.getArena().setPlayersInventoryArmor(
				Papi.Function.randomEnchantment(new ItemStack(Material.DIAMOND_BOOTS, 1), false, Papi.Function.randomInteger(1, 4)),
				Papi.Function.randomEnchantment(new ItemStack(Material.DIAMOND_LEGGINGS, 1), false, Papi.Function.randomInteger(1, 4)),
				Papi.Function.randomEnchantment(new ItemStack(Material.DIAMOND_CHESTPLATE, 1), false, Papi.Function.randomInteger(1, 4)),
				Papi.Function.randomEnchantment(new ItemStack(Material.DIAMOND_HELMET, 1), false, Papi.Function.randomInteger(1, 4))
				);
			
		ArenaPluginManager.doStartGame(this, 5);
	}

	@Override
	public void onStartArenaGame(StartArenaGameEvent event) {
		event.getArena().setPlayersFreeze(false);
		event.getArena().setPlayersGodmode(false);
		event.getArena().playersPlaySound(Sound.ENTITY_BLAZE_HURT);
	}

	@Override
	public void onEndArenaGame(EndArenaGameEvent event) {
	}

	@Override
	public void onAbortArenaGameEvent(AbortArenaGameEvent event) {
	}

	@Override
	public void onPlayerDeathInArena(PlayerDeathInArenaEvent event) {
		Plugin.getPlugin().getLogger().warning("Player '"+event.getPlayer().getPlayer().getName()+"' arena: PlayerDeathInArenaEvent");
		event.getDrops().clear();
		event.setDroppedExp(0);
		
		event.playerLeaveArena();
	}

	@Override
	public void onPlayerRespawnInArena(PlayerRespawnInArenaEvent event) {
		Plugin.getPlugin().getLogger().warning("Player '"+event.getPlayer().getPlayer().getName()+"' arena: PlayerRespawnInArenaEvent");
	}

	@Override
	public void onPlayerDamageInArena(PlayerDamageInArenaEvent event) {
	}

	@Override
	public void onPlayerLeaveArena(PlayerLeaveArenaEvent event) {
		event.getPlayer().getPlayer().getInventory().clear();
		event.getPlayer().getPlayer().updateInventory();
		
		if(!event.getArena().isGameEnded()) {
			Collection<ArpTeam> teamsInsideArena = event.getArena().getTeamsInsideArena();
			
			if(teamsInsideArena.size() == 1)
				teamsInsideArena.iterator().next().doWinnerGame();
		}
	}

}

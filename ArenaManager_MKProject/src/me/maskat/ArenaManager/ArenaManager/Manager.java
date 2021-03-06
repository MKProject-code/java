package me.maskat.ArenaManager.ArenaManager;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.maskat.ArenaManager.Plugin;
import me.maskat.ArenaManager.ArenaPlugin.AbortArenaGameEvent;
import me.maskat.ArenaManager.ArenaPlugin.ArpPlayer;
import me.maskat.ArenaManager.ArenaPlugin.ArpTeam;
import me.maskat.ArenaManager.ArenaPlugin.EndArenaGameEvent;
import me.maskat.ArenaManager.ArenaPlugin.PlayerDamageInArenaEvent;
import me.maskat.ArenaManager.ArenaPlugin.PlayerDeathInArenaEvent;
import me.maskat.ArenaManager.ArenaPlugin.PlayerRespawnInArenaEvent;
import me.maskat.ArenaManager.ArenaPlugin.PrepareArenaAsyncEvent;
import me.maskat.ArenaManager.ArenaPlugin.PreparePlayersToArenaEvent;
import me.maskat.ArenaManager.ArenaPlugin.StartArenaGameEvent;
import me.maskat.ArenaManager.Models.ArenesModel;
import me.maskat.ArenaManager.Models.ModelArena;
import me.maskat.ArenaManager.Models.ModelArenaPlayer;
import mkproject.maskat.Papi.Utils.Message;

public class Manager {

	public static void prepareArena(ModelArena modelArena) {
		modelArena.setArenaPluginNewInstance();
		
		for(ArpPlayer arpPlayer : modelArena.getArenaPluginInstanceArpPlayers())
		{
			ArenesModel.getPlayer(arpPlayer.getPlayer()).setBlockArenaMenu(true);
			arpPlayer.getPlayer().closeInventory();
			Message.sendTitle(arpPlayer.getPlayer(), null, "&6Przygotuj się na arene!");
		}
		
		modelArena.runTaskPreparePlayersFromAsync();
		
        Bukkit.getScheduler().runTaskLaterAsynchronously(Plugin.getPlugin(), new Runnable() {
            @Override
            public void run() {
            	if(modelArena.getArenaPluginInstance() != null)
            	{
            		modelArena.getArenaPluginInstance().onPrepareArenaAsync(new PrepareArenaAsyncEvent(modelArena));
					modelArena.doPreparePlayersFromAsync();
            	}
            }
        }, 40L);
	}
	
	public static void preparePlayersTask(ModelArena modelArena, int seconds) {
		Collection<ArpPlayer> arpPlayersOnline = modelArena.getArenaPluginInstanceArpPlayersOnline();
		
		if(arpPlayersOnline.size() < modelArena.getMaxPlayers())
		{
			for(ArpPlayer arpPlayer : arpPlayersOnline)
			{
				Message.sendTitle(arpPlayer.getPlayer(), null, "&cUczestnik opóścił grę...");
				ArenesModel.getPlayer(arpPlayer.getPlayer()).setBlockArenaMenu(false);
			}
			modelArena.getArenaPluginInstance().onAbortArenaGameEvent(new AbortArenaGameEvent(modelArena));
			modelArena.doClearArenaGame();
			return;
		}
		
		if(seconds<=0) {
			preparePlayers(modelArena);
			return;
		}
		
		for(ArpPlayer arpPlayer : arpPlayersOnline)
			Message.sendTitle(arpPlayer.getPlayer(), null, "&c"+String.valueOf(seconds));

        Plugin.getPlugin().getServer().getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
            @Override
            public void run() {
            	preparePlayersTask(modelArena, seconds-1);
            }
        }, 20L);
	}
	
	public static void preparePlayers(ModelArena modelArena) {
		for(ArpPlayer arpPlayer : modelArena.getArenaPluginInstanceArpPlayersOnline())
			arpPlayer.getPlayer().spigot().respawn();
		
		modelArena.getArenaPluginInstance().onPreparePlayersToArenaEvent(new PreparePlayersToArenaEvent(modelArena));
		modelArena.setGameStarted(true);
	}
	
	public static void doStartGame(ModelArena modelArena, int startSecoundDelay) {
		Collection<ArpPlayer> arpPlayersOnline = modelArena.getArenaPluginInstanceArpPlayersOnline();
		for(ArpPlayer arpPlayer : arpPlayersOnline)
			Message.sendTitle(arpPlayer.getPlayer(), null, null);
		
		if(startSecoundDelay <= 0)
			startSecoundDelay = 0;

        Plugin.getPlugin().getServer().getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
            @Override
            public void run() {
            	if(!modelArena.isAlreadyGamed() || modelArena.isGameEnded())
            		return;
            		
            	for(ArpPlayer arpPlayer : arpPlayersOnline)
					Message.sendTitle(arpPlayer.getPlayer(), null, "&aSTART", 0, 10, 10);
            	
            	modelArena.getArenaPluginInstance().onStartArenaGame(new StartArenaGameEvent(modelArena));
            }
        }, 20*startSecoundDelay);
	}
	
	public static void onPlayerDeathEvent(PlayerDeathEvent e) {
		
		ModelArenaPlayer modelPlayer = ArenesModel.getPlayer(e.getEntity());
		if(!modelPlayer.isRegisteredArena())
			return;
		
		if(!modelPlayer.getRegisteredArena().isGameStarted())
			return;
		
		ModelArena modelArena = ArenesModel.getModelArenaPlayed(e.getEntity());
		if(modelArena==null)
			return;
		
		PlayerDeathInArenaEvent event = new PlayerDeathInArenaEvent(e.getEntity(), modelArena, e);
		
		modelArena.getArenaPluginInstance().onPlayerDeathInArena(event);
		
		e.setKeepInventory(event.getKeepInventory());
		e.setKeepLevel(event.getKeepLevel());
		
		e.setDroppedExp(event.getDroppedExp());
	}
	
	public static void onPlayerRespawnEvent(PlayerRespawnEvent e) {
		ModelArenaPlayer modelPlayer = ArenesModel.getPlayer(e.getPlayer());
		if(modelPlayer == null)
			return;
		
		Location leaveRespawnLocation = modelPlayer.getLeaveRespawnTeleportLocation();
		if(leaveRespawnLocation != null)
		{
			e.setRespawnLocation(leaveRespawnLocation);
			return;
		}
		
		if(!modelPlayer.isRegisteredArena())
			return;
		
		if(!modelPlayer.getRegisteredArena().isGameStarted())
			return;
		
		ModelArena modelArena = ArenesModel.getModelArenaPlayed(e.getPlayer());
		if(modelArena==null)
			return;
		
		PlayerRespawnInArenaEvent event = new PlayerRespawnInArenaEvent(e.getPlayer(), modelArena);
		
		modelArena.getArenaPluginInstance().onPlayerRespawnInArena(event);
		
		e.setRespawnLocation(event.getRespawnPlayerLocation());
	}
//	
//	public static void preparePlayerWhenSpawnInArena(Player player) {
//		player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
//		player.setFoodLevel(20);
//		player.setSaturation(20);
//		player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20, 10));
//	}

	public static void onPlayerDamageEvent(EntityDamageEvent e, Player player) {
		
		ModelArenaPlayer modelPlayer = ArenesModel.getPlayer((Player)e.getEntity());
		if(!modelPlayer.isRegisteredArena())
			return;
		
		if(modelPlayer.isGodmode())
		{
			e.setCancelled(true);
			return;
		}
		
		if(!modelPlayer.getRegisteredArena().isGameStarted())
			return;
		
		ModelArena modelArena = ArenesModel.getModelArenaPlayed(player);
		if(modelArena==null)
			return;
		
		PlayerDamageInArenaEvent event = new PlayerDamageInArenaEvent(player, modelArena, e);
		
		modelArena.getArenaPluginInstance().onPlayerDamageInArena(event);
		
		e.setCancelled(event.isCancelled());
	}

	public static void onPlayerQuitEvent(PlayerQuitEvent e) {
		ModelArena modelArena = ArenesModel.getModelArenaPlayed(e.getPlayer());
		if(modelArena==null)
			return;
		
		ArpPlayer arpPlayer = modelArena.getArenaPluginInstanceArpPlayer(e.getPlayer());
		
		if(modelArena.isGameStarted())
			doEventPlayerLeaveArena(arpPlayer);
		else
			arpPlayer.setQuitedServer(true);
	}
	
	private static void doEventPlayerLeaveArena(ArpPlayer arpPlayer) {
		arpPlayer.leaveArena();
	}

	public static void doEndGame(ModelArena modelArena, ArpTeam winnerTeam, ArpPlayer winnerPlayer) {
		
		modelArena.setGameEnded(true);
		
		if(!modelArena.isGameStarted())
		{
			modelArena.doClearArenaGame();
			return;
		}
		
		EndArenaGameEvent event = new EndArenaGameEvent(modelArena, winnerTeam, winnerPlayer);
		
		modelArena.getArenaPluginInstance().onEndArenaGame(event);
		
		long leavePlayersSecoundDelay = event.getKickPlayersTime();
		if(leavePlayersSecoundDelay <= 0)
			leavePlayersSecoundDelay = 0;

        Plugin.getPlugin().getServer().getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
            @Override
            public void run() {
            	for(ArpPlayer arpPlayer : modelArena.getArenaPluginInstanceArpPlayersOnline())
            		arpPlayer.leaveArena();
            	
        		Plugin.getPlugin().getServer().getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                    	modelArena.doClearArenaGame();
                    }
                }, 5L);
        		
            }
        }, 20*leavePlayersSecoundDelay);
	}
}

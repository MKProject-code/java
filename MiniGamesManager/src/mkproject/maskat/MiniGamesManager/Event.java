package mkproject.maskat.MiniGamesManager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import mkproject.maskat.MiniGamesManager.Game.GameModel;
import mkproject.maskat.MiniGamesManager.Game.GamesManager;

public class Event implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		GameModel gameModel = GamesManager.getGameModel(e.getPlayer().getWorld());
		
		if(gameModel == null)
			return;
		
		gameModel.quitPlayer(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		if(e.getFrom().getWorld() == e.getTo().getWorld())
			return;
		
		GameModel gameModel = GamesManager.getGameModel(e.getFrom().getWorld());
		
		if(gameModel == null)
			return;
		
		gameModel.quitPlayer(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player)
		{
			GameModel gameModel = GamesManager.getGameModel(e.getEntity().getLocation().getWorld());
			
			if(gameModel == null)
				return;
			
			gameModel.doEvent((Player) e.getEntity(), e);
		}
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent e) {
		GameModel gameModel = GamesManager.getGameModel(e.getEntity().getLocation().getWorld());
		
		if(gameModel == null)
			return;
		
		Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				e.getEntity().spigot().respawn();
			}
		});
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		World deathWorld = e.getPlayer().getLocation().getWorld();
		
		GameModel gameModel = GamesManager.getGameModel(deathWorld);
		
		if(gameModel == null)
			return;
		
		e.setRespawnLocation(deathWorld.getSpawnLocation());
		
		gameModel.doEvent(e);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
		if(e.getNewGameMode() != GameMode.SPECTATOR)
			return;
		
		GameModel gameModel = GamesManager.getGameModel(e.getPlayer().getWorld());
		
		if(gameModel == null)
			return;
		
		gameModel.quitPlayer(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent e) {
		GameModel gameModel = GamesManager.getGameModel(e.getTo().getWorld());
		
		if(gameModel == null)
			return;
		
		gameModel.doEvent(e);
	}
}

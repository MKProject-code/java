package mkproject.maskat.MiniGamesManager.Game;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;

import mkproject.maskat.MiniGamesManager.Database;
import mkproject.maskat.MiniGamesManager.Plugin;
import mkproject.maskat.MiniGamesManager.GamesController.GameController;
import mkproject.maskat.MiniGamesManager.GamesController.GameController;
import mkproject.maskat.MiniGamesManager.GamesController.GamesControllerManager;
import mkproject.maskat.Papi.BoardManager.PapiBoard;
import mkproject.maskat.Papi.BoardManager.PapiObjective;
import mkproject.maskat.Papi.Utils.Message;

public class GameModel {

	private GameController gameController;
	private World world;
	private MiniGame miniGame;
//	private Location lobbyLocation;
	private boolean started;
	private boolean closed;
	private int playersNeed;
	private int playersMax;
	private BukkitTask timer;
	private BukkitTask infoWaitTask;
	private PapiObjective boardObjective;
	
	protected static GameModel createInstance(MiniGame miniGame, World world) {
		GameController gameController = GamesControllerManager.getController(miniGame, world);
		if(gameController == null)
			return null;
		return new GameModel(miniGame, world, gameController);
	}
	
	private GameModel(MiniGame miniGame, World world, GameController gameController) {
		this.gameController = gameController;
		this.world = world;
		this.miniGame = miniGame;
//		this.lobbyLocation = world.getSpawnLocation();
		this.started = false;
		this.closed = false;
		this.playersNeed = Database.getPlayersNeed(miniGame, world);
		this.playersMax = Database.getPlayersMax(miniGame, world);
//		this.gameController.createLobby();
		this.timer = null;
		
		PapiBoard board = new PapiBoard();
		PapiObjective objective = board.registerNewObjective​("title", "&7»» &6"+miniGame+" &7««", RenderType.INTEGER, DisplaySlot.SIDEBAR);
		objective.addScore("top", "", 3);
		objective.addScore("players", "&2» &aGraczy: &e0   ", 2);
		objective.addScore("bottom", " ", 1);
		
		this.boardObjective = objective;
	}
	
	protected World getWorld() {
		return this.world;
	}
	
	public void doEvent(PlayerMoveEvent e) {
		this.gameController.onPlayerMove(e);
	}
	public void doEvent(PlayerRespawnEvent e) {
		this.gameController.onPlayerRespawn(e);
	}
	public void doEvent(Player player, EntityDamageEvent e) {
		this.gameController.onPlayerDamage(player, e);
	}
	
	public boolean joinPlayer(Player player) {
//		if(player.teleport(this.lobbyLocation))
		if(GamesManager.teleportSafe(player, this.world.getSpawnLocation()))
		{
			this.boardObjective.setScore("players", "&2» &aGraczy: &e" + getPlayers().size()+"   ");
			this.boardObjective.getBoard().assignToPlayer(player);
			
			this.gameController.joinPlayer(player);
			this.checkGameStart();
			return true;
		}
		return false;
	}
	
	private void checkGameStart() {
//		if(this.playersMax <= 1)
//			return;
		if(this.playersMax <= 0)
			return;
		
		Collection<Player> playersGame = this.getPlayers();
		if(playersGame.size() == 0)
			return;
		
		if(playersGame.size() == this.playersMax)
		{
			this.runGame();
		}
		else
		{
			this.infoWaitTimer();
			
			if(playersGame.size() >= this.playersNeed)
				this.startTimer();
			else
				this.stopTimer();
		}
	}
	
	private void infoWaitTimer() {
		if(this.infoWaitTask != null || this.started)
			return;
		
		this.infoWaitTask = Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				infoWaitTask = null;
				if(!started) {
					sendInfoWait();
				}
			}
		});
	}
	
	private void startTimer() {
		if(this.timer != null || this.started == true)
			return;
		
		this.timer = Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if(getPlayers().size() >= playersNeed)
					runGame();
				else
					timer = null;
			}
		}, 30L*20L);
	}
	
	private void stopTimer() {
		if(this.timer != null)
		{
			this.timer.cancel();
			this.timer = null;
		}
	}
	
	private void runGame() {
		this.started = true;
		
		GamesManager.deleteLobby(this.miniGame);
		for(Player p : this.world.getPlayers()) {
			Message.sendMessage(p, GamesManager.getMessagePrefix(this.miniGame)+"&a&lPrzygotuj się! Startujemy!");
			Message.sendTitle(p, "&e&l-- 3 --", null);
			p.playSound(p.getLocation(), Sound.ITEM_TRIDENT_RETURN, 1f, 1f);
		}
		Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				for(Player p : world.getPlayers()) {
					Message.sendTitle(p, "&a&l-- 2 --", null);
					p.playSound(p.getLocation(), Sound.ITEM_TRIDENT_RETURN, 1f, 1f);
				}
				Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
					@Override
					public void run() {
						for(Player p : world.getPlayers()) {
							Message.sendTitle(p, "&c&l-- 1 --", null);
							p.playSound(p.getLocation(), Sound.ITEM_TRIDENT_RETURN, 1f, 1f);
						}
						Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
							@Override
							public void run() {
								for(Player p : world.getPlayers()) {
									Message.sendTitle(p, null, null);
									p.playSound(p.getLocation(), Sound.ENTITY_SHULKER_SHOOT, 1f, 1f);
								}
								gameController.startGame();
							}
						}, 30L);
					}
				}, 30L);
			}
		}, 30L);
	}

	public boolean isGameStarted() {
		return this.started;
	}

	public int getMaxPlayers() {
		return this.playersMax;
	}

	public Collection<Player> getPlayers() {
		return GamesManager.getPlayers(this.world);
	}
	public void setGameClosed(boolean closed) {
		this.closed = closed;
		this.boardObjective.unregister();
	}
	public void quitPlayer(Player playerQuit) {
		if(this.started) {
			Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					if(!closed)
					{
						boardObjective.setScore("players", "&2» &aGraczy: &e" + getPlayers().size()+"   ");
						if(playerQuit.getWorld() != world)
							boardObjective.getBoard().unassignPlayer(playerQuit);
						
						gameController.checkEnd(playerQuit);
					}
				}
			});
		}
		else
		{
			Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					boardObjective.setScore("players", "&2» &aGraczy: &e" + getPlayers().size()+"   ");
					boardObjective.getBoard().unassignPlayer(playerQuit);
					
					Plugin.getPlayerMenu().refreshPapiMenuPage();
					
					sendInfoWait();
				}
			});
		}
	}

	private void sendInfoWait() {
		for(Player p : world.getPlayers()) {
			Message.sendMessage(p, GamesManager.getMessagePrefix(this.miniGame)+"&c[BETA] &7&lOczekiwanie na graczy &7&l("+getPlayers().size()+"/"+playersMax+")");
		}
	}

}

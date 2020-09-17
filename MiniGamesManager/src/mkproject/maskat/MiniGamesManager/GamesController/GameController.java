package mkproject.maskat.MiniGamesManager.GamesController;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import mkproject.maskat.MiniGamesManager.Game.GamesManager;
import mkproject.maskat.MiniGamesManager.Game.MiniGame;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class GameController {

	protected World world;
	protected MiniGame miniGame;
	protected boolean startedGame;
	protected boolean endedGame;
	
	protected GameController(World world, MiniGame miniGame) {
		this.world = world;
		this.miniGame = miniGame;
		this.startedGame = false;
		this.endedGame = false;
	}
	
	protected void closeGame() {
		this.endedGame = true;
		GamesManager.closeGame(this.miniGame, this.world);
	}
	
	protected void kickPlayer(Player player, boolean withTeleport) {
		player.setGameMode(GameMode.SPECTATOR);
		if(withTeleport)
			GamesManager.teleportSafe(player, this.world.getSpawnLocation());
		
		player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_DEACTIVATE, 1f, 1f);
	}
	
	protected void broadcastEndGame(Player winner) {
		Message.sendTitle(winner, "&a&lZWYCIĘSTWO", null);
		winner.playSound(winner.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
		
		for(Player p : this.world.getPlayers())
			Message.sendMessage(p, GamesManager.getMessagePrefix(this.miniGame)+"&2&lKoniec gry! &a&lWygrywa "+Papi.Model.getPlayer(winner).getNameWithPrefix(true));
		
		for(Player p : Bukkit.getOnlinePlayers())
			Message.sendMessage(p, GamesManager.getMessagePrefix(this.miniGame)+"&6&lDołącz do zabawy! &e&l/"+this.miniGame.name().toLowerCase());
	}
	
	//Methods to override below
	
	public boolean startGame() {
		if(this.endedGame == true)
			return false;
		
		this.startedGame = true;
		return true;
	}
	
	public boolean checkEnd(Player playerQuit) {
		if(this.endedGame == true)
			return false;
		return true;
	}
	
	public void joinPlayer(Player player) {}
	
	public void onPlayerMove(PlayerMoveEvent e) {}
	public void onPlayerRespawn(PlayerRespawnEvent e) {}
	public void onPlayerDamage(Player player, EntityDamageEvent e) {};
}

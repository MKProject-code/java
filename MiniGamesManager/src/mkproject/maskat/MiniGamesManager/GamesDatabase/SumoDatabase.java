package mkproject.maskat.MiniGamesManager.GamesDatabase;

import org.bukkit.World;

import mkproject.maskat.MiniGamesManager.Database;
import mkproject.maskat.MiniGamesManager.Game.MiniGame;
import mkproject.maskat.MiniGamesManager.GamesController.GameController;

public abstract class SumoDatabase extends GameController {
	
	public final static MiniGame miniGame = MiniGame.Sumo;
	
	protected int locPlayBlockY;
	
	public SumoDatabase(World world) {
		super(world, miniGame);
		
		this.locPlayBlockY = Database.getPropertyInt(miniGame, world, "Platform.Play.Y");
	}
}

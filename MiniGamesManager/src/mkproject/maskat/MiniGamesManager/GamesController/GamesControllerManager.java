package mkproject.maskat.MiniGamesManager.GamesController;

import org.bukkit.World;

import mkproject.maskat.MiniGamesManager.Game.MiniGame;

public class GamesControllerManager {
	
	public static GameController getController(MiniGame miniGame, World world) {
		if(miniGame == MiniGame.ColorMix)
			return new ColorMixController(world);
		return null;
	}
}

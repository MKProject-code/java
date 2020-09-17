package mkproject.maskat.MiniGamesManager.GamesController;

import org.bukkit.World;

import mkproject.maskat.MiniGamesManager.Game.MiniGame;

public class GamesControllerManager {
	
	public static GameController getController(MiniGame miniGame, World world) {
		if(miniGame == ColorMixController.miniGame)
			return new ColorMixController(world);
		else if(miniGame == SumoController.miniGame)
			return new SumoController(world);
		return null;
	}
}

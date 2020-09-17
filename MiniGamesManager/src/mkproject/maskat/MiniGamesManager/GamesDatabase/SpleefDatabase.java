package mkproject.maskat.MiniGamesManager.GamesDatabase;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import mkproject.maskat.MiniGamesManager.Database;
import mkproject.maskat.MiniGamesManager.Game.MiniGame;
import mkproject.maskat.MiniGamesManager.GamesController.GameController;

public abstract class SpleefDatabase extends GameController {

	protected Location locPlay1;
	protected Location locPlay2;
	protected Material materialPlay;
	
	public SpleefDatabase(World world) {
		super(world, MiniGame.ColorMix);
		
		this.locPlay1 = new Location(world,
				Database.getPropertyInt(this.miniGame, world, "Platform.Play.X1"),
				Database.getPropertyInt(this.miniGame, world, "Platform.Play.Y1"),
				Database.getPropertyInt(this.miniGame, world, "Platform.Play.Z1")
				);
		this.locPlay1 = new Location(world,
				Database.getPropertyInt(this.miniGame, world, "Platform.Play.X2"),
				Database.getPropertyInt(this.miniGame, world, "Platform.Play.Y2"),
				Database.getPropertyInt(this.miniGame, world, "Platform.Play.Z2")
				);
		try {
			this.materialPlay = Material.valueOf(Database.getPropertyString(this.miniGame, world, "Platform.Play.Material"));
		} catch(Exception ex) {
			this.materialPlay = Material.STONE;
		}
	}

}

package mkproject.maskat.MiniGamesManager.GamesDatabase;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import mkproject.maskat.MiniGamesManager.Database;
import mkproject.maskat.MiniGamesManager.Game.MiniGame;
import mkproject.maskat.MiniGamesManager.GamesController.GameController;

public abstract class TntRunDatabase extends GameController {

	public final static MiniGame miniGame = MiniGame.TntRun;
	
	protected Location locPlayCenter;
	protected int locPlayRadius;
	protected int locPlayAmount;
	protected int locPlaySpace;
	protected List<Material> materialsPlay;

	
	public TntRunDatabase(World world) {
		super(world, miniGame);
		
		this.locPlayCenter = new Location(world,
				Database.getPropertyInt(miniGame, world, "Platform.Play.CenterX"),
				Database.getPropertyInt(miniGame, world, "Platform.Play.CenterY"),
				Database.getPropertyInt(miniGame, world, "Platform.Play.CenterZ")
				);
		this.locPlayRadius = Database.getPropertyInt(miniGame, world, "Platform.Play.Radius");
		this.locPlayAmount = Database.getPropertyInt(miniGame, world, "Platform.Play.Amount");
		this.locPlaySpace = Database.getPropertyInt(miniGame, world, "Platform.Play.Space");
		
		this.materialsPlay = new ArrayList<>();
		this.materialsPlay.add(Material.SAND);
		this.materialsPlay.add(Material.GRAVEL);
	}

}

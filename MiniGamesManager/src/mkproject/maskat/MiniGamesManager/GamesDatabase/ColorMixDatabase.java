package mkproject.maskat.MiniGamesManager.GamesDatabase;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import mkproject.maskat.MiniGamesManager.Database;
import mkproject.maskat.MiniGamesManager.Game.MiniGame;
import mkproject.maskat.MiniGamesManager.GamesController.GameController;
import mkproject.maskat.Papi.Papi;

public abstract class ColorMixDatabase extends GameController {

	protected Location locDef1;
	protected Location locDef2;
	protected Location locPlay1;
	protected Location locPlay2;
	
	public ColorMixDatabase(World world) {
		super(world, MiniGame.ColorMix);
		
		World locDefWorld = Bukkit.getWorld(Database.getPropertyString(this.miniGame, world, "Platform.Default.World"));
		if(locDefWorld==null) return;
		
		this.locDef1 = new Location(locDefWorld,
				Database.getPropertyInt(this.miniGame, world, "Platform.Default.X1"),
				Database.getPropertyInt(this.miniGame, world, "Platform.Default.Y1"),
				Database.getPropertyInt(this.miniGame, world, "Platform.Default.Z1")
				);
		this.locDef2 = new Location(locDefWorld,
				Database.getPropertyInt(this.miniGame, world, "Platform.Default.X2"),
				Database.getPropertyInt(this.miniGame, world, "Platform.Default.Y2"),
				Database.getPropertyInt(this.miniGame, world, "Platform.Default.Z2")
				);
		this.locPlay1 = new Location(world,
				Database.getPropertyInt(this.miniGame, world, "Platform.Play.X1"),
				Database.getPropertyInt(this.miniGame, world, "Platform.Play.Y1"),
				Database.getPropertyInt(this.miniGame, world, "Platform.Play.Z1")
				);
		this.locPlay2 = new Location(world,
				this.locPlay1.getBlockX() + (Papi.Function.getDifference(this.locDef1.getBlockX(), this.locDef2.getBlockX()) * (this.locDef2.getBlockX() > this.locDef1.getBlockX() ? 1 : -1)),
				this.locPlay1.getBlockY() + (Papi.Function.getDifference(this.locDef1.getBlockY(), this.locDef2.getBlockY()) * (this.locDef2.getBlockY() > this.locDef1.getBlockY() ? 1 : -1)),
				this.locPlay1.getBlockZ() + (Papi.Function.getDifference(this.locDef1.getBlockZ(), this.locDef2.getBlockZ()) * (this.locDef2.getBlockZ() > this.locDef1.getBlockZ() ? 1 : -1))
				);
	}

}

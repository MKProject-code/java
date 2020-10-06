package mkproject.maskat.MiniGamesManager.GamesController;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import mkproject.maskat.MiniGamesManager.Database;
import mkproject.maskat.MiniGamesManager.Plugin;
import mkproject.maskat.MiniGamesManager.Game.GamesManager;
import mkproject.maskat.MiniGamesManager.GamesDatabase.TntRunDatabase;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class TntRunController extends TntRunDatabase {

	private Collection<Block> blocksPlatform = new ArrayList<>();
	
	private long secondsToRemove = 10L;//1sekunda
	
	protected TntRunController(World world) {
		super(world);
		
		for(int i = 0; i < this.locPlayAmount; i++)
		{
			Location loc = this.locPlayCenter.clone();
			loc.setY(loc.getY()-(this.locPlaySpace*i));
			this.blocksPlatform.addAll(Papi.Region.setAreaCyl(loc, this.locPlayRadius, 1, this.materialsPlay, false));
		}
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent e) {
		if(e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getTo().getBlockZ() || e.getFrom().getBlockY() != e.getTo().getBlockY())
		{
			this.checkUnderBlocks(e.getPlayer(), e.getFrom());
		}
		
		if(e.getFrom().getBlockY() == e.getTo().getBlockY())
			return;
		
		if(e.getTo().getBlockY() <= 0)
		{
			if(this.startedGame)
				this.kickPlayerToSpect(e.getPlayer(), true);
			else
				this.teleportPlayerToStart(e.getPlayer());
		}
	}
	private void checkUnderBlocks(Player player, Location locationFrom) {
		if(this.startedGame && player.getGameMode() != GameMode.SPECTATOR)
		{
			int x = (int) Math.round(locationFrom.getX());
			int y = (int) locationFrom.getBlockY()-1;
			int z = (int) Math.round(locationFrom.getZ());
			
			Block checkBlock1 = locationFrom.getWorld().getBlockAt(x, y, z);
			Block checkBlock2 = locationFrom.getWorld().getBlockAt(x-1, y, z);
			Block checkBlock3 = locationFrom.getWorld().getBlockAt(x, y, z-1);
			Block checkBlock4 = locationFrom.getWorld().getBlockAt(x-1, y, z-1);
			
			boolean cb1 = this.blocksPlatform.remove(checkBlock1);
			boolean cb2 = this.blocksPlatform.remove(checkBlock2);
			boolean cb3 = this.blocksPlatform.remove(checkBlock3);
			boolean cb4 = this.blocksPlatform.remove(checkBlock4);
			
			if(cb1 || cb2 || cb3 || cb4) {
				Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
					@Override
					public void run() {
						if(cb1) {
							BlockState blockState = checkBlock1.getState();
							blockState.setType(Material.AIR);
							blockState.update(true, false);
						}
						if(cb2) {
							BlockState blockState = checkBlock2.getState();
							blockState.setType(Material.AIR);
							blockState.update(true, false);
						}
						if(cb3) {
							BlockState blockState = checkBlock3.getState();
							blockState.setType(Material.AIR);
							blockState.update(true, false);
						}
						if(cb4) {
							BlockState blockState = checkBlock4.getState();
							blockState.setType(Material.AIR);
							blockState.update(true, false);
						}
					}
				}, 6L);
			}
		}
	}

	@Override
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if(this.startedGame)
			this.kickPlayerToSpect(e.getPlayer(), false);
		else if(!this.endedGame)
			this.joinPlayer(e.getPlayer());
		else
			e.setRespawnLocation(Database.getSpawnLocation());
	}
	@Override
	public  void onPlayerDamage(Player player, EntityDamageEvent e) {
		e.setCancelled(true);
	}
	
	@Override
	public void joinPlayer(Player player) {
		GamesManager.initPlayer(player, GameMode.ADVENTURE);
	}
	
	@Override
	public boolean startGame() {
		if(!super.startGame())
			return false;
		
		for(Player player : this.world.getPlayers())
		{
			Message.sendTitle(player, null, "&eBiegnij!");
			this.checkUnderBlocks(player, player.getLocation());
		}
		
		return true;
	}

	@Override
	public boolean checkEnd(Player playerQuit) {
		if(!super.checkEnd(playerQuit))
			return false;
		
		Collection<Player> players = GamesManager.getPlayers(this.world);

		if(players.size() == 1)
		{
			if(this.startedGame)
			{
				Player winner = players.toArray(new Player[1])[0];
				if(winner.getLocation().getBlockY() >= (this.locPlayCenter.getBlockY()-(this.locPlaySpace*this.locPlayAmount)-1))
					this.broadcastEndGame(winner);
			}
			this.closeGame();
		}
		else if(players.size() <= 0)
		{
			this.closeGame();
		}
		return true;
	}
}

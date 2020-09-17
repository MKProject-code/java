package mkproject.maskat.MiniGamesManager.GamesController;

import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import mkproject.maskat.MiniGamesManager.Database;
import mkproject.maskat.MiniGamesManager.Game.GamesManager;
import mkproject.maskat.MiniGamesManager.GamesDatabase.SumoDatabase;
import mkproject.maskat.Papi.Utils.Message;

public class SumoController extends SumoDatabase {
	
	protected SumoController(World world) {
		super(world);
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent e) {
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
		if(!this.startedGame || this.endedGame)
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
			Message.sendTitle(player, null, "&eZepchnij wszystkich!");
		
		doStartRound();
		
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
				if(winner.getLocation().getBlockY() >= this.locPlayBlockY-1)
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

	private void doStartRound() {
		ItemStack itemStack = new ItemStack(Material.STICK);
		itemStack.addUnsafeEnchantment(Enchantment.KNOCKBACK, 15);
		
		for(Player player : GamesManager.getPlayers(this.world)) {
			this.addPlayerEquip(player, itemStack, true);
			this.addPlayerPotion(player, PotionEffectType.SPEED, Integer.MAX_VALUE, 2);
		}
	}
}

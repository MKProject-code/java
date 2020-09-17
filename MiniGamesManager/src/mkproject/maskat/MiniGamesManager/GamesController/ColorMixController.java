package mkproject.maskat.MiniGamesManager.GamesController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import mkproject.maskat.MiniGamesManager.Database;
import mkproject.maskat.MiniGamesManager.Plugin;
import mkproject.maskat.MiniGamesManager.Game.GamesManager;
import mkproject.maskat.MiniGamesManager.Game.MiniGame;
import mkproject.maskat.MiniGamesManager.GamesDatabase.ColorMixDatabase;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class ColorMixController extends ColorMixDatabase {

	private Collection<Block> blocksPlatform;
	private Map<Block, Material> blocksToRestore;
	private List<Material> materials;
	
	private long startSecondsMoveToColor = 80L;//8sekund
	private long minSecondsMoveToColor = 20L;//1sekunda
	private int round = 0;
	
	protected ColorMixController(World world) {
		super(world);
		this.blocksPlatform = Papi.Region.copyArea(this.locDef1, this.locDef2, this.locPlay1);
		this.materials = new ArrayList<>();
		this.blocksToRestore = new HashMap<>();
		for(Block block : blocksPlatform) {
			if(!this.materials.contains(block.getType()))
				this.materials.add(block.getType());
		}
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
			Message.sendTitle(player, null, "&eStań na odpowiednim kolorze!");
		
		doNextRound(null);
		
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
				if(winner.getLocation().getBlockY() >= this.locPlay1.getBlockY()-1)
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

	private void doNextRound(Material lastChoosenMaterial) {
		Collections.shuffle(this.materials);
		Material choosenMaterial = this.materials.remove(0);
		
		if(lastChoosenMaterial != null)
			this.materials.add(lastChoosenMaterial);
		
		this.round++;
		ItemStack itemStack = new ItemStack(choosenMaterial);
		
		for(Player player : GamesManager.getPlayers(this.world)) {
			this.addPlayerEquip(player, itemStack, true);
			player.setLevel(this.round);
		}
		
		this.timerMoveToColor(choosenMaterial);
	}
	
	private void timerMoveToColor(Material material) {
		Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if(!endedGame)
				{
					if(startSecondsMoveToColor > minSecondsMoveToColor)
					{
						startSecondsMoveToColor -= 3L;
						if(startSecondsMoveToColor < minSecondsMoveToColor)
							startSecondsMoveToColor = minSecondsMoveToColor;
					}
					removePlatform(material);
				}
			}
		}, this.startSecondsMoveToColor);
	}
	
	private void removePlatform(Material materialNot) {
		for(Player p : this.world.getPlayers())
			p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1f, 1f);
		
		for(Block block : this.blocksPlatform) {
			if(block.getType() != materialNot)
			{
				this.blocksToRestore.put(block, block.getType());
				block.setType(Material.AIR);
			}
		}
		
		Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if(!endedGame)
				{
					restorePlatform();
					doNextRound(materialNot);
				}
			}
		}, 60L);
	}
	
	private void restorePlatform() {
		for(Entry<Block, Material> entry : this.blocksToRestore.entrySet()) {
			entry.getKey().setType(entry.getValue());
		}
		this.blocksToRestore.clear();
	}
//	
//	private static void showBossBarTimer(String title, Collection<Player> targets, float time, BarColor barColor, BarStyle barStyle) {
//		BossBar bossBar = Bukkit.getServer().createBossBar(title, barColor, barStyle);
//		bossBar.setProgress(1D);
//		for(Player targetPlayer : targets) {
//			BossBar lastBB = targetsBossBarMap.get(targetPlayer);
//			if(lastBB != null)
//				lastBB.removePlayer(targetPlayer);
//			
//			bossBar.addPlayer(targetPlayer);
//			targetsBossBarMap.put(targetPlayer, bossBar);
//		}
//		
//		Timer t = new Timer();
//		
//		scheduleTaskBoss(t, bossBar, time);
//	}
//	
//	private static void scheduleTaskBoss(Timer timer, BossBar bossBar, float seconds) {
//		TimerTask task = new TimerTask() {
//			public void run() {
//				if(bossBar.getPlayers().size() <= 0)
//				{
//					bossBar.removeAll();
//				}
//				else
//				{
//					double newExp = bossBar.getProgress()-(0.1D/seconds);
//					if(newExp <= 0)
//					{
//						bossBar.setProgress(0);
//						for(Player player : bossBar.getPlayers())
//							targetsBossBarMap.remove(player);
//						bossBar.removeAll();
//					}
//					else
//					{
//						bossBar.setProgress(newExp);
//						scheduleTaskBoss(timer, bossBar, seconds);
//					}
//				}
//			}
//		};
//		long delay = 100L;
//		timer.schedule(task, delay);
//	}
}

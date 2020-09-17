package mkproject.maskat.MiniGamesManager.GamesController;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
	
	protected void kickPlayerToSpect(Player player, boolean withTeleport) {
		player.setGameMode(GameMode.SPECTATOR);
		if(withTeleport)
			GamesManager.teleportSafe(player, this.world.getSpawnLocation());
		
		player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_DEACTIVATE, 1f, 1f);
	}
	
	protected boolean teleportPlayerToStart(Player player) {
		return GamesManager.teleportSafe(player, this.world.getSpawnLocation());
	}
	
	protected void broadcastEndGame(Player winner) {
		Message.sendTitle(winner, "&a&lZWYCIĘSTWO", null);
		winner.playSound(winner.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
		
		for(Player p : this.world.getPlayers())
			Message.sendMessage(p, GamesManager.getMessagePrefix(this.miniGame)+"&2&lKoniec gry! &a&lWygrywa "+Papi.Model.getPlayer(winner).getNameWithPrefix(true));
		
		for(Player p : Bukkit.getOnlinePlayers())
			Message.sendMessage(p, GamesManager.getMessagePrefix(this.miniGame)+"&6&lDołącz do zabawy! &e&l/"+this.miniGame.name().toLowerCase());
	}
	
	protected void addPlayersEquip(ItemStack itemStack, boolean fullHotbar) {
		for(Player player : GamesManager.getPlayers(this.world)) {
			this.addPlayerEquip(player, itemStack, fullHotbar);
		}
	}
	
	protected void addPlayerEquip(Player player, ItemStack itemStack, boolean fullHotbar) {
		player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 1f);
		
		PlayerInventory playerInv = player.getInventory();
		
		if(fullHotbar) {
			for(int i=0;i<9;i++)
				playerInv.setItem(i, itemStack);
		}
		else
		{
			// TODO !!! check if eq is full?
			playerInv.addItem(itemStack);
		}
	}
	
	protected void addPlayersPotion(PotionEffectType type, int duration, int amplifier) {
		for(Player player : GamesManager.getPlayers(this.world)) {
			this.addPlayerPotion(player, type, duration, amplifier);
		}
	}
	
	protected void addPlayerPotion(Player player, PotionEffectType type, int duration, int amplifier) {
		player.addPotionEffect(new PotionEffect(type, duration, amplifier));
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

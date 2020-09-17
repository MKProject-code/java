package mkproject.maskat.MiniGamesManager.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mkproject.maskat.MiniGamesManager.Plugin;
import mkproject.maskat.MiniGamesManager.Game.GameModel;
import mkproject.maskat.MiniGamesManager.Game.GamesManager;
import mkproject.maskat.MiniGamesManager.Game.MiniGame;
import mkproject.maskat.Papi.Utils.Message;

public class JoinCommand implements CommandExecutor {

	private MiniGame miniGame;
	
	public JoinCommand(MiniGame miniGame) {
		this.miniGame = miniGame;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		GameModel gameModel = GamesManager.getLobbyGame(this.miniGame);
		if(gameModel == null) {
			Message.sendMessage(player, GamesManager.getMessagePrefix(this.miniGame)+"&cWszystkie mapy w trakcie gry :(");
			Plugin.getPlayerMenu().openPapiMenuPage(player);
			return true;
		}
		
		if(!gameModel.joinPlayer(player))
			Message.sendMessage(player, GamesManager.getMessagePrefix(this.miniGame)+"&cUpss.. Nie udało się dołączyć do gry :(");
		else
			Plugin.getPlayerMenu().refreshPapiMenuItem(this.miniGame);
		
		return true;
	}
}

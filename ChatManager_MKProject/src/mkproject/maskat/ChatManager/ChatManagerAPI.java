package mkproject.maskat.ChatManager;

import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;

public class ChatManagerAPI {
	public static void updatePlayerNameTab(Player player) {
		if(Papi.Model.getPlayer(player).isLogged())
			TabFormatter.updatePlayerListName(player, true);
		else
			TabFormatter.updatePlayerListName(player, false);
	}
}

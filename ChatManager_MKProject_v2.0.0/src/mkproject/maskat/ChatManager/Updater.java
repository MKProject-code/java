package mkproject.maskat.ChatManager;

import me.maskat.MoneyManager.MapiPlayer;
import me.maskat.MoneyManager.MapiUpdater;

public class Updater implements MapiUpdater {
	@Override
	public void onMoneyUpdate(MapiPlayer mapiPlayer) {
		TabFormatter.updatePlayerPoints(mapiPlayer.getPlayer(), (int)mapiPlayer.getPoints());
	}
}

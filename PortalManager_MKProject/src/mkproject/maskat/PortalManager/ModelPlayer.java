package mkproject.maskat.PortalManager;

import org.bukkit.entity.Player;

public class ModelPlayer {
	//private Player player;
	private boolean isTeleporting = false;
	
	public ModelPlayer(Player player) {
		//this.player = player;
	}
	
	public void setTeleporting(boolean b) {
		isTeleporting = b;
	}
	public boolean isTeleporting() {
		return isTeleporting;
	}
}

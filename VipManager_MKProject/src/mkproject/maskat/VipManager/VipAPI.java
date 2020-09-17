package mkproject.maskat.VipManager;

import java.time.LocalDateTime;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class VipAPI {
	public static void giveVIP(Player player, LocalDateTime endDateTime, String description) {
		Manager.addVIP(player, endDateTime, "VipAPI", description);
	}
	public static boolean isOfflineVIP(OfflinePlayer offlinePlayer) {
		return Manager.isVIP(offlinePlayer.getUniqueId());
	}
	public static boolean isOfflineVIP(UUID playerUUID) {
		return Manager.isVIP(playerUUID);
	}
	public static boolean isDonationVIP(Player player) {
		return Manager.isDonationVIP(player);
	}
}

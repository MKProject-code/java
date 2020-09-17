package mkproject.maskat.VipManager;

import java.time.LocalDateTime;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.maskat.wolfsecurity.api.WolfSecurityApi;
import mkproject.maskat.ChatManager.ChatManagerAPI;
import mkproject.maskat.Papi.Papi;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

public class Manager {
	public static void checkValidVIP(Player player, boolean updateChatManager) {
		if(Database.Users.getVipEnd(player) == null)
		{
			if(player.hasPermission("group.vip") || player.hasPermission("group.svip")) {
				User user = Plugin.getLuckPerms().getUserManager().getUser(player.getUniqueId());
				user.data().remove(Node.builder("group.vip").build());
				user.data().remove(Node.builder("group.svip").build());
				Plugin.getLuckPerms().getUserManager().saveUser(user);
			}
			
			if(updateChatManager)
			{
				//ChatManagerAPI.updateAllPlayerNameTab();
				ChatManagerAPI.updatePlayerNameTab(player);
			}
			WolfSecurityApi.getWolfPlayer(player).updateOwnRegionProtection();
		}
	}

	public static void addVIP(Player player, LocalDateTime endDateTime, String descriptionFrom, String description) {
		Database.Users.addVip(player, endDateTime, "["+descriptionFrom+"-"+Papi.Function.getRemainingTimeString(endDateTime)+"] "+description);
		User user = Plugin.getLuckPerms().getUserManager().getUser(player.getUniqueId());
		user.data().add(Node.builder("group.vip").build());
		Plugin.getLuckPerms().getUserManager().saveUser(user);
		
		//ChatManagerAPI.updateAllPlayerNameTab();
		ChatManagerAPI.updatePlayerNameTab(player);
		
		WolfSecurityApi.getWolfPlayer(player).updateOwnRegionProtection();
	}

	public static boolean isVIP(UUID playerUUID) {
		Player player = Bukkit.getPlayer(playerUUID);
		if(player != null)
			return player.hasPermission("group.vip");
		else
		{
			OfflinePlayer playerOffline = Bukkit.getOfflinePlayer(playerUUID);
			if(playerOffline == null || Database.Users.getVipOfflineEnd(playerOffline) == null)
				return false;
			else
				return true;
		}
	}

	public static boolean isDonationVIP(Player player) {
		return Database.Users.isDonationVIP(player);
	}
}

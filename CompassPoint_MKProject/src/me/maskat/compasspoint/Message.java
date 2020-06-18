package me.maskat.compasspoint;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Message {
	
	public static void sendMessage(final Player player, String msg) {
		player.sendMessage(getColorMessage(msg));
	}
	
    public static void sendActionBar(final Player player, String msg) {
    	player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(getColorMessage(msg)));
    }
	
    public static String getColorMessage(final String msg) {
    	return ChatColor.translateAlternateColorCodes('&', msg);
    }
}

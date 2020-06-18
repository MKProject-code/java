package me.maskat.landsecurity;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Message {
	
	public static void sendMessage(final Player player, String msglang) {
		player.sendMessage(getColorMessageLang(msglang));
	}
	
	public static void sendMessage(final Player player, String msglang, final ImmutableMap<String, String> replaceMap) {
		player.sendMessage(getColorMessageLang(msglang, replaceMap));
	}
	
    public static void sendActionBar(final Player player, String msglang) {
    	player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', getMessageLang(msglang))));
    }
    
    public static void sendActionBar(final Player player, String msglang, final ImmutableMap<String, String> replaceMap) {
    	player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(getMessageLang(msglang, replaceMap)));
    }
	
    public static void sendTitle(final Player player, final Integer fadeIn, final Integer stay, final Integer fadeOut, final String title) {
        sendTitle(player, title, null, fadeIn, stay, fadeOut);
    }
    
    public static void sendFullTitle(final Player player, final Integer fadeIn, final Integer stay, final Integer fadeOut, final String title, final String subtitle) {
        sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
    }
    
    public static void sendSubTitle(final Player player, final Integer fadeIn, final Integer stay, final Integer fadeOut, final String subtitle) {
    	sendTitle(player, null, subtitle, fadeIn, stay, fadeOut);
    }
    
    public static String getColorMessage(final String msg) {
    	return ChatColor.translateAlternateColorCodes('&', msg);
    }
    
    public static String getColorMessageLang(final String msglang) {
    	return ChatColor.translateAlternateColorCodes('&', getMessageLang(msglang));
    }
    
    public static String getColorMessageLang(final String msglang, final ImmutableMap<String, String> replaceMap) {
    	return ChatColor.translateAlternateColorCodes('&', getMessageLang(msglang, replaceMap));
    }
    
    private static String getMessageLang(final String msglang) {
    	String msg = LandSecurity.plugin.lang.getString(msglang);
    	if(msg == null)
    	{
    		msg = "!!! EMPTY LANG FOR: " + msglang;
    		System.out.println("[LandSecurity] ****** ERROR ****** " + msg);
    	}
    	return msg;
    }
    
    private static String getMessageLang(final String msglang, final ImmutableMap<String, String> replaceMap) {
    	String msg = LandSecurity.plugin.lang.getString(msglang);
    	if(msg == null)
    	{
    		msg = "!!! EMPTY LANG FOR: " + msglang;
    		System.out.println("[LandSecurity] ****** ERROR ****** " + msg);
    	}
    	else
    	{
    		msg = ChatColor.translateAlternateColorCodes('&', msg);
	    	for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
	    		msg = msg.replace("%"+entry.getKey()+"%", entry.getValue());
	    	}
    	}
    	return msg;
    }
	
    private static void sendTitle(final Player player, String title, String subtitle, final Integer fadeIn, final Integer stay, final Integer fadeOut) {
        try {
            if (title != null) {
                title = ChatColor.translateAlternateColorCodes('&', title);
                title = title.replaceAll("%player%", player.getDisplayName());
                Object e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
                Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                Constructor<?> subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                Object titlePacket = subtitleConstructor.newInstance(e, chatTitle, fadeIn, stay, fadeOut);
                sendPacket(player, titlePacket);
                e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"));
                titlePacket = subtitleConstructor.newInstance(e, chatTitle);
                sendPacket(player, titlePacket);
            }
            if (subtitle != null) {
                subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
                subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
                Object e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
                Object chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                Constructor<?> subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                Object subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
                sendPacket(player, subtitlePacket);
                e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
                chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
                subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
                sendPacket(player, subtitlePacket);
            }
        }
        catch (Exception var11) {
            var11.printStackTrace();
        }
    }
    
    private static void sendPacket(final Player player, final Object packet) {
        try {
            final Object handle = player.getClass().getMethod("getHandle", (Class<?>[])new Class[0]).invoke(player, new Object[0]);
            final Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static Class<?> getNMSClass(final String name) {
        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package mkproject.maskat.Papi.Utils;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableMap;

import me.maskat.InventoryManager.Plugin;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.PapiPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Message {
	public static void sendMessage(final Player player, String colorMessage) {
		player.sendMessage(getColorMessage(colorMessage));
	}
	
	public static void sendRawMessage(final Player player, String rawMessage) {
	    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw "+player.getName()+" "+rawMessage);
	}
	
	@Deprecated
	public static void sendConsole(String colorMessage) {
		Plugin.getPlugin().getLogger().info(getColorMessage(colorMessage));
	}
	public static void sendConsoleInfo(JavaPlugin pluginExecutor, String colorMessage) {
		pluginExecutor.getLogger().info(getColorMessage(colorMessage));
	}
	public static void sendConsoleWarrning(JavaPlugin pluginExecutor, String colorMessage) {
		pluginExecutor.getLogger().warning(getColorMessage(colorMessage));
	}
	
	public static void sendBroadcast(String colorMessage) {
		Bukkit.broadcastMessage(getColorMessage(colorMessage));
	}
	
    public static void sendActionBar(final Player player, String colorMessage) {
    	player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(getColorMessage(colorMessage)));
    }
	
    public static String getColorMessage(final String message) {
    	return (message == null ? null : ChatColor.translateAlternateColorCodes('&', message));
    }
    
    public static String getMessageLang(YamlConfiguration languageYaml, final String colorMessageLang) {
    	String msg = languageYaml.getString(colorMessageLang);
    	if(msg == null)
    	{
    		msg = "!!! EMPTY LANG FOR: " + colorMessageLang;
    		PapiPlugin.getPlugin().getLogger().warning("****** WARNING ****** " + msg);
    		PapiPlugin.getPlugin().getLogger().warning("****** WARNING ****** Missing defined in file: " + languageYaml.getCurrentPath());
    	}
    	return getColorMessage(msg);
    }
    
    public static String getMessageLang(YamlConfiguration languageYaml, final String colorMessageLang, final ImmutableMap<String, String> replaceMap) {
    	String msg = languageYaml.getString(colorMessageLang);
    	if(msg == null)
    	{
    		msg = "!!! EMPTY LANG FOR: " + colorMessageLang;
    		PapiPlugin.getPlugin().getLogger().warning("****** WARNING ****** " + msg);
    		PapiPlugin.getPlugin().getLogger().warning("****** WARNING ****** Missing defined in file: " + languageYaml.getCurrentPath());
    	}
    	else
    	{
	    	for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
	    		msg = msg.replace("{"+entry.getKey()+"}", entry.getValue());
	    	}
    	}
    	return getColorMessage(msg);
    }
    
   public static String getMessageConfig(FileConfiguration configFile, final String colorMessageConfig) {
   		String msg = configFile.getString(colorMessageConfig);
		if(msg == null)
    	{
    		msg = "!!! EMPTY CONFIG FOR: " + colorMessageConfig;
    		PapiPlugin.getPlugin().getLogger().warning("****** WARNING ****** " + msg);
    		PapiPlugin.getPlugin().getLogger().warning("****** WARNING ****** Missing defined in file: " + configFile.getCurrentPath());
    	}
    	return getColorMessage(msg);
   }    
   
//   public static String getMessageConfigList(FileConfiguration configFile, final String colorMessageListConfig) {
//	   List<?> msgArr = configFile.getList(colorMessageListConfig);
//	   String msg = "";
//	   if(msgArr == null)
//	   {
//		   msg = "!!! EMPTY CONFIG FOR: " + colorMessageListConfig;
//		   PapiPlugin.getPlugin().getLogger().warning("****** WARNING ****** " + msg);
//		   PapiPlugin.getPlugin().getLogger().warning("****** WARNING ****** Missing defined in file: " + configFile.getCurrentPath());
//	   }
//	   for(Object msgLine : msgArr) {
//		   msg = "msg"
//	   }
//	   return getColorMessage(msg);
//  }
    
    public static String getMessageConfig(FileConfiguration configFile, final String colorMessageConfig, final ImmutableMap<String, String> replaceMap) {
    	String msg = configFile.getString(colorMessageConfig);
    	if(msg == null)
    	{
    		msg = "!!! EMPTY CONFIG FOR: " + colorMessageConfig;
    		PapiPlugin.getPlugin().getLogger().warning("****** WARNING ****** " + msg);
    		PapiPlugin.getPlugin().getLogger().warning("****** WARNING ****** Missing defined in file: " + configFile.getCurrentPath());
    	}
    	else
    	{
	    	for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
	    		msg = msg.replace("{"+entry.getKey()+"}", entry.getValue());
	    	}
    	}
    	return getColorMessage(msg);
    }
    
    public static String getCommandMessage(final String[] args, final int startIndex) {
        final StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < args.length; ++i) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString();
    }
    
    public static void debugMessage(Object plugin, final String colorMessage) {
    	if(!Papi.DEVELOPER_DEBUG)
    		return;
    	PapiPlugin.getPlugin().getLogger().info(getColorMessage("&7[&6"+plugin.getClass().getName()+"&7] &e" + colorMessage));
    	
    }
    
    public static void sendTitle(final Player player, String title, String subtitle) {
    	sendTitle(player, title, subtitle, 10, 70, 20);
    }
//	Parameters:
//    title - Title text
//    subtitle - Subtitle text
//    fadeIn - time in ticks for titles to fade in. Defaults to 10.
//    stay - time in ticks for titles to stay. Defaults to 70.
//    fadeOut - time in ticks for titles to fade out. Defaults to 20. 
    public static void sendTitle(final Player player, String title, String subtitle, final Integer fadeIn, final Integer stay, final Integer fadeOut) {
    	if(title==null) title = "";
    	if(subtitle==null) subtitle = "";
    	player.sendTitle(getColorMessage(title), getColorMessage(subtitle), fadeIn, stay, fadeOut);
    }


    
//    public static void sendTitle(final Player player, String title, String subtitle, final Integer fadeIn, final Integer stay, final Integer fadeOut) {
//        try {
//            if (title != null) {
//                title = ChatColor.translateAlternateColorCodes('&', title);
//                title = title.replaceAll("%player%", player.getDisplayName());
//                Object e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
//                Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
//                Constructor<?> subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
//                Object titlePacket = subtitleConstructor.newInstance(e, chatTitle, fadeIn, stay, fadeOut);
//                sendPacket(player, titlePacket);
//                e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
//                chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
//                subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"));
//                titlePacket = subtitleConstructor.newInstance(e, chatTitle);
//                sendPacket(player, titlePacket);
//            }
//            if (subtitle != null) {
//                subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
//                subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
//                Object e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
//                Object chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
//                Constructor<?> subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
//                Object subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
//                sendPacket(player, subtitlePacket);
//                e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
//                chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
//                subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
//                subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
//                sendPacket(player, subtitlePacket);
//            }
//        }
//        catch (Exception var11) {
//            var11.printStackTrace();
//        }
//    }
//    
//    private static void sendPacket(final Player player, final Object packet) {
//        try {
//            final Object handle = player.getClass().getMethod("getHandle", (Class<?>[])new Class[0]).invoke(player, new Object[0]);
//            final Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
//            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    
//    private static Class<?> getNMSClass(final String name) {
//        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
//        try {
//            return Class.forName("net.minecraft.server." + version + "." + name);
//        }
//        catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    
//    public enum ClickAction {
//        RUN_COMMAND, SUGGEST_COMMAND, OPEN_URL
//    }
//    public enum HoverAction {
//        SHOW_TEXT
//    }
    
//	public static JsonBuilder_off getJsonBuilder() {
//		return new JsonBuilder_off();
//	}
//	public static void sendMessageJson(Player player, JsonBuilder_off jsonBuilder) {
//		jsonBuilder.sendJson(player);
//	}
}

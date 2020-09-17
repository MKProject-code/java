package mkproject.maskat.ChatManager;

import org.bukkit.event.player.AsyncPlayerChatEvent;

import mkproject.maskat.Papi.Utils.Message;

public class ChatFormatter {
    private static final String GROUP_PLACEHOLDER = "{group}";
    private static final String NAME_PLACEHOLDER = "{name}";
    private static final String DISPLAYNAME_PLACEHOLDER = "{displayname}";
    private static final String MESSAGE_PLACEHOLDER = "{message}";
    private static final String PREFIX_PLACEHOLDER = "{prefix}";
    private static final String SUFFIX_PLACEHOLDER = "{suffix}";
    private static final String PREFIXMESSAGE_PLACEHOLDER = "{prefixmessage}";
    private static final String DEFAULT_FORMAT = "<" + PREFIX_PLACEHOLDER + NAME_PLACEHOLDER + SUFFIX_PLACEHOLDER + "> " + MESSAGE_PLACEHOLDER;

    private static String chatFormat;

    protected static void reloadConfigValues() {
    	chatFormat = colorize(Plugin.getPlugin().getConfig().getString("ChatFormatter.Chat", DEFAULT_FORMAT)
                .replace(DISPLAYNAME_PLACEHOLDER, "%1$s")
                .replace(MESSAGE_PLACEHOLDER, "%2$s"));
    }

//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        if (args.length != 0 && args[0].equalsIgnoreCase("reload")) {
//            reloadConfig();
//            reloadConfigValues();
//
//            sender.sendMessage("Reloaded successfully.");
//            return true;
//        }
//
//        return false;
//    }

    public static void onChatLow(AsyncPlayerChatEvent e) {
        e.setFormat(chatFormat);
    }

    public static void onChatHigh(AsyncPlayerChatEvent e) {
    	
        String format = e.getFormat();
        if (Plugin.getVaultChat() != null && format.contains(GROUP_PLACEHOLDER)) {
            format = format.replace(GROUP_PLACEHOLDER, Plugin.getVaultChat().getPrimaryGroup(e.getPlayer()));
        }
        if (Plugin.getVaultChat() != null && format.contains(PREFIX_PLACEHOLDER)) {
        	format = format.replace(PREFIX_PLACEHOLDER, Plugin.getVaultChat().getPlayerPrefix(e.getPlayer()));
        }
        if (Plugin.getVaultChat() != null && format.contains(SUFFIX_PLACEHOLDER)) {
            format = format.replace(SUFFIX_PLACEHOLDER, Plugin.getVaultChat().getPlayerSuffix(e.getPlayer()));
        }
        if (Plugin.getVaultChat() != null && format.contains(PREFIXMESSAGE_PLACEHOLDER)) {
        	format = format.replace(PREFIXMESSAGE_PLACEHOLDER, Plugin.getPlugin().getConfig().getString("ChatFormatter.PrefixMessage.Default"));
        }
        
        format = colorize(format.replace(NAME_PLACEHOLDER, e.getPlayer().getName()));
        
        if(e.getPlayer().hasPermission("mkp.chatmanager.chatcolors"))
        	format = format.replace("%2$s", colorize(e.getMessage()));
        else
        	format = format.replace("%2$s", e.getMessage());
        
        e.setFormat(format.replace("%", "%%"));
    }

    private static String colorize(String s) {
        return s == null ? null : Message.getColorMessage(s);
    }

}

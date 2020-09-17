// 
// Decompiled by Procyon v0.5.36
// 

package Trade;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

public class Configuration
{
    private Map<String, String> messages;
    private Map<String, Boolean> booleans;
    private Map<String, Integer> integers;
    private Map<String, Long> longs;
    private String prefix;
    
    public Configuration(final boolean reload) {
        this.messages = new HashMap<String, String>();
        this.booleans = new HashMap<String, Boolean>();
        this.integers = new HashMap<String, Integer>();
        this.longs = new HashMap<String, Long>();
        Main.getInstance().getConfig().options().copyDefaults(true);
        Main.getInstance().getConfig().addDefault("Prefix", (Object)"&7[&cTrade&7] ");
        Main.getInstance().getConfig().addDefault("PermissionTrade", (Object)"trade.player");
        Main.getInstance().getConfig().addDefault("PermissionReload", (Object)"trade.admin");
        Main.getInstance().getConfig().addDefault("Messages.Trade", (Object)"%prefix%&7/&7trade &c<playerName> &7| &cTrade with Player");
        Main.getInstance().getConfig().addDefault("Messages.Reload", (Object)"%prefix%&7/&7trade &creload &7| &cReload the Configuration");
        Main.getInstance().getConfig().addDefault("Messages.TradeRequest", (Object)"%prefix%&7Trade request has been sent to &a%player%&7!");
        Main.getInstance().getConfig().addDefault("Messages.TradeRequestAlreadySent", (Object)"%prefix%&7Trade request has been already sent to &a%player%&7!");
        Main.getInstance().getConfig().addDefault("Messages.TradeRequestOtherLine1", (Object)"%prefix%&7Trade request has been sent from &a%player%&7!");
        Main.getInstance().getConfig().addDefault("Messages.TradeRequestOtherLine2", (Object)"%prefix%&7Accept it using &c/trade %player%&7!");
        Main.getInstance().getConfig().addDefault("Messages.TradeRequestAccepted", (Object)"%prefix%&7Trade request has been accepted from &a%player%&7!");
        Main.getInstance().getConfig().addDefault("Messages.TradeRequestOtherAccepted", (Object)"%prefix%&7You accepted the trade request from &a%player%&7!");
        Main.getInstance().getConfig().addDefault("Messages.TradeRequestTimeOver", (Object)"%prefix%&7Trade request with &c%player% &7has been removed, time over&7!");
        Main.getInstance().getConfig().addDefault("Messages.TradeOtherWorld", (Object)"%prefix%&7Trade Request can't be sent, Player is on other World&7!");
        Main.getInstance().getConfig().addDefault("Messages.TradeHighRadius", (Object)"%prefix%&7Trade Request failed, Player is too far away&7!");
        Main.getInstance().getConfig().addDefault("Messages.NoSelfTrade", (Object)"%prefix%&cYou can't trade with yourself!");
        Main.getInstance().getConfig().addDefault("Messages.TradeCancelled", (Object)"%prefix%&cInventory has been closed, trade has been cancelled!");
        Main.getInstance().getConfig().addDefault("Messages.PlayerDoesNotExist", (Object)"%prefix%&cThe entered Player does not exist!");
        Main.getInstance().getConfig().addDefault("Messages.Reloaded", (Object)"%prefix%&eConfiguration &7has been reloaded.");
        Main.getInstance().getConfig().addDefault("TradeSettings.BlockRadius", (Object)5);
        Main.getInstance().getConfig().addDefault("TradeSettings.AllowedTime", (Object)20000);
        Main.getInstance().getConfig().addDefault("TradeSettings.InventoryTitle", (Object)"You                  Other");
        Main.getInstance().getConfig().addDefault("TradeSettings.Items.Accept", (Object)"&aClick here to accept the Trade!");
        Main.getInstance().getConfig().addDefault("TradeSettings.Items.Decline", (Object)"&cClick here to decline the Trade!");
        Main.getInstance().getConfig().addDefault("TradeSettings.Items.Status.Empty", (Object)"&7Status Pending..");
        Main.getInstance().getConfig().addDefault("TradeSettings.Items.Status.Accepted", (Object)"&aAccepted!");
        Main.getInstance().getConfig().addDefault("TradeSettings.Items.Status.Declined", (Object)"&cDeclined!");
        Main.getInstance().getConfig().addDefault("TradeSettings.Items.Status.Traded", (Object)"&b > Traded <\n&7OK");
        Main.getInstance().getConfig().addDefault("TradeSettings.AutoClose", (Object)false);
        if (!reload) {
            Main.getInstance().saveConfig();
        }
        Main.getInstance().reloadConfig();
        this.prefix = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Prefix"));
        this.messages.put("PermissionTrade", Main.getInstance().getConfig().getString("PermissionTrade"));
        this.messages.put("PermissionReload", Main.getInstance().getConfig().getString("PermissionReload"));
        this.messages.put("Trade", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.Trade")));
        this.messages.put("Reload", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.Reload")));
        this.messages.put("TradeRequest", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.TradeRequest")));
        this.messages.put("TradeRequestAlreadySent", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.TradeRequestAlreadySent")));
        this.messages.put("TradeRequestOtherLine1", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.TradeRequestOtherLine1")));
        this.messages.put("TradeRequestOtherLine2", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.TradeRequestOtherLine2")));
        this.messages.put("TradeRequestAccepted", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.TradeRequestAccepted")));
        this.messages.put("TradeRequestOtherAccepted", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.TradeRequestOtherAccepted")));
        this.messages.put("TradeRequestTimeOver", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.TradeRequestTimeOver")));
        this.messages.put("TradeOtherWorld", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.TradeOtherWorld")));
        this.messages.put("TradeHighRadius", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.TradeHighRadius")));
        this.messages.put("NoSelfTrade", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.NoSelfTrade")));
        this.messages.put("TradeCancelled", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.TradeCancelled")));
        this.messages.put("PlayerDoesNotExist", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.PlayerDoesNotExist")));
        this.messages.put("Reloaded", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.Reloaded")));
        this.integers.put("TradeSettings.BlockRadius", Main.getInstance().getConfig().getInt("TradeSettings.BlockRadius"));
        this.longs.put("TradeSettings.AllowedTime", Main.getInstance().getConfig().getLong("TradeSettings.AllowedTime"));
        this.messages.put("TradeSettings.InventoryTitle", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("TradeSettings.InventoryTitle")));
        this.messages.put("TradeSettings.Items.Accept", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("TradeSettings.Items.Accept")));
        this.messages.put("TradeSettings.Items.Decline", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("TradeSettings.Items.Decline")));
        this.messages.put("TradeSettings.Items.Status.Empty", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("TradeSettings.Items.Status.Empty")));
        this.messages.put("TradeSettings.Items.Status.Accepted", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("TradeSettings.Items.Status.Accepted")));
        this.messages.put("TradeSettings.Items.Status.Declined", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("TradeSettings.Items.Status.Declined")));
        this.messages.put("TradeSettings.Items.Status.Traded", ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("TradeSettings.Items.Status.Traded")));
        this.booleans.put("TradeSettings.AutoClose", Main.getInstance().getConfig().getBoolean("TradeSettings.AutoClose"));
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public Integer getInteger(final String path) {
        return this.integers.get(path);
    }
    
    public Long getLong(final String path) {
        return this.longs.get(path);
    }
    
    public String getMessage(final String path) {
        return this.messages.get(path).replace("%prefix%", this.prefix);
    }
    
    public Boolean getBoolean(final String path) {
        return this.booleans.get(path);
    }
}

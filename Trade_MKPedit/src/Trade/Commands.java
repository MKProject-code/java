// 
// Decompiled by Procyon v0.5.36
// 

package Trade;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class Commands implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("trade")) {
            if (args.length == 0) {
                if (sender instanceof Player && sender.hasPermission(Main.getInstance().getConfiguration().getMessage("PermissionTrade"))) {
                    sender.sendMessage(Main.getInstance().getConfiguration().getMessage("Trade"));
                }
                if (sender.hasPermission(Main.getInstance().getConfiguration().getMessage("PermissionReload"))) {
                    sender.sendMessage(Main.getInstance().getConfiguration().getMessage("Reload"));
                }
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission(Main.getInstance().getConfiguration().getMessage("PermissionReload"))) {
                Main.getInstance().setConfiguration(new Configuration(true));
                sender.sendMessage(Main.getInstance().getConfiguration().getMessage("Reloaded"));
            }
            if (args.length == 1 && !args[0].equalsIgnoreCase("reload") && sender instanceof Player && sender.hasPermission(Main.getInstance().getConfiguration().getMessage("PermissionTrade"))) {
                final Player player = (Player)sender;
                final String name = args[0];
                final Player onlinePlayer = Bukkit.getPlayer(name);
                if (onlinePlayer != null) {
                    if (onlinePlayer.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                        player.sendMessage(Main.getInstance().getConfiguration().getMessage("NoSelfTrade"));
                    }
                    else if (Main.getInstance().getTradeHandler().isWorld(player, onlinePlayer)) {
                        if (Main.getInstance().getTradeHandler().isRadius(player, onlinePlayer)) {
                            if (!Main.getInstance().getTradeHandler().isAlreadySent(player, onlinePlayer)) {
                                final boolean newRequest = Main.getInstance().getTradeHandler().updateRequest(player, onlinePlayer);
                                if (newRequest) {
                                    player.sendMessage(Main.getInstance().getConfiguration().getMessage("TradeRequest").replace("%player%", onlinePlayer.getName()));
                                    onlinePlayer.sendMessage(Main.getInstance().getConfiguration().getMessage("TradeRequestOtherLine1").replace("%player%", player.getName()));
                                    onlinePlayer.sendMessage(Main.getInstance().getConfiguration().getMessage("TradeRequestOtherLine2").replace("%player%", player.getName()));
                                }
                                else {
                                    onlinePlayer.sendMessage(Main.getInstance().getConfiguration().getMessage("TradeRequestAccepted").replace("%player%", player.getName()));
                                    player.sendMessage(Main.getInstance().getConfiguration().getMessage("TradeRequestOtherAccepted").replace("%player%", onlinePlayer.getName()));
                                    Main.getInstance().getTradeHandler().removeRequests(player);
                                    Main.getInstance().getTradeHandler().removeRequests(onlinePlayer);
                                    Main.getInstance().getTradeHandler().open(player, onlinePlayer);
                                }
                            }
                            else {
                                player.sendMessage(Main.getInstance().getConfiguration().getMessage("TradeRequestAlreadySent").replace("%player%", onlinePlayer.getName()));
                            }
                        }
                        else {
                            player.sendMessage(Main.getInstance().getConfiguration().getMessage("TradeHighRadius"));
                        }
                    }
                    else {
                        player.sendMessage(Main.getInstance().getConfiguration().getMessage("TradeOtherWorld"));
                    }
                }
                else {
                    player.sendMessage(Main.getInstance().getConfiguration().getMessage("PlayerDoesNotExist"));
                }
            }
        }
        return false;
    }
}

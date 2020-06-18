package config;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.Plugin;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Sound;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.command.CommandExecutor;

public class msg implements CommandExecutor, Listener
{
    private int task;
    private int task1;
    
    public msg() {
        this.task = 0;
        this.task1 = 1;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args) {
        if (command.getName().equalsIgnoreCase("msg") || command.getName().equalsIgnoreCase("t") || command.getName().equalsIgnoreCase("tell")) {
            final Player player = Bukkit.getPlayer(sender.getName());
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("deny-messages.no-console")));
            }
            if (sender instanceof Player) {
                if (!sender.hasPermission("msg.use")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.no-permission")) + config.plugin.getConfig().getString("deny-messages.permission")));
                    return true;
                }
                if (sender.hasPermission("msg.use")) {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("usage.command")));
                        return true;
                    }
                    if (args.length == 1) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("usage.command")));
                        return true;
                    }
                    final String targetName = args[0];
                    final String message = this.getMessage(args, 1);
                    final Player target = Bukkit.getPlayer(targetName);
                    if (args.length >= 1 && (target == null || !target.isOnline())) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("deny-messages.msg-send")).replace("%t", String.valueOf(args[0])));
                        return false;
                    }
                    if (target == sender) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("deny-messages.self-message")));
                    }
                    if(config.plugin.getPlayerIsBlockedDatabase(sender, target))
                    {
                    	sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("deny-messages.player-blocked-me-message")));
                    	return false;
                    }
                    if (args.length >= 2 && target != sender) {
                        if (target != null) {
                            player.sendMessage(String.valueOf(ChatColor.translateAlternateColorCodes('&', new StringBuilder(String.valueOf(config.plugin.getConfig().getString("prefixes.msg"))).append(config.plugin.getConfig().getString("lay-out.sender").replace("%t", target.getName())).toString())) + message);
                            target.sendMessage(String.valueOf(ChatColor.translateAlternateColorCodes('&', new StringBuilder(String.valueOf(config.plugin.getConfig().getString("prefixes.msg"))).append(config.plugin.getConfig().getString("lay-out.receiver").replace("%s", player.getName())).toString())) + message);
                            target.playSound(target.getLocation(), Sound.valueOf(config.plugin.getConfig().getString("sounds.msg")), (float)config.plugin.getConfig().getInt("volume.msg"), (float)config.plugin.getConfig().getInt("pitch.msg"));
                            this.task1 = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)config.plugin, (Runnable)new Runnable() {
                                int counter = config.plugin.getConfig().getInt("duration.msg-actionbar");
                                
                                @Override
                                public void run() {
                                    --this.counter;
                                    target.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', config.plugin.getConfig().getString("actionbar.msg"))));
                                    if (this.counter <= 0) {
                                        Bukkit.getScheduler().cancelTask(msg.this.task1);
                                    }
                                }
                            }, 0L, 20L);
                            //return false;
                        }
                        if (config.lastMessageSent.containsKey(player)) {
                            config.lastMessageSent.remove(player);
                        }
                        config.lastMessageSent.put(target, player);
                    }
                }
            }
        }
        else if (command.getName().equalsIgnoreCase("reply")) {
            final Player player = Bukkit.getPlayer(sender.getName());
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("deny-messages.no-console")));
            }
            if (sender instanceof Player) {
                if (!sender.hasPermission("msg.use")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.no-permission")) + config.plugin.getConfig().getString("deny-messages.permission")));
                    return true;
                }
                if (sender.hasPermission("msg.use") && sender instanceof Player) {
                    final Player target2 = config.lastMessageSent.get(sender);
                    final String message = this.getMessage(args, 0);
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("usage.reply")));
                    }
                    if (args.length >= 1) {
                        if (target2 != null) {
                            if (target2.isOnline()) {
                                if(config.plugin.getPlayerIsBlockedDatabase(sender, target2))
                                {
                                	sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("deny-messages.player-blocked-me-reply")));
                                	return false;
                                }
//                              player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("messages.reply-success").replace("%t", target2.getName())));
                                player.sendMessage(String.valueOf(ChatColor.translateAlternateColorCodes('&', new StringBuilder(String.valueOf(config.plugin.getConfig().getString("prefixes.msg"))).append(config.plugin.getConfig().getString("lay-out.sender").replace("%t", target2.getName())).toString())) + message);
                                target2.sendMessage(String.valueOf(ChatColor.translateAlternateColorCodes('&', new StringBuilder(String.valueOf(config.plugin.getConfig().getString("prefixes.msg"))).append(config.plugin.getConfig().getString("lay-out.receiver").replace("%s", player.getName())).toString())) + message);
                                target2.playSound(target2.getLocation(), Sound.valueOf(config.plugin.getConfig().getString("sounds.reply")), (float)config.plugin.getConfig().getInt("volume.reply"), (float)config.plugin.getConfig().getInt("pitch.reply"));
                                config.lastMessageSent.put(target2, player);
                                this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)config.plugin, (Runnable)new Runnable() {
                                    int counter = config.plugin.getConfig().getInt("duration.reply-actionbar");
                                    
                                    @Override
                                    public void run() {
                                        --this.counter;
                                        target2.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', config.plugin.getConfig().getString("actionbar.reply"))));
                                        if (this.counter <= 0) {
                                            Bukkit.getScheduler().cancelTask(msg.this.task);
                                        }
                                    }
                                }, 0L, 20L);
                                return false;
                            }
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("deny-messages.reply-send")));
                        }
                        else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("deny-messages.reply")));
                        }
                    }
                }
            }
        }
        else if (command.getName().equalsIgnoreCase("msgblock")) {
            final Player player = Bukkit.getPlayer(sender.getName());
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("deny-messages.no-console")));
            }
            if (sender instanceof Player) {
                if (!sender.hasPermission("msg.use")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.no-permission")) + config.plugin.getConfig().getString("deny-messages.permission")));
                    return true;
                }
                if (sender.hasPermission("msg.use")) {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("usage.block")));
                        return true;
                    }
                    if (args.length >= 2) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("usage.block")));
                        return true;
                    }
                    final String targetName = args[0];
                    final Player target = Bukkit.getPlayer(targetName);
                    if (args.length >= 1 && (target == null || !target.isOnline())) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("deny-messages.block-send")).replace("%t", String.valueOf(args[0])));
                        return false;
                    }
                    if (target == sender) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("deny-messages.self-block")));
                    }
                    if (args.length == 1 && target != sender) {
                    	if(!config.plugin.putBlockDatabase(sender, target))
                    		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("block.already-blocked")));
                    	else
                    		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("block.success-blocked")).replace("%t", String.valueOf(args[0])));
//                    	if(config.plugin.getDatabase().getList(sender.getName().toLowerCase()).contains(target.getName().toLowerCase()))
                    }
                }
            }
        }
        else if (command.getName().equalsIgnoreCase("msgunblock")) {
        	final Player player = Bukkit.getPlayer(sender.getName());
        	if (!(sender instanceof Player)) {
        		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("deny-messages.no-console")));
        	}
        	if (sender instanceof Player) {
        		if (!sender.hasPermission("msg.use")) {
        			player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.no-permission")) + config.plugin.getConfig().getString("deny-messages.permission")));
        			return true;
        		}
        		if (sender.hasPermission("msg.use")) {
        			if (args.length == 0) {
        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("usage.unblock")));
        				return true;
        			}
        			if (args.length >= 2) {
        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("usage.unblock")));
        				return true;
        			}
        			final String targetName = args[0];
        			final Player target = Bukkit.getPlayer(targetName);
        			if (args.length >= 1 && (target == null || !target.isOnline())) {
        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("deny-messages.unblock-send")).replace("%t", String.valueOf(args[0])));
        				return false;
        			}
        			if (target == sender) {
        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("deny-messages.self-unblock")));
        			}
        			if (args.length == 1 && target != sender) {
        				if(!config.plugin.putUnBlockDatabase(sender, target))
        					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("unblock.already-unblocked")));
        				else
        					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.plugin.getConfig().getString("prefixes.msg")) + config.plugin.getConfig().getString("unblock.success-unblocked")).replace("%t", String.valueOf(args[0])));
//                    	if(config.plugin.getDatabase().getList(sender.getName().toLowerCase()).contains(target.getName().toLowerCase()))
        			}
        		}
        	}
        }
        return false;
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        final List<String> players = new ArrayList<String>();
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().startsWith(args[0])) {
                players.add(player.getName());
            }
        }
        return players;
    }
    
    private String getMessage(final String[] args, final int index) {
        final StringBuilder sb = new StringBuilder();
        for (int i = index; i < args.length; ++i) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString();
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        config.lastMessageSent.remove(e.getPlayer());
    }
}

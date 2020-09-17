package io.github.at.commands.home;

import io.github.at.api.ATTeleportEvent;
import io.github.at.config.Config;
import io.github.at.config.CustomMessages;
import io.github.at.config.Homes;
import io.github.at.events.MovementManager;
import io.github.at.main.CoreClass;
import io.github.at.utilities.DistanceLimiter;
import io.github.at.utilities.PaymentManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Home implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (Config.isFeatureEnabled("homes")) {
            if (sender.hasPermission("at.member.home")) {
                if (sender instanceof Player) {
                    Player player = (Player)sender;
                    String uuid = player.getUniqueId().toString();
                    HashMap<String, Location> homes = Homes.getHomes(uuid);
                    if (args.length>0) {
                        if (Bukkit.getOfflinePlayer(args[0]) != null) {
                            if (sender.hasPermission("at.admin.home")) {
                                if (args.length > 1) {
                                    uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString();
                                    homes = Homes.getHomes(uuid);
                                    try {
                                        if (homes.containsKey(args[1])) {
                                            Location tlocation = homes.get(args[1]);
                                            player.teleport(tlocation);
                                            sender.sendMessage(CustomMessages.getString("Teleport.teleportingToHomeOther")
                                                    .replaceAll("\\{player}", Bukkit.getOfflinePlayer(args[0]).getName())
                                                    .replaceAll("\\{home}", args[1]));
                                            return true;
                                        } else if (args[1].equalsIgnoreCase("bed")) {
                                            Location location = player.getBedSpawnLocation();
                                            if (location == null) {
                                                player.sendMessage(CustomMessages.getString("Error.noBedHomeOther").replaceAll("\\{player}", Bukkit.getOfflinePlayer(args[0]).getName()));
                                                return true;
                                            }

                                        } else if (args[1].equalsIgnoreCase("list")) {
                                            if (sender.hasPermission("at.admin.homes")) {
                                                StringBuilder hlist = new StringBuilder();
                                                hlist.append(CustomMessages.getString("Info.homesOther").replaceAll("\\{player}", player.getName()));
                                                if (Bukkit.getPlayer(args[0]) != null) {
                                                    homes = Homes.getHomes(uuid);
                                                    try {
                                                        if (homes.size()>0) {
                                                            for (String home: homes.keySet()) {
                                                                hlist.append(home + ", ");
                                                            }
                                                            hlist.setLength(hlist.length() - 2);
                                                        } else {
                                                            sender.sendMessage(CustomMessages.getString("Error.noHomesOther").replaceAll("\\{player}", player.getName()));
                                                            return true;
                                                        }

                                                    } catch (NullPointerException ex) {
                                                        sender.sendMessage(CustomMessages.getString("Error.noHomesOther").replaceAll("\\{player}", player.getName()));
                                                        return true;
                                                    }
                                                    sender.sendMessage(hlist.toString());
                                                    return true;
                                                }
                                            }
                                        } else {
                                            sender.sendMessage(CustomMessages.getString("Error.noSuchHome"));
                                            return true;
                                        }
                                    } catch (NullPointerException ex) {
                                        Location tlocation = Homes.getHomes(uuid).get(args[1]);
                                        player.teleport(tlocation);
                                        sender.sendMessage(CustomMessages.getString("Teleport.teleportingToHomeOther")
                                                .replaceAll("\\{player}", Bukkit.getOfflinePlayer(args[0]).getName().replaceAll("\\{home}", args[1])));
                                        return true;
                                    }
                                }
                            }
                        }
                        if (MovementManager.getMovement().containsKey(player.getUniqueId())) {
                            player.sendMessage(CustomMessages.getString("Error.onCountdown"));
                            return true;
                        }
                        if (PaymentManager.canPay("home", player)) {
                            try {
                                if (Homes.getHomes(uuid).containsKey(args[0])) {
                                    Location location = Homes.getHomes(uuid).get(args[0]);
                                    teleport(player, location, args[0]);
                                    return true;
                                } else if (args[0].equalsIgnoreCase("bed")) {
                                    Location location = player.getBedSpawnLocation();
                                    if (location == null) {
                                        player.sendMessage(CustomMessages.getString("Error.noBedHome"));
                                        return true;
                                    }
                                    teleport(player, location, args[0]);
                                    return true;

                                } else if (args[0].equalsIgnoreCase("list")) {
                                    Bukkit.dispatchCommand(sender, "homes");
                                } else {
                                    sender.sendMessage(CustomMessages.getString("Error.noSuchHome"));
                                }
                            } catch (NullPointerException ex) {
                                Location location = Homes.getHomes(uuid).get(args[0]);
                                teleport(player, location, args[0]);
                                return true;
                            }
                        }

                    } else {
                        if (homes.size() == 1) {
                            String name = homes.keySet().iterator().next();
                            teleport(player, homes.get(name), name);
                        } else {
                            sender.sendMessage(CustomMessages.getString("Error.noHomeInput"));
                            return true;
                        }
                    }
                } else {
                    sender.sendMessage(CustomMessages.getString("Error.notAPlayer"));
                }
            }
        } else {
            sender.sendMessage(CustomMessages.getString("Error.featureDisabled"));
            return true;
        }
        return true;
    }

    private void teleport(Player player, Location loc, String name) {
        if (!DistanceLimiter.canTeleport(player.getLocation(), loc, "home") && !player.hasPermission("at.admin.bypass.distance-limit")) {
            player.sendMessage(CustomMessages.getString("Error.tooFarAway"));
            return;
        }
        ATTeleportEvent event = new ATTeleportEvent(player, loc, player.getLocation(), name, ATTeleportEvent.TeleportType.HOME);
        if (!event.isCancelled()) {
            if (PaymentManager.canPay("home", player)) {
                if (Config.getTeleportTimer("home") > 0 && !player.hasPermission("at.admin.bypass.timer")) {
                    BukkitRunnable movementtimer = new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.sendMessage(CustomMessages.getString("Teleport.teleportingToHome").replaceAll("\\{home}",name));
                            player.teleport(loc);
                            MovementManager.getMovement().remove(player.getUniqueId());
                            PaymentManager.withdraw("home", player);
                        }
                    };
                    MovementManager.getMovement().put(player.getUniqueId(), movementtimer);
                    movementtimer.runTaskLater(CoreClass.getInstance(), Config.getTeleportTimer("home") * 20);
                    player.sendMessage(CustomMessages.getEventBeforeTPMessage().replaceAll("\\{countdown}", String.valueOf(Config.getTeleportTimer("home"))));

                } else {
                    player.sendMessage(CustomMessages.getString("Teleport.teleportingToHome").replaceAll("\\{home}",name));
                    player.teleport(loc);
                    PaymentManager.withdraw("home", player);
                }
            }
        }
    }
}

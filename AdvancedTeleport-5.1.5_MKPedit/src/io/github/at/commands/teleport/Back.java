package io.github.at.commands.teleport;

import io.github.at.api.ATTeleportEvent;
import io.github.at.config.Config;
import io.github.at.config.CustomMessages;
import io.github.at.config.LastLocations;
import io.github.at.events.MovementManager;
import io.github.at.events.TeleportTrackingManager;
import io.github.at.main.CoreClass;
import io.github.at.utilities.DistanceLimiter;
import io.github.at.utilities.PaymentManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Back implements CommandExecutor {

    private final List<String> airMaterials = new ArrayList<>(Arrays.asList("AIR", "WATER", "CAVE_AIR"));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (Config.isFeatureEnabled("teleport")) {
            if (sender.hasPermission("at.member.back")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    Location loc = TeleportTrackingManager.getLastLocation(player.getUniqueId());
                    if (loc == null) {
                        loc = LastLocations.getLocation(player);
                        if (loc == null) {
                            sender.sendMessage(CustomMessages.getString("Error.noLocation"));
                            return true;
                        }
                    }
                    double originalY = loc.getY();
                    while (!airMaterials.contains(loc.getBlock().getType().name())) {
                        // If we go beyond max height, stop and reset the Y value
                        if (loc.getY() > loc.getWorld().getMaxHeight()) {
                            loc.setY(originalY);
                            break;
                        }
                        loc.add(0.0, 1.0, 0.0);
                    }
                    ATTeleportEvent event = new ATTeleportEvent(player, player.getLocation(), loc, "back", ATTeleportEvent.TeleportType.BACK);
                    if (!event.isCancelled()) {
                        Location finalLoc = loc;
                        if (PaymentManager.canPay("back", player)) {
                            if (Config.getTeleportTimer("back") > 0 && !player.hasPermission("at.admin.bypass.timer")) {
                                BukkitRunnable movementtimer = new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        player.teleport(finalLoc);
                                        MovementManager.getMovement().remove(player.getUniqueId());
                                        player.sendMessage(CustomMessages.getString("Teleport.teleportingToLastLoc"));
                                        PaymentManager.withdraw("back", player);

                                    }
                                };
                                MovementManager.getMovement().put(player.getUniqueId(), movementtimer);
                                movementtimer.runTaskLater(CoreClass.getInstance(), Config.getTeleportTimer("back")*20);
                                player.sendMessage(CustomMessages.getEventBeforeTPMessage().replaceAll("\\{countdown}" , String.valueOf(Config.getTeleportTimer("back"))));

                            } else {
                                player.teleport(loc);
                                PaymentManager.withdraw("back", player);
                                player.sendMessage(CustomMessages.getString("Teleport.teleportingToLastLoc"));
                            }
                        }
                    }

                    if (!DistanceLimiter.canTeleport(player.getLocation(), loc, "back") && !player.hasPermission("at.admin.bypass.distance-limit")) {
                        player.sendMessage(CustomMessages.getString("Error.tooFarAway"));
                        return true;
                    }


                } else {
                    sender.sendMessage(CustomMessages.getString("Error.notAPlayer"));
                }
            } else {
                sender.sendMessage(CustomMessages.getString("Error.noPermission"));
                return true;
            }
        } else {
            sender.sendMessage(CustomMessages.getString("Error.featureDisabled"));
            return true;
        }
        return true;
    }
}

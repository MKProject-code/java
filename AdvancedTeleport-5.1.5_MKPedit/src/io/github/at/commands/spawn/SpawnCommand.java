package io.github.at.commands.spawn;

import io.github.at.api.ATTeleportEvent;
import io.github.at.config.Config;
import io.github.at.config.CustomMessages;
import io.github.at.config.Spawn;
import io.github.at.events.MovementManager;
import io.github.at.main.CoreClass;
import io.github.at.utilities.DistanceLimiter;
import io.github.at.utilities.PaymentManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (Config.isFeatureEnabled("spawn")) {
            if (sender.hasPermission("at.member.spawn")){
                if (MovementManager.getMovement().containsKey(sender)) {
                    sender.sendMessage(CustomMessages.getString("Error.onCountdown"));
                    return true;
                }
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    spawn(player);
                } else {
                    sender.sendMessage(CustomMessages.getString("Error.notAPlayer"));
                }
            }
        } else {
            sender.sendMessage(CustomMessages.getString("Error.featureDisabled"));
        }
        return true;
    }

    public static void spawn(Player player) {
        if (!DistanceLimiter.canTeleport(player.getLocation(), Spawn.getSpawnFile() != null ? Spawn.getSpawnFile() : player.getWorld().getSpawnLocation(), "spawn") && !player.hasPermission("at.admin.bypass.distance-limit")) {
            player.sendMessage(CustomMessages.getString("Error.tooFarAway"));
            return;
        }
        Location spawn;
        if (Spawn.getSpawnFile() != null) {
            spawn = Spawn.getSpawnFile();
        } else {
            spawn = player.getWorld().getSpawnLocation();
        }
        ATTeleportEvent event = new ATTeleportEvent(player, spawn, player.getLocation(), "spawn", ATTeleportEvent.TeleportType.SPAWN);
        if (!event.isCancelled()) {
            if (PaymentManager.canPay("spawn", player)) {
                if (Config.getTeleportTimer("spawn") > 0 && !player.hasPermission("at.admin.bypass.timer")) {
                    BukkitRunnable movementtimer = new BukkitRunnable() {
                        @Override
                        public void run() {
                            PaymentManager.withdraw("spawn", player);
                            player.teleport(spawn);
                            player.sendMessage(CustomMessages.getString("Teleport.teleportingToSpawn"));
                            MovementManager.getMovement().remove(player.getUniqueId());
                        }
                    };
                    MovementManager.getMovement().put(player.getUniqueId(), movementtimer);
                    movementtimer.runTaskLater(CoreClass.getInstance(), Config.getTeleportTimer("spawn") * 20);
                    player.sendMessage(CustomMessages.getEventBeforeTPMessage().replaceAll("\\{countdown}", String.valueOf(Config.getTeleportTimer("spawn"))));

                } else {
                    PaymentManager.withdraw("spawn", player);
                    player.teleport(spawn);
                    player.sendMessage(CustomMessages.getString("Teleport.teleportingToSpawn"));
                }
            }
        }
    }
}

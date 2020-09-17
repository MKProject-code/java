package io.github.at.events;

import io.github.at.config.Config;
import io.github.at.config.CustomMessages;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class MovementManager implements Listener {

    private static HashMap<UUID, BukkitRunnable> movement = new HashMap<>();

    @EventHandler
    public void onMovement(PlayerMoveEvent event) {
        if (!Config.cancelOnRotate()) {
            Location locTo = event.getTo();
            Location locFrom = event.getFrom();
            if (locTo.getBlockX() == locFrom.getBlockX() // If the player rotated instead of moved
                    && locTo.getBlockY() == locFrom.getBlockY()
                    && locTo.getBlockZ() == locTo.getBlockZ()) {
                return;
            }
        }
        UUID uuid = event.getPlayer().getUniqueId();
        if (Config.cancelOnMovement() && movement.containsKey(uuid)) {
            BukkitRunnable timer = movement.get(uuid);
            timer.cancel();
            event.getPlayer().sendMessage(CustomMessages.getString("Teleport.eventMovement"));
            movement.remove(uuid);
        }
    }

    public static HashMap<UUID, BukkitRunnable> getMovement() {
        return movement;
    }
}

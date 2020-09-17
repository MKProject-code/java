package io.github.at.events;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {

    private static HashMap<UUID, BukkitRunnable> cooldown = new HashMap<>();

    public static HashMap<UUID, BukkitRunnable> getCooldown() {
        return cooldown;
    }
}

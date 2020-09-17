package io.github.at.config;

import io.github.at.events.TeleportTrackingManager;
import io.github.at.main.CoreClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class LastLocations {
    public static File configFile = new File(CoreClass.getInstance().getDataFolder(),"last-locations.yml");
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

    public static void save() throws IOException {
        config.save(configFile);
    }

    public static void saveLocations() {
        for (UUID uuid : TeleportTrackingManager.getLastLocations().keySet()) {
            try {
                Location loc = TeleportTrackingManager.getLastLocation(uuid);
                // Format: player-uuid: x.y.z.yaw.pitch.world
                config.set(uuid.toString(),
                        loc.getX() + ":"
                                + loc.getY() + ":"
                                + loc.getZ() + ":"
                                + loc.getYaw() + ":"
                                + loc.getPitch() + ":"
                                + loc.getWorld().getName());
            } catch (NullPointerException ignored) { // Null location, no idea what causes it
            }

        }
        for (UUID uuid : TeleportTrackingManager.getDeathLocations().keySet()) {
            try {
                Location loc = TeleportTrackingManager.getDeathLocation(uuid);
                config.set("death." + uuid.toString(),  loc.getX() + ":"
                        + loc.getY() + ":"
                        + loc.getZ() + ":"
                        + loc.getYaw() + ":"
                        + loc.getPitch() + ":"
                        + loc.getWorld().getName());
            } catch (NullPointerException ignored) { // Means that the player never teleported

            }

        }
        config.options().copyDefaults(true);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Location getLocation(Player player) {
        try {
            String[] loc = config.getString(player.getUniqueId().toString()).split(":");
            return new Location(Bukkit.getWorld(loc[5]), Double.valueOf(loc[0]), Double.valueOf(loc[1]), Double.valueOf(loc[2]), Float.valueOf(loc[3]), Float.valueOf(loc[4]));
        } catch (Exception e) {
            return null;
        }

    }

    public static Location getDeathLocation(Player player) {
        try {
            String[] loc = config.getString("death." + player.getUniqueId().toString()).split(":");
            return new Location(Bukkit.getWorld(loc[5]), Double.valueOf(loc[0]), Double.valueOf(loc[1]), Double.valueOf(loc[2]), Float.valueOf(loc[3]), Float.valueOf(loc[4]));
        } catch (Exception e) {
            return null;
        }
    }

    public static void deleteDeathLocation(Player player) {
        config.set("death." + player.getUniqueId().toString(), null);
        config.options().copyDefaults(true);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadBackLocations() throws IOException {
        if (configFile == null) {
            configFile = new File(CoreClass.getInstance().getDataFolder(), "last-locations.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        save();
    }
}

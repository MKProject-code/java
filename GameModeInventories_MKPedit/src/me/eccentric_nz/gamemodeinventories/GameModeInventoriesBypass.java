package me.eccentric_nz.gamemodeinventories;

import org.bukkit.entity.Player;

public class GameModeInventoriesBypass {
   public static boolean canBypass(Player p, String bypass, GameModeInventories plugin) {
      return !p.hasPermission("gamemodeinventories.bypass") && !p.isOp() ? false : plugin.getConfig().getBoolean("bypass." + bypass);
   }
}

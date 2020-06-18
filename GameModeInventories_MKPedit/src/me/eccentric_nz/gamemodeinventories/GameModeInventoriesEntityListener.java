package me.eccentric_nz.gamemodeinventories;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class GameModeInventoriesEntityListener implements Listener {
   private final GameModeInventories plugin;

   public GameModeInventoriesEntityListener(GameModeInventories plugin) {
      this.plugin = plugin;
   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onArmorStandPlace(CreatureSpawnEvent event) {
      if (this.plugin.getConfig().getBoolean("track_creative_place.enabled")) {
         if (event.getEntityType().equals(EntityType.ARMOR_STAND)) {
            Location l = event.getLocation();
            if (!this.plugin.getConfig().getStringList("track_creative_place.worlds").contains(l.getWorld().getName())) {
               return;
            }

            List<String> locs = Arrays.asList(l.getBlockX() + "," + l.getBlockZ(), l.getBlockX() + "," + (l.getBlockZ() - 1), l.getBlockX() - 1 + "," + l.getBlockZ(), l.getBlockX() + "," + (l.getBlockZ() + 1), l.getBlockX() + 1 + "," + l.getBlockZ());
            Iterator var4 = locs.iterator();

            while(var4.hasNext()) {
               String p = (String)var4.next();
               if (this.plugin.getPoints().contains(p)) {
                  this.plugin.getStands().add(event.getEntity().getUniqueId());
                  this.plugin.getPoints().remove(p);
                  break;
               }
            }
         }

      }
   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onArmorStandBreakOrCreativePVP(EntityDamageByEntityEvent event) {
      if (this.plugin.getConfig().getBoolean("no_creative_pvp") && event.getEntityType().equals(EntityType.PLAYER)) {
         Entity attacker = event.getDamager();
         if (attacker instanceof Player && ((Player)attacker).getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
            return;
         }
      }

      if (this.plugin.getConfig().getBoolean("track_creative_place.enabled") && event.getEntityType().equals(EntityType.ARMOR_STAND) && event.getEntity().getLastDamageCause() != null && this.plugin.getStands().contains(event.getEntity().getUniqueId())) {
         event.setCancelled(true);
         String message = (String)this.plugin.getM().getMessage().get("NO_CREATIVE_BREAK");
         if (this.plugin.getConfig().getBoolean("track_creative_place.break_no_drop")) {
            event.getEntity().remove();
            message = (String)this.plugin.getM().getMessage().get("NO_CREATIVE_DROPS");
         }

         if (!this.plugin.getConfig().getBoolean("dont_spam_chat")) {
            Entity damager = event.getDamager();
            if (damager instanceof Player) {
               Player p = (Player)damager;
               p.sendMessage(this.plugin.MY_PLUGIN_NAME + message);
            }
         }
      }

   }
}

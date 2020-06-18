package me.eccentric_nz.gamemodeinventories;

import java.util.Iterator;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GameModeInventoriesBlockListener implements Listener {
   private final GameModeInventories plugin;

   public GameModeInventoriesBlockListener(GameModeInventories plugin) {
      this.plugin = plugin;
   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onItemSpawn(ItemSpawnEvent event) {
      if (this.plugin.getConfig().getBoolean("no_falling_drops")) {
         event.getEntity().getNearbyEntities(0.5D, 0.5D, 0.5D).forEach((e) -> {
            if (e instanceof FallingBlock) {
               event.setCancelled(true);
            }

         });
      }
   }

   @EventHandler
   public void onPlayerInteract(PlayerInteractEvent event) {
      Player p = event.getPlayer();
      if (p.getGameMode().equals(GameMode.CREATIVE)) {
         if (event.hasItem()) {
            if (this.plugin.getConfig().getBoolean("track_creative_place.enabled") && event.getItem().getType().equals(Material.ARMOR_STAND)) {
               Block b = event.getClickedBlock();
               if (b != null) {
                  if (!this.plugin.getConfig().getStringList("track_creative_place.worlds").contains(b.getWorld().getName())) {
                     return;
                  }

                  Location l = b.getLocation();
                  if (l != null) {
                     String gmip = l.getBlockX() + "," + l.getBlockZ();
                     this.plugin.getPoints().add(gmip);
                     this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                        if (this.plugin.getPoints().contains(gmip)) {
                           this.plugin.getPoints().remove(gmip);
                        }

                     }, 600L);
                  }
               }
            }

            if (!this.plugin.getConfig().getBoolean("creative_blacklist")) {
               return;
            }

            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
               Material mat = event.getItem().getType();
               if (this.plugin.getBlackList().contains(mat) && !GameModeInventoriesBypass.canBypass(p, "blacklist", this.plugin)) {
                  event.setCancelled(true);
                  event.setUseItemInHand(Result.DENY);
                  if (!this.plugin.getConfig().getBoolean("dont_spam_chat")) {
                     p.sendMessage(this.plugin.MY_PLUGIN_NAME + String.format((String)this.plugin.getM().getMessage().get("NO_CREATIVE_PLACE"), mat.toString()));
                  }
               }
            }
         }

      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onBedrockBreak(BlockBreakEvent event) {
      if (!this.plugin.getConfig().getBoolean("break_bedrock")) {
         if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            Block br = event.getBlock();
            if (br.getType().equals(Material.BEDROCK)) {
               if (br.getLocation().getY() < 5.0D) {
                  event.setCancelled(true);
               } else if (br.getWorld().getEnvironment().equals(Environment.NETHER) && br.getLocation().getY() > 122.0D) {
                  event.setCancelled(true);
               }

            }
         }
      }
   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onEntityExplode(EntityExplodeEvent event) {
      if (this.plugin.getConfig().getBoolean("track_creative_place.enabled")) {
         if (!this.plugin.getConfig().getStringList("track_creative_place.worlds").contains(event.getLocation().getWorld().getName())) {
            return;
         }

         Iterator var2 = event.blockList().iterator();

         while(var2.hasNext()) {
            Block b = (Block)var2.next();
            if (!this.plugin.getNoTrackList().contains(b.getType())) {
               String gmiwc = b.getWorld().getName() + "," + b.getChunk().getX() + "," + b.getChunk().getZ();
               if (this.plugin.getCreativeBlocks().containsKey(gmiwc) && ((List)this.plugin.getCreativeBlocks().get(gmiwc)).contains(b.getLocation().toString())) {
                  event.setYield(0.0F);
                  event.setCancelled(true);
                  return;
               }
            }
         }
      }

   }
}

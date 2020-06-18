package me.eccentric_nz.gamemodeinventories;

import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class GameModeInventoriesTrackBlackListener implements Listener {
   private final GameModeInventories plugin;

   public GameModeInventoriesTrackBlackListener(GameModeInventories plugin) {
      this.plugin = plugin;
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onBlockBreak(BlockBreakEvent event) {
      if (this.plugin.getConfig().getBoolean("track_creative_place.enabled")) {
         Player p = event.getPlayer();
         Block block = event.getBlock();
         if (!block.getType().equals(Material.AIR)) {
            if (this.plugin.getConfig().getStringList("track_creative_place.worlds").contains(block.getWorld().getName())) {
               if (!this.plugin.getNoTrackList().contains(block.getType())) {
                  String gmiwc = block.getWorld().getName() + "," + block.getChunk().getX() + "," + block.getChunk().getZ();
                  if (this.plugin.getCreativeBlocks().containsKey(gmiwc)) {
                     if (((List)this.plugin.getCreativeBlocks().get(gmiwc)).contains(block.getLocation().toString())) {
                        if (p.getGameMode().equals(GameMode.CREATIVE)) {
                           this.plugin.getBlock().removeBlock(gmiwc, block.getLocation().toString());
                        } else {
                           String message;
                           if (this.plugin.getConfig().getBoolean("track_creative_place.break_no_drop")) {
                              this.plugin.getBlock().removeBlock(gmiwc, block.getLocation().toString());
                              if (this.plugin.getBlockLogger().isLogging()) {
                                 Location loc = block.getLocation();
                                 String pname = p.getName();
                                 switch(this.plugin.getBlockLogger().getWhichLogger()) {
                                 case CORE_PROTECT:
                                    Material type = block.getType();
                                    BlockData data = block.getBlockData();
                                    this.plugin.getBlockLogger().getCoreProtectAPI().logRemoval(pname, loc, type, data);
                                    break;
                                 case LOG_BLOCK:
                                    this.plugin.getBlockLogger().getLogBlockConsumer().queueBlockBreak(pname, block.getState());
                                    break;
                                 case PRISM:
                                    if (this.plugin.getBlockLogger().getPrism() != null) {
                                       GameModeInventoriesPrismHandler.log(loc, block, pname);
                                    }
                                 }
                              }

                              block.setType(Material.AIR);
                              block.getDrops().clear();
                              message = (String)this.plugin.getM().getMessage().get("NO_CREATIVE_DROPS");
                           } else {
                              event.setCancelled(true);
                              message = (String)this.plugin.getM().getMessage().get("NO_CREATIVE_BREAK");
                           }

                           if (!this.plugin.getConfig().getBoolean("dont_spam_chat")) {
                              p.sendMessage(this.plugin.MY_PLUGIN_NAME + message);
                           }
                        }
                     }

                  }
               }
            }
         }
      }
   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onBlockPlace(BlockPlaceEvent event) {
      Player p = event.getPlayer();
      if (p.getGameMode().equals(GameMode.CREATIVE)) {
         Material mat = event.getBlock().getType();
         if (this.plugin.getConfig().getBoolean("creative_blacklist") && this.plugin.getBlackList().contains(mat) && !GameModeInventoriesBypass.canBypass(p, "blacklist", this.plugin)) {
            event.setCancelled(true);
            if (!this.plugin.getConfig().getBoolean("dont_spam_chat")) {
               p.sendMessage(this.plugin.MY_PLUGIN_NAME + String.format((String)this.plugin.getM().getMessage().get("NO_CREATIVE_PLACE"), mat.toString()));
            }

         } else {
            if (this.plugin.getConfig().getBoolean("track_creative_place.enabled")) {
               Block block = event.getBlock();
               if (!this.plugin.getConfig().getStringList("track_creative_place.worlds").contains(block.getWorld().getName())) {
                  return;
               }

               if (this.plugin.getNoTrackList().contains(mat)) {
                  return;
               }

               String gmiwc = block.getWorld().getName() + "," + block.getChunk().getX() + "," + block.getChunk().getZ();
               if (!this.plugin.getCreativeBlocks().containsKey(gmiwc) || !((List)this.plugin.getCreativeBlocks().get(gmiwc)).contains(block.getLocation().toString())) {
                  this.plugin.getBlock().addBlock(gmiwc, block.getLocation().toString());
               }
            }

         }
      }
   }
}

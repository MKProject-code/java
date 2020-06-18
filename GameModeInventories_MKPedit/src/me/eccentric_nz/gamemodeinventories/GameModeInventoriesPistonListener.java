package me.eccentric_nz.gamemodeinventories;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class GameModeInventoriesPistonListener implements Listener {
   private final GameModeInventories plugin;
   private final List<Material> wouldDrop = new ArrayList();

   public GameModeInventoriesPistonListener(GameModeInventories plugin) {
      this.plugin = plugin;
      this.wouldDrop.add(Material.ACACIA_DOOR);
      this.wouldDrop.add(Material.ACACIA_PRESSURE_PLATE);
      this.wouldDrop.add(Material.ACACIA_SAPLING);
      this.wouldDrop.add(Material.ACACIA_TRAPDOOR);
      this.wouldDrop.add(Material.ALLIUM);
      this.wouldDrop.add(Material.AZURE_BLUET);
      this.wouldDrop.add(Material.BIRCH_DOOR);
      this.wouldDrop.add(Material.BIRCH_PRESSURE_PLATE);
      this.wouldDrop.add(Material.BIRCH_SAPLING);
      this.wouldDrop.add(Material.BIRCH_TRAPDOOR);
      this.wouldDrop.add(Material.BLACK_BED);
      this.wouldDrop.add(Material.BLUE_BED);
      this.wouldDrop.add(Material.BLUE_ORCHID);
      this.wouldDrop.add(Material.BROWN_BED);
      this.wouldDrop.add(Material.BROWN_MUSHROOM);
      this.wouldDrop.add(Material.COBWEB);
      this.wouldDrop.add(Material.CYAN_BED);
      this.wouldDrop.add(Material.DANDELION);
      this.wouldDrop.add(Material.DARK_OAK_DOOR);
      this.wouldDrop.add(Material.DARK_OAK_PRESSURE_PLATE);
      this.wouldDrop.add(Material.DARK_OAK_SAPLING);
      this.wouldDrop.add(Material.DARK_OAK_TRAPDOOR);
      this.wouldDrop.add(Material.DEAD_BUSH);
      this.wouldDrop.add(Material.FERN);
      this.wouldDrop.add(Material.GRASS);
      this.wouldDrop.add(Material.GRAY_BED);
      this.wouldDrop.add(Material.GREEN_BED);
      this.wouldDrop.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
      this.wouldDrop.add(Material.IRON_DOOR);
      this.wouldDrop.add(Material.IRON_TRAPDOOR);
      this.wouldDrop.add(Material.JACK_O_LANTERN);
      this.wouldDrop.add(Material.JUNGLE_DOOR);
      this.wouldDrop.add(Material.JUNGLE_PRESSURE_PLATE);
      this.wouldDrop.add(Material.JUNGLE_SAPLING);
      this.wouldDrop.add(Material.JUNGLE_TRAPDOOR);
      this.wouldDrop.add(Material.LADDER);
      this.wouldDrop.add(Material.LARGE_FERN);
      this.wouldDrop.add(Material.LEVER);
      this.wouldDrop.add(Material.LIGHT_BLUE_BED);
      this.wouldDrop.add(Material.LIGHT_GRAY_BED);
      this.wouldDrop.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
      this.wouldDrop.add(Material.LILAC);
      this.wouldDrop.add(Material.LILY_PAD);
      this.wouldDrop.add(Material.LIME_BED);
      this.wouldDrop.add(Material.MAGENTA_BED);
      this.wouldDrop.add(Material.MELON);
      this.wouldDrop.add(Material.OAK_DOOR);
      this.wouldDrop.add(Material.OAK_PRESSURE_PLATE);
      this.wouldDrop.add(Material.OAK_SAPLING);
      this.wouldDrop.add(Material.OAK_TRAPDOOR);
      this.wouldDrop.add(Material.ORANGE_BED);
      this.wouldDrop.add(Material.ORANGE_TULIP);
      this.wouldDrop.add(Material.OXEYE_DAISY);
      this.wouldDrop.add(Material.PEONY);
      this.wouldDrop.add(Material.PINK_BED);
      this.wouldDrop.add(Material.PINK_TULIP);
      this.wouldDrop.add(Material.POPPY);
      this.wouldDrop.add(Material.PUMPKIN);
      this.wouldDrop.add(Material.PURPLE_BED);
      this.wouldDrop.add(Material.REDSTONE);
      this.wouldDrop.add(Material.REDSTONE_TORCH);
      this.wouldDrop.add(Material.REDSTONE_WALL_TORCH);
      this.wouldDrop.add(Material.REDSTONE_WIRE);
      this.wouldDrop.add(Material.RED_BED);
      this.wouldDrop.add(Material.RED_MUSHROOM);
      this.wouldDrop.add(Material.RED_TULIP);
      this.wouldDrop.add(Material.REPEATER);
      this.wouldDrop.add(Material.ROSE_BUSH);
      this.wouldDrop.add(Material.SPRUCE_DOOR);
      this.wouldDrop.add(Material.SPRUCE_PRESSURE_PLATE);
      this.wouldDrop.add(Material.SPRUCE_SAPLING);
      this.wouldDrop.add(Material.SPRUCE_TRAPDOOR);
      this.wouldDrop.add(Material.STONE_BUTTON);
      this.wouldDrop.add(Material.STONE_PRESSURE_PLATE);
      this.wouldDrop.add(Material.SUNFLOWER);
      this.wouldDrop.add(Material.TALL_GRASS);
      this.wouldDrop.add(Material.TORCH);
      this.wouldDrop.add(Material.TRIPWIRE_HOOK);
      this.wouldDrop.add(Material.WHITE_BED);
      this.wouldDrop.add(Material.WHITE_TULIP);
      this.wouldDrop.add(Material.YELLOW_BED);
   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onPistonExtend(BlockPistonExtendEvent event) {
      if (this.plugin.getConfig().getBoolean("track_creative_place.enabled")) {
         if (this.plugin.getConfig().getBoolean("track_creative_place.no_piston_move")) {
            Block block = event.getBlock();
            if (!this.plugin.getConfig().getStringList("track_creative_place.worlds").contains(block.getWorld().getName())) {
               return;
            }

            Iterator var3 = event.getBlocks().iterator();

            while(var3.hasNext()) {
               Block b = (Block)var3.next();
               String gmiwc = block.getWorld().getName() + "," + block.getChunk().getX() + "," + block.getChunk().getZ();
               if (!this.plugin.getCreativeBlocks().containsKey(gmiwc)) {
                  return;
               }

               if (((List)this.plugin.getCreativeBlocks().get(gmiwc)).contains(b.getLocation().toString())) {
                  if (this.wouldDrop.contains(b.getType())) {
                     event.setCancelled(true);
                     this.plugin.debug("Cancelled piston extension because one of the moved blocks would drop an item");
                     return;
                  }

                  if (((List)this.plugin.getCreativeBlocks().get(gmiwc)).contains(block.getLocation().toString())) {
                     ((List)this.plugin.getCreativeBlocks().get(gmiwc)).remove(b.getLocation().toString());
                     ((List)this.plugin.getCreativeBlocks().get(gmiwc)).add(b.getRelative(event.getDirection()).getLocation().toString());
                  } else {
                     event.setCancelled(true);
                     this.plugin.debug("Cancelled piston extension because one of the moved blocks was a CREATIVE placed block");
                  }
               }
            }
         }

      }
   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onPistonRetract(BlockPistonRetractEvent event) {
      if (event.isSticky()) {
         if (this.plugin.getConfig().getBoolean("track_creative_place.enabled")) {
            Block block = event.getBlock();
            if (this.plugin.getConfig().getStringList("track_creative_place.worlds").contains(block.getWorld().getName())) {
               String gmiwc = block.getWorld().getName() + "," + block.getChunk().getX() + "," + block.getChunk().getZ();
               if (this.plugin.getCreativeBlocks().containsKey(gmiwc)) {
                  if (((List)this.plugin.getCreativeBlocks().get(gmiwc)).contains(block.getRelative(event.getDirection(), 2).getLocation().toString())) {
                     if (((List)this.plugin.getCreativeBlocks().get(gmiwc)).contains(block.getLocation().toString())) {
                        ((List)this.plugin.getCreativeBlocks().get(gmiwc)).remove(block.getRelative(event.getDirection(), 2).getLocation().toString());
                        ((List)this.plugin.getCreativeBlocks().get(gmiwc)).add(block.getRelative(event.getDirection()).getLocation().toString());
                     } else {
                        event.setCancelled(true);
                        this.plugin.debug("Cancelled piston retraction because the moved block was a CREATIVE placed block");
                     }
                  }

               }
            }
         }
      }
   }
}

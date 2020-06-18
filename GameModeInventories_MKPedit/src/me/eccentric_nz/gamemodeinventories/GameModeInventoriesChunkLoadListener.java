package me.eccentric_nz.gamemodeinventories;

import me.eccentric_nz.gamemodeinventories.database.GameModeInventoriesBlockLoader;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class GameModeInventoriesChunkLoadListener implements Listener {
   private final GameModeInventories plugin;
   private boolean firstLogin = true;

   public GameModeInventoriesChunkLoadListener(GameModeInventories plugin) {
      this.plugin = plugin;
   }

   @EventHandler
   public void onChunkLoad(ChunkLoadEvent event) {
      if (this.plugin.getConfig().getBoolean("track_creative_place.enabled")) {
         Chunk chunk = event.getChunk();
         String gmiwc = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
         if (!this.plugin.getCreativeBlocks().containsKey(gmiwc)) {
            (new GameModeInventoriesBlockLoader(this.plugin, gmiwc)).runTaskAsynchronously(this.plugin);
         }

      }
   }

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event) {
      if (this.plugin.getConfig().getBoolean("track_creative_place.enabled")) {
         if (this.firstLogin) {
            this.firstLogin = false;
            Chunk[] chunks = event.getPlayer().getWorld().getLoadedChunks();
            Chunk[] var3 = chunks;
            int var4 = chunks.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Chunk c = var3[var5];
               String gmiwc = c.getWorld().getName() + "," + c.getX() + "," + c.getZ();
               if (!this.plugin.getCreativeBlocks().containsKey(gmiwc)) {
                  (new GameModeInventoriesBlockLoader(this.plugin, gmiwc)).runTaskAsynchronously(this.plugin);
               }
            }
         } else {
            Chunk chunk = event.getPlayer().getLocation().getChunk();
            String gmiwc = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
            if (!this.plugin.getCreativeBlocks().containsKey(gmiwc)) {
               this.plugin.debug(gmiwc, GMIDebug.ALL);
               (new GameModeInventoriesBlockLoader(this.plugin, gmiwc)).runTaskAsynchronously(this.plugin);
            }
         }

      }
   }
}

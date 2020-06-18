package me.eccentric_nz.gamemodeinventories.database;

public class GameModeInventoriesQueueData {
   private final String worldchunk;
   private final String location;

   public GameModeInventoriesQueueData(String worldchunk, String location) {
      this.worldchunk = worldchunk;
      this.location = location;
   }

   public String getWorldChunk() {
      return this.worldchunk;
   }

   public String getLocation() {
      return this.location;
   }
}

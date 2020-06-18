package me.eccentric_nz.gamemodeinventories.database;

import java.util.concurrent.LinkedBlockingQueue;

public class GameModeInventoriesRecordingQueue {
   private static final LinkedBlockingQueue<GameModeInventoriesQueueData> QUEUE = new LinkedBlockingQueue();

   public static int getQueueSize() {
      return QUEUE.size();
   }

   public static void addToQueue(GameModeInventoriesQueueData data) {
      if (data != null) {
         QUEUE.add(data);
      }
   }

   public static LinkedBlockingQueue<GameModeInventoriesQueueData> getQUEUE() {
      return QUEUE;
   }
}

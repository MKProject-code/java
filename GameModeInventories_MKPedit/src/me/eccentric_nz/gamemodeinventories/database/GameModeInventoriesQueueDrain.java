package me.eccentric_nz.gamemodeinventories.database;

import me.eccentric_nz.gamemodeinventories.GMIDebug;
import me.eccentric_nz.gamemodeinventories.GameModeInventories;

public class GameModeInventoriesQueueDrain {
   private final GameModeInventories plugin;

   public GameModeInventoriesQueueDrain(GameModeInventories plugin) {
      this.plugin = plugin;
   }

   public void forceDrainQueue() {
      this.plugin.debug("Forcing recorder queue to run a new batch before shutdown...", GMIDebug.INFO);
      GameModeInventoriesRecordingTask recorderTask = new GameModeInventoriesRecordingTask(this.plugin);

      while(!GameModeInventoriesRecordingQueue.getQUEUE().isEmpty()) {
         this.plugin.debug("Starting drain batch...", GMIDebug.INFO);
         this.plugin.debug("Current queue size: " + GameModeInventoriesRecordingQueue.getQUEUE().size(), GMIDebug.INFO);

         try {
            recorderTask.insertIntoDatabase();
         } catch (Exception var3) {
            this.plugin.debug("Stopping queue drain due to caught exception. Queue items lost: " + GameModeInventoriesRecordingQueue.getQUEUE().size(), GMIDebug.INFO);
            break;
         }

         if (GameModeInventoriesRecordingManager.failedDbConnectionCount > 0) {
            this.plugin.debug("Stopping queue drain due to detected database error. Queue items lost: " + GameModeInventoriesRecordingQueue.getQUEUE().size(), GMIDebug.INFO);
         }
      }

   }
}

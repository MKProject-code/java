package me.eccentric_nz.gamemodeinventories.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import me.eccentric_nz.gamemodeinventories.GMIDebug;
import me.eccentric_nz.gamemodeinventories.GameModeInventories;

public class GameModeInventoriesRecordingTask implements Runnable {
   private final GameModeInventories plugin;

   public GameModeInventoriesRecordingTask(GameModeInventories plugin) {
      this.plugin = plugin;
   }

   public void save() {
      if (!GameModeInventoriesRecordingQueue.getQUEUE().isEmpty()) {
         this.insertIntoDatabase();
      }

   }

   public void insertIntoDatabase() {
      PreparedStatement s = null;
      Connection conn = null;

      try {
         int perBatch = 1000;
         if (GameModeInventoriesRecordingQueue.getQUEUE().isEmpty()) {
            return;
         }

         this.plugin.debug("Beginning batch insert from queue. " + System.currentTimeMillis(), GMIDebug.INFO);
         conn = GameModeInventoriesConnectionPool.dbc();
         if (conn != null && !conn.isClosed()) {
            GameModeInventoriesRecordingManager.failedDbConnectionCount = 0;
            conn.setAutoCommit(false);
            s = conn.prepareStatement("INSERT INTO blocks (worldchunk,location) VALUES (?,?)");

            for(int i = 0; !GameModeInventoriesRecordingQueue.getQUEUE().isEmpty(); ++i) {
               if (conn.isClosed()) {
                  this.plugin.debug("GMI database error. We have to bail in the middle of building primary bulk insert query.", GMIDebug.ERROR);
                  break;
               }

               GameModeInventoriesQueueData a = (GameModeInventoriesQueueData)GameModeInventoriesRecordingQueue.getQUEUE().poll();
               if (a == null) {
                  break;
               }

               s.setString(1, a.getWorldChunk());
               s.setString(2, a.getLocation());
               s.addBatch();
               if (i >= perBatch) {
                  this.plugin.debug("Recorder: Batch max exceeded, running insert. Queue remaining: " + GameModeInventoriesRecordingQueue.getQUEUE().size(), GMIDebug.INFO);
                  break;
               }
            }

            s.executeBatch();
            if (conn.isClosed()) {
               this.plugin.debug("GMI database error. We have to bail in the middle of building primary bulk insert query.", GMIDebug.ERROR);
            } else {
               conn.commit();
               conn.setAutoCommit(true);
               this.plugin.debug("Batch insert was committed: " + System.currentTimeMillis(), GMIDebug.INFO);
            }

            return;
         }

         if (GameModeInventoriesRecordingManager.failedDbConnectionCount == 0) {
            this.plugin.debug("GMI database error. Connection should be there but it's not. Leaving actions to log in queue.", GMIDebug.INFO);
         }

         ++GameModeInventoriesRecordingManager.failedDbConnectionCount;
         if (GameModeInventoriesRecordingManager.failedDbConnectionCount > 5) {
            this.plugin.debug("Too many problems connecting. Giving up for a bit.", GMIDebug.INFO);
            this.scheduleNextRecording();
         }

         this.plugin.debug("Database connection still missing, incrementing count.", GMIDebug.INFO);
      } catch (SQLException var15) {
         this.plugin.debug("SQL error: " + var15.getMessage(), GMIDebug.ERROR);
         return;
      } finally {
         try {
            if (s != null) {
               s.close();
            }

            if (conn != null && GameModeInventoriesConnectionPool.isIsMySQL()) {
               conn.close();
            }
         } catch (SQLException var14) {
         }

      }

   }

   public void run() {
      this.save();
      this.scheduleNextRecording();
   }

   protected int getTickDelayForNextBatch() {
      return GameModeInventoriesRecordingManager.failedDbConnectionCount > 5 ? GameModeInventoriesRecordingManager.failedDbConnectionCount * 20 : 3;
   }

   protected void scheduleNextRecording() {
      if (!this.plugin.isEnabled()) {
         this.plugin.debug("Can't schedule new recording tasks as plugin is now disabled. If you're shutting down the server, ignore me.", GMIDebug.INFO);
      } else {
         this.plugin.recordingTask = this.plugin.getServer().getScheduler().runTaskLaterAsynchronously(this.plugin, new GameModeInventoriesRecordingTask(this.plugin), (long)this.getTickDelayForNextBatch());
      }
   }
}

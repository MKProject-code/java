package me.eccentric_nz.gamemodeinventories;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class GameModeInventoriesMessage {
   private final GameModeInventories plugin;
   private final HashMap<String, String> message = new HashMap();
   private FileConfiguration messagesConfig = null;
   private File messagesFile = null;

   public GameModeInventoriesMessage(GameModeInventories plugin) {
      this.plugin = plugin;
      this.messagesFile = this.getMessagesFile();
      this.messagesConfig = YamlConfiguration.loadConfiguration(this.messagesFile);
   }

   public void getMessages() {
      this.messagesConfig.getKeys(false).forEach((m) -> {
         this.message.put(m, this.messagesConfig.getString(m));
      });
   }

   public HashMap<String, String> getMessage() {
      return this.message;
   }

   private File getMessagesFile() {
      File file = new File(this.plugin.getDataFolder(), "messages.yml");
      InputStream in = this.plugin.getResource("messages.yml");
      if (!file.exists()) {
         try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];

            try {
               int len;
               try {
                  while((len = in.read(buf)) > 0) {
                     out.write(buf, 0, len);
                  }
               } catch (IOException var31) {
                  System.err.println("[GameModeInventories] Could not save the file (" + file.toString() + ").");
               }
            } finally {
               try {
                  out.close();
               } catch (IOException var30) {
               }

            }
         } catch (FileNotFoundException var33) {
            System.err.println("[GameModeInventories] File not found.");
         } finally {
            if (in != null) {
               try {
                  in.close();
               } catch (IOException var29) {
               }
            }

         }
      }

      return file;
   }

   public void updateMessages() {
      int i = 0;
      if (!this.messagesConfig.contains("NO_SPECTATOR")) {
         this.messagesConfig.set("NO_SPECTATOR", "You are not allowed to be a SPECTATOR!");
         ++i;
      }

      if (!this.messagesConfig.contains("INVALID_MATERIAL_TRACK")) {
         this.messagesConfig.set("INVALID_MATERIAL_TRACK", "Invalid material in dont_track list");
         ++i;
      }

      if (i > 0) {
         try {
            this.messagesConfig.save(new File(this.plugin.getDataFolder(), "messages.yml"));
            this.plugin.getServer().getConsoleSender().sendMessage(this.plugin.MY_PLUGIN_NAME + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to messages.yml");
         } catch (IOException var3) {
            this.plugin.debug("Could not save messages.yml, " + var3);
         }
      }

   }
}

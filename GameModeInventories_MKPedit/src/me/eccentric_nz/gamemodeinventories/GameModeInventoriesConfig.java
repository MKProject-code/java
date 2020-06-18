package me.eccentric_nz.gamemodeinventories;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class GameModeInventoriesConfig {
   private final GameModeInventories plugin;
   HashMap<String, String> strOptions = new HashMap();
   HashMap<String, Integer> intOptions = new HashMap();
   HashMap<String, Boolean> boolOptions = new HashMap();
   List<String> bl = new ArrayList();
   List<String> com = new ArrayList();
   List<String> wor = new ArrayList();
   List<String> no = new ArrayList();
   private FileConfiguration config = null;
   private File configFile = null;

   public GameModeInventoriesConfig(GameModeInventories plugin) {
      this.plugin = plugin;
      this.configFile = new File(plugin.getDataFolder(), "config.yml");
      this.config = YamlConfiguration.loadConfiguration(this.configFile);
      this.strOptions.put("debug_level", "ERROR");
      this.strOptions.put("storage.mysql.server", "localhost");
      this.strOptions.put("storage.mysql.port", "3306");
      this.strOptions.put("storage.mysql.database", "GMI");
      this.strOptions.put("storage.mysql.user", "bukkit");
      this.strOptions.put("storage.mysql.password", "mysecurepassword");
      this.strOptions.put("storage.database", "sqlite");
      this.strOptions.put("creative_world.world", "creative");
      this.strOptions.put("creative_world.location", "last_known");
      this.intOptions.put("storage.mysql.pool_size", 10);
      this.boolOptions.put("armor", true);
      this.boolOptions.put("break_bedrock", false);
      this.boolOptions.put("bypass.inventories", true);
      this.boolOptions.put("bypass.items", true);
      this.boolOptions.put("bypass.blacklist", false);
      this.boolOptions.put("bypass.commands", false);
      this.boolOptions.put("bypass.survival", false);
      this.boolOptions.put("command_blacklist", false);
      this.boolOptions.put("creative_blacklist", false);
      this.boolOptions.put("debug", false);
      this.boolOptions.put("dont_spam_chat", false);
      this.boolOptions.put("enderchest", true);
      this.boolOptions.put("no_creative_pvp", false);
      this.boolOptions.put("no_drops", false);
      this.boolOptions.put("no_falling_drops", false);
      this.boolOptions.put("no_pickups", false);
      this.boolOptions.put("remove_potions", true);
      this.boolOptions.put("restrict_creative", false);
      this.boolOptions.put("restrict_spectator", false);
      this.boolOptions.put("save_on_death", true);
      this.boolOptions.put("creative_world.switch_to", false);
      this.boolOptions.put("track_creative_place.break_no_drop", false);
      this.boolOptions.put("track_creative_place.enabled", true);
      this.boolOptions.put("track_creative_place.no_piston_move", false);
      this.boolOptions.put("track_creative_place.attached_block", false);
      this.boolOptions.put("track_creative_place.dont_track_is_whitelist", false);
      this.boolOptions.put("xp", true);
      this.boolOptions.put("uuid_conversion_done", false);
      this.boolOptions.put("blocks_conversion_done", false);
      this.boolOptions.put("storage.mysql.test_connection", false);
      this.bl.add("TNT");
      this.bl.add("BEDROCK");
      this.bl.add("LAVA_BUCKET");
      this.com.add("give");
      this.com.add("i");
      this.com.add("buy");
      this.com.add("sell");
      this.wor.add("world");
      this.no.add("STONE");
      this.no.add("DIRT");
   }

   public void checkConfig() {
      int i = 0;
      Iterator var2 = this.strOptions.entrySet().iterator();

      Entry entry;
      while(var2.hasNext()) {
         entry = (Entry)var2.next();
         if (!this.config.contains((String)entry.getKey())) {
            this.plugin.getConfig().set((String)entry.getKey(), entry.getValue());
            ++i;
         }
      }

      var2 = this.intOptions.entrySet().iterator();

      while(var2.hasNext()) {
         entry = (Entry)var2.next();
         if (!this.config.contains((String)entry.getKey())) {
            this.plugin.getConfig().set((String)entry.getKey(), entry.getValue());
            ++i;
         }
      }

      var2 = this.boolOptions.entrySet().iterator();

      while(var2.hasNext()) {
         entry = (Entry)var2.next();
         if (!this.config.contains((String)entry.getKey())) {
            if (((String)entry.getKey()).equals("track_creative_place.enabled")) {
               if (this.plugin.getConfig().contains("track_creative_place")) {
                  this.plugin.getConfig().set((String)entry.getKey(), this.plugin.getConfig().getBoolean("track_creative_place"));
               } else {
                  this.plugin.getConfig().set((String)entry.getKey(), entry.getValue());
               }
            }

            this.plugin.getConfig().set((String)entry.getKey(), entry.getValue());
            ++i;
         }
      }

      if (!this.config.contains("blacklist")) {
         this.plugin.getConfig().set("blacklist", this.bl);
         ++i;
      }

      if (!this.config.contains("commands")) {
         this.plugin.getConfig().set("commands", this.com);
         ++i;
      }

      if (!this.config.contains("track_creative_place.worlds")) {
         this.plugin.getConfig().set("track_creative_place.worlds", this.wor);
         ++i;
      }

      if (!this.config.contains("track_creative_place.dont_track")) {
         this.plugin.getConfig().set("track_creative_place.dont_track", this.no);
         ++i;
      }

      if (this.config.contains("storage.mysql.url")) {
         String url = this.config.getString("storage.mysql.url");
         String[] split = url.split("/");
         String[] sp = split[2].split(":");
         this.plugin.getConfig().set("storage.mysql.server", sp[0]);
         this.plugin.getConfig().set("storage.mysql.port", sp[1]);
         this.plugin.getConfig().set("storage.mysql.database", split[3]);
         this.plugin.getConfig().set("storage.mysql.url", (Object)null);
      }

      if (i > 0) {
         this.plugin.getServer().getConsoleSender().sendMessage(this.plugin.MY_PLUGIN_NAME + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to config");
      }

      this.plugin.saveConfig();
   }
}

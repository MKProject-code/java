package me.eccentric_nz.gamemodeinventories;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.eccentric_nz.gamemodeinventories.database.GameModeInventoriesBlocksConverter;
import me.eccentric_nz.gamemodeinventories.database.GameModeInventoriesConnectionPool;
import me.eccentric_nz.gamemodeinventories.database.GameModeInventoriesMySQL;
import me.eccentric_nz.gamemodeinventories.database.GameModeInventoriesQueueDrain;
import me.eccentric_nz.gamemodeinventories.database.GameModeInventoriesRecordingTask;
import me.eccentric_nz.gamemodeinventories.database.GameModeInventoriesSQLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class GameModeInventories extends JavaPlugin {
   public static GameModeInventories plugin;
   public final String MY_PLUGIN_NAME;
   private final HashMap<String, List<String>> creativeBlocks;
   private final List<Material> blackList;
   private final List<Material> noTrackList;
   private final List<String> points;
   private final List<UUID> stands;
   public BukkitTask recordingTask;
   private GameModeInventoriesInventory inventoryHandler;
   private GameModeInventoriesBlock block;
   private GameModeInventoriesMessage m;
   private GameModeInventoriesBlockLogger blockLogger;
   private GMIDebug db_level;

   public GameModeInventories() {
      this.MY_PLUGIN_NAME = ChatColor.GOLD + "[GameModeInventories] " + ChatColor.RESET;
      this.creativeBlocks = new HashMap();
      this.blackList = new ArrayList();
      this.noTrackList = new ArrayList();
      this.points = new ArrayList();
      this.stands = new ArrayList();
   }

   public void onDisable() {
      this.getServer().getOnlinePlayers().forEach((p) -> {
         if (p.hasPermission("gamemodeinventories.use") && p.isOnline()) {
            this.inventoryHandler.switchInventories(p, p.getGameMode());
         }

      });
      (new GameModeInventoriesStand(this)).saveStands();
      (new GameModeInventoriesQueueDrain(this)).forceDrainQueue();
   }

   public void onEnable() {
      plugin = this;
      PluginManager pm = Bukkit.getServer().getPluginManager();
      Version bukkitversion = this.getServerVersion(this.getServer().getVersion());
      Version minversion = new Version("1.13");
      if (bukkitversion.compareTo(minversion) >= 0) {
         this.saveDefaultConfig();
         GameModeInventoriesConfig tc = new GameModeInventoriesConfig(this);
         tc.checkConfig();
         this.loadDatabase();
         if (!this.getConfig().getBoolean("blocks_conversion_done")) {
            (new GameModeInventoriesBlocksConverter(this)).convertBlocksTable();
            this.getConfig().set("blocks_conversion_done", true);
            this.saveConfig();
            System.out.println("[GameModeInventories] Blocks conversion successful :)");
         }

         if (this.getConfig().getBoolean("creative_world.switch_to")) {
            World creative = this.getServer().getWorld(this.getConfig().getString("creative_world.world"));
            if (creative == null) {
               this.getConfig().set("creative_world.switch_to", false);
               this.saveConfig();
               System.out.println("[GameModeInventories] Creative world specified in the config was not found, disabling world switching!");
            }
         }

         this.block = new GameModeInventoriesBlock(this);
         this.m = new GameModeInventoriesMessage(this);
         this.m.updateMessages();
         this.m.getMessages();

         try {
            this.db_level = GMIDebug.valueOf(this.getConfig().getString("debug_level"));
         } catch (IllegalArgumentException var6) {
            this.db_level = GMIDebug.ERROR;
         }

         this.inventoryHandler = new GameModeInventoriesInventory(this);
         pm.registerEvents(new GameModeInventoriesListener(this), this);
         pm.registerEvents(new GameModeInventoriesChunkLoadListener(this), this);
         pm.registerEvents(new GameModeInventoriesDeath(this), this);
         pm.registerEvents(new GameModeInventoriesBlockListener(this), this);
         if (this.getConfig().getBoolean("track_creative_place.dont_track_is_whitelist")) {
            pm.registerEvents(new GameModeInventoriesTrackWhiteListener(this), this);
         } else {
            pm.registerEvents(new GameModeInventoriesTrackBlackListener(this), this);
         }

         pm.registerEvents(new GameModeInventoriesPistonListener(this), this);
         pm.registerEvents(new GameModeInventoriesCommandListener(this), this);
         pm.registerEvents(new GameModeInventoriesWorldListener(this), this);
         pm.registerEvents(new GameModeInventoriesEntityListener(this), this);
         pm.registerEvents(new GameModeInventoriesPhysicsListener(this), this);
         GameModeInventoriesCommands command = new GameModeInventoriesCommands(this);
         this.getCommand("gmi").setExecutor(command);
         this.getCommand("gmi").setTabCompleter(command);
         (new GameModeInventoriesStand(this)).loadStands();
         this.loadBlackList();
         this.loadNoTrackList();
         this.setUpBlockLogger();
         this.actionRecorderTask();
      } else {
         this.getServer().getConsoleSender().sendMessage(this.MY_PLUGIN_NAME + ChatColor.RED + "This plugin requires CraftBukkit/Spigot 1.9 or higher, disabling...");
         pm.disablePlugin(this);
      }

   }

   private Version getServerVersion(String s) {
      Pattern pat = Pattern.compile("\\((.+?)\\)", 32);
      Matcher mat = pat.matcher(s);
      String v;
      if (mat.find()) {
         String[] split = mat.group(1).split(" ");
         String[] tmp = split[1].split("-");
         if (tmp.length > 1) {
            v = tmp[0];
         } else {
            v = split[1];
         }
      } else {
         v = "1.7.10";
      }

      return new Version(v);
   }

   private void loadDatabase() {
      String dbtype = this.getConfig().getString("storage.database");

      try {
         if (dbtype.equals("sqlite")) {
            String path = this.getDataFolder() + File.separator + "GMI.db";
            new GameModeInventoriesConnectionPool(path);
            GameModeInventoriesSQLite sqlite = new GameModeInventoriesSQLite(this);
            sqlite.createTables();
         } else {
            new GameModeInventoriesConnectionPool();
            GameModeInventoriesMySQL mysql = new GameModeInventoriesMySQL(this);
            mysql.createTables();
         }
      } catch (ClassNotFoundException var5) {
         this.getServer().getConsoleSender().sendMessage(this.MY_PLUGIN_NAME + "Connection and Tables Error: " + var5);
      }

   }

   public void setUpBlockLogger() {
      this.blockLogger = new GameModeInventoriesBlockLogger(this);
      this.blockLogger.enableLogger();
   }

   public GameModeInventoriesBlockLogger getBlockLogger() {
      return this.blockLogger;
   }

   public void debug(Object o, GMIDebug b) {
      if (this.getConfig().getBoolean("debug") && (b.equals(this.db_level) || b.equals(GMIDebug.ALL))) {
         System.out.println("[GameModeInventories Debug] " + o);
      }

   }

   public void debug(Object o) {
      this.debug(o, GMIDebug.ERROR);
   }

   public GameModeInventoriesInventory getInventoryHandler() {
      return this.inventoryHandler;
   }

   public GameModeInventoriesBlock getBlock() {
      return this.block;
   }

   public HashMap<String, List<String>> getCreativeBlocks() {
      return this.creativeBlocks;
   }

   public List<Material> getBlackList() {
      return this.blackList;
   }

   private void loadBlackList() {
      List<String> bl = this.getConfig().getStringList("blacklist");
      bl.forEach((s) -> {
         try {
            this.blackList.add(Material.valueOf(s));
         } catch (IllegalArgumentException var3) {
            this.getServer().getConsoleSender().sendMessage(this.MY_PLUGIN_NAME + String.format((String)this.m.getMessage().get("INVALID_MATERIAL"), s));
         }

      });
   }

   public List<Material> getNoTrackList() {
      return this.noTrackList;
   }

   private void loadNoTrackList() {
      List<String> ntl = this.getConfig().getStringList("track_creative_place.dont_track");
      ntl.forEach((s) -> {
         try {
            this.noTrackList.add(Material.valueOf(s));
         } catch (IllegalArgumentException var3) {
            this.getServer().getConsoleSender().sendMessage(this.MY_PLUGIN_NAME + String.format((String)this.m.getMessage().get("INVALID_MATERIAL_TRACK"), s));
         }

      });
   }

   public List<String> getPoints() {
      return this.points;
   }

   public List<UUID> getStands() {
      return this.stands;
   }

   public GameModeInventoriesMessage getM() {
      return this.m;
   }

   public void actionRecorderTask() {
      int recorder_tick_delay = 3;
      this.recordingTask = this.getServer().getScheduler().runTaskLaterAsynchronously(this, new GameModeInventoriesRecordingTask(this), (long)recorder_tick_delay);
   }
}

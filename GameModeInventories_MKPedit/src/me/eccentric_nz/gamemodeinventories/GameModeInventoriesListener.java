package me.eccentric_nz.gamemodeinventories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.gamemodeinventories.database.GameModeInventoriesConnectionPool;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GameModeInventoriesListener implements Listener {
   private final GameModeInventories plugin;
   List<Material> containers = new ArrayList();

   public GameModeInventoriesListener(GameModeInventories plugin) {
      this.plugin = plugin;
      this.containers.add(Material.ANVIL);
      this.containers.add(Material.BEACON);
      this.containers.add(Material.BLACK_SHULKER_BOX);
      this.containers.add(Material.BLAST_FURNACE);
      this.containers.add(Material.BLUE_SHULKER_BOX);
      this.containers.add(Material.BREWING_STAND);
      this.containers.add(Material.BROWN_SHULKER_BOX);
      this.containers.add(Material.CARTOGRAPHY_TABLE);
      this.containers.add(Material.CHEST);
      this.containers.add(Material.CHIPPED_ANVIL);
      this.containers.add(Material.CYAN_SHULKER_BOX);
      this.containers.add(Material.DAMAGED_ANVIL);
      this.containers.add(Material.DISPENSER);
      this.containers.add(Material.DROPPER);
      this.containers.add(Material.ENCHANTING_TABLE);
      this.containers.add(Material.ENDER_CHEST);
      this.containers.add(Material.FLETCHING_TABLE);
      this.containers.add(Material.FURNACE);
      this.containers.add(Material.GRAY_SHULKER_BOX);
      this.containers.add(Material.GREEN_SHULKER_BOX);
      this.containers.add(Material.GRINDSTONE);
      this.containers.add(Material.HOPPER);
      this.containers.add(Material.JUKEBOX);
      this.containers.add(Material.LIGHT_BLUE_SHULKER_BOX);
      this.containers.add(Material.LIGHT_GRAY_SHULKER_BOX);
      this.containers.add(Material.LIME_SHULKER_BOX);
      this.containers.add(Material.LOOM);
      this.containers.add(Material.MAGENTA_SHULKER_BOX);
      this.containers.add(Material.ORANGE_SHULKER_BOX);
      this.containers.add(Material.PINK_SHULKER_BOX);
      this.containers.add(Material.PURPLE_SHULKER_BOX);
      this.containers.add(Material.RED_SHULKER_BOX);
      this.containers.add(Material.SMITHING_TABLE);
      this.containers.add(Material.SMOKER);
      this.containers.add(Material.STONECUTTER);
      this.containers.add(Material.TRAPPED_CHEST);
      this.containers.add(Material.WHITE_SHULKER_BOX);
      this.containers.add(Material.YELLOW_SHULKER_BOX);
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   public void onGameModeChange(PlayerGameModeChangeEvent event) {
      Player p = event.getPlayer();
      GameMode newGM = event.getNewGameMode();
      if (newGM.equals(GameMode.SPECTATOR) && this.plugin.getConfig().getBoolean("restrict_spectator") && !p.hasPermission("gamemodeinventories.spectator")) {
         event.setCancelled(true);
         p.sendMessage(this.plugin.MY_PLUGIN_NAME + (String)this.plugin.getM().getMessage().get("NO_SPECTATOR"));
      } else {
         if (p.hasPermission("gamemodeinventories.use") && p.isOnline()) {
            this.plugin.getInventoryHandler().switchInventories(p, newGM);
            if (newGM.equals(GameMode.CREATIVE) && this.plugin.getConfig().getBoolean("creative_world.switch_to")) {
               Location loc = this.plugin.getServer().getWorld(this.plugin.getConfig().getString("creative_world.world")).getSpawnLocation();
               if (this.plugin.getConfig().getString("creative_world.location").equals("last_known")) {
                  String uuid = p.getUniqueId().toString();
                  Connection connection = null;
                  PreparedStatement statement = null;
                  ResultSet rs = null;

                  try {
                     connection = GameModeInventoriesConnectionPool.dbc();
                     if (connection != null && !connection.isClosed()) {
                        statement = connection.prepareStatement("SELECT * FROM worlds WHERE uuid = ? AND world = ?");
                        statement.setString(1, uuid);
                        statement.setString(2, this.plugin.getConfig().getString("creative_world.world"));
                        rs = statement.executeQuery();
                        if (rs.next()) {
                           World w = this.plugin.getServer().getWorld(rs.getString("world"));
                           if (w != null) {
                              double x = rs.getDouble("x");
                              double y = rs.getDouble("y");
                              double z = rs.getDouble("z");
                              float yaw = rs.getFloat("yaw");
                              float pitch = rs.getFloat("pitch");
                              loc = new Location(w, x, y, z, yaw, pitch);
                           }
                        }
                     }
                  } catch (SQLException var26) {
                     this.plugin.debug("Could not get creative world location, " + var26);
                  } finally {
                     try {
                        if (rs != null) {
                           rs.close();
                        }

                        if (statement != null) {
                           statement.close();
                        }

                        if (connection != null && GameModeInventoriesConnectionPool.isIsMySQL()) {
                           connection.close();
                        }
                     } catch (SQLException var25) {
                        System.err.println("Could not close resultsets, statements or connection [worlds], " + var25);
                     }

                  }
               }

               if (loc != null) {
                  p.teleport(loc);
               }
            }
         }

      }
   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onInventoryOpen(PlayerInteractEvent event) {
      if (this.plugin.getConfig().getBoolean("restrict_creative")) {
         Block b = event.getClickedBlock();
         if (b != null) {
            Player p = event.getPlayer();
            if (p.isSneaking() && this.isBlock(p.getInventory().getItemInMainHand().getType())) {
               return;
            }

            Material m = b.getType();
            GameMode gm = p.getGameMode();
            if (gm.equals(GameMode.CREATIVE) && this.containers.contains(m) && !GameModeInventoriesBypass.canBypass(p, "inventories", this.plugin) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
               event.setCancelled(true);
               if (!this.plugin.getConfig().getBoolean("dont_spam_chat")) {
                  p.sendMessage(this.plugin.MY_PLUGIN_NAME + (String)this.plugin.getM().getMessage().get("NO_CREATIVE_INVENTORY"));
               }
            }
         }
      }

   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onInventoryClose(InventoryCloseEvent event) {
      if (this.plugin.getConfig().getBoolean("no_drops")) {
         Inventory inv = event.getInventory();
         if (inv.getType().equals(InventoryType.WORKBENCH)) {
            Player p = (Player)event.getPlayer();
            if (p.getGameMode().equals(GameMode.CREATIVE) && !GameModeInventoriesBypass.canBypass(p, "inventories", this.plugin)) {
               boolean empty = true;
               ItemStack[] var5 = inv.getContents();
               int var6 = var5.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  ItemStack is = var5[var7];
                  if (!is.getType().equals(Material.AIR)) {
                     empty = false;
                  }
               }

               if (!empty) {
                  inv.clear();
                  if (!this.plugin.getConfig().getBoolean("dont_spam_chat")) {
                     p.sendMessage(this.plugin.MY_PLUGIN_NAME + (String)this.plugin.getM().getMessage().get("NO_WORKBENCH_DROPS"));
                  }
               }
            }
         }
      }

   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onEntityClick(PlayerInteractEntityEvent event) {
      if (this.plugin.getConfig().getBoolean("restrict_creative")) {
         Entity entity = event.getRightClicked();
         Player p = event.getPlayer();
         if (p.getGameMode().equals(GameMode.CREATIVE) && this.plugin.getInventoryHandler().isInstanceOf(entity) && !GameModeInventoriesBypass.canBypass(p, "inventories", this.plugin)) {
            if (!this.plugin.getConfig().getBoolean("dont_spam_chat")) {
               p.sendMessage(this.plugin.MY_PLUGIN_NAME + (String)this.plugin.getM().getMessage().get("NO_CREATIVE_INVENTORY"));
            }

            event.setCancelled(true);
         }
      }

   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
      this.onEntityClick(event);
   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onPlayerDrop(PlayerDropItemEvent event) {
      if (this.plugin.getConfig().getBoolean("no_drops")) {
         Player p = event.getPlayer();
         GameMode gm = p.getGameMode();
         if (gm.equals(GameMode.CREATIVE) && !GameModeInventoriesBypass.canBypass(p, "items", this.plugin)) {
            event.setCancelled(true);
            if (!this.plugin.getConfig().getBoolean("dont_spam_chat")) {
               p.sendMessage(this.plugin.MY_PLUGIN_NAME + (String)this.plugin.getM().getMessage().get("NO_PLAYER_DROPS"));
            }
         }
      }

   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void noPickup(EntityPickupItemEvent event) {
      if (event.getEntity() instanceof Player) {
         if (this.plugin.getConfig().getBoolean("no_pickups")) {
            Player p = (Player)event.getEntity();
            GameMode gm = p.getGameMode();
            if (gm.equals(GameMode.CREATIVE) && !GameModeInventoriesBypass.canBypass(p, "items", this.plugin)) {
               event.setCancelled(true);
               if (!this.plugin.getConfig().getBoolean("dont_spam_chat")) {
                  p.sendMessage(this.plugin.MY_PLUGIN_NAME + (String)this.plugin.getM().getMessage().get("NO_CREATIVE_PICKUP"));
               }
            }
         }

      }
   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void noHorseInventory(InventoryOpenEvent event) {
      if (this.plugin.getConfig().getBoolean("restrict_creative") && this.plugin.getInventoryHandler().isInstanceOf(event.getInventory().getHolder())) {
         Player p = (Player)event.getPlayer();
         GameMode gm = p.getGameMode();
         if (gm.equals(GameMode.CREATIVE) && !GameModeInventoriesBypass.canBypass(p, "inventories", this.plugin)) {
            event.setCancelled(true);
            if (!this.plugin.getConfig().getBoolean("dont_spam_chat")) {
               p.sendMessage(this.plugin.MY_PLUGIN_NAME + (String)this.plugin.getM().getMessage().get("NO_CREATIVE_HORSE"));
            }
         }
      }

   }

   private boolean isBlock(Material m) {
      return !m.equals(Material.AIR) && m.isBlock();
   }
}

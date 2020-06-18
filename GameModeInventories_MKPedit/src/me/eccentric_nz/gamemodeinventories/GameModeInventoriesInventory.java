package me.eccentric_nz.gamemodeinventories;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.eccentric_nz.gamemodeinventories.database.GameModeInventoriesConnectionPool;
import org.bukkit.GameMode;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GameModeInventoriesInventory {
   private final GameModeInventories plugin;
   private final boolean saveXP;
   private final boolean saveArmour;
   private final boolean saveEnderChest;
   private final boolean potions;
   GameModeInventoriesXPCalculator xpc;

   public GameModeInventoriesInventory(GameModeInventories plugin) {
      this.plugin = plugin;
      this.saveXP = this.plugin.getConfig().getBoolean("xp");
      this.saveArmour = this.plugin.getConfig().getBoolean("armor");
      this.saveEnderChest = this.plugin.getConfig().getBoolean("enderchest");
      this.potions = this.plugin.getConfig().getBoolean("remove_potions");
   }

   public void switchInventories(Player p, GameMode newGM) {
      String uuid = p.getUniqueId().toString();
      String name = p.getName();
      String currentGM = p.getGameMode().name();
      if (this.saveXP) {
         this.xpc = new GameModeInventoriesXPCalculator(p);
      }

      String inv = GameModeInventoriesBukkitSerialization.toDatabase(p.getInventory().getContents());
      String attr = "";
      Connection connection = null;
      PreparedStatement statement = null;
      ResultSet rsInv = null;
      PreparedStatement ps = null;
      ResultSet idRS = null;
      PreparedStatement psx = null;
      PreparedStatement psa = null;
      PreparedStatement pse = null;
      ResultSet rsNewInv = null;

      try {
         connection = GameModeInventoriesConnectionPool.dbc();
         if (connection != null && !connection.isClosed()) {
            statement = connection.prepareStatement("SELECT * FROM inventories WHERE uuid = ? AND gamemode = ?");
            statement.setString(1, uuid);
            statement.setString(2, currentGM);
            rsInv = statement.executeQuery();
            int id = 0;
            String arm;
            if (rsInv.next()) {
               id = rsInv.getInt("id");
               arm = "UPDATE inventories SET inventory = ?, attributes = ? WHERE id = ?";
               ps = connection.prepareStatement(arm);
               ps.setString(1, inv);
               ps.setString(2, attr);
               ps.setInt(3, id);
               ps.executeUpdate();
            } else {
               arm = "INSERT INTO inventories (uuid, player, gamemode, inventory, attributes) VALUES (?, ?, ?, ?, ?)";
               ps = connection.prepareStatement(arm, 1);
               ps.setString(1, uuid);
               ps.setString(2, name);
               ps.setString(3, currentGM);
               ps.setString(4, inv);
               ps.setString(5, attr);
               ps.executeUpdate();
               idRS = ps.getGeneratedKeys();
               if (idRS.next()) {
                  id = idRS.getInt(1);
               }
            }

            String savedinventory;
            int amount;
            if (this.saveXP) {
               amount = this.xpc.getCurrentExp();
               savedinventory = "UPDATE inventories SET xp = ? WHERE id = ?";
               psx = connection.prepareStatement(savedinventory);
               psx.setInt(1, amount);
               psx.setInt(2, id);
               psx.executeUpdate();
            }

            String enderQuery;
            if (this.saveArmour) {
               arm = GameModeInventoriesBukkitSerialization.toDatabase(p.getInventory().getArmorContents());
               savedinventory = "";
               enderQuery = "UPDATE inventories SET armour = ?, armour_attributes = ? WHERE id = ?";
               psa = connection.prepareStatement(enderQuery);
               psa.setString(1, arm);
               psa.setString(2, savedinventory);
               psa.setInt(3, id);
               psa.executeUpdate();
            }

            if (this.saveEnderChest) {
               Inventory ec = p.getEnderChest();
               if (ec != null) {
                  savedinventory = GameModeInventoriesBukkitSerialization.toDatabase(ec.getContents());
                  enderQuery = "UPDATE inventories SET enderchest = ? WHERE id = ?";
                  pse = connection.prepareStatement(enderQuery);
                  pse.setString(1, savedinventory);
                  pse.setInt(2, id);
                  pse.executeUpdate();
               }
            }

            if (this.potions && currentGM.equals("CREATIVE") && newGM.equals(GameMode.SURVIVAL)) {
               p.getActivePotionEffects().forEach((effect) -> {
                  p.removePotionEffect(effect.getType());
               });
            }

            try {
               statement.setString(1, uuid);
               statement.setString(2, newGM.name());
               rsNewInv = statement.executeQuery();
               if (rsNewInv.next()) {
                  savedinventory = rsNewInv.getString("inventory");
                  ItemStack[] i;
                  if (savedinventory.startsWith("[")) {
                     i = GameModeInventoriesJSONSerialization.toItemStacks(savedinventory);
                  } else {
                     i = GameModeInventoriesBukkitSerialization.fromDatabase(savedinventory);
                  }

                  p.getInventory().setContents(i);
                  amount = rsNewInv.getInt("xp");
                  String savedender;
                  ItemStack[] e;
                  if (this.saveArmour) {
                     savedender = rsNewInv.getString("armour");
                     if (savedender != null) {
                        if (savedender.startsWith("[")) {
                           e = GameModeInventoriesJSONSerialization.toItemStacks(savedender);
                        } else {
                           e = GameModeInventoriesBukkitSerialization.fromDatabase(savedender);
                        }

                        p.getInventory().setArmorContents(e);
                     }
                  }

                  if (this.saveEnderChest) {
                     savedender = rsNewInv.getString("enderchest");
                     if (savedender == null || savedender.equals("[Null]") || savedender.equals("") || savedender.isEmpty()) {
                        savedender = "[\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\",\"null\"]";
                     }

                     if (savedender.startsWith("[")) {
                        e = GameModeInventoriesJSONSerialization.toItemStacks(savedender);
                     } else {
                        e = GameModeInventoriesBukkitSerialization.fromDatabase(savedender);
                     }

                     Inventory echest = p.getEnderChest();
                     echest.setContents(e);
                  }
               } else {
                  p.getInventory().clear();
                  if (this.saveArmour) {
                     p.getInventory().setBoots((ItemStack)null);
                     p.getInventory().setChestplate((ItemStack)null);
                     p.getInventory().setLeggings((ItemStack)null);
                     p.getInventory().setHelmet((ItemStack)null);
                  }

                  if (this.saveEnderChest) {
                     Inventory echest = p.getEnderChest();
                     echest.clear();
                  }

                  amount = 0;
               }

               if (this.saveXP) {
                  this.xpc.setExp(amount);
               }

               p.updateInventory();
            } catch (IOException var33) {
               GameModeInventories.plugin.debug("Could not restore inventory on gamemode change, " + var33);
            }
         } else {
            GameModeInventories.plugin.debug("Database connection was NULL or closed");
         }
      } catch (SQLException var34) {
         GameModeInventories.plugin.debug("Could not save inventory on gamemode change, " + var34);
      } finally {
         try {
            if (rsNewInv != null) {
               rsNewInv.close();
            }

            if (pse != null) {
               pse.close();
            }

            if (psa != null) {
               psa.close();
            }

            if (psx != null) {
               psx.close();
            }

            if (idRS != null) {
               idRS.close();
            }

            if (ps != null) {
               ps.close();
            }

            if (rsInv != null) {
               rsInv.close();
            }

            if (statement != null) {
               statement.close();
            }

            if (connection != null && GameModeInventoriesConnectionPool.isIsMySQL()) {
               connection.close();
            }
         } catch (SQLException var32) {
            System.err.println("Could not close resultsets, statements or connection, " + var32);
         }

      }

   }

   public void saveOnDeath(Player p) {
      String uuid = p.getUniqueId().toString();
      String name = p.getName();
      String gm = p.getGameMode().name();
      String inv = GameModeInventoriesBukkitSerialization.toDatabase(p.getInventory().getContents());
      String arm = GameModeInventoriesBukkitSerialization.toDatabase(p.getInventory().getArmorContents());
      Connection connection = null;
      PreparedStatement statement = null;
      ResultSet rsInv = null;
      PreparedStatement ps = null;

      try {
         connection = GameModeInventoriesConnectionPool.dbc();
         statement = connection.prepareStatement("SELECT id FROM inventories WHERE uuid = ? AND gamemode = ?");
         statement.setString(1, uuid);
         statement.setString(2, gm);
         rsInv = statement.executeQuery();
         if (rsInv.next()) {
            int id = rsInv.getInt("id");
            String updateQuery = "UPDATE inventories SET inventory = ?, armour = ?, attributes = ?, armour_attributes = ?  WHERE id = ?";
            ps = connection.prepareStatement(updateQuery);
            ps.setString(1, inv);
            ps.setString(2, arm);
            ps.setInt(5, id);
            ps.executeUpdate();
         } else {
            String invQuery = "INSERT INTO inventories (uuid, player, gamemode, inventory, armour, attributes, armour_attributes) VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = connection.prepareStatement(invQuery);
            ps.setString(1, uuid);
            ps.setString(2, name);
            ps.setString(3, gm);
            ps.setString(4, inv);
            ps.setString(5, arm);
            ps.executeUpdate();
         }
      } catch (SQLException var21) {
         GameModeInventories.plugin.debug("Could not save inventories on player death, " + var21);
      } finally {
         try {
            if (ps != null) {
               ps.close();
            }

            if (rsInv != null) {
               rsInv.close();
            }

            if (statement != null) {
               statement.close();
            }

            if (connection != null && GameModeInventoriesConnectionPool.isIsMySQL()) {
               connection.close();
            }
         } catch (SQLException var20) {
            System.err.println("Could not close resultsets, statements or connection, " + var20);
         }

      }

   }

   public void restoreOnSpawn(Player p) {
      String uuid = p.getUniqueId().toString();
      String gm = p.getGameMode().name();
      Connection connection = null;
      PreparedStatement statement = null;
      ResultSet rsInv = null;

      try {
         connection = GameModeInventoriesConnectionPool.dbc();
         statement = connection.prepareStatement("SELECT * FROM inventories WHERE uuid = ? AND gamemode = ?");
         statement.setString(1, uuid);
         statement.setString(2, gm);
         rsInv = statement.executeQuery();
         if (rsInv.next()) {
            try {
               String savedinventory = rsInv.getString("inventory");
               ItemStack[] i;
               if (savedinventory.startsWith("[")) {
                  i = GameModeInventoriesJSONSerialization.toItemStacks(savedinventory);
               } else {
                  i = GameModeInventoriesBukkitSerialization.fromDatabase(savedinventory);
               }

               p.getInventory().setContents(i);
               String savedarmour = rsInv.getString("armour");
               ItemStack[] a;
               if (savedarmour.startsWith("[")) {
                  a = GameModeInventoriesJSONSerialization.toItemStacks(savedarmour);
               } else {
                  a = GameModeInventoriesBukkitSerialization.fromDatabase(savedarmour);
               }

               p.getInventory().setArmorContents(a);
            } catch (IOException var20) {
               GameModeInventories.plugin.debug("Could not restore inventories on respawn, " + var20);
            }
         }
      } catch (SQLException var21) {
         GameModeInventories.plugin.debug("Could not restore inventories on respawn, " + var21);
      } finally {
         try {
            if (rsInv != null) {
               rsInv.close();
            }

            if (statement != null) {
               statement.close();
            }

            if (connection != null && GameModeInventoriesConnectionPool.isIsMySQL()) {
               connection.close();
            }
         } catch (SQLException var19) {
            System.err.println("Could not close resultsets, statements or connection, " + var19);
         }

      }

   }

   public boolean isInstanceOf(Entity e) {
      return e instanceof PoweredMinecart || e instanceof StorageMinecart || e instanceof HopperMinecart || e instanceof ItemFrame || e instanceof ArmorStand;
   }

   public boolean isInstanceOf(InventoryHolder h) {
      return h instanceof AbstractHorse;
   }
}

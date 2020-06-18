package me.eccentric_nz.gamemodeinventories;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.gamemodeinventories.database.GameModeInventoriesConnectionPool;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

public class GameModeInventoriesCommands implements CommandExecutor, TabCompleter {
   private final GameModeInventories plugin;
   private final HashMap<String, String> firstArgs = new HashMap();
   private final ImmutableList<String> ROOT_SUBS;

   public GameModeInventoriesCommands(GameModeInventories plugin) {
      this.plugin = plugin;
      this.firstArgs.put("armor", "armor");
      this.firstArgs.put("attached_block", "track_creative_place.attached_block");
      this.firstArgs.put("break_bedrock", "break_bedrock");
      this.firstArgs.put("break_no_drop", "track_creative_place.break_no_drop");
      this.firstArgs.put("bypass.blacklist", "bypass.blacklist");
      this.firstArgs.put("bypass.commands", "bypass.commands");
      this.firstArgs.put("bypass.inventories", "bypass.inventories");
      this.firstArgs.put("bypass.items", "bypass.items");
      this.firstArgs.put("bypass.survival", "bypass.survival");
      this.firstArgs.put("command_blacklist", "command_blacklist");
      this.firstArgs.put("creative_blacklist", "creative_blacklist");
      this.firstArgs.put("debug", "debug");
      this.firstArgs.put("dont_spam_chat", "dont_spam_chat");
      this.firstArgs.put("enderchest", "enderchest");
      this.firstArgs.put("no_creative_pvp", "no_creative_pvp");
      this.firstArgs.put("no_drops", "no_drops");
      this.firstArgs.put("no_falling_drops", "no_falling_drops");
      this.firstArgs.put("no_pickups", "no_pickups");
      this.firstArgs.put("no_piston_move", "track_creative_place.no_piston_move");
      this.firstArgs.put("remove_potions", "remove_potions");
      this.firstArgs.put("restrict_creative", "restrict_creative");
      this.firstArgs.put("restrict_spectator", "restrict_spectator");
      this.firstArgs.put("save_on_death", "save_on_death");
      this.firstArgs.put("survival_on_world_change", "survival_on_world_change");
      this.firstArgs.put("switch_to", "creative_world.switch_to");
      this.firstArgs.put("track_creative_place", "track_creative_place.enabled");
      this.firstArgs.put("xp", "xp");
      this.ROOT_SUBS = ImmutableList.copyOf(this.firstArgs.keySet());
   }

   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
      if (cmd.getName().equalsIgnoreCase("gmi")) {
         if (args.length == 0) {
            sender.sendMessage(this.plugin.MY_PLUGIN_NAME + (String)this.plugin.getM().getMessage().get("HELP"));
            return true;
         }

         if (!sender.hasPermission("gamemodeinventories.admin")) {
            sender.sendMessage(this.plugin.MY_PLUGIN_NAME + (String)this.plugin.getM().getMessage().get("NO_PERMISSION"));
            return true;
         }

         String option = args[0].toLowerCase(Locale.ENGLISH);
         if (args.length == 1 && this.firstArgs.containsKey(option)) {
            boolean bool = !this.plugin.getConfig().getBoolean((String)this.firstArgs.get(option));
            this.plugin.getConfig().set((String)this.firstArgs.get(option), bool);
            sender.sendMessage(this.plugin.MY_PLUGIN_NAME + String.format((String)this.plugin.getM().getMessage().get("CONFIG_SET"), option, bool));
            this.plugin.saveConfig();
            return true;
         }

         if (args.length == 2 && option.equals("kit")) {
            String uuid = "00000000-0000-0000-0000-000000000000";
            Player p = (Player)sender;
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet rsInv = null;
            ResultSet rsNewInv = null;

            try {
               connection = GameModeInventoriesConnectionPool.dbc();
               if (connection != null && !connection.isClosed()) {
                  statement = connection.prepareStatement("SELECT id FROM inventories WHERE uuid = ? AND gamemode = 'SURVIVAL'");
                  String savedinventory;
                  if (args[1].toLowerCase().equals("save")) {
                     savedinventory = GameModeInventoriesBukkitSerialization.toDatabase(p.getInventory().getContents());
                     statement.setString(1, uuid);
                     rsInv = statement.executeQuery();
                     PreparedStatement ps;
                     String updateQuery;
                     if (rsInv.next()) {
                        int id = rsInv.getInt("id");
                        updateQuery = "UPDATE inventories SET inventory = ? WHERE id = ?";
                        ps = connection.prepareStatement(updateQuery);
                        ps.setString(1, savedinventory);
                        ps.setInt(2, id);
                        ps.executeUpdate();
                        ps.close();
                     } else {
                        updateQuery = "INSERT INTO inventories (uuid, player, gamemode, inventory) VALUES (?, 'kit', 'SURVIVAL', ?)";
                        ps = connection.prepareStatement(updateQuery);
                        ps.setString(1, uuid);
                        ps.setString(2, savedinventory);
                        ps.executeUpdate();
                        ps.close();
                     }

                     p.sendMessage(this.plugin.MY_PLUGIN_NAME + "Kit inventory saved.");
                  } else {
                     statement = connection.prepareStatement("SELECT inventory FROM inventories WHERE uuid = ? AND gamemode = 'SURVIVAL'");
                     statement.setString(1, uuid);
                     rsNewInv = statement.executeQuery();
                     if (rsNewInv.next()) {
                        try {
                           savedinventory = rsNewInv.getString("inventory");
                           ItemStack[] i;
                           if (savedinventory.startsWith("[")) {
                              i = GameModeInventoriesJSONSerialization.toItemStacks(savedinventory);
                           } else {
                              i = GameModeInventoriesBukkitSerialization.fromDatabase(savedinventory);
                           }

                           p.getInventory().setContents(i);
                        } catch (IOException var25) {
                           this.plugin.debug("Could not set inventory for kit, " + var25);
                        }
                     }

                     p.sendMessage(this.plugin.MY_PLUGIN_NAME + "Kit inventory loaded.");
                  }
               } else {
                  this.plugin.debug("Connection was " + (connection == null ? "NULL" : "closed"));
               }
            } catch (SQLException var26) {
               this.plugin.debug("Could not " + args[1].toLowerCase() + " inventory for kit, " + var26);
            } finally {
               try {
                  if (rsInv != null) {
                     rsInv.close();
                  }

                  if (rsNewInv != null) {
                     rsNewInv.close();
                  }

                  if (statement != null) {
                     statement.close();
                  }

                  if (connection != null && GameModeInventoriesConnectionPool.isIsMySQL()) {
                     connection.close();
                  }
               } catch (SQLException var24) {
                  System.err.println("Could not remove block, " + var24);
               }

            }

            return true;
         }
      }

      return false;
   }

   public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
      return (List)(args.length <= 1 ? this.partial(args[0], this.ROOT_SUBS) : ImmutableList.of());
   }

   private List<String> partial(String token, Collection<String> from) {
      return (List)StringUtil.copyPartialMatches(token, from, new ArrayList(from.size()));
   }
}

package me.eccentric_nz.gamemodeinventories;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class GameModeInventoriesCommandListener implements Listener {
   private final GameModeInventories plugin;
   private final List<String> blacklist;

   public GameModeInventoriesCommandListener(GameModeInventories plugin) {
      this.plugin = plugin;
      this.blacklist = plugin.getConfig().getStringList("commands");
      int i = 0;

      for(int l = this.blacklist.size(); i < l; ++i) {
         this.blacklist.set(i, ((String)this.blacklist.get(i)).toLowerCase());
      }

   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onCommandUse(PlayerCommandPreprocessEvent event) {
      if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE) && this.plugin.getConfig().getBoolean("command_blacklist") && !GameModeInventoriesBypass.canBypass(event.getPlayer(), "commands", this.plugin)) {
         String message = event.getMessage();
         String[] args = message.split(" ");
         if (args.length > 0) {
            String command = args[0].substring(1).toLowerCase();
            if (this.blacklist.contains(command)) {
               event.setCancelled(true);
               if (!this.plugin.getConfig().getBoolean("dont_spam_chat")) {
                  event.getPlayer().sendMessage(this.plugin.MY_PLUGIN_NAME + String.format((String)this.plugin.getM().getMessage().get("NO_CREATIVE_COMMAND"), ChatColor.GREEN + "/" + command + ChatColor.RESET));
               }
            }
         }

      }
   }
}

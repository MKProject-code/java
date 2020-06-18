// 
// Decompiled by Procyon v0.5.36
// 

package me.xmelvinnx.itemid;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Main extends JavaPlugin implements Listener, CommandExecutor {
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("[ItemID] Has been enabled!");
    }
    
    private List<Player> commandEnabled = new ArrayList<Player>();
    
//    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args) {
////        if (!command.getName().equalsIgnoreCase("itemid")) {
////            return false;
////        }
////        if (!(sender instanceof Player) || !sender.hasPermission("itemid.use")) {
////            return true;
////        }
////        
////        final Player player = Bukkit.getPlayer(sender.getName());
////        
////        if(commandEnabled.contains(player)) {
////        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5[Budowniczy] &dITEM ID: &cWy³¹czono"));
////        	commandEnabled.remove(player);
////        }
////        else
////        {
////        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5[Budowniczy] &dITEM ID: &aW³¹czono"));
////            commandEnabled.add(player);
////            setInfoTask(player);
////        }
//        return true;
//    }
    
	private void setInfoTask(Player player) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				if(!commandEnabled.contains(player))
					return;
				if(!player.isOnline() || player.getGameMode() != GameMode.CREATIVE)
				{
					commandEnabled.remove(player);
					return;
				}
				setInfoTaskRun(player, true);
			}
		}, 1*20L); //20 Tick (1 Second) delay before run() is called
	}
	
	private void setInfoTaskRun(Player player, boolean runtask) {
		ItemStack item = player.getInventory().getItemInMainHand();
		if(item.getType().equals(Material.AIR))
		{
			if(runtask) setInfoTask(player);
			return;
		}
        if (item.getType().getId() == 0) {
        	player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&7" + item.getType().name())));
        	if(runtask) setInfoTask(player);
        	return;
        }
        if (item.getData().getData() != 0) {
        	player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&7" + item.getType().name() + "&8 -> &7" + item.getType().getId() + ":" + item.getData().getData())));
        	if(runtask) setInfoTask(player);
        	return;
        }
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&7" + item.getType().name() + "&8 -> &7" + item.getType().getId())));
        if(runtask) setInfoTask(player);
	}
	
	@EventHandler
	public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent e) {
		if(e.getNewGameMode() != GameMode.CREATIVE)
			return;
		
		Player player = e.getPlayer();
        if(!player.hasPermission("mkp.itemid.use"))
        	return;
        
        if(!commandEnabled.contains(player))
        {
        	commandEnabled.add(player);
        	setInfoTask(player);
        }
		setInfoTaskRun(player, false);
	}
	
	@EventHandler
	public void onInventoryCreativeEvent(InventoryCreativeEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;
            
        Player player = (Player)e.getWhoClicked();
        if(!player.hasPermission("mkp.itemid.use"))
        	return;
        
        if(!commandEnabled.contains(player))
        {
        	commandEnabled.add(player);
        	setInfoTask(player);
        }
		setInfoTaskRun((Player)e.getWhoClicked(), false);
	}
}

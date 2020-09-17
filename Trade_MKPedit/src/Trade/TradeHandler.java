// 
// Decompiled by Procyon v0.5.36
// 

package Trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TradeHandler implements Listener
{
    private Map<String, Map<String, Long>> requests;
    private List<TradeInventory> tradeInventories;
    
    public TradeHandler() {
        this.requests = new HashMap<String, Map<String, Long>>();
        this.tradeInventories = new ArrayList<TradeInventory>();
        new BukkitRunnable() {
            public void run() {
                for (final Map.Entry<String, Map<String, Long>> entries : TradeHandler.this.requests.entrySet()) {
                    final String playerUUID = entries.getKey();
                    final Player bukkitPlayer = Bukkit.getPlayer(UUID.fromString(playerUUID));
                    final Map<String, Long> players = entries.getValue();
                    for (final String playersUUID : players.keySet()) {
                        if (System.currentTimeMillis() >= players.get(playersUUID)) {
                            players.remove(playersUUID);
                            final Player otherBukkitPlayer = Bukkit.getPlayer(UUID.fromString(playersUUID));
                            if (bukkitPlayer == null || otherBukkitPlayer == null) {
                                continue;
                            }
                            bukkitPlayer.sendMessage(Main.getInstance().getConfiguration().getMessage("TradeRequestTimeOver").replace("%player%", otherBukkitPlayer.getName()));
                            otherBukkitPlayer.sendMessage(Main.getInstance().getConfiguration().getMessage("TradeRequestTimeOver").replace("%player%", bukkitPlayer.getName()));
                        }
                    }
                    TradeHandler.this.requests.put(playerUUID, players);
                }
            }
        }.runTaskTimer((Plugin)Main.getInstance(), 5L, 5L);
    }
    
    public boolean updateRequest(final Player sender, final Player receiver) {
        if (this.isWorld(sender, receiver) && this.isRadius(sender, receiver)) {
            if (this.requests.containsKey(receiver.getUniqueId().toString())) {
                final Set<String> requestsReceiver = this.requests.get(receiver.getUniqueId().toString()).keySet();
                if (requestsReceiver.contains(sender.getUniqueId().toString())) {
                    return false;
                }
            }
            if (!this.requests.containsKey(sender.getUniqueId().toString())) {
                final Map<String, Long> emptyRequests = new HashMap<String, Long>();
                emptyRequests.put(receiver.getUniqueId().toString(), System.currentTimeMillis() + Main.getInstance().getConfiguration().getLong("TradeSettings.AllowedTime"));
                this.requests.put(sender.getUniqueId().toString(), emptyRequests);
                return true;
            }
            final Map<String, Long> requestsSender = this.requests.get(sender.getUniqueId().toString());
            if (!requestsSender.containsKey(receiver.getUniqueId().toString())) {
                requestsSender.put(receiver.getUniqueId().toString(), System.currentTimeMillis() + Main.getInstance().getConfiguration().getLong("TradeSettings.AllowedTime"));
                this.requests.put(sender.getUniqueId().toString(), requestsSender);
                return true;
            }
        }
        return true;
    }
    
    public void removeRequests(final Player player) {
        if (this.requests.containsKey(player.getUniqueId().toString())) {
            this.requests.remove(player.getUniqueId().toString());
        }
    }
    
    public boolean isAlreadySent(final Player sender, final Player receiver) {
        return this.requests.containsKey(sender.getUniqueId().toString()) && this.requests.get(sender.getUniqueId().toString()).containsKey(receiver.getUniqueId().toString());
    }
    
    public boolean isRadius(final Player sender, final Player receiver) {
        final int radius = Main.getInstance().getConfiguration().getInteger("TradeSettings.BlockRadius");
        if(radius < 0)
        	return true;
        return sender.getWorld().getName().equals(receiver.getWorld().getName()) && sender.getLocation().distance(receiver.getLocation()) <= radius;
    }
    
    public boolean isWorld(final Player sender, final Player receiver) {
        return sender.getWorld().getName().equals(receiver.getWorld().getName());
    }
    
    public void open(final Player sender, final Player receiver) {
        final TradeInventory tradeInventory = new TradeInventory(sender, receiver);
        this.tradeInventories.add(tradeInventory);
    }
    
    @EventHandler
    public void closeInventory(final InventoryCloseEvent e) {
        final Player player = (Player)e.getPlayer();
        TradeInventory tradeInventory = null;
        for (final TradeInventory inventory : this.tradeInventories) {
            if (inventory.getSender().getUniqueId().toString().equals(player.getUniqueId().toString()) || inventory.getReceiver().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                tradeInventory = inventory;
            }
        }
        if (tradeInventory != null) {
            if (tradeInventory.isFinished()) {
                if (!tradeInventory.isClosed(player)) {
                    tradeInventory.closeInventory(player);
                    tradeInventory.addSingleClose();
                    if (tradeInventory.isFullyClosed()) {
                        this.tradeInventories.remove(tradeInventory);
                    }
                }
            }
            else {
                tradeInventory.closeInventories(player);
                this.tradeInventories.remove(tradeInventory);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void Inventory(final InventoryMoveItemEvent e) {
        final Inventory clickedInventory = e.getDestination();
        TradeInventory tradeInventory = null;
        if (clickedInventory != null) {
            for (final TradeInventory inventory : this.tradeInventories) {
                if (inventory.isInventory(clickedInventory)) {
                    tradeInventory = inventory;
                }
            }
        }
        if (tradeInventory != null) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void dragInventory(final InventoryDragEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            final Inventory clickedInventory = e.getInventory();
            TradeInventory tradeInventory = null;
            if (clickedInventory != null) {
                for (final TradeInventory inventory : this.tradeInventories) {
                    if (inventory.isInventory(clickedInventory)) {
                        tradeInventory = inventory;
                    }
                }
                if (tradeInventory != null) {
                    final Set<Integer> slots = (Set<Integer>)e.getRawSlots();
                    boolean disallowed = false;
                    if (tradeInventory.isFinished()) {
                        for (final Integer slotId : slots) {
                            if (tradeInventory.getYouSlots().contains(slotId)) {
                                disallowed = true;
                            }
                        }
                    }
                    else {
                        for (final Integer slotId : slots) {
                            if (tradeInventory.getOtherSlots().contains(slotId)) {
                                disallowed = true;
                            }
                        }
                    }
                    if (disallowed) {
                        e.setCancelled(true);
                        e.setResult(Event.Result.DENY);
                        return;
                    }
                    final int playerMinId = 54;
                    final int playerMaxId = 89;
                    boolean playerInventory = false;
                    boolean foundNonItem = false;
                    for (final int id : slots) {
                        if (id >= playerMinId && id <= playerMaxId) {
                            if (foundNonItem) {
                                continue;
                            }
                            playerInventory = true;
                        }
                        else {
                            playerInventory = false;
                            foundNonItem = true;
                        }
                    }
                    if (playerInventory) {
                        return;
                    }
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);
                    for (final Integer slotId2 : slots) {
                        if (tradeInventory.isFinished()) {
                            if (!tradeInventory.getOtherSlots().contains(slotId2) || clickedInventory.getItem((int)slotId2) == null || clickedInventory.getItem((int)slotId2).getType() == Material.AIR) {
                                continue;
                            }
                            e.setCancelled(false);
                            e.setResult(Event.Result.ALLOW);
                            new BukkitRunnable() {
                                public void run() {
                                    TradeInventory tradeInventory = null;
                                    for (final TradeInventory inventory : TradeHandler.this.tradeInventories) {
                                        if (inventory.isInventory(clickedInventory)) {
                                            tradeInventory = inventory;
                                        }
                                    }
                                    if (tradeInventory != null) {
                                        tradeInventory.updateSlots(clickedInventory, tradeInventory.getInventory(clickedInventory));
                                    }
                                }
                            }.runTaskLater((Plugin)Main.getInstance(), 10L);
                        }
                        else {
                            if (!tradeInventory.getYouSlots().contains(slotId2)) {
                                continue;
                            }
                            e.setCancelled(false);
                            e.setResult(Event.Result.ALLOW);
                            new BukkitRunnable() {
                                public void run() {
                                    TradeInventory tradeInventory = null;
                                    for (final TradeInventory inventory : TradeHandler.this.tradeInventories) {
                                        if (inventory.isInventory(clickedInventory)) {
                                            tradeInventory = inventory;
                                        }
                                    }
                                    if (tradeInventory != null) {
                                        tradeInventory.updateSlots(clickedInventory, tradeInventory.getInventory(clickedInventory));
                                    }
                                }
                            }.runTaskLater((Plugin)Main.getInstance(), 10L);
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void clickInventory(final InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            final Player player = (Player)e.getWhoClicked();
            final Inventory clickedInventory = e.getClickedInventory();
            TradeInventory tradeInventory = null;
            if (clickedInventory != null) {
                for (final TradeInventory inventory : this.tradeInventories) {
                    if (inventory.isInventory(clickedInventory)) {
                        tradeInventory = inventory;
                    }
                }
            }
            if (tradeInventory == null) {
                final Inventory inventory2 = e.getInventory();
                if (inventory2 != null) {
                    TradeInventory tradeInventoryValue = null;
                    for (final TradeInventory tradeInventoryCopy : this.tradeInventories) {
                        if (tradeInventoryCopy.isInventory(inventory2)) {
                            tradeInventoryValue = tradeInventoryCopy;
                        }
                    }
                    if (tradeInventoryValue != null) {
                    	if(e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || e.getAction() != InventoryAction.PICKUP_ALL)
                        e.setCancelled(true);
                        e.setResult(Event.Result.DENY);
                        return;
                    }
                }
            }
            if (tradeInventory != null) {
                final int slotId = e.getRawSlot();
                boolean performed = false;
                if (tradeInventory.isFinished()) {
                    if (tradeInventory.getOtherSlots().contains(slotId)) {
                        if (clickedInventory.getItem(slotId) != null) {
                            if (clickedInventory.getItem(slotId).getType() != Material.AIR) {
                                performed = true;
                                new BukkitRunnable() {
                                    public void run() {
                                        TradeInventory tradeInventory = null;
                                        for (final TradeInventory inventory : TradeHandler.this.tradeInventories) {
                                            if (inventory.isInventory(clickedInventory)) {
                                                tradeInventory = inventory;
                                            }
                                        }
                                        if (tradeInventory != null && clickedInventory.getItem(slotId) != null && clickedInventory.getItem(slotId).getType() != Material.AIR) {
                                            tradeInventory.updateSlots(clickedInventory, tradeInventory.getInventory(clickedInventory));
                                        }
                                    }
                                }.runTaskLater((Plugin)Main.getInstance(), 10L);
                            }
                            else {
                                e.setCancelled(true);
                                e.setResult(Event.Result.DENY);
                            }
                        }
                        else {
                            e.setCancelled(true);
                            e.setResult(Event.Result.DENY);
                        }
                    }
                    else {
                        e.setCancelled(true);
                        e.setResult(Event.Result.DENY);
                    }
                }
                else if (tradeInventory.getYouSlots().contains(slotId)) {
                	tradeInventory.setItem(-1, player);
                	
                    performed = true;
                    new BukkitRunnable() {
                        public void run() {
                            TradeInventory tradeInventory = null;
                            for (final TradeInventory inventory : TradeHandler.this.tradeInventories) {
                                if (inventory.isInventory(clickedInventory)) {
                                    tradeInventory = inventory;
                                }
                            }
                            if (tradeInventory != null) {
                                tradeInventory.updateSlots(clickedInventory, tradeInventory.getInventory(clickedInventory));
                            }
                        }
                    }.runTaskLater((Plugin)Main.getInstance(), 10L);
                }
                if (slotId == 45 || slotId == 46) {
                    tradeInventory.setItem(e.getSlot(), player);
                }
                if (!performed) {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);
                }
            }
        }
    }
}

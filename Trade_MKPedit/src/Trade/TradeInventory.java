// 
// Decompiled by Procyon v0.5.36
// 

package Trade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TradeInventory
{
    private Player sender;
    private Player receiver;
    private Inventory senderInventory;
    private Inventory receiverInventory;
    private List<Integer> youSlots;
    private List<Integer> otherSlots;
    private boolean finished;
    private boolean closed;
    private int singleClose;
    private List<String> closedPlayers;
    
    public TradeInventory(final Player sender, final Player receiver) {
        this.finished = false;
        this.closed = false;
        this.closedPlayers = new ArrayList<String>();
        this.sender = sender;
        this.receiver = receiver;
        this.senderInventory = Bukkit.createInventory((InventoryHolder)sender, 54, Main.getInstance().getConfiguration().getMessage("TradeSettings.InventoryTitle"));
        this.receiverInventory = Bukkit.createInventory((InventoryHolder)receiver, 54, Main.getInstance().getConfiguration().getMessage("TradeSettings.InventoryTitle"));
        final List<Integer> wallSlots = Arrays.asList(4, 13, 22, 31, 40, 49);
        final List<Integer> statusSlots = Arrays.asList(48, 50);
        final int acceptedSlot = 45;
        final int declinedSlot = 46;
        final int placeholderSlot = 47;
        this.youSlots = Arrays.asList(0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39);
        this.otherSlots = Arrays.asList(5, 6, 7, 8, 14, 15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35, 41, 42, 43, 44);
        for (final Integer wallSlot : wallSlots) {
            this.senderInventory.setItem((int)wallSlot, ItemStackUtils.getItemParseNewLine(Material.BLACK_STAINED_GLASS_PANE, 1, 0, " "));
            this.receiverInventory.setItem((int)wallSlot, ItemStackUtils.getItemParseNewLine(Material.BLACK_STAINED_GLASS_PANE, 1, 0, " "));
        }
        this.senderInventory.setItem(placeholderSlot, ItemStackUtils.getItemParseNewLine(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, 0, " "));
        this.receiverInventory.setItem(placeholderSlot, ItemStackUtils.getItemParseNewLine(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, 0, " "));
        for (final Integer statusSlot : statusSlots) {
            this.senderInventory.setItem((int)statusSlot, ItemStackUtils.getItemParseNewLine(Material.GRAY_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Empty")));
            this.receiverInventory.setItem((int)statusSlot, ItemStackUtils.getItemParseNewLine(Material.GRAY_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Empty")));
        }
        this.senderInventory.setItem(acceptedSlot, ItemStackUtils.getItemParseNewLine(Material.GREEN_WOOL, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Accept")));
        this.senderInventory.setItem(declinedSlot, ItemStackUtils.getItemParseNewLine(Material.RED_WOOL, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Decline")));
        this.receiverInventory.setItem(acceptedSlot, ItemStackUtils.getItemParseNewLine(Material.GREEN_WOOL, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Accept")));
        this.receiverInventory.setItem(declinedSlot, ItemStackUtils.getItemParseNewLine(Material.RED_WOOL, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Decline")));
        sender.openInventory(this.senderInventory);
        receiver.openInventory(this.receiverInventory);
    }
    
    public boolean isFinished() {
        return this.finished;
    }
    
    public void setFinished() {
        this.finished = true;
        final List<Integer> statusSlots = Arrays.asList(48, 50);
        for (final Integer statusSlot : statusSlots) {
            this.senderInventory.setItem((int)statusSlot, ItemStackUtils.getItemParseNewLine(Material.LIGHT_BLUE_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Traded")));
            this.receiverInventory.setItem((int)statusSlot, ItemStackUtils.getItemParseNewLine(Material.LIGHT_BLUE_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Traded")));
        }
        if (Main.getInstance().getConfiguration().getBoolean("TradeSettings.AutoClose")) {
            this.getSender().closeInventory();
            this.getReceiver().closeInventory();
        }
    }
    
    public List<Integer> getYouSlots() {
        return this.youSlots;
    }
    
    public List<Integer> getOtherSlots() {
        return this.otherSlots;
    }
    
    public Player getSender() {
        return this.sender;
    }
    
    public Player getReceiver() {
        return this.receiver;
    }
    
    public Inventory getSenderInventory() {
        return this.senderInventory;
    }
    
    public Inventory getReceiverInventory() {
        return this.receiverInventory;
    }
    
    public List<ItemStack> getYouItems(final Player player) {
        final List<ItemStack> itemStackList = new ArrayList<ItemStack>();
        if (player.getUniqueId().toString().equals(this.getSender().getUniqueId().toString())) {
            for (final int youSlot : this.getYouSlots()) {
                itemStackList.add(this.getSenderInventory().getItem(youSlot));
            }
        }
        if (player.getUniqueId().toString().equals(this.getReceiver().getUniqueId().toString())) {
            for (final int youSlot : this.getYouSlots()) {
                itemStackList.add(this.getReceiverInventory().getItem(youSlot));
            }
        }
        return itemStackList;
    }
    
    public List<ItemStack> getOtherItems(final Player player) {
        final List<ItemStack> itemStackList = new ArrayList<ItemStack>();
        if (player.getUniqueId().toString().equals(this.getSender().getUniqueId().toString())) {
            for (final int otherSlot : this.getOtherSlots()) {
                itemStackList.add(this.getSenderInventory().getItem(otherSlot));
            }
        }
        if (player.getUniqueId().toString().equals(this.getReceiver().getUniqueId().toString())) {
            for (final int otherSlot : this.getOtherSlots()) {
                itemStackList.add(this.getReceiverInventory().getItem(otherSlot));
            }
        }
        return itemStackList;
    }
    
    public void giveItemsBack() {
        final List<ItemStack> senderItems = this.getYouItems(this.getSender());
        final List<ItemStack> receiverItems = this.getYouItems(this.getReceiver());
        final List<ItemStack> senderDropItems = new ArrayList<ItemStack>();
        final List<ItemStack> receiverDropItems = new ArrayList<ItemStack>();
        for (final ItemStack senderItem : senderItems) {
            if (senderItem != null) {
                for (final ItemStack itemStack : this.getSender().getInventory().addItem(new ItemStack[] { senderItem }).values()) {
                    senderDropItems.add(itemStack);
                }
            }
        }
        for (final ItemStack receiverItem : receiverItems) {
            if (receiverItem != null) {
                for (final ItemStack itemStack : this.getReceiver().getInventory().addItem(new ItemStack[] { receiverItem }).values()) {
                    receiverDropItems.add(itemStack);
                }
            }
        }
        for (final ItemStack dropSenderItem : senderDropItems) {
            if (dropSenderItem != null) {
                this.getSender().getLocation().getWorld().dropItem(this.getSender().getLocation(), dropSenderItem);
            }
        }
        for (final ItemStack dropReceiverItem : receiverDropItems) {
            if (dropReceiverItem != null) {
                this.getReceiver().getLocation().getWorld().dropItem(this.getReceiver().getLocation(), dropReceiverItem);
            }
        }
    }
    
    public boolean isClosed(final Player player) {
        return this.closedPlayers.contains(player.getUniqueId().toString());
    }
    
    public void closeInventory(final Player player) {
        if (!this.closedPlayers.contains(player.getUniqueId().toString())) {
            this.closedPlayers.add(player.getUniqueId().toString());
        }
        if (this.isFinished()) {
            if (this.getSender().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                final List<ItemStack> senderItems = this.getOtherItems(this.getSender());
                final List<ItemStack> senderDropItems = new ArrayList<ItemStack>();
                for (final ItemStack senderItem : senderItems) {
                    if (senderItem != null) {
                        for (final ItemStack itemStack : this.getSender().getInventory().addItem(new ItemStack[] { senderItem }).values()) {
                            if (itemStack != null) {
                                senderDropItems.add(itemStack);
                            }
                        }
                    }
                }
                for (final ItemStack dropSenderItem : senderDropItems) {
                    if (dropSenderItem != null) {
                        this.getSender().getLocation().getWorld().dropItem(this.getSender().getLocation(), dropSenderItem);
                    }
                }
            }
            if (this.getReceiver().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                final List<ItemStack> receiverItems = this.getOtherItems(this.getReceiver());
                final List<ItemStack> receiverDropItems = new ArrayList<ItemStack>();
                for (final ItemStack receiverItem : receiverItems) {
                    if (receiverItem != null) {
                        for (final ItemStack itemStack : this.getReceiver().getInventory().addItem(new ItemStack[] { receiverItem }).values()) {
                            if (itemStack != null) {
                                receiverDropItems.add(itemStack);
                            }
                        }
                    }
                }
                for (final ItemStack dropReceiverItem : receiverDropItems) {
                    if (dropReceiverItem != null) {
                        this.getReceiver().getLocation().getWorld().dropItem(this.getReceiver().getLocation(), dropReceiverItem);
                    }
                }
            }
        }
    }
    
    public void closeInventories(final Player player) {
        if (this.closed) {
            return;
        }
        new BukkitRunnable() {
            public void run() {
                if (TradeInventory.this.getSender().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                    final InventoryView view = TradeInventory.this.getReceiver().getOpenInventory();
                    if (view != null) {
                        view.close();
                    }
                }
                else {
                    final InventoryView view = TradeInventory.this.getSender().getOpenInventory();
                    if (view != null) {
                        view.close();
                    }
                }
            }
        }.runTask((Plugin)Main.getInstance());
        this.getSender().sendMessage(Main.getInstance().getConfiguration().getMessage("TradeCancelled"));
        this.getReceiver().sendMessage(Main.getInstance().getConfiguration().getMessage("TradeCancelled"));
        this.closed = true;
        this.giveItemsBack();
    }
    
    public void addSingleClose() {
        ++this.singleClose;
    }
    
    public boolean isFullyClosed() {
        return this.singleClose >= 2;
    }
    
    public Inventory getInventory(final Inventory clickedInventory) {
        if (clickedInventory.equals(this.getSenderInventory())) {
            return this.getReceiverInventory();
        }
        if (clickedInventory.equals(this.getReceiverInventory())) {
            return this.getSenderInventory();
        }
        return null;
    }
    
    public boolean isInventory(final Inventory inventory) {
        return inventory.equals(this.getSenderInventory()) || inventory.equals(this.getReceiverInventory());
    }
    
    public void updateSlots(final Inventory clickedInventory, final Inventory inventory) {
        this.youSlots = Arrays.asList(0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39);
        this.otherSlots = Arrays.asList(5, 6, 7, 8, 14, 15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35, 41, 42, 43, 44);
        final Map<Integer, ItemStack> youItems = new HashMap<Integer, ItemStack>();
        for (final Integer youId : this.youSlots) {
            final ItemStack itemStack = clickedInventory.getItem((int)youId);
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                youItems.put(youId, itemStack);
            }
        }
        for (final Integer otherId : this.otherSlots) {
            inventory.clear((int)otherId);
        }
        for (final Integer youId : this.youSlots) {
            if (youItems.containsKey(youId)) {
                inventory.setItem(youId + 5, (ItemStack)youItems.get(youId));
            }
        }
    }
    
    public void setItem(final int slot, final Player player) {
        if (this.isFinished()) {
            return;
        }
        if (player.getUniqueId().toString().equals(this.getSender().getUniqueId().toString())) {
            if (slot == 45) {
                this.getSenderInventory().setItem(48, ItemStackUtils.getItemParseNewLine(Material.LIME_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Accepted")));
                this.getReceiverInventory().setItem(50, ItemStackUtils.getItemParseNewLine(Material.LIME_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Accepted")));
                if (this.getSenderInventory().getItem(48).equals((Object)ItemStackUtils.getItemParseNewLine(Material.LIME_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Accepted"))) && this.getSenderInventory().getItem(50).equals((Object)ItemStackUtils.getItemParseNewLine(Material.LIME_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Accepted")))) {
                    this.setFinished();
                }
            }
            else if (slot == 46) {
                this.getSenderInventory().setItem(48, ItemStackUtils.getItemParseNewLine(Material.RED_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Declined")));
                this.getReceiverInventory().setItem(50, ItemStackUtils.getItemParseNewLine(Material.RED_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Declined")));
            }
            else if(slot == -1) {
            	this.getSenderInventory().setItem(48, ItemStackUtils.getItemParseNewLine(Material.GRAY_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Empty")));
            	this.getReceiverInventory().setItem(50, ItemStackUtils.getItemParseNewLine(Material.GRAY_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Empty")));
            	this.getReceiverInventory().setItem(48, ItemStackUtils.getItemParseNewLine(Material.GRAY_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Empty")));
            	this.getSenderInventory().setItem(50, ItemStackUtils.getItemParseNewLine(Material.GRAY_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Empty")));
            }
        }
        if (player.getUniqueId().toString().equals(this.getReceiver().getUniqueId().toString())) {
            if (slot == 45) {
                this.getSenderInventory().setItem(50, ItemStackUtils.getItemParseNewLine(Material.LIME_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Accepted")));
                this.getReceiverInventory().setItem(48, ItemStackUtils.getItemParseNewLine(Material.LIME_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Accepted")));
                if (this.getReceiverInventory().getItem(50).equals((Object)ItemStackUtils.getItemParseNewLine(Material.LIME_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Accepted"))) && this.getReceiverInventory().getItem(48).equals((Object)ItemStackUtils.getItemParseNewLine(Material.LIME_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Accepted")))) {
                    this.setFinished();
                }
            }
            else if (slot == 46) {
                this.getSenderInventory().setItem(50, ItemStackUtils.getItemParseNewLine(Material.RED_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Declined")));
                this.getReceiverInventory().setItem(48, ItemStackUtils.getItemParseNewLine(Material.RED_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Declined")));
            }
            else if(slot == -1) {
            	this.getSenderInventory().setItem(50, ItemStackUtils.getItemParseNewLine(Material.GRAY_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Empty")));
            	this.getReceiverInventory().setItem(48, ItemStackUtils.getItemParseNewLine(Material.GRAY_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Empty")));
            	this.getReceiverInventory().setItem(50, ItemStackUtils.getItemParseNewLine(Material.GRAY_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Empty")));
            	this.getSenderInventory().setItem(48, ItemStackUtils.getItemParseNewLine(Material.GRAY_DYE, 1, 0, Main.getInstance().getConfiguration().getMessage("TradeSettings.Items.Status.Empty")));
            }
        }
    }
}

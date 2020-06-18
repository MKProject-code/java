// 
// Decompiled by Procyon v0.5.36
// 

package eu.mip.alandioda.region.spigot;

import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import java.util.List;
import org.bukkit.Material;
import java.util.Collections;
import java.util.ArrayList;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.World;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import java.util.HashMap;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.Map;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener
{
    String permAdmin;
    String permIgnoreRegionPlayerCantDamage;
    String permIgnoreRegionProjectileDamage;
    String permIgnoreRegionPlayerCantBeDamaged;
    String permIgnoreRegionFallDamageDisabled;
    String permIgnoreRegionPlayerCantDamagePlayer;
    String permIgnoreRegionEntityCantDamagePlayer;
    String permIgnoreRegionHunger;
    String permIgnoreRegionInteract;
    String permIgnoreRegionBuild;
    String permIgnoreRegionBreak;
    String permIgnoreRegionPickUp;
    String permIgnoreRegionItemDrop;
    Map<String, Region> regionList;
    FileConfiguration config;
    
    public main() {
        this.permAdmin = "RegionProtection.admin";
        this.permIgnoreRegionPlayerCantDamage = "RegionProtection.ignore.CantDamage";
        this.permIgnoreRegionProjectileDamage = "RegionProtection.ignore.Projectile";
        this.permIgnoreRegionPlayerCantBeDamaged = "RegionProtection.ignore.CantBeDamaged";
        this.permIgnoreRegionFallDamageDisabled = "RegionProtection.ignore.Fall";
        this.permIgnoreRegionPlayerCantDamagePlayer = "RegionProtection.ignore.PlayerCantDamagePlyer";
        this.permIgnoreRegionEntityCantDamagePlayer = "RegionProtection.ignore.EntityCantDamagePlyer";
        this.permIgnoreRegionHunger = "RegionProtection.ignore.Hunger";
        this.permIgnoreRegionInteract = "RegionProtection.ignore.Interact";
        this.permIgnoreRegionBuild = "RegionProtection.ignore.Build";
        this.permIgnoreRegionBreak = "RegionProtection.ignore.Break";
        this.permIgnoreRegionPickUp = "RegionProtection.ignore.ItemPickUp";
        this.permIgnoreRegionItemDrop = "RegionProtection.ignore.ItemDrop";
        this.regionList = new HashMap<String, Region>();
    }
    
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.reloadConfig();
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveDefaultConfig();
        this.LoadConfig(this.config);
    }
    
    void LoadConfig(final FileConfiguration conf) {
        final ConfigurationSection section = conf.getConfigurationSection("regions");
        if (section != null) {
            final Set<String> regions = (Set<String>)section.getKeys(false);
            for (final String rName : regions) {
                final Region region = new Region();
                final World w = Bukkit.getWorld(conf.getString("regions." + rName + ".world"));
                final Location max = new Location(w, conf.getDouble("regions." + rName + ".posMax.x"), conf.getDouble("regions." + rName + ".posMax.y"), conf.getDouble("regions." + rName + ".posMax.z"));
                final Location min = new Location(w, conf.getDouble("regions." + rName + ".posMin.x"), conf.getDouble("regions." + rName + ".posMin.y"), conf.getDouble("regions." + rName + ".posMin.z"));
                region.boundMax = max;
                region.boundMin = min;
                region.isEnabled = (max != null && min != null);
                region.playerCD = PlayerSetting.valueOf(conf.getString("regions." + rName + ".player.playerCD"));
                region.projectileCD = PlayerSetting.valueOf(conf.getString("regions." + rName + ".player.projectileCD"));
                region.playerCBD = PlayerSetting.valueOf(conf.getString("regions." + rName + ".player.playerCBD"));
                region.fallDD = PlayerSetting.valueOf(conf.getString("regions." + rName + ".player.fallDD"));
                region.playersCD = PlayerSetting.valueOf(conf.getString("regions." + rName + ".player.playersCD"));
                region.entityCD = PlayerSetting.valueOf(conf.getString("regions." + rName + ".player.entityCD"));
                region.hungerChange = PlayerSetting.valueOf(conf.getString("regions." + rName + ".hunger"));
                region.interacts = PlayerSetting.valueOf(conf.getString("regions." + rName + ".interacts"));
                region.explosionDamage = Setting.valueOf(conf.getString("regions." + rName + ".explosion"));
                region.cBuild = PlayerSetting.valueOf(conf.getString("regions." + rName + ".build"));
                region.cBreak = PlayerSetting.valueOf(conf.getString("regions." + rName + ".break"));
                region.pickUpItems = PlayerSetting.valueOf(conf.getString("regions." + rName + ".pickUpItems"));
                region.dropItems = PlayerSetting.valueOf(conf.getString("regions." + rName + ".dropItems"));
                region.grow = Setting.valueOf(conf.getString("regions." + rName + ".grow"));
                region.regionName = rName;
                this.regionList.put(rName, region);
            }
        }
    }
    
    public void onDisable() {
        for (final Map.Entry<String, Region> rEntry : this.regionList.entrySet()) {
            if (rEntry.getValue().boundMax != null && rEntry.getValue().boundMin != null) {
                this.config.set("regions." + rEntry.getKey() + ".world", (Object)rEntry.getValue().boundMax.getWorld().getName());
                this.config.set("regions." + rEntry.getKey() + ".posMax.x", (Object)rEntry.getValue().boundMax.getBlockX());
                this.config.set("regions." + rEntry.getKey() + ".posMax.y", (Object)rEntry.getValue().boundMax.getBlockY());
                this.config.set("regions." + rEntry.getKey() + ".posMax.z", (Object)rEntry.getValue().boundMax.getBlockZ());
                this.config.set("regions." + rEntry.getKey() + ".posMin.x", (Object)rEntry.getValue().boundMin.getBlockX());
                this.config.set("regions." + rEntry.getKey() + ".posMin.y", (Object)rEntry.getValue().boundMin.getBlockY());
                this.config.set("regions." + rEntry.getKey() + ".posMin.z", (Object)rEntry.getValue().boundMin.getBlockZ());
            }
            this.config.set("regions." + rEntry.getKey() + ".player.playerCD", (Object)rEntry.getValue().playerCD.name());
            this.config.set("regions." + rEntry.getKey() + ".player.projectileCD", (Object)rEntry.getValue().projectileCD.name());
            this.config.set("regions." + rEntry.getKey() + ".player.playerCBD", (Object)rEntry.getValue().playerCBD.name());
            this.config.set("regions." + rEntry.getKey() + ".player.fallDD", (Object)rEntry.getValue().fallDD.name());
            this.config.set("regions." + rEntry.getKey() + ".player.playersCD", (Object)rEntry.getValue().playersCD.name());
            this.config.set("regions." + rEntry.getKey() + ".player.entityCD", (Object)rEntry.getValue().entityCD.name());
            this.config.set("regions." + rEntry.getKey() + ".hunger", (Object)rEntry.getValue().hungerChange.name());
            this.config.set("regions." + rEntry.getKey() + ".interacts", (Object)rEntry.getValue().interacts.name());
            this.config.set("regions." + rEntry.getKey() + ".explosion", (Object)rEntry.getValue().explosionDamage.name());
            this.config.set("regions." + rEntry.getKey() + ".build", (Object)rEntry.getValue().cBuild.name());
            this.config.set("regions." + rEntry.getKey() + ".break", (Object)rEntry.getValue().cBreak.name());
            this.config.set("regions." + rEntry.getKey() + ".pickUpItems", (Object)rEntry.getValue().pickUpItems.name());
            this.config.set("regions." + rEntry.getKey() + ".dropItems", (Object)rEntry.getValue().dropItems.name());
            this.config.set("regions." + rEntry.getKey() + ".grow", (Object)rEntry.getValue().grow.name());
        }
        this.saveConfig();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player)sender;
            final int length = args.length;
            if ((cmd.getName().equals("region") || cmd.getName().equals("regionprotection") || cmd.getName().equals("rp")) && player.hasPermission(this.permAdmin)) {
                if (length == 0) {
                    final ChatColor c1 = ChatColor.GOLD;
                    final ChatColor c2 = ChatColor.YELLOW;
                    player.sendMessage(c1 + "---------- Help ----------");
                    player.sendMessage(c2 + "/region add <name>");
                    player.sendMessage(c2 + "/region remove");
                    player.sendMessage(c2 + "/region edit");
                    return true;
                }
                if (length == 1) {
                    if (args[0].equalsIgnoreCase("help")) {
                        final ChatColor c1 = ChatColor.GOLD;
                        final ChatColor c2 = ChatColor.YELLOW;
                        player.sendMessage(c1 + "---------- Help ----------");
                        player.sendMessage(c2 + "/region add <name>");
                        player.sendMessage(c2 + "/region remove");
                        player.sendMessage(c2 + "/region edit");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("edit")) {
                        this.OpenInventory(player, 1, InvType.EDIT);
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("remove")) {
                        this.OpenInventory(player, 1, InvType.REMOVE);
                        return true;
                    }
                }
                else if (length == 2 && args[0].equalsIgnoreCase("add")) {
                    if (!this.regionList.containsKey(args[1])) {
                        final Region reg = new Region();
                        reg.regionName = args[1];
                        this.regionList.put(args[1], reg);
                        player.sendMessage(ChatColor.GREEN + "Region Created!");
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "Region already exists!");
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    void OpenInventory(final Player player, final int i, final InvType type) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 54, "Select a region " + (type.equals(InvType.REMOVE) ? "to remove" : "to edit"));
        final List<String> regionNames = new ArrayList<String>();
        final Set<String> regions = this.regionList.keySet();
        for (final String s : regions) {
            regionNames.add(s.toString());
        }
        Collections.sort(regionNames);
        for (int a = 0; a < 45; ++a) {
            if (regionNames.size() > a + (i - 1) * 45) {
                inv.setItem(a, GeneralMethods.CreateItem(regionNames.get(a + (i - 1) * 45), GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + (type.equals(InvType.REMOVE) ? "Click to remove" : "Click to edit") }), Material.NAME_TAG));
            }
        }
        if (i > 1) {
            inv.setItem(45, GeneralMethods.CreateItem("Back", null, "http://textures.minecraft.net/texture/5f133e91919db0acefdc272d67fd87b4be88dc44a958958824474e21e06d53e6"));
        }
        if (i * 45 < regionNames.size()) {
            inv.setItem(53, GeneralMethods.CreateItem("Next", null, "http://textures.minecraft.net/texture/e3fc52264d8ad9e654f415bef01a23947edbccccf649373289bea4d149541f70"));
        }
        inv.setItem(49, GeneralMethods.CreateItem("Page:", GeneralMethods.arrayToList(new String[] { new StringBuilder().append(i).toString() }), "http://textures.minecraft.net/texture/fc271052719ef64079ee8c1498951238a74dac4c27b95640db6fbddc2d6b5b6e"));
        player.openInventory(inv);
    }
    
    void RemoveRegion(final String regionName) {
        this.regionList.remove(regionName);
        this.config.set("regions." + regionName + ".world", (Object)null);
        this.config.set("regions." + regionName + ".posMax.x", (Object)null);
        this.config.set("regions." + regionName + ".posMax.y", (Object)null);
        this.config.set("regions." + regionName + ".posMax.z", (Object)null);
        this.config.set("regions." + regionName + ".posMin.x", (Object)null);
        this.config.set("regions." + regionName + ".posMin.y", (Object)null);
        this.config.set("regions." + regionName + ".posMin.z", (Object)null);
        this.config.set("regions." + regionName + ".player.playerCD", (Object)null);
        this.config.set("regions." + regionName + ".player.projectileCD", (Object)null);
        this.config.set("regions." + regionName + ".player.playerCBD", (Object)null);
        this.config.set("regions." + regionName + ".player.fallDD", (Object)null);
        this.config.set("regions." + regionName + ".player.playersCD", (Object)null);
        this.config.set("regions." + regionName + ".player.entityCD", (Object)null);
        this.config.set("regions." + regionName + ".hunger", (Object)null);
        this.config.set("regions." + regionName + ".interacts", (Object)null);
        this.config.set("regions." + regionName + ".explosion", (Object)null);
        this.config.set("regions." + regionName + ".build", (Object)null);
        this.config.set("regions." + regionName + ".break", (Object)null);
        this.config.set("regions." + regionName + ".pickUpItems", (Object)null);
        this.config.set("regions." + regionName + ".dropItems", (Object)null);
        this.config.set("regions." + regionName + ".grow", (Object)null);
        this.config.set("regions." + regionName, (Object)null);
        this.saveConfig();
    }
    
    void EditRegion(final Player player, final String displayName) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 54, "Edit Region: " + displayName);
        final Region region = this.regionList.get(displayName);
        if (region.boundMax != null) {
            inv.setItem(3, GeneralMethods.CreateItem("Bound1", GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + "World: " + region.boundMax.getWorld().getName(), new StringBuilder().append(ChatColor.GOLD).append(region.boundMax.getBlockX()).append(", ").append(region.boundMax.getBlockY()).append(", ").append(region.boundMax.getBlockZ()).toString() }), Material.LIME_TERRACOTTA));
        }
        else {
            inv.setItem(3, GeneralMethods.CreateItem("Bound1", GeneralMethods.arrayToList(new String[] { ChatColor.RED + "Not set yet!" }), Material.LIME_TERRACOTTA));
        }
        if (region.boundMin != null) {
            inv.setItem(5, GeneralMethods.CreateItem("Bound2", GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + "World: " + region.boundMin.getWorld().getName(), new StringBuilder().append(ChatColor.GOLD).append(region.boundMin.getBlockX()).append(", ").append(region.boundMin.getBlockY()).append(", ").append(region.boundMin.getBlockZ()).toString() }), Material.YELLOW_TERRACOTTA));
        }
        else {
            inv.setItem(5, GeneralMethods.CreateItem("Bound2", GeneralMethods.arrayToList(new String[] { ChatColor.RED + "Not set yet!" }), Material.YELLOW_TERRACOTTA));
        }
        inv.setItem(19, GeneralMethods.CreateItem("Player can't damage", GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + "Set: " + region.playerCD, ChatColor.GOLD + "Perm: " + ChatColor.YELLOW + "RegionProtect." + displayName + ".PlayerDamage" }), Material.DIAMOND_SWORD));
        inv.setItem(20, GeneralMethods.CreateItem("Projectile can't damage", GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + "Set: " + region.projectileCD, ChatColor.GOLD + "Perm: " + ChatColor.YELLOW + "RegionProtect." + displayName + ".ProjectileDamage" }), Material.FIRE_CHARGE));
        inv.setItem(21, GeneralMethods.CreateItem("Player can't be damaged", GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + "Set: " + region.playerCBD, ChatColor.GOLD + "Perm: " + ChatColor.YELLOW + "RegionProtect." + displayName + ".PlayerBeDamaged" }), Material.SHIELD));
        inv.setItem(23, GeneralMethods.CreateItem("Fall damage disabled", GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + "Set: " + region.fallDD, ChatColor.GOLD + "Perm: " + ChatColor.YELLOW + "RegionProtect." + displayName + ".FallDamageDisabled" }), Material.DIAMOND_BOOTS));
        inv.setItem(24, GeneralMethods.CreateItem("Player can't damage player", GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + "Set: " + region.playersCD, ChatColor.GOLD + "Perm: " + ChatColor.YELLOW + "RegionProtect." + displayName + ".PlayerDamagePlayer" }), Material.PLAYER_HEAD));
        inv.setItem(25, GeneralMethods.CreateItem("Entity can't damage player", GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + "Set: " + region.entityCD, ChatColor.GOLD + "Perm: " + ChatColor.YELLOW + "RegionProtect." + displayName + ".EntityDamagePlayer" }), Material.ROTTEN_FLESH));
        inv.setItem(36, GeneralMethods.CreateItem("Hunger doesn't change", GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + "Set: " + region.hungerChange, ChatColor.GOLD + "Perm: " + ChatColor.YELLOW + "RegionProtect." + displayName + ".Hunger" }), Material.COOKED_CHICKEN));
        inv.setItem(37, GeneralMethods.CreateItem("Can't interact", GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + "Set: " + region.interacts, ChatColor.GOLD + "Perm: " + ChatColor.YELLOW + "RegionProtect." + displayName + ".Interact" }), Material.ITEM_FRAME));
        inv.setItem(38, GeneralMethods.CreateItem("Explosions can't damage", GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + "Set: " + region.explosionDamage }), Material.TNT));
        inv.setItem(39, GeneralMethods.CreateItem("Can't build", GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + "Set: " + region.cBuild, ChatColor.GOLD + "Perm: " + ChatColor.YELLOW + "RegionProtect." + displayName + ".Build" }), Material.GRASS_BLOCK));
        inv.setItem(41, GeneralMethods.CreateItem("Can't break", GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + "Set: " + region.cBreak, ChatColor.GOLD + "Perm: " + ChatColor.YELLOW + "RegionProtect." + displayName + ".Break" }), Material.DIAMOND_PICKAXE));
        inv.setItem(42, GeneralMethods.CreateItem("Can't pick up items", GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + "Set: " + region.pickUpItems, ChatColor.GOLD + "Perm: " + ChatColor.YELLOW + "RegionProtect." + displayName + ".PickUp" }), Material.STRING));
        inv.setItem(43, GeneralMethods.CreateItem("Can't drop items", GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + "Set: " + region.dropItems, ChatColor.GOLD + "Perm: " + ChatColor.YELLOW + "RegionProtect." + displayName + ".Drop" }), Material.SUGAR_CANE));
        inv.setItem(44, GeneralMethods.CreateItem("Can't grow", GeneralMethods.arrayToList(new String[] { ChatColor.GOLD + "Set: " + region.grow }), Material.GRASS));
        player.openInventory(inv);
    }
    
    @EventHandler
    public void onPlayerClick(final InventoryClickEvent e) {
        final Player player = (Player)e.getWhoClicked();
        final ItemStack item = e.getCurrentItem();
        if (item != null && !item.getType().equals((Object)Material.AIR) && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            if (e.getView().getTitle().equals("Select a region to remove")) {
                e.setCancelled(true);
                if (e.getRawSlot() == 53) {
                    final List<String> lore = (List<String>)e.getInventory().getItem(49).getItemMeta().getLore();
                    this.OpenInventory(player, Integer.parseInt(lore.get(0)) + 1, InvType.REMOVE);
                }
                else if (e.getRawSlot() == 45) {
                    final List<String> lore = (List<String>)e.getInventory().getItem(49).getItemMeta().getLore();
                    this.OpenInventory(player, Integer.parseInt(lore.get(0)) - 1, InvType.REMOVE);
                }
                else if (e.getRawSlot() < 45) {
                    this.RemoveRegion(e.getCurrentItem().getItemMeta().getDisplayName());
                    this.OpenInventory(player, 1, InvType.REMOVE);
                }
            }
            else if (e.getView().getTitle().equals("Select a region to edit")) {
                e.setCancelled(true);
                if (e.getRawSlot() == 53) {
                    final List<String> lore = (List<String>)e.getInventory().getItem(49).getItemMeta().getLore();
                    this.OpenInventory(player, Integer.parseInt(lore.get(0)) + 1, InvType.EDIT);
                }
                else if (e.getRawSlot() == 45) {
                    final List<String> lore = (List<String>)e.getInventory().getItem(49).getItemMeta().getLore();
                    this.OpenInventory(player, Integer.parseInt(lore.get(0)) - 1, InvType.EDIT);
                }
                else if (e.getRawSlot() < 45) {
                    this.EditRegion(player, e.getCurrentItem().getItemMeta().getDisplayName());
                }
            }
            else if (e.getView().getTitle().startsWith("Edit Region: ")) {
                e.setCancelled(true);
                final String regionName = e.getView().getTitle().replaceAll("Edit Region: ", "");
                final String buttonName = e.getCurrentItem().getItemMeta().getDisplayName();
                final Region region = this.regionList.get(regionName);
                if (buttonName.equals("Bound1")) {
                    this.CalculateMaxAndMin(region, region.boundMax = player.getLocation().getBlock().getLocation(), region.boundMin);
                }
                else if (buttonName.equals("Bound2")) {
                    region.boundMin = player.getLocation().getBlock().getLocation();
                    this.CalculateMaxAndMin(region, region.boundMax, region.boundMin);
                }
                else if (buttonName.equals("Player can't damage")) {
                    region.playerCD = (region.playerCD.equals(PlayerSetting.OFF) ? PlayerSetting.ALL : (region.playerCD.equals(PlayerSetting.ALL) ? PlayerSetting.PERMISSION : PlayerSetting.OFF));
                }
                else if (buttonName.equals("Projectile can't damage")) {
                    region.projectileCD = (region.projectileCD.equals(PlayerSetting.OFF) ? PlayerSetting.ALL : (region.projectileCD.equals(PlayerSetting.ALL) ? PlayerSetting.PERMISSION : PlayerSetting.OFF));
                }
                else if (buttonName.equals("Player can't be damaged")) {
                    region.playerCBD = (region.playerCBD.equals(PlayerSetting.OFF) ? PlayerSetting.ALL : (region.playerCBD.equals(PlayerSetting.ALL) ? PlayerSetting.PERMISSION : PlayerSetting.OFF));
                }
                else if (buttonName.equals("Fall damage disabled")) {
                    region.fallDD = (region.fallDD.equals(PlayerSetting.OFF) ? PlayerSetting.ALL : (region.fallDD.equals(PlayerSetting.ALL) ? PlayerSetting.PERMISSION : PlayerSetting.OFF));
                }
                else if (buttonName.equals("Player can't damage player")) {
                    region.playersCD = (region.playersCD.equals(PlayerSetting.OFF) ? PlayerSetting.ALL : (region.playersCD.equals(PlayerSetting.ALL) ? PlayerSetting.PERMISSION : PlayerSetting.OFF));
                }
                else if (buttonName.equals("Entity can't damage player")) {
                    region.entityCD = (region.entityCD.equals(PlayerSetting.OFF) ? PlayerSetting.ALL : (region.entityCD.equals(PlayerSetting.ALL) ? PlayerSetting.PERMISSION : PlayerSetting.OFF));
                }
                else if (buttonName.equals("Hunger doesn't change")) {
                    region.hungerChange = (region.hungerChange.equals(PlayerSetting.OFF) ? PlayerSetting.ALL : (region.hungerChange.equals(PlayerSetting.ALL) ? PlayerSetting.PERMISSION : PlayerSetting.OFF));
                }
                else if (buttonName.equals("Can't interact")) {
                    region.interacts = (region.interacts.equals(PlayerSetting.OFF) ? PlayerSetting.ALL : (region.interacts.equals(PlayerSetting.ALL) ? PlayerSetting.PERMISSION : PlayerSetting.OFF));
                }
                else if (buttonName.equals("Explosions can't damage")) {
                    region.explosionDamage = (region.explosionDamage.equals(Setting.OFF) ? Setting.ON : Setting.OFF);
                }
                else if (buttonName.equals("Can't build")) {
                    region.cBuild = (region.cBuild.equals(PlayerSetting.OFF) ? PlayerSetting.ALL : (region.cBuild.equals(PlayerSetting.ALL) ? PlayerSetting.PERMISSION : PlayerSetting.OFF));
                }
                else if (buttonName.equals("Can't break")) {
                    region.cBreak = (region.cBreak.equals(PlayerSetting.OFF) ? PlayerSetting.ALL : (region.cBreak.equals(PlayerSetting.ALL) ? PlayerSetting.PERMISSION : PlayerSetting.OFF));
                }
                else if (buttonName.equals("Can't pick up items")) {
                    region.pickUpItems = (region.pickUpItems.equals(PlayerSetting.OFF) ? PlayerSetting.ALL : (region.pickUpItems.equals(PlayerSetting.ALL) ? PlayerSetting.PERMISSION : PlayerSetting.OFF));
                }
                else if (buttonName.equals("Can't drop items")) {
                    region.dropItems = (region.dropItems.equals(PlayerSetting.OFF) ? PlayerSetting.ALL : (region.dropItems.equals(PlayerSetting.ALL) ? PlayerSetting.PERMISSION : PlayerSetting.OFF));
                }
                else if (buttonName.equals("Can't grow")) {
                    region.grow = (region.grow.equals(Setting.OFF) ? Setting.ON : Setting.OFF);
                }
                this.EditRegion(player, regionName);
            }
        }
    }
    
    @EventHandler
    public void onPlayerAttackedByEntity(final EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            if (!e.getDamager().hasPermission(this.permIgnoreRegionPlayerCantDamage)) {
                final List<Region> regions = this.isInsideOfRegion(e.getEntity().getLocation());
                for (final Region r : regions) {
                    if (r.playerCD.equals(PlayerSetting.ALL)) {
                        e.setCancelled(true);
                        return;
                    }
                    if (r.playerCD.equals(PlayerSetting.PERMISSION) && e.getDamager().hasPermission("RegionProtect." + r.regionName + ".PlayerDamage")) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
            if (e.getEntity() instanceof Player && !e.getEntity().hasPermission(this.permIgnoreRegionPlayerCantDamagePlayer)) {
                final List<Region> regions = this.isInsideOfRegion(e.getEntity().getLocation());
                for (final Region r : regions) {
                    if (r.playersCD.equals(PlayerSetting.ALL)) {
                        e.setCancelled(true);
                        return;
                    }
                    if (r.playersCD.equals(PlayerSetting.PERMISSION) && e.getDamager().hasPermission("RegionProtect." + r.regionName + ".PlayerDamagePlayer")) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
            if (e.getEntity() instanceof ItemFrame && !e.getDamager().hasPermission(this.permIgnoreRegionInteract)) {
                final List<Region> regions = this.isInsideOfRegion(e.getEntity().getLocation());
                for (final Region r : regions) {
                    if (r.interacts.equals(PlayerSetting.ALL)) {
                        e.setCancelled(true);
                        return;
                    }
                    if (r.interacts.equals(PlayerSetting.PERMISSION) && e.getDamager().hasPermission("RegionProtect." + r.regionName + ".Interact")) {
                        e.setCancelled(true);
                    }
                }
            }
        }
        else if (e.getEntity() instanceof Player) {
            if (e.getDamager() instanceof Projectile && !e.getEntity().hasPermission(this.permIgnoreRegionProjectileDamage)) {
                final List<Region> regions = this.isInsideOfRegion(e.getEntity().getLocation());
                for (final Region r : regions) {
                    if (r.projectileCD.equals(PlayerSetting.ALL)) {
                        e.setCancelled(true);
                        return;
                    }
                    if (r.projectileCD.equals(PlayerSetting.PERMISSION) && e.getDamager().hasPermission("RegionProtect." + r.regionName + ".ProjectileDamage")) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
            if (!e.getDamager().hasPermission(this.permIgnoreRegionEntityCantDamagePlayer)) {
                final List<Region> regions = this.isInsideOfRegion(e.getEntity().getLocation());
                for (final Region r : regions) {
                    if (r.entityCD.equals(PlayerSetting.ALL)) {
                        e.setCancelled(true);
                        return;
                    }
                    if (r.entityCD.equals(PlayerSetting.PERMISSION) && e.getDamager().hasPermission("RegionProtect." + r.regionName + ".EntityDamagePlayer")) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!e.getEntity().hasPermission(this.permIgnoreRegionPlayerCantBeDamaged)) {
                final List<Region> regions = this.isInsideOfRegion(e.getEntity().getLocation());
                for (final Region r : regions) {
                    if (r.playerCBD.equals(PlayerSetting.ALL)) {
                        e.setCancelled(true);
                        return;
                    }
                    if (r.playerCBD.equals(PlayerSetting.PERMISSION) && e.getEntity().hasPermission("RegionProtect." + r.regionName + ".PlayerBeDamaged")) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
            if (e.getCause().equals((Object)EntityDamageEvent.DamageCause.FALL) && !e.getEntity().hasPermission(this.permIgnoreRegionFallDamageDisabled)) {
                final List<Region> regions = this.isInsideOfRegion(e.getEntity().getLocation());
                for (final Region r : regions) {
                    if (r.fallDD.equals(PlayerSetting.ALL)) {
                        e.setCancelled(true);
                        return;
                    }
                    if (r.fallDD.equals(PlayerSetting.PERMISSION) && e.getEntity().hasPermission("RegionProtect." + r.regionName + ".FallDamageDisabled")) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerHunger(final FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player && !e.getEntity().hasPermission(this.permIgnoreRegionHunger)) {
            final List<Region> regions = this.isInsideOfRegion(e.getEntity().getLocation());
            for (final Region r : regions) {
                if (r.hungerChange.equals(PlayerSetting.ALL)) {
                    e.setCancelled(true);
                    return;
                }
                if (r.hungerChange.equals(PlayerSetting.PERMISSION) && e.getEntity().hasPermission("RegionProtect." + r.regionName + ".Hunger")) {
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onBlockGrow(final BlockGrowEvent e) {
        final List<Region> regions = this.isInsideOfRegion(e.getBlock().getLocation());
        for (final Region r : regions) {
            if (r.grow.equals(Setting.ON)) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onBlockSpread(final BlockSpreadEvent e) {
        final List<Region> regions = this.isInsideOfRegion(e.getBlock().getLocation());
        for (final Region r : regions) {
            if (r.grow.equals(Setting.ON)) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onLeavesDecay(final LeavesDecayEvent e) {
        final List<Region> regions = this.isInsideOfRegion(e.getBlock().getLocation());
        for (final Region r : regions) {
            if (r.grow.equals(Setting.ON)) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onBlockExplode(final BlockExplodeEvent e) {
        final List<Region> regions = this.isInsideOfRegion(e.getBlock().getLocation());
        for (final Region r : regions) {
            if (r.explosionDamage.equals(Setting.ON)) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onEntityExplode(final EntityExplodeEvent e) {
        final List<Region> regions = this.isInsideOfRegion(e.getEntity().getLocation());
        for (final Region r : regions) {
            if (r.explosionDamage.equals(Setting.ON)) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onBuild(final BlockPlaceEvent e) {
        if (!e.getPlayer().hasPermission(this.permIgnoreRegionBreak)) {
            final List<Region> regions = this.isInsideOfRegion(e.getBlock().getLocation());
            for (final Region r : regions) {
                if (r.cBuild.equals(PlayerSetting.ALL)) {
                    e.setCancelled(true);
                    return;
                }
                if (r.cBuild.equals(PlayerSetting.PERMISSION) && e.getPlayer().hasPermission("RegionProtect." + r.regionName + ".Build")) {
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onBreak(final BlockBreakEvent e) {
        if (!e.getPlayer().hasPermission(this.permIgnoreRegionBuild)) {
            final List<Region> regions = this.isInsideOfRegion(e.getBlock().getLocation());
            for (final Region r : regions) {
                if (r.cBreak.equals(PlayerSetting.ALL)) {
                    e.setCancelled(true);
                    return;
                }
                if (r.cBreak.equals(PlayerSetting.PERMISSION) && e.getPlayer().hasPermission("RegionProtect." + r.regionName + ".Break")) {
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onPickUp(final EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player && !e.getEntity().hasPermission(this.permIgnoreRegionItemDrop)) {
            final List<Region> regions = this.isInsideOfRegion(e.getItem().getLocation());
            for (final Region r : regions) {
                if (r.pickUpItems.equals(PlayerSetting.ALL)) {
                    e.setCancelled(true);
                    return;
                }
                if (r.pickUpItems.equals(PlayerSetting.PERMISSION) && e.getEntity().hasPermission("RegionProtect." + r.regionName + ".PickUp")) {
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onDrop(final PlayerDropItemEvent e) {
        if (!e.getPlayer().hasPermission(this.permIgnoreRegionItemDrop)) {
            final List<Region> regions = this.isInsideOfRegion(e.getPlayer().getLocation());
            for (final Region r : regions) {
                if (r.dropItems.equals(PlayerSetting.ALL)) {
                    e.setCancelled(true);
                    return;
                }
                if (r.dropItems.equals(PlayerSetting.PERMISSION) && e.getPlayer().hasPermission("RegionProtect." + r.regionName + ".Drop")) {
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerInt(final PlayerInteractEvent e) {
        if (!e.getPlayer().hasPermission(this.permIgnoreRegionInteract) && (e.getAction().equals((Object)Action.PHYSICAL) || (e.getClickedBlock() != null && (e.getClickedBlock().getType().name().endsWith("BED") || e.getClickedBlock().getType().equals((Object)Material.ITEM_FRAME) || e.getClickedBlock().getType().equals((Object)Material.FARMLAND) || e.getClickedBlock().getType().name().endsWith("BUTTON") || e.getClickedBlock().getType().name().endsWith("TRAPDOOR"))))) {
            final List<Region> regions = this.isInsideOfRegion(e.getPlayer().getLocation());
            for (final Region r : regions) {
                if (r.interacts.equals(PlayerSetting.ALL)) {
                    e.setCancelled(true);
                    return;
                }
                if (r.interacts.equals(PlayerSetting.PERMISSION) && e.getPlayer().hasPermission("RegionProtect." + r.regionName + ".Interact")) {
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent e) {
        if (!e.getPlayer().hasPermission(this.permIgnoreRegionInteract) && e.getRightClicked() instanceof ItemFrame) {
            final List<Region> regions = this.isInsideOfRegion(e.getRightClicked().getLocation());
            for (final Region r : regions) {
                if (r.interacts.equals(PlayerSetting.ALL)) {
                    e.setCancelled(true);
                    return;
                }
                if (r.interacts.equals(PlayerSetting.PERMISSION) && e.getPlayer().hasPermission("RegionProtect." + r.regionName + ".Interact")) {
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onBreakHanging(final HangingBreakByEntityEvent e) {
        if (!e.getRemover().hasPermission(this.permIgnoreRegionInteract)) {
            final List<Region> regions = this.isInsideOfRegion(e.getEntity().getLocation());
            for (final Region r : regions) {
                if (r.interacts.equals(PlayerSetting.ALL)) {
                    e.setCancelled(true);
                    return;
                }
                if (r.interacts.equals(PlayerSetting.PERMISSION) && e.getRemover().hasPermission("RegionProtect." + r.regionName + ".Interact")) {
                    e.setCancelled(true);
                }
            }
        }
    }
    
    void CalculateMaxAndMin(final Region region, final Location boundMax, final Location boundMin) {
        if (boundMax != null && boundMin != null) {
            if (!region.isEnabled) {
                region.isEnabled = true;
            }
            final int maxX = (boundMax.getBlockX() > boundMin.getBlockX()) ? boundMax.getBlockX() : boundMin.getBlockX();
            final int maxY = (boundMax.getBlockY() > boundMin.getBlockY()) ? boundMax.getBlockY() : boundMin.getBlockY();
            final int maxZ = (boundMax.getBlockZ() > boundMin.getBlockZ()) ? boundMax.getBlockZ() : boundMin.getBlockZ();
            final int minX = (boundMax.getBlockX() < boundMin.getBlockX()) ? boundMax.getBlockX() : boundMin.getBlockX();
            final int minY = (boundMax.getBlockY() < boundMin.getBlockY()) ? boundMax.getBlockY() : boundMin.getBlockY();
            final int minZ = (boundMax.getBlockZ() < boundMin.getBlockZ()) ? boundMax.getBlockZ() : boundMin.getBlockZ();
            region.boundMax = new Location(boundMax.getWorld(), (double)maxX, (double)maxY, (double)maxZ);
            region.boundMin = new Location(boundMax.getWorld(), (double)minX, (double)minY, (double)minZ);
        }
    }
    
    List<Region> isInsideOfRegion(final Location location) {
        final List<Region> regions = new ArrayList<Region>();
        for (final Map.Entry<String, Region> regionEntry : this.regionList.entrySet()) {
            if (regionEntry.getValue().boundMax != null && regionEntry.getValue().boundMin != null && location.getWorld().equals(regionEntry.getValue().boundMax.getWorld())) {
                final boolean isInsidexpos = location.getX() < regionEntry.getValue().boundMax.getBlockX();
                final boolean isInsideypos = location.getY() < regionEntry.getValue().boundMax.getBlockY();
                final boolean isInsidezpos = location.getZ() < regionEntry.getValue().boundMax.getBlockZ();
                final boolean isInsidexneg = location.getX() > regionEntry.getValue().boundMin.getBlockX();
                final boolean isInsideyneg = location.getY() > regionEntry.getValue().boundMin.getBlockY();
                final boolean isInsidezneg = location.getZ() > regionEntry.getValue().boundMin.getBlockZ();
                if (!isInsidexpos || !isInsideypos || !isInsidezpos || !isInsidexneg || !isInsideyneg || !isInsidezneg) {
                    continue;
                }
                regions.add(regionEntry.getValue());
            }
        }
        return regions;
    }
    
    public enum InvType
    {
        EDIT("EDIT", 0), 
        REMOVE("REMOVE", 1);
        
        private InvType(final String name, final int ordinal) {
        }
    }
    
    public enum PlayerSetting
    {
        OFF("OFF", 0), 
        ALL("ALL", 1), 
        PERMISSION("PERMISSION", 2);
        
        private PlayerSetting(final String name, final int ordinal) {
        }
    }
    
    public enum Setting
    {
        OFF("OFF", 0), 
        ON("ON", 1);
        
        private Setting(final String name, final int ordinal) {
        }
    }
}

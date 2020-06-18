package other;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.List;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CompassOriginal extends JavaPlugin implements Listener
{
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
    }
    
    public void setMetadata(final Metadatable object, final String key, final Object value, final Plugin plugin) {
        object.setMetadata(key, (MetadataValue)new FixedMetadataValue(plugin, value));
    }
    
    public void removeMetadata(final Metadatable object, final String key, final Plugin plugin) {
        object.removeMetadata(key, plugin);
    }
    
    public Object getMetadata(final Metadatable object, final String key, final Plugin plugin) {
        final List<MetadataValue> values = (List<MetadataValue>)object.getMetadata(key);
        for (final MetadataValue value : values) {
            if (value.getOwningPlugin() == plugin) {
                return value.value();
            }
        }
        return null;
    }
    
    private void addTracker(final Player requester, final Player toTrack) {
        String data = (String)this.getMetadata((Metadatable)toTrack, "compasstarget", (Plugin)this);
        if (data == null) {
            data = requester.getName();
        }
        else {
            data = String.valueOf(data) + "," + requester.getName();
        }
        this.setMetadata((Metadatable)toTrack, "compasstrackers", data, (Plugin)this);
        this.setMetadata((Metadatable)requester, "compasstracking", toTrack.getName(), (Plugin)this);
    }
    
    private void removeRequesterFromTrackingOnly(final Player p, final String requester) {
        final String data = (String)this.getMetadata((Metadatable)p, "compasstrackers", (Plugin)this);
        if (data == null) {
            return;
        }
        final String[] players = data.split(",");
        String newData = "";
        String[] array;
        for (int length = (array = players).length, i = 0; i < length; ++i) {
            final String player = array[i];
            if (player != requester) {
                newData = String.valueOf(newData) + player + ",";
            }
        }
        if (newData == "") {
            this.removeMetadata((Metadatable)p, "compasstrackers", (Plugin)this);
        }
        else {
            newData = newData.substring(0, newData.length() - 2);
            this.setMetadata((Metadatable)p, "compasstrackers", newData, (Plugin)this);
        }
    }
    
    private void removeTrackerFromRequester(final Player requester) {
        final String toTrack = (String)this.getMetadata((Metadatable)requester, "compasstracking", (Plugin)this);
        this.removeMetadata((Metadatable)requester, "compasstracking", (Plugin)this);
        if (toTrack == null) {
            return;
        }
        final Player p = Bukkit.getServer().getPlayer(toTrack);
        if (p != null) {
            this.removeRequesterFromTrackingOnly(p, requester.getName());
        }
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("compass")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to set your own compass!");
                return false;
            }
            if (args.length < 1) {
                sender.sendMessage("No arguments specified! View \"compass help\" for help.");
                return false;
            }
            final Player p = (Player)sender;
            Label_1702: {
                Label_1665: {
                    Label_1547: {
                        Label_1496: {
                            Label_1445: {
                                Label_1394: {
                                    switch (args[0].toLowerCase()) {
                                        case "follow": {
                                            break;
                                        }
                                        case "e": {
                                            break Label_1445;
                                        }
                                        case "f": {
                                            break;
                                        }
                                        case "n": {
                                            break Label_1394;
                                        }
                                        case "r": {
                                            break Label_1665;
                                        }
                                        case "s": {
                                            break Label_1496;
                                        }
                                        case "w": {
                                            break Label_1547;
                                        }
                                        case "bed": {
                                            if (p.getBedSpawnLocation() != null) {
                                                p.setCompassTarget(p.getBedSpawnLocation());
                                                p.sendMessage("Your compass has been set to your bed's location");
                                                return true;
                                            }
                                            p.sendMessage(ChatColor.RED + "You don't have a bed");
                                            return false;
                                        }
                                        case "pos": {
                                            if (args.length < 2) {
                                                p.sendMessage(ChatColor.RED + "Usage: /compass pos current or /compass pos <x> <y> <z>");
                                                return false;
                                            }
                                            if (args[1].toLowerCase().startsWith("c")) {
                                                p.setCompassTarget(p.getLocation());
                                                p.sendMessage("Your compass is now pointing to your current location.");
                                                return true;
                                            }
                                            if (args.length < 4) {
                                                p.sendMessage(ChatColor.RED + "Usage: /compass pos current or /compass pos <x> <y> <z>");
                                                return false;
                                            }
                                            try {
                                                final int x = Integer.parseInt(args[1]);
                                                final int y = Integer.parseInt(args[2]);
                                                final int z = Integer.parseInt(args[3]);
                                                p.setCompassTarget(p.getWorld().getBlockAt(x, y, z).getLocation());
                                                p.sendMessage("Your compass is now pointing to " + x + ", " + y + ", " + z);
                                                return true;
                                            }
                                            catch (Exception ex) {
                                                p.sendMessage("Failed to convert positions");
                                                return false;
                                            }
                                        }
                                        case "east": {
                                            break Label_1445;
                                        }
                                        case "help": {
                                            p.sendMessage(ChatColor.GOLD + "/compass [direction]" + ChatColor.GRAY + " - sets your compass direction to North, West, East or South.");
                                            p.sendMessage(ChatColor.GOLD + "/compass pos <x> <y> <z>" + ChatColor.GRAY + " - sets your compass direction to the specific location.");
                                            p.sendMessage(ChatColor.GOLD + "/compass pos current" + ChatColor.GRAY + " - sets your compass direction to your current position.");
                                            p.sendMessage(ChatColor.GOLD + "/compass follow <player>" + ChatColor.GRAY + " - sets your compass direction to someone else's" + " position. Keeps updating.");
                                            p.sendMessage(ChatColor.GOLD + "/compass bed" + ChatColor.GRAY + " - sets your compass to your bed's location");
                                            p.sendMessage(ChatColor.GOLD + "To make your compass normal again, use /compass reset.");
                                            p.sendMessage(ChatColor.GOLD + "To modify someone else's compass, use /setplayercompass and then anything listed here.");
                                            return true;
                                        }
                                        case "west": {
                                            break Label_1547;
                                        }
                                        case "about": {
                                            final PluginDescriptionFile pdf = this.getDescription();
                                            final String name = "PoleCompass";
                                            final String des = pdf.getDescription();
                                            final String ver = pdf.getVersion();
                                            final String url = pdf.getWebsite();
                                            final String aut = pdf.getAuthors().get(0);
                                            if (sender instanceof Player) {
                                                p.sendMessage(ChatColor.AQUA + name + " " + ver);
                                                p.sendMessage(ChatColor.GOLD + des);
                                                p.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.RESET + aut);
                                                p.sendMessage(ChatColor.GOLD + "URL: " + ChatColor.RESET + url);
                                            }
                                            else {
                                                sender.sendMessage(String.valueOf(name) + " " + ver);
                                                sender.sendMessage(des);
                                                sender.sendMessage("Author: " + aut);
                                                sender.sendMessage("URL: " + url);
                                            }
                                            return true;
                                        }
                                        case "north": {
                                            break Label_1394;
                                        }
                                        case "reset": {
                                            break Label_1665;
                                        }
                                        case "south": {
                                            break Label_1496;
                                        }
                                        case "default": {
                                            break Label_1665;
                                        }
                                        default:
                                            break Label_1702;
                                    }
                                    if (args.length < 2) {
                                        p.sendMessage(ChatColor.RED + "Syntax error: Please enter a player to point to with your compass");
                                        return false;
                                    }
                                    final Player toFollow = Bukkit.getServer().getPlayer(args[1]);
                                    if (toFollow != null) {
                                        p.setCompassTarget(toFollow.getLocation());
                                        this.addTracker(p, toFollow);
                                        p.sendMessage("You now follow " + toFollow.getName() + " with your compass!");
                                        return true;
                                    }
                                    p.sendMessage(ChatColor.RED + "\"" + args[1] + "\" is not found on this server");
                                    return false;
                                }
                                p.setCompassTarget(p.getWorld().getBlockAt((int)p.getLocation().getX(), 0, -12550820).getLocation());
                                p.sendMessage("Your compass has been set to the North");
                                return true;
                            }
                            p.setCompassTarget(p.getWorld().getBlockAt(12550820, 0, (int)p.getLocation().getZ()).getLocation());
                            p.sendMessage("Your compass has been set to the East");
                            return true;
                        }
                        p.setCompassTarget(p.getWorld().getBlockAt((int)p.getLocation().getX(), 0, 12550820).getLocation());
                        p.sendMessage("Your compass has been set to the South");
                        return true;
                    }
                    p.setCompassTarget(p.getWorld().getBlockAt(-12550820, 0, (int)p.getLocation().getZ()).getLocation());
                    p.sendMessage("Your compass has been set to the West");
                    return true;
                }
                p.setCompassTarget(p.getWorld().getSpawnLocation());
                p.sendMessage("Your compass has been set to the world's spawnpoint");
                this.removeTrackerFromRequester(p);
                return true;
            }
            p.sendMessage(ChatColor.RED + "\"" + args[0] + "\" is not a valid direction.");
            return false;
        }
        else {
            if (!cmd.getName().equalsIgnoreCase("setplayercompass")) {
                return false;
            }
            if (args.length < 2) {
                sender.sendMessage("Syntax error: Too few arguments!");
                return false;
            }
            final Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("Error: Player \"" + args[0] + "\" not found");
                return false;
            }
            Label_3071: {
                Label_3012: {
                    Label_2847: {
                        Label_2774: {
                            Label_2701: {
                                Label_2628: {
                                    switch (args[1].toLowerCase()) {
                                        case "follow": {
                                            break;
                                        }
                                        case "e": {
                                            break Label_2701;
                                        }
                                        case "f": {
                                            break;
                                        }
                                        case "n": {
                                            break Label_2628;
                                        }
                                        case "r": {
                                            break Label_3012;
                                        }
                                        case "s": {
                                            break Label_2774;
                                        }
                                        case "w": {
                                            break Label_2847;
                                        }
                                        case "bed": {
                                            if (target.getBedSpawnLocation() != null) {
                                                target.setCompassTarget(target.getBedSpawnLocation());
                                                sender.sendMessage(String.valueOf(target.getName()) + "'s compass has been set to your bed's location");
                                                return true;
                                            }
                                            sender.sendMessage(String.valueOf(target.getName()) + " doesn't have a bed");
                                            return false;
                                        }
                                        case "pos": {
                                            if (args.length < 2) {
                                                sender.sendMessage("Syntax error: no location specified");
                                                return false;
                                            }
                                            if (args[2].toLowerCase().startsWith("c")) {
                                                target.setCompassTarget(target.getLocation());
                                                sender.sendMessage(String.valueOf(target.getName()) + "'s compass is now pointing to " + target.getName() + " current location");
                                                return true;
                                            }
                                            if (args.length < 5) {
                                                sender.sendMessage(ChatColor.RED + "Usage: /compass pos current or /compass pos <x> <y> <z>");
                                                return false;
                                            }
                                            try {
                                                final int x2 = Integer.parseInt(args[2]);
                                                final int y2 = Integer.parseInt(args[3]);
                                                final int z2 = Integer.parseInt(args[4]);
                                                target.setCompassTarget(target.getWorld().getBlockAt(x2, y2, z2).getLocation());
                                                sender.sendMessage(String.valueOf(target.getName()) + "'s compass is now pointing to " + x2 + ", " + y2 + ", " + z2);
                                                return true;
                                            }
                                            catch (Exception ex2) {
                                                sender.sendMessage("Failed to convert positions");
                                                return false;
                                            }
                                        }
                                        case "east": {
                                            break Label_2701;
                                        }
                                        case "help": {
                                            sender.sendMessage("/" + cmd.getName().toLowerCase() + " <player> <compass command>. See /compass help");
                                            return true;
                                        }
                                        case "west": {
                                            break Label_2847;
                                        }
                                        case "north": {
                                            break Label_2628;
                                        }
                                        case "reset": {
                                            break Label_3012;
                                        }
                                        case "south": {
                                            break Label_2774;
                                        }
                                        case "default": {
                                            break Label_3012;
                                        }
                                        default:
                                            break Label_3071;
                                    }
                                    if (args.length < 3) {
                                        sender.sendMessage("Syntax error: missing player to follow");
                                        return false;
                                    }
                                    final Player toFollow2 = Bukkit.getServer().getPlayer(args[2]);
                                    if (toFollow2 == null) {
                                        sender.sendMessage("Player \"" + args[2] + " not found");
                                        return false;
                                    }
                                    target.setCompassTarget(toFollow2.getLocation());
                                    this.addTracker(target, toFollow2);
                                    sender.sendMessage(String.valueOf(target.getName()) + "'s compass is now pointing to " + toFollow2.getName());
                                    return true;
                                }
                                target.setCompassTarget(target.getWorld().getBlockAt((int)target.getLocation().getX(), 0, -12550820).getLocation());
                                sender.sendMessage(String.valueOf(target.getName()) + "'s compass has been set to the North");
                                return true;
                            }
                            target.setCompassTarget(target.getWorld().getBlockAt(12550820, 0, (int)target.getLocation().getZ()).getLocation());
                            sender.sendMessage(String.valueOf(target.getName()) + "'s compass has been set to the East");
                            return true;
                        }
                        target.setCompassTarget(target.getWorld().getBlockAt((int)target.getLocation().getX(), 0, 12550820).getLocation());
                        sender.sendMessage(String.valueOf(target.getName()) + "'s compass has been set to the South");
                        return true;
                    }
                    target.setCompassTarget(target.getWorld().getBlockAt(-12550820, 0, (int)target.getLocation().getZ()).getLocation());
                    sender.sendMessage(String.valueOf(target.getName()) + "'s compass has been set to the West");
                    return true;
                }
                target.setCompassTarget(target.getWorld().getSpawnLocation());
                sender.sendMessage(String.valueOf(target.getName()) + "'s compass has been set to the world's spawnpoint");
                this.removeTrackerFromRequester(target);
                return true;
            }
            sender.sendMessage("Syntax error: \"" + args[1] + "\" is not a valid direction or command");
            return false;
        }
    }
    
    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        final Object data = this.getMetadata((Metadatable)p, "compasstrackers", (Plugin)this);
        if (data == null) {
            return;
        }
        final String[] players = ((String)data).split(",");
        String[] array;
        for (int length = (array = players).length, i = 0; i < length; ++i) {
            final String pr = array[i];
            final Player toUpdate = Bukkit.getServer().getPlayer(pr);
            if (toUpdate != null) {
                toUpdate.setCompassTarget(p.getLocation());
            }
            else {
                this.removeRequesterFromTrackingOnly(p, pr);
            }
        }
    }
}
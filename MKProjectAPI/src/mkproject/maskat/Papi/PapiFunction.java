package mkproject.maskat.Papi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class PapiFunction {
	protected static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	protected static String getLocationToString(Location location, boolean toPreciseLocation, boolean withYawPitch) {
		if(toPreciseLocation)
			return location.getWorld().getName()+","+location.getX()+","+location.getY()+","+location.getZ()+(withYawPitch ? ","+location.getYaw()+","+location.getPitch() : "");
		else
			return location.getWorld().getName()+","+location.getBlockX()+","+location.getBlockY()+","+location.getBlockZ()+(withYawPitch ? ","+location.getYaw()+","+location.getPitch() : "");
	}
	
	protected static Location getLocationFromString(String stringLocation) {
		String[] strloc = stringLocation.split(",");
		
		World world = Bukkit.getWorld(strloc[0]);
		
		if(world == null)
			return null;
		
		if(strloc.length == 4)
			return new Location(world,Double.parseDouble(strloc[1]),Double.parseDouble(strloc[2]),Double.parseDouble(strloc[3]));
		else if(strloc.length == 6)
			return new Location(world,Double.parseDouble(strloc[1]),Double.parseDouble(strloc[2]),Double.parseDouble(strloc[3]),Float.parseFloat(strloc[4]),Float.parseFloat(strloc[5]));
		
		return null;
	}
	
	protected static boolean isMovedBlock(Location locationFrom, Location locationTo, boolean checkVertical, boolean checkPrecision) {
		if(checkPrecision)
		{
			if(locationFrom.getX() != locationTo.getX() || (checkVertical && locationFrom.getY() != locationTo.getY()) || locationFrom.getZ() != locationTo.getZ())
				return true;
		}
		else if(locationFrom.getBlockX() != locationTo.getBlockX() || (checkVertical && locationFrom.getBlockY() != locationTo.getBlockY()) || locationFrom.getBlockZ() != locationTo.getBlockZ())
			return true;
		return false; //The player hasn't moved
	}
	
	protected static boolean isLocationInRegion(Location location, Location regionPosFirst, Location regionPosSecound, boolean checkVertical) {
    	if(location.getWorld() != regionPosFirst.getWorld() || regionPosFirst.getWorld() != regionPosSecound.getWorld())
    		return false;
    	
    	int locX = location.getBlockX();
    	int locY = 0;
    	if(checkVertical)
    		locY = location.getBlockY();
    	int locZ = location.getBlockZ();
    	
    	int minX = Math.min(regionPosFirst.getBlockX(), regionPosSecound.getBlockX());
    	int minY = 0;
    	if(checkVertical)
    		minY = Math.min(regionPosFirst.getBlockY(), regionPosSecound.getBlockY());
    	int minZ = Math.min(regionPosFirst.getBlockZ(), regionPosSecound.getBlockZ());
    	
    	int maxX = Math.max(regionPosFirst.getBlockX(), regionPosSecound.getBlockX());
    	int maxY = 0;
    	if(checkVertical)
    		maxY = Math.max(regionPosFirst.getBlockY(), regionPosSecound.getBlockY());
    	int maxZ = Math.max(regionPosFirst.getBlockZ(), regionPosSecound.getBlockZ());
    	
//    	If the player X is less than maxX and greater than minX.
    	if(!checkVertical && locX <= maxX && locX >= minX && locZ <= maxZ && locZ >= minZ)
    		return true;
    	if(checkVertical && locX <= maxX && locX >= minX && locY <= maxY && locY >= minY && locZ <= maxZ && locZ >= minZ)
    		return true;
    	return false;
    }
    
//    protected Location getHorizontalCenterLocation() {
//    	int minX = Math.min(pos1x, pos2x);
//    	int minZ = Math.min(pos1z, pos2z);
//    	int maxX = Math.max(pos1x, pos2x);
//    	int maxZ = Math.max(pos1z, pos2z);
//    	
//    	int locX = maxX-((maxX-minX)/2);
//    	int locZ = maxZ-((maxZ-minZ)/2);
//    	
//    	World world = Bukkit.getWorld(Config.allowWorld);
//    	Location preloc = new Location(world, locX, 64, locZ);
//    	return new Location(world, preloc.getBlockX(), world.getHighestBlockYAt(preloc.getBlockX(), preloc.getBlockZ()), preloc.getBlockZ());
//    }
	
	protected static Player getNearestPlayer(Location location) {
	    Player nearest = null;
	    for (Player p : location.getWorld().getPlayers()) {
	        if (nearest == null) nearest = p;
	        else if (p.getLocation().distance(location) < nearest.getLocation().distance(location)) nearest = p;
	    }
	    return nearest;
	}
	
	protected static List<Player> getPlayersNearSquared(Player player, int distance) {
		List<Player> res = new ArrayList<Player>();
		int d2 = distance * distance;
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.getWorld() == player.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= d2) {
		    	res.add(p);
			}
		}
		return res;
	}
	protected static List<Player> getPlayersNearSquared(Location location, int distance) {
		List<Player> res = new ArrayList<Player>();
		int d2 = distance * distance;
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.getWorld() == location.getWorld() && p.getLocation().distanceSquared(location) <= d2) {
				res.add(p);
			}
		}
		return res;
	}
	protected static boolean isSomePlayerNearSquared(Player player, int distance) {
		int d2 = distance * distance;
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.getWorld() == player.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= d2) {
				return true;
			}
		}
		return false;
	}
	protected static boolean isSomePlayerNearSquared(Location location, int distance) {
		int d2 = distance * distance;
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.getWorld() == location.getWorld() && p.getLocation().distanceSquared(location) <= d2) {
				return true;
			}
		}
		return false;
	}
	
	protected static List<Player> getPlayersNear(Player player, int distance) {
		List<Player> res = new ArrayList<Player>();
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.getWorld() == player.getWorld() && p.getLocation().distance(player.getLocation()) <= distance) {
		    	res.add(p);
			}
		}
		return res;
	}
	protected static List<Player> getPlayersNear(Location location, int distance) {
		List<Player> res = new ArrayList<Player>();
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.getWorld() == location.getWorld() && p.getLocation().distance(location) <= distance) {
				res.add(p);
			}
		}
		return res;
	}
	protected static boolean isSomePlayerNear(Player player, int distance) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.getWorld() == player.getWorld() && p.getLocation().distance(player.getLocation()) <= distance) {
				return true;
			}
		}
		return false;
	}
	protected static boolean isSomePlayersNear(Location location, int distance) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.getWorld() == location.getWorld() && p.getLocation().distance(location) <= distance) {
				return true;
			}
		}
		return false;
	}
	
	protected static LocalDateTime getCurrentLocalDateTime() {
		return LocalDateTime.now();
	}
	protected static String getCurrentLocalDateTimeToString() {
//		Date dt = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		return sdf.format(dt);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.now().format(formatter);
	}
	protected static LocalDateTime getLocalDateTimeFromString(String datetime, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern==null ? "yyyy-MM-dd HH:mm:ss" : pattern);
		if(pattern==null) {
			int milisec = datetime.indexOf(".");
			if(milisec >= 0)
				datetime = datetime.substring(0, milisec);
		}
		
		return LocalDateTime.parse(datetime, formatter);
	}
	protected static String getLocalDateTimeToString(LocalDateTime datetime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return datetime.format(formatter);
	}
	
	protected static ItemStack randomEnchantment(ItemStack item, boolean allowAllEnchant, int numberOfEnchants) {
		if(numberOfEnchants<=0)
			numberOfEnchants=1;
		
		for(int i=0;i<numberOfEnchants;i++)
			item = randomEnchant(item, allowAllEnchant);
		return item;
	}
	
	private static ItemStack randomEnchant(ItemStack item, boolean allowAllEnchant) {
        // Store all possible enchantments for the item
        List<Enchantment> possible = new ArrayList<Enchantment>();
     
        // Loop through all enchantemnts
        for (Enchantment ench : Enchantment.values()) {
            // Check if the enchantment can be applied to the item, save it if it can
            if (allowAllEnchant || ench.canEnchantItem(item)) {
                possible.add(ench);
            }
        }
     
        // If we have at least one possible enchantment
        if (possible.size() >= 1) {
            // Randomize the enchantments
            Collections.shuffle(possible);
            // Get the first enchantment in the shuffled list
            Enchantment chosen = possible.get(0);
            // Apply the enchantment with a random level between 1 and the max level
            if(!allowAllEnchant)
            	item.addEnchantment(chosen, 1 + (int) (Math.random() * ((chosen.getMaxLevel() - 1) + 1)));
            else
            	item.addUnsafeEnchantment(chosen, 1 + (int) (Math.random() * ((chosen.getMaxLevel() - 1) + 1)));
        }
     
        // Return the item even if it doesn't have any enchantments
        return item;
    }
	
	public static ItemStack getCustomSkull(String headBase64) {
		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		if (headBase64.isEmpty()) return head;
		SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		profile.getProperties().put("textures", new Property("textures", headBase64));
		try {
			Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
			mtd.setAccessible(true);
			mtd.invoke(skullMeta, profile);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
			ex.printStackTrace();
		}
		head.setItemMeta(skullMeta);
		return head;
	}
	
	public static String getRemainingTimeString(LocalDateTime datetimeExpires) {
		Duration duration = Duration.between(LocalDateTime.now(), datetimeExpires);
		
	    long seconds = duration.getSeconds();
	    
	    if(seconds <= 0)
	    	return null;
	    
	    if(duration.toDays() < 2) {
	    	long absSeconds = Math.abs(seconds);
	    	long iHour = absSeconds / 3600;
	    	long iMin = (absSeconds % 3600) / 60;
	    	long iSec = absSeconds % 60;
	    	
	    	String positive = "";
	    	if(iHour > 0)
	    		positive += iHour+"h ";
	    	if(iMin > 0)
	    		positive += iMin+"m ";
	    	if(iSec > 0)
	    		positive += iSec+"s";
//		    
//		    String positive = String.format(
//		        "%dh %dm %ds",
//		        absSeconds / 3600,
//		        (absSeconds % 3600) / 60,
//		        absSeconds % 60);
		    return positive.trim();
	    }
	    else
	    	return duration.toDays() + " dni";
	}
}

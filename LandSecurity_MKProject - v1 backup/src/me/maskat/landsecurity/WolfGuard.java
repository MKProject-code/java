package me.maskat.landsecurity;

import java.util.Random;
import java.util.UUID;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

public class WolfGuard {
	
//    public static void summonWolf(Player p) {
//        if (!isBone(p))
//            return;
// 
//        // Spawn anywhere between 1 and 4 wolves.
//                Random rand = new Random();
//        int amount = rand.nextInt(4) + 1;
//        for (int i = 0; i < amount; i++) {
//            spawnWolf(p);
//        }
// 
//        // Remove the bone.
//        p.getInventory().removeItem(new ItemStack[] {new ItemStack(Material.BONE, 1)});
//    }
 
    public static UUID spawnWolf(Player player, String wolfName, int wolfCollarColorId) {
    	return spawnWolf(player, wolfName, wolfCollarColorId, player.getLocation());
    }
    public static UUID spawnWolf(Player player, String wolfName, int wolfCollarColorId, Location location) {
        Wolf wolf = (Wolf) player.getWorld().spawnEntity(location, EntityType.WOLF);
        
        // Just to make sure it's a normal wolf.
        wolf.setAdult();
        wolf.setTamed(true);
        wolf.setOwner(player);
 
        // We don't want extra wolves.
        wolf.setBreed(false);
 
        // Clarify the owner.
        wolf.setCustomNameVisible(false);
        wolf.setCustomName(Message.getColorMessage(wolfName));
        wolf.setSilent(true);
        //wolf.setInvulnerable(true);
        
        // Let's have a little bit of variation
        wolf.setCollarColor(getDyeColor(wolfCollarColorId));
 
        // Misc.
        wolf.setHealth(wolf.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
        //wolf.setHealth(1);
        wolf.setCanPickupItems(false);
        
        return wolf.getUniqueId();
    }
 
    // Check if the player's item is a bone.
//    public static boolean isBone(Player p) {
//        return (p.getInventory().getItemInMainHand().getType() == Material.BONE);
//    }
 
    public static int getRandomColorId() {
    	return new Random().nextInt(DyeColor.values().length);
    }
    
    public static DyeColor getDyeColor(int id) {
    	return DyeColor.values()[id];
    }
    
//    public static int getColorsLength() {
//    	return DyeColor.values().length;
//    }
}

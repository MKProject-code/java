// 
// Decompiled by Procyon v0.5.36
// 

package Trade;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class ItemStackUtils
{
    private static Method asNMSCopy;
    private static Method asBukkitCopy;
    private static Method getTag;
    private static Method setTag;
    private static Method getString;
    private static Method mojangsonParse;
    private static Class<?> craftItemStack;
    private static Class<?> nmsItemStack;
    private static Class<?> nbtTagCompound;
    private static Class<?> mojangsonParser;
    private static int versionId;
    
    public static void loadUtils() {
        try {
            ItemStackUtils.nbtTagCompound = Reflection.getNMSClass("NBTTagCompound");
            ItemStackUtils.mojangsonParser = Reflection.getNMSClass("MojangsonParser");
            ItemStackUtils.nmsItemStack = Reflection.getNMSClass("ItemStack");
            ItemStackUtils.craftItemStack = Reflection.getClass("org.bukkit.craftbukkit." + Reflection.getVersion() + ".inventory.CraftItemStack");
            ItemStackUtils.mojangsonParse = ItemStackUtils.mojangsonParser.getDeclaredMethod("parse", String.class);
            ItemStackUtils.asNMSCopy = ItemStackUtils.craftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
            ItemStackUtils.asBukkitCopy = ItemStackUtils.craftItemStack.getDeclaredMethod("asBukkitCopy", ItemStackUtils.nmsItemStack);
            ItemStackUtils.getTag = ItemStackUtils.nmsItemStack.getDeclaredMethod("getTag", (Class<?>[])new Class[0]);
            ItemStackUtils.setTag = ItemStackUtils.nmsItemStack.getDeclaredMethod("setTag", ItemStackUtils.nbtTagCompound);
            ItemStackUtils.getString = ItemStackUtils.nbtTagCompound.getDeclaredMethod("getString", String.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String getNBTString(final ItemStack itemStack, final String path) {
        String message = null;
        try {
            final Object nmsCopy = ItemStackUtils.asNMSCopy.invoke(null, itemStack);
            final Object tag = ItemStackUtils.getTag.invoke(nmsCopy, new Object[0]);
            final String value = (String)ItemStackUtils.getString.invoke(tag, path);
            if (!value.equals("")) {
                message = value;
            }
        }
        catch (Exception e) {
            return null;
        }
        return message;
    }
    
    public static String getNBTTags(final ItemStack itemStack) {
        String message = "";
        try {
            final Object nmsCopy = ItemStackUtils.asNMSCopy.invoke(null, itemStack);
            final Object tag = ItemStackUtils.getTag.invoke(nmsCopy, new Object[0]);
            if (tag != null) {
                message = tag.toString();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
    
    public static ItemStack applyNBTTags(final ItemStack itemStack, final String nbtTag) {
        if (nbtTag == null) {
            return itemStack;
        }
        if (nbtTag.equals("")) {
            return itemStack;
        }
        ItemStack finalItemStack = itemStack;
        try {
            final Object nmsCopy = ItemStackUtils.asNMSCopy.invoke(null, itemStack);
            final Object nbtTagCompound = ItemStackUtils.mojangsonParse.invoke(null, nbtTag);
            ItemStackUtils.setTag.invoke(nmsCopy, nbtTagCompound);
            finalItemStack = (ItemStack)ItemStackUtils.asBukkitCopy.invoke(null, nmsCopy);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return finalItemStack;
    }
    
    public static ItemStack getTexturedNBTItem(final int amount, final String displayName, final String texture, final String nbtTag) {
        ItemStack itemStack = getItem(Material.valueOf((ItemStackUtils.versionId >= 13) ? "LEGACY_SKULL_ITEM" : "SKULL_ITEM"), amount, 3, displayName, new String[0]);
        itemStack = applyNBTTags(itemStack, nbtTag);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack = addTexture(itemStack, itemMeta, texture);
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public static ItemStack getTexturedNBTItem(final int amount, final String displayName, final List<String> lores, final String texture, final String nbtTag) {
        ItemStack itemStack = getItem(Material.valueOf((ItemStackUtils.versionId >= 13) ? "LEGACY_SKULL_ITEM" : "SKULL_ITEM"), amount, 3, displayName, new String[0]);
        itemStack = applyNBTTags(itemStack, nbtTag);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack = addTexture(itemStack, itemMeta, texture);
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore((List)lores);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public static ItemStack getTexturedItem(final int amount, final String displayName, final String texture) {
        ItemStack itemStack = getItem(Material.valueOf((ItemStackUtils.versionId >= 13) ? "LEGACY_SKULL_ITEM" : "SKULL_ITEM"), amount, 3, displayName, new String[0]);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack = addTexture(itemStack, itemMeta, texture);
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public static ItemStack getTexturedItem(final int amount, final String displayName, final List<String> lores, final String texture) {
        ItemStack itemStack = getItem(Material.valueOf((ItemStackUtils.versionId >= 13) ? "LEGACY_SKULL_ITEM" : "SKULL_ITEM"), amount, 3, displayName, new String[0]);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack = addTexture(itemStack, itemMeta, texture);
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore((List)lores);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public static ItemStack addTexture(final ItemStack itemStack, final ItemMeta meta, final String texture) {
        final GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
        profile.getProperties().put("textures", new Property("textures", texture));
        try {
            final Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return itemStack;
    }
    
    public static String getTexture(final ItemStack itemStack) {
        final ItemMeta meta = itemStack.getItemMeta();
        try {
            final Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            final GameProfile gameProfile = (GameProfile)profileField.get(meta);
            if (gameProfile == null) {
                return "";
            }
            return gameProfile.getProperties().get("textures").iterator().next().getValue();
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static ItemStack getItem(final Material material, final int amount, final int data, final String title, final String... lores) {
        final ItemStack itemStack = new ItemStack(material, amount, (short)(byte)data);
        final ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(title);
        meta.setLore(Arrays.asList(lores));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
    
    private static ItemStack getItem(final Material material, final int amount, final String name, final String[] lore) {
        final ItemStack item = new ItemStack(material, amount);
    	final ItemMeta meta = item.getItemMeta();
    	// Set the name of the item
    	meta.setDisplayName(name);
    	
    	// Set the lore of the item
    	meta.setLore(Arrays.asList(lore));
    	
    	item.setItemMeta(meta);
    	
    	return item;
    }
//    
//    public static ItemStack getItem(final Material material, final int amount, final int data, final String title, final List<String> lores) {
//        final ItemStack itemStack = new ItemStack(material, amount, (short)(byte)data);
//        final ItemMeta meta = itemStack.getItemMeta();
//        meta.setDisplayName(title);
//        meta.setLore((List)lores);
//        itemStack.setItemMeta(meta);
//        return itemStack;
//    }
    
    public static ItemStack getItemParseNewLine(final Material material, final int amount, final int data, final String msglang)
    {
    	String[] msgArray = msglang.split("\n");
    	return getItem(material, amount, msgArray[0], Arrays.copyOfRange(msgArray, 1, msgArray.length));
    }
    
    
    
    static {
        ItemStackUtils.versionId = Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].replace(".", "#").split("#")[1]);
    }
}

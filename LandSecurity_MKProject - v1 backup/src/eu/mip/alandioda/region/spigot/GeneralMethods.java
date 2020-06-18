// 
// Decompiled by Procyon v0.5.36
// 

package eu.mip.alandioda.region.spigot;

import java.util.ArrayList;
import java.lang.reflect.Field;
import com.mojang.authlib.properties.Property;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.ChatColor;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class GeneralMethods
{
//    public static String ConbineString(final int startFrom, final String[] array) {
//        String s = array[startFrom];
//        for (int x = startFrom + 1; x < array.length; ++x) {
//            s = String.valueOf(s) + " " + array[x];
//        }
//        return s;
//    }
    
//    public static boolean isNumeric(final String str) {
//        try {
//            Integer.parseInt(str);
//        }
//        catch (NumberFormatException nfe) {
//            return false;
//        }
//        return true;
//    }
    
//    public static ItemStack CreateItem(final Material material, final int count) {
//        final ItemStack item = new ItemStack(material, count);
//        return item;
//    }
    
//    public static ItemStack CreateItem(final Material material, final int count, final int data) {
//        final ItemStack item = new ItemStack(material, count, (short)(byte)data);
//        return item;
//    }
    
    public static ItemStack CreateItem(final String name, final List<String> loreList, final Material material) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        if (!name.equalsIgnoreCase("")) {
            meta.setDisplayName(name);
            meta.setLore(loreList);
        }
        else {
            meta.setDisplayName(ChatColor.BOLD + " ");
        }
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
        item.setItemMeta(meta);
        return item;
    }
    
//    public static ItemStack CreateItem(final String name, final List<String> loreList, final Material material, final int data) {
//        final ItemStack item = new ItemStack(material, 1, (short)(byte)data);
//        final ItemMeta meta = item.getItemMeta();
//        if (!name.equalsIgnoreCase("")) {
//            meta.setDisplayName(name);
//            meta.setLore(loreList);
//        }
//        else {
//            meta.setDisplayName(ChatColor.BOLD + " ");
//        }
//        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
//        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
//        item.setItemMeta(meta);
//        return item;
//    }
    
    public static ItemStack CreateItem(final String name, final List<String> loreList, final String url) {
        final ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        if (url.isEmpty()) {
            return head;
        }
        final SkullMeta headMeta = (SkullMeta)head.getItemMeta();
        final GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
        profile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"" + url + "\"}}}")));
        try {
            final Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        }
        catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex2) {
            ex2.printStackTrace();
        }
        if (!name.equalsIgnoreCase("")) {
            headMeta.setDisplayName(name);
            headMeta.setLore(loreList);
        }
        else {
            headMeta.setDisplayName(ChatColor.BOLD + " ");
        }
        headMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
        headMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
        head.setItemMeta((ItemMeta)headMeta);
        return head;
    }
    
    public static List<String> arrayToList(final String[] list) {
        final List<String> newList = new ArrayList<String>();
        for (final String s : list) {
            newList.add(s);
        }
        return newList;
    }
}

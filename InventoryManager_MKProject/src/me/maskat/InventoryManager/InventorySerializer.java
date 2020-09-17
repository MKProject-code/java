package me.maskat.InventoryManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class InventorySerializer {
    /**
     * Converts the player inventory to a String array of Base64 strings. First string is the content and second string is the armor.
     * 
     * @param playerInventory to turn into an array of strings.
     * @return Array of strings: [ main content, armor content ]
     * @throws IllegalStateException
     */
//    public static String playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
//    	//get the main content part, this doesn't return the armor
//    	String content = itemStackArrayToBase64((ItemStack[])ArrayUtils.addAll(playerInventory.getContents(), playerInventory.getArmorContents()));
//    	return content;
////    	String armor = itemStackArrayToBase64(playerInventory.getArmorContents());
//    	
////    	return new String[] { content, armor };
//    }
//	protected static String inventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
//    	return itemStackArrayToBase64(playerInventory.getContents());
//    }
    protected static String inventoryToBase64(Inventory inventory) throws IllegalStateException {
    	return itemStackArrayToBase64(inventory.getContents());
    }
    
//    public static List<ItemStack[]> playerInventoryFromBase64(String data) throws IllegalStateException, IOException {
//    	//get the main content part, this doesn't return the armor
//    	ItemStack[] items = itemStackArrayFromBase64(data);
//    	int i=0;
//    	for(ItemStack item:items) {
//    		if(item!=null && item.getType() != null)
//    		Bukkit.broadcastMessage(i+"="+item.getType().toString());
//    		i++;
//    	}
//	    //return List.of(Arrays.copyOfRange(items, 0, 36), Arrays.copyOfRange(items, 37, 41));
//	    return List.of(Arrays.copyOfRange(items, 0, 41), Arrays.copyOfRange(items, 41, 45));
//    }
    
    protected static ItemStack[] inventoryFromBase64(String data) throws IllegalStateException, IOException {
    	return itemStackArrayFromBase64(data);
    }
    
    /**
     * 
     * A method to serialize an {@link ItemStack} array to Base64 String.
     * 
     * <p />
     * 
     * Based off of {@link #toBase64(Inventory)}.
     * 
     * @param items to turn into a Base64 String.
     * @return Base64 string of the items.
     * @throws IllegalStateException
     */
    protected static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
    	try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            
            // Write the size of the inventory
            dataOutput.writeInt(items.length);
            
            // Save every element in the list
            for (int i = 0; i < items.length; i++) {
            	try {
            		dataOutput.writeObject(items[i]);
            	} catch (Exception e) {
            		throw new IllegalStateException("Unable to save item stack. (item:"+(items[i]==null ? "NULL" : items[i].getType()+"->"+items[i].getAmount())+")", e);
            	}
            }
//            for (int i = 0; i < items.length; i++) {
//            	try {
//            		dataOutput.writeObject(items[i]);
//            	} catch (Exception e) {
//            		throw new IllegalStateException("Unable to save item stack. (index:"+i+")", e);
//            	}
//            }
            
            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }
    
    /**
     * A method to serialize an inventory to Base64 string.
     * 
     * <p />
     * 
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     * 
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     * 
     * @param inventory to serialize
     * @return Base64 string of the provided inventory
     * @throws IllegalStateException
     */
//    protected static String toBase64(Inventory inventory) throws IllegalStateException {
//        try {
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
//            
//            // Write the size of the inventory
//            dataOutput.writeInt(inventory.getSize());
//            
//            // Save every element in the list
//            for (int i = 0; i < inventory.getSize(); i++) {
//                dataOutput.writeObject(inventory.getItem(i));
//            }
//            
//            // Serialize that array
//            dataOutput.close();
//            return Base64Coder.encodeLines(outputStream.toByteArray());
//        } catch (Exception e) {
//            throw new IllegalStateException("Unable to save item stacks.", e);
//        }
//    }
    
    /**
     * 
     * A method to get an {@link Inventory} from an encoded, Base64, string.
     * 
     * <p />
     * 
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     * 
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     * 
     * @param data Base64 string of data containing an inventory.
     * @return Inventory created from the Base64 string.
     * @throws IOException
     */
//    protected static Inventory fromBase64(String data) throws IOException {
//        try {
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
//            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
//            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());
//    
//            // Read the serialized inventory
//            for (int i = 0; i < inventory.getSize(); i++) {
//                inventory.setItem(i, (ItemStack) dataInput.readObject());
//            }
//            
//            dataInput.close();
//            return inventory;
//        } catch (ClassNotFoundException e) {
//            throw new IOException("Unable to decode class type.", e);
//        }
//    }
    
    /**
     * Gets an array of ItemStacks from Base64 string.
     * 
     * <p />
     * 
     * Base off of {@link #fromBase64(String)}.
     * 
     * @param data Base64 string to convert to ItemStack array.
     * @return ItemStack array created from the Base64 string.
     * @throws IOException
     */
    protected static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
    	try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
    
            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
            	items[i] = (ItemStack) dataInput.readObject();
            }
            
            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
    
    protected static String velocityToBase64(Vector vector) throws IllegalStateException {
    	try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            
            Map<String,Object> velocitySerialized = vector.serialize();
            
            dataOutput.writeInt(velocitySerialized.size());
            
            for (Entry<String, Object> entry : velocitySerialized.entrySet()) {
            	dataOutput.writeUTF(entry.getKey());
            	dataOutput.writeObject(entry.getValue());
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }
    protected static Vector velocityFromBase64(String data) throws IOException {
    	try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Map<String,Object> velocitySerialized = new HashMap<>();
    
            int size = dataInput.readInt();
            
            for (int i = 0; i < size; i++) {
            	velocitySerialized.put(dataInput.readUTF(), dataInput.readObject());
            }
            
            dataInput.close();
            return Vector.deserialize(velocitySerialized);
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

	protected static String potionEffectsToBase64(Collection<PotionEffect> activePotionEffects) {
    	try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            
            dataOutput.writeInt(activePotionEffects.size());
            
            for (PotionEffect effect : activePotionEffects) {
            	try {
            		dataOutput.writeObject(effect);
            	} catch (Exception e) {
            		throw new IllegalStateException("Unable to save potion effect. (effect:"+(effect==null ? "NULL" : effect.getType().getName()+"->"+effect.getDuration()+"s|"+effect.getAmplifier()+"lvl")+")", e);
            	}
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
	}
	
    protected static Collection<PotionEffect> potionEffectsFromBase64(String data) throws IOException {
    	ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
		BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
		Collection<PotionEffect> activePotionEffects = new ArrayList<>();
   
		int size = dataInput.readInt();
		
		for (int i = 0; i < size; i++) {
			try {
				activePotionEffects.add((PotionEffect) dataInput.readObject());
			} catch(Exception ex) {}
		}
		
		dataInput.close();
		return activePotionEffects;
    }
}

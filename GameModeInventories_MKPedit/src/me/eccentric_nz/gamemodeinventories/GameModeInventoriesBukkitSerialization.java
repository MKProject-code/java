package me.eccentric_nz.gamemodeinventories;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class GameModeInventoriesBukkitSerialization {
   public static String toDatabase(ItemStack[] inventory) {
      try {
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
         Throwable var3 = null;

         try {
            dataOutput.writeInt(inventory.length);
            ItemStack[] var4 = inventory;
            int var5 = inventory.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               ItemStack is = var4[var6];
               dataOutput.writeObject(is);
            }
         } catch (Throwable var16) {
            var3 = var16;
            throw var16;
         } finally {
            if (dataOutput != null) {
               if (var3 != null) {
                  try {
                     dataOutput.close();
                  } catch (Throwable var15) {
                     var3.addSuppressed(var15);
                  }
               } else {
                  dataOutput.close();
               }
            }

         }

         return Base64Coder.encodeLines(outputStream.toByteArray());
      } catch (IOException var18) {
         throw new IllegalStateException("Unable to save item stacks.", var18);
      }
   }

   public static ItemStack[] fromDatabase(String data) throws IOException {
      try {
         ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
         BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
         Throwable var4 = null;

         ItemStack[] inventory;
         try {
            int size = dataInput.readInt();
            inventory = new ItemStack[size];

            for(int i = 0; i < size; ++i) {
               inventory[i] = (ItemStack)dataInput.readObject();
            }
         } catch (Throwable var15) {
            var4 = var15;
            throw var15;
         } finally {
            if (dataInput != null) {
               if (var4 != null) {
                  try {
                     dataInput.close();
                  } catch (Throwable var14) {
                     var4.addSuppressed(var14);
                  }
               } else {
                  dataInput.close();
               }
            }

         }

         return inventory;
      } catch (ClassNotFoundException var17) {
         throw new IOException("Unable to decode class type.", var17);
      }
   }
}

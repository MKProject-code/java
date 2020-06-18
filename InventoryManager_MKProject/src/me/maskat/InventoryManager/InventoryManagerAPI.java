package me.maskat.InventoryManager;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryManagerAPI {
	public static void savePlayerInventory(Player player) {
		Model.getPlayer(player).saveInventory(player.getGameMode());
	}
    public static String inventoryToBase64(PlayerInventory playerInventory) {
    	return InventorySerializer.inventoryToBase64(playerInventory);
    }
    public static String inventoryToBase64(Inventory inventory) {
    	return InventorySerializer.inventoryToBase64(inventory);
    }
    public static ItemStack[] inventoryFromBase64(String data) throws IOException {
    	return InventorySerializer.inventoryFromBase64(data);
    }
}

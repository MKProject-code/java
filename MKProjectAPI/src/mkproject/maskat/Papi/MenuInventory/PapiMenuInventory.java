//package mkproject.maskat.Papi.MenuInventory;
//
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//
//public class PapiMenuInventory {
//	public static MenuPage createPage(final int numberOfRows, final String colorTitle) {
//		return MenuManager.createPage(numberOfRows, colorTitle);
//	}
//	public static boolean removePage(final MenuPage menuPage) {
//		return MenuManager.removePage(menuPage);
//	}
//	
//	public static void setItem(final MenuPage menuPage, final InventorySlot menuInvSlot, final Material material, final String colorNameAndLore) {
//		menuPage.setItem(menuInvSlot, material, colorNameAndLore);
//	}
//	
//	public static void setItemHeadAsync(final MenuPage menuPage, final InventorySlot menuInvSlot, final Player player, final String colorNameAndLore) {
//		menuPage.setItemHeadAsync(menuInvSlot, player, colorNameAndLore);
//	}
//	
//	public static void openPage(final MenuPage menuPage, final Player player) {
//		player.openInventory(menuPage.getInventory());
//	}
//}

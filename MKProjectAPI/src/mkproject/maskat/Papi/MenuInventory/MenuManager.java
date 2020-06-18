//package mkproject.maskat.Papi.MenuInventory;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.bukkit.entity.Player;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.event.inventory.InventoryCloseEvent;
//
//public class MenuManager {
//
//	private static List<MenuPage> menuPagesList = new ArrayList<MenuPage>();
//	
//	protected static MenuPage createPage(final int numberOfRows, final String colorTitle) {
//		MenuPage menuInvPage = new MenuPage(numberOfRows, colorTitle);
//		menuPagesList.add(menuInvPage);
//		return menuInvPage;
//	}
//	protected static boolean removePage(MenuPage menuInvPage) {
//		return menuPagesList.remove(menuInvPage);
//	}
//	
//	public static void onInventoryClick(final InventoryClickEvent e, final Player player)
//	{
//		if(!(e.getInventory().getHolder() instanceof MenuPage))
//			return;
//		
//		if(menuPagesList.contains((MenuPage)e.getInventory().getHolder()))
//			((MenuPage)e.getInventory().getHolder()).onInventoryClick(e, player);
//	}
//	
//	public static void onInventoryClose(final InventoryCloseEvent e) {
//		if(!(e.getInventory().getHolder() instanceof MenuPage))
//			return;
//		
//		menuPagesList.remove((MenuPage)e.getInventory().getHolder());
//	}
//}

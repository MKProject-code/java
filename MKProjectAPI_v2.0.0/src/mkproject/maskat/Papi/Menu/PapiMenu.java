package mkproject.maskat.Papi.Menu;

import org.bukkit.entity.Player;

public interface PapiMenu {
	public PapiMenuPage getPapiMenuPage();
//	@Deprecated
//	public default void openPapiMenuPage(Player player) { Papi.Model.getPlayer(player).openMenu(this.getPapiMenuPage()); };
	public default void openPapiMenuPage(Player player) { this.getPapiMenuPage().openMenu(player); };

	@Deprecated
	public default void initPapiMenu(Player player, int page, PapiMenu backMenu) {};
	
	public void onMenuClick(PapiMenuClickEvent e);
	public void onMenuClose(PapiMenuCloseEvent e);
}

package mkproject.maskat.Papi.Menu;

import org.bukkit.entity.Player;

import mkproject.maskat.Papi.PapiPlugin;

public interface PapiMenuV2 extends PapiMenu {
	public PapiMenuPage getPapiMenuPage();
	public default void openPapiMenuPage(Player player) { this.getPapiMenuPage().openMenu(player); };
	public boolean refreshPapiMenuPage();
	public void onMenuClick(PapiMenuClickEvent e);
	public void onMenuClose(PapiMenuCloseEvent e);
}

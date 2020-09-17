package mkproject.maskat.Papi.Menu;

import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;

public interface PapiMenu {
	public PapiMenuPage getPapiMenuPage();
	public default void openPapiMenuPage(Player player) { Papi.Model.getPlayer(player).openMenu(this.getPapiMenuPage()); };
	public void initPapiMenu(Player player, int page, PapiMenu backMenu);
	public void onMenuClick(PapiMenuClickEvent e);
	public void onMenuClose(PapiMenuCloseEvent e);
}

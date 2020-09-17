package mkproject.maskat.Papi.TabList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PapiTabList
{
	public static void clearTabPlayers(Player player)
	{
		TabListFunction.clearTabPlayers(player);
	}
	
	public static class Four {
		public static void addTabsClear(Player player, boolean doClearTabPlayers)
		{
			if(doClearTabPlayers)
				clearTabPlayers(player);
			
			for(int slot=0;slot < 80;slot++)
				TabListFunction.updateTab(player, slot, null);
		}
		
		public static void updateTab(Player player, TabListFourSlot tabListFourSlot, String colorName)
		{
			TabListFunction.updateTab(player, tabListFourSlot.getValue(), colorName);
		}
		
		public static void updateTab(Player player, TabListFourSlot tabListFourSlot, String colorName, int ping)
		{
			TabListFunction.updateTab(player, tabListFourSlot.getValue(), colorName, ping);
		}
		
		public static void clearTabColumn(int column, int row) {
			int updateRow = (column-1)*20;
			updateRow += row-1;
			for(Player player : Bukkit.getOnlinePlayers())
				TabListFunction.clearTab(player, updateRow);
		}
		
		public static void updateTabColumn(int column, int row, String colorName) {
			int updateRow = (column-1)*20;
			updateRow += row-1;
			for(Player player : Bukkit.getOnlinePlayers())
				TabListFunction.updateTab(player, updateRow, colorName);
		}
		
		public static void updateTabColumn(int column, int row, String colorName, int ping) {
			int updateRow = (column-1)*20;
			updateRow += row-1;
			for(Player player : Bukkit.getOnlinePlayers())
				TabListFunction.updateTab(player, updateRow, colorName, ping);
		}
		
		public static void updateTabColumn(Player player, int column, int row, String colorName) {
			int updateRow = (column-1)*20;
			updateRow += row-1;
			TabListFunction.updateTab(player, updateRow, colorName);
		}
		
		public static void updateTabColumn(Player player, int column, int row, String colorName, int ping) {
			int updateRow = (column-1)*20;
			updateRow += row-1;
			TabListFunction.updateTab(player, updateRow, colorName, ping);
		}
	}
}

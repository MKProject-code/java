package mkproject.maskat.PortalManager;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class Manager {
	public static class Admin {
		public static boolean createPortal(final Player commandPlayer, final String portalName) {
			
			if(portalName.length() > 30) {
				Message.sendMessage(commandPlayer, "&cNazwa portalu nie może być dłuższa niż 30 znaków!");
				return false;
			}
			
			if(!Plugin.worldEditPlugin.getSession(commandPlayer).isSelectionDefined(BukkitAdapter.adapt(commandPlayer.getWorld())))
			{
				Message.sendMessage(commandPlayer, "&cNic nie zaznaczyłeś!");
				return false;
			}
			
			Region selection = Plugin.worldEditPlugin.
					getSession(commandPlayer)
					.getSelection(BukkitAdapter.
							adapt(commandPlayer.
									getWorld()));
			
			BlockVector3 selectedMinimumPoint = selection.getMinimumPoint();
			BlockVector3 selectedMaximumPoint = selection.getMaximumPoint();
			
			Model.createPortal(commandPlayer, portalName, selectedMinimumPoint, selectedMaximumPoint);
			
			Message.sendMessage(commandPlayer, "&aPortal został stworzony poprawnie.");
			return true;
		}
		public static boolean removePortal(final Player commandPlayer, final String portalName) {
			Object result = Papi.MySQL.get(Database.Portals.ID, Database.Portals.NAME, "=", portalName, Database.Portals.TABLE);
			if(result == null)
			{
				Message.sendMessage(commandPlayer, "&cTaki portal nie istnieje.");
				return false;
			}
			int portalid = Integer.parseInt(String.valueOf(result));
			
			Model.removePortal(portalid);
			Message.sendMessage(commandPlayer, "&aPortal został usunięty.");
			
			return true;
		}
		public static boolean setTpPortal(final Player commandPlayer, final String portalName, final String destName) {
			Object result = Papi.MySQL.get(Database.Portals.ID, Database.Portals.NAME, "=", portalName, Database.Portals.TABLE);
			if(result == null)
			{
				Message.sendMessage(commandPlayer, "&cTaki portal nie istnieje.");
				return false;
			}
			int portalid = Integer.parseInt(String.valueOf(result));
			
			if(destName.equalsIgnoreCase("tome"))
				Model.getPortal(portalid).setPortalDestinationToMe(commandPlayer.getLocation());
			else
				Model.getPortal(portalid).setPortalDestinationToLocationName(destName);
			Message.sendMessage(commandPlayer, "&aUstawiono miejsce docelowe dla portalu &e"+portalName);
			
			return true;
		}
		public static boolean getListPortal(final Player player) {
			ResultSet rs = Papi.MySQL.getResultSetAll(-1, "*", Database.Portals.TABLE);
			if(rs == null) 
			{
				Message.sendMessage(player, "&cBrak wyników.");
				return false;
			}
			try {
				while(rs.next()) {
					Message.sendMessage(player, "&c"+rs.getInt(Database.Portals.ID)+". &e"+
							rs.getString(Database.Portals.NAME)+": &b"+
							rs.getString(Database.Portals.PORTAL_LOCATION_WORLD)+" &a"+
							rs.getString(Database.Portals.PORTAL_LOCATION_FIRST)+" &7> &d"+
							rs.getString(Database.Portals.DESTINATION_POSITION));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return true;
		}
	}

	public static Location getPortalDestinationFromLocationName(String portalDestinationName) {
		if(portalDestinationName.equalsIgnoreCase("toserverspawn"))
		{
			return Bukkit.getWorld(Config.getString(Config.ConfigKey.ServerSpawnWorldName)).getSpawnLocation();
		}
		else if(portalDestinationName.equalsIgnoreCase("toplayersurvivalspawn"))
		{
			return null;
		}
		
		return null;
	}
}

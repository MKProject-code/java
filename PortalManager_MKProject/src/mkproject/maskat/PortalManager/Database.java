package mkproject.maskat.PortalManager;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import mkproject.maskat.Papi.Papi;

public class Database {
	
	public static void Initialize() {
		Portals.TABLE = Config.getString("MySQL.Table.Portals")+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		
		if(Papi.DEVELOPER_DATABASE && Papi.DEVELOPER_DATABASE_AUTODELETE)
			Papi.MySQL.deleteTable(Portals.TABLE);
		
		Papi.MySQL.createTable(Portals.TABLE,
					Papi.SQL.createColumnParse(Portals.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Portals.NAME, Papi.SQL.ValType.VARCHAR, 32, true, false, true)
				+","+Papi.SQL.createColumnParse(Portals.PORTAL_LOCATION_FIRST, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Portals.PORTAL_LOCATION_SECOUND, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Portals.PORTAL_LOCATION_WORLD, Papi.SQL.ValType.VARCHAR, 64, true)
				+","+Papi.SQL.createColumnParse(Portals.DESTINATION_POSITION, Papi.SQL.ValType.VARCHAR, 256, false)
				+","+Papi.SQL.createColumnPrimary(Portals.ID)
		);
		
		ResultSet rs = Papi.MySQL.getResultSetAll(-1, "*", Portals.TABLE);

		if(rs != null)
		{
			try {
				while (rs.next()) {
					int portalId = rs.getInt(Portals.ID);
					String portalName = rs.getString(Portals.NAME);
					String[] portalLocationFirst = rs.getString(Portals.PORTAL_LOCATION_FIRST).split(",");
					String[] portalLocationSecound = rs.getString(Portals.PORTAL_LOCATION_SECOUND).split(",");
					World portalWorld = Bukkit.getWorld(rs.getString(Portals.PORTAL_LOCATION_WORLD));
					
					Model.getPortalsMap().put(portalId, new ModelPortal(portalId, portalName,
							Integer.parseInt(portalLocationFirst[0]), Integer.parseInt(portalLocationFirst[1]), Integer.parseInt(portalLocationFirst[2]), 
							Integer.parseInt(portalLocationSecound[0]), Integer.parseInt(portalLocationSecound[1]), Integer.parseInt(portalLocationSecound[2]),
							portalWorld));
					
					String portalDestinationName = rs.getString(Portals.DESTINATION_POSITION);
					if(portalDestinationName.equalsIgnoreCase("toserverspawn") || portalDestinationName.equalsIgnoreCase("toplayersurvivalspawn"))
						Model.getPortal(portalId).initPortalDestinationToLocationName(portalDestinationName);
					else
					{
						Location portalDestinationLocation= Papi.Function.getLocationFromString(portalDestinationName);
						Model.getPortal(portalId).initPortalDestinationToMe(portalDestinationLocation);
					}
					
				}
			} catch (NumberFormatException | SQLException e) {
				Plugin.getPlugin().getLogger().warning("**************** Error load data from database ****************");
				Plugin.getPlugin().getLogger().warning("**************** Error load data from database ****************");
				Plugin.getPlugin().getLogger().warning("**************** Error load data from database ****************");
				e.printStackTrace();
			}
		}
	}
	
	public static class Portals {
		public static String TABLE;
		public static final String ID = "portalid";
		public static final String NAME = "portalname";
		public static final String PORTAL_LOCATION_FIRST = "portallocationfirst";
		public static final String PORTAL_LOCATION_SECOUND = "portallocationsecound";
		public static final String PORTAL_LOCATION_WORLD = "portallocationworld";
		public static final String DESTINATION_POSITION = "destinationposition";
	}
}

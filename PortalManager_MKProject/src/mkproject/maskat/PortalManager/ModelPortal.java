package mkproject.maskat.PortalManager;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;

import mkproject.maskat.Papi.Papi;

public class ModelPortal {

	private int portalId;
	private String portalName;
//    private int posFirstX;
//    private int posFirstY;
//    private int posFirstZ;
//    private int posSecoundX;
//    private int posSecoundY;
//    private int posSecoundZ;
    private Location portalLocationFirst;
    private Location portalLocationSecound;
//	private World portalWorld;
	private Location portalDestination = null;
	private String portalDestinationCustom = null;
	
	public ModelPortal(int portalId, String portalName, int posFirstX, int posFirstY, int posFirstZ, int posSecoundX, int posSecoundY, int posSecoundZ, World portalWorld) {
		this.portalId = portalId;
		this.portalName = portalName;
//		this.posFirstX = posFirstX;
//		this.posFirstY = posFirstY;
//		this.posFirstZ = posFirstZ;
//		this.posSecoundX = posSecoundX;
//		this.posSecoundY = posSecoundY;
//		this.posSecoundZ = posSecoundZ;
//		this.portalWorld = portalWorld;
		
		portalLocationFirst = new Location(portalWorld, posFirstX, posFirstY, posFirstZ);
		portalLocationSecound = new Location(portalWorld, posSecoundX, posSecoundY, posSecoundZ);
	}
	
	public String getPortalName() {
		return portalName;
	}
//	public String getPortalLocationPositionFirst() {
//		return selectedFirst;
//	}
//	public String getPortalLocationPositionSecound() {
//		return selectedSecound;
//	}
	public World getPortalWorld() {
		return portalLocationFirst.getWorld();
	}
	public Location getPortalLocationFirst() {
		return portalLocationFirst;
	}
	public Location getPortalLocationSecound() {
		return portalLocationSecound;
	}
	public Location getPortalDestination() {
		return portalDestination;
	}
	public String getPortalDestinationCustom() {
		return portalDestinationCustom;
	}
	public void setPortalDestinationToMe(Location portalDestinationLoc) {
		Papi.MySQL.set(Map.of(Database.Portals.DESTINATION_POSITION, Papi.Function.getLocationToString(portalDestinationLoc,true,true)), Database.Portals.ID, "=", portalId, Database.Portals.TABLE);
		this.portalDestinationCustom = null;
		this.portalDestination = portalDestinationLoc;
	}
	public void setPortalDestinationToLocationName(String portalDestinationName) {
		Papi.MySQL.set(Map.of(Database.Portals.DESTINATION_POSITION, portalDestinationName.toLowerCase()), Database.Portals.ID, "=", portalId, Database.Portals.TABLE);
		this.portalDestinationCustom = portalDestinationName;
		this.portalDestination = null;
	}
	public void initPortalDestinationToMe(Location portalDestinationLoc) {
		this.portalDestinationCustom = null;
		this.portalDestination = portalDestinationLoc;
	}
	public void initPortalDestinationToLocationName(String portalDestinationName) {
		this.portalDestinationCustom = portalDestinationName;
		this.portalDestination = null;
	}

}

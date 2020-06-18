package mkproject.maskat.PortalManager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.math.BlockVector3;

import mkproject.maskat.Papi.Papi;

public class Model {
	private static Map<Integer, ModelPortal> portalsMap = new HashMap<>();
	private static Map<Player, ModelPlayer> playersMap = new HashMap<>();
	
	public static ModelPortal getPortal(int id) {
		return portalsMap.get(id);
	}
	public static Map<Integer, ModelPortal> getPortalsMap() {
		return portalsMap;
	}
	
	public static ModelPlayer addPlayer(Player player) {
		return playersMap.putIfAbsent(player, new ModelPlayer(player));
	}
	public static ModelPlayer getPlayer(Player player) {
		return playersMap.get(player);
	}
	public static boolean existPlayer(Player player) {
		return playersMap.containsKey(player);
	}
	public static ModelPlayer removePlayer(Player player) {
		return playersMap.remove(player);
	}
	
	public static void createPortal(Player commandPlayer, String portalName, BlockVector3 selectedMinimumPoint, BlockVector3 selectedMaximumPoint) {
		
		String selectedFirst = selectedMinimumPoint.getBlockX()+","+selectedMinimumPoint.getBlockY()+","+selectedMinimumPoint.getBlockZ();
		String selectedSecound = selectedMaximumPoint.getBlockX()+","+selectedMaximumPoint.getBlockY()+","+selectedMaximumPoint.getBlockZ();
		
		int portalid = Papi.MySQL.put(Map.of(
				Database.Portals.NAME, portalName,
				Database.Portals.PORTAL_LOCATION_FIRST, selectedFirst,
				Database.Portals.PORTAL_LOCATION_SECOUND, selectedSecound,
				Database.Portals.PORTAL_LOCATION_WORLD, commandPlayer.getWorld().getName()
				), Database.Portals.TABLE);
		
		portalsMap.put(portalid, new ModelPortal(portalid, portalName, selectedMinimumPoint.getBlockX(), selectedMinimumPoint.getBlockY(), selectedMinimumPoint.getBlockZ(),
				selectedMaximumPoint.getBlockX(), selectedMaximumPoint.getBlockY(), selectedMaximumPoint.getBlockZ(), commandPlayer.getWorld()));
	}

	public static void removePortal(int portalId) {
		Papi.MySQL.deleteData(Database.Portals.ID, "=", String.valueOf(portalId), Database.Portals.TABLE);
		portalsMap.remove(portalId);
	}

}

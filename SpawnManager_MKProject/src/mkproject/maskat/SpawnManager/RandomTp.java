package mkproject.maskat.SpawnManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;


public class RandomTp {

	private static int MIN_RADIUS = 0;
	private static int MAX_RADIUS = 50000;
	private static int CENTER_LOCATION_X = 0;
	private static int CENTER_LOCATION_Z = 0;
	private static int MIN_OTHER_PLAYER_DISTANCE = 1000;
	
	//private static List<Material> spawnonmaterialBlackList = new ArrayList<Material>();
	private static List<Material> spawnonmaterialBlackList = List.of(
			Material.CAVE_AIR,
			Material.VOID_AIR,
			
			Material.CACTUS,
			
			Material.ACACIA_LEAVES,
			Material.BIRCH_LEAVES,
			Material.DARK_OAK_LEAVES,
			Material.JUNGLE_LEAVES,
			Material.OAK_LEAVES,
			Material.SPRUCE_LEAVES,
			
			Material.ICE,
			Material.FROSTED_ICE,
			Material.BLUE_ICE,
			
			Material.WATER,
			Material.LAVA
			);
	
	private static List<Material> spawnonmaterialAirAllowList = List.of(
			Material.AIR,
			
			Material.ACACIA_SAPLING,
			Material.BAMBOO_SAPLING,
			Material.BIRCH_SAPLING,
			Material.DARK_OAK_SAPLING,
			Material.JUNGLE_SAPLING,
			Material.OAK_SAPLING,
			Material.SPRUCE_SAPLING,
			
			Material.GRASS,
			Material.FERN,
			Material.DEAD_BUSH,
			Material.SEAGRASS,
			Material.DANDELION,
			Material.POPPY,
			Material.BLUE_ORCHID,
			Material.ALLIUM,
			Material.AZURE_BLUET,
			Material.RED_TULIP,
			Material.ORANGE_TULIP,
			Material.WHITE_TULIP,
			Material.PINK_TULIP,
			Material.OXEYE_DAISY,
			Material.CORNFLOWER,
			Material.LILY_OF_THE_VALLEY,
			Material.WITHER_ROSE,
			Material.BROWN_MUSHROOM,
			Material.RED_MUSHROOM,
			
			Material.TORCH,
			
			Material.SUNFLOWER,
			Material.LILAC,
			Material.ROSE_BUSH,
			Material.PEONY,
			
			Material.TALL_GRASS,
			Material.LARGE_FERN,
			
			Material.TALL_SEAGRASS
			);
	private static List<Material> spawnonmaterialStayBlackList = List.of(
			Material.AIR
			);
	
	protected static void preparePlayerSpawnArea(Player player) {
		Location loc = Papi.Model.getPlayer(player).getPlayerSpawnLocation().clone();
		
		loc.setY(loc.getY()+1);
		
		loc.setX(loc.getX()-5);
		loc.setZ(loc.getZ()-5);
		for(int i1=-5; i1<=5; i1++)
		{
			for(int i2=-5; i2<=5; i2++)
			{
				if(loc.getBlock().getType() != Material.AIR && isAllowedAirBlock(loc))
					loc.getBlock().setType(Material.AIR);
				
				loc.setZ(loc.getZ()+1);
			}
			loc.setZ(loc.getZ()-11);
			loc.setX(loc.getX()+1);
		}
	}
	
	protected static void updatePlayerSpawnLocationAsync(Player player, World world) {
		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable() {
		      @Override
		      public void run() {
		    	  Location loc = RandomTp.getRandomLocationTask(player, Papi.Server.getSurvivalWorld());
		    	  if(loc == null)
		    		  return;
		    	  Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
				      @Override
				      public void run() {
				    	  Papi.MySQL.set(Map.of(Database.Users.PLAYER_SURVIVAL_SPAWN_LOCATION, Papi.Function.getLocationToString(loc, false, false)), Database.Users.USERNAME, "=", player.getName().toLowerCase(), Database.Users.TABLE);
				    	  Papi.Model.getPlayer(player).initPlayerSpawnLocation(loc);
				      }});
		      }});
	}
	
	private static Location getRandomLocationTask(Player player, World world) {
		if(!player.isOnline())
			return null;
		//Message.sendTitle(player, null, "Szykamy bezpiecznego miejsca dla Ciebie...", 0, 1, 0);
		
		ThreadLocalRandom generator = ThreadLocalRandom.current();
		int randomX = generator.nextInt(MIN_RADIUS, MAX_RADIUS + 1);
		int randomZ = generator.nextInt(MIN_RADIUS, MAX_RADIUS + 1);
		
		if(generator.nextInt(0, 2)==1)
			randomX *= -1;
		if(generator.nextInt(0, 2)==1)
			randomZ *= -1;
		
		randomX += CENTER_LOCATION_X;
		randomZ += CENTER_LOCATION_Z;
		
		Location randomLocation = new Location(world, randomX+0.5, world.getHighestBlockYAt(randomX, randomZ), randomZ+0.5);
		
		if(checkValidLocation(randomLocation))
			return randomLocation;
		else
			return getRandomLocationTask(player, world);
		
/*
- stationary_water
- stationary_lava
- water
- lava
- cactus
- leaves
- leaves_2
- air
 */
	}
	
	private static boolean isAllowedSpawnBlock(Location location) {
//		if(location.getBlock().getType()!=Material.AIR)
//			Message.sendBroadcast("BlockType: "+location.getBlock().getType());
		return !spawnonmaterialBlackList.contains(location.getBlock().getType());
	}
	
	private static boolean isAllowedAirBlock(Location location) {
		return spawnonmaterialAirAllowList.contains(location.getBlock().getType());
	}
	private static boolean isAllowedStayBlock(Location location) {
		return !spawnonmaterialStayBlackList.contains(location.getBlock().getType());
	}
	
	protected static boolean checkValidLocation(Location location) {
		
		if(Papi.Function.isSomePlayersNear(location, MIN_OTHER_PLAYER_DISTANCE))
			return false;
		
		Location tempLocationX = location.clone();
		//Y0
		if(checkZ(tempLocationX)) {
			//Y-1
			tempLocationX.setX(tempLocationX.getX()-1);
			if(checkZ(tempLocationX)) {
				//Y-2
				tempLocationX.setX(tempLocationX.getX()-1);
				if(checkZ(tempLocationX)) {
					//Y+1
					tempLocationX.setX(tempLocationX.getX()+3);
					if(checkZ(tempLocationX)) {
						//Y+2
						tempLocationX.setX(tempLocationX.getX()+1);
						if(checkZ(tempLocationX)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	private static boolean checkY(Location tempLocationZ) {
		Location tempLocationY = tempLocationZ.clone();
		for(int i=0;i<6;i++) {
			if(!isAllowedSpawnBlock(tempLocationY) || (i>0 && !isAllowedAirBlock(tempLocationY)) || (i==0 && !isAllowedStayBlock(tempLocationY))) {
				return false;
			}
			tempLocationY.setY(tempLocationY.getY()+1);
		}
		return true;
	}
	
	private static boolean checkZ(Location tempLocationX) {
		Location tempLocationZ = tempLocationX.clone();
		//Z0
		if(checkY(tempLocationZ)) {
			//Z-1
			tempLocationZ.setZ(tempLocationZ.getZ()-1);
			if(checkY(tempLocationZ)) {
				//Z-2
				tempLocationZ.setZ(tempLocationZ.getZ()-1);
				if(checkY(tempLocationZ)) {
					//Z+1
					tempLocationZ.setZ(tempLocationZ.getZ()+3);
					if(checkY(tempLocationZ)) {
						//Z+2
						tempLocationZ.setZ(tempLocationZ.getZ()+1);
						if(checkY(tempLocationZ)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}

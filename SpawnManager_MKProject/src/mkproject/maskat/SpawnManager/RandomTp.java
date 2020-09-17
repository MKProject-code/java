package mkproject.maskat.SpawnManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Bed.Part;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;


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
	
	protected static void preparePlayerSpawnArea(Player player, boolean bigZone) {
		Location loc = Papi.Model.getPlayer(player).getPlayerSpawnLocation().clone();
		if(bigZone) {
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
		else
		{
			loc.setX(loc.getX()-2);
			loc.setZ(loc.getZ()-2);
			for(int i1=-2; i1<=2; i1++)
			{
				for(int i2=-2; i2<=2; i2++)
				{
					if(loc.getBlock().getType() != Material.AIR && isAllowedAirBlock(loc))
						loc.getBlock().setType(Material.AIR);
					
					loc.setZ(loc.getZ()+1);
				}
				loc.setZ(loc.getZ()-5);
				loc.setX(loc.getX()+1);
			}
		}
	}
	
	protected static void updatePlayerSpawnLocationAsync(Player player, World world) {
		Thread thread = new Thread() {
			public void run() {
				Location loc = RandomTp.getRandomLocationTask(player, Papi.Server.getSurvivalWorld(), true);
		    	if(loc == null)
		    		return;
		    	Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
		    		@Override
		    		public void run() {
		    			Papi.MySQL.set(Map.of(Database.Users.PLAYER_SURVIVAL_SPAWN_LOCATION, Papi.Function.getLocationToString(loc, false, false)), Database.Users.USERNAME, "=", player.getName().toLowerCase(), Database.Users.TABLE);
		    			Papi.Model.getPlayer(player).initPlayerSpawnLocation(loc);
		    		}
		    	});
			}
		};
		thread.start();
		
//		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable() {
//		      @Override
//		      public void run() {
//		    	  Location loc = RandomTp.getRandomLocationTask(player, Papi.Server.getSurvivalWorld());
//		    	  if(loc == null)
//		    		  return;
//		    	  Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
//				      @Override
//				      public void run() {
//				    	  Papi.MySQL.set(Map.of(Database.Users.PLAYER_SURVIVAL_SPAWN_LOCATION, Papi.Function.getLocationToString(loc, false, false)), Database.Users.USERNAME, "=", player.getName().toLowerCase(), Database.Users.TABLE);
//				    	  Papi.Model.getPlayer(player).initPlayerSpawnLocation(loc);
//				      }});
//		      }});
	}
	
	protected static Location getRandomLocationTask(Player player, World world, boolean checkFullValid) {
		if(player != null && !player.isOnline())
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
		
		if(checkValidLocation(randomLocation, checkFullValid)) {
			randomLocation.setY(randomLocation.getY()+1);
			return randomLocation;
		}
		else
			return getRandomLocationTask(player, world, checkFullValid);
		
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
	
	protected static boolean checkValidLocation(Location location, boolean checkFullValid) {
		
		if(Papi.Function.isSomePlayersNear(location, MIN_OTHER_PLAYER_DISTANCE))
			return false;
		
		if(!checkFullValid)
		{
			Location tempLocation = location.clone();
			if(!isAllowedSpawnBlock(tempLocation))
				return false;
			
			tempLocation.setY(tempLocation.getY()+1);
			if(!isAllowedSpawnBlock(tempLocation))
				return false;
			
			tempLocation.setY(tempLocation.getY()+1);
			if(!isAllowedSpawnBlock(tempLocation))
				return false;
			
			tempLocation.setY(tempLocation.getY()+1);
			if(tempLocation.getBlock().getType() == Material.LAVA)
				return false;
			
			return true;
		}
		
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

	public static void preparePlayerSpawnAreaWithoutPasteSchematic(Player player) {
		Location loc = Papi.Model.getPlayer(player).getPlayerSpawnLocation().clone();
		
		loc.setZ(loc.getZ()+1);
		loc.getBlock().setType(Material.CHEST);
		
		Chest chest =  (Chest)loc.getBlock().getState();
		Inventory inv = chest.getInventory();
		inv.addItem(new ItemStack(Material.WOODEN_SWORD));
		inv.addItem(new ItemStack(Material.WOODEN_PICKAXE));
		inv.addItem(new ItemStack(Material.WOODEN_SHOVEL));
		inv.addItem(new ItemStack(Material.WOODEN_AXE));
		inv.addItem(new ItemStack(Material.BREAD, 10));
		inv.addItem(new ItemStack(Material.BONE));
		
		ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta bookMeta = (BookMeta)bookItem.getItemMeta();
		bookMeta.setAuthor(Message.getColorMessage("&bSkyidea.pl"));
		bookMeta.setTitle(Message.getColorMessage("&6Księga startowa"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		bookMeta.setLore(List.of(Message.getColorMessage("&f&oWłasność: &7&o"+player.getName()),Message.getColorMessage("&f&oDnia: &7&o"+LocalDateTime.now().format(formatter))));
		bookMeta.setUnbreakable(false);
		bookMeta.addPage(Message.getColorMessage(
				"&2&lWitaj przybyszu!&r\n"
				+"&9Rozpocząłeś Prawdziwy Survival :)&r\n\n"
				+"Spróbuj przetrwać w tym niebezpiecznym, a jakże cudownym świecie. W ciężkiej wędrówce potowarzyszy ci twój wilk, który będzie bronił twojego terytorium! Zwiedzaj, buduj, rób co chcesz!"
				));
		bookMeta.addPage(Message.getColorMessage(
				"Możesz dostać się do piekła lub do \"Endu\", ale uważaj, tam może być bardzo niebezpiecznie!\n\n"
				+ "&6Aby otworzyć menu serwera wpisz &l/menu"
				+"\n\n&2&lPowodzenia!"
				));
		bookItem.setItemMeta(bookMeta);
		inv.addItem(bookItem);
		
		
		loc.setZ(loc.getZ()-2);
		loc.setX(loc.getX()+1);
		loc.getBlock().setType(Material.RED_BED);
		
		Bed bed = (Bed)loc.getBlock().getBlockData();
		bed.setPart(Part.HEAD);
		loc.getBlock().setBlockData(bed);
		
		loc.setZ(loc.getZ()+1);
		loc.getBlock().setType(Material.RED_BED);
		
		bed = (Bed)loc.getBlock().getBlockData();
		bed.setPart(Part.FOOT);
		loc.getBlock().setBlockData(bed);
	}
}

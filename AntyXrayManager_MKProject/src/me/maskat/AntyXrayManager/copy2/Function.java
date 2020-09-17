package me.maskat.AntyXrayManager.copy2;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;

public class Function {
	
	public static void doHider(Player player, World world, int x, int y, int z, Block lookingBlockFar, Block lookingBlockNear) {
		 List<Location> circleblocks = getCircleBlocks(world, x, y, z, 10, 20, false, true, 1);
		 
		 for(Location blockLoc : circleblocks) {
			 blocksAroundCheck(player, blockLoc.getBlock(), lookingBlockNear);
		 }
		 
		 if(lookingBlockNear == null && lookingBlockFar != null) {
			 circleblocks = getCircleBlocks(world, lookingBlockFar.getX(), lookingBlockFar.getY(), lookingBlockFar.getZ(), 10, 20, false, true, 1);
			 
			 for(Location blockLoc : circleblocks) {
				 blocksAroundCheck(player, blockLoc.getBlock(), lookingBlockNear);
			 }
		 }
	}
	
	protected static void blockBreakCheckAround(Player player, Block block) {
		
		World world = block.getWorld();
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		
		Block checkBlock = world.getBlockAt(x+1, y, z);
		if(Model.getHiddenMaterialsList(world).contains(checkBlock.getType())) {
			player.sendBlockChange(checkBlock.getLocation(), checkBlock.getBlockData());
			Model.getPlayer(player).removeHiddenBlock(checkBlock.getLocation());
		}
		checkBlock = world.getBlockAt(x-1, y, z);
		if(Model.getHiddenMaterialsList(world).contains(checkBlock.getType())) {
			player.sendBlockChange(checkBlock.getLocation(), checkBlock.getBlockData());
			Model.getPlayer(player).removeHiddenBlock(checkBlock.getLocation());
		}
		checkBlock = world.getBlockAt(x, y+1, z);
		if(Model.getHiddenMaterialsList(world).contains(checkBlock.getType())) {
			player.sendBlockChange(checkBlock.getLocation(), checkBlock.getBlockData());
			Model.getPlayer(player).removeHiddenBlock(checkBlock.getLocation());
		}
		checkBlock = world.getBlockAt(x, y-1, z);
		if(Model.getHiddenMaterialsList(world).contains(checkBlock.getType())) {
			player.sendBlockChange(checkBlock.getLocation(), checkBlock.getBlockData());
			Model.getPlayer(player).removeHiddenBlock(checkBlock.getLocation());
		}
		checkBlock = world.getBlockAt(x, y, z+1);
		if(Model.getHiddenMaterialsList(world).contains(checkBlock.getType())) {
			player.sendBlockChange(checkBlock.getLocation(), checkBlock.getBlockData());
			Model.getPlayer(player).removeHiddenBlock(checkBlock.getLocation());
		}
		checkBlock = world.getBlockAt(x, y, z-1);
		if(Model.getHiddenMaterialsList(world).contains(checkBlock.getType())) {
			player.sendBlockChange(checkBlock.getLocation(), checkBlock.getBlockData());
			Model.getPlayer(player).removeHiddenBlock(checkBlock.getLocation());
		}
	}
	
	protected static void blocksAroundCheck(Player player, Block block, Block lookingBlockNear) {

		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		
		boolean hideBlock = true;
		boolean showBlock = false;
		if(lookingBlockNear != null) {
			int xLooking = lookingBlockNear.getX();
			int yLooking = lookingBlockNear.getY();
			int zLooking = lookingBlockNear.getZ();
			
			if(x == xLooking+1 && y == yLooking && z == zLooking)
				showBlock = true;
			else if(x == xLooking-1 && y == yLooking && z == zLooking)
				showBlock = true;
			else if(x == xLooking && y == xLooking+1 && z == zLooking)
				showBlock = true;
			else if(x == xLooking && y == xLooking-1 && z == zLooking)
				showBlock = true;
			else if(x == xLooking && y == yLooking && z == xLooking+1)
				showBlock = true;
			else if(x == xLooking && y == yLooking && z == xLooking-1)
				showBlock = true;
		}
		
		if(Model.getPlayer(player).existHiddenBlock(block.getLocation()) || !Model.getHiddenMaterialsList(block.getWorld()).contains(block.getType()))
			hideBlock = false;
		
		if(!showBlock && hideBlock) {
			Material material = block.getWorld().getBlockAt(x+1, y, z).getType();
			if(material.isTransparent() || material == Material.WATER || material == Material.LAVA)
				hideBlock = false;
			else {
				material = block.getWorld().getBlockAt(x-1, y, z).getType();
				if(material.isTransparent() || material == Material.WATER || material == Material.LAVA)
					hideBlock = false;
				else {
					material = block.getWorld().getBlockAt(x, y+1, z).getType();
					if(material.isTransparent() || material == Material.WATER || material == Material.LAVA)
						hideBlock = false;
					else {
						material = block.getWorld().getBlockAt(x, y-1, z).getType();
						if(material.isTransparent() || material == Material.WATER || material == Material.LAVA)
							hideBlock = false;
						else {
							material = block.getWorld().getBlockAt(x, y, z+1).getType();
							if(material.isTransparent() || material == Material.WATER || material == Material.LAVA)
								hideBlock = false;
							else {
								material = block.getWorld().getBlockAt(x, y, z-1).getType();
								if(material.isTransparent() || material == Material.WATER || material == Material.LAVA)
									hideBlock = false;
							}
						}
					}
				}
			}
		}
		if(showBlock)
		{
			player.sendBlockChange(block.getLocation(), block.getBlockData());
			Model.getPlayer(player).removeHiddenBlock(block.getLocation());
			for(ModelPlayer modelPlayer : Model.getPlayers())
			{
				if(modelPlayer.existHiddenBlock(block.getLocation()))
				{
					player.sendBlockChange(block.getLocation(), block.getBlockData());
					modelPlayer.removeHiddenBlock(block.getLocation());
				}
			}
		}
		else if(hideBlock)
		{
			player.sendBlockChange(block.getLocation(), getRandomMaterialToShow(block.getWorld()).createBlockData());
			Model.getPlayer(player).addHiddenBlock(block.getLocation());
		}
//		else
//			player.sendBlockChange(block.getLocation(), block.getBlockData());
	}
//	
//	protected static void blockCheck(Player player, int x, int y, int z) {
//		
//		Block block = Plugin.world.getBlockAt(x, y, z);
//		
//		if(!Plugin.hiddenMaterials.contains(block.getType()))
//			return;
//		
//		boolean hideBlock = true;
//		
//
////			
//////		int radius = 1;
////		
//////		for(int xx=x; xx < x+radius; xx++) {
//////			for(int zz=z; zz < z+radius; zz++) {
//////				for(int yy=y; yy < y+radius; yy++) {
//////					Block blockAround = Plugin.world.getBlockAt(x, y, z);
//////					if(blockAround.getType().isAir()) {
//////						hideBlock = false;
//////						break;
//////					}
//////				}
//////				if(!hideBlock)
//////					break;
//////			}
//////			if(!hideBlock)
//////				break;
//////		}
//////		
//////		if(hideBlock)
//////		{
//////			for(int xx=x; xx > x-radius; xx--) {
//////				for(int zz=z; zz > z-radius; zz--) {
//////					for(int yy=y; yy > y-radius; yy--) {
//////						Block blockAround = Plugin.world.getBlockAt(x, y, z);
//////						if(blockAround.getType().isAir()) {
//////							hideBlock = false;
//////							break;
//////						}
//////					}
//////					if(!hideBlock)
//////						break;
//////				}
//////				if(!hideBlock)
//////					break;
//////			}
//////		}
////		
//		if(hideBlock)
//			player.sendBlockChange(block.getLocation(), getRandomMaterialToShow().createBlockData());
//		else
//			player.sendBlockChange(block.getLocation(), block.getBlockData());
//	}
	
	private static Material getRandomMaterialToShow(World world) {
		return Model.getMaterialsToShowList(world).get(Papi.Function.randomInteger(0, Model.getMaterialsToShowList(world).size()-1));
	}
	
    private static List<Location> getCircleBlocks(World world, int cx, int cy, int cz, int radius, int height, boolean hollow, boolean sphere, int plusY){
        List<Location> circleblocks = new ArrayList<Location>();
 
        for(int x = cx - radius; x <= cx + radius; x++){
            for (int z = cz - radius; z <= cz + radius; z++){
                for(int y = (sphere ? cy - radius : cy); y < (sphere ? cy + radius : cy + height); y++){
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
 
                    if(dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1))){
                        Location l = new Location(world, x, y + plusY, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
 
        return circleblocks;
    }


}

package mkproject.maskat.Papi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import mkproject.maskat.Papi.Utils.Message;

abstract public class PapiRegion {

	protected static Collection<Block> copyArea(Location fromPointStart, Location fromPointEnd, Location toPointStart) {
		World fromWorld = fromPointStart.getWorld();
		World toWorld = toPointStart.getWorld();
		
    	int fromStartX = Math.min(fromPointStart.getBlockX(), fromPointEnd.getBlockX());
    	int fromEndX = Math.max(fromPointStart.getBlockX(), fromPointEnd.getBlockX());
    	
    	int fromStartY = Math.min(fromPointStart.getBlockY(), fromPointEnd.getBlockY());
    	int fromEndY = Math.max(fromPointStart.getBlockY(), fromPointEnd.getBlockY());
    	
    	int fromStartZ = Math.min(fromPointStart.getBlockZ(), fromPointEnd.getBlockZ());
    	int fromEndZ = Math.max(fromPointStart.getBlockZ(), fromPointEnd.getBlockZ());

    	int toStartX = Math.min(toPointStart.getBlockX(),
    			toPointStart.getBlockX() + (
    					Papi.Function.getDifference(fromPointStart.getBlockX(), fromPointEnd.getBlockX())
    					* (fromPointEnd.getBlockX() > fromPointStart.getBlockX() ? 1 : -1)));

    	int toStartY = Math.min(toPointStart.getBlockY(),
    			toPointStart.getBlockY() + (
    					Papi.Function.getDifference(fromPointStart.getBlockY(), fromPointEnd.getBlockY())
    					* (fromPointEnd.getBlockY() > fromPointStart.getBlockY() ? 1 : -1)));

    	int toStartZ = Math.min(toPointStart.getBlockZ(),
    			toPointStart.getBlockZ() + (
    					Papi.Function.getDifference(fromPointStart.getBlockZ(), fromPointEnd.getBlockZ())
    					* (fromPointEnd.getBlockZ() > fromPointStart.getBlockZ() ? 1 : -1)));

    	Collection<Block> blocks = new ArrayList<>();
    	
    	for(int iX=fromStartX; iX<=fromEndX; iX++)
    	{
	    	for(int iY=fromStartY; iY<=fromEndY; iY++)
	    	{
		    	for(int iZ=fromStartZ; iZ<=fromEndZ; iZ++)
		    	{
		    		BlockData fromBlockData = fromWorld.getBlockAt(iX, iY, iZ).getBlockData();
		    		int toX = toStartX + Papi.Function.getDifference(fromStartX, iX);
		    		int toY = toStartY + Papi.Function.getDifference(fromStartY, iY);
		    		int toZ = toStartZ + Papi.Function.getDifference(fromStartZ, iZ);
		    		
		    		Block toBlock = toWorld.getBlockAt(toX, toY, toZ);
		    		toBlock.setType(fromBlockData.getMaterial());
		    		toBlock.setBlockData(fromBlockData);
		    		
		    		blocks.add(toBlock);
		    	}
	    	}
    	}
    	return blocks;
	}
	
	protected static Collection<Block> getBlocks(Location pointStart, Location pointEnd) {
		World world = pointStart.getWorld();
		
    	int fromStartX = Math.min(pointStart.getBlockX(), pointEnd.getBlockX());
    	int fromEndX = Math.max(pointStart.getBlockX(), pointEnd.getBlockX());
    	
    	int fromStartY = Math.min(pointStart.getBlockY(), pointEnd.getBlockY());
    	int fromEndY = Math.max(pointStart.getBlockY(), pointEnd.getBlockY());
    	
    	int fromStartZ = Math.min(pointStart.getBlockZ(), pointEnd.getBlockZ());
    	int fromEndZ = Math.max(pointStart.getBlockZ(), pointEnd.getBlockZ());
		
    	Collection<Block> blocks = new ArrayList<>();
    	
    	for(int iX=fromStartX; iX<=fromEndX; iX++)
    	{
	    	for(int iY=fromStartY; iY<=fromEndY; iY++)
	    	{
		    	for(int iZ=fromStartZ; iZ<=fromEndZ; iZ++)
		    	{
		    		blocks.add(world.getBlockAt(iX, iY, iZ));
		    	}
	    	}
    	}
    	
    	return blocks;
	}
	
	protected static Collection<Block> setArea(Location pointStart, Location pointEnd, Material material) {
		World world = pointStart.getWorld();
		
		int fromStartX = Math.min(pointStart.getBlockX(), pointEnd.getBlockX());
		int fromEndX = Math.max(pointStart.getBlockX(), pointEnd.getBlockX());
		
		int fromStartY = Math.min(pointStart.getBlockY(), pointEnd.getBlockY());
		int fromEndY = Math.max(pointStart.getBlockY(), pointEnd.getBlockY());
		
		int fromStartZ = Math.min(pointStart.getBlockZ(), pointEnd.getBlockZ());
		int fromEndZ = Math.max(pointStart.getBlockZ(), pointEnd.getBlockZ());
		
		Collection<Block> blocks = new ArrayList<>();
		
		for(int iX=fromStartX; iX<=fromEndX; iX++)
		{
			for(int iY=fromStartY; iY<=fromEndY; iY++)
			{
				for(int iZ=fromStartZ; iZ<=fromEndZ; iZ++)
				{
					Block block = world.getBlockAt(iX, iY, iZ);
					block.setType(material);
					blocks.add(block);
				}
			}
		}
		
		return blocks;
	}
	
	protected static Collection<Block> setAreaCyl(Location pointCenter, int radius, int height, Material material) {

		World world = pointCenter.getWorld();

		int cx = pointCenter.getBlockX();
		int cy = pointCenter.getBlockY();
		int cz = pointCenter.getBlockZ();

		Collection<Block> blocks = new ArrayList<>();

		int rSquared = radius * radius;
		for (int x = cx - radius; x <= cx + radius; x++) {
			for (int z = cz - radius; z <= cz + radius; z++) {
				if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
					for (int y = cy; y <= height; y++) {
						Block block = world.getBlockAt(x, y, z);
						block.setType(material);
						blocks.add(block);
					}
				}
			}
		}

		return blocks;
	}
	
	protected static Collection<Block> setAreaCyl(Location pointCenter, int radius, int height, List<Material> materials, boolean applyPhysics) {
		
		World world = pointCenter.getWorld();
		
		int cx = pointCenter.getBlockX();
		int cy = pointCenter.getBlockY();
		int cz = pointCenter.getBlockZ();
		
		Collection<Block> blocks = new ArrayList<>();
		
		int rSquared = radius * radius;
		for (int x = cx - radius; x <= cx + radius; x++) {
			for (int z = cz - radius; z <= cz + radius; z++) {
				if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
					for (int y = cy; y < (cy+height); y++) {
						Block block = world.getBlockAt(x, y, z);
						if(!applyPhysics) {
							BlockState blockState = block.getState();
							blockState.setType((Material) Papi.Function.getRandom(materials.toArray()));
							blockState.update(true, false);
						}
						else
						{
							block.setType((Material) Papi.Function.getRandom(materials.toArray()));
						}
						blocks.add(block);
					}
				}
			}
		}
		
		return blocks;
	}
		 
}

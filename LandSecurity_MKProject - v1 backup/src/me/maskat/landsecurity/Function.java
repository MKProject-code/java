package me.maskat.landsecurity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;

public class Function {
//	public static int[] StringToIntegerArray(String str) {
//		int[] arr = {};
//		if(!isNullOrEmpty(str))
//			arr = Arrays.stream(str.substring(1, str.length()-1).split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
//		return arr;
//	}
	
	public static List<Integer> StringToIntegerArrayList(String str) {
		if(isNullOrEmpty(str) || str.trim().equals("[]"))
			return new ArrayList<Integer>();
		List<Integer> intList = new ArrayList<Integer>();
		for (int i : Arrays.stream(str.substring(1, str.length()-1).split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray())
		{
		    intList.add(i);
		}
		return intList;
	}
	
    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }
    
    public static boolean isInLocation(Location loc1, Location loc2) {
    	if(loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockY() == loc2.getBlockY() && loc1.getBlockZ() == loc2.getBlockZ() && loc1.getWorld() == loc2.getWorld())
    		return true;
    	return false;
    }
    
    public static boolean isNearLocation(Location loc1, Location loc2, int radius) {
    	if(loc1.getWorld() != loc2.getWorld())
    		return false;
    	
    	int pos1x = loc1.getBlockX()+radius;
    	int pos2x = loc1.getBlockX()-radius;
    	int pos1z = loc1.getBlockZ()+radius;
    	int pos2z = loc1.getBlockZ()-radius;
    	
    	int minX = Math.min(pos1x, pos2x);
    	int minZ = Math.min(pos1z, pos2z);
    	
    	int maxX = Math.max(pos1x, pos2x);
    	int maxZ = Math.max(pos1z, pos2z);
    	
    	int maxY = loc1.getBlockY()+radius;
    	int minY = loc1.getBlockY()-radius;
    	
    	int locX = loc2.getBlockX();
    	int locY = loc2.getBlockY();
    	int locZ = loc2.getBlockZ();
    	
//    	If the player X is less than maxX and greater than minX.
    	if(locX <= maxX && locX >= minX && locZ <= maxZ && locZ >= minZ && locY <= maxY && locY >= minY)
    		return true;
    	return false;
    }

    
	   
//	   public static int[] StringToIntegerArray(String str) {
//	        String[] splitArray = str.substring(1, str.length()-1).split(",");
//	        int[] array = new int[splitArray.length];
//	        for(int i=0;i<splitArray.length;i++)
//	        {
//	                try {
//	                    array[i] = Integer.parseInt(splitArray[i].trim());
//	                } catch (NumberFormatException e) {
//	                    array[i]=-1;
//	                }
//	        }
//	        return array;
//	   }
}

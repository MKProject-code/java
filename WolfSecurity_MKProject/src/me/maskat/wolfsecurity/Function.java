package me.maskat.wolfsecurity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.maskat.wolfsecurity.models.Model;

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

    
    public static void checkPlayerInsideRegions(Player player, Location location, boolean checkIsAfterClaimAndUnclaim) {
    	//if(Config.debug) Bukkit.broadcastMessage("checkPlayerInsideRegions | player="+player+" | location="+location+ " | checkIsAfterClaimAndUnclaim="+checkIsAfterClaimAndUnclaim);
    	
    	List<Integer> regionsidInside = Model.Regions.getRegionsId(location);
        //List<Integer> regionsidLeave = new ArrayList<>();
        
        //int inOwnRegionId = -1;
        if(regionsidInside.size() > 0)
        {
        	List<Integer> last_regionsidInside = Model.Player(player).getInsideRegionsId();
    		for(int i=0;i<last_regionsidInside.size();i++)
    		{
    			int last_regionidInside = last_regionsidInside.get(i);
    			
//    		}
//        	for(int last_regionidInside : Model.Player(player).getInsideRegionsId())
//        	{
        		if(!regionsidInside.contains((Object)last_regionidInside))
        			Model.Player(player).LeaveRegionId(last_regionidInside, checkIsAfterClaimAndUnclaim);
        		//Model.Player(player).unsetInsideRegionId(last_regionidInside);
        		
//        		if(Model.Player(player).getOwnRegionId() == last_regionidInside)
//        		{
//        			if(Model.Player(player).existWolf())
//        				Model.Player(player).getWolfEntity().setSitting(true);
//        			
//		        	Message.sendActionBar(player, "actionbar.move.leave.owner", ImmutableMap.of(
//		        		    "region_name", Model.Region(last_regionidInside).getName()
//		        		));
//		        	Model.Player(player).unsetInsideOwnRegion();
//        		}
//        		else
//        		{
////		        	Message.sendActionBar(player, "actionbar.move.leave.other", ImmutableMap.of(
////		        		    "region_name", Model.Region(last_regionidInside).getName()
////		        		));
//        		}
        	}
        	
    		for(int i=0;i<regionsidInside.size();i++)
    		{
    			int regionidInside = regionsidInside.get(i);
//	        for(int regionidInside : regionsidInside)
//	        {
	        	Model.Player(player).EntryRegionId(regionidInside, checkIsAfterClaimAndUnclaim);
//	        	Model.Player(player).setInsideRegionId(regionidInside);
//	        	
//	    		if(Model.Player(player).getOwnRegionId() == regionidInside)
//	    		{
////	    			inOwnRegionId = regionidInside;
//	            	Model.Player(player).setInsideOwnRegion();
//	        		if(Model.Player(player).existWolf())
//	        			Model.Player(player).fixWolfSitting();
//	        		
//	            	Message.sendActionBar(player, "actionbar.move.enter.owner", ImmutableMap.of(
//	            		    "region_name", Model.Region(regionidInside).getName()
//	            		));
//	    		}
//	    		else
//	    		{
//		        	if(!Model.Region(regionidInside).isFriend(player) && !Model.Region(regionidInside).isFamily(player))
//		        	{
//		        		// atakuje jestli nie jest przyjacielem
//		        		Model.Region(regionidInside).getWolf().setTarget(player, regionidInside);
//		        	}
//		        	
//	//		        	Message.sendActionBar(player, "actionbar.move.enter.other", ImmutableMap.of(
//	//        		    		"region_name", Model.Region(regionidInside).getName()
//	//        				));
//	    		}
	        }
        }
        else
        	Model.Player(player).LeaveAllRegions();
        
//        if(inOwnRegionId > 0)
//        {
//        	Model.Player(player).setInsideOwnRegion();
//    		if(Model.Player(player).existWolf())
//    			Model.Player(player).fixWolfSitting();
//    		
//        	Message.sendActionBar(player, "actionbar.move.enter.owner", ImmutableMap.of(
//        		    "region_name", Model.Region(inOwnRegionId).getName()
//        		));
//        }
        ////////////////////////////////////////////////////////////////////
//        if(Model.Regions().containsKey(regionidInside)) {
//        	if(!Model.Player(player).isInsideAnyRegion())
//        	{
//	        	int userid = Model.Player(player).getUserId();
//	        	if(Model.Player(player).getOwnRegionId() == regionidInside)
//	        	{
//	        		Model.Player(player).setInsideOwnRegion();
//	        		
//	        		if(Model.Player(player).existWolf())
//	        			Model.Player(player).fixWolfSitting();
//	        			//Model.Player(player).getWolfEntity().setSitting(false);
//	        		
//		        	Message.sendActionBar(player, "actionbar.move.enter.owner", ImmutableMap.of(
//		        		    "region_name", Model.Region(regionidInside).getName()
//		        		));
//	        	}
//	        	else
//	        	{
////		        	Message.sendActionBar(player, "actionbar.move.enter.other", ImmutableMap.of(
////		        		    "region_name", Model.Region(regionidInside).getName()
////		        		));
//		        	
//		        	//TODO: .isEnemy(player) itp...
//		        	if(!Model.Region(regionidInside).isFriend(player))
//		        	{
//		        		// atakuje jestli nie jest przyjacielem
//		        		Model.Region(regionidInside).getWolf().setTarget(player, regionidInside);
//		        	}
//	        	}
//	        	Model.Player(player).setInsideRegionId(regionidInside);
//        	}
//        }
//        else
//        {
//        	if(Model.Player(player).isInsideAnyRegion())
//        	{
//        		int regionidLeave = Model.Player(player).getInsideRegionId();
//        		if(Model.Player(player).getOwnRegionId() == regionidLeave)
//        		{
//		        	Message.sendActionBar(player, "actionbar.move.leave.owner", ImmutableMap.of(
//		        		    "region_name", Model.Region(regionidLeave).getName()
//		        		));
//        		}
//        		else
//        		{
////		        	Message.sendActionBar(player, "actionbar.move.leave.other", ImmutableMap.of(
////		        		    "region_name", Model.Region(regionidLeave).getName()
////		        		));
//        		}
//        		Model.Player(player).unsetInsideOwnRegion();
//            	Model.Player(player).unsetInsideRegionId();
//        		if(Model.Player(player).getOwnRegionId() == regionidLeave && Model.Player(player).existWolf())
//        			Model.Player(player).getWolfEntity().setSitting(true);
//        	}
//        }
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

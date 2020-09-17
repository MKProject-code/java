package mkproject.maskat.CmdBlockAdvancedManager.CmdsExecute;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.BlockPredicateArgument;
import dev.jorel.commandapi.arguments.BlockStateArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;

public abstract class CommandFill {
	
	private static Map<Player,Timer> targetsTimerExpMap = new HashMap<>();
	private static Map<Player,BossBar> targetsBossBarMap = new HashMap<>();
	
	@SuppressWarnings({ })
	protected static void register(JavaPlugin plugin) {
		commandFill(plugin);
	}
	
	@SuppressWarnings({ "unchecked" })
	private static void commandFill(JavaPlugin plugin) {
		
	    LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	    arguments.put("from", new LocationArgument(LocationType.BLOCK_POSITION));
	    arguments.put("to", new LocationArgument(LocationType.BLOCK_POSITION));
	    arguments.put("block", new BlockStateArgument());
	    arguments.put("operation", new LiteralArgument("replacenot"));
	    arguments.put("filter", new BlockPredicateArgument());
	    
	    new CommandAPICommand("fill")
        .withArguments(arguments)
        .withPermission(CommandPermission.OP)
        .executes((sender, args) -> {
        	Location LocFrom = (Location) args[0];
        	Location LocTo = (Location) args[1];
	    	BlockData blockDataFill = (BlockData) args[2];
	    	Predicate<Block> predicate = (Predicate<Block>) args[3];

	    	if(!setBlock(LocFrom, LocTo, blockDataFill, predicate))
	    		sender.sendMessage("WORLD ERROR");
        })
        .register();
	}

	private static boolean setBlock(Location LocFrom, Location LocTo, BlockData blockDataFill, Predicate<Block> predicate) {
		World world = LocFrom.getWorld();
		
		if(world == null)
			return false;
		
    	int startX;
    	int endX;
    	if(LocFrom.getBlockX() > LocTo.getBlockX())
    	{
    		startX = LocTo.getBlockX();
    		endX = LocFrom.getBlockX();
    	}
    	else
    	{
    		startX = LocFrom.getBlockX();
    		endX = LocTo.getBlockX();
    	}
    	
    	int startY;
    	int endY;
    	if(LocFrom.getBlockY() > LocTo.getBlockY())
    	{
    		startY = LocTo.getBlockY();
    		endY = LocFrom.getBlockY();
    	}
    	else
    	{
    		startY = LocFrom.getBlockY();
    		endY = LocTo.getBlockY();
    	}
    	
    	int startZ;
    	int endZ;
    	if(LocFrom.getBlockZ() > LocTo.getBlockZ())
    	{
    		startZ = LocTo.getBlockZ();
    		endZ = LocFrom.getBlockZ();
    	}
    	else
    	{
    		startZ = LocFrom.getBlockZ();
    		endZ = LocTo.getBlockZ();
    	}
    	
    	for(int iX=startX; iX<=endX; iX++)
    	{
	    	for(int iY=startY; iY<=endY; iY++)
	    	{
		    	for(int iZ=startZ; iZ<=endZ; iZ++)
		    	{
		    		Block block = world.getBlockAt(iX, iY, iZ);
		    		if(!predicate.test(block))
		    		{
		    			block.setType(blockDataFill.getMaterial());
		    			block.setBlockData(blockDataFill);
		    		}
		    		
			        //targetBlock.setType(blockdata.getMaterial());
			        //targetBlock.getState().setBlockData(blockdata);
		    	}
	    	}
    	}
    	return true;
	}
}

package mkproject.maskat.CmdBlockAdvancedManager;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;

import mkproject.maskat.CmdBlockAdvancedManager.Models.CbamModel;
import mkproject.maskat.CmdBlockAdvancedManager.Models.CbamPlayer;
import mkproject.maskat.CmdBlockAdvancedManager.ProjectManager.ProjectManager;
import mkproject.maskat.Papi.Utils.Message;

public class Event implements Listener {
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e) {
		if(e.getBlock().getType() != Material.COMMAND_BLOCK)
			return;
		
		Location breakBlockLoc = e.getBlock().getLocation();
		
		for(Entry<Player, CbamPlayer> entry : CbamModel.getPlayersMap().entrySet()) {
			
			if(entry.getValue().isCommandBlockExist())
			{
				Location commandBlockLoc = entry.getValue().getCommandBlock().getLocation();
				
				if(commandBlockLoc.getBlockX() == breakBlockLoc.getBlockX() && commandBlockLoc.getBlockY() == breakBlockLoc.getBlockY() && commandBlockLoc.getBlockZ() == breakBlockLoc.getBlockZ())
				{
					if(entry.getKey() == e.getPlayer())
					{
						entry.getValue().unsetCommandBlock();
						Message.sendMessage(e.getPlayer(), "&c&lOperation canceled. Type /cb to reopen menu");
					}
					else
						e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		if(e.getFrom().getWorld() == e.getTo().getWorld())
			return;
			
		CbamPlayer cbamPlayer = CbamModel.getPlayer(e.getPlayer());
		
		 if(cbamPlayer == null) return;
		 
		 cbamPlayer.unsetLastPapiMenuPage();
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		//TODO !
		//ProjectManager.debugBreakProject(e.getPlayer());
		
		CbamPlayer cbamPlayer = CbamModel.getPlayer(e.getPlayer());
		
        if(cbamPlayer == null) return;
        
        if(cbamPlayer.isCommandBlockExist() && cbamPlayer.getCommandBlock().getType() == Material.COMMAND_BLOCK)
        	cbamPlayer.getCommandBlock().setType(Material.AIR);
        
        CbamModel.removePlayer(e.getPlayer());
	}

	public void onPacketReceivingSetCommandBlock(PacketEvent e) {
		CbamPlayer cbamPlayer = CbamModel.getPlayer(e.getPlayer());
		
        if(cbamPlayer == null) return;
        if(!cbamPlayer.isCommandBlockExist()) return;
        
        BlockPosition bp = e.getPacket().getBlockPositionModifier().getValues().get(0);
        
        Block commandBlock = cbamPlayer.getCommandBlock();
        Location commandBlockLocation = commandBlock.getLocation();
        
        if (bp.getX() != commandBlockLocation.getBlockX()) return;
        if (bp.getY() != commandBlockLocation.getBlockY()) return;
        if (bp.getZ() != commandBlockLocation.getBlockZ()) return;
        
        Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
		        if(cbamPlayer.updateCommandBlock(e.getPacket().getStrings().getValues().get(0)))
		        	commandBlock.setType(Material.AIR);
		        else
		        	Message.sendMessage(e.getPlayer(), "&c&lNothing update! So... we left this CommandBlock for you.");
		        
		        cbamPlayer.unsetCommandBlock();
		        
		        cbamPlayer.openLastPapiMenuPage();
			}
        });
        
        e.setCancelled(true);
	}
}

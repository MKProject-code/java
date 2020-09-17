package me.maskat.WorldEditTools;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.maskat.WorldEditTools.models.Model;
import me.maskat.WorldEditTools.models.ModelPlayer;
import me.maskat.WorldEditTools.models.ModelPlayer.Tool;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.MenuInventory.PapiMenuInventoryClickEvent;
import mkproject.maskat.Papi.Utils.Message;

public class Event implements Listener {
	
	@EventHandler
    public void onPlayerInteractEvent(final PlayerInteractEvent e) {
		if(e.getItem() == null)
			return;
		
		if(e.getItem().getType() != Material.WOODEN_SHOVEL)
			return;
		
		Player player = e.getPlayer();
		
		if(!player.hasPermission("mkp.worldedittools.use") || !Papi.Model.getPlayer(player).isLogged())
			return;
		
		e.setCancelled(true);
		//Location playerlocation = player.getLocation();
		
		if((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK))
		{	
			if(Model.getPlayer(player).isEnabledTool(Tool.SIGN_EDITOR))
			{
				Block block = player.getTargetBlockExact(200);
				if(block == null)
					return;
				
				if(block.getType() != Material.ACACIA_WALL_SIGN &&
						block.getType() != Material.BIRCH_WALL_SIGN &&
						block.getType() != Material.DARK_OAK_WALL_SIGN &&
						block.getType() != Material.JUNGLE_WALL_SIGN &&
						block.getType() != Material.OAK_WALL_SIGN &&
						block.getType() != Material.CRIMSON_WALL_SIGN &&
						block.getType() != Material.WARPED_WALL_SIGN &&
						block.getType() != Material.SPRUCE_WALL_SIGN)
					return;
				
				Sign sign = (Sign)block.getState();
				
				if(Model.getPlayer(player).writeSignLines(sign))
					Message.sendMessage(player, "&a&oZmieniono wszystkie linie na znaku");
				else
					Message.sendMessage(player, "&c&oNie zmieniono nic");
			}
			else if(Model.getPlayer(player).isEnabledTool(Tool.WORLDEDIT_SCHEMATIC))
			{
				if(Model.getPlayer(player).getSelectedSchemat() == null)
					return;
				
				Block block = player.getTargetBlockExact(200);
				if(block == null)
					return;
				
				Location blocklocation = block.getLocation().clone();
				
				String fileName = Plugin.getWorldEditPlayerSchemFolder(player) + File.separator + Model.getPlayer(player).getSelectedSchemat();
				
				blocklocation.setY(blocklocation.getY()+1);
				
				if(!Papi.Model.getPlayer(player).pasteSchematic(fileName, blocklocation, false))
				{
					Message.sendMessage(player, "&c&oSchemat nie istnieje?");
					return;
				}
			}
			else if(Model.getPlayer(player).isEnabledTool(Tool.WORLDEDIT_FAST_UNDO_REDO))
			{
				if(Papi.Model.getPlayer(player).redoWorldEdit())
					Message.sendActionBar(player, "&a&oPrzywrócono ostatnią cofniętą zmianę WorldEdit");
				else
					Message.sendMessage(player, "&c&oNie udało się przywrócić zmian WorldEdit");
			}
			else if(Model.getPlayer(player).isEnabledTool(Tool.RELATIVE_DISTANCE_CALCULATOR))
			{
				Block block = player.getTargetBlockExact(200);
				if(block == null)
					return;
				
				Location loc = block.getLocation();
				ModelPlayer modelPlayer = Model.getPlayer(player);
				modelPlayer.setSelectedRelativeDistanceLoc1(loc);
				
				Message.sendRawMessage(player, "[\"\",{\"text\":\"[\",\"color\":\"dark_gray\"},{\"text\":\"WET\",\"bold\":true,\"color\":\"dark_purple\"},{\"text\":\"]\",\"color\":\"dark_gray\"},{\"text\":\" First selected block: "+block.getType().name()+" \",\"color\":\"light_purple\"},{\"text\":\"("+loc.getBlockX()+" "+loc.getBlockY()+" "+loc.getBlockZ()+")\",\"color\":\"light_purple\",\"clickEvent\":{\"action\":\"copy_to_clipboard\",\"value\":\""+loc.getBlockX()+" "+loc.getBlockY()+" "+loc.getBlockZ()+"\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"Click to copy to clipboard\",\"italic\":true,\"color\":\"gray\"}}}]");

				if(modelPlayer.getSelectedRelativeDistanceLoc1() != null && modelPlayer.getSelectedRelativeDistanceLoc2() != null)
				{
					if(modelPlayer.getSelectedRelativeDistanceLoc1().getWorld() == modelPlayer.getSelectedRelativeDistanceLoc2().getWorld())
					{
						int x = modelPlayer.getSelectedRelativeDistanceLoc2().getBlockX() - modelPlayer.getSelectedRelativeDistanceLoc1().getBlockX();
						int y = modelPlayer.getSelectedRelativeDistanceLoc2().getBlockY() - modelPlayer.getSelectedRelativeDistanceLoc1().getBlockY();
						int z = modelPlayer.getSelectedRelativeDistanceLoc2().getBlockZ() - modelPlayer.getSelectedRelativeDistanceLoc1().getBlockZ();

						Message.sendRawMessage(player, "[\"\",{\"text\":\"[\",\"color\":\"dark_gray\"},{\"text\":\"WET\",\"bold\":true,\"color\":\"dark_purple\"},{\"text\":\"]\",\"color\":\"dark_gray\"},{\"text\":\" Relative distance position: \",\"color\":\"light_purple\"},{\"text\":\"~"+x+" ~"+y+" ~"+z+"\",\"bold\":true,\"color\":\"light_purple\",\"clickEvent\":{\"action\":\"copy_to_clipboard\",\"value\":\"~"+x+" ~"+y+" ~"+z+"\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"Click to copy to clipboard\",\"italic\":true,\"color\":\"gray\"}}}]");
					}
				}
			}
		}
		else if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getHand() == EquipmentSlot.HAND)
		{
			if(Model.getPlayer(player).isEnabledTool(Tool.SIGN_EDITOR))
			{
				Block block = player.getTargetBlockExact(200);
				if(block == null)
					return;
				
				if(block.getType() != Material.ACACIA_WALL_SIGN &&
						block.getType() != Material.BIRCH_WALL_SIGN &&
						block.getType() != Material.DARK_OAK_WALL_SIGN &&
						block.getType() != Material.JUNGLE_WALL_SIGN &&
						block.getType() != Material.OAK_WALL_SIGN &&
						block.getType() != Material.CRIMSON_WALL_SIGN &&
						block.getType() != Material.WARPED_WALL_SIGN &&
						block.getType() != Material.SPRUCE_WALL_SIGN)
					return;
				
				Sign sign = (Sign)block.getState();
				
				Model.getPlayer(player).readSignLines(sign.getLines());
				Message.sendMessage(player, "&a&oZapisano wszystkie linie do schowka");
			}
			else if(Model.getPlayer(player).isEnabledTool(Tool.WORLDEDIT_SCHEMATIC) || Model.getPlayer(player).isEnabledTool(Tool.WORLDEDIT_FAST_UNDO_REDO))
			{
				if(Papi.Model.getPlayer(player).undoWorldEdit())
					Message.sendActionBar(player, "&a&oCofnięto ostatnią zmianę WorldEdit");
				else
					Message.sendMessage(player, "&c&oNie udało się cofnąć zmian WorldEdit");
			}
			else if(Model.getPlayer(player).isEnabledTool(Tool.RELATIVE_DISTANCE_CALCULATOR))
			{
				Block block = player.getTargetBlockExact(200);
				if(block == null)
					return;
				
				Location loc = block.getLocation();
				ModelPlayer modelPlayer = Model.getPlayer(player);
				modelPlayer.setSelectedRelativeDistanceLoc2(loc);
				Message.sendRawMessage(player, "[\"\",{\"text\":\"[\",\"color\":\"dark_gray\"},{\"text\":\"WET\",\"bold\":true,\"color\":\"dark_purple\"},{\"text\":\"]\",\"color\":\"dark_gray\"},{\"text\":\" Second selected block: "+block.getType().name()+" \",\"color\":\"light_purple\"},{\"text\":\"("+loc.getBlockX()+" "+loc.getBlockY()+" "+loc.getBlockZ()+")\",\"color\":\"light_purple\",\"clickEvent\":{\"action\":\"copy_to_clipboard\",\"value\":\""+loc.getBlockX()+" "+loc.getBlockY()+" "+loc.getBlockZ()+"\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"Click to copy to clipboard\",\"italic\":true,\"color\":\"gray\"}}}]");
				
				if(modelPlayer.getSelectedRelativeDistanceLoc1() != null && modelPlayer.getSelectedRelativeDistanceLoc2() != null)
				{
					if(modelPlayer.getSelectedRelativeDistanceLoc1().getWorld() == modelPlayer.getSelectedRelativeDistanceLoc2().getWorld())
					{
						int x = modelPlayer.getSelectedRelativeDistanceLoc2().getBlockX() - modelPlayer.getSelectedRelativeDistanceLoc1().getBlockX();
						int y = modelPlayer.getSelectedRelativeDistanceLoc2().getBlockY() - modelPlayer.getSelectedRelativeDistanceLoc1().getBlockY();
						int z = modelPlayer.getSelectedRelativeDistanceLoc2().getBlockZ() - modelPlayer.getSelectedRelativeDistanceLoc1().getBlockZ();
						
						Message.sendRawMessage(player, "[\"\",{\"text\":\"[\",\"color\":\"dark_gray\"},{\"text\":\"WET\",\"bold\":true,\"color\":\"dark_purple\"},{\"text\":\"]\",\"color\":\"dark_gray\"},{\"text\":\" Relative distance position: \",\"color\":\"light_purple\"},{\"text\":\"~"+x+" ~"+y+" ~"+z+"\",\"bold\":true,\"color\":\"light_purple\",\"clickEvent\":{\"action\":\"copy_to_clipboard\",\"value\":\"~"+x+" ~"+y+" ~"+z+"\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"Click to copy to clipboard\",\"italic\":true,\"color\":\"gray\"}}}]");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
		if(!e.getPlayer().hasPermission("mkp.worldedittools.use") || !Papi.Model.getPlayer(e.getPlayer()).isLogged())
			return;
		
		if(e.getOffHandItem().getType()== Material.WOODEN_SHOVEL)
		{
			Model.getPlayer(e.getPlayer()).openInvnetoryMenu();
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPapiMenuInventoryClickEvent(PapiMenuInventoryClickEvent e) {
		if(e.getPluginExecutor() != Plugin.getPlugin())
			return;
		
		Model.getPlayer(e.getPlayer()).onPapiMenuInventoryClick(e);
	}
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Model.addPlayer(e.getPlayer());
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Model.removePlayer(e.getPlayer());
	}
}

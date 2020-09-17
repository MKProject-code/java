package mkproject.maskat.AdminUtils.Cmds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import mkproject.maskat.AdminUtils.Config;
import mkproject.maskat.AdminUtils.Config.ConfigKey;
import mkproject.maskat.AdminUtils.Plugin;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.CommandManager;
import mkproject.maskat.Papi.Utils.Message;

public class CmdEventSpawnChestItems implements CommandExecutor {

	private Collection<Material> chestMaterials = List.of(
				Material.CHEST,
				Material.CHEST_MINECART,
				Material.TRAPPED_CHEST,
				Material.BARREL
			);
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args);
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		this.eventRandomItemsChestSpawn(manager);
		
		return manager.doReturn();
	}
	
	private boolean whileEnabled = true;
	private int itemsDoneAmount = 0;
	
	// --------- /eventspawnchestitems
	private void eventRandomItemsChestSpawn(CommandManager manager) {
		
		Message.sendMessage(manager.getPlayer(), "&o&eSzukanie dostępnych skrzyń... Następnie przedmioty, które trzymasz w ręce zostaną rozrzucone po skrzyniach.\n&4NIE RÓB NIC AŻ DO ZAKOŃCZENIA TEGO PROCESU !!!");
		
		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Location startLocation = Papi.Server.getServerSpawnLocation().clone();
				World spawnWorld = startLocation.getWorld();
				
				List<Block> chestsAvailable = new ArrayList<>();
				
				for(int iX=0;iX<100;iX++)
				{
					for(int iY=60;iY<100;iY++)
					{
						for(int iZ=0;iZ<100;iZ++)
						{
							int x = startLocation.getBlockX()+iX;
							int y = iY;
							int z = startLocation.getBlockZ()+iZ;
							Block block = spawnWorld.getBlockAt(x, y, z);
							
							if(chestMaterials.contains(block.getType())) {
								chestsAvailable.add(block);
							}
						}
					}
				}
				
				for(int iX=0;iX<100;iX++)
				{
					for(int iY=60;iY<100;iY++)
					{
						for(int iZ=100;iZ>0;iZ--)
						{
							int x = startLocation.getBlockX()+iX;
							int y = iY;
							int z = startLocation.getBlockZ()-iZ;
							Block block = spawnWorld.getBlockAt(x, y, z);
							
							if(chestMaterials.contains(block.getType())) {
								chestsAvailable.add(block);
							}
						}
					}
				}
				
				for(int iX=100;iX>0;iX--)
				{
					for(int iY=60;iY<100;iY++)
					{
						for(int iZ=0;iZ<100;iZ++)
						{
							int x = startLocation.getBlockX()-iX;
							int y = iY;
							int z = startLocation.getBlockZ()+iZ;
							Block block = spawnWorld.getBlockAt(x, y, z);
							
							if(chestMaterials.contains(block.getType())) {
								chestsAvailable.add(block);
							}
						}
					}
				}
				
				for(int iX=100;iX>0;iX--)
				{
					for(int iY=60;iY<100;iY++)
					{
						for(int iZ=100;iZ>0;iZ--)
						{
							int x = startLocation.getBlockX()-iX;
							int y = iY;
							int z = startLocation.getBlockZ()-iZ;
							Block block = spawnWorld.getBlockAt(x, y, z);
							
							if(chestMaterials.contains(block.getType())) {
								chestsAvailable.add(block);
							}
						}
					}
				}
				
				Message.sendMessage(manager.getPlayer(), "&o&eWykryto skrzynek: "+chestsAvailable.size());
				
				if(chestsAvailable.size() <= 0)
					return;
				
				ItemStack itemHand = manager.getPlayer().getInventory().getItemInMainHand();
				manager.getPlayer().getInventory().setItemInMainHand(null);
				manager.getPlayer().updateInventory();
				
				Message.sendMessage(manager.getPlayer(), "&o&eRozmieszczanie przedmiotów: "+itemHand.getType()+" (x"+itemHand.getAmount()+")");
				
				whileEnabled = true;
				itemsDoneAmount = 0;
				
				while(whileEnabled) {
					Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
						@Override
						public void run() {
							if(updateRandomChest(itemHand, chestsAvailable) == -1)
								whileEnabled = false;
						}});
				}
				
				Message.sendMessage(manager.getPlayer(), "&o&aPoprawnie rozmieszczono "+itemsDoneAmount+" sztuk w dostępnych "+chestsAvailable.size()+" skrzyniach.");
				
				itemHand.setAmount(itemHand.getAmount()-itemsDoneAmount);
				manager.getPlayer().getInventory().setItemInMainHand(itemHand);
				manager.getPlayer().updateInventory();
				
				Message.sendMessage(manager.getPlayer(), "&2PROCES ZAKOŃCZONY");
			}
		});

		manager.setReturnMessage(null);
	}
	
	private int updateRandomChest(ItemStack itemHand, List<Block> chestsAvailable) {
		if(itemsDoneAmount >= itemHand.getAmount())
			return -1;
		
		Block block = chestsAvailable.get(Papi.Function.randomInteger(0, chestsAvailable.size()-1));
		try
		{
			if(block.getState() instanceof Chest || block.getState() instanceof Barrel)
			{
				Inventory chestInv;
				if(block.getState() instanceof Chest)
					chestInv = ((Chest)block.getState()).getInventory();
				else
					chestInv = ((Barrel)block.getState()).getInventory();
				
				if(chestInv.firstEmpty() >= 0)
				{
					ItemStack itemHandClone = itemHand.clone();
					itemHandClone.setAmount(1);
					
					chestInv.addItem(itemHandClone);
					itemsDoneAmount++;
					return 1;
				}
				else
				{
					chestsAvailable.remove(block);
				}
			}
			else
			{
				Plugin.getPlugin().getLogger().warning("Material '"+block.getType()+"' is not instance of Chest!");
				chestsAvailable.remove(block);
			}
		}
		catch(Exception ex)
		{
			Plugin.getPlugin().getLogger().warning("Material '"+block.getType()+"' generated exception: "+ex.getMessage());
			chestsAvailable.remove(block);
		}
		
		if(chestsAvailable.size() <= 0)
			return -1;
		
		return 0;
	}
}

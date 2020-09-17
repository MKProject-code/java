package mkproject.maskat.SpecialTools;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class Event implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreakEvent(BlockBreakEvent e) {
		Bukkit.broadcastMessage("e.getBlock()getType="+e.getBlock().getType());
		if(e.getBlock().getBlockData() instanceof Ageable)
		{
			ItemStack itemStackInMainHand = e.getPlayer().getInventory().getItemInMainHand();
			Damageable damagableItemInMainHand = (Damageable)itemStackInMainHand.getItemMeta();
			damagableItemInMainHand = autoBreakBlockValid(damagableItemInMainHand, e.getBlock(), 1);
			itemStackInMainHand.setItemMeta((ItemMeta)damagableItemInMainHand);
			e.getPlayer().getInventory().setItemInMainHand(itemStackInMainHand);
			e.getPlayer().updateInventory();
		}
		
//		Bukkit.broadcastMessage("getBlockData="+e.getBlock().getBlockData().getAsString());
//		Bukkit.broadcastMessage("getBlockData.getAsString="+e.getBlock().getBlockData().getAsString());
//		
//		ItemStack itemStack = e.getPlayer().getInventory().getItemInMainHand();
//		if(itemStack.getType() == Material.NETHERITE_HOE)
//		if(Config.DropSpecialBone.getMaterialsToDrop().contains(e.getBlock().getType().toString().toUpperCase()))
//		{
//			if(Papi.Function.randomInteger(1, 100) <= Config.DropSpecialBone.getProcentageChance()) {
//				RewardSpecialBone.dropReward(e.getBlock());
//				e.setDropItems(false);
//			}
//		}
	}
	
	public Damageable autoBreakBlockValid(Damageable damagableItemInMainHand, Block startBlock, int radius) {
		if(damagableItemInMainHand.hasDamage())
			return damagableItemInMainHand;
//		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getPlugin(), new Runnable() {
//			@Override
//			public void run() {
				int radiusFixed = radius+1;
				Material material = startBlock.getType();
				World world = startBlock.getWorld();
				Location location = startBlock.getLocation();
				int xDef = location.getBlockX();
				int yDef = location.getBlockY();
				int zDef = location.getBlockZ();
				for(int x=xDef; x<xDef+radiusFixed; x++)
				{
					for(int z=zDef; z<zDef+radiusFixed; z++)
					{
						if(x!=xDef || z!=zDef) {
							Block checkBlock = world.getBlockAt(x, yDef, z);
							if(checkBlock.getType() == material)
							{
								Ageable ageable = (Ageable)checkBlock.getBlockData();
								if(true || ageable.getAge() == ageable.getMaximumAge())
								{
									checkBlock.breakNaturally();
									damagableItemInMainHand.setDamage(damagableItemInMainHand.getDamage()-1);
									if(damagableItemInMainHand.hasDamage())
										return damagableItemInMainHand;
								}
							}
						}
					}
					for(int z=zDef; z>zDef-radiusFixed; z--)
					{
						if(x!=xDef || z!=zDef) {
							Block checkBlock = world.getBlockAt(x, yDef, z);
							if(checkBlock.getType() == material)
							{
								Ageable ageable = (Ageable)checkBlock.getBlockData();
								if(true || ageable.getAge() == ageable.getMaximumAge())
								{
									checkBlock.breakNaturally();
									damagableItemInMainHand.setDamage(damagableItemInMainHand.getDamage()-1);
									if(damagableItemInMainHand.hasDamage())
										return damagableItemInMainHand;
								}
							}
						}
					}
				}
				for(int x=xDef; x>xDef-radiusFixed; x--)
				{
					for(int z=zDef; z<zDef+radiusFixed; z++)
					{
						if(x!=xDef || z!=zDef) {
							Block checkBlock = world.getBlockAt(x, yDef, z);
							if(checkBlock.getType() == material)
							{
								Ageable ageable = (Ageable)checkBlock.getBlockData();
								if(true || ageable.getAge() == ageable.getMaximumAge())
								{
									checkBlock.breakNaturally();
									damagableItemInMainHand.setDamage(damagableItemInMainHand.getDamage()-1);
									if(damagableItemInMainHand.hasDamage())
										return damagableItemInMainHand;
								}
							}
						}
					}
					for(int z=zDef; z>zDef-radiusFixed; z--)
					{
						if(x!=xDef || z!=zDef) {
							Block checkBlock = world.getBlockAt(x, yDef, z);
							if(checkBlock.getType() == material)
							{
								Ageable ageable = (Ageable)checkBlock.getBlockData();
								if(true || ageable.getAge() == ageable.getMaximumAge())
								{
									checkBlock.breakNaturally();
									damagableItemInMainHand.setDamage(damagableItemInMainHand.getDamage()-1);
									if(damagableItemInMainHand.hasDamage())
										return damagableItemInMainHand;
								}
							}
						}
					}
				}
//			}
//		});
		return damagableItemInMainHand;
	}
}

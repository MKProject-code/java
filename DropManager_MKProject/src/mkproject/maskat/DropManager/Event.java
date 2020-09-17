package mkproject.maskat.DropManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import mkproject.maskat.DropManager.Rewards.VipBook;
import mkproject.maskat.DropManager.Rewards.VipPotion;
import mkproject.maskat.Papi.Papi;

public class Event implements Listener {
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e) {
//		if(Config.DropSpecialBone.getMaterialsToDrop().contains(e.getBlock().getType().toString().toUpperCase()))
//		{
//			if(Papi.Function.randomInteger(1, 100) <= Config.DropSpecialBone.getProcentageChance()) {
//				SpecialBone.dropReward(e.getBlock());
//				e.setDropItems(false);
//			}
//		}
		if(!e.getPlayer().hasPermission("group.vip")) {
			if(Config.VipPotion.getMaterialsToDrop().contains(e.getBlock().getType().toString().toUpperCase()))
			{
				if(Papi.Function.randomInteger(1, 1000) <= Config.VipPotion.getChance()) {
					VipPotion.dropReward(e.getPlayer(), e.getBlock().getLocation());
				}
			}
		}
		else
		{
			if(Config.VipBook.getMaterialsToDrop().contains(e.getBlock().getType().toString().toUpperCase()))
			{
				if(Papi.Function.randomInteger(1, 1000) <= Config.VipBook.getChance()) {
					VipBook.dropReward(e.getPlayer(), e.getBlock().getLocation());
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent e) {
		if(e.getEntity().getKiller() == null)
			return;
		
		if(!e.getEntity().getKiller().hasPermission("group.vip")) {
			if(Papi.Function.randomInteger(1, 1000) <= Config.VipPotion.getChance())
				VipPotion.dropReward(e.getEntity().getKiller(), e.getEntity().getLocation());
		}
		else
		{
			if(Papi.Function.randomInteger(1, 1000) <= Config.VipBook.getChance())
				VipBook.dropReward(e.getEntity().getKiller(), e.getEntity().getLocation());
		}
	}
	
	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
		VipPotion.onPlayerItemConsumeEvent(e);
	}
}

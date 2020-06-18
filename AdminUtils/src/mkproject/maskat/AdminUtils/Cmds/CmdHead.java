package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import mkproject.maskat.AdminUtils.Plugin;

public class CmdHead implements CommandExecutor {
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager_local manager = new CommandManager_local(sender, command, label, args, List.of("<headname>","[player]"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		if(manager.hasArgs(1,2))
			this.givePlayerHeadPlayer(manager, manager.getChosenPlayerFromArg(2, true), manager.getArg(1));
		
		return manager.doReturn();
	}
	
	private void givePlayerHeadPlayer(CommandManager_local manager, Player destPlayer, String headName) {
		if(destPlayer == null)
			return;
		
		this.taskPlayerHeadPlayer(manager, destPlayer, headName);
		
		manager.setReturnMessage("&7&oTrwa wczytywanie głowy...");
	}
	
	private void taskPlayerHeadPlayer(CommandManager_local manager, Player destPlayer, String headName) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(Plugin.getPlugin(), new Runnable() {
            @SuppressWarnings("deprecation")
			@Override
            public void run() {
            	try {
	        		ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
	                SkullMeta meta = (SkullMeta) skull.getItemMeta();
	                
	                Player player = Bukkit.getPlayer(headName);
	                OfflinePlayer offlinePlayer;
	                if(player != null)
	                	offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());
	                else
	                	offlinePlayer = Bukkit.getOfflinePlayer(headName);
	                meta.setOwningPlayer(offlinePlayer);
	                skull.setItemMeta(meta);

	        		if(!manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
	        		{
	        			manager.doReturn();
	        			return;
	        		}
	        		
                	destPlayer.getInventory().setItemInMainHand(skull);
                	manager.sendMessage(manager.getMessageWhoIsPlayer(destPlayer,
                			"&a&oDodałeś sobie głowę gracza &b&o"+headName,
                			"&a&oDodałeś graczowi &e&o"+destPlayer.getName()+"&a&o głowę gracza &b&o"+headName));
        		} catch (Exception ex) {
        			manager.sendMessage("&c&oUpss... Nie udało się uzyskać głowy tego gracza :(");
        		}
            }
        }, 1L);
	}
}
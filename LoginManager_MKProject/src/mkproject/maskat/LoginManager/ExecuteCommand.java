package mkproject.maskat.LoginManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ExecuteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length != 0) {
			if(label.equalsIgnoreCase("loginmanager")) {
				if(args[0].equalsIgnoreCase("reload")) {
				    Plugin.reloadAllConfigs();
				    
				    sender.sendMessage("Reloaded successfully.");
				    return true;
				}
			}
			
//			if(label.equalsIgnoreCase("loginmanager")) {
//				if(args[0].equalsIgnoreCase("invget")) {
//				    Player player = (Player)sender;
//				    
//				    Player playerDestination = Bukkit.getPlayer(args[1]);
//				    
//					player.setExp(0);
//					
//					player.updateInventory();
//					
//					player.setExp(playerDestination.getExp());
//					player.getInventory().setContents(playerDestination.getInventory().getContents());
//					player.getInventory().setArmorContents(playerDestination.getInventory().getArmorContents());
//					
//					player.updateInventory();
//				    
//				    sender.sendMessage("Inv get successfully.");
//				    return true;
//				}
//			}
//			else if(sender instanceof Player) {
//				Player player = (Player)sender;
//				if(Papi.Model.getPlayer(player).isLogged()) {
//					Message.sendMessage(player, Plugin.getLanguageYaml().getString("Player.AlreadyLogged"));
//					return true;
//				}
//				
//				if(Model.getPlayer(player).isRegistered()) {
//					if(label.equalsIgnoreCase("login") || label.equalsIgnoreCase("l")) {
//						Login.onExecuteCommandLogin(player, Message.getCommandMessage(args, 0));
//					    return true;
//					}
//				} else {
//					if(label.equalsIgnoreCase("register") || label.equalsIgnoreCase("reg")) {
//						Login.onExecuteCommandRegister(player, Message.getCommandMessage(args, 0));
//					    return true;
//					}
//				}
//			}
		}
	
	  return false;
	}
}
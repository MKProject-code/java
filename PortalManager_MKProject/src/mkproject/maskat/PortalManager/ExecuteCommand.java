package mkproject.maskat.PortalManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Utils.Message;
import mkproject.maskat.PortalManager.Config.ConfigKey;

public class ExecuteCommand implements CommandExecutor {

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		if (!command.getName().equalsIgnoreCase("portalmanager"))
            return false;
        
		Player player = (Player)sender;
		
		if(!player.hasPermission("mkp.portalmanager.admin"))
			return false;
		
		if(args.length < 1)
		{
			Message.sendMessage(player, "&c---- Portal Manager by MasKAT ----\n"
					+ "&c/portalmanager create <name>\n"
					+ "&c/portalmanager remove <name>\n"
					+ "&c/portalmanager settp tome <name>\n"
					+ "&c/portalmanager settp toserverspawn <name>\n"
					+ "&c/portalmanager settp toplayersurvivalspawn <name>\n"
					+ "&c/portalmanager list");
			return false;
		}
		else if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("list")) {
				Manager.Admin.getListPortal(player);
				return false;
			}
		}
		else if(args.length == 2)
		{
			if(args[0].equalsIgnoreCase("create")) {
//				if(!player.getWorld().getName().equalsIgnoreCase(Config.getString(ConfigKey.CustomPortalsWorldName)))
//				{
//					Message.sendMessage(player, "&cPortale można tworzyć tylko na dozwolonej mapie.");
//					return false;
//				}
					
				if(args[1].length() > 30)
				{
					Message.sendMessage(player, "&cNazwa portalu może mieć maksymalnie 30 znaków!");
					return false;
				}
				Manager.Admin.createPortal(player, args[1]);
				return false;
			}
			else if(args[0].equalsIgnoreCase("remove")) {
				Manager.Admin.removePortal(player, args[1]);
				return false;
			}
		}
		else if(args.length == 3)
		{
			if(args[0].equalsIgnoreCase("settp") && (
					args[1].equalsIgnoreCase("tome") ||
					args[1].equalsIgnoreCase("toserverspawn") ||
					args[1].equalsIgnoreCase("toplayersurvivalspawn")
					)) {
				Manager.Admin.setTpPortal(player, args[2], args[1]);
				return false;
			}
		}
		
		Message.sendMessage(player, "&cBłędna składnia komendy.");
        return false;
	}

}

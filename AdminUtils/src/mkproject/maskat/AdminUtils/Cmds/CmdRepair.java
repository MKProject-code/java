package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CmdRepair implements CommandExecutor {
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager_local manager = new CommandManager_local(sender, command, label, args, List.of("[player]"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
			return manager.doReturn();
		
		if(manager.hasArgs(0,1))// /repair [player]
			this.doRepair(manager, manager.getChosenPlayerFromArg(1, true));
		
		return manager.doReturn();
	}
	
	// --------- /repair [player]
	@SuppressWarnings("deprecation")
	public void doRepair(CommandManager_local manager, Player destPlayer) {
		if(destPlayer == null)
			return;
		
		try {
			destPlayer.getInventory().getItemInMainHand().setDurability((short)0);
		}
		catch(Exception ex) {
			manager.setReturnMessage(destPlayer,
					"&c&oNie udało sie naprawić aktualnie trzymanego przedmiotu",
					"&c&oNie udało sie naprawić aktualnie trzymanego przedmiotu przez gracza &e&o"+destPlayer.getName());
			return;
		}
		manager.setReturnMessage(destPlayer,
				"&a&oNaprawiłeś aktualnie trzymany przedmiot",
				"&a&oNaprawiłeś aktualnie trzymany przedmiot przez gracza &e&o"+destPlayer.getName());
	}
}

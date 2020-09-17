package mkproject.maskat.AdminUtils.Cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CmdGive implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		// TODO Auto-generated method stub
		return false;
	}

}
//
//else if(CmdPlayer.hasLabel("give"))
//{
//	if(args.length == 1)
//		if(!CmdPlayer.isLabelPermissionSelf())
//			result = MSG.ACCESS_DENIED;
//		else
//			result = CmdPlayer.giveItemToPlayer(null, args[0]);
//	else if(args.length == 2)
//		if(!CmdPlayer.isLabelPermissionOther())
//			result = MSG.ACCESS_DENIED;
//		else
//			result = CmdPlayer.giveItemToPlayer(args[0], args[1]);
//	
//	else if(CmdPlayer.isLabelPermissionOther())
//		result = "&6Użyj: &e/give [player] <itemID|itemName>";
//	else if(CmdPlayer.isLabelPermissionSelf())
//		result = "&6Użyj: &e/give <itemID|itemName>";
//	else
//		result = MSG.ACCESS_DENIED;
//}

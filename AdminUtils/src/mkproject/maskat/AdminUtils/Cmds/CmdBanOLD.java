package mkproject.maskat.AdminUtils.Cmds;

import java.time.LocalDateTime;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import mkproject.maskat.AdminUtils.Database;
import mkproject.maskat.AdminUtils.Plugin;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.CommandManager;
import mkproject.maskat.Papi.Utils.Message;

public class CmdBanOLD implements CommandExecutor, TabCompleter {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args);

		List<String> values = manager.getValuesRangeList(1,60);
		values.add("perm");
		
		manager.registerArgTabComplete(0, manager.getOnlinePlayersCanChooseNameList(false, true));
		manager.registerArgTabComplete(1, null, values);
		manager.registerArgTabComplete(2, null, manager.getValuesRangeList(1,60), List.of("seconds","minutes","hours","days"));
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args, List.of("<player>", "<time|perm>", "<[timeformat]>", "[reason]"));
		
		manager.registerArgUsage(1, null,"<time|perm>","<[timeformat]>","[reason]");
		manager.registerArgUsage(2, null,null,"<[timeformat]>","[reason]");
		manager.registerArgUsage(3, null,null,null,"[reason]");
		
		if(manager.isPlayer())
			if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
				return manager.doReturn();
		
		if(manager.hasArgStart(3) && manager.hasArg(2, "perm"))
			this.ban(manager, manager.getChosenPlayerFromArg(1, false, true), 0, manager.getArg(2), manager.getStringArgStart(3));
		else if(manager.hasArgStart(4) && manager.hasArg(3, List.of("seconds", "minutes", "hours", "days")))
			this.ban(manager, manager.getChosenPlayerFromArg(1, false, true), manager.getIntArg(2), manager.getArg(3), manager.getStringArgStart(4));
		return manager.doReturn();
	}
	
	// --------- /ban <player> <time|perm> <[timeformat]> [reason]
	public void ban(CommandManager manager, Player destPlayer, Integer time, String timeformat, String reason) {
		if(destPlayer == null || time == null || timeformat == null)
			return;
		
		LocalDateTime endDatetime = null;
		String duration = "";
		
		if(timeformat.equalsIgnoreCase("perm")) {
			duration = "zawsze";
		}
		else if(timeformat.equalsIgnoreCase("seconds")) {
			if(time < 30) {
				manager.setReturnMessage("&c&oMożesz zbanować gracza minimalnie na 30 sekund");
				return;
			}
			else if(time > 59) {
				manager.setReturnMessage("&c&oWpisałeś wartość powyżej 59 sekund! Może powinieneś użyć formatu 'minutes'?");
				return;
			}
			
			if(time == 1)
				duration = time + " sekunde";
			else if(time >= 2 && time <= 4)
				duration = time + " sekundy";
			else if(time >= 5)
				duration = time + " sekund";
			endDatetime = LocalDateTime.now().plusSeconds(time);
		}
		else if(timeformat.equalsIgnoreCase("minutes")) {
			if(time < 1) {
				manager.setReturnMessage("&c&oWpisałeś wartość mniejszą lub równą 0 minut!");
				return;
			}
			else if(time > 59) {
				manager.setReturnMessage("&c&oWpisałeś wartość powyżej 59 minut! Może powinieneś użyć formatu 'hours'?");
				return;
			}
			if(time == 1)
				duration = time + " minute";
			else if(time >= 2 && time <= 4)
				duration = time + " minuty";
			else if(time >= 5)
				duration = time + " minut";
			endDatetime = LocalDateTime.now().plusMinutes(time);
		}
		else if(timeformat.equalsIgnoreCase("hours")) {
			if(time < 1) {
				manager.setReturnMessage("&c&oWpisałeś wartość mniejszą lub równą 0 godzin!");
				return;
			}
			else if(time > 23) {
				manager.setReturnMessage("&c&oWpisałeś wartość powyżej 23 godzin! Może powinieneś użyć formatu 'days'?");
				return;
			}
			if(time == 1)
				duration = time + " godzine";
			else if(time >= 2 && time <= 4)
				duration = time + " godziny";
			else if(time >= 5)
				duration = time + " godzin";
			endDatetime = LocalDateTime.now().plusHours(time);
		}
		else if(timeformat.equalsIgnoreCase("days")) {
			if(time < 1) {
				manager.setReturnMessage("&c&oWpisałeś wartość mniejszą lub równą 0 dni!");
				return;
			}
			else if(time == 1)
				duration = time + " dzień";
			else if(time >= 2)
				duration = time + " dni";
			endDatetime = LocalDateTime.now().plusDays(time);
		}
//		else if(timeformat.equalsIgnoreCase("months")) {
//			if(time == 1)
//				duration = time + " miesiąc";
//			else if(time >= 2 && time <= 4)
//				duration = time + " miesiące";
//			else if(time >= 5)
//				duration = time + " miesięcy";
//			endDatetime = LocalDateTime.now().plusMonths(time);
//		}
		
		if(reason == null)
			reason = "";
		if(Database.Bans.addBan(destPlayer, endDatetime, manager.isPlayer() ? manager.getPlayer() : null, reason) > 0)
		{
			String admin = manager.isPlayer() ? Papi.Model.getPlayer(manager.getPlayer()).getNameWithPrefix() : "serwer";
			Message.sendBroadcast(Papi.Model.getPlayer(destPlayer).getNameWithPrefix()+" &7został zbanowany na &6"+duration+"&7 przez "+admin+"&7."+((reason!="") ? ("\nPowód: &6"+reason) : ""));
			destPlayer.kickPlayer(Message.getColorMessage("&cZostałeś zbanowany na "+duration+"!"+((reason!="") ? ("\n&6Powód: &e"+reason) : "")));
			manager.setReturnMessage(null);
		}
		else
			manager.setReturnMessage("&c&oWystąpił błąd podczas próby zbanowania gracza &e&o"+destPlayer.getName());
			
	}
}

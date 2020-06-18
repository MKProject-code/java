package mkproject.maskat.AdminUtils.Cmds;

import java.time.LocalDateTime;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import mkproject.maskat.AdminUtils.Config;
import mkproject.maskat.AdminUtils.Config.ConfigKey;
import mkproject.maskat.AdminUtils.Database;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.CommandManager;
import mkproject.maskat.Papi.Utils.Message;

public class CmdMute implements CommandExecutor, TabCompleter {

	private void registerArgAliases(CommandManager manager) {
		manager.registerArgAliasAuto(3, "seconds", 1);
		manager.registerArgAliasAuto(3, "minutes", 1);
		manager.registerArgAliasAuto(3, "hours", 1);
		manager.registerArgAliasAuto(3, "days", 1);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args);
		
		this.registerArgAliases(manager);
		
		manager.registerArgTabComplete(0, manager.getOnlinePlayersNameList());
		manager.registerArgTabComplete(1, null, manager.getValuesRangeList(1,60));
		manager.registerArgTabComplete(2, null, null, List.of("seconds","minutes","hours","days"));
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args, List.of("<player>", "<time>","<timeformat>","[reason]"));
		
		manager.registerArgUsage(1, null,"<time>","<timeformat>","[reason]");
		manager.registerArgUsage(2, null,null,"<timeformat>","[reason]");
		manager.registerArgUsage(3, null,null,null,"[reason]");
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		this.registerArgAliases(manager);
		
		if(manager.hasArgStart(3) && manager.hasArg(3, List.of("seconds", "minutes", "hours", "days")))
		{
			this.mute(manager, manager.getChosenPlayerFromArg(1, false, true), manager.getIntArg(2), manager.getArg(3), manager.getStringArgStart(4));
		}
		return manager.doReturn();
	}
	
	// --------- /mute <player> <time> <timeformat> [reason]
	public void mute(CommandManager manager, Player destPlayer, Integer time, String timeformat, String reason) {
		if(destPlayer == null || time == null || timeformat == null || time <= 0)
			return;
		
		if(Papi.Model.getPlayer(destPlayer).isMuted())
		{
			manager.setReturnMessage("&c&oGracz &e&o"+destPlayer.getName() + "&c&o ma już aktywne wyciszenie. Pozostało &6&o" + Papi.Model.getPlayer(destPlayer).getMutedRemainingTime());
			return;
		}
		
		LocalDateTime endDatetime = null;
		String duration = "";
		if(timeformat.equalsIgnoreCase("seconds")) {
			if(time < 30) {
				manager.setReturnMessage("&c&oMożesz wyciszyć gracza minimalnie na 30 sekund");
				return;
			}
			if(time > 59) {
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
			if(time > 59) {
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
			if(time > 23) {
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
			if(time == 1)
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
		if(Database.Mutes.addMute(destPlayer, endDatetime, manager.getPlayer(), reason) > 0)
		{
			Papi.Model.getPlayer(destPlayer).setMuted(endDatetime);
			Message.sendBroadcast(Papi.Model.getPlayer(destPlayer).getNameWithPrefix()+" &7został wyciszony na &6"+duration+"&7 przez "+Papi.Model.getPlayer(manager.getPlayer()).getNameWithPrefix()+"&7."+((reason!="") ? ("\nPowód: &6"+reason) : ""));
//			manager.setReturnMessage("&a&oWyciszyłeś gracza &e&o"+destPlayer.getName());
			manager.setReturnMessage(null);
		}
		else
			manager.setReturnMessage("&c&oWystąpił błąd podczas próby wyciszenia gracza &e&o"+destPlayer.getName());
			
	}
}

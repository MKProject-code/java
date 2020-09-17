//package me.maskat.MoneyManager;
//
//import java.util.List;
//
//import org.bukkit.Bukkit;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandExecutor;
//import org.bukkit.command.CommandSender;
//import org.bukkit.command.TabCompleter;
//import org.bukkit.entity.Player;
//
//import mkproject.maskat.Papi.Utils.CommandManager;
//
//public class CmdSkyPoints implements CommandExecutor, TabCompleter {
//	@Override
//	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
//		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args);
//		
//		if(!manager.setArgTabComplete(1, List.of("add", "set", "del", "get")))
//		{
//			if(manager.hasArgTabComplete(1, "add")) {
//				if(manager.hasArgOrSetTabComplete(2, manager.getOnlinePlayersNameList()))
//					manager.setArgTabComplete(3, manager.getValuesRangeList(0, 50000));
//			}
//			else if(manager.hasArgTabComplete(1, "set")) {
//				if(manager.hasArgOrSetTabComplete(2, manager.getOnlinePlayersNameList()))
//					manager.setArgTabComplete(3, manager.getValuesRangeList(0, 50000));
//			}
//			else if(manager.hasArgTabComplete(1, "del")) {
//				if(manager.hasArgOrSetTabComplete(2, manager.getOnlinePlayersNameList()))
//					manager.setArgTabComplete(3, manager.getValuesRangeList(0, 5000));
//			}
//			else if(manager.hasArgTabComplete(1, "get")) {
//				manager.setArgTabComplete(2, manager.getOnlinePlayersNameList());
//			}
//		}
//		
//		return manager.getTabComplete();
//	}
//	@Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args, List.of("create|modify|reload ?"));
//		
//		if(manager.isPlayer())
//			if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
//				return manager.doReturn();
//		
//		manager.registerArgUsage(1, "get", "<nick>");
//		manager.registerArgUsage(1, "add", "<nick>", "<value>");
//		manager.registerArgUsage(1, "set", "<nick>", "<value>");
//		manager.registerArgUsage(1, "del", "<nick>", "<value>");
//		
//		if(manager.hasArgs(2) && manager.hasArgAndPermission(1, "get"))
//			this.get(manager, manager.getChosenPlayerFromArg(2, true));
//		else if(manager.hasArgs(3) && manager.isNumericArg(3) && manager.hasArgAndPermission(1, "add"))
//			this.add(manager, manager.getChosenPlayerFromArg(2, true), manager.getIntArg(3));
//		else if(manager.hasArgs(3) && manager.isNumericArg(3) && manager.hasArgAndPermission(1, "set"))
//			this.set(manager, manager.getChosenPlayerFromArg(2, true), manager.getIntArg(3));
//		else if(manager.hasArgs(3) && manager.isNumericArg(3) && manager.hasArgAndPermission(1, "del"))
//			this.del(manager, manager.getChosenPlayerFromArg(2, true), manager.getIntArg(3));
//		
//		return manager.doReturn();
//	}
//	private void del(CommandManager manager, Player destPlayer, Integer value) {
//		if(destPlayer == null || value == null)
//			return;
//		
//		MapiModel.getPlayer(destPlayer).delPoints(value);
//		manager.setReturnMessage(
//				"&a&oOdebrano graczowi "+value+" SkyPunktów\n"
//				+ "&a&oSkyPunkty gracza "+destPlayer.getName()+": "+(int)MapiModel.getPlayer(destPlayer).getPoints()
//			);
//	}
//	private void set(CommandManager manager, Player destPlayer, Integer value) {
//		if(destPlayer == null || value == null)
//			return;
//
//		int newValue = (int)MapiModel.getPlayer(destPlayer).setPoints(value);
//		manager.setReturnMessage(
//				"&a&oUstawiono graczowi "+value+" SkyPunktów\n"
//				+ "&a&oSkyPunkty gracza "+destPlayer.getName()+": "+newValue
//			);
//	}
//	private void add(CommandManager manager, Player destPlayer, Integer value) {
//		if(destPlayer == null || value == null)
//			return;
//
//		int newValue = (int)MapiModel.getPlayer(destPlayer).addPoints(value);
//		manager.setReturnMessage(
//				"&a&oDodano graczowi "+value+" SkyPunktów\n"
//				+ "&a&oSkyPunkty gracza "+destPlayer.getName()+": "+newValue
//			);
//	}
//	private void get(CommandManager manager, Player destPlayer) {
//		if(destPlayer == null)
//			return;
//
//		manager.setReturnMessage("&a&oSkyPunkty gracza "+destPlayer.getName()+": "+(int)MapiModel.getPlayer(destPlayer).getPoints());
//	}
//}

package me.maskat.TradeManager.Cmds;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.maskat.TradeManager.Plugin;
import me.maskat.TradeManager.PlayerMenu.Models;
import me.maskat.TradeManager.PlayerMenu.TradePlayer;
import mkproject.maskat.Papi.Utils.CommandManager;
import mkproject.maskat.Papi.Utils.Message;

public class CmdMenu implements CommandExecutor, TabCompleter {
	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.PERMISSION_PREFIX, sender, command, label, args);
		
		manager.registerArgTabComplete(0, manager.getOnlinePlayersNameList(false));
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.PERMISSION_PREFIX, sender, command, label, args, List.of("<gracz>"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		if(!manager.hasArgs(1))
		{
			manager.setReturnMessage("&2================= &lHandel &r&2=================\n"
					+"&7Bezpiecznie wymieniaj się przedmiotami z innymi graczami!\n"
					+"&2/trade <gracz>&7 - Wyślij prośbę handlu do gracza");
			return manager.doReturn();
		}
		
		Player destPlayer = manager.getChosenPlayerFromArg(1, false);
		if(destPlayer == null)
			return manager.doReturn();
		
		this.sendTrade(manager, destPlayer);
		return manager.doReturn();
	}
	
	private void sendTrade(CommandManager manager, Player destPlayer) {
		boolean tradePlayerExist = Models.existTradePlayer(manager.getPlayer());
		TradePlayer tradePlayer = Models.getTradePlayer(manager.getPlayer());
		
		if(tradePlayerExist) {
			if(tradePlayer.isTrading()) {
				manager.setReturnMessage("&c&oAktualnie prowadzisz inna transakcję");
				return;
			} else if(tradePlayer.getDestinationPlayer() == destPlayer) {
				manager.setReturnMessage("&c&oWysłałeś juz prośbę handlu temu graczowi");
				return;
			}
		}
		
		boolean tradePlayerDestinationExist = Models.existTradePlayer(destPlayer);
		TradePlayer tradePlayerDestination = Models.getTradePlayer(destPlayer);
		
		if(tradePlayerDestinationExist && tradePlayerDestination.isTrading()) {
			manager.setReturnMessage("&c&oTen gracz aktualnie prowadzi inna transakcję");
			return;
		}
		
		Models.addTradePlayer(manager.getPlayer(), destPlayer);
		tradePlayerExist = true;
		tradePlayer = Models.getTradePlayer(manager.getPlayer());
		
		if(tradePlayerDestinationExist && tradePlayerDestination.getDestinationPlayer() == manager.getPlayer()) {
			tradePlayer.doTrading();
			tradePlayerDestination.doTrading();
			manager.setReturnMessage("&2Zaakceptowałeś prośbę handlu od &e"+tradePlayerDestination.getPlayer().getName()+"&2.\n&2Otwieram menu...");
			Message.sendMessage(tradePlayerDestination.getPlayer(), "&2Gracz &e"+manager.getPlayer().getName()+"&2 zaakceptował twoją prośbę handlu.\n&2Otwieram menu...");
			return;
		}
		
		manager.setReturnMessage("&a&oWysłano prośbę handlu graczowi &e"+destPlayer.getName());
		Message.sendMessage(destPlayer, "&2Gracz &e"+manager.getPlayer().getName()+"&2 chce z tobą handlować!\n&2Wpisz &6/trade "+manager.getPlayer().getName()+"&2 aby rozpocząć handel");
		
		return;
	}
}

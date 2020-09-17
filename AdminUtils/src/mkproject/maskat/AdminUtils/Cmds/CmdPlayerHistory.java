package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import mkproject.maskat.AdminUtils.Database;
import mkproject.maskat.AdminUtils.Plugin;
import mkproject.maskat.AdminUtils.Model.HistoryData;
import mkproject.maskat.LoginManager.UsersAPI;
import mkproject.maskat.Papi.Utils.CommandManager;
import mkproject.maskat.Papi.Utils.Message;

public class CmdPlayerHistory implements CommandExecutor {
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args, List.of("<player>","[page]"));
		
		if(manager.isPlayer())
			if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
				return manager.doReturn();
		
		if(manager.hasArgs(1))// /phistory [player] [page]
			this.printHistory(manager, manager.getArg(1), 1);
		else if(manager.hasArgs(2) && manager.isNumericArg(2))// /phistory [player] [page]
			this.printHistory(manager, manager.getArg(1), manager.getIntArg(2));
		
		return manager.doReturn();
	}
	
	// --------- /phistory [player]
	public void printHistory(CommandManager manager, String destPlayerStr, int page) {
		if(page < 1) {
			manager.setReturnMessage("&c&oWprowadzono nieprawidłowy numer strony");
			return;
		}
		
		int maxPerPage = 7;
		
		List<HistoryData> historyDataList = Database.History.getPlayerAll(destPlayerStr, page, maxPerPage+1);
		
		OfflinePlayer destPlayer = UsersAPI.getOfflinePlayer(destPlayerStr);
		
		if(historyDataList == null)
		{
			if(destPlayer == null) {
				manager.sendMessage("&c&oBrak historii gracza &e&l"+destPlayerStr);
				manager.sendMessage("&c&o[Ten gracz nie został odnaleziony w bazie zarejestrowanych użytkowników]");
			}
			else
				manager.sendMessage("&c&oBrak historii gracza &e&l"+destPlayer.getName());
			manager.setReturnMessage(null);
			return;
		}
		else
		{
			if(destPlayer == null) {
				manager.sendMessage("&5&lHistoria gracza &e&l"+destPlayerStr);
				manager.sendMessage("&c&o[Ten gracz nie został odnaleziony w bazie zarejestrowanych użytkowników]");
			}
			else
				manager.sendMessage("&a&lHistoria gracza &e&l"+destPlayer.getName());
			
			int iMax = historyDataList.size();
			if(iMax > maxPerPage)
				iMax = maxPerPage;
			
			for(int i=0;i<iMax;i++) {
				historyDataList.get(i).sendMessageRaw(manager.getPlayer());
			}
		}
		
		Message.sendRawMessage(manager.getPlayer(),
					"[" + 
						"{" + 
							"\"text\":\" -------- \"," + 
							"\"color\":\"yellow\"" + 
						"}," + 
						"{" + 
							"\"text\":\"[<<<]\"," + 
//							"\"bold\":true," + 
							"\"color\":\""+(page<=1?"yellow":"gold")+"\"" + 
							(page<=1?"":",\"clickEvent\":" + 
							"{" + 
								"\"action\":\"run_command\"," + 
								"\"value\":\"/playerhistory "+destPlayerStr+" "+(page-1)+"\"" + 
							"}," + 
							"\"hoverEvent\":" + 
							"{" + 
								"\"action\":\"show_text\"," + 
								"\"contents\":" + 
								"{" + 
									"\"text\":\"Poprzednia strona\"" + 
								"}" + 
							"}") + 
						"}," + 
						"{" + 
							"\"text\":\" ["+page+"] \"," + 
							"\"color\":\"yellow\"" + 
						"}," + 
						"{" + 
							"\"text\":\"[>>>]\"," + 
//							"\"bold\":true," + 
							"\"color\":\""+(historyDataList != null && historyDataList.size()>maxPerPage?"gold":"yellow")+"\"" + 
							(historyDataList != null && historyDataList.size()>maxPerPage?",\"clickEvent\":" + 
							"{" + 
								"\"action\":\"run_command\"," + 
								"\"value\":\"/playerhistory "+destPlayerStr+" "+(page+1)+"\"" + 
							"}," + 
							"\"hoverEvent\":" + 
							"{" + 
								"\"action\":\"show_text\"," + 
								"\"contents\":" + 
								"{" + 
									"\"text\":\"Następna strona\"" + 
								"}" + 
							"}":"") + 
						"}," + 
						"{" + 
							"\"text\":\" --------\"," + 
							"\"color\":\"yellow\"" + 
						"}" + 
					"]");
		
		manager.setReturnMessage(null);
		
//		historyList.add("&6SkyPoints: &e"+xx);
//		
//		manager.setReturnMessage(destPlayer,
//				"&a&oTwoja historia:\n" + String.join("\n", historyList),
//				"&a&oHistoria gracza &e&o"+destPlayer.getName()+"&a&o:\n" + String.join("\n", historyList));
	}
}

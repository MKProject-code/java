package me.maskat.PromoManager.Cmds;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.maskat.MoneyManager.Mapi;
import me.maskat.MoneyManager.MapiOfflinePlayer;
import me.maskat.MoneyManager.MapiPlayer;
import me.maskat.PromoManager.Database;
import me.maskat.PromoManager.Plugin;
import me.maskat.PromoManager.PromoGift;
import mkproject.maskat.LoginManager.UsersAPI;
import mkproject.maskat.Papi.Utils.CommandManager;
import mkproject.maskat.Papi.Utils.Message;

public class CmdPromo implements CommandExecutor, TabCompleter {

	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager("mkp.promomanager",sender, command, label, args);
		
		manager.registerArgTabComplete(0, List.of());
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager("mkp.promomanager",sender, command, label, args, List.of("<kod promocyjny>"));
		
		if(!manager.isPlayer())
			return manager.doReturn();

		this.doPromo(manager);
		return false;
	}
	
	private void doPromo(CommandManager manager) {
		Player player = manager.getPlayer();
		
		Database.Users.initUser(player);
		
		String promoCodeUsed = Database.Users.getPromoCodeUsed(player);
		String promoCodeAssigned = Database.Users.getPromoCodeAssigned(player);
		
		Timestamp timestampFirstPlayed=new Timestamp(player.getFirstPlayed());
		boolean allowUseTime = timestampFirstPlayed.after(Timestamp.valueOf(LocalDateTime.now().minusHours(1L)));
		
		if(manager.existArg(1)) {
			if(manager.getArg(1).equalsIgnoreCase(promoCodeAssigned))
				Message.sendMessage(player, "&c&oNie możesz użyć własnego kodu promocyjnego!");
			else if(promoCodeUsed != null)
				Message.sendMessage(player, "&c&oMożesz wykorzystać tylko jeden kod promocyjny");
			else if(!allowUseTime)
				Message.sendMessage(player, "&c&oPromocja jest przeznaczona tylko dla nowych graczy");
			else if(Database.Users.existPromoUsedIP(player))
				Message.sendMessage(player, "&c&oPromocja jest przeznaczona tylko dla nowych graczy");
			else if(UsersAPI.getPlayersWithSameIP(player).size() > 0)
				Message.sendMessage(player, "&c&oPromocja jest przeznaczona tylko dla nowych graczy");
			else
			{
				int promoCodeOther = Database.Users.getPromoCodeOther(player, manager.getArg(1));
				if(promoCodeOther > 0)
				{
					Plugin.getPlugin().getLogger().info("Player '"+manager.getPlayer().getName()+"' used promo code: " + manager.getArg(1));
					if(Database.Users.setPromoCodeUsed(player, manager.getArg(1)))
					{
						PromoGift.doGivePromoGift(player);
						Message.sendMessage(player, "&a&oOtrzymałeś nagrodę! Sprawdź swój ekwipunek :)");
						
						OfflinePlayer playerAssignedPromoCode = Database.Users.getPlayerAssignedPromoCode(promoCodeOther);
						if(playerAssignedPromoCode == null)
							return;
						
						MapiOfflinePlayer mapiOfflinePlayer = Mapi.getPlayer(playerAssignedPromoCode);
						
						double POINTS_ADD = 20D;
						
						if(mapiOfflinePlayer == null)
						{
							MapiPlayer mapiPlayer = Mapi.getPlayer(playerAssignedPromoCode.getPlayer());
							if(mapiPlayer != null)
							{
								if(!Database.Users.setPromoCodeUsedPayed(player.getPlayer()))
									return;
								
								mapiPlayer.addPoints(POINTS_ADD);
								Message.sendMessage(mapiPlayer.getPlayer(), "&6&oGracz &e"+player.getName()+"&6&o użył twojego kodu promocyjnego! :)");
							}
						}
						else
						{
							if(!Database.Users.setPromoCodeUsedPayed(player.getPlayer()))
								return;
							
							mapiOfflinePlayer.addPoints(POINTS_ADD);
						}
					}
					else
					{
						Plugin.getPlugin().getLogger().warning("Player '"+manager.getPlayer().getName()+"' used promo code not work!");
						Message.sendMessage(player, "&c&oUpss... Coś poszło nie tak, nie udało się użyć kodu promocyjnego :(");
					}
				}
				else
					Message.sendMessage(player, "&c&oKwalifikujesz się do promocji, ale podałeś błędny kod promocyjny");
			}
		}
		
		Message.sendMessage(player, "&aTwój unikalny kod promocyjny: &b" + promoCodeAssigned.toUpperCase());
		Message.sendMessage(player, "&7&oUżyj go do zapraszania nowych graczy!\n&7&oGdy użyją &6&o/promo "+promoCodeAssigned.toUpperCase()+"&7&o obydowje dostaniecie nagrodę!\n&7&oNagroda dla ciebie zostanie przyznana w postaci SkyPunktów.");
	}
}

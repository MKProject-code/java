package mkproject.maskat.AdminUtils.Cmds;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;


public class CmdPlayerInfo implements CommandExecutor {
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager_local manager = new CommandManager_local(sender, command, label, args, List.of("[player]"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
			return manager.doReturn();
		
		if(manager.hasArgs(0,1))// /info [player]
			this.printInfo(manager, manager.getChosenPlayerFromArg(1, true));
		
		return manager.doReturn();
	}
	
	// --------- /feed [player]StartNewProjectExample_MKProject
	public void printInfo(CommandManager_local manager, Player destPlayer) {
		if(destPlayer == null)
			return;
		
		String info = "&6Location: "+getValue(destPlayer.getLocation())+"\n"
				+ "&6Health: "+getValue(destPlayer.getHealth())+"\n"
				+ "&6Food: "+getValue(destPlayer.getFoodLevel())+"\n"
				+ "&6Custom Name: "+getValue(destPlayer.getCustomName())+"\n"
				+ "&6Display Name: "+getValue(destPlayer.getDisplayName())+"\n"
				+ "&6Entity ID: "+getValue(destPlayer.getEntityId())+"\n"
				+ "&6Experience in Current Level: "+getValue(destPlayer.getExp()*100)+"%\n"
				+ "&6Experience Level: "+getValue(destPlayer.getLevel())+"\n"
				+ "&6Require Experience to Level Up: "+getValue(destPlayer.getExpToLevel())+"\n"
				+ "&6Current Experience Points: "+getValue(Papi.Vault.getEconomy().getBalance(destPlayer))+"\n"
				+ "&6Total Experience Points: "+getValue(destPlayer.getTotalExperience())+"\n"
				+ "&6First Played: "+getValueDate(destPlayer.getFirstPlayed())+"\n"
				+ "&6Last Played: "+getValueDate(destPlayer.getLastPlayed())+"\n"
				+ "&6Walk Speed: "+getValue(destPlayer.getWalkSpeed())+"\n"
				+ "&6Fly Speed: "+getValue(destPlayer.getFlySpeed())+"\n"
				+ "&6Client View Distance: "+getValue(destPlayer.getClientViewDistance())+"\n"
				+ "&6Last Damage: "+getValue(destPlayer.getLastDamage())+"\n"
				+ "&6Last Damage Cause: "+getValue(destPlayer.getLastDamageCause() != null ? destPlayer.getLastDamageCause().getCause() : null)+"\n"
				+ "&6Killer: "+getValue((destPlayer.getKiller() != null ? destPlayer.getKiller().getName() : null))+"\n"
				+ "&6Player Time: "+getValue(getValueDurationTime(destPlayer.getPlayerTime()))+"\n"
				+ "&6Player Weather: "+getValue(destPlayer.getPlayerWeather())+"\n"
				+ "&6Allow Flight: "+getValue(destPlayer.getAllowFlight())+"\n"
				+ "&6Can Pickup Items: "+getValue(destPlayer.getCanPickupItems())+"\n"
				+ "&6GameMode: "+getValue(destPlayer.getGameMode())+"\n"
				+ "&6UniqueId: "+getValue(destPlayer.getUniqueId())+"\n"
				+ "&6Sleeping: "+getValue(destPlayer.isSleeping())+"\n"
				+ "&6Bed Location (if sleeping): "+getValue(destPlayer.isSleeping() ? destPlayer.getBedLocation() : null)+"\n"
				+ "&6Bed Spawn Location: "+getValue(destPlayer.getBedSpawnLocation())+"\n"
				+ "&6Address IP: "+getValuePlayerIP(destPlayer)+"\n"
				+ "&6Logged: "+getValue(Papi.Model.getPlayer(destPlayer).isLogged())+"\n"
				+ "&6Afk: "+getValue(Papi.Model.getPlayer(destPlayer).isAfk())+"\n"
				+ "&6Muted: "+getValue(Papi.Model.getPlayer(destPlayer).isMuted())+"\n"
				+ "&6Muted Remaining Time: "+getValue(Papi.Model.getPlayer(destPlayer).getMutedRemainingTime())+"\n"
				+ "&6Survival Spawn Generated: "+getValue(Papi.Model.getPlayer(destPlayer).isPlayerSpawnGenerated())+"\n"
				+ "&6Survival Spawn Location: "+getValue(Papi.Model.getPlayer(destPlayer).getPlayerSpawnLocation())+"\n"
				+ "&6Next Respawn Location: "+getValue(Papi.Model.getPlayer(destPlayer).getRespawnLocation())+"\n"
				+ "&6Survival Last Location: "+getValue(Papi.Model.getPlayer(destPlayer).getSurvivalLastLocation());
		
		manager.setReturnMessage(destPlayer,
				"&a&oInformacje o tobie:\n" + info,
				"&a&oInformacje o graczu &e&o"+destPlayer.getName()+"&a&o:\n" + info);
	}
	
	private String getValue(Object object) {
		if(object instanceof Location)
		{
			Location loc = (Location) object;
			return "&e"+loc.getWorld().getName()+"&6, X&e"+loc.getBlockX()+"&6, Y&e"+loc.getBlockY()+"&6, Z&e"+loc.getBlockZ();
		}
					
		if(object == null)
			return "&e-";
		return "&e"+object.toString();
	}
	private String getValueDate(Object object) {
		if(object == null)
			return "&e-";
		return "&e"+Papi.Convert.TimestampToDateStringFormat(object.toString(), "dd-MM-yyyy HH:mm:ss");
	}
	private String getValueDurationTime(Object object) {
		if(object == null)
			return "&e-";
		return "&e"+Papi.Convert.TimestempTicksToDurationString((long)object);
	}
	private String getValuePlayerIP(Player destPlayer) {
		String ip = Papi.Model.getPlayer(destPlayer).getAddressIP();
		if(ip.equals("127.0.0.1") || ip.substring(0, 8).equals("192.168.") || ip.equals("89.25.222.88"))
			return "&e-";
		
		return "&e"+ip;
	}
}

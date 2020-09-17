package mkproject.maskat.AdminUtils.Cmds;

import java.util.ArrayList;
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
		
		if(manager.hasArgs(0,1))// /pinfo [player]
			this.printInfo(manager, manager.getChosenPlayerFromArg(1, true));
		
		return manager.doReturn();
	}
	
	// --------- /pinfo [player]
	public void printInfo(CommandManager_local manager, Player destPlayer) {
		if(destPlayer == null)
			return;
		
		List<String> infoList = new ArrayList<>();
		
		try { infoList.add("&6Location: "+getValue(destPlayer.getLocation()));
		} catch(Exception ex) { infoList.add("&6Location: &o*ERROR*"); }
		
		try { infoList.add("&6Health: "+getValue(destPlayer.getHealth()));
		} catch(Exception ex) { infoList.add("&6Health: &o*ERROR*"); }
		
		try { infoList.add("&6Food: "+getValue(destPlayer.getFoodLevel()));
		} catch(Exception ex) { infoList.add("&6Food: &o*ERROR*"); }
		
		try { infoList.add("&6Custom Name: "+getValue(destPlayer.getCustomName()));
		} catch(Exception ex) { infoList.add("&6Custom Name: &o*ERROR*"); }
		
		try { infoList.add("&6Display Name: "+getValue(destPlayer.getDisplayName()));
		} catch(Exception ex) { infoList.add("&6Display Name: &o*ERROR*"); }
		
		try { infoList.add("&6Entity ID: "+getValue(destPlayer.getEntityId()));
		} catch(Exception ex) { infoList.add("&6Entity ID: &o*ERROR*"); }
		
		try { infoList.add("&6Experience in Current Level: "+getValue(destPlayer.getExp()*100));
		} catch(Exception ex) { infoList.add("&6Experience in Current Level: &o*ERROR*"); }
		
		try { infoList.add("&6Experience Level: "+getValue(destPlayer.getLevel()));
		} catch(Exception ex) { infoList.add("&6Experience Level: &o*ERROR*"); }
		
		try { infoList.add("&6Require Experience to Level Up: "+getValue(destPlayer.getExpToLevel()));
		} catch(Exception ex) { infoList.add("&6Require Experience to Level Up: &o*ERROR*"); }
		
		try { infoList.add("&6Current Experience Points: "+getValue(Papi.Vault.getEconomy().getBalance(destPlayer)));
		} catch(Exception ex) { infoList.add("&6Current Experience Points: &o*ERROR*"); }
		
		try { infoList.add("&6Total Experience Points: "+getValue(destPlayer.getTotalExperience()));
		} catch(Exception ex) { infoList.add("&6Total Experience Points: &o*ERROR*"); }
		
		try { infoList.add("&6First Played: "+getValueDate(destPlayer.getFirstPlayed()));
		} catch(Exception ex) { infoList.add("&6First Played: &o*ERROR*"); }
		
		try { infoList.add("&6Last Played: "+getValueDate(destPlayer.getLastPlayed()));
		} catch(Exception ex) { infoList.add("&6Last Played: &o*ERROR*"); }
		
		try { infoList.add("&6Walk Speed: "+getValue(destPlayer.getWalkSpeed()));
		} catch(Exception ex) { infoList.add("&6Walk Speed: &o*ERROR*"); }
		
		try { infoList.add("&6Fly Speed: "+getValue(destPlayer.getFlySpeed()));
		} catch(Exception ex) { infoList.add("&6Fly Speed: &o*ERROR*"); }
		
		try { infoList.add("&6Client View Distance: "+getValue(destPlayer.getClientViewDistance()));
		} catch(Exception ex) { infoList.add("&6Client View Distance: &o*ERROR*"); }
		
		try { infoList.add("&6Last Damage: "+getValue(destPlayer.getLastDamage()));
		} catch(Exception ex) { infoList.add("&6Last Damage: &o*ERROR*"); }
		
		try { infoList.add("&6Last Damage Cause: "+getValue(destPlayer.getLastDamageCause() != null ? destPlayer.getLastDamageCause().getCause() : null));
		} catch(Exception ex) { infoList.add("&6Last Damage Cause: &o*ERROR*"); }
		
		try { infoList.add("&6Killer: "+getValue((destPlayer.getKiller() != null ? destPlayer.getKiller().getName() : null)));
		} catch(Exception ex) { infoList.add("&6Killer: &o*ERROR*"); }
		
		try { infoList.add("&6Player Time: "+getValue(getValueDurationTime(destPlayer.getPlayerTime())));
		} catch(Exception ex) { infoList.add("&6Player Time: &o*ERROR*"); }
		
		try { infoList.add("&6Player Weather: "+getValue(destPlayer.getPlayerWeather()));
		} catch(Exception ex) { infoList.add("&6Player Weather: &o*ERROR*"); }
		
		try { infoList.add("&6Allow Flight: "+getValue(destPlayer.getAllowFlight()));
		} catch(Exception ex) { infoList.add("&6Allow Flight: &o*ERROR*"); }
		
		try { infoList.add("&6Can Pickup Items: "+getValue(destPlayer.getCanPickupItems()));
		} catch(Exception ex) { infoList.add("&6Can Pickup Items: &o*ERROR*"); }
		
		try { infoList.add("&6GameMode: "+getValue(destPlayer.getGameMode()));
		} catch(Exception ex) { infoList.add("&6GameMode: &o*ERROR*"); }
		
		try { infoList.add("&6UniqueId: "+getValue(destPlayer.getUniqueId()));
		} catch(Exception ex) { infoList.add("&6UniqueId: &o*ERROR*"); }
		
		try { infoList.add("&6Sleeping: "+getValue(destPlayer.isSleeping()));
		} catch(Exception ex) { infoList.add("&6Sleeping: &o*ERROR*"); }
		
		try { infoList.add("&6Bed Location (if sleeping): "+getValue(destPlayer.isSleeping() ? destPlayer.getBedLocation() : null));
		} catch(Exception ex) { infoList.add("&6Bed Location (if sleeping): &o*ERROR*"); }
		
		try { infoList.add("&6Bed Spawn Location: "+getValue(destPlayer.getBedSpawnLocation()));
		} catch(Exception ex) { infoList.add("&6Bed Spawn Location: &o*ERROR*"); }
		
		try { infoList.add("&6Address IP: "+getValuePlayerIP(destPlayer));
		} catch(Exception ex) { infoList.add("&6Address IP: &o*ERROR*"); }
		
		try { infoList.add("&6Logged: "+getValue(Papi.Model.getPlayer(destPlayer).isLogged()));
		} catch(Exception ex) { infoList.add("&6Logged: &o*ERROR*"); }
		
		try { infoList.add("&6Afk: "+getValue(Papi.Model.getPlayer(destPlayer).isAfk()));
		} catch(Exception ex) { infoList.add("&6Afk: &o*ERROR*"); }
		
		try { infoList.add("&6Muted: "+getValue(Papi.Model.getPlayer(destPlayer).isMuted()));
		} catch(Exception ex) { infoList.add("&6Muted: &o*ERROR*"); }
		
		try { infoList.add("&6Muted Remaining Time: "+getValue(Papi.Model.getPlayer(destPlayer).getMutedRemainingTime()));
		} catch(Exception ex) { infoList.add("&6Muted Remaining Time: &o*ERROR*"); }
		
		try { infoList.add("&6Survival Spawn Generated: "+getValue(Papi.Model.getPlayer(destPlayer).isPlayerSpawnGenerated()));
		} catch(Exception ex) { infoList.add("&6Survival Spawn Generated: &o*ERROR*"); }
		
		try { infoList.add("&6Survival Spawn Location: "+getValue(Papi.Model.getPlayer(destPlayer).getPlayerSpawnLocation()));
		} catch(Exception ex) { infoList.add("&6Survival Spawn Location: &o*ERROR*"); }
		
		try { infoList.add("&6Next Respawn Location: "+getValue(Papi.Model.getPlayer(destPlayer).getRespawnLocation()));
		} catch(Exception ex) { infoList.add("&6Next Respawn Location: &o*ERROR*"); }
		
		try { infoList.add("&6Survival Last Location: "+getValue(Papi.Model.getPlayer(destPlayer).getSurvivalLastLocation()));
		} catch(Exception ex) { infoList.add("&6Survival Last Location: &o*ERROR*"); }
		
		try { infoList.add("&6SkyPoints: "+getValue(Papi.Model.getPlayer(destPlayer).getPoints()));
		} catch(Exception ex) { infoList.add("&6SkyPoints: &o*ERROR*"); }
		
		try {
			Class.forName("me.maskat.wolfsecurity.api.WolfSecurityApi");
			infoList.add("&6Wolf Location: "+getValue(me.maskat.wolfsecurity.api.WolfSecurityApi.existWolf(destPlayer) ? (me.maskat.wolfsecurity.api.WolfSecurityApi.getWolfEntity(destPlayer) != null ? me.maskat.wolfsecurity.api.WolfSecurityApi.getWolfEntity(destPlayer).getLocation() : null) : null));
		} catch( ClassNotFoundException e ) {
			infoList.add("&6Wolf Location: &o*ERROR*");
		}
		
		manager.setReturnMessage(destPlayer,
				"&a&oInformacje o tobie:\n" + String.join("\n", infoList),
				"&a&oInformacje o graczu &e&o"+destPlayer.getName()+"&a&o:\n" + String.join("\n", infoList));
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

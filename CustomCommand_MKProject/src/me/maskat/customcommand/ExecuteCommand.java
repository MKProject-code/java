package me.maskat.customcommand;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.CommandManager;

public class ExecuteCommand implements CommandExecutor, TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args);
		
		manager.setArgTabComplete(1, List.of("opis", "dolacz", "podglad"));
		
		if(manager.hasArgTabComplete(1, "podglad"))
		{
			List<String> avaliblePlayersWorlds = new ArrayList<>();
			for(World world : Bukkit.getWorlds())
			{
				if(world.getName().indexOf(Config.WorldPrefix) == 0 && world.getName().length() > Config.WorldPrefix.length())
					avaliblePlayersWorlds.add(world.getName().substring(Config.WorldPrefix.length()));
			}
			manager.setArgTabComplete(2, avaliblePlayersWorlds);
		}
		
		return manager.getTabComplete();
	}
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Plugin.getPlugin(), sender, command, label, args, List.of("opis | dolacz | podglad"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowWorld() || !manager.isPermissionAllowGameMode())
			return manager.doReturn();
		
		if(manager.hasArgs(1,2) && manager.hasArg(1, "podglad"))
		{
			if(manager.hasArgs(1))
			{
				manager.setReturnMessage("&bWpisz &l/konkurs podglad <nick>");
				return manager.doReturn();
			}
			
			World world = Bukkit.getWorld(Config.WorldPrefix+manager.getArg(2));
			
			if(world == null) {
				manager.setReturnMessage("&cTen gracz nie został odnaleziony");
				return manager.doReturn();
			}
			
			Papi.Model.getPlayer(manager.getPlayer()).teleportTimer(world.getSpawnLocation(), GameMode.SPECTATOR);
			manager.setReturnMessage(null);
			return manager.doReturn();
		}
		
		if(LocalDateTime.of(Config.EndYear, Config.EndMonth, Config.EndDay, 23, 59).isBefore(LocalDateTime.now()))
		{
			manager.setReturnMessage("&d=============== &d&lKONKURS &d===============\n"
					+ "&b&o&lZbuduj najlepszą mapę i zgarnij nagrodę!\n"
					+ "&bKonkurs trwa do: &l"+String.format("%02d.%02d.%04d",Config.EndDay, Config.EndMonth, Config.EndYear)+" 23:59\n"
					+ "&bTematyka budowanej mapy: &l"+Config.MapInfo+"\n"
					+ "&bNagroda dla najlepszej mapy: &lVIP+ na 7 dni\n"
					+ "&b&o&lKonkurs zakończony. Za niedługo pojawią się wyniki!\n"
					+ "&b&oMożesz zobaczyć prace innych: &b&l/konkurs podglad <nick>");
			return manager.doReturn();
		}
		
		if(manager.hasArgs(1) && manager.hasArg(1, "dolacz"))
		{
			String worldName = Config.WorldPrefix+manager.getPlayer().getName();
			
			World world = Bukkit.getWorld(worldName);
			if(world == null)
			{
				manager.sendMessage("&a&oSuper! Już przygotowujemy mapę dla ciebie...");
				
				Papi.Server.dispatchCommandSilent("worldmanager create "+worldName+" EMPTY SPECTATOR PEACEFUL FALSE");
				
				world = Bukkit.getWorld(worldName);
				if(world == null)
				{
					manager.setReturnMessage("&cWystąpił nieoczekiwany błąd. Skontaktuj się z administracją");
					return manager.doReturn();
				}
				
				world.setSpawnLocation(0, 50, 0);
				
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" BorderRadius 100");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules doFireTick FALSE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules fireDamage FALSE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules disableElytraMovementCheck TRUE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules announceAdvancements FALSE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules drowningDamage FALSE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules doMobSpawning FALSE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules disableRaids TRUE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules doWeatherCycle FALSE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules doDaylightCycle FALSE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules showDeathMessages FALSE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules doTileDrops FALSE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules doInsomnia FALSE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules doMobLoot FALSE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules fallDamage FALSE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules doEntityDrops FALSE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules mobGriefing FALSE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules spawnRadius 0");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules doTraderSpawning FALSE");
				Papi.Server.dispatchCommandSilent("worldmanager modify "+worldName+" GameRules doPatrolSpawning FALSE");
				
				Location loc = world.getSpawnLocation().clone();
				loc.setY(loc.getY()-1);
				loc.getBlock().setType(Material.DIAMOND_BLOCK);
				loc.setZ(loc.getZ()-1);
				loc.getBlock().setType(Material.DIAMOND_BLOCK);
				loc.setX(loc.getX()-1);
				loc.getBlock().setType(Material.DIAMOND_BLOCK);
				loc.setZ(loc.getZ()+1);
				loc.getBlock().setType(Material.DIAMOND_BLOCK);
				Papi.Model.getPlayer(manager.getPlayer()).teleportTimer(world.getSpawnLocation(), GameMode.CREATIVE);
			}
			else
			{
				Papi.Model.getPlayer(manager.getPlayer()).teleportTimer(world.getSpawnLocation(), GameMode.CREATIVE);
			}
			
			manager.setReturnMessage(null);
			return manager.doReturn();
		}
		
		manager.setReturnMessage("&d=============== &d&lKONKURS &d===============\n"
				+ "&b&o&lZbuduj najlepszą mapę i zgarnij nagrodę!\n"
				+ "&bKonkurs trwa do: &l"+String.format("%02d.%02d.%04d",Config.EndDay, Config.EndMonth, Config.EndYear)+" 23:59\n"
				+ "&bTematyka budowanej mapy: &l"+Config.MapInfo+"\n"
				+ "&bNagroda dla najlepszej mapy: &lVIP+ na 7 dni\n"
				+ "&b&oDołącz do konkursu wpisując: &b&l/konkurs dolacz\n"
				+ "&b&oMożesz zobaczyć prace innych: &b&l/konkurs podglad <nick>");
		return manager.doReturn();
	}
}
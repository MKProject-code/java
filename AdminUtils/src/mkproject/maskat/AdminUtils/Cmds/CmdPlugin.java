package mkproject.maskat.AdminUtils.Cmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import mkproject.maskat.AdminUtils.Config;
import mkproject.maskat.AdminUtils.Config.ConfigKey;
import mkproject.maskat.Papi.Utils.CommandManager;

public class CmdPlugin implements CommandExecutor, TabCompleter {

	private void registerArgAliases(CommandManager manager) {
		manager.registerArgAliasAuto(1, "list", 1);
		manager.registerArgAliasAuto(1, "restart", 1);
		manager.registerArgAliasAuto(1, "enable", 1);
		manager.registerArgAliasAuto(1, "disable", 1);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args);
		
		this.registerArgAliases(manager);
		
		List<String> pluginsNamesList = new ArrayList<>();
		for(Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			pluginsNamesList.add(plugin.getName());
		}
		
		manager.registerArgTabComplete(0, List.of("list","restart","enable","disable"));
		manager.registerArgTabComplete(1, List.of("restart","enable","disable"), pluginsNamesList);
		
		return manager.getTabComplete();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager manager = new CommandManager(Config.getString(ConfigKey.PermissionPrefix), sender, command, label, args, List.of("list|restart|enable|disable", "<[plugin]>"));
		
		if(!manager.isPlayer())
			return manager.doReturn();
		
		if(!manager.isPersmissionUse() || !manager.isPermissionAllowGameMode() || !manager.isPermissionAllowWorld())
			return manager.doReturn();
		
		this.registerArgAliases(manager);
		
		manager.registerArgUsage(1, "restart","<plugin>");
		manager.registerArgUsage(1, "enable","<plugin>");
		manager.registerArgUsage(1, "disable","<plugin>");
		
		if(manager.hasArgs(1) && manager.hasArgAndPermission(1, "list"))
		{
			this.list(manager);
			return manager.doReturn();
		}
		else if(manager.hasArgs(2))
		{
			if(manager.hasArgAndPermission(1, "restart") && manager.hasArgs(2))
			{
				this.restart(manager, manager.getArg(2));
				return manager.doReturn();
			}
			else if(manager.hasArgAndPermission(1, "enable") && manager.hasArgs(1,2))
			{
				this.enable(manager, manager.getArg(2));
				return manager.doReturn();
			}
			else if(manager.hasArgAndPermission(1, "disable") && manager.hasArgs(1,2))
			{
				this.disable(manager, manager.getArg(2));
				return manager.doReturn();
			}
		}
		return manager.doReturn();
	}
	private void list(CommandManager manager) {
		List<String> pluginsNamesList = new ArrayList<>();
		for(Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			boolean enabled = Bukkit.getPluginManager().isPluginEnabled(plugin);
			pluginsNamesList.add((enabled?"&a":"&c")+plugin.getName());
		}
		manager.setReturnMessage("&a&oLista pluginów: \n"+String.join("&7, ", pluginsNamesList));
	}
	private void restart(CommandManager manager, String pluginName) {
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if(plugin==null)
		{
			manager.setReturnMessage("&c&oPlugin &b&o"+pluginName+"&c&o nie został odnaleziony");
		}
		else if(!Bukkit.getPluginManager().isPluginEnabled(plugin))
		{
			manager.setReturnMessage("&c&oPlugin &b&o"+pluginName+"&c&o jest wyłączony");
		}
		else
		{
			Bukkit.getPluginManager().disablePlugin(plugin);
			Bukkit.getPluginManager().enablePlugin(plugin);
			manager.setReturnMessage("&a&oPlugin &b&o"+plugin.getName()+"&a&o został zrestartowany");
		}
	}
	private void enable(CommandManager manager, String pluginName) {
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if(plugin==null)
		{
			manager.setReturnMessage("&c&oPlugin &b&o"+pluginName+"&c&o nie został odnaleziony");
		}
		else if(Bukkit.getPluginManager().isPluginEnabled(plugin))
		{
			manager.setReturnMessage("&c&oPlugin &b&o"+pluginName+"&c&o jest już włączony");
		}
		else
		{
			Bukkit.getPluginManager().enablePlugin(plugin);
			manager.setReturnMessage("&a&oPlugin &b&o"+plugin.getName()+"&a&o został włączony");
		}
	}
	private void disable(CommandManager manager, String pluginName) {
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if(plugin==null)
		{
			manager.setReturnMessage("&c&oPlugin &b&o"+pluginName+"&c&o nie został odnaleziony");
		}
		else if(!Bukkit.getPluginManager().isPluginEnabled(plugin))
		{
			manager.setReturnMessage("&c&oPlugin &b&o"+pluginName+"&c&o jest już wyłączony");
		}
		else
		{
			Bukkit.getPluginManager().disablePlugin(plugin);
			manager.setReturnMessage("&a&oPlugin &b&o"+plugin.getName()+"&a&o został wyłączony");
		}
	}
}

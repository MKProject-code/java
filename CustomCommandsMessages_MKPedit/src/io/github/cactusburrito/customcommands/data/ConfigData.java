package io.github.cactusburrito.customcommands.data;

import io.github.cactusburrito.customcommands.interfaces.Disposable;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Extracts and stores the data from the config file, for easier access.
 */
public class ConfigData implements Disposable
{
	/**
	 * Contains all the defined custom commands associated with their defined data from the config file.
	 */
	private HashMap<String, CustomCommandData> _CommandMap;

	/**
	 * Create a new instance of the {@link ConfigData}.
	 * @param config
	 */
	public ConfigData(FileConfiguration config)
	{
		_CommandMap = new HashMap<String, CustomCommandData>();

		FillCommandMap(config);
		UpdateBukkitCommandMap();
	}

	/**
	 * Fill the command map with the custom defined commands found in the config.
	 * @param config The config used in this plugin instance.
	 */
	private void FillCommandMap(FileConfiguration config)
	{
		Set<String> customCommands = config.getConfigurationSection("CustomCommands").getKeys(false);

		for(String path : customCommands)
		{
			String realCommand = config.getConfigurationSection("CustomCommands").getString(path + ".command");
			String message = config.getConfigurationSection("CustomCommands").getString(path + ".message");
			String prefix = config.getConfigurationSection("CustomCommands").getString(path + ".messageprefix");
			List<String> aliases = config.getConfigurationSection("CustomCommands").getStringList(path + ".aliases");

			CustomCommandData data;

			if(realCommand == null && message == null)
			{
				data = null;
			}
			else
			{
				data = new CustomCommandData(message, realCommand, prefix);
			}

			for(String alias : aliases)
				_CommandMap.put(alias.toLowerCase(), data);
			_CommandMap.put(path.toLowerCase(), data);
		}
	}

	/**
	 * Use reflection to manually add commands to the bukkit command map, to allow the use of tab complete without adding to the plugin.yml file.
	 */
	private void UpdateBukkitCommandMap()
	{
		try
		{
			final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);
			CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

			for(String command : _CommandMap.keySet())
			{
				if(commandMap.getCommand(command) != null) continue;

				commandMap.register(command, new CommandEntry(command));
			}

		} catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception)
		{
			exception.printStackTrace();
		}
	}

	/**
	 * Attempt to get a custom command from the defined list of commands in the config.
	 * @param command The command name to check for.
	 * @return The {@link CustomCommandData} for the associated custom command.
	 */
	public CustomCommandData GetCustomCommand(String command)
	{
		return _CommandMap.get(command);
	}


	/**
	 * {@inheritDoc}
	 */
	public void Dispose()
	{
		_CommandMap.clear();
		_CommandMap = null;
	}
}

/**
 * Command to manually insert into the bukkit command map.
 */
class CommandEntry extends Command
{

	protected CommandEntry(String name)
	{
		super(name);
	}

	protected CommandEntry(String name, String description, String usageMessage, List<String> aliases)
	{
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender commandSender, String command, String[] args)
	{
		return true;
	}
}

package io.github.cactusburrito.customcommands;

import io.github.cactusburrito.customcommands.utils.DebugUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigurationInitialiser
{
	/**
	 * Initialise and get the main file configuration for this plugin.
	 * @return {@link FileConfiguration} file.
	 */
	protected static FileConfiguration GetConfig(CustomCommandsMain main)
	{
		FileConfiguration config;

		//Create a directory for the plugin, if one doesnt exist.
		main.getDataFolder().mkdir();

		config = main.getConfig();
		config.options().copyDefaults(true);
		config.addDefault("Config Version", 0);

		try
		{
			//Test config version against version in the plugin info class.
			if(main.getConfig().getInt("Config Version") < main.CONFIG_VERSION)
			{
				//Find the folder that must contain the config file.
				File destination = new File(main.getDataFolder() + File.separator + "config.yml");

				if(destination.exists())
				{
					//Save the old config for the user reference.
					File renameTo = new File(destination.getParent() + File.separator + "old_config.yml");

					if(renameTo.exists())
					{
						renameTo.delete();
					}

					destination.renameTo(renameTo);

					DebugUtils.Print("Replaced old config with new, please check files!");
				}

				main.saveDefaultConfig();

				DebugUtils.Print("Config successfully exported.");
			}

		} catch(Exception ex)
		{
			ex.printStackTrace();
		}

		main.reloadConfig();
		config = main.getConfig();

		return config;
	}
}

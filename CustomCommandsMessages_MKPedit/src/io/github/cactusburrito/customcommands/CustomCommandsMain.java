package io.github.cactusburrito.customcommands;

import io.github.cactusburrito.customcommands.commands.PluginCommandExecutor;
import io.github.cactusburrito.customcommands.data.ConfigData;
import io.github.cactusburrito.customcommands.handlers.CustomCommandHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class.
 */
public class CustomCommandsMain extends JavaPlugin
{

	/**
	 * Plugin prefix/name.
	 */
	public final String PLUGIN_PREFIX = "CustomCommands";

	/**
	 * Version of the plugin, use the version provided in the plugin.yml.
	 */
	public final String PLUGIN_VERSION = getDescription().getVersion();

	/**
	 * Config version, used to decide if the config needs deleting and copying from defaults.
	 */
	public final int CONFIG_VERSION = 3; //Only change once released to the public with any changes to the config file.

	/**
	 * Instance of this plugin class.
	 */
	private static CustomCommandsMain _Instance;

	/**
	 * The config file used by current instance of the plugin.
	 */
	private FileConfiguration _ConfigFile;

	/**
	 * Data extracted from the config file.
	 */
	private ConfigData _ConfigData;

	/**
	 * Command handler for the custom set commands within the config file.
	 */
	private CustomCommandHandler _CustomCommandHandler;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEnable()
	{
		super.onEnable();

		if(_Instance != null) return;

		InitPlugin();
		RegisterCommandExecutors();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDisable()
	{
		super.onDisable();
	}

	/**
	 * Initialise the plugin and its components.
	 */
	public void InitPlugin()
	{
		_Instance = this;

		_ConfigFile = ConfigurationInitialiser.GetConfig(this);

		_ConfigData = new ConfigData(_ConfigFile);

		_CustomCommandHandler = new CustomCommandHandler(this, _ConfigData);
	}

	/**
	 * Register the command executors of the plugin.
	 */
	public void RegisterCommandExecutors()
	{
		getCommand("customcommands").setExecutor(new PluginCommandExecutor());
	}

	/**
	 * Reload the plugin and the config files.
	 */
	public void Reload()
	{
		_ConfigFile = null;

		_CustomCommandHandler.Dispose();
		_CustomCommandHandler = null;

		_ConfigData.Dispose();
		_ConfigData = null;

		InitPlugin();
	}

	/**
	 * Get the config file for the current instance of the plugin.
	 * @return {@link FileConfiguration} file.
	 */
	public FileConfiguration GetConfig()
	{
		return _ConfigFile;
	}

	/**
	 * Get the data from the config.
	 * @return {@link ConfigData} which contains the extracted data from the config file.
	 */
	public ConfigData GetConfigData()
	{
		return _ConfigData;
	}

	/**
	 * Get the instance of the main class of {@link CustomCommandsMain}.
	 * @return Class instance.
	 */
	public static CustomCommandsMain GetInstance()
	{
		return _Instance;
	}
}

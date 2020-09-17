package io.github.cactusburrito.customcommands.handlers;

import io.github.cactusburrito.customcommands.data.ConfigData;
import io.github.cactusburrito.customcommands.CustomCommandsMain;
import io.github.cactusburrito.customcommands.data.CustomCommandData;
import io.github.cactusburrito.customcommands.interfaces.Disposable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static org.bukkit.Bukkit.getServer;

/**
 * Listens to the events and handles appropriately.
 */
public class CustomCommandHandler implements Listener, Disposable
{
	/**
	 * Reference to the config data instance for easier access.
	 */
	private ConfigData _ConfigData;

	/**
	 * Reference to the main class for easier access.
	 */
	private CustomCommandsMain _Main;

	/**
	 * Create a new instance of the {@link CustomCommandHandler}.
	 * @param main {@link CustomCommandsMain} class instance.
	 * @param configData {@link ConfigData} class instance.
	 */
	public CustomCommandHandler(CustomCommandsMain main, ConfigData configData)
	{
		getServer().getPluginManager().registerEvents(this, main);

		_Main = main;

		_ConfigData = configData;
	}


	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void OnPlayerCommandPreProcess(PlayerCommandPreprocessEvent event)
	{
		String command = event.getMessage().toLowerCase().substring(1);

		CustomCommandData commandData = _ConfigData != null ? _ConfigData.GetCustomCommand(command) : null;

		if(commandData == null) return;

		if(commandData.Message != null && !commandData.Message.equals(""))
		{
			event.getPlayer().sendMessage((commandData.Prefix != null ? commandData.Prefix + " " : "") + commandData.Message);
		}

		if(commandData.Command != null && !commandData.Command.equals(""))
		{
			event.setMessage(commandData.Command);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void Dispose()
	{
		HandlerList.unregisterAll();

		_ConfigData = null;
		_Main = null;
	}
}

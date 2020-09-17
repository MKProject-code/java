package io.github.cactusburrito.customcommands.data;

import org.bukkit.ChatColor;

/**
 * Data class for the custom command defined in the config.yml.
 */
public class CustomCommandData
{
	public String Message;
	public String Command;
	public String Prefix;

	/**
	 * Create a new instance of the {@link CustomCommandData}.
	 * @param message The message to print out on custom command execution.
	 * @param command The real command to execute.
	 * @param prefix The prefix to apply before the message print out.
	 */
	protected CustomCommandData(String message, String command, String prefix)
	{
		Message = message == null ? null : ChatColor.translateAlternateColorCodes('&', message);
		Command = command == null ? null : "/" + command.toLowerCase();
		Prefix = prefix == null ? null : ChatColor.translateAlternateColorCodes('&', prefix);
	}
}

package io.github.cactusburrito.customcommands.utils;

import io.github.cactusburrito.customcommands.CustomCommandsMain;

public class DebugUtils
{

	public static void Print(String message)
	{
		System.out.println("[" + CustomCommandsMain.GetInstance().PLUGIN_PREFIX + "_" + CustomCommandsMain.GetInstance().PLUGIN_VERSION + "]: " + message);
	}

}

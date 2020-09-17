package mkproject.maskat.CmdBlockAdvancedManager.CmdsExecute;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class CmdsExecuteManager {
	public static void registerCommandsExecute(JavaPlugin plugin) {
		ChildExecuteIfCount.register(plugin);
		ChildExecuteIfRandomChance.register(plugin);
		ChildExecuteIfPermission.register(plugin);
		CommandBar.register(plugin);
		CommandFill.register(plugin);
		CommandHotBarItem.register(plugin);
		CommandBar.register(plugin);
	}
}

package me.maskat.cauldroninfinite;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new Event(), this);
		getLogger().info("[CauldronInfinite] Has been enabled!");
	}
}

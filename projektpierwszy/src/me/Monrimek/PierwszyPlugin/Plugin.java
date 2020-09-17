package me.Monrimek.PierwszyPlugin;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	
	public void onEnable()
	{
		this.getServer().getPluginManager().registerEvents(new Event(), this);
		
		//asdsadas
	}
	
	public void onDisable()
	{
		
		
	}
	
}

package me.maskat.customcommand;

import org.bukkit.configuration.file.FileConfiguration;

abstract public class Config {
    protected static String WorldPrefix = "k2skywars_";
	protected static String MapInfo = "SkyWars";
    protected static int EndYear = 2020;
    protected static int EndMonth = 9;
    protected static int EndDay = 6;
	
	protected static void init(FileConfiguration config) {
		WorldPrefix = config.getString("WorldPrefix");
		MapInfo = config.getString("MapInfo");
		EndYear = config.getInt("EndYear");
		EndMonth = config.getInt("EndMonth");
		EndDay = config.getInt("EndDay");
	}
}

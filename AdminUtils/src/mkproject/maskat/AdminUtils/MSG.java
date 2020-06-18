package mkproject.maskat.AdminUtils;

public class MSG {
	public static final boolean ACCESS_DENIED_PERMISSION_SHOW_IN_CONSOLE = true;
	public static final boolean ACCESS_DENIED_PERMISSION_SHOW_FOR_SENDER = false;
	
	public static final String COMMAND_USE_ONLY_PLAYER = "&cKomende moze uzyc tylko gracz!";
	public static final String PLAYER_NOTFOUND = "&c&oNie znaleziono takiego gracza";
	public static final String ACCESS_DENIED = "&c&oBrak uprawnień (%permission%)";
	public static final String ACCESS_DENIED_SLIM = "&c&oBrak uprawnień";
	public static final String USAGE_PREFIX = "&6Użyj: &e";
//	public static final String ALLOW_ONLY_NUMBERS = "&cDozwolone tylko cyfry!";
	public static final String WRONG_SYNTAX = "&cNiepoprawna składnia komendy!";
	public static final String CHOOSE_PLAYER_NOT_FOUND = "&cTaki gracz nie został odnaleziony";
	public static final String CHOOSE_WORLD_NOT_FOUND = "&cTaki świat nie został odnaleziony";
	public static final String CHOSEN_YOURSELF = "&cNie możesz wybrać siebie!";
	public static final String CHOSEN_YOURSELF_WORLD = "&cNie możesz wybrać świata, w którym jesteś!";
	public static final String PLAYER_TELEPORT_ERROR = "&cTeleportacja nieudana";
	
	public static String getDenyMessage(String permission) {
		if(ACCESS_DENIED_PERMISSION_SHOW_IN_CONSOLE)
			Plugin.getPlugin().getLogger().info("Player use AdminUtils command - access denied! Permission missing: " + permission);
		if(ACCESS_DENIED_PERMISSION_SHOW_FOR_SENDER)
			return ACCESS_DENIED.replace("%permission%", permission);
		else
			return ACCESS_DENIED_SLIM;
	}
	public static String getUsageMessage(String usage) {
		return USAGE_PREFIX+usage;
	}
}

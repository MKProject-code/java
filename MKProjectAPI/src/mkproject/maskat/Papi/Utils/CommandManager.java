package mkproject.maskat.Papi.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.PapiPlugin;

public class CommandManager {
	public static void initCommand(JavaPlugin plugin, String command, Object listener, boolean tabCompleter) {
		PluginCommand pluginCommand = plugin.getCommand(command);
		if(pluginCommand == null)
		{
			plugin.getLogger().warning("********** Command '"+command+"' not registered in plugin.yml! **********");
			plugin.getLogger().warning("********** Command '"+command+"' not registered in plugin.yml! **********");
			plugin.getLogger().warning("********** Command '"+command+"' not registered in plugin.yml! **********");
			return;
		}
		
		pluginCommand.setExecutor((CommandExecutor)listener);
		if(tabCompleter)
			pluginCommand.setTabCompleter((TabCompleter)listener);
	}
	
	private static final boolean ACCESS_DENIED_PERMISSION_SHOW_IN_CONSOLE = true;
	private static final boolean ACCESS_DENIED_PERMISSION_SHOW_FOR_SENDER = false;
	
	private static final String COMMAND_USE_ONLY_PLAYER = "&cKomende moze uzyc tylko gracz!";
	private static final String PLAYER_NOTFOUND = "&c&oNie znaleziono takiego gracza";
	private static final String ACCESS_DENIED = "&c&oBrak uprawnień (%permission%)";
	private static final String ACCESS_DENIED_SLIM = "&c&oBrak uprawnień";
	private static final String USAGE_PREFIX = "&6Użyj: &e";
//		private static final String ALLOW_ONLY_NUMBERS = "&cDozwolone tylko cyfry!";
	private static final String WRONG_SYNTAX = "&cNiepoprawna składnia komendy!";
	private static final String CHOOSE_PLAYER_NOT_FOUND = "&cTaki gracz nie został odnaleziony";
	private static final String CHOOSE_WORLD_NOT_FOUND = "&cTaki świat nie został odnaleziony";
	private static final String CHOSEN_YOURSELF = "&cNie możesz wybrać siebie!";
	private static final String CHOSEN_YOURSELF_WORLD = "&cNie możesz wybrać świata, w którym jesteś!";
	private static final String PLAYER_TELEPORT_ERROR = "&cTeleportacja nieudana";
	
	private String getDenyMessage(String permission) {
		if(ACCESS_DENIED_PERMISSION_SHOW_IN_CONSOLE)
			PapiPlugin.getPlugin().getLogger().info("Player '"+player+"' use command - access denied! Permission missing: " + permission);
		if(ACCESS_DENIED_PERMISSION_SHOW_FOR_SENDER)
			return ACCESS_DENIED.replace("%permission%", permission);
		else
			return ACCESS_DENIED_SLIM;
	}
//	private String getUsageMessage(String usage) {
//		return USAGE_PREFIX+usage;
//	}
	private String getUsageMessage() {
		return USAGE_PREFIX+getFullUsage();
	}
	
	private ConsoleCommandSender console;
	private Player player;
	private Command command;
	private String label;
	private String[] args;
	private String[] argsDefault;
	private Map<Integer, String> defaultUsageList = new HashMap<>();
	private String returnMessage;
	private String lastCheckedPermission;
	
	private String PERMISSION_PREFIX = "mkp.commandmanager.";
	
	public CommandManager(String PERMISSION_PREFIX, CommandSender sender, Command command, String label, String[] args, List<String> defaultUsageList) {
		this.PERMISSION_PREFIX = PERMISSION_PREFIX+".";
		this.lastCheckedPermission = this.PERMISSION_PREFIX+command.getName();
		
		if(sender instanceof ConsoleCommandSender)
			this.console = (ConsoleCommandSender)sender;
		else
			this.console = null;
		
		if(sender instanceof Player)
			this.player = (Player)sender;
		else
			this.player = null;
		
		this.command = command;
		this.label = label;
		this.args = args;
		this.argsDefault = args.clone();
		for(int i=0; i<defaultUsageList.size(); i++) {
			this.defaultUsageList.put(i+1, defaultUsageList.get(i));
		}
		this.returnMessage = getUsageMessage();
	}
	
	public CommandManager(String PERMISSION_PREFIX, CommandSender sender, Command command, String label, String[] args) {
		this.PERMISSION_PREFIX = PERMISSION_PREFIX+".";
		this.lastCheckedPermission = this.PERMISSION_PREFIX+command.getName();
		
		if(sender instanceof ConsoleCommandSender)
			this.console = (ConsoleCommandSender)sender;
		else
			this.console = null;
		
		if(sender instanceof Player)
			this.player = (Player)sender;
		else
			this.player = null;
		
		this.command = command;
		this.label = label;
		this.args = args;
		this.argsDefault = args.clone();
	}

	//private Map<Integer, List<String[]>> usagesMap = new HashMap<>();
	
	public boolean registerArgUsage(int argInt, String... argsUsages) {
		if(args.length >= argInt)
		{
			boolean next = true;
			for(int i=0; i<argInt; i++) {
				if(argsUsages[i] != null && !argsUsages[i].equalsIgnoreCase(args[i]))
				{
					next = false;
				}
			}
			if(!next)
			{
				return false;
			}
			
			for(int i=0; i<argsUsages.length; i++) {
//				if(i < argInt && args[i-1].equalsIgnoreCase(argsUsages[i-1]))
				if(argsUsages[i]!=null) {
					defaultUsageList.put(i+1, argsUsages[i]);
				}
				else {
					defaultUsageList.put(i+1, args[i]);
				}
//				else
//					defaultUsageList.put(i, argsUsages[i-1]);
			}
			setReturnMessage(getUsageMessage());
			return true;
		}
		return false;
	}
//	public void registerArgUsage(int argInt, String... argsUsages) {
//		if(!usagesMap.containsKey(argInt))
//			usagesMap.put(argInt, new ArrayList<String[]>());
//		
//		for(int i=0;i<argsUsages.length;i++)
//		{
//			if(argsAliasesMap.containsKey(i+1))
//				argsUsages[i] = argsAliasesMap.get(i+1);
//		}
//		
//		usagesMap.get(argInt).add(argsUsages);
//	}
	
	//private Map<Integer, String> argsAliasesMap = new HashMap<>();

	public void registerArgAlias(int argInt, String base, String alias) {
		if(args.length < argInt)
			return;
		
		if(args[argInt-1].equalsIgnoreCase(alias))
		{
			//argsAliasesMap.put(argInt-1, alias);
			args[argInt-1] = base;
		}
	}
	
	public void registerArgAliasAuto(int argInt, String base, int indexBegin) {
		if(args.length < argInt)
			return;
		
		if(args[argInt-1].length() >= indexBegin && args[argInt-1].substring(0, indexBegin).equalsIgnoreCase(base.substring(0, indexBegin)))
		{
			//argsAliasesMap.put(argInt-1, args[argInt-1]);
			args[argInt-1] = base;
		}
	}
	
	public String getFullUsage() {
		return "/"+label+" "+String.join(" ", defaultUsageList.values());
	}
	
//	public String getFullUsage() {
//		if(args.length == 0)
//			return "/"+label+" "+defaultUsage;
//		
//		List<String[]> usagesList;
//		
//		if(args[args.length-1].equals("?"))
//		{
//			if(!usagesMap.containsKey(args.length-1))
//				return "/"+label+" "+defaultUsage;
//			
//			usagesList = usagesMap.get(args.length-1);
//		}
//		else
//		{
//			if(!usagesMap.containsKey(args.length))
//				return "/"+label+" "+defaultUsage;
//			
//			usagesList = usagesMap.get(args.length);
//		}
//		
//		String[] usageArr = null;
//		for(String[] itemList : usagesList) {
//			boolean result = true;
//			
//			for(int i=0;i<itemList.length;i++) {
//				if(args.length < itemList.length)
//					break;
//				
//				if(itemList[i] != null && !args[i].equals("?") && !args[i].equalsIgnoreCase(itemList[i]))
//				{
//					result = false;
//				}
//			}
//			
//			if(result)
//			{
//				usageArr = itemList;
//				break;
//			}
//		}
//		
//		if(usageArr == null)
//			return "/"+label+" "+defaultUsage;
//		
//		String usageOutput = " ";
//		
//		for(int i=0;i<usageArr.length;i++) {
//			if(args.length > i && usageArr[i] == null)
//				usageOutput = usageOutput+args[i]+" ";
//			else
//			{
//				usageOutput = usageOutput+usageArr[i]+" ";
//			}
//		}
//		
//		return "/"+label+usageOutput.substring(0, usageOutput.length()-1);
//	}
	
//	public String getFullUsage() {
//		String usageOutput = " ";
//		for(int i=0;i<usage.size();i++) {
//			if(args.length >= (i+1))
//				usageOutput = usageOutput+args[i]+" ";
//			else
//				usageOutput = usageOutput+usage.get(i)+" ";
//		}
//		
//		return "/"+label+usageOutput.substring(0, usageOutput.length()-1);
//	}
	
	public boolean isPersmissionUse() {
		if(player.hasPermission(PERMISSION_PREFIX+command.getName()+".use"))
			return true;
		
		setReturnMessage(getDenyMessage(PERMISSION_PREFIX+command.getName()+".use"));
		return false;
	}
	
//	private boolean isChosenYourselfInArg(int i) {
//		if(!existArg(i))
//			return false;
//		Player otherPlayer = this.getOtherPlayer(this.getArg(i));
//		if(otherPlayer != null && otherPlayer.equals(player))
//		{
//			setReturnMessage(MSG.CHOSEN_YOURSELF);
//			return true;
//		}
//		return false;
//	}

	public boolean hasArgs(int... lens) {
		for(int len : lens) {
			if(args.length == len)
			{
				if(args.length > 0 && args[args.length-1].equals("?"))
				{
					setReturnMessage(getUsageMessage());
					return false;
				}
				else
				{
					setReturnMessage(getUsageMessage());
					return true;
				}
			}
		}
		setReturnMessage(getUsageMessage());
		return false;
	}

	public boolean hasArgAndPermission(int intArg, String argument) {
		return hasArgAndPermission(intArg, argument, argument);
	}
	public boolean hasArgAndPermission(int intArg, String argument, String customArgForPermission) {
		if(args[intArg-1].equalsIgnoreCase(argument))
		{
			lastCheckedPermission = PERMISSION_PREFIX+command.getName()+"."+customArgForPermission;
			if(!this.isPlayer() || player.hasPermission(PERMISSION_PREFIX+command.getName()+"."+customArgForPermission+".use"))
				return true;
			else
				setReturnMessage(getDenyMessage(PERMISSION_PREFIX+command.getName()+"."+customArgForPermission+".use"));
		}
		return false;
	}
	
	public boolean hasArg(int intArg, String argument) {
		if(args[intArg-1].equalsIgnoreCase(argument))
			return true;
		
		return false;
	}
	public boolean hasArg(int intArg, List<String> argumentsList) {
		for(String argument : argumentsList) {
			if(hasArg(intArg, argument))
				return true;
		}
		return false;
	}
	
	public void setReturnMessage(String message) {
		returnMessage = message;
	}
	public void setReturnMessage(Player otherPlayer, String messageWhenMe, String messageWhenOther) {
		returnMessage = this.getMessageWhoIsPlayer(otherPlayer, messageWhenMe, messageWhenOther);
	}
	public String getMessageWhoIsPlayer(Player otherPlayer, String messageWhenMe, String messageWhenOther) {
		if(player.equals(otherPlayer))
			return messageWhenMe;
		else
			return messageWhenOther;
	}
	public void setReturnMessage(World otherWorld, String messageWhenMe, String messageWhenOther) {
		returnMessage = this.getMessageWhoIsWorld(otherWorld, messageWhenMe, messageWhenOther);
	}
	public String getMessageWhoIsWorld(World otherWorld, String messageWhenMe, String messageWhenOther) {
		if(player.getWorld().equals(otherWorld))
			return messageWhenMe;
		else
			return messageWhenOther;
	}
	
	public boolean doReturn() {
		if(returnMessage == null)
			return false;
		if(this.isPlayer())
			Message.sendMessage(player, returnMessage);
		else if(this.isConsole())
			Message.sendConsole(returnMessage);
		
		return false;
	}

	public String getArg(int i) {
		if(args.length < i || args[i-1].length() <= 0)
			return null;
		return args[i-1];
	}
	public Integer getIntArg(int i) {
		if(args.length < i || args[i-1].length() <= 0 || !isNumericArg(i))
			return null;
		return Integer.parseInt(args[i-1]);
	}
	public boolean existArg(int i) {
		return args.length >= i;
	}

	public boolean isNumericArgs() {
		for(String arg : args) {
			if(!Papi.Function.isNumeric(arg))
			{
				//setReturnMessage(MSG.ALLOW_ONLY_NUMBERS);
				setReturnMessage(WRONG_SYNTAX);
				return false;
			}
		}
		return true;
	}
	

//	public boolean isNumericArg(int argInt) {
//		if(!Papi.Function.isNumeric(args[argInt-1]))
//		{
//			//setReturnMessage(MSG.ALLOW_ONLY_NUMBERS);
//			setReturnMessage(MSG.WRONG_SYNTAX);
//			return false;
//		}
//		return true;
//	}
	
	public boolean isNumericArg(int... argInt) {
		for(int item : argInt) {
			if(!Papi.Function.isNumeric(args[item-1]))
			{
				//setReturnMessage(MSG.ALLOW_ONLY_NUMBERS);
				setReturnMessage(WRONG_SYNTAX);
				return false;
			}
		}
		return true;
		
	}
	

	public boolean isPlayer() {
		if(player == null)
		{
			setReturnMessage(COMMAND_USE_ONLY_PLAYER);
			return false;
		}
		return true;
	}
	
	public boolean isConsole() {
		if(player == null)
		{
			setReturnMessage(COMMAND_USE_ONLY_PLAYER);
			return false;
		}
		return true;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Player getChosenPlayerFromArg(int argInt, boolean canChoseSelf) {
		return getChosenPlayerFromArg(argInt, canChoseSelf, false);
	}
	public Player getChosenPlayerFromArg(int argInt, boolean canChoseSelf, boolean checkImmunitet) {
		if(this.getArg(argInt) == null)
			return player;
		
		Player otherPlayer = Bukkit.getPlayer(this.getArg(argInt));
		
		if(otherPlayer != null)
		{
			Player resultPlayer = getChosenPlayer(otherPlayer, canChoseSelf, checkImmunitet);
			if(resultPlayer != null)
				return resultPlayer;
		}
		else
			setReturnMessage(CHOOSE_PLAYER_NOT_FOUND);
		
		return null;
	}
	private Player getChosenPlayer(Player otherPlayer, boolean canChoseSelf, boolean checkImmunitet) {
		if(otherPlayer.equals(player))
		{
			if(canChoseSelf)
				return player;
			else
				setReturnMessage(CHOSEN_YOURSELF);
			return null;
		}
		else if(this.isPlayer() && !player.hasPermission(lastCheckedPermission+".otherplayer"))
		{
			setReturnMessage(getDenyMessage(lastCheckedPermission+".otherplayer"));
			return null;
		}
		else if(this.isPlayer() && checkImmunitet && otherPlayer.hasPermission(lastCheckedPermission+".immunitet") && !player.hasPermission(lastCheckedPermission+".bypass.immunitet"))
		{
			setReturnMessage(getDenyMessage(lastCheckedPermission+".immunitet"));
			return null;
		}
		
		if(otherPlayer.isOnline())
			return otherPlayer;
		
		setReturnMessage(CHOOSE_PLAYER_NOT_FOUND);
		return null;
	}
	
	public World getChosenWorldFromArg(int argInt, boolean canChoseSelfWorld) {
		if(this.getArg(argInt) == null)
			return player.getWorld();
		
		World otherWorld = Bukkit.getWorld(this.getArg(argInt));
		
		if(otherWorld != null)
		{
			if(otherWorld.equals(player.getWorld()))
			{
				if(canChoseSelfWorld)
					return player.getWorld();
				else
					setReturnMessage(CHOSEN_YOURSELF_WORLD);
				return null;
			}
			
			if(!otherWorld.equals(player.getWorld()) && !player.hasPermission(lastCheckedPermission+".otherworld"))
			{
				setReturnMessage(getDenyMessage(lastCheckedPermission+".otherworld"));
				return null;
			}
			
			return otherWorld;
		}
		
		setReturnMessage(CHOOSE_WORLD_NOT_FOUND);
		return null;
	}

	public boolean hasArgStart(int argIntStart) {
		if(args.length >= argIntStart)
		{
			if(args[args.length-1].equals("?"))
			{
				setReturnMessage(getUsageMessage());
				return false;
			}
			else
				return true;
		}
		setReturnMessage(getUsageMessage());
		return false;
	}

	public String getStringArgStart(int argIntStart) {
		if(args.length<=0 || args.length < argIntStart)
			return null;
		
		String output = "";
		int i=1;
		for(String arg : args)
		{
			if(i>=argIntStart)
				output = output+arg+" ";
			i++;
		}
		
		return output.substring(0,output.length()-1);
	}

	public void sendMessage(String message) {
		Message.sendMessage(player, message);
	}

	public boolean isPermissionAllowGameMode() {
		if(player.hasPermission(lastCheckedPermission+".allowgamemode."+player.getGameMode().name()))
			return true;
		
		setReturnMessage(getDenyMessage(lastCheckedPermission+".allowgamemode."+player.getGameMode().name()));
		return false;
	}
	
	public boolean isPermissionAllowWorld() {
		if(player.hasPermission(lastCheckedPermission+".allowworld."+player.getWorld().getName()))
			return true;
		
		setReturnMessage(getDenyMessage(lastCheckedPermission+".allowworld."+player.getWorld().getName()));
		return false;
	}

	//private Map<Integer,List<String>> completeListAll = new HashMap<>();
	
	//////////////////////// TAB COMPLETE
	private List<String> completeTab = new ArrayList<>();
	
	public void registerArgTabComplete(int argInt, @SuppressWarnings("unchecked") List<String>... completeList) {
		if(args.length != (argInt+1))
			return;
		
		if(args.length == 1)
		{
			completeTab = getListWhenContainsFirstString(argsDefault[0], completeList[0]);
			return;
		}
		
		for(int i=0; i<argInt;i++) {
			if(completeList[i] != null && !completeList[i].contains(args[i]))
				return;
		}
		completeTab = getListWhenContainsFirstString(argsDefault[argInt], completeList[argInt]);
	}
	
	public List<String> getTabComplete() {
		return completeTab;
	}
	
	private List<String> getListWhenContainsFirstString(String firstChars, List<String> list) {
		List<String> resultlist = new ArrayList<>();
		for(String item : list)
		{
			if(item.length() > firstChars.length() && item.substring(0, firstChars.length()).equalsIgnoreCase(firstChars))
				resultlist.add(item);
		}
		return resultlist;
	}

	public List<String> getOnlinePlayersNameList() {
		return getOnlinePlayersNameList(true);
	}
	public List<String> getOnlinePlayersNameList(boolean withSelf) {
		List<String> onlinePlayersNameList = new ArrayList<>();
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(withSelf || player != this.player)
				onlinePlayersNameList.add(player.getName());
		}
		return onlinePlayersNameList;
	}
	public List<String> getOnlinePlayersCanChooseNameList(boolean canChoseSelf, boolean checkImmunitet) {
		List<String> onlinePlayersNameList = new ArrayList<>();
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(this.getChosenPlayer(player, canChoseSelf, checkImmunitet) != null)
				onlinePlayersNameList.add(player.getName());
		}
		return onlinePlayersNameList;
	}
	
	public List<String> getWorldsNameList() {
		List<String> worldNameList = new ArrayList<>();
		for(World world : Bukkit.getWorlds()) {
			worldNameList.add(world.getName());
		}
		return worldNameList;
	}

	public List<String> getValuesRangeList(int iBegin, int iEnd) {
		List<String> valuesRangeList = new ArrayList<>();
		for(int i=iBegin;i<=iEnd;i++)
		{
			valuesRangeList.add(String.valueOf(i));
		}
		return valuesRangeList;
	}

	public boolean playerTeleport(Player destPlayer, Location location) {
		if(destPlayer.teleport(location))
			return true;
		
		setReturnMessage(PLAYER_TELEPORT_ERROR);
		return false;
	}

	public int getUsePrice(int priceWithoutBypass) {
		if(player.hasPermission(PERMISSION_PREFIX+command.getName()+".bypass.payment"))
			return 0;
		return priceWithoutBypass;
	}
	
//	private String getPermissinCommandUse() {
//		return "mkp.adminutils."
//	}
}

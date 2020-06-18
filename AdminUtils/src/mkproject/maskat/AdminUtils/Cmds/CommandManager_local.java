package mkproject.maskat.AdminUtils.Cmds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mkproject.maskat.AdminUtils.MSG;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class CommandManager_local {
	private CommandSender sender;
	private Player player;
	private Command command;
	private String label;
	private String[] args;
	private String[] argsDefault;
	private Map<Integer, String> defaultUsageList = new HashMap<>();
	private String returnMessage;
	private String lastCheckedPermission;
	
	private String PERMISSION_PREFIX = "mkp.adminutils.";
	
	public CommandManager_local(CommandSender sender, Command command, String label, String[] args, List<String> defaultUsageList) {
		this.sender = sender;
		
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
		this.returnMessage = MSG.getUsageMessage(getFullUsage());
	}
	
	public CommandManager_local(CommandSender sender, Command command, String label, String[] args) {
		this.sender = sender;
		
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
	
	public void registerArgUsage(int argInt, String... argsUsages) {
		if(args.length >= argInt)
		{
			boolean next = true;
			for(int i=0; i<argInt; i++) {
				if(argsUsages[i] != null && !argsUsages[i].equalsIgnoreCase(args[i]))
					next = false;
			}
			if(!next)
				return;
			
			for(int i=0; i<argsUsages.length; i++) {
//				if(i < argInt && args[i-1].equalsIgnoreCase(argsUsages[i-1]))
				if(argsUsages[i]!=null)
					defaultUsageList.put(i+1, argsUsages[i]);
				else
					defaultUsageList.put(i+1, args[i]);
//				else
//					defaultUsageList.put(i, argsUsages[i-1]);
			}
		}
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
		lastCheckedPermission = PERMISSION_PREFIX+command.getName();
		if(player.hasPermission(PERMISSION_PREFIX+command.getName()+".use"))
			return true;
		
		setReturnMessage(MSG.getDenyMessage(PERMISSION_PREFIX+command.getName()+".use"));
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
					setReturnMessage(MSG.getUsageMessage(getFullUsage()));
					return false;
				}
				else
					return true;
			}
		}
		setReturnMessage(MSG.getUsageMessage(getFullUsage()));
		return false;
	}

	public boolean hasArgAndPermission(int i, String argument) {
		if(args[i-1].equalsIgnoreCase(argument))
		{
			lastCheckedPermission = PERMISSION_PREFIX+command.getName()+"."+argument;
			if(player.hasPermission(PERMISSION_PREFIX+command.getName()+"."+argument+".use"))
				return true;
			else
				setReturnMessage(MSG.getDenyMessage(PERMISSION_PREFIX+command.getName()+"."+argument+".use"));
		}
		return false;
	}
	
	public boolean hasArg(int i, String argument) {
		if(args[i-1].equalsIgnoreCase(argument))
			return true;
		
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
		Message.sendMessage(player, returnMessage);
		return false;
	}

	public String getArg(int i) {
		if(args.length < i)
			return null;
		return args[i-1];
	}
	public boolean existArg(int i) {
		return args.length >= i;
	}

	public boolean isNumericArgs() {
		for(String arg : args) {
			if(!Papi.Function.isNumeric(arg))
			{
				//setReturnMessage(MSG.ALLOW_ONLY_NUMBERS);
				setReturnMessage(MSG.WRONG_SYNTAX);
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
				setReturnMessage(MSG.WRONG_SYNTAX);
				return false;
			}
		}
		return true;
		
	}
	

	public boolean isPlayer() {
		if(player == null)
		{
			setReturnMessage(MSG.COMMAND_USE_ONLY_PLAYER);
			return false;
		}
		return true;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Player getChosenPlayerFromArg(int argInt, boolean canChoseSelf) {
		if(this.getArg(argInt) == null)
			return player;
		
		Player otherPlayer = Bukkit.getPlayer(this.getArg(argInt));
		
		if(otherPlayer != null)
		{
			if(otherPlayer.equals(player))
			{
				if(canChoseSelf)
					return player;
				else
					setReturnMessage(MSG.CHOSEN_YOURSELF);
				return null;
			}
			
			if(!otherPlayer.equals(player) && !player.hasPermission(lastCheckedPermission+".otherplayer"))
			{
				setReturnMessage(MSG.getDenyMessage(lastCheckedPermission+".otherplayer"));
				return null;
			}
			
			if(otherPlayer.isOnline())
				return otherPlayer;
		}
		
		setReturnMessage(MSG.CHOOSE_PLAYER_NOT_FOUND);
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
					setReturnMessage(MSG.CHOSEN_YOURSELF_WORLD);
				return null;
			}
			
			if(!otherWorld.equals(player.getWorld()) && !player.hasPermission(lastCheckedPermission+".otherworld"))
			{
				setReturnMessage(MSG.getDenyMessage(lastCheckedPermission+".otherworld"));
				return null;
			}
			
			return otherWorld;
		}
		
		setReturnMessage(MSG.CHOOSE_WORLD_NOT_FOUND);
		return null;
	}

	public boolean hasArgStart(int argIntStart) {
		if(args.length >= argIntStart)
		{
			if(args[args.length-1].equals("?"))
			{
				setReturnMessage(MSG.getUsageMessage(getFullUsage()));
				return false;
			}
			else
				return true;
		}
		setReturnMessage(MSG.getUsageMessage(getFullUsage()));
		return false;
	}

	public String getStringArgStart(int argIntStart) {
		if(args.length<=0 || args.length < argIntStart)
			return null;
		
		String output = "";
		for(String arg : args)
		{
			output = output+arg+" ";
		}
		
		return output.substring(0,output.length()-1);
	}

	public void sendMessage(String message) {
		Message.sendMessage(player, message);
	}

	public boolean isPermissionAllowGameMode() {
		if(player.hasPermission(lastCheckedPermission+".allowgamemode."+player.getGameMode().name()))
			return true;
		
		setReturnMessage(MSG.getDenyMessage(lastCheckedPermission+".allowgamemode."+player.getGameMode().name()));
		return false;
	}
	
	public boolean isPermissionAllowWorld() {
		if(player.hasPermission(lastCheckedPermission+".allowworld."+player.getWorld().getName()))
			return true;
		
		setReturnMessage(MSG.getDenyMessage(lastCheckedPermission+".allowworld."+player.getWorld().getName()));
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
		List<String> onlinePlayersNameList = new ArrayList<>();
		for(Player player : Bukkit.getOnlinePlayers()) {
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
		
		setReturnMessage(MSG.PLAYER_TELEPORT_ERROR);
		return false;
	}

	
//	private String getPermissinCommandUse() {
//		return "mkp.adminutils."
//	}
}

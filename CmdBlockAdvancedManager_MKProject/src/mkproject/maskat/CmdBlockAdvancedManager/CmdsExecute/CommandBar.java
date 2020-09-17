package mkproject.maskat.CmdBlockAdvancedManager.CmdsExecute;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.FloatArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import mkproject.maskat.Papi.Utils.Message;

public abstract class CommandBar {
	
	private static Map<Player,Timer> targetsTimerExpMap = new HashMap<>();
	private static Map<Player,BossBar> targetsBossBarMap = new HashMap<>();
	
	@SuppressWarnings({ })
	protected static void register(JavaPlugin plugin) {
		commandExp(plugin);
		commandBoss(plugin);
	}
	
	@SuppressWarnings({ "unchecked" })
	private static void commandExp(JavaPlugin plugin) {
		
	    LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	    arguments.put("type", new LiteralArgument("exp"));
	    
	    //bar exp clear <target>
	    arguments.put("option", new LiteralArgument("clear"));
	    arguments.put("target", new EntitySelectorArgument(EntitySelector.MANY_PLAYERS));
	    
	    new CommandAPICommand("bar")
        .withArguments(arguments)
        .executes((sender, args) -> {
	    	Collection<Player> targets = (Collection<Player>) args[0];
	    	for(Player targetPlayer : targets) {
	    		Timer timer = targetsTimerExpMap.remove(targetPlayer);
	    		if(timer != null) {
	    			timer.cancel();
	    			targetPlayer.setExp(0F);
	    		}
	    	}
        })
        .register();
	    
	    //bar exp timer <target> <time>
	    arguments.put("option", new MultiLiteralArgument("timer"));
	    arguments.put("target", new EntitySelectorArgument(EntitySelector.MANY_PLAYERS));
	    arguments.put("time", new FloatArgument());
	    
	    new CommandAPICommand("bar")
	        .withArguments(arguments)
	        .withPermission(CommandPermission.OP)
	        .executes((sender, args) -> {
	        	String option = (String) args[0];
		    	Collection<Player> targets = (Collection<Player>) args[1];
		    	float time = (float) args[2];
		    	
		    	if(option.equals("timer")) {
					for(Player targetPlayer : targets) {
						Timer t = targetsTimerExpMap.get(targetPlayer);
						if(t != null)
							t.cancel();
						
						t = new Timer();
						targetsTimerExpMap.put(targetPlayer, t);
						
						targetPlayer.setLevel(0);
						targetPlayer.setExp(1.0F);
						scheduleTaskExp(t, targetPlayer, time);
					}
		    	}
	        })
	        .register();
	}
	
	private static void scheduleTaskExp(Timer timer, Player player, float seconds) {
	    TimerTask task = new TimerTask() {
	        public void run() {
	        	if(player.isOnline()) {
	        		float newExp = player.getExp()-(0.1F/seconds);
	        		if(newExp <= 0)
	        			player.setExp(0);
	        		else
	        		{
	        			player.setExp(newExp);
	        			scheduleTaskExp(timer, player, seconds);
	        		}
	        	}
	        	else
	        		targetsTimerExpMap.remove(player);
	        }
	    };
	    long delay = 100L;
	    timer.schedule(task, delay);
	}
	

	@SuppressWarnings({ "unchecked" })
	private static void commandBoss(JavaPlugin plugin) {
		
	    LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	    arguments.put("type", new LiteralArgument("boss"));
	    
	    //bar boss clear <target>
	    arguments.put("option", new LiteralArgument("clear"));
	    arguments.put("target", new EntitySelectorArgument(EntitySelector.MANY_PLAYERS));
	    
	    new CommandAPICommand("bar")
        .withArguments(arguments)
        .withPermission(CommandPermission.OP)
        .executes((sender, args) -> {
	    	Collection<Player> targets = (Collection<Player>) args[0];
	    	for(Player targetPlayer : targets) {
	    		BossBar bossBar = targetsBossBarMap.remove(targetPlayer);
	    		if(bossBar != null) {
	    			bossBar.removePlayer(targetPlayer);
	    		}
	    	}
        })
        .register();
	    
	    //bar boss timer <target> <time> <barColor> <barStyle>
	    arguments.put("option", new LiteralArgument("timer"));
	    arguments.put("target", new EntitySelectorArgument(EntitySelector.MANY_PLAYERS));
	    arguments.put("time", new FloatArgument());
	    arguments.put("barColor", CustomArgumentManager.BossBarColor());
	    arguments.put("barStyle", CustomArgumentManager.BossBarStyle());
	    
	    new CommandAPICommand("bar")
	        .withArguments(arguments)
	        .withPermission(CommandPermission.OP)
	        .executes((sender, args) -> {
		    	Collection<Player> targets = (Collection<Player>) args[0];
		    	float time = (float) args[1];
		    	BarColor barColor = (BarColor) args[2];
		    	BarStyle barStyle = (BarStyle) args[3];
		    	
		    	showBossBarTimer(null, targets, time, barColor, barStyle);
	        })
	        .register();
	    
	    //bar boss timer <target> <time> <barColor> <barStyle> [<title>]
	    arguments.put("title", new GreedyStringArgument());
	    
	    new CommandAPICommand("bar")
	    .withArguments(arguments)
        .withPermission(CommandPermission.OP)
	    .executes((sender, args) -> {
	    	Collection<Player> targets = (Collection<Player>) args[0];
	    	float time = (float) args[1];
	    	BarColor barColor = (BarColor) args[2];
	    	BarStyle barStyle = (BarStyle) args[3];
	    	String title = (String) args[4];
	    	
	    	showBossBarTimer(Message.getColorMessage(title), targets, time, barColor, barStyle);
	    })
	    .register();
	}
	
	private static void showBossBarTimer(String title, Collection<Player> targets, float time, BarColor barColor, BarStyle barStyle) {
		BossBar bossBar = Bukkit.getServer().createBossBar(title, barColor, barStyle);
		bossBar.setProgress(1D);
		for(Player targetPlayer : targets) {
			BossBar lastBB = targetsBossBarMap.get(targetPlayer);
			if(lastBB != null)
				lastBB.removePlayer(targetPlayer);
			
			bossBar.addPlayer(targetPlayer);
			targetsBossBarMap.put(targetPlayer, bossBar);
		}
		
		Timer t = new Timer();
		
		scheduleTaskBoss(t, bossBar, time);
	}
	
	private static void scheduleTaskBoss(Timer timer, BossBar bossBar, float seconds) {
		TimerTask task = new TimerTask() {
			public void run() {
				if(bossBar.getPlayers().size() <= 0)
				{
					bossBar.removeAll();
				}
				else
				{
					double newExp = bossBar.getProgress()-(0.1D/seconds);
					if(newExp <= 0)
					{
						bossBar.setProgress(0);
						for(Player player : bossBar.getPlayers())
							targetsBossBarMap.remove(player);
						bossBar.removeAll();
					}
					else
					{
						bossBar.setProgress(newExp);
						scheduleTaskBoss(timer, bossBar, seconds);
					}
				}
			}
		};
		long delay = 100L;
		timer.schedule(task, delay);
	}
}

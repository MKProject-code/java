package me.maskat.MonsterEvent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.maskat.ArenaManager.ArenaAPI.ApiArena;
import me.maskat.ArenaManager.ArenaAPI.ApiTeam;
import me.maskat.MonsterEvent.enums.Team;
import mkproject.maskat.Papi.Papi;

public class Task_off {
	
	private static List<EntityType> allowEntityTypes = List.of(
			EntityType.BLAZE,
			EntityType.CREEPER,
			EntityType.PHANTOM,
			EntityType.PILLAGER,
			EntityType.ZOMBIE,
			EntityType.ZOMBIE_VILLAGER,
			EntityType.SKELETON,
			EntityType.SPIDER
			);
	
	protected static long checkTimeRepeatingSecondsDefault = 60*120;//2h
	private static boolean eventStarted = false;
	
	protected static void spawnMobsTask(ApiArena apiArena, int secoundDuration) {
		Plugin.getPlugin().getLogger().info(">>>>>>>>>>>>>>>>> spawnMobsTask(ApiArena apiArena, int secoundDuration="+secoundDuration+")");
		Bukkit.getScheduler().runTaskLaterAsynchronously(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if(apiArena.getWorld().getPlayers().size() > 0 && apiArena.getWorld().getEntities().size() < 50)
				{
					Plugin.getPlugin().getLogger().info("### SPAWN MOB:");
					EntityType entityType = allowEntityTypes.get(Papi.Function.randomInteger(0, allowEntityTypes.size()));
					Plugin.getPlugin().getLogger().info("### Random entity type: " + entityType);
					ApiTeam monstersTeam = apiArena.getTeam(Team.MONSTERS.name());
					Location loc = monstersTeam.getSpawnsLocation().get(Papi.Function.randomInteger(0, monstersTeam.getSpawnsLocation().size()));
					Plugin.getPlugin().getLogger().info("### Location: " + loc);
					Entity entity = loc.getWorld().spawnEntity(loc, entityType);
					Plugin.getPlugin().getLogger().info("### Type: " + entity.getType());
					if(entity instanceof Creature)
					{
						Plugin.getPlugin().getLogger().info("### Mob is not Creature :)");
						Player pNear = Papi.Function.getNearestPlayer(loc);
						((Creature) entity).setTarget(pNear);
						Plugin.getPlugin().getLogger().info("### Target near: " + pNear.getName());
					}
					else
						Plugin.getPlugin().getLogger().info("### Mob is not Creature !!!!!!!!!");
					entity.remove();
				}
				if(secoundDuration>0)
					spawnMobsTask(apiArena, secoundDuration-1);
				else
					eventStarted = false;
			}
		}, 20L);
	}
	
	protected static void checkMobsEventStart(ApiArena apiArena, long checkTimeRepeatingSeconds) {
		Plugin.getPlugin().getLogger().info(">>>>>>>>>>>>>>>>> checkMobsEventStart(ApiArena apiArena, long checkTimeRepeatingSeconds="+checkTimeRepeatingSeconds+")");
		Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if(!eventStarted && apiArena != null)
				{
					Plugin.getPlugin().getLogger().info("*************** MonsterEvent - eventStarted = false ***************");
					Date date = new Date();   // given date
					Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
					calendar.setTime(date);   // assigns calendar to given date 
					int hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
					int minute = calendar.get(Calendar.MINUTE);

					Plugin.getPlugin().getLogger().info("*************** MonsterEvent - hour = "+hour+" & minute = "+minute+" ***************");
					
					if(hour < 21)
					{
						if(hour > 17)
						{
							Plugin.getPlugin().getLogger().info("*************** MonsterEvent - checkMobsEventStart(30min) ***************");
							checkMobsEventStart(apiArena, 60*30);
							return;
						}
						else if(hour > 19 && minute > 20)
						{
							Plugin.getPlugin().getLogger().info("*************** MonsterEvent - checkMobsEventStart(10min) ***************");
							checkMobsEventStart(apiArena, 60*10);
							return;
						}
						else if(hour > 19 && minute > 45)
						{
							Plugin.getPlugin().getLogger().info("*************** MonsterEvent - checkMobsEventStart(3min) ***************");
							checkMobsEventStart(apiArena, 60*3);
							return;
						}
						else if(hour > 19 && minute > 56)
						{
							Plugin.getPlugin().getLogger().info("*************** MonsterEvent - checkMobsEventStart(60s) ***************");
							checkMobsEventStart(apiArena, 60);
							return;
						}
						else if(hour > 19 && minute > 59)
						{
							Plugin.getPlugin().getLogger().info("*************** MonsterEvent - checkMobsEventStart(10s) ***************");
							checkMobsEventStart(apiArena, 10);
							return;
						}
						else if(hour == 20)
						{
							eventStarted = true;
							Plugin.getPlugin().getLogger().info("*************** MonsterEvent - Start! ***************");
//							for(Player player : apiArena.getWorld().getPlayers()) {
//								ArenaAPI.sendTitle(player, "&cCzas zagłady!", "&eWrogie moby atakują");
//								ArenaAPI.playSound(player, Sound.ENTITY_ZOMBIE_INFECT);
//							}
							
							Task_off.spawnMobsTask(apiArena, 10*60);
							checkMobsEventStart(apiArena, checkTimeRepeatingSecondsDefault);
							return;
						}
					}
				}
				else
					Plugin.getPlugin().getLogger().info("*************** MonsterEvent - eventStarted = true ***************");
				Plugin.getPlugin().getLogger().info("*************** MonsterEvent - checkMobsEventStart("+checkTimeRepeatingSeconds+"s) ***************");
				checkMobsEventStart(apiArena, checkTimeRepeatingSeconds);
			}
		}, 20*checkTimeRepeatingSeconds);
	}
}

package me.maskat.MonsterEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import me.maskat.ArenaManager.ArenaAPI.ApiArena;
import me.maskat.ArenaManager.ArenaAPI.ApiTeam;
import me.maskat.ArenaManager.ArenaAPI.ArenaAPI;
import me.maskat.MonsterEvent.enums.Team;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Scheduler.PapiScheduler;
import mkproject.maskat.Papi.Utils.Message;

public class SchedulerTask implements PapiScheduler {
	
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
	
	protected static boolean eventStarted = false;
	private static ApiArena apiArena;
	protected static Collection<Entity> entitiesArenaAlive = new ArrayList<>();
	
	@Override
	public void runTaskThread() {
		if(eventStarted)
			return;
		eventStarted = true;
		SchedulerTask.apiArena = ArenaAPI.getModelArena("MonsterEvent");
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player.getWorld() == Papi.Server.getServerSpawnWorld())
				Message.sendTitle(player, null, "&c&lCzas zagłady! &aWalcz z wrogimi mobami");
			else
				Message.sendTitle(player, null, "&c&lCzas zagłady! &aDołącz do eventu na &a&l/spawn");
		}
		
		spawnMobsTask(60*1);
	}

	protected static void spawnMobsTask(int secoundDuration) {
		Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				
				Bukkit.broadcastMessage("players on spawn="+apiArena.getWorld().getPlayers().size()+" && entities="+apiArena.getWorld().getEntities().size());
				
				if(apiArena.getWorld().getPlayers().size() > 0)
				{
					Plugin.getPlugin().getLogger().info("### SPAWN MOB:");
					EntityType entityType = allowEntityTypes.get(Papi.Function.randomInteger(0, allowEntityTypes.size()-1));
					Plugin.getPlugin().getLogger().info("### Random entity type: " + entityType);
					ApiTeam monstersTeam = apiArena.getTeam(Team.MONSTERS.name());
					Location loc = monstersTeam.getSpawnsLocation().get(Papi.Function.randomInteger(0, monstersTeam.getSpawnsLocation().size()-1));
					Plugin.getPlugin().getLogger().info("### Location: " + loc);
					Entity entity = loc.getWorld().spawnEntity(loc, entityType);
					entitiesArenaAlive.add(entity);
					Plugin.getPlugin().getLogger().info("### Type: " + entity.getType());
					if(entity instanceof Mob)
					{
						Plugin.getPlugin().getLogger().info("### Mob is Mob :)");
						Player pNear = Papi.Function.getNearestPlayer(loc);
						((Mob) entity).setTarget(pNear);
						Plugin.getPlugin().getLogger().info("### Target near: " + pNear.getName());
					}
					else
						Plugin.getPlugin().getLogger().info("### Mob is not Mob !!!!!!!!!");
				}
				if(secoundDuration>0)
					spawnMobsTask(secoundDuration-1);
				else
				{
					apiArena.getWorld().getEntities().clear();
					eventStarted = false;
				}
			}
		}, 20L);
	}

}

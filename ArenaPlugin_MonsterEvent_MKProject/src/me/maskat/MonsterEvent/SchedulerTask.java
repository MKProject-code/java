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
			EntityType.CREEPER,
			EntityType.PILLAGER,
			EntityType.ZOMBIE,
			EntityType.ZOMBIE,
			EntityType.ZOMBIE_VILLAGER,
			EntityType.ZOMBIE_VILLAGER,
			EntityType.SKELETON,
			EntityType.SPIDER,
			
			EntityType.CREEPER,
			EntityType.PILLAGER,
			EntityType.ZOMBIE,
			EntityType.ZOMBIE,
			EntityType.ZOMBIE_VILLAGER,
			EntityType.ZOMBIE_VILLAGER,
			EntityType.SKELETON,
			EntityType.SPIDER,
			
			EntityType.CREEPER,
			EntityType.PILLAGER,
			EntityType.ZOMBIE,
			EntityType.ZOMBIE,
			EntityType.ZOMBIE_VILLAGER,
			EntityType.ZOMBIE_VILLAGER,
			EntityType.SKELETON,
			EntityType.SPIDER,
			
			EntityType.ZOMBIE,
			EntityType.ZOMBIE,
			EntityType.ZOMBIE_VILLAGER,
			EntityType.ZOMBIE_VILLAGER,
			EntityType.SKELETON,
			EntityType.SPIDER,
			
			EntityType.PHANTOM
			);
	
//	private static Map<Material, Integer> allowMusicMap = new HashMap<>();
//	protected static void allowMusicMapInitialize() {
//		allowMusicMap.put(Sound.MUSIC_DISC_11,71);
//		allowMusicMap.put(Sound.MUSIC_DISC_13,178);
//		allowMusicMap.put(Sound.MUSIC_DISC_BLOCKS,305);
//		allowMusicMap.put(Sound.MUSIC_DISC_CAT,185);
//		allowMusicMap.put(Sound.MUSIC_DISC_CHIRP,185);
//		allowMusicMap.put(Sound.MUSIC_DISC_FAR,174);
//		allowMusicMap.put(Sound.MUSIC_DISC_MALL,197);
//		allowMusicMap.put(Sound.MUSIC_DISC_MELLOHI,96);
//		allowMusicMap.put(Sound.MUSIC_DISC_STAL,150);
//		allowMusicMap.put(Sound.MUSIC_DISC_STRAD,188);
//		allowMusicMap.put(Sound.MUSIC_DISC_WAIT,238);
//		allowMusicMap.put(Sound.MUSIC_DISC_WARD,251);
//	}
//	protected static void allowMusicMapInitialize() {
//		allowMusicMap.put(Material.MUSIC_DISC_11,71);
//		allowMusicMap.put(Material.MUSIC_DISC_13,178);
//		allowMusicMap.put(Material.MUSIC_DISC_BLOCKS,305);
//		allowMusicMap.put(Material.MUSIC_DISC_CAT,185);
//		allowMusicMap.put(Material.MUSIC_DISC_CHIRP,185);
//		allowMusicMap.put(Material.MUSIC_DISC_FAR,174);
//		allowMusicMap.put(Material.MUSIC_DISC_MALL,197);
//		allowMusicMap.put(Material.MUSIC_DISC_MELLOHI,96);
//		allowMusicMap.put(Material.MUSIC_DISC_STAL,150);
//		allowMusicMap.put(Material.MUSIC_DISC_STRAD,188);
//		allowMusicMap.put(Material.MUSIC_DISC_WAIT,238);
//		allowMusicMap.put(Material.MUSIC_DISC_WARD,251);
//	}
	
	protected static boolean eventStarted = false;
	protected static boolean eventEnded = false;
	private static ApiArena apiArena;
	protected static Collection<Entity> entitiesArenaAlive = new ArrayList<>();
	
	private static int secoundDurationDefault = 60*15;
	//private static int soundPlayedTime = 0;
	
	@Override
	public void runTaskThread() {
		if(eventStarted)
			return;
		entitiesArenaAlive = new ArrayList<>();
		eventStarted = true;
		eventEnded = false;
		apiArena = ArenaAPI.getModelArena("MonsterEvent");
		
//		Sound currentPlayed = new ArrayList<>(allowMusicMap.keySet()).get(Papi.Function.randomInteger(0, allowMusicMap.size()-1));
//		soundPlayedTime = allowMusicMap.get(currentPlayed);
		
//		Papi.Server.getServerSpawnWorld().playSound(Papi.Server.getServerSpawnLocation(), currentPlayed, 1F, 1F);
		
//        Jukebox juke;
//        juke.setPlaying(materialDisk);
		
		Plugin.getPlugin().getLogger().info("---> Starting arena event: MonsterEvent");
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player.getWorld() == Papi.Server.getServerSpawnWorld())
				Message.sendTitle(player, null, "&c&lCzas zagłady! &aWalcz z wrogimi mobami");
			else {
				Message.sendTitle(player, null, "&c&lCzas zagłady! &aDołącz do eventu na &a&l/spawn");
			}
		}
		
		Message.sendBroadcast("&7[&a&lEVENT&7] &c&lCzas zagłady! &aDołącz do eventu na &a&l/spawn");
		
		int secoundDuration = secoundDurationDefault;
		spawnMobsTask(secoundDuration);
	}

	protected static void spawnMobsTask(int secoundDuration) {
		Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				
				if(secoundDuration<=0)
				{
					Plugin.getPlugin().getLogger().info("---> Arena event 'MonsterEvent' mobs alive: "+entitiesArenaAlive.size());
					for(Entity entityAlive : entitiesArenaAlive) {
						if(entityAlive.isValid())
							entityAlive.remove();
					}
					entitiesArenaAlive = new ArrayList<>();
					eventStarted = false;
					for(Player player : Bukkit.getOnlinePlayers()) {
						if(player.getWorld() == Papi.Server.getServerSpawnWorld())
							Message.sendTitle(player, null, "&a&lKoniec zagłady!");
					}
					Plugin.getPlugin().getLogger().info("---> Finish arena event: MonsterEvent");
					return;
				}
			
				if(apiArena.getWorld().getPlayers().size() > 0)
				{
					EntityType entityType = allowEntityTypes.get(Papi.Function.randomInteger(0, allowEntityTypes.size()-1));
					ApiTeam monstersTeam = apiArena.getTeam(Team.MONSTERS.name());
					Location loc = monstersTeam.getSpawnsLocation().get(Papi.Function.randomInteger(0, monstersTeam.getSpawnsLocation().size()-1));
					Entity entity = loc.getWorld().spawnEntity(loc, entityType);
					entitiesArenaAlive.add(entity);
					for(Entity entityAlive : entitiesArenaAlive) {
						if(entityAlive instanceof Mob)
						{
							if(entityAlive.isValid())
							{
								Player pNear = Papi.Function.getNearestPlayer(loc);
								((Mob) entity).setTarget(pNear);
							}
						}
					}
				}
				
				spawnMobsTask(secoundDuration-1);
			}
		}, 20L);
	}



}

package me.maskat.compasspoint;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Function {

	public static void spawnPoint(Player player, Location location, boolean showFirstLowerParticle)
	{
		for(int i = (showFirstLowerParticle)?5:8;i<(200-location.getY());i=i+10) {
			player.spawnParticle(Particle.EXPLOSION_HUGE, location.getX(), location.getY()+i, location.getZ(), 1, 0, 0, 0);
		}
	}
	
//    public static void sendActionBar(final Player player, final String msg) {
//    	player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', msg)));
//    }
	
//	protected static void spawnAllParticle(Player player, Location location, int lastenabledId, int i) {
//		Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.plugin, new Runnable() {
//			@Override
//			public void run() {
//				//Bukkit.broadcastMessage("RUN TASK");
//				if(!player.isOnline() || enabledId != lastenabledId)
//				{
//					Bukkit.broadcastMessage("DISABLE TASK");
//					return;
//				}
//				
//				Particle particle = Particle.values()[i];
//				//for(Particle particle : Particle.values()) {
//					if(particle != Particle.REDSTONE &&
//							particle != Particle.ITEM_CRACK &&
//							particle != Particle.BLOCK_CRACK &&
//							particle != Particle.BLOCK_DUST &&
//							particle != Particle.LEGACY_BLOCK_CRACK &&
//							particle != Particle.LEGACY_BLOCK_DUST &&
//							particle != Particle.LEGACY_FALLING_DUST &&
//							particle != Particle.FALLING_DUST
//							
//							)
//					{
//						Bukkit.broadcastMessage("* Particle: "+i+" = "+particle.name());
//						player.spawnParticle(particle, location.getX(), location.getY(), location.getZ()+i, 1, 0, 0, 0);
//						
//					}
//					else
//					{
//						Bukkit.broadcastMessage("* Particle: "+i+" = "+particle.name()+" (restricted! particle show off)");
//						//i=i+2;
//					}
//				//}
//				spawnAllParticle(player, location, lastenabledId, i+1);
//			}
//
//
//		}, 5*10L); //20 Tick (1 Second) delay before run() is called
//
//	}
//	public static void spawnFireworkForAll(Player player, Location location)
//	{
//		//Spawn the Firework, get the FireworkMeta.
//		Firework fw = (Firework) player.getWorld().spawnEntity(location, EntityType.FIREWORK);
//		FireworkMeta fwm = fw.getFireworkMeta();
//		
//		//Our random generator
////            Random r = new Random();
//// 
////            //Get the type
////            int rt = r.nextInt(4) + 1;
////            Type type = Type.BALL;
////            if (rt == 1) type = Type.BALL;
////            if (rt == 2) type = Type.BALL_LARGE;
////            if (rt == 3) type = Type.BURST;
////            if (rt == 4) type = Type.CREEPER;
////            if (rt == 5) type = Type.STAR;
//		
//		//Get our random colours
////            int r1i = r.nextInt(17) + 1;
////            int r2i = r.nextInt(17) + 1;
////            Color c1 = getColor(r1i);
////            Color c2 = getColor(r2i);
//		
//		//Create our effect with this
//		FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.AQUA).withFade(Color.AQUA).with(Type.BALL).trail(true).build();
//		
//		//Then apply the effect to the meta
//		fwm.addEffect(effect);
//		
//		//Generate some random power and set it
////            int rp = r.nextInt(2) + 1;
//		fwm.setPower(2);
//		
//		//Then apply this to our rocket
//		fw.setFireworkMeta(fwm);
//	}
//	
}

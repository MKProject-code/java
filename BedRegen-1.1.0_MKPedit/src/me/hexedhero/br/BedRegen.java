package me.hexedhero.br;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BedRegen extends JavaPlugin implements Listener {
	Plugin plugin;
	
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        plugin = Bukkit.getServer().getPluginManager().getPlugin("BedRegen");
        getLogger().info("BedRegen v" + this.getDescription().getVersion() + " Enabled!");
    }
    
    public void onDisable() {
        getLogger().info("BedRegen v" + this.getDescription().getVersion() + " Disabled!");
    }
    
    private ArrayList<Player> playersEnabledEffect = new ArrayList<Player>();
    @EventHandler
    public void playerBedEnterEvent(PlayerBedEnterEvent event) {
    	Player bedplayer = event.getPlayer();
    	if(playersEnabledEffect.contains(bedplayer))
    		return;
    	playersEnabledEffect.add(bedplayer);
    	doTask(bedplayer);
    	startTask(bedplayer);
    }
    
//    @EventHandler
//    public void playerBedLeaveEvent(PlayerBedLeaveEvent event) {
//    	Player bedplayer = event.getPlayer();
//    	if(playersEnabledEffect.contains(bedplayer))
//    		playersEnabledEffect.remove(bedplayer);
//    }
    
    public void startTask(Player bedplayer) {
        getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                if (bedplayer.isOnline() && bedplayer.isSleeping()) {
                	doTask(bedplayer);
                	startTask(bedplayer);
                }
                else
                {
                	if(playersEnabledEffect.contains(bedplayer))
                		playersEnabledEffect.remove(bedplayer);
                }
            }
        }, 30L);
    }
    
    public void doTask(Player bedplayer) {
    	if((double)bedplayer.getHealth() < (double)20) {
        	bedplayer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 1));
	    	for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
	    		player.spawnParticle(Particle.valueOf(plugin.getConfig().getString("Settings.Particle")), bedplayer.getLocation(), plugin.getConfig().getInt("Settings.Amount"), plugin.getConfig().getDouble("Settings.X-Spread"), plugin.getConfig().getDouble("Settings.Y-Spread"), plugin.getConfig().getDouble("Settings.Z-Spread"), plugin.getConfig().getDouble("Settings.Speed"));
	    	}
    	}
    }
}

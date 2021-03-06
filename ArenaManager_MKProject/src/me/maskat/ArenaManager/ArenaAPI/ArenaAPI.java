package me.maskat.ArenaManager.ArenaAPI;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.maskat.ArenaManager.Models.ArenesModel;
import me.maskat.ArenaManager.Models.ModelArena;
import me.maskat.ArenaManager.PlayerMenu.PlayerMenuMain;
import mkproject.maskat.Papi.Menu.PapiMenu;
import mkproject.maskat.Papi.Utils.Message;

public class ArenaAPI {
	public static ApiArena getModelArena(String arenaType) {
		for(ModelArena modelArena : ArenesModel.getArenesMap().values()) {
			if(modelArena.getType().equals(arenaType)) {
				return new ApiArena(modelArena);
			}
		}
		return null;
	}
	
	public static void playSound(Player player, Sound sound) {
		player.playSound(player.getLocation(), sound, 1, 0);
	}
	
	public static void sendTitle(Player player, String title, String subtitle) {
		Message.sendTitle(player, title, subtitle);
	}
	
	public static void openArenesPlayerMenu(Player player, PapiMenu backPapiMenu) {
		PlayerMenuMain.openMenu(player, backPapiMenu);
	}
}

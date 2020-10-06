package me.maskat.QuestManager.Quests;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface Quest {
	public String getDescription();
	public String getReward();
	public void questDone(Player player);
	
	//Listeners
	public void onKillEntity(Player killer, LivingEntity entity);
}

package me.maskat.QuestManager.Quests;

import org.bukkit.entity.Player;

public interface QuestInterface {
	public String getDescription();
	public String getReward();
	public void questDone(Player player);
}

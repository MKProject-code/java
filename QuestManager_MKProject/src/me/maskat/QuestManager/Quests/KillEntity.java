package me.maskat.QuestManager.Quests;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.maskat.QuestManager.Model.QModel;
import mkproject.maskat.Papi.Papi;

public class KillEntity implements Quest {

	private EntityType entityType;
	
	public KillEntity() {
		this.entityType = (EntityType) Papi.Function.getRandom(EntityType.values());
	}
	
	@Override
	public String getDescription() {
		return "Zabij "+this.entityType.name();
	}

	@Override
	public String getReward() {
		
		return null;
	}

	@Override
	public void questDone(Player player) {
		
	}

	@Override
	public void onKillEntity(Player killer, LivingEntity entity) {
		if(entity.getType() == entityType) {
			QModel.getPlayer(killer).addScore();
		}
	}

}

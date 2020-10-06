package me.maskat.QuestManager;

import mkproject.maskat.Papi.Scheduler.PapiScheduler;

public class Scheduler implements PapiScheduler {

	@Override
	public void runTaskThread() {
		Plugin.getQuestManager().doNewQuest();
	}

}

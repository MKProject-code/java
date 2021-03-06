package mkproject.maskat.Papi.Scheduler;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;

import mkproject.maskat.Papi.Papi;

public class PapiSchedulerManager {
	
	private static Collection<PapiSchedulerInstance> papiSchedulers = new ArrayList<>();
	
	public static void doCheck() {
		DayOfWeek dayOfWeek = Papi.Function.getCurrentLocalDateTime().getDayOfWeek();
		int hourOfDay = Papi.Function.getCurrentLocalDateTime().getHour();
		int minute = Papi.Function.getCurrentLocalDateTime().getMinute();
		int second = Papi.Function.getCurrentLocalDateTime().getSecond();
		
		for(PapiSchedulerInstance papiScheduler : papiSchedulers) {
			if(papiScheduler.getDayOfWeek() != null && papiScheduler.getDayOfWeek() != dayOfWeek)
				continue;
			if(papiScheduler.getHourOfDay() != -1 && papiScheduler.getHourOfDay() != hourOfDay)
				continue;
			if(papiScheduler.getMinute() != -1 && papiScheduler.getMinute() != minute)
				continue;
			if(papiScheduler.getSecond() != -1 && papiScheduler.getSecond() != second)
				continue;
			
			papiScheduler.getPapiScheduler().runTaskThread();
		}
	}
	
	public static void registerTask(PapiScheduler papiScheduler, DayOfWeek dayOfWeek, int hourOfDay, int minute, int second) {
		papiSchedulers.add(new PapiSchedulerInstance(papiScheduler, dayOfWeek, hourOfDay, minute, second));
	}
}

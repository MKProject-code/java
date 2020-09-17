package mkproject.maskat.Papi.Scheduler;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import mkproject.maskat.Papi.Papi;

public class PapiSchedulerManager {
	
	private static Collection<PapiSchedulerInstance> papiSchedulers = new ArrayList<>();
	
	public static void doCheck() {
		int year = Papi.Function.getCurrentLocalDateTime().getYear();
		int month = Papi.Function.getCurrentLocalDateTime().getMonthValue();
		int dayOfMonth = Papi.Function.getCurrentLocalDateTime().getDayOfMonth();
		DayOfWeek dayOfWeek = Papi.Function.getCurrentLocalDateTime().getDayOfWeek();
		int hourOfDay = Papi.Function.getCurrentLocalDateTime().getHour();
		int minute = Papi.Function.getCurrentLocalDateTime().getMinute();
		int second = Papi.Function.getCurrentLocalDateTime().getSecond();
		
		Collection<PapiSchedulerInstance> papiSchedulersToCheck = new ArrayList<>();
		for(PapiSchedulerInstance papiScheduler : papiSchedulers) {
			if(papiScheduler.getYear() != -1 && papiScheduler.getYear() != year)
				continue;
			if(papiScheduler.getMonth() != -1 && papiScheduler.getMonth() != month)
				continue;
			if(papiScheduler.getDayOfMonth() != -1 && papiScheduler.getDayOfMonth() != dayOfMonth)
				continue;
			if(papiScheduler.getDayOfWeek() != null && papiScheduler.getDayOfWeek() != dayOfWeek)
				continue;
			if(papiScheduler.getHourOfDay() != -1 && papiScheduler.getHourOfDay() != hourOfDay)
				continue;
			if(papiScheduler.getMinute() != -1 && papiScheduler.getMinute() != minute)
				continue;
			if(papiScheduler.getSecond() != -1 && papiScheduler.getSecond() != second)
				continue;
			
//			if(papiScheduler.getYear() != -1 && 
//					papiScheduler.getMonth() != -1 && 
//					papiScheduler.getDayOfMonth() != -1 && 
//					papiScheduler.getHourOfDay() != -1 && 
//					papiScheduler.getMinute() != -1 && 
//					papiScheduler.getSecond() != -1)
//			{
//				papiSchedulersToCheck.add(papiScheduler);
//			}
			
			if(papiScheduler.getRepeatAmount() == 0)
			{
				papiSchedulersToCheck.add(papiScheduler);
				continue;
			}
			else if(papiScheduler.getRepeatAmount() > 0)
				papiScheduler.minusRepeatAmount();
			
			papiScheduler.getPapiScheduler().runTaskThread();
			
			if(papiScheduler.getRepeatAmount() == 0)
				papiSchedulersToCheck.add(papiScheduler);
		}
		
		for(PapiSchedulerInstance papiSchedulerToCheck : papiSchedulersToCheck) {
			papiSchedulers.remove(papiSchedulerToCheck);
		}
	}
	
	@Deprecated
	public static boolean registerTask(PapiScheduler papiScheduler, DayOfWeek dayOfWeek, int hourOfDay, int minute, int second) {
		if(hourOfDay != -1 && hourOfDay < 0 && hourOfDay > 23)
			return false;
		if(minute != -1 && minute < 0 && minute > 60)
			return false;
		if(second != -1 && second < 0 && second > 60)
			return false;
		
		papiSchedulers.add(new PapiSchedulerInstance(papiScheduler, -1, -1, -1, dayOfWeek, hourOfDay, minute, second, -1));
		return true;
	}

	@Deprecated
	public static boolean registerTask(PapiScheduler papiScheduler, int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
		if(year != -1 && year < LocalDateTime.now().getYear())
			return false;
		if(month != -1 && month < 1 && month > 12)
			return false;
		if(dayOfMonth != -1 && dayOfMonth < 1 && dayOfMonth > 31)
			return false;
		if(hourOfDay != -1 && hourOfDay < 0 && hourOfDay > 23)
			return false;
		if(minute != -1 && minute < 0 && minute > 60)
			return false;
		if(second != -1 && second < 0 && second > 60)
			return false;
		
		if(year != -1 && month != -1 && dayOfMonth != -1 && hourOfDay != -1 && minute != -1 && second != -1)
		{
			if(LocalDateTime.of(year, month, dayOfMonth, hourOfDay, minute, second).isBefore(LocalDateTime.now()))
				return false;
		}
		
		papiSchedulers.add(new PapiSchedulerInstance(papiScheduler, year, month, dayOfMonth, null, hourOfDay, minute, second, 1));
		return true;
	}
	
	//int repeatAmount:
	//-1 = infinity
	//0 = never
	//1 = one time
	//2,3,4... = two time, etc.
	public static boolean registerRepeatTask(PapiScheduler papiScheduler, DayOfWeek dayOfWeek, int hourOfDay, int minute, int second, int repeatAmount) {
		if(hourOfDay != -1 && hourOfDay < 0 && hourOfDay > 23)
			return false;
		if(minute != -1 && minute < 0 && minute > 60)
			return false;
		if(second != -1 && second < 0 && second > 60)
			return false;
		
		papiSchedulers.add(new PapiSchedulerInstance(papiScheduler, -1, -1, -1, dayOfWeek, hourOfDay, minute, second, repeatAmount));
		return true;
	}
	
	//int repeatAmount:
	//-1 = infinity
	//0 = never
	//1 = one time
	//2,3,4... = two time, etc.
	public static boolean registerRepeatTask(PapiScheduler papiScheduler, int year, int month, int dayOfMonth, int hourOfDay, int minute, int second, int repeatAmount) {
		if(year != -1 && year < LocalDateTime.now().getYear())
			return false;
		if(month != -1 && month < 1 && month > 12)
			return false;
		if(dayOfMonth != -1 && dayOfMonth < 1 && dayOfMonth > 31)
			return false;
		if(hourOfDay != -1 && hourOfDay < 0 && hourOfDay > 23)
			return false;
		if(minute != -1 && minute < 0 && minute > 60)
			return false;
		if(second != -1 && second < 0 && second > 60)
			return false;
		
		if(year != -1 && month != -1 && dayOfMonth != -1 && hourOfDay != -1 && minute != -1 && second != -1)
		{
			if(LocalDateTime.of(year, month, dayOfMonth, hourOfDay, minute, second).isBefore(LocalDateTime.now()))
				return false;
		}
		
		papiSchedulers.add(new PapiSchedulerInstance(papiScheduler, year, month, dayOfMonth, null, hourOfDay, minute, second, repeatAmount));
		return true;
	}
}

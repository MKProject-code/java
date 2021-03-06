package mkproject.maskat.Papi.Scheduler;

import java.time.DayOfWeek;

public class PapiSchedulerInstance {

	private PapiScheduler papiScheduler;
	private DayOfWeek dayOfWeek;
	private int hourOfDay;
	private int minute;
	private int second;
	
	public PapiSchedulerInstance(PapiScheduler papiScheduler, DayOfWeek dayOfWeek, int hourOfDay, int minute, int second) {
		this.papiScheduler = papiScheduler;
		this.dayOfWeek = dayOfWeek;
		this.hourOfDay = hourOfDay;
		this.minute = minute;
		this.second = second;
	}
	
	public PapiScheduler getPapiScheduler() {
		return this.papiScheduler;
	}
	public DayOfWeek getDayOfWeek() {
		return this.dayOfWeek;
	}
	public int getHourOfDay() {
		return this.hourOfDay;
	}
	public int getMinute() {
		return this.minute;
	}
	public int getSecond() {
		return this.second;
	}
}

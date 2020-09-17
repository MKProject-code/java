package mkproject.maskat.Papi.Scheduler;

import java.time.DayOfWeek;

public class PapiSchedulerInstance {

	private PapiScheduler papiScheduler;
	private int year;
	private int month;
	private int dayOfMonth;
	private DayOfWeek dayOfWeek;
	private int hourOfDay;
	private int minute;
	private int second;
	private int repeatAmount;
	
	public PapiSchedulerInstance(PapiScheduler papiScheduler, int year, int month, int dayOfMonth, DayOfWeek dayOfWeek, int hourOfDay, int minute, int second, int repeatAmount) {
		this.papiScheduler = papiScheduler;
		this.year = year;
		this.month = month;
		this.dayOfMonth = dayOfMonth;
		this.dayOfWeek = dayOfWeek;
		this.hourOfDay = hourOfDay;
		this.minute = minute;
		this.second = second;
		this.repeatAmount = repeatAmount;
	}
	
	public PapiScheduler getPapiScheduler() {
		return this.papiScheduler;
	}
	public int getYear() {
		return this.year;
	}
	public int getMonth() {
		return this.month;
	}
	public int getDayOfMonth() {
		return this.dayOfMonth;
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
	public int getRepeatAmount() {
		return this.repeatAmount;
	}
	public void minusRepeatAmount() {
		if(this.repeatAmount > 0)
			this.repeatAmount--;
	}
}

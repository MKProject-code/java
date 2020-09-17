package mkproject.maskat.Papi;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

public class PapiConvert {
	protected static String getTimestampToDateStringFormat(String timestamp, String format) {
        return new SimpleDateFormat(format).format(getTimestampToDate(timestamp));
	}
	protected static Date getTimestampToDate(String timestamp) {
		Timestamp ts=new Timestamp(Long.parseLong(timestamp));  
		Date date=new Date(ts.getTime());
		return date;
	}
	protected static Duration getTimestampToDuration(String timestamp) {
		Timestamp ts=new Timestamp(Long.parseLong(timestamp));
		LocalTime now = ts.toLocalDateTime().toLocalTime();
		LocalTime previous = LocalTime.MIN;
		return Duration.between(previous, now);
	}
//	protected static String getTimestampToDurationString(String timestamp) {
//		Duration duration = getTimestampToDuration(timestamp);
//		long d = duration.toDays();
//		long h = duration.toHours() - 24 * d;
//		long m = duration.toMinutes() - 60 * duration.toHours();
//		long s = duration.toSeconds() - 60 * duration.toMinutes();
//		Bukkit.broadcastMessage(d + "d2X " + h + "h " + m + "m " + s + "s");
//		return d + "d " + h + "h " + m + "m " + s + "s";
//	}
	protected static String getTimestempTicksToDurationString(long timestempTicks) {
		Duration duration = Duration.ofSeconds((timestempTicks/20));
		long d = duration.toDays();
		long h = duration.toHours() - 24 * d;
		long m = duration.toMinutes() - 60 * duration.toHours();
		long s = duration.toSeconds() - 60 * duration.toMinutes();
		return d + "d " + h + "h " + m + "m " + s + "s";
	}
//	protected static String getTimestampToDurationString(String timestamp) {
//		Timestamp ts=new Timestamp(Long.parseLong(timestamp));
//		
//		long d = .toDays();
//		long h = duration.toHours() - 24 * d;
//		long m = duration.toMinutes() - 60 * duration.toHours();
//		long s = duration.toSeconds() - 60 * duration.toMinutes();
//		Bukkit.broadcastMessage(d + "d " + h + "h " + m + "m " + s + "s");
//		return d + "d " + h + "h " + m + "m " + s + "s";
//	}
}

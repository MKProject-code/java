package mkproject.maskat.AdminUtils.Model;

import java.time.Duration;
import java.time.LocalDateTime;

import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;

public class HistoryData {
	
	String playerName;
	String type;
	LocalDateTime datetime;
	LocalDateTime datetime_end;
	String admin;
	boolean active;
	String reason;
	String admin_deactive;
	String reason_deactive;
	
	public HistoryData(
			String playerName,
			String type,
			String datetime,
			String datetime_end,
			String admin,
			boolean active,
			String reason,
			String admin_deactive,
			String reason_deactive)
	{
		this.playerName = playerName;
		this.type = type;
		this.datetime = Papi.Function.getLocalDateTimeFromString(datetime);
		this.datetime_end = (datetime_end == null ? null : Papi.Function.getLocalDateTimeFromString(datetime_end));
		this.admin = admin;
		this.active = active;
		this.reason = reason;
		this.admin_deactive = admin_deactive;
		this.reason_deactive = reason_deactive;
	}
	
	public void sendMessageRaw(Player player) {

		String remaining = (datetime_end == null ? null : Papi.Function.getRemainingTimeString(datetime_end));
		
		String rawMessage = formatMessageRaw(
				Papi.Function.getLocalDateTimeToString(datetime),
				(datetime_end == null ? "NIGDY" : Papi.Function.getLocalDateTimeToString(datetime_end)),
				this.type.toUpperCase(),
				this.admin,
				(this.datetime_end == null ? "-" : Papi.Function.getRemainingTimeString(datetime, datetime_end)),
				(remaining == null ? "-" : remaining),
				Message.getColorMessage(this.reason),
				this.active,
				this.admin_deactive,
				Message.getColorMessage(this.reason_deactive)
				);
		
		Message.sendRawMessage(player, rawMessage);
	}
	
	private static String formatMessageRaw(String datetime, String datetime_end, String type, String admin, String duration, String remaining, String reason, boolean active, String admin_deactive, String reason_deactive) {
		return 
				"[" + 
					"\"\"," + 
					"{" + 
						"\"text\":\"[\"," + 
						"\"color\":\"dark_gray\"" + 
					"}," + 
					"{" + 
						"\"text\":\""+datetime+"\"," + 
//						"\"bold\":true," + 
						"\"color\":\"aqua\"" + 
						(type.equals("KICK")?"":",\"hoverEvent\":" + 
						"{" + 
							"\"action\":\"show_text\"," + 
							"\"contents\":" + 
							"[" + 
								"{" + 
									"\"text\":\"Data zakończenia: "+datetime_end+"\"," + 
									"\"color\":\"gray\"" + 
								"}" + 
							"]" + 
						"}") + 
					"}," + 
					"{" + 
						"\"text\":\"]\"," + 
						"\"color\":\"dark_gray\"" + 
					"}," + 
					"{" + 
						"\"text\":\" "+type+"\"," + 
						"\"bold\":true," + 
						"\"color\":\""+(active?(!(duration.equals("-") && type.equals("BAN")) && remaining.equals("-")?"blue":"dark_green"):"red")+"\"," + 
						"\"hoverEvent\":" + 
						"{" + 
							"\"action\":\"show_text\"," + 
							"\"contents\":" + 
							"[" + 
								"{" + 
									"\"text\":\"Admin: "+admin+"\"," + 
									"\"color\":\"gray\"" + 
								"}" + 
								(active==false?(",{" + 
									"\"text\":\"\\nDeaktywował: "+admin_deactive+"\"," + 
									"\"color\":\"gray\"" + 
								"}"):"") + 
							"]" + 
						"}" + 
					"}," + 
					(duration.equals("-") ? 
					(
						type.equals("BAN") ?
						"{" + 
							"\"text\":\" perm\"," + 
							"\"color\":\"aqua\"" + 
						"},"
						:
						""
					):(
					"{" + 
						"\"text\":\" "+duration+"\"," + 
						"\"color\":\"aqua\"," + 
						"\"hoverEvent\":" + 
						"{" + 
							"\"action\":\"show_text\"," + 
							"\"contents\":" + 
							"[" + 
								"{" + 
									"\"text\":\"Pozostało: "+remaining+"\"," + 
									"\"color\":\"gray\"" + 
								"}" + 
							"]" + 
						"}" + 
					"},"
					)) + 
					"{" + 
						"\"text\":\":\"," + 
						"\"color\":\"dark_gray\"" + 
					"}," + 
					"{" + 
						"\"text\":\" "+reason+"\"," + 
						"\"color\":\"red\"" + 
						(active==false?(",\"hoverEvent\":" + 
						"{" + 
							"\"action\":\"show_text\"," + 
							"\"contents\":" + 
							"[" + 
								"{" + 
									"\"text\":\"Powód deaktywacji: "+reason_deactive+"\"," + 
									"\"color\":\"gray\"" + 
								"}" + 
							"]" + 
						"}"):"") + 
					"}" + 
				"]";
	}
}

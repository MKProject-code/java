package mkproject.maskat.ChatManager.Models;

import java.time.LocalDateTime;

public class ModelPlayer {
	
	private String lastMessage = null;
	private LocalDateTime lastMessageDateTime = null;
	
	public String getLastMessage() {
		if(lastMessageDateTime != null && lastMessageDateTime.plusSeconds(5).isAfter(LocalDateTime.now()))
			return this.lastMessage;
		return null;
	}
	
	public void setLastMessage(String message) {
		this.lastMessage = message;
		this.lastMessageDateTime = LocalDateTime.now();
	}
}

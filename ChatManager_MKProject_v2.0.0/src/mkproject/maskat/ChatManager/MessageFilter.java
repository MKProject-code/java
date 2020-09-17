package mkproject.maskat.ChatManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageFilter {

	private Map<String,String> replaceChars = new HashMap<>();
	private Map<String,String> replaceWorlds = new HashMap<>();
	
	public MessageFilter() {
		replaceChars.put("ę", "e");
		replaceChars.put("ó", "u");//!!!
		replaceChars.put("o", "u");//!!!
		replaceChars.put("ą", "a");
		replaceChars.put("ś", "s");
		replaceChars.put("ł", "l");
		replaceChars.put("ż", "z");
		replaceChars.put("ź", "z");
		replaceChars.put("ć", "c");
		replaceChars.put("ń", "n");
		replaceChars.put("ch", "h");
		replaceChars.put("ćh", "h");
		replaceChars.put("\\.", "");
		replaceChars.put(",", "");
		replaceChars.put(" ", "");
		replaceChars.put("_", "");
		
		replaceWorlds.put("sluhuj", "");
	}
	
	public boolean isDisallowedWords(String message) {
		message = message.toLowerCase();
		
		for(Map.Entry<String,String> entry : replaceChars.entrySet())
			message = message.replaceAll(entry.getKey(), entry.getValue());
		
		for(Map.Entry<String,String> entry : replaceWorlds.entrySet())
			message = message.replaceAll(entry.getKey(), entry.getValue());
		
		List<String> disallowedWords = List.of(
				"kurw",
				"jeba",
				"skurw",
				"huj",
				"pedal",
				"ciul",
				"pierdol",
				"pierdal",
				"pizd"
				);
		
		for(String word : disallowedWords) {
			if(message.indexOf(word) >= 0) {
				return true;
			}
		}
		return false;
	}
}

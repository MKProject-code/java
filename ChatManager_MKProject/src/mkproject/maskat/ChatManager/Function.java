package mkproject.maskat.ChatManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Function {
	
	public static boolean isDisallowedWords(String message) {
		Map<String,String> replaceChars = new HashMap<>();
		replaceChars.put("ę", "e");
		replaceChars.put("ó", "u");//!!!
		replaceChars.put("ą", "a");
		replaceChars.put("ś", "s");
		replaceChars.put("ł", "l");
		replaceChars.put("ż", "z");
		replaceChars.put("ź", "z");
		replaceChars.put("ć", "c");
		replaceChars.put("ń", "n");
		replaceChars.put("\\.", "");
		replaceChars.put(",", "");
		replaceChars.put(" ", "");
		replaceChars.put("_", "");
		
		for(Map.Entry<String,String> entry : replaceChars.entrySet()) {
			message = message.replaceAll(entry.getKey(), entry.getValue());
		}
		
		List<String> disallowedWords = List.of(
				"kurw",
				"jeba",
				"skurw",
				"chuj",
				"pedal",
				"ciul",
				"pierdol",
				"pierdal"
				);
		
		for(String word : disallowedWords) {
			if(message.indexOf(word) >= 0) {
				return true;
			}
		}
		return false;
	}
}
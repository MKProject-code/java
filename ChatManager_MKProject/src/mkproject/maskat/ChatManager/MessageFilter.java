package mkproject.maskat.ChatManager;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;

public class MessageFilter {

	private Map<String,String> replaceChars = new HashMap<>();
	private Map<String,String> replaceWords = new HashMap<>();
	
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
		
		replaceWords.put("sluhuj", "");
		replaceWords.put("aciul", "");
	}
	
	public int isDisallowedWords(String message) {
		message = message.toLowerCase();
		
		final String URL_REGEX = "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
		final String IP_REGEX = "\\b(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\b";
		//final String URL_REGEX = "((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+[a-z0-9-]+(\\.[a-z0-9-]+[a-z0-9-]+)+([/?].*)?";

		Pattern p_url = Pattern.compile(URL_REGEX);
		Pattern p_ip = Pattern.compile(IP_REGEX);
		Matcher m_url = p_url.matcher(message);//replace with string to compare
		Matcher m_ip = p_ip.matcher(message);//replace with string to compare
		if(m_url.find())
		    return 2;
		if(m_ip.find())
			return 3;
		
		for(Map.Entry<String,String> entry : replaceChars.entrySet())
			message = message.replaceAll(Pattern.quote(entry.getKey()), entry.getValue());
		
		for(Map.Entry<String,String> entry : replaceWords.entrySet())
			message = message.replaceAll(Pattern.quote(entry.getKey()), entry.getValue());
		
		List<String> disallowedWords = List.of(
				"kurw",
				"jebac",
				"jebal",
				"jeban",
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
				return 1;
			}
		}
		return 0;
	}
}

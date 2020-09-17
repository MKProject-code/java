package mkproject.maskat.ChatManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;

public class DictionaryFilter {

	private Map<String,String> replaceChars = new HashMap<>();
	private Map<String,String> replaceWords = new HashMap<>();
	private String colorMessage;
	private String colorInvalidWord;
	
	public DictionaryFilter() {
		replaceChars.put("ę", "e");
		replaceChars.put("ó", "o");//!!!
//		replaceChars.put("ó", "u");//!!!
//		replaceChars.put("o", "u");//!!!
		replaceChars.put("ą", "a");
		replaceChars.put("ś", "s");
		replaceChars.put("ł", "l");
		replaceChars.put("ż", "z");
		replaceChars.put("ź", "z");
		replaceChars.put("ć", "c");
		replaceChars.put("ń", "n");
		reloadDatabase();
	}
	
	public void reloadDatabase() {
		ConfigurationSection databaseSection = Plugin.getPlugin().getConfig().getConfigurationSection("DictionaryFilter.Words");
		if(databaseSection == null)
			return;
		
		for(String key : databaseSection.getKeys(false)) {
			String value = Plugin.getPlugin().getConfig().getString("DictionaryFilter.Words."+key);
			replaceWords.put(key, value);
		}
		
		this.colorMessage = Plugin.getPlugin().getConfig().getString("DictionaryFilter.ColorForInvalidMessage");
		this.colorInvalidWord = Plugin.getPlugin().getConfig().getString("DictionaryFilter.ColorForInvalidWord");
	}
	
	public String getValidMessage(String message) {
		List<String> messageWords = Arrays.asList(message.split(" "));
		
		boolean messageInvalid = false;
		
		for(int i=0; i<messageWords.size(); i++) {
			String messageWord = messageWords.get(i);
			for(Entry<String, String> replaceWordEntry : replaceWords.entrySet())
			{
				String messageWordTemp = messageWord;
				for(Map.Entry<String,String> entry : replaceChars.entrySet())
					messageWordTemp = messageWord.replaceAll(Pattern.quote(entry.getKey()), entry.getValue());
				
				if(messageWordTemp.equalsIgnoreCase(replaceWordEntry.getKey()))
				{
					String validWord = "";
//					List<Character> wordChars =  messageWord.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
					char wordChar = messageWord.charAt(0);
					if(Character.isLowerCase(wordChar))
						validWord += Character.toLowerCase(replaceWordEntry.getValue().charAt(0));
					if(Character.isUpperCase(wordChar))
						validWord += Character.toUpperCase(replaceWordEntry.getValue().charAt(0));
					
					validWord += replaceWordEntry.getValue().substring(1);
					
//					for(int iChar=0; iChar<messageWord.length(); iChar++) {
//						char wordChar = messageWord.charAt(iChar);
//						if(Character.isLowerCase(wordChar))
//							validWord += Character.toLowerCase(replaceWordEntry.getValue().charAt(iChar));
//						if(Character.isUpperCase(wordChar))
//							validWord += Character.toUpperCase(replaceWordEntry.getValue().charAt(iChar));
//						//if(String.valueOf(wordChars[iChar]).equals(String.valueOf(replaceWordEntry.getValue().charAt(iChar)).toLowerCase()))
//							
//						//boolean hasLowercase = !password.equals(password.toUpperCase());
//					}
					
					messageWords.set(i, colorInvalidWord+validWord+colorMessage);
					messageInvalid = true;
				}
			}
			//wordsREGEX += "|(\\s"+Pattern.quote(messageWord)+"\\s)";
		}
		
		if(messageInvalid)
			return colorMessage+String.join(" ", messageWords);
		else
			return null;
//		
//		final String REGEX = "("+wordsREGEX.substring(1)+")/i";
//
//		Bukkit.broadcastMessage(REGEX);
//		
//		Pattern p = Pattern.compile(REGEX);
//		Matcher m = p.matcher(message);//replace with string to compare
//		if(m.find())
//		{
//			m.group(1);
//			Bukkit.broadcastMessage("find!");
//		}
//		else
//			Bukkit.broadcastMessage("not");
//		return message;
	}
}

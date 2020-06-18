package mkproject.maskat.WorldManager;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Function {
	public static boolean isJSONValid(String jsonString) {
		JSONParser parser = new JSONParser();
		try { 
			parser.parse(jsonString); 
		} catch (ParseException e) {
			return false;
		}
	    return true;
	}
}

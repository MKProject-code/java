package mkproject.maskat.CmdBlockAdvancedManager;

import java.util.regex.Pattern;

public class Validation {
	
	// Return calculated value or return -1 if string is invalid
	public static long getActionsDelayFromInput(String newActionsDelay) {
		Pattern p = Pattern.compile("[^0-9\\*]");
		if(p.matcher(newActionsDelay).find())
			return -1L;
		
		long newActionsDelayLong = 0;
		
		if(newActionsDelay.indexOf("*") >= 0) {
			String[] newActionsInDelayArr = newActionsDelay.split("\\*");
			
			if(newActionsInDelayArr.length <= 0)
				return -1L;
			
			boolean isEmpty = true;
			for(String valStr : newActionsInDelayArr)
			{
				if(valStr.length() > 0)
				{
					if(isEmpty)
					{
						newActionsDelayLong = Long.parseLong(valStr);
						isEmpty = false;
					}
					else
						newActionsDelayLong *= Long.parseLong(valStr);
				}
			}
			
			if(isEmpty)
				return -1L;
		}
		else
			newActionsDelayLong = Long.parseLong(newActionsDelay);
		
		return newActionsDelayLong;
	}
}

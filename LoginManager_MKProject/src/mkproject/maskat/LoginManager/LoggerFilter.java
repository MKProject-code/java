package mkproject.maskat.LoginManager;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

@Deprecated
public class LoggerFilter implements Filter {
	
    @Override
    public boolean isLoggable(LogRecord record) {
        if (record == null || record.getMessage() == null) {
            return true;
        }

        String lowerMessage = record.getMessage();
        String playerName = record.getMessage().split(" ")[0];
        
        Plugin.getPlugin().getLogger().warning("LOG="+lowerMessage);
        
        if (isProtectedCommand(lowerMessage, "/l"))
            record.setMessage(playerName + " issued server command: /l ****");
        else if(isProtectedCommand(lowerMessage, "/login"))
        	record.setMessage(playerName + " issued server command: /login ****");
        else if(isProtectedCommand(lowerMessage, "/reg"))
        	record.setMessage(playerName + " issued server command: /reg ****");
        else if(isProtectedCommand(lowerMessage, "/register"))
        	record.setMessage(playerName + " issued server command: /register ****");
        else if(isProtectedCommand(lowerMessage, "/changepass"))
        	record.setMessage(playerName + " issued server command: /changepass ****");
        else if(isProtectedCommand(lowerMessage, "/changepassword"))
        	record.setMessage(playerName + " issued server command: /changepassword ****");
        
        return true;
    }
    
    static boolean isProtectedCommand(String lowerMessage, String lowerCommand) {
        return lowerMessage.contains("issued server command: "+lowerCommand+" ");
    }

}
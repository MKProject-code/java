package mkproject.maskat.LoginManager;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public class Logger4JFilter implements AbstractFilter  {

    private static final long serialVersionUID = -5594073755007974254L;

    private static Result validateMessage(Message message) {
        if (message == null) {
            return Result.NEUTRAL;
        }
        return validateMessage(message.getFormattedMessage());
    }

    private static Result validateMessage(String message) {
    	
    	String lowerMessage = message.toLowerCase();
        if (isProtectedCommand(lowerMessage, "/l"))
        	return Result.DENY;
        else if(isProtectedCommand(lowerMessage, "/login"))
        	return Result.DENY;
        else if(isProtectedCommand(lowerMessage, "/reg"))
        	return Result.DENY;
        else if(isProtectedCommand(lowerMessage, "/register"))
        	return Result.DENY;
        else if(isProtectedCommand(lowerMessage, "/changepass"))
        	return Result.DENY;
        else if(isProtectedCommand(lowerMessage, "/changepassword"))
        	return Result.DENY;
        
        return Result.NEUTRAL;
    }
    
	static boolean isProtectedCommand(String lowerMessage, String lowerCommand) {
	    return lowerMessage.contains("issued server command: "+lowerCommand+" ");
	}

    @Override
    public Result filter(LogEvent event) {
        Message candidate = null;
        if (event != null) {
            candidate = event.getMessage();
        }
        return validateMessage(candidate);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return validateMessage(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return validateMessage(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        String candidate = null;
        if (msg != null) {
            candidate = msg.toString();
        }
        return validateMessage(candidate);
    }

}
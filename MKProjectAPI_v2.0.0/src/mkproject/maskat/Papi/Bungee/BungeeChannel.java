package mkproject.maskat.Papi.Bungee;

public enum BungeeChannel {
	GlobalChat("globalchat");
	
	private String bungeeChannel;
	 
	BungeeChannel(String bungeeChannel) {
    	this.bungeeChannel = bungeeChannel;
	}
 
    public String toString() {
        return bungeeChannel;
    }
    
    public String getChannelOutgoing() {
    	return bungeeChannel;
    }
    
    public String getChannelIncoming() {
    	return "papi:"+bungeeChannel;
    }
}

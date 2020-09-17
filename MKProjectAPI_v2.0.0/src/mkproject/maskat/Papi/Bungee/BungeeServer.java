package mkproject.maskat.Papi.Bungee;

public enum BungeeServer {
	LOBBY("lobby"),
	SURVIVAL("survival"),
	SKYBLOCK("skyblock"),
	ARENES("arenes");
	
	private String bungeeChannel;
	 
	BungeeServer(String bungeeChannel) {
    	this.bungeeChannel = bungeeChannel;
	}
 
    public String toString() {
        return bungeeChannel;
    }
}

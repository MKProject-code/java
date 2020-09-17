package mkproject.maskat.GuildsManager.Model;

public class GuildPlayerOffline {

	private String playerName;
	private int kills;
	private int deaths;
	private String lastKiller;
	
	public GuildPlayerOffline(String playerName, int kills, int deaths, String lastKiller) {
		this.playerName = playerName;
		this.kills = kills;
		this.deaths = deaths;
		this.lastKiller = lastKiller;
	}
	
	public String getPlayerName() {
		return this.playerName;
	}
	
	public int getKills() {
		return this.kills;
	}
	
	public int getDeaths() {
		return this.deaths;
	}

	public String getLastKiller() {
		return this.lastKiller;
	}
	
	

}

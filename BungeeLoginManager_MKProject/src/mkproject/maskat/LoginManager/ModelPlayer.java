package mkproject.maskat.LoginManager;

import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;

public class ModelPlayer {
	private boolean registered;
	private boolean registered_byAuthme;
	private boolean executedFirstCmd;
	private boolean isLogged;
	
	public ModelPlayer(Player p) {
		this.registered = Papi.MySQL.exists(
				Papi.SQL.getWhereObject(Database.Users.NAME, "=", p.getName().toLowerCase()),
				Database.Users.TABLE);
		
		this.isLogged = false;
		
		this.registered_byAuthme = false;
		
//		AuthMe Verifier
//		if(registered == false) {
//			if(Papi.MySQL.exists(
//					Papi.SQL.getWhereObject("username", "=", p.getName().toLowerCase()),
//					"authme"))
//			{
//				registered = true;
//				registered_byAuthme = true;
//			}
//		}
		
		this.executedFirstCmd = false;
	}
	protected boolean isRegistered() { 
		return this.registered;
	}
	protected boolean setLogged(boolean logged) { 
		return this.isLogged = logged;
	}
	protected boolean isLogged() { 
		return this.isLogged;
	}
	protected boolean isRegisteredByAuthMe() { 
		return this.registered_byAuthme;
	}
	protected boolean isExecutedFirstCmd() { 
		return this.executedFirstCmd;
	}
	public void registerExecuteCmd() {
		this.executedFirstCmd = true;
	}
}

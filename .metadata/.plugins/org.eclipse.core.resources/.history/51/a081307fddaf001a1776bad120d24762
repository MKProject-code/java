package me.maskat.MoneyManager;

import java.util.Map;

import org.bukkit.OfflinePlayer;

import mkproject.maskat.Papi.Papi;

public class MapiOfflinePlayer {
	private OfflinePlayer offlinePlayer;
	private double points;
	
	public MapiOfflinePlayer(OfflinePlayer offlinePlayer) {
		this.offlinePlayer = offlinePlayer;
		
		if(!Papi.MySQL.exists(Database.Users.NAME, "=", offlinePlayer.getName().toLowerCase(), Database.Users.TABLE))
			Papi.MySQL.put(Map.of(Database.Users.NAME, offlinePlayer.getName().toLowerCase()), Database.Users.TABLE);
		
		Object result = Papi.MySQL.get(Database.Users.POINTS, Database.Users.NAME, "=", offlinePlayer.getName().toLowerCase(), Database.Users.TABLE);
		
		if(result == null)
			this.points = 0;
		else
		{
			try {
				this.points = Double.parseDouble(result.toString());
			} catch(Exception ex) {
				this.points = 0;
			}
		}
	}
	
	public OfflinePlayer getOfflinePlayer() {
		return this.offlinePlayer;
	}
	
	public double getPoints() {
		return this.points;
	}
	
	public double addPoints(double points) {
		this.points += points;
		Papi.MySQL.set(Map.of(Database.Users.POINTS, this.points), Database.Users.NAME, "=", offlinePlayer.getName().toLowerCase(), Database.Users.TABLE);
		return this.points;
	}
	public boolean delPoints(double points) {
		if(this.points >= points)
		{
			this.points -= points;
			Papi.MySQL.set(Map.of(Database.Users.POINTS, this.points), Database.Users.NAME, "=", offlinePlayer.getName().toLowerCase(), Database.Users.TABLE);
			return true;
		}
		else
		{
			return false;
		}
	}
}

package me.maskat.MoneyManager;

import java.util.Map;

import org.bukkit.entity.Player;

import mkproject.maskat.Papi.Papi;

public class MapiPlayer {
	private Player player;
	private double points;
	
	public MapiPlayer(Player player) {
		this.player = player;
		
		if(!Papi.MySQL.exists(Database.Users.NAME, "=", player.getName().toLowerCase(), Database.Users.TABLE))
			Papi.MySQL.put(Map.of(Database.Users.NAME, player.getName().toLowerCase()), Database.Users.TABLE);
		
		Object result = Papi.MySQL.get(Database.Users.POINTS, Database.Users.NAME, "=", player.getName().toLowerCase(), Database.Users.TABLE);
		
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
	
	public Player getPlayer() {
		return this.player;
	}
	
	public double getPoints() {
		return this.points;
	}
	
	public double addPoints(double points) {
		this.points += points;
		MapiModel.doMapiUpdate(this);
		return this.points;
	}
	public boolean delPoints(double points) {
		if(this.points >= points)
		{
			this.points -= points;
			MapiModel.doMapiUpdate(this);
			return true;
		}
		else
		{
			return false;
		}
	}
	public double setPoints(Integer points) {
		this.points = points;
		MapiModel.doMapiUpdate(this);
		return this.points;
	}

	protected void saveData() {
		Papi.MySQL.set(Map.of(Database.Users.POINTS,  MapiModel.getPlayer(player).getPoints()), Database.Users.NAME, "=", player.getName().toLowerCase(), Database.Users.TABLE);
	}
}

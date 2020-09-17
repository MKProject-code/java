package mkproject.maskat.WorldManager;

import org.bukkit.GameMode;

public class ModelWorld {

	private int borderRadius;
//	private int borderRadiusSquared;
	private boolean borderSquared;
	private GameMode gamemode;
	//private boolean allowWeather;
	
	public ModelWorld(GameMode gamemode, int borderRadius, boolean borderSquared) {//, int borderRadiusSquared, boolean allowWeather) {
		this.borderRadius = borderRadius;
//		this.borderRadiusSquared = borderRadiusSquared; // remember ^2 !!!
		this.borderSquared = borderSquared;
		this.gamemode = gamemode;
		//this.allowWeather = allowWeather;
	}
	
	public int getBorderRadius() {
		return this.borderRadius;
	}
	public boolean isBorderSquared() {
		return this.borderSquared;
	}
	public int getBorderRadiusSquared() {
		if(borderSquared)
			return this.borderRadius*this.borderRadius;
		else
			return -1;
	}
	public GameMode getGameMode() {
		return this.gamemode;
	}
//	public boolean isAllowWeather() {
//		return allowWeather;
//	}

	public void setGameMode(GameMode gamemode) {
		this.gamemode = gamemode;
	}

	public void setBorderRadius(int borderRadius) {
		this.borderRadius = borderRadius;
	}

	public void setBorderRadiusSquared(boolean borderSquared) {
		this.borderSquared = borderSquared;
	}

}

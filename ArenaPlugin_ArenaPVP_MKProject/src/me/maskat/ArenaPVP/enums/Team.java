package me.maskat.ArenaPVP.enums;

public enum Team {
    PLAYER_ONE("player_one"),
    PLAYER_TWO("player_two");
 
    public final String label;
 
	private Team(String label) {
        this.label = label;
    }
}

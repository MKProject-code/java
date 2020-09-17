package mkproject.maskat.Papi.BoardManager;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import mkproject.maskat.Papi.Utils.Message;

public class PapiScore {

	private Objective objective;
	private String scoreLastDisplayNameColor;
	private Score score;
	
	public PapiScore(Objective objective, String scoreDisplayNameColor, int value) {
		this.objective = objective;
		this.scoreLastDisplayNameColor = Message.getColorMessage(scoreDisplayNameColor);
		this.score = objective.getScore(this.scoreLastDisplayNameColor);
		this.score.setScore(value);
	}

	public PapiScore setScore(int value) {
		this.score.setScore(value);
		return this;
	}

	public PapiScore setScore(String scoreDisplayNameColor) {
		int value = this.score.getScore();
		this.objective.getScoreboard().resetScores(this.scoreLastDisplayNameColor);
		this.scoreLastDisplayNameColor = Message.getColorMessage(scoreDisplayNameColor);
		this.score = this.objective.getScore(this.scoreLastDisplayNameColor);
		this.score.setScore(value);
		return this;
	}

	public PapiScore setScore(String scoreDisplayNameColor, int value) {
		this.objective.getScoreboard().resetScores(this.scoreLastDisplayNameColor);
		this.scoreLastDisplayNameColor = Message.getColorMessage(scoreDisplayNameColor);
		this.score = this.objective.getScore(this.scoreLastDisplayNameColor);
		this.score.setScore(value);
		return this;
	}

}

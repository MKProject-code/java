package mkproject.maskat.Papi.BoardManager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;

import mkproject.maskat.Papi.Utils.Message;

public class PapiObjective {
	
	private PapiBoard papiBoard;
	private Objective objective;
	private Map<String, PapiScore> papiScores = new HashMap<>();

	public PapiObjective(PapiBoard papiBoard, String name, String criteria, String displayNameColor) {
		this.papiBoard = papiBoard;
		this.objective = papiBoard.board.registerNewObjective(name, criteria, Message.getColorMessage(displayNameColor));
	}
	
	public PapiObjective(PapiBoard papiBoard, String name, String criteria, String displayNameColor, RenderType renderType) {
		this.papiBoard = papiBoard;
		this.objective = papiBoard.board.registerNewObjective(name, criteria, Message.getColorMessage(displayNameColor), renderType);
	}
	
	public PapiObjective(PapiBoard papiBoard, String name, String criteria, String displayNameColor, RenderType renderType, DisplaySlot displaySlot) {
		this.papiBoard = papiBoard;
		this.objective = papiBoard.board.registerNewObjective(name, criteria, Message.getColorMessage(displayNameColor), renderType);
		this.objective.setDisplaySlot(displaySlot);
	}
	public void unregister() throws IllegalStateException {
		this.objective.unregister();
	}

	public PapiScore addScore(String scoreName, String scoreDisplayNameColor, int value) {
		PapiScore papiScore = new PapiScore(this.objective, scoreDisplayNameColor, value);
		this.papiScores.put(scoreName, papiScore);
		return papiScore;
	}
	
	public PapiBoard getBoard() {
		return this.papiBoard;
	}
	
	public PapiScore getScore(String scoreName) {
		return this.papiScores.get(scoreName);
	}
	
	public PapiScore setScore(String scoreName, int value) {
		return this.papiScores.get(scoreName).setScore(value);
	}
	
	public PapiScore setScore(String scoreName, String scoreDisplayNameColor) {
		return this.papiScores.get(scoreName).setScore(scoreDisplayNameColor);
	}
	
	public PapiScore setScore(String scoreName, String scoreDisplayNameColor, int value) {
		return this.papiScores.get(scoreName).setScore(scoreDisplayNameColor, value);
	}
}

package mkproject.maskat.Papi.BoardManager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import mkproject.maskat.Papi.Utils.Message;

public class PapiBoard {
	protected Scoreboard board;

	private Map<String, PapiObjective> papiObjectives = new HashMap<>();
	
	public PapiBoard() {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		this.board = manager.getNewScoreboard();
	}
	
	public PapiObjective registerNewObjective​(String name, String displayNameColor) {
		PapiObjective papiObjective = new PapiObjective(this, name, "dummy", Message.getColorMessage(displayNameColor));
		papiObjectives.put(name, papiObjective);
		return papiObjective;
	}
	public PapiObjective registerNewObjective​(String name, String displayNameColor, RenderType renderType) {
		PapiObjective papiObjective = new PapiObjective(this, name, "dummy", Message.getColorMessage(displayNameColor), renderType);
		papiObjectives.put(name, papiObjective);
		return papiObjective;
	}
	public PapiObjective registerNewObjective​(String name, String displayNameColor, RenderType renderType, DisplaySlot displaySlot) {
		PapiObjective papiObjective = new PapiObjective(this, name, "dummy", Message.getColorMessage(displayNameColor), renderType, displaySlot);
		papiObjectives.put(name, papiObjective);
		return papiObjective;
	}
	
	public PapiObjective registerNewObjective​(String name, String criteria, String displayNameColor) {
		PapiObjective papiObjective = new PapiObjective(this, name, criteria, Message.getColorMessage(displayNameColor));
		papiObjectives.put(name, papiObjective);
		return papiObjective;
	}
	public PapiObjective registerNewObjective​(String name, String criteria, String displayNameColor, RenderType renderType) {
		PapiObjective papiObjective = new PapiObjective(this, name, criteria, Message.getColorMessage(displayNameColor), renderType);
		papiObjectives.put(name, papiObjective);
		return papiObjective;
	}
	public PapiObjective registerNewObjective​(String name, String criteria, String displayNameColor, RenderType renderType, DisplaySlot displaySlot) {
		PapiObjective papiObjective = new PapiObjective(this, name, criteria, Message.getColorMessage(displayNameColor), renderType, displaySlot);
		papiObjectives.put(name, papiObjective);
		return papiObjective;
	}

	public Team registerNewTeam(String name) {
		return this.board.registerNewTeam(name);
	}
	public Scoreboard getScoreboard() {
		return this.board;
	}
	public void assignToPlayer(Player player) {
		player.setScoreboard(this.board);
	}
	public void unassignPlayer(Player player) {
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}
}

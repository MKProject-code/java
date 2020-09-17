package me.maskat.ArenaManager.PlayerMenu;

import me.maskat.ArenaManager.Models.ModelArena;
import me.maskat.ArenaManager.Models.ModelArenaObject;
import me.maskat.ArenaManager.Models.ModelArenaSpawn;
import me.maskat.ArenaManager.Models.ModelArenaTeam;
import me.maskat.ArenaManager.PlayerMenu.PlayerMenuMain.SlotOption;
import mkproject.maskat.Papi.Menu.PapiMenuPage;

public class PlayerMenuItem {
	private PapiMenuPage papiMenuPage;
	private SlotOption slotOption;
	private ModelArena arena = null;
	private ModelArenaTeam team = null;
	private ModelArenaSpawn spawn = null;
	private ModelArenaObject object = null;
	
	public PlayerMenuItem(PapiMenuPage papiMenuPage, SlotOption slotOption) {
		this.papiMenuPage = papiMenuPage;
		this.slotOption = slotOption;
	}
	public PlayerMenuItem(PapiMenuPage papiMenuPage, SlotOption slotOption, PlayerMenuItem playerMenuItem) {
		this.papiMenuPage = papiMenuPage;
		this.slotOption = slotOption;
		if(playerMenuItem!=null) {
			this.arena = playerMenuItem.getArenaModel();
			this.team = playerMenuItem.getArenaTeamModel();
			this.spawn = playerMenuItem.getArenaSpawnModel();
		}
	}
	public PlayerMenuItem(PapiMenuPage papiMenuPage, SlotOption slotOption, ModelArena arena) {
		this.papiMenuPage = papiMenuPage;
		this.slotOption = slotOption;
		this.arena = arena;
	}
	public PlayerMenuItem(PapiMenuPage papiMenuPage, SlotOption slotOption, ModelArena arena, ModelArenaTeam team) {
		this.papiMenuPage = papiMenuPage;
		this.slotOption = slotOption;
		this.arena = arena;
		this.team = team;
	}
	public PlayerMenuItem(PapiMenuPage papiMenuPage, SlotOption slotOption, ModelArena arena, ModelArenaObject object) {
		this.papiMenuPage = papiMenuPage;
		this.slotOption = slotOption;
		this.arena = arena;
		this.object = object;
	}
	public PlayerMenuItem(PapiMenuPage papiMenuPage, SlotOption slotOption, ModelArena arena, ModelArenaTeam team, ModelArenaSpawn spawn) {
		this.papiMenuPage = papiMenuPage;
		this.slotOption = slotOption;
		this.arena = arena;
		this.team = team;
		this.spawn = spawn;
	}
	
	public PapiMenuPage getMenuPage() { return papiMenuPage; }
	public SlotOption getSlotOption() { return slotOption; }
	public ModelArena getArenaModel() { return arena; }
	public ModelArenaTeam getArenaTeamModel() { return team; }
	public ModelArenaSpawn getArenaSpawnModel() { return spawn; }
	public ModelArenaObject getArenaObjectModel() { return object; }
}

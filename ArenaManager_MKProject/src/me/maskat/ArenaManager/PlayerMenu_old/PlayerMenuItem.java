package me.maskat.ArenaManager.PlayerMenu_old;

import me.maskat.ArenaManager.Models.ModelArena;
import me.maskat.ArenaManager.Models.ModelArenaObject;
import me.maskat.ArenaManager.Models.ModelArenaSpawn;
import me.maskat.ArenaManager.Models.ModelArenaTeam;
import me.maskat.ArenaManager.PlayerMenu.PlayerMenuMain.SlotOption;
import mkproject.maskat.Papi.MenuInventory.MenuPage;

public class PlayerMenuItem {
	private MenuPage menuPage;
	private SlotOption slotOption;
	private ModelArena arena = null;
	private ModelArenaTeam team = null;
	private ModelArenaSpawn spawn = null;
	private ModelArenaObject object = null;
	
	public PlayerMenuItem(MenuPage menuPage, SlotOption slotOption) {
		this.menuPage = menuPage;
		this.slotOption = slotOption;
	}
	public PlayerMenuItem(MenuPage menuPage, SlotOption slotOption, PlayerMenuItem playerMenuItem) {
		this.menuPage = menuPage;
		this.slotOption = slotOption;
		if(playerMenuItem!=null) {
			this.arena = playerMenuItem.getArenaModel();
			this.team = playerMenuItem.getArenaTeamModel();
			this.spawn = playerMenuItem.getArenaSpawnModel();
		}
	}
	public PlayerMenuItem(MenuPage menuPage, SlotOption slotOption, ModelArena arena) {
		this.menuPage = menuPage;
		this.slotOption = slotOption;
		this.arena = arena;
	}
	public PlayerMenuItem(MenuPage menuPage, SlotOption slotOption, ModelArena arena, ModelArenaTeam team) {
		this.menuPage = menuPage;
		this.slotOption = slotOption;
		this.arena = arena;
		this.team = team;
	}
	public PlayerMenuItem(MenuPage menuPage, SlotOption slotOption, ModelArena arena, ModelArenaObject object) {
		this.menuPage = menuPage;
		this.slotOption = slotOption;
		this.arena = arena;
		this.object = object;
	}
	public PlayerMenuItem(MenuPage menuPage, SlotOption slotOption, ModelArena arena, ModelArenaTeam team, ModelArenaSpawn spawn) {
		this.menuPage = menuPage;
		this.slotOption = slotOption;
		this.arena = arena;
		this.team = team;
		this.spawn = spawn;
	}
	
	public MenuPage getMenuPage() { return menuPage; }
	public SlotOption getSlotOption() { return slotOption; }
	public ModelArena getArenaModel() { return arena; }
	public ModelArenaTeam getArenaTeamModel() { return team; }
	public ModelArenaSpawn getArenaSpawnModel() { return spawn; }
	public ModelArenaObject getArenaObjectModel() { return object; }
}

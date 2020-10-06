package mkproject.maskat.MiniGamesManager.Game;

import org.bukkit.Material;

import mkproject.maskat.Papi.Menu.InventorySlot;

public enum MiniGame {
	ColorMix(InventorySlot.ROW2_COLUMN1, Material.GREEN_CONCRETE),
	Sumo(InventorySlot.ROW2_COLUMN2, Material.STICK),
	TntRun(InventorySlot.ROW2_COLUMN3, Material.TNT);

	private InventorySlot menuSlot;
	private Material menuIcon;
	
	MiniGame(InventorySlot menuSlot, Material menuIcon) {
		this.menuSlot = menuSlot;
		this.menuIcon = menuIcon;
	}
	
	public Material getMenuIcon() {
		return this.menuIcon;
	}

	public InventorySlot getMenuSlot() {
		return this.menuSlot;
	}
}

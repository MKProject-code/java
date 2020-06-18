package me.maskat.WorldEditTools.models;

import java.io.File;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import me.maskat.WorldEditTools.Plugin;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.MenuInventory.InventorySlot;
import mkproject.maskat.Papi.MenuInventory.MenuPage;
import mkproject.maskat.Papi.MenuInventory.PapiMenuInventoryClickEvent;
import mkproject.maskat.Papi.Utils.Message;

public class ModelPlayer {
	public enum Tool {
		WORLDEDIT_SCHEMATIC,
		WORLDEDIT_FAST_UNDO_REDO,
		SIGN_EDITOR,
	}
	
	private Player player;
	//private MenuPage inventoryMenuPage = null;
	private Tool toolEnabled = null;
	private String selectedSchemat = null;
	String[] signLines = null;
	
	public ModelPlayer(Player player) {
		this.player = player;
	}

	public void onPapiMenuInventoryClick(PapiMenuInventoryClickEvent e) {
		if(!e.existSlotStoreObject())
			return;
		
		Object[] objectArr = (Object[])e.getSlotStoreObject();
		
		if(objectArr[0] == null) {
			toolEnabled = null;
			selectedSchemat = null;
			signLines = null;
			Message.sendMessage(player, "&c&oWyłączono narzędzie");
		}
		else if(objectArr[0] == Tool.WORLDEDIT_SCHEMATIC) {
			if(objectArr[1] == null) {
				selectedSchemat = null;
				toolEnabled = Tool.WORLDEDIT_SCHEMATIC;
				Message.sendMessage(player, "&a&oAktywne narzędzie: " + toolEnabled);
			} else if(toolEnabled != Tool.WORLDEDIT_SCHEMATIC) {
				Message.sendMessage(player, "&c&oMusisz najpierw aktywować narzędzie: " + Tool.WORLDEDIT_SCHEMATIC);
			} else {
				selectedSchemat = (String)objectArr[1];
				Message.sendMessage(player, "&a&oWybrano schemat: " + selectedSchemat);
			}
		}
		else if(objectArr[0] == Tool.SIGN_EDITOR && objectArr[1] == null) {
			if(objectArr[1] == null) {
				signLines = null;
				toolEnabled = Tool.SIGN_EDITOR;
				Message.sendMessage(player, "&a&oAktywne narzędzie: " + toolEnabled);
			}
		}
		else if(objectArr[0] == Tool.WORLDEDIT_FAST_UNDO_REDO && objectArr[1] == null) {
			if(objectArr[1] == null) {
				signLines = null;
				toolEnabled = Tool.WORLDEDIT_FAST_UNDO_REDO;
				Message.sendMessage(player, "&a&oAktywne narzędzie: " + toolEnabled);
			}
		}
    	e.closeInventory();
	}

	public void openInvnetoryMenu() {
		MenuPage inventoryMenuPage = Papi.Model.getPlayer(player).createMenu(Plugin.getPlugin(), 6, "&cWybierz narzędzie");
		File folder = new File(Plugin.getWorldEditPlayerSchemFolder(player));
		if(!folder.exists()) {
			Message.sendMessage(player, "&c&oBrak schematów");
			return;
		}
		
		inventoryMenuPage.setItem(InventorySlot.valueOf(0), Material.BARRIER, "&cWyłącz narzędzie: " + toolEnabled, new Object[] { null });
		inventoryMenuPage.setItem(InventorySlot.valueOf(1), Material.WOODEN_AXE, "&aWorldEdit Schematic Tool", new Object[] { Tool.WORLDEDIT_SCHEMATIC, null });
		inventoryMenuPage.setItem(InventorySlot.valueOf(2), Material.WOODEN_SHOVEL, "&aWorldEdit Fast Undo and Redo Tool", new Object[] { Tool.WORLDEDIT_FAST_UNDO_REDO, null });
		inventoryMenuPage.setItem(InventorySlot.valueOf(3), Material.ACACIA_SIGN, "&aSignEditor", new Object[] { Tool.SIGN_EDITOR, null });
	    int i=InventorySlot.ROW2_COLUMN1.getValue();
		for (final File fileEntry : folder.listFiles()) {
			inventoryMenuPage.setItem(InventorySlot.valueOf(i), Material.PAPER, fileEntry.getName(), new Object[] { Tool.WORLDEDIT_SCHEMATIC, fileEntry.getName() });
	    	i++;
	    }
		
		if(i==0) {
			Message.sendMessage(player, "&c&oBrak schematów");
			return;
		}
		
		Papi.Model.getPlayer(player).openMenu(inventoryMenuPage);
	}

	public boolean isEnabledTool(Tool tool) {
		if(tool == Tool.WORLDEDIT_SCHEMATIC) {
			return (toolEnabled == Tool.WORLDEDIT_SCHEMATIC && selectedSchemat != null);
		}
		else if(tool == Tool.SIGN_EDITOR) {
			return (toolEnabled == Tool.SIGN_EDITOR);
		}
		else if(tool == Tool.WORLDEDIT_FAST_UNDO_REDO) {
			return (toolEnabled == Tool.WORLDEDIT_FAST_UNDO_REDO);
		}
		return false;
	}

	public String getSelectedSchemat() { return selectedSchemat; }

	public void readSignLines(String[] lines) {
		this.signLines = lines;
	}
	
	public boolean writeSignLines(Sign sign) {
		if(signLines == null)
			return false;
		for(int i=0; i<signLines.length; i++) {
			sign.setLine(i, Message.getColorMessage(signLines[i]));
		}
		sign.update();
		return true;
	}

	public boolean editSignLines(Integer line, String text) {
		if(line == null || signLines == null || signLines.length < (line-1) || line>4||line<1)
			return false;
		
		signLines[line-1] = text;
		return true;
	}

	public String getSignLine(Integer intArg) {
		if(signLines == null || signLines.length < (intArg-1) || intArg>4||intArg<1)
			return "";
		
		return this.signLines[intArg-1];
	}

}
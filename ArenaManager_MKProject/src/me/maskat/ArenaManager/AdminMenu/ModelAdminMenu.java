package me.maskat.ArenaManager.AdminMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.maskat.ArenaManager.Plugin;
import me.maskat.ArenaManager.Models.ArenesModel;
import me.maskat.ArenaManager.Models.ModelArena;
import me.maskat.ArenaManager.Models.ModelArenaObject;
import me.maskat.ArenaManager.Models.ModelArenaObjectsGroup;
import me.maskat.ArenaManager.Models.ModelArenaSpawn;
import me.maskat.ArenaManager.Models.ModelArenaTeam;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.MenuInventory.InventorySlot;
import mkproject.maskat.Papi.MenuInventory.MenuPage;
import mkproject.maskat.Papi.MenuInventory.PapiMenuInventoryClickEvent;
import mkproject.maskat.Papi.Model.PapiListenChatEvent;
import mkproject.maskat.Papi.Utils.Message;

public class ModelAdminMenu {
	Player player;
	
	private enum Page {
		ARENES_LIST,
		ARENA_MANAGER,
		TEAM_MANAGER,
		SPAWN_MANAGER,
		OBJ_GROUP_MANAGER,
		OBJECT_MANAGER,
	}
	private enum Listen {
		ADD_ARENA_NAME,
		ADD_TEAM_NAME,
		ADD_SPAWN_NAME,
		ADD_OBJ_GROUP_NAME,
		ADD_OBJECT_NAME,
		CHANGE_ARENA_NAME,
		CHANGE_ARENA_DESC,
		CHANGE_ARENA_TYPE,
		CHANGE_ARENA_ICON,
		CHANGE_ARENA_WORLD,
		CHANGE_TEAM_NAME,
		CHANGE_TEAM_MAX_PLAYERS,
		CHANGE_TEAM_TYPE,
		CHANGE_TEAM_ICON,
		CHANGE_OBJ_GROUP_NAME,
		CHANGE_OBJ_GROUP_TYPE,
	}
	private enum SlotOption {
		ADD_ARENA,
		ADD_TEAM,
		ADD_SPAWN,
		ADD_OBJ_GROUP,
		ADD_OBJECT,
		DEL_ARENA,
		DEL_TEAM,
		DEL_SPAWN,
		DEL_OBJ_GROUP,
		DEL_OBJECT,
		TP_SPAWN,
		TP_OBJECT,
		LIST,
		LIST_TEAMS,
		LIST_OBJ_GROUP,
		LIST_OBJECTS,
		BACK,
		PAGE_PREV,
		PAGE_NEXT,
		CHANGE_ENABLE,
		CHANGE_NAME,
		CHANGE_DESC,
		CHANGE_TYPE,
		CHANGE_ICON,
		CHANGE_WORLD,
		CHANGE_MAX_PLAYERS,
	}
	
	public ModelAdminMenu(Player player) {
		this.player = player;
	}

	public void openMenuMain() {
		openMenuArenesList();
	}
	
	public class MenuItem {
		private MenuPage menuPage;
		private SlotOption slotOption;
		private ModelArena arena = null;
		private int arenaPage = 1;
		private ModelArenaTeam team = null;
		private ModelArenaSpawn spawn = null;
		private ModelArenaObjectsGroup objGroup = null;
		private ModelArenaObject object = null;
		public MenuItem(MenuPage menuPage, SlotOption slotOption) {
			this.menuPage = menuPage;
			this.slotOption = slotOption;
		}
		public MenuItem(MenuPage menuPage, SlotOption slotOption, ModelArena arena, int arenaPage) {
			this.menuPage = menuPage;
			this.slotOption = slotOption;
			this.arena = arena;
			this.arenaPage = arenaPage;
		}
		public MenuItem(MenuPage menuPage, SlotOption slotOption, ModelArena arena, ModelArenaTeam team) {
			this.menuPage = menuPage;
			this.slotOption = slotOption;
			this.arena = arena;
			this.team = team;
		}
		public MenuItem(MenuPage menuPage, SlotOption slotOption, ModelArena arena, ModelArenaObjectsGroup objGroup) {
			this.menuPage = menuPage;
			this.slotOption = slotOption;
			this.arena = arena;
			this.objGroup = objGroup;
		}
		public MenuItem(MenuPage menuPage, SlotOption slotOption, ModelArena arena, ModelArenaObjectsGroup objGroup, ModelArenaObject object) {
			this.menuPage = menuPage;
			this.slotOption = slotOption;
			this.arena = arena;
			this.objGroup = objGroup;
			this.object = object;
		}
		public MenuItem(MenuPage menuPage, SlotOption slotOption, ModelArena arena, ModelArenaTeam team, ModelArenaSpawn spawn) {
			this.menuPage = menuPage;
			this.slotOption = slotOption;
			this.arena = arena;
			this.team = team;
			this.spawn = spawn;
		}
		public MenuPage getMenuPage() { return menuPage; }
		public SlotOption getSlotOption() { return slotOption; }
		public ModelArena getArenaModel() { return arena; }
		public int getArenaPage() { return arenaPage; }
		public ModelArenaTeam getArenaTeamModel() { return team; }
		public ModelArenaSpawn getArenaSpawnModel() { return spawn; }
		public ModelArenaObjectsGroup getArenaObjectsGroupModel() { return objGroup; }
		public ModelArenaObject getArenaObjectModel() { return object; }
	}
	
	public void openMenuArenesList() {
		MenuPage menuPage = Papi.Model.getPlayer(player).createMenu(Plugin.getPlugin(), 6, "*** Arena Manager ***", Page.ARENES_LIST);
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.NETHER_STAR, "&2Add &carena", new MenuItem(menuPage, SlotOption.ADD_ARENA));
		menuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.ENCHANTED_BOOK, "&aPlugin by &bSkyidea.pl");
		
	    int i=InventorySlot.ROW2_COLUMN1.getValue();
		for (Map.Entry<Integer, ModelArena> entry : ArenesModel.getArenesMap().entrySet()) {
			menuPage.setItem(InventorySlot.valueOf(i), entry.getValue().getIcon(),
					"&fArena: &c"+entry.getValue().getName()+" &8[ID:"+entry.getKey()+"]"
					+"\n&fEnabled: &7" + entry.getValue().isEnabled()
					+"\n&fDescription: &7" + entry.getValue().getDescription()
					+"\n&fType: &7" + entry.getValue().getType()
					+"\n&fWorld: &7" + entry.getValue().getWorld()
					+"\n&fTeams: &a" + entry.getValue().getTeamsMap().size()
					+"\n&fObjects Groups: &b" + entry.getValue().getObjectsGroupsMap().size(), new MenuItem(menuPage, SlotOption.LIST, entry.getValue(), 1));
	    	i++;
		}
		
		Papi.Model.getPlayer(player).openMenu(menuPage);
	}
	
	public void openMenuArenaManager(ModelArena arena, int arenaPage) {
		MenuPage menuPage = Papi.Model.getPlayer(player).createMenu(Plugin.getPlugin(), 6, "Arena: " + arena.getName(), Page.ARENA_MANAGER);
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.BOOK,
				"&cArena: &f"+arena.getName()+" &8[ID:"+arena.getId()+"]"
				+"\n&fEnabled: &7" + arena.isEnabled()
				+"\n&cDescription: &7"+arena.getDescription()
				+"\n&cType: &7"+arena.getType()
				+"\n&cWorld: &7"+arena.getWorld()
				+"\n&cTeams: &7" + arena.getTeamsMap().size()
				+"\n&cObjects Groups: &7" + arena.getObjectsGroupsMap().size()
				);

		menuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.NETHER_STAR, "&2Add &ateam", new MenuItem(menuPage, SlotOption.ADD_TEAM, arena, arenaPage));
		menuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.NETHER_STAR, "&2Add &eobject group", new MenuItem(menuPage, SlotOption.ADD_OBJ_GROUP, arena, arenaPage));
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.REDSTONE, "&4Delete &carena", new MenuItem(menuPage, SlotOption.DEL_ARENA, arena, arenaPage));
		menuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.ENDER_PEARL, "&7Back", new MenuItem(menuPage, SlotOption.BACK, arena, arenaPage));
		
		menuPage.setItem(InventorySlot.ROW2_COLUMN1, arena.isEnabled() ? Material.SLIME_BALL : Material.RED_DYE, arena.isEnabled() ? "&aArena is enabled\n&7Click for disable" : "&cArena is disabled\n&7Click for enable", new MenuItem(menuPage, SlotOption.CHANGE_ENABLE, arena, arenaPage));
		menuPage.setItem(InventorySlot.ROW2_COLUMN2, arena.getIcon(), "&6Change &carena&6 icon", new MenuItem(menuPage, SlotOption.CHANGE_ICON, arena, arenaPage));
		menuPage.setItem(InventorySlot.ROW2_COLUMN3, Material.WRITABLE_BOOK, "&6Change &carena&6 name", new MenuItem(menuPage, SlotOption.CHANGE_NAME, arena, arenaPage));
		menuPage.setItem(InventorySlot.ROW2_COLUMN4, Material.WRITABLE_BOOK, "&6Change &carena&6 description", new MenuItem(menuPage, SlotOption.CHANGE_DESC, arena, arenaPage));
		menuPage.setItem(InventorySlot.ROW2_COLUMN5, Material.TNT, "&6Change &carena&6 type", new MenuItem(menuPage, SlotOption.CHANGE_TYPE, arena, arenaPage));
		menuPage.setItem(InventorySlot.ROW2_COLUMN6, Material.DIRT, "&6Change &carena&6 world", new MenuItem(menuPage, SlotOption.CHANGE_WORLD, arena, arenaPage));
		
		if(arenaPage>1)
			menuPage.setItem(InventorySlot.ROW2_COLUMN8, Material.MAP, "&7Previous page", new MenuItem(menuPage, SlotOption.PAGE_PREV, arena, arenaPage));
		if((arenaPage*18) < arena.getTeamsMap().size() && (arenaPage*18) < arena.getObjectsGroupsMap().size())
			menuPage.setItem(InventorySlot.ROW2_COLUMN9, Material.MAP, "&7Next page", new MenuItem(menuPage, SlotOption.PAGE_NEXT, arena, arenaPage));
		
	    int i=InventorySlot.ROW3_COLUMN1.getValue();
		for (Map.Entry<Integer, ModelArenaTeam> entry : arena.getTeams(arenaPage,18).entrySet()) {
			menuPage.setItem(InventorySlot.valueOf(i), entry.getValue().getIcon(), 
					"&fTeam: &a" + entry.getValue().getName() + " &8[ID:"+entry.getValue().getId() + "]"
					+"\n&fMax Players: &a" + entry.getValue().getMaxPlayers()
					+"\n&fType: &a" + entry.getValue().getType()
					+"\n&fSpawns: &d" + entry.getValue().getSpawnsMap().size()
					, new MenuItem(menuPage, SlotOption.LIST_TEAMS, arena, entry.getValue()));
	    	i++;
		}
		//menuPage.setItem(InventorySlot.valueOf(i), Material.BEDROCK, "");
		//i++;
		i=InventorySlot.ROW5_COLUMN1.getValue();
		for (Map.Entry<Integer, ModelArenaObjectsGroup> entry : arena.getObjectsGroups(arenaPage,18).entrySet()) {
			menuPage.setItem(InventorySlot.valueOf(i), Material.PAPER, 
					"&fObjects Group: &e" + entry.getValue().getName() + " &8[ID:"+entry.getValue().getId() + "]"
					+"\n&fType: &e" + entry.getValue().getType()
					, new MenuItem(menuPage, SlotOption.LIST_OBJ_GROUP, arena, entry.getValue()));
	    	i++;
		}
		
		Papi.Model.getPlayer(player).openMenu(menuPage);
	}
	
	public void openMenuObjectsGroupManager(ModelArena arena, ModelArenaObjectsGroup objGroup) {
		MenuPage menuPage = Papi.Model.getPlayer(player).createMenu(Plugin.getPlugin(), 6, "Objects Group: " + objGroup.getName(), Page.OBJ_GROUP_MANAGER);
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.BOOK,
				"&cArena: &f"+arena.getName()+" &8[ID:"+arena.getId()+"]"
				+"\n&fEnabled: &7" + arena.isEnabled()
				+"\n&cDescription: &7"+arena.getDescription()
				+"\n&cType: &7"+arena.getType()
				+"\n&cWorld: &7"+arena.getWorld()
				+"\n&cTeams: &7" + arena.getTeamsMap().size()
				+"\n&cObjects Groups: &7" + arena.getObjectsGroupsMap().size()
				+"\n&eObjects Group: &f" + arena.getObjectsGroupsMap().size()
				+"\n&eType: &7"+objGroup.getType()
				+"\n&eObjects: &7" + objGroup.getObjectsMap().size()
				);
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.WRITABLE_BOOK, "&6Change &eobjects group&6 name", new MenuItem(menuPage, SlotOption.CHANGE_NAME, arena, objGroup));
		menuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.TNT, "&6Change &eobjects group&6 type", new MenuItem(menuPage, SlotOption.CHANGE_TYPE, arena, objGroup));
		menuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.NETHER_STAR, "&2Add &bobject", new MenuItem(menuPage, SlotOption.ADD_OBJECT, arena, objGroup));
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.REDSTONE, "&4Delete &eobjects group", new MenuItem(menuPage, SlotOption.DEL_OBJ_GROUP, arena, objGroup));
		menuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.ENDER_PEARL, "&7Back", new MenuItem(menuPage, SlotOption.BACK, arena, objGroup));
		
	    int i=InventorySlot.ROW2_COLUMN1.getValue();
		for (Map.Entry<Integer, ModelArenaObject> entry : objGroup.getObjectsMap().entrySet()) {
			menuPage.setItem(InventorySlot.valueOf(i), Material.PAPER,
					"&fObject: &b" + entry.getValue().getName()+" &8[ID:"+entry.getValue().getId() + "]"
					+"\n&fLocation: &7"+entry.getValue().getLocation().getWorld().getName()+",X"+entry.getValue().getLocation().getBlockX()+",Y"+entry.getValue().getLocation().getBlockY()+",Z"+entry.getValue().getLocation().getBlockZ()
					, new MenuItem(menuPage, SlotOption.LIST_OBJECTS, arena, objGroup, entry.getValue()));
	    	i++;
		}
		
		Papi.Model.getPlayer(player).openMenu(menuPage);
	}
	
	public void openMenuObjectManager(ModelArena arena, ModelArenaObjectsGroup objGroup, ModelArenaObject object) {
		MenuPage menuPage = Papi.Model.getPlayer(player).createMenu(Plugin.getPlugin(), 6, "Object: " + object.getName(), Page.OBJECT_MANAGER);
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.BOOK,
				"&cArena: &f"+arena.getName()+" &8[ID:"+arena.getId()+"]"
				+"\n&fEnabled: &7" + arena.isEnabled()
				+"\n&cDescription: &7"+arena.getDescription()
				+"\n&cType: &7"+arena.getType()
				+"\n&cWorld: &7"+arena.getWorld()
				+"\n&cTeams: &7" + arena.getTeamsMap().size()
				+"\n&cObjects Groups: &7" + arena.getObjectsGroupsMap().size()
				+"\n&eObjects Group: &f" + arena.getObjectsGroupsMap().size()
				+"\n&eType: &7"+objGroup.getType()
				+"\n&eObjects: &7" + objGroup.getObjectsMap().size()
				+"\n&bObject: &f" + object.getName() + " &8[ID:"+object.getId()+"]"
				+"\n&bLocation: &7" + object.getLocation().getWorld().getName()+",X"+object.getLocation().getBlockX()+",Y"+object.getLocation().getBlockY()+",Z"+object.getLocation().getBlockZ()
				);
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.CHORUS_FRUIT, "&6Teleport to &bobject", new MenuItem(menuPage, SlotOption.TP_OBJECT, arena, objGroup, object));
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.REDSTONE, "&4Delete &bobject", new MenuItem(menuPage, SlotOption.DEL_OBJECT, arena, objGroup, object));
		menuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.ENDER_PEARL, "&7Back", new MenuItem(menuPage, SlotOption.BACK, arena, objGroup, object));
		
	    int i=InventorySlot.ROW2_COLUMN1.getValue();
		for (Map.Entry<Integer, ModelArenaObject> entry : objGroup.getObjectsMap().entrySet()) {
			menuPage.setItem(InventorySlot.valueOf(i), Material.PAPER, 
					"&fObject: &b" + object.getName()+" &8[ID:"+entry.getValue().getId() + "]"
					+"\n&fLocation: &7"+entry.getValue().getLocation().getWorld().getName()+",X"+entry.getValue().getLocation().getBlockX()+",Y"+entry.getValue().getLocation().getBlockY()+",Z"+entry.getValue().getLocation().getBlockZ()
					, new MenuItem(menuPage, SlotOption.LIST_OBJECTS, arena, objGroup, entry.getValue()));
	    	i++;
		}
		
		Papi.Model.getPlayer(player).openMenu(menuPage);
	}
	
	public void openMenuTeamManager(ModelArena arena, ModelArenaTeam team) {
		MenuPage menuPage = Papi.Model.getPlayer(player).createMenu(Plugin.getPlugin(), 6, "Team: " + team.getName(), Page.TEAM_MANAGER);
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.BOOK,
				"&cArena: &f"+arena.getName()+" &8[ID:"+arena.getId()+"]"
				+"\n&fEnabled: &7" + arena.isEnabled()
				+"\n&cDescription: &7"+arena.getDescription()
				+"\n&cType: &7"+arena.getType()
				+"\n&cWorld: &7"+arena.getWorld()
				+"\n&cTeams: &7" + arena.getTeamsMap().size()
				+"\n&cObjects Groups: &7" + arena.getObjectsGroupsMap().size()
				+"\n&aTeam: &f" + team.getName() + " &8[ID:"+team.getId()+"]"
				+"\n&aMax Players: &7"+team.getMaxPlayers()
				+"\n&aType: &7"+team.getType()
				+"\n&dSpawns: &7" + team.getSpawnsMap().size()
				);
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN2, team.getIcon(), "&6Change &ateam&6 icon", new MenuItem(menuPage, SlotOption.CHANGE_ICON, arena, team));
		menuPage.setItem(InventorySlot.ROW1_COLUMN3, Material.WRITABLE_BOOK, "&6Change &ateam&6 name", new MenuItem(menuPage, SlotOption.CHANGE_NAME, arena, team));
		menuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.WRITABLE_BOOK, "&6Change &ateam&6 max players", new MenuItem(menuPage, SlotOption.CHANGE_MAX_PLAYERS, arena, team));
		menuPage.setItem(InventorySlot.ROW1_COLUMN5, Material.TNT, "&6Change &ateam&6 type", new MenuItem(menuPage, SlotOption.CHANGE_TYPE, arena, team));
		menuPage.setItem(InventorySlot.ROW1_COLUMN6, Material.NETHER_STAR, "&2Add &dspawn", new MenuItem(menuPage, SlotOption.ADD_SPAWN, arena, team));
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.REDSTONE, "&4Delete &ateam", new MenuItem(menuPage, SlotOption.DEL_TEAM, arena, team));
		menuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.ENDER_PEARL, "&7Back", new MenuItem(menuPage, SlotOption.BACK, arena, team));
		
	    int i=InventorySlot.ROW2_COLUMN1.getValue();
		for (Map.Entry<Integer, ModelArenaSpawn> entry : team.getSpawnsMap().entrySet()) {
			menuPage.setItem(InventorySlot.valueOf(i), Material.PAPER, 
					"Spawn: &d" + entry.getValue().getName() + " &8[ID:"+entry.getValue().getId()+"]"
					+"\n&fLocation: &7"+entry.getValue().getLocation().getWorld().getName()+",X"+entry.getValue().getLocation().getBlockX()+",Y"+entry.getValue().getLocation().getBlockY()+",Z"+entry.getValue().getLocation().getBlockZ()
					, new MenuItem(menuPage, SlotOption.LIST, arena, team, entry.getValue()));
	    	i++;
		}
		
		Papi.Model.getPlayer(player).openMenu(menuPage);
	}
	
	public void openMenuSpawnManager(ModelArena arena, ModelArenaTeam team, ModelArenaSpawn spawn) {
		MenuPage menuPage = Papi.Model.getPlayer(player).createMenu(Plugin.getPlugin(), 6, "Spawn: " + spawn.getName(), Page.SPAWN_MANAGER);
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN1, Material.BOOK,
				"&cArena: &f"+arena.getName()+" &8[ID:"+arena.getId()+"]"
				+"\n&fEnabled: &7" + arena.isEnabled()
				+"\n&cDescription: &7"+arena.getDescription()
				+"\n&cType: &7"+arena.getType()
				+"\n&cWorld: &7"+arena.getWorld()
				+"\n&cTeams: &7" + arena.getTeamsMap().size()
				+"\n&cObjects Groups: &7" + arena.getObjectsGroupsMap().size()
				+"\n&aTeam: &f" + team.getName() + " &8[ID:"+team.getId()+"]"
				+"\n&aMax Players: &7"+team.getMaxPlayers()
				+"\n&aType: &7"+team.getType()
				+"\n&aSpawns: &7" + team.getSpawnsMap().size()
				+"\n&dSpawn: &f" + spawn.getName() + " &8[ID:"+spawn.getId()+"]"
				+"\n&dName: &7"+spawn.getName()
				+"\n&dLocation: &7" + spawn.getLocation().getWorld().getName()+",X"+spawn.getLocation().getBlockX()+",Y"+spawn.getLocation().getBlockY()+",Z"+spawn.getLocation().getBlockZ()
				);
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN4, Material.CHORUS_FRUIT, "&6Teleport to &dspawn", new MenuItem(menuPage, SlotOption.TP_SPAWN, arena, team, spawn));
		
		menuPage.setItem(InventorySlot.ROW1_COLUMN7, Material.REDSTONE, "&4Delete &dspawn", new MenuItem(menuPage, SlotOption.DEL_SPAWN, arena, team, spawn));
		menuPage.setItem(InventorySlot.ROW1_COLUMN9, Material.ENDER_PEARL, "&7Back", new MenuItem(menuPage, SlotOption.BACK, arena, team, spawn));
		
		int i=InventorySlot.ROW2_COLUMN1.getValue();
		for (Map.Entry<Integer, ModelArenaSpawn> entry : team.getSpawnsMap().entrySet()) {
			menuPage.setItem(InventorySlot.valueOf(i), Material.PAPER, 
					"&fSpawn: &d"+entry.getValue().getName()+" &8[ID:"+entry.getValue().getId()+"]"
					+"\n&fLocation: &7"+entry.getValue().getLocation().getWorld().getName()+",X"+entry.getValue().getLocation().getBlockX()+",Y"+entry.getValue().getLocation().getBlockY()+",Z"+entry.getValue().getLocation().getBlockZ()
					, new MenuItem(menuPage, SlotOption.LIST, arena, team, entry.getValue()));
			i++;
		}
		
		Papi.Model.getPlayer(player).openMenu(menuPage);
	}
	
	public void onPapiMenuInventoryClick(PapiMenuInventoryClickEvent e) {
		if(!e.existSlotStoreObject() || !(e.getSlotStoreObject() instanceof MenuItem))
			return;
		
		if(e.getUniquePageId() == Page.ARENES_LIST)
			onMenuArenesListClick(e, (MenuItem)e.getSlotStoreObject());	
		else if(e.getUniquePageId() == Page.ARENA_MANAGER)
				onMenuArenaManagerClick(e, (MenuItem)e.getSlotStoreObject());
		else {
			if(((MenuItem)e.getSlotStoreObject()).getArenaModel().isAlreadyGamed()) {
				Message.sendMessage(e.getPlayer(), "&c&oArena w grze! Zmiana ustawień niedostępna!");
				return;
			}
			else if(e.getUniquePageId() == Page.OBJ_GROUP_MANAGER)
				onMenuObjectsGroupManagerClick(e, (MenuItem)e.getSlotStoreObject());
			else if(e.getUniquePageId() == Page.OBJECT_MANAGER)
				onMenuObjectManagerClick(e, (MenuItem)e.getSlotStoreObject());
			else if(e.getUniquePageId() == Page.TEAM_MANAGER)
				onMenuTeamManagerClick(e, (MenuItem)e.getSlotStoreObject());
			else if(e.getUniquePageId() == Page.SPAWN_MANAGER)
				onMenuSpawnManagerClick(e, (MenuItem)e.getSlotStoreObject());
		}
		
    	//e.closeMenu();
	}

	private void onMenuArenesListClick(PapiMenuInventoryClickEvent e, MenuItem menuItem) {
		if(menuItem.getSlotOption() == SlotOption.ADD_ARENA) {
			Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.ADD_ARENA_NAME, menuItem);
			Message.sendMessage(player, "&b&oWpisz nazwę areny w chat");
			e.closeInventory();
		}
		else if(menuItem.getSlotOption() == SlotOption.LIST) {
			this.openMenuArenaManager(menuItem.getArenaModel(), 1);
		}
	}
	
	private void onMenuArenaManagerClick(PapiMenuInventoryClickEvent e, MenuItem menuItem) {
//		if(menuItem.getSlotOption() == SlotOption.ADD_ARENA) {
//			Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.ADD_ARENA_NAME, menuItem);
//			Message.sendMessage(player, "&bWpisz nazwę areny w chat");
//			e.closeInventory();
//		}
		if(menuItem.getSlotOption() == SlotOption.CHANGE_ENABLE) {
			if(!menuItem.getArenaModel().existType())
				Message.sendMessage(player, "&c&oWpierw ustaw typ areny!");
			else if(!ArenesModel.existArenaType(menuItem.getArenaModel().getType()))
				Message.sendMessage(player, "&c&oUstawiony typ areny nie istnieje! Zmien typ areny lub wgraj plugin, który zarządza tym trybem");
			else if(menuItem.getArenaModel().setEnabled(!menuItem.getArenaModel().isEnabled()))
				Message.sendMessage(player, "&a&oZmieniono status areny na &e&o" + menuItem.getArenaModel().isEnabled());
			else
				Message.sendMessage(player, "&c&oBłąd podczas zmiany statusu areny");
			this.openMenuArenaManager(menuItem.getArenaModel(), menuItem.getArenaPage());
		}
		else
		{
			if(((MenuItem)e.getSlotStoreObject()).getArenaModel().isAlreadyGamed()) {
				Message.sendMessage(e.getPlayer(), "&c&oArena w grze! Zmiana ustawień niedostępna!");
				return;
			}
			
			if(menuItem.getSlotOption() == SlotOption.DEL_ARENA) {
				if(ArenesModel.removeArena(menuItem.getArenaModel().getId())) {
					Message.sendMessage(player, "&a&oUsunięto arenę");
					this.openMenuArenesList();
				}
				else
					Message.sendMessage(player, "&c&oBłąd podczas usuwania areny");
			}
			else if(menuItem.getSlotOption() == SlotOption.ADD_TEAM) {
				Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.ADD_TEAM_NAME, menuItem);
				Message.sendMessage(player, "&bWpisz nazwę teamu w chat");
				e.closeInventory();
			}
			else if(menuItem.getSlotOption() == SlotOption.ADD_OBJ_GROUP) {
				Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.ADD_OBJ_GROUP_NAME, menuItem);
				Message.sendMessage(player, "&bWpisz nazwę grupy obiektów w chat");
				e.closeInventory();
			}
			
			else if(menuItem.getSlotOption() == SlotOption.CHANGE_ICON) {
				Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.CHANGE_ARENA_ICON, menuItem);
				Message.sendMessage(player, "&bWpisz nazwę nowej ikony (przedmiotu) areny '"+menuItem.getArenaModel().getName()+"' w chat");
				e.closeInventory();
			}
			else if(menuItem.getSlotOption() == SlotOption.CHANGE_NAME) {
				Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.CHANGE_ARENA_NAME, menuItem);
				Message.sendMessage(player, "&bWpisz nową nazwę areny '"+menuItem.getArenaModel().getName()+"' w chat");
				e.closeInventory();
			}
			else if(menuItem.getSlotOption() == SlotOption.CHANGE_DESC) {
				Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.CHANGE_ARENA_DESC, menuItem);
				Message.sendMessage(player, "&bWpisz nowy opis areny '"+menuItem.getArenaModel().getName()+"' w chat");
				e.closeInventory();
			}
			else if(menuItem.getSlotOption() == SlotOption.CHANGE_TYPE) {
				Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.CHANGE_ARENA_TYPE, menuItem);
				Message.sendMessage(player, "&6Dostępne typy aren: &e"+ArenesModel.getArenesTypes());
				Message.sendMessage(player, "&bWpisz nowy typ areny '"+menuItem.getArenaModel().getName()+"' w chat");
				e.closeInventory();
			}
			else if(menuItem.getSlotOption() == SlotOption.CHANGE_WORLD) {
				Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.CHANGE_ARENA_WORLD, menuItem);
				
				List<String> worldsNameList = new ArrayList<>();
				for(World world : Bukkit.getWorlds()) {
					worldsNameList.add(world.getName());
				}
				
				Message.sendMessage(player, "&6Dostępne światy: &e"+worldsNameList);
				Message.sendMessage(player, "&bWpisz nowy świat areny '"+menuItem.getArenaModel().getName()+"' w chat");
				e.closeInventory();
			}
			else if(menuItem.getSlotOption() == SlotOption.PAGE_PREV) {
				this.openMenuArenaManager(menuItem.getArenaModel(), menuItem.getArenaPage()-1);
			}
			else if(menuItem.getSlotOption() == SlotOption.PAGE_NEXT) {
				this.openMenuArenaManager(menuItem.getArenaModel(), menuItem.getArenaPage()+1);
			}
			else if(menuItem.getSlotOption() == SlotOption.LIST_TEAMS) {
				this.openMenuTeamManager(menuItem.getArenaModel(), menuItem.getArenaTeamModel());
			}
			else if(menuItem.getSlotOption() == SlotOption.LIST_OBJ_GROUP) {
				this.openMenuObjectsGroupManager(menuItem.getArenaModel(), menuItem.getArenaObjectsGroupModel());
			}
			else if(menuItem.getSlotOption() == SlotOption.BACK) {
				this.openMenuArenesList();
			}
		}
	}

	private void onMenuObjectsGroupManagerClick(PapiMenuInventoryClickEvent e, MenuItem menuItem) {
		if(menuItem.getSlotOption() == SlotOption.DEL_OBJ_GROUP) {
			if(menuItem.getArenaModel().removeObjectsGroup(menuItem.getArenaObjectsGroupModel().getId())) {
				Message.sendMessage(player, "&a&oUsunięto grupe obiektów");
				this.openMenuArenaManager(menuItem.getArenaModel(), 1);
			}
			else
				Message.sendMessage(player, "&c&oBłąd podczas usuwania grupy obiektów");
		}
		else if(menuItem.getSlotOption() == SlotOption.CHANGE_NAME) {
			Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.CHANGE_OBJ_GROUP_NAME, menuItem);
			Message.sendMessage(player, "&bWpisz nową nazwę grupy obiektów w chat");
			e.closeInventory();
		}
		else if(menuItem.getSlotOption() == SlotOption.CHANGE_TYPE) {
			if(!menuItem.getArenaModel().existType())
			{
				Message.sendMessage(player, "&cUstaw wpierw typ dla areny!");
				return;
			}
			Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.CHANGE_OBJ_GROUP_TYPE, menuItem);
			Message.sendMessage(player, "&6Dostępne typy grup obiektów: &e"+ArenesModel.getArenaType(menuItem.getArenaModel().getType()).getObjectsGroupTypes());
			Message.sendMessage(player, "&bWpisz nowy typ grupy obiektów '"+menuItem.getArenaObjectsGroupModel().getName()+"' w chat");
			e.closeInventory();
		}
		else if(menuItem.getSlotOption() == SlotOption.ADD_OBJECT) {
			Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.ADD_OBJECT_NAME, menuItem, false);
			Message.sendMessage(player, "&bWpisz nazwę obiektu w chat");
			e.closeInventory();
		}
		else if(menuItem.getSlotOption() == SlotOption.LIST_OBJECTS) {
			this.openMenuObjectManager(menuItem.getArenaModel(), menuItem.getArenaObjectsGroupModel(), menuItem.getArenaObjectModel());
		}
		else if(menuItem.getSlotOption() == SlotOption.BACK) {
			this.openMenuArenaManager(menuItem.getArenaModel(), menuItem.getArenaPage());
		}
	}
	
	private void onMenuObjectManagerClick(PapiMenuInventoryClickEvent e, MenuItem menuItem) {
		if(menuItem.getSlotOption() == SlotOption.DEL_OBJECT) {
			if(menuItem.getArenaObjectsGroupModel().removeObject(menuItem.getArenaObjectModel().getId())) {
				Message.sendMessage(player, "&a&oUsunięto obiekt");
				this.openMenuObjectsGroupManager(menuItem.getArenaModel(), menuItem.getArenaObjectsGroupModel());
			}
			else
				Message.sendMessage(player, "&c&oBłąd podczas usuwania obiektu");
		}
		else if(menuItem.getSlotOption() == SlotOption.ADD_OBJECT) {
			Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.ADD_OBJECT_NAME, menuItem, false);
			Message.sendMessage(player, "&bWpisz nazwę obiektu w chat");
			e.closeInventory();
		}
		else if(menuItem.getSlotOption() == SlotOption.TP_OBJECT) {
			player.teleport(menuItem.getArenaObjectModel().getLocation());
			Message.sendMessage(player, "&a&oTeleportowano");
			this.openMenuObjectManager(menuItem.getArenaModel(), menuItem.getArenaObjectsGroupModel(), menuItem.getArenaObjectModel());
		}
		else if(menuItem.getSlotOption() == SlotOption.LIST_OBJECTS) {
			this.openMenuObjectManager(menuItem.getArenaModel(), menuItem.getArenaObjectsGroupModel(), menuItem.getArenaObjectModel());
		}
		else if(menuItem.getSlotOption() == SlotOption.BACK) {
			this.openMenuObjectsGroupManager(menuItem.getArenaModel(), menuItem.getArenaObjectsGroupModel());
		}
	}
	
	private void onMenuTeamManagerClick(PapiMenuInventoryClickEvent e, MenuItem menuItem) {
		if(menuItem.getSlotOption() == SlotOption.DEL_TEAM) {
			if(menuItem.getArenaModel().removeTeam(menuItem.getArenaTeamModel().getId())) {
				Message.sendMessage(player, "&a&oUsunięto team");
				this.openMenuArenaManager(menuItem.getArenaModel(), 1);
			}
			else
				Message.sendMessage(player, "&c&oBłąd podczas usuwania teamu");
		}
		else if(menuItem.getSlotOption() == SlotOption.CHANGE_ICON) {
			Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.CHANGE_TEAM_ICON, menuItem);
			Message.sendMessage(player, "&bWpisz nazwę nowej ikony (przedmiotu) dla teamu '"+menuItem.getArenaTeamModel().getName()+"' w chat");
			e.closeInventory();
		}
		else if(menuItem.getSlotOption() == SlotOption.CHANGE_NAME) {
			Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.CHANGE_TEAM_NAME, menuItem);
			Message.sendMessage(player, "&bWpisz nową nazwę teamu w chat");
			e.closeInventory();
		}
		else if(menuItem.getSlotOption() == SlotOption.CHANGE_MAX_PLAYERS) {
			Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.CHANGE_TEAM_MAX_PLAYERS, menuItem);
			Message.sendMessage(player, "&bWpisz maksymalną ilośc graczy, którzy mogą dołączych do teamu w chat");
			e.closeInventory();
		}
		else if(menuItem.getSlotOption() == SlotOption.CHANGE_TYPE) {
			if(!menuItem.getArenaModel().existType())
			{
				Message.sendMessage(player, "&cUstaw wpierw typ dla areny!");
				return;
			}
			Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.CHANGE_TEAM_TYPE, menuItem);
			Message.sendMessage(player, "&6Dostępne typy teamów: &e"+ArenesModel.getArenaType(menuItem.getArenaModel().getType()).getTeamTypes());
			Message.sendMessage(player, "&bWpisz nowy typ teamu '"+menuItem.getArenaTeamModel().getName()+"' w chat");
			e.closeInventory();
		}
		else if(menuItem.getSlotOption() == SlotOption.ADD_SPAWN) {
			Papi.Model.getPlayer(player).listenChat(Plugin.getPlugin(), Listen.ADD_SPAWN_NAME, menuItem, false);
			Message.sendMessage(player, "&bWpisz nazwę spawnu w chat");
			e.closeInventory();
		}
		else if(menuItem.getSlotOption() == SlotOption.LIST) {
			this.openMenuSpawnManager(menuItem.getArenaModel(), menuItem.getArenaTeamModel(), menuItem.getArenaSpawnModel());
		}
		else if(menuItem.getSlotOption() == SlotOption.BACK) {
			this.openMenuArenaManager(menuItem.getArenaModel(), menuItem.getArenaPage());
		}
	}
	
	private void onMenuSpawnManagerClick(PapiMenuInventoryClickEvent e, MenuItem menuItem) {
		if(menuItem.getSlotOption() == SlotOption.DEL_SPAWN) {
			if(menuItem.getArenaTeamModel().removeSpawn(menuItem.getArenaSpawnModel().getId())) {
				Message.sendMessage(player, "&a&oUsunięto spawn");
				this.openMenuTeamManager(menuItem.getArenaModel(), menuItem.getArenaTeamModel());
			}
			else
				Message.sendMessage(player, "&c&oBłąd podczas usuwania spawnu");
		}
		else if(menuItem.getSlotOption() == SlotOption.TP_SPAWN) {
			player.teleport(menuItem.getArenaSpawnModel().getLocation());
			Message.sendMessage(player, "&a&oTeleportowano");
			this.openMenuSpawnManager(menuItem.getArenaModel(), menuItem.getArenaTeamModel(), menuItem.getArenaSpawnModel());
		}
		else if(menuItem.getSlotOption() == SlotOption.LIST) {
			this.openMenuSpawnManager(menuItem.getArenaModel(), menuItem.getArenaTeamModel(), menuItem.getArenaSpawnModel());
		}
		else if(menuItem.getSlotOption() == SlotOption.BACK) {
			this.openMenuTeamManager(menuItem.getArenaModel(), menuItem.getArenaTeamModel());
		}
	}

	public void onPapiListenChat(PapiListenChatEvent e) {
		MenuItem menuItem = (MenuItem)e.getStoreObject();
		
		if(e.getEventUniqueId() == Listen.ADD_ARENA_NAME)
		{
			if(e.isNoMessage())
				Message.sendMessage(player, "&c&oAnulowano");
			else
			{
				if(ArenesModel.addArena(e.getMessage()))
					Message.sendMessage(player, "&a&oUworzono arene o nazwie &e&o" + e.getMessage());
				else
					Message.sendMessage(player, "&c&oBłąd tworzenia areny");
			}
			this.openMenuArenesList();
		}
		else if(e.getEventUniqueId() == Listen.ADD_TEAM_NAME)
		{
			if(e.isNoMessage())
				Message.sendMessage(player, "&c&oAnulowano");
			else
			{
				if(menuItem.getArenaModel().addTeam(e.getMessage()))
					Message.sendMessage(player, "&a&oUworzono team o nazwie &e&o" + e.getMessage() + "&a&o dla areny &e&o" + menuItem.getArenaModel().getName());
				else
					Message.sendMessage(player, "&c&oBłąd tworzenia teamu dla areny &e&o" + menuItem.getArenaModel().getName());
			}
			this.openMenuArenaManager(menuItem.getArenaModel(), menuItem.getArenaPage());
		}
		else if(e.getEventUniqueId() == Listen.ADD_OBJ_GROUP_NAME)
		{
			if(e.isNoMessage())
				Message.sendMessage(player, "&c&oAnulowano");
			else
			{
				if(menuItem.getArenaModel().addObjectsGroup(e.getMessage()))
					Message.sendMessage(player, "&a&oUworzono grupe obiektów o nazwie &e&o" + e.getMessage() + "&a&o dla areny &e&o" + menuItem.getArenaModel().getName());
				else
					Message.sendMessage(player, "&c&oBłąd tworzenia grupy obiektów dla areny &e&o" + menuItem.getArenaModel().getName());
			}
			this.openMenuArenaManager(menuItem.getArenaModel(), menuItem.getArenaPage());
		}
		else if(e.getEventUniqueId() == Listen.ADD_OBJECT_NAME)
		{
			if(e.isNoMessage())
				Message.sendMessage(player, "&c&oAnulowano");
			else 
			{
				if(menuItem.getArenaObjectsGroupModel().addObject(e.getMessage(), player.getLocation()))
					Message.sendMessage(player, "&a&oUworzono obiekt o nazwie &e&o" + e.getMessage() + "&a&o dla grupy obiektów &e&o" + menuItem.getArenaObjectsGroupModel().getName());
				else
					Message.sendMessage(player, "&c&oBłąd tworzenia obiektu dla areny &e&o" + menuItem.getArenaModel().getName());
			}
			this.openMenuObjectsGroupManager(menuItem.getArenaModel(), menuItem.getArenaObjectsGroupModel());
		}
		else if(e.getEventUniqueId() == Listen.ADD_SPAWN_NAME)
		{
			if(e.isNoMessage())
				Message.sendMessage(player, "&c&oAnulowano");
			else 
			{
				if(menuItem.getArenaTeamModel().addSpawn(e.getMessage(), player.getLocation()))
					Message.sendMessage(player, "&a&oUworzono spawn o nazwie &e&o" + e.getMessage() + "&a&o dla teamu &e&o"+menuItem.getArenaTeamModel().getName()+"&a&o i areny &e&o" + menuItem.getArenaModel().getName());
				else
					Message.sendMessage(player, "&c&oBłąd tworzenia teamu dla areny &e&o" + menuItem.getArenaModel().getName());
			}
			this.openMenuTeamManager(menuItem.getArenaModel(),menuItem.getArenaTeamModel());
		}
		else if(e.getEventUniqueId() == Listen.CHANGE_ARENA_ICON)
		{
			if(e.isNoMessage())
				Message.sendMessage(player, "&c&oAnulowano");
			else 
			{
				if(Material.getMaterial(e.getMessage().toUpperCase()) == null)
					Message.sendMessage(player, "&c&oWybrany materiał nie istnieje!");
				else
				{
					if(menuItem.getArenaModel().setIcon(e.getMessage()))
						Message.sendMessage(player, "&a&oZmieniono ikone areny na &e&o" + e.getMessage());
					else
						Message.sendMessage(player, "&c&oBłąd podczas zmiany ikony areny");
				}
			}
			this.openMenuArenaManager(menuItem.getArenaModel(), 1);
		}
		else if(e.getEventUniqueId() == Listen.CHANGE_ARENA_NAME)
		{
			if(e.isNoMessage())
				Message.sendMessage(player, "&c&oAnulowano");
			else
			{
				if(menuItem.getArenaModel().setName(e.getMessage()))
					Message.sendMessage(player, "&a&oZmieniono nazwę areny na &e&o" + e.getMessage());
				else
					Message.sendMessage(player, "&c&oBłąd podczas zmiany nazwy areny");
			}
			this.openMenuArenaManager(menuItem.getArenaModel(), 1);
		}
		else if(e.getEventUniqueId() == Listen.CHANGE_ARENA_DESC)
		{
			if(e.isNoMessage())
				Message.sendMessage(player, "&c&oAnulowano");
			else
			{
				if(menuItem.getArenaModel().setDescription(e.getMessage()))
					Message.sendMessage(player, "&a&oZmieniono opis areny na &e&o" + e.getMessage());
				else
					Message.sendMessage(player, "&c&oBłąd podczas zmiany opisu areny");
			}
			this.openMenuArenaManager(menuItem.getArenaModel(), 1);
		}
		else if(e.getEventUniqueId() == Listen.CHANGE_ARENA_TYPE)
		{
			if(e.isNoMessage())
				Message.sendMessage(player, "&c&oAnulowano");
			else
			{
				if(menuItem.getArenaModel().setType(e.getMessage()))
					Message.sendMessage(player, "&a&oZmieniono typ areny na &e&o" + e.getMessage());
				else
					Message.sendMessage(player, "&c&oBłąd podczas zmiany typu areny");
			}
			this.openMenuArenaManager(menuItem.getArenaModel(), 1);
		}
		else if(e.getEventUniqueId() == Listen.CHANGE_ARENA_WORLD)
		{
			if(e.isNoMessage())
				Message.sendMessage(player, "&c&oAnulowano");
			else
			{
				if(menuItem.getArenaModel().setWorld(e.getMessage()))
					Message.sendMessage(player, "&a&oZmieniono świat areny na &e&o" + e.getMessage());
				else
					Message.sendMessage(player, "&c&oBłąd podczas zmiany typu areny");
			}
			this.openMenuArenaManager(menuItem.getArenaModel(), 1);
		}
		else if(e.getEventUniqueId() == Listen.CHANGE_TEAM_ICON)
		{
			if(e.isNoMessage())
				Message.sendMessage(player, "&c&oAnulowano");
			else
			{
				if(Material.getMaterial(e.getMessage().toUpperCase()) == null)
					Message.sendMessage(player, "&c&oWybrany materiał nie istnieje!");
				else
				{
					if(menuItem.getArenaTeamModel().setIcon(e.getMessage()))
						Message.sendMessage(player, "&a&oZmieniono ikone teamu na &e&o" + e.getMessage());
					else
						Message.sendMessage(player, "&c&oBłąd podczas zmiany ikony teamu");
				}
			}
			this.openMenuTeamManager(menuItem.getArenaModel(), menuItem.getArenaTeamModel());
		}
		else if(e.getEventUniqueId() == Listen.CHANGE_TEAM_NAME)
		{
			if(e.isNoMessage())
				Message.sendMessage(player, "&c&oAnulowano");
			else
			{
				if(menuItem.getArenaTeamModel().setName(e.getMessage()))
					Message.sendMessage(player, "&a&oZmieniono nazwę teamu na &e&o" + e.getMessage());
				else
					Message.sendMessage(player, "&c&oBłąd podczas zmiany nazwy teamu");
			}
			this.openMenuTeamManager(menuItem.getArenaModel(), menuItem.getArenaTeamModel());
		}
		else if(e.getEventUniqueId() == Listen.CHANGE_TEAM_MAX_PLAYERS)
		{
			if(e.isNoMessage())
				Message.sendMessage(player, "&c&oAnulowano");
			else
			{
				if(!Papi.Function.isNumeric(e.getMessage()))
					Message.sendMessage(player, "&c&oMożesz użyć tylko cyfr!");
				else
				{
					if(menuItem.getArenaTeamModel().setMaxPlayers(Integer.parseInt(e.getMessage())))
						Message.sendMessage(player, "&a&oZmieniono maksymalna ilosc graczy w teamie na &e&o" + e.getMessage());
					else
						Message.sendMessage(player, "&c&oBłąd podczas zmiany ilosci graczy w teamie");
				}
			}
			this.openMenuTeamManager(menuItem.getArenaModel(), menuItem.getArenaTeamModel());
		}
		else if(e.getEventUniqueId() == Listen.CHANGE_TEAM_TYPE)
		{
			if(e.isNoMessage())
				Message.sendMessage(player, "&c&oAnulowano");
			else
			{
				if(menuItem.getArenaTeamModel().setType(e.getMessage(), menuItem.getArenaModel().existTeamType(e.getMessage())))
					Message.sendMessage(player, "&a&oZmieniono typ teamu na &e&o" + e.getMessage());
				else
					Message.sendMessage(player, "&c&oBłąd podczas zmiany typu teamu");
			}
			this.openMenuTeamManager(menuItem.getArenaModel(), menuItem.getArenaTeamModel());
		}
		else if(e.getEventUniqueId() == Listen.CHANGE_OBJ_GROUP_NAME)
		{
			if(e.isNoMessage())
				Message.sendMessage(player, "&c&oAnulowano");
			else
			{
				if(menuItem.getArenaObjectsGroupModel().setName(e.getMessage()))
					Message.sendMessage(player, "&a&oZmieniono nazwę grupy obiektów na &e&o" + e.getMessage());
				else
					Message.sendMessage(player, "&c&oBłąd podczas zmiany nazwy grupy obiektów");
			}
			this.openMenuObjectsGroupManager(menuItem.getArenaModel(), menuItem.getArenaObjectsGroupModel());
		}
		else if(e.getEventUniqueId() == Listen.CHANGE_OBJ_GROUP_TYPE)
		{
			if(e.isNoMessage())
				Message.sendMessage(player, "&c&oAnulowano");
			else
			{
				if(menuItem.getArenaObjectsGroupModel().setType(e.getMessage(), menuItem.getArenaModel().existObjectsGroupType(e.getMessage())))
					Message.sendMessage(player, "&a&oZmieniono typ grupy obiektów na &e&o" + e.getMessage());
				else
					Message.sendMessage(player, "&c&oBłąd podczas zmiany typu grupy obiektów");
			}
			this.openMenuObjectsGroupManager(menuItem.getArenaModel(), menuItem.getArenaObjectsGroupModel());
		}
	}

}

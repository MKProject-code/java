package me.maskat.InventoryManager;

import mkproject.maskat.Papi.Papi;

public class Database {
	
	protected static void initialize(Plugin plugin) {
		Inv.TABLE = Config.getString("MySQL.Table.Inventories")+(Papi.DEVELOPER_DATABASE ? "_develop" : "");
		
		if(Papi.DEVELOPER_DATABASE && Papi.DEVELOPER_DATABASE_AUTODELETE)
			Papi.MySQL.deleteTable(Inv.TABLE);
		
		Papi.MySQL.createTable(Inv.TABLE,
					Papi.SQL.createColumnParse(Inv.ID, Papi.SQL.ValType.INT, 16, true, true, true)
				+","+Papi.SQL.createColumnParse(Inv.NAME, Papi.SQL.ValType.VARCHAR, 32, true, false, true)
				+","+Papi.SQL.createColumnParse(Inv.INV_SURVIVAL_CONTENT, Papi.SQL.ValType.TEXT, -1, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_CREATIVE_CONTENT, Papi.SQL.ValType.TEXT, -1, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_ADVENTURE_CONTENT, Papi.SQL.ValType.TEXT, -1, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_SPECTATOR_CONTENT, Papi.SQL.ValType.TEXT, -1, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_SURVIVAL_ENDERCHEST_CONTENT, Papi.SQL.ValType.TEXT, -1, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_CREATIVE_ENDERCHEST_CONTENT, Papi.SQL.ValType.TEXT, -1, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_ADVENTURE_ENDERCHEST_CONTENT, Papi.SQL.ValType.TEXT, -1, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_SPECTATOR_ENDERCHEST_CONTENT, Papi.SQL.ValType.TEXT, -1, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_SURVIVAL_EXP, Papi.SQL.ValType.VARCHAR, 32, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_CREATIVE_EXP, Papi.SQL.ValType.VARCHAR, 32, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_ADVENTURE_EXP, Papi.SQL.ValType.VARCHAR, 32, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_SPECTATOR_EXP, Papi.SQL.ValType.VARCHAR, 32, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_SURVIVAL_HEALTH, Papi.SQL.ValType.DOUBLE, -1, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_SURVIVAL_FOOD, Papi.SQL.ValType.DOUBLE, -1, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_SURVIVAL_SATURATION, Papi.SQL.ValType.DOUBLE, -1, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_SURVIVAL_EXHAUSTION, Papi.SQL.ValType.DOUBLE, -1, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_SURVIVAL_ABSORPTION, Papi.SQL.ValType.DOUBLE, -1, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_SURVIVAL_FIRE_TICKS, Papi.SQL.ValType.DOUBLE, -1, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_SURVIVAL_VELOCITY, Papi.SQL.ValType.TEXT, -1, false)
				+","+Papi.SQL.createColumnParse(Inv.INV_SURVIVAL_POTION_EFFECTS, Papi.SQL.ValType.TEXT, -1, false)
				+","+Papi.SQL.createColumnPrimary(Inv.
						ID)
		);
	}
	
	protected static class Inv {
		public static String TABLE;
		public static final String ID = "userid";
		public static final String NAME = "username";
		public static final String INV_SURVIVAL_CONTENT = "inv_survival_content";
		public static final String INV_CREATIVE_CONTENT = "inv_creative_content";
		public static final String INV_ADVENTURE_CONTENT = "inv_adventure_content";
		public static final String INV_SPECTATOR_CONTENT = "inv_spectator_content";
		public static final String INV_SURVIVAL_ENDERCHEST_CONTENT = "inv_survival_enderchest_content";
		public static final String INV_CREATIVE_ENDERCHEST_CONTENT = "inv_creative_enderchest_content";
		public static final String INV_ADVENTURE_ENDERCHEST_CONTENT = "inv_adventure_enderchest_content";
		public static final String INV_SPECTATOR_ENDERCHEST_CONTENT = "inv_spectator_enderchest_content";
		public static final String INV_SURVIVAL_EXP = "inv_survival_experience";
		public static final String INV_CREATIVE_EXP = "inv_creative_experience";
		public static final String INV_ADVENTURE_EXP = "inv_adventure_experience";
		public static final String INV_SPECTATOR_EXP = "inv_spectator_experience";
		public static final String INV_SURVIVAL_HEALTH = "inv_survival_health";
		public static final String INV_SURVIVAL_FOOD = "inv_survival_food";
		public static final String INV_SURVIVAL_SATURATION = "inv_survival_saturation";
		public static final String INV_SURVIVAL_EXHAUSTION = "inv_survival_exhaustion";
		public static final String INV_SURVIVAL_ABSORPTION = "inv_survival_absorption";
		public static final String INV_SURVIVAL_FIRE_TICKS = "inv_survival_fire_ticks";
		public static final String INV_SURVIVAL_VELOCITY = "inv_survival_velocity";
		public static final String INV_SURVIVAL_POTION_EFFECTS = "inv_survival_potion_effects";
	}
}

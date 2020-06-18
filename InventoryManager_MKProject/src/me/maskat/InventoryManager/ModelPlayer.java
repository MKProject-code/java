package me.maskat.InventoryManager;

import java.io.IOException;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import mkproject.maskat.Papi.Papi;

public class ModelPlayer {
	private Player player;
	
	public ModelPlayer(Player player) {
		this.player = player;
		
		if(!Papi.MySQL.exists(Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE))
			Papi.MySQL.put(Map.of(Database.Inv.NAME,player.getName().toLowerCase()), Database.Inv.TABLE);
	}

	public void saveInventory(GameMode gameMode) {
		String playerInvBase64 = InventorySerializer.inventoryToBase64(player.getInventory());
		String playerSqlColumn = null;
		if(gameMode == GameMode.SURVIVAL)
			playerSqlColumn = Database.Inv.INV_SURVIVAL_CONTENT;
		else if(gameMode == GameMode.CREATIVE)
			playerSqlColumn = Database.Inv.INV_CREATIVE_CONTENT;
		else if(gameMode == GameMode.ADVENTURE)
			playerSqlColumn = Database.Inv.INV_ADVENTURE_CONTENT;
		else if(gameMode == GameMode.SPECTATOR)
			playerSqlColumn = Database.Inv.INV_SPECTATOR_CONTENT;
		
		Papi.MySQL.set(Map.of(playerSqlColumn, playerInvBase64), Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE);
		
		
		String enderInvBase64 = InventorySerializer.inventoryToBase64(player.getEnderChest());
		String enderSqlColumn = null;
		if(gameMode == GameMode.SURVIVAL)
			enderSqlColumn = Database.Inv.INV_SURVIVAL_ENDERCHEST_CONTENT;
		else if(gameMode == GameMode.CREATIVE)
			enderSqlColumn = Database.Inv.INV_CREATIVE_ENDERCHEST_CONTENT;
		else if(gameMode == GameMode.ADVENTURE)
			enderSqlColumn = Database.Inv.INV_ADVENTURE_ENDERCHEST_CONTENT;
		else if(gameMode == GameMode.SPECTATOR)
			enderSqlColumn = Database.Inv.INV_SPECTATOR_ENDERCHEST_CONTENT;
		
		Papi.MySQL.set(Map.of(enderSqlColumn, enderInvBase64), Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE);		
		
		
		String expSqlColumn = null;
		if(gameMode == GameMode.SURVIVAL)
			expSqlColumn = Database.Inv.INV_SURVIVAL_EXP;
		else if(gameMode == GameMode.CREATIVE)
			expSqlColumn = Database.Inv.INV_CREATIVE_EXP;
		else if(gameMode == GameMode.ADVENTURE)
			expSqlColumn = Database.Inv.INV_ADVENTURE_EXP;
		else if(gameMode == GameMode.SPECTATOR)
			expSqlColumn = Database.Inv.INV_SPECTATOR_EXP;
		
		Papi.MySQL.set(Map.of(expSqlColumn, String.valueOf(player.getLevel()) + "," + String.valueOf(player.getExp())), Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE);
	}

//	public void loadInventory(GameMode newGameMode) {
//		String sqlcolumn = null;
//		if(newGameMode == GameMode.SURVIVAL)
//			sqlcolumn = Database.Inv.INV_SURVIVAL_CONTENT;
//		else if(newGameMode == GameMode.CREATIVE)
//			sqlcolumn = Database.Inv.INV_CREATIVE_CONTENT;
//		else if(newGameMode == GameMode.ADVENTURE)
//			sqlcolumn = Database.Inv.INV_ADVENTURE_CONTENT;
//		else if(newGameMode == GameMode.SPECTATOR)
//			sqlcolumn = Database.Inv.INV_SPECTATOR_CONTENT;
//		
//		Object invBase64 = Papi.MySQL.get(sqlcolumn, Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE);
//		
//		List<ItemStack[]> invs = null;
//		if(invBase64 != null)
//		{
//			try {
//				invs = InventorySerializer.playerInventoryFromBase64(invBase64.toString());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		if(invs != null)
//		{
//			player.getInventory().setContents(invs.get(0));
//			player.getInventory().setArmorContents(invs.get(1));
//		}
//		else
//			player.getInventory().clear();
//		
//		player.updateInventory();
//	}
	
	public void loadInventory(GameMode newGameMode) {
		String playerSqlColumn = null;
		if(newGameMode == GameMode.SURVIVAL)
		{
			for(PotionEffect potionEffect : player.getActivePotionEffects())
				player.removePotionEffect(potionEffect.getType());
			
			player.setAbsorptionAmount(0);
			
			playerSqlColumn = Database.Inv.INV_SURVIVAL_CONTENT;
		}
		else if(newGameMode == GameMode.CREATIVE)
			playerSqlColumn = Database.Inv.INV_CREATIVE_CONTENT;
		else if(newGameMode == GameMode.ADVENTURE)
			playerSqlColumn = Database.Inv.INV_ADVENTURE_CONTENT;
		else if(newGameMode == GameMode.SPECTATOR)
			playerSqlColumn = Database.Inv.INV_SPECTATOR_CONTENT;
		
		Object playerInvBase64 = Papi.MySQL.get(playerSqlColumn, Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE);
		
		ItemStack[] playerInv = null;
		if(playerInvBase64 != null)
		{
			try {
				playerInv = InventorySerializer.inventoryFromBase64(playerInvBase64.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(playerInv != null)
			player.getInventory().setContents(playerInv);
		else
			player.getInventory().clear();
		
		player.updateInventory();
		
		
		String enderSqlColumn = null;
		if(newGameMode == GameMode.SURVIVAL)
			enderSqlColumn = Database.Inv.INV_SURVIVAL_ENDERCHEST_CONTENT;
		else if(newGameMode == GameMode.CREATIVE)
			enderSqlColumn = Database.Inv.INV_CREATIVE_ENDERCHEST_CONTENT;
		else if(newGameMode == GameMode.ADVENTURE)
			enderSqlColumn = Database.Inv.INV_ADVENTURE_ENDERCHEST_CONTENT;
		else if(newGameMode == GameMode.SPECTATOR)
			enderSqlColumn = Database.Inv.INV_SPECTATOR_ENDERCHEST_CONTENT;
		
		Object enderInvBase64 = Papi.MySQL.get(enderSqlColumn, Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE);
		
		ItemStack[] enderInv = null;
		if(enderInvBase64 != null)
		{
			try {
				enderInv = InventorySerializer.inventoryFromBase64(enderInvBase64.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(enderInv != null)
			player.getEnderChest().setContents(enderInv);
		else
			player.getEnderChest().clear();
		
		player.closeInventory();
		
		String expSqlColumn = null;
		if(newGameMode == GameMode.SURVIVAL)
			expSqlColumn = Database.Inv.INV_SURVIVAL_EXP;
		else if(newGameMode == GameMode.CREATIVE)
			expSqlColumn = Database.Inv.INV_CREATIVE_EXP;
		else if(newGameMode == GameMode.ADVENTURE)
			expSqlColumn = Database.Inv.INV_ADVENTURE_EXP;
		else if(newGameMode == GameMode.SPECTATOR)
			expSqlColumn = Database.Inv.INV_SPECTATOR_EXP;
		
		player.setLevel(0);
		player.setExp(0.0f);
		
		try {
			String exp = (String)Papi.MySQL.get(expSqlColumn, Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE);
			if(exp != null) {
				String[] expArr = exp.split(",");
				if(player.isDead())
				{
					Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
						@Override
						public void run() {
							player.setLevel(Integer.parseInt(expArr[0]));
							player.setExp(Float.parseFloat(expArr[1]));
						}
					});
				}
				else
				{
					player.setLevel(Integer.parseInt(expArr[0]));
					player.setExp(Float.parseFloat(expArr[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

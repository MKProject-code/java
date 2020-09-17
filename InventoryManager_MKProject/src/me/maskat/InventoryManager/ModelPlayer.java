package me.maskat.InventoryManager;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import mkproject.maskat.Papi.Papi;

public class ModelPlayer {
	private Player player;

	public ModelPlayer(Player player) {
		this.player = player;

		if (!Papi.MySQL.exists(Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE))
		{
			Papi.MySQL.put(Map.of(Database.Inv.NAME, player.getName().toLowerCase()), Database.Inv.TABLE);
			if(player.getGameMode() == GameMode.SURVIVAL)
				this.saveInventory(GameMode.SURVIVAL);
		}
	}

	public void saveInventory(GameMode gameMode) {
		String playerInvBase64 = InventorySerializer.inventoryToBase64(player.getInventory());
		String playerSqlColumn = null;
		if (gameMode == GameMode.SURVIVAL)
			playerSqlColumn = Database.Inv.INV_SURVIVAL_CONTENT;
		else if (gameMode == GameMode.CREATIVE)
			playerSqlColumn = Database.Inv.INV_CREATIVE_CONTENT;
		else if (gameMode == GameMode.ADVENTURE)
			playerSqlColumn = Database.Inv.INV_ADVENTURE_CONTENT;
		else if (gameMode == GameMode.SPECTATOR)
			playerSqlColumn = Database.Inv.INV_SPECTATOR_CONTENT;

//		Papi.MySQL.set(Map.of(playerSqlColumn, playerInvBase64), Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE);

		String enderInvBase64 = InventorySerializer.inventoryToBase64(player.getEnderChest());
		String enderSqlColumn = null;
		if (gameMode == GameMode.SURVIVAL)
			enderSqlColumn = Database.Inv.INV_SURVIVAL_ENDERCHEST_CONTENT;
		else if (gameMode == GameMode.CREATIVE)
			enderSqlColumn = Database.Inv.INV_CREATIVE_ENDERCHEST_CONTENT;
		else if (gameMode == GameMode.ADVENTURE)
			enderSqlColumn = Database.Inv.INV_ADVENTURE_ENDERCHEST_CONTENT;
		else if (gameMode == GameMode.SPECTATOR)
			enderSqlColumn = Database.Inv.INV_SPECTATOR_ENDERCHEST_CONTENT;

//		Papi.MySQL.set(Map.of(enderSqlColumn, enderInvBase64), Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE);		

		String expSqlColumn = null;
		if (gameMode == GameMode.SURVIVAL)
			expSqlColumn = Database.Inv.INV_SURVIVAL_EXP;
		else if (gameMode == GameMode.CREATIVE)
			expSqlColumn = Database.Inv.INV_CREATIVE_EXP;
		else if (gameMode == GameMode.ADVENTURE)
			expSqlColumn = Database.Inv.INV_ADVENTURE_EXP;
		else if (gameMode == GameMode.SPECTATOR)
			expSqlColumn = Database.Inv.INV_SPECTATOR_EXP;

//		Papi.MySQL.set(Map.of(expSqlColumn, String.valueOf(player.getLevel()) + "," + String.valueOf(player.getExp())), Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE);

		
		String velocityBase64 = InventorySerializer.velocityToBase64(player.getVelocity());
		String potionEffectsBase64 = InventorySerializer.potionEffectsToBase64(player.getActivePotionEffects());
		
		if (gameMode == GameMode.SURVIVAL) {
			Map<String, Object> sqlSetData = new HashMap<>();
			sqlSetData.put(playerSqlColumn, playerInvBase64);
			sqlSetData.put(enderSqlColumn, enderInvBase64);
			sqlSetData.put(expSqlColumn, String.valueOf(player.getLevel()) + "," + String.valueOf(player.getExp()));
			sqlSetData.put(Database.Inv.INV_SURVIVAL_HEALTH, String.valueOf(player.getHealth()));
			sqlSetData.put(Database.Inv.INV_SURVIVAL_FOOD, String.valueOf(player.getFoodLevel()));
			sqlSetData.put(Database.Inv.INV_SURVIVAL_SATURATION, String.valueOf(player.getSaturation()));
			sqlSetData.put(Database.Inv.INV_SURVIVAL_EXHAUSTION, String.valueOf(player.getExhaustion()));
			sqlSetData.put(Database.Inv.INV_SURVIVAL_ABSORPTION, String.valueOf(player.getAbsorptionAmount()));
			sqlSetData.put(Database.Inv.INV_SURVIVAL_FIRE_TICKS, String.valueOf(player.getFireTicks()));
			sqlSetData.put(Database.Inv.INV_SURVIVAL_VELOCITY, velocityBase64);
			sqlSetData.put(Database.Inv.INV_SURVIVAL_POTION_EFFECTS, potionEffectsBase64);
			Papi.MySQL.set(sqlSetData,
					Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE);
		} else {
			Papi.MySQL.set(
					Map.of(playerSqlColumn, playerInvBase64, enderSqlColumn, enderInvBase64, expSqlColumn,
							String.valueOf(player.getLevel()) + "," + String.valueOf(player.getExp())),
					Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE);
		}
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

		if (newGameMode == GameMode.SURVIVAL) {
			for (PotionEffect potionEffect : player.getActivePotionEffects())
				player.removePotionEffect(potionEffect.getType());

			player.setCollidable(true);
		}

		ResultSet rs = Papi.MySQL.getResultSetAll(1, "*",
				Papi.SQL.getWhereObject(Database.Inv.NAME, "=", player.getName().toLowerCase()), Database.Inv.TABLE);

		try {
			if (rs != null && rs.next()) {
				// Player inventory
				String playerInvBase64 = null;
				if (newGameMode == GameMode.SURVIVAL)
					playerInvBase64 = rs.getString(Database.Inv.INV_SURVIVAL_CONTENT);
				else if (newGameMode == GameMode.CREATIVE)
					playerInvBase64 = rs.getString(Database.Inv.INV_CREATIVE_CONTENT);
				else if (newGameMode == GameMode.ADVENTURE)
					playerInvBase64 = rs.getString(Database.Inv.INV_ADVENTURE_CONTENT);
				else if (newGameMode == GameMode.SPECTATOR)
					playerInvBase64 = rs.getString(Database.Inv.INV_SPECTATOR_CONTENT);

				ItemStack[] playerInv = null;
				if (playerInvBase64 != null) {
					try {
						playerInv = InventorySerializer.inventoryFromBase64(playerInvBase64);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (playerInv != null)
					player.getInventory().setContents(playerInv);
				else
					player.getInventory().clear();

				player.updateInventory();

				// EnderChest inventory
				String enderInvBase64 = null;
				if (newGameMode == GameMode.SURVIVAL)
					enderInvBase64 = rs.getString(Database.Inv.INV_SURVIVAL_ENDERCHEST_CONTENT);
				else if (newGameMode == GameMode.CREATIVE)
					enderInvBase64 = rs.getString(Database.Inv.INV_CREATIVE_ENDERCHEST_CONTENT);
				else if (newGameMode == GameMode.ADVENTURE)
					enderInvBase64 = rs.getString(Database.Inv.INV_ADVENTURE_ENDERCHEST_CONTENT);
				else if (newGameMode == GameMode.SPECTATOR)
					enderInvBase64 = rs.getString(Database.Inv.INV_SPECTATOR_ENDERCHEST_CONTENT);

				ItemStack[] enderInv = null;
				if (enderInvBase64 != null) {
					try {
						enderInv = InventorySerializer.inventoryFromBase64(enderInvBase64);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (enderInv != null)
					player.getEnderChest().setContents(enderInv);
				else
					player.getEnderChest().clear();

				player.closeInventory();

				//Player Experience
				String playerExpData = null;
				if (newGameMode == GameMode.SURVIVAL)
					playerExpData = rs.getString(Database.Inv.INV_SURVIVAL_EXP);
				else if (newGameMode == GameMode.CREATIVE)
					playerExpData = rs.getString(Database.Inv.INV_CREATIVE_EXP);
				else if (newGameMode == GameMode.ADVENTURE)
					playerExpData = rs.getString(Database.Inv.INV_ADVENTURE_EXP);
				else if (newGameMode == GameMode.SPECTATOR)
					playerExpData = rs.getString(Database.Inv.INV_SPECTATOR_EXP);

				player.setLevel(0);
				player.setExp(0.0f);

				try {
					if (playerExpData != null) {
						String[] expArr = playerExpData.split(",");
						if (player.isDead()) {
							Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
								@Override
								public void run() {
									player.setLevel(Integer.parseInt(expArr[0]));
									player.setExp(Float.parseFloat(expArr[1]));
								}
							});
						} else {
							player.setLevel(Integer.parseInt(expArr[0]));
							player.setExp(Float.parseFloat(expArr[1]));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Player parameters
				if (newGameMode == GameMode.SURVIVAL)
				{
					double health = Papi.Function.getNumeric(String.valueOf(rs.getObject(Database.Inv.INV_SURVIVAL_HEALTH)), 20D);
					int foodLevel = Papi.Function.getNumeric(String.valueOf(rs.getObject(Database.Inv.INV_SURVIVAL_FOOD)), 20);
					float saturation = Papi.Function.getNumeric(String.valueOf(rs.getObject(Database.Inv.INV_SURVIVAL_SATURATION)), 20F);
					float exhaustion = Papi.Function.getNumeric(String.valueOf(rs.getObject(Database.Inv.INV_SURVIVAL_EXHAUSTION)), 20F);
					double absorptionAmount = Papi.Function.getNumeric(String.valueOf(rs.getObject(Database.Inv.INV_SURVIVAL_ABSORPTION)), 0D);
					int fireTicks = Papi.Function.getNumeric(String.valueOf(rs.getObject(Database.Inv.INV_SURVIVAL_FIRE_TICKS)), 0);
					String velocityBase64 = rs.getString(Database.Inv.INV_SURVIVAL_VELOCITY);
					
					Vector velocityTemp = null;
					if (velocityBase64 != null) {
						try {
							velocityTemp = InventorySerializer.velocityFromBase64(velocityBase64);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					final Vector velocityFinal = velocityTemp;
					
					String potionEffectsBase64 = rs.getString(Database.Inv.INV_SURVIVAL_POTION_EFFECTS);
					
					Collection<PotionEffect> potionEffectsTemp = null;
					if (potionEffectsBase64 != null) {
						try {
							potionEffectsTemp = InventorySerializer.potionEffectsFromBase64(potionEffectsBase64);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					final Collection<PotionEffect> potionEffectsFinal = potionEffectsTemp;
					
					if (player.isDead()) {
						Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
							@Override
							public void run() {
								player.setHealth(health);
								player.setFoodLevel(foodLevel);
								player.setSaturation(saturation);
								player.setExhaustion(exhaustion);
								player.setAbsorptionAmount(absorptionAmount);
								player.setFireTicks(fireTicks);
								try {
									if(velocityFinal != null)
										player.setVelocity(velocityFinal);
								} catch(Exception ex) {}
								try {
									if(potionEffectsFinal != null)
										player.addPotionEffects(potionEffectsFinal);
								} catch(Exception ex) {}
							}
						});
					} else {
						player.setHealth(health);
						player.setFoodLevel(foodLevel);
						player.setSaturation(saturation);
						player.setExhaustion(exhaustion);
						player.setAbsorptionAmount(absorptionAmount);
						player.setFireTicks(fireTicks);
						try {
							if(velocityFinal != null)
								player.setVelocity(velocityFinal);
						} catch(Exception ex) {}
						try {
							if(potionEffectsFinal != null)
								player.addPotionEffects(potionEffectsFinal);
						} catch(Exception ex) {}
					}
				}
			}
		} catch (SQLException e1) {
			player.getInventory().clear();
			e1.printStackTrace();
		}

		// Old
//		String playerSqlColumn = null;
//		if (newGameMode == GameMode.SURVIVAL) {
//			for (PotionEffect potionEffect : player.getActivePotionEffects())
//				player.removePotionEffect(potionEffect.getType());
//
//			Object playerHealth = Papi.MySQL.get(Database.Inv.INV_SURVIVAL_HEALTH, Database.Inv.NAME, "=",
//					player.getName().toLowerCase(), Database.Inv.TABLE);
//			Object playerFood = Papi.MySQL.get(Database.Inv.INV_SURVIVAL_FOOD, Database.Inv.NAME, "=",
//					player.getName().toLowerCase(), Database.Inv.TABLE);
//			Object playerSaturation = Papi.MySQL.get(Database.Inv.INV_SURVIVAL_SATURATION, Database.Inv.NAME, "=",
//					player.getName().toLowerCase(), Database.Inv.TABLE);
//			Object playerExhaustion = Papi.MySQL.get(Database.Inv.INV_SURVIVAL_EXHAUSTION, Database.Inv.NAME, "=",
//					player.getName().toLowerCase(), Database.Inv.TABLE);
//			Object playerAbsorption = Papi.MySQL.get(Database.Inv.INV_SURVIVAL_ABSORPTION, Database.Inv.NAME, "=",
//					player.getName().toLowerCase(), Database.Inv.TABLE);
//
//			player.setHealth(Papi.Function.getNumeric(String.valueOf(playerHealth), 20D));
//			player.setFoodLevel(Papi.Function.getNumeric(String.valueOf(playerFood), 20));
//			player.setSaturation(Papi.Function.getNumeric(String.valueOf(playerSaturation), 20F));
//			player.setExhaustion(Papi.Function.getNumeric(String.valueOf(playerExhaustion), 20F));
//			player.setAbsorptionAmount(Papi.Function.getNumeric(String.valueOf(playerAbsorption), 20D));
//
//			playerSqlColumn = Database.Inv.INV_SURVIVAL_CONTENT;
//		} else if (newGameMode == GameMode.CREATIVE)
//			playerSqlColumn = Database.Inv.INV_CREATIVE_CONTENT;
//		else if (newGameMode == GameMode.ADVENTURE)
//			playerSqlColumn = Database.Inv.INV_ADVENTURE_CONTENT;
//		else if (newGameMode == GameMode.SPECTATOR)
//			playerSqlColumn = Database.Inv.INV_SPECTATOR_CONTENT;
//
//		Object playerInvBase64 = Papi.MySQL.get(playerSqlColumn, Database.Inv.NAME, "=", player.getName().toLowerCase(),
//				Database.Inv.TABLE);
//
//		ItemStack[] playerInv = null;
//		if (playerInvBase64 != null) {
//			try {
//				playerInv = InventorySerializer.inventoryFromBase64(playerInvBase64.toString());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		if (playerInv != null)
//			player.getInventory().setContents(playerInv);
//		else
//			player.getInventory().clear();
//
//		player.updateInventory();
//
//		String enderSqlColumn = null;
//		if (newGameMode == GameMode.SURVIVAL)
//			enderSqlColumn = Database.Inv.INV_SURVIVAL_ENDERCHEST_CONTENT;
//		else if (newGameMode == GameMode.CREATIVE)
//			enderSqlColumn = Database.Inv.INV_CREATIVE_ENDERCHEST_CONTENT;
//		else if (newGameMode == GameMode.ADVENTURE)
//			enderSqlColumn = Database.Inv.INV_ADVENTURE_ENDERCHEST_CONTENT;
//		else if (newGameMode == GameMode.SPECTATOR)
//			enderSqlColumn = Database.Inv.INV_SPECTATOR_ENDERCHEST_CONTENT;
//
//		Object enderInvBase64 = Papi.MySQL.get(enderSqlColumn, Database.Inv.NAME, "=", player.getName().toLowerCase(),
//				Database.Inv.TABLE);
//
//		ItemStack[] enderInv = null;
//		if (enderInvBase64 != null) {
//			try {
//				enderInv = InventorySerializer.inventoryFromBase64(enderInvBase64.toString());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		if (enderInv != null)
//			player.getEnderChest().setContents(enderInv);
//		else
//			player.getEnderChest().clear();
//
//		player.closeInventory();
//
//		String expSqlColumn = null;
//		if (newGameMode == GameMode.SURVIVAL)
//			expSqlColumn = Database.Inv.INV_SURVIVAL_EXP;
//		else if (newGameMode == GameMode.CREATIVE)
//			expSqlColumn = Database.Inv.INV_CREATIVE_EXP;
//		else if (newGameMode == GameMode.ADVENTURE)
//			expSqlColumn = Database.Inv.INV_ADVENTURE_EXP;
//		else if (newGameMode == GameMode.SPECTATOR)
//			expSqlColumn = Database.Inv.INV_SPECTATOR_EXP;
//
//		player.setLevel(0);
//		player.setExp(0.0f);
//
//		try {
//			String exp = (String) Papi.MySQL.get(expSqlColumn, Database.Inv.NAME, "=", player.getName().toLowerCase(),
//					Database.Inv.TABLE);
//			if (exp != null) {
//				String[] expArr = exp.split(",");
//				if (player.isDead()) {
//					Bukkit.getScheduler().runTask(Plugin.getPlugin(), new Runnable() {
//						@Override
//						public void run() {
//							player.setLevel(Integer.parseInt(expArr[0]));
//							player.setExp(Float.parseFloat(expArr[1]));
//						}
//					});
//				} else {
//					player.setLevel(Integer.parseInt(expArr[0]));
//					player.setExp(Float.parseFloat(expArr[1]));
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public void updateInventory(GameMode gameMode, PlayerInventory playerInventory, boolean keepInventory, int newLevel, int newExp) {
		String playerSqlColumn = null;
		if (gameMode == GameMode.SURVIVAL)
			playerSqlColumn = Database.Inv.INV_SURVIVAL_CONTENT;
		else if (gameMode == GameMode.CREATIVE)
			playerSqlColumn = Database.Inv.INV_CREATIVE_CONTENT;
		else if (gameMode == GameMode.ADVENTURE)
			playerSqlColumn = Database.Inv.INV_ADVENTURE_CONTENT;
		else if (gameMode == GameMode.SPECTATOR)
			playerSqlColumn = Database.Inv.INV_SPECTATOR_CONTENT;

		String enderSqlColumn = null;
		if (gameMode == GameMode.SURVIVAL)
			enderSqlColumn = Database.Inv.INV_SURVIVAL_ENDERCHEST_CONTENT;
		else if (gameMode == GameMode.CREATIVE)
			enderSqlColumn = Database.Inv.INV_CREATIVE_ENDERCHEST_CONTENT;
		else if (gameMode == GameMode.ADVENTURE)
			enderSqlColumn = Database.Inv.INV_ADVENTURE_ENDERCHEST_CONTENT;
		else if (gameMode == GameMode.SPECTATOR)
			enderSqlColumn = Database.Inv.INV_SPECTATOR_ENDERCHEST_CONTENT;
		
		String expSqlColumn = null;
		if (gameMode == GameMode.SURVIVAL)
			expSqlColumn = Database.Inv.INV_SURVIVAL_EXP;
		else if (gameMode == GameMode.CREATIVE)
			expSqlColumn = Database.Inv.INV_CREATIVE_EXP;
		else if (gameMode == GameMode.ADVENTURE)
			expSqlColumn = Database.Inv.INV_ADVENTURE_EXP;
		else if (gameMode == GameMode.SPECTATOR)
			expSqlColumn = Database.Inv.INV_SPECTATOR_EXP;

		

		
		
		if (keepInventory)
		{
			String playerInvBase64 = InventorySerializer.inventoryToBase64(player.getInventory());
			String enderInvBase64 = InventorySerializer.inventoryToBase64(player.getEnderChest());
			
			if (gameMode == GameMode.SURVIVAL) {
				Map<String, Object> sqlSetData = new HashMap<>();
				sqlSetData.put(playerSqlColumn, playerInvBase64);
				sqlSetData.put(enderSqlColumn, enderInvBase64);
				sqlSetData.put(expSqlColumn, String.valueOf(newLevel) + "," + String.valueOf(newExp));
				sqlSetData.put(Database.Inv.INV_SURVIVAL_HEALTH, 20);
				sqlSetData.put(Database.Inv.INV_SURVIVAL_FOOD, 20);
				sqlSetData.put(Database.Inv.INV_SURVIVAL_SATURATION, 20);
				sqlSetData.put(Database.Inv.INV_SURVIVAL_EXHAUSTION, 20);
				sqlSetData.put(Database.Inv.INV_SURVIVAL_ABSORPTION, 0);
				sqlSetData.put(Database.Inv.INV_SURVIVAL_FIRE_TICKS, 0);
				sqlSetData.put(Database.Inv.INV_SURVIVAL_VELOCITY, "");
				sqlSetData.put(Database.Inv.INV_SURVIVAL_POTION_EFFECTS, "");
				Papi.MySQL.set(sqlSetData, Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE);
			}
			else
			{
				Papi.MySQL.set(
						Map.of(
								playerSqlColumn, playerInvBase64,
								enderSqlColumn, enderInvBase64,
								expSqlColumn, String.valueOf(newLevel) + "," + String.valueOf(newExp)
							), Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE);
			}
		}
		else
		{
			String enderInvBase64 = InventorySerializer.inventoryToBase64(player.getEnderChest());
			
			if (gameMode == GameMode.SURVIVAL) {
				Map<String, Object> sqlSetData = new HashMap<>();
				sqlSetData.put(playerSqlColumn, "");
				sqlSetData.put(enderSqlColumn, enderInvBase64);
				sqlSetData.put(expSqlColumn, String.valueOf(newLevel) + "," + String.valueOf(newExp));
				sqlSetData.put(Database.Inv.INV_SURVIVAL_HEALTH, 20);
				sqlSetData.put(Database.Inv.INV_SURVIVAL_FOOD, 20);
				sqlSetData.put(Database.Inv.INV_SURVIVAL_SATURATION, 20);
				sqlSetData.put(Database.Inv.INV_SURVIVAL_EXHAUSTION, 20);
				sqlSetData.put(Database.Inv.INV_SURVIVAL_ABSORPTION, 0);
				sqlSetData.put(Database.Inv.INV_SURVIVAL_FIRE_TICKS, 0);
				sqlSetData.put(Database.Inv.INV_SURVIVAL_VELOCITY, "");
				sqlSetData.put(Database.Inv.INV_SURVIVAL_POTION_EFFECTS, "");
				Papi.MySQL.set(sqlSetData, Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE);
			} else {
				Papi.MySQL.set(
						Map.of(
								playerSqlColumn, "",
								enderSqlColumn, enderInvBase64,
								expSqlColumn, String.valueOf(newLevel) + "," + String.valueOf(newExp)
							), Database.Inv.NAME, "=", player.getName().toLowerCase(), Database.Inv.TABLE);
			}
		}
	}

	public void clearInventory() {
		this.player.getInventory().clear();
		this.player.setLevel(0);
		this.player.setExp(0F);
		this.player.setHealth(20D);
		this.player.setFoodLevel(20);
		this.player.setSaturation(20F);
		this.player.setExhaustion(20F);
		this.player.setAbsorptionAmount(0D);
		for (PotionEffect potionEffect : player.getActivePotionEffects())
			player.removePotionEffect(potionEffect.getType());
	}

}

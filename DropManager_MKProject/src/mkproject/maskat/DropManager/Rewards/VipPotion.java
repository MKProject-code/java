package mkproject.maskat.DropManager.Rewards;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import mkproject.maskat.DropManager.Database;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;
import mkproject.maskat.VipManager.VipAPI;

public class VipPotion {

	private static final String DisplayName = Message.getColorMessage("&aMikstura potęgi Skyidea");
	private static final String Lore30Minute = Message.getColorMessage("&b&lVIP&b na 30 minut");
	private static final String Lore1Hour = Message.getColorMessage("&b&lVIP&b na 1 godzinę");
	private static final String Lore2Hour = Message.getColorMessage("&b&lVIP&b na 2 godziny");
	private static final String Lore3Hour = Message.getColorMessage("&b&lVIP&b na 3 godziny");
	private static final String Lore4Hour = Message.getColorMessage("&b&lVIP&b na 4 godziny");
	private static final String LoreExpired = Message.getColorMessage("&cData ważności: {date}");
	
	public static void dropReward(Player player, Location location) {
		Timestamp timestampFirstPlayed=new Timestamp(player.getFirstPlayed());
		boolean disallowDropTime = timestampFirstPlayed.after(Timestamp.valueOf(LocalDateTime.now().minusDays(2L)));
		
		if(disallowDropTime)
			return;
		
		if(Database.DropsVipPotion.isVipPotionBlockDropValidExist(player))
			return;
		
		ItemStack potion = new ItemStack(Material.POTION, 1);
		PotionMeta potionmeta = (PotionMeta) potion.getItemMeta();
		PotionEffect luck = new PotionEffect(PotionEffectType.LUCK, 60, 1);
		potionmeta.addCustomEffect(luck, true);
		potionmeta.setDisplayName(DisplayName);
		
		LocalDateTime potionExpired = LocalDateTime.now().plusHours(Papi.Function.randomInteger(1, 48));
		
		int rand = Papi.Function.randomInteger(1, 5);
		if(rand==1)
			potionmeta.setLore(List.of(Lore30Minute,LoreExpired.replace("{date}", potionExpired.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")))));
		else if(rand==2)
			potionmeta.setLore(List.of(Lore1Hour,LoreExpired.replace("{date}", potionExpired.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")))));
		else if(rand==3)
			potionmeta.setLore(List.of(Lore2Hour,LoreExpired.replace("{date}", potionExpired.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")))));
		else if(rand==4)
			potionmeta.setLore(List.of(Lore3Hour,LoreExpired.replace("{date}", potionExpired.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")))));
		else if(rand==5)
			potionmeta.setLore(List.of(Lore4Hour,LoreExpired.replace("{date}", potionExpired.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")))));
		potion.setItemMeta(potionmeta);
		
		Database.DropsVipPotion.addVipPotion(player, LocalDateTime.now().plusDays(Papi.Function.randomInteger(4, 9)));
		
		location.getWorld().dropItemNaturally(location, potion);
	}

	public static void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
		ItemMeta itemMeta = e.getItem().getItemMeta();
		if(itemMeta.getDisplayName().equals(DisplayName)) {
		
			if(e.getPlayer().getGameMode() != GameMode.SURVIVAL) {
				e.setCancelled(true);
				return;
			}
			
			if(itemMeta.getLore().size() < 2)
			{
				Message.sendTitle(e.getPlayer(), null, "&cMikstura straciła ważność");
				return;
			}
			
			String expiredDateTimeStr = itemMeta.getLore().get(1).split(": ")[1];
			LocalDateTime expiredDateTime = Papi.Function.getLocalDateTimeFromString(expiredDateTimeStr, "dd.MM.yyyy HH:mm:ss");
			
			if(expiredDateTime.isBefore(LocalDateTime.now()))
			{
				Message.sendTitle(e.getPlayer(), null, "&cMikstura straciła ważność");
				return;
			}
			
			if(itemMeta.getLore().get(0).equals(Lore30Minute)) {
				VipAPI.giveVIP(e.getPlayer(), LocalDateTime.now().plusMinutes(30), "potion_30m");
				Message.sendTitle(e.getPlayer(), "&bVIP", "&aAktywowano na 30 minut");
			}
			else if(itemMeta.getLore().get(0).equals(Lore1Hour)) {
				VipAPI.giveVIP(e.getPlayer(), LocalDateTime.now().plusHours(1), "potion_1h");
				Message.sendTitle(e.getPlayer(), "&bVIP", "&aAktywowano na 1 godzinę");
			}
			else if(itemMeta.getLore().get(0).equals(Lore2Hour)) {
				VipAPI.giveVIP(e.getPlayer(), LocalDateTime.now().plusHours(2), "potion_2h");
				Message.sendTitle(e.getPlayer(), "&bVIP", "&aAktywowano na 2 godziny");
			}
			else if(itemMeta.getLore().get(0).equals(Lore3Hour)) {
				VipAPI.giveVIP(e.getPlayer(), LocalDateTime.now().plusHours(3), "potion_3h");
				Message.sendTitle(e.getPlayer(), "&bVIP", "&aAktywowano na 3 godziny");
			}
			else if(itemMeta.getLore().get(0).equals(Lore4Hour)) {
				VipAPI.giveVIP(e.getPlayer(), LocalDateTime.now().plusHours(4), "potion_4h");
				Message.sendTitle(e.getPlayer(), "&bVIP", "&aAktywowano na 4 godziny");
			}
			Message.sendBroadcast("&b>&e "+e.getPlayer().getName()+"&6 skosztował jak smakuje &lVIP&6!");
			Message.sendBroadcast("&b>&a&l Lubisz się wyróżniać? Sprawdź co posiada &b&lVIP&a&l: &e&l/vip");
		}
	}
}

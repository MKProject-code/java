package mkproject.maskat.DropManager.Rewards;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import mkproject.maskat.DropManager.Database;
import mkproject.maskat.Papi.Papi;
import mkproject.maskat.Papi.Utils.Message;
import mkproject.maskat.VipManager.VipAPI;

public class VipBook {

	private static final String DisplayName = Message.getColorMessage("&dCzarna magia");
	private static final String LoreMinus = Message.getColorMessage("&b&l-{procent}%&a na &b&lVIP&a z kodem: {code}");
	private static final String LoreExpires = Message.getColorMessage("&cKod ważny do &c&l{date}");
	
	public static void dropReward(Player player, Location location) {
		Timestamp timestampFirstPlayed=new Timestamp(player.getFirstPlayed());
		boolean disallowDropTime = timestampFirstPlayed.after(Timestamp.valueOf(LocalDateTime.now().minusDays(7L)));
		
		if(disallowDropTime)
			return;
		
		if(VipAPI.isDonationVIP(player))
			return;
		
		if(Database.DropsVipBook.isVipBookSaleValidExist(player))
			return;
		
		ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta bookMeta = (BookMeta)bookItem.getItemMeta();
		
		bookMeta.setAuthor("Skyidea.pl");
		bookMeta.setTitle(Message.getColorMessage(DisplayName));
		
//		int rand2 = Papi.Function.randomInteger(1, 2);
//		LocalDateTime expireDateTime = (rand2 == 1 ? LocalDateTime.now().plusHours(6) : LocalDateTime.now().plusHours(12));
		LocalDateTime expireDateTime = LocalDateTime.now().plusHours(Papi.Function.randomInteger(6, 24));
		
		String saleExpiredDateTime = expireDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
		
		String saleCode = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
		
//		int rand1 = Papi.Function.randomInteger(1, 4);
//		String saleProcent = (rand1 == 1 ? "50" : "25");
		String saleProcent = "25";
		String saleProcentBook = LoreMinus.replace("{procent}", saleProcent).replace("{code}", saleCode);
		
		String expiredBook = LoreExpires.replace("{date}", saleExpiredDateTime);
		
		bookMeta.setLore(List.of(saleProcentBook, expiredBook));
		
		bookMeta.setUnbreakable(false);
		bookMeta.addPage(Message.getColorMessage(
				"&5&lCzarna magia!&r\n"
				+"Chcesz być &1&lVIP na dłużej&r?\n\n"
				+"Z tą księgą to możliwe! Wykożystaj specjalny kod &l"+saleCode+"&r i otrzymaj VIPa &2&ltaniej&r!\n\n"
				+"&4&lŚpiesz się!&r Tylko do &c"+saleExpiredDateTime+"&r masz zniżkę &4&l-"+saleProcent+"%&r!\n\n"
				+"&1Discord: MasKAT#5316"
				));
		bookItem.setItemMeta(bookMeta);
		
		Database.DropsVipBook.addVipBook(player, saleCode, saleProcent, expireDateTime);
		
		location.getWorld().dropItemNaturally(location, bookItem);
	}
}

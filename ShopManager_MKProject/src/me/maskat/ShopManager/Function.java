package me.maskat.ShopManager;

public class Function {
	public static class Menu {
		public static String getCurrency(int amount) {
			String currency = "SkyPunkt";
			if(amount >= 2)
				if(amount >= 5)
					currency = "SkyPunktów";
				else
					currency = "SkyPunkty";
			return currency;
		}
	}
}

package fr.traqueur.venusiaessentials.api.utils;

import fr.traqueur.venusiaessentials.VenusiaEssentials;

public class EconomyUtils {

	@SuppressWarnings("deprecation")
	public static double getBalance(String name) {
		VenusiaEssentials venusia = VenusiaEssentials.getInstance();
		return venusia.getEconomy().getBalance(name);
	}
	
	public @Deprecated static void setBalance(String name, double value) {
		VenusiaEssentials venusia = VenusiaEssentials.getInstance();
		venusia.getEconomy().withdrawPlayer(name, venusia.getEconomy().getBalance(name));
		venusia.getEconomy().depositPlayer(name, value);
	}
	
	@SuppressWarnings("deprecation")
	public static void deposit(String name, double value) {
		VenusiaEssentials venusia = VenusiaEssentials.getInstance();
		venusia.getEconomy().depositPlayer(name, value);
	}
	
	@SuppressWarnings("deprecation")
	public static void withdraw(String name, double value) {
		VenusiaEssentials venusia = VenusiaEssentials.getInstance();
		venusia.getEconomy().withdrawPlayer(name, value);
	}
	
	@SuppressWarnings("deprecation")
	public static boolean has(String name, double value) {
		VenusiaEssentials venusia = VenusiaEssentials.getInstance();
		return venusia.getEconomy().has(name, value);
	}
}

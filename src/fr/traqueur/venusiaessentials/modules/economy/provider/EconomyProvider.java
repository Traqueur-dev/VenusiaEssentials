package fr.traqueur.venusiaessentials.modules.economy.provider;

import java.util.List;

import org.bukkit.Bukkit;

import fr.traqueur.venusiaessentials.VenusiaEssentials;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;

public class EconomyProvider extends AbstractEconomy {

	@Override
	public boolean createPlayerAccount(String name) {
		ProfileModule profileManager = ProfileModule.getInstance();
		if (profileManager.profileExist(name)) {
			return false;
		}
		profileManager.createProfile(Bukkit.getPlayer(name));
		return true;
	}

	@Override
	public boolean createPlayerAccount(String name, String worldName) {
		ProfileModule profileManager = ProfileModule.getInstance();
		if (profileManager.profileExist(name)) {
			return false;
		}
		profileManager.createProfile(Bukkit.getPlayer(name));
		return true;
	}

	@Override
	public EconomyResponse depositPlayer(String name, double amount) {
		if (!this.hasAccount(name)) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
					"Le joueur n'a pas de compte!");
		}
		if (amount < 0.0D) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
					"La valeur est inférieure à zéro!");
		}
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(name);
		profile.deposit(amount);
		return new EconomyResponse(amount, 0.0D, EconomyResponse.ResponseType.SUCCESS,
				"Transaction effectuée avec succés!");
	}

	@Override
	public EconomyResponse depositPlayer(String name, String worldName, double amount) {
		if (!this.hasAccount(name)) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
					"Le joueur n'a pas de compte!");
		}
		if (amount < 0.0D) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
					"La valeur est inférieure à zéro!");
		}
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(name);
		profile.deposit(amount);
		return new EconomyResponse(amount, 0.0D, EconomyResponse.ResponseType.SUCCESS,
				"Transaction effectuée avec succés!");
	}

	@Override
	public double getBalance(String name) {
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(name);
		return profile.getBalance();
	}

	@Override
	public double getBalance(String name, String worldName) {
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(name);
		return profile.getBalance();
	}

	@Override
	public String getName() {
		return "VenusiaEconomy";
	}

	@Override
	public boolean has(String name, double amount) {
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(name);
		return profile.has(amount);
	}

	@Override
	public boolean has(String name, String worldName, double amount) {
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(name);
		return profile.has(amount);
	}

	@Override
	public boolean hasAccount(String name) {
		ProfileModule profileManager = ProfileModule.getInstance();
		return profileManager.profileExist(name);
	}

	@Override
	public boolean hasAccount(String name, String worldName) {
		ProfileModule profileManager = ProfileModule.getInstance();
		return profileManager.profileExist(name);
	}

	@Override
	public EconomyResponse withdrawPlayer(String name, double amount) {
		if (!hasAccount(name)) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
					"Le joueur n'a pas de compte!");
		}
		double balance = getBalance(name);
		if (getBalance(name) < amount) {
			return new EconomyResponse(0.0D, balance, EconomyResponse.ResponseType.FAILURE,
					"La valeur est plus que le solde du joueur!");
		}
		balance -= amount;
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(name);
		profile.withdraw(amount);
		return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS,
				"Transaction effectuée avec succés!");
	}

	@Override
	public EconomyResponse withdrawPlayer(String name, String worldName, double amount) {
		if (!hasAccount(name)) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
					"Le joueur n'a pas de compte!");
		}
		double balance = getBalance(name);
		if (getBalance(name) < amount) {
			return new EconomyResponse(0.0D, balance, EconomyResponse.ResponseType.FAILURE,
					"La valeur est plus que le solde du joueur!");
		}
		balance -= amount;
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(name);
		profile.withdraw(amount);
		return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS,
				"Transaction effectuée avec succés!");
	}

	@Override
	public boolean isEnabled() {
		return VenusiaEssentials.getInstance() != null;
	}

	@Override
	public String currencyNamePlural() {
		return "";
	}

	@Override
	public String currencyNameSingular() {
		return "";
	}

	@Override
	public String format(double name) {
		return String.valueOf(name);
	}

	@Override
	public int fractionalDigits() {
		return -1;
	}

	// null

	@Override
	public EconomyResponse bankBalance(String name) {
		return null;
	}

	@Override
	public EconomyResponse bankDeposit(String name, double amount) {
		return null;
	}

	@Override
	public EconomyResponse bankHas(String name, double amount) {
		return null;
	}

	@Override
	public EconomyResponse bankWithdraw(String name, double amount) {
		return null;
	}

	@Override
	public EconomyResponse createBank(String name, String worldName) {
		return null;
	}

	@Override
	public EconomyResponse deleteBank(String name) {
		return null;
	}

	@Override
	public List<String> getBanks() {
		return null;
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public EconomyResponse isBankMember(String name, String worldName) {
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String name, String worldName) {
		return null;
	}
}

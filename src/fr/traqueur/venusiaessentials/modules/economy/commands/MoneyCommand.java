package fr.traqueur.venusiaessentials.modules.economy.commands;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;
import net.minecraft.util.com.google.common.primitives.Doubles;

public class MoneyCommand extends ICommand {

	@Command(name = "bal", aliases = {"balance", "money", "argent"})
	public void onCommand(CommandArgs args) {
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(args.getPlayer().getName());
		double balance = profile.getBalance();
		if (args.length() < 1) { // self check
			profile.msg(Utils.LINE);
			profile.msg("&7» &6Commandes§7:");
			profile.msg("  §7• &e/money <joueur> &7| &6Afficher l'argent d'un joueur.");
			profile.msg("  §7• &e/money pay <joueur> <valeur> &7| &6Envoyez de l'argent à un joueur.");
			profile.msg("  §7• &e/money top &7| &6Afficher le classement argent du serveur.");
			profile.msg(" ");
			profile.msg("&7» &eVous avez &a%1$s$§e.", balance);
			profile.msg(Utils.LINE);
		} else { // target check
			if (!profileManager.profileExist(args.getArgs(0))) {
				profile.msg("&cCe joueur est introuvable.");
				return;
			}
			Profile target = profileManager.getProfile(args.getArgs(0));
			double balanceTarget = target.getBalance();
			profile.msg("&c» §eLe joueur &6%1$s &epossède &a%2$s$§e.", target.getPlayerName(), balanceTarget);
		}
	}

	@Command(name = "bal.pay", aliases ={"balance.pay", "money.pay", "argent.pay"})
	public void onPayCommand(CommandArgs args) {
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(args.getPlayer().getName());
		double balance = profile.getBalance();
		
		if (args.length() < 2) {
			profile.msg("&e/money pay <joueur> <valeur> &7- &6Envoyez de l'argent à un joueur.");
			return;
		}
		if (!profileManager.profileExist(args.getArgs(0))) {
			profile.msg("&cCe joueur est introuvable.");
			return;
		}
		Profile target = profileManager.getProfile(args.getArgs(0));
		Double value = Doubles.tryParse(args.getArgs(1)); // faster...
		if (value == null) {  // ...check
			profile.msg("&eLa somme '&c%1$s&e' est invalide.", args.getArgs(1));
			return;
		}
		if (profile.equals(target)) { // self action
			profile.msg("&cVous ne pouvez pas faire ceci à vous même ...");
			return;
		}
		if (value <= 0) {
			profile.msg("&eMerci de spécifier une somme supérieur à 0.");
			return;
		}
		if (value > balance) {
			profile.msg("&eVous ne pouvez pas envoyer &f%1$s&e, vous n'avez uniquement que &f%2$s&e.", value, balance);
			return;
		}
		
		profile.withdraw(value);
		profile.msg("&eVous venez d'envoyer &f%1$s &eau joueur &f%2$s&e.", value, target.getPlayerName());
		
		target.deposit(value);
		target.msg("&eLe joueur &f%1$s &evient de vous envoyez &f%2$s&e.", profile.getPlayerName(), value);
	}
}
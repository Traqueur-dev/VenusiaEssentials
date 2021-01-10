package fr.traqueur.venusiaessentials.modules.economy.commands;


import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;
import net.minecraft.util.com.google.common.primitives.Doubles;

public class MoneyAdminCommand extends ICommand {

	@Command(name = "bal.give", aliases = {"balance.give", "money.give", "argent.give"}, permission = "base.money.admin")
	public void onCommand(CommandArgs args) {
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(args.getPlayer().getName());
		
		if (args.length() < 2) {
			profile.msg("&e/money give <joueur> <valeur>");
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
		if (value <= 0) {
			profile.msg("&eMerci de spécifier une somme supérieur à 0.");
			return;
		}
		
		target.deposit(value);
		profile.msg("&eVous venez de &9give &c%1$s &eau joueur &f%2$s&e.", value, target.getPlayerName());
	}
	
	@Command(name = "bal.set", aliases = {"balance.set", "money.set", "argent.set"}, permission = "base.money.admin")
	public void onMoneySet(CommandArgs args) {
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(args.getPlayer().getName());
		
		if (args.length() < 2) {
			profile.msg("&e/money set <joueur> <valeur>");
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
		if (value <= 0) {
			profile.msg("&eMerci de spécifier une somme supérieur à 0.");
			return;
		}
		
		target.setBalance(value);
		profile.msg("&eVous venez &9définir &el'argent du joueur &f%1$s &eà &c%2$s", target.getPlayerName(), target.getBalance());
	}

}

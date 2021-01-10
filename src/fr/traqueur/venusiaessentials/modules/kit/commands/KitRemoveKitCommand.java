package fr.traqueur.venusiaessentials.modules.kit.commands;

import org.bukkit.command.CommandSender;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.kit.Kit;
import fr.traqueur.venusiaessentials.modules.kit.KitModule;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;
import fr.traqueur.venusiaessentials.modules.profiles.server.clazz.TemporaryKit;

public class KitRemoveKitCommand extends ICommand {

	@Command(name = "kit.removekit", permission = "base.kit.removekit", inGameOnly = false)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();
		ProfileModule profileManager = ProfileModule.getInstance();

		if (args.length() < 2) {
			sender.sendMessage(Utils.color("&e/kit removekit <joueur> <kit>"));
			return;
		}
		Profile target = profileManager.getProfile(args.getArgs(0));
		if (target == null) {
			sender.sendMessage(Utils.color("&eLe joueur '&c" + args.getArgs(0) + "&e' est introuvable."));
			return;
		}
		Kit kit = KitModule.getInstance().getKit(args.getArgs(1));
		if (kit == null) {
			sender.sendMessage(Utils.color("&eLe kit '&c" + args.getArgs(1) + "&e' n'existe pas."));
			return;
		}
		TemporaryKit temporaryKit = target.getTemporaryKit(kit);
		if (temporaryKit == null) {
			sender.sendMessage(Utils.color("&eCe joueur ne detient pas le kit '&c" + args.getArgs(1) + "&e'."));
			return;
		}
		target.getTemporaryKits().remove(temporaryKit);
		sender.sendMessage(Utils.color("&eVous venez de &csupprimer &ele kit &6&l" + kit.getName() + " &edu joueur &c"
				+ target.getPlayerName() + "&e."));
	}

}

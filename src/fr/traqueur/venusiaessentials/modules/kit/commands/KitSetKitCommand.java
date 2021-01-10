package fr.traqueur.venusiaessentials.modules.kit.commands;

import org.bukkit.command.CommandSender;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.JavaUtils;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.kit.Kit;
import fr.traqueur.venusiaessentials.modules.kit.KitModule;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class KitSetKitCommand extends ICommand {

	@Command(name = "kit.setkit", permission = "base.kit.setkit", inGameOnly = false)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();
		ProfileModule profileManager = ProfileModule.getInstance();

		if (args.length() < 3) {
			sender.sendMessage(Utils.color("&e/kit setkit <joueur> <kit> <durée>"));
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
		long delay = JavaUtils.parse(args.getArgs(2));
		if (delay == -1L) {
			sender.sendMessage(Utils.color("&eCe délais est invalide."));
			return;
		}
		KitModule.getInstance().addTemporaryKit(target, kit, delay);
		sender.sendMessage(Utils.color("&eLe joueur &6&l" + target.getPlayerName() + " &eà maintenant le kit &6"
				+ kit.getName() + " &epour &f" + args.getArgs(2) + "&e."));
	}

}

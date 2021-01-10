package fr.traqueur.venusiaessentials.modules.kit.commands;

import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.kit.Kit;
import fr.traqueur.venusiaessentials.modules.kit.KitModule;

public class KitCommand extends ICommand {

	@Command(name = "kit")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();

		if (args.length() < 1) {
			player.sendMessage(Utils.LINE);
			player.sendMessage(Utils.color("&c- &e/kit <nom> &7- &6Utiliser un Kit"));
			player.sendMessage(Utils.color("&c- &e/kit list &7- &6Liste de vos Kits"));
			player.sendMessage(Utils.color("&c- &e/kit preview <nom> &7- &6Afficher un Kit"));
			player.sendMessage(Utils.LINE);
			if (player.isOp()) {
				player.sendMessage(Utils.color("&e/kit create <nom> <délais> [Utilisation maximal]"));
				player.sendMessage(Utils.color("&e/kit delete <nom>"));
				player.sendMessage(Utils.color("&e/kit setkit <joueur> <kit> <durée>"));
				player.sendMessage(Utils.color("&e/kit removekit <joueur> <kit>"));
				player.sendMessage(Utils.color("&e/kit listkit <joueur>"));
			}
		} else {
			KitModule manager = KitModule.getInstance();
			Kit kit = manager.getKit(args.getArgs(0));
			if (kit == null) {
				player.sendMessage(Utils.color("&eLe kit '&c" + args.getArgs(0) + "&e' n'existe pas."));
				return;
			}
			if (!kit.hasPermission(player)) {
				player.sendMessage(Utils.color("&eVous n'avez pas accés à ce kit."));
				return;
			}
			kit.applyPlayer(player, player.isOp());
		}
	}

}

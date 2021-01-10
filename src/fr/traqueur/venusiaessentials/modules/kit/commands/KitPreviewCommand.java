package fr.traqueur.venusiaessentials.modules.kit.commands;


import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.kit.Kit;
import fr.traqueur.venusiaessentials.modules.kit.KitModule;
import fr.traqueur.venusiaessentials.modules.kit.gui.KitPreviewInventory;

public class KitPreviewCommand extends ICommand {

	@Command(name="kit.preview")
	public void onCommand(CommandArgs args) {		
		Player player = args.getPlayer();
		KitModule manager = KitModule.getInstance();
		
		if (args.length() < 1) {
			player.sendMessage(Utils.color("&e/kit preview <nom> &7- &6Afficher un Kit"));
			return;
		}
		
		Kit kit = manager.getKit(args.getArgs(0));
		if (kit == null) {
			player.sendMessage(Utils.color("&eLe kit '&c" + args.getArgs(0) + "&e' n'existe pas."));
			return;
		}

		KitPreviewInventory.openInv(player, kit);
		
		player.sendMessage(Utils.color("&eAffichage du &6Kit &c" + kit.getName() + "&e."));
	}

}

package fr.traqueur.venusiaessentials.modules.kit.commands;

import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.kit.Kit;
import fr.traqueur.venusiaessentials.modules.kit.KitModule;

public class KitDeleteCommand extends ICommand {


	@Command(name = "kit.delete", permission = "base.kit.delete")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		KitModule manager = KitModule.getInstance();
		
		if (args.length() < 1) {
			player.sendMessage(Utils.color("&e/kit delete <nom>"));
			return;
		}
		
		Kit kit = manager.getKit(args.getArgs(0));
		if (kit == null) {
			player.sendMessage(Utils.color("&eLe kit '&c" + args.getArgs(0) + "&e' n'existe pas."));
			return;
		}
		manager.getKits().remove(kit.getId());
		player.sendMessage(Utils.color("&eVous venez de supprimer le kit &c" + kit.getName() + "&e."));
	}

}

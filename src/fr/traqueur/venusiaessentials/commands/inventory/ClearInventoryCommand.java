package fr.traqueur.venusiaessentials.commands.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class ClearInventoryCommand extends ICommand {

	@Command(name = "clearinventory", aliases = {"ci","clear"}, permission = "base.clear.inventory")
	public void onCommand(CommandArgs args) {
		Player sender = args.getPlayer();

		if (args.length() < 1) {
			sender.getInventory().clear();
			sender.updateInventory();
			sender.sendMessage(Utils.color("&eVotre &6inventaire&r &evient d'être nettoyé."));
		} else {
			Player target  = Bukkit.getPlayer(args.getArgs(0));
			if (target == null) {
				sender.sendMessage(Utils.color("&eCe joueur est introuvable."));
				return;
			}		
			target.getInventory().clear();
			target.updateInventory();
			sender.sendMessage(Utils.color("&eVous venez de nettoyer l'inventaire du joueur &c" + target.getName() + "&e."));
		}
	}
}

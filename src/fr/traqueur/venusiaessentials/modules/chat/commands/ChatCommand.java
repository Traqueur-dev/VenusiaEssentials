package fr.traqueur.venusiaessentials.modules.chat.commands;

import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.BaseCommand;

public class ChatCommand extends BaseCommand {

	@Command(name = "chat", aliases = {"tchat"})
	public void onCommand(CommandArgs args) {
		Player sender = args.getPlayer();
		sender.sendMessage(Utils.LINE);
		sender.sendMessage(Utils.color("&c&l• &e/chat enable &7- &6Activer/Désactiver le chat public"));
		sender.sendMessage(Utils.color("&c&l• &e/chat slow <temps en seconde> &7- &6Ralentir le chat public"));
		sender.sendMessage(Utils.color("&c&l• &e/chat clear &7- &6Nettoyer le chat"));
		sender.sendMessage(Utils.LINE);
	}

}

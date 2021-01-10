package fr.traqueur.venusiaessentials.commands.essentials;

import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class HelpCommand extends ICommand {

	@Command(name = "help", aliases =  {"aide", "aides" })
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		player.sendMessage(Utils.color(Utils.LINE));
		player.sendMessage("");
		player.sendMessage("§eEh oh c'est bon frére j'ai trop la flemme de t'afficher");
		player.sendMessage("§eLe /help y'a trop d'truc ....");
		player.sendMessage("");
		player.sendMessage(Utils.color(Utils.LINE));
	}
}

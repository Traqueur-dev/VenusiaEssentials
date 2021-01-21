package fr.traqueur.venusiaessentials.modules.chat.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.VenusiaEssentials;
import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.BaseCommand;

public class ChatClearCommand extends BaseCommand {

	@Command(name = "chat.clear", aliases = {"tchat.clear"}, permission = "chat.clear")
	public void onCommand(CommandArgs args) {
		for(Player player: Utils.getOnlinePlayers()) {
			for (int i = 0; i < 100; i++) {
				player.sendMessage(" ");
			}
		}
		Bukkit.broadcastMessage(Utils.getPrefix(VenusiaEssentials.PREFIX) + Utils.color("&eLe tchat public vient d'être &cclear&e."));
	}

}

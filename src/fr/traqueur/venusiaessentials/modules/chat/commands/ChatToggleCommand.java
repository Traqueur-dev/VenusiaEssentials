package fr.traqueur.venusiaessentials.modules.chat.commands;

import org.bukkit.Bukkit;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.BaseCommand;
import fr.traqueur.venusiaessentials.modules.chat.ChatModule;

public class ChatToggleCommand extends BaseCommand {

	@Command(name = "chat.toggle", aliases = { "tchat.toggle", "tchat.enable","chat.enable" }, permission = "chat.toggle")
	public void onCommand(CommandArgs args) {
		ChatModule module = ChatModule.getInstance();
		boolean newStatut = !module.getChat().isEnabled();
		module.getChat().setEnabled(newStatut);
		Bukkit.broadcastMessage(
				Utils.color("&eLe tchat public vient d'être " + (!newStatut ? "&cdésactivé" : "&aactivé") + "&e."));
	}

}

package fr.traqueur.venusiaessentials.commands.server;

import fr.traqueur.venusiaessentials.api.Plugin;
import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;

public class SaveCommand extends ICommand {

	@Command(name = "save", aliases = {"sauvegarde", "sauvegardes" }, inGameOnly = false, permission = "base.save")
	public void onCommand(CommandArgs args) {
		Plugin.getInstance().save();
	}
}

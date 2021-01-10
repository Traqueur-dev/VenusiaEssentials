package fr.traqueur.venusiaessentials.modules.clearlag;

import org.bukkit.Bukkit;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class ClearLagCommand extends ICommand {

	@Command(name = "clearlag", aliases = {"cl"}, inGameOnly = false, permission = "base.clearlag.use")
	public void onCommand(CommandArgs args) {
		ClearLagModule clearLagManager = ClearLagModule.getInstance();
		int i = clearLagManager.removeEntities();
		Bukkit.broadcastMessage(Utils.getPrefix("Lag") + "§eSuppression de §6" + i + " §eentitée(s)");
	}
}

package fr.traqueur.venusiaessentials.commands.essentials;

import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;

public class CraftCommand extends ICommand {

	@Command(name = "craft", aliases = {"workbench", "crafting"}, permission = "base.craft")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		player.openWorkbench(player.getLocation(), true);
	}
}

package fr.traqueur.venusiaessentials.commands.essentials;

import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class SuicideCommand extends ICommand {

	@Command(name = "suicide", permission = "base.suicide")
	public void onCommand(CommandArgs args) {
		Player sender = args.getPlayer();		
		sender.setHealth(0.0);
		sender.sendMessage(Utils.color("&eVous venez de vous &6suicider&e."));		
	}
}

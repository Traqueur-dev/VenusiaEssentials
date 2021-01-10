package fr.traqueur.venusiaessentials.modules.profiles.commands;

import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class WelcomeCommand extends ICommand {

	@Command(name = "welcome", aliases = {"bienvenue","b"})
	public void onCommand(CommandArgs args) {
		ProfileModule profileManager = ProfileModule.getInstance();
		Player player = args.getPlayer();
		if(profileManager.getNewPlayer() == null) {
			player.sendMessage("§cIl n'y a pas eu de nouveau joueur récemment.");
			return;
		}
		player.chat("§eBienvenue §esur §6§lVenusia §d" + profileManager.getNewPlayer().getName() + "§e!");
	}
}

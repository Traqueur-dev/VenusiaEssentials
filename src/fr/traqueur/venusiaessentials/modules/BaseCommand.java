package fr.traqueur.venusiaessentials.modules;

import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public abstract class BaseCommand extends ICommand {

	private ProfileModule profileManager = ProfileModule.getInstance();

	public Profile getProfile(String string) {
		return profileManager.getProfile(string);
	}

	public Profile getProfile(Player player) {
		return profileManager.getProfile(player.getName());
	}

}

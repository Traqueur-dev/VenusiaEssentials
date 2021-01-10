package fr.traqueur.venusiaessentials.commands.admin;

import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class VanishCommand extends ICommand {
	
	@Command(name = "vanish", aliases ="v", permission = "base.vanish")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(player.getName());
		
		boolean newStatut = !profile.isVanish();
		profile.setVanish(newStatut);
		for (Player pls : Utils.getOnlinePlayers()) {
			if (pls.hasPermission("base.vanish")) continue;
			
			if (newStatut) pls.hidePlayer(player);
			else pls.showPlayer(player);
		}
		
		player.sendMessage(Utils.color("&eMode &6Vanish " + (newStatut ? "&aON" : "&cOFF")));
	}

}

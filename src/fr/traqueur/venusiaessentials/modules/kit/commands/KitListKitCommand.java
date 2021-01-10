package fr.traqueur.venusiaessentials.modules.kit.commands;

import org.bukkit.command.CommandSender;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.DurationFormatter;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;
import fr.traqueur.venusiaessentials.modules.profiles.server.clazz.TemporaryKit;

public class KitListKitCommand extends ICommand {

	@Command(name = "kit.listkit", permission = "base.kit.listkit", inGameOnly = false)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();
		ProfileModule profileManager = ProfileModule.getInstance();
		if (args.length() < 1) {
			sender.sendMessage(Utils.color("&e/kit listkit <joueur>"));
			return;
		}
		Profile target = profileManager.getProfile(args.getArgs(0));
		if (target == null) {
			sender.sendMessage(Utils.color("&eLe joueur '&c"+ args.getArgs(0) +"&e' est introuvable."));
			return;
		}
		if (target.getTemporaryKits().isEmpty()) {
			sender.sendMessage(Utils.color("&eCe joueur n'a pas de kit temporaire."));
			return;
		}
		for (TemporaryKit kit : target.getTemporaryKits()) {
			sender.sendMessage(Utils.color("&6- &e"+kit.getKitName() 
				+" &7[&6" + DurationFormatter.getRemaining(kit.getExpirationMillis() - System.currentTimeMillis(), true) +"&7]"));
		}
		
	}

}

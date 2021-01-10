package fr.traqueur.venusiaessentials.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class FlyCommand extends ICommand {

	@Command(name ="fly", permission = "base.fly", inGameOnly = false)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();
		ProfileModule profileManager = ProfileModule.getInstance();

		Player target = null;

		if (args.length() < 1) {

			if (!args.isPlayer()) {
				sender.sendMessage(Utils.color("&e/fly <joueur>"));
				return;
			}

			target = args.getPlayer();
		} else {

			if (Bukkit.getPlayer(args.getArgs(0)) == null) {
				sender.sendMessage(Utils.color("&eCe joueur est introuvable."));
				return;
			}

			target = Bukkit.getPlayer(args.getArgs(0));

			if (args.length() == 2) {

				String bool = args.getArgs(1);

				boolean newFlight;
				try {
					newFlight = Utils.stringToBool(bool);
				} catch (IllegalArgumentException e) {
					sender.sendMessage("§cVeuillez utiliser 'true' ou 'false'.");
					return;
				}

				target.setAllowFlight(newFlight);
				profileManager.getProfile(target.getName()).setFlyMode(newFlight);
				sender.sendMessage(Utils.color("&eLe joueur &6" + target.getName() + " &e"
						+ (newFlight ? "&aest maintenant" : "&cn'est maintenant plus") + " &een mode &6Fly&e."));

				return;
			}
		}

		boolean newFlight = !target.getAllowFlight();
		target.setAllowFlight(newFlight);
		profileManager.getProfile(target.getName()).setFlyMode(newFlight);
		if (newFlight) {
			target.setFlying(true);
		}
		sender.sendMessage(Utils.color("&eLe joueur &6" + target.getName() + " &e"
				+ (newFlight ? "&aest maintenant" : "&cn'est maintenant plus") + " &een mode &6Fly&e."));
	}

}

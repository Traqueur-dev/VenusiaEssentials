package fr.traqueur.venusiaessentials.modules.punishments;

import java.util.concurrent.TimeUnit;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class PunishCommand extends ICommand {

	@Command(name = "punish", aliases = {"pu"}, permission = "punish.use", inGameOnly = false)
	public void onCommand(CommandArgs args) {
		if (args.getArgs().length >= 1) {
			String playerName = args.getArgs(0);

			if (ProfileModule.getInstance().profileExist(playerName)) {

				Profile userProfile = ProfileModule.getInstance().getProfile(playerName);

				if (args.getArgs().length == 2) {
					if (PunishmentReasons.exists(args.getArgs(1))) {

						PunishmentReasons punishmentReasons = PunishmentReasons.valueOf(args.getArgs(1).toUpperCase());
						userProfile.punish(new Punishment(userProfile.getPlayerName(),
								punishmentReasons.getPunishmentType(), punishmentReasons.getName(),
								args.getSender().getName(),
								punishmentReasons.getTimeInMinutes() == -1 ? -1
										: System.currentTimeMillis()
												+ TimeUnit.MINUTES.toMillis(punishmentReasons.getTimeInMinutes())));

						args.getSender().sendMessage(Utils.LINE);
						args.getSender().sendMessage("§a" + userProfile.getPlayerName() + " §evient d'être §cpuni§e.");
						args.getSender().sendMessage(Utils.LINE);
					} else {
						args.getSender().sendMessage(Utils.LINE);
						args.getSender().sendMessage("§cCette raison n'existe pas !");
						args.getSender().sendMessage(Utils.LINE);
					}
				} else {
					args.getSender().sendMessage(Utils.LINE);
					args.getSender().sendMessage("§ePunitions de " + playerName);
					args.getSender().sendMessage(" ");

					if (!userProfile.getPunishments().isEmpty()) {
						for (Punishment punishment : userProfile.getPunishments()) {
							args.getSender()
									.sendMessage("§6- " + punishment.getPunishmentType().getName() + " "
											+ (punishment.isExpired() ? "Expiré" : " Non-Expiré") + " par "
											+ punishment.getPunisher() + "§6: §e" + punishment.getReason());
						}
					} else {
						args.getSender().sendMessage("§cCe profil n'a aucune punition.");
					}

					args.getSender().sendMessage(Utils.LINE);
					return;
				}
			} else {
				args.getSender().sendMessage(Utils.LINE);
				args.getPlayer().sendMessage("§cCette personne n'a pas de profil !");
				args.getSender().sendMessage(Utils.LINE);
			}
		} else {
			args.getSender().sendMessage(Utils.LINE);
			args.getSender().sendMessage("§eSanctions");
			args.getSender().sendMessage(" ");
			args.getSender().sendMessage("§6/punish §7<joueur> <punition>.");
			args.getSender().sendMessage(Utils.LINE);
		}
	}

	@Command(name = "unpunish", aliases = {"unpu"}, permission = "punish.use", inGameOnly = false)
	public void onUnpunishCommand(CommandArgs args) {
		if (args.getArgs().length == 1) {
			String playerName = args.getArgs(0);

			if (ProfileModule.getInstance().profileExist(playerName)) {

				Profile userProfile = ProfileModule.getInstance().getProfile(playerName);

				userProfile.getPunishments().clear();
				userProfile.getNonExpiredPunishments().clear();

				args.getSender().sendMessage(Utils.LINE);
				args.getSender().sendMessage(
						"§a" + userProfile.getPlayerName() + " §evient de voir ses punitions §aenlevées§e.");
				args.getSender().sendMessage(Utils.LINE);
			} else {
				args.getSender().sendMessage(Utils.LINE);
				args.getPlayer().sendMessage("§cCette personne n'a pas de profil !");
				args.getSender().sendMessage(Utils.LINE);
			}
		}
	}
}

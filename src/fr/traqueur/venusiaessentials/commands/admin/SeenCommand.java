package fr.traqueur.venusiaessentials.commands.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import fr.traqueur.venusiaessentials.VenusiaEssentials;
import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.DurationFormatter;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;
import fr.traqueur.venusiaessentials.modules.punishments.Punishment;

public class SeenCommand extends ICommand {

	@Command(name = "seen", aliases ="history", permission = "base.seen", inGameOnly = false)
	public void onCommand(CommandArgs args) {
		final CommandSender sender = args.getSender();
		ProfileModule profileManager = ProfileModule.getInstance();
		if (args.length() < 1) {
			sender.sendMessage(Utils.color("&e/seen <joueur>"));
			return;
		}
		final Profile profile = profileManager.getProfile(args.getArgs(0));
		if (profile == null) {
			sender.sendMessage(Utils.color("&eCe joueur est introuvable."));
			return;
		}

		sender.sendMessage(Utils.LINE);
		sender.sendMessage(Utils.color(
				"&c• &eJoueur&7: &6" + profile.getPlayerName() + " " + (profile.isBanned() ? "&c[Banni ✖]" : "")));
		sender.sendMessage(Utils.color("&c• &eUUID&7: &6" + profile.getPlayerId().toString()));
		sender.sendMessage(Utils.color("&c• &eIP&7: &6" + profile.getIpAddress()));
		sender.sendMessage(Utils.color("&c• &eEn prison&7: &6" + (profile.isJail() ? "Oui" : "Non")));
		sender.sendMessage(Utils.color("&c• &eDernière connexion&7: &6" + (profile.isOnline() ? "&aEn ligne"
				: DurationFormatter.getDurationDate(profile.getLastConnectionMillis()))));
		if (!profile.isOnline())
			sender.sendMessage(Utils.color("&c• &eDernière localisation&7: &fX"
					+ (profile.getLogoutLocation().getBlockX() + ", Y" + profile.getLogoutLocation().getBlockY() + ", Z"
							+ profile.getLogoutLocation().getBlockZ())));

		sender.sendMessage(Utils.color("&c• &eSanctions&7: " + (profile.getPunishments().isEmpty() ? "&6Aucune" : "")));

		for (Punishment punish : profile.getNonExpiredPunishments()) {
			sender.sendMessage(Utils.color("&6» &e&l" + punish.getPunishmentType().name() + " &6par " + "&f"
					+ punish.getPunisher() + " &6le &f" + punish.getDate() + "&6, "
					+ 
					(punish.getTime() == -1 ? "durée &cdéfinitive" : " prendra §afin §6le §c" + DateFormatUtils.format(punish.getTime(), "dd/MM/yyyy | HH:mm:ss")) + "&6."));
		}

		// double accounts checks
		final List<String> acc = new ArrayList<>();
		Bukkit.getScheduler().runTaskAsynchronously(VenusiaEssentials.getInstance(), new Runnable() {
			@Override
			public void run() {
				for (Profile p : ProfileModule.getInstance().getProfiles()) {
					if (!p.getIpAddress().equalsIgnoreCase(profile.getIpAddress())
							|| p.getPlayerName().equalsIgnoreCase(profile.getPlayerName()))
						continue;
					acc.add((p.isBanned() ? "&c" : "&a") + "[" + p.getPlayerName() + "] ");
				}
				if (acc.isEmpty()) {
					acc.add("Aucun");
				}
				sender.sendMessage(Utils.color("&c• &eDouble comptes&7: " + StringUtils.join(acc, ' ')));
				sender.sendMessage(Utils.LINE);
			}
		});
	}
}

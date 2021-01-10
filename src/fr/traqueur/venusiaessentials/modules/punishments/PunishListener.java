package fr.traqueur.venusiaessentials.modules.punishments;

import org.apache.commons.lang.time.DateFormatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

@SuppressWarnings("deprecation")
public class PunishListener implements Listener {

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {

		Player player = event.getPlayer();
		Profile userProfile = ProfileModule.getInstance().getProfile(player.getName());

		if (!userProfile.getPunishments().isEmpty()) {
			userProfile.refreshNonExpiredPunishments();
		}

		if (!userProfile.getNonExpiredPunishments().isEmpty()) {

			if (userProfile.isMuted()) {
				Punishment punishment = userProfile.getNonExpiredPunishments().stream()
						.filter(p -> p.getPunishmentType() == PunishmentType.MUTE).findFirst().orElse(null);

				if (punishment == null) {
					return;
				}

				event.setCancelled(true);

				event.getPlayer().sendMessage(Utils.LINE);
				event.getPlayer().sendMessage("§eSanctions");
				event.getPlayer().sendMessage(" ");
				event.getPlayer().sendMessage("§eVous vous êtes fait §6mute §epar §c" + punishment.getPlayerName()
						+ "§e pour \"§a" + punishment.getReason() + "§e\".");
				event.getPlayer().sendMessage(" ");
				event.getPlayer()
						.sendMessage(punishment.getTime() == -1 ? "§eCette sanction est §cdéfinitive§e."
								: "§eCette §csanction §eprendra §afin§e le: §c"
										+ DateFormatUtils.format(punishment.getTime(), "dd/MM/yyyy | HH:mm:ss")
										+ "§e.");
				event.getPlayer().sendMessage(" ");
				event.getPlayer().sendMessage(
						"§cEn cas de problèmes avec cette sanction, contactez un contactez un membre du personnel.");
				event.getPlayer().sendMessage(Utils.LINE);
			}
		}
	}
	
	@EventHandler
	public void onPreLoginEvent(PlayerPreLoginEvent event) {
		String player = event.getName();

		Profile profile = ProfileModule.getInstance().getProfile(player);

		if (profile == null) {
			return;
		}

		if (!profile.getNonExpiredPunishments().isEmpty()) {

			if (profile.isBanned()) {
				Punishment punishment = profile.getNonExpiredPunishments().stream()
						.filter(p -> p.getPunishmentType() == PunishmentType.BAN).findFirst().orElse(null);

				if (punishment == null) {
					return;
				}
				event.disallow(PlayerPreLoginEvent.Result.KICK_BANNED, Utils.LINE + "\n§6Venusia\n§eVous vous êtes fait §6bannir §epar §c"
						+ punishment.getPunisher() + "§e pour \"§a" + punishment.getReason() + "§e\".\n\n"
						+ (punishment.getTime() == -1 ? "§eCette sanction est §cdéfinitive§e."
								: "§eCette §csanction §eprendra §afin§e le: §c"
										+ DateFormatUtils.format(punishment.getTime(), "dd/MM/yyyy | HH:mm:ss") + "§e.")
						+ "\n\n§cEn cas de problèmes avec cette sanction, contactez un membre du personnel.\n"
						+ Utils.LINE);
			}
		}
	}
}

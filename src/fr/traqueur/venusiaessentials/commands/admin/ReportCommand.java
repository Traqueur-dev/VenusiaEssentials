package fr.traqueur.venusiaessentials.commands.admin;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;

public class ReportCommand extends ICommand {

	@Command(name = "report")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();

		if (args.length() < 2) {
			player.sendMessage(Utils.color("&e/report <joueur> <raison>"));
			return;
		}
		Player target = Bukkit.getPlayer(args.getArgs(0));
		if (target == null) {
			player.sendMessage(Utils.color("&eCe joueur est introuvable."));
			return;
		}
		if (!this.checkCooldown(player, 60)) {
			return;
		}
		if (player.getUniqueId().equals(target.getUniqueId())) {
			player.sendMessage(Utils.color("&eVous ne pouvez pas vous report vous même."));
			return;
		}
		String reason = StringUtils.join(args.getArgs(), ' ', 1, args.length());

		player.sendMessage(Utils.color(
				"&eVous §avenez §ede §creport §ele joueur §6" + target.getName() + " §epour §7" + reason + "§e."));

		for (Player pls : Utils.getOnlinePlayers()) {
			if (pls.hasPermission("base.report")) {
				pls.sendMessage(Utils.color("&8[&6§lStaff&8] &eLe joueur &6" + player.getName()
						+ " &evient de report &c" + target.getName() + " &epour &7" + reason));
			}
		}
	}

}

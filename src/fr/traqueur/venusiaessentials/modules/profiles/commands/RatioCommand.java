package fr.traqueur.venusiaessentials.modules.profiles.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.BaseCommand;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;

public class RatioCommand extends BaseCommand {

	@Command(name = "ratio", aliases = {"ks", "stats", "statistic", "statistics", "statistique", "statistiques"})
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		Player target = null;
		if (args.length() > 0) {
			String targetName = args.getArgs(0);
			Player current = Bukkit.getPlayer((String) targetName);
			if (current == null) {
				player.sendMessage(Utils.color((String) "&eD\u00e9sol\u00e9, le joueur cibl\u00e9 est introuvable!"));
				return;
			}
			target = current;
		} else {
			target = player;
		}
		this.displayRatio(player, target);
	}

	public void displayRatio(Player player, Player target) {
		Profile targetProfile = getProfile(target.getName());
		if (targetProfile == null) {
			player.sendMessage(
					Utils.color((String) "&eD\u00e9sol\u00e9, le profile le joueur cibl\u00e9 est introuvable!"));
			return;
		}
		player.sendMessage("");
		player.sendMessage(Utils.LINE);
		player.sendMessage(Utils.color((String) ("&c&l\u00bb &eRatio du joueur &6" + targetProfile.getPlayerName())));
		player.sendMessage("");
		player.sendMessage(Utils.color((String) ("&6Kills&7: " + targetProfile.getKills())));
		player.sendMessage(Utils.color((String) ("&cMorts&7: " + targetProfile.getDeaths())));
		player.sendMessage("");
		player.sendMessage(Utils.color((String) ("&eRatio &6K&7/&cD&7: " + RatioCommand.getRatio(targetProfile))));
		player.sendMessage(Utils.LINE);
		player.sendMessage("");
	}

	public static double getRatio(Profile profile) {
		int kills = profile.getKills();
		int deaths = profile.getDeaths();
		if (deaths >= 1) {
			return kills / deaths;
		}
		return kills;
	}
}

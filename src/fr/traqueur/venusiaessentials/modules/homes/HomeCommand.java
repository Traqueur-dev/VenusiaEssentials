package fr.traqueur.venusiaessentials.modules.homes;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.BaseCommand;
import fr.traqueur.venusiaessentials.modules.groups.Group;
import fr.traqueur.venusiaessentials.modules.groups.GroupModule;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;
import fr.traqueur.venusiaessentials.modules.teleports.TeleportModule;

public class HomeCommand extends BaseCommand {

	@Command(name ="home")
	public void onCommand(CommandArgs args) {
		ProfileModule profileManager = ProfileModule.getInstance();
		TeleportModule teleportManager = TeleportModule.getInstance();

		Profile profile = profileManager.getProfile(args.getPlayer().getName());

		if (args.length() < 1) {

			if (profile.getHomes().isEmpty()) {
				profile.msg("&eVous n'avez aucun &6Home&e.");
				return;
			}
			String homes = "";
			for (Home home : profile.getHomes()) {
				homes += "&f" + home.getName() + "&7, ";
			}

			homes = homes.substring(0, homes.length() - 2) + "&7.";

			profile.msg(Utils.LINE);
			profile.msg("&e» &6Home&7: " + homes);
			profile.msg(Utils.LINE);
			return;
		} else {
			String name = args.getArgs(0);
			Home to = null;
			for (Home home : profile.getHomes()) {
				if (home.getName().equalsIgnoreCase(name)) {
					to = home;
					break;
				}
			}
			if (to == null) {
				profile.msg("&eLe &6Home &e'&c" + name + "&e' est introuvable.");
				return;
			}
			teleportManager.handleTeleport(profile.getPlayer(), to.getLocation());
		}
	}

	@Command(name ="sethome")
	public void sethomeCmd(CommandArgs args) {
		Player player = args.getPlayer();

		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(player.getName());

		if (args.length() < 1) {
			profile.msg("&e/sethome <nom>");
			return;
		}
		String name = args.getArgs(0);
		for (Home home : profile.getHomes()) {
			if (home.getName().equalsIgnoreCase(name)) {
				profile.msg("&eUn &6Home &eportant le nom de '&c" + name + "&e' existe déjà.");
				return;
			}
		}
		if (profile.getHomes().size() >= getMaxRank(player)) {
			profile.msg("&eVous avez atteint le nombre maximales de &6Home &epour votre grade.");
			return;
		}
		Home home = new Home(name, player.getLocation());
		profile.getHomes().add(home);
		player.sendMessage(Utils.color("&eVous venez de définir le &6Home &e'&c" + name + "&e'&e."));
	}

	@Command(name ="delhome")
	public void delhomeCmd(CommandArgs args) {
		Player player = args.getPlayer();

		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(player.getName());

		if (args.length() < 1) {
			profile.msg("&e/delhome <nom>");
			return;
		}
		if (args.length() > 1) {

			if (!player.hasPermission("base.home.delothers")) {
				profile.msg("&e/delhome <nom>");
				return;
			}
			Profile target = profileManager.getProfile(args.getArgs(0));
			String homeName = args.getArgs(1);

			if (target == null) {
				player.sendMessage(Utils.color("&cCe joueur est introuvable."));
				return;
			}
			Home remove = null;
			for (Home home : target.getHomes()) {
				if (home.getName().equalsIgnoreCase(homeName)) {
					remove = home;
					break;
				}
			}
			if (remove == null) {
				player.sendMessage(Utils.color("&eLe &6Home &e'&c" + homeName + "&e' est introuvable pour le joueur &c"
						+ target.getPlayerName() + "&e."));
				return;
			}

			target.getHomes().remove(remove);
			player.sendMessage(Utils.color("&eVous venez de supprimer le &6Home &c" + homeName + " &edu joueur &c"
					+ target.getPlayerName() + "&e."));
		} else {
			String name = args.getArgs(0);
			Home remove = null;
			for (Home home : profile.getHomes()) {
				if (home.getName().equalsIgnoreCase(name)) {
					remove = home;
					break;
				}
			}
			if (remove == null) {
				profile.msg("&eLe &6Home &e'&c" + name + "&e' est introuvable.");
				return;
			}
			profile.getHomes().remove(remove);
			profile.msg("&eVous venez de supprimer le &6Home &e'&c" + name + "&e'&e.");
		}
	}

	@Command(name = "homelist", aliases = "homeshow", permission  = "base.home.show")
	public void homeShowCmd(CommandArgs args) {
		Player player = args.getPlayer();

		ProfileModule profileManager = ProfileModule.getInstance();

		if (args.length() < 1) {
			player.sendMessage(Utils.color("&e/homeshow <pseudo>"));
			return;
		}
		String name = args.getArgs(0);
		Profile target = profileManager.getProfile(name);
		if (target == null) {
			player.sendMessage(Utils.color("&cCe joueur est introuvable."));
			return;
		}
		if (target.getHomes().isEmpty()) {
			player.sendMessage(Utils.color("&eCe joueur n'a aucun &6Home&e."));
			return;
		}
		player.sendMessage(Utils.LINE);
		player.sendMessage(Utils.color("&e» &6Homes &edu joueur &c" + name + "&7:"));

		for (Home home : target.getHomes()) {
			Location loc = home.getLocation();
			String coords = "";

			coords += "&6X: &c" + loc.getBlockX() + "&7, &6Y: &c" + loc.getBlockY() + "&7, &6Z: &c" + loc.getBlockZ();

			player.sendMessage(Utils.color("&6- &eNom&7: &c" + home.getName()));
			player.sendMessage(Utils.color("&eLocalisation&7: " + coords));
		}
		player.sendMessage(Utils.LINE);
	}

	public int getMaxRank(Player player) {
		if (player.hasPermission("base.home.infinity")) {
			return Integer.MAX_VALUE;
		}

		Group g = GroupModule.getInstance().getByName(getProfile(player).getGroup());
		for(String p : g.getPermissions()) {
			if(p.contains("base.home")) {
				return Integer.parseInt(p.split(".")[3]);
			}
		}
		return 2;
	}

}

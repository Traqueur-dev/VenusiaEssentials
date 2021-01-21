package fr.traqueur.venusiaessentials.modules.teleports;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.JavaUtils;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.BaseCommand;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class TeleportCommand extends BaseCommand {

	@Command(name = "teleport", aliases = {"tp" }, permission = "base.teleport", inGameOnly = false)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();

		if (args.length() < 1) {
			sender.sendMessage(Utils.color("&e/tp <joueur|coordonées> [joueur]"));
			return;
		}
		if (args.length() < 3) {
			Player target = Bukkit.getPlayer(args.getArgs(0));
			if (target == null) {
				sender.sendMessage(Utils.color("&eCe joueur est introuvable."));
				return;
			}
			if (args.length() > 1) {
				Player t2 = Bukkit.getPlayer(args.getArgs(1));
				if (t2 == null) {
					sender.sendMessage(Utils.color("&eCe joueur est introuvable."));
					return;
				}
				target.teleport(t2, TeleportCause.COMMAND);
				sender.sendMessage(
						Utils.color("&eVous venez de téléporté &c" + target.getName() + " &eà &c" + t2.getName()));
			} else {
				if (!args.isPlayer()) {
					sender.sendMessage(Utils.color("&e/tp <joueur> <joueur>"));
					return;
				}
				args.getPlayer().teleport(target, TeleportCause.COMMAND);
				sender.sendMessage(Utils.color("&eVous venez de vous téléporté à &c" + target.getName() + "&e."));
			}
		} else {
			if (!args.isPlayer()) {
				sender.sendMessage(Utils.color("&e/tp <joueur> <joueur>"));
				return;
			}
			if (!JavaUtils.isInteger(args.getArgs(0)) || !JavaUtils.isInteger(args.getArgs(1))
					|| !JavaUtils.isInteger(args.getArgs(2))) {
				sender.sendMessage(Utils.color("&eCoordonées invalide."));
				return;
			}
			Player player = args.getPlayer();
			int x = Integer.parseInt(args.getArgs(0));
			int y = Integer.parseInt(args.getArgs(1));
			int z = Integer.parseInt(args.getArgs(2));
			World world = player.getWorld();
			player.teleport(new Location(world, x, y, z), TeleportCause.COMMAND);
			player.sendMessage(
					Utils.color("&eVous venez de vous TP en &cX" + x + "&7, &cY" + y + "&7, &cZ" + z + "&e."));
		}

	}

	@Command(name = "tpa", aliases = {"call"})
	public void tpaCmd(CommandArgs args) {
		Player player = args.getPlayer();

		if (args.length() < 1) {
			player.sendMessage(Utils.color("&e/tpa <joueur>"));
			return;
		}
		Player target = Bukkit.getPlayer(args.getArgs(0));
		if (target == null) {
			player.sendMessage(Utils.color("&eCe joueur n'est pas en ligne."));
			return;
		}
		Profile playerProfile = getProfile(player.getName());
		TeleportCooldown alreadyExist = TeleportModule.getInstance().getRequestFrom(playerProfile, target);
		if (alreadyExist != null) {
			if (alreadyExist.hasExpired()) {
				playerProfile.getTeleportRequests().remove(alreadyExist);
			} else {
				player.sendMessage(
						Utils.color("&eVous avez déjà envoyé une demande de téléportation auprés de ce joueur."));
				return;
			}
		}
		TeleportCooldown request = new TeleportCooldown(player.getName(), target.getName());
		playerProfile.getTeleportRequests().add(request.send());
		getProfile(target.getName()).getTeleportRequests().add(request);
	}

	@Command(name = "tpyes", aliases = "tpaccept")
	public void tpyesCmd(CommandArgs args) {
		Profile profile = getProfile(args.getPlayer().getName());
		TeleportCooldown request = TeleportModule.getInstance().getFristRequestToAccept(profile);
		if (request == null) {
			profile.msg("&eAucune demande de téléportation n'a pu étre trouver.");
			return;
		}
		if (request.hasExpired()) {
			profile.msg("&eAucune demande de téléportation n'a pu être trouver.");
			profile.getTeleportRequests().remove(request);
			return;
		}
		Profile senderProfile = getProfile(request.getSender());
		profile.getTeleportRequests().remove(request);
		senderProfile.getTeleportRequests().remove(request);

		if (senderProfile.getPlayer() == null) {
			profile.msg("&eCe joueur n'est plus en ligne !");
			return;
		}
		senderProfile
				.msg("&eLe joueur &c" + request.getReceiver() + " &evient d'accepter votre demande de téléportation.");
		profile.msg("&eVous venez d'accepter la demande de téléportation de &c" + request.getSender() + "&e.");

		TeleportModule.getInstance().handleTeleport(senderProfile.getPlayer(), profile.getPlayer());
	}

	@Command(name = "tpno", aliases = "tpdeny")
	public void tpnoCmd(CommandArgs args) {
		Profile profile = getProfile(args.getPlayer().getName());
		TeleportCooldown request = TeleportModule.getInstance().getFristRequestToAccept(profile);
		if (request == null) {
			profile.msg("&eAucune demande de téléportation n'a pu être trouver.");
			return;
		}
		if (request.hasExpired()) {
			profile.msg("&eAucune demande de téléportation n'a pu être trouver.");
			profile.getTeleportRequests().remove(request);
			return;
		}
		Profile senderProfile = getProfile(request.getSender());
		profile.msg(
				"&eVous venez de &crefuser &ela demande de téléportation du joueur &c" + request.getSender() + "&e.");
		senderProfile.msg(
				"&eLe joueur &c" + request.getReceiver() + " &evient de &crefuser &evotre demande de téléportation.");

		profile.getTeleportRequests().remove(request);
		senderProfile.getTeleportRequests().remove(request);
	}

	@Command(name = "teleporthere", aliases = {"tphere", "s"}, permission = "base.teleport")
	public void tphereCmd(CommandArgs args) {
		Profile profile = getProfile(args.getPlayer().getName());

		if (args.length() < 1) {
			profile.msg("&e/tphere <joueur>");
			return;
		}
		Player target = Bukkit.getPlayer(args.getArgs(0));
		if (target == null) {
			profile.msg("&eCe joueur est introuvable.");
			return;
		}
		target.teleport(args.getPlayer().getLocation());
		profile.msg("&eVous venez de &ctéléporté &ele joueur &6&l" + target.getName() + " &eà vous.");
	}

	@Command(name = "tpall", aliases = {"teleportall", "tphereall"}, permission = "base.tpall")
	public void tpallCmd(CommandArgs args) {
		Player player = args.getPlayer();

		for (Player pls : Utils.getOnlinePlayers()) {
			pls.teleport(player.getLocation());
		}

		player.sendMessage(
				Utils.color("&eVous venez de téléporter l'intégralité des joueurs du &cserveur &esur vous."));
	}


	@Command(name = "back", aliases = "return", permission = "base.back")
	public void backCmd(CommandArgs args) {
		Player player = args.getPlayer();
		Profile profile = getProfile(args.getPlayer().getName());

		if (profile.getLastTeleportLocation() == null) {
			profile.msg("&eAucune destination précèdente n'a pu être trouver.");
			return;
		}
		Location to = profile.getLastTeleportLocation();
		TeleportModule.getInstance().handleTeleport(player, to);
	}
	
	@Command(name = "top", permission = "base.top")
	public void topCmd(CommandArgs args) {
		Player player = args.getPlayer();
		Location top = player.getWorld().getHighestBlockAt(player.getLocation()).getLocation();
		
		player.teleport(top);
		player.sendMessage(Utils.color("&eVous venez d'être téléporté à la localisation &cla plus haute &ede votre position."));
	}
	
	@Command(name = "spawn")
	public void spawnCmd(CommandArgs args) {
		Player player = args.getPlayer();
		
		ProfileModule profileManager = ProfileModule.getInstance();
		TeleportModule teleportManager = TeleportModule.getInstance();
		
		if (profileManager.getServerProfile().getSpawn() == null) {
			player.sendMessage(Utils.color("&eAucun point de spawn trouvable."));
			return;
		}
		
		if (args.length() < 1) {
			teleportManager.handleTeleport(player, profileManager.getServerProfile().getSpawn());
		} else {
			if (!player.hasPermission("base.spawn.other")) {
				player.sendMessage(Utils.color("&cVous n'avez pas la permission d'executer cette commande."));
				return;
			}
			Player target = Bukkit.getPlayer(args.getArgs(0));
			if (target == null) {
				player.sendMessage(Utils.color("&eCe joueur est introuvable."));
				return;
			}
			target.teleport(profileManager.getServerProfile().getSpawn());
			player.sendMessage(Utils.color("&eVous venez de téléporter &c" + target.getName() + " &eau &6Spawn&e."));
			this.logCommand(args);
		}
	}
	
	@Command(name = "setspawn", permission = "op")
	public void setSpawnCmd(CommandArgs args) {
		Player player = args.getPlayer();
		ProfileModule profileManager = ProfileModule.getInstance();
		profileManager.getServerProfile().setSpawn(player.getLocation());
		player.sendMessage(Utils.color("&eVous venez de définir le point de &6Spawn&e."));
		this.logCommand(args);
	}
}

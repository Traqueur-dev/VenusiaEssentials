package fr.traqueur.venusiaessentials.commands.admin;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.api.utils.JavaUtils;
import fr.traqueur.venusiaessentials.api.utils.Utils;
import fr.traqueur.venusiaessentials.modules.BaseCommand;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;
import fr.traqueur.venusiaessentials.modules.profiles.server.clazz.Jail;

public class JailCommand extends BaseCommand {
	
private WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

	@Command(name = "jail", aliases = {"jails", "prison"}, permission = "base.jail")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
	
		ProfileModule profileModule = ProfileModule.getInstance();
		
		if (args.length() < 1) {
			player.sendMessage(Utils.LINE);
			player.sendMessage(Utils.color("&e/jail <prison> <joueur> <temps> <raison>"));
			player.sendMessage(Utils.color("&e/jail create <nom>"));
			player.sendMessage(Utils.color("&e/jail delete <nom>"));
			player.sendMessage(Utils.color("&e/jail list"));
			player.sendMessage(Utils.LINE);
		} else {
			
			if (args.length() < 4) {
				player.sendMessage(Utils.color("&e/jail <joueur> <temps> <raison>"));
				return;
			}
			Jail jail = profileModule.getServerProfile().getJail(args.getArgs(0));
			if (jail == null) {
				player.sendMessage(Utils.color("&eCette prison n'existe pas !"));
				return;
			}
			Player target = Bukkit.getPlayer(args.getArgs(1));
			if (target == null) {
				player.sendMessage(Utils.color("&cCe joueur est introuvable."));
				return;
			}
			long time = JavaUtils.parse(args.getArgs(2));
			if (time == -1L) {
				player.sendMessage(Utils.color("&eTemps invalide."));
				return;
			}
			String reason = StringUtils.join(args.getArgs(), ' ', 3, args.length());
			jail.jailPlayer(target, time, reason);
			player.sendMessage(Utils.color("&eVous venez d'emprisonner le joueur &c" + target.getName() + " &epour &c" + args.getArgs(2) 
			+ " &eraison &f" + reason + "&e."));
			this.logCommand(args);
		}
	}

	@Command(name = "jail.create", aliases = {"jails.create", "prison.create"}, permission = "base.jail.edit")
	public void jailCreateCommand(CommandArgs args) {
		Player player = args.getPlayer();
		
		ProfileModule profileModule = ProfileModule.getInstance();
		
		if (args.length() < 1) {
			player.sendMessage(Utils.color("&e/jail create <nom>"));
			return;
		}
		String name = args.getArgs(0);
		if (profileModule.getServerProfile().getJail(name) != null) {
			player.sendMessage(Utils.color("&eUne prison ayant le nom de &c" + name + " &eexiste déjà."));
			return;
		}
		Selection sel = worldEdit.getSelection(player);
		if (sel == null) {
			player.sendMessage(Utils.color("&eVeuillez effectuer une selection en utilisant WorldEdit."));
			return;
		}
		Jail jail = new Jail(name, sel);
		profileModule.getServerProfile().getJails().add(jail);
		player.sendMessage(Utils.color("&eVous venez de créer la prison &c" + name + "&e."));
	}
	
	@Command(name = "jail.delete", aliases = {"jails.delete", "prison.delete"}, permission = "base.jail.edit")
	public void jailDeleteCommand(CommandArgs args) {
		Player player = args.getPlayer();
		
		ProfileModule profileModule = ProfileModule.getInstance();
		
		if (args.length() < 1) {
			player.sendMessage(Utils.color("&e/jail delete <nom>"));
			return;
		}
		Jail jail = profileModule.getServerProfile().getJail(args.getArgs(0));
		if (jail == null) {
			player.sendMessage(Utils.color("&eCette prison n'existe pas !"));
			return;
		}
		
		// on libére les joueurs emprisonnés avant de supprimer la prison
		for (String name : jail.getJailedPlayers().keySet()) {
			Profile profile = profileModule.getProfile(name);
			if (profile != null) {
				profile.setJail(false);
				if (profile.getPlayer() != null) {
					profile.getPlayer().teleport(profileModule.getServerProfile().getSpawn());
					profile.msg("&eVous n'êtes maintenant plus en prison.");
				}
			}
		}
		
		profileModule.getServerProfile().getJails().remove(jail);
		player.sendMessage(Utils.color("&eVous venez de supprimer la prison &c" + jail.getName() + "&e."));
	}
	
	@Command(name = "jail.list", aliases = {"jails.list", "prison.list"}, permission = "base.jail")
	public void jailListCommand(CommandArgs args) {
		Player player = args.getPlayer();

		ProfileModule profileModule = ProfileModule.getInstance();
		
		if (profileModule.getServerProfile().getJails().isEmpty()) {
			player.sendMessage(Utils.color("&eAucune prison de disponible."));
			return;
		}
		
		for (Jail jail : profileModule.getServerProfile().getJails()) {
			player.sendMessage(Utils.color("&6- &e" + jail.getName()));
		}
	}
}
